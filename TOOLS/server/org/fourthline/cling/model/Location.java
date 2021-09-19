/*    */ package org.fourthline.cling.model;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.URL;
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
/*    */ public class Location
/*    */ {
/*    */   protected final NetworkAddress networkAddress;
/*    */   protected final String path;
/*    */   protected final URL url;
/*    */   
/*    */   public Location(NetworkAddress networkAddress, String path) {
/* 41 */     this.networkAddress = networkAddress;
/* 42 */     this.path = path;
/* 43 */     this.url = createAbsoluteURL(networkAddress.getAddress(), networkAddress.getPort(), path);
/*    */   }
/*    */   
/*    */   public NetworkAddress getNetworkAddress() {
/* 47 */     return this.networkAddress;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 51 */     return this.path;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 56 */     if (this == o) return true; 
/* 57 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 59 */     Location location = (Location)o;
/*    */     
/* 61 */     if (!this.networkAddress.equals(location.networkAddress)) return false; 
/* 62 */     if (!this.path.equals(location.path)) return false;
/*    */     
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     int result = this.networkAddress.hashCode();
/* 70 */     result = 31 * result + this.path.hashCode();
/* 71 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URL getURL() {
/* 78 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   private static URL createAbsoluteURL(InetAddress address, int localStreamPort, String path) {
/*    */     try {
/* 84 */       return new URL("http", address.getHostAddress(), localStreamPort, path);
/* 85 */     } catch (Exception ex) {
/* 86 */       throw new IllegalArgumentException("Address, port, and URI can not be converted to URL", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\Location.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */