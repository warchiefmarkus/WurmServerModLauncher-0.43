/*     */ package org.apache.http.params;
/*     */ 
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractHttpParams
/*     */   implements HttpParams, HttpParamsNames
/*     */ {
/*     */   public long getLongParameter(String name, long defaultValue) {
/*  52 */     Object param = getParameter(name);
/*  53 */     if (param == null) {
/*  54 */       return defaultValue;
/*     */     }
/*  56 */     return ((Long)param).longValue();
/*     */   }
/*     */   
/*     */   public HttpParams setLongParameter(String name, long value) {
/*  60 */     setParameter(name, new Long(value));
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public int getIntParameter(String name, int defaultValue) {
/*  65 */     Object param = getParameter(name);
/*  66 */     if (param == null) {
/*  67 */       return defaultValue;
/*     */     }
/*  69 */     return ((Integer)param).intValue();
/*     */   }
/*     */   
/*     */   public HttpParams setIntParameter(String name, int value) {
/*  73 */     setParameter(name, new Integer(value));
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public double getDoubleParameter(String name, double defaultValue) {
/*  78 */     Object param = getParameter(name);
/*  79 */     if (param == null) {
/*  80 */       return defaultValue;
/*     */     }
/*  82 */     return ((Double)param).doubleValue();
/*     */   }
/*     */   
/*     */   public HttpParams setDoubleParameter(String name, double value) {
/*  86 */     setParameter(name, new Double(value));
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getBooleanParameter(String name, boolean defaultValue) {
/*  91 */     Object param = getParameter(name);
/*  92 */     if (param == null) {
/*  93 */       return defaultValue;
/*     */     }
/*  95 */     return ((Boolean)param).booleanValue();
/*     */   }
/*     */   
/*     */   public HttpParams setBooleanParameter(String name, boolean value) {
/*  99 */     setParameter(name, value ? Boolean.TRUE : Boolean.FALSE);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isParameterTrue(String name) {
/* 104 */     return getBooleanParameter(name, false);
/*     */   }
/*     */   
/*     */   public boolean isParameterFalse(String name) {
/* 108 */     return !getBooleanParameter(name, false);
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
/*     */   public Set<String> getNames() {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\AbstractHttpParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */