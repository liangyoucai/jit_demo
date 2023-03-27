package Func;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import Object.Index;
import Object.Tree;
import Object.HEAD;
import Object.Commit;
import Util.FileOperationUtil;

public class JitCommit {
    /**
     * 提交缓存区
     * @param args
     */
    public void run(String workPath, String[] args) {
        //1. 将index中所有条目生成tree对象序列化到objects文件夹下；
        Index index = FileOperationUtil.readObject(new File(workPath + File.separator + ".git" + File.separator +
                "index"), Index.class);//index文件已经建立，反序列化index
        Tree tree = new Tree(index);//生成tree文件存储在object文件夹下，内容为index中的条目
        tree.write();

        //2. 将commit对象序列化到objects文件夹下
        HEAD head = new HEAD();//读取HEAD
        String author = System.getProperty("user.name");
        String date = new Date().toString();
        Commit commit = new Commit(head.getPre_commitID(), author, tree.getKey(), args[2], date);//创建commit文件,HEAD文件栈顶存储最近一次历史提交记录
        commit.write();

        //3. 更新HEAD文件中的commit id。
        head.setCommitID(commit.getKey());
        System.out.println("New file has committed.");

        //4. 输出本次提交文件变动情况
        commit.compare();
    }
}
