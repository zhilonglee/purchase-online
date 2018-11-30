var PAYMENT = {
    request : {
        order_request_url : "http://192.168.137.10:8042/item/v1/nets/orderRequest",
        query_request_url : "http://192.168.137.10:8042/item/v1/nets/query"
    },

    inquiryCount : 0,

    getOrder : function () {
        var amount = $("#amount").val();
        var txn_identifier = '';
        var transaction_time = '';
        var transaction_date = '';
        PAYMENT.inquiryCount = 1;
        $.ajax({
            url: PAYMENT.request.order_request_url,
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json;charset=UTF-8',
            data: amount,
            beforeSend: function (xhr) {
                console.log("Request URL : " + PAYMENT.request.order_request_url);
                $('#result_image').html("");
            },
            success: function (data) {

                txn_identifier = data.txn_identifier;
                transaction_time = data.transaction_time;
                transaction_date = data.transaction_date;

                $('#payment_image').html('<img id="netspay-mobile-link" class="hidden-xs" height="180" width="180" src="data:image/png;base64,' + data.qr_code + '" /> ');

                setTimeout(function() {
                    PAYMENT.query(txn_identifier, transaction_time, transaction_date);
                }, 3000);
            },
            error: function (xhr, errormsg) {
                console.log("error msg : " + errormsg);
            },
            complete: function (xhr) {
                console.log("xhr.readyState : " + xhr.readyState);
                console.log("xhr.status : " + xhr.status);
            }
        });
    },
    query : function (txn_identifier, transaction_time, transaction_date) {
        var param = {};
        param.txn_identifier = txn_identifier;
        param.transaction_time = transaction_time;
        param.transaction_date = transaction_date;
        $.ajax({
            url: PAYMENT.request.query_request_url,
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(param),
            beforeSend: function (xhr) {
                console.log("Request URL : " + PAYMENT.request.query_request_url);
            },
            success: function (data) {
                if (data.response_code == '09' && PAYMENT.inquiryCount < 40) {
                    setTimeout(function() {
                        PAYMENT.query(txn_identifier, transaction_time, transaction_date);
                    }, 2500);
                    PAYMENT.inquiryCount++;
                } else {
                    $('#payment_image').hide();
                    if (data.response_code == '00') {
                        $('#result_image').html('<img src="http://localhost:8082/img/success.svg" alt="Transaction Successful" height="100" width="100"><h4>Payment Success!</h4>');
                    } else {
                        $('#result_image').html('<img src="http://localhost:8082/img/error.svg" alt="Transaction Failure" height="100" width="100"><h4>Payment Failure!</h4>');
                    }
                }
            },
            error: function (xhr, errormsg) {
                console.log("error msg : " + errormsg);
            },
            complete: function (xhr) {
                console.log("xhr.readyState : " + xhr.readyState);
                console.log("xhr.status : " + xhr.status);
            }
        });
    }
};

