 Image Upload Debugging Guide

## Issue
Images uploaded through the Product Management form are not displaying on:
1. Product Management table
2. Borrow Books page

## Root Cause Analysis

### What We Found
1. ✅ Form has correct `enctype="multipart/form-data"`
2. ✅ Controller accepts `MultipartFile imageFile`
3. ✅ `uploads` folder exists
4. ❌ **uploads folder is EMPTY** - files are not being saved
5. ❌ Images showing default emoji icon instead of uploaded images

### Possible Causes
1. **File not being sent**: JavaScript or browser issue
2. **Multipart config issue**: Spring Boot not parsing multipart data
3. **File size limit**: File exceeds configured limit
4. **Permissions**: Cannot write to uploads directory

## Debugging Steps

### Step 1: Check Logs After Upload
After restarting the app and creating a product with an image, check:

```powershell
Get-Content logs/lms.log -Tail 30
```

Look for these log messages:
- `Image upload check - imageFile: <filename>, isEmpty: false`
- `Upload directory path: <path>`
- `Saving file to: <path>`
- `File saved successfully. URL: /uploads/<filename>`

### Step 2: Test Manual Upload
Try this simplified test:

1. Open browser DevTools (F12)
2. Go to Network tab
3. Create a product with an image
4. Check the request:
   - Verify `Content-Type: multipart/form-data` in headers
   - Verify file is in the request payload

### Step 3: Check Multipart Configuration
Verify in `application.properties`:
```properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

### Step 4: Check File Upload Working Directory
The app saves to relative path `uploads/`. Verify:
```powershell
# Check current directory
Get-Location

# List uploads
ls uploads
```

## Quick Fix Options

### Option 1: Use Absolute Path
Change controller to use absolute path:
```java
private static final String UPLOAD_DIR = System.getProperty("user.home") + "/lms-uploads";
```

### Option 2: Add Image Preview
Add client-side preview to verify file is selected:
```javascript
imageFileInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file) {
        console.log('File selected:', file.name, file.size);
    }
});
```

### Option 3: Debug Multipart Resolver
Add this bean to verify multipart is working:
```java
@Bean
public MultipartResolver multipartResolver() {
    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    resolver.setMaxUploadSize(52428800); // 50MB
    return resolver;
}
```

## Testing

### Test 1: Console Logging
Add to browser console:
```javascript
// Check if file input exists
console.log('imageFile input:', document.getElementById('imageFile'));

// Monitor form submission
document.getElementById('createProductForm').addEventListener('submit', (e) => {
    const formData = new FormData(e.target);
    console.log('Form data entries:');
    for (let [key, value] of formData.entries()) {
        console.log(key, value);
    }
});
```

### Test 2: Direct API Test
Use curl to test upload directly:
```powershell
curl -X POST http://localhost:8080/createProduct `
  -F "title=Test Book" `
  -F "author=Test Author" `
  -F "isbn=TEST123" `
  -F "imageFile=@path/to/test/image.jpg"
```

## Expected Behavior
1. File should be saved to `uploads/<UUID>_<filename>`
2. Database should store `/uploads/<UUID>_<filename>` in `image_url` column
3. Images should display using `<img src="/uploads/<UUID>_<filename>">`
4. Resource handler should serve from `file:uploads/`

## Next Steps
1. Restart app and try creating a product with an image
2. Check the logs immediately after
3. Share the log output to diagnose the exact issue
