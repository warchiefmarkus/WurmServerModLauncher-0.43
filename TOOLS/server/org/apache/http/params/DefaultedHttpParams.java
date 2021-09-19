/*     */ package org.apache.http.params;
/*     */ 
/*     */ import java.util.HashSet;
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
/*     */ public final class DefaultedHttpParams
/*     */   extends AbstractHttpParams
/*     */ {
/*     */   private final HttpParams local;
/*     */   private final HttpParams defaults;
/*     */   
/*     */   public DefaultedHttpParams(HttpParams local, HttpParams defaults) {
/*  56 */     if (local == null) {
/*  57 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  59 */     this.local = local;
/*  60 */     this.defaults = defaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpParams copy() {
/*  70 */     HttpParams clone = this.local.copy();
/*  71 */     return new DefaultedHttpParams(clone, this.defaults);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getParameter(String name) {
/*  80 */     Object obj = this.local.getParameter(name);
/*  81 */     if (obj == null && this.defaults != null) {
/*  82 */       obj = this.defaults.getParameter(name);
/*     */     }
/*  84 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeParameter(String name) {
/*  92 */     return this.local.removeParameter(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpParams setParameter(String name, Object value) {
/* 100 */     return this.local.setParameter(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpParams getDefaults() {
/* 110 */     return this.defaults;
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
/*     */   public Set<String> getNames() {
/* 126 */     Set<String> combined = new HashSet<String>(getNames(this.defaults));
/* 127 */     combined.addAll(getNames(this.local));
/* 128 */     return combined;
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
/*     */   public Set<String> getDefaultNames() {
/* 142 */     return new HashSet<String>(getNames(this.defaults));
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
/*     */   public Set<String> getLocalNames() {
/* 156 */     return new HashSet<String>(getNames(this.local));
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<String> getNames(HttpParams params) {
/* 161 */     if (params instanceof HttpParamsNames) {
/* 162 */       return ((HttpParamsNames)params).getNames();
/*     */     }
/* 164 */     throw new UnsupportedOperationException("HttpParams instance does not implement HttpParamsNames");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\DefaultedHttpParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */