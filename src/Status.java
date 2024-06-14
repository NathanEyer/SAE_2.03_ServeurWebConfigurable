import java.io.File;

/**
 * Affichage de l'état de la machine sur une page web
 */
public class Status {
    /**
     * Récupération de la mémoire dispo
     * @return mémoire dispo
     */
    public long memoireDispo(){
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Récupération de l'espace disque utilisable
     * @return long
     */
    public long espaceDisque(){
        File path = File.listRoots()[0];
        return path.getUsableSpace();
    }

    /**
     * Récupération du nombre de processus en cours
     * @return int
     */
    public int nbProcessus(){
        return Runtime.getRuntime().availableProcessors();
    }
}
