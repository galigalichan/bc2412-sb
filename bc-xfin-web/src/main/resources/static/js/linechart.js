document.addEventListener("DOMContentLoaded", function () {
  const chart = LightweightCharts.createChart(
    document.getElementById("chart-container"),
    {
      width: 1000,
      height: 600,
      layout: {
        background: { type: "solid", color: "#000000" }, // Force black background
        textColor: "#ffffff", // White text for contrast
      },
      grid: {
        vertLines: { color: "#1f1f1f" }, // Dark grid
        horzLines: { color: "#1f1f1f" },
      },
      priceScale: {
        borderColor: "#7b5353",
      },
      timeScale: {
        borderColor: "#7b5353",
        timeVisible: true, // Enable time visibility
        secondsVisible: false,
        tickMarkFormatter: (time) => {
          const date = new Date(time * 1000); // Convert Unix timestamp to milliseconds
          date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480); // Convert to GMT+8
          const hours = date.getHours().toString().padStart(2, "0");
          const minutes = date.getMinutes().toString().padStart(2, "0");
          return `${hours}:${minutes}`; // Show time in HH:mm format
        },
      },
      crosshair: {
        mode: LightweightCharts.CrosshairMode.Normal, // Keep crosshair active
        vertLine: {
          color: '#aaaaaa',
          width: 1,
          style: LightweightCharts.LineStyle.Dashed,
          visible: true,
        },
        horzLine: {
          color: '#aaaaaa',
          width: 1,
          style: LightweightCharts.LineStyle.Dashed,
          visible: true,
        },
      },
    }
  );

  // Add line series for stock prices
  const stockPriceSeries = chart.addLineSeries({
    color: "#26a69a", // Green for stock prices
    lineWidth: 1.5,
    priceLineVisible: false,  // This removes the horizontal dotted line
    lastValueVisible: true,   // Keeps the price tag
  });

  // Add line series for moving averages
  const movingAverageSeries = chart.addLineSeries({
    color: "#ff9800", // Orange for moving average
    lineWidth: 1.5,
    lineStyle: LightweightCharts.LineStyle.Dotted, // Optional: Dotted line for differentiation
    priceLineVisible: false,  // This removes the horizontal dotted line
    lastValueVisible: true,   // Keeps the price tag
  });

  chart.applyOptions({
    timeScale: {
      barSpacing: 20, // Adjust spacing
      timeVisible: true,
      secondsVisible: false,
      tickMarkFormatter: (time) => {
        const date = new Date(time * 1000); // Convert to Date
        date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480); // Convert to GMT+8
        const hours = date.getHours().toString().padStart(2, "0");
        const minutes = date.getMinutes().toString().padStart(2, "0");
        return `${hours}:${minutes}`;
      },
    },
    localization: {
      dateFormat: "yyyy-MM-dd",
      timeFormatter: (time) => {
        const date = new Date(time * 1000);
        date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480); // Convert to GMT+8
        return `${date.getFullYear()}-${(date.getMonth() + 1)
          .toString()
          .padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")} ${
          date.getHours().toString().padStart(2, "0")
        }:${date.getMinutes().toString().padStart(2, "0")}`;
      },
    },
    priceFormat: {
      type: "price",
      precision: 2,
      minMove: 0.01,
    },
    crosshair: {
      // Remove default x-axis & y-axis tooltips
      horzLine: {
        labelVisible: false, 
      },
      vertLine: {
        labelVisible: false, 
      },
    },
  });

  // Tooltip handling
  chart.subscribeCrosshairMove((param) => {
    const tooltip = document.getElementById("tooltip");

    if (!param || !param.time) {
      tooltip.style.display = "none";
      return;
    }

    const timestamp = param.time; // This is in seconds (Unix time)
    const date = new Date(timestamp * 1000); // Convert to Date object
    date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480); // Convert to GMT+8

    const formattedTime = `${date.getFullYear()}-${(date.getMonth() + 1)
      .toString()
      .padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")} ${
      date.getHours().toString().padStart(2, "0")
    }:${date.getMinutes().toString().padStart(2, "0")}`;

    // Get prices from both series
    const stockPrice = param.seriesData.get(stockPriceSeries)?.value || "N/A";
    const movingAvg = param.seriesData.get(movingAverageSeries)?.value || "N/A";

    // console.log("Tooltip Time (GMT+8):", formattedTime, "Price:", price);

    // Set tooltip text with both values
    tooltip.innerHTML = `${formattedTime}<br>
                         Price: <span style="color:#26a69a">${stockPrice.toFixed(2)}</span><br>
                         SMA(5): <span style="color:#ff9800">${movingAvg.toFixed(2)}</span>`;
    tooltip.style.display = "block";

    if (param.point) {
      tooltip.style.left = `${param.point.x + 10}px`;
      tooltip.style.top = `${param.point.y - 30}px`;
    }
  });

  // Fetch stock data
  async function fetchStockData(symbol) {
    try {
      const params = new URLSearchParams({ symbol: symbol, interval: "5m" });
      const response = await fetch(`/v1/chart/line?${params.toString()}`);
      const data = await response.json();
    
      console.log("Raw Data from API:", data);

      // Transform stock prices
      const stockPrices = data
        .filter(entry => entry.close != null)
        .map(entry => ({
            time: Math.floor(entry.dateTime), // Keep in seconds if chart expects seconds
            value: entry.close}))
        .sort((a, b) => a.time - b.time);

      // Transform moving averages
      const movingAverages = data
          .filter(entry => entry.movingAverage != null)
          .map(entry => ({
              time: Math.floor(entry.dateTime), 
              value: entry.movingAverage}))
          .sort((a, b) => a.time - b.time);

      // Validate data before updating chart
      const isValidStockData = stockPrices.every(entry => typeof entry.time === 'number' && typeof entry.value === 'number');
      const isValidMovingAvg = movingAverages.every(entry => typeof entry.time === 'number' && typeof entry.value === 'number');

      if (!isValidStockData || !isValidMovingAvg) {
          console.error("Invalid stock or moving average data found!", stockPrices, movingAverages);
          return;
      }

      console.log("Transformed Stock Prices:", stockPrices);
      console.log("Transformed Moving Averages:", movingAverages);

      // Set data separately for each series
      stockPriceSeries.setData(stockPrices);
      movingAverageSeries.setData(movingAverages);

      chart.timeScale().fitContent();
    } catch (error) {
      console.error("Error fetching stock data:", error);
    }
  }

  // Extract stock symbol from URL
  function getStockSymbolFromUrl() {
    const pathSegments = window.location.pathname.split("/");
    return pathSegments[pathSegments.length - 1]; // Get last segment
  }

  // Fetch stock symbol dynamically
  const stockSymbol = getStockSymbolFromUrl();

  // Debugging: Check if symbol is correctly extracted
  console.log("Extracted stock symbol:", stockSymbol);

  // Fetch data dynamically based on the stock symbol
  fetchStockData(stockSymbol);

  // Refresh stock data every 10 seconds
  setInterval(() => fetchStockData(stockSymbol), 10000);
});