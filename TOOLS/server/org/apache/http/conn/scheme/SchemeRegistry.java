/*     */ package org.apache.http.conn.scheme;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class SchemeRegistry
/*     */ {
/*  55 */   private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap<String, Scheme>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Scheme getScheme(String name) {
/*  69 */     Scheme found = get(name);
/*  70 */     if (found == null) {
/*  71 */       throw new IllegalStateException("Scheme '" + name + "' not registered.");
/*     */     }
/*     */     
/*  74 */     return found;
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
/*     */   public final Scheme getScheme(HttpHost host) {
/*  89 */     if (host == null) {
/*  90 */       throw new IllegalArgumentException("Host must not be null.");
/*     */     }
/*  92 */     return getScheme(host.getSchemeName());
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
/*     */   public final Scheme get(String name) {
/* 104 */     if (name == null) {
/* 105 */       throw new IllegalArgumentException("Name must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 109 */     Scheme found = this.registeredSchemes.get(name);
/* 110 */     return found;
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
/*     */   public final Scheme register(Scheme sch) {
/* 124 */     if (sch == null) {
/* 125 */       throw new IllegalArgumentException("Scheme must not be null.");
/*     */     }
/* 127 */     Scheme old = this.registeredSchemes.put(sch.getName(), sch);
/* 128 */     return old;
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
/*     */   public final Scheme unregister(String name) {
/* 140 */     if (name == null) {
/* 141 */       throw new IllegalArgumentException("Name must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 145 */     Scheme gone = this.registeredSchemes.remove(name);
/* 146 */     return gone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<String> getSchemeNames() {
/* 155 */     return new ArrayList<String>(this.registeredSchemes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Map<String, Scheme> map) {
/* 165 */     if (map == null) {
/*     */       return;
/*     */     }
/* 168 */     this.registeredSchemes.clear();
/* 169 */     this.registeredSchemes.putAll(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\scheme\SchemeRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */