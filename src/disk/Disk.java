package disk;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Disk {
    List<Sector> Drives;

    public Disk() {
        Drives = new ArrayList();
    }

    public void CreateDrives(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of sectors:");
        int driveSize = Integer.parseInt(sc.nextLine());
        System.out.println("Enter size of a sector: ");
        int sizeOfSector = Integer.parseInt(sc.nextLine());

    }
}
