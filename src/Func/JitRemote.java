package Func;

import java.io.File;
import java.io.IOException;
import Object.Management;

public class JitRemote {
    /**
     * 创建、查看和删除本地仓库与其他代码仓库之间的连接
     * git remote -v:展示所有链接
     * git remote add <name> <IP> <Port>
     * git remote rm <name>
     * git remote rename <old-name> <new-name>
     * @param args
     */
    public void run(Management mgmt, String workPath, String[] args) throws IOException {
        if (args[1].equals("-v") && args.length == 2)
            System.out.println(mgmt.DepToString());
        else if (args[1].equals("add") && args.length == 5)
            mgmt.addDep(args[2], args[3] + " " + args[4]);
        else if (args[1].equals("rm") && args.length == 3)
            mgmt.rmDep(args[2]);
        else if (args[1].equals("rename") && args.length == 4)
            mgmt.renameDep(args[2], args[3]);
        else
            throw new IOException();

        mgmt.write(workPath + File.separator + ".git");
    }
}
