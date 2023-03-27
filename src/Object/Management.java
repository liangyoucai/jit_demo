package Object;

import Util.FileOperationUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Management implements Serializable {
    //protected static final String path = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "singers" + File.separator + ".git";
    //protected static final String path = System.getProperty("user.dir") + File.separator + ".git";
    private final String ORIGIN_SOCKET = "127.0.0.1 8888";//本地服务器：IP 端口号
    private Map<String, String> depositories = new TreeMap<String, String>();//<name> <url>

    public Map<String, String> getDepositories() {
        return depositories;
    }
    /**
     * Constructor
     */
    public Management(){
        depositories.put("origin", ORIGIN_SOCKET);
    }

    /**
     * add depository
     * @param name
     * @param url
     */
    public void addDep(String name, String url){
        depositories.put(name, url);
        System.out.println("Add successfully!Current remote depository:\n" + DepToString());
    }

    /**
     * delete depository
     * @param name
     */
    public void rmDep(String name){
        depositories.remove(name);
    }

    /**
     * rename the oldname depository
     * @param oldname
     * @param newname
     */
    public void renameDep(String oldname, String newname){
        String value = depositories.get(oldname);
        depositories.remove(oldname);
        depositories.put(newname, value);
    }

    public int getPort(String key){
        String[] url = depositories.get(key).split(" ");
        return Integer.valueOf(url[1]);
    }

    public String getIP(String key){
        String[] url = depositories.get(key).split(" ");
        return url[0];
    }

    public void setPort(String key, int port){
        String[] url = depositories.get(key).split(" ");
        url[1] = String.valueOf(port);
        depositories.replace(key, url[0] + " " + url[1]);
    }

    /**
     *
     * transfer the depositories to String
     * @return String
     */
    public String DepToString() {
        String content = "";
        for (Map.Entry<String, String> entry: depositories.entrySet()) {
            content += entry.getKey() + " " + entry.getValue() + "\n";
        }
        return content;
    }

    /**
     *Write entries into mgmt file
     */
    public void write(String path) {
        File mgmtfile = new File(path + File.separator + "mgmt");
        if (mgmtfile.exists()) {
            FileOperationUtil.writeObject(mgmtfile, this);
        } else {
            System.out.println("ERROR: Cannot find mgmt file!");
            System.exit(0);
        }

    }


}
