// Name: 丁士宸
// Student ID: F74001014
/*
 * Description:
 * This program reads real-price housing information from the given URL as input.
 * The output is the maximum/minimum price of a road which has maximum distinct 
 * month from the trading records.
 *
 * This program maintains a hashmap which maps road name to its attributes. The
 * attributes includes its maximum/minimum price and its distinct months. Distinct
 * months is stored as a set. The attribute is defined at RoadAttr.java.
 */
import java.net.*;
import java.io.*;
import java.lang.Math;
import java.util.HashMap;
import java.util.HashSet;
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

class RoadAttr {
    HashSet<Integer> yearMonth;
    int maxPrice;
    int minPrice;

    // constructor
    public RoadAttr()
    {
        yearMonth = new HashSet<Integer>();
        maxPrice = 0;
        minPrice = Integer.MAX_VALUE;
    }

    // set the attribute of the road
    public void setAttr(JSONObject jobj) throws JSONException
    {
        this.yearMonth.add(jobj.getInt("交易年月"));
        this.maxPrice = Math.max(this.maxPrice, jobj.getInt("總價元"));
        this.minPrice = Math.min(this.minPrice, jobj.getInt("總價元"));
    }

    // get the size of yearMonth set
    public int getYearMonthSize()
    {
        return this.yearMonth.size();
    }

    // get the value of maxPrice
    public int getMaxPrice()
    {
        return this.maxPrice;
    }
    
    // get the value of minPrice
    public int getMinPrice()
    {
        return this.minPrice;
    }
}
