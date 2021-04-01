package gitlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class Status {
    public static void status() {
        System.out.println("=== Branches ===");
        branches();
        System.out.println();
        System.out.println("=== Staged Files ===");
        stagedFiles();
        System.out.println();
        System.out.println("=== Removed Files ===");
        rmFiles();
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        modsNotStaged();
        System.out.println();
        System.out.println("=== Untracked Files ===");
        untracked();
        System.out.println();
    }

    private static void branches() {
        List<String> branchList = plainFilenamesIn(BRANCHES_DIR);
        String currBranch = readContentsAsString(CURRENT_BRANCH);
        for (String branch : branchList) {
            if (branch.equals(currBranch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
    }

    private static void stagedFiles() {
        HashMap<String, String> addMap = readObject(ADD_FILE, HashMap.class);
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private static void rmFiles() {
        HashMap<String, String> removeMap = readObject(REMOVE_FILE, HashMap.class);
        for (Map.Entry<String, String> entry : removeMap.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    /* Extra Credit (32 Points) -------------------------------------------------- */
    private static void modsNotStaged() {
        Commit head = currentCommit();
        HashMap<String, String> addMap = readObject(ADD_FILE, HashMap.class);
        HashMap<String, String> removeMap = readObject(REMOVE_FILE, HashMap.class);\
        //Check for deleted files
        if (!head.map.isEmpty()) {
            for (String file : head.map.keySet()) {
                if (!plainFilenamesIn(CWD).contains(file)) {
                    if (removeMap.containsKey(file)) {
                        System.out.println(file + " (deleted)");
                    }
                } else {
                    File cWDFile = join(CWD, file);
                    byte[] contents = readContents(cWDFile);
                    String cWDFileUID = sha1(contents);
                    if (!head.map.get(file).equals(cWDFileUID)
                    && !addMap.containsKey(file)) {
                        System.out.println(file + " (modified)");
                    }
                }
            }
        }
        //check add map
        if (!addMap.isEmpty()) {
            for (String file : addMap.keySet()) {
                if (plainFilenamesIn(CWD).contains(file)) {
                    File cWDFile = join(CWD, file);
                    byte[] contents = readContents(cWDFile);
                    String wdFileUID = sha1(contents);
                    if (!wdFileUID.equals(head.map.get(file))
                            && !wdFileUID.equals(addMap.get(file))) {
                        System.out.println(file + " (modified)");
                    }
                } else {
                    System.out.println(file + " (deleted)");
                }
            }
        }

    }

    private static void untracked() {
        Commit head = currentCommit();
        HashMap<String, String> addMap = readObject(ADD_FILE, HashMap.class);
        for (String file : plainFilenamesIn(CWD)) {
            if (!head.map.entrySet().contains(file) && !addMap.entrySet().contains(file)) {
                System.out.println(file);
            }
        }
    }
}
