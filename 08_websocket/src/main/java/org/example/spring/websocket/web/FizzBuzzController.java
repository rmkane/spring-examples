package org.example.spring.websocket.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FizzBuzzController {

    @GetMapping("/fizzbuzz")
    public String fizzbuzz(Model model) {
        model.addAttribute("title", "FizzBuzz WebSocket Demo");
        model.addAttribute("description", "Real-time FizzBuzz messages via WebSocket");
        return "fizzbuzz";
    }
}
