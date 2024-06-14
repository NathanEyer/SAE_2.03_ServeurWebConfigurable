import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;

/**
 * Classe représentant la configuration du serveur.
 * Cette classe lit les paramètres de configuration depuis un fichier XML.
 */
public class ServerConfig {
    //Valeurs récupérées dans le xml
    private final int port;
    private final String root;
    private final String access;
    private final String error;
    private final String accept;
    private final String reject;

    /**
     * Lit le fichier XML de configuration et initialise les variables.
     * @param nomXml Chemin vers le fichier XML de configuration.
     */
    public ServerConfig(String nomXml){
        //Initialisation des constructeurs XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document xml = null;

        //Récupération des valeurs du xml
        try {
            builder = factory.newDocumentBuilder();
            xml = builder.parse(new File(nomXml));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.write(e.getMessage(), this.getError());
        }

        // Récupère et initialise les valeurs des balises de configuration avec des valeurs par défaut si nécessaire
        assert xml != null;
        this.port = Integer.parseInt(getTagValue(xml, "port", "8080"));
        this.root = getTagValue(xml, "root", "var/www");
        this.access = getTagValue(xml, "accesslog", "var/log/myweb/access.log");
        this.error = getTagValue(xml, "errorlog", "var/log/myweb/error.log");
        this.accept = getTagValue(xml, "accept", "192.168.0.0/24");
        this.reject = getTagValue(xml, "reject", "192.168.1.0/24");
    }

    /**
     * Récupère la valeur d'un tag dans le document XML.
     * Si le tag n'est pas présent, retourne une valeur par défaut.
     * @param doc Document XML.
     * @param tag Nom du tag.
     * @param defaultValue Valeur par défaut si le tag n'est pas présent.
     * @return Valeur du tag ou la valeur par défaut.
     */
    private String getTagValue(Document doc, String tag, String defaultValue) {
        NodeList tagList = doc.getElementsByTagName(tag);
        Node tagNode = tagList.item(0);
        return (tagNode != null) ? tagNode.getTextContent() : defaultValue;
    }

    /**
     * Retourne le port du serveur.
     * @return Port du serveur.
     */
    public int getPort() {
        return port;
    }

    /**
     * Retourne le chemin racine du serveur.
     * @return Chemin racine du serveur.
     */
    public String getRoot() {
        return root;
    }

    /**
     * Retourne le chemin du fichier de log des accès.
     * @return Chemin du fichier de log des accès.
     */
    public String getAccess() {
        return access;
    }

    /**
     * Retourne le chemin du fichier de log des erreurs.
     * @return Chemin du fichier de log des erreurs.
     */
    public String getError() {
        return error;
    }

    /**
     * Retourne l'adresse IP acceptée
     * @return IP
     */
    public String getAccept() {
        return accept;
    }

    /**
     * Retourne l'adresse IP refusée
     * @return IP
     */
    public String getReject() {
        return reject;
    }
}
