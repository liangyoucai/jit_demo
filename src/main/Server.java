package main;

import Util.FileOperationUtil;
import Object.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Server {
    private static final String workPath = System.getProperty("user.dir");
    //private static final String workPath = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "git_SERVER";
    //远程仓库地址
    //private static int SERVER_PORT = 8888; // 服务端端口
    private static Socket clientSocket;//与client的通信套接字
    private ServerSocket server;//服务器
    private DataOutputStream dos;//数据输出流
    private DataInputStream dis;//数据输入流
    private FileInputStream fis;
    private FileOutputStream fos;//文件输出流

    /**
     * 构造函数
     * @exception BindException,IOException
     */
    public Server(int port) {
        try {
            //1.启动服务器，等待连接
            server = new ServerSocket(port);
            System.out.println("Git-Server is running...");
            //2.接收来自客户端的连接请求
            clientSocket = server.accept();
        } catch (BindException e){
            System.out.println("ERROR: Address already in use, please reset the port number!");
        } catch (IOException e){
            System.out.println("ERROR: Fail to activate Server, please check the IP address& PORT and retry!");
        }
    }

    /**
     * 接收信息
     * @return String
     * @exception IOException
     */
    private String receiveMessage() {
        try {
            dis = new DataInputStream(clientSocket.getInputStream());
            byte[] bytes = new byte[1024];
            int len = dis.read(bytes);//使用byte数组读入信息
            return new String(bytes, 0, len);
        }catch (IOException e){
            System.out.println("ERROR: an I/O error occurs, please restart the server!");
        }
        return null;
    }

    /**
     * 发送文件
     * @return boolean
     * @exception IOException
     */
    private boolean sendGit() {
        try {
            File git = new File(workPath + File.separator + ".git.zip");
            dos = new DataOutputStream(clientSocket.getOutputStream());
            fis = new FileInputStream(git);
            // 文件名和长度
            dos.writeLong(git.length());
            dos.flush();
            dos.writeUTF(git.getName());
            dos.flush();
            // 开始传输文件
            System.out.println("======== Start sending Git ============");
            byte[] bytes = new byte[1024];

            int length;//文件当前读入的最后一个指针
            long progress = 0;//当前读入进度

            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();//清空缓存区，继续读入
                //传输进度条
                progress += length;
                System.out.print("| " + (100 * progress / git.length()) + "% |");
            }
            System.out.println();
            System.out.println("======== Send Git successfully ========");
            return true;
        } catch (IOException e) {
            System.out.println("ERROR: Cannot send git because of an IOException, please retry!");
        }
        return false;
    }

    /**
     * 接收文件
     * @exception EOFException,IOException
     */
    private boolean receiveGit() {
        try {
            dis = new DataInputStream(clientSocket.getInputStream());
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();
            File file = new File(workPath + File.separatorChar + fileName);
            //文件输出流
            fos = new FileOutputStream(file);
            System.out.println("======== Starting receive ===================================");
            // 开始接收文件
            byte[] bytes = new byte[1024];
            int length, tmpLength = 0;//templength检查当前读入位置
            while (tmpLength <= fileLength) {//tempLength <= fileLength，没有读完文件
                if((length = dis.read(bytes, 0, bytes.length)) == -1)//越界
                    break;
                fos.write(bytes, 0, length);
                fos.flush();
                tmpLength += length;//更新当前读取位置
                System.out.print("| " + (100L * tmpLength / fileLength) + "% |");
            }

            System.out.println("\n======== Received Successfully [File Name: " + fileName + "] ========");
            return true;
        } catch (EOFException e) {
            System.out.println("ERROR: Received no files from client!");
            return false;
        } catch (IOException e) {
            System.out.println("ERROR: an I/O error occurs, " +
                    "please restart the server!");
            return false;
        }
    }

    /**
     * 运行server
     * @exception IOException
     */
    private void run() throws IOException {
        try {
            //1.接收client端命令
            String mode = receiveMessage();
            //2.判断命令
            switch (mode) {
                case "push":
                    System.out.println("Loading the depository from Client...");
                    FileOperationUtil.deleteFileNoAsking(workPath + File.separator + ".git.zip");//删除git
                    if (receiveGit())
                        System.out.println("The depository has refreshed.");
                    break;
                case "pull":
                    System.out.println("Uploading the depository to Client...");
                    if (!isGitExist()) {
                        dos.writeLong(0);
                        dos.flush();
                        throw new Exception();
                    }
                    if(!sendGit())
                        System.out.println("ERROR: Failed to send git.");
                    break;
                default:
                    System.out.println("ERROR: This function is not provided yet. The server can only provide pull and push functions!");
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("ERROR: Remote depository is empty!");
        } finally {
            //3.最后统一回收资源
            if (dis != null)
                dis.close();
            if (fis != null)
                fis.close();
            if (fos != null)
                fos.close();
            if (dos != null)
                dos.close();
            clientSocket.close();
            System.out.println("Server has closed.");
        }
    }

    /**
     * 检查.git是否存在
     * @return boolean
     */
    private boolean isGitExist() {
        File git = new File(workPath + File.separator + ".git.zip");
        return git.exists();
    }

    /**
     * 设置port
     * @exception IOException
     */
    private static void resetPort(Management mgmt)  {
        try{
            System.out.println("Please enter new port: ");
            Scanner sc = new Scanner(System.in);
            int new_port = sc.nextInt();
            if (new_port >= 1024 && new_port <= 65535 ){
                mgmt.setPort("origin", new_port);
                mgmt.write(workPath);//反序列化到服务端
                System.out.println("New port has been reset!");
                print(mgmt);
            }else{
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Illegal number for PORT, the port id must between 1024 and 65535. Please retry!");
        }
    }

    /**
     * 打印当前的IP&port
     * @exception UnknownHostException
     */
    private static void print(Management mgmt){
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            System.out.println("IP address: " + ip4.getHostAddress() +
                    "\nPort: " + mgmt.getPort("origin"));
        } catch (UnknownHostException e){
            System.out.println("ERROR: IP address of the host could not be determined, please check your network!");
        }

    }

    /**
     * 帮助文档
     */
    private static void help(){
        System.out.println("This is Git-Server. (work path: " + workPath + ")" +
                "\nYou may choose the following options:\n" +
                "local              print local IP & port\n" +
                "reset              reset the port\n" +
                "print              print current IP & port\n" +
                "run                run the server (it will close automatically after a \"pull\" or \"push\")");
    }

    /**
     * 初始化仓库
     * @return Management
     */
    private static Management init(){
        Management mgmt = new Management();
        File mgmtfile = new File(workPath + File.separator + "mgmt");
        if (!mgmtfile.exists())
            FileOperationUtil.createFile(workPath, "mgmt", mgmt);//创建空文件
        else
            mgmt = FileOperationUtil.readObject(mgmtfile, Management.class);
        return mgmt;
    }

    public static void main(String[] args) {
        Management mgmt = init();
        try {
            switch (args[0]) {
                case "local":
                    System.out.println("Local Server IP: 127.0.0.1, " + " Server PORT:" + mgmt.getPort("origin"));
                    break;
                case "run":
                    Server server = new Server(mgmt.getPort("origin"));
                    server.run();
                    break;
                case "reset": //重设端口号
                    resetPort(mgmt);
                    break;
                case "print": //打印IP地址
                    print(mgmt);
                    break;
                case "help":
                    help();
                    break;
                default:
                    throw new IOException();
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            help();
        } catch (IOException e){
            System.out.println("ERROR: Wrong command, please re-enter!");
        } catch (IllegalArgumentException e){
            System.out.println("FATAL: The system cannot deserialize, please clear the work path and retry.");
        }
        //catch (Exception e){//for debug
        //    e.printStackTrace();
        //}
    }
}


