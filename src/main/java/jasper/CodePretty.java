package jasper;

import com.alibaba.fastjson.JSONObject;
import de.hunsicker.jalopy.Jalopy;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CodePretty {
    private String code;
    private static String API_URL = "http://tool.oschina.net/action/format/java";

    public CodePretty(String code) {
        this.code = code;
    }

    public String pretty() {
        return sendPost(API_URL,code);
    }

    public static String pretty(String code) {
        return sendPost(API_URL,code);
    }

    public static String sendPost(String strURL,String JavaCode) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");


            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write("java="+JavaCode);
            out.flush();
            out.close();

            int code = connection.getResponseCode();
            InputStream is = null;
            if (code == 200) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }

            // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                JSONObject json = JSONObject.parseObject(result);
                result = json.getString("fjava");
                return result;
            }

        } catch (IOException e) {
            System.out.println("Exception occur when send http post request!");
            e.printStackTrace();
        }
        return "code Pretty error";
    }

    public static void pretty(File input,File output) {
        try {
            Jalopy j = new Jalopy();
            j.setEncoding("GBK");
            j.setInput(input);
            j.setOutput(output);
            j.format();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}