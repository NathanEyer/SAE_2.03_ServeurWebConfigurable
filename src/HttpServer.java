import java.net.*;
import java.io.*;

/**
 * Serveur Web configurable
 */
public class HttpServer {
    private static ServerSocket srv;
    /**
     * Méthode principale de lancement du processus
     * @param args potentiel argument
     */
    public static void main(String[] args){
        try {
            // Définit le port, utilise 80 par défaut si aucun argument n'est donné
            int port = (args.length > 0) ? Integer.parseInt(args[0]) : 80;

            //Ouvre le serveur sur le port
            srv = new ServerSocket(port);

            //Informations de suivi du processus
            System.out.println("CTRL + C pour arrêter le serveur");
            System.out.println("En attente d'une connexion sur le port " + port + "...");
            System.out.println("Veuillez entrer l'adresse : http://localhost:" + port + "/index.html dans votre navigateur");

            //Condition infinie
            while (true) {
                // Accepte une nouvelle connexion
                Socket socket = srv.accept();
                System.out.println("Connexion du client " + socket.getInetAddress());

                //Traite la requête
                traitement_requete(socket);
            }
        } catch (IOException e) {
            System.err.println("Exception " + e);
        } finally {
            if (srv != null && !srv.isClosed()) {
                try {
                    srv.close();
                } catch (IOException e) {
                    System.err.println("Exception" + e);
                }
            }
        }
    }

    /**
     * Méthode de traitement de la requête
     * @param socket connexion
     * @throws IOException exception
     */
    private static void traitement_requete(Socket socket) throws IOException {
        // Initialisation des flux pour lire la requête et envoyer la réponse
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        //Réponse d'erreur par défaut
        String reponse404 = """
                HTTP/1.1 404 Not Found\r
                Content-Type: text/html\r
                \r
                <html><body><h1>404 Not Found</h1></body></html>""";

        // Lecture de la requête
        String requete = br.readLine();
        if (requete == null || requete.isEmpty()) {
            socket.close();
            return;
        }

        //affichage de la requête reçue
        System.out.println("Requête reçue: " + requete);

        // Séparation de la requête pour obtenir le chemin du fichier demandé
        String[] part = requete.split(" ");

        if (part.length < 2) {
            //Envoie la réponse par défaut
            dos.writeBytes(reponse404);
            dos.flush();
            return;
        }

        // Extraction du chemin du fichier, on retire le premier élément (/) de la chaîne de caractères
        String lien = part[1].substring(1);

        //Affiche le index.html par défaut
        if (lien.isEmpty()) {
            // Page par défaut
            lien = "index.html";
        }

        //Crée un fichier
        File file = new File(lien);
        if (!file.exists() || file.isDirectory()) {
            dos.writeBytes(reponse404);
            dos.flush();
        }

        // Lecture du fichier et envoi du contenu au client
        try{
            //Ouverture du fichier
            FileInputStream fichier = new FileInputStream(file);

            //Lecture du ficher
            byte[] content = fichier.readAllBytes();
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Longueur du document: " + content.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(content);
            dos.flush();
            fichier.close();
        }catch (FileNotFoundException e){
            System.err.println("Exception " + e);
        }
    }
}
