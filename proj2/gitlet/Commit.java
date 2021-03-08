package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

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

    //Static variables for now, but have to find a way to use references to save the head commit and master branch.
    static Commit head;
    static Commit master;

    //instance variables
    Date date;
    String parent; //the SHA1 hash of the parent commit, not a pointer to commit object.
    String message;
    HashMap map;


    public Commit(String message, String parent, Date date) {
        this.message = message;
        this.parent = parent;
        this.date = date;
        if (parent == null) { //In the case of the parent commit
            return;
        }
        //Load parent node
        Commit p = loadCommit(parent);
        //In the case that there is nothing staged to be added or removed.
        if (Repository.addMap.isEmpty() & Repository.removeMap.isEmpty()) {
            this.map = p.map;
        }
        //Handle adding new mappings from Repository.addMap and removeMap here.



        //Advance head pointer
        head = this; // Each branch has it's own HEAD...but keep this for now.
        master = this; //Change this once we have to figure out merging.
        //Write the commit to disk
        saveCommit(this);
    }

    public void saveCommit(Commit c) {
        //setup persistence for commits.
    }

    //Returns a Commit from disk with the given UID.
    public Commit loadCommit(String parent) { //takes in UID
        return this;
    }
}
