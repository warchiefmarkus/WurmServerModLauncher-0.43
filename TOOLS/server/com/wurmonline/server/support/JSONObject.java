/*      */ package com.wurmonline.server.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
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
/*      */   private final Map map;
/*      */   
/*      */   private static final class Null
/*      */   {
/*      */     private Null() {}
/*      */     
/*      */     protected final Object clone() {
/*  138 */       return this;
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
/*      */     
/*      */     public boolean equals(Object object) {
/*  151 */       return (object == null || object == this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  161 */       return "null";
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
/*  174 */   public static final Object NULL = new Null();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject() {
/*  180 */     this.map = new HashMap<>();
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
/*      */   public JSONObject(JSONObject jo, String[] names) {
/*  198 */     this();
/*  199 */     for (int i = 0; i < names.length; i++) {
/*      */ 
/*      */       
/*      */       try {
/*  203 */         putOnce(names[i], jo.opt(names[i]));
/*      */       }
/*  205 */       catch (Exception exception) {}
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
/*      */   public JSONObject(JSONTokener x) throws JSONException {
/*  222 */     this();
/*      */     
/*      */     char c;
/*  225 */     if (x.nextClean() != '{')
/*      */     {
/*  227 */       throw x.syntaxError("A JSONObject text must begin with '{'");
/*      */     }
/*      */     
/*      */     while (true) {
/*  231 */       c = x.nextClean();
/*  232 */       switch (c) {
/*      */         
/*      */         case '\000':
/*  235 */           throw x.syntaxError("A JSONObject text must end with '{'");
/*      */         case '}':
/*      */           return;
/*      */       } 
/*  239 */       x.back();
/*  240 */       String key = x.nextValue().toString();
/*      */       
/*  242 */       c = x.nextClean();
/*  243 */       if (c != ':')
/*      */       {
/*  245 */         throw x.syntaxError("Expected a ':' after a key");
/*      */       }
/*      */       
/*  248 */       putOnce(key, x.nextValue());
/*      */       
/*  250 */       c = x.nextClean();
/*  251 */       switch (c) {
/*      */         
/*      */         case ',':
/*      */         case ';':
/*  255 */           if (x.nextClean() == '}') {
/*      */             return;
/*      */           }
/*      */           
/*  259 */           x.back(); continue;
/*      */         case '}':
/*      */           return;
/*      */       }  break;
/*      */     } 
/*  264 */     throw x.syntaxError("Expected a ',' or '}' got '" + c + "'");
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
/*      */   public JSONObject(Map aMap) {
/*  279 */     this.map = new HashMap<>();
/*  280 */     if (aMap != null) {
/*      */       
/*  282 */       Iterator<Map.Entry> i = aMap.entrySet().iterator();
/*  283 */       while (i.hasNext()) {
/*      */         
/*  285 */         Map.Entry e = i.next();
/*  286 */         Object value = e.getValue();
/*  287 */         if (value != null)
/*      */         {
/*  289 */           this.map.put(e.getKey(), wrap(value));
/*      */         }
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
/*      */   public JSONObject(Object bean) {
/*  318 */     this();
/*  319 */     populateMap(bean);
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
/*      */   public JSONObject(Object object, String[] names) {
/*  338 */     this();
/*  339 */     Class<?> c = object.getClass();
/*  340 */     for (int i = 0; i < names.length; i++) {
/*      */       
/*  342 */       String name = names[i];
/*      */       
/*      */       try {
/*  345 */         putOpt(name, c.getField(name).get(object));
/*      */       }
/*  347 */       catch (Exception exception) {}
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
/*      */ 
/*      */   
/*      */   public JSONObject(String source) throws JSONException {
/*  367 */     this(new JSONTokener(source));
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
/*      */   public JSONObject(String baseName, Locale locale) throws JSONException {
/*  382 */     this();
/*  383 */     ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, 
/*  384 */         Thread.currentThread().getContextClassLoader());
/*      */     
/*  386 */     Enumeration<String> keys = bundle.getKeys();
/*  387 */     while (keys.hasMoreElements()) {
/*      */       
/*  389 */       Object key = keys.nextElement();
/*  390 */       if (key instanceof String) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  395 */         String[] path = ((String)key).split("\\.");
/*  396 */         int last = path.length - 1;
/*  397 */         JSONObject target = this;
/*  398 */         for (int i = 0; i < last; i++) {
/*      */           
/*  400 */           String segment = path[i];
/*  401 */           JSONObject nextTarget = target.optJSONObject(segment);
/*  402 */           if (nextTarget == null) {
/*      */             
/*  404 */             nextTarget = new JSONObject();
/*  405 */             target.put(segment, nextTarget);
/*      */           } 
/*  407 */           target = nextTarget;
/*      */         } 
/*  409 */         target.put(path[last], bundle.getString((String)key));
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
/*  435 */     testValidity(value);
/*  436 */     Object object = opt(key);
/*  437 */     if (object == null) {
/*      */       
/*  439 */       put(key, (value instanceof JSONArray) ? (new JSONArray())
/*  440 */           .put(value) : value);
/*      */     
/*      */     }
/*  443 */     else if (object instanceof JSONArray) {
/*      */       
/*  445 */       ((JSONArray)object).put(value);
/*      */     }
/*      */     else {
/*      */       
/*  449 */       put(key, (new JSONArray()).put(object).put(value));
/*      */     } 
/*  451 */     return this;
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
/*      */   public JSONObject append(String key, Object value) throws JSONException {
/*  471 */     testValidity(value);
/*  472 */     Object object = opt(key);
/*  473 */     if (object == null) {
/*      */       
/*  475 */       put(key, (new JSONArray()).put(value));
/*      */     }
/*  477 */     else if (object instanceof JSONArray) {
/*      */       
/*  479 */       put(key, ((JSONArray)object).put(value));
/*      */     }
/*      */     else {
/*      */       
/*  483 */       throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
/*      */     } 
/*      */     
/*  486 */     return this;
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
/*      */   public static String doubleToString(double d) {
/*  499 */     if (Double.isInfinite(d) || Double.isNaN(d))
/*      */     {
/*  501 */       return "null";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  506 */     String string = Double.toString(d);
/*  507 */     if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
/*  508 */       .indexOf('E') < 0) {
/*      */       
/*  510 */       while (string.endsWith("0"))
/*      */       {
/*  512 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  514 */       if (string.endsWith("."))
/*      */       {
/*  516 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     } 
/*  519 */     return string;
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
/*      */   public Object get(String key) throws JSONException {
/*  533 */     if (key == null)
/*      */     {
/*  535 */       throw new JSONException("Null key.");
/*      */     }
/*  537 */     Object object = opt(key);
/*  538 */     if (object == null)
/*      */     {
/*  540 */       throw new JSONException("JSONObject[" + quote(key) + "] not found.");
/*      */     }
/*  542 */     return object;
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
/*      */   public boolean getBoolean(String key) throws JSONException {
/*  557 */     Object object = get(key);
/*  558 */     if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
/*      */       
/*  560 */       .equalsIgnoreCase("false")))
/*      */     {
/*  562 */       return false;
/*      */     }
/*  564 */     if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
/*      */       
/*  566 */       .equalsIgnoreCase("true")))
/*      */     {
/*  568 */       return true;
/*      */     }
/*  570 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a Boolean.");
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
/*      */   public double getDouble(String key) throws JSONException {
/*  586 */     Object object = get(key);
/*      */     
/*      */     try {
/*  589 */       return (object instanceof Number) ? ((Number)object).doubleValue() : 
/*  590 */         Double.parseDouble((String)object);
/*      */     }
/*  592 */     catch (Exception e) {
/*      */       
/*  594 */       throw new JSONException("JSONObject[" + quote(key) + "] is not a number.");
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
/*      */   public int getInt(String key) throws JSONException {
/*  611 */     Object object = get(key);
/*      */     
/*      */     try {
/*  614 */       return (object instanceof Number) ? ((Number)object).intValue() : 
/*  615 */         Integer.parseInt((String)object);
/*      */     }
/*  617 */     catch (Exception e) {
/*      */       
/*  619 */       throw new JSONException("JSONObject[" + quote(key) + "] is not an int.");
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
/*      */   public JSONArray getJSONArray(String key) throws JSONException {
/*  635 */     Object object = get(key);
/*  636 */     if (object instanceof JSONArray)
/*      */     {
/*  638 */       return (JSONArray)object;
/*      */     }
/*  640 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONArray.");
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
/*      */   public JSONObject getJSONObject(String key) throws JSONException {
/*  655 */     Object object = get(key);
/*  656 */     if (object instanceof JSONObject)
/*      */     {
/*  658 */       return (JSONObject)object;
/*      */     }
/*  660 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONObject.");
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
/*      */   public long getLong(String key) throws JSONException {
/*  676 */     Object object = get(key);
/*      */     
/*      */     try {
/*  679 */       return (object instanceof Number) ? ((Number)object).longValue() : 
/*  680 */         Long.parseLong((String)object);
/*      */     }
/*  682 */     catch (Exception e) {
/*      */       
/*  684 */       throw new JSONException("JSONObject[" + quote(key) + "] is not a long.");
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
/*      */   public static String[] getNames(JSONObject jo) {
/*  696 */     int length = jo.length();
/*  697 */     if (length == 0)
/*      */     {
/*  699 */       return null;
/*      */     }
/*  701 */     Iterator<String> iterator = jo.keys();
/*  702 */     String[] names = new String[length];
/*  703 */     int i = 0;
/*  704 */     while (iterator.hasNext()) {
/*      */       
/*  706 */       names[i] = iterator.next();
/*  707 */       i++;
/*      */     } 
/*  709 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getNames(Object object) {
/*  719 */     if (object == null)
/*      */     {
/*  721 */       return null;
/*      */     }
/*  723 */     Class<?> klass = object.getClass();
/*  724 */     Field[] fields = klass.getFields();
/*  725 */     int length = fields.length;
/*  726 */     if (length == 0)
/*      */     {
/*  728 */       return null;
/*      */     }
/*  730 */     String[] names = new String[length];
/*  731 */     for (int i = 0; i < length; i++)
/*      */     {
/*  733 */       names[i] = fields[i].getName();
/*      */     }
/*  735 */     return names;
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
/*      */   public String getString(String key) throws JSONException {
/*  749 */     Object object = get(key);
/*  750 */     if (object instanceof String)
/*      */     {
/*  752 */       return (String)object;
/*      */     }
/*  754 */     throw new JSONException("JSONObject[" + quote(key) + "] not a string.");
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
/*      */   public boolean has(String key) {
/*  766 */     return this.map.containsKey(key);
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
/*      */   public JSONObject increment(String key) throws JSONException {
/*  783 */     Object value = opt(key);
/*  784 */     if (value == null) {
/*      */       
/*  786 */       put(key, 1);
/*      */     }
/*  788 */     else if (value instanceof Integer) {
/*      */       
/*  790 */       put(key, ((Integer)value).intValue() + 1);
/*      */     }
/*  792 */     else if (value instanceof Long) {
/*      */       
/*  794 */       put(key, ((Long)value).longValue() + 1L);
/*      */     }
/*  796 */     else if (value instanceof Double) {
/*      */       
/*  798 */       put(key, ((Double)value).doubleValue() + 1.0D);
/*      */     }
/*  800 */     else if (value instanceof Float) {
/*      */       
/*  802 */       put(key, (((Float)value).floatValue() + 1.0F));
/*      */     }
/*      */     else {
/*      */       
/*  806 */       throw new JSONException("Unable to increment [" + quote(key) + "].");
/*      */     } 
/*  808 */     return this;
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
/*      */   public boolean isNull(String key) {
/*  822 */     return NULL.equals(opt(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator keys() {
/*  832 */     return keySet().iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set keySet() {
/*  842 */     return this.map.keySet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  852 */     return this.map.size();
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
/*      */   public JSONArray names() {
/*  864 */     JSONArray ja = new JSONArray();
/*  865 */     Iterator keys = keys();
/*  866 */     while (keys.hasNext())
/*      */     {
/*  868 */       ja.put(keys.next());
/*      */     }
/*  870 */     return (ja.length() == 0) ? null : ja;
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
/*      */   public static String numberToString(Number number) throws JSONException {
/*  884 */     if (number == null)
/*      */     {
/*  886 */       throw new JSONException("Null pointer");
/*      */     }
/*  888 */     testValidity(number);
/*      */ 
/*      */ 
/*      */     
/*  892 */     String string = number.toString();
/*  893 */     if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
/*  894 */       .indexOf('E') < 0) {
/*      */       
/*  896 */       while (string.endsWith("0"))
/*      */       {
/*  898 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  900 */       if (string.endsWith("."))
/*      */       {
/*  902 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     } 
/*  905 */     return string;
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
/*      */   public Object opt(String key) {
/*  917 */     return (key == null) ? null : this.map.get(key);
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
/*  930 */     return optBoolean(key, false);
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
/*      */   public boolean optBoolean(String key, boolean defaultValue) {
/*      */     try {
/*  948 */       return getBoolean(key);
/*      */     }
/*  950 */     catch (Exception e) {
/*      */       
/*  952 */       return defaultValue;
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
/*      */   public double optDouble(String key) {
/*  967 */     return optDouble(key, Double.NaN);
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
/*      */   public double optDouble(String key, double defaultValue) {
/*      */     try {
/*  985 */       return getDouble(key);
/*      */     }
/*  987 */     catch (Exception e) {
/*      */       
/*  989 */       return defaultValue;
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
/* 1004 */     return optInt(key, 0);
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
/*      */   public int optInt(String key, int defaultValue) {
/*      */     try {
/* 1022 */       return getInt(key);
/*      */     }
/* 1024 */     catch (Exception e) {
/*      */       
/* 1026 */       return defaultValue;
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
/* 1040 */     Object o = opt(key);
/* 1041 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
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
/* 1054 */     Object object = opt(key);
/* 1055 */     return (object instanceof JSONObject) ? (JSONObject)object : null;
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
/* 1069 */     return optLong(key, 0L);
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
/*      */   public long optLong(String key, long defaultValue) {
/*      */     try {
/* 1087 */       return getLong(key);
/*      */     }
/* 1089 */     catch (Exception e) {
/*      */       
/* 1091 */       return defaultValue;
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
/*      */   public String optString(String key) {
/* 1106 */     return optString(key, "");
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
/*      */   public String optString(String key, String defaultValue) {
/* 1121 */     Object object = opt(key);
/* 1122 */     return NULL.equals(object) ? defaultValue : object.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private void populateMap(Object bean) {
/* 1127 */     Class<?> klass = bean.getClass();
/*      */ 
/*      */ 
/*      */     
/* 1131 */     boolean includeSuperClass = (klass.getClassLoader() != null);
/*      */ 
/*      */     
/* 1134 */     Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
/* 1135 */     for (int i = 0; i < methods.length; i++) {
/*      */ 
/*      */       
/*      */       try {
/* 1139 */         Method method = methods[i];
/* 1140 */         if (Modifier.isPublic(method.getModifiers())) {
/*      */           
/* 1142 */           String name = method.getName();
/* 1143 */           String key = "";
/* 1144 */           if (name.startsWith("get")) {
/*      */             
/* 1146 */             if ("getClass".equals(name) || "getDeclaringClass"
/* 1147 */               .equals(name))
/*      */             {
/* 1149 */               key = "";
/*      */             }
/*      */             else
/*      */             {
/* 1153 */               key = name.substring(3);
/*      */             }
/*      */           
/* 1156 */           } else if (name.startsWith("is")) {
/*      */             
/* 1158 */             key = name.substring(2);
/*      */           } 
/* 1160 */           if (key.length() > 0 && 
/* 1161 */             Character.isUpperCase(key.charAt(0)) && (method
/* 1162 */             .getParameterTypes()).length == 0)
/*      */           {
/* 1164 */             if (key.length() == 1) {
/*      */               
/* 1166 */               key = key.toLowerCase();
/*      */             }
/* 1168 */             else if (!Character.isUpperCase(key.charAt(1))) {
/*      */ 
/*      */               
/* 1171 */               key = key.substring(0, 1).toLowerCase() + key.substring(1);
/*      */             } 
/*      */             
/* 1174 */             Object result = method.invoke(bean, (Object[])null);
/* 1175 */             if (result != null)
/*      */             {
/* 1177 */               this.map.put(key, wrap(result));
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/* 1182 */       } catch (Exception exception) {}
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
/*      */   
/*      */   public JSONObject put(String key, boolean value) throws JSONException {
/* 1201 */     put(key, value ? Boolean.TRUE : Boolean.FALSE);
/* 1202 */     return this;
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
/*      */   public JSONObject put(String key, Collection value) throws JSONException {
/* 1218 */     put(key, new JSONArray(value));
/* 1219 */     return this;
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
/*      */   public JSONObject put(String key, double value) throws JSONException {
/* 1235 */     put(key, new Double(value));
/* 1236 */     return this;
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
/*      */   public JSONObject put(String key, int value) throws JSONException {
/* 1252 */     put(key, new Integer(value));
/* 1253 */     return this;
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
/*      */   public JSONObject put(String key, long value) throws JSONException {
/* 1269 */     put(key, new Long(value));
/* 1270 */     return this;
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
/*      */   public JSONObject put(String key, Map value) throws JSONException {
/* 1286 */     put(key, new JSONObject(value));
/* 1287 */     return this;
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
/*      */   public JSONObject put(String key, Object value) throws JSONException {
/* 1306 */     if (key == null)
/*      */     {
/* 1308 */       throw new NullPointerException("Null key.");
/*      */     }
/* 1310 */     if (value != null) {
/*      */       
/* 1312 */       testValidity(value);
/* 1313 */       this.map.put(key, value);
/*      */     }
/*      */     else {
/*      */       
/* 1317 */       remove(key);
/*      */     } 
/* 1319 */     return this;
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
/*      */   public JSONObject putOnce(String key, Object value) throws JSONException {
/* 1335 */     if (key != null && value != null) {
/*      */       
/* 1337 */       if (opt(key) != null)
/*      */       {
/* 1339 */         throw new JSONException("Duplicate key \"" + key + "\"");
/*      */       }
/* 1341 */       put(key, value);
/*      */     } 
/* 1343 */     return this;
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
/*      */   public JSONObject putOpt(String key, Object value) throws JSONException {
/* 1362 */     if (key != null && value != null)
/*      */     {
/* 1364 */       put(key, value);
/*      */     }
/* 1366 */     return this;
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
/*      */   public static String quote(String string) {
/* 1381 */     StringWriter sw = new StringWriter();
/* 1382 */     synchronized (sw.getBuffer()) {
/*      */ 
/*      */ 
/*      */       
/* 1386 */       return quote(string, sw).toString();
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
/*      */   public static Writer quote(String string, Writer w) throws IOException {
/* 1398 */     if (string == null || string.length() == 0) {
/*      */       
/* 1400 */       w.write("\"\"");
/* 1401 */       return w;
/*      */     } 
/*      */ 
/*      */     
/* 1405 */     char c = Character.MIN_VALUE;
/*      */ 
/*      */     
/* 1408 */     int len = string.length();
/*      */     
/* 1410 */     w.write(34);
/* 1411 */     for (int i = 0; i < len; i++) {
/*      */       
/* 1413 */       char b = c;
/* 1414 */       c = string.charAt(i);
/* 1415 */       switch (c) {
/*      */         
/*      */         case '"':
/*      */         case '\\':
/* 1419 */           w.write(92);
/* 1420 */           w.write(c);
/*      */           break;
/*      */         case '/':
/* 1423 */           if (b == '<')
/*      */           {
/* 1425 */             w.write(92);
/*      */           }
/* 1427 */           w.write(c);
/*      */           break;
/*      */         case '\b':
/* 1430 */           w.write("\\b");
/*      */           break;
/*      */         case '\t':
/* 1433 */           w.write("\\t");
/*      */           break;
/*      */         case '\n':
/* 1436 */           w.write("\\n");
/*      */           break;
/*      */         case '\f':
/* 1439 */           w.write("\\f");
/*      */           break;
/*      */         case '\r':
/* 1442 */           w.write("\\r");
/*      */           break;
/*      */         default:
/* 1445 */           if (c < ' ' || (c >= '' && c < ' ') || (c >= ' ' && c < '℀')) {
/*      */ 
/*      */             
/* 1448 */             w.write("\\u");
/* 1449 */             String hhhh = Integer.toHexString(c);
/* 1450 */             w.write("0000", 0, 4 - hhhh.length());
/* 1451 */             w.write(hhhh);
/*      */             
/*      */             break;
/*      */           } 
/* 1455 */           w.write(c);
/*      */           break;
/*      */       } 
/*      */     } 
/* 1459 */     w.write(34);
/* 1460 */     return w;
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
/*      */   public Object remove(String key) {
/* 1473 */     return this.map.remove(key);
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
/*      */   public static Object stringToValue(String string) {
/* 1487 */     if (string.equals(""))
/*      */     {
/* 1489 */       return string;
/*      */     }
/* 1491 */     if (string.equalsIgnoreCase("true"))
/*      */     {
/* 1493 */       return Boolean.TRUE;
/*      */     }
/* 1495 */     if (string.equalsIgnoreCase("false"))
/*      */     {
/* 1497 */       return Boolean.FALSE;
/*      */     }
/* 1499 */     if (string.equalsIgnoreCase("null"))
/*      */     {
/* 1501 */       return NULL;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1509 */     char b = string.charAt(0);
/* 1510 */     if ((b >= '0' && b <= '9') || b == '-') {
/*      */       
/*      */       try {
/*      */         
/* 1514 */         if (string.indexOf('.') > -1 || string.indexOf('e') > -1 || string
/* 1515 */           .indexOf('E') > -1)
/*      */         {
/* 1517 */           Double d = Double.valueOf(string);
/* 1518 */           if (!d.isInfinite() && !d.isNaN())
/*      */           {
/* 1520 */             return d;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1525 */           Long myLong = new Long(string);
/* 1526 */           if (string.equals(myLong.toString()))
/*      */           {
/* 1528 */             if (myLong.longValue() == myLong.intValue())
/*      */             {
/* 1530 */               return new Integer(myLong.intValue());
/*      */             }
/*      */ 
/*      */             
/* 1534 */             return myLong;
/*      */           }
/*      */         
/*      */         }
/*      */       
/* 1539 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */     
/* 1543 */     return string;
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
/*      */   public static void testValidity(Object o) throws JSONException {
/* 1556 */     if (o != null)
/*      */     {
/* 1558 */       if (o instanceof Double) {
/*      */         
/* 1560 */         if (((Double)o).isInfinite() || ((Double)o).isNaN())
/*      */         {
/* 1562 */           throw new JSONException("JSON does not allow non-finite numbers.");
/*      */         
/*      */         }
/*      */       }
/* 1566 */       else if (o instanceof Float) {
/*      */         
/* 1568 */         if (((Float)o).isInfinite() || ((Float)o).isNaN())
/*      */         {
/* 1570 */           throw new JSONException("JSON does not allow non-finite numbers.");
/*      */         }
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
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray toJSONArray(JSONArray names) throws JSONException {
/* 1590 */     if (names == null || names.length() == 0)
/*      */     {
/* 1592 */       return null;
/*      */     }
/* 1594 */     JSONArray ja = new JSONArray();
/* 1595 */     for (int i = 0; i < names.length(); i++)
/*      */     {
/* 1597 */       ja.put(opt(names.getString(i)));
/*      */     }
/* 1599 */     return ja;
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
/*      */   public String toString() {
/*      */     try {
/* 1619 */       return toString(0);
/*      */     }
/* 1621 */     catch (Exception e) {
/*      */       
/* 1623 */       return null;
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
/*      */ 
/*      */   
/*      */   public String toString(int indentFactor) throws JSONException {
/* 1643 */     StringWriter w = new StringWriter();
/* 1644 */     synchronized (w.getBuffer()) {
/*      */       
/* 1646 */       return write(w, indentFactor, 0).toString();
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
/*      */   public static String valueToString(Object value) throws JSONException {
/* 1676 */     if (value == null || value.equals(null))
/*      */     {
/* 1678 */       return "null";
/*      */     }
/* 1680 */     if (value instanceof JSONString) {
/*      */       Object object;
/*      */ 
/*      */       
/*      */       try {
/* 1685 */         object = ((JSONString)value).toJSONString();
/*      */       }
/* 1687 */       catch (Exception e) {
/*      */         
/* 1689 */         throw new JSONException(e);
/*      */       } 
/* 1691 */       if (object instanceof String)
/*      */       {
/* 1693 */         return (String)object;
/*      */       }
/* 1695 */       throw new JSONException("Bad value from toJSONString: " + object);
/*      */     } 
/* 1697 */     if (value instanceof Number)
/*      */     {
/* 1699 */       return numberToString((Number)value);
/*      */     }
/* 1701 */     if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray)
/*      */     {
/*      */       
/* 1704 */       return value.toString();
/*      */     }
/* 1706 */     if (value instanceof Map)
/*      */     {
/* 1708 */       return (new JSONObject((Map)value)).toString();
/*      */     }
/* 1710 */     if (value instanceof Collection)
/*      */     {
/* 1712 */       return (new JSONArray((Collection)value)).toString();
/*      */     }
/* 1714 */     if (value.getClass().isArray())
/*      */     {
/* 1716 */       return (new JSONArray(value)).toString();
/*      */     }
/* 1718 */     return quote(value.toString());
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
/*      */   public static Object wrap(Object object) {
/*      */     try {
/* 1737 */       if (object == null)
/*      */       {
/* 1739 */         return NULL;
/*      */       }
/* 1741 */       if (object instanceof JSONObject || object instanceof JSONArray || NULL
/* 1742 */         .equals(object) || object instanceof JSONString || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1749 */         return object;
/*      */       }
/*      */       
/* 1752 */       if (object instanceof Collection)
/*      */       {
/* 1754 */         return new JSONArray((Collection)object);
/*      */       }
/* 1756 */       if (object.getClass().isArray())
/*      */       {
/* 1758 */         return new JSONArray(object);
/*      */       }
/* 1760 */       if (object instanceof Map)
/*      */       {
/* 1762 */         return new JSONObject((Map)object);
/*      */       }
/* 1764 */       Package objectPackage = object.getClass().getPackage();
/*      */       
/* 1766 */       String objectPackageName = (objectPackage != null) ? objectPackage.getName() : "";
/* 1767 */       if (objectPackageName.startsWith("java.") || objectPackageName
/* 1768 */         .startsWith("javax.") || object
/* 1769 */         .getClass().getClassLoader() == null)
/*      */       {
/* 1771 */         return object.toString();
/*      */       }
/* 1773 */       return new JSONObject(object);
/*      */     }
/* 1775 */     catch (Exception exception) {
/*      */       
/* 1777 */       return null;
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
/*      */   public Writer write(Writer writer) throws JSONException {
/* 1792 */     return write(writer, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
/* 1798 */     if (value == null || value.equals(null)) {
/*      */       
/* 1800 */       writer.write("null");
/*      */     }
/* 1802 */     else if (value instanceof JSONObject) {
/*      */       
/* 1804 */       ((JSONObject)value).write(writer, indentFactor, indent);
/*      */     }
/* 1806 */     else if (value instanceof JSONArray) {
/*      */       
/* 1808 */       ((JSONArray)value).write(writer, indentFactor, indent);
/*      */     }
/* 1810 */     else if (value instanceof Map) {
/*      */       
/* 1812 */       (new JSONObject((Map)value)).write(writer, indentFactor, indent);
/*      */     }
/* 1814 */     else if (value instanceof Collection) {
/*      */       
/* 1816 */       (new JSONArray((Collection)value)).write(writer, indentFactor, indent);
/*      */     
/*      */     }
/* 1819 */     else if (value.getClass().isArray()) {
/*      */       
/* 1821 */       (new JSONArray(value)).write(writer, indentFactor, indent);
/*      */     }
/* 1823 */     else if (value instanceof Number) {
/*      */       
/* 1825 */       writer.write(numberToString((Number)value));
/*      */     }
/* 1827 */     else if (value instanceof Boolean) {
/*      */       
/* 1829 */       writer.write(value.toString());
/*      */     }
/* 1831 */     else if (value instanceof JSONString) {
/*      */       Object o;
/*      */ 
/*      */       
/*      */       try {
/* 1836 */         o = ((JSONString)value).toJSONString();
/*      */       }
/* 1838 */       catch (Exception e) {
/*      */         
/* 1840 */         throw new JSONException(e);
/*      */       } 
/* 1842 */       writer.write((o != null) ? o.toString() : quote(value.toString()));
/*      */     }
/*      */     else {
/*      */       
/* 1846 */       quote(value.toString(), writer);
/*      */     } 
/* 1848 */     return writer;
/*      */   }
/*      */ 
/*      */   
/*      */   static final void indent(Writer writer, int indent) throws IOException {
/* 1853 */     for (int i = 0; i < indent; i++)
/*      */     {
/* 1855 */       writer.write(32);
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
/*      */   Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
/*      */     try {
/* 1873 */       boolean commanate = false;
/* 1874 */       int length = length();
/* 1875 */       Iterator keys = keys();
/* 1876 */       writer.write(123);
/*      */       
/* 1878 */       if (length == 1) {
/*      */         
/* 1880 */         Object key = keys.next();
/* 1881 */         writer.write(quote(key.toString()));
/* 1882 */         writer.write(58);
/* 1883 */         if (indentFactor > 0)
/*      */         {
/* 1885 */           writer.write(32);
/*      */         }
/* 1887 */         writeValue(writer, this.map.get(key), indentFactor, indent);
/*      */       }
/* 1889 */       else if (length != 0) {
/*      */         
/* 1891 */         int newindent = indent + indentFactor;
/* 1892 */         while (keys.hasNext()) {
/*      */           
/* 1894 */           Object key = keys.next();
/* 1895 */           if (commanate)
/*      */           {
/* 1897 */             writer.write(44);
/*      */           }
/* 1899 */           if (indentFactor > 0)
/*      */           {
/* 1901 */             writer.write(10);
/*      */           }
/* 1903 */           indent(writer, newindent);
/* 1904 */           writer.write(quote(key.toString()));
/* 1905 */           writer.write(58);
/* 1906 */           if (indentFactor > 0)
/*      */           {
/* 1908 */             writer.write(32);
/*      */           }
/* 1910 */           writeValue(writer, this.map.get(key), indentFactor, newindent);
/*      */           
/* 1912 */           commanate = true;
/*      */         } 
/* 1914 */         if (indentFactor > 0)
/*      */         {
/* 1916 */           writer.write(10);
/*      */         }
/* 1918 */         indent(writer, indent);
/*      */       } 
/* 1920 */       writer.write(125);
/* 1921 */       return writer;
/*      */     }
/* 1923 */     catch (IOException exception) {
/*      */       
/* 1925 */       throw new JSONException(exception);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\JSONObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */