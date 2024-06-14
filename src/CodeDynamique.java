import java.io.*;

/**
 * Classe qui s'occupe de créer une page où l'on peut exécuter du code
 */
public class CodeDynamique {
    //Attributs
    private String nomHtml;
    private String code = "<html>\n<body>\n";

    //Construit un objet avec le nom du fichier html
    public CodeDynamique(String nomHtml) {
        this.nomHtml = nomHtml;
    }

    /**
     * Méthode qui s'occupe de créer le fichier html et d'appeler ecrireCode() pour le remplir avec:
     * @param bashCode date en bash
     * @param cCode date en c
     * @param pythonCode date en python
     * @param rubyCode date en ruby
     * @param cPP date en c++
     */
    public void ecrireCodeDynamique(String bashCode, String cCode, String pythonCode, String rubyCode, String cPP) {
        try {
            //Création du fichier code.html
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.nomHtml));
            this.code += "<center><h1>VOICI LES DATES EN PLUSIEURS LANGAGES</h1>\n";

            //Ecriture et exécution du code en bash
            this.code += "<h2>\nEn bash\n</h2>\n<pre>";
            ecrireCode("bash", bashCode);

            //Ecriture et exécution du code en python
            this.code += "</pre><h2>\nEn python\n</h2>\n<pre>";
            ecrireCode("python", pythonCode);

            //Ecriture et exécution du code en c
            this.code += "</pre><h2>\nEn c\n</h2>\n<pre>";
            ecrireCode("c", cCode);

            //Ecriture et exécution du code en ruby
            this.code += "</pre><h2>\nEn Ruby\n</h2>\n<pre>";
            ecrireCode("ruby", rubyCode);

            //Ecriture et exécution du code en c++
            this.code += "</pre><h2>\nEn C++\n</h2>\n<pre>";
            ecrireCode("cpp", cPP);

            //Ecriture et exécution du code en java
            this.code += "</pre><h2>\nEn Java\n</h2>\n<pre>";
            code += java.time.LocalDate.now();

            //Fermeture et remplissage de l'html
            code += "</pre>\n</center>\n</body>\n</html>";
            writer.write(code);
            writer.flush();
            writer.close();
            Log.write("Création de la page de code dynamique effectuée.", "var/log/myweb/access.log");
        } catch (IOException e) {
            Log.write(e.getMessage(), "var/log/myweb/error.log");
        }
    }

    /**
     * Ecriture d'un fichier qui contient le code et compilation
     * @param langage choisi
     * @param contenu du code
     */
    public void ecrireCode(String langage, String contenu) {
        // Initialisations
        BufferedWriter writer;
        ProcessBuilder pb = null;
        String fichier = "var/code/dynamique.";

        //Création et compilation des codes
        try {
            switch (langage) {
                case "bash":
                    // Écriture d'un fichier dynamique.sh en bash
                    fichier += "sh";
                    writer = new BufferedWriter(new FileWriter(fichier));
                    writer.write(contenu);
                    writer.flush();
                    writer.close();
                    //Compilation
                    pb = new ProcessBuilder("/bin/bash", fichier);
                    break;
                case "python":
                    // Écriture d'un fichier dynamique.java en Java
                    fichier += "py";
                    writer = new BufferedWriter(new FileWriter(fichier));
                    writer.write(contenu);
                    writer.flush();
                    writer.close();
                    //Compilation
                    pb = new ProcessBuilder("python3", fichier);
                    break;
                case "c":
                    //Ecriture d'un fichier dynamique.c en C
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
                case "ruby":
                    //Ecriture d'un fichier dynamique.rb en Ruby
                    fichier += "rb";
                    writer = new BufferedWriter(new FileWriter(fichier));
                    writer.write(contenu);
                    writer.flush();
                    writer.close();
                    //Compilation du fichier ruby
                    pb = new ProcessBuilder("ruby", fichier);
                    break;
                case "cpp":
                    //Ecriture d'un fichier dynamique.cpp en c++
                    fichier += "cpp";
                    writer = new BufferedWriter(new FileWriter(fichier));
                    writer.write(contenu);
                    writer.flush();
                    writer.close();
                    // Compilation du fichier C++
                    ProcessBuilder pbCompileCPP = new ProcessBuilder("g++", fichier, "-o", "var/code/dynamique.cpp");
                    pbCompileCPP.start().waitFor();
                    pb = new ProcessBuilder("./var/code/dynamique");
                    break;
            }

            // Ajout du code compilé
            ajoutCodeCompile(pb);
        } catch (IOException | InterruptedException e) {
            Log.write(e.getMessage(), "var/log/myweb/error.log");
        }
    }

    /**
     * Affichage du résultat du code dans code.html
     * @param pb ProcessBuilder qui contient le résultat
     */
    public void ajoutCodeCompile(ProcessBuilder pb) {
        try {
            //Récupération du code compilé et ajout dans l'html
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                code += line + "\n";
            }
            reader.close();
        } catch (IOException e) {
            Log.write(e.getMessage(), "error.log");
        }
    }
}