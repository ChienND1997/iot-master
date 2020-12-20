window.onscroll = function() {scrollFunction()};
function scrollFunction() {
    if (document.body.scrollTop > 0 || document.documentElement.scrollTop > 0) {
        document.getElementById("navbar").style.backgroundColor = "#ffff";
    } else {
        document.getElementById("navbar").style.backgroundColor = "#fff0";
    }
}