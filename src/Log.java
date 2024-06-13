import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe qui s'occupe d'écrire les informations
 * de suivi et d'erreurs dans les fichiers correspondants
 */
public class Log {
    /**
     * Ecriture des messages de suivi et d'erreurs
     * @param message texte à rajouter
     * @param log fichier ou écrire le message
     */
    public static void write(String message, String log) {
        try (FileWriter fw = new FileWriter(log, true)) {
            //Ecriture du message
            fw.write(message + "\n");
        } catch (IOException e) {
            //Rappel de la fonction si exception
            write("" + e, log);
        }
    }

    /**
     * Suppression du contenu des fichiers de suivi
     * @param log fichier à vider
     */
    public static void clearLog(String log) {
        try (FileWriter fw = new FileWriter(log, false)) {
            //Remplacement de tout le texte par rien
            fw.write("");
        } catch (IOException e) {
            //Rappel de la fonction si exception
            write("" + e, log);
        }
    }
}
