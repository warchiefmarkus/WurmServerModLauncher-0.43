/*     */ package com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.Plugin;
/*     */ import com.sun.tools.xjc.util.SubtreeCutter;
/*     */ import com.sun.xml.bind.v2.util.EditDistance;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
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
/*     */ public abstract class AbstractExtensionBindingChecker
/*     */   extends SubtreeCutter
/*     */ {
/*  60 */   protected final NamespaceSupport nsSupport = new NamespaceSupport();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected final Set<String> enabledExtensions = new HashSet<String>();
/*     */   
/*  67 */   private final Set<String> recognizableExtensions = new HashSet<String>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String schemaLanguage;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean allowExtensions;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Options options;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractExtensionBindingChecker(String schemaLanguage, Options options, ErrorHandler handler) {
/*  89 */     this.schemaLanguage = schemaLanguage;
/*  90 */     this.allowExtensions = (options.compatibilityMode != 1);
/*  91 */     this.options = options;
/*  92 */     setErrorHandler(handler);
/*     */     
/*  94 */     for (Plugin plugin : options.getAllPlugins())
/*  95 */       this.recognizableExtensions.addAll(plugin.getCustomizationURIs()); 
/*  96 */     this.recognizableExtensions.add("http://java.sun.com/xml/ns/jaxb/xjc");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void checkAndEnable(String uri) throws SAXException {
/* 106 */     if (!isRecognizableExtension(uri)) {
/* 107 */       String nearest = EditDistance.findNearest(uri, this.recognizableExtensions);
/*     */       
/* 109 */       error(Messages.ERR_UNSUPPORTED_EXTENSION.format(new Object[] { uri, nearest }));
/*     */     }
/* 111 */     else if (!isSupportedExtension(uri)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       Plugin owner = null;
/* 117 */       for (Plugin p : this.options.getAllPlugins()) {
/* 118 */         if (p.getCustomizationURIs().contains(uri)) {
/* 119 */           owner = p;
/*     */           break;
/*     */         } 
/*     */       } 
/* 123 */       if (owner != null) {
/*     */         
/* 125 */         error(Messages.ERR_PLUGIN_NOT_ENABLED.format(new Object[] { owner.getOptionName(), uri }));
/*     */       } else {
/*     */         
/* 128 */         error(Messages.ERR_UNSUPPORTED_EXTENSION.format(new Object[] { uri }));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 133 */     this.enabledExtensions.add(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void verifyTagName(String namespaceURI, String localName, String qName) throws SAXException {
/* 141 */     if (this.options.pluginURIs.contains(namespaceURI)) {
/*     */       
/* 143 */       boolean correct = false;
/* 144 */       for (Plugin p : this.options.activePlugins) {
/* 145 */         if (p.isCustomizationTagName(namespaceURI, localName)) {
/* 146 */           correct = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 150 */       if (!correct) {
/* 151 */         error(Messages.ERR_ILLEGAL_CUSTOMIZATION_TAGNAME.format(new Object[] { qName }));
/* 152 */         startCutting();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isSupportedExtension(String namespaceUri) {
/* 162 */     return (namespaceUri.equals("http://java.sun.com/xml/ns/jaxb/xjc") || this.options.pluginURIs.contains(namespaceUri));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isRecognizableExtension(String namespaceUri) {
/* 170 */     return this.recognizableExtensions.contains(namespaceUri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 175 */     super.setDocumentLocator(locator);
/* 176 */     this.locator = locator;
/*     */   }
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 180 */     super.startDocument();
/*     */     
/* 182 */     this.nsSupport.reset();
/* 183 */     this.enabledExtensions.clear();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 187 */     super.startPrefixMapping(prefix, uri);
/* 188 */     this.nsSupport.pushContext();
/* 189 */     this.nsSupport.declarePrefix(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 193 */     super.endPrefixMapping(prefix);
/* 194 */     this.nsSupport.popContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SAXParseException error(String msg) throws SAXException {
/* 202 */     SAXParseException spe = new SAXParseException(msg, this.locator);
/* 203 */     getErrorHandler().error(spe);
/* 204 */     return spe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void warning(String msg) throws SAXException {
/* 211 */     SAXParseException spe = new SAXParseException(msg, this.locator);
/* 212 */     getErrorHandler().warning(spe);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\AbstractExtensionBindingChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */