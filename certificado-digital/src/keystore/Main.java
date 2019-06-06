package keystore;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;

public class Main {

    public static void main(String[] args) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStore keyStore = KeyStore.getInstance(new File("./keystore" ), new char[] {'a', 'b', '1','2','3'});
        keyStore.aliases().asIterator().forEachRemaining(alias -> {
            try {
                Certificate abstractContract = keyStore.getCertificate(alias);
                if (abstractContract.getType().equals("X.509")) {
                    X509Certificate certificate = (X509Certificate) abstractContract;
                    String issuer = certificate.getIssuerDN().getName();
                    String subject = certificate.getSubjectDN().getName();
                    System.out.println(String.format("Emissor: %s", issuer));
                    System.out.println(String.format("Proprietário: %s", subject));
                    System.out.println(String.format("Auto-assinado: %s", subject.equals(issuer) ? "Sim" : "Não"));
                    System.out.println();
                }
            } catch (KeyStoreException e) {
                throw new RuntimeException(String.format("Certificado não encontrado: %s", alias));
            }
        });
    }

}
