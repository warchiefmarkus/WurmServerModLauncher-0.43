/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import com.winterwell.jgeoplanet.IGeoCode;
/*     */ import com.winterwell.jgeoplanet.IPlace;
/*     */ import com.winterwell.jgeoplanet.MFloat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import winterwell.json.JSONArray;
/*     */ import winterwell.json.JSONException;
/*     */ import winterwell.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Twitter_Geo
/*     */   implements IGeoCode
/*     */ {
/*     */   private double accuracy;
/*     */   private final Twitter jtwit;
/*     */   
/*     */   Twitter_Geo(Twitter jtwit) {
/*  41 */     assert jtwit != null;
/*  42 */     this.jtwit = jtwit;
/*     */   }
/*     */   
/*     */   public List geoSearch(double latitude, double longitude) {
/*  46 */     throw new RuntimeException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Place> geoSearch(String query) {
/*  54 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/geo/search.json";
/*  55 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "query", query });
/*  56 */     if (this.accuracy != 0.0D) {
/*  57 */       vars.put("accuracy", String.valueOf(this.accuracy));
/*     */     }
/*  59 */     boolean auth = InternalUtils.authoriseIn11(this.jtwit);
/*  60 */     String json = this.jtwit.getHttpClient().getPage(url, vars, auth);
/*     */     try {
/*  62 */       JSONObject jo = new JSONObject(json);
/*  63 */       JSONObject jo2 = jo.getJSONObject("result");
/*  64 */       JSONArray arr = jo2.getJSONArray("places");
/*  65 */       List<Place> places = new ArrayList(arr.length());
/*  66 */       for (int i = 0; i < arr.length(); i++) {
/*  67 */         JSONObject _place = arr.getJSONObject(i);
/*     */ 
/*     */         
/*  70 */         Place place = new Place(_place);
/*  71 */         places.add(place);
/*     */       } 
/*  73 */       return places;
/*  74 */     } catch (JSONException e) {
/*  75 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List geoSearchByIP(String ipAddress) {
/*  80 */     throw new RuntimeException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Place> getTrendRegions() {
/*  89 */     String json = this.jtwit.getHttpClient().getPage(
/*  90 */         String.valueOf(this.jtwit.TWITTER_URL) + "/trends/available.json", null, false);
/*     */     try {
/*  92 */       JSONArray json2 = new JSONArray(json);
/*  93 */       List<Place> trends = new ArrayList<Place>();
/*  94 */       for (int i = 0; i < json2.length(); i++) {
/*  95 */         JSONObject ti = json2.getJSONObject(i);
/*  96 */         Place place = new Place(ti);
/*  97 */         trends.add(place);
/*     */       } 
/*  99 */       return trends;
/* 100 */     } catch (JSONException e) {
/* 101 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setAccuracy(double metres) {
/* 106 */     this.accuracy = metres;
/*     */   }
/*     */ 
/*     */   
/*     */   public IPlace getPlace(String locationDescription, MFloat confidence) {
/* 111 */     List<Place> places = geoSearch(locationDescription);
/* 112 */     if (places.size() == 0) return null;
/*     */     
/* 114 */     if (places.size() == 1) {
/* 115 */       if (confidence != null) confidence.value = 0.8F; 
/* 116 */       return places.get(0);
/*     */     } 
/* 118 */     return InternalUtils.prefer((List)places, "city", confidence, 0.8F);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Twitter_Geo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */