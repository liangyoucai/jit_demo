package Object;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Socket {
    private static final String workPath = System.getProperty("user.dir");
    //private static final String workPath = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "singers";
    private Socket client;//客户端套接字
    private FileInputStream fis;
    private FileOutputStream fos;
    private DataOutputStream dos;//数据输出流
    private DataInputStream dis;//数据输入流

    /**
     * Constructor
     * @param ip
     * @param port
     */
    public Client(String ip, int port) throws IOException {
        //调用父类构造函数，初始化client
        super(ip, port);
        this.client = this;
        System.out.println("Client[port:" + client.getLocalPort() + "] successfully connected.");
    }

    /**
     * 发送信息
     * @param message
     */
    public void sendMessage(String message) {
        try {
            dos = new DataOutputStream(client.getOutputStream());
            dos.write(message.getBytes());
            dos.flush();//清空缓存区
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    /**
     * 接收文件
     */
    public void receiveGit() {
        try {
            dis = new DataInputStream(client.getInputStream());
            long fileLength = dis.readLong();
            // 读取文件名和长度
            String fileName = dis.readUTF();
            File file = new File(workPath + File.separatorChar + fileName);
            //文件输出流
            fos = new FileOutputStream(file);
            System.out.println("============ Starting receive =============================================");
            // 开始接收文件
            byte[] bytes = new byte[1024];
            int length, tempLength = 0;//templength检查当前读入位置
            while (tempLength <= fileLength) {//tempLength <= fileLength，没有读完文件
                if((length = dis.read(bytes, 0, bytes.length)) == -1)//越界
                    break;
                fos.write(bytes, 0, length);
                fos.flush();
                tempLength += length;//更新当前读取位置
                System.out.print("| " + (100L * tempLength / fileLength) + "% |");
            }

            System.out.println("\n============ Received Successfully [File Name: " + fileName + "] ==================");

        } catch (EOFException e) {
            //e.printStackTrace();
            System.out.println("ERROR: Remote depository is empty! Please pull first!");
            System.exit(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件
     *     若文件存在返回true,且输入流发送一个true值，
     *     反之方法返回false且输入流发送一个false值
     * @return boolean
     */
    public boolean sendGit() throws IOException{
        File git = new File(workPath + File.separator + ".git.zip");
        dos = new DataOutputStream(client.getOutputStream());
        if (git.exists()) {//文件存在
            fis = new FileInputStream(git);
            // 文件名和长度
            dos.writeLong(git.length());
            dos.flush();
            dos.writeUTF(git.getName());
            dos.flush();
            // 开始传输文件
            System.out.println("============ Start sending Git ================");
            byte[] bytes = new byte[512];

            int length = 0;//文件当前读入的最后一个指针
            long progress = 0;//当前读入进度

            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();//清空缓存区，继续读入
                //传输进度条
                progress += length;
                System.out.print("| " + (100 * progress / git.length()) + "% |");
            }

            System.out.println("\n============ Send Git successfully ============");
            return true;
        }else{//没有文件，返回错误信息
            dos.writeLong(0);
            dos.flush();
            return false;
        }


    }

    /**
     * 运行客户端
     * @param mode
     */
    public void run(String mode) throws IOException {
        try {
            switch (mode) {
                case "push":
                    sendMessage("push");
                    if(!sendGit())
                        throw new SocketException();
                    break;
                case "pull":
                    sendMessage("pull");
                    receiveGit();
                    break;
                default:
                    break;
            }
        }
        catch (IOException e){
            System.out.println("ERROR: an I/O error occurs, please restart the server!");
        }
        finally {
            //回收资源
            if (dos != null)
                dos.close();
            if (fis != null)
                fis.close();
            if (fos != null)
                fos.close();
            if (dis != null)
                dis.close();
            client.close();
            System.out.println("Client has closed.");
        }

    }


}

