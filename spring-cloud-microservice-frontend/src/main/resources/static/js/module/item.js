var ITEM = {
    request : {
        item_list_utl : "http://192.168.137.10:8042/item/v1/brief"
    },

    getAll : function (page, size) {
        var currentAttr = {};
        currentAttr.page = page;
        currentAttr.size = size;
        $.ajax({
            url: ITEM.request.item_list_utl,
            dataType: 'json',
            type: 'GET',
            contentType: 'application/json;charset=UTF-8',
            data: currentAttr,
            beforeSend: function (xhr) {
                console.log("Request URL : " + ITEM.request.item_list_utl);
            },
            success: function (data) {
                $.each(data,function () {
                    $("#item-list").append(
                        "<div class='col-xs-6 col-sm-3 placeholder'>" +
                        "<img src='" + this.image + "' class='img-responsive' alt='Generic placeholder thumbnail' width='200' height='200'>" +
                        "<h4>" + this.title + "</h4>" +
                        "<div class='price-sales'>" +
                        "<span class='price'><i class='rmb'>¥</i>" + this.currentprice + "</span>" +
                        "</div>"+
                        "<span class='text-muted'>" + this.item_des + "</span>" +
                        "</div>"
                    );
                })
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