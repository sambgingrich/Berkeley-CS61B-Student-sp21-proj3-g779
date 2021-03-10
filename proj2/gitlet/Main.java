package gitlet;

import static gitlet.Utils.exitWithError;

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
        Repository.setupPersistence();
        String firstArg = args[0];
        Repository.init();
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].isEmpty()) {
                    exitWithError("Please enter a commit message.");
                }
                Repository.commit(args[1]);
        }
    }

}
