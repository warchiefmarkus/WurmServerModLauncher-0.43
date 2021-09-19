/*     */ package com.sun.xml.bind.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx;
/*     */ import java.util.Enumeration;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DOMScanner
/*     */   implements LocatorEx, InfosetScanner
/*     */ {
/*  76 */   private Node currentNode = null;
/*     */ 
/*     */   
/*  79 */   private final AttributesImpl atts = new AttributesImpl();
/*     */ 
/*     */   
/*  82 */   private ContentHandler receiver = null;
/*     */   
/*  84 */   private Locator locator = (Locator)this;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocator(Locator loc) {
/*  94 */     this.locator = loc;
/*     */   }
/*     */   
/*     */   public void scan(Object node) throws SAXException {
/*  98 */     if (node instanceof Document) {
/*  99 */       scan((Document)node);
/*     */     } else {
/* 101 */       scan((Element)node);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void scan(Document doc) throws SAXException {
/* 106 */     scan(doc.getDocumentElement());
/*     */   }
/*     */   
/*     */   public void scan(Element e) throws SAXException {
/* 110 */     setCurrentLocation(e);
/*     */     
/* 112 */     this.receiver.setDocumentLocator(this.locator);
/* 113 */     this.receiver.startDocument();
/*     */     
/* 115 */     NamespaceSupport nss = new NamespaceSupport();
/* 116 */     buildNamespaceSupport(nss, e.getParentNode());
/*     */     
/* 118 */     for (Enumeration<String> enumeration1 = nss.getPrefixes(); enumeration1.hasMoreElements(); ) {
/* 119 */       String prefix = enumeration1.nextElement();
/* 120 */       this.receiver.startPrefixMapping(prefix, nss.getURI(prefix));
/*     */     } 
/*     */     
/* 123 */     visit(e);
/*     */     
/* 125 */     for (Enumeration<String> en = nss.getPrefixes(); en.hasMoreElements(); ) {
/* 126 */       String prefix = en.nextElement();
/* 127 */       this.receiver.endPrefixMapping(prefix);
/*     */     } 
/*     */ 
/*     */     
/* 131 */     setCurrentLocation(e);
/* 132 */     this.receiver.endDocument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(Element e, ContentHandler handler) throws SAXException {
/* 144 */     this.receiver = handler;
/*     */     
/* 146 */     setCurrentLocation(e);
/* 147 */     this.receiver.startDocument();
/*     */     
/* 149 */     this.receiver.setDocumentLocator(this.locator);
/* 150 */     visit(e);
/*     */     
/* 152 */     setCurrentLocation(e);
/* 153 */     this.receiver.endDocument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseWithContext(Element e, ContentHandler handler) throws SAXException {
/* 164 */     setContentHandler(handler);
/* 165 */     scan(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildNamespaceSupport(NamespaceSupport nss, Node node) {
/* 172 */     if (node == null || node.getNodeType() != 1) {
/*     */       return;
/*     */     }
/* 175 */     buildNamespaceSupport(nss, node.getParentNode());
/*     */     
/* 177 */     nss.pushContext();
/* 178 */     NamedNodeMap atts = node.getAttributes();
/* 179 */     for (int i = 0; i < atts.getLength(); i++) {
/* 180 */       Attr a = (Attr)atts.item(i);
/* 181 */       if ("xmlns".equals(a.getPrefix())) {
/* 182 */         nss.declarePrefix(a.getLocalName(), a.getValue());
/*     */       
/*     */       }
/* 185 */       else if ("xmlns".equals(a.getName())) {
/* 186 */         nss.declarePrefix("", a.getValue());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(Element e) throws SAXException {
/* 196 */     setCurrentLocation(e);
/* 197 */     NamedNodeMap attributes = e.getAttributes();
/*     */     
/* 199 */     this.atts.clear();
/* 200 */     int len = (attributes == null) ? 0 : attributes.getLength();
/*     */     
/* 202 */     for (int i = len - 1; i >= 0; i--) {
/* 203 */       Attr a = (Attr)attributes.item(i);
/* 204 */       String name = a.getName();
/*     */       
/* 206 */       if (name.startsWith("xmlns")) {
/* 207 */         if (name.length() == 5) {
/* 208 */           this.receiver.startPrefixMapping("", a.getValue());
/*     */         } else {
/* 210 */           String localName = a.getLocalName();
/* 211 */           if (localName == null)
/*     */           {
/* 213 */             localName = name.substring(6);
/*     */           }
/* 215 */           this.receiver.startPrefixMapping(localName, a.getValue());
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 220 */         String str1 = a.getNamespaceURI();
/* 221 */         if (str1 == null) str1 = "";
/*     */         
/* 223 */         String str2 = a.getLocalName();
/* 224 */         if (str2 == null) str2 = a.getName();
/*     */ 
/*     */         
/* 227 */         this.atts.addAttribute(str1, str2, a.getName(), "CDATA", a.getValue());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     String uri = e.getNamespaceURI();
/* 236 */     if (uri == null) uri = ""; 
/* 237 */     String local = e.getLocalName();
/* 238 */     String qname = e.getTagName();
/* 239 */     if (local == null) local = qname; 
/* 240 */     this.receiver.startElement(uri, local, qname, this.atts);
/*     */ 
/*     */     
/* 243 */     NodeList children = e.getChildNodes();
/* 244 */     int clen = children.getLength(); int j;
/* 245 */     for (j = 0; j < clen; j++) {
/* 246 */       visit(children.item(j));
/*     */     }
/*     */ 
/*     */     
/* 250 */     setCurrentLocation(e);
/* 251 */     this.receiver.endElement(uri, local, qname);
/*     */ 
/*     */     
/* 254 */     for (j = len - 1; j >= 0; j--) {
/* 255 */       Attr a = (Attr)attributes.item(j);
/* 256 */       String name = a.getName();
/* 257 */       if (name.startsWith("xmlns"))
/* 258 */         if (name.length() == 5) {
/* 259 */           this.receiver.endPrefixMapping("");
/*     */         } else {
/* 261 */           this.receiver.endPrefixMapping(a.getLocalName());
/*     */         }  
/*     */     } 
/*     */   } private void visit(Node n) throws SAXException {
/*     */     String value;
/*     */     ProcessingInstruction pi;
/* 267 */     setCurrentLocation(n);
/*     */ 
/*     */     
/* 270 */     switch (n.getNodeType()) {
/*     */       case 3:
/*     */       case 4:
/* 273 */         value = n.getNodeValue();
/* 274 */         this.receiver.characters(value.toCharArray(), 0, value.length());
/*     */         break;
/*     */       case 1:
/* 277 */         visit((Element)n);
/*     */         break;
/*     */       case 5:
/* 280 */         this.receiver.skippedEntity(n.getNodeName());
/*     */         break;
/*     */       case 7:
/* 283 */         pi = (ProcessingInstruction)n;
/* 284 */         this.receiver.processingInstruction(pi.getTarget(), pi.getData());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setCurrentLocation(Node currNode) {
/* 290 */     this.currentNode = currNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getCurrentLocation() {
/* 298 */     return this.currentNode;
/*     */   }
/*     */   
/*     */   public Object getCurrentElement() {
/* 302 */     return this.currentNode;
/*     */   }
/*     */   
/*     */   public LocatorEx getLocator() {
/* 306 */     return this;
/*     */   }
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/* 310 */     this.receiver = handler;
/*     */   }
/*     */   
/*     */   public ContentHandler getContentHandler() {
/* 314 */     return this.receiver;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPublicId() {
/* 319 */     return null; }
/* 320 */   public String getSystemId() { return null; }
/* 321 */   public int getLineNumber() { return -1; } public int getColumnNumber() {
/* 322 */     return -1;
/*     */   }
/*     */   public ValidationEventLocator getLocation() {
/* 325 */     return (ValidationEventLocator)new ValidationEventLocatorImpl(getCurrentLocation());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bin\\unmarshaller\DOMScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */