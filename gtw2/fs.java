/**
 * Created by likaituan on 16/5/17.
 * 文件操作方法集
 */

package com.wecash.likaituan.gtw2;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class fs {

    //写入文件
    public static boolean writeFile(String fileName, String code) {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(code);
            out.flush();
            out.close();

            File file = new File(fileName);
            Log.i(fileName,file.length()+"");
            return true;
        }catch (Exception e) {
            Log.e("写入文件出错",e.toString());
            return false;
        }
    }

    //复制资源文件夹
    public static void copyAssetDirToFiles(AssetManager assetManager, File filesDir, String dirname) throws IOException {
        File dir = new File(filesDir + "/" + dirname);
        dir.mkdir();

        //AssetManager assetManager = getAssets();
        String[] children = assetManager.list(dirname);
        for (String child : children) {
            child = dirname + '/' + child;
            String[] grandChildren = assetManager.list(child);
            if (0 == grandChildren.length) {
                copyAssetFileToFiles(assetManager, filesDir, child);
            } else {
                copyAssetDirToFiles(assetManager, filesDir, child);
            }
        }
    }

    //复制资源文件
    private static void copyAssetFileToFiles(AssetManager assetManager, File filesDir, String filename) throws IOException {
        InputStream is = assetManager.open(filename);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();

        File of = new File(filesDir + "/" + filename);
        of.createNewFile();
        FileOutputStream os = new FileOutputStream(of);
        os.write(buffer);
        os.close();
    }

    //获取文件代码(本地文件)
    public static String getCode(String fileName) {
        File file = new File(fileName);

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        }catch (Exception e){
            Log.e(fileName+"获取出错", e.toString());
        }

        return b2c.getCodeByReader(stream);
        /*

        InputStreamReader isReader = null;
        try {
            isReader = new InputStreamReader(stream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();*/
    }

    //获取远程文件代码
    public static String getRemoteCode(String remoteUrl) {
        InputStream stream = getRemoteStream(remoteUrl);
        return b2c.getCodeByReader(stream);
    }

    //获取远程文件流
    private static InputStream getRemoteStream(String remoteUrl) {
        InputStream stream = null;
        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.connect();
            stream = conn.getInputStream();
            int cLen = conn.getContentLength();
            //Log.i(remoteUrl+"长度", cLen+"");
            //conn.disconnect();
        }catch (Exception ee){
            Log.e(remoteUrl+"文件获取出错", ee.toString());
        }
        return stream;
    }

    //删除目录
    public static void deleteDir(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteDir(childFiles[i]);
            }
            file.delete();
        }
    }

    //从远程流写文件
    private void writeFileFromUrl(String url, String clientFile) {
        try {
            InputStream stream = getRemoteStream(url);
            FileOutputStream file = new FileOutputStream(clientFile);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = stream.read(b)) != -1) {
                file.write(b, 0, len);
            }
            stream.close();
            file.close();
        } catch (Exception e) {
            Log.e("解析build.js出错", e.toString());
        }
    }

}