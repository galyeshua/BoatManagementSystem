package bms.encryption;

import java.security.MessageDigest;

public class Encryptor {

    public static String Encrypt(String originalString){
        String algorithm = "SHA";
        byte[] plainText = originalString.getBytes();
        String encreptedString = "enc: '"; // add enc: ' to start of result
        // hash the original string
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.reset();
            md.update(plainText);
            byte[] encodedPassword = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < encodedPassword.length; i++) {
                if ((encodedPassword[i] & 0xff) < 0x10)
                    sb.append("0");
                sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
            }
            encreptedString = encreptedString.concat(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        encreptedString = encreptedString.concat("'");  // add ' to end of result
        return encreptedString; // the returned string format: "enc: 'ENCRYPTEDSTRING'"
    }

    public static boolean isEncrypted(String encryptedString){
        boolean sizeIsMatch = encryptedString.length()==47;
        boolean stringIsInEncryptedFormat = encryptedString.startsWith("enc: '") && encryptedString.endsWith("'");

        return stringIsInEncryptedFormat && sizeIsMatch;
    }
}
