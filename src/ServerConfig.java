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
    private final int port;
    private final String root;
    private final String access;
    private final String error;

    /**
     * Constructeur de la classe ServerConfig.
     * Lit le fichier XML de configuration et initialise les variables d'instance.
     *
     * @param configPath Chemin vers le fichier XML de configuration.
     */
    public ServerConfig(String configPath){
        //Initialisation des constructeurs XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document xml = null;

        //Récupération des valeurs du xml
        try {
            builder = factory.newDocumentBuilder();
            xml = builder.parse(new File(configPath));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.write(e.getMessage(), this.getError());
        }

        // Récupère et initialise les valeurs des balises de configuration avec des valeurs par défaut si nécessaire
        this.port = Integer.parseInt(getTagValue(xml, "port", "80"));
        this.root = getTagValue(xml, "root", "/");
        this.access = getTagValue(xml, "accesslog", "access.log");
        this.error = getTagValue(xml, "errorlog", "error.log");
    }

    /**
     * Récupère la valeur d'un tag dans le document XML.
     * Si le tag n'est pas présent, retourne une valeur par défaut.
     *
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
     *
     * @return Port du serveur.
     */
    public int getPort() {
        return port;
    }

    /**
     * Retourne le chemin racine du serveur.
     *
     * @return Chemin racine du serveur.
     */
    public String getRoot() {
        return root;
    }

    /**
     * Retourne le chemin du fichier de log des accès.
     *
     * @return Chemin du fichier de log des accès.
     */
    public String getAccess() {
        return access;
    }

    /**
     * Retourne le chemin du fichier de log des erreurs.
     *
     * @return Chemin du fichier de log des erreurs.
     */
    public String getError() {
        return error;
    }
}
