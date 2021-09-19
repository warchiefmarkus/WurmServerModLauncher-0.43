/*    */ package com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.xsom.XSWildcard;
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
/*    */ 
/*    */ 
/*    */ final class GWildcardElement
/*    */   extends GElement
/*    */ {
/*    */   private boolean strict = true;
/*    */   
/*    */   public String toString() {
/* 57 */     return "#any";
/*    */   }
/*    */   
/*    */   String getPropertyNameSeed() {
/* 61 */     return "any";
/*    */   }
/*    */   
/*    */   public void merge(XSWildcard wc) {
/* 65 */     switch (wc.getMode()) {
/*    */       case 1:
/*    */       case 3:
/* 68 */         this.strict = false;
/*    */         break;
/*    */     } 
/*    */   }
/*    */   public boolean isStrict() {
/* 73 */     return this.strict;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\GWildcardElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */