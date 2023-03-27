package Util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class FileOperationUtil {
    //private static final String workPath = "D:" + File.separator + "jit_demo" + File.separator + "src" + File.separator + "singers";
    private static final String workPath = System.getProperty("user.dir");

    /**
     * Create a file with contents in the parent path
     * @param parentpath
     * @param filename
     * @param obj
     */
    public static void createFile(String parentpath, String filename, Serializable obj) {
        try {
            //1.检查路径名
            File file_dir = new File(parentpath);
            if (!file_dir.isDirectory()) {
                System.out.println(file_dir.getPath());
                throw new IOException(file_dir.getPath() + " is NOT a directory!");
            }
            //2.检查同目录下是否已有文件
            File file = new File(parentpath + File.separator + filename);
            if (file.isDirectory()) {
                throw new IOException(filename + " is not a file!");
            }
            if (file.exists())
                throw new IOException(filename + " already exists!");
            //3.序列化对象
            writeObject(file, obj);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete all files in the workspace
     */
    public static void clearFiles(){
        File dir = new File(workPath);
        File[] files = dir.listFiles();
        for(File f:files){
            if(f.isFile()){
                f.delete();
            }
        }
    }

    /**
     * Delete file without asking (git.zip or .git)
     * @param path
     */
    public static void deleteFileNoAsking(String path){
        File f = new File(path);
        if (f.exists())
            f.delete();
        else
            System.out.println("ERROR: Cannot find " + path);
    }

    /**
     * Delete the specified file
     * @param file
     */
    public static boolean deleteFile(File file){
        System.out.println("Are you sure to delete the file: " + file.getPath() + "?\n" +
                "Enter \"YES\" to continue the delete process, other words will be considered as a cancel statement.");
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        if (ans.equals("YES")) {
            file.delete();
            return true;
        }
        else
            return false;
    }

    /**
     * Read file contents
     * @param file
     * @return String
     */
    public static String readContent(File file) throws IOException {
        if (!file.exists()) {
            System.out.println("ERROR: Pathspec '" + file.getPath() + "' did not match any files!");
            System.exit(0);
        }
        else {
            //缓存文件内容
            FileInputStream is = new FileInputStream(file);
            String content = "";
            int numread = is.read();
            while (numread != -1) {
                content += (char) numread;
                numread = is.read();
            }
            is.close();
            return content;
        }
        return null;
    }


    /*--------- SERIALIZATION UTILITIES ---------*/
    /** Write OBJ to FILE. */
    public static void writeObject(File file, Serializable obj) {
        writeContents(file, serialize(obj));
    }

    /** Returns a byte array containing the serialized contents of OBJ. */
    private static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(obj);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            System.out.println("Internal error serializing commit.");
            excp.printStackTrace();
            return null;
        }
    }

    /** Write the result of concatenating the bytes in CONTENTS to FILE,
     *  creating or overwriting it as needed.  Each object in CONTENTS may be
     *  either a String or a byte array.  Throws IllegalArgumentException
     *  in case of problems. */
    private static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /*--------- DESERIALIZATION UTILITIES ---------*/

    /** Return an object of type T read from FILE, casting it to EXPECTEDCLASS.
     *  Throws IllegalArgumentException in case of problems. */
    public static <T extends Serializable> T readObject(File file,
                                                        Class<T> expectedClass) {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(file));
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (Exception excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }


}
