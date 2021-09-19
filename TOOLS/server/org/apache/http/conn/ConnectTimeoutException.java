/*    */ package org.apache.http.conn;
/*    */ 
/*    */ import java.io.InterruptedIOException;
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
/*    */ public class ConnectTimeoutException
/*    */   extends InterruptedIOException
/*    */ {
/*    */   private static final long serialVersionUID = -4816682903149535989L;
/*    */   
/*    */   public ConnectTimeoutException() {}
/*    */   
/*    */   public ConnectTimeoutException(String message) {
/* 59 */     super(message);
/*    */   }
/*    */   
/*    */   public ConnectTimeoutException(String message, Throwable cause) {
/* 63 */     super(message);
/* 64 */     initCause(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\ConnectTimeoutException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */