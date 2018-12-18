var ORDER = {
    request : {
        order_info_url : "http://192.168.137.10:8042/order/v1/detail"
    },
    getParameter : function(sProp) {
        var re = new RegExp(sProp + "=([^\&]*)", "i");
        var a = re.exec(document.location.search);
        if (a == null)
            return null;
        return a[1];
    },
    getOrderDetails : function () {
        var id = ORDER.getParameter("id");
        $.ajax({
            url: ORDER.request.order_info_url + "/" + id,
            dataType: 'json',
            type: 'GET',
            contentType: 'application/json;charset=UTF-8',
            beforeSend: function (xhr) {
                console.log("Request URL : " + ORDER.request.order_info_url+ "/" + id);
                $('#loading').show();
            },
            success: function (data) {

                setTimeout(function () {
                    $('#loading').hide();
                }, 2 * 1000);
            },
            error: function (xhr, errormsg) {
                console.log("error msg : " + errormsg);
                setTimeout(function () {
                    $('#loading').hide();
                    alert("Something Wrong!")
                }, 2 * 1000);
            },
            complete: function (xhr) {
                console.log("xhr.readyState : " + xhr.readyState);
                console.log("xhr.status : " + xhr.status);

            }
        });
    }
}