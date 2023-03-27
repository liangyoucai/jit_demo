package Func;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import Object.*;
import Util.FileOperationUtil;

public class JitReset {
    /**
     * 重置
     * @param args
     * @param workPath
     */
    public void run(String workPath, String[] args) throws IOException {
        //取出args[] 中的commit id，以及reset模式，其中mixed是默认模式，
        Commit commit = new Commit();
        if (args.length == 3){//reset [mode] [commit ID]
            File commitfile = new File(workPath + File.separator + ".git" + File.separator +
                    "objects" + File.separator + args[2]);
            if (commitfile.exists()) {//反序列化
                commit = FileOperationUtil.readObject(commitfile, Commit.class);
            }
            else{
                System.out.println("ERROR: The commit id is invalid.");
                System.exit(0);
            }

            switch (args[1]) {
                case "--soft":
                    resetSoft(commit);
                    System.out.println("The HEAD has reset.");
                    break;
                case "--mixed":
                    resetMixed(workPath, commit);
                    System.out.println("The index has reset.");
                    break;
                case "--hard":
                    resetHard(workPath, commit);
                    System.out.println("The work path has reset.");
                    break;
                default:
                    throw new IOException();
            }

        } else {//reset [commit ID]
            File commitfile = new File(workPath + File.separator + ".git" + File.separator +
                    "objects" + File.separator + args[1]);

            if (commitfile.exists()) {//反序列化
                commit = FileOperationUtil.readObject(commitfile, Commit.class);
            }
            else{
                System.out.println("ERROR: The commit id is invalid.");
                System.exit(0);
            }
            resetMixed(workPath, commit);
            System.out.println("The index has reset.");
        }

    }


    /**
     * 重置HEAD
     * @param C
     */
    private static void resetSoft(Commit C) {
        HEAD head = new HEAD();
        head.setCommitID(C.getKey());
    }

    /**
     * 重置缓存区
     * @param C
     * @param workPath
     */
    private static void resetMixed(String workPath, Commit C) {
        resetSoft(C);

        Tree tree = FileOperationUtil.readObject(new File(
                workPath + File.separator + ".git" + File.separator +
                        "objects" + File.separator + C.getTree_ID()), Tree.class);
        Index index = FileOperationUtil.readObject(new File(workPath + File.separator + ".git" + File.separator +
                "index"), Index.class);
        index.setInfo(tree.getEntries());
        index.write();
    }

    /**
     * 重置工作区
     * @param C
     * @param workPath
     */
    private static void resetHard(String workPath, Commit C) {
        try {
            //reset --hard：在mixed的基础上，重置工作区与暂存区内容一致。
            resetMixed(workPath, C);
            Index index = FileOperationUtil.readObject(new File(workPath + File.separator + ".git" + File.separator +
                    "index"), Index.class);

            //1.清空工作区文件
            FileOperationUtil.clearFiles();

            //2.重置工作区
            for (Map.Entry<String, String> entry: index.getInfo().entrySet()) {
                File f = new File(workPath + File.separator + entry.getKey());
                Blob b = FileOperationUtil.readObject(new File(
                        workPath + File.separator + ".git" + File.separator +
                                "objects" + File.separator + entry.getValue()), Blob.class);

                //写入文件
                FileWriter fw = new FileWriter(f);
                fw.write(new String(b.getContent()));
                fw.close();
            }
        } catch (IOException e){
            System.out.println("ERROR: IOException, please retry!");
        }

    }

}
