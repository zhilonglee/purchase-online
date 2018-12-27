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
                $(".goods-suit goods-last").html("");
                $.each(data.orderItems,function () {
                    $(".goods-suit").append(
                        '<div class="goods-item goods-item-extra">' +
                        '<div class="p-img">' +
                        '<a target="_blank" href="/item/' + this.id +'.html">' +
                        '<img src="' + this.image + '" alt=" ' + this.title + '"></a>' +
                        '</div>' +
                        '<div class="goods-msg">' +
                        '<div class="p-name">' +
                        '<a target="_blank" href="/item/' + this.id +'.html">' + this.title + '</a>' +
                        '</div>' +
                        '<div class="p-price">' +
                        '<strong>￥' + this.currentprice + '</strong>' +
                        '<span class="ml20"> x ' + this.num + ' </span>' +
                        '<span class="ml20 p-inventory">'+ (this.stockNum > 0 ? 'available' : 'sell out') + '</span>' +
                        '</div>' +
                        '<i class="p-icon p-icon-w"></i><span class="ftx-04">7 days no reason to refund</span>' +
                        ' </div>' +
                        '<div class="clr"></div>' +
                        '</div>'
                    );
                });
                $("#warePriceId").html("￥ " + data.disacountTotal);
                $("#sumPayPriceId").html("￥ " + data.disacountTotal);
                $("#payPriceId").html("￥ " + data.disacountTotal);
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