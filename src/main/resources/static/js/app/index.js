function goPost() {
    var logoutForm = $('<form></form>');
    logoutForm.attr("method", "post");
    logoutForm.attr("action","/member/logout");

    logoutForm.appendTo("body");

    logoutForm.submit();
}