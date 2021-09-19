/*    */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.Options;
/*    */ import com.sun.tools.xjc.reader.AbstractExtensionBindingChecker;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DTDExtensionBindingChecker
/*    */   extends AbstractExtensionBindingChecker
/*    */ {
/*    */   public DTDExtensionBindingChecker(String schemaLanguage, Options options, ErrorHandler handler) {
/* 60 */     super(schemaLanguage, options, handler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean needsToBePruned(String uri) {
/* 68 */     if (uri.equals(this.schemaLanguage))
/* 69 */       return false; 
/* 70 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb"))
/* 71 */       return false; 
/* 72 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb/xjc")) {
/* 73 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 78 */     return this.enabledExtensions.contains(uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 86 */     if (!isCutting() && 
/* 87 */       !uri.equals("")) {
/*    */       
/* 89 */       checkAndEnable(uri);
/*    */       
/* 91 */       verifyTagName(uri, localName, qName);
/*    */       
/* 93 */       if (needsToBePruned(uri)) {
/* 94 */         startCutting();
/*    */       }
/*    */     } 
/*    */     
/* 98 */     super.startElement(uri, localName, qName, atts);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\DTDExtensionBindingChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */