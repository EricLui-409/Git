package gitlet;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by EricLui on 16/7/2017.
 */
public class FuncCommit {

    private HashMap<String, String> stagingArea;
    private HashMap<String, String> tracking;
    private HashMap<String, String> removed;
    private String message;
    private String path = System.getProperty("user.dir");
    private Commit newCommit;
    private Gitlet gitlet;

    public FuncCommit(String[] mes) throws InvalidOperandsException {
        if (mes[1].equals("")) {
            throw new InvalidOperandsException("Please enter a commit message.");
        } else if (mes.length > 2) {
            throw new InvalidOperandsException("Only one commit message allowed.");
        } else {
            message = mes[1];
        }
    }

    public void apply() throws IOException, ClassNotFoundException {
        gitlet = Gitlet.getGitlet();
        stagingArea = gitlet.getStaged();
        tracking = gitlet.getTracked();
        removed = gitlet.getRemoved();
        boolean success;
        success = createCommit();
        if (!success) {
            return;
        }
        newCommit.writeFile();
        gitlet.addCommit(newCommit);
        gitlet.writeFile();
    }

    //create a new commit
    public boolean createCommit() throws IOException, ClassNotFoundException {
        if (stagingArea.size() == 0 && removed.size() == 0) {
            System.out.println("No changes added to the commit");
            return false;
        } else {
            String prev = Gitlet.getCurrCommit().getHash();
            HashMap<String, String> currContents = new HashMap<>();
            for (String filename : tracking.keySet()) {
                if (!removed.keySet().contains(filename)) {
                    currContents.put(filename, tracking.get(filename));
                }
            }
            for (String filename : stagingArea.keySet()) {
                currContents.put(filename, stagingArea.get(filename));
            }
            newCommit = new Commit(message, prev, currContents);
            return true;
        }
    }
}
