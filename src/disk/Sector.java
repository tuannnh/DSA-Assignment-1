package disk;

public class Sector {
    String [] contain;
    int index;
    boolean isUsed;

    public Sector(int index, int sizeOfSector) {
        this.index = index;
        contain = new String[sizeOfSector];
    }

    public String[] getContain() {
        return contain;
    }

    public void setContain(String[] contain) {
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
}
