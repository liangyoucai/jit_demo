package main;

import java.io.*;

import Func.*;
import Object.*;
import Util.FileOperationUtil;

/**
     *    git主函数
     *
    **/
public class git {
    //设定工作目录
    private static final String workPath = System.getProperty("user.dir");
    //private static final String workPath = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "singers";
    public static void main(String[] args) {
        String gitPath = workPath + File.separator + ".git";
        try {
            switch (args[0]) {
                case "init":
                    if (args.length != 1)
                        throw new IOException();
                    JitInit func_init = new JitInit();
                    func_init.run(workPath);
                    break;
                case "add":
                    if (args.length != 2)
                        throw new IOException();
                    JitAdd func_add = new JitAdd();
                    func_add.run(workPath, args);
                    break;
                case "commit":
                    if (args.length != 3 || !args[1].equals("-m"))
                        throw new IOException();
                    JitCommit func_commit = new JitCommit();
                    func_commit.run(workPath, args);
                    break;
                case "log":
                    if (args.length != 1)
                        throw new IOException();
                    Jitlog func_log = new Jitlog();
                    func_log.run(workPath);
                    break;
                case "rm":
                    if (args.length < 2 || args.length > 3)
                        throw new IOException();
                    JitRm func_rm = new JitRm();
                    func_rm.run(workPath, args);
                    break;
                case "reset":
                    if (args.length < 2 || args.length > 3)
                        throw new IOException();
                    JitReset func_reset = new JitReset();
                    func_reset.run(workPath, args);
                    break;
                case "remote":
                    if (args.length < 2 || args.length > 5)
                        throw new IOException();
                    Management mgmt_r = FileOperationUtil.readObject(new File(gitPath + File.separator + "mgmt"), Management.class);
                    JitRemote func_remote = new JitRemote();
                    func_remote.run(mgmt_r, workPath, args);
                    break;
                case "push":
                    if (args.length != 2)
                        throw new IOException();
                    Management mgmt_push = FileOperationUtil.readObject(new File(gitPath + File.separator + "mgmt"), Management.class);
                    JitPush func_push = new JitPush();
                    func_push.run(mgmt_push, workPath, args);
                    break;
                case "pull":
                    if (args.length != 2)
                        throw new IOException();
                    Management mgmt_pull = FileOperationUtil.readObject(new File(gitPath + File.separator + "mgmt"), Management.class);
                    JitPull func_pull = new JitPull();
                    func_pull.run(mgmt_pull, workPath, args);
                    break;
                case "help":
                    help();
                    break;
                default: //其他输入均报错，提示用户重新输入
                    throw new IOException();
            }
        }
        catch (IOException e){
            System.out.println("ERROR: Your input is incorrect, please re-enter!\n" +
                    "Input '--help' for the possible usage of git.");
        } catch (ArrayIndexOutOfBoundsException e){
            help();
        } catch (IllegalArgumentException e){
            System.out.println("FATAL: The system cannot deserialize, please clear the work path and use \"init\" to initialize environment.");
        }
        //catch (Exception e){
        //    e.printStackTrace();
        //}
    }

    /**
     * Print Help Document
     */
    private static void help(){
        System.out.println("Welcome to git 0.0 :)\t(work path: " + workPath + ")\n" +
                "Git usage: git <command> [args] " +
                "\n" +
                "These are common Git commands used in various situations:\n" +
                "\n" +
                "start a working area \n" +
                "   init                          Create an empty Git repository or reinitialize an existing one\n" +
                "\n" +
                "work on the current change \n" +
                "   add [file]                    Add file contents to the index\n" +
                "       .                         Add all files under the current directory to the staging area\n" +
                "   reset [mode][commitID]        Reset current HEAD to the specified state\n" +
                "   rm [file]                     Remove files from the working tree and from the index\n" +
                "      --cache [file]             Remove files from the index only\n" +
                "\n" +
                "examine the history and state \n" +
                "   log                           Show commit logs\n" +
                "\n" +
                "grow, mark and tweak your common history\n" +
                "   commit -m [message]           Record changes to the repository\n" +
                "\n" +
                "collaborate \n" +
                "   remote -v                     Show all remote warehouses\n" +
                "          add [shortname] [url]  Add remote version library\n" +
                "          rm [name]              Delete remote version library\n" +
                "          rename [name1] [name2] Modify name1 warehouse to name2\n" +
                "   pull [alias]                  Fetch from and integrate with another repository or a local branch\n" +
                "   push [alias]                  Update remote refs along with associated objects\n" +
                "\n" );
    }



















}
