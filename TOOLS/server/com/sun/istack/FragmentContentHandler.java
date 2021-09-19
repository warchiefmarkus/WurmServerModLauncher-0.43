/*    */ package com.sun.istack;
/*    */ 
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.XMLFilterImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FragmentContentHandler
/*    */   extends XMLFilterImpl
/*    */ {
/*    */   public FragmentContentHandler() {}
/*    */   
/*    */   public FragmentContentHandler(XMLReader parent) {
/* 17 */     super(parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public FragmentContentHandler(ContentHandler handler) {
/* 22 */     setContentHandler(handler);
/*    */   }
/*    */   
/*    */   public void startDocument() throws SAXException {}
/*    */   
/*    */   public void endDocument() throws SAXException {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\FragmentContentHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */