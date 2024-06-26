package package4;

import java.util.ArrayList;
import java.util.List;

public class DirectoryItem extends FileSystemItem {
    private List<FileMetaData> files;

    public DirectoryItem(String name) {
        super(name);
        this.files = new ArrayList<>();
    }

    public void addFile(FileMetaData file) {
        files.add(file);
    }

    public void removeFile(FileMetaData file) {
        files.remove(file);
    }

    
    public List<FileMetaData> getFiles(){
    	return files;
    }
}
    