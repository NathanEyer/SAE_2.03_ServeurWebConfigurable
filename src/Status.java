import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Affichage de l'état de la machine sur une page web
 */
public class Status {
    /**
     * Récupération de la mémoire dispo
     * @return mémoire dispo
     */
    public int memoireDispo(){
        return Math.round((float) Runtime.getRuntime().freeMemory() / (1024*1024));
    }

    /**
     * Récupération de l'espace disque utilisable
     * @return long
     */
    public int espaceDisque(){
        File disque = File.listRoots()[0];
        return Math.round((float) disque.getUsableSpace() / (1024*1024*1024));
    }

    /**
     * Récupération du nombre de processus en cours
     * @return int
     */
    public int nbProcessus(){
        return Runtime.getRuntime().availableProcessors();
    }


    /**
     * permet d'écrire dans le fichier status, les informations suivantes :
     * La mémoire disponible
     * L'espace disque disponible
     * Le nombre de processus
     */
    public void ecrireStatus(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("var/www/status.html"));
            String status = "<!DOCTYPE html>\n<head>\n<meta charset=\"UTF-8\">\n<title>STATUS</title>\n</head>\n<body>\n<h1>ETAT DE LA MACHINE:</h1>\n";
            status += "<h2>Mémoire disponible: " + this.memoireDispo() + " Mo.</h2>\n";
            status += "<h2>Espace disque disponible: " + this.espaceDisque() + " Go.</h2>\n";
            status += "<h2>Nombre de processus: " + this.nbProcessus() +  " processeurs.</h2>\n";
            status += "</body>\n</html>";
            writer.write(status);
            writer.flush();
            writer.close();
            Log.write("Création de la page de l'état de la machine effectuée", "var/log/myweb/access.log");
        } catch (IOException e) {
            Log.write(e.getMessage(), "var/log/myweb/error.log");
        }
    }
}
