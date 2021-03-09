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
    static HashMap<String, String> addMap;
    static HashMap<String, String> removeMap;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOB_FOLDER = join(GITLET_DIR, "blobs");
    public static final File COMMITS_FOLDER = join(GITLET_DIR, "commits");
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    public static final File ADD_FILE = join(GITLET_DIR, "staging");
    public static final File REMOVE_FILE = join(GITLET_DIR, "staging");
    public static final File HEAD_FILE = join(GITLET_DIR, "head");
    public static final File MASTER_FILE = join(GITLET_DIR, "master");

    public static void init(){
        //Check if there's a version control system already in the CWD.
        if (GITLET_DIR.exists()) {
            exitWithError("A Gitlet version-control system already exists in the current directory.");
        }
        //Make initial commit.
        Date epoch = new Date(0); //Hopefully this is the right date.
        Commit initial = new Commit("initial commit", null, epoch);
        addMap = new HashMap<> (3);
        removeMap = new HashMap<> (3);

        //Set up file structure so that gitlet persists.
        setupPersistence();
        writeObject(ADD_FILE, addMap);
        writeObject(REMOVE_FILE, removeMap);
    }

    private static void setupPersistence() {
        GITLET_DIR.mkdir();
        STAGING_DIR.mkdir();
        BLOB_FOLDER.mkdir();
        COMMITS_FOLDER.mkdir();
        try {
            ADD_FILE.createNewFile();
            REMOVE_FILE.createNewFile();
            HEAD_FILE.createNewFile();
            MASTER_FILE.createNewFile();

        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
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
        //Check if an identical SHA1-hash is in the current Commit
        String UID = Utils.sha1(newFile);
        Commit c = currentCommit();
        if (c.map.containsKey(fileName) && c.map.get(fileName) == UID) {
            if (addMap.containsKey(fileName) && c.map.get(fileName) == UID){
                addMap.remove(fileName);
            }
            return;
        }
        //If it's not there, add it to the addMap
        addMap.put(fileName, UID);
        /* Save a snapshot of the file to the BLOBS_FOLDER
        * with the SHA1 hash as the name of the file.
         */
        File newBlob = join(BLOB_FOLDER, UID);
        try {
            newBlob.createNewFile();
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }
}
