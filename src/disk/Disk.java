package disk;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Disk {
    Scanner sc = new Scanner(System.in);
    List<Sector> drive;
    List<File> fileList = new LinkedList<>();
    int size, sizeOfSector;

    public Disk() {
        drive = new ArrayList<Sector>();
    }

    public void CreateDrives() {
        String s;
        drive.clear();
        System.out.println("Enter number of sectors:");
        do{
            s = sc.nextLine().trim();
        }while(!s.matches("\\d+"));
        size = Integer.parseInt(s);
        System.out.println("Enter size of a sector: ");
        do{
            s = sc.nextLine().trim();
        }while(!s.matches("\\d+"));
        sizeOfSector = Integer.parseInt(s);
        for (int i = 0; i < size; i++) {
            drive.add(new Sector(i));
        }
        System.out.println("Drive created!");
    }

    private boolean CheckFreeSpace(int needSector) {
        int emptySectors = 0;
        for (int sector = 0; sector < drive.size(); sector++) {
            if (!drive.get(sector).isUsed()) {
                emptySectors++;
            }
        }
        if (emptySectors < needSector) return false;
        return true;
    }

    private boolean CheckEmptyDrive() {
        for (int i = 0; i < drive.size(); i++) {
            if (drive.get(i).isUsed()) return false;
        }
        return true;
    }

    private int CheckFileExist(String fileName) {
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getFileName().equalsIgnoreCase(fileName))
                return i;
        }
        return -1;
    }

    public void AddNewFile() {
        System.out.println("Enter file name: ");
        String name = sc.nextLine().trim();
        if (CheckFileExist(name) >= 0) {
            System.out.println("File name existed!");
            return;
        }
        System.out.println("Enter file content: ");
        String content = sc.nextLine();
        char[] chars = content.toCharArray();
        int numOfChars = chars.length;
        int needSector = (int) Math.ceil((double) numOfChars / sizeOfSector);
        if (!CheckFreeSpace(needSector)) {
            System.out.println("Disk is not enough space!");
            return;
        } else {
            List<Integer> sectorList = new ArrayList<>();
            int startIndex = 0, endIndex = 0;
            int charsRemain = content.length();
            int usedSector = 0;
            for (Sector aSector : drive) {
                String s = "";
                if (!aSector.isUsed() && charsRemain > 0) {
                    startIndex = (usedSector * sizeOfSector);
                    endIndex = ((usedSector * sizeOfSector) + sizeOfSector);
                    while (endIndex - startIndex > (charsRemain)) {
                        endIndex--;
                    }
                    for (int i = startIndex; i < endIndex; i++) {
                        s += content.charAt(i);
                    }
                    System.out.println(s);
                    aSector.setContain(s);
                    aSector.setFileName(name);
                    usedSector++;
                    charsRemain -= sizeOfSector;
                    aSector.setUsed(true);
                    sectorList.add(aSector.getIndex());
                }
            }
            fileList.add(new File(name, sectorList));
        }
    }

    public void DeleteFile() {
        if (CheckEmptyDrive()) {
            System.out.println("The Disk is Empty!");
            return;
        }
        System.out.println("Enter file name to delete: ");
        String name = sc.nextLine().trim();
        int fileIndex = CheckFileExist(name);
        if (fileIndex < 0) {
            System.out.println("File name not existed!");
            return;
        }
        List<Integer> sectorList = fileList.get(fileIndex).getSectorList();
        for (Integer index : sectorList) {
            drive.get(index).setUsed(false);
            drive.get(index).setContain(null);
            drive.get(index).setFileName(null);
        }
        fileList.remove(fileIndex);
        System.out.println("File " + name + " is deleted!");
    }

    public void ViewContentFile() {
        if (CheckEmptyDrive()) {
            System.out.println("The Disk is Empty!");
            return;
        }
        System.out.println("Enter file name to view content: ");
        String name = sc.nextLine().trim();
        int fileIndex = CheckFileExist(name);
        if (fileIndex < 0) {
            System.out.println("File name not existed!");
            return;
        }
        System.out.println("File: " + name);
        List<Integer> sectorList = fileList.get(fileIndex).getSectorList();
        for (int i = 0; i < sectorList.size(); i++) {
            System.out.print(drive.get(sectorList.get(i)).getContain());
        }
        System.out.println();
    }

    public void PrintDisk() {
        for (File aFile : fileList) {
            System.out.println(aFile);
        }
        for (Sector aSector : drive) {
            System.out.println(aSector);
        }

    }

    private void moveSector(File aFile, int indexDrive, int fileSector) {
        String contain = "", fname = "";
        contain = drive.get(aFile.getSectorList().get(fileSector)).getContain();
        fname = drive.get(aFile.getSectorList().get(fileSector)).getFileName();

        drive.get(aFile.getSectorList().get(fileSector)).setUsed(false);
        drive.get(indexDrive).setContain(contain);
        drive.get(indexDrive).setUsed(true);
        drive.get(indexDrive).setFileName(fname);
        aFile.getSectorList().set(fileSector, indexDrive);
    }


    public void Defragmented() {
        if (CheckEmptyDrive()) {
            System.out.println("The Disk is Empty! Cannot defragment");
            return;
        }
        String contain = "", fname = "";
        boolean isMoved;
        int indexDrive = 0;
        for (File aFile : fileList) {
            for (int i = 0; i < aFile.getSectorList().size(); i++) {
                isMoved = false;
                if (!drive.get(indexDrive).isUsed()) {

                    moveSector(aFile, indexDrive, i);
                    indexDrive++;
                } else if (drive.get(indexDrive).isUsed()) {
                    if (drive.get(indexDrive).getIndex() == aFile.getSectorList().get(i) &&
                            drive.get(indexDrive).getFileName().equals(aFile.getFileName())) {
                        indexDrive++;
                    } else {
                        int temp = indexDrive;
                        while (!isMoved) {
                            if (!drive.get(temp).isUsed()) {
                                drive.get(temp).setFileName(drive.get(indexDrive).getFileName());
                                drive.get(temp).setContain(drive.get(indexDrive).getContain());
                                //Update file list sector
                                for (int j = 0; j < fileList.size(); j++) {
                                    for (int k = 0; k < fileList.get(j).getSectorList().size(); k++) {
                                        if (drive.get(indexDrive).getIndex() == fileList.get(j).getSectorList().get(k)) {
                                            fileList.get(j).getSectorList().set(k, temp);
                                        }
                                    }
                                }
                                drive.get(temp).setUsed(true);
                                moveSector(aFile, indexDrive, i);
                                indexDrive++;
                                isMoved = true;
                            } else {
                                temp++;
                            }
                        }

                    }

                }
            }

        }
    }
}
