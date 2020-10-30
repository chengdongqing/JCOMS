### 加解密器
- 支持基于AES的对称加解密，固定长度的加解密密钥
- 支持基于PBE的口令加解密，这里指任意长度的密码，依然基于AES加解密

#### 使用
```
public static void main(String[] args) {
    IEncryptor aesEncryptor = new AESEncryptor();
    String content = "239879832 f f你好 发";
    String key = "12345678901234561234567890123456"; //16/32字节密钥
    String password = "8473";
    
    byte[] encrypt = aesEncryptor.encrypt(content.getBytes(), key.getBytes());
    System.out.println(Base64.getEncoder().encodeToString(encrypt));
    byte[] decrypt = aesEncryptor.decrypt(encrypt, key.getBytes());
    System.out.println(new String(decrypt));

    byte[] encrypt1 = aesEncryptor.encrypt(content.getBytes(), key.getBytes(), password);
    System.out.println(Base64.getEncoder().encodeToString(encrypt1));
    byte[] decrypt1 = aesEncryptor.decrypt(encrypt1, key.getBytes(), password);
    System.out.println(new String(decrypt1));

    // 若微信小程序用户数据解密，将iv与密文合并后执行解密，可调用aesEncryptor.mergeBytes()
}
```