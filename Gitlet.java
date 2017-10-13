package gitlet;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by EricLui on 17/7/2017.
 */
public class Gitlet implements Serializable {

    /**
     * Initialization
     */
    private HashMap<String, String> branches;
    private HashMap<String, String> tracked;
    private HashMap<String, String> staged;
    private HashMap<String, String> removed;
    private String head;
    private final String path = System.getProperty("user.dir");

    public Gitlet() throws IOException {
        Commit initial = new Commit("initial commit", null, null);
        initial.writeFile();
        branches = new HashMap<>();
        branches.put("master", initial.getHash());
        head = "master";
        tracked = new HashMap<>();
        staged = new HashMap<>();
        removed = new HashMap<>();
        writeFile();
    }

    HashMap<String, String> getBranches() {
        if (branches == null) {
            return new HashMap<>();
        }
        return branches;
    }

    HashMap<String, String> getTracked() {
        if (tracked == null) {
            return new HashMap<>();
        }
        return tracked;
    }

    void setTracked(HashMap<String, String> track) {
        tracked = track;
    }

    void removeTracked(String input) {
        tracked.remove(input);
    }

    HashMap<String, String> getStaged() {
        if (staged == null) {
            return new HashMap<>();
        }
        return staged;
    }

    void setStaged(HashMap<String, String> stage) {
        staged = stage;
    }

    void addStaged(String key, String val) {
        staged.put(key, val);
    }

    void removeStaged(String file) {
        staged.remove(file);
    }

    HashMap<String, String> getRemoved() {
        return removed;
    }

    void addRemoved(String key, String val) {
        removed.put(key, val);
    }


    public void removeRemoved(String key) {
        removed.remove(key);
    }

    public String getHead() {
        return head;
    }

    void setHead(String input) {
        head = input;
    }

    public void setBranches(String head1, String hash) {
        branches.put(head1, hash);
    }

    public void removeBranch(String name) {
        branches.remove(name);
    }

    /**
     * Commit Operations
     */
    //points head to new commit
    //adds the files tracked by new commit to tracked
    public void addCommit(Commit newCommit) throws IOException {
        newCommit.writeFile();
        branches.put(head, newCommit.getHash());
        tracked = newCommit.getFilenames();
        staged = new HashMap<>();
        removed = new HashMap<>();
    }

    //get the head Commit of the inputted branch
    public Commit getBranchHead(String branchName) throws IOException, ClassNotFoundException {
        return Commit.getCommit(branches.get(branchName));
    }

    /**
     * Staging Area Operations
     */
    public void unstage(String filename) {
        staged.remove(filename);
    }

    //serialize and save the object as a file in the .gitlet file
    public void writeFile() throws IOException {
        List<String> existing = Utils.plainFilenamesIn(path + "//.gitlet");
        File file = new File(path + "//.gitlet//Gitlet.txt");
        if (existing.contains("Gitlet.txt")) {
            file.delete();
        }
        Utils.writeHelper(this, path + "//.gitlet//Gitlet.txt");
    }

    /**
     * Static Methods
     */
    public static Gitlet getGitlet() {
        String path = System.getProperty("user.dir");
        return (Gitlet) Utils.readHelper(path + "//.gitlet//Gitlet.txt");
    }

    public static Commit getCurrCommit() throws IOException, ClassNotFoundException {
        Gitlet gitlet = Gitlet.getGitlet();
        String head = gitlet.branches.get(gitlet.head);
        return Commit.getCommit(head);
    }
}
