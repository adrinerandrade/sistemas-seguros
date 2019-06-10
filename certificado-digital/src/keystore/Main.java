package keystore;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (FileInputStream fis = new FileInputStream(new File("./keystore"))) {
            keyStore.load(fis, new char[]{'a', 'b', '1', '2', '3'});
        }

        HashMap<Integer, PublicKey> publicKeyHashMap = new HashMap<>();
        int count = 1;
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

                    publicKeyHashMap.put(count, certificate.getPublicKey());
                    count++;
                }
            } catch (KeyStoreException e) {
                throw new RuntimeException(String.format("Certificado não encontrado: %s", alias));
            }
        }

        writeFile(new File("key.aes"), Files.readAllBytes(Paths.get("key.txt")), publicKeyHashMap.get(1));
    }

    private static void writeFile(File output, byte[] bytes, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        if(output.exists()){
            output.delete();
        }

        FileOutputStream fos = new FileOutputStream(output);
        fos.write(cipher.doFinal(bytes));
        fos.flush();
        fos.close();

        System.out.println("Chave encriptada:" + output.toString());
    }

}
