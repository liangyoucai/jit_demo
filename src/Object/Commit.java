package Object;

import Util.FileOperationUtil;
import Util.SHAUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Commit extends GitObject{
        private static final long serialVersionID = 154353533453L;

        private String pre_commit_ID = null;
        private String author;
        private String tree_ID;
        private String date;
        private String message;

        public String getTree_ID() {
                return tree_ID;
        }
        public String getPre_commit_ID() {
                return pre_commit_ID;
        }
        /**
         * Constructor
         * @param pre_commit_ID
         * @param author
         * @param tree_ID
         * @param message
         * @param date
         */
        public Commit(String pre_commit_ID, String author, String tree_ID, String message, String date) {
                this.pre_commit_ID = pre_commit_ID;
                this.author = author;
                this.tree_ID = tree_ID;
                this.date = date;
                this.message = message;
                this.content = toString().getBytes(StandardCharsets.UTF_8);
                this.key = SHAUtil.sha1(content);//file name
        }

        /**
         * Empty Constructor
         */
        public Commit(){}

        /*
        * Print new, modified and deleted files compared with
        * the last submission
        */
        public void compare(){
                //存放新增、修改、删除文件名
                Set<String> add_set = new HashSet<>();
                Set<String> revise_set = new HashSet<>();
                Set<String> delete_set = new HashSet<>();
                Set<String> this_set;
                Set<String> pre_set;

                //反序列化本次tree
                Tree this_tree= FileOperationUtil.readObject(new File(
                        path + File.separator + tree_ID), Tree.class);
                this_set = this_tree.getEntries().keySet();

                if(pre_commit_ID == null || pre_commit_ID.length() == 0){//first commit
                        add_set = this_set;
                }else {
                        //1. 反序列化前一次commit和tree对象
                        Commit pre_commit = FileOperationUtil.readObject(new File(
                                path + File.separator + pre_commit_ID), Commit.class);

                        Tree pre_tree = FileOperationUtil.readObject(new File(
                                path + File.separator + pre_commit.getTree_ID()), Tree.class);
                        pre_set = pre_tree.getEntries().keySet();
                
                /*2. 对每一个pre_set中key检查：
                  如果this_set包含key
                        判断value值是否相同
                                相同，  无修改
                                不相同，文件内容修改
                  如果this_set不包含key
                        该文件已删除
                  this_set中有，preset中无
                        新增文件
                */
                        for (String key : pre_set) {
                                if (this_set.contains(key)) {
                                        if (!this_tree.getEntries().get(key).equals(
                                                pre_tree.getEntries().get(key))) {
                                                revise_set.add(key);
                                        }
                                } else {
                                        delete_set.add(key);
                                }
                        }
                        for (String key : this_set) {
                                if (!pre_set.contains(key)) {
                                        add_set.add(key);
                                }
                        }
                }

                System.out.println("This commit: \nNew files include:");
                printCompareResult(add_set);
                System.out.println("Modification files include:");
                printCompareResult(revise_set);
                System.out.println("Delete files include:");
                printCompareResult(delete_set);
                
        }

        private void printCompareResult(Set<String> set) {
                if (set.isEmpty()){
                        System.out.println("\tNo files.");
                }else {
                        for (String key : set) {
                                System.out.println("\t" + key);
                        }
                }
        }

        /**
         * Reorganize the commit file content.
         * @return String
         */
        @Override
        public String toString() {
                return "commit\t"
                        + "tree " + tree_ID + "\n"
                        + "\tauthor " + author + "\n"
                        + "\ttime " + date + "\n"
                        + "\tmessage " + message +"\n"
                        + "\tprevious_commitID " + pre_commit_ID+"\n";
        }
}
