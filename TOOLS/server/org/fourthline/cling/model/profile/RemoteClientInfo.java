/*     */ package org.fourthline.cling.model.profile;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.fourthline.cling.model.message.Connection;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.message.header.UserAgentHeader;
/*     */ import org.seamless.http.RequestInfo;
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
/*     */ public class RemoteClientInfo
/*     */   extends ClientInfo
/*     */ {
/*     */   protected final Connection connection;
/*  40 */   protected final UpnpHeaders extraResponseHeaders = new UpnpHeaders();
/*     */   
/*     */   public RemoteClientInfo() {
/*  43 */     this((StreamRequestMessage)null);
/*     */   }
/*     */   
/*     */   public RemoteClientInfo(StreamRequestMessage requestMessage) {
/*  47 */     this((requestMessage != null) ? requestMessage.getConnection() : null, (requestMessage != null) ? requestMessage
/*  48 */         .getHeaders() : new UpnpHeaders());
/*     */   }
/*     */   
/*     */   public RemoteClientInfo(Connection connection, UpnpHeaders requestHeaders) {
/*  52 */     super(requestHeaders);
/*  53 */     this.connection = connection;
/*     */   }
/*     */   
/*     */   public Connection getConnection() {
/*  57 */     return this.connection;
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
/*     */ 
/*     */   
/*     */   public boolean isRequestCancelled() {
/*  74 */     return !getConnection().isOpen();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void throwIfRequestCancelled() throws InterruptedException {
/*  81 */     if (isRequestCancelled())
/*  82 */       throw new InterruptedException("Client's request cancelled"); 
/*     */   }
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/*  86 */     return getConnection().getRemoteAddress();
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/*  90 */     return getConnection().getLocalAddress();
/*     */   }
/*     */   
/*     */   public UpnpHeaders getExtraResponseHeaders() {
/*  94 */     return this.extraResponseHeaders;
/*     */   }
/*     */   
/*     */   public void setResponseUserAgent(String userAgent) {
/*  98 */     setResponseUserAgent(new UserAgentHeader(userAgent));
/*     */   }
/*     */   
/*     */   public void setResponseUserAgent(UserAgentHeader userAgentHeader) {
/* 102 */     getExtraResponseHeaders().add(UpnpHeader.Type.USER_AGENT, (UpnpHeader)userAgentHeader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWMPRequest() {
/* 110 */     return RequestInfo.isWMPRequest(getRequestUserAgent());
/*     */   }
/*     */   
/*     */   public boolean isXbox360Request() {
/* 114 */     return RequestInfo.isXbox360Request(
/* 115 */         getRequestUserAgent(), 
/* 116 */         getRequestHeaders().getFirstHeaderString(UpnpHeader.Type.SERVER));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPS3Request() {
/* 121 */     return RequestInfo.isPS3Request(
/* 122 */         getRequestUserAgent(), 
/* 123 */         getRequestHeaders().getFirstHeaderString(UpnpHeader.Type.EXT_AV_CLIENT_INFO));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     return "(" + getClass().getSimpleName() + ") Remote Address: " + getRemoteAddress();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\profile\RemoteClientInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */