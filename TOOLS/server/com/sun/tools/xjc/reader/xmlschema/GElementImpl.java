/*    */ package com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.xsom.XSElementDecl;
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class GElementImpl
/*    */   extends GElement
/*    */ {
/*    */   public final QName tagName;
/*    */   public final XSElementDecl decl;
/*    */   
/*    */   public GElementImpl(QName tagName, XSElementDecl decl) {
/* 67 */     this.tagName = tagName;
/* 68 */     this.decl = decl;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 72 */     return this.tagName.toString();
/*    */   }
/*    */   
/*    */   String getPropertyNameSeed() {
/* 76 */     return this.tagName.getLocalPart();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\GElementImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */