/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlEnum;
/*    */ import javax.xml.bind.annotation.XmlEnumValue;
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
/*    */ @XmlEnum
/*    */ public enum EnumMemberMode
/*    */ {
/* 49 */   SKIP,
/*    */   
/* 51 */   ERROR,
/*    */   
/* 53 */   GENERATE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumMemberMode getModeWithEnum() {
/* 62 */     if (this == SKIP) return ERROR; 
/* 63 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\EnumMemberMode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */