package package4;

import java.util.Date;

public class FileMetaData {
    private String name;
    private long size;
    private String permissions;
    private Date creationDate;
    private Date modificationDate;
    private long startPosition; // Start position on the disk
    private long endPosition;   // End position on the disk

    public FileMetaData(String name, long size, String permissions) {
        this.name = name;
        this.size = size;
        this.permissions = permissions;
        this.creationDate = new Date();
        this.modificationDate = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.modificationDate = new Date();
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
        this.modificationDate = new Date();
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
        this.modificationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    @Override
    public String toString() {
        return "FileMetaData{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", permissions='" + permissions + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }
}
