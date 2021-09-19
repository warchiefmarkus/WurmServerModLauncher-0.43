/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.support.model.dlna.types.NormalPlayTime;
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
/*    */ public class RealTimeInfoHeader
/*    */   extends DLNAHeader<NormalPlayTime>
/*    */ {
/*    */   public static final String PREFIX = "DLNA.ORG_TLAG=";
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 32 */     if (s.length() != 0 && s.startsWith("DLNA.ORG_TLAG=")) {
/*    */       try {
/* 34 */         s = s.substring("DLNA.ORG_TLAG=".length());
/* 35 */         setValue(s.equals("*") ? null : NormalPlayTime.valueOf(s));
/*    */         return;
/* 37 */       } catch (Exception exception) {}
/*    */     }
/* 39 */     throw new InvalidHeaderException("Invalid RealTimeInfo header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 44 */     NormalPlayTime v = (NormalPlayTime)getValue();
/* 45 */     if (v == null)
/* 46 */       return "DLNA.ORG_TLAG=*"; 
/* 47 */     return "DLNA.ORG_TLAG=" + v.getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\RealTimeInfoHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */