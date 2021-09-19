/*    */ package org.apache.http.impl;
/*    */ 
/*    */ import org.apache.http.ConnectionReuseStrategy;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class NoConnectionReuseStrategy
/*    */   implements ConnectionReuseStrategy
/*    */ {
/*    */   public boolean keepAlive(HttpResponse response, HttpContext context) {
/* 48 */     if (response == null) {
/* 49 */       throw new IllegalArgumentException("HTTP response may not be null");
/*    */     }
/* 51 */     if (context == null) {
/* 52 */       throw new IllegalArgumentException("HTTP context may not be null");
/*    */     }
/*    */     
/* 55 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\NoConnectionReuseStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */