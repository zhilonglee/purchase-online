var TIME = {
    CountdownHour : function () {
        var time_start = new Date().getTime(); //set current time
        //var times = $("#closed_group").val();
        var currentHour = new Date().getHours();
        var time_end = new Date().setHours(currentHour + 1,0,0);
        // var time_end = new Date(times).getTime(); //set target time
        var time_differences = time_end - time_start;// calculate time differences
        if (time_differences < 1000) {
            TIME.CountdownHour();
            ITEM.seckillList();
            return false;
        }
        //Days
        var int_day = Math.floor(time_differences /86400000);
        // Hours
        time_differences -= int_day * 86400000;
        var int_hour = Math.floor(time_differences / 3600000);
        // Minutes
        time_differences -= int_hour * 3600000;
        var int_minute = Math.floor(time_differences / 60000);
        // Seconds
        time_differences -= int_minute * 60000;
        var int_second = Math.floor(time_differences / 1000);

        //When the number of minutes and seconds is singular, add zero in front
        if(int_day==0){
            int_day = '';
        } else if (int_day < 10) {
            int_day = "0" + int_day+' Days';
        }else{
            int_day =  int_day+' Days';
        }
        if(int_hour==0){
            int_hour = '';
        } else if (int_hour < 10) {
            int_hour = "0" + int_hour+':';
        }else{
            int_hour = int_hour+':';
        }
        if(int_minute==0){
            int_minute = '00:';
        } else if (int_minute < 10) {
            int_minute = "0" + int_minute+':';
        }else{
            int_minute =  int_minute+':';
        }
        if(int_second==0){
            int_second = '00';
        } else if (int_second < 10) {
            int_second = "0" + int_second;
        }else{
            int_second = int_second;
        }
        if(int_day==0 && int_hour==0 && int_hour=='00' && int_second=='00' ){
            int_time="End";
        }

// show time
        var int_time=int_day+int_hour+int_minute+int_second;
        $('#countdownTime').html(int_time);
// set timer
        setTimeout("TIME.CountdownHour()", 1000);

    }
};