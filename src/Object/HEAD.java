package Object;
import java.io.*;

public class HEAD {
    private static String pre_commitID;
    //protected static String path = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "singers" + File.separator  + ".git";
    protected static String path = System.getProperty("user.dir") + File.separator + ".git";
    public String getPre_commitID() {
        return pre_commitID;
    }

    /**
     * Constructor
     */
    public HEAD() {
        try {
            //检查HEAD是否已经存在，有则读取HEAD，没有则在'.git'文件夹下创建HEAD文件，内容为空
            File file = new File(path + File.separator + "HEAD");
            if (file.exists())
                read();
            else {
                //创建空HEAD文件
                FileWriter fw = new FileWriter(file);
                fw.write("");
                fw.close();
            }
        } catch (IOException e){
            System.out.println("ERROR: Fail to init head object, please retry!");
        }
    }

    /**
     * Read the previous commit ID from the file.
     */
    public void read() throws IOException {
        FileInputStream fis = new FileInputStream(path + File.separator + "HEAD");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len = fis.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        fis.close();

        this.pre_commitID = new String(bytes);
    }

    /**
     * Set previous commit ID attribute and rewrite the file content.
     * @param commitID
     */
    public void setCommitID(String commitID) {
        try {
            this.pre_commitID = commitID;
            //写入文件
            FileWriter fw = new FileWriter(path + File.separator + "HEAD");
            fw.write("");//清空HEAD文件内容
            fw.flush();
            fw.write(pre_commitID);
            fw.close();
        } catch (IOException e){
            System.out.println("ERROR: IOException, please retry!");
        }

    }

}
