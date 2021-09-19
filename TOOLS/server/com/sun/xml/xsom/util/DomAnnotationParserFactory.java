/*    */ package com.sun.xml.xsom.util;
/*    */ 
/*    */ import com.sun.xml.xsom.parser.AnnotationContext;
/*    */ import com.sun.xml.xsom.parser.AnnotationParser;
/*    */ import com.sun.xml.xsom.parser.AnnotationParserFactory;
/*    */ import javax.xml.transform.TransformerConfigurationException;
/*    */ import javax.xml.transform.dom.DOMResult;
/*    */ import javax.xml.transform.sax.SAXTransformerFactory;
/*    */ import javax.xml.transform.sax.TransformerHandler;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DomAnnotationParserFactory
/*    */   implements AnnotationParserFactory
/*    */ {
/*    */   public AnnotationParser create() {
/* 35 */     return new AnnotationParserImpl();
/*    */   }
/*    */   
/* 38 */   private static final SAXTransformerFactory stf = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
/*    */ 
/*    */   
/*    */   private static class AnnotationParserImpl
/*    */     extends AnnotationParser
/*    */   {
/*    */     private final TransformerHandler transformer;
/*    */     
/*    */     private DOMResult result;
/*    */     
/*    */     AnnotationParserImpl() {
/*    */       try {
/* 50 */         this.transformer = DomAnnotationParserFactory.stf.newTransformerHandler();
/* 51 */       } catch (TransformerConfigurationException e) {
/* 52 */         throw new Error(e);
/*    */       } 
/*    */     }
/*    */     
/*    */     public ContentHandler getContentHandler(AnnotationContext context, String parentElementName, ErrorHandler errorHandler, EntityResolver entityResolver) {
/* 57 */       this.result = new DOMResult();
/* 58 */       this.transformer.setResult(this.result);
/* 59 */       return this.transformer;
/*    */     }
/*    */     
/*    */     public Object getResult(Object existing) {
/* 63 */       Document dom = (Document)this.result.getNode();
/* 64 */       Element e = dom.getDocumentElement();
/* 65 */       if (existing instanceof Element) {
/*    */         
/* 67 */         Element prev = (Element)existing;
/* 68 */         Node anchor = e.getFirstChild();
/* 69 */         while (prev.getFirstChild() != null) {
/* 70 */           Node move = prev.getFirstChild();
/* 71 */           e.insertBefore(e.getOwnerDocument().adoptNode(move), anchor);
/*    */         } 
/*    */       } 
/* 74 */       return e;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\DomAnnotationParserFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */