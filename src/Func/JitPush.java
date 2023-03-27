package Func;

import java.io.*;
import java.net.SocketException;

import Object.*;
import Util.FileOperationUtil;
import Util.ZipUtil;

public class JitPush {

    /**
     * 上传本地代码(Client)到远程仓库(Server)
     * @param mgmt
     * @param workPath
     * @param args
     */
    public void run(Management mgmt, String workPath, String[] args) {
        //1. 压缩根目录，规定压缩后的文件名为".git.zip"，压缩后存到根目录下
        File git = new File(workPath + File.separator + ".git");
        File gitZip = new File(workPath + File.separator +".git.zip");
        ZipUtil.zipFiles(git, gitZip);
        try {
            //2. 发送文件
            if (mgmt.getDepositories().containsKey(args[1])) {//调取socket
                Client client = new Client(mgmt.getIP(args[1]), mgmt.getPort(args[1]));//启动client
                client.run("push");
            } else {
                System.out.println("ERROR: Management doesn't contain depository named " + args[1] + "!");
                System.exit(0);
            }
        } catch (SocketException e){
            System.out.println("ERROR: Server has not activated, please run Server first!");
        } catch (IOException e) {
            System.out.println("ERROR: Fail to activate Client, please retry!");
        }
        finally {
            FileOperationUtil.deleteFileNoAsking(gitZip.getPath());//删除压缩文件
        }
    }
}
