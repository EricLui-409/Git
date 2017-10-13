package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by vasud on 16/7/2017.
 */
public class FuncMerge {

    private String branchName;
    private Gitlet _gitlet;
    private final String path = System.getProperty("user.dir");
    private String head;
    private String _head;
    private String _split;
    private Commit branchHead1;

    public FuncMerge(String[] branch) {
        if (branch.length > 2) {
            throw new InvalidOperandsException("Only one branch name is allowed");
        }
        branchName = branch[1];
        _gitlet = Gitlet.getGitlet();
        head = _gitlet.getHead();
        _head = _gitlet.getBranches().get(head);

    }

    public String findSplit() throws IOException, ClassNotFoundException {
        /** Finds the commit ID of the split point */
        List<String> currBranch = new ArrayList<String>();
        List<String> givenBranch = new ArrayList<String>();
        Commit curr1 = Commit.getCommit(_gitlet.getBranches().get(head));
        Commit given1 = Commit.getCommit(_gitlet.getBranches().get(branchName));
        while (given1.getParent() != null || curr1.getParent() != null) {
            currBranch.add(curr1.getHash());
            givenBranch.add(given1.getHash());
            for (String testCurr1 : currBranch) {
                for (String testGiven1 : givenBranch) {
                    if (testCurr1.equals(testGiven1)) {
                        return testCurr1;
                    }
                }
            }
            if (given1.getParent() != null) {
                given1 = Commit.getCommit(given1.getParent());
            }
            if (curr1.getParent() != null) {
                curr1 = Commit.getCommit(curr1.getParent());
            }
        }
        return null;
    }

    public HashMap<String, String> modifiedBranch(String base1, String compare1)
            throws IOException, ClassNotFoundException {
        /** Returns a Hashmap containing the name and hash of all files in the compare1
         *  commit that have been modified (changed or removed), relative to the base1 commit
          */
        HashMap<String, String> changed1 = new HashMap<>();
        Commit base = Commit.getCommit(base1);
        Commit compare = Commit.getCommit(compare1);
        HashMap<String, String> compareHash1 = compare.getFilenames();
        for (String key1 : base.getFilenames().keySet()) {
            //Checks for modified files in compare1
            if (compareHash1.keySet().contains(key1)) {
                if (!compareHash1.get(key1).equals(base.getFilenames().get(key1))) {
                    //Adds to changed1 if hash values are not equal
                    changed1.put(key1, compareHash1.get(key1));
                }
            } else {
                changed1.put(key1, null);
            }
        }
        return changed1;
    }

    public HashMap<String, String> pureModifiedBranch(String base1, String compare1)
            throws IOException, ClassNotFoundException {
        /** Returns a Hashmap containing the name and hash of all files in the compare1
         *  commit that have been modified (changed), relative to the base1 commit
         */
        HashMap<String, String> changed1 = new HashMap<>();
        Commit base = Commit.getCommit(base1);
        Commit compare = Commit.getCommit(compare1);
        HashMap<String, String> compareHash1 = compare.getFilenames();
        for (String key1 : base.getFilenames().keySet()) {
            //Checks for modified files in compare1
            if (compareHash1.keySet().contains(key1)) {
                if (!compareHash1.get(key1).equals(base.getFilenames().get(key1))) {
                    //Adds to changed1 if hash values are not equal
                    changed1.put(key1, compareHash1.get(key1));
                }
            }
        }
        return changed1;
    }

    public HashMap<String, String> unmodifiedBranch(String base1, String compare1)
            throws IOException, ClassNotFoundException {
        /** Returns a Hashmap containing the name and hash of all files in the compare1
         *  commit that have been unmodified, relative to the base1 commit
         */
        HashMap<String, String> changed1 = new HashMap<>();
        Commit base = Commit.getCommit(base1);
        Commit compare = Commit.getCommit(compare1);
        HashMap<String, String> compareHash1 = compare.getFilenames();
        for (String key1 : base.getFilenames().keySet()) {
            //Checks for modified files in compare1
            if (compareHash1.keySet().contains(key1)) {
                if (compareHash1.get(key1).equals(base.getFilenames().get(key1))) {
                    //Adds to changed1 if hash values are not equal
                    changed1.put(key1, compareHash1.get(key1));
                }
            }
        }
        return changed1;
    }

    public HashMap<String, String> addedBranch(String base1, String compare1)
            throws IOException, ClassNotFoundException {
        /** Return a Hashmap containing the name and hash values of all files that
         * have been added to the compare commit, relative to the base
         */
        HashMap<String, String> added1 = new HashMap<>();
        Commit base = Commit.getCommit(base1);
        Commit compare = Commit.getCommit(compare1);
        HashMap<String, String> compareHash1 = compare.getFilenames();
        for (String key1 : compareHash1.keySet()) {
            if (!base.getFilenames().keySet().contains(key1)) {
                added1.put(key1, compareHash1.get(key1));
            }
        }
        return added1;
    }

    public HashMap<String, String> removedBranch(String base1, String compare1)
            throws IOException, ClassNotFoundException {
        /** Return a Hashmap containing the name and hash values of all files that
         * have been removed from the compare commit, relative to the base
         */
        return addedBranch(compare1, base1);
    }

    public HashMap<String, String> intersection1(HashMap<String, String> a,
                                                 HashMap<String, String> b) {
        /** Returns a Hashmap of the keys that overlap between 2 hashmaps,
         * and retaining the value of a
         * */
        HashMap<String, String> ret = new HashMap<>();
        for (String aKey1 : a.keySet()) {
            if (b.keySet().contains(aKey1)) {
                ret.put(aKey1, a.get(aKey1));
            }
        }
        return ret;
    }

    public HashMap<String, String> notIntersection1(HashMap<String, String> a,
                                                    HashMap<String, String> b) {
        /** Returns a Hashmap of the keys that do not overlap between 2 hashmaps,
         * retaining only the the values and keys of a
         * */
        HashMap<String, String> ret = new HashMap<>();
        for (String aKey1 : a.keySet()) {
            if (!b.keySet().contains(aKey1)) {
                ret.put(aKey1, a.get(aKey1));
            }
        }
        return ret;
    }

    public void test1(HashMap<String, String> branchOnlyModified)
            throws IOException, ClassNotFoundException {
        /** Test for if branch is modified but head is not */
        File user1 = null;
        Blob blob1 = null;
        Set<String> keySet1 = branchOnlyModified.keySet();
        for (String branchKey1: keySet1) {
            user1 = new File(path + "//" + branchKey1 + ".txt");
            user1.delete();
            blob1 = Blob.getBlob(branchOnlyModified.get(branchKey1));
            Utils.writeContents(user1, blob1.getContent());
            _gitlet.addStaged(branchKey1, branchOnlyModified.get(branchKey1));
        }
    }

    public void test4(HashMap<String, String> branchOnlyAdded)
            throws IOException, ClassNotFoundException {
        File user1 = null;
        Blob blob1 = null;
        Set<String> keySet1 = branchOnlyAdded.keySet();
        for (String branchKey1: keySet1) {
            user1 = new File(path + "//" + branchKey1 + ".txt");
            blob1 = Blob.getBlob(branchOnlyAdded.get(branchKey1));
            Utils.writeContents(user1, blob1.getContent());
            _gitlet.addStaged(branchKey1, branchOnlyAdded.get(branchKey1));
        }
    }

    public void test5(HashMap<String, String> headUnmodified2)
            throws IOException, ClassNotFoundException {
        /** Test for if files present at split and unmodified in current
         * BUT absent in given branch
         */
        File user1;
        Set<String> keySet1 = headUnmodified2.keySet();
        for (String branchKey1: keySet1) {
            user1 = new File(path + "//" + branchKey1 + ".txt");
            user1.delete();
            _gitlet.addRemoved(branchKey1, headUnmodified2.get(branchKey1));
        }
    }

    public void test7(HashMap<String, String> bothModified1,
                      HashMap<String, String> headModifiedRm,
                      HashMap<String, String> branchModifiedRm)
            throws IOException, ClassNotFoundException {
        /** Tests for files with the same key that have both been modified
         * in identical or non-identical ways
         */
        String initial = "<<<<<<< HEAD";
        String middle = "=======";
        String end = ">>>>>>>";
        String lineSeparator1 = System.getProperty("line.separator");
        String currFile1, givenFile1, final1, separator1, separator2;
        Blob currFile, givenFile;
        File f1;
        for (String key1 : bothModified1.keySet()) {
            //Write all new conflicted files here
            currFile1 = "";
            givenFile1 = "";
            if (branchModifiedRm.get(key1) != null) {
                givenFile = Blob.getBlob(branchModifiedRm.get(key1));
                givenFile1 = new String(givenFile.getContent(), "UTF-8");
            }
            if (headModifiedRm.get(key1) != null) {
                currFile = Blob.getBlob(headModifiedRm.get(key1));
                currFile1 = new String(currFile.getContent(), "UTF-8");
            }
            if (currFile1.length() == 0) {
                separator1 = "";
            } else {
                separator1 = lineSeparator1;
            }
            if (givenFile1.length() == 0) {
                separator2 = "";
            } else {
                separator2 = lineSeparator1;
            }
            f1 = new File(path + "//" + key1 + ".txt");
            final1 = initial + lineSeparator1 + currFile1
                    + middle + lineSeparator1 + givenFile1
                    + end + lineSeparator1;
            Utils.writeContents(f1, final1.getBytes());
        }
        System.out.println("Encountered a merge conflict.");
        _gitlet.writeFile();
        return;
    }
    public boolean checkUntracked(List<String> userFiles1) {
        for (String file1 : userFiles1) {
            String toCheck = file1.substring(0, file1.length() - 4);
            if (!_gitlet.getTracked().containsKey(toCheck)) {
                //find any untracked files in pwd
                if (branchHead1.getFilenames().
                        containsKey(file1.substring(0, file1.length() - 4))) {
                    //test if sha is same
                    File file = new File(path + "//" + file1);
                    String sha = Utils.sha1(Utils.readContents(file));
                    if (!sha.equals(branchHead1.getFilenames().get(file1))) {
                        System.out.println("There is an untracked file in the way; "
                                + "delete it or add it first.");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void apply() throws IOException, ClassNotFoundException {
        if (!_gitlet.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (_gitlet.getHead().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        List<String> userFiles1 = Utils.plainFilenamesIn(path);
        branchHead1 = Commit.getCommit(_gitlet.getBranches().get(branchName));
        _split = findSplit();
        if (!_gitlet.getStaged().isEmpty() || !_gitlet.getRemoved().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (branchHead1.getHash().equals(_split)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (_split.equals(_head)) {
            _gitlet.setBranches(head, _gitlet.getBranches().get(branchName));
            _gitlet.writeFile();
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if (checkUntracked(userFiles1)) {
            return;
        }
        //Bulletpoint #1 & 2
        HashMap<String, String> headModified = pureModifiedBranch(_split, _head);
        HashMap<String, String> branchModified = pureModifiedBranch(_split, branchHead1.getHash());
        //Test #1
        HashMap<String, String> branchOnlyModified = notIntersection1(branchModified, headModified);
        if (!branchOnlyModified.isEmpty()) {
            /** Test for if branch is modified but head is not */
            test1(branchOnlyModified);
        }
        //Test #2 - Do nothing
        //Bulletpoint #3 &4
        HashMap<String, String> headAdded = addedBranch(_split, _head);
        HashMap<String, String> branchAdded = addedBranch(_split, branchHead1.getHash());
        //Test #3 - Do nothing
        //Test #4
        HashMap<String, String> branchOnlyAdded = notIntersection1(branchAdded, headAdded);
        if (!branchOnlyAdded.isEmpty()) {
            /** Test for if any new blobs have been added to the given heed */
            test4(branchOnlyAdded);
        }
        //Bulletpoint #5 and 6
        HashMap<String, String> branchRemoved = removedBranch(_split, branchHead1.getHash());
        HashMap<String, String> headUnmodified1 = unmodifiedBranch(_split, _head);
        //Test #5
        HashMap<String, String> headUnmodified2 = intersection1(headUnmodified1, branchRemoved);
        if (!headUnmodified2.isEmpty()) {
            /** Test for if files present at split and unmodified in current
             * BUT absent in given branch
             */
            test5(headUnmodified2);
        }
        //Test #6 - Do nothing
        //Test #7- Overlap
        //Does not work with files that were modified in identical ways
        HashMap<String, String> headModifiedRm = modifiedBranch(_split, _head);
        HashMap<String, String> branchModifiedRm = modifiedBranch(_split, branchHead1.getHash());
        HashMap<String, String> bothModified1 = intersection1(headModifiedRm, branchModifiedRm);
        if (!bothModified1.isEmpty()) {
            /** Tests for files with the same key that have both been modified
             * in identical or non-identical ways
             */
            test7(bothModified1, headModifiedRm, branchModifiedRm);
        } else {
            String[] commit1 = {"commit", "Merged " + head + " with " + branchName + "."};
            _gitlet.writeFile();
            new FuncCommit(commit1).apply();
        }
    }
}
