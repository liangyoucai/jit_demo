package Object;

import Util.SHAUtil;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tree extends GitObject{
    private static final long serialVersionID = 13426785687453L;

    private Map<String, String> entries = new TreeMap<String, String>();//<file name, hash>

    public Map<String, String> getEntries() {
        return entries;
    }

    /**
     * Constructor
     * @param index
     */
    public Tree(Index index) {
        fmt = "tree";
        entries = index.getInfo();
        content = toString().getBytes(StandardCharsets.UTF_8);
        key = SHAUtil.sha1(entriesToString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * transfer entries toString.
     * @return String
     */
    private String entriesToString() {
        String content = "";
        for (Map.Entry<String, String> entry: entries.entrySet()) {
            content += entry.getKey() + " " + entry.getValue() + "\n";
        }
        return content;
    }

    @Override
    public String toString(){
        return "tree " + entriesToString().length() + "\r" + entriesToString();
    }
}
