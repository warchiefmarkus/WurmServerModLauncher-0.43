/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.GuardedBy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class UriPatternMatcher<T>
/*     */ {
/*     */   @GuardedBy("this")
/*  60 */   private final Map<String, T> map = new HashMap<String, T>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void register(String pattern, T obj) {
/*  70 */     if (pattern == null) {
/*  71 */       throw new IllegalArgumentException("URI request pattern may not be null");
/*     */     }
/*  73 */     this.map.put(pattern, obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unregister(String pattern) {
/*  82 */     if (pattern == null) {
/*     */       return;
/*     */     }
/*  85 */     this.map.remove(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public synchronized void setHandlers(Map<String, T> map) {
/*  93 */     if (map == null) {
/*  94 */       throw new IllegalArgumentException("Map of handlers may not be null");
/*     */     }
/*  96 */     this.map.clear();
/*  97 */     this.map.putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setObjects(Map<String, T> map) {
/* 105 */     if (map == null) {
/* 106 */       throw new IllegalArgumentException("Map of handlers may not be null");
/*     */     }
/* 108 */     this.map.clear();
/* 109 */     this.map.putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Map<String, T> getObjects() {
/* 119 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized T lookup(String requestURI) {
/* 129 */     if (requestURI == null) {
/* 130 */       throw new IllegalArgumentException("Request URI may not be null");
/*     */     }
/*     */     
/* 133 */     int index = requestURI.indexOf("?");
/* 134 */     if (index != -1) {
/* 135 */       requestURI = requestURI.substring(0, index);
/*     */     }
/*     */ 
/*     */     
/* 139 */     T obj = this.map.get(requestURI);
/* 140 */     if (obj == null) {
/*     */       
/* 142 */       String bestMatch = null;
/* 143 */       for (Iterator<String> it = this.map.keySet().iterator(); it.hasNext(); ) {
/* 144 */         String pattern = it.next();
/* 145 */         if (matchUriRequestPattern(pattern, requestURI))
/*     */         {
/* 147 */           if (bestMatch == null || bestMatch.length() < pattern.length() || (bestMatch.length() == pattern.length() && pattern.endsWith("*"))) {
/*     */ 
/*     */             
/* 150 */             obj = this.map.get(pattern);
/* 151 */             bestMatch = pattern;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 156 */     return obj;
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
/*     */   protected boolean matchUriRequestPattern(String pattern, String requestUri) {
/* 168 */     if (pattern.equals("*")) {
/* 169 */       return true;
/*     */     }
/* 171 */     return ((pattern.endsWith("*") && requestUri.startsWith(pattern.substring(0, pattern.length() - 1))) || (pattern.startsWith("*") && requestUri.endsWith(pattern.substring(1, pattern.length()))));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\UriPatternMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */