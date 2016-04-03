function Main() {

    $("#main a").on("click", function(e) {
        var option = $(this).attr("href");
        $("#main").hide();
        $("#header").show();
        $("#" + option + "_page").click();
        e.preventDefault();
    });

    $("#main a").on("mouseover", function(e) {
        $(this).before("<span id='arrow'>a </span>");
    });

    $("#main a").on("mouseout", function(e) {
        $("#arrow").remove();
    });

    $("#name_page").on("click", function(e) {
        $("#main").show();
        $("#options").hide();
        $("#header").hide();
        e.preventDefault();
    });

    $("#header a").on("click", function(e) {
        var option = $(this).attr("href");
        $("#options").show();
        $(this).addClass("currTab").siblings("a").removeClass("currTab");
        $("#" + option).show().siblings("div").hide();
        e.preventDefault();
    });

	$("#name_page").click();
}

$(document).ready(function() {
    Main();
});