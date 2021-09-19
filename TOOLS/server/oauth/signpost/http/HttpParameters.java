/*     */ package oauth.signpost.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import oauth.signpost.OAuth;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpParameters
/*     */   implements Map<String, SortedSet<String>>, Serializable
/*     */ {
/*  43 */   private TreeMap<String, SortedSet<String>> wrappedMap = new TreeMap<String, SortedSet<String>>();
/*     */   
/*     */   public SortedSet<String> put(String key, SortedSet<String> value) {
/*  46 */     return this.wrappedMap.put(key, value);
/*     */   }
/*     */   
/*     */   public SortedSet<String> put(String key, SortedSet<String> values, boolean percentEncode) {
/*  50 */     if (percentEncode) {
/*  51 */       remove(key);
/*  52 */       for (String v : values) {
/*  53 */         put(key, v, true);
/*     */       }
/*  55 */       return get(key);
/*     */     } 
/*  57 */     return this.wrappedMap.put(key, values);
/*     */   }
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
/*     */   public String put(String key, String value) {
/*  72 */     return put(key, value, false);
/*     */   }
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
/*     */   public String put(String key, String value, boolean percentEncode) {
/*  90 */     key = percentEncode ? OAuth.percentEncode(key) : key;
/*  91 */     SortedSet<String> values = this.wrappedMap.get(key);
/*  92 */     if (values == null) {
/*  93 */       values = new TreeSet<String>();
/*  94 */       this.wrappedMap.put(key, values);
/*     */     } 
/*  96 */     if (value != null) {
/*  97 */       value = percentEncode ? OAuth.percentEncode(value) : value;
/*  98 */       values.add(value);
/*     */     } 
/*     */     
/* 101 */     return value;
/*     */   }
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
/*     */   public String putNull(String key, String nullString) {
/* 115 */     return put(key, nullString);
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends SortedSet<String>> m) {
/* 119 */     this.wrappedMap.putAll(m);
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends SortedSet<String>> m, boolean percentEncode) {
/* 123 */     if (percentEncode) {
/* 124 */       for (String key : m.keySet()) {
/* 125 */         put(key, m.get(key), true);
/*     */       }
/*     */     } else {
/* 128 */       this.wrappedMap.putAll(m);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void putAll(String[] keyValuePairs, boolean percentEncode) {
/* 133 */     for (int i = 0; i < keyValuePairs.length - 1; i += 2) {
/* 134 */       put(keyValuePairs[i], keyValuePairs[i + 1], percentEncode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putMap(Map<String, List<String>> m) {
/* 145 */     for (String key : m.keySet()) {
/* 146 */       SortedSet<String> vals = get(key);
/* 147 */       if (vals == null) {
/* 148 */         vals = new TreeSet<String>();
/* 149 */         put(key, vals);
/*     */       } 
/* 151 */       vals.addAll(m.get(key));
/*     */     } 
/*     */   }
/*     */   
/*     */   public SortedSet<String> get(Object key) {
/* 156 */     return this.wrappedMap.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFirst(Object key) {
/* 168 */     return getFirst(key, false);
/*     */   }
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
/*     */   public String getFirst(Object key, boolean percentDecode) {
/* 187 */     SortedSet<String> values = this.wrappedMap.get(key);
/* 188 */     if (values == null || values.isEmpty()) {
/* 189 */       return null;
/*     */     }
/* 191 */     String value = values.first();
/* 192 */     return percentDecode ? OAuth.percentDecode(value) : value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsQueryString(Object key) {
/* 204 */     return getAsQueryString(key, true);
/*     */   }
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
/*     */   public String getAsQueryString(Object key, boolean percentEncode) {
/* 223 */     StringBuilder sb = new StringBuilder();
/* 224 */     if (percentEncode)
/* 225 */       key = OAuth.percentEncode((String)key); 
/* 226 */     Set<String> values = this.wrappedMap.get(key);
/* 227 */     if (values == null) {
/* 228 */       return key + "=";
/*     */     }
/* 230 */     Iterator<String> iter = values.iterator();
/* 231 */     while (iter.hasNext()) {
/* 232 */       sb.append(key + "=" + (String)iter.next());
/* 233 */       if (iter.hasNext()) {
/* 234 */         sb.append("&");
/*     */       }
/*     */     } 
/* 237 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getAsHeaderElement(String key) {
/* 241 */     String value = getFirst(key);
/* 242 */     if (value == null) {
/* 243 */       return null;
/*     */     }
/* 245 */     return key + "=\"" + value + "\"";
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 249 */     return this.wrappedMap.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 253 */     for (Set<String> values : this.wrappedMap.values()) {
/* 254 */       if (values.contains(value)) {
/* 255 */         return true;
/*     */       }
/*     */     } 
/* 258 */     return false;
/*     */   }
/*     */   
/*     */   public int size() {
/* 262 */     int count = 0;
/* 263 */     for (String key : this.wrappedMap.keySet()) {
/* 264 */       count += ((SortedSet)this.wrappedMap.get(key)).size();
/*     */     }
/* 266 */     return count;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 270 */     return this.wrappedMap.isEmpty();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 274 */     this.wrappedMap.clear();
/*     */   }
/*     */   
/*     */   public SortedSet<String> remove(Object key) {
/* 278 */     return this.wrappedMap.remove(key);
/*     */   }
/*     */   
/*     */   public Set<String> keySet() {
/* 282 */     return this.wrappedMap.keySet();
/*     */   }
/*     */   
/*     */   public Collection<SortedSet<String>> values() {
/* 286 */     return this.wrappedMap.values();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<String, SortedSet<String>>> entrySet() {
/* 290 */     return this.wrappedMap.entrySet();
/*     */   }
/*     */   
/*     */   public HttpParameters getOAuthParameters() {
/* 294 */     HttpParameters oauthParams = new HttpParameters();
/*     */     
/* 296 */     for (Map.Entry<String, SortedSet<String>> param : entrySet()) {
/* 297 */       String key = param.getKey();
/* 298 */       if (key.startsWith("oauth_") || key.startsWith("x_oauth_")) {
/* 299 */         oauthParams.put(key, param.getValue());
/*     */       }
/*     */     } 
/*     */     
/* 303 */     return oauthParams;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\http\HttpParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */