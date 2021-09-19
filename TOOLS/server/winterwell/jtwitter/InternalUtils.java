/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import com.winterwell.jgeoplanet.IPlace;
/*     */ import com.winterwell.jgeoplanet.MFloat;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InternalUtils
/*     */ {
/*     */   public static <P extends IPlace> P prefer(List<P> places, String prefType, MFloat confidence, float baseConfidence) {
/*     */     List<IPlace> list1;
/*  65 */     assert places.size() != 0;
/*  66 */     assert baseConfidence >= 0.0F && baseConfidence <= 1.0F;
/*     */     
/*  68 */     List<IPlace> cities = new ArrayList();
/*  69 */     for (IPlace place : places) {
/*  70 */       if (prefType.equals(place.getType())) {
/*  71 */         cities.add(place);
/*     */       }
/*     */     } 
/*  74 */     if (cities.size() != 0 && cities.size() != places.size())
/*  75 */     { if (confidence != null) {
/*  76 */         float conf = 0.95F * baseConfidence / cities.size();
/*  77 */         confidence.value = conf;
/*     */       } 
/*  79 */       list1 = cities; }
/*     */     
/*  81 */     else if (confidence != null) { confidence.set(baseConfidence / list1.size()); }
/*     */ 
/*     */     
/*  84 */     return (P)list1.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String stripUrls(String text) {
/*  89 */     return Regex.VALID_URL.matcher(text).replaceAll("");
/*     */   }
/*     */   
/*  92 */   public static final Pattern TAG_REGEX = Pattern.compile("<!?/?[\\[\\-a-zA-Z][^>]*>", 32);
/*     */   
/*  94 */   static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   static final DateFormat dfMarko = new SimpleDateFormat(
/* 101 */       "EEE MMM dd HH:mm:ss ZZZZZ yyyy");
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
/* 112 */   public static final Pattern latLongLocn = Pattern.compile("(\\S+:)?\\s*(-?[\\d\\.]+)\\s*,\\s*(-?[\\d\\.]+)");
/*     */   
/* 114 */   static final Comparator<Status> NEWEST_FIRST = new Comparator<Status>()
/*     */     {
/*     */       public int compare(Status o1, Status o2) {
/* 117 */         return -o1.id.compareTo(o2.id);
/*     */       }
/*     */     };
/*     */   
/* 121 */   public static final Pattern REGEX_JUST_DIGITS = Pattern.compile("\\d+");
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
/* 132 */   static final Pattern URL_REGEX = Pattern.compile("[hf]tt?ps?://[a-zA-Z0-9_%\\-\\.,\\?&\\/=\\+'~#!\\*:]+[a-zA-Z0-9_%\\-&\\/=\\+]");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ConcurrentHashMap<String, Long> usage;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map asMap(Object... keyValuePairs) {
/* 143 */     assert keyValuePairs.length % 2 == 0;
/* 144 */     Map<Object, Object> m = new HashMap<Object, Object>(keyValuePairs.length / 2);
/* 145 */     for (int i = 0; i < keyValuePairs.length; i += 2) {
/* 146 */       Object v = keyValuePairs[i + 1];
/* 147 */       if (v != null)
/*     */       {
/*     */         
/* 150 */         m.put(keyValuePairs[i], v); } 
/*     */     } 
/* 152 */     return m;
/*     */   }
/*     */   
/*     */   public static void close(OutputStream output) {
/* 156 */     if (output == null) {
/*     */       return;
/*     */     }
/*     */     
/* 160 */     try { output.flush(); }
/* 161 */     catch (Exception exception)
/*     */     
/*     */     { 
/*     */       try {
/*     */         
/* 166 */         output.close();
/* 167 */       } catch (IOException iOException) {} } finally { try { output.close(); } catch (IOException iOException) {} }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void close(InputStream input) {
/* 174 */     if (input == null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 178 */       input.close();
/* 179 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void count(String url) {
/* 190 */     if (usage == null) {
/*     */       return;
/*     */     }
/* 193 */     int i = url.indexOf("?");
/* 194 */     if (i != -1) {
/* 195 */       url = url.substring(0, i);
/*     */     }
/*     */     
/* 198 */     i = url.indexOf("/1/");
/* 199 */     if (i != -1) {
/* 200 */       url = url.substring(i + 3);
/*     */     }
/*     */     
/* 203 */     url = url.replaceAll("\\d+", "");
/*     */     
/* 205 */     for (int j = 0; j < 100; j++) {
/* 206 */       boolean done; Long v = usage.get(url);
/*     */       
/* 208 */       if (v == null) {
/* 209 */         Long old = usage.putIfAbsent(url, Long.valueOf(1L));
/* 210 */         done = (old == null);
/*     */       } else {
/* 212 */         long nv = v.longValue() + 1L;
/* 213 */         done = usage.replace(url, v, Long.valueOf(nv));
/*     */       } 
/* 215 */       if (done) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static String encode(Object x) {
/*     */     try {
/* 224 */       encd = URLEncoder.encode(String.valueOf(x), "UTF-8");
/* 225 */     } catch (UnsupportedEncodingException e) {
/*     */       
/* 227 */       encd = URLEncoder.encode(String.valueOf(x));
/*     */     } 
/*     */     
/* 230 */     String encd = encd.replace("*", "%2A");
/* 231 */     return encd.replace("+", "%20");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConcurrentHashMap<String, Long> getAPIUsageStats() {
/* 241 */     return usage;
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
/*     */   public static Date getDate(int year, String month, int day) {
/*     */     try {
/* 255 */       Field field = GregorianCalendar.class.getField(month.toUpperCase());
/* 256 */       int m = field.getInt(null);
/* 257 */       Calendar date = new GregorianCalendar(year, m, day);
/* 258 */       return date.getTime();
/* 259 */     } catch (Exception x) {
/* 260 */       throw new IllegalArgumentException(x.getMessage());
/*     */     } 
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
/*     */   static Boolean getOptBoolean(JSONObject obj, String key) throws JSONException {
/* 273 */     Object o = obj.opt(key);
/* 274 */     if (o == null || o.equals(JSONObject.NULL))
/* 275 */       return null; 
/* 276 */     if (o instanceof Boolean) {
/* 277 */       return (Boolean)o;
/*     */     }
/* 279 */     if (o instanceof String) {
/* 280 */       String os = (String)o;
/* 281 */       if (os.equalsIgnoreCase("true")) return Boolean.valueOf(true); 
/* 282 */       if (os.equalsIgnoreCase("false")) return Boolean.valueOf(false);
/*     */     
/*     */     } 
/* 285 */     if (o instanceof Integer) {
/* 286 */       int oi = ((Integer)o).intValue();
/* 287 */       if (oi == 1) return Boolean.valueOf(true); 
/* 288 */       if (oi == 0 || oi == -1) return Boolean.valueOf(false); 
/*     */     } 
/* 290 */     System.err.println("JSON parse fail: " + o + " (" + key + ") is not boolean");
/* 291 */     return null;
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
/*     */   static String join(List screenNamesOrIds, int first, int last) {
/* 305 */     StringBuilder names = new StringBuilder();
/* 306 */     for (int si = first, n = Math.min(last, screenNamesOrIds.size()); si < n; si++) {
/* 307 */       names.append(screenNamesOrIds.get(si));
/* 308 */       names.append(",");
/*     */     } 
/*     */     
/* 311 */     if (names.length() != 0) {
/* 312 */       names.delete(names.length() - 1, names.length());
/*     */     }
/* 314 */     return names.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(String[] screenNames) {
/* 324 */     StringBuilder names = new StringBuilder();
/* 325 */     for (int si = 0, n = screenNames.length; si < n; si++) {
/* 326 */       names.append(screenNames[si]);
/* 327 */       names.append(",");
/*     */     } 
/*     */     
/* 330 */     if (names.length() != 0) {
/* 331 */       names.delete(names.length() - 1, names.length());
/*     */     }
/* 333 */     return names.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String jsonGet(String key, JSONObject jsonObj) {
/* 342 */     assert key != null : jsonObj;
/* 343 */     assert jsonObj != null;
/* 344 */     Object val = jsonObj.opt(key);
/* 345 */     if (val == null)
/* 346 */       return null; 
/* 347 */     if (JSONObject.NULL.equals(val))
/* 348 */       return null; 
/* 349 */     String s = val.toString();
/* 350 */     return s;
/*     */   }
/*     */   
/*     */   static Date parseDate(String c) {
/* 354 */     if (REGEX_JUST_DIGITS.matcher(c).matches())
/* 355 */       return new Date(Long.valueOf(c).longValue()); 
/*     */     try {
/* 357 */       Date _createdAt = new Date(c);
/* 358 */       return _createdAt;
/* 359 */     } catch (Exception e) {
/*     */       
/*     */       try {
/* 362 */         Date _createdAt = dfMarko.parse(c);
/* 363 */         return _createdAt;
/* 364 */       } catch (ParseException e1) {
/* 365 */         throw new TwitterException.Parsing(c, e1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTrackAPIUsage(boolean on) {
/* 377 */     if (!on) {
/* 378 */       usage = null;
/*     */       return;
/*     */     } 
/* 381 */     if (usage != null)
/*     */       return; 
/* 383 */     usage = new ConcurrentHashMap<String, Long>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 389 */   private static final Charset UTF_8 = Charset.forName("UTF-8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String read(InputStream inputStream) {
/*     */     try {
/* 397 */       Reader reader = new InputStreamReader(inputStream, UTF_8);
/* 398 */       reader = new BufferedReader(reader);
/* 399 */       StringBuilder output = new StringBuilder();
/*     */       while (true) {
/* 401 */         int c = reader.read();
/* 402 */         if (c == -1) {
/*     */           break;
/*     */         }
/* 405 */         output.append((char)c);
/*     */       } 
/* 407 */       return output.toString();
/* 408 */     } catch (IOException e) {
/* 409 */       throw new RuntimeException(e);
/*     */     } finally {
/* 411 */       close(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String unencode(String text) {
/* 423 */     if (text == null) {
/* 424 */       return null;
/*     */     }
/* 426 */     text = text.replace("&quot;", "\"");
/* 427 */     text = text.replace("&apos;", "'");
/* 428 */     text = text.replace("&nbsp;", " ");
/* 429 */     text = text.replace("&amp;", "&");
/* 430 */     text = text.replace("&gt;", ">");
/* 431 */     text = text.replace("&lt;", "<");
/*     */     
/* 433 */     if (text.indexOf(false) != -1) {
/* 434 */       text = text.replace(false, ' ').trim();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 439 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static URI URI(String uri) {
/*     */     try {
/* 447 */       return new URI(uri);
/* 448 */     } catch (URISyntaxException e) {
/* 449 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   static User user(String json) {
/*     */     try {
/* 455 */       JSONObject obj = new JSONObject(json);
/* 456 */       User u = new User(obj, null);
/* 457 */       return u;
/* 458 */     } catch (JSONException e) {
/* 459 */       throw new TwitterException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String stripTags(String xml) {
/* 470 */     if (xml == null) return null;
/*     */     
/* 472 */     if (xml.indexOf('<') == -1) return xml;
/*     */     
/* 474 */     Matcher m4 = pScriptOrStyle.matcher(xml);
/* 475 */     xml = m4.replaceAll("");
/*     */     
/* 477 */     Matcher m2 = pComment.matcher(xml);
/* 478 */     String txt = m2.replaceAll("");
/*     */     
/* 480 */     Matcher m = TAG_REGEX.matcher(txt);
/* 481 */     String txt2 = m.replaceAll("");
/* 482 */     Matcher m3 = pDocType.matcher(txt2);
/* 483 */     String txt3 = m3.replaceAll("");
/* 484 */     return txt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 491 */   public static final Pattern pComment = Pattern.compile("<!-*.*?-+>", 32);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 496 */   public static final Pattern pScriptOrStyle = Pattern.compile("<(script|style)[^<>]*>.+?</(script|style)>", 34);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 501 */   public static final Pattern pDocType = Pattern.compile("<!DOCTYPE.*?>", 34);
/*     */   
/*     */   public static void sleep(long msecs) {
/*     */     try {
/* 505 */       Thread.sleep(msecs);
/* 506 */     } catch (InterruptedException e) {
/* 507 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean authoriseIn11(Twitter jtwit) {
/* 518 */     return !(!jtwit.getHttpClient().canAuthenticate() && 
/* 519 */       !jtwit.TWITTER_URL.endsWith("1.1"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigInteger getMinId(BigInteger maxId, List<? extends Twitter.ITweet> stati) {
/* 530 */     BigInteger min = maxId;
/* 531 */     for (Twitter.ITweet s : stati) {
/* 532 */       if (min == null || min.compareTo(s.getId()) > 0) {
/* 533 */         min = s.getId();
/*     */       }
/*     */     } 
/*     */     
/* 537 */     if (min != null) min = min.subtract(BigInteger.ONE); 
/* 538 */     return min;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\InternalUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */