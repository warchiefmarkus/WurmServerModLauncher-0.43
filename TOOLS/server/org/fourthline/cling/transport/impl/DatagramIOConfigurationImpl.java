/*    */ package org.fourthline.cling.transport.impl;
/*    */ 
/*    */ import org.fourthline.cling.transport.spi.DatagramIOConfiguration;
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
/*    */ public class DatagramIOConfigurationImpl
/*    */   implements DatagramIOConfiguration
/*    */ {
/* 27 */   private int timeToLive = 4;
/* 28 */   private int maxDatagramBytes = 640;
/*    */ 
/*    */ 
/*    */   
/*    */   public DatagramIOConfigurationImpl() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public DatagramIOConfigurationImpl(int timeToLive, int maxDatagramBytes) {
/* 37 */     this.timeToLive = timeToLive;
/* 38 */     this.maxDatagramBytes = maxDatagramBytes;
/*    */   }
/*    */   
/*    */   public int getTimeToLive() {
/* 42 */     return this.timeToLive;
/*    */   }
/*    */   
/*    */   public void setTimeToLive(int timeToLive) {
/* 46 */     this.timeToLive = timeToLive;
/*    */   }
/*    */   
/*    */   public int getMaxDatagramBytes() {
/* 50 */     return this.maxDatagramBytes;
/*    */   }
/*    */   
/*    */   public void setMaxDatagramBytes(int maxDatagramBytes) {
/* 54 */     this.maxDatagramBytes = maxDatagramBytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\DatagramIOConfigurationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */