/*    */ package com.sun.tools.xjc.runtime;
/*    */ 
/*    */ import javax.xml.bind.DatatypeConverter;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*    */ public class ZeroOneBooleanAdapter
/*    */   extends XmlAdapter<String, Boolean>
/*    */ {
/*    */   public Boolean unmarshal(String v) {
/* 50 */     if (v == null) return null; 
/* 51 */     return Boolean.valueOf(DatatypeConverter.parseBoolean(v));
/*    */   }
/*    */   
/*    */   public String marshal(Boolean v) {
/* 55 */     if (v == null) return null; 
/* 56 */     if (v.booleanValue()) {
/* 57 */       return "1";
/*    */     }
/* 59 */     return "0";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\runtime\ZeroOneBooleanAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */