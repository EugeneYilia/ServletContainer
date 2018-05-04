package config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import server.Constants;

import java.io.*;
import java.util.ArrayList;

import static server.Constants.CONFIG_LOCATION;
import static server.Constants.LOCALHOST;

public class ServletConfigContext {
    private static Jedis jedis = new Jedis(LOCALHOST);

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
            jedis.set("servletJsonArray", String.valueOf(jsonArray));
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

    private static void printAll(){
        for(int i=0;i<servletConfigArrayList.size();i++){
            System.out.println(servletConfigArrayList.get(i).getName());
            System.out.println(servletConfigArrayList.get(i).getClassName());
            System.out.println(servletConfigArrayList.get(i).getMappingUri());
        }
    }

    public static void main(String[] args) {
        ServletConfigContext servletConfigContext = new ServletConfigContext();
        servletConfigContext.init();
        System.out.println(jedis.get("servletJsonArray"));
        //servletConfigContext.printAll();
    }
}
