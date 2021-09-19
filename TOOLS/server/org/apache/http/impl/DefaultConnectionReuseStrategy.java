/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.TokenIterator;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicTokenIterator;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class DefaultConnectionReuseStrategy
/*     */   implements ConnectionReuseStrategy
/*     */ {
/*     */   public boolean keepAlive(HttpResponse response, HttpContext context) {
/*  72 */     if (response == null) {
/*  73 */       throw new IllegalArgumentException("HTTP response may not be null.");
/*     */     }
/*     */     
/*  76 */     if (context == null) {
/*  77 */       throw new IllegalArgumentException("HTTP context may not be null.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
/*  84 */     Header teh = response.getFirstHeader("Transfer-Encoding");
/*  85 */     if (teh != null) {
/*  86 */       if (!"chunked".equalsIgnoreCase(teh.getValue())) {
/*  87 */         return false;
/*     */       }
/*     */     }
/*  90 */     else if (canResponseHaveBody(response)) {
/*  91 */       Header[] clhs = response.getHeaders("Content-Length");
/*     */       
/*  93 */       if (clhs.length == 1) {
/*  94 */         Header clh = clhs[0];
/*     */         try {
/*  96 */           int contentLen = Integer.parseInt(clh.getValue());
/*  97 */           if (contentLen < 0) {
/*  98 */             return false;
/*     */           }
/* 100 */         } catch (NumberFormatException ex) {
/* 101 */           return false;
/*     */         } 
/*     */       } else {
/* 104 */         return false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     HeaderIterator hit = response.headerIterator("Connection");
/* 113 */     if (!hit.hasNext()) {
/* 114 */       hit = response.headerIterator("Proxy-Connection");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (hit.hasNext()) {
/*     */       try {
/* 141 */         TokenIterator ti = createTokenIterator(hit);
/* 142 */         boolean keepalive = false;
/* 143 */         while (ti.hasNext()) {
/* 144 */           String token = ti.nextToken();
/* 145 */           if ("Close".equalsIgnoreCase(token))
/* 146 */             return false; 
/* 147 */           if ("Keep-Alive".equalsIgnoreCase(token))
/*     */           {
/* 149 */             keepalive = true;
/*     */           }
/*     */         } 
/* 152 */         if (keepalive) {
/* 153 */           return true;
/*     */         }
/*     */       }
/* 156 */       catch (ParseException px) {
/*     */ 
/*     */         
/* 159 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 164 */     return !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0);
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
/*     */   protected TokenIterator createTokenIterator(HeaderIterator hit) {
/* 178 */     return (TokenIterator)new BasicTokenIterator(hit);
/*     */   }
/*     */   
/*     */   private boolean canResponseHaveBody(HttpResponse response) {
/* 182 */     int status = response.getStatusLine().getStatusCode();
/* 183 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\DefaultConnectionReuseStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */