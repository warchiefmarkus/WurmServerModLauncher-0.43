/*    */ package org.apache.http.impl.entity;
/*    */ 
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.entity.ContentLengthStrategy;
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
/*    */ public class DisallowIdentityContentLengthStrategy
/*    */   implements ContentLengthStrategy
/*    */ {
/*    */   private final ContentLengthStrategy contentLengthStrategy;
/*    */   
/*    */   public DisallowIdentityContentLengthStrategy(ContentLengthStrategy contentLengthStrategy) {
/* 49 */     this.contentLengthStrategy = contentLengthStrategy;
/*    */   }
/*    */   
/*    */   public long determineLength(HttpMessage message) throws HttpException {
/* 53 */     long result = this.contentLengthStrategy.determineLength(message);
/* 54 */     if (result == -1L) {
/* 55 */       throw new ProtocolException("Identity transfer encoding cannot be used");
/*    */     }
/* 57 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\entity\DisallowIdentityContentLengthStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */