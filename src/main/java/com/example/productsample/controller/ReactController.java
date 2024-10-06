package com.example.productsample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactController {
    @RequestMapping(value = {
            "/lowest-price"
            , "/lowest-price/brand"
            , "/price-range"})
    public String index() {
        return "forward:/index.html";
    }
}
