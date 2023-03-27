package Func;

import java.io.File;
import Object.Index;
import Object.HEAD;
import Object.Management;
import Util.FileOperationUtil;

public class JitInit {
    /**
     * 初始化git环境
     */
    public static void run(String workPath) {
        //1.1 判断工作区中（当前路径）是否存在.git目录，已存在则打印提示信息；
        File git_dir = new File(workPath + File.separator + ".git");
        if (git_dir.exists() && git_dir.isDirectory()) {//找到文件夹".git",返回提示信息
            System.out.println("Found '.git' directory.");
        } else {
            //1.2 创建.git目录，在其中创建objects目录用于blob、tree、commit等对象，命名均为其hash值；
            //  1.2.1 创建.git
            git_dir.mkdirs();
        }

        //  1.2.2 创建objects
        File objects = new File(git_dir.getPath() + File.separator + "objects");
        if (!objects.exists())
            objects.mkdirs();
        else
            System.out.println("Found 'objects' directory.");

        //1.3 创建Index对象序列化到.git目录下用于储存 文件名-hash值的对应条目（初始为空）；
        Index index = new Index();
        FileOperationUtil.createFile(workPath + File.separator + ".git", "index", index);//创建空文件

        //1.4 创建HEAD文件储存最近一次的commit id（即commit对象的hash值，初始为空）。
        new HEAD();//创建空文件

        //1.5 创建mgmt文件，存储远程仓库地址
        Management mgmt = new Management();
        FileOperationUtil.createFile(workPath + File.separator + ".git", "mgmt", mgmt);//创建空文件

        //1.6 输出init完成提示
        System.out.println("Initialized empty git repository in " + workPath + File.separator + ".git");
    }
}
