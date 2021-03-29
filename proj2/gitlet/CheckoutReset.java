package gitlet;

import java.io.File;
import java.util.HashMap;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class CheckoutReset {
    /* For cases 1: Checkout a given file from the head
     * and 2: Checkout a given filename from a given commit ID */
    public static void checkout(Commit c, String fileName) {
        String blobUID = c.map.get(fileName);
        File blobFile = join(BLOB_FOLDER, blobUID);
        byte[] contents = readContents(blobFile);
        File cWDFile = join(CWD, fileName);
        writeContents(cWDFile, contents);
    }

    /* Cover the branch checkout case */
    public static void checkout(String branchName) {
        //check branch exists
        if (!plainFilenamesIn(BRANCHES_DIR).contains(branchName)) {
            System.out.println("No such branch exists.");
        }
        //Load HEAD commit and branch's head
        String headUID = readContentsAsString(HEAD_FILE);
        Commit head = Commit.loadCommit(headUID);
        File branchFile = join(BRANCHES_DIR, branchName);
        String branchHeadUID = readContentsAsString(branchFile);
        //check branch is the current branch
        if (branchHeadUID.equals(headUID)) {
            System.out.println("No need to checkout the current branch");
            System.exit(0);
        }
        moveTrackedFiles(branchHeadUID, false);
    }

    public static void reset(String commitID) {
        moveTrackedFiles(commitID, true);
    }

    private static void moveTrackedFiles(String commitID, boolean reset) {
        //check for  untracked files
        String headUID = readContentsAsString(HEAD_FILE);
        Commit head = Commit.loadCommit(headUID);
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!head.map.entrySet().contains(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, "
                        + "or add and commit it first.");
                System.exit(0);
            }
        }
        Commit c = Commit.loadCommit(commitID);
        //Delete all files in CWD that weren't in in branchHead
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!c.map.entrySet().contains(fileName)) {
                File fileToDelete = join(CWD, fileName);
                fileToDelete.delete();
            } else { //Move all the files from branchHead to CWD, overwriting existing files
                String blobUID = c.map.get(fileName);
                File blobFile = join(BLOB_FOLDER, blobUID);
                byte[] contents = readContents(blobFile);
                File cWDFile = join(CWD, fileName);
                writeContents(cWDFile, contents);
            }
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
