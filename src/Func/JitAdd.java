package Func;

import java.io.File;
import java.io.IOException;
import Object.Index;
import Util.FileOperationUtil;

public class JitAdd {
    /**
     * 向缓存区添加文件
     */
    public void run(String workPath, String[] args) {
        try {
            Index index = FileOperationUtil.readObject(new File(workPath + File.separator + ".git" + File.separator +
                    "index"), Index.class);//反序列化index对象
            if (!args[1].equals(".")) {//添加指定文件
                File file = new File(workPath + File.separator + args[1]);
                addFile(index, file);
            } else {//添加全体文件
                File dir = new File(workPath);
                File[] files = dir.listFiles();
                for (File f : files) {
                    if (f.isFile()) {
                        addFile(index, f);
                    }
                }
            }
            index.checkLegal();//检查index中的文件是否都在工作区
            index.write();
        } catch (IllegalArgumentException e){
            System.out.println("ERROR: serialVersionUID mismatch, please reset index file or delete the .git directory and init again!");
        } catch (IOException e){
            System.out.println("ERROR: an IOException occurs in reading file, please retry!");
        }
    }

    /**
     * 向缓存区添加一个文件
     * @param index
     * @param file
     */
    private void addFile(Index index, File file) throws IOException {
        String content = FileOperationUtil.readContent(file);
        int res = index.add(file, content);
        if (res > 0)//修改或新增info
            System.out.println(file.getName() + " has added.");
        else
            System.out.println(file.getName() + " has no changes.");
    }
}
