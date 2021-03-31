package gitlet;

import static gitlet.AddRemove.*;
import java.io.File;
import java.util.Set;
import static gitlet.Repository.*;
import static gitlet.Utils.*;


public class Merge {
    private static Commit splitPoint(String curr, String other) {
        Commit c = Commit.loadCommit(curr);
        Commit o = Commit.loadCommit(other);
        if (c.equals(o)) {
            return c;
        } else {
            return splitPoint(c.parent, o.parent);
        }
    }

    private static Set<String> relevantFiles(Commit splitPoint, Commit curr, Commit other) {
        Set<String> mergedSet = splitPoint.map.keySet();
        mergedSet.removeAll(curr.map.keySet());
        mergedSet.addAll(curr.map.keySet());
        mergedSet.removeAll(other.map.keySet());
        mergedSet.addAll(other.map.keySet());
        return mergedSet;
    }

    /*0. Find split point
        1. Create a list of files to consider
        2. Go through and decide what to put in CWD
        3. Check based on what's in the CWD and in the current branch what to stage
        4. Commit */

    public static void merge(String otherBranch) {
        Commit curr = currentCommit();
        File otherBranchFile = join(BRANCHES_DIR, otherBranch);
        String otherBranchHead = readContentsAsString(otherBranchFile);
        Commit other = Commit.loadCommit(otherBranchHead);
        Commit split = splitPoint(readContentsAsString(HEAD_FILE), otherBranch);
        Set<String> relevantFiles = relevantFiles(split, curr, other);
        boolean conflict = false;
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
                            if (other.map.get(file).equals(split.map.get(curr))) {
                                return; //Modified curr, unmodified other
                            } else { //Modified in curr and modified in other
                                if (!curr.map.get(file).equals(other.map.get(file))) {
                                    conflict = true; //Modified in different ways
                                }
                            }
                        } else { //Modified in curr and not present in other
                            conflict = true;
                        }
                    }
                } else { // Not in curr
                    if (other.map.containsKey(file)) { // Not in curr, in other
                        if (other.map.get(file).equals(split.map.get(file))) {
                            //Not in curr, modified in other
                            conflict = true;
                        }
                    } //not in curr, not in other (in split)
                }
            } else { //Not in the split
                if (curr.map.containsKey(file)) { //Not in split, in curr
                    if (other.map.containsKey(file)) { //Not in split, in curr, in other
                        if (!other.map.get(file).equals(curr.map.get(file))) {
                            conflict = true; //Not in split, different in curr/other
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
        String message = "Merged " + otherBranch + " into " + curr.BRANCH_FILE;
        new Commit(message, readContentsAsString(CURRENT_BRANCH), otherBranchHead);
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

    private static void conflict(String currUID, String otherUID) {

    }

}
