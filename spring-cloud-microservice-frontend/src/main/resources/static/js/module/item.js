var ITEM = {
    request : {
        item_category_list_url : "http://192.168.137.10:8042/item/v1/category",
        item_list_utl : "http://192.168.137.10:8042/item/v1/brief"
    },
    username : "",
    categoryId : [],
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
                        "<div><button type='button' itemId='" + this.id + "' class='btn btn-default btn-sm btn-xs cartbtn'>" +
                        "<span class='glyphicon glyphicon-shopping-cart'></span>Add to cart"  +
                        "</button>" +
                        "</div>" +
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
    },
    getItemCategorys : function (username) {
        ITEM.username = username;
        $.ajax({
            url: ITEM.request.item_category_list_url,
            dataType: 'json',
            type: 'GET',
            async : 'false',
            contentType: 'application/json;charset=UTF-8',
            beforeSend: function (xhr) {
                console.log("Request URL : " + ITEM.request.item_category_list_url);
            },
            success: function (data) {
                $.each(data,function (i, value) {
                    $("#main-pannel").prepend(
                        "<div class='row placeholders' id='itemcategory-" + value.id + "'>" +
                        "<div class='panel panel-item'>" +
                        "<div class='panel-heading'>" +
                        "<h3 class='panel-title'>" + value.name + "</h3>" +
                        "</div>" +
                        "<div class='panel-body' id='item-" + value.id + "'>" +
                        "</div>" +
                        "</div>" +
                        "</div>"
                    )
                    ITEM.categoryId[i] = value.id;
                });
                ITEM.getBriefByCategory();
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
    getBriefByCategory : function () {
        var currentAttr = {};
        currentAttr.page = 0;
        currentAttr.size = 4;
        $.each(ITEM.categoryId,function (i,value) {
            var currentCategoryId = value;
            $.ajax({
                url: ITEM.request.item_list_utl + "/" + currentCategoryId,
                dataType: 'json',
                type: 'GET',
                data: currentAttr,
                contentType: 'application/json;charset=UTF-8',
                beforeSend: function (xhr) {
                    console.log("Request URL : " + ITEM.request.item_list_utl + "/" + currentCategoryId);
                },
                success: function (data) {
                    $.each(data,function (i,value) {
                        $("#item-" + currentCategoryId).append(
                            "<div class='col-xs-6 col-sm-3 placeholder'>" +
                            "<img src='" + value.image + "' class='img-responsive' alt='Generic placeholder thumbnail' width='200' height='200'>" +
                            "<h4>" + value.title + "</h4>" +
                            "<div class='price-sales'>" +
                            "<span class='price'><i class='rmb'>¥</i>" + value.currentprice + "</span>" +
                            "</div>"+
                            "<span class='text-muted'>" + value.item_des + "</span>" +
                            "<div><button type='button' data-toggle=\"modal\" data-target=\"#myModal\" onclick='ITEM.add2Cart(" + this.id + ")' itemId='" + this.id + "' class='btn btn-default btn-sm btn-xs cartbtn'>" +
                            "<span class='glyphicon glyphicon-shopping-cart'></span>Add to cart"  +
                            "</button>" +
                            "</div>"
                        );
                    });
                },
                error: function (xhr, errormsg) {
                    console.log("error msg : " + errormsg);
                },
                complete: function (xhr) {
                    console.log("xhr.readyState : " + xhr.readyState);
                    console.log("xhr.status : " + xhr.status);
                }
            });
        });
    },
    add2Cart : function (itemId) {
            //var itemId = $(this).attr("itemId");
            var params = {};
            params.num = 1;
            params.username = ITEM.username;
            if (params.username == '') {
                window.location.href="/login.html";
            }
            CART.add2Cart(itemId, params);
    }
};