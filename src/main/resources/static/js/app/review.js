function reviewPage() {
    var pilen = $("input[name=productId]").length;
    var productIds = new Array(pilen);
    for(var i=0; i<pilen; i++) {
        productIds[i] = $("input[name=productId]").eq(i).val();
    }

    var data = {
        productIds: productIds
    };

    var reviewForm = $('<form></form>');
    reviewForm.attr("method", "get")
    reviewForm.attr("action", "/review");
    for(var i=0; i<pilen; i++) {
        reviewForm.append($('<input/>', {type: 'hidden', name: 'productIds', value: $("input[name=productId]").eq(i).val()}));
    }
    reviewForm.appendTo('body');
    reviewForm.submit();
}

function reviewSave() {

    var memberId = $('#memberId').val();
    var productId = $('#productId').val();
    var productReview = $('#productReview').val();

    var data = {
        memberId: memberId,
        productId: productId,
        productReview: productReview
    };


    $.ajax({
        type: 'POST',
        url: '/review/' + productId + '/save',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(response) {
        if(response.status == "SUCCESS") {
            $('#error').hide('slow');
            alert("리뷰 작성 성공!");
            window.close();
        } else {
            errorInfo = response.result[0].defaultMessage;
            $('#error').html(errorInfo);
            $('#error').show('slow');
            console.log(errorInfo)
        }
    }).fail(function(error) {
        alert(JSON.stringify(error));
    });
}