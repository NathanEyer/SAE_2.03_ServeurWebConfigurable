import java.io.*;
import java.net.Socket;

public class Gerer_Requete {
    public static void handleRequest(Socket socket, ServerConfig config) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        String requete = br.readLine();
        if (requete == null || requete.isEmpty()) {
            socket.close();
            return;
        }

        Log.write("Requête reçue: " + requete, config.getAccess());

        String[] part = requete.split(" ");
        String lienB = config.getRoot() + "/" + part[1].substring(1);

        String reponse404 = """
                HTTP/1.1 404 Not Found\r
                Content-Type: text/html\r
                \r
                <html><body><h1>Le document <a href=\"""" + lienB + "\">" + lienB
                + "</a> n'existe pas</h1></body></html>";

        File file = new File(lienB);
        try (FileInputStream fichier = new FileInputStream(file)) {
            byte[] content = fichier.readAllBytes();
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Longueur du document: " + content.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(content);
            dos.flush();
        } catch (FileNotFoundException e) {
            dos.writeBytes(reponse404);
            dos.flush();
            Log.write(e.getMessage(), config.getError());
            Log.write("Connexion refusée: " + e.getMessage(), config.getAccess());
        }
    }
}

