const hoursState = Array(24).fill('0'); // 初始化所有狀態為 "關"

// 設定拖拉操作
let isDragging = false;

document.querySelectorAll('.hour').forEach((hour, index) => {
    hour.addEventListener('mousedown', () => {
        isDragging = true;
        toggleHourState(index);
    });

    hour.addEventListener('mouseover', () => {
        if (isDragging) {
            toggleHourState(index);
        }
    });

    hour.addEventListener('mouseup', () => {
        isDragging = false;
    });
});

function toggleHourState(index) {
    // 切換狀態
    hoursState[index] = hoursState[index] === '0' ? '1' : '0';

    // 更新 UI
    const hourElement = document.querySelectorAll('.hour')[index];
    hourElement.classList.toggle('active');

    // 查看結果（用於測試）
    console.log(hoursState.join('')); // 例: "000011111110000000000000"
}