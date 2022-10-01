package gitlet;

import java.io.File;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/** Represents a gitlet commit object.
 *  It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Bryce.txt Szarzynski
 */
public class Commit implements Serializable {
    /**
     * add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     *
     *
     * The message of this Commit.
     */
    String _message;

    String _timestamp;

    HashMap<String, File> _blobs;

    Commit _parent;

    private transient DateTimeFormatter gitFormat =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.ofPattern
                    ("EEE MMM d HH:mm:ss uuuu Z");

    public Commit(String msg, HashMap<String, File> blobs, Commit parent) {
        _message = msg;
        _parent = parent;
        _blobs = blobs;
        if (parent == null) {
            _timestamp = "Date: " + OffsetDateTime.of(1970, 1, 1,
                    0, 0, 0, 0,
                    ZoneOffset.UTC).format(gitFormat);
        } else {
            _timestamp = "Date: " + OffsetDateTime.now().format(gitFormat);
        }
    }
}
