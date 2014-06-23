// Name: 丁士宸
// Student ID: F74001014
import java.net.*;
import java.io.*;
import java.lang.Math;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.*;

public class TocHw4 {
    public static void main(String[] args) {
        int maxSpread = 0;
        URL jsonUrl = null;
        InputStream input = null;
        JSONTokener jtk = null;
        JSONArray jarr = null;
        int jarrLen = 0;
        LinkedList<String> roads = new LinkedList<String>();
        HashMap<String, RoadAttr> map = new HashMap<String, RoadAttr>();

        // check the number of argument
        if(args.length != 1){
            System.out.println("Usage: TowHw4 <URL>");
            System.exit(1);
        }

        try {
            // open the URL from the argument
            jsonUrl = new URL(args[0]);
            // open a stream from the url
            input = jsonUrl.openStream();
            // construct a JSONTokener
            jtk = new JSONTokener(input);
            // construct a JSONArray from JSONTokener
            jarr = new JSONArray(jtk);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid URL.");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("Fail to open stream.");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch(JSONException e) {
            System.out.println("JSONTokener can't read from stream.");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // get the number of object in the JSONArray
        jarrLen = jarr.length();

        try {
            // iterate through the json objects
            for(int i = 0; i < jarrLen; i++) {
                JSONObject jobj = jarr.getJSONObject(i);
                String addr = jobj.getString("土地區段位置或建物區門牌"); 
                String roadName = null;
                int lastIndex = -1;
                RoadAttr roadattr;

                lastIndex = Math.max(addr.lastIndexOf("大道"), lastIndex);
                if (lastIndex == -1)
                    lastIndex = Math.max(addr.lastIndexOf("路"), lastIndex);
                if (lastIndex == -1)
                    lastIndex = Math.max(addr.lastIndexOf("街"), lastIndex);
                if (lastIndex == -1)
                    lastIndex = Math.max(addr.lastIndexOf("巷"), lastIndex);

                // if a valid road name is found
                if (lastIndex != -1) {
                    roadName = addr.substring(0, lastIndex + 1);
                    // if it is a new road
                    if (!map.containsKey(roadName)) {
                        roads.add(roadName);
                        map.put(roadName, new RoadAttr());
                    }

                    roadattr = map.get(roadName);
                    roadattr.setAttr(jobj);
                    maxSpread = Math.max(maxSpread, roadattr.getYearMonthSize());
                }
            }
            // for each road in LinkedList roads
            for (String roadName : roads) {
                RoadAttr roadattr = map.get(roadName);
                // print the attribute of the road if the spread matches maxSpread
                if (roadattr.yearMonth.size() == maxSpread) {
                    System.out.print(roadName + ", ");
                    System.out.print("最高成交價: " + roadattr.getMaxPrice());
                    System.out.println(", 最低成交價: " + roadattr.getMinPrice());
                }
            }
        } 
        catch (JSONException e) {
            System.out.println("JSON error.");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
