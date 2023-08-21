package com.hsf.utilstest.packet.utils;


import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;

public class AntZipUtils {

    /**
     * 使用Apache的Ant解压方式
     * @param sourceFilePath 源压缩文件的路径
     * @param targetDirPath 解压的目标路径
     * @param deleteSource 解压成功后，是否删除源文件
     */
    public static void uncompressFile(String sourceFilePath, String targetDirPath, boolean deleteSource) throws Exception{
        BufferedInputStream bis;

        File sourceFile = new File(sourceFilePath);

        ZipFile zf = new ZipFile(sourceFile, "GBK");
        Enumeration entries = zf.getEntries();
        while (entries.hasMoreElements()){
            ZipEntry ze = (ZipEntry) entries.nextElement();
            String entryName = ze.getName();
            LogUtil.d("entryName:" + entryName + ", " + sourceFilePath + ",  " + targetDirPath);
            if(entryName.contains("../")){//2016-08-25
                throw new Exception("unsecurity zipfile");
            }else{
                String path = targetDirPath + entryName;
                LogUtil.d("AntZipUtils", "path="+path);
                if (ze.isDirectory()){
                    LogUtil.d("Creating a decompression directory - " + entryName);
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()){
                        decompressDirFile.mkdirs();
                    }
                } else{
                    LogUtil.d("Creating an extract file - " + entryName);
                    String fileDir = path.substring(0, path.lastIndexOf(File.separator));
                    LogUtil.d("fileDir="+fileDir);
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()){
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetDirPath + entryName));
                    bis = new BufferedInputStream(zf.getInputStream(ze));
                    byte[] readContent = new byte[1024];
                    int readCount = bis.read(readContent);
                    while (readCount != -1){
                        bos.write(readContent, 0, readCount);
                        readCount = bis.read(readContent);
                    }
                    bos.close();
                }
            }
        }
        zf.close();

        if (deleteSource && sourceFile.exists()) {
            sourceFile.delete();
            LogUtil.d("The source file has been deleted");
        }
    }
}
