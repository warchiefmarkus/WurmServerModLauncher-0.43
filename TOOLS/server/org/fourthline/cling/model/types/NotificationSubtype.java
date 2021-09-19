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
/*    */ public enum NotificationSubtype
/*    */ {
/* 25 */   ALIVE("ssdp:alive"),
/* 26 */   UPDATE("ssdp:update"),
/* 27 */   BYEBYE("ssdp:byebye"),
/* 28 */   ALL("ssdp:all"),
/* 29 */   DISCOVER("ssdp:discover"),
/* 30 */   PROPCHANGE("upnp:propchange");
/*    */   
/*    */   private String headerString;
/*    */   
/*    */   NotificationSubtype(String headerString) {
/* 35 */     this.headerString = headerString;
/*    */   }
/*    */   
/*    */   public String getHeaderString() {
/* 39 */     return this.headerString;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\NotificationSubtype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */