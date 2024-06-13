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
    private final String rootPath;
    private final String accessLogPath;
    private final String errorLogPath;

    /**
     * Constructeur de la classe ServerConfig.
     * Lit le fichier XML de configuration et initialise les variables d'instance.
     *
     * @param configPath Chemin vers le fichier XML de configuration.
     * @throws ParserConfigurationException Si une erreur se produit lors de la configuration du parser.
     * @throws SAXException Si une erreur se produit lors du parsing du fichier XML.
     * @throws IOException Si une erreur se produit lors de la lecture du fichier.
     */
    public ServerConfig(String configPath) throws ParserConfigurationException, IOException, SAXException {
        //Initialisation des constructeurs XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document config = builder.parse(new File(configPath));

        // Récupère et initialise les valeurs des balises de configuration avec des valeurs par défaut si nécessaire
        this.port = Integer.parseInt(getTagValue(config, "port", "80"));
        this.rootPath = getTagValue(config, "root", "/");
        this.accessLogPath = getTagValue(config, "accesslog", "access.log");
        this.errorLogPath = getTagValue(config, "errorlog", "error.log");
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
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Retourne le chemin du fichier de log des accès.
     *
     * @return Chemin du fichier de log des accès.
     */
    public String getAccessLogPath() {
        return accessLogPath;
    }

    /**
     * Retourne le chemin du fichier de log des erreurs.
     *
     * @return Chemin du fichier de log des erreurs.
     */
    public String getErrorLogPath() {
        return errorLogPath;
    }
}
