function put() {
    var productId = $('#productId').val();
    var count = $('#count').val();

    var cartForm = $('<form></form>');
    cartForm.attr("method", "post");
    cartForm.attr("action", "/products/" + productId);
    cartForm.append($('<input/>', {type:'hidden', name:'id', value:productId}));
    cartForm.append($('<input/>', {type:'hidden', name:'count', value:count}));

    cartForm.appendTo("body");

    cartForm.submit();
}

function cancel(id) {

    var memberId = $('#memberId').val();
    var data = {
        cartId: id
    };

    $.ajax({
        type: 'DELETE',
        url: '/member/' + memberId + '/my-cart',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function() {
        location.replace('/member/' + memberId + '/my-cart');
    }).fail(function(error) {
        alert(JSON.stringify(error));
    });
}