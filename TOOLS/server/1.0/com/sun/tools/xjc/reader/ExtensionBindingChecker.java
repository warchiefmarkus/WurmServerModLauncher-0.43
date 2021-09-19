/*     */ package 1.0.com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.Messages;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
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
/*     */ public class ExtensionBindingChecker
/*     */   extends XMLFilterImpl
/*     */ {
/*  43 */   private final NamespaceSupport nsSupport = new NamespaceSupport();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private int count = 0;
/*     */ 
/*     */   
/*  51 */   private final Set enabledExtensions = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private int cutDepth = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final ContentHandler stub = new DefaultHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ContentHandler next;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String schemaLanguage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtensionBindingChecker(String schemaLanguage, ErrorHandler handler) {
/*  87 */     this.schemaLanguage = schemaLanguage;
/*  88 */     setErrorHandler(handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSupportedExtension(String namespaceUri) {
/*  97 */     return namespaceUri.equals("http://java.sun.com/xml/ns/jaxb/xjc");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needsToBePruned(String uri) {
/* 105 */     if (uri.equals(this.schemaLanguage))
/* 106 */       return false; 
/* 107 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb"))
/* 108 */       return false; 
/* 109 */     if (this.enabledExtensions.contains(uri)) {
/* 110 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     return isSupportedExtension(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 122 */     super.startDocument();
/*     */     
/* 124 */     this.count = 0;
/* 125 */     this.cutDepth = 0;
/* 126 */     this.nsSupport.reset();
/* 127 */     this.enabledExtensions.clear();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 131 */     super.startPrefixMapping(prefix, uri);
/* 132 */     this.nsSupport.pushContext();
/* 133 */     this.nsSupport.declarePrefix(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 137 */     super.endPrefixMapping(prefix);
/* 138 */     this.nsSupport.popContext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 144 */     if (this.cutDepth == 0) {
/* 145 */       String v = atts.getValue("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes");
/* 146 */       if (v != null) {
/* 147 */         if (this.count != 0)
/*     */         {
/* 149 */           error(Messages.format("ExtensionBindingChecker.UnexpectedExtensionBindingPrefixes"));
/*     */         }
/*     */         
/* 152 */         StringTokenizer tokens = new StringTokenizer(v);
/* 153 */         while (tokens.hasMoreTokens()) {
/* 154 */           String prefix = tokens.nextToken();
/* 155 */           String uri = this.nsSupport.getURI(prefix);
/* 156 */           if (uri == null) {
/*     */             
/* 158 */             error(Messages.format("ExtensionBindingChecker.UndeclaredPrefix", prefix)); continue;
/*     */           } 
/* 160 */           if (!isSupportedExtension(uri))
/*     */           {
/* 162 */             error(Messages.format("ExtensionBindingChecker.UnsupportedExtension", prefix));
/*     */           }
/* 164 */           this.enabledExtensions.add(uri);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 169 */       if (needsToBePruned(namespaceURI)) {
/*     */         
/* 171 */         if (isSupportedExtension(namespaceURI))
/*     */         {
/*     */           
/* 174 */           warning(Messages.format("ExtensionBindingChecker.SupportedExtensionIgnored", namespaceURI));
/*     */         }
/* 176 */         super.setContentHandler(stub);
/* 177 */         this.cutDepth = 1;
/*     */       } 
/*     */     } else {
/* 180 */       this.cutDepth++;
/*     */     } 
/* 182 */     this.count++;
/* 183 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 187 */     super.endElement(namespaceURI, localName, qName);
/*     */     
/* 189 */     if (this.cutDepth != 0) {
/* 190 */       this.cutDepth--;
/* 191 */       if (this.cutDepth == 0)
/*     */       {
/* 193 */         super.setContentHandler(this.next); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 198 */     super.setDocumentLocator(locator);
/* 199 */     this.locator = locator;
/*     */   }
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/* 203 */     this.next = handler;
/*     */     
/* 205 */     if (getContentHandler() != stub) {
/* 206 */       super.setContentHandler(handler);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SAXParseException error(String msg) throws SAXException {
/* 214 */     SAXParseException spe = new SAXParseException(msg, this.locator);
/* 215 */     getErrorHandler().error(spe);
/* 216 */     return spe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void warning(String msg) throws SAXException {
/* 223 */     SAXParseException spe = new SAXParseException(msg, this.locator);
/* 224 */     getErrorHandler().warning(spe);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\ExtensionBindingChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */