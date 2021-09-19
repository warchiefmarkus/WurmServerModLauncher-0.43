/*     */ package com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.tools.xjc.Options;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public final class ExtensionBindingChecker
/*     */   extends AbstractExtensionBindingChecker
/*     */ {
/*  74 */   private int count = 0;
/*     */   
/*     */   public ExtensionBindingChecker(String schemaLanguage, Options options, ErrorHandler handler) {
/*  77 */     super(schemaLanguage, options, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needsToBePruned(String uri) {
/*  85 */     if (uri.equals(this.schemaLanguage))
/*  86 */       return false; 
/*  87 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb"))
/*  88 */       return false; 
/*  89 */     if (this.enabledExtensions.contains(uri)) {
/*  90 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     return isRecognizableExtension(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 101 */     super.startDocument();
/* 102 */     this.count = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 108 */     if (!isCutting()) {
/* 109 */       String v = atts.getValue("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes");
/* 110 */       if (v != null) {
/* 111 */         if (this.count != 0)
/*     */         {
/* 113 */           error(Messages.ERR_UNEXPECTED_EXTENSION_BINDING_PREFIXES.format(new Object[0]));
/*     */         }
/* 115 */         if (!this.allowExtensions) {
/* 116 */           error(Messages.ERR_VENDOR_EXTENSION_DISALLOWED_IN_STRICT_MODE.format(new Object[0]));
/*     */         }
/*     */         
/* 119 */         StringTokenizer tokens = new StringTokenizer(v);
/* 120 */         while (tokens.hasMoreTokens()) {
/* 121 */           String prefix = tokens.nextToken();
/* 122 */           String uri = this.nsSupport.getURI(prefix);
/* 123 */           if (uri == null) {
/*     */             
/* 125 */             error(Messages.ERR_UNDECLARED_PREFIX.format(new Object[] { prefix })); continue;
/*     */           } 
/* 127 */           checkAndEnable(uri);
/*     */         } 
/*     */       } 
/*     */       
/* 131 */       if (needsToBePruned(namespaceURI)) {
/*     */         
/* 133 */         if (isRecognizableExtension(namespaceURI))
/*     */         {
/*     */           
/* 136 */           warning(Messages.ERR_SUPPORTED_EXTENSION_IGNORED.format(new Object[] { namespaceURI }));
/*     */         }
/* 138 */         startCutting();
/*     */       } else {
/* 140 */         verifyTagName(namespaceURI, localName, qName);
/*     */       } 
/*     */     } 
/* 143 */     this.count++;
/* 144 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\ExtensionBindingChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */