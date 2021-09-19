/*      */ package com.wurmonline.server.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONArray
/*      */ {
/*      */   private final List myArrayList;
/*      */   
/*      */   public JSONArray() {
/*  118 */     this.myArrayList = new ArrayList();
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
/*      */   public JSONArray(JSONTokener x) throws JSONException {
/*  131 */     this();
/*  132 */     if (x.nextClean() != '[')
/*      */     {
/*  134 */       throw x.syntaxError("A JSONArray text must start with '['");
/*      */     }
/*  136 */     if (x.nextClean() != ']') {
/*      */       
/*  138 */       x.back();
/*      */       
/*      */       while (true) {
/*  141 */         if (x.nextClean() == ',') {
/*      */           
/*  143 */           x.back();
/*  144 */           this.myArrayList.add(JSONObject.NULL);
/*      */         }
/*      */         else {
/*      */           
/*  148 */           x.back();
/*  149 */           this.myArrayList.add(x.nextValue());
/*      */         } 
/*  151 */         switch (x.nextClean()) {
/*      */           
/*      */           case ',':
/*  154 */             if (x.nextClean() == ']') {
/*      */               return;
/*      */             }
/*      */             
/*  158 */             x.back(); continue;
/*      */           case ']':
/*      */             return;
/*      */         }  break;
/*      */       } 
/*  163 */       throw x.syntaxError("Expected a ',' or ']'");
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
/*      */   public JSONArray(String source) throws JSONException {
/*  181 */     this(new JSONTokener(source));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray(Collection collection) {
/*  192 */     this.myArrayList = new ArrayList();
/*  193 */     if (collection != null) {
/*      */       
/*  195 */       Iterator iter = collection.iterator();
/*  196 */       while (iter.hasNext())
/*      */       {
/*  198 */         this.myArrayList.add(JSONObject.wrap(iter.next()));
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
/*      */   public JSONArray(Object array) throws JSONException {
/*  211 */     this();
/*  212 */     if (array.getClass().isArray()) {
/*      */       
/*  214 */       int length = Array.getLength(array);
/*  215 */       for (int i = 0; i < length; i++)
/*      */       {
/*  217 */         put(JSONObject.wrap(Array.get(array, i)));
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  222 */       throw new JSONException("JSONArray initial value should be a string or collection or array.");
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
/*      */   public Object get(int index) throws JSONException {
/*  238 */     Object object = opt(index);
/*  239 */     if (object == null)
/*      */     {
/*  241 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/*  243 */     return object;
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
/*      */   public boolean getBoolean(int index) throws JSONException {
/*  259 */     Object object = get(index);
/*  260 */     if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
/*      */       
/*  262 */       .equalsIgnoreCase("false")))
/*      */     {
/*  264 */       return false;
/*      */     }
/*  266 */     if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
/*      */       
/*  268 */       .equalsIgnoreCase("true")))
/*      */     {
/*  270 */       return true;
/*      */     }
/*  272 */     throw new JSONException("JSONArray[" + index + "] is not a boolean.");
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
/*      */   public double getDouble(int index) throws JSONException {
/*  287 */     Object object = get(index);
/*      */     
/*      */     try {
/*  290 */       return (object instanceof Number) ? ((Number)object).doubleValue() : 
/*  291 */         Double.parseDouble((String)object);
/*      */     }
/*  293 */     catch (Exception e) {
/*      */       
/*  295 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
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
/*      */   public int getInt(int index) throws JSONException {
/*  310 */     Object object = get(index);
/*      */     
/*      */     try {
/*  313 */       return (object instanceof Number) ? ((Number)object).intValue() : 
/*  314 */         Integer.parseInt((String)object);
/*      */     }
/*  316 */     catch (Exception e) {
/*      */       
/*  318 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
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
/*      */   public JSONArray getJSONArray(int index) throws JSONException {
/*  334 */     Object object = get(index);
/*  335 */     if (object instanceof JSONArray)
/*      */     {
/*  337 */       return (JSONArray)object;
/*      */     }
/*  339 */     throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
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
/*      */   public JSONObject getJSONObject(int index) throws JSONException {
/*  354 */     Object object = get(index);
/*  355 */     if (object instanceof JSONObject)
/*      */     {
/*  357 */       return (JSONObject)object;
/*      */     }
/*  359 */     throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
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
/*      */   public JSONObject getJSONObject(String key) throws JSONException {
/*  375 */     for (int i = 0; i < this.myArrayList.size(); i++) {
/*      */       
/*  377 */       Object object = get(i);
/*  378 */       if (object instanceof JSONObject) {
/*      */         
/*  380 */         JSONObject jo = (JSONObject)object;
/*  381 */         if (jo.has("name"))
/*      */         {
/*  383 */           if (jo.getString("name").equalsIgnoreCase(key))
/*      */           {
/*  385 */             return jo;
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*  390 */     throw new JSONException("JSONObject " + key + " not found.");
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
/*      */   public long getLong(int index) throws JSONException {
/*  405 */     Object object = get(index);
/*      */     
/*      */     try {
/*  408 */       return (object instanceof Number) ? ((Number)object).longValue() : 
/*  409 */         Long.parseLong((String)object);
/*      */     }
/*  411 */     catch (Exception e) {
/*      */       
/*  413 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
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
/*      */   public String getString(int index) throws JSONException {
/*  428 */     Object object = get(index);
/*  429 */     if (object instanceof String)
/*      */     {
/*  431 */       return (String)object;
/*      */     }
/*  433 */     throw new JSONException("JSONArray[" + index + "] not a string.");
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
/*      */   public boolean isNull(int index) {
/*  445 */     return JSONObject.NULL.equals(opt(index));
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
/*      */   public String join(String separator) throws JSONException {
/*  461 */     int len = length();
/*  462 */     StringBuffer sb = new StringBuffer();
/*      */     
/*  464 */     for (int i = 0; i < len; i++) {
/*      */       
/*  466 */       if (i > 0)
/*      */       {
/*  468 */         sb.append(separator);
/*      */       }
/*  470 */       sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
/*      */     } 
/*  472 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  482 */     return this.myArrayList.size();
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
/*      */   public Object opt(int index) {
/*  494 */     return (index < 0 || index >= length()) ? null : this.myArrayList
/*  495 */       .get(index);
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
/*      */   public boolean optBoolean(int index) {
/*  509 */     return optBoolean(index, false);
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
/*      */   public boolean optBoolean(int index, boolean defaultValue) {
/*      */     try {
/*  527 */       return getBoolean(index);
/*      */     }
/*  529 */     catch (Exception e) {
/*      */       
/*  531 */       return defaultValue;
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
/*      */   public double optDouble(int index) {
/*  546 */     return optDouble(index, Double.NaN);
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
/*      */   public double optDouble(int index, double defaultValue) {
/*      */     try {
/*  564 */       return getDouble(index);
/*      */     }
/*  566 */     catch (Exception e) {
/*      */       
/*  568 */       return defaultValue;
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
/*      */   public int optInt(int index) {
/*  583 */     return optInt(index, 0);
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
/*      */   public int optInt(int index, int defaultValue) {
/*      */     try {
/*  601 */       return getInt(index);
/*      */     }
/*  603 */     catch (Exception e) {
/*      */       
/*  605 */       return defaultValue;
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
/*      */   public JSONArray optJSONArray(int index) {
/*  619 */     Object o = opt(index);
/*  620 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
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
/*      */   public JSONObject optJSONObject(int index) {
/*  634 */     Object o = opt(index);
/*  635 */     return (o instanceof JSONObject) ? (JSONObject)o : null;
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
/*      */   public long optLong(int index) {
/*  649 */     return optLong(index, 0L);
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
/*      */   public long optLong(int index, long defaultValue) {
/*      */     try {
/*  667 */       return getLong(index);
/*      */     }
/*  669 */     catch (Exception e) {
/*      */       
/*  671 */       return defaultValue;
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
/*      */   public String optString(int index) {
/*  686 */     return optString(index, "");
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
/*      */   public String optString(int index, String defaultValue) {
/*  701 */     Object object = opt(index);
/*  702 */     return JSONObject.NULL.equals(object) ? defaultValue : object
/*  703 */       .toString();
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
/*      */   public JSONArray put(boolean value) {
/*  715 */     put(value ? Boolean.TRUE : Boolean.FALSE);
/*  716 */     return this;
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
/*      */   public JSONArray put(Collection value) {
/*  729 */     put(new JSONArray(value));
/*  730 */     return this;
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
/*      */   public JSONArray put(double value) throws JSONException {
/*  744 */     Double d = new Double(value);
/*  745 */     JSONObject.testValidity(d);
/*  746 */     put(d);
/*  747 */     return this;
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
/*      */   public JSONArray put(int value) {
/*  759 */     put(new Integer(value));
/*  760 */     return this;
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
/*      */   public JSONArray put(long value) {
/*  772 */     put(new Long(value));
/*  773 */     return this;
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
/*      */   public JSONArray put(Map value) {
/*  786 */     put(new JSONObject(value));
/*  787 */     return this;
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
/*      */   public JSONArray put(Object value) {
/*  801 */     this.myArrayList.add(value);
/*  802 */     return this;
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
/*      */   public JSONArray put(int index, boolean value) throws JSONException {
/*  820 */     put(index, value ? Boolean.TRUE : Boolean.FALSE);
/*  821 */     return this;
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
/*      */   public JSONArray put(int index, Collection value) throws JSONException {
/*  838 */     put(index, new JSONArray(value));
/*  839 */     return this;
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
/*      */   public JSONArray put(int index, double value) throws JSONException {
/*  857 */     put(index, new Double(value));
/*  858 */     return this;
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
/*      */   public JSONArray put(int index, int value) throws JSONException {
/*  876 */     put(index, new Integer(value));
/*  877 */     return this;
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
/*      */   public JSONArray put(int index, long value) throws JSONException {
/*  895 */     put(index, new Long(value));
/*  896 */     return this;
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
/*      */   public JSONArray put(int index, Map value) throws JSONException {
/*  914 */     put(index, new JSONObject(value));
/*  915 */     return this;
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
/*      */   public JSONArray put(int index, Object value) throws JSONException {
/*  936 */     JSONObject.testValidity(value);
/*  937 */     if (index < 0)
/*      */     {
/*  939 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/*  941 */     if (index < length()) {
/*      */       
/*  943 */       this.myArrayList.set(index, value);
/*      */     }
/*      */     else {
/*      */       
/*  947 */       while (index != length())
/*      */       {
/*  949 */         put(JSONObject.NULL);
/*      */       }
/*  951 */       put(value);
/*      */     } 
/*  953 */     return this;
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
/*      */   public Object remove(int index) {
/*  966 */     Object o = opt(index);
/*  967 */     this.myArrayList.remove(index);
/*  968 */     return o;
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
/*      */   public JSONObject toJSONObject(JSONArray names) throws JSONException {
/*  985 */     if (names == null || names.length() == 0 || length() == 0)
/*      */     {
/*  987 */       return null;
/*      */     }
/*  989 */     JSONObject jo = new JSONObject();
/*  990 */     for (int i = 0; i < names.length(); i++)
/*      */     {
/*  992 */       jo.put(names.getString(i), opt(i));
/*      */     }
/*  994 */     return jo;
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
/*      */   public String toString() {
/*      */     try {
/* 1013 */       return toString(0);
/*      */     }
/* 1015 */     catch (Exception e) {
/*      */       
/* 1017 */       return null;
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
/* 1035 */     StringWriter sw = new StringWriter();
/* 1036 */     synchronized (sw.getBuffer()) {
/*      */       
/* 1038 */       return write(sw, indentFactor, 0).toString();
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
/* 1053 */     return write(writer, 0, 0);
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
/*      */   Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
/*      */     try {
/* 1074 */       boolean commanate = false;
/* 1075 */       int length = length();
/* 1076 */       writer.write(91);
/*      */       
/* 1078 */       if (length == 1) {
/*      */         
/* 1080 */         JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
/*      */       
/*      */       }
/* 1083 */       else if (length != 0) {
/*      */         
/* 1085 */         int newindent = indent + indentFactor;
/*      */         
/* 1087 */         for (int i = 0; i < length; i++) {
/*      */           
/* 1089 */           if (commanate)
/*      */           {
/* 1091 */             writer.write(44);
/*      */           }
/* 1093 */           if (indentFactor > 0)
/*      */           {
/* 1095 */             writer.write(10);
/*      */           }
/* 1097 */           JSONObject.indent(writer, newindent);
/* 1098 */           JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent);
/*      */           
/* 1100 */           commanate = true;
/*      */         } 
/* 1102 */         if (indentFactor > 0)
/*      */         {
/* 1104 */           writer.write(10);
/*      */         }
/* 1106 */         JSONObject.indent(writer, indent);
/*      */       } 
/* 1108 */       writer.write(93);
/* 1109 */       return writer;
/*      */     }
/* 1111 */     catch (IOException e) {
/*      */       
/* 1113 */       throw new JSONException(e);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\JSONArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */