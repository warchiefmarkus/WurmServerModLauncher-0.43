/*    */ package org.apache.http.conn.params;
/*    */ 
/*    */ import org.apache.http.params.HttpAbstractParamBean;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ @Deprecated
/*    */ public class ConnConnectionParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public ConnConnectionParamBean(HttpParams params) {
/* 47 */     super(params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMaxStatusLineGarbage(int maxStatusLineGarbage) {
/* 54 */     this.params.setIntParameter("http.connection.max-status-line-garbage", maxStatusLineGarbage);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\params\ConnConnectionParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */