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
            System.out.println("Afficher l'état de la machine: http://localhost:" + config.getPort() + "/status");

            // Condition infinie
            while (true) {
                // Accepte une nouvelle connexion
                Socket socket = srv.accept();
                Log.write("Vérification de la connexion du client " + socket.getInetAddress(), config.getAccess());
                if(verifierConnexion(socket.getInetAddress())){
                    // Traite la requête
                    Gerer_Requete.handleRequest(socket, config, false);
                }else{
                    Log.write("Autorisation non valide", config.getAccess());
                    System.out.println("IP non valide (HttpServer.java, verifierConnexion)");
                    Gerer_Requete.handleRequest(socket, config, true);
                }
            }
        } catch (IOException e) {
            //Affichage de l'erreur
            Log.write(e.getMessage(), config.getError());
        }
    }

    /**
     * Vérification que l'adresse ip n'est pas refusée, mais acceptée ou localhost
     * @param ip du client
     * @return false si refusé
     */
    public boolean verifierConnexion(InetAddress ip) {
        // Vérifie si l'adresse IP est localhost
        if (ip.isLoopbackAddress()) {
            Log.write("Connexion autorisée : adresse IP locale", config.getAccess());
            return true;
        }

        // Vérifie si l'adresse IP est dans la liste des adresses IP autorisées
        if (config.getAccept().contains(ip.getHostAddress())) {
            Log.write("Connexion autorisée : " + ip, config.getAccess());
            return true;
        }

        // Vérifie si l'adresse IP est dans la liste des adresses IP refusées
        if (config.getReject().contains(ip.getHostAddress())) {
            Log.write("Connexion refusée : " + ip, config.getAccess());
            return false;
        }

        // Si l'adresse IP n'est ni autorisée ni refusée, elle est refusée par défaut
        Log.write("Connexion refusée : " + ip, config.getAccess());
        return false;
    }
}

