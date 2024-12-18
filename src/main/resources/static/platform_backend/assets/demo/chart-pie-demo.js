// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';


  document.addEventListener("DOMContentLoaded", function () {
  // Get table and rows
  const table = document.getElementById("datatablesSimple");
  const rows = table.querySelectorAll("tbody tr");

  // Payment method counts
  let creditCardCount = 0;      // 信用卡
  let mobilePaymentCount = 0;   // 行動支付
  let atmTransferCount = 0;     // ATM轉帳

  // Iterate through rows and count payment methods
  rows.forEach(row => {
  const paymentMethod = row.cells[10]?.innerText.trim(); // Last column (付款方式)

  switch (paymentMethod) {
  case "信用卡":
  creditCardCount++;
  break;
  case "行動支付":
  mobilePaymentCount++;
  break;
  case "ATM轉帳":
  atmTransferCount++;
  break;
}

    const footerDiv = document.querySelector(".payment-method");
    footerDiv.innerHTML = `
        <h5>付款方式統計</h5>
        <p>信用卡：${creditCardCount} 筆</p>
        <p>行動支付：${mobilePaymentCount} 筆</p>
        <p>ATM轉帳：${atmTransferCount} 筆</p>
    `;

const total = creditCardCount + mobilePaymentCount + atmTransferCount;

// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: 'pie',
  data: {
    labels: ["信用卡", "行動支付", "ATM轉帳"],
    datasets: [{
      data: [(creditCardCount/total*100).toFixed(1), (mobilePaymentCount/total*100).toFixed(1), (atmTransferCount/total*100).toFixed(1)],
      backgroundColor: ['#007bff', '#ffc107', '#28a745'],
    }],
  },
});
  });
  });