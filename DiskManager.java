package package4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiskManager {
    private long totalSpace;
    private long usedSpace;
    private List<FileMetaData> files;
    private Journal journal;

    public DiskManager(long totalSpace, Journal journal) {
        this.totalSpace = totalSpace;
        this.usedSpace = 0;
        this.files = new ArrayList<>();
        this.journal = journal;
        journal.log("Initialized DiskManager with total space: " + totalSpace + " bytes");
    }

    public DiskManager(long totalSpace) {
        this.totalSpace = totalSpace;
        this.usedSpace = 0;
        this.files = new ArrayList<>();
        Journal.log("Initialized DiskManager with total space: " + totalSpace + " bytes");
    }

    public long getFreeSpace() {
        return totalSpace - usedSpace;
    }

    public boolean allocateSpace(long size) {
        if (size <= getFreeSpace()) {
            usedSpace += size;
            journal.log("Allocated space: " + size + " bytes");
            return true;
        }
        journal.log("Failed to allocate space: " + size + " bytes");
        return false;
    }

    public void releaseSpace(long size) {
        usedSpace -= size;
        journal.log("Released space: " + size + " bytes");
    }

    public void addFile(FileMetaData file) {
        if (allocateSpace(file.getSize())) {
            files.add(file);
            journal.log("Added file: " + file.getName());
        } else {
            System.out.println("Not enough space to add file: " + file.getName());
            journal.log("Failed to add file: " + file.getName() + " due to insufficient space");
        }
    }

    public void removeFile(FileMetaData file) {
        files.remove(file);
        releaseSpace(file.getSize());
        journal.log("Removed file: " + file.getName());
    }

    public void defragment() {
        Collections.sort(files, (f1, f2) -> Long.compare(f1.getCreationDate().getTime(), f2.getCreationDate().getTime()));

        long currentPosition = 0;
        for (FileMetaData file : files) {
            // Lire le contenu du fichier
            String content = FileOperations.readFile(file.getName());

            if (content == null || content.startsWith("Error")) {
                continue; // Skip this file if reading failed
            }

            // Trouver un bloc contigu
            long startPosition = currentPosition;
            long endPosition = startPosition + file.getSize();
            currentPosition = endPosition;

            // Écrire le contenu du fichier de manière contiguë
            if (FileOperations.writeFile(file.getName(), content)) {
                // Mettre à jour les métadonnées du fichier
                file.setStartPosition(startPosition);
                file.setEndPosition(endPosition);
            } else {
                journal.log("Failed to write file during defragmentation: " + file.getName());
            }
        }

        journal.log("Defragmented disk with contiguous allocation");
    }

    public List<FileMetaData> getFiles() {
        return files;
    }
}
