/*    */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.xml.bind.marshaller.SAX2DOMEx;
/*    */ import org.xml.sax.Attributes;
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
/*    */ final class DOMBuilder
/*    */   extends SAX2DOMEx
/*    */ {
/*    */   private Locator locator;
/*    */   
/*    */   public void setDocumentLocator(Locator locator) {
/* 56 */     super.setDocumentLocator(locator);
/* 57 */     this.locator = locator;
/*    */   }
/*    */   
/*    */   public void startElement(String namespace, String localName, String qName, Attributes attrs) {
/* 61 */     super.startElement(namespace, localName, qName, attrs);
/* 62 */     DOMLocator.setLocationInfo(getCurrentElement(), this.locator);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\DOMBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */