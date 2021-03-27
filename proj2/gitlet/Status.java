package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;
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
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private static void rmFiles() {

    }

    private static void modsNotStaged() {

    }

    private static void untracked() {

    }
}
