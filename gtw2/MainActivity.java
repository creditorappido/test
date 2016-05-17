package com.wecash.likaituan.gtw2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

import android.app.Activity;
import android.view.KeyEvent;
import android.util.Log;
import android.content.res.AssetManager;

import java.io.File;
import java.util.Arrays;
import java.lang.String;

import org.json.JSONObject;

public class MainActivity extends Activity {
    private String clientPath;
    private String serverPath;
    private JSONObject clientJson;
    private JSONObject serverJson;
    private WebView webView;
    private File filesDir;

    //初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filesDir = getFilesDir();
        File w3Path = new File(filesDir + "/www/");
        //fs.deleteDir(w3Path);
        Log.i("files list:", Arrays.toString(fileList()));
        if(w3Path.isDirectory()){
            checkVersion();
        }else{
            try {
                AssetManager assetManager = getAssets();
                fs.copyAssetDirToFiles(assetManager, filesDir, "www");
                init();
            }catch (Exception ee){

            }
        }
    }

    //检查版本更新
    private void checkVersion(){
        clientPath = filesDir + "/www/bin/";
        String fileName = clientPath + "config.json";
        String code = fs.getCode(fileName);
        Log.i(fileName, code);
        try {
            clientJson = new JSONObject(code);
            serverPath = clientJson.getString("remotePath");
            new Thread(getVersionTask).start();
        }catch (Exception e){
            Log.e("config.js客户端JSON格式出错", e.toString());
        }
    }

    //获取版本
    Runnable getVersionTask = new Runnable() {
        public void run() {
            String url = serverPath + "config.json";
            String code = fs.getRemoteCode(url);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            data.putString("code", code);
            msg.setData(data);
            versionHandler.sendMessage(msg);
        }
    };

    //获取版本回调
    Handler versionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String url = data.getString("url");
            String code = data.getString("code");
            Log.i(url,code);
            try {
                serverJson = new JSONObject(code);
                if(clientJson.getString("version") != serverJson.getString("version")){
                    if(fs.writeFile(clientPath+"config.json", code)) {
                        new Thread(getBuildTask).start();
                    }
                }else {
                    init();
                }
            }catch (Exception e){
                Log.e("config.js服务端JSON解析出错", e.toString());
            }
        }

    };

    //获取包
    Runnable getBuildTask = new Runnable() {
        @Override
        public void run() {
            String url = serverPath + "build.js";
            String code = getRemote.sendGet(url,null);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            data.putString("code", code);
            msg.setData(data);
            buildHandler.sendMessage(msg);
        }
    };

    //获取包回调
    Handler buildHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String url = data.getString("url");
            String code = data.getString("code");
            Log.i(url,code);
            if(fs.writeFile(clientPath+"build.js", code)){
                init();
            }
        }
    };

    //webView初始化
    private void init(){
        String entryFile = "file://" + filesDir + "/www/index.html";
        //String entryFile = "file:///android_asset/www/index.html";
        Log.i("entryFile: ", entryFile);

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(entryFile);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    //改写物理按键——返回的逻辑
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                System.exit(0);//退出程序
            }
        }
        return false;//super.onKeyDown(keyCode, event);
    }


}