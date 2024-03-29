/*    */ package com.sun.dtdparser;
/*    */ 
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class DTDHandlerBase
/*    */   implements DTDEventListener
/*    */ {
/*    */   public void processingInstruction(String target, String data) throws SAXException {}
/*    */   
/*    */   public void setDocumentLocator(Locator loc) {}
/*    */   
/*    */   public void fatalError(SAXParseException e) throws SAXException {
/* 26 */     throw e;
/*    */   }
/*    */   
/*    */   public void error(SAXParseException e) throws SAXException {
/* 30 */     throw e;
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException err) throws SAXException {}
/*    */   
/*    */   public void notationDecl(String name, String publicId, String systemId) throws SAXException {}
/*    */   
/*    */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {}
/*    */   
/*    */   public void endDTD() throws SAXException {}
/*    */   
/*    */   public void externalGeneralEntityDecl(String n, String p, String s) throws SAXException {}
/*    */   
/*    */   public void internalGeneralEntityDecl(String n, String v) throws SAXException {}
/*    */   
/*    */   public void externalParameterEntityDecl(String n, String p, String s) throws SAXException {}
/*    */   
/*    */   public void internalParameterEntityDecl(String n, String v) throws SAXException {}
/*    */   
/*    */   public void startDTD(InputEntity in) throws SAXException {}
/*    */   
/*    */   public void comment(String n) throws SAXException {}
/*    */   
/*    */   public void characters(char[] ch, int start, int length) throws SAXException {}
/*    */   
/*    */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*    */   
/*    */   public void startCDATA() throws SAXException {}
/*    */   
/*    */   public void endCDATA() throws SAXException {}
/*    */   
/*    */   public void startContentModel(String elementName, short contentModelType) throws SAXException {}
/*    */   
/*    */   public void endContentModel(String elementName, short contentModelType) throws SAXException {}
/*    */   
/*    */   public void attributeDecl(String elementName, String attributeName, String attributeType, String[] enumeration, short attributeUse, String defaultValue) throws SAXException {}
/*    */   
/*    */   public void childElement(String elementName, short occurence) throws SAXException {}
/*    */   
/*    */   public void mixedElement(String elementName) throws SAXException {}
/*    */   
/*    */   public void startModelGroup() throws SAXException {}
/*    */   
/*    */   public void endModelGroup(short occurence) throws SAXException {}
/*    */   
/*    */   public void connector(short connectorType) throws SAXException {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\dtdparser\DTDHandlerBase.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */