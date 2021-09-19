/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import java.util.logging.Logger;
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
/*    */ public enum Protocol
/*    */ {
/* 25 */   ALL("*"),
/* 26 */   HTTP_GET("http-get"),
/* 27 */   RTSP_RTP_UDP("rtsp-rtp-udp"),
/* 28 */   INTERNAL("internal"),
/* 29 */   IEC61883("iec61883"),
/* 30 */   XBMC_GET("xbmc-get"),
/* 31 */   OTHER("other"); private static final Logger LOG;
/*    */   static {
/* 33 */     LOG = Logger.getLogger(Protocol.class.getName());
/*    */   }
/*    */   private String protocolString;
/*    */   
/*    */   Protocol(String protocolString) {
/* 38 */     this.protocolString = protocolString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return this.protocolString;
/*    */   }
/*    */   
/*    */   public static Protocol value(String s) {
/* 47 */     for (Protocol protocol : values()) {
/* 48 */       if (protocol.toString().equals(s)) {
/* 49 */         return protocol;
/*    */       }
/*    */     } 
/* 52 */     LOG.info("Unsupported OTHER protocol string: " + s);
/* 53 */     return OTHER;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */