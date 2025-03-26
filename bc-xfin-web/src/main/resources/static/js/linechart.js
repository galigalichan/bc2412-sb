document.addEventListener("DOMContentLoaded", function () {
  const chart = LightweightCharts.createChart(
    document.getElementById("chart-container"),
    {
      width: 1000,
      height: 600,
      layout: {
        background: { type: "solid", color: "#000000" }, // Black background
        textColor: "#ffffff", // White text
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
        timeVisible: true,
        secondsVisible: false,
      },
      crosshair: {
        mode: LightweightCharts.CrosshairMode.Normal,
        vertLine: { color: '#aaaaaa', width: 1, style: LightweightCharts.LineStyle.Dashed, visible: true },
        horzLine: { color: '#aaaaaa', width: 1, style: LightweightCharts.LineStyle.Dashed, visible: true },
      },
    }
  );

  // Stock price series
  const priceSeries = chart.addLineSeries({
    color: "#4CAF50", // Green for stock price
    lineWidth: 2,
  });

  // Moving average series
  const maSeries = chart.addLineSeries({
    color: "#FF5733", // Orange for moving average
    lineWidth: 2,
    lineStyle: LightweightCharts.LineStyle.Dotted,
  });

  chart.applyOptions({
    timeScale: {
      barSpacing: 20,
      timeVisible: true,
      secondsVisible: false,
      tickMarkFormatter: (time) => {
        const date = new Date(time * 1000);
        date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480);
        return `${date.getHours().toString().padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
      },
    },
    localization: {
      dateFormat: "yyyy-MM-dd",
      timeFormatter: (time) => {
        const date = new Date(time * 1000);
        date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480);
        return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")} ${date.getHours().toString().padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
      },
    },
  });

  // Fetch stock data
  async function fetchStockData(symbol) {
    try {
        const params = new URLSearchParams({ symbol: symbol, interval: "5m" });
        const response = await fetch(`/v1/chart/line?${params.toString()}`);
        const data = await response.json();

        console.log("Raw Data from API:", data);
        console.log("Raw Moving Averages from API:", data.movingAverages);

        // Handle case where API returns an array directly
        if (!data.stockData || !Array.isArray(data.stockData) || !Array.isArray(data.movingAverages)) {
          console.error("Invalid stock data format:", data);
          return;
        }

        console.log("Data structure check:", {
          hasStockData: !!data.stockData,
          isStockDataArray: Array.isArray(data.stockData),
          hasMovingAverages: !!data.movingAverages,
          isMovingAveragesArray: Array.isArray(data.movingAverages)
        });

        // Transform stock prices
        const stockPrices = data
            .filter(entry => entry.close != null)
            .map(entry => ({
                time: Math.floor(entry.dateTime), // Keep in seconds if chart expects seconds
                value: entry.close
            }));

        // Transform moving averages (handle missing array)
        const movingAverages = (data.movingAverages || [])
            .filter(entry => entry.regularMarketPrice != null)
            .map(entry => ({
                time: Math.floor(entry.regularMarketTime), 
                value: entry.regularMarketPrice
            }));

        // Validate data before updating chart
        const isValidStockData = stockPrices.every(entry => typeof entry.time === 'number' && typeof entry.value === 'number');
        const isValidMovingAvg = movingAverages.every(entry => typeof entry.time === 'number' && typeof entry.value === 'number');

        if (!isValidStockData || !isValidMovingAvg) {
            console.error("❌ Invalid stock or moving average data found!", stockPrices, movingAverages);
            return;
        }

        console.log("✅ Transformed Stock Prices:", stockPrices);
        console.log("✅ Transformed Moving Averages:", movingAverages);

        updateChart(stockPrices, movingAverages);
    } catch (error) {
        console.error("🔥 Error fetching stock data:", error);
    }
  }

  // Update the chart with stock prices and moving average
  function updateChart(stockPrices, movingAverage) {
    if (!stockPrices || stockPrices.length === 0) {
      console.error("⚠️ Stock prices array is empty or undefined!");
      return;
    }
  
    console.log("📊 Raw Stock Prices Data before filtering:", stockPrices);
    console.log("📈 Raw Moving Average Data before filtering:", movingAverage);
  
    // 🔍 Remove any entries where value or time is null or undefined
    const filteredStockPrices = stockPrices.filter(entry => entry?.value != null && entry?.time != null);
    const filteredMovingAverage = movingAverage.filter(entry => entry?.value != null && entry?.time != null);
  
    console.log("✅ Filtered Stock Prices Data:", JSON.stringify(filteredStockPrices, null, 2));
    console.log("✅ Filtered Moving Average Data:", JSON.stringify(filteredMovingAverage, null, 2));
  
    // 🚨 Double-check for missing or malformed entries
    const invalidStockEntries = filteredStockPrices.filter(entry => typeof entry.value !== 'number' || typeof entry.time !== 'number');
    const invalidMovingAvgEntries = filteredMovingAverage.filter(entry => typeof entry.value !== 'number' || typeof entry.time !== 'number');
  
    if (invalidStockEntries.length > 0) {
      console.error("❌ Invalid stock price entries detected:", invalidStockEntries);
    }
  
    if (invalidMovingAvgEntries.length > 0) {
      console.error("❌ Invalid moving average entries detected:", invalidMovingAvgEntries);
    }
  
    if (filteredStockPrices.length === 0) {
      console.error("❌ No valid stock price data after filtering!");
      return;
    }
  
    if (filteredMovingAverage.length === 0) {
      console.error("❌ No valid moving average data after filtering!");
      return;
    }
  
    try {
      console.log("📌 Setting data on price series...");
      if (!filteredStockPrices || filteredStockPrices.length === 0) {
        console.error("Stock price data is empty or null.");
      } else {
        filteredStockPrices.sort((a, b) => a.time - b.time);
        priceSeries.setData(filteredStockPrices);
      }
      console.log("📌 Setting data on moving average series...");
      if (!filteredMovingAverage || filteredMovingAverage.length === 0) {
        console.error("Moving average data is empty or null.");
      } else {
        filteredMovingAverage.sort((a, b) => a.time - b.time);
        maSeries.setData(filteredMovingAverage);
      }
    } catch (error) {
      console.error("🔥 Error in setData:", error);
    }
  
    chart.timeScale().fitContent();
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