// 設定最下方的user control菜單收合
var el_nav_user_control = document.querySelector(".bs-navbar-user-field");
el_nav_user_control.addEventListener("click", function () {
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

// 設定nav-item的選取效果
var els_nav_item = document.querySelectorAll(".bs-navbar-item");
for (let i = 0; i < els_nav_item.length; i++) {
  els_nav_item[i].addEventListener("click", function () {
    els_nav_item.forEach((e) => e.classList.remove("-open"));
    this.classList.add("-open");
    // 觸發a tag點擊事件
    this.lastElementChild.click();
  });
}
