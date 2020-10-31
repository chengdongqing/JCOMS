### 加解密器
- 支持基于AES的对称加解密，固定长度的加解密密钥
- 支持基于PBE的口令加解密，这里指任意长度的密码，依然基于AES加解密

#### 使用
- AES加解密：
```
public static void main(String[] args) {
    AESEncryptor me = AESEncryptor.me();
    String content = "324idkjfhjk 你好ask就 321";
    String key = "qwertyuiopasdfghjklzxcvbnm123456";
    byte[] bytes = me.encrypt(content.getBytes(), key);
    System.out.println(Base64.getEncoder().encodeToString(bytes));
    byte[] decrypt = me.decrypt(bytes, key);
    System.out.println(new String(decrypt));

    // 若微信小程序用户数据解密，将iv与密文合并后执行解密，可调用aesEncryptor.mergeBytes()
}
```
- PEB加解密：
```
public static void main(String[] args) {
    AESEncryptor me = AESEncryptor.me();
    String content = "324idkjfhjk 你好ask就 321";
    String key = "qwertyuiopasdfghjklzxcvbnm123456";
    String password = "1212";
    byte[] bytes = me.encrypt(content.getBytes(), key, password);
    System.out.println(Base64.getEncoder().encodeToString(bytes));
    byte[] decrypt = me.decrypt(bytes, key, password);
    System.out.println(new String(decrypt));
}
```
- RSA加解密：
```
public static void main(String[] args) {
    SecretKeyPair secretKeyPair = SecretKeyGenerator.generateKeyPair(SignatureAlgorithm.SHA256_RSA);
    RSAEncryptor me = RSAEncryptor.me();
    String content = "324idkjfhjk 你好ask就 321";
    byte[] bytes = me.encrypt(content.getBytes(), secretKeyPair.publicKey());
    System.out.println(Base64.getEncoder().encodeToString(bytes));
    byte[] decrypt = me.decrypt(bytes, secretKeyPair.privateKey());
    System.out.println(new String(decrypt));
}
```