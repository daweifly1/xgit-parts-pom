<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>GitHub登录失败</title>
</head>
<body>
您通过GitHub账号登录失败，原因 <span th:text="${error}">请尝试其他方式登录</span>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>setTimeout</title>
</head>
<body>
<div id='div1'>  </div>

</body>
</html>

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
