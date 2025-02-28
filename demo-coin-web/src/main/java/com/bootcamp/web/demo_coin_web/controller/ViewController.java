package com.bootcamp.web.demo_coin_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bootcamp.web.demo_coin_web.model.dto.CoinGeckoMarketDto;
import com.bootcamp.web.demo_coin_web.service.CoinService;

// @RestController // return JSON as response
@Controller // return HTML
public class ViewController {
    @Autowired
    private CoinService coinService;
    
    @GetMapping(value = "/bootcamp")
    public String sayHelloPage(Model model) { // pass by reference
        // select from DB, return list
        // put the list into model
        // closed-loop: static when waiting for user's response
        // user presses another button -> server generates another html
        // html cannot use Maven
        // Java gets Spring Boot through Maven
        // Angular JS (safer libraries)
        // React JS (famous for cross-platform)
        model.addAttribute("tutor", "vincent");
        return "hello"; // html file name
    }
    // price change 24h 3.23% (Green)
    // price change 24h -1.23% (Red)
    @GetMapping(value = "/coins")
    public String coinPage(Model model) {
        List<CoinGeckoMarketDto> dtos = this.coinService.getCoinMarket();
        model.addAttribute("coinList", dtos);
        return "coin"; // html file coins
    }
}
