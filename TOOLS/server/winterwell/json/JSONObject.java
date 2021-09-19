/*      */ package winterwell.json;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONObject
/*      */ {
/*      */   private final HashMap myHashMap;
/*      */   
/*      */   private static final class Null
/*      */   {
/*      */     private Null() {}
/*      */     
/*      */     protected final Object clone() {
/*  104 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  116 */       return !(object != null && object != this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  126 */       return "null";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   public static final Object NULL = new Null(null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject() {
/*  150 */     this.myHashMap = new HashMap<Object, Object>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(JSONObject jo, String[] sa) throws JSONException {
/*  163 */     this();
/*  164 */     for (int i = 0; i < sa.length; i++) {
/*  165 */       putOpt(sa[i], jo.opt(sa[i]));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(JSONTokener x) throws JSONException {
/*  176 */     this();
/*      */ 
/*      */ 
/*      */     
/*  180 */     if (x.nextClean() != '{') {
/*  181 */       throw x.syntaxError("A JSONObject text must begin with '{'");
/*      */     }
/*      */     while (true) {
/*  184 */       char c = x.nextClean();
/*  185 */       switch (c) {
/*      */         case '\000':
/*  187 */           throw x.syntaxError("A JSONObject text must end with '}'");
/*      */         case '}':
/*      */           return;
/*      */       } 
/*  191 */       x.back();
/*  192 */       String key = x.nextValue().toString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  199 */       c = x.nextClean();
/*  200 */       if (c == '=') {
/*  201 */         if (x.next() != '>') {
/*  202 */           x.back();
/*      */         }
/*  204 */       } else if (c != ':') {
/*  205 */         throw x.syntaxError("Expected a ':' after a key");
/*      */       } 
/*  207 */       put(key, x.nextValue());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  213 */       switch (x.nextClean()) {
/*      */         case ',':
/*      */         case ';':
/*  216 */           if (x.nextClean() == '}') {
/*      */             return;
/*      */           }
/*  219 */           x.back(); continue;
/*      */         case '}':
/*      */           return;
/*      */       }  break;
/*      */     } 
/*  224 */     throw x.syntaxError("Expected a ',' or '}'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Map<?, ?> map) {
/*  236 */     this.myHashMap = (map == null) ? 
/*  237 */       new HashMap<Object, Object>() : 
/*  238 */       new HashMap<Object, Object>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Object object, String[] names) {
/*  254 */     this();
/*  255 */     Class<?> c = object.getClass();
/*  256 */     for (int i = 0; i < names.length; i++) {
/*      */       try {
/*  258 */         String name = names[i];
/*  259 */         Field field = c.getField(name);
/*  260 */         Object value = field.get(object);
/*  261 */         put(name, value);
/*  262 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(String string) throws JSONException {
/*  278 */     this(new JSONTokener(string));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject accumulate(String key, Object value) throws JSONException {
/*  296 */     testValidity(value);
/*  297 */     Object o = opt(key);
/*  298 */     if (o == null) {
/*  299 */       put(key, (value instanceof JSONArray) ? (
/*  300 */           new JSONArray()).put(value) : 
/*  301 */           value);
/*  302 */     } else if (o instanceof JSONArray) {
/*  303 */       ((JSONArray)o).put(value);
/*      */     } else {
/*  305 */       put(key, (new JSONArray()).put(o).put(value));
/*      */     } 
/*  307 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject append(String key, Object value) throws JSONException {
/*  324 */     testValidity(value);
/*  325 */     Object o = opt(key);
/*  326 */     if (o == null) {
/*  327 */       put(key, (new JSONArray()).put(value));
/*  328 */     } else if (o instanceof JSONArray) {
/*  329 */       put(key, ((JSONArray)o).put(value));
/*      */     } else {
/*  331 */       throw new JSONException("JSONObject[" + key + 
/*  332 */           "] is not a JSONArray. " + o.getClass());
/*      */     } 
/*  334 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String doubleToString(double d) {
/*  345 */     if (Double.isInfinite(d) || Double.isNaN(d)) {
/*  346 */       return "null";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  351 */     String s = Double.toString(d);
/*  352 */     if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
/*  353 */       while (s.endsWith("0")) {
/*  354 */         s = s.substring(0, s.length() - 1);
/*      */       }
/*  356 */       if (s.endsWith(".")) {
/*  357 */         s = s.substring(0, s.length() - 1);
/*      */       }
/*      */     } 
/*  360 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object get(String key) throws JSONException {
/*  372 */     Object o = opt(key);
/*  373 */     if (o == null) {
/*  374 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  375 */           "] not found.");
/*      */     }
/*  377 */     return o;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String key) throws JSONException {
/*  390 */     Object o = get(key);
/*  391 */     if (o.equals(Boolean.FALSE) || (
/*  392 */       o instanceof String && (
/*  393 */       (String)o).equalsIgnoreCase("false")))
/*  394 */       return false; 
/*  395 */     if (o.equals(Boolean.TRUE) || (
/*  396 */       o instanceof String && (
/*  397 */       (String)o).equalsIgnoreCase("true"))) {
/*  398 */       return true;
/*      */     }
/*  400 */     throw new JSONException("JSONObject[" + quote(key) + 
/*  401 */         "] is not a Boolean. " + o.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String key) throws JSONException {
/*  413 */     Object o = get(key);
/*      */     try {
/*  415 */       return (o instanceof Number) ? (
/*  416 */         (Number)o).doubleValue() : 
/*  417 */         Double.valueOf((String)o).doubleValue();
/*  418 */     } catch (Exception e) {
/*  419 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  420 */           "] is not a number. " + o.getClass());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String key) throws JSONException {
/*  435 */     Object o = get(key);
/*  436 */     return (o instanceof Number) ? (
/*  437 */       (Number)o).intValue() : (int)getDouble(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray getJSONArray(String key) throws JSONException {
/*  450 */     Object o = get(key);
/*  451 */     if (o instanceof JSONArray) {
/*  452 */       return (JSONArray)o;
/*      */     }
/*  454 */     throw new JSONException("JSONObject[" + quote(key) + 
/*  455 */         "] is not a JSONArray. " + o.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject getJSONObject(String key) throws JSONException {
/*  468 */     Object o = get(key);
/*  469 */     if (o instanceof JSONObject) {
/*  470 */       return (JSONObject)o;
/*      */     }
/*  472 */     throw new JSONException("JSONObject[" + quote(key) + 
/*  473 */         "] is not a JSONObject. " + o.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String key) throws JSONException {
/*  487 */     Object o = get(key);
/*  488 */     return (o instanceof Number) ? (
/*  489 */       (Number)o).longValue() : (long)getDouble(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String key) throws JSONException {
/*  501 */     return get(key).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean has(String key) {
/*  511 */     return this.myHashMap.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNull(String key) {
/*  523 */     return NULL.equals(opt(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator keys() {
/*  533 */     return this.myHashMap.keySet().iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  543 */     return this.myHashMap.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray names() {
/*  554 */     JSONArray ja = new JSONArray();
/*  555 */     Iterator keys = keys();
/*  556 */     while (keys.hasNext()) {
/*  557 */       ja.put(keys.next());
/*      */     }
/*  559 */     return (ja.length() == 0) ? null : ja;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String numberToString(Number n) throws JSONException {
/*  570 */     if (n == null) {
/*  571 */       throw new JSONException("Null pointer");
/*      */     }
/*  573 */     testValidity(n);
/*      */ 
/*      */ 
/*      */     
/*  577 */     String s = n.toString();
/*  578 */     if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
/*  579 */       while (s.endsWith("0")) {
/*  580 */         s = s.substring(0, s.length() - 1);
/*      */       }
/*  582 */       if (s.endsWith(".")) {
/*  583 */         s = s.substring(0, s.length() - 1);
/*      */       }
/*      */     } 
/*  586 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object opt(String key) {
/*  596 */     return (key == null) ? null : this.myHashMap.get(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean optBoolean(String key) {
/*  609 */     return optBoolean(key, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean optBoolean(String key, boolean defaultValue) {
/*      */     try {
/*  624 */       return getBoolean(key);
/*  625 */     } catch (Exception e) {
/*  626 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Collection value) throws JSONException {
/*  640 */     put(key, new JSONArray(value));
/*  641 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double optDouble(String key) {
/*  655 */     return optDouble(key, Double.NaN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double optDouble(String key, double defaultValue) {
/*      */     try {
/*  671 */       Object o = opt(key);
/*  672 */       return (o instanceof Number) ? ((Number)o).doubleValue() : (
/*  673 */         new Double((String)o)).doubleValue();
/*  674 */     } catch (Exception e) {
/*  675 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int optInt(String key) {
/*  690 */     return optInt(key, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int optInt(String key, int defaultValue) {
/*      */     try {
/*  706 */       return getInt(key);
/*  707 */     } catch (Exception e) {
/*  708 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray optJSONArray(String key) {
/*  722 */     Object o = opt(key);
/*  723 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject optJSONObject(String key) {
/*  736 */     Object o = opt(key);
/*  737 */     return (o instanceof JSONObject) ? (JSONObject)o : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long optLong(String key) {
/*  751 */     return optLong(key, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long optLong(String key, long defaultValue) {
/*      */     try {
/*  767 */       return getLong(key);
/*  768 */     } catch (Exception e) {
/*  769 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String optString(String key) {
/*  783 */     return optString(key, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, ?> getMap() {
/*  790 */     return this.myHashMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String optString(String key, String defaultValue) {
/*  802 */     Object o = opt(key);
/*  803 */     return (o != null) ? o.toString() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, boolean value) throws JSONException {
/*  816 */     put(key, value ? Boolean.TRUE : Boolean.FALSE);
/*  817 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, double value) throws JSONException {
/*  830 */     put(key, new Double(value));
/*  831 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, int value) throws JSONException {
/*  844 */     put(key, new Integer(value));
/*  845 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, long value) throws JSONException {
/*  858 */     put(key, new Long(value));
/*  859 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Map value) throws JSONException {
/*  872 */     put(key, new JSONObject(value));
/*  873 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Object value) throws JSONException {
/*  889 */     if (key == null) {
/*  890 */       throw new JSONException("Null key.");
/*      */     }
/*  892 */     if (value != null) {
/*  893 */       testValidity(value);
/*  894 */       this.myHashMap.put(key, value);
/*      */     } else {
/*  896 */       remove(key);
/*      */     } 
/*  898 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject putOpt(String key, Object value) throws JSONException {
/*  913 */     if (key != null && value != null) {
/*  914 */       put(key, value);
/*      */     }
/*  916 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String quote(String string) {
/*  929 */     if (string == null || string.length() == 0) {
/*  930 */       return "\"\"";
/*      */     }
/*      */ 
/*      */     
/*  934 */     char c = Character.MIN_VALUE;
/*      */     
/*  936 */     int len = string.length();
/*  937 */     StringBuffer sb = new StringBuffer(len + 4);
/*      */ 
/*      */     
/*  940 */     sb.append('"');
/*  941 */     for (int i = 0; i < len; i++) {
/*  942 */       char b = c;
/*  943 */       c = string.charAt(i);
/*  944 */       switch (c) {
/*      */         case '"':
/*      */         case '\\':
/*  947 */           sb.append('\\');
/*  948 */           sb.append(c);
/*      */           break;
/*      */         case '/':
/*  951 */           if (b == '<') {
/*  952 */             sb.append('\\');
/*      */           }
/*  954 */           sb.append(c);
/*      */           break;
/*      */         case '\b':
/*  957 */           sb.append("\\b");
/*      */           break;
/*      */         case '\t':
/*  960 */           sb.append("\\t");
/*      */           break;
/*      */         case '\n':
/*  963 */           sb.append("\\n");
/*      */           break;
/*      */         case '\f':
/*  966 */           sb.append("\\f");
/*      */           break;
/*      */         case '\r':
/*  969 */           sb.append("\\r");
/*      */           break;
/*      */         default:
/*  972 */           if (c < ' ' || (c >= '' && c < ' ') || (
/*  973 */             c >= ' ' && c < '℀')) {
/*  974 */             String t = "000" + Integer.toHexString(c);
/*  975 */             sb.append("\\u" + t.substring(t.length() - 4)); break;
/*      */           } 
/*  977 */           sb.append(c);
/*      */           break;
/*      */       } 
/*      */     } 
/*  981 */     sb.append('"');
/*  982 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object remove(String key) {
/*  992 */     return this.myHashMap.remove(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void testValidity(Object o) throws JSONException {
/* 1002 */     if (o != null) {
/* 1003 */       if (o instanceof Double) {
/* 1004 */         if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
/* 1005 */           throw new JSONException(
/* 1006 */               "JSON does not allow non-finite numbers.");
/*      */         }
/* 1008 */       } else if (o instanceof Float && ((
/* 1009 */         (Float)o).isInfinite() || ((Float)o).isNaN())) {
/* 1010 */         throw new JSONException(
/* 1011 */             "JSON does not allow non-finite numbers.");
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray toJSONArray(JSONArray names) throws JSONException {
/* 1027 */     if (names == null || names.length() == 0) {
/* 1028 */       return null;
/*      */     }
/* 1030 */     JSONArray ja = new JSONArray();
/* 1031 */     for (int i = 0; i < names.length(); i++) {
/* 1032 */       ja.put(opt(names.getString(i)));
/*      */     }
/* 1034 */     return ja;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*      */     try {
/* 1052 */       Iterator keys = keys();
/* 1053 */       StringBuffer sb = new StringBuffer("{");
/*      */       
/* 1055 */       while (keys.hasNext()) {
/* 1056 */         if (sb.length() > 1) {
/* 1057 */           sb.append(',');
/*      */         }
/* 1059 */         Object o = keys.next();
/* 1060 */         sb.append(quote(o.toString()));
/* 1061 */         sb.append(':');
/* 1062 */         sb.append(valueToString(this.myHashMap.get(o)));
/*      */       } 
/* 1064 */       sb.append('}');
/* 1065 */       return sb.toString();
/* 1066 */     } catch (Exception e) {
/* 1067 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(int indentFactor) throws JSONException {
/* 1085 */     return toString(indentFactor, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String toString(int indentFactor, int indent) throws JSONException {
/* 1104 */     int n = length();
/* 1105 */     if (n == 0) {
/* 1106 */       return "{}";
/*      */     }
/* 1108 */     Iterator keys = keys();
/* 1109 */     StringBuffer sb = new StringBuffer("{");
/* 1110 */     int newindent = indent + indentFactor;
/*      */     
/* 1112 */     if (n == 1) {
/* 1113 */       Object o = keys.next();
/* 1114 */       sb.append(quote(o.toString()));
/* 1115 */       sb.append(": ");
/* 1116 */       sb.append(valueToString(this.myHashMap.get(o), indentFactor, 
/* 1117 */             indent));
/*      */     } else {
/* 1119 */       while (keys.hasNext()) {
/* 1120 */         Object o = keys.next();
/* 1121 */         if (sb.length() > 1) {
/* 1122 */           sb.append(",\n");
/*      */         } else {
/* 1124 */           sb.append('\n');
/*      */         } 
/* 1126 */         for (int i = 0; i < newindent; i++) {
/* 1127 */           sb.append(' ');
/*      */         }
/* 1129 */         sb.append(quote(o.toString()));
/* 1130 */         sb.append(": ");
/* 1131 */         sb.append(valueToString(this.myHashMap.get(o), indentFactor, 
/* 1132 */               newindent));
/*      */       } 
/* 1134 */       if (sb.length() > 1) {
/* 1135 */         sb.append('\n');
/* 1136 */         for (int i = 0; i < indent; i++) {
/* 1137 */           sb.append(' ');
/*      */         }
/*      */       } 
/*      */     } 
/* 1141 */     sb.append('}');
/* 1142 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String valueToString(Object value) throws JSONException {
/* 1163 */     if (value == null || value.equals(null)) {
/* 1164 */       return "null";
/*      */     }
/* 1166 */     if (value instanceof JSONString) {
/*      */       Object o;
/*      */       try {
/* 1169 */         o = ((JSONString)value).toJSONString();
/* 1170 */       } catch (Exception e) {
/* 1171 */         throw new JSONException(e);
/*      */       } 
/* 1173 */       if (o instanceof String) {
/* 1174 */         return (String)o;
/*      */       }
/* 1176 */       throw new JSONException("Bad value from toJSONString: " + o);
/*      */     } 
/* 1178 */     if (value instanceof Number) {
/* 1179 */       return numberToString((Number)value);
/*      */     }
/* 1181 */     if (value instanceof Boolean || value instanceof JSONObject || 
/* 1182 */       value instanceof JSONArray) {
/* 1183 */       return value.toString();
/*      */     }
/* 1185 */     return quote(value.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String valueToString(Object value, int indentFactor, int indent) throws JSONException {
/* 1205 */     if (value == null || value.equals(null)) {
/* 1206 */       return "null";
/*      */     }
/*      */     try {
/* 1209 */       if (value instanceof JSONString) {
/* 1210 */         Object o = ((JSONString)value).toJSONString();
/* 1211 */         if (o instanceof String) {
/* 1212 */           return (String)o;
/*      */         }
/*      */       } 
/* 1215 */     } catch (Exception exception) {}
/*      */ 
/*      */     
/* 1218 */     if (value instanceof Number) {
/* 1219 */       return numberToString((Number)value);
/*      */     }
/* 1221 */     if (value instanceof Boolean) {
/* 1222 */       return value.toString();
/*      */     }
/* 1224 */     if (value instanceof JSONObject) {
/* 1225 */       return ((JSONObject)value).toString(indentFactor, indent);
/*      */     }
/* 1227 */     if (value instanceof JSONArray) {
/* 1228 */       return ((JSONArray)value).toString(indentFactor, indent);
/*      */     }
/* 1230 */     return quote(value.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Writer write(Writer writer) throws JSONException {
/*      */     try {
/* 1245 */       boolean b = false;
/* 1246 */       Iterator keys = keys();
/* 1247 */       writer.write(123);
/*      */       
/* 1249 */       while (keys.hasNext()) {
/* 1250 */         if (b) {
/* 1251 */           writer.write(44);
/*      */         }
/* 1253 */         Object k = keys.next();
/* 1254 */         writer.write(quote(k.toString()));
/* 1255 */         writer.write(58);
/* 1256 */         Object v = this.myHashMap.get(k);
/* 1257 */         if (v instanceof JSONObject) {
/* 1258 */           ((JSONObject)v).write(writer);
/* 1259 */         } else if (v instanceof JSONArray) {
/* 1260 */           ((JSONArray)v).write(writer);
/*      */         } else {
/* 1262 */           writer.write(valueToString(v));
/*      */         } 
/* 1264 */         b = true;
/*      */       } 
/* 1266 */       writer.write(125);
/* 1267 */       return writer;
/* 1268 */     } catch (IOException e) {
/* 1269 */       throw new JSONException(e);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\json\JSONObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */