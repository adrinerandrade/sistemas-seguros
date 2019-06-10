package keystore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (FileInputStream fis = new FileInputStream(new File("./keystore"))) {
            keyStore.load(fis, new char[]{'a', 'b', '1', '2', '3'});
        }

        final Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            final String alias = aliases.nextElement();
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
        }
    }

}
