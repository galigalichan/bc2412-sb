package com.bootcamp.bc_xfin_web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.bc_xfin_web.config.StocksProperties;

@Controller
public class ViewController {
  private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
  private final StocksProperties stocksProperties;

  public ViewController(StocksProperties stocksProperties) {
    this.stocksProperties = stocksProperties;
  }

  @GetMapping("/linechart/{symbol}")
  public String linechart(@PathVariable String symbol, Model model) {
    logger.info("Rendering linechart page for symbol: {}", symbol);
    model.addAttribute("stockSymbol", symbol);
    return "linechart";
  }

  @GetMapping("/candlechart/{symbol}")
  public String candlechart(@PathVariable String symbol,
                            @RequestParam(name = "interval", defaultValue = "1d") String interval,
                            Model model) {
    logger.info("Rendering candlestick chart page for symbol: {}, interval: {}", symbol, interval);
    model.addAttribute("stockSymbol", symbol);
    model.addAttribute("interval", interval);
    return "candlechart";
  }

  // Redirect to default symbol from config
  @GetMapping("/linechart")
  public String redirectLinechart() {
      return "redirect:/linechart/" + stocksProperties.getDefaultSymbol();
  }

  @GetMapping("/candlechart")
  public String redirectCandlechart() {
      return "redirect:/candlechart/" + stocksProperties.getDefaultSymbol();
  }
}
