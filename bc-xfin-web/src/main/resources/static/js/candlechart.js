document.addEventListener("DOMContentLoaded", function () {
  // Utility Functions
  function getStockSymbolFromUrl() {
    const pathParts = window.location.pathname.split("/");
    return pathParts.length >= 3 ? decodeURIComponent(pathParts[2]) : "0388.HK";
  }

  function getIntervalFromUrl() { return "1d"; }
  function updateUrl(symbol, interval) {
    history.replaceState({}, "", `/candlechart/${encodeURIComponent(symbol)}`);
  }

  function updateDropdowns(symbol, interval) {
    symbolSelect.value = symbol;
    intervalSelect.value = interval;
  }

  function formatHKDate(timestamp) {
    // Daily: "DD/MM/YYYY" in Hong Kong Time
    return new Date(timestamp * 1000).toLocaleDateString("en-GB", {
      timeZone: "Asia/Hong_Kong"
    });
  }

  function getISOWeekNumber(date) {
    // date: JS Date object in HK time
    // Copy date so we don't modify original
    const d = new Date(Date.UTC(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate()));
    // Set to nearest Thursday: current date + 4 - current day number
    d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay() || 7));
    // Get first day of year
    const yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
    // Calculate week number
    const weekNo = Math.ceil((((d - yearStart) / 86400000) + 1) / 7);
    return weekNo;
  }

  function formatCombinedHKWeek(timestamp) {
    // Convert to JS Date in HK time zone for the week START
    const options = { timeZone: "Asia/Hong_Kong" };
    const startDate = new Date(timestamp * 1000);
    // Get start day/month/year
    const [startDay, startMonth, startYear] = startDate.toLocaleDateString("en-GB", options).split('/');
    // Week end date (+6 days)
    const endDate = new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000);
    const [endDay, endMonth] = endDate.toLocaleDateString("en-GB", options).split('/');
  
    // Week number (ISO, using HK time)
    // To ensure week number is in HK, construct HK date string
    const hkDate = new Date(`${startYear}-${startMonth}-${startDay}T00:00:00+08:00`);
    const weekNum = getISOWeekNumber(hkDate);
  
    // Example: "Week 16, 2025 (15/04–21/04)"
    return `Week ${weekNum}, ${startYear} (${startDay}/${startMonth}-${endDay}/${endMonth})`;
  }

  function formatHKMonth(timestamp) {
    // Monthly: "MM/YYYY" in Hong Kong Time
    const date = new Date(timestamp * 1000);
    const month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
    const year = date.getUTCFullYear();
    return `${month}/${year}`;
  }

  function getFormatterByInterval(interval) {
    interval = interval.toLowerCase();
    if (["monthly", "1m", "month"].includes(interval)) {
      return formatHKMonth;
    } else if (["weekly", "1w", "week"].includes(interval)) {
      return formatCombinedHKWeek;
    }
    return formatHKDate;
  }

  function formatNumber(num) {
    if (num == null) return "-";
    if (Math.abs(num) >= 1e6) return (num / 1e6).toFixed(2) + "M";
    if (Math.abs(num) >= 1e3) return (num / 1e3).toFixed(2) + "K";
    return num.toLocaleString('en-US', { maximumFractionDigits: 2 });
  }

  // DOM Elements
  const chartContainer = document.getElementById("chart-container");
  const symbolSelect = document.getElementById("symbol-select");
  const intervalSelect = document.getElementById("interval-select");
  const errorModal = document.getElementById("error-modal");
  const errorModalCloseBtn = document.getElementById("errorModalCloseBtn");
  const errorText = document.getElementById("error-text");
  const smaToggleContainer = document.getElementById("sma-toggle-container");
  const smaResetBtn = document.getElementById("sma-reset");
  const smaToggleBtn = document.getElementById("sma-toggle");

  // Tooltip element
  let tooltip = document.getElementById("tooltip");
  if (!tooltip) {
    tooltip = document.createElement("div");
    tooltip.id = "tooltip";
    tooltip.style.display = "none";
    tooltip.style.position = "absolute";
    tooltip.style.zIndex = "10";
    chartContainer.appendChild(tooltip);
  }

  // Error Modal
  function dismissError() { if (errorModal) errorModal.style.display = "none"; }
  function showError(message) {
    if (errorModal && errorText) {
      errorText.textContent = message;
      errorModal.style.display = "block";
    } else { alert(message); }
  }
  if (errorModalCloseBtn) errorModalCloseBtn.addEventListener("click", dismissError);
  window.addEventListener("click", function (event) {
    if (event.target === errorModal) dismissError();
  });

  // Chart Loader
  let chart = null;
  let currentSymbol = null;
  let currentInterval = null;
  let currentSmaLines = {};
  let currentCandleData = [];
  let candleSeries = null; // For tooltip
  let volumeSeries = null;

  function loadChart(symbol, interval) {
    currentSymbol = symbol;
    currentInterval = interval;
    chartContainer.innerHTML = "";
    // Add tooltip element back if removed
    if (!chartContainer.querySelector("#tooltip")) {
      chartContainer.appendChild(tooltip);
    }

    chart = LightweightCharts.createChart(chartContainer, {
      width: chartContainer.clientWidth,
      height: 500,
      rightPriceScale: {
        visible: true,
        borderVisible: true,
      },
      timeScale: {
        timeVisible: false,
        secondsVisible: false,
        barSpacing: 10,
        rightOffset: 5,
        tickMarkFormatter: (time) => getFormatterByInterval(currentInterval)(time),
      },
      layout: {
        background: { color: "#000000" },
        textColor: "#DDD",
      },
      grid: {
        vertLines: { color: "#333" },
        horzLines: { color: "#333" },
      },
      crosshair: { mode: 1 },
    });

    fetchAndRenderData(symbol, interval);
  }

  function fetchAndRenderData(symbol, interval) {
    fetch(`http://ec2-54-176-81-104.us-west-1.compute.amazonaws.com:8100/v1/chart/candle?symbol=${symbol}&interval=${interval}`)
      .then((res) => res.json())
      .then((candleData) => {
        if (!candleData.length) {
          showError("No data available for this symbol.");
          return;
        }
        // Clear chart
        chart.remove();
        chart = LightweightCharts.createChart(chartContainer, {
          width: chartContainer.clientWidth,
          height: 500,
          rightPriceScale: {
            visible: true,
            borderVisible: true
          },
          timeScale: {
            timeVisible: false,
            barSpacing: 10,
            rightOffset: 5,
            tickMarkFormatter: (time) => formatHKDate(time),
          },
          layout: { background: { color: "#000000" }, textColor: "#DDD" },
          grid: { vertLines: { color: "#333" }, horzLines: { color: "#333" } },
          crosshair: { mode: 1 },
        });

        // Volume
        volumeSeries = chart.addHistogramSeries({
          priceFormat: { type: "volume" },
          priceScaleId: "",
          scaleMargins: { top: 0.85, bottom: 0 },
        });
        volumeSeries.setData(candleData.map(item => ({
          time: item.timestamp,
          value: item.volume,
          color: item.close >= item.open
            ? "rgba(128, 203, 196, 0.3)"
            : "rgba(239, 154, 154, 0.3)",
        })));

        // Candlesticks
        candleSeries = chart.addCandlestickSeries({
          upColor: "#26a69a",
          downColor: "#ef5350",
          borderVisible: false,
          wickUpColor: "#26a69a",
          wickDownColor: "#ef5350",
          scaleMargins: { top: 0.15, bottom: 0.3 },
        });
        candleSeries.setData(candleData.map(item => ({
          time: item.timestamp,
          open: item.open,
          high: item.high,
          low: item.low,
          close: item.close,
        })));

        // SMA Lines
        const smaKeys = Object.keys(candleData[0]).filter(k => k.startsWith("sma"));
        const smaLines = {};
        smaKeys.forEach((smaKey, index) => {
          const smaSeries = chart.addLineSeries({
            color: getColor(index),
            lineWidth: 1.5,
            priceLineVisible: false,
            lastValueVisible: false,
          });
          smaSeries.setData(candleData
            .filter(item => item[smaKey] != null)
            .map(item => ({
              time: item.timestamp,
              value: item[smaKey],
            })));
          smaLines[smaKey] = smaSeries;
        });

        // Save for reset functionality
        currentSmaLines = smaLines;
        currentCandleData = candleData;

        createSmaTogglesForInterval(interval, smaLines, candleData);

        chart.timeScale().fitContent();
        chart.resize(chartContainer.clientWidth, 500);

        // Tooltip logic
        subscribeTooltip(chart, candleSeries, currentCandleData);
      })
      .catch((err) => {
        console.error("Error fetching data:", err);
        showError("❌ Failed to load chart data. Please try again.");
      });
  }

  function createSmaTogglesForInterval(interval, smaLines, candleData) {
    smaToggleContainer.innerHTML = "";
    if (!candleData || candleData.length === 0) return;
    if (!smaLines || Object.keys(smaLines).length === 0) return;
    const latestData = candleData[candleData.length - 1];
    const toggleKey = `smaToggles-${interval}`;
    const storedStates = JSON.parse(localStorage.getItem(toggleKey)) || {};

    Object.entries(smaLines).forEach(([smaKey, line], index) => {
      const label = document.createElement("label");
      label.className = "sma-switch";
      label.style.display = "flex";
      label.style.alignItems = "center";
      label.style.gap = "6px";

      const input = document.createElement("input");
      input.type = "checkbox";
      input.dataset.sma = smaKey;
      const isAvailable = latestData[smaKey] != null;
      const storedValue = storedStates[smaKey];
      input.disabled = !isAvailable;
      input.checked = isAvailable && (storedValue !== false);

      line.applyOptions({ visible: input.checked });
      label.style.opacity = isAvailable ? "1" : "0.5";

      const slider = document.createElement("span");
      slider.className = "slider";

      const legend = document.createElement("span");
      legend.textContent = " " + smaKey.toUpperCase().replace("SMA", "SMA(") + ")";
      legend.style.color = getColor(index);
      legend.style.fontWeight = "bold";

      label.appendChild(input);
      label.appendChild(slider);
      label.appendChild(legend);
      smaToggleContainer.appendChild(label);

      if (isAvailable) {
        input.addEventListener("change", function () {
          line.applyOptions({ visible: this.checked });
          const currentToggles = JSON.parse(localStorage.getItem(toggleKey)) || {};
          currentToggles[smaKey] = this.checked;
          localStorage.setItem(toggleKey, JSON.stringify(currentToggles));
        });
      }
    });
  }

  // Toggle container for mobile
  if (smaToggleBtn && smaToggleContainer) {
    smaToggleBtn.addEventListener("click", () => {
      smaToggleContainer.classList.toggle("show");
      smaToggleBtn.innerHTML = smaToggleContainer.classList.contains("show") ? "&#9650;" : "&#9660;";
    });
  }

  // Reset button logic
  if (smaResetBtn) {
    smaResetBtn.addEventListener("click", () => {
      const toggleKey = `smaToggles-${currentInterval}`;
      localStorage.removeItem(toggleKey);

      // Show all available SMA lines
      Object.entries(currentSmaLines).forEach(([smaKey, line]) => {
        const isAvailable = currentCandleData?.[currentCandleData.length - 1]?.[smaKey] != null;
        if (isAvailable) {
          line.applyOptions({ visible: true });
        }
      });

      createSmaTogglesForInterval(currentInterval, currentSmaLines, currentCandleData);
    });
  }

  // Tooltip logic (Yahoo Finance style)
  function subscribeTooltip(chart, candleSeries, candleData) {
    const tooltip = document.getElementById("tooltip");
    chart.subscribeCrosshairMove(param => {
      if (
        !param.point ||
        !param.time ||
        !param.seriesData ||
        param.point.x < 0 ||
        param.point.y < 0
      ) {
        tooltip.style.display = "none";
        return;
      }
      
      // seriesData is a Map in v4.x
      let candleBar;
      if (typeof param.seriesData.get === 'function') {
        candleBar = param.seriesData.get(candleSeries);
      } else {
        candleBar = param.seriesData[candleSeries];
      }

      if (!candleBar) {
        tooltip.style.display = "none";
        return;
      }

      const candleDatum = candleData.find(item => 
        item.timestamp === param.time || item.time === param.time
      );

      tooltip.innerHTML = `
        <b>Date:</b> ${getFormatterByInterval(currentInterval)(param.time)}<br>
        <b>Open:</b> ${formatNumber(candleBar.open)}<br>
        <b>High:</b> ${formatNumber(candleBar.high)}<br>
        <b>Low:</b> ${formatNumber(candleBar.low)}<br>
        <b>Close:</b> ${formatNumber(candleBar.close)}<br>
        <b>Volume:</b> ${candleDatum ? formatNumber(candleDatum.volume) : "-"}
      `;
      tooltip.style.display = "block";
      // Prevent overflow to the right
      let left = param.point.x + 25;
      if (left + 200 > chartContainer.clientWidth) left = param.point.x - 180;
      tooltip.style.left = left + "px";
      tooltip.style.top = (param.point.y) + "px";
      tooltip.style.background = "rgba(0,0,0,0.85)";
      tooltip.style.color = "#ffe066";
      tooltip.style.padding = "8px 12px";
      tooltip.style.borderRadius = "6px";
      tooltip.style.fontSize = "12px";
      tooltip.style.pointerEvents = "none";
      tooltip.style.boxShadow = "0 2px 8px rgba(0,0,0,0.3)";
      tooltip.style.minWidth = "140px";
      tooltip.style.maxWidth = "200px";
    });
  }

  // Load symbols list
  function loadSymbols() {
    fetch("http://ec2-54-176-81-104.us-west-1.compute.amazonaws.com:8101/api/stocklist")
      .then((res) => res.json())
      .then((data) => {
        const symbols = data["STOCK-LIST"];
        const urlSymbol = getStockSymbolFromUrl();
        const interval = getIntervalFromUrl();

        symbolSelect.innerHTML = "";
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

        updateDropdowns(urlSymbol, interval);
        loadChart(urlSymbol, interval);
        updateUrl(urlSymbol, interval);
      })
      .catch((err) => {
        console.error("Error loading stock list:", err);
        showError("❌ Failed to load stock list.");
      });
  }

  // Dropdown Change Handlers
  symbolSelect.addEventListener("change", () => {
    const symbol = symbolSelect.value;
    const interval = intervalSelect.value;
    updateUrl(symbol, interval);
    loadChart(symbol, interval);
  });

  intervalSelect.addEventListener("change", () => {
    const symbol = symbolSelect.value;
    const interval = intervalSelect.value;
    updateUrl(symbol, interval);
    loadChart(symbol, interval);
  });

  // SMA Colors
  function getColor(index) {
    const colors = ["#1976d2", "#f9a825", "#8e24aa", "#ff7043", "#4caf50", "#ff4081"];
    return colors[index % colors.length];
  }

  // Init
  loadSymbols();
});