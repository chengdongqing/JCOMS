### 发送器
- 支持发送邮件
- 支持发送短信
- 支持动态选择执行发送任务的邮件和短信发送器
- 支持扩展发送器和发送模板
- 统一的设计风格和方便的链式调用

#### 使用
- 验证码发送器
```
@Autowired
private VerificationCode verificationCode;

public void test() {
    // 发送短信验证码
    verificationCode.send("19999999999", SmsTemplate.LOGIN);
    // 发送邮件验证码
    verificationCode.send("19999999999@qq.com", EmailTemplate.BIND_ACCOUNT);
    // 校验验证码
    String account = ...
    String code = ...
    verificationCode.verify(account, code);
}
```