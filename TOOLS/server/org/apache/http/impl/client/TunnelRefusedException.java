/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ public class TunnelRefusedException
/*    */   extends HttpException
/*    */ {
/*    */   private static final long serialVersionUID = -8646722842745617323L;
/*    */   private final HttpResponse response;
/*    */   
/*    */   public TunnelRefusedException(String message, HttpResponse response) {
/* 48 */     super(message);
/* 49 */     this.response = response;
/*    */   }
/*    */   
/*    */   public HttpResponse getResponse() {
/* 53 */     return this.response;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\TunnelRefusedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */