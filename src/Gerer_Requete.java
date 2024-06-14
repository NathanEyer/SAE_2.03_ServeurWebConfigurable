import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

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
    public static void handleRequest(Socket socket, ServerConfig config, boolean refuse) throws IOException {
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
        String demande = part[1].substring(1);
        String lienB = config.getRoot() + "/" + demande;

        //Si l'autorisation est refusée, affichage de la page 403
        if(refuse){
            //Affichage de la page d'erreur
            dos.writeBytes("HTTP/1.1 403 Forbidden\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<html><body><h1>Accès interdit au document <a href=\""
                    + lienB + "\">" + lienB + "</a>, désolé.</h1></body></html>");
            dos.flush();
        }

        //Page en cas d'erreur 404
        String reponse404 = """
                HTTP/1.1 404 Not Found\r
                Content-Type: text/html\r
                \r
                <html><body><h1>Le document <a href=\"""" + demande + "\">" + demande
                + "</a> n'existe pas, sorry.</h1></body></html>";

        if(demande.equals("status")){
            File file = new File("var/www/status.html");
            try (FileInputStream fichier = new FileInputStream(file)) {
                //Lecture du fichier
                byte[] content = fichier.readAllBytes();
                dos.writeBytes("HTTP/1.1 200 OK\r\n");
                dos.writeBytes("\r\n");
                dos.write(content);
                dos.flush();
                return;
            } catch (FileNotFoundException e) {
                //Affichage de la page d'erreur
                dos.writeBytes(reponse404);
                dos.flush();
                //Ecriture de l'erreur
                Log.write(e.getMessage(), config.getError());
                Log.write("Connexion refusée: " + e.getMessage(), config.getAccess());
                return;
            }
        }
        System.out.println(lienB);


        // Ouverture du fichier
        File file = new File(lienB);
        try (FileInputStream fichier = new FileInputStream(file)) {
            byte[] content = fichier.readAllBytes();
            String contentType = Files.probeContentType(file.toPath());

            if (Encoder64.etreBinaire(contentType)) {
                String base64Content = Encoder64.encodeBase64(content);
                dos.writeBytes("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\nContent-Encoding: base64\r\nContent-Length: " + base64Content.length() + "\r\n\r\n");
                dos.writeBytes(base64Content);
            } else {
                dos.writeBytes("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\n\r\n");
                dos.write(content);
            }

            dos.flush();
        } catch (FileNotFoundException e) {
            // Affichage de la page d'erreur
            dos.writeBytes(reponse404);
            dos.flush();
            // Ecriture de l'erreur
            Log.write(e.getMessage(), "error.log");
            Log.write("Connexion refusée: " + e.getMessage(), "error.log");
        }
    }
}


