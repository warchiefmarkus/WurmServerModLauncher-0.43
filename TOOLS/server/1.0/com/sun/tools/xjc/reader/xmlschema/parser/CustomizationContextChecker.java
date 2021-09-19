/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.msv.util.StringPair;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.Messages;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
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
/*     */ public class CustomizationContextChecker
/*     */   extends XMLFilterImpl
/*     */ {
/*  95 */   private final Stack elementNames = new Stack();
/*     */ 
/*     */   
/*     */   private final ErrorHandler errorHandler;
/*     */   
/*     */   private Locator locator;
/*     */   
/* 102 */   private static final Set prohibitedSchemaElementNames = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomizationContextChecker(ErrorHandler _errorHandler) {
/* 109 */     this.errorHandler = _errorHandler;
/*     */   }
/*     */   
/*     */   static {
/* 113 */     prohibitedSchemaElementNames.add("restriction");
/* 114 */     prohibitedSchemaElementNames.add("extension");
/* 115 */     prohibitedSchemaElementNames.add("simpleContent");
/* 116 */     prohibitedSchemaElementNames.add("complexContent");
/* 117 */     prohibitedSchemaElementNames.add("list");
/* 118 */     prohibitedSchemaElementNames.add("union");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringPair top() {
/* 126 */     return this.elementNames.peek();
/*     */   }
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 130 */     StringPair newElement = new StringPair(namespaceURI, localName);
/*     */     
/* 132 */     if (newElement.namespaceURI.equals("http://java.sun.com/xml/ns/jaxb") && (top()).namespaceURI.equals("http://www.w3.org/2001/XMLSchema"))
/*     */     {
/*     */ 
/*     */       
/* 136 */       if (this.elementNames.size() >= 3) {
/*     */ 
/*     */         
/* 139 */         StringPair schemaElement = this.elementNames.get(this.elementNames.size() - 3);
/* 140 */         if (prohibitedSchemaElementNames.contains(schemaElement.localName))
/*     */         {
/* 142 */           this.errorHandler.error(new SAXParseException(Messages.format("CustomizationContextChecker.UnacknolwedgedCustomization", localName), this.locator));
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
/* 153 */     this.elementNames.push(newElement);
/*     */     
/* 155 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 161 */     super.endElement(namespaceURI, localName, qName);
/*     */     
/* 163 */     this.elementNames.pop();
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 167 */     super.setDocumentLocator(locator);
/* 168 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\parser\CustomizationContextChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */