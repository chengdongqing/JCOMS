### 加解密器

- 支持基于AES的对称加解密
- 支持基于RSA的非对称加解密
- 支持基于口令和密钥的加解密

### 实际使用

- 微信小程序用户信息解密，使用AES/CBC/PKCS7Padding
- 微信支付V3回调通知解密，使用AES/GCM/NoPadding
- 微信支付V3敏感信息加解密，使用RSA/ECB/OAEPWithSHA-1AndMGF1Padding
- 等等...