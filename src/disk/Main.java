package disk;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Disk drive = new Disk();
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Create disk");
            System.out.println("2. Create file");
            System.out.println("3. Remove file");
            System.out.println("4. View content of a file");
            System.out.println("5. Print");
            System.out.println("6. Defragment");

            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    drive.CreateDrives();
                     break;
                case 2:
                    drive.AddNewFile();
                    break;
                case 3:
                    drive.DeleteFile();
                    break;
                case 4:
                    drive.ViewContentFile();
                    break;
                case 5:
                    drive.PrintDisk();
                    break;
                case 6:
                    drive.Defragmented();
                    break;
            }

        } while (1 <= choice && choice <= 6);
    }
}
