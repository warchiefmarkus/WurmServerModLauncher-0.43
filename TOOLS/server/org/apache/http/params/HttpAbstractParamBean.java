/*    */ package org.apache.http.params;
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
/*    */ public abstract class HttpAbstractParamBean
/*    */ {
/*    */   protected final HttpParams params;
/*    */   
/*    */   public HttpAbstractParamBean(HttpParams params) {
/* 38 */     if (params == null)
/* 39 */       throw new IllegalArgumentException("HTTP parameters may not be null"); 
/* 40 */     this.params = params;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\HttpAbstractParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */