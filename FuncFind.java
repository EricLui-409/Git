package gitlet;

import java.io.IOException;
import java.util.List;

public class FuncFind {

    private String message;
    private String path = System.getProperty("user.dir");
    private List<String> fileNameList = Utils.plainFilenamesIn(path + "//.gitlet//saved//commits");

    public FuncFind(String[] messages) {
        if (messages.length > 2) {
            throw new RuntimeException("Only one message is allowed");
        }
        message = messages[1];
    }

    public void apply() throws IOException, ClassNotFoundException {
        int count = 0;
        for (String name : fileNameList) {
            name = name.substring(0, name.length() - 4);
            Commit curr = Commit.getCommit(name);
            if (curr.getLogMessage().equals(message)) {
                count++;
                System.out.println(curr.getHash());
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
        }
    }
}
