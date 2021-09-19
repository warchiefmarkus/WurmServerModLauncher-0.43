/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ final class ElementWrapper
/*    */   implements ParsedElementAnnotation
/*    */ {
/*    */   final Element element;
/*    */   
/*    */   public ElementWrapper(Element e) {
/* 13 */     this.element = e;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\ElementWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */