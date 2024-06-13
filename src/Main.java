public class Main {
    public static void main(String[] args) {
        try {
            // Initialisation de la configuration
            ServerConfig config = new ServerConfig("configuration.xml");

            // Création du serveur
            HttpServer server = new HttpServer(config);

            // Suppression des fichiers de log
            Log.clearLog(config.getAccessLogPath());
            Log.clearLog(config.getErrorLogPath());

            server.startServer();
        } catch (Exception e) {
            e. printStackTrace();
        }
    }
}
