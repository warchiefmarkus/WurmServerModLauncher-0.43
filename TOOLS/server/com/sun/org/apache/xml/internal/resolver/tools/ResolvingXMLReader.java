/*    */ package com.sun.org.apache.xml.internal.resolver.tools;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
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
/*    */ public class ResolvingXMLReader
/*    */   extends ResolvingXMLFilter
/*    */ {
/*    */   public static boolean namespaceAware = true;
/*    */   public static boolean validating = false;
/*    */   
/*    */   public ResolvingXMLReader() {
/* 60 */     SAXParserFactory spf = SAXParserFactory.newInstance();
/* 61 */     spf.setNamespaceAware(namespaceAware);
/* 62 */     spf.setValidating(validating);
/*    */     try {
/* 64 */       SAXParser parser = spf.newSAXParser();
/* 65 */       setParent(parser.getXMLReader());
/* 66 */     } catch (Exception ex) {
/* 67 */       ex.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResolvingXMLReader(CatalogManager manager) {
/* 79 */     super(manager);
/* 80 */     SAXParserFactory spf = SAXParserFactory.newInstance();
/* 81 */     spf.setNamespaceAware(namespaceAware);
/* 82 */     spf.setValidating(validating);
/*    */     try {
/* 84 */       SAXParser parser = spf.newSAXParser();
/* 85 */       setParent(parser.getXMLReader());
/* 86 */     } catch (Exception ex) {
/* 87 */       ex.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\tools\ResolvingXMLReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */