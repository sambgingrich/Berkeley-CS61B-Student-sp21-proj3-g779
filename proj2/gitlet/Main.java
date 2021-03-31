package gitlet;

import java.io.File;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.Log.*;
import static gitlet.AddRemove.*;
import static gitlet.CheckoutReset.*;
import static gitlet.Status.*;
import static gitlet.Merge.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     *  For some reason it only works if you add new cases above the log case,
     *  it doesn't like it when you add them at the end. Probably had something
     *  to do with "break" at the end of every case.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "merge":
                if (!plainFilenamesIn(BRANCHES_DIR).contains(args[1])) {
                    System.out.println("A branch with that name does not exist.");
                    System.exit(0);
                }
                if (args[1].equals(readContentsAsString(CURRENT_BRANCH))) {
                    System.out.println("Cannot merge a branch with itself.");
                    System.exit(0);
                } else {
                    merge(args[1]);
                }
                break;
            case "global-log":
                globalLog();
                break;
            case "find":
                find(args[1]);
                break;
            case "status":
                status();
                break;
            case "init":
                init();
                break;
            case "add":
                add(args[1]);
                break;
            case "rm-branch":
                File branchFile = join(BRANCHES_DIR, args[1]);
                if (!branchFile.exists()) {
                    System.out.println("A branch with that name does not exists.");
                    System.exit(0);
                } else if (args[1].equals(readContentsAsString(CURRENT_BRANCH))) {
                    System.out.println("Cannot remove the current branch.");
                    System.exit(0);
                } else {
                    branchFile.delete();
                }
                break;
            case "rm":
                rm(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                } if (args.length > 2) {
                    System.out.println("Incorrect operands");
                } else {
                    commit(args[1]);
                }
                break;
            case "checkout":
                if (args.length == 3) {
                    Commit head = currentCommit();
                    //Handle case of checkout --filename
                    checkout(head, args[2]);
                } else if (args.length == 4) {
                    //handles the case that the ID doesn't exist.
                    Commit commitX = Commit.loadCommit(args[1]);
                    //Handle case of checkout commit id -- filename
                    checkout(commitX, args[3]);
                } else {
                    checkout(args[1]);
                }
                break;
            case "branch":
                File branchName = join(BRANCHES_DIR, args[1]);
                if (branchName.exists()) {
                    System.out.println("A branch with that name already exists.");
                    System.exit(0);
                } else {
                    /* Branch command creates new file with the name
                     of the the new branch and the sha-1 hash of the head
                     commit.*/
                    String headUID = readContentsAsString(HEAD_FILE);
                    writeContents(branchName, headUID);
                }
                break;
            case "log":
                log();
                break;
            case "reset":
                reset(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
