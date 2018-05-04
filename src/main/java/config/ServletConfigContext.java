package config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import server.Constants;

import java.io.*;
import java.util.ArrayList;

import static server.Constants.CONFIG_LOCATION;
import static server.Constants.LOCALHOST;

public class ServletConfigContext {

    private ServletConfigContext(){}
    private static ArrayList<ServletConfig> servletConfigArrayList = new ArrayList<>();

    public static ArrayList<ServletConfig> getServletConfigArrayList() {
        return servletConfigArrayList;
    }

    public static void init() {
        File file = new File(CONFIG_LOCATION);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader((file)));
            StringBuilder stringBuilder = new StringBuilder();
            String tempRecord;
            while ((tempRecord = bufferedReader.readLine()) != null) {
                stringBuilder.append(tempRecord);
            }
            String completeConfigContent = stringBuilder.toString();
            JSONObject jsonObject = JSON.parseObject(completeConfigContent);
            JSONArray jsonArray = (JSONArray) jsonObject.get("servlet");
            //System.out.println(jsonArray);
            for(int i=0;i<jsonArray.size();i++){
                ServletConfig servletConfig = new ServletConfig();
                servletConfig.setName(jsonArray.getJSONObject(i).getString("name"));
                servletConfig.setClassName(jsonArray.getJSONObject(i).getString("class"));
                servletConfig.setMappingUrl(jsonArray.getJSONObject(i).getString("mapping"));
                servletConfigArrayList.add(servletConfig);
            }
            JSONObject serverJson = jsonObject.getJSONObject("server");
            Constants.PORT = serverJson.getInteger("port");
            Constants.SERVLET_ROOT = serverJson.getString("servletPath");
            Constants.WEB_ROOT = serverJson.getString("staticPath");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
