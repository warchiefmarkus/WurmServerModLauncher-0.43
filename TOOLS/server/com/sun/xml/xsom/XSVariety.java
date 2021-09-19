/*    */ package com.sun.xml.xsom;
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
/*    */ public final class XSVariety
/*    */ {
/* 29 */   public static final XSVariety ATOMIC = new XSVariety("atomic");
/* 30 */   public static final XSVariety UNION = new XSVariety("union"); private final String name;
/* 31 */   public static final XSVariety LIST = new XSVariety("list");
/*    */   private XSVariety(String _name) {
/* 33 */     this.name = _name;
/*    */   } public String toString() {
/* 35 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSVariety.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */