let currentIndex = 0;

document.querySelector('#prev').addEventListener('click', () => {
    const carousel = document.querySelector('.carousel');
    const items = document.querySelectorAll('.carousel-item');
    currentIndex = (currentIndex - 1 + items.length) % items.length;
    carousel.style.transform = `translateX(-${currentIndex * 100}%)`;
});

document.querySelector('#next').addEventListener('click', () => {
    const carousel = document.querySelector('.carousel');
    const items = document.querySelectorAll('.carousel-item');
    currentIndex = (currentIndex + 1) % items.length;
    carousel.style.transform = `translateX(-${currentIndex * 100}%)`;
});
