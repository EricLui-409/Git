package gitlet;


import java.io.*;

import static gitlet.Utils.readContents;
import static gitlet.Utils.sha1;

/**
 * Created by EricLui on 15/7/2017.
 */

public class Blob implements Serializable {

    private String filename;
    private String hash;
    private byte[] content;
    private String path = System.getProperty("user.dir");

    //constructor for a blob object
    public Blob(File file) {
        filename = file.getName();
        content = readContents(file);
        hash = this.hasher();
    }

    private String hasher() {
        return sha1(content);
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getContent() {
        return content;
    }

    public String getHash() {
        return hash;
    }


    private byte[] serialize() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(this);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            throw new Error("Internal error serializing blob.");
        }
    }

    //serialize and save the object as a file in the .gitlet file
    public void writeFile() throws IOException {
        Utils.writeHelper(this, path + "//.gitlet//saved//blobs//" + hash + ".txt");
    }

    //convert hash to Blob object from the blobs file
    static Blob getBlob(String iD) throws IOException, ClassNotFoundException {
        String path = System.getProperty("user.dir");
        return (Blob) Utils.readHelper(path + "//.gitlet//saved//blobs//" + iD + ".txt");
    }
}
