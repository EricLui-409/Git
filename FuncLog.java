package gitlet;

import java.io.IOException;
import java.io.Serializable;
/**
 * Created by EricLui on 15/7/2017.
 */

public class FuncLog implements Serializable {

    private Commit head;

    public FuncLog(String[] messages) {
        if (messages.length > 1) {
            throw new RuntimeException("No operands needed");
        }
    }

    public void apply() throws IOException, ClassNotFoundException {
        head = Gitlet.getCurrCommit();
        Commit curr = head;
        while (curr != null) {
            printSingle(curr);
            curr = curr.getPrev();
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
