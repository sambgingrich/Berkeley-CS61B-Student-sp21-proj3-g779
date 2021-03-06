package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class CheckoutReset {
    /* For cases 1: Checkout a given file from the head
     * and 2: Checkout a given filename from a given commit ID */
    public static void checkout(Commit c, String fileName) {
        /*check failure case*/
        if (!c.map.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File cWDFile = join(CWD, fileName);
        try {
            cWDFile.createNewFile();
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
        String blobUID = c.map.get(fileName);
        File blobFile = join(BLOB_FOLDER, blobUID);
        byte[] contents = readContents(blobFile);
        writeContents(cWDFile, contents);
    }

    /* Cover the branch checkout case */
    public static void checkout(String branchName) {
        //check branch exists
        if (!plainFilenamesIn(BRANCHES_DIR).contains(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        //Load HEAD commit and branch's head
        File branchFile = join(BRANCHES_DIR, branchName);
        String branchHeadUID = readContentsAsString(branchFile);
        //check branch is the current branch
        if (branchName.equals(readContentsAsString(CURRENT_BRANCH))) {
            System.out.println("No need to checkout the current branch");
            System.exit(0);
        }
        moveTrackedFiles(branchHeadUID, false);
        writeContents(CURRENT_BRANCH, branchName);
    }

    public static void reset(String commitID) {
        moveTrackedFiles(commitID, true);
    }

    private static void moveTrackedFiles(String commitID, boolean reset) {
        //check for  untracked files
        Commit head = currentCommit();
        Commit c = Commit.loadCommit(commitID);
        /* Checks if working file is both untracked and would be overwritten*/
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!head.map.keySet().contains(fileName)
                    && c.map.containsKey(fileName)) {
                String branchFileUID = c.map.get(fileName);
                File cWDFile = join(CWD, fileName);
                byte[] contents = readContents(cWDFile);
                String cWDFileUID = sha1(contents);
                if (!branchFileUID.equals(cWDFileUID)) {
                    System.out.println("There is an untracked file in the way; delete it, "
                            + "or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        //Delete all files in CWD
        for (String fileName : plainFilenamesIn(CWD)) {
            File cWDFile = join(CWD, fileName);
            cWDFile.delete();
        }
        for (String file : c.map.keySet()) {
            checkout(c, file);
        }
        //If reset, move the current branch pointer to the previous commit
        if (reset) {
            writeContents(head.BRANCH_FILE, commitID);
        }
        //Make branchHead the HEAD
        writeContents(HEAD_FILE, commitID);
        //Clear the staging area
        writeObject(ADD_FILE, new HashMap<String, String>(3));
        writeObject(REMOVE_FILE, new HashMap<String, String>(3));
    }
}
