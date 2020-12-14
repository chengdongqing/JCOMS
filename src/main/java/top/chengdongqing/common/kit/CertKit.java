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
 * Certificate utility methods
 *
 * @author Luyao
 */
public class CertKit {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Calculates the certificate {@code SN} for ALIPAY
     *
     * @param certPath   the path of the cert
     * @param alipayRoot {@code true} if for ALIPAY root cert
     * @return the {@code SN} of the cert
     */
    public static String calcAlipayCertSN(String certPath, boolean alipayRoot) {
        try {
            return readCerts(certPath).stream().map(item -> {
                if (!alipayRoot || item.getSigAlgOID().startsWith("1.2.840.113549.1.1")) {
                    String signContent = item.getIssuerX500Principal().getName() + item.getSerialNumber();
                    return DigitalSigner.newInstance(SignatureAlgorithm.MD5).signature(signContent, null).toMD5Hex();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.joining("_"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the public key of the certificate
     *
     * @param certPath the path of the certificate
     * @return the public key of the cert
     */
    public static BytesToStr readPublicKey(String certPath) {
        return BytesToStr.of(readFirstCert(certPath).getPublicKey().getEncoded());
    }

    /**
     * Reads the serial no of the certificate
     *
     * @param certPath the path of the cert
     * @return the hex string of the serial no of the cert
     */
    public static String readSerialNo(String certPath) {
        return readFirstCert(certPath).getSerialNumber().toString(16);
    }

    /**
     * Reads the first certificate from the cert file
     *
     * @param certPath the path of the cert file
     * @return the first cert with X.509
     */
    public static X509Certificate readFirstCert(String certPath) {
        try {
            return Objects.requireNonNull(readCerts(certPath).get(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Reads all certificate with X.509 from the cert file</p>
     * <p>It maybe have many X.509 certs in one cert file, just like a {@code YAML} file</p>
     * <p>For example of the certificate content:</p>
     * <pre>
     * -----BEGIN CERTIFICATE-----
     * The first cert main content with BASE64 encoded
     * -----END CERTIFICATE-----
     *
     * -----BEGIN CERTIFICATE-----
     * The other cert main content with BASE64 encoded
     * -----END CERTIFICATE-----
     * </pre>
     *
     * @param certPath the path of the cert file
     * @return the list of the certificate with X.509 from the cert file
     */
    public static List<X509Certificate> readCerts(String certPath) throws Exception {
        // reads the cert file
        Path path = Path.of(CertKit.class.getResource(certPath).toURI());
        try (InputStream stream = Files.newInputStream(path)) {
            // generates certificates
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
            return (List<X509Certificate>) certFactory.generateCertificates(stream);
        }
    }
}
