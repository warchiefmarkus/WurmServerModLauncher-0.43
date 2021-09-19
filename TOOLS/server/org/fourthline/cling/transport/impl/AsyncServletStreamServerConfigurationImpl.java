/*    */ package org.fourthline.cling.transport.impl;
/*    */ 
/*    */ import org.fourthline.cling.transport.spi.ServletContainerAdapter;
/*    */ import org.fourthline.cling.transport.spi.StreamServerConfiguration;
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
/*    */ public class AsyncServletStreamServerConfigurationImpl
/*    */   implements StreamServerConfiguration
/*    */ {
/*    */   protected ServletContainerAdapter servletContainerAdapter;
/* 33 */   protected int listenPort = 0;
/* 34 */   protected int asyncTimeoutSeconds = 60;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncServletStreamServerConfigurationImpl(ServletContainerAdapter servletContainerAdapter) {
/* 40 */     this.servletContainerAdapter = servletContainerAdapter;
/*    */   }
/*    */ 
/*    */   
/*    */   public AsyncServletStreamServerConfigurationImpl(ServletContainerAdapter servletContainerAdapter, int listenPort) {
/* 45 */     this.servletContainerAdapter = servletContainerAdapter;
/* 46 */     this.listenPort = listenPort;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncServletStreamServerConfigurationImpl(ServletContainerAdapter servletContainerAdapter, int listenPort, int asyncTimeoutSeconds) {
/* 52 */     this.servletContainerAdapter = servletContainerAdapter;
/* 53 */     this.listenPort = listenPort;
/* 54 */     this.asyncTimeoutSeconds = asyncTimeoutSeconds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getListenPort() {
/* 61 */     return this.listenPort;
/*    */   }
/*    */   
/*    */   public void setListenPort(int listenPort) {
/* 65 */     this.listenPort = listenPort;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getAsyncTimeoutSeconds() {
/* 75 */     return this.asyncTimeoutSeconds;
/*    */   }
/*    */   
/*    */   public void setAsyncTimeoutSeconds(int asyncTimeoutSeconds) {
/* 79 */     this.asyncTimeoutSeconds = asyncTimeoutSeconds;
/*    */   }
/*    */   
/*    */   public ServletContainerAdapter getServletContainerAdapter() {
/* 83 */     return this.servletContainerAdapter;
/*    */   }
/*    */   
/*    */   public void setServletContainerAdapter(ServletContainerAdapter servletContainerAdapter) {
/* 87 */     this.servletContainerAdapter = servletContainerAdapter;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\AsyncServletStreamServerConfigurationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */