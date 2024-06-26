JFLAGS = -g
JC = javac
JAR = jar
MAIN = package4.FileManagementGUI
CLASSES = \
    src/package4/DirectoryItem.java \
    src/package4/DiskManager.java \
    src/package4/FileManagementGUI.java \
    src/package4/FileMetaData.java \
    src/package4/FileOperations.java \
    src/package4/FileSystemItem.java \
    src/package4/Journal.java

.SUFFIXES: .java .class

# Règle pour compiler les fichiers .java en .class
.java.class:
	$(JC) $(JFLAGS) -d bin $<

CLASSPATH = bin

# Règle par défaut pour compiler toutes les classes
default: classes

# Règle pour compiler les classes
classes: $(CLASSES:.java=.class)

# Règle pour exécuter l'application
run: classes
	java -cp $(CLASSPATH) $(MAIN)

# Règle pour créer un fichier JAR
jar: classes
	$(JAR) cvfe app.jar $(MAIN) -C $(CLASSPATH) .

# Règle pour nettoyer les fichiers compilés
clean:
	del bin\package4\*.class
