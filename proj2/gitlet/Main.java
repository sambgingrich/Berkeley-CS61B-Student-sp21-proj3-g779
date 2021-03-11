package gitlet;

import java.awt.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Utils.exitWithError("Must have at least one argument");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "log":
                log();
            case "init":
                init();
                break;
            case "add":
                add(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].isEmpty()) {
                    exitWithError("Please enter a commit message.");
                } else {
                    commit(args[1]);
                }
            case "checkout":
                if (args.length == 3) {
                    String headUID = readContentsAsString(HEAD_FILE);
                    Commit head = Commit.loadCommit(headUID);
                    /*check failure case
                    if (!head.map.containsKey(args[2])) {
                        exitWithError("File does not exist in that commit.");
                    }*/
                    //Handle case of checkout --filename
                    checkout(head, args[2]);
                } else if (args.length == 4) {
                    Commit commitX = Commit.loadCommit(args[1]); //handles the case that the ID doesn't exist.
                    /*Check failure cases

                    if (!commitX.map.containsKey(args[3])) {
                        exitWithError("File does not exist in that commit.");
                    } */
                    //Handle case of checkout commit id -- filename
                    checkout(commitX, args[3]);
                } else {
                    //Handle branch name case
                }
        }
    }

}
