package cn.tech.wings.cloud.message.core.util;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zhaoyun on 2016/8/31.
 */
public class Ntification {



    /**
     * @author zhaoyun
     * push Ntification to termina
     * @param content 内容
     * @param pushUserIds 用户ID
     * @param type 推送至（模块）
     *  1、商品详情 2、店铺 、3积分商城，4、活动， 5、网页,6、订单
     * @param value 对应的值
     */
    public static void send(String app_id, String content,String[] pushUserIds, String type, String value) {
    	
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NGEwMGZmMjItY2NkNy0xMWUzLTk5ZDUtMDAwYzI5NDBlNjJj");
            con.setRequestMethod("POST");
            String key = arrayToString(pushUserIds);

            String strJsonBody = "{"
                    +   "\"app_id\": \"" + app_id + "\","
                    +   "\"include_player_ids\": "+key+","
                    +   "\"data\": {\"type\":"+type+",\"value\":\""+value+"\",\"foo\": \"bar\"},"
                    +   "\"ios_badgeType\":\"Increase\","
                    +   "\"ios_badgeCount\":\"1\","
                    +   "\"contents\": {\"en\": \""+content+"\"}"
                    + "}";

            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * @author zhaoyun
     * push Ntification to termina
     * @param content
     */
    public static void pushAll(String app_id, String content) {
        try {
            String jsonResponse;
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NGEwMGZmMjItY2NkNy0xMWUzLTk5ZDUtMDAwYzI5NDBlNjJj");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"" + app_id + "\","
                    +   "\"included_segments\": [\"Active Users\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"ios_badgeType\":\"Increase\","
                    +   "\"ios_badgeCount\":\"1\","
                    +   "\"contents\": {\"en\": \""+content+"\"}"
                    + "}";

            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

   //获取字符串数组
    public static String[] getPushUserId(List<String> listStr){
    	 String ids[] = new String[listStr.size()];
    	 for (int i = 0; i < listStr.size(); i++) {
    		 ids[i] = listStr.get(i);
         }
		return ids;
    }
    
    public static void main(String[] args) {
       String ids[] = new String[]{"7da307a7-b6af-4c6f-9fbe-c02b138ac826"};
        send("46514ab1-d7dd-45ce-b0d8-f364a8f13ce6", "test app",ids,"3","1");
    }

    /**
     * 返回指定的字符串
     * @param arr
     * @return
     */
    public static String arrayToString(Object[] arr) {
        return arrayToString(arr, ",");
    }

    public static String arrayToString(Object[] arr, String delim) {
        if (arr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0)
                sb.append(delim);
            sb.append('"');
            sb.append(arr[i]);
            sb.append('"');
        }
        sb.insert(0,"[");
        sb.insert(sb.length(),"]");
        return sb.toString();
    }


}
