import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Encoder64 {

    /**
     * Encode un tableau d'octets en base64
     * @param data tableau d'octets à encoder
     * @return chaîne encodée en base64
     */
    public static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Détermine si le type de contenu est binaire
     * @param type type de contenu
     * @return vrai si le type de contenu est binaire, sinon faux
     */
    static boolean etreBinaire(String type) {
        return type != null && (type.startsWith("image/") || type.startsWith("audio/") || type.startsWith("video/"));
    }
}
