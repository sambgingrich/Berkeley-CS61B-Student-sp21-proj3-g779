package gitlet;

import java.io.File;
import java.util.HashMap;
import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class AddRemove {
    public static void add(String fileName) {
        //Check if the file exists
        File newFile = join(CWD, fileName);
        if (!newFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        //Load addMap
        HashMap<String, String> addMap = readObject(ADD_FILE, HashMap.class);
        HashMap<String, String> removeMap = readObject(REMOVE_FILE, HashMap.class);
        //Check if an identical SHA1-hash is in the current Commit
        byte[] contents = readContents(newFile);
        String uID = Utils.sha1(contents);
        Commit c = currentCommit();
        //If it's tracked in current commit and hasn't changed, do nothing.
        if (c.map != null && c.map.containsKey(fileName)
                && c.map.get(fileName).equals(uID)) {
            if (removeMap.containsKey(fileName)) {
                removeMap.remove(fileName);
            }
        }
        //If an older version is there, overwrite it
        if (addMap.containsKey(fileName)) {
            addMap.remove(fileName);
        }
        //Unstage for removal
        if (removeMap.containsKey(fileName)) {
            removeMap.remove(fileName);
        }
        //If it's not there, add it to the addMap
        addMap.put(fileName, uID);
        writeObject(ADD_FILE, addMap);
        writeObject(REMOVE_FILE, removeMap);
        /* Save a snapshot of the file to the BLOBS_FOLDER
         * with the SHA1 hash as the name of the file.
         */
        File newBlob = join(BLOB_FOLDER, uID);
        writeContents(newBlob, contents);
    }

    public static void rm(String filename) {
        /*Check failure case where the file is not in addMap
        or map or current commit */
        HashMap<String, String> addMap = readObject(ADD_FILE, HashMap.class);
        Commit c = currentCommit();
        File removeFile = join(CWD, filename);
        HashMap<String, String> removeMap = readObject(REMOVE_FILE, HashMap.class);

        if (!addMap.containsKey(filename) && !c.map.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        if (addMap.containsKey(filename)) {
            addMap.remove(filename);
        }
        if (c.map.containsKey(filename)) {
            removeMap.put(filename, c.map.get(filename));
            removeFile.delete();
        }

        writeObject(ADD_FILE, addMap);
        writeObject(REMOVE_FILE, removeMap);
    }
}
