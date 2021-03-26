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
    static SimpleDateFormat formatForDates = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z" );

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
    public static final File master = join(BRANCHES_DIR, "master");

    private static void setupPersistence() {
        /*Check if there's a version control system already in the CWD.
        if (GITLET_DIR.exists()) {
            exitWithError("A Gitlet version-control system already exists in the current directory.");
        }*/
        //Make all the files and directories
        GITLET_DIR.mkdir();
        STAGING_DIR.mkdir();
        BLOB_FOLDER.mkdir();
        COMMITS_FOLDER.mkdir();
        BRANCHES_DIR.mkdir();
    }

    public static void init(){
        //Set up file structure so that gitlet persists.
        setupPersistence();

        //Make initial commit.
        Date epoch = new Date(0); //This is the right date, use date format when outputting logs.
        Commit initial = new Commit("initial commit", null, epoch);
        addMap = new HashMap<> (3);
        removeMap = new HashMap<> (3);

        writeObject(ADD_FILE, addMap);
        writeObject(REMOVE_FILE, removeMap);
    }



    private static Commit currentCommit(){
        //Load current commit
        String currentCommitID = readContentsAsString(HEAD_FILE);
        File currentCommitFile = join(COMMITS_FOLDER, currentCommitID);
        return readObject(currentCommitFile, Commit.class);
    }

    public static void add(String fileName) {
        //Check if the file exists
        File newFile = join(CWD, fileName);
        if (!newFile.exists()) {
            exitWithError("File does not exist.");
        }
        //Load addMap
        addMap = readObject(ADD_FILE, HashMap.class);
        //Check if an identical SHA1-hash is in the current Commit
        byte[] contents = readContents(newFile);
        String UID = Utils.sha1(contents);
        Commit c = currentCommit();
        if (c.map != null && c.map.containsKey(fileName)
                && c.map.get(fileName).equals(UID)) {
            if (addMap.containsKey(fileName) && c.map.get(fileName).equals(UID)){
                addMap.remove(fileName);
            }
            writeObject(ADD_FILE, addMap);
            return;
        }
        //If it's not there, add it to the addMap
        addMap.put(fileName, UID);
        writeObject(ADD_FILE, addMap);
        /* Save a snapshot of the file to the BLOBS_FOLDER
        * with the SHA1 hash as the name of the file.
         */
        File newBlob = join(BLOB_FOLDER, UID);
        writeContents(newBlob, contents);
    }

    public static void rm(String filename) {
        /*Check failure case where the file is not in addMap
        or map or current commit */
        addMap = readObject(ADD_FILE, HashMap.class);
        Commit c = currentCommit();
        File removeFile = join(CWD, filename);
        if (addMap.containsKey(filename)) {
            addMap.remove(filename);
            removeFile.delete();
        } else if (c.map.containsKey(filename)) {
            removeMap = readObject(REMOVE_FILE, HashMap.class);
            removeMap.put(filename, null);
            removeFile.delete();
        } else {
            exitWithError("No reason to remove the file.");
        }
    }

    public static void commit(String message) {
        //Load addMap
        addMap = readObject(ADD_FILE, HashMap.class);
        removeMap = readObject(REMOVE_FILE, HashMap.class);
        //Catch failure cases here
        if (addMap.isEmpty() && removeMap.isEmpty()) {
            exitWithError("No changes added to the commit.");
        }
        //Load date and UID of current (now parent) commit
        Date today = new Date();
        String currentCommitID = readContentsAsString(HEAD_FILE);
        //Commit
        new Commit(message, currentCommitID, today);
    }

    /* For cases 1: Checkout a given file from the head
    * and 2: Checkout a given filename from a given commit ID */
    public static void checkout(Commit c, String fileName) {
        String blobUID = c.map.get(fileName);
        File blobFile = join(BLOB_FOLDER, blobUID);
        byte[] contents = readContents(blobFile);
        File CWDFile = join(CWD, fileName);
        writeContents(CWDFile, contents);
    }


    //Space here for branchCheckout

    public static void log() {
        String headUID = readContentsAsString(HEAD_FILE);
        loghelp(headUID);
    }

    private static void loghelp (String curr) {
        Commit c = Commit.loadCommit(curr);
        logmessage(curr);
        if (c.parent == null) {
            return;
        } else {
            loghelp(c.parent);
        }
    }

    private static void logmessage (String curr) {
        Commit c = Commit.loadCommit(curr);
        System.out.println("===");
        System.out.println("commit " + curr);
        System.out.println("Date: " + formatForDates.format(c.date));
        System.out.println(c.message);
        System.out.println();
    }
}
