/*    */ package org.fourthline.cling.model;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.util.Arrays;
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
/*    */ public class NetworkAddress
/*    */ {
/*    */   protected InetAddress address;
/*    */   protected int port;
/*    */   protected byte[] hardwareAddress;
/*    */   
/*    */   public NetworkAddress(InetAddress address, int port) {
/* 33 */     this(address, port, null);
/*    */   }
/*    */   
/*    */   public NetworkAddress(InetAddress address, int port, byte[] hardwareAddress) {
/* 37 */     this.address = address;
/* 38 */     this.port = port;
/* 39 */     this.hardwareAddress = hardwareAddress;
/*    */   }
/*    */   
/*    */   public InetAddress getAddress() {
/* 43 */     return this.address;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 47 */     return this.port;
/*    */   }
/*    */   
/*    */   public byte[] getHardwareAddress() {
/* 51 */     return this.hardwareAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 56 */     if (this == o) return true; 
/* 57 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 59 */     NetworkAddress that = (NetworkAddress)o;
/*    */     
/* 61 */     if (this.port != that.port) return false; 
/* 62 */     if (!this.address.equals(that.address)) return false; 
/* 63 */     if (!Arrays.equals(this.hardwareAddress, that.hardwareAddress)) return false;
/*    */     
/* 65 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     int result = this.address.hashCode();
/* 71 */     result = 31 * result + this.port;
/* 72 */     result = 31 * result + ((this.hardwareAddress != null) ? Arrays.hashCode(this.hardwareAddress) : 0);
/* 73 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\NetworkAddress.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */