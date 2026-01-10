package com.demo.lms.controller.doc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling settings and configuration views
 */
@Controller
@RequestMapping("/createSetting")
@RequiredArgsConstructor
@Slf4j
public class SettingViewController {

    /**
     * Display the settings page
     */
    @GetMapping
    public String showSettingsPage(Model model) {
        log.info("Displaying settings page");
        
        // Add system information
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("osName", System.getProperty("os.name"));
        model.addAttribute("osVersion", System.getProperty("os.version"));
        
        return "createSetting";
    }
}
