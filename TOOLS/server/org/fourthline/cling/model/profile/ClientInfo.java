/*    */ package org.fourthline.cling.model.profile;
/*    */ 
/*    */ import org.fourthline.cling.model.message.UpnpHeaders;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.message.header.UserAgentHeader;
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
/*    */ public class ClientInfo
/*    */ {
/*    */   protected final UpnpHeaders requestHeaders;
/*    */   
/*    */   public ClientInfo() {
/* 32 */     this(new UpnpHeaders());
/*    */   }
/*    */   
/*    */   public ClientInfo(UpnpHeaders requestHeaders) {
/* 36 */     this.requestHeaders = requestHeaders;
/*    */   }
/*    */   
/*    */   public UpnpHeaders getRequestHeaders() {
/* 40 */     return this.requestHeaders;
/*    */   }
/*    */   
/*    */   public String getRequestUserAgent() {
/* 44 */     return getRequestHeaders().getFirstHeaderString(UpnpHeader.Type.USER_AGENT);
/*    */   }
/*    */   
/*    */   public void setRequestUserAgent(String userAgent) {
/* 48 */     getRequestHeaders().add(UpnpHeader.Type.USER_AGENT, (UpnpHeader)new UserAgentHeader(userAgent));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return "(" + getClass().getSimpleName() + ") User-Agent: " + getRequestUserAgent();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\profile\ClientInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */