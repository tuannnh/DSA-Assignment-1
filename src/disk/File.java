package disk;

import java.util.List;

public class File {
    private String fileName;
    private List<Integer> sectorList;

    public File(String fileName, List<Integer> sectorList) {
        this.fileName = fileName;
        this.sectorList = sectorList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Integer> getSectorList() {
        return sectorList;
    }

    public void setSectorList(List<Integer> sectorList) {
        this.sectorList = sectorList;
    }

    @Override
    public String toString() {
        System.out.println("File: "+fileName);
        System.out.println("Used Sectors: ");
        for (int i = 0; i < sectorList.size(); i++) {
            if(i == sectorList.size()-1){
                System.out.print(sectorList.get(i));
                            }
            else{
                System.out.print(sectorList.get(i) + ", ");
            }
        }
        return "";
    }
}
