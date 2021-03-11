package gitlet;

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
 * 5. Advance HEAD to point to new HEAD, master.
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
    String message;
    HashMap<String, String> map;
    File BRANCH_FILE;


    public Commit(String message, String parent, Date date) {
        this.message = message;
        this.parent = parent;
        this.date = date;
        if (parent != null) { //In the case of the initial commit
            //Load parent node
            Commit p = loadCommit(parent);
            //Set branch to parent's branch
            this.BRANCH_FILE = p.BRANCH_FILE;
            //In the case that there is nothing staged to be added or removed.
            if (Repository.addMap.isEmpty() & Repository.removeMap.isEmpty()) {
                this.map = p.map;
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
                String val = entry.getValue();
                p.map.remove(key, val);
            }
            this.map = p.map;
            //Clear the add and remove maps
            addMap.clear();
            removeMap.clear();
            //Write the commit to disk, persist HEAD and master pointers
        }
        //In the case of the initial commit.
        if (parent == null) {
            this.BRANCH_FILE = master;
        }
        saveCommit(this);
    }

    public void saveCommit(Commit c) {
        //setup persistence for commits.
        byte[] cAsByte = serialize(c);
        String UID = Utils.sha1(cAsByte);
        File newCommit = join(COMMITS_FOLDER, UID);
        try {
            newCommit.createNewFile();
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
        writeObject(newCommit, c);
        //Write the SHA1 hash of c into the HEAD and branch files.
        writeObject(HEAD_FILE, UID);
        writeObject(BRANCH_FILE, UID);
    }

    //Returns a Commit from disk with the given UID.
    public static Commit loadCommit(String UID) {
        File loadFile = join(COMMITS_FOLDER, UID);
        /*Check if the file exists
        if (!loadFile.exists()) {
            exitWithError("No commit with that id exists.");
        }*/
        Commit c = readObject(loadFile, Commit.class);
        return c;
    }
}
