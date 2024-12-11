var el_nav_item_discover = document.querySelector("div.navbar-item");
var el_div_nav_menu_box = document.querySelector("div.nav-menu-box");
var el_div_nav_user_box = document.querySelector("div.user_div");
var el_div_nav_menu = document.querySelector("div.nav-menu");
var el_eiv_user_menu = document.querySelector("div.user-menu-box");

//=====================觸發視窗========================
// 1. 探索
el_nav_item_discover.addEventListener("mouseenter", function () {
  if (!el_div_nav_menu_box.classList.contains("open"))
    el_div_nav_menu_box.classList.add("open");
});
console.log(el_div_nav_menu);
el_div_nav_menu.addEventListener("mouseleave", function () {
  if (el_div_nav_menu_box.classList.contains("open"))
    el_div_nav_menu_box.classList.remove("open");
});

// 2. 會員
el_div_nav_user_box.addEventListener("mouseenter", function () {
  if (!el_eiv_user_menu.classList.contains("open"))
    el_eiv_user_menu.classList.add("open");
});
el_eiv_user_menu.addEventListener("mouseleave", function () {
  if (el_eiv_user_menu.classList.contains("open"))
    el_eiv_user_menu.classList.remove("open");
});
