package gitlet;

import java.io.IOException;

/**
 * Created by EricLui on 16/7/2017.
 */
public class FuncRmbranch {

    private String name;
    private Gitlet gitlet;
    private String path = System.getProperty("user.dir");

    public FuncRmbranch(String[] branchName) throws InvalidOperandsException {
        if (branchName.length > 2) {
            throw new InvalidOperandsException("Only one input is allowed");
        }
        name = branchName[1];
    }

    public void apply() throws IOException, ClassNotFoundException {
        gitlet = Gitlet.getGitlet();
        if (gitlet.getHead().equals(name)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        if (!gitlet.getBranches().containsKey(name)) {
            System.out.println("A branch with that name does not exist.");
        }
        gitlet.removeBranch(name);
        gitlet.writeFile();
    }
}
