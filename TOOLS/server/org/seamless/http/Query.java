/*     */ package org.seamless.http;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ public class Query
/*     */ {
/*  39 */   protected final Map<String, List<String>> parameters = new LinkedHashMap<String, List<String>>();
/*     */   
/*     */   public static Query newInstance(Map<String, List<String>> parameters) {
/*  42 */     Query query = new Query();
/*  43 */     query.parameters.putAll(parameters);
/*  44 */     return query;
/*     */   }
/*     */ 
/*     */   
/*     */   public Query() {}
/*     */   
/*     */   public Query(Map<String, String[]> parameters) {
/*  51 */     for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
/*  52 */       List<String> list = Arrays.asList((entry.getValue() != null) ? entry.getValue() : new String[0]);
/*     */       
/*  54 */       this.parameters.put(entry.getKey(), list);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Query(URL url) {
/*  59 */     this(url.getQuery());
/*     */   }
/*     */   
/*     */   public Query(String qs) {
/*  63 */     if (qs == null) {
/*     */       return;
/*     */     }
/*  66 */     String[] pairs = qs.split("&");
/*  67 */     for (String pair : pairs) {
/*     */       String name, value;
/*     */       
/*  70 */       int pos = pair.indexOf('=');
/*     */       
/*  72 */       if (pos == -1) {
/*  73 */         name = pair;
/*  74 */         value = null;
/*     */       } else {
/*     */         try {
/*  77 */           name = URLDecoder.decode(pair.substring(0, pos), "UTF-8");
/*  78 */           value = URLDecoder.decode(pair.substring(pos + 1, pair.length()), "UTF-8");
/*  79 */         } catch (UnsupportedEncodingException e) {
/*     */           
/*  81 */           throw new IllegalStateException("Query string is not UTF-8");
/*     */         } 
/*     */       } 
/*  84 */       List<String> list = this.parameters.get(name);
/*  85 */       if (list == null) {
/*  86 */         list = new ArrayList<String>();
/*  87 */         this.parameters.put(name, list);
/*     */       } 
/*  89 */       list.add(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String get(String name) {
/*  94 */     List<String> values = this.parameters.get(name);
/*  95 */     if (values == null) {
/*  96 */       return "";
/*     */     }
/*  98 */     if (values.size() == 0) {
/*  99 */       return "";
/*     */     }
/* 101 */     return values.get(0);
/*     */   }
/*     */   
/*     */   public String[] getValues(String name) {
/* 105 */     List<String> values = this.parameters.get(name);
/* 106 */     if (values == null) {
/* 107 */       return null;
/*     */     }
/* 109 */     return values.<String>toArray(new String[values.size()]);
/*     */   }
/*     */   
/*     */   public List<String> getValuesAsList(String name) {
/* 113 */     return this.parameters.containsKey(name) ? Collections.<String>unmodifiableList(this.parameters.get(name)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getNames() {
/* 119 */     return Collections.enumeration(this.parameters.keySet());
/*     */   }
/*     */   
/*     */   public Map<String, String[]> getMap() {
/* 123 */     Map<String, String[]> map = (Map)new TreeMap<String, String>();
/* 124 */     for (Map.Entry<String, List<String>> entry : this.parameters.entrySet()) {
/* 125 */       String[] values; List<String> list = entry.getValue();
/*     */       
/* 127 */       if (list == null) {
/* 128 */         values = null;
/*     */       } else {
/* 130 */         values = list.<String>toArray(new String[list.size()]);
/* 131 */       }  map.put(entry.getKey(), values);
/*     */     } 
/* 133 */     return map;
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getMapWithLists() {
/* 137 */     return Collections.unmodifiableMap(this.parameters);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 141 */     return (this.parameters.size() == 0);
/*     */   }
/*     */   
/*     */   public Query cloneAndAdd(String name, String... values) {
/* 145 */     Map<String, List<String>> params = new HashMap<String, List<String>>(getMapWithLists());
/* 146 */     List<String> existingValues = params.get(name);
/* 147 */     if (existingValues == null) {
/* 148 */       existingValues = new ArrayList<String>();
/* 149 */       params.put(name, existingValues);
/*     */     } 
/* 151 */     existingValues.addAll(Arrays.asList(values));
/* 152 */     return newInstance(params);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     StringBuilder sb = new StringBuilder();
/* 158 */     for (Map.Entry<String, List<String>> entry : this.parameters.entrySet()) {
/* 159 */       for (String v : entry.getValue()) {
/* 160 */         if (v == null || v.length() == 0)
/* 161 */           continue;  if (sb.length() > 0) sb.append("&"); 
/* 162 */         sb.append(entry.getKey());
/* 163 */         sb.append("=");
/* 164 */         sb.append(v);
/*     */       } 
/*     */     } 
/* 167 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\http\Query.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */