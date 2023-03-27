package Func;

import java.io.File;
import java.io.IOException;
import Object.Index;
import Util.FileOperationUtil;

public class JitRm {
    /**
     * 移除文件
     * @param workPath
     * @param args
     */
    public void run(String workPath, String[] args) throws IOException {
        if (args.length == 3 && args[1].equals("--cached")) {
            //rm --cached：仅删除index中对应条目
            File file = new File(workPath + File.separator + args[2]);
            if (!file.exists()) {
                System.out.println("ERROR: pathspec '" + args[2] + "' did not match any files.");
                System.exit(0);
            }
            else {
                Index index = FileOperationUtil.readObject(new File(workPath + File.separator + ".git" + File.separator +
                        "index"), Index.class);
                index.remove(file.getName());
                index.write();
                System.out.println("File "+ args[2] + " has removed from cache successfully.");
            }
        } else if (args.length == 2){
            //在index对象中删除对应条目，在工作区中删除该文件
            File file = new File( workPath + File.separator + args[1]);
            if (!file.exists()) {
                System.out.println("ERROR: pathspec '" + args[1] + "' did not match any files.");
                System.exit(0);
            }
            else {
                //1. 在工作区中删除该文件
                if (FileOperationUtil.deleteFile(file)) {
                    System.out.println("File " + args[1] + " has removed from the workpath successfully.");
                    //2.在index对象中删除对应条目
                    Index index = FileOperationUtil.readObject(new File(workPath + File.separator + ".git" + File.separator +
                            "index"), Index.class);
                    index.remove(file.getName());
                    index.write();
                } else
                    System.out.println("rm operation has canceled.");
            }
        }else {
            throw new IOException();
        }
    }
}
