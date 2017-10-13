package gitlet;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasud on 7/17/2017.
 */
public class FuncRm {

    private String userPath = System.getProperty("user.dir");
    private String[] args;
    private Gitlet gitlet;
    String _filename;
    String oldHash;

    public FuncRm(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 2) {
            throw new InvalidOperandsException("Rm accepts only 2 args");
        }
        this.args = args;
        this._filename = args[1].substring(0, args[1].length() - 4);
        this.gitlet = Gitlet.getGitlet();
    }

    public boolean checkstage(String file) {
        /** Checks if file is in the staging area. If so removes it.
         */
        if (!gitlet.getStaged().keySet().contains(file)) {
            return false;
        }
        return true;
    }

    public boolean checktracking(String file) {
        /** Check if file is tracked by the current commit.
         * If so, untracks it and deletes it in working directory.
         */
        if (!gitlet.getTracked().keySet().contains(file)) {
            return false;
        }
        return true;
    }


    public void apply() throws IOException {
        if (checktracking(_filename)) {
            File toDelete = new File(userPath + "//" + _filename + ".txt");
            toDelete.delete();
            gitlet.addRemoved(_filename, gitlet.getTracked().get(_filename));
            if (checkstage(_filename)) {
                gitlet.removeStaged(_filename);
            }
        } else if (checkstage(_filename)) {
            gitlet.removeStaged(_filename);
        } else {
            System.out.println("No reason to remove the file.");
        }
        gitlet.writeFile();
    }
}
