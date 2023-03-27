package Func;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import Object.*;
import Util.FileOperationUtil;
import Util.ZipUtil;

public class JitPull {
    /**
     * 从Server拉取版本
     * @param mgmt
     * @param workPath
     * @param args
     */
    public void run(Management mgmt, String workPath, String[] args) {
        File zip = new File(workPath + File.separator + ".git.zip");
        try {
            if (mgmt.getDepositories().containsKey(args[1])) {//调取socket
                //1. 接收文件
                //1.1 开启客户端
                Client client = new Client(mgmt.getIP(args[1]), mgmt.getPort(args[1]));//启动client
                //1.2 清空工作区
                FileOperationUtil.clearFiles();
                FileOperationUtil.deleteFileNoAsking(workPath + File.separator + ".git");
                //1.3 接收文件
                client.run("pull");

                //2. 解压文件包
                ZipUtil.unZip(zip, workPath + File.separator + ".git");
                //3. 删除压缩文件
                zip.delete();

                //4.重设工作区
                String[] arg = new String[3];
                arg[0] = "reset";
                arg[1] = "--hard";
                arg[2] = new HEAD().getPre_commitID();
                JitReset reset = new JitReset();
                reset.run(workPath, arg);
            } else {
                System.out.println("ERROR: Management doesn't contain depository named " + args[1] + "!");
                System.exit(0);
            }
        }
        catch (SocketException e){
            System.out.println("ERROR: Fail to connect Server, please check IP&Port and make sure Server is running!");
        } catch (NullPointerException e){
            System.out.println("ERROR: Cannot reach " + args[1] + "!");
        } catch (IOException e) {
            System.out.println("ERROR: Fail to activate Client, please retry!");
        }
        //catch (Exception e){//debug
        //    e.printStackTrace();
        //}
    }
}
