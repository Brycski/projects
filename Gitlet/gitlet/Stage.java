package gitlet;

import java.io.Serializable;
import java.util.ArrayList;


public class Stage implements Serializable {

    ArrayList<String> _toAdd;

    ArrayList<String> _toRm;

    public Stage() {
        _toAdd = new ArrayList<String>();
        _toRm = new ArrayList<String>();
    }

    public void clear() {
        _toAdd = new ArrayList<String>();
        _toRm = new ArrayList<String>();
    }
}
