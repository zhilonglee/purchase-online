var CART = {
    request : {
        cart_url : "http://192.168.137.10:8042/item/v1/cart"
	},
	load : function(username){
        $.ajax({
            url: CART.request.cart_url + "/" + username,
            dataType: 'json',
            type: 'GET',
            contentType: 'application/json;charset=UTF-8',
            beforeSend: function (xhr) {
                console.log("Request URL : " + CART.request.cart_url + "/" + username);
            },
            success: function (data) {
                var itemCount = 0;
                var amount = 0;
                var currentAmount = 0;
                var reducedAmount = 0;
                $.each(data.cartItems,function () {
                    itemCount = itemCount +1;
                    amount = amount + this.price * this.num;
                    currentAmount = currentAmount + this.currentprice * this.num;
                    $("#product-list").append(
                    '<div id="product_"' + this.itemId + ' data-bind="rowid:1" class="item item_selected ">' +
                        '<div class="item_form clearfix">' +
                        '<div class="cell p-checkbox"><input data-bind="cbid:1" class="checkbox" type="checkbox" name="checkItem" checked="" value="11345721-1"></div>'+
                        '<div class="cell p-goods">' +
                        '<div class="p-img">' +
                        '<a href="/item/' + this.itemId + '.html" target="_blank">' +
                        '<img clstag="clickcart|keycount|xincart|p-imglistcart" src="' + this.image + '" alt="' + this.title +'" width="52" height="52">' +
                        '</a>' +
                        '</div>' +
                        '<div class="p-name">' +
                        '<a href="/item/' + this.itemId + '.html" clstag="clickcart|keycount|xincart|productnamelink" target="_blank">' + this.title + '</a>' +
                    '<span class="promise411 promise411_11345721" id="promise411_11345721">' + this.item_des + '</span>' +
                        '</div>' +
                        '</div>' +
                        '<div class="cell p-price"><span class="price">¥' + this.price + '</span></div>' +
                    '<div class="cell p-promotion">' +
                        '<div class="cell p-price"><span class="price">¥' + this.currentprice + '</span></div>' +
                        '</div>' +
                        '<div class="cell p-inventory stock-11345721">available</div>' +
                        '<div class="cell p-quantity" for-stock="for-stock-11345721">' +
                        '<div class="quantity-form" data-bind="">' +
                        '<a href="javascript:void(0);" class="decrement" clstag="clickcart|keycount|xincart|diminish1" id="decrement">-</a>' +
                        '<input type="text" class="quantity-text" itemPrice="' + this.currentprice + '" itemId="' + this.itemId + '" value="' + this.num + '" id="changeQuantity-' + this.itemId + '">' +
                        '<a href="javascript:void(0);" class="increment" clstag="clickcart|keycount|xincart|add1" id="increment">+</a>' +
                        '</div>' +
                        '</div>' +
                        '<div class="cell p-remove"><a id="remove-' + this.itemId + '"  clstag="clickcart|keycount|xincart|btndel318558" class="cart-remove" href="/cart/delete/' + this.itemId + '.html">Delete</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>'
                    );
                });

                reducedAmount = amount - currentAmount;
                $("#totalSkuPrice").html(amount.toFixed(2)).priceFormat({
                    prefix: '￥ ',
                    thousandsSeparator: ',',
                    centsLimit: 2
                });
                $("#totalRePrice").html(reducedAmount.toFixed(2)).priceFormat({
                    prefix: '- ￥ ',
                    thousandsSeparator: ',',
                    centsLimit: 2
                });
                $("#selectedCount").html(itemCount);
                $("#totalCurrentSkuPrice").html(currentAmount.toFixed(2)).priceFormat({
                    prefix: '￥ ',
                    thousandsSeparator: ',',
                    centsLimit: 2
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
	},
	itemNumChange : function(){

	}
};

