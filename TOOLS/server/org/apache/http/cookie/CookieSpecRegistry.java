/*     */ package org.apache.http.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public final class CookieSpecRegistry
/*     */ {
/*  55 */   private final ConcurrentHashMap<String, CookieSpecFactory> registeredSpecs = new ConcurrentHashMap<String, CookieSpecFactory>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String name, CookieSpecFactory factory) {
/*  70 */     if (name == null) {
/*  71 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/*  73 */     if (factory == null) {
/*  74 */       throw new IllegalArgumentException("Cookie spec factory may not be null");
/*     */     }
/*  76 */     this.registeredSpecs.put(name.toLowerCase(Locale.ENGLISH), factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(String id) {
/*  85 */     if (id == null) {
/*  86 */       throw new IllegalArgumentException("Id may not be null");
/*     */     }
/*  88 */     this.registeredSpecs.remove(id.toLowerCase(Locale.ENGLISH));
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
/*     */   public CookieSpec getCookieSpec(String name, HttpParams params) throws IllegalStateException {
/* 105 */     if (name == null) {
/* 106 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/* 108 */     CookieSpecFactory factory = this.registeredSpecs.get(name.toLowerCase(Locale.ENGLISH));
/* 109 */     if (factory != null) {
/* 110 */       return factory.newInstance(params);
/*     */     }
/* 112 */     throw new IllegalStateException("Unsupported cookie spec: " + name);
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
/*     */   public CookieSpec getCookieSpec(String name) throws IllegalStateException {
/* 127 */     return getCookieSpec(name, null);
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
/*     */   public List<String> getSpecNames() {
/* 140 */     return new ArrayList<String>(this.registeredSpecs.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Map<String, CookieSpecFactory> map) {
/* 150 */     if (map == null) {
/*     */       return;
/*     */     }
/* 153 */     this.registeredSpecs.clear();
/* 154 */     this.registeredSpecs.putAll(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\cookie\CookieSpecRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */