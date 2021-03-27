package gitlet;

import static gitlet.Repository.*;

public class Status {
    public static void status() {
        System.out.println("=== Branches ===");
        branches();
        System.out.println("=== Staged Files ===");
        stagedFiles();
        System.out.println("=== Removed Files ===");
        rmFiles();
        System.out.println("=== Modifications Not Staged For Commit ===");
        modsNotStaged();
        System.out.println("=== Untracked Files ===");
        untracked();
    }

    private static void branches() {
        
    }

    private static void stagedFiles() {

    }

    private static void rmFiles() {

    }

    private static void modsNotStaged() {

    }

    private static void untracked() {

    }
}
