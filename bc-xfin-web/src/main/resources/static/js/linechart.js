document.addEventListener("DOMContentLoaded", function () {
  // ============ Chart Setup ============
  const chartContainer = document.getElementById("chart-container");
  let chart = null, stockPriceSeries = null, movingAverageSeries = null;

  function createChart() {
    if (chart) {
      chart.remove();
    }
    chart = LightweightCharts.createChart(chartContainer, {
      width: chartContainer.clientWidth,
      height: chartContainer.clientHeight,
      layout: {
        background: { type: "solid", color: "#000" },
        textColor: "#fff",
      },
      grid: {
        vertLines: { color: "#1f1f1f" },
        horzLines: { color: "#1f1f1f" },
      },
      priceScale: { borderColor: "#7b5353" },
      timeScale: {
        borderColor: "#7b5353",
        timeVisible: true,
        secondsVisible: false,
        tickMarkFormatter: gmt8TimeFormatter,
      },
      crosshair: {
        mode: LightweightCharts.CrosshairMode.Normal,
        vertLine: { color: '#aaaaaa', width: 1, style: LightweightCharts.LineStyle.Dashed, visible: true },
        horzLine: { color: '#aaaaaa', width: 1, style: LightweightCharts.LineStyle.Dashed, visible: true },
      },
    });
    stockPriceSeries = chart.addLineSeries({
      color: "#26a69a", lineWidth: 1.5, priceLineVisible: false, lastValueVisible: true
    });
    movingAverageSeries = chart.addLineSeries({
      color: "#ff9800", lineWidth: 1.5, lineStyle: LightweightCharts.LineStyle.Dotted,
      priceLineVisible: false, lastValueVisible: true
    });
    chart.applyOptions({
      crosshair: {
        horzLine: { labelVisible: false },
        vertLine: { labelVisible: false },
      }
    });

    // Responsive resize
    window.addEventListener('resize', debounce(function() {
      chart.applyOptions({
        width: chartContainer.clientWidth,
        height: chartContainer.clientHeight,
      });
    }, 100));
  }
  createChart();

  // ============ Symbol Handling ============
  const symbolSelect = document.getElementById("symbol");

  function getStockSymbolFromUrl() {
    const pathSegments = window.location.pathname.split("/");
    return pathSegments[pathSegments.length - 1]; // last segment
  }

  function loadSymbols() {
    fetch("http://ec2-54-176-81-104.us-west-1.compute.amazonaws.com:8101/api/stocklist")
      .then((res) => res.json())
      .then((data) => {
        const symbols = data["STOCK-LIST"];
        const urlSymbol = getStockSymbolFromUrl();
        symbolSelect.innerHTML = ""; // Clear existing options

        // Populate the dropdown
        symbols.forEach((symbol) => {
          const option = document.createElement("option");
          option.value = symbol;
          option.textContent = symbol;
          symbolSelect.appendChild(option);
        });

        if (!symbols.includes(urlSymbol)) {
          showError(`Invalid stock symbol: ${urlSymbol}`);
          return;
        }

        // Valid symbol — set dropdown, load chart, and update URL
        symbolSelect.value = urlSymbol;
        loadChart(urlSymbol);
        history.replaceState({}, '', `/linechart/${urlSymbol}`);
      })
      .catch((error) => {
        showErrorMessage("❌ Failed to load stock list.");
        console.error("Error loading symbols:", error);
      });
  }

  // ============ Data Fetch & Render ============
  async function fetchStockData(symbol) {
    try {
      const params = new URLSearchParams({ symbol, interval: "5m" });
      const response = await fetch(`/v1/chart/line?${params.toString()}`);

      // If response is 4xx or 5xx, throw an error
      if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

      const data = await response.json();

      if (!data || data.length === 0) {
        showError(`No data available for "${symbol}".`);
        return;
      }

      dismissError(); // clear previous errors if all good

      // Transform stock prices
      const stockPrices = data
        .filter(entry => entry.close != null)
        .map(entry => ({
          time: Math.floor(entry.dateTime), // Keep in seconds if chart expects seconds
          value: entry.close,
        }))
        .sort((a, b) => a.time - b.time);

      // Transform moving averages (if available)
      const movingAverages = data
        .filter(entry => entry.movingAverage != null)
        .map(entry => ({
          time: Math.floor(entry.dateTime),
          value: entry.movingAverage,
        }))
        .sort((a, b) => a.time - b.time);

      if (stockPrices.length === 0) {
        showError(`Stock data for "${symbol}" is invalid.`);
        return;
      }

      // Set data separately for each series
      stockPriceSeries.setData(stockPrices);

      // Only update moving average series if valid data is available
      if (movingAverages.length > 0) {
        movingAverageSeries.setData(movingAverages);
      } else {
        movingAverageSeries.setData([]); // Clear if no MA
      }

      chart.timeScale().fitContent();
    } catch (error) {
      showError("Error fetching stock data.");
      console.error("Error fetching stock data:", error);
    }
  }

  // ============ Chart Loader ============
  function loadChart(symbol) {
    fetchStockData(symbol);
  }

  // ============ Tooltip Handling ============
  chart.subscribeCrosshairMove((param) => {
    const tooltip = document.getElementById("tooltip");
    if (!param || !param.time) {
      tooltip.style.display = "none";
      return;
    }
    const time = param.time;
    const formattedTime = gmt8DateTimeFormatter(time);

    const stockData = param.seriesData.get(stockPriceSeries);
    const maData = param.seriesData.get(movingAverageSeries);

    const stockPrice = stockData?.value?.toFixed(2) ?? "N/A";
    const movingAvg = maData?.value?.toFixed(2) ?? "N/A";

    tooltip.innerHTML = `${formattedTime}<br>
      <b>Price:</b> <span style="color:#26a69a">${stockPrice}</span><br>
      <b>SMA(5):</b> <span style="color:#ff9800">${movingAvg}</span>`;
    tooltip.style.display = "block";

    if (param.point) {
      // Offset: 16px right, 10px up for mobile/desktop usability
      let left = param.point.x + 16;
      let top = param.point.y - 10;
      // Prevent tooltip overflow
      const containerRect = chartContainer.getBoundingClientRect();
      const tooltipRect = tooltip.getBoundingClientRect();
      // Right edge
      if (left + tooltipRect.width > containerRect.width) {
        left = containerRect.width - tooltipRect.width - 6;
      }
      // Top edge
      if (top < 0) top = 2;
      // Bottom edge
      if (top + tooltipRect.height > containerRect.height) {
        top = containerRect.height - tooltipRect.height - 6;
      }
      tooltip.style.left = left + "px";
      tooltip.style.top = top + "px";
    }
  });

  // ============ Form Submission ============
  document.getElementById("chart-options").addEventListener("submit", function(e) {
    e.preventDefault();
    const stockSymbol = symbolSelect.value;
    loadChart(stockSymbol);
    history.pushState({}, '', `/linechart/${stockSymbol}`);
  });

  // ============ URL/Navigation Handling ============
  window.addEventListener("popstate", () => {
    const symbol = getStockSymbolFromUrl();
    symbolSelect.value = symbol; // Update dropdown
    loadChart(symbol); // Load new chart
  });

  // Auto-refresh every 10 seconds
  setInterval(() => {
    const currentSymbol = getStockSymbolFromUrl();
    fetchStockData(currentSymbol);
  }, 10000);

  // ============ Error Modal ============
  function showError(message) {
    const modal = document.getElementById("error-modal");
    const text = document.getElementById("error-text");
    text.textContent = message;
    modal.style.display = "block";
  }
  function dismissError() {
    const modal = document.getElementById("error-modal");
    modal.style.display = "none";
  }
  window.dismissError = dismissError;
  document.getElementById("error-modal").addEventListener("click", (e) => {
    if (e.target.classList.contains("modal")) dismissError();
  });
  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") dismissError();
  });
  function showErrorMessage(message) {
    showError(message);
  }

  // ============ Helper Functions ============
  function gmt8TimeFormatter(unixTime) {
    const date = new Date(unixTime * 1000);
    date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480);
    return `${date.getHours().toString().padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
  }
  function gmt8DateTimeFormatter(unixTime) {
    const date = new Date(unixTime * 1000);
    date.setMinutes(date.getMinutes() + date.getTimezoneOffset() + 480);
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, "0")}-${date
      .getDate().toString().padStart(2, "0")} ${date.getHours().toString().padStart(2, "0")}:${date
      .getMinutes().toString().padStart(2, "0")}`;
  }
  // Debounce function for resize
  function debounce(fn, ms) {
    let t;
    return function(...args) {
      clearTimeout(t);
      t = setTimeout(() => fn.apply(this, args), ms);
    };
  }

  // ============ Initial Load ============
  loadSymbols();
});