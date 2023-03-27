package Object;

import Util.FileOperationUtil;

import java.io.File;
import java.io.Serializable;

/**
 * GitObject: Blob, Commit, Tree
 */
public class GitObject implements Serializable {
    protected String fmt;//the format of the file(Blob/Commit/Tree)
    protected String key;//the file name(the hash of the value)
    protected static final String path = System.getProperty("user.dir") + File.separator + ".git" + File.separator + "objects";
    //protected static String path = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "singers" + File.separator +
    //        ".git" + File.separator + "objects";//absolute path of objects
    protected byte[] content;//the content of file

    public byte[] getContent() {
        return content;
    }

    public String getKey() {
        return key;
    }

    /**
     * Write GitObject to file
     */
    public void write() {
        File file = new File(path + File.separator + key);
        if (!file.exists()) {//如果blob文件不存在，创建文件
            FileOperationUtil.createFile(path, key, this);
        }
        FileOperationUtil.writeObject(file, this);
    }
}
