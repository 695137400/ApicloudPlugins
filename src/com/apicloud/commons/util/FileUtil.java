//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
    public FileUtil() {
    }

    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
            target = new File(target.getAbsolutePath());
        }

        return target;
    }

    public static int compare(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        } else if (s1 == null) {
            return -1;
        } else if (s2 == null) {
            return 1;
        } else {
            String[] arr1 = s1.split("[^a-zA-Z0-9]+");
            String[] arr2 = s2.split("[^a-zA-Z0-9]+");
            int ii = 0;

            for(int max = Math.min(arr1.length, arr2.length); ii <= max; ++ii) {
                if (ii == arr1.length) {
                    return ii == arr2.length ? 0 : -1;
                }

                if (ii == arr2.length) {
                    return 1;
                }

                int i1;
                try {
                    i1 = Integer.parseInt(arr1[ii]);
                } catch (Exception var11) {
                    i1 = 2147483647;
                }

                int i2;
                try {
                    i2 = Integer.parseInt(arr2[ii]);
                } catch (Exception var10) {
                    i2 = 2147483647;
                }

                if (i1 != i2) {
                    return i1 - i2;
                }

                int i3 = arr1[ii].compareTo(arr2[ii]);
                if (i3 != 0) {
                    return i3;
                }
            }

            return 0;
        }
    }

    public static String readVersion(String filePath) {
        String version = "";

        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);

                for(String lineTxt = null; (lineTxt = bufferedReader.readLine()) != null; version = version + lineTxt) {
                    System.out.println(lineTxt);
                }

                read.close();
            }

            return version;
        } catch (Exception var6) {
            var6.printStackTrace();
            return "1.0.0";
        }
    }

    public static String readCustomAPPID(String filePath) {
        return readVersion(filePath);
    }

    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }

        File dirFile = new File(dir);
        if (dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();
            File[] var7 = files;
            int var6 = files.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                File f = var7[var5];
                if (f.isFile()) {
                    flag = deleteFile(f.getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                } else {
                    flag = deleteDirectory(f.getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }

            if (!flag) {
                System.out.println("delete folder fail");
                return false;
            } else if (dirFile.delete()) {
                System.out.println("delete folder" + dir + "success");
                return true;
            } else {
                System.out.println("delete folder" + dir + "fail");
                return false;
            }
        } else {
            System.out.println("delete fail" + dir + "folder is not exit!");
            return false;
        }
    }

    public static boolean delAllFiles(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        } else if (!file.isDirectory()) {
            return flag;
        } else {
            String[] tempList = file.list();
            File temp = null;

            for(int i = 0; i < tempList.length; ++i) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }

                if (temp.isFile()) {
                    temp.delete();
                }

                if (temp.isDirectory()) {
                    delAllFiles(path + "/" + tempList[i]);
                    flag = true;
                }
            }

            return flag;
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            System.out.println("delete file" + fileName + "success");
            return true;
        } else {
            System.out.println("delete file" + fileName + "fail");
            return false;
        }
    }

    public static void copyFile(String sourcefile, String targetFile) throws IOException {
        File sourceFile = new File(sourcefile);
        if (sourceFile.exists()) {
            copyFile(sourceFile, new File(targetFile));
        }

    }

    public static void copyFile(File sourcefile, File targetFile) throws IOException {
        FileInputStream in = new FileInputStream(sourcefile);
        BufferedInputStream bis = new BufferedInputStream(in);
        FileOutputStream out = new FileOutputStream(targetFile);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        byte[] b = new byte[1024];
        boolean var7 = false;

        int length;
        while((length = bis.read(b)) != -1) {
            bos.write(b, 0, length);
        }

        bos.flush();
        bis.close();
        bos.close();
        in.close();
        out.close();
    }
}
