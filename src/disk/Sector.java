package disk;

public class Sector {
    private String contain;
    private int index;
    private boolean isUsed;
    private String fileName;

    public Sector(int index) {
        this.index = index;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public String toString() {
        if(isUsed){
            return String.format("Sector: %d - Used by file: %s", getIndex(), getFileName());
        }
        else return String.format("Sector: %d - Empty", getIndex());

    }
}
