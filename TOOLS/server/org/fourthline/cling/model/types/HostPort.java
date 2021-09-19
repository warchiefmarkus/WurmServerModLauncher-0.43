/*    */ package org.fourthline.cling.model.types;
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
/*    */ public class HostPort
/*    */ {
/*    */   private String host;
/*    */   private int port;
/*    */   
/*    */   public HostPort() {}
/*    */   
/*    */   public HostPort(String host, int port) {
/* 32 */     this.host = host;
/* 33 */     this.port = port;
/*    */   }
/*    */   
/*    */   public String getHost() {
/* 37 */     return this.host;
/*    */   }
/*    */   
/*    */   public void setHost(String host) {
/* 41 */     this.host = host;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 45 */     return this.port;
/*    */   }
/*    */   
/*    */   public void setPort(int port) {
/* 49 */     this.port = port;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 54 */     if (this == o) return true; 
/* 55 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 57 */     HostPort hostPort = (HostPort)o;
/*    */     
/* 59 */     if (this.port != hostPort.port) return false; 
/* 60 */     if (!this.host.equals(hostPort.host)) return false;
/*    */     
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 67 */     int result = this.host.hashCode();
/* 68 */     result = 31 * result + this.port;
/* 69 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return this.host + ":" + this.port;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\HostPort.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */