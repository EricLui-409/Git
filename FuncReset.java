package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by EricLui on 19/7/2017.
 */
public class FuncReset {

    private String commitID;
    private Gitlet gitlet;
    private Commit requiredCommit;
    private String path = System.getProperty("user.dir");

    public FuncReset(String[] messages) {
        if (messages.length != 2) {
            throw new InvalidOperandsException("Invalid number of arguments passed in.");
        } else {
            commitID = messages[1];
        }
    }

    public void apply() throws IOException, ClassNotFoundException {
        try {
            commitID = finderFunc(commitID);
        } catch (InvalidOperandsException e) {
            System.out.println("No commit with that id exists.");
            return;
        }
        gitlet = Gitlet.getGitlet();
        requiredCommit = Commit.getCommit(commitID);
        HashMap<String, String> branchHeadContents = requiredCommit.getFilenames();
        HashMap<String, String> tracked = gitlet.getTracked();
        File file1 = new File(path);
        List<String> workingDir = Utils.plainFilenamesIn(file1);
        for (String file : workingDir) {
            if (!tracked.containsKey(file.substring(0, file.length() - 4))
                    && branchHeadContents.containsKey(
                    file.substring(0, file.length() - 4))) {
                System.out.println("There is "
                        + "an untracked file in the way; delete it or add it first.");
                return;
            }
        }
        for (String s : workingDir) {
            if (tracked.containsKey(s.substring(0, s.length() - 4))
                    && !branchHeadContents.containsKey(s.substring(0, s.length() - 4))) {
                Utils.restrictedDelete(s);
            }
        }
        for (String filename : branchHeadContents.keySet()) {
            Blob curr = Blob.getBlob(branchHeadContents.get(filename));
            File file2 = new File(path + "//" + filename + ".txt");
            Utils.writeContents(file2, curr.getContent());
        }
        gitlet.setStaged(new HashMap<>());
        gitlet.setTracked(requiredCommit.getFilenames());
        gitlet.setBranches(gitlet.getHead(), commitID);
        gitlet.writeFile();
    }

    private String finderFunc(String shor) throws InvalidOperandsException {
        List<String> commits = Utils.plainFilenamesIn(path + "//.gitlet//saved//commits");
        HashMap<String, String> compare = new HashMap<>();
        for (String name : commits) {
            String sho = name.substring(0, shor.length());
            compare.put(sho, name.substring(0, name.length() - 4));
        }
        if (compare.keySet().contains(shor)) {
            return compare.get(shor);
        }
        throw new InvalidOperandsException("");
    }
}
