package gitlet;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasud on 7/16/2017.
 */
public class FuncInit {

    public FuncInit() {
        /** Creates new (temporary- not serialized) instance of FuncInit */
    }

    public void apply(String[] args) throws InvalidOperandsException, IOException {
        if (args.length != 1) {
            throw new InvalidOperandsException("Too many arguments to init");
        }
        String path = System.getProperty("user.dir");
        File file = new File(path);
        if ((new File(file, ".gitlet")).isDirectory()) {
            /** Prints and error and exits if .gitlet exists in pwd */
            System.out.println("A gitlet version-control system "
                    + "already exists in the current directory.");
        } else {
            /** Creates .gitlet (and .saved within gitlet) iff not initialized
             * Still need to make initial commit*/
            File gitlet1 = new File(path + "//.gitlet//saved//blobs");
            gitlet1.mkdirs();
            File commits = new File(path + "//.gitlet//saved//commits");
            commits.mkdirs();
            Gitlet gitletGit = new Gitlet();
        }
    }
}

