package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  This is where the main project of the program is.
 *  This handles all the commands that are called in the Main file
 *  and makes sure that commits persist
 *
 *  @author Samuel Gingrich
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    static HashMap addMap;
    static HashMap removeMap;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOB_FOLDER = join(GITLET_DIR, "blobs");
    public static final File COMMITS_FOLDER = join(GITLET_DIR, "commits");
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    public static final File ADD_FILE = join(GITLET_DIR, "staging");
    public static final File REMOVE_FILE = join(GITLET_DIR, "staging");

    public static void init(){
        //Check if there's a version control system already in the CWD.
        if (GITLET_DIR.exists()) {
            exitWithError("A Gitlet version-control system already exists in the current directory.");
        }
        //Make initial commit.
        Date epoch = new Date(0); //Hopefully this is the right date.
        Commit initial = new Commit("initial commit", null, epoch);
        addMap = new HashMap();
        removeMap = new HashMap();
        setupPersistence();
        writeObject(ADD_FILE, addMap);
        writeObject(REMOVE_FILE, removeMap);
    }

    private static void setupPersistence() {
        GITLET_DIR.mkdir();
        STAGING_DIR.mkdir();
        try {
            BLOB_FOLDER.createNewFile();
            COMMITS_FOLDER.createNewFile();
            ADD_FILE.createNewFile();
            REMOVE_FILE.createNewFile();
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    public static void add() {
        //implement the add function here.
    }
}
