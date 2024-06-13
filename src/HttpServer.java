import java.io.*;
import java.net.*;

public class HttpServer {

    private final ServerConfig config;

    /**
     * Objet server
     * @param config ServerConfig
     */
    public HttpServer(ServerConfig config) {
        this.config = config;
    }

    /**
     * Lancement du serveur
     */
    public void startServer() {
        try (ServerSocket srv = new ServerSocket(config.getPort());) {
            // Informations de suivi du processus
            System.out.println("En attente de connexion sur le port " + config.getPort() + "...");
            System.out.println("Afficher la page: http://localhost:" + config.getPort() + "/index.html");

            // Condition infinie
            while (true) {
                // Accepte une nouvelle connexion
                Socket socket = srv.accept();
                Log.write("Connexion du client " + socket.getInetAddress(), config.getAccess());

                // Traite la requête
                Gerer_Requete.handleRequest(socket, config);
            }
        } catch (IOException e) {
            //Affichage de l'erreur
            Log.write(e.getMessage(), config.getError());
        }
    }
}

