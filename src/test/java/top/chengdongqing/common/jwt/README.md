### token生成与验证
- 默认使用EdDSA非对称加密算法生成数字签名
- 支持动态修改密钥和有效时长
- 默认有效时长单位为分钟

#### 使用
```
@Autowired
private TokenOperator tokenOperator;

public void test() {
    Map<String, String> payloads = new HashMap<>();
    payloads.put("id", 100);
    payloads.put("name", "你好");
    Token token = tokenOperator.generate(payloads);
    System.out.println(token.token());

    boolean isOk = tokenOperator.validate(token.token());
    System.out.println("有效：" + isOk);

    JSONObject payloads = tokenOperator.getPayloads(token);
    System.out.println("id：" + payloads.getInteger("id"));
}
```

#### 配置
application.yml
```
jwt:
    publicKey: ...
    privateKey: ...
    duration: ...
```