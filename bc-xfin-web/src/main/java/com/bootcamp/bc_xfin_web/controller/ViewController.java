package com.bootcamp.bc_xfin_web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {
  private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

  @GetMapping("/linechart/{symbol}")
  public String linechart(@PathVariable String symbol, Model model) {
    logger.info("Rendering linechart page for symbol: {}", symbol);
    model.addAttribute("stockSymbol", symbol);
    return "linechart";
  }

  @GetMapping("/candlechart")
  public String candlechart(Model model) {
    return "candlechart";
  }
}
