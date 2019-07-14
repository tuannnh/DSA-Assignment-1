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
        drive.clear();
        System.out.println("Enter number of sectors:");
        size = Integer.parseInt(sc.nextLine());
        System.out.println("Enter size of a sector: ");
        sizeOfSector = Integer.parseInt(sc.nextLine());
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

    public void Defragmented() {
        String contain = "", fname = "";
        boolean isMoved;
        int indexDrive = 0;
        int fileSector;
        for (File aFile : fileList) {
            String fileName = aFile.getFileName();
            fileSector = 0;
            while (fileSector < aFile.getSectorList().size()) {
                isMoved = false;
                if (!drive.get(indexDrive).isUsed()) {
                    contain = drive.get(aFile.getSectorList().get(fileSector)).getContain();
                    fname = drive.get(aFile.getSectorList().get(fileSector)).getFileName();
                    drive.get(indexDrive).setContain(contain);
                    drive.get(indexDrive).setUsed(true);
                    drive.get(indexDrive).setFileName(fname);
                    drive.get(aFile.getSectorList().get(fileSector)).setUsed(false);
                    aFile.getSectorList().set(fileSector, indexDrive);
                    indexDrive++;
                    fileSector++;
                } else if (drive.get(indexDrive).isUsed()) {
                    if (drive.get(indexDrive).getIndex() == aFile.getSectorList().get(fileSector)) {
                        indexDrive++;
                        fileSector++;
                    } else {
                        int i = indexDrive + 1;
                        while (!isMoved) {
                            if (!drive.get(i).isUsed()) {
                                drive.get(i).setFileName(drive.get(indexDrive).getFileName());
                                drive.get(i).setContain(drive.get(indexDrive).getContain());
                                drive.get(i).setUsed(true);
                                isMoved = true;
                            }
                            i++;
                        }
                        contain = drive.get(aFile.getSectorList().get(fileSector)).getContain();
                        fname = drive.get(aFile.getSectorList().get(fileSector)).getFileName();
                        drive.get(indexDrive).setContain(contain);
                        drive.get(indexDrive).setUsed(true);
                        drive.get(indexDrive).setFileName(fname);
                        drive.get(aFile.getSectorList().get(fileSector)).setUsed(false);
                        aFile.getSectorList().set(fileSector, indexDrive);
                        indexDrive++;
                        fileSector++;
                    }

                }
            }
        }
    }
}
