import java.io.*;
import java.net.*;

public class HttpServer {
    private ServerConfig config;

    public HttpServer(ServerConfig config) {
        this.config = config;
    }

    public void startServer() {
        try {
            // Ouvre le serveur sur le port
            ServerSocket srv = new ServerSocket(config.getPort());

            // Informations de suivi du processus
            System.out.println("En attente de connexion sur le port " + config.getPort() + "...");
            System.out.println("Afficher la page: http://localhost:" + config.getPort() + "/index.html");

            // Condition infinie
            while (true) {
                // Accepte une nouvelle connexion
                Socket socket = srv.accept();
                Log.write("Connexion du client " + socket.getInetAddress(), config.getAccessLogPath());

                // Traite la requête
                Gerer_Requete.handleRequest(socket, config);
            }
        } catch (IOException e) {
            Log.write(e.getMessage(), config.getErrorLogPath());
        }
    }
}

