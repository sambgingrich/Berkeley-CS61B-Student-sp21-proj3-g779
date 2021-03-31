package gitlet;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  This is a class that represents commits used in the repository class.
 * Commits contain metadata and pointers to files with specific hashes.
 *
 *
 * Creating a new commit:
 * 1. Clone head commit
 * 2. Change Metadata according to user
 * 3. Track files to blobs based on the staging area
 * 4. Denote the parent of current commit as the HEAD
 * 5. Advance HEAD to point to new HEAD, MASTER.
 * 6. Clear staging area
 *
 * A commit is a map of file names to blobs. File names
 * don't map to blobs directly, but instead map to
 * a blob's unique hash code.
 *  @author Samuel Gingrich
 */
public class Commit implements Serializable {
    //instance variables
    Date date;
    String parent; //the SHA1 hash of the parent commit, not a pointer to commit object.
    String parent2;
    String message;
    HashMap<String, String> map;
    File BRANCH_FILE;


    public Commit(String message, String parent, Date date) {
        this.message = message;
        this.parent = parent;
        this.date = date;
        //In the case of the initial commit
        if (parent == null) {
            this.BRANCH_FILE = MASTER;
            this.map = new HashMap<>(5);
            saveCommit(this);
        } else {
            Commit p = loadCommit(parent);
            modifyMaps(p, this);
            //Set branch to parent's branch
            this.BRANCH_FILE = p.BRANCH_FILE;
            saveCommit(this);
        }
    }
    
    public Commit(String message, String parent, String parent2) {
        this.message = message;
        this.parent = parent;
        this.parent2 = parent2;
        this.date = new Date();
        Commit p = loadCommit(parent);
        this.BRANCH_FILE = p.BRANCH_FILE;
        modifyMaps(p, this);
        saveCommit(this);
    }

    private static void modifyMaps (Commit p, Commit c) {
        HashMap<String, String> addMap = readObject(ADD_FILE, HashMap.class);
        HashMap<String, String> removeMap = readObject(REMOVE_FILE, HashMap.class);
        //In the case that there is nothing staged to be added or removed.
        if (addMap.isEmpty() && removeMap.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        //Handle adding new mappings from Repository.addMap here.
        //Source: Learned how to iterate through hashmap from geeksforgeeks.org
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            //Case where the file name is already in the parent map.
            if (p.map.replace(key, val) == null) {
                p.map.put(key, val); //Case where the filename wasn't in the parent map.
            }
        }
        //Handle removing mappings from remove map
        for (Map.Entry<String, String> entry : removeMap.entrySet()) {
            String key = entry.getKey();
            p.map.remove(key);
        }
        c.map = p.map;
    }

    private static void saveCommit(Commit c) {
        //setup persistence for commits.
        byte[] cAsByte = serialize(c);
        String uID = Utils.sha1(cAsByte);
        File newCommit = join(COMMITS_FOLDER, uID);
        try {
            newCommit.createNewFile();
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
        writeObject(newCommit, c);
        //Write the SHA1 hash of c into the HEAD and branch files once they are cleared.
        writeContents(HEAD_FILE, uID);
        writeContents(c.BRANCH_FILE, uID);
        //Clear the add and remove maps
        writeObject(ADD_FILE, new HashMap<String, String>(3));
        writeObject(REMOVE_FILE, new HashMap<String, String>(3));
    }

    //Returns a Commit from disk with the given uID.
    public static Commit loadCommit(String uID) {
        String uIDToLoad = uID;
        //Check for case where the 6 digit abbreviation is used
        if (uID.length() < 40) {
            boolean found = false;
            for (String commituID : plainFilenamesIn(COMMITS_FOLDER)) {
                if (commituID.startsWith(uID)) {
                    found = true;
                    uIDToLoad = commituID;
                    break;
                }
            }
            if (!found) {
                System.out.println("No commit with that id exists.");
            }
        }
        File loadFile = join(COMMITS_FOLDER, uIDToLoad);
        /*Check if the file exists*/
        if (!loadFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit c = readObject(loadFile, Commit.class);
        return c;
    }
}
