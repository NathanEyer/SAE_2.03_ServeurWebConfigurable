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

            //Ecriture de la page de status
            Status status = new Status();
            status.ecrireStatus();

            //Ecriture de la page de code dynamique
            CodeDynamique codeDynamique = new CodeDynamique("var/www/code.html");
            String bashCode = "#bin/bash\ndate";
            String cCode = "#include <stdio.h>\n#include <time.h>\n\n int main(void) {\n time_t now;\n time(&now);\n printf(\"%s\", ctime(&now));\n return 0;\n}";
            String pythonCode = "import time\nimport datetime\ntimestamp = time.time()\ndate_obj = datetime.datetime.fromtimestamp(timestamp)\nprint(date_obj)";
            String rubyCode = "require 'date'\nputs Date.today";
            String cPP = "#include <iostream> #include <ctime> #include <iomanip> int main() { std::time_t now = std::time(nullptr); std::tm* t = std::localtime(&now); std::cout << std::put_time(t, \"%Y-%m-%d\") << std::endl; return 0;}";
            codeDynamique.ecrireCodeDynamique(bashCode, cCode, pythonCode, rubyCode, cPP);

            //Lancement du serveur
            server.startServer();
        } catch (Exception e) {
            Log.write("" + e, config.getError());
        }
    }
}
