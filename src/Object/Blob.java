package Object;

import Util.SHAUtil;
import java.nio.charset.StandardCharsets;

public class Blob extends GitObject {
    private static final long serialVersionID = 134252411453L;
    private long length;

    /**
     * Constructor: create a blob and file according to a new file
     * @param fcontent
     */
    public Blob(String fcontent) {
        String blob_ID = SHAUtil.sha1(fcontent.getBytes(StandardCharsets.UTF_8));
        this.fmt = "blob";
        this.content = fcontent.getBytes(StandardCharsets.UTF_8);
        this.length = content.length;
        this.key = blob_ID;
    }

    /**
     * Reorder the content of the blob file, eg."blob 4 xyzw"
     * @return String
     */
    @Override
    public String toString() {
        return (fmt + " " + length + " " + new String(content));
    }
}
