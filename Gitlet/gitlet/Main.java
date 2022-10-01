package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Bryce.txt Szarzynski
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     *
     *  If a user inputs a command with the wrong number or format of operands,
     *  print the message Incorrect operands. and exit.
     *
     *  If a user inputs a command that requires being in an initialized Gitlet working directory
     *  (i.e., one containing a .gitlet subdirectory), but is not in such a directory, print the
     *  message Not in an initialized Gitlet directory.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.print("Please enter a command.");
            return;
        }
        Repository repo = new Repository();
        if (repo._HEAD == null && (!(args[0].equals("init")))) {
            System.out.print("Not in an initialized Gitlet directory.");
            return;
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                repo.init();
                break;
            case "add":
                repo.add(args[1]);
                break;
            case "commit":
                repo.commit(args[1]);
                break;
            case "rm":
                repo.rm(args[1]);
                break;
            case "log":
                repo.log();
                break;
            case "global-log":
                repo.global();
                break;
            case "find":
                repo.find(args[1]);
                break;
            case "status":
                repo.status();
                break;
            case "checkout":
                if (args[1].equals("--")) {
                    repo.checkout1(args[2]);
                } else if (args.length == 2) {
                    repo.checkout3(args[1]);
                } else if (args[2].equals("--")) {
                    repo.checkout2(args[1], args[3]);
                } else {
                    System.out.print("Incorrect operands.");
                    break;
                }
                break;
            case "rm-branch":
                repo.rmBranch(args[1]);
                break;
            case "branch":
                repo.branch(args[1]);
                break;
            case "reset":
                repo.reset(args[1]);
                break;
            case "merge":
                break;
            default:
                System.out.print("No command with that name exists.");
                break;
        }
    }
}
