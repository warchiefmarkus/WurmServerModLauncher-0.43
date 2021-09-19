/*     */ package org.apache.http.auth;
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
/*     */ @ThreadSafe
/*     */ public final class AuthSchemeRegistry
/*     */ {
/*  52 */   private final ConcurrentHashMap<String, AuthSchemeFactory> registeredSchemes = new ConcurrentHashMap<String, AuthSchemeFactory>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String name, AuthSchemeFactory factory) {
/*  73 */     if (name == null) {
/*  74 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/*  76 */     if (factory == null) {
/*  77 */       throw new IllegalArgumentException("Authentication scheme factory may not be null");
/*     */     }
/*  79 */     this.registeredSchemes.put(name.toLowerCase(Locale.ENGLISH), factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(String name) {
/*  89 */     if (name == null) {
/*  90 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/*  92 */     this.registeredSchemes.remove(name.toLowerCase(Locale.ENGLISH));
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
/*     */   public AuthScheme getAuthScheme(String name, HttpParams params) throws IllegalStateException {
/* 109 */     if (name == null) {
/* 110 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/* 112 */     AuthSchemeFactory factory = this.registeredSchemes.get(name.toLowerCase(Locale.ENGLISH));
/* 113 */     if (factory != null) {
/* 114 */       return factory.newInstance(params);
/*     */     }
/* 116 */     throw new IllegalStateException("Unsupported authentication scheme: " + name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getSchemeNames() {
/* 127 */     return new ArrayList<String>(this.registeredSchemes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Map<String, AuthSchemeFactory> map) {
/* 137 */     if (map == null) {
/*     */       return;
/*     */     }
/* 140 */     this.registeredSchemes.clear();
/* 141 */     this.registeredSchemes.putAll(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\AuthSchemeRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */