/*    */ package winterwell.jtwitter.ecosystem;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
/*    */ import winterwell.json.JSONException;
/*    */ import winterwell.json.JSONObject;
/*    */ 
/*    */ public class TwitterCounterStats
/*    */ {
/*    */   public final String screenName;
/*    */   public final Date dateUpdated;
/*    */   public final int followDays;
/*    */   
/*    */   public String toString() {
/* 19 */     if (this.data.isEmpty()) {
/* 20 */       return "TwitterCounterStats[@" + this.screenName + " no data]";
/*    */     }
/* 22 */     Date s = ((DateValue)this.data.get(0)).date;
/* 23 */     Date e = ((DateValue)this.data.get(this.data.size() - 1)).date;
/* 24 */     return "TwitterCounterStats[@" + this.screenName + " " + this.data.size() + " pts from " + s + " to " + e + "]";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final double avgGrowth;
/*    */ 
/*    */   
/*    */   public final long rank;
/*    */   
/*    */   public final ArrayList<DateValue> data;
/*    */ 
/*    */   
/*    */   public static final class DateValue
/*    */     implements Comparable<DateValue>
/*    */   {
/*    */     public final int value;
/*    */     
/*    */     public final Date date;
/*    */ 
/*    */     
/*    */     DateValue(Date date, int v) {
/* 46 */       this.date = date;
/* 47 */       this.value = v;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 52 */       return this.date + ": " + this.value;
/*    */     }
/*    */ 
/*    */     
/*    */     public int compareTo(DateValue o) {
/* 57 */       return this.date.compareTo(o.date);
/*    */     }
/*    */   }
/*    */   
/* 61 */   static final SimpleDateFormat format = new SimpleDateFormat("'date'yyyy-MM-dd");
/* 62 */   static final SimpleDateFormat duformat = new SimpleDateFormat("yyyy-MM-dd");
/*    */   public final String website;
/*    */   
/*    */   TwitterCounterStats(JSONObject jo) throws JSONException, ParseException {
/* 66 */     this.screenName = jo.getString("username");
/* 67 */     this.dateUpdated = duformat.parse(jo.getString("date_updated"));
/* 68 */     this.followDays = jo.getInt("follow_days");
/* 69 */     this.avgGrowth = jo.getDouble("average_growth");
/* 70 */     this.website = jo.optString("url");
/* 71 */     this.rank = jo.getLong("rank");
/* 72 */     Map<String, ?> perdate = jo.getJSONObject("followersperdate").getMap();
/* 73 */     this.data = new ArrayList<DateValue>(perdate.size());
/* 74 */     for (String key : perdate.keySet()) {
/*    */       
/* 76 */       Date date = format.parse(key);
/* 77 */       int v = ((Integer)perdate.get(key)).intValue();
/* 78 */       this.data.add(new DateValue(date, v));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 84 */     Collections.sort(this.data);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\TwitterCounterStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */