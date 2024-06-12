import java.net.*;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) {
        ServerSocket srv = null;
        try {
            // Définit le port, utilise 80 par défaut si aucun argument n'est donné
            int port = (args.length > 0) ? Integer.parseInt(args[0]) : 80;
            srv = new ServerSocket(port);
            System.out.println("CTRL + C pour arrêter le serveur");
            System.out.println("En attente d'une connexion sur le port " + port + "...");
            System.out.println("Veuillez entrer l'adresse : http://localhost:" + port + "/index.html dans votre navigateur");

            while (true) {
                // Accepte une nouvelle connexion
                Socket socket = srv.accept();
                System.out.println("Connexion du client " + socket.getInetAddress());
                traitement_requete(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (srv != null && !srv.isClosed()) {
                try {
                    srv.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void traitement_requete(Socket socket) throws IOException {
        // Initialisation des flux pour lire la requête et envoyer la réponse
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        // Lecture de la requête
        String requete = br.readLine();
        if (requete == null || requete.isEmpty()) {
            socket.close();
            return;
        }

        System.out.println("Requête reçue: " + requete);

        // Séparation de la requête pour obtenir le chemin du fichier demandé
        String[] part = requete.split(" ");
        if (part.length < 2) {
            // Envoie une réponse 404 si la requête est un échec
            String reponse404 = """
                HTTP/1.1 404 Not Found\r
                Content-Type: text/html\r
                \r
                <html><body><h1>404 Not Found</h1></body></html>""";
            dos.writeBytes(reponse404);
            dos.flush();
            return;
        }

        // Extraction du chemin du fichier, on retire le premier élément (/) de la chaîne de caractères
        String lien = part[1].substring(1);

        if (lien.isEmpty()) {
            // Page par défaut
            lien = "index.html";
        }

        File file = new File(lien);
        if (!file.exists() || file.isDirectory()) {
            // Envoie une réponse 404 si le fichier n'existe pas ou est un répertoire
            String reponse404 = """
                HTTP/1.1 404 Not Found\r
                Content-Type: text/html\r
                \r
                <html><body><h1>404 Not Found</h1></body></html>""";
            dos.writeBytes(reponse404);
            dos.flush();
        }

        // Lecture du fichier et envoi du contenu au client
        try{
            FileInputStream fichier = new FileInputStream(file);
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
