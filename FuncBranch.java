package gitlet;

import java.io.IOException;

/**
 * Created by EricLui on 16/7/2017.
 */
public class FuncBranch {

    private String name;
    private Gitlet gitlet;
    private String path = System.getProperty("user.dir");

    public FuncBranch(String[] branchName) throws InvalidOperandsException {
        if (branchName.length > 2) {
            throw new InvalidOperandsException("Only one input is allowed");
        }
        name = branchName[1];
    }

    public void apply() throws IOException, ClassNotFoundException {
        gitlet = Gitlet.getGitlet();
        if (gitlet.getBranches().containsKey(name)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        String currCommit = Gitlet.getCurrCommit().getHash();
        gitlet.setBranches(name, currCommit);
        gitlet.writeFile();
    }
}
