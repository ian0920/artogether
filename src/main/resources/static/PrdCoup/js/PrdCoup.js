// PrdCoup.js

// 分區切換
function showSection(sectionId) {
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => section.classList.add('hidden'));
    document.getElementById(sectionId).classList.remove('hidden');
}

// AJAX 加載所有優惠券
function loadCoupons() {
    fetch('/coupons')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#coupon-table tbody'); // 選擇表格的 tbody
            tableBody.innerHTML = ''; // 清空表格內容

            data.forEach(coupon => {
                // 創建一行
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${coupon.id}</td>
                    <td>${coupon.businessMember || '無資料'}</td>
                    <td>${coupon.name}</td>
					<td>${coupon.status === 0 ? '下架' : '上架'}</td>
                    <td>${coupon.type === 0 ? '金額折扣' : '比例折扣'}</td>          
                   	<td>${coupon.deduction}</td> 				
					<td>${coupon.ratio}</td>
					<td>${coupon.startDate}</td>
                    <td>${coupon.endDate}</td>	
                    <td>${coupon.duration} 天</td>
                    <td>${coupon.threshold} 元</td>
                    <td>
                        <button onclick="deleteCoupon(${coupon.id})">刪除</button>
                    </td>
                `;
                // 將新行添加到表格中
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading coupons:', error));
}


// AJAX 新增優惠券
document.getElementById('add-coupon-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    const jsonData = Object.fromEntries(formData.entries());

    fetch('/coupons', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(jsonData)
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadCoupons();
        showSection('list-coupons');
    })
    .catch(error => console.error('Error adding coupon:', error));
});

// AJAX 刪除優惠券
function deleteCoupon(id) {
    if (confirm('確定要刪除嗎？')) {
        fetch(`/coupons/${id}`, {
            method: 'DELETE'
        })
        .then(response => response.text())
        .then(message => {
            alert(message);
            loadCoupons();
        })
        .catch(error => console.error('Error deleting coupon:', error));
    }
}

// AJAX 查詢優惠券
document.getElementById('search-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const status = document.getElementById('query-status').value;
    const type = document.getElementById('query-type').value;

    const queryParams = new URLSearchParams({
        status: status || '',
        type: type || ''
    });

    fetch(`/coupons/filter?${queryParams}`)
        .then(response => response.json())
        .then(data => {
            const table = document.getElementById('search-results');
            table.innerHTML = '';
            data.forEach(coupon => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${coupon.id}</td>
                    <td>${coupon.name}</td>
                    <td>${coupon.type === 0 ? '金額折扣' : '比例折扣'}</td>
                    <td>${coupon.status === 0 ? '下架' : '上架'}</td>
                    <td>${coupon.startDate}</td>
                    <td>${coupon.endDate}</td>
                `;
                table.appendChild(row);
            });
        })
        .catch(error => console.error('Error searching coupons:', error));
});

// 初始化加載
window.addEventListener('DOMContentLoaded', () => {
    loadCoupons();
    showSection('list-coupons');
});
