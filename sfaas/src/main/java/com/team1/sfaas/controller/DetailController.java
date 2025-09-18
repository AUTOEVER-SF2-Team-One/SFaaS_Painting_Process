package com.team1.sfaas.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
//@RequestMapping("/api/v1")
public class DetailController {

    @GetMapping("/test")
    public String test() {
        return "hello spring";
    }
}