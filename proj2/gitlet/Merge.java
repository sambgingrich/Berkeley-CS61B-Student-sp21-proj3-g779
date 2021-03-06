package gitlet;

import static gitlet.AddRemove.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import static gitlet.Repository.*;
import static gitlet.Utils.*;


public class Merge {
    private static boolean conflict;

    private static void checkUntracked(String otherBranch) {
        HashMap<String, String> addMap = readObject(ADD_FILE, HashMap.class);
        HashMap<String, String> removeMap = readObject(REMOVE_FILE, HashMap.class);
        if (!addMap.isEmpty() || !removeMap.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        Commit curr = currentCommit();
        File otherBranchFile = join(BRANCHES_DIR, otherBranch);
        String otherBranchHead = readContentsAsString(otherBranchFile);
        Commit other = Commit.loadCommit(otherBranchHead);
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!curr.map.keySet().contains(fileName)
                    && other.map.containsKey(fileName)) {
                String branchFileUID = other.map.get(fileName);
                File cWDFile = join(CWD, fileName);
                byte[] contents = readContents(cWDFile);
                String cWDFileUID = sha1(contents);
                if (!branchFileUID.equals(cWDFileUID)) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
    }

    private static Commit splitPoint(String curr, String other) {
        Commit c = Commit.loadCommit(curr);
        if (curr.equals(other)) {
            return c;
        } else {
            Commit o = Commit.loadCommit(other);
            String currParent = c.parent;
            String otherParent = o.parent;
            return Merge.splitPoint(currParent, otherParent);
        }
    }

    private static Set<String> relevantFiles(Commit splitPoint, Commit curr, Commit other) {
        Set<String> mergedSet = new HashSet<>();
        mergedSet.addAll(splitPoint.map.keySet());
        mergedSet.removeAll(curr.map.keySet());
        mergedSet.addAll(curr.map.keySet());
        mergedSet.removeAll(other.map.keySet());
        mergedSet.addAll(other.map.keySet());
        return mergedSet;
    }

    /* -1. Check for untracked files, check for uncommited changes.
     0. Find split point
     1. Create a list of files to consider
     2. Go through and decide what to put in CWD
     3. Check based on what's in the CWD and in the current branch what to stage
     4. Commit */

    public static void merge(String otherBranch) {
        checkUntracked(otherBranch);
        Commit curr = currentCommit();
        File otherBranchFile = join(BRANCHES_DIR, otherBranch);
        String otherBranchHead = readContentsAsString(otherBranchFile);
        Commit other = Commit.loadCommit(otherBranchHead);
        Commit split = splitPoint(readContentsAsString(HEAD_FILE), otherBranchHead);
        Set<String> relevantFiles = relevantFiles(split, curr, other);
        conflict = false;
        for (String file : relevantFiles) {
            if (split.map.containsKey(file)) { //In the split
                if (curr.map.containsKey(file)) { //In curr
                    if (curr.map.get(file).equals(split.map.get(file))) { //Unmodified in curr
                        if (other.map.containsKey(file)) {  //Unmodified in curr and in other
                            if (!split.map.get(file).equals(other.map.get(file))) {
                                //Unmodified curr, modified other
                                moveOtherVersion(file, other);
                            }
                        } else { //unmodified in curr and not present in other
                            File cWDFile = join(CWD, file);
                            cWDFile.delete();
                        }
                    } else { //Modified in curr
                        if (other.map.containsKey(file)) { //Modified in curr and present in other
                            if (other.map.get(file).equals(split.map.get(file))) {
                                return; //Modified curr, unmodified other
                            } else { //Modified in curr and modified in other
                                if (!curr.map.get(file).equals(other.map.get(file))) {
                                    conflict(curr, other, file); //Modified in different ways
                                }
                            }
                        } else { //Modified in curr and not present in other
                            conflict(curr, other, file);
                        }
                    }
                } else { // Not in curr
                    if (other.map.containsKey(file)) { // Not in curr, in other
                        if (!other.map.get(file).equals(split.map.get(file))) {
                            conflict(curr, other, file); //Not in curr, modified in other
                        } //Not in curr, unmodified in other
                    } //not in curr, not in other (in split)
                }
            } else { //Not in the split
                if (curr.map.containsKey(file)) { //Not in split, in curr
                    if (other.map.containsKey(file)) { //Not in split, in curr, in other
                        if (!other.map.get(file).equals(curr.map.get(file))) {
                            conflict(curr, other, file); //Not in split, different in curr/other
                        }
                    } //Not in split, in curr, not in other
                } else { //Not in split, not in curr
                    if (other.map.containsKey(file)) { //Not in split, not in curr, in other
                        moveOtherVersion(file, other);
                    }
                }
            }
        }
        stage(curr);
        String message = "Merged " + otherBranch
                + " into " + readContentsAsString(CURRENT_BRANCH) + ".";
        new Commit(message, readContentsAsString(HEAD_FILE), otherBranchHead);
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static void stage(Commit curr) {
        //Look through CWD and add files that have changed or aren't in curr
        for (String file : plainFilenamesIn(CWD)) {
            if (curr.map.containsKey(file)) {
                File cWDFile = join(CWD, file);
                byte[] contents = readContents(cWDFile);
                String cWDFileUID = Utils.sha1(contents);
                if (!curr.map.get(file).equals(cWDFileUID)) {
                    add(file);
                }
            } else {
                add(file);
            }
        }
        //Look through curr and remove files that aren't in CWD
        for (String file : curr.map.keySet()) {
            if (!plainFilenamesIn(CWD).contains(file)) {
                rm(file);
            }
        }
    }

    private static void moveOtherVersion(String file, Commit other) {
        File cWDFile = join(CWD, file);
        cWDFile.delete();
        String blobUID = other.map.get(file);
        File blobFile = join(BLOB_FOLDER, blobUID);
        byte[] contents = readContents(blobFile);
        writeContents(cWDFile, contents);
    }

    private static void conflict(Commit curr, Commit other, String file) {
        String contents;
        conflict = true;
        File cWDFile = join(CWD, file);
        cWDFile.delete();
        if (!curr.map.containsKey(file)) { //Not in curr
            contents = "<<<<<<< HEAD" + "\n" + "=======";
            File otherFile = join(BLOB_FOLDER, other.map.get(file));
            String otherContents = readContentsAsString(otherFile);
            contents = contents + "\n" +  otherContents + ">>>>>>>" + "\n";
        } else if (!other.map.containsKey(file)) { // Not in other
            File currFile = join(BLOB_FOLDER, curr.map.get(file));
            String currContents = readContentsAsString(currFile);
            contents = "<<<<<<< HEAD" + "\n" + currContents + "======="
                    + "\n" + ">>>>>>>" + "\n";
        } else { // in both
            File currFile = join(BLOB_FOLDER, curr.map.get(file));
            File otherFile = join(BLOB_FOLDER, other.map.get(file));
            String currContents = readContentsAsString(currFile);
            String otherContents = readContentsAsString(otherFile);
            contents = "<<<<<<< HEAD" + "\n" + currContents + "======="
                    + "\n" +  otherContents + ">>>>>>>" + "\n";
        }
        writeContents(cWDFile, contents);
    }
}
