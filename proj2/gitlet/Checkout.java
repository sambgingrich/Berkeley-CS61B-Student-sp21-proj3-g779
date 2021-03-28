package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class Checkout {
    /* For cases 1: Checkout a given file from the head
     * and 2: Checkout a given filename from a given commit ID */
    public static void checkout(Commit c, String fileName) {
        String blobUID = c.map.get(fileName);
        File blobFile = join(BLOB_FOLDER, blobUID);
        byte[] contents = readContents(blobFile);
        File CWDFile = join(CWD, fileName);
        writeContents(CWDFile, contents);
    }

    /* Cover the branch checkout case */
    public static void checkout(String branchName) {
        //check branch exists
        if (!plainFilenamesIn(BRANCHES_DIR).contains(branchName)) {
            error("No such branch exists.", null);
        }
        //Load HEAD commit and branch's head
        String headUID = readContentsAsString(HEAD_FILE);
        Commit head = Commit.loadCommit(headUID);
        File branchFile = join(BRANCHES_DIR, branchName);
        String branchHeadUID = readContentsAsString(branchFile);
        //check branch is the current branch
        if (branchHeadUID.equals(headUID)) {
            error("No need to checkout the current branch");
        }
        //check for  untracked files
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!head.map.entrySet().contains(fileName)) {
                error("There is an untracked file in the way; delete it, or add and commit it first.", null);
            }
        }
        Commit branchHead = Commit.loadCommit(branchHeadUID);
        //Delete all files in CWD that weren't in in branchHead,
        // or maybe just clear the CWD?
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!branchHead.map.entrySet().contains(fileName)) {
                File fileToDelete = join(CWD, fileName);
                fileToDelete.delete();
            }
        }
        //Move all the files from branchHead to CWD, overwriting existing files

        //Make branchHead the HEAD

        //Clear the staging area
    }
}
