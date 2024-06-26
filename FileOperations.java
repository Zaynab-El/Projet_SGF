package package4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class FileOperations {

    private boolean deleteRecursive(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteRecursive(f);
                }
            }
        }

        // Delete the directory or file
        return file.delete();
    }

    public boolean deleteDirectory(String dirPath) {
        if (dirPath == null || dirPath.trim().isEmpty()) {
            Journal.log("Chemin de répertoire invalide pour la suppression.");
            return false;
        }

        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            Journal.log("Not a valid directory: " + dirPath);
            return false;
        }

        return deleteRecursive(dir);
    }

    public static boolean concatenerFichiers(String fichier1, String fichier2, String fichierResultat) {
        // Vérifiez que les noms de fichiers ne sont pas vides ou nuls
        if (fichier1 == null || fichier1.trim().isEmpty() ||
            fichier2 == null || fichier2.trim().isEmpty() ||
            fichierResultat == null || fichierResultat.trim().isEmpty()) {
            System.out.println("Noms de fichiers invalides pour la concaténation.");
            return false;
        }

        try {
            // Lire le contenu du premier fichier
            String contenu1 = readFile(fichier1);
            if (contenu1 == null) {
                System.out.println("Impossible de lire le premier fichier: " + fichier1);
                return false;
            }

            // Lire le contenu du deuxième fichier
            String contenu2 = readFile(fichier2);
            if (contenu2 == null) {
                System.out.println("Impossible de lire le deuxième fichier: " + fichier2);
                return false;
            }

            // Concaténer les contenus
            String contenuConcatene = contenu1 + contenu2;

            // Écrire le contenu concaténé dans le fichier résultant
            return writeFile(fichierResultat, contenuConcatene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Échec de la concaténation des fichiers: " + e.getMessage());
            return false;
        }
    }


    public static boolean createFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            Journal.log("Nom de fichier invalide pour la création.");
            return false;
        }

        try {
            File file = new File(fileName);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();  // Create the directory if it doesn't exist
            }

            boolean created = file.createNewFile();
            if (created) {
                Journal.log("Created file: " + fileName);
            } else {
                Journal.log("File already exists: " + fileName);
            }
            return created;
        } catch (IOException e) {
            e.printStackTrace();
            Journal.log("Failed to create file: " + fileName + " - " + e.getMessage());
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            Journal.log("Failed to create file: " + fileName + " due to security restrictions - " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            Journal.log("Nom de fichier invalide pour la suppression.");
            return false;
        }

        File file = new File(fileName);
        boolean deleted = file.delete();
        if (deleted) {
            Journal.log("Deleted file: " + fileName);
        } else {
            Journal.log("Failed to delete file: " + fileName);
        }
        return deleted;
    }

    public static boolean writeFile(String fileName, String content) {
        if (fileName == null || content == null) {
            Journal.log("Nom de fichier ou contenu invalide pour l'écriture.");
            return false;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            Journal.log("Wrote to file: " + fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Journal.log("Failed to write to file: " + fileName);
            return false;
        }
    }

    public static String readFile(String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            Journal.log("File not found: " + fileName);
            return "Error: File not found.";
        }
        try {
            String content = new String(Files.readAllBytes(path));
            Journal.log("Read file: " + fileName);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            Journal.log("Failed to read file: " + fileName);
            return "Error: Could not read file.";
        }
    }

    public static boolean setFilePermissions(String fileName, Set<PosixFilePermission> permissions) {
        try {
            Path path = Paths.get(fileName);
            Files.setPosixFilePermissions(path, permissions);
            Journal.log("Set permissions for file: " + fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Journal.log("Failed to set permissions for file: " + fileName);
            return false;
        }
    }

    public FileOperations() {
    }

    public FileOperations(String fileName, String content) {
        if (fileName != null && content != null) {
            createFile(fileName);
            writeFile(fileName, content);
        }
    }
}
