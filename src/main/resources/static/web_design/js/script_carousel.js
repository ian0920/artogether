document.addEventListener("DOMContentLoaded", function () {
  const carousel = document.getElementById('carousel');
  const prevButton = document.getElementById('prev');
  const nextButton = document.getElementById('next');
  const carouselItems = document.querySelectorAll('.carousel-item');
  let currentIndex = 0;
  const totalItems = carouselItems.length;

  // 如果 autoPlay 為 true，則啟用自動播放
  if (autoPlay) {
    let autoPlayInterval = setInterval(function () {
      showImage(currentIndex + 1);
    }, 3000); // 每 3 秒切換一次
  }

  // 顯示指定索引的圖片
  function showImage(index) {
    if (index >= totalItems) {
      currentIndex = 0; // 到達最後一張時回到第一張
    } else if (index < 0) {
      currentIndex = totalItems - 1; // 到達第一張時回到最後一張
    } else {
      currentIndex = index;
    }

    // 移動 carousel
    carousel.style.transform = `translateX(-${currentIndex * 100}%)`;

    // 更新按鈕狀態
    updateButtonVisibility();
  }

  // 更新左右按鈕顯示狀態
  function updateButtonVisibility() {
    if (currentIndex === 0) {
      prevButton.style.display = 'none'; // 隱藏上一張按鈕
    } else {
      prevButton.style.display = 'block';
    }

    if (currentIndex === totalItems - 1) {
      nextButton.style.display = 'none'; // 隱藏下一張按鈕
    } else {
      nextButton.style.display = 'block';
    }
  }

  // 處理上一張按鈕
  prevButton.addEventListener('click', function () {
    showImage(currentIndex - 1);
  });

  // 處理下一張按鈕
  nextButton.addEventListener('click', function () {
    showImage(currentIndex + 1);
  });

  // 初始顯示第一張圖片
  showImage(currentIndex);
});
