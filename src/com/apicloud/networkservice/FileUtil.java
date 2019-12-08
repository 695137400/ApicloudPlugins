//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.networkservice;

import java.io.*;
import java.util.*;

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

    public static void updateJar(String path, String jarName) {
        File file = new File(path);
        System.out.println("Update path" + file.toString());
        List<File> files = new ArrayList();
        File[] var7;
        int var6 = (var7 = file.listFiles()).length;

        for(int var5 = 0; var5 < var6; ++var5) {
            File f = var7[var5];
            System.out.println(f.toString());
            if (f.toString().contains(jarName)) {
                files.add(f);
                System.out.println("add compare obj:" + file.toString());
            }
        }

        update(files);
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

    private static void update(List<File> files) {
        File[] lists = (File[])files.toArray(new File[files.size()]);
        List<File> deleteFiles = new ArrayList();
        Arrays.sort(lists, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return this.compare(f1.getAbsoluteFile().toString(), f2.getAbsoluteFile().toString());
            }

            private int compare(String s1, String s2) {
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
                        } catch (Exception var12) {
                            i1 = 2147483647;
                        }

                        int i2;
                        try {
                            i2 = Integer.parseInt(arr2[ii]);
                        } catch (Exception var11) {
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
        });

        File f;
        for(int i = 0; i < lists.length - 1; ++i) {
            f = lists[i];
            deleteFiles.add(f);
        }

        System.out.println("delete length" + deleteFiles.size());
        Iterator var5 = deleteFiles.iterator();

        while(var5.hasNext()) {
            f = (File)var5.next();
            if (f.isDirectory()) {
                deleteDirectory(f.getAbsolutePath().toString());
            } else {
                deleteFile(f.getAbsolutePath().toString());
            }
        }

    }

    public static List<String> readTxtFile(String filePath) {
        ArrayList lists = new ArrayList();

        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    lists.add(lineTxt);
                }

                read.close();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return lists;
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

    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }

        File dirFile = new File(dir);
        if (dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                } else {
                    flag = deleteDirectory(files[i].getAbsolutePath());
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
        FileInputStream fis = new FileInputStream(sourcefile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(targetFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] b = new byte[1024];
        boolean var7 = false;

        int len;
        while((len = bis.read(b)) != -1) {
            bos.write(b, 0, len);
        }

        bos.flush();
        bis.close();
        bos.close();
        fos.close();
        fis.close();
    }

    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        (new File(targetDir)).mkdirs();
        File[] file = (new File(sourceDir)).listFiles();

        for(int i = 0; i < file.length; ++i) {
            if (file[i].isFile()) {
                File sourceFile = file[i];
                File targetFile = new File((new File(targetDir)).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }

            if (file[i].isDirectory() && !file[i].getName().startsWith(".")) {
                System.out.println(file[i].getName());
                String sourcePath = sourceDir + "/" + file[i].getName();
                String targetPath = targetDir + "/" + file[i].getName();
                copyDirectiory(sourcePath, targetPath);
            }
        }

    }
}
