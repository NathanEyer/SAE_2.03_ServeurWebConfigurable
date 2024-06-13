import java.io.*;
import java.net.Socket;

/**
 * Classe qui récup
 */
public class Gerer_Requete {
    /**
     * Méthode d'affichage de la page
     * @param socket socket
     * @param config server
     * @throws IOException exception
     */
    public static void handleRequest(Socket socket, ServerConfig config) throws IOException {
        //Ouverture des flux
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        //Récupération de la demande
        String requete = br.readLine();
        if (requete == null || requete.isEmpty()) {
            socket.close();
            return;
        }

        //Affichage du suivi
        Log.write("Requête reçue: " + requete, config.getAccess());

        //Récupération du lien
        String[] part = requete.split(" ");
        String lienB = config.getRoot() + "/" + part[1].substring(1);

        //Page en cas d'erreur
        String reponse404 = """
                HTTP/1.1 404 Not Found\r
                Content-Type: text/html\r
                \r
                <html><body><h1>Le document <a href=\"""" + lienB + "\">" + lienB
                + "</a> n'existe pas, sorry.</h1></body></html>";

        //Ouverture du =='un fichier
        File file = new File(lienB);
        try (FileInputStream fichier = new FileInputStream(file)) {
            //Lecture du fichier
            byte[] content = fichier.readAllBytes();
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("\r\n");
            dos.write(content);
            dos.flush();
        } catch (FileNotFoundException e) {
            //Affichage de la page d'erreur
            dos.writeBytes(reponse404);
            dos.flush();
            //Ecriture de l'erreur
            Log.write(e.getMessage(), config.getError());
            Log.write("Connexion refusée: " + e.getMessage(), config.getAccess());
        }
    }
}

