package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by EricLui on 16/7/2017.
 */
public class FuncStatus {

    private Gitlet gitlet;
    private String path = System.getProperty("user.dir");

    public FuncStatus(String[] arg) {
        if (arg.length != 1) {
            throw new InvalidOperandsException("No arguments required");
        }
    }

    public void apply() throws IOException, ClassNotFoundException {
        gitlet = Gitlet.getGitlet();

        printBranch();
        printStaged();
        printRemoved();
        printModified();
        printUntracked();
    }

    public void printBranch() {
        Set<String> branchNames = gitlet.getBranches().keySet();
        String head = gitlet.getHead();
        branchNames.remove(head);

        System.out.println("=== Branches ===");
        System.out.println("*" + head);
        for (String b : branchNames) {
            System.out.println(b);
        }
        System.out.println();
    }

    public void printStaged() {
        System.out.println("=== Staged Files ===");
        for (String s : gitlet.getStaged().keySet()) {
            System.out.println(s + ".txt");
        }
        System.out.println();
    }

    public void printRemoved() {
        System.out.println("=== Removed Files ===");
        for (String s : gitlet.getRemoved().keySet()) {
            System.out.println(s + ".txt");
        }
        System.out.println();
    }

    public void printModified() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        HashMap<String, String> staged = gitlet.getStaged();
        HashMap<String, String> tracked = gitlet.getTracked();
        File file1 = new File(path);
        List<String> workingDir = Utils.plainFilenamesIn(file1);
        for (String file : staged.keySet()) {
            if (!workingDir.contains(file + ".txt")) {
                System.out.println(file + ".txt (deleted)");
            }
        }
        for (String name : tracked.keySet()) {
            if (!staged.keySet().contains(name)
                    && !workingDir.contains(name + ".txt")
                    && !gitlet.getRemoved().keySet().contains(name)) {
                System.out.println(name + ".txt (deleted)");
            }
        }
        for (String s : workingDir) {
            File file2 = new File(path + "//" + s);
            Blob curr = new Blob(file2);
            if ((!tracked.values().contains(curr.getHash())
                    && tracked.keySet().contains(curr.getFilename())
                    && !staged.keySet().contains(curr.getFilename()))
                    || (staged.keySet().contains(curr.getFilename())
                    && staged.get(curr.getFilename()) != curr.getHash())) {
                System.out.println(s + " (modified)");
            }
        }
        System.out.println();
    }

    public void printUntracked() {
        System.out.println("=== Untracked Files ===");
        HashMap<String, String> tracked = gitlet.getTracked();
        File file1 = new File(path);
        List<String> workingDir = Utils.plainFilenamesIn(file1);
        for (String file : workingDir) {
            if (!tracked.keySet().contains(file.substring(0, file.length() - 4))
                    && !gitlet.getStaged().keySet()
                    .contains(file.substring(0, file.length() - 4))) {
                System.out.println(file);
            }
        }
        System.out.println();
    }
}
