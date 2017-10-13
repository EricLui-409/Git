package gitlet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by vasud on 7/15/2017.
 */
public class FuncAdd {

    private String filename;
    private String user = System.getProperty("user.dir");
    private Gitlet gitlet;
    private Blob blob;
    private File file;


    public FuncAdd(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 2) {
            throw new InvalidOperandsException("Add expect 2 operands");
        }
        this.filename = args[1].substring(0, args[1].length() - 4);
        this.gitlet = Gitlet.getGitlet();
    }

    public void apply() throws IOException, ClassNotFoundException {
        file = new File(user + "//" + filename + ".txt");
        if (gitlet.getRemoved().containsKey(filename)) {
            String removedHash = gitlet.getRemoved().get(filename);
            Blob removedBlob = Blob.getBlob(removedHash);
            Utils.writeContents(file, removedBlob.getContent());
            gitlet.removeRemoved(filename);
        }
        List<String> userFiles = Utils.plainFilenamesIn(user);
        if (!userFiles.contains(filename + ".txt")) {
            System.out.println("File does not exist.");
            return;
        }
        blob = new Blob(file);
        HashMap<String, String> track = gitlet.getTracked();
        if (!blob.getHash().equals(track.get(filename))) {
            gitlet.addStaged(filename, blob.getHash());
            blob.writeFile();
        }
        gitlet.writeFile();
    }
}
