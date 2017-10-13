package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;

/**
 * Created by EricLui on 16/7/2017.
 */
public class FuncCheckout {

    private int mode;
    private String fileName;
    private String branchName;
    private Commit requiredCommit;
    private String commitID;
    private String operand;
    private String path = System.getProperty("user.dir");
    private Gitlet gitlet;

    public FuncCheckout(String[] inputs) throws IOException, ClassNotFoundException {
        if (inputs.length == 2) {
            branchName = inputs[1];
            mode = 3;
        } else if (inputs.length == 3) {
            fileName = inputs[2];
            operand = inputs[1];
            mode = 1;
        } else if (inputs.length == 4) {
            fileName = inputs[3];
            commitID = inputs[1];
            operand = inputs[2];
            mode = 2;
        } else {
            throw new InvalidOperandsException("Invalid number of arguments passed in.");
        }
    }

    public void apply() throws IOException, ClassNotFoundException {
        gitlet = Gitlet.getGitlet();
        if (mode == 1 || mode == 2) {
            if (!operand.equals("--")) {
                System.out.println("Incorrect operands.");
                return;
            }
            if (mode == 1) {
                requiredCommit = Gitlet.getCurrCommit();
            } else {
                try {
                    commitID = finderFunc(commitID);
                } catch (InvalidOperandsException e) {
                    System.out.println("No commit with that id exists.");
                    return;
                }
                List<String> commits = Utils.plainFilenamesIn(path + "//.gitlet//saved//commits");
                requiredCommit = Commit.getCommit(commitID);
            }
            HashMap<String, String> content = requiredCommit.getFilenames();
            if (content.keySet().contains(fileName.substring(0, fileName.length() - 4))) {
                Blob curr = Blob.getBlob(content.get(fileName.substring(0, fileName.length() - 4)));
                File file = new File(path + "//" + curr.getFilename());
                File workingDIr = new File(path);
                if (Utils.plainFilenamesIn(workingDIr).contains(curr.getFilename() + ".txt")) {
                    file.delete();
                }
                Utils.writeContents(file, curr.getContent());
                gitlet.unstage(curr.getFilename());
                gitlet.writeFile();
            } else {
                System.out.println("File does not exist in that commit.");
            }
        } else {
            if (!gitlet.getBranches().keySet().contains(branchName)) {
                System.out.println("No such branch exists.");
            } else if (branchName.equals(gitlet.getHead())) {
                System.out.println("No need to checkout the current branch.");
            } else {
                requiredCommit = gitlet.getBranchHead(branchName);
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
                gitlet.setHead(branchName);
                gitlet.writeFile();
            }
        }
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
