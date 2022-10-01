package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Bryce.txt Szarzynski
 */
public class Repository implements Serializable {
    /** add instance variables here.
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     *
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    Stage _stage;

    Commit _HEAD;

    public Repository() {
        if (new File(".gitlet/stage.txt").exists()) {
            _stage = readObject(new File(".gitlet/stage.txt"), Stage.class);
        }
        if (new File(".gitlet/branches/HEAD.txt").exists()) {
            String headBranch = readContentsAsString(new File(".gitlet/branches/"
                    + "HEAD.txt"));
            _HEAD = readObject(new File(".gitlet/branches/"
                    + headBranch + ".txt"), Commit.class);
        }
    }

    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory.");
        } else {
            GITLET_DIR.mkdirs();
            _stage = new Stage();
            writeObject(new File(".gitlet/stage.txt"), _stage);
            File blobs = new File(".gitlet/blobs");
            blobs.mkdirs();
            File commitsDir = new File(".gitlet/commits");
            commitsDir.mkdirs();
            Commit initial = new Commit("initial comm"
                    + "it", new HashMap<>(), null);
            writeObject(new File(commitsDir + "/"
                    + sha1(serialize(initial)) + ".txt"), initial);
            File branches = new File(".gitlet/branches");
            branches.mkdirs();
            writeContents(new File(".gitlet/branches/HEAD.txt"), "main");
            writeObject(new File(".gitlet/branches/main.txt"), initial);
        }
    }

    public void add(String file) {
        if (!(new File(file).exists())) {
            System.out.println("File does not exist.");
        } else {
            if (_HEAD._blobs.containsKey(file + _HEAD._message)) {
                File oldFile = _HEAD._blobs.get(file + _HEAD._message);
                if (readContentsAsString(oldFile).equals(
                        readContentsAsString(new File(file)))) {
                    _stage._toRm.remove(file);
                    writeObject(new File(".gitlet/stage.txt"), _stage);
                    return;
                }
            }
            for (String addFile : _stage._toAdd) {
                if (file.equals(addFile)) {
                    if (readContentsAsString(new File(addFile)).equals(
                            readContentsAsString(new File(file)))) {
                        return;
                    } else {
                        _stage._toAdd.remove(addFile);
                    }
                }
            }
            if (_stage._toRm.contains(file)) {
                _stage._toRm.remove(file);
            } else {
                _stage._toAdd.add(file);
            }
            writeObject(new File(".gitlet/stage.txt"), _stage);
        }
    }

    public void rm(String file) {
        if (_stage._toAdd.contains(file)) {
            _stage._toAdd.remove(file);
            writeObject(new File(".gitlet/stage.txt"), _stage);
        } else if (_HEAD._blobs.containsKey(file + _HEAD._message) || (_HEAD._parent != null
                && _HEAD._parent._blobs.containsKey(file + _HEAD._parent._message))) {
            _stage._toRm.add(file);
            restrictedDelete(new File(file));
            writeObject(new File(".gitlet/stage.txt"), _stage);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    public void commit(String message) {
        if (_stage._toAdd.isEmpty() && _stage._toRm.isEmpty()) {
            System.out.println("No changes added to the commit.");
        }
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
        }
        HashMap<String, File> blobFiles = new HashMap<String, File>();
        for (String file : _stage._toAdd) {
            File blob = new File(".gitlet/blobs/"
                    + file + message + ".txt");
            String contents = readContentsAsString(new File(file));
            writeContents(blob, contents);
            blobFiles.put((file + message), blob);
        }
        Commit curr = new Commit(message, blobFiles, _HEAD);
        File commitsDir = new File(".gitlet/commits");
        File currDir = new File(commitsDir + "/"
                + sha1(serialize(curr)) + ".txt");
        writeContents(currDir, serialize(curr));
        String branch = readContentsAsString(new File(".gitlet/branches/HEAD.txt"));
        writeObject(new File(".gitlet/branches/" + branch + ".txt"),  curr);
        _stage.clear();
        writeObject(new File(".gitlet/stage.txt"), _stage);
    }

    /** checkout -- [file name] */
    public void checkout1(String file) {
        if (!(_HEAD._blobs.containsKey(new File(file) + _HEAD._message))) {
            System.out.println("File does not exist in that commit.");
        } else {
            String oldBlob = readContentsAsString(_HEAD._blobs.get(file
                    + _HEAD._message));
            writeContents(new File(file), oldBlob);
        }
    }

    /** checkout [commit id] -- [file name] */
    public void checkout2(String commitID, String file) {
        File commitsDir = new File(".gitlet/commits");
        if (commitID.length() == 8) {
            List<String> allCommits = new ArrayList<String>(plainFilenamesIn(
                    new File(".gitlet/commits")));
            for (String commit : allCommits) {
                if (commit.contains(commitID)) {
                    Commit oldCommit = readObject((new File(commitsDir + "/"
                            + commit)), Commit.class);
                    if (!(oldCommit._blobs.containsKey(file
                            + oldCommit._message))) {
                        System.out.print("File does not exist in that commit.");
                        return;
                    }
                    String oldBlob = readContentsAsString(oldCommit._blobs.get(file
                            + oldCommit._message));
                    writeContents(new File(file), oldBlob);
                    return;
                }
            }
        }
        if (!(new File(commitsDir + "/" + commitID + ".txt").exists())) {
            System.out.print("No commit with that id exists.");
        } else {
            Commit oldCommit = readObject((new File(commitsDir + "/"
                    + commitID + ".txt")), Commit.class);
            if (!(oldCommit._blobs.containsKey(file
                    + oldCommit._message))) {
                System.out.print("File does not exist in that commit.");
                return;
            }
            String oldBlob = readContentsAsString(oldCommit._blobs.get(file
                    + oldCommit._message));
            writeContents(new File(file), oldBlob);
        }
    }

    /** checkout [branch] */
    public void checkout3(String branchName) {
        List<String> branches = new ArrayList<String>(plainFilenamesIn(
                new File(".gitlet/branches")));
        if (!(branches.contains(branchName + ".txt"))) {
            System.out.print("No such branch exists.");
            return;
        }
        if (readContentsAsString(new File(".gitlet/branch"
                + "es/HEAD.txt")).equals(branchName)) {
            System.out.print("No need to checkout the current branch.");
            return;
        }
        Commit newHEAD = _HEAD;
        for (String branch : branches) {
            if (branch.equals(branchName + ".txt")) {
                newHEAD = readObject(new File(".gitlet/branches/" + branch),
                        Commit.class);
                break;
            }
        }
        Set<String> newBlobs = new LinkedHashSet<String>(newHEAD._blobs.keySet());
        Set<String> oldBlobs = new LinkedHashSet<String>(_HEAD._blobs.keySet());
        for (String file : oldBlobs) {
            if (new File(file.replace(_HEAD._message, "")).exists()) {
                if ((new File(".gitlet/blobs/"
                        + file.replace(_HEAD._message, newHEAD._message) + ".txt")).exists()) {
                    writeContents(new File(file.replace(_HEAD._message, "")),
                            readContentsAsString(newHEAD._blobs.get(file.replace(
                                    _HEAD._message, newHEAD._message))));
                    newBlobs.remove(file.replace(_HEAD._message, newHEAD._message));
                } else {
                    restrictedDelete(new File(file.replace(_HEAD._message, "")));
                    newBlobs.remove(file.replace(_HEAD._message, newHEAD._message));
                }
            } else {
                writeContents(new File(file.replace(_HEAD._message, "")),
                        readContentsAsString(newHEAD._blobs.get(file.replace(
                                _HEAD._message, newHEAD._message))));
                newBlobs.remove(file.replace(_HEAD._message, newHEAD._message));
            }
        }
        for (String file : newBlobs) {
            if (new File(file.replace(newHEAD._message, "")).exists()) {
                String curr = readContentsAsString(new File(file.replace(newHEAD._message, "")));
                String old = readContentsAsString(new File(".gitlet/blobs/"
                        + file + ".txt"));
                if (!(curr.equals(old))) {
                    System.out.print("There is an untracked file in the way; de"
                            + "lete it, or add and commit it first.");
                    return;
                } else {
                    continue;
                }
            }
            writeContents(new File(file.replace(newHEAD._message, "")),
                    readContentsAsString(newHEAD._blobs.get(file)));
        }
        writeContents(new File(".gitlet/branches/HEAD.txt"), branchName);
        _stage.clear();
        writeObject(new File(".gitlet/stage.txt"), _stage);
    }

    /** under the third line in the while loop add in
     * if (merge commits) {
     *    System.out.println("Merge: " + __ + " " + __);
     */
    public void log() {
        Commit curr = _HEAD;
        while (curr != null) {
            System.out.println("===");
            System.out.println("commit " + sha1(serialize(curr)));
            System.out.println(curr._timestamp);
            System.out.println(curr._message);
            System.out.println();
            curr = curr._parent;
        }
    }

    public void global() {
        List<String> allCommits = new ArrayList<String>(plainFilenamesIn(
                new File(".gitlet/commits")));
        for (String commit : allCommits) {
            Commit curr = readObject(new File(".gitlet/commits/"
                    + commit), Commit.class);
            System.out.println("===");
            System.out.println("commit " + sha1(serialize(curr)));
            System.out.println(curr._timestamp);
            System.out.println(curr._message);
            System.out.println();
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        String headBranch = readContentsAsString(new File(".git"
                + "let/branches/HEAD.txt"));
        System.out.println("*" + headBranch);
        List<String> branches = new ArrayList<String>(plainFilenamesIn(
                new File(".gitlet/branches")));
        branches.remove("HEAD.txt");
        branches.remove(headBranch + ".txt");
        if (!(branches.isEmpty())) {
            for (String branch : branches) {
                System.out.println(branch.replace(".txt", ""));
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        if (!(_stage._toAdd.isEmpty())) {
            Collections.sort(_stage._toAdd);
            for (String name : _stage._toAdd) {
                System.out.println(name);
            }
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        if (!(_stage._toRm.isEmpty())) {
            Collections.sort(_stage._toRm);
            for (String name : _stage._toRm) {
                System.out.println(name);
            }
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public void find(String msg) {
        List<String> commits = plainFilenamesIn(new File(".gitlet/commits"));
        boolean flag = false;
        for (String id : commits) {
            Commit curr = readObject(new File(".gitlet/commits/"
                    + id), Commit.class);
            if (curr._message.equals(msg)) {
                System.out.println(sha1(serialize(curr)));
                flag = true;
            }
        }
        if (!flag) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void branch(String name) {
        if (new File(".gitlet/branches/" + name + ".txt").exists()) {
            System.out.print("A branch with that name already exists.");
            return;
        }
        writeObject(new File(".gitlet/branches/" + name + ".txt"), _HEAD);
    }

    public void rmBranch(String name) {
        if (!(new File(".gitlet/branches/" + name + ".txt").exists())) {
            System.out.print("A branch with that name does not exist.");
            return;
        }
        String headBranch = readContentsAsString(new File(".gitlet/branch"
                + "es/HEAD.txt"));
        if (headBranch.equals(name)) {
            System.out.print("Cannot remove the current branch.");
            return;
        }
        (new File(".gitlet/branches/" + name + ".txt")).delete();
    }

    public void reset(String commitID) {
        if (!(new File(".gitlet/commits/" + commitID + ".txt").exists())) {
            System.out.print("No commit with that id exists.");
            return;
        }
        Commit curr = readObject(new File(".gitlet/commits/" + commitID
                + ".txt"), Commit.class);
        Set<String> blobs = new LinkedHashSet<String>(curr._blobs.keySet());
        Set<String> oldBlobs = new LinkedHashSet<String>(_HEAD._blobs.keySet());
        for (String blob: oldBlobs) {
            if (curr._blobs.containsKey(blob.replace(_HEAD._message, curr._message))) {
                String newContents = readContentsAsString(curr._blobs.get(blob.replace(
                        _HEAD._message, curr._message)));
                writeContents(new File(blob.replace(_HEAD._message, "")),
                        newContents);
                blobs.remove(blob.replace(_HEAD._message, curr._message));
            } else {
                restrictedDelete(new File(blob.replace(_HEAD._message, "")));
            }
        }
        for (String blob : blobs) {
            if (new File(blob.replace(curr._message, "")).exists()
                    && (!((readContentsAsString(new File(blob.replace(
                            curr._message, "")))
                            .equals(readContentsAsString(curr._blobs.get(blob))))))
                    && (!(oldBlobs.contains(blob.replace(curr._message, _HEAD._message))))) {
                System.out.print("There is an untracked file in the way; delete it, or "
                        + "add and commit it first.");
                return;
            } else {
                writeContents(new File(blob.replace(curr._message, "")),
                        readContentsAsString(curr._blobs.get(blob)));
            }
        }
        String branch = readContentsAsString(new File(".gitlet/branches/HEAD.txt"));
        writeObject(new File(".gitlet/branches/" + branch + ".txt"), curr);
        _stage.clear();
        writeObject(new File(".gitlet/stage.txt"), _stage);
    }
}
