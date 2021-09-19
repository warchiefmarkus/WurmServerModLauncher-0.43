/*    */ package com.wurmonline.shared.xml;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ import org.xml.sax.helpers.XMLReaderFactory;
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
/*    */ public final class XmlParser
/*    */   extends DefaultHandler
/*    */ {
/* 31 */   private final List<XmlNode> nodeStack = new ArrayList<>();
/* 32 */   private XmlNode rootNode = null;
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/*    */     try {
/* 38 */       XMLReaderFactory.createXMLReader();
/*    */     }
/* 40 */     catch (Exception e) {
/*    */       
/* 42 */       System.out.println("Failed to load default xml reader.. attempting org.apache.crimson.parser.XMLReaderImpl");
/* 43 */       System.setProperty("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
/*    */       
/*    */       try {
/* 46 */         XMLReaderFactory.createXMLReader();
/*    */       }
/* 48 */       catch (SAXException e1) {
/*    */         
/* 50 */         System.out.println("Failed to create XMLReader!!");
/* 51 */         e1.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static XmlNode parse(InputStream in) throws IOException, SAXException {
/* 62 */     XMLReader xmlReader = XMLReaderFactory.createXMLReader();
/* 63 */     XmlParser xmlParser = new XmlParser();
/* 64 */     xmlReader.setContentHandler(xmlParser);
/* 65 */     xmlReader.parse(new InputSource(in));
/*    */     
/* 67 */     return xmlParser.rootNode;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 73 */     if (this.nodeStack.size() > 0) {
/* 74 */       ((XmlNode)this.nodeStack.get(this.nodeStack.size() - 1)).setText(new String(ch, start, length));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 80 */     if (this.nodeStack.size() > 0) {
/* 81 */       this.nodeStack.remove(this.nodeStack.size() - 1);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 88 */     XmlNode xmlNode = new XmlNode(localName, attributes);
/* 89 */     if (this.rootNode == null) {
/* 90 */       this.rootNode = xmlNode;
/*    */     }
/* 92 */     if (this.nodeStack.size() > 0) {
/* 93 */       ((XmlNode)this.nodeStack.get(this.nodeStack.size() - 1)).addChild(xmlNode);
/*    */     }
/* 95 */     this.nodeStack.add(xmlNode);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\xml\XmlParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */