package Object;

import Util.FileOperationUtil;
import java.io.*;
import java.util.*;

public class Index implements Serializable{
    private static final long serialVersionID = 234252411453L;

    private Map<String, String> info = new TreeMap<String, String>();//<relative path, hash>
    //protected static final String path = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "singers" + File.separator + ".git";
    protected static final String path = System.getProperty("user.dir") + File.separator + ".git";
    public Map<String, String> getInfo() {
        return info;
    }
    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    /**
     * Constructor
     */
    public Index(){}

    /**
     *Remove key from info
     * @param key
     */
    public void remove(String key) {
        if (!info.containsKey(key))
            return;
        info.remove(key);
    }

    /**
     *Add a new entry in the index object
     *@param : file
     *@param : content
     */
    public int add(File file, String content) {
        ////1. 文件在工作区不存在：blob不操作 && index如果有key则删除
        //if (!file.exists() && info.containsKey(file.getName())) {
        //    remove(file.getName());
        //    return 0;
        //}
        //2. 文件存在于工作区：创建blob
        Blob blob = new Blob(content);
        blob.write();

        // 2.1 检查info是否有key == file.getName()
        if (info.containsKey(file.getName())){
            //2.1.1 检查value是否相同
            if (info.get(file.getName()).equals(blob.getKey())){
                //相同则说明文件未发生变化，什么也不做，给用户提示
                return 0;
            }
            else {
                //如果不同，则修改对应key的value
                info.replace(file.getName(), blob.getKey());
                return 1;
            }
        }else{//新增key
            info.put(file.getName(), blob.getKey());
            return 1;
        }

    }

    /**
     * Check whether the info is consistent with the workspace
     */
    public void checkLegal() {
        String file_path = "D:" + File.separator + "jit_demo" +
                File.separator + "src" + File.separator + "singers";
        for (Map.Entry<String, String> entry : info.entrySet())
        {
           File f = new File(file_path + File.separator + entry.getKey());
           if (!f.exists())
               remove(f.getName());//删除index记录
        }
    }

    /**
     *Write entries into index file
     */
    public void write() {
        File indexfile = new File(path + File.separator + "index");
        if (indexfile.exists()) {
            FileOperationUtil.writeObject(indexfile, this);
        }else {
            System.out.println("Cannot find index file!");
        }
    }

    public String toString() {
        String content = "";
        for (Map.Entry<String, String> entry: info.entrySet()) {
            content += entry.getKey() + " " + entry.getValue() + "\n";
        }
        return content;
    }

}
