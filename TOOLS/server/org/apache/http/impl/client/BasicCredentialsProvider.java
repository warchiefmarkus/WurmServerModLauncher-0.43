/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicCredentialsProvider
/*     */   implements CredentialsProvider
/*     */ {
/*  53 */   private final ConcurrentHashMap<AuthScope, Credentials> credMap = new ConcurrentHashMap<AuthScope, Credentials>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCredentials(AuthScope authscope, Credentials credentials) {
/*  59 */     if (authscope == null) {
/*  60 */       throw new IllegalArgumentException("Authentication scope may not be null");
/*     */     }
/*  62 */     this.credMap.put(authscope, credentials);
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
/*     */   private static Credentials matchCredentials(Map<AuthScope, Credentials> map, AuthScope authscope) {
/*  77 */     Credentials creds = map.get(authscope);
/*  78 */     if (creds == null) {
/*     */ 
/*     */       
/*  81 */       int bestMatchFactor = -1;
/*  82 */       AuthScope bestMatch = null;
/*  83 */       for (AuthScope current : map.keySet()) {
/*  84 */         int factor = authscope.match(current);
/*  85 */         if (factor > bestMatchFactor) {
/*  86 */           bestMatchFactor = factor;
/*  87 */           bestMatch = current;
/*     */         } 
/*     */       } 
/*  90 */       if (bestMatch != null) {
/*  91 */         creds = map.get(bestMatch);
/*     */       }
/*     */     } 
/*  94 */     return creds;
/*     */   }
/*     */   
/*     */   public Credentials getCredentials(AuthScope authscope) {
/*  98 */     if (authscope == null) {
/*  99 */       throw new IllegalArgumentException("Authentication scope may not be null");
/*     */     }
/* 101 */     return matchCredentials(this.credMap, authscope);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 105 */     this.credMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return this.credMap.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\BasicCredentialsProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */