package Func;

import java.io.File;
import java.io.IOException;
import Object.HEAD;
import Object.Commit;
import Util.FileOperationUtil;

public class Jitlog {
    /**
     * 打印commit日志
     */
    public void run(String workPath) throws IOException {
        String objects_path = workPath + File.separator + ".git" + File.separator + "objects";
        HEAD head = new HEAD();
        //1. 从HEAD文件中读到最近一次的commit id，若HEAD为空打印提示信息。
        if (head.getPre_commitID() == null || head.getPre_commitID().length() == 0) {//HEAD为空
            System.out.println("No documents have been committed!");
            return;
        }
        //2. 反序列化对应的commit对象，打印commit id，message，commit时间
        File commitfile = new File(objects_path + File.separator + head.getPre_commitID());
        Commit commit = FileOperationUtil.readObject(commitfile, Commit.class);
        //2.1 打印本次commit
        System.out.println("HEAD -> commit " + commit.getKey() + "\r\n" +
                commit + "\r\n");
        String parentHash = commit.getPre_commit_ID();
        //2.2 只要parentHash不为空，循环打印log
        while (parentHash.length() != 0) {
            //2.2.1 反序列化parent commit
            Commit c = FileOperationUtil.readObject(new File(
                    objects_path + File.separator + parentHash), Commit.class);
            //2.2.2 打印commit信息
            System.out.println("commit " + c.getKey() + "\r\n" + c + "\r\n");
            //2.2.3 迭代parentHash
            parentHash = c.getPre_commit_ID();
        }
    }
}
