/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import com.winterwell.jgeoplanet.BoundingBox;
/*     */ import com.winterwell.jgeoplanet.IPlace;
/*     */ import com.winterwell.jgeoplanet.Location;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class Place
/*     */   implements IPlace, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private BoundingBox boundingBox;
/*     */   private String country;
/*     */   private String countryCode;
/*     */   private List<Location> geometry;
/*     */   private String id;
/*     */   private String name;
/*     */   private String type;
/*     */   private Place parent;
/*     */   
/*     */   public IPlace getParent() {
/*  36 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Place(JSONObject _place) throws JSONException {
/*  47 */     this.id = InternalUtils.jsonGet("id", _place);
/*  48 */     if (this.id == null) {
/*  49 */       this.id = InternalUtils.jsonGet("woeid", _place);
/*     */     }
/*     */ 
/*     */     
/*  53 */     this.type = InternalUtils.jsonGet("place_type", _place);
/*     */ 
/*     */     
/*  56 */     this.name = InternalUtils.jsonGet("full_name", _place);
/*  57 */     if (this.name == null) {
/*  58 */       this.name = InternalUtils.jsonGet("name", _place);
/*     */     }
/*  60 */     this.countryCode = InternalUtils.jsonGet("country_code", _place);
/*  61 */     this.country = InternalUtils.jsonGet("country", _place);
/*  62 */     Object _parent = _place.opt("contained_within");
/*     */     
/*  64 */     if (_parent instanceof JSONArray) {
/*  65 */       JSONArray pa = (JSONArray)_parent;
/*  66 */       _parent = (pa.length() == 0) ? null : pa.get(0);
/*     */     } 
/*  68 */     if (_parent != null) {
/*  69 */       this.parent = new Place((JSONObject)_parent);
/*     */     }
/*     */     
/*  72 */     Object bbox = _place.opt("bounding_box");
/*  73 */     if (bbox instanceof JSONObject) {
/*     */       
/*  75 */       List<Location> bb = parseCoords((JSONObject)bbox);
/*  76 */       double n = -90.0D, e = -180.0D, s = 90.0D, w = 180.0D;
/*  77 */       for (Location ll : bb) {
/*  78 */         n = Math.max(ll.latitude, n);
/*  79 */         s = Math.min(ll.latitude, s);
/*  80 */         e = Math.max(ll.longitude, e);
/*  81 */         w = Math.min(ll.longitude, w);
/*     */       } 
/*  83 */       this.boundingBox = new BoundingBox(new Location(n, e), new Location(s, w));
/*     */     } 
/*  85 */     Object geo = _place.opt("geometry");
/*  86 */     if (geo instanceof JSONObject) {
/*  87 */       this.geometry = parseCoords((JSONObject)geo);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/*  95 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public String getCountryCode() {
/*  99 */     return this.countryCode;
/*     */   }
/*     */   
/*     */   public String getCountryName() {
/* 103 */     return this.country;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Location> getGeometry() {
/* 110 */     return this.geometry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 117 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInfoUrl() {
/* 126 */     return "http://api.twitter.com/1/geo/id/" + this.id + ".json";
/*     */   }
/*     */   
/*     */   public String getName() {
/* 130 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 138 */     return this.type;
/*     */   }
/*     */   
/*     */   private List<Location> parseCoords(JSONObject bbox) throws JSONException {
/* 142 */     JSONArray coords = bbox.getJSONArray("coordinates");
/*     */     
/* 144 */     coords = coords.getJSONArray(0);
/* 145 */     List<Location> coordinates = new ArrayList<Location>();
/* 146 */     for (int i = 0, n = coords.length(); i < n; i++) {
/*     */       
/* 148 */       JSONArray pt = coords.getJSONArray(i);
/* 149 */       Location x = new Location(pt.getDouble(1), pt.getDouble(0));
/* 150 */       coordinates.add(x);
/*     */     } 
/* 152 */     return coordinates;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     return getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Location getCentroid() {
/* 162 */     if (this.boundingBox == null) return null; 
/* 163 */     return this.boundingBox.getCenter();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUID() {
/* 168 */     return String.valueOf(this.id) + "@twitter";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Place.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */