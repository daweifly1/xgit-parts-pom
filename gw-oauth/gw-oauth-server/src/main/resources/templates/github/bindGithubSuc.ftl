<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>绑定GitHub账号成功</title>
</head>
<body>
您已经成功绑定GitHub账号
<script type="text/javascript">
    //设定倒数秒数
    var t = 3;
    //显示倒数秒数
    function showTime(){
        t -= 1;
        document.getElementById('div1').innerHTML= t;
        if(t==0){
            location.href='/';
        }
        //每秒执行一次,showTime()
        setTimeout("showTime()",1000);
    }
    //执行showTime()
    showTime();
</script>
</body>
</html>
