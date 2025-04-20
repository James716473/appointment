document.addEventListener("DOMContentLoaded", function () {
const toggle = document.getElementById("mobile-menu");
const menu = document.querySelector(".manage__menu");

toggle.addEventListener("click", () => {
    menu.classList.toggle("show");
});

// Optional: Hide dropdown when clicking outside
document.addEventListener("click", (e) => {
    if (!toggle.contains(e.target) && !menu.contains(e.target)) {
    menu.classList.remove("show");
    }
});
});
