var USER = {
    request : {
        access_token_url: "http://192.168.137.10:8042/user/v1/person/oauth/token",
        register_url : "http://192.168.137.10:8042/user/v1/person/register",
        active_url : "http://192.168.137.10:8042/user/v1/person/active",
        reset_password_url : "http://192.168.137.10:8042/user/v1/person/resetpassword",
        new_password_url : "http://192.168.137.10:8042/user/v1/person/newpassword"
    },
    getParameter : function(sProp) {
    var re = new RegExp(sProp + "=([^\&]*)", "i");
    var a = re.exec(document.location.search);
    if (a == null)
        return null;
    return a[1];
    },
register : function () {
        var password = $("#password").val();
        var confirmpassword = $("#confirmpassword").val();
        if((password != '' && password != null) && password == confirmpassword) {
            var user = {};
            user.username = $("#username").val();
            user.email = $("#email").val();
            user.password = password;
            if (user.username == '' || user.email == '') {
                $(".clear").after(
                    '<div class="alert alert-warning">' +
                    '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                    '<strong>Required username or email.</strong>' +
                    '</div>'
                )
            }
            $.ajax({
                url: USER.request.register_url,
                async: false,
                dataType: 'json',
                type: 'POST',
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify(user),
                beforeSend: function (xhr) {
                    console.log("Request URL : " + USER.request.register_url);
                },
                success: function (data) {
                    console.log(data);
                    $(".clear").after(
                        '<div class="alert alert-success">' +
                        '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                        '<strong>Congratulations</strong>, registration is successful! Activation email has been sent!' +
                        '</div>'
                    )
                    $(this).attr("disabled","disabled");
                },
                error: function (xhr, errormsg) {
                    console.log("error msg : " + errormsg);
                    $(".clear").after(
                        '<div class="alert alert-warning">' +
                        '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                        '<strong>Unfortunately</strong>, registration is failed! Kindly change your username or email.' +
                        '</div>'
                    )
                },
                complete: function (xhr) {
                    console.log("xhr.readyState : " + xhr.readyState);
                    console.log("xhr.status : " + xhr.status);
                }
            });
        } else {
            $(".clear").after(
                '<div class="alert alert-warning">' +
                '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                '<strong>Alert</strong>, registration is failed! password must compare with confirm password.' +
                '</div>'
            )
        }
    },
    active: function (token) {
        if(token != '' || token != null) {
            var url = USER.request.active_url + "/" + token;
            $.ajax({
                url: url,
                dataType: 'json',
                type: 'POST',
                contentType: 'application/json;charset=UTF-8',
                beforeSend: function (xhr) {
                    console.log("Request URL : " + url);
                },
                success: function (data) {
                    console.log(data);
                    if (data.statusCode == 200) {
                        $("#remain-seconds").html(
                            '<div class="alert alert-success">' +
                            '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                            '<strong>Congratulations</strong>,' +
                            data.message +
                            '</div>'
                        )
                    } else {
                        $("#remain-seconds").html(
                            '<div class="alert alert-warning">' +
                            '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                            '<strong>Unfortunately</strong>, ' +
                            data.message +
                            '</div>'
                        )
                    }
                },
                error: function (xhr, errormsg) {
                    console.log("error msg : " + errormsg);
                    $("#remain-seconds").html(
                        '<div class="alert alert-danger">' +
                        '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                        '<strong>Unfortunately</strong>, activation is failed! Something wrong on the server.' +
                        '</div>'
                    )
                },
                complete: function (xhr) {
                    console.log("xhr.readyState : " + xhr.readyState);
                    console.log("xhr.status : " + xhr.status);
                }
            });
        }
    },
    restpassword: function () {
        var username = $("#username").val();
        var user = {};
        user.username = $("#username").val();
        user.email = $("#email").val();
            var url = USER.request.reset_password_url + "/" + username;
            $.ajax({
                url: url,
                dataType: 'json',
                type: 'POST',
                data: JSON.stringify(user),
                contentType: 'application/json;charset=UTF-8',
                beforeSend: function (xhr) {
                    console.log("Request URL : " + url);
                },
                success: function (data) {
                    console.log(data);
                    if (data.statusCode == 200) {
                        $("#reg_passmail").html(
                            '<div class="alert alert-success">' +
                            '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                            '<strong>Congratulations</strong>,' +
                            data.message +
                            '</div>'
                        )
                        $(this).attr("disabled","disabled");
                    } else {
                        $("#reg_passmail").html(
                            '<div class="alert alert-warning">' +
                            '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                            '<strong>Unfortunately</strong>, ' +
                            data.message +
                            '</div>'
                        )
                    }
                },
                error: function (xhr, errormsg) {
                    console.log("error msg : " + errormsg);
                    $("#reg_passmail").html(
                        '<div class="alert alert-danger">' +
                        '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                        '<strong>Unfortunately</strong>, activation is failed! Something wrong on the server.' +
                        '</div>'
                    )
                },
                complete: function (xhr) {
                    console.log("xhr.readyState : " + xhr.readyState);
                    console.log("xhr.status : " + xhr.status);
                }
            });
        },
    newpassword : function () {
        var user = {};
        var password = $("#password").val();
        var confirmpassword = $("#confirmpassword").val();
        user.password = password;
        var token = USER.getParameter('token');
        var url = USER.request.new_password_url + "/" + token;
        $.ajax({
            url: url,
            dataType: 'json',
            type: 'POST',
            data: JSON.stringify(user),
            contentType: 'application/json;charset=UTF-8',
            beforeSend: function (xhr) {
                console.log("Request URL : " + url);
            },
            success: function (data) {
                console.log(data);
                if((password != '' && password != null) && password == confirmpassword) {
                    if (data.statusCode == 200) {
                        $("#reg_passmail").html(
                            '<div class="alert alert-success">' +
                            '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                            '<strong>Congratulations</strong>,' +
                            data.message +
                            '</div>'
                        )
                        $(this).attr("disabled", "disabled");
                    } else {
                        $("#reg_passmail").html(
                            '<div class="alert alert-warning">' +
                            '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                            '<strong>Unfortunately</strong>, ' +
                            data.message +
                            '</div>'
                        )
                    }
                } else {
                    $("#reg_passmail").html(
                        '<div class="alert alert-warning">' +
                        '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                        '<strong>Alert</strong>, reset password is failed! password must compare with confirm password.' +
                        '</div>'
                    )
                }
            },
            error: function (xhr, errormsg) {
                console.log("error msg : " + errormsg);
                $("#reg_passmail").html(
                    '<div class="alert alert-danger">' +
                    '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                    '<strong>Unfortunately</strong>, reset password is failed! Something wrong on the server.' +
                    '</div>'
                )
            },
            complete: function (xhr) {
                console.log("xhr.readyState : " + xhr.readyState);
                console.log("xhr.status : " + xhr.status);
            }
        });
    },
    getToken : function () {
        var user = {};
        user.username = $("#username").val();
        user.password = $("#password").val();
        $.ajax({
            url: USER.request.access_token_url,
            async: false,
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(user),
            beforeSend: function (xhr) {
                console.log("Request URL : " + USER.request.access_token_url);
            },
            success: function (data) {
                setTimeout(function () {
                    $('.login').removeClass('testtwo'); // remove pan effect
                }, 2000);
                setTimeout(function () {
                    $('.login').removeClass('test'); // remove tilt effect
                    if (data.access_token != '') {
                        //sucess
                        $('.login').fadeOut(500);
                        //submit form
                        $("#loginForm").submit();

                    }
                }, 2400);
            },
            error: function (xhr, errormsg) {
                console.log("error msg : " + errormsg);
                setTimeout(function () {
                    $('.login').removeClass('testtwo'); // remove pan effect
                }, 2000);
                setTimeout(function () {
                    $('.login').removeClass('test'); // remove tilt effect
                    $('.login div').fadeIn(100);
                }, 2400);
            },
            complete: function (xhr) {
                console.log("xhr.readyState : " + xhr.readyState);
                console.log("xhr.status : " + xhr.status);
            }
        });
    }
}