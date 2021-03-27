package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class Checkout {
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
}
