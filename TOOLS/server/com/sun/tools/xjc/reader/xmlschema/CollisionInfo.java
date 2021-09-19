/*    */ package com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import org.xml.sax.Locator;
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
/*    */ final class CollisionInfo
/*    */ {
/*    */   private final String name;
/*    */   private final Locator source1;
/*    */   private final Locator source2;
/*    */   
/*    */   public CollisionInfo(String name, Locator source1, Locator source2) {
/* 56 */     this.name = name;
/* 57 */     this.source1 = source1;
/* 58 */     this.source2 = source2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return Messages.format("CollisionInfo.CollisionInfo", new Object[] { this.name, printLocator(this.source1), printLocator(this.source2) });
/*    */   }
/*    */ 
/*    */   
/*    */   private String printLocator(Locator loc) {
/* 70 */     if (loc == null) return "";
/*    */     
/* 72 */     int line = loc.getLineNumber();
/* 73 */     String sysId = loc.getSystemId();
/* 74 */     if (sysId == null) sysId = Messages.format("CollisionInfo.UnknownFile", new Object[0]);
/*    */     
/* 76 */     if (line != -1) {
/* 77 */       return Messages.format("CollisionInfo.LineXOfY", new Object[] { Integer.toString(line), sysId });
/*    */     }
/*    */     
/* 80 */     return sysId;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\CollisionInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */