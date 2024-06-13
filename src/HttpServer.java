import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.net.*;
import java.io.*;

/**
 * Serveur Web configurable
 */
public class HttpServer {
    private static final String CONFIG_XML = "configuration.xml";
    private static int port;
    private static String lien;
    private static Document config;
    private final String accept;
    private final String reject;
    private static String access;
    private static String error;

    /**
     * Récupére les valeurs du fichier xml
     */
    public HttpServer(){
        // Récupération de la balise port
        NodeList portTag  = config.getElementsByTagName("port");
        Node portNode = portTag.item(0);
        String portString = portNode.getTextContent();

        // Définit le port, utilise 80 par défaut si aucun argument n'est donné
        if(portString.isEmpty()){
            port = 80;
        }else port = Integer.parseInt(portString);

        //Récupération de la balise root
        NodeList rootTag = config.getElementsByTagName("root");
        Node rootNode = rootTag.item(0);
        lien = rootNode.getTextContent();

        //Récupération de la balise accept
        NodeList acceptTag = config.getElementsByTagName("accept");
        Node acceptNode = acceptTag.item(0);
        accept = acceptNode.getTextContent();

        //Récupération de la balise reject
        NodeList rejectTag = config.getElementsByTagName("reject");
        Node rejectNode = rejectTag.item(0);
        reject = rejectNode.getTextContent();

        //Récupération de la balise access
        NodeList accessTag = config.getElementsByTagName("accesslog");
        Node accessNode = accessTag.item(0);
        access = accessNode.getTextContent();

        //Récupération de la balise error
        NodeList errorTag = config.getElementsByTagName("errorlog");
        Node errorNode = errorTag.item(0);
        error = errorNode.getTextContent();
    }

    /**
     * Méthode principale de lancement du processus
     * @param args potentiel argument
     */
    public static void main(String[] args) throws IOException {
        try {
            //Initialisation des constructeurs
            DocumentBuilderFactory protection = DocumentBuilderFactory.newInstance();
            DocumentBuilder construction = protection.newDocumentBuilder();

            //Ouverture du fichier
            config = construction.parse(CONFIG_XML);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException();
        }

        //Création du serveur
        HttpServer server = new HttpServer();

        //Suppression des fichiers
        File accessSup = new File(access);
        accessSup.delete();
        File errorSup = new File(error);
        errorSup.delete();

        server.creationServeur();
    }

    public void creationServeur(){
        ServerSocket srv;
        try {
            //Ouvre le serveur sur le port
            srv = new ServerSocket(port);

            //Informations de suivi du processus
            System.out.println("En attente de connexion sur le port " + port + "...");
            System.out.println("Afficher la page: http://localhost:" + port + "/index.html");

            //Condition infinie
            while (true) {
                // Accepte une nouvelle connexion
                Socket socket = srv.accept();
                ecritureAccess("Vérification de la connexion du client " + socket.getInetAddress());
                if(verifierConnexion(socket.getInetAddress())){
                    //Traite la requête
                    traitement_requete(socket);
                }else{
                    ecritureError("" + new Error("Connexion refusée, " + socket.getInetAddress()) + " n'est pas autorisé.");
                }
            }
        } catch (IOException e) {
            ecritureError("" + e);
        }
    }

    /**
     * Méthode de traitement de la requête
     * @param socket connexion
     * @throws IOException exception
     */
    private void traitement_requete(Socket socket) throws IOException {
        // Initialisation des flux pour lire la requête et envoyer la réponse
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        // Lecture de la requête
        String requete = br.readLine();
        if (requete == null || requete.isEmpty()) {
            socket.close();
            return;
        }

        //affichage de la requête reçue
        this.ecritureAccess("Requête reçue: " + requete);

        // Séparation de la requête pour obtenir le chemin du fichier demandé
        String[] part = requete.split(" ");

        // Extraction du chemin du fichier, on retire le premier élément (/) de la chaîne de caractères
        String lienB = lien + "/" + part[1].substring(1);

        //Réponse d'erreur par défaut
        String reponse404 = """
                HTTP/1.1 404 Not Found\r
                Content-Type: text/html\r
                \r
                <html><body><h1>Le document <a href=\"""" + lienB + "\">" + lienB
                + "</a> n'existe pas</h1></body></html>";


        //Crée un fichier
        File file = new File(lienB);
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
            ecritureError("" + e);
        }
    }

    /**
     * Méthode d'écriture dans le fichier access.log
     * @param ajout chaîne à ajouter
     */
    public void ecritureAccess(String ajout){
        try (FileWriter fw = new FileWriter(access, true)){
            //Ajout de la chaîne de caractère
            fw.write(ajout + "\n");
        } catch (IOException e) {
            ecritureError("" + e);
        }
    }

    /**
     * Méthode d'écriture dans le fichier error.log
     * @param ajout chaîne à ajouter
     */
    public void ecritureError(String ajout){
        try (FileWriter fw = new FileWriter(error, true)){
            //Ajout de la chaîne de caractère
            fw.write(ajout + "\n");
        } catch (IOException e) {
            ecritureError("" + e);
        }
    }

    /**
     * Vérifie la connexion du client
     * @param ip adresse ipp du client
     * @return false si pas acceptée
     */
    public boolean verifierConnexion(InetAddress ip){
        String clientIp = ip.getHostAddress();
        String clientHost = ip.getHostName();

        // Comparaison des adresses IP avec les valeurs accept et reject
        if (clientIp.equals(reject) || clientHost.equals(reject)) {
            return false;
        }
        if (clientIp.equals(accept) || clientHost.equals(accept)) {
            return true;
        }

        // Accepter par défaut si aucune condition n'est remplie
        return true;
    }
}
