package com.shans.kaluhin;

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
        System.out.println("Sort by: "+ csvWorker.getSort().val);
        System.out.println("Write command: ");
        String command = scanner.nextLine();
        switch (command) {
            case "start":

                    fileWorker.startCount();

                break;
            case "read":
                System.out.println("Please write you project path");
                String projectPath = scanner.nextLine();
                fileWorker.setProjectPath(projectPath);

                    fileWorker.startCount();

                break;
            case "sort":
                setSort();
                break;
            default:
                System.err.println("Wrong command");
                runProgram();
                break;
        }
        csvWorker.writeWords(fileWorker.getKeyWords());
        System.out.println( "\u001B[32m" + "Success!" + "\u001B[0m");
        scanner.close();
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
                System.out.println("sort set (A-Z)");
                csvWorker.setSort(Sort.AZ);
                runProgram();
                break;
            case "2":
                System.out.println("sort set (Z-A)");
                csvWorker.setSort(Sort.ZA);
                runProgram();
                break;
            case "3":
                System.out.println("sort set value descending");
                csvWorker.setSort(Sort.DSC);
                runProgram();
                break;
            case "4":
                System.out.println("sort set value ascending");
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
