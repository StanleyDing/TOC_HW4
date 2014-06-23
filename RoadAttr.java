import org.json.*;
import java.util.HashSet;
import java.lang.Math;

public class RoadAttr {
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
