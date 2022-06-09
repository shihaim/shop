function put() {

    var productId = $('#id').val();
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

    console.log(data);

    $.ajax({
        type: 'DELETE',
        url: '/member/' + memberId + '/cart',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function() {
        location.href = '/member/' + memberId + '/my-page';
    });
}