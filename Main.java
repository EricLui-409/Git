package gitlet;

import java.io.IOException;

public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) throws IOException, ClassNotFoundException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String command1 = args[0];
        try {
            switch (command1) {
                case "init":
                    new FuncInit().apply(args);
                    break;
                case "add":
                    new FuncAdd(args).apply();
                    break;
                case "commit":
                    new FuncCommit(args).apply();
                    break;
                case "rm":
                    new FuncRm(args).apply();
                    break;
                case "log":
                    new FuncLog(args).apply();
                    break;
                case "global-log":
                    new FuncGloballog(args).apply();
                    break;
                case "find":
                    new FuncFind(args).apply();
                    break;
                case "status":
                    new FuncStatus(args).apply();
                    break;
                case "checkout":
                    new FuncCheckout(args).apply();
                    break;
                case "branch":
                    new FuncBranch(args).apply();
                    break;
                case "rm-branch":
                    new FuncRmbranch(args).apply();
                    break;
                case "reset":
                    new FuncReset(args).apply();
                    break;
                case "merge":
                    new FuncMerge(args).apply();
                    break;
                default:
                    System.out.println("No command with that name exists.");
                    break;
            }
        } catch (InvalidOperandsException e) {
            System.out.println(e.getMessage());
        }
    }
}

