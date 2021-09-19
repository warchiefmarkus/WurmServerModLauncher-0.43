/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.types.HostPort;
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
/*    */ public class HostHeader
/*    */   extends UpnpHeader<HostPort>
/*    */ {
/* 26 */   int port = 1900;
/* 27 */   String group = "239.255.255.250";
/*    */   
/*    */   public HostHeader() {
/* 30 */     setValue(new HostPort(this.group, this.port));
/*    */   }
/*    */   
/*    */   public HostHeader(int port) {
/* 34 */     setValue(new HostPort(this.group, port));
/*    */   }
/*    */   
/*    */   public HostHeader(String host, int port) {
/* 38 */     setValue(new HostPort(host, port));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 43 */     if (s.contains(":")) {
/*    */       
/*    */       try {
/* 46 */         this.port = Integer.valueOf(s.substring(s.indexOf(":") + 1)).intValue();
/* 47 */         this.group = s.substring(0, s.indexOf(":"));
/* 48 */         setValue(new HostPort(this.group, this.port));
/* 49 */       } catch (NumberFormatException ex) {
/* 50 */         throw new InvalidHeaderException("Invalid HOST header value, can't parse port: " + s + " - " + ex.getMessage());
/*    */       } 
/*    */     } else {
/* 53 */       this.group = s;
/* 54 */       setValue(new HostPort(this.group, this.port));
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 59 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\HostHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */