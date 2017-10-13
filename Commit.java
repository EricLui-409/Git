package gitlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.sha1;

public class Commit implements Serializable {

    private String timeStamp;
    private String logMessage;
    private HashMap<String, String> filenames;
    private String parent;
    private String path = System.getProperty("user.dir");
    private String hash;

    //constructor
    public Commit(String message, String previous, HashMap<String, String> content) {
        logMessage = message;
        parent = previous;
        timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        filenames = content;
        hash = this.hasher();
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public HashMap<String, String> getFilenames() {
        if (filenames == null) {
            return new HashMap<>();
        }
        return filenames;
    }

    public String getParent() {
        return parent;
    }

    public String getHash() {
        return hash;
    }

    public String hasher() {
        byte[] content = this.serialize();
        return sha1(content);
    }

    public byte[] serialize() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(this);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            throw new Error("Internal error serializing commit.");
        }
    }

    public Commit getPrev() throws IOException, ClassNotFoundException {
        if (parent == null) {
            return null;
        }
        Commit result;
        result = (Commit) Utils.readHelper(path + "//.gitlet//saved//commits//" + parent + ".txt");
        return result;
    }

    //serialize and save the object as a file in the .gitlet file
    public void writeFile() throws IOException {
        Utils.writeHelper(this, path + "//.gitlet//saved//commits//" + hash + ".txt");
    }

    //add Blob to the contents of a commit
    //map filename to hash
    //takes in the file name of the Blob object
    public void addBlob(Blob content) {
        filenames.put(content.getFilename(), content.getHash());
    }

    //add a serialized Blob to the contents of a commit
    public void addBlob(String blob) throws IOException, ClassNotFoundException {
        Blob b;
        File file = new File(path + "//.gitlet//saved//blobs//" + blob + ".txt");
        b = (Blob) Utils.readHelper(path + "//.gitlet//saved//blobs//" + blob + ".txt");
        addBlob(b);
    }

    //convert hash to Commit object
    public static Commit getCommit(String iD) throws IOException, ClassNotFoundException {
        String path1 = System.getProperty("user.dir");
        return (Commit) Utils.readHelper(path1 + "//.gitlet//saved//commits//" + iD + ".txt");
    }
}
