<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Email Verification</title>
    <style>
        body {
            background-color: #ECECEC;
        }

        span {
            color: #ff8c00;
            font-weight:bold;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            border-radius: 5px;
            font-family: "微软雅黑", 黑体;
            font-size: 14px;
            line-height: 1.5;
            box-shadow: 0 0 5px rgb(153, 153, 153);
        }

        .header {
            padding: 15px 35px;
            background-color: RGB(148, 0, 211);
            color: #fff;
            border-radius: 5px 5px 0 0;
        }

        .content {
            padding: 25px 35px 40px;
            background-color: #fff;
            opacity: 0.8;
        }

        .message {
            margin: 5px 0;
        }

        .signature {
            padding: 10px;
            border-top: 1px solid #ccc;
            color: #747474;
            font-size: 12px;
            margin: 0 9% 0 9%;
        }

        .wrapper {
            width: 90%;
            margin: 0 auto;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="header">
        <h1>Imall团队</h1>
    </div>
    <div class="content">
        <div class="wrapper">
            <h2 class="message">尊敬的用户：${name}</h2>
            <p>您好！感谢您使用Imall，您的账号正在进行邮箱验证，验证码为：<span>${verificationCode}</span>，有效期30分钟，请尽快填写验证码完成验证！</p>
            <br>
            <h2 class="message">Dear user:${name}</h2>
            <p>Hello! Thanks for using Imall, your account is being authenticated by email, the verification code is:
                <span>${verificationCode}</span>, valid for 30 minutes. Please fill in the verification code as soon as possible!</p>
        </div>
    </div>
    <div class="signature">
        <p>Imall团队</p>
        <p>联系我们：110</p>
        <br>
        <p>此为系统邮件，请勿回复</p>
        <p>Please do not reply to this system email</p>
    </div>
</div>
</body>
</html>
