/*     */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.internalizer.Messages;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.LocatorImpl;
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
/*     */ class VersionChecker
/*     */   extends XMLFilterImpl
/*     */ {
/*  32 */   private String version = null;
/*     */ 
/*     */   
/*     */   private boolean seenRoot = false;
/*     */ 
/*     */   
/*     */   private boolean seenBindings = false;
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */   
/*     */   private Locator rootTagStart;
/*     */ 
/*     */   
/*     */   public VersionChecker(XMLReader parent) {
/*  48 */     setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  54 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  56 */     if (!this.seenRoot) {
/*     */       
/*  58 */       this.seenRoot = true;
/*  59 */       this.rootTagStart = new LocatorImpl(this.locator);
/*     */       
/*  61 */       this.version = atts.getValue("http://java.sun.com/xml/ns/jaxb", "version");
/*  62 */       if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxb")) {
/*  63 */         String version2 = atts.getValue("", "version");
/*  64 */         if (this.version != null && version2 != null) {
/*     */           
/*  66 */           SAXParseException e = new SAXParseException(Messages.format("Internalizer.TwoVersionAttributes"), this.locator);
/*     */           
/*  68 */           getErrorHandler().error(e);
/*     */         } 
/*  70 */         if (this.version == null) {
/*  71 */           this.version = version2;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  76 */     if ("http://java.sun.com/xml/ns/jaxb".equals(namespaceURI))
/*  77 */       this.seenBindings = true; 
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/*  81 */     super.endDocument();
/*     */     
/*  83 */     if (this.seenBindings && this.version == null) {
/*     */       
/*  85 */       SAXParseException e = new SAXParseException(Messages.format("Internalizer.VersionNotPresent"), this.rootTagStart);
/*     */       
/*  87 */       getErrorHandler().error(e);
/*     */     } 
/*     */ 
/*     */     
/*  91 */     if (this.version != null && !this.version.equals("1.0")) {
/*  92 */       SAXParseException e = new SAXParseException(Messages.format("Internalizer.IncorrectVersion"), this.rootTagStart);
/*     */       
/*  94 */       getErrorHandler().error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/*  99 */     super.setDocumentLocator(locator);
/* 100 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\VersionChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */