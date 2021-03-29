package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Repository.*;
import static gitlet.Utils.*;


public class Log {
    public static void log() {
        String headUID = readContentsAsString(HEAD_FILE);
        loghelp(headUID);
    }

    private static void loghelp(String curr) {
        File loadFile = join(COMMITS_FOLDER, curr);
        Commit c = readObject(loadFile, Commit.class);
        logMessage(curr);
        if (c.parent == null) {
            return;
        } else {
            loghelp(c.parent);
        }
    }

    private static void logMessage(String curr) {
        File loadFile = join(COMMITS_FOLDER, curr);
        Commit c = readObject(loadFile, Commit.class);
        System.out.println("===");
        System.out.println("commit " + curr);
        System.out.println("Date: " + formatForDates.format(c.date));
        System.out.println(c.message);
        System.out.println();
    }

    public static void globalLog() {
        List<String> commitsList = plainFilenamesIn(COMMITS_FOLDER);
        for (String c : commitsList) {
            logMessage(c);
        }
    }

    public static void find(String message) {
        List<String> commitsList = plainFilenamesIn(COMMITS_FOLDER);
        boolean cExists = false;
        for (String c : commitsList) {
            Commit charlie = Commit.loadCommit(c);
            if (charlie.message.equals(message)) {
                System.out.println(c);
                cExists = true;
            }
        }
        if (!cExists) {
            System.out.println("Found no commit with that message.");
        }
    }
}
