package package4;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.tree.TreeNode;


public class FileManagementGUI extends JFrame {
    private DiskManager diskManager;
    private FileOperations fileOperations;
    private JTree fileTree;//afficher la structure des fichiers et des répertoires sous forme d'arbre.
    private DefaultTreeModel treeModel;

    public FileManagementGUI() {
        diskManager = new DiskManager(1024L * 1024L * 1024L, new Journal());
        fileOperations = new FileOperations();

        setTitle("File Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel);//Ajoute le panneau au contenu principal de la fenêtre.
        panel.setLayout(null);//vous devez spécifier les positions et tailles des composants manuellement.

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        treeModel = new DefaultTreeModel(root);
        fileTree = new JTree(treeModel);
        JScrollPane treeScroll = new JScrollPane(fileTree);// pour permettre le défilement de l'arbre si nécessaire.
        treeScroll.setBounds(10, 10, 200, 350);
        panel.add(treeScroll);

        JButton createButton = new JButton("Créer un fichier");
        createButton.setBounds(220, 30, 200, 30);
        panel.add(createButton);

        createButton.addActionListener(new ActionListener() {//un écouteur d'événements 
            public void actionPerformed(ActionEvent e) {
                createFile();
            }
        });

        JButton readButton = new JButton("Lire un fichier");
        readButton.setBounds(220, 70, 200, 30);
        panel.add(readButton);

        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readFile();
            }
        });


        JButton writeButton = new JButton("Écrire dans un fichier");
        writeButton.setBounds(220, 110, 200, 30);
        panel.add(writeButton);
        writeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writeFile();
            }
        });

        JButton deleteButton = new JButton("Supprimer un fichier");
        deleteButton.setBounds(220, 150, 200, 30);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteFile();
            }
        });

        JButton createDirButton = new JButton("Créer un répertoire");
        createDirButton.setBounds(220, 190, 200, 30);
        panel.add(createDirButton);

        createDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createDirectory();
            }
        });

        JButton deleteDirButton = new JButton("Supprimer un répertoire");
        deleteDirButton.setBounds(220, 230, 200, 30);
        panel.add(deleteDirButton);

        deleteDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteDirectory();
            }
        });

        JButton readDirButton = new JButton("Lire un répertoire");
        readDirButton.setBounds(220, 270, 200, 30);
        panel.add(readDirButton);

        readDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readDirectory();
            }
        });

        JButton defragButton = new JButton("Défragmenter");
        defragButton.setBounds(220, 310, 200, 30);
        panel.add(defragButton);

        defragButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defragment();
            }
        });
        
        JButton concatenateButton = new JButton("Concaténer des fichiers");
        concatenateButton.setBounds(220, 350, 200, 30);
        panel.add(concatenateButton);

        concatenateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concatenateFiles();
            }
        });
    }
    
    private void concatenateFiles() {
        String file1 = JOptionPane.showInputDialog(this, "Nom du premier fichier:");
        String file2 = JOptionPane.showInputDialog(this, "Nom du deuxième fichier:");
        String resultFile = JOptionPane.showInputDialog(this, "Nom du fichier résultant:");

        if (file1 != null && !file1.trim().isEmpty() &&
            file2 != null && !file2.trim().isEmpty() &&
            resultFile != null && !resultFile.trim().isEmpty()) {

            boolean concatenated = fileOperations.concatenerFichiers(file1, file2, resultFile);
            if (concatenated) {
                refreshFileTree();
                JOptionPane.showMessageDialog(this, "Fichiers concaténés avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la concaténation des fichiers.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Noms de fichiers invalides pour la concaténation.");
        }
    }


    private void createFile() {
        String fileName = JOptionPane.showInputDialog(this, "Nom du fichier:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            if (fileOperations.createFile(fileName)) {
                diskManager.addFile(new FileMetaData(fileName, 0, "rw-r--r--"));//ajoute ses métadonnées au diskManager.
                refreshFileTree();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la création du fichier.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nom de fichier invalide.");
        }
    }
    
    private FileMetaData findFileByName(String fileName) {//Recherche un fichier par son nom dans la liste des fichiers du diskManager.
        for (FileMetaData file : diskManager.getFiles()) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    private void writeFile() {
        String fileName = JOptionPane.showInputDialog(this, "Nom du fichier:");
        String content = JOptionPane.showInputDialog(this, "Contenu:");
        if (fileName != null && !fileName.trim().isEmpty() && content != null) {//!fileName.trim().isEmpty() vérifie que le nom du fichier, une fois les espaces blancs supprimés, n'est pas vide.
            FileMetaData fileToUpdate = findFileByName(fileName);
            if (fileToUpdate != null) {
                // Demander confirmation pour écraser le contenu existant
                int choice = JOptionPane.showConfirmDialog(this, "Le fichier existe déjà. Voulez-vous écraser son contenu ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    if (fileOperations.writeFile(fileName, content)) {
                        fileToUpdate.setSize(content.length());
                        refreshFileTree();
                    } else {
                        JOptionPane.showMessageDialog(this, "Échec de l'écriture dans le fichier.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Le fichier n'existe pas. Veuillez créer le fichier en premier lieu.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nom de fichier ou contenu invalide.");
        }
    }

    

    private void readFile() {
        String fileName = JOptionPane.showInputDialog(this, "Nom du fichier:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            String content = fileOperations.readFile(fileName);
            if (content != null) {
                JOptionPane.showMessageDialog(this, content);
            } else {
                JOptionPane.showMessageDialog(this, "Le fichier n'existe pas ou est vide.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nom de fichier invalide.");
        }
    }


    
    private void deleteFile() {
        String fileName = JOptionPane.showInputDialog(this, "Nom du fichier:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            if (fileOperations.deleteFile(fileName)) {
                FileMetaData fileToRemove = null;//Cette variable sera utilisée pour stocker les métadonnées du fichier à supprimer.
                for (FileMetaData file : diskManager.getFiles()) {
                    if (file.getName().equals(fileName)) {
                        fileToRemove = file;
                        break;
                    }
                }
                if (fileToRemove != null) {
                    diskManager.removeFile(fileToRemove);
                    refreshFileTree();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la suppression du fichier.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nom de fichier invalide.");
        }
    }


    private void createDirectory() {
        String dirName = JOptionPane.showInputDialog(this, "Nom du répertoire:");
        if (dirName != null && !dirName.trim().isEmpty()) {
            File dir = new File(dirName);
            if (dir.mkdirs()) {
                JOptionPane.showMessageDialog(this, "Répertoire créé avec succès.");
                refreshFileTree();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la création du répertoire.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nom de répertoire invalide.");
        }
    }

    private void deleteDirectory() {
        String dirPath = JOptionPane.showInputDialog(this, "Chemin du répertoire à supprimer:");
        if (dirPath != null && !dirPath.trim().isEmpty()) {
            File dir = new File(dirPath);
            if (dir.isDirectory()) {
                if (fileOperations.deleteDirectory(dirPath)) {
                    JOptionPane.showMessageDialog(this, "Répertoire supprimé avec succès.");
                    refreshFileTree();
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de la suppression du répertoire.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chemin invalide ou n'est pas un répertoire.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chemin de répertoire invalide.");
        }
    }




    private void readDirectory() {
        String dirPath = JOptionPane.showInputDialog(this, "Chemin du répertoire:");
        if (dirPath != null && !dirPath.trim().isEmpty()) {
            File dir = new File(dirPath);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    StringBuilder fileList = new StringBuilder("Contenu du répertoire :\n");// en permettant d'ajouter des chaînes sans créer de nouveaux objets à chaque concaténation.
                    for (File file : files) {
                        fileList.append(file.getName()).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, fileList.toString());//Affiche une boîte de dialogue contenant la liste des fichiers et répertoires dans le répertoire spécifié.
                } else {
                    JOptionPane.showMessageDialog(this, "Le répertoire est vide ou inaccessible.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chemin invalide ou n'est pas un répertoire.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chemin de répertoire invalide.");
        }
    }

    private void defragment() {
        diskManager.defragment();
        refreshFileTree();
        JOptionPane.showMessageDialog(this, "Défragmentation terminée.");
    }

    private void refreshFileTree() {//Mise à jour de l'affichage de l'arbre des fichiers 
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        for (FileMetaData file : diskManager.getFiles()) {
            root.add(new DefaultMutableTreeNode(file.getName()));
        }
        treeModel.setRoot(root);                                                                  //Met à jour le modèle d'arbre treeModel avec le nouveau nœud racine root.
        treeModel.reload();//Recharge le modèle de l'arbre pour refléter les modifications apportées.
    }
    
    public static void main(String[] args) {//Un thread est la plus petite unité de traitement qu'un système d'exploitation peut planifier pour exécution.
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			FileManagementGUI frame = new FileManagementGUI();
    			frame.setVisible(true);
    		}
    	});
    }
}
