package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class Status {
    private static List<String> untrackedFiles;

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
        String headUID = readContentsAsString(HEAD_FILE);
        for (String branch : branchList) {
            File branchFile = join(BRANCHES_DIR, branch);
            if (readContentsAsString(branchFile).equals(headUID)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
    }

    private static void stagedFiles() {
        if (addMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private static void rmFiles() {
        if (removeMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : removeMap.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    //Extra Credit (32 Points) --------------------------------------------------
    private static void modsNotStaged() {
        String headUID = readContentsAsString(HEAD_FILE);
        Commit head = Commit.loadCommit(headUID);
        if (head.map.isEmpty()) {
            return;
        }
        //Check for deleted files
        for (Map.Entry<String, String> entry : head.map.entrySet()) {
            if (!plainFilenamesIn(CWD).contains(entry.getKey())) {
                System.out.println(entry.getKey() + " (deleted)");
            }
        }
        //Check for modified files
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!head.map.entrySet().contains(fileName)) {
                untrackedFiles.add(fileName);
            } else {
                File wdFile = join(CWD, fileName);
                String wdFileUID = sha1(wdFile);
                boolean unmodified = false;
                for (Map.Entry<String, String> entry : head.map.entrySet()) {
                    if (wdFileUID.equals(entry.getValue())) {
                        unmodified = true;
                    }
                }
                if (!unmodified) {
                    System.out.println(fileName + " (modified)");
                }
            }
        }
    }

    private static void untracked() {
        if (!untrackedFiles.isEmpty()) {
            for (String untrackedFileName : untrackedFiles) {
                System.out.println(untrackedFileName);
            }
        }
    }
}
