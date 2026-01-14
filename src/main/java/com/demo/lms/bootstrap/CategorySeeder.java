package com.demo.lms.bootstrap;

import com.demo.lms.model.entity.BookCategory;
import com.demo.lms.repository.BookCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategorySeeder implements ApplicationRunner {

    private final BookCategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) {
        String[] defaults = new String[]{
                "Fiction", "Non-Fiction", "Science", "Technology", "History",
                "Biography", "Educational"
        };

        int created = 0;
        for (String name : defaults) {
            if (!categoryRepository.existsByName(name)) {
                BookCategory c = new BookCategory();
                c.setName(name);
                c.setDescription(null);
                categoryRepository.save(c);
                created++;
            }
        }
        if (created > 0) {
            log.info("Seeded {} default book categories", created);
        }
    }
}
