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
        addMap = readObject(ADD_FILE, HashMap.class);
        //Check if an identical SHA1-hash is in the current Commit
        byte[] contents = readContents(newFile);
        String uID = Utils.sha1(contents);
        Commit c = currentCommit();
        if (c.map != null && c.map.containsKey(fileName)
                && c.map.get(fileName).equals(uID)) {
            if (addMap.containsKey(fileName) && c.map.get(fileName).equals(uID)) {
                addMap.remove(fileName);
            }
            writeObject(ADD_FILE, addMap);
            return;
        }
        //If it's not there, add it to the addMap
        addMap.put(fileName, uID);
        writeObject(ADD_FILE, addMap);
        /* Save a snapshot of the file to the BLOBS_FOLDER
         * with the SHA1 hash as the name of the file.
         */
        File newBlob = join(BLOB_FOLDER, uID);
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
            System.out.println("No reason to remove the file.");
        }
    }
}
