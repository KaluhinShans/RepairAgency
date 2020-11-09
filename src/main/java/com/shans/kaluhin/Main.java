package com.shans.kaluhin;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static FileWorker fileWorker = new FileWorker();
    private static CsvWorker csvWorker = new CsvWorker();

    public static void main(String[] args) {
        System.out.println("1. start (read default project)");
        System.out.println("2. read (read your project)");
        System.out.println("3. sort (select sort method)");
        runProgram();
    }

    public static void runProgram() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sort by: " + csvWorker.getSort().val);
        System.out.println("Write command: ");
        String command = scanner.nextLine();
        switch (command) {
            case "start":
                fileWorker.setProjectPath("C:\\Users\\Shansonye\\Desktop\\Practice\\RepairAgency");
                startRead();

                scanner.close();
                break;
            case "read":
                System.out.println("Please write you project path");
                String projectPath = scanner.nextLine();
                fileWorker.setProjectPath(projectPath);
                startRead();

                scanner.close();
                break;
            case "sort":
                setSort();
                break;
            default:
                System.err.println("Wrong command");
                runProgram();
                break;
        }
    }

    public static void startRead(){
        try {
            fileWorker.startCount();
        } catch (IOException exception) {
            System.err.println("Invalid path");
            runProgram();
            return;
        }

        csvWorker.writeWords(fileWorker.getKeyWords());
        System.out.println("\u001B[32m" + "Success!" + "\u001B[0m");
    }

    public static void setSort() {
        System.out.println("Select sort method: ");
        System.out.println("or 'back'");

        System.out.println("1 (A-Z)");
        System.out.println("2 (Z-A)");
        System.out.println("3 value descending");
        System.out.println("4 value ascending");

        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextLine()) {
            case "1":
                csvWorker.setSort(Sort.AZ);
                runProgram();
                break;
            case "2":
                csvWorker.setSort(Sort.ZA);
                runProgram();
                break;
            case "3":
                csvWorker.setSort(Sort.DSC);
                runProgram();
                break;
            case "4":
                csvWorker.setSort(Sort.ASC);
                runProgram();
                break;
            case "back":
                runProgram();
                break;
            default:
                System.out.println("wrong sort");
                setSort();
                break;
        }
        scanner.close();
    }
}
