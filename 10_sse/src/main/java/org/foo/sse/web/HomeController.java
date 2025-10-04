package org.foo.sse.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/sse";
    }

    @GetMapping("/sse")
    public String sse(Model model) {
        model.addAttribute("title", "Server-Sent Events (SSE) Demo");
        model.addAttribute("description", "Real-time server-to-client communication via SSE");
        return "sse";
    }
}
