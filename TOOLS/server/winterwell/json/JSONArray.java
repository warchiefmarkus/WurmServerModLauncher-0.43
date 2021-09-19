/*     */ package winterwell.json;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class JSONArray
/*     */ {
/*     */   private final ArrayList myArrayList;
/*     */   
/*     */   public ArrayList getList() {
/*  95 */     return this.myArrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray() {
/* 102 */     this.myArrayList = new ArrayList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray(JSONTokener x) throws JSONException {
/* 111 */     this();
/* 112 */     if (x.nextClean() != '[') {
/* 113 */       throw x.syntaxError("A JSONArray text must start with '['");
/*     */     }
/* 115 */     if (x.nextClean() == ']') {
/*     */       return;
/*     */     }
/* 118 */     x.back();
/*     */     while (true) {
/* 120 */       if (x.nextClean() == ',') {
/* 121 */         x.back();
/* 122 */         this.myArrayList.add(null);
/*     */       } else {
/* 124 */         x.back();
/* 125 */         this.myArrayList.add(x.nextValue());
/*     */       } 
/* 127 */       switch (x.nextClean()) {
/*     */         case ',':
/*     */         case ';':
/* 130 */           if (x.nextClean() == ']') {
/*     */             return;
/*     */           }
/* 133 */           x.back(); continue;
/*     */         case ']':
/*     */           return;
/*     */       }  break;
/*     */     } 
/* 138 */     throw x.syntaxError("Expected a ',' or ']'");
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
/*     */   public JSONArray(String string) throws JSONException {
/* 152 */     this(new JSONTokener(string));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray(Collection<?> collection) {
/* 161 */     this.myArrayList = (collection == null) ? 
/* 162 */       new ArrayList() : 
/* 163 */       new ArrayList(collection);
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
/*     */   public Object get(int index) throws JSONException {
/* 175 */     Object o = opt(index);
/* 176 */     if (o == null) {
/* 177 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*     */     }
/* 179 */     return o;
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
/*     */   public boolean getBoolean(int index) throws JSONException {
/* 193 */     Object o = get(index);
/* 194 */     if (o.equals(Boolean.FALSE) || (
/* 195 */       o instanceof String && (
/* 196 */       (String)o).equalsIgnoreCase("false")))
/* 197 */       return false; 
/* 198 */     if (o.equals(Boolean.TRUE) || (
/* 199 */       o instanceof String && (
/* 200 */       (String)o).equalsIgnoreCase("true"))) {
/* 201 */       return true;
/*     */     }
/* 203 */     throw new JSONException("JSONArray[" + index + "] is not a Boolean.");
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
/*     */   public double getDouble(int index) throws JSONException {
/* 216 */     Object o = get(index);
/*     */     try {
/* 218 */       return (o instanceof Number) ? (
/* 219 */         (Number)o).doubleValue() : 
/* 220 */         Double.valueOf((String)o).doubleValue();
/* 221 */     } catch (Exception e) {
/* 222 */       throw new JSONException("JSONArray[" + index + 
/* 223 */           "] is not a number.");
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
/*     */ 
/*     */   
/*     */   public int getInt(int index) throws JSONException {
/* 238 */     Object o = get(index);
/* 239 */     return (o instanceof Number) ? (
/* 240 */       (Number)o).intValue() : (int)getDouble(index);
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
/*     */   public JSONArray getJSONArray(int index) throws JSONException {
/* 252 */     Object o = get(index);
/* 253 */     if (o instanceof JSONArray) {
/* 254 */       return (JSONArray)o;
/*     */     }
/* 256 */     throw new JSONException("JSONArray[" + index + 
/* 257 */         "] is not a JSONArray.");
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
/*     */   public JSONObject getJSONObject(int index) throws JSONException {
/* 269 */     Object o = get(index);
/* 270 */     if (o instanceof JSONObject) {
/* 271 */       return (JSONObject)o;
/*     */     }
/* 273 */     throw new JSONException("JSONArray[" + index + 
/* 274 */         "] is not a JSONObject.");
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
/*     */   public long getLong(int index) throws JSONException {
/* 287 */     Object o = get(index);
/* 288 */     return (o instanceof Number) ? (
/* 289 */       (Number)o).longValue() : (long)getDouble(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(int index) throws JSONException {
/* 300 */     return get(index).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNull(int index) {
/* 310 */     return JSONObject.NULL.equals(opt(index));
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
/*     */   public String join(String separator) throws JSONException {
/* 323 */     int len = length();
/* 324 */     StringBuffer sb = new StringBuffer();
/*     */     
/* 326 */     for (int i = 0; i < len; i++) {
/* 327 */       if (i > 0) {
/* 328 */         sb.append(separator);
/*     */       }
/* 330 */       sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
/*     */     } 
/* 332 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 342 */     return this.myArrayList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object opt(int index) {
/* 353 */     return (index < 0 || index >= length()) ? 
/* 354 */       null : this.myArrayList.get(index);
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
/*     */   public boolean optBoolean(int index) {
/* 367 */     return optBoolean(index, false);
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
/*     */   public boolean optBoolean(int index, boolean defaultValue) {
/*     */     try {
/* 382 */       return getBoolean(index);
/* 383 */     } catch (Exception e) {
/* 384 */       return defaultValue;
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
/*     */   
/*     */   public double optDouble(int index) {
/* 398 */     return optDouble(index, Double.NaN);
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
/*     */   public double optDouble(int index, double defaultValue) {
/*     */     try {
/* 413 */       return getDouble(index);
/* 414 */     } catch (Exception e) {
/* 415 */       return defaultValue;
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
/*     */   
/*     */   public int optInt(int index) {
/* 429 */     return optInt(index, 0);
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
/*     */   public int optInt(int index, int defaultValue) {
/*     */     try {
/* 443 */       return getInt(index);
/* 444 */     } catch (Exception e) {
/* 445 */       return defaultValue;
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
/*     */   public JSONArray optJSONArray(int index) {
/* 457 */     Object o = opt(index);
/* 458 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
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
/*     */   public JSONObject optJSONObject(int index) {
/* 471 */     Object o = opt(index);
/* 472 */     return (o instanceof JSONObject) ? (JSONObject)o : null;
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
/*     */   public long optLong(int index) {
/* 485 */     return optLong(index, 0L);
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
/*     */   public long optLong(int index, long defaultValue) {
/*     */     try {
/* 499 */       return getLong(index);
/* 500 */     } catch (Exception e) {
/* 501 */       return defaultValue;
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
/*     */   
/*     */   public String optString(int index) {
/* 515 */     return optString(index, "");
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
/*     */   public String optString(int index, String defaultValue) {
/* 528 */     Object o = opt(index);
/* 529 */     return (o != null) ? o.toString() : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(boolean value) {
/* 540 */     put(value ? Boolean.TRUE : Boolean.FALSE);
/* 541 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(Collection value) {
/* 552 */     put(new JSONArray(value));
/* 553 */     return this;
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
/*     */   public JSONArray put(double value) throws JSONException {
/* 565 */     Double d = new Double(value);
/* 566 */     JSONObject.testValidity(d);
/* 567 */     put(d);
/* 568 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(int value) {
/* 579 */     put(new Integer(value));
/* 580 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(long value) {
/* 591 */     put(new Long(value));
/* 592 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(Map value) {
/* 603 */     put(new JSONObject(value));
/* 604 */     return this;
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
/*     */   public JSONArray put(Object value) {
/* 616 */     this.myArrayList.add(value);
/* 617 */     return this;
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
/*     */   public JSONArray put(int index, boolean value) throws JSONException {
/* 631 */     put(index, value ? Boolean.TRUE : Boolean.FALSE);
/* 632 */     return this;
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
/*     */   public JSONArray put(int index, Collection value) throws JSONException {
/* 646 */     put(index, new JSONArray(value));
/* 647 */     return this;
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
/*     */   public JSONArray put(int index, double value) throws JSONException {
/* 662 */     put(index, new Double(value));
/* 663 */     return this;
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
/*     */   public JSONArray put(int index, int value) throws JSONException {
/* 677 */     put(index, new Integer(value));
/* 678 */     return this;
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
/*     */   public JSONArray put(int index, long value) throws JSONException {
/* 692 */     put(index, new Long(value));
/* 693 */     return this;
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
/*     */   public JSONArray put(int index, Map value) throws JSONException {
/* 707 */     put(index, new JSONObject(value));
/* 708 */     return this;
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
/*     */   public JSONArray put(int index, Object value) throws JSONException {
/* 725 */     JSONObject.testValidity(value);
/* 726 */     if (index < 0) {
/* 727 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*     */     }
/* 729 */     if (index < length()) {
/* 730 */       this.myArrayList.set(index, value);
/*     */     } else {
/* 732 */       while (index != length()) {
/* 733 */         put(JSONObject.NULL);
/*     */       }
/* 735 */       put(value);
/*     */     } 
/* 737 */     return this;
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
/*     */   public JSONObject toJSONObject(JSONArray names) throws JSONException {
/* 751 */     if (names == null || names.length() == 0 || length() == 0) {
/* 752 */       return null;
/*     */     }
/* 754 */     JSONObject jo = new JSONObject();
/* 755 */     for (int i = 0; i < names.length(); i++) {
/* 756 */       jo.put(names.getString(i), opt(i));
/*     */     }
/* 758 */     return jo;
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
/*     */   public String toString() {
/*     */     try {
/* 776 */       return String.valueOf('[') + join(",") + ']';
/* 777 */     } catch (Exception e) {
/* 778 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int indentFactor) throws JSONException {
/* 795 */     return toString(indentFactor, 0);
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
/*     */   String toString(int indentFactor, int indent) throws JSONException {
/* 810 */     int len = length();
/* 811 */     if (len == 0) {
/* 812 */       return "[]";
/*     */     }
/*     */     
/* 815 */     StringBuffer sb = new StringBuffer("[");
/* 816 */     if (len == 1) {
/* 817 */       sb.append(JSONObject.valueToString(this.myArrayList.get(0), 
/* 818 */             indentFactor, indent));
/*     */     } else {
/* 820 */       int newindent = indent + indentFactor;
/* 821 */       sb.append('\n'); int i;
/* 822 */       for (i = 0; i < len; i++) {
/* 823 */         if (i > 0) {
/* 824 */           sb.append(",\n");
/*     */         }
/* 826 */         for (int j = 0; j < newindent; j++) {
/* 827 */           sb.append(' ');
/*     */         }
/* 829 */         sb.append(JSONObject.valueToString(this.myArrayList.get(i), 
/* 830 */               indentFactor, newindent));
/*     */       } 
/* 832 */       sb.append('\n');
/* 833 */       for (i = 0; i < indent; i++) {
/* 834 */         sb.append(' ');
/*     */       }
/*     */     } 
/* 837 */     sb.append(']');
/* 838 */     return sb.toString();
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
/*     */   public Writer write(Writer writer) throws JSONException {
/*     */     try {
/* 853 */       boolean b = false;
/* 854 */       int len = length();
/*     */       
/* 856 */       writer.write(91);
/*     */       
/* 858 */       for (int i = 0; i < len; i++) {
/* 859 */         if (b) {
/* 860 */           writer.write(44);
/*     */         }
/* 862 */         Object v = this.myArrayList.get(i);
/* 863 */         if (v instanceof JSONObject) {
/* 864 */           ((JSONObject)v).write(writer);
/* 865 */         } else if (v instanceof JSONArray) {
/* 866 */           ((JSONArray)v).write(writer);
/*     */         } else {
/* 868 */           writer.write(JSONObject.valueToString(v));
/*     */         } 
/* 870 */         b = true;
/*     */       } 
/* 872 */       writer.write(93);
/* 873 */       return writer;
/* 874 */     } catch (IOException e) {
/* 875 */       throw new JSONException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\json\JSONArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */