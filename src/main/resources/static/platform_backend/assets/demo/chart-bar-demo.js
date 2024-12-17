// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';



  document.addEventListener("DOMContentLoaded", function () {
  // Get table and rows
  const table = document.getElementById("datatablesSimple");
  const rows = table.querySelectorAll("tbody tr");

  let totalPriceSum = 0;
  let discountSum = 0;
  let paidSum = 0;
  let refundSum = 0;

  // Iterate through each row and calculate sums
  rows.forEach(row => {
  const totalPrice = parseFloat(row.cells[4]?.innerText || 0); // 總金額
  const discount = parseFloat(row.cells[5]?.innerText || 0);   // 折扣金額
  const paid = parseFloat(row.cells[6]?.innerText || 0);       // 付款金額
  const refund = parseFloat(row.cells[9]?.innerText || 0);     // 退款金額

  totalPriceSum += totalPrice;
  discountSum += discount;
  paidSum += paid;
  refundSum += refund;
});

    const footerDiv = document.querySelector(".card-footer.small.text-muted");
    footerDiv.innerHTML = `
        <h5>統計資料</h5>
        <p>總金額合計：${totalPriceSum.toFixed(0)}</p>
        <p>折扣金額合計：${discountSum.toFixed(0)}</p>
        <p>付款金額合計：${paidSum.toFixed(0)}</p>
        <p>退款金額合計：${refundSum.toFixed(0)}</p>
    `;






// Bar Chart Example
var ctx = document.getElementById("myBarChart");
var myLineChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ["總金額", "折扣金額", "付款金額", "退款金額"],
    datasets: [{
      label: "Revenue",
      backgroundColor: "rgba(2,117,216,1)",
      borderColor: "rgba(2,117,216,1)",
      data: [totalPriceSum, discountSum, paidSum, refundSum],
    }],
  },
  options: {
    scales: {
      xAxes: [{
        time: {
          unit: 'month'
        },
        gridLines: {
          display: false
        },
        ticks: {
          maxTicksLimit: 6
        }
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: (totalPriceSum * 1.5) ? totalPriceSum * 1.5 : '',
          maxTicksLimit: 5
        },
        gridLines: {
          display: true
        }
      }],
    },
    legend: {
      display: false
    }
  }
});
  });