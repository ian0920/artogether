// 設定最下方的user control菜單收合
var el_nav_user_field = document.querySelector(".bs-navbar-user-field");
el_nav_user_field.addEventListener("click", function (e) {
  e.stopPropagation();
  let el_nav_user_menu = document.querySelector(".bs-navbar-user-menu");
  el_nav_user_menu.classList.toggle("-hidden");
  let el_field_icon = document.querySelector(
    ".bs-navbar-user-field #icon_unfold"
  );
  if (el_field_icon.classList.contains("unfold")) {
    el_field_icon.classList.remove("unfold");
    el_field_icon.innerHTML = "unfold_less";
  } else {
    el_field_icon.classList.add("unfold");
    el_field_icon.innerHTML = "unfold_more";
  }
});

// 設定user-item的選取範圍
var els_nav_user_item = document.querySelectorAll(
  "div.bs-navbar-user-menu .bs-navbar-user-item"
);
for (let i = 0; i < els_nav_user_item.length; i++) {
  els_nav_user_item[i].addEventListener("click", function (e) {
    console.log("hi");
    // TODO: 這裡要補上跳轉連結
    this.lastElementChild.click();
    el_nav_user_field.click();
  });
}

// 設定nav-item的選取效果
var els_nav_item = document.querySelectorAll(".bs-navbar-item");
var els_nav_header = document.querySelectorAll(".bs-navbar-header");
var els_nav_submenu = document.querySelectorAll(".bs-navbar-submenu");
var els_nav_subitem = document.querySelectorAll(".bs-navbar-subitem");
for (let i = 0; i < els_nav_item.length; i++) {
  els_nav_item[i].addEventListener("click", function () {
    els_nav_header.forEach((e) => e.classList.remove("-open"));
    els_nav_submenu.forEach((e) => e.classList.remove("-open"));
    this.firstElementChild.classList.add("-open");
    this.lastElementChild.classList.add("-open");
    // 觸發a tag點擊事件
    this.lastElementChild.click();
  });
}
// 設定nav-subitem的選取效果
for (let i = 0; i < els_nav_subitem.length; i++) {
  els_nav_subitem[i].addEventListener("click", function () {
    els_nav_subitem.forEach((e) => e.classList.remove("-focus"));
    this.classList.add("-focus");
  });
}