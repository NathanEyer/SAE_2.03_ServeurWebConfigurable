import java.io.*;

/**
 * Classe qui s'occupe de créer une page où l'on peut exécuter du code
 */
public class CodeDynamique {
    private String nomHtml;
    private String code = "<html>\n<body>\n";
    public CodeDynamique(String nomHtml) {
        this.nomHtml = nomHtml;
    }

    public void ecrireCodeDynamique(String bashCode, String cCode, String pythonCode) {
        try {
            //Création du fichier code.html
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.nomHtml));
            this.code += "<h2>\nEn bash\n</h2>\nLa date est <pre>";

            //Ecriture et exécution du code
            ecrireCode("bash", bashCode);
            this.code += "</pre><h2>\nEn python\n</h2>\nLa date est <pre>";
            ecrireCode("python", pythonCode);
            this.code += "</pre><h2>\nEn c\n</h2>\nLa date est <pre>";
            ecrireCode("c", cCode);

            //Fermeture et remplissage de l'html
            code += "</pre>\n</body>\n</html>";
            writer.write(code);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.write(e.getMessage(), "error.log");
        }
    }

    public void ecrireCode(String langage, String contenu) {
        // Initialisations
        BufferedWriter writer;
        ProcessBuilder pb = null;
        String fichier = "var/code/dynamique.";

        try {
            switch (langage) {
                case "bash":
                    // Écriture d'un fichier dynamique.sh en bash
                    fichier += "sh";
                    writer = new BufferedWriter(new FileWriter(fichier));
                    writer.write(contenu);
                    writer.flush();
                    writer.close();
                    pb = new ProcessBuilder("/bin/bash", fichier);
                    break;
                case "python":
                    // Écriture d'un fichier dynamique.java en Java
                    fichier += "py";
                    writer = new BufferedWriter(new FileWriter(fichier));
                    writer.write(contenu);
                    writer.flush();
                    writer.close();
                    pb = new ProcessBuilder("python3", fichier);
                    break;
                case "c":
                    fichier += "c";
                    writer = new BufferedWriter(new FileWriter(fichier));
                    writer.write(contenu);
                    writer.flush();
                    writer.close();
                    // Compilation du fichier C
                    ProcessBuilder pbCompileC = new ProcessBuilder("gcc", fichier, "-o", "var/code/dynamique");
                    pbCompileC.start().waitFor();
                    pb = new ProcessBuilder("./var/code/dynamique");
                    break;
            }

            // Ajout du code compilé
            ajoutCodeCompile(pb);
        } catch (IOException | InterruptedException e) {
            Log.write(e.getMessage(), "error.log");
        }
    }

    public void ajoutCodeCompile(ProcessBuilder pb) {
        try {
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                code += line;
            }
            reader.close();
        } catch (IOException e) {
            Log.write(e.getMessage(), "error.log");
        }
    }
}