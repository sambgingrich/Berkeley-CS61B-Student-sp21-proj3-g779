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
            //In the split
            if (split.map.containsKey(file)) {
                //In curr
                if (curr.map.containsKey(file)) {
                    //Unmodified in curr
                    if (curr.map.get(file).equals(split.map.get(file))) {
                        //Unmodified in curr and in other
                        if (other.map.containsKey(file)) {
                            //Unmodified in curr and unmodified in other
                            if (split.map.get(file).equals(other.map.get(file))) {
                                return;
                            } else { //Unmodified in curr and modified in other
                                File cWDFile = join(CWD, file);
                                cWDFile.delete();
                                String blobUID = other.map.get(file);
                                File blobFile = join(BLOB_FOLDER, blobUID);
                                byte[] contents = readContents(blobFile);
                                writeContents(cWDFile, contents);
                            }
                        } else { //unmodified in curr and not present in other
                            File cWDFile = join(CWD, file);
                            cWDFile.delete();
                        }
                    } else {//Modified in curr
                        //Modified in curr and present in other
                        if (other.map.containsKey(file)) {
                            //Modified in curr and unmodified in other
                            if (other.map.get(file).equals(split.map.get(curr))) {
                                return; //May need to move the curr version to the CWD in a special case
                            } else { //Modified in curr and modified in other
                                //Modified in the same way
                                if(curr.map.get(file).equals(other.map.get(file))) {
                                    return;
                                } else { //Modified in different ways
                                    //CONFLICT!
                                    conflict = true;
                                }
                            }
                        } else { //Modified in curr and not present in other
                            //Conflict!
                            conflict = true;
                        }
                    }
                } else { // Not in curr
                    // Not in curr, in other
                    if (other.map.containsKey(file)) {
                        //Not in curr, unmodified in other
                        if (other.map.get(file).equals(split.map.get(file))) {
                            return;
                        } else { //Not in curr, modified in other
                            //CONFLICT
                            conflict = true;
                        }
                    } else { //not in curr, not in other (in split)
                        return;
                    }
                }
            } else {//Not in the split
                //Not in split, in curr
                if(curr.map.containsKey(file)) {
                    //Not in split, in curr, in other
                    if(other.map.containsKey(file)) {
                        //(Modified) the same in curr and other
                        if (other.map.get(file).equals(curr.map.get(file))) {
                            return;
                        } else {
                            //CONFLICT!
                            conflict = true;
                        }
                    } else { //Not in split, in curr, not in other
                        return; //May need to move curr version to CWD in special case
                    }
                } else { //Not in split, not in curr
                    //Not in split, not in curr, in other
                    if(other.map.containsKey(file)) {
                        //Move other version to CWD
                        File cWDFile = join(CWD, file);
                        cWDFile.delete();
                        String blobUID = other.map.get(file);
                        File blobFile = join(BLOB_FOLDER, blobUID);
                        byte[] contents = readContents(blobFile);
                        writeContents(cWDFile, contents);
                    } else { //Not in split, not in curr, not in other
                        System.out.println("Considering an irrelevant file");
                        System.exit(0);
                    }
                }
            }
        }
        //Stage the right files (this assumes the right files are in the CWD)
        stage(curr);
        //Commit
        String message = "Merged " + otherBranch + " into " +curr.BRANCH_FILE;
        String currUID = readContentsAsString(HEAD_FILE);
        new Commit(message, currUID, otherBranchHead);
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

    private static void conflict(String currUID, String otherUID) {

    }

}
