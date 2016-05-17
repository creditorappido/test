/**
 * Created by likaituan on 16/5/17.
 * 二进制转文本代码
 */

package com.wecash.likaituan.gtw2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class b2c {

    //使用Scanner转码
    public static String getCodeByScanner(InputStream stream) {
        Scanner scanner = new Scanner(stream, "UTF-8");
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }

    //使用buffer转码
    public static String getCodeByReader(InputStream stream) {
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
        return sb.toString();
    }
}
