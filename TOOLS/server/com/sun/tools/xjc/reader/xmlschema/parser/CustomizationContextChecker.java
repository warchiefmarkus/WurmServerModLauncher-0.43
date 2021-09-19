/*     */ package com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomizationContextChecker
/*     */   extends XMLFilterImpl
/*     */ {
/* 128 */   private final Stack<QName> elementNames = new Stack<QName>();
/*     */ 
/*     */   
/*     */   private final ErrorHandler errorHandler;
/*     */   
/*     */   private Locator locator;
/*     */   
/* 135 */   private static final Set<String> prohibitedSchemaElementNames = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomizationContextChecker(ErrorHandler _errorHandler) {
/* 142 */     this.errorHandler = _errorHandler;
/*     */   }
/*     */   
/*     */   static {
/* 146 */     prohibitedSchemaElementNames.add("restriction");
/* 147 */     prohibitedSchemaElementNames.add("extension");
/* 148 */     prohibitedSchemaElementNames.add("simpleContent");
/* 149 */     prohibitedSchemaElementNames.add("complexContent");
/* 150 */     prohibitedSchemaElementNames.add("list");
/* 151 */     prohibitedSchemaElementNames.add("union");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private QName top() {
/* 159 */     return this.elementNames.peek();
/*     */   }
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 163 */     QName newElement = new QName(namespaceURI, localName);
/*     */     
/* 165 */     if (newElement.getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb") && top().getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema"))
/*     */     {
/*     */ 
/*     */       
/* 169 */       if (this.elementNames.size() >= 3) {
/*     */ 
/*     */         
/* 172 */         QName schemaElement = this.elementNames.get(this.elementNames.size() - 3);
/* 173 */         if (prohibitedSchemaElementNames.contains(schemaElement.getLocalPart()))
/*     */         {
/* 175 */           this.errorHandler.error(new SAXParseException(Messages.format("CustomizationContextChecker.UnacknolwedgedCustomization", new Object[] { localName }), this.locator));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     this.elementNames.push(newElement);
/*     */     
/* 188 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 194 */     super.endElement(namespaceURI, localName, qName);
/*     */     
/* 196 */     this.elementNames.pop();
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 200 */     super.setDocumentLocator(locator);
/* 201 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\parser\CustomizationContextChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */