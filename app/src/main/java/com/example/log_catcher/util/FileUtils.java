package com.example.log_catcher.util;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import static com.example.log_catcher.util.StrUtil.byte2HexStr;

/**
 * <p>Description: 用于File操作的相关接口<br>
 * <p>CreatDate: 20200325 <br></p>
 * <p>author: Miles<br></p>
 * <p>version: v1.0<br></p>
 * <p>update: [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]<br></p>
 */
public class FileUtils {

    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte


    /**
     * <p>Description: 创建strFileDir目录<br>
     * @param strFileDir 创建的路径
     * @return true
     *         false
     */
    public static boolean CreateFolder(String strFileDir){

        if(null == strFileDir){
            return false;
        }

        try{
            //目标目录
            File targetDir = new File(strFileDir);
            //创建目录
            if(!targetDir.exists())
            {
                targetDir.mkdirs();
            }

            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }


    }
    /**
     * <p>Description: 判断file目录是否存在<br>
     * @param file
     * @return true:存在
     *         false:不存在
     */
    public static boolean checkDirExist(File file)
    {
        if(null == file)
            return false;

        if (file.exists()) {
            if (file.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Description: 判断file文件是否存在<br>
     * @param fileDir   目录路径
     * @return true:存在
     *         false:不存在
     */
    public static boolean checkDirExist(String fileDir)
    {
        if(null == fileDir){
            return false;
        }
        File fileDirTemp =new File(fileDir);

        if (fileDirTemp.exists()) {
            if (fileDirTemp.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Description: 实现单个文件copy<br>
     * @param sourceFile 源文件路径
     * @param targetFile 目标文件路径
     * @return boolean
     */
    public static boolean copyFile(File sourceFile, File targetFile) {

        boolean ret=true;
        try {
            if (sourceFile.exists()) { //文件存在
                BufferedInputStream inBuff = null;
                BufferedOutputStream outBuff = null;
                inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
                outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = inBuff.read(b)) != -1) {
                    outBuff.write(b, 0, len);
                }
                outBuff.flush();
                inBuff.close();
                outBuff.close();

            }
            else{
                ret=false;
            }
        }
        catch (Exception e) {
            // System.out.println("复制单个文件操作出错");
            // e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    /**
     *
     * <p>Description: 复制单个文件<br>
     * @param oldPath String 原文件路径，如：c:/fqf.txt
     * @param newPath String 复制后路径，如：f:/fqf.txt
     * @return boolean
     */
    public boolean copyFile(String oldPath, String newPath) {
        boolean ret = true;

        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
                fs.close();
                inStream.close();
            }
            else
            {
                ret = false;
            }
        }
        catch (Exception e) {
            // System.out.println("复制单个文件操作出错");
            // e.printStackTrace();
            ret = false;
        }
        return ret;

    }

    /**
     * 复制整个文件夹内的所有东西
     * @param oldPath String 原文件路径, 如：c:/fqf
     * @param newPath String 复制后路径, 如：f:/fqf/ff
     * @return boolean
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        boolean ret = true;
        try {
            (new File(newPath)).mkdirs(); //如果目录不存在 则建立该目录
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }
                else
                {
                    temp=new File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + File.separator +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子目录
                    copyFolder(oldPath+File.separator+file[i],newPath+File.separator+file[i]);
                }
            }
        }
        catch (Exception e) {
            ret = false;
        }
        return ret;
    }

    /**
     * <p>Description: 实现单个文件的delete<br>
     * @param deleteFilePath   源文件路径
     * @return  true
     *          false
     */
    public static boolean deleteFile(String deleteFilePath) {
        try {
            File file = new File(deleteFilePath);
            if (file.exists()) {
                file.delete();
            }
            return true;
        } catch (Exception e) {
            Log.e("warning", "删除指定文件时出现异常");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除本文件夹以及目录下的所有文件
     * @param filePath    被删除目录的文件路径
     * @return 目录删除成功返回true，
     *         否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        // 如果filePath不以文件分隔符结尾，自动添加文件分隔�?
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();

        if(null != files)
        {
            // 遍历删除文件夹下的所有文件夹(包括子目录)
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    // 删除子文件
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag){
                        break;
                    }
                }
                else {
                    // 删除子目录
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }
            if (!flag){
                return false;
            }

            // 删除当前空目录
            return dirFile.delete();
        }

        return false;

    }


    /**
     * 判断文件名后缀是否是想要的
     * @param f  File文件
     * @param fileSuffix   过滤的后缀
     * @return  成功: 有该后缀
     *          失败：吴该后缀
     */
    /* 判断文件MimeType的method */
    public static boolean isSpecifiedFileType(File f, String fileSuffix) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展�? */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        LogHelper.getInstance().w("isSpecifiedFileType---fName: " + fName);
        LogHelper.getInstance().w("isSpecifiedFileType---end: " + end);


        /* 依扩展名的类型决定MimeType */
        if(end.equals(fileSuffix))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断文件名后缀是否是想要的
     * @param fileName  File文件
     * @param fileSuffix   过滤的后缀
     * @return  成功: 有该后缀
     *          失败：无该后缀
     */
    /* 判断文件MimeType的method */
    public static boolean isSpecifiedFileType(String fileName, String fileSuffix) {
        String type = "";
        /* 取得后缀名*/
        String end = fileName
                .substring(fileName.lastIndexOf(".") + 1, fileName.length())
                .toLowerCase();

        LogHelper.getInstance().w("isSpecifiedFileType---fName=" + fileName);
        LogHelper.getInstance().w("isSpecifiedFileType---end=" + end);


        Boolean result=end.equals(fileSuffix);
        LogHelper.getInstance().w("fileSuffix=" + fileSuffix+",result="+result);

        /* 依扩展名的类型决定MimeType */
        if(end.equals(fileSuffix))
        {
            return true;
        }
        return false;
    }
    /**
     * 在dir中查找是否有吻合的iFileType类型，若有，都一一添加到List<File>中
     * @param dir          被查找的目录
     * @param fileSuffix   想要匹配的文件后缀
     * @return 返回找的的已匹配的文件列表List<File>
     */
    public static List<File> getSpecifiedFileTypeList(String dir, String fileSuffix)
    {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(dir);
        //如同判断SD卡是否存在或者文件是否存�?
        //如果不存在则 return出去
        if(!root.exists())
        {
            return null;
        }
        //如果存在则获取当前目录下的全部文�? 填充数组
        currentFiles = root.listFiles();

        if(currentFiles.length <= 0)
            return null;

        int fileNums = currentFiles.length;

        List<File> fileList = new ArrayList<File>();
        //遍历要复制该目录下的全部文件
        for(int i= 0;i<fileNums;i++)
        {
            //旁路子目录
            if(currentFiles[i].isDirectory())//如果当前项为子目�? 进行递归
            {

            }
            else
            {
                //U盘root目录下的文件
                if(isSpecifiedFileType(currentFiles[i], fileSuffix))
                {
                    fileList.add(currentFiles[i]);
                }
            }

        }

        return fileList;
    }


    /**
     * <p>Description: 获取单个文件的MD5值<br>
     * @param filePath
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getFileMD5(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();

//		BigInteger bigInt = new BigInteger(1, digest.digest());
//		return bigInt.toString(16);
        return byte2HexStr(digest.digest());
    }

    /**
     * <p>Description: 实现ZIP的解压<br>
     * @param zipFile     源ZIP文件
     * @param folderPath  展开的目标文件夹路径
     * @throws ZipException
     * @throws IOException
     */
    public static void upZipFile(File zipFile, String folderPath) throws ZipException,
            IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
            try {
                zf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>Description: 实现gzip的解压<br>
     * @param sourceFilePath   源GZIP文件
     * @return
     */
    public static boolean unGzipFile(String sourceFilePath) {
        Log.e("调试", "开始");
        String outputfilePath = "";
        try {
            File sourceFile = new File(sourceFilePath);
            if(!sourceFile.exists()) { // 如果目标文件不存在
                Log.e("error", "*.gz not exist");
                return false;
            }
            outputfilePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf('.'));
            outputfilePath += ".bmp";
            File outputfile = new File(outputfilePath);
            if(outputfile.exists()) {
                if(!outputfile.delete()) {
                    Log.e("warning", "*.bmp delete fail, deletefilePath = " + outputfilePath);
                }
            }
            // 建立gzip压缩文件输入流
            FileInputStream fin = new FileInputStream(sourceFilePath);
            // 建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            // 建立解压文件输出流
            FileOutputStream fout = new FileOutputStream(outputfile);
            int num;
            byte[] buf = new byte[1024];
            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }
            gzin.close();
            fout.close();
            fin.close();
            Log.e("调试", "结束");
            return true;
        } catch (Exception ex) {
            Log.e("调试", "异常");
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * <p>Description: 实现gzip的压缩<br>
     * @param sourceFilePath   源文件
     * @return 返回压缩后文件路径
     * @throws IOException
     * @throws FileNotFoundException sourceFilePath指向的文件不存在
     */
    public static String gzipFile(String sourceFilePath) throws FileNotFoundException,
            IOException {
        byte[] buf = new byte[2048];
        BufferedInputStream in =
                new BufferedInputStream(new FileInputStream(sourceFilePath));
        String destFilePath = sourceFilePath + ".gz";
        try {
            BufferedOutputStream out =
                    new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(
                            destFilePath)));
            try {
                int c;
                while ((c = in.read(buf)) != -1) {
                    out.write(buf, 0, c);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
        return destFilePath;
    }


}
