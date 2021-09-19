/*    */ package org.apache.http.params;
/*    */ 
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.ProtocolVersion;
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
/*    */ public class HttpProtocolParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public HttpProtocolParamBean(HttpParams params) {
/* 42 */     super(params);
/*    */   }
/*    */   
/*    */   public void setHttpElementCharset(String httpElementCharset) {
/* 46 */     HttpProtocolParams.setHttpElementCharset(this.params, httpElementCharset);
/*    */   }
/*    */   
/*    */   public void setContentCharset(String contentCharset) {
/* 50 */     HttpProtocolParams.setContentCharset(this.params, contentCharset);
/*    */   }
/*    */   
/*    */   public void setVersion(HttpVersion version) {
/* 54 */     HttpProtocolParams.setVersion(this.params, (ProtocolVersion)version);
/*    */   }
/*    */   
/*    */   public void setUserAgent(String userAgent) {
/* 58 */     HttpProtocolParams.setUserAgent(this.params, userAgent);
/*    */   }
/*    */   
/*    */   public void setUseExpectContinue(boolean useExpectContinue) {
/* 62 */     HttpProtocolParams.setUseExpectContinue(this.params, useExpectContinue);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\HttpProtocolParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */