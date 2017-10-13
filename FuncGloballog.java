package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by EricLui on 15/7/2017.
 */

public class FuncGloballog implements Serializable {

    private String path = System.getProperty("user.dir");
    private List<String> fileNameList = Utils.plainFilenamesIn(path + "//.gitlet//saved//commits");

    public FuncGloballog(String[] messages) {
        if (messages.length > 1) {
            throw new RuntimeException("No operands needed");
        }
    }

    public void apply() throws IOException, ClassNotFoundException {
        for (String name : fileNameList) {
            name = name.substring(0, name.length() - 4);
            Commit curr = Commit.getCommit(name);
            printSingle(curr);
        }
    }

    private void printSingle(Commit target) {
        System.out.println("===");
        System.out.println("Commit " + target.getHash());
        System.out.println(target.getTimeStamp());
        System.out.println(target.getLogMessage());
        System.out.println();
    }
}
