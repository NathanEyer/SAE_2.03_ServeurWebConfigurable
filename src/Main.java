/**
 * Lancement du serveur HTTP
 */
public class Main {
    private static ServerConfig config;
    public static void main(String[] args) {
        try {
            // Initialisation de la configuration
            config = new ServerConfig("configuration.xml");

            // Création du serveur
            HttpServer server = new HttpServer(config);

            // Suppression des fichiers de log
            Log.clearLog(config.getAccess());
            Log.clearLog(config.getError());

            Status status = new Status();
            status.ecrireStatus();

            //Lancement du serveur
            server.startServer();
        } catch (Exception e) {
            Log.write("" + e, config.getError());
        }
    }
}
