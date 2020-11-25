package top.chengdongqing.common.kit;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 证书工具类
 *
 * @author Luyao
 */
public class CertKit {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 计算支付宝证书SN
     *
     * @param certPath   证书路径
     * @param alipayRoot 是否为支付宝根证书
     * @return 证书SN
     */
    public static String calcAlipayCertSN(String certPath, boolean alipayRoot) {
        try {
            return readCerts(certPath).stream().map(item -> {
                if (!alipayRoot || item.getSigAlgOID().startsWith("1.2.840.113549.1.1")) {
                    String signContent = item.getIssuerX500Principal().getName() + item.getSerialNumber();
                    return fillMD5(DigitalSigner.signature(SignatureAlgorithm.MD5, signContent, null).toHex());
                }
                return null;
            }).filter(item -> item != null).collect(Collectors.joining("_"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取证书文件里的公钥
     *
     * @param certPath 证书文件路径
     * @return 公钥
     */
    public static BytesToStr readPublicKey(String certPath) {
        return BytesToStr.of(CertKit.readCert(certPath).getPublicKey().getEncoded());
    }

    /**
     * 读取证书序列号
     *
     * @param certPath 证书路径
     * @return 序列号
     */
    public static String readSerialNo(String certPath) {
        return CertKit.readCert(certPath).getSerialNumber().toString(16);
    }

    /**
     * 读取证书
     *
     * @param certPath 证书路径
     * @return X.509证书
     */
    public static X509Certificate readCert(String certPath) {
        try {
            return Objects.requireNonNull(readCerts(certPath).get(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取证书文件里的所有证书
     * 一个证书文件可包含多个证书，像YAML文件一样，可以有多段
     * 每个证书有特殊的开始和结束标识符
     * -----BEGIN CERTIFICATE-----
     * BASE64编码后的证书内容
     * -----END CERTIFICATE-----
     *
     * @param certPath 证书文件路径
     * @return X.509证书集合
     */
    public static List<X509Certificate> readCerts(String certPath) throws Exception {
        // 读取证书内容
        Path path = Path.of(CertKit.class.getResource(certPath).toURI());
        try (InputStream stream = Files.newInputStream(path)) {
            // 生成X.509证书集合
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
            return (List<X509Certificate>) certFactory.generateCertificates(stream);
        }
    }

    /**
     * 填充MD5的长度到32位
     *
     * @param md5 原始MD5
     * @return 32位长度的MD5值
     */
    private static String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }
}
