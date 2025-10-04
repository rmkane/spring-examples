package org.foo.scheduling.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web controller for the scheduling example
 */
@Controller
public class HomeController {

    /**
     * Redirect root to scheduling page
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/scheduling";
    }

    /**
     * Serve the scheduling demo page
     */
    @GetMapping("/scheduling")
    public String scheduling() {
        return "scheduling";
    }
}
