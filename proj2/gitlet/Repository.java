package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
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
    static HashMap<String, String> addMap;
    static HashMap<String, String> removeMap;
    //How to format the date from Stack Overflow.
    static SimpleDateFormat formatForDates = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOB_FOLDER = join(GITLET_DIR, "blobs");
    public static final File COMMITS_FOLDER = join(GITLET_DIR, "commits");
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    public static final File ADD_FILE = join(STAGING_DIR, "add");
    public static final File REMOVE_FILE = join(STAGING_DIR, "remove");
    public static final File HEAD_FILE = join(GITLET_DIR, "head");
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    public static final File MASTER = join(BRANCHES_DIR, "master");
    public static final File CURRENT_BRANCH = join(GITLET_DIR, "currBranch");

    private static void setupPersistence() {
        /*Check if there's a version control system already in the CWD.*/
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the current directory.");
            System.exit(0);
        }
        //Make all the files and directories
        GITLET_DIR.mkdir();
        STAGING_DIR.mkdir();
        BLOB_FOLDER.mkdir();
        COMMITS_FOLDER.mkdir();
        BRANCHES_DIR.mkdir();
    }

    public static void init() {
        //Set up file structure so that gitlet persists.
        setupPersistence();
        writeContents(CURRENT_BRANCH, "master");
        //Make initial commit.
        Date epoch = new Date(0); //This is the right date, use date format when outputting logs.
        new Commit("initial commit", null, epoch);
        addMap = new HashMap<>(3);
        removeMap = new HashMap<>(3);
        writeObject(ADD_FILE, addMap);
        writeObject(REMOVE_FILE, removeMap);
    }

    public static Commit currentCommit() {
        //Load current commit
        String currentCommitID = readContentsAsString(HEAD_FILE);
        File currentCommitFile = join(COMMITS_FOLDER, currentCommitID);
        return readObject(currentCommitFile, Commit.class);
    }

    public static void commit(String message) {
        //Load date and UID of current (now parent) commit
        Date today = new Date();
        String currentCommitID = readContentsAsString(HEAD_FILE);
        //Commit
        new Commit(message, currentCommitID, today);
    }
}
