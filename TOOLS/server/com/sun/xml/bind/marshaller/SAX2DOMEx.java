/*     */ package com.sun.xml.bind.marshaller;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.xml.bind.util.Which;
/*     */ import java.util.Stack;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class SAX2DOMEx
/*     */   implements ContentHandler
/*     */ {
/*  69 */   private Node node = null;
/*  70 */   private final Stack<Node> nodeStack = new Stack<Node>();
/*  71 */   private final FinalArrayList<String> unprocessedNamespaces = new FinalArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Document document;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SAX2DOMEx(Node node) {
/*  85 */     this.node = node;
/*  86 */     this.nodeStack.push(this.node);
/*     */     
/*  88 */     if (node instanceof Document) {
/*  89 */       this.document = (Document)node;
/*     */     } else {
/*  91 */       this.document = node.getOwnerDocument();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SAX2DOMEx() throws ParserConfigurationException {
/*  98 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  99 */     factory.setNamespaceAware(true);
/* 100 */     factory.setValidating(false);
/*     */     
/* 102 */     this.document = factory.newDocumentBuilder().newDocument();
/* 103 */     this.node = this.document;
/* 104 */     this.nodeStack.push(this.document);
/*     */   }
/*     */   
/*     */   public final Element getCurrentElement() {
/* 108 */     return (Element)this.nodeStack.peek();
/*     */   }
/*     */   
/*     */   public Node getDOM() {
/* 112 */     return this.node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() {}
/*     */ 
/*     */   
/*     */   public void endDocument() {}
/*     */   
/*     */   public void startElement(String namespace, String localName, String qName, Attributes attrs) {
/* 122 */     Node parent = this.nodeStack.peek();
/*     */ 
/*     */ 
/*     */     
/* 126 */     Element element = this.document.createElementNS(namespace, qName);
/*     */     
/* 128 */     if (element == null)
/*     */     {
/*     */       
/* 131 */       throw new AssertionError(Messages.format("SAX2DOMEx.DomImplDoesntSupportCreateElementNs", this.document.getClass().getName(), Which.which(this.document.getClass())));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     for (int i = 0; i < this.unprocessedNamespaces.size(); i += 2) {
/* 139 */       String qname, prefix = (String)this.unprocessedNamespaces.get(i + 0);
/* 140 */       String uri = (String)this.unprocessedNamespaces.get(i + 1);
/*     */ 
/*     */       
/* 143 */       if ("".equals(prefix) || prefix == null) {
/* 144 */         qname = "xmlns";
/*     */       } else {
/* 146 */         qname = "xmlns:" + prefix;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 151 */       if (element.hasAttributeNS("http://www.w3.org/2000/xmlns/", qname))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 159 */         element.removeAttributeNS("http://www.w3.org/2000/xmlns/", qname);
/*     */       }
/*     */ 
/*     */       
/* 163 */       element.setAttributeNS("http://www.w3.org/2000/xmlns/", qname, uri);
/*     */     } 
/* 165 */     this.unprocessedNamespaces.clear();
/*     */ 
/*     */     
/* 168 */     int length = attrs.getLength();
/* 169 */     for (int j = 0; j < length; j++) {
/* 170 */       String namespaceuri = attrs.getURI(j);
/* 171 */       String value = attrs.getValue(j);
/* 172 */       String qname = attrs.getQName(j);
/* 173 */       element.setAttributeNS(namespaceuri, qname, value);
/*     */     } 
/*     */     
/* 176 */     parent.appendChild(element);
/*     */     
/* 178 */     this.nodeStack.push(element);
/*     */   }
/*     */   
/*     */   public void endElement(String namespace, String localName, String qName) {
/* 182 */     this.nodeStack.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void characters(char[] ch, int start, int length) {
/* 187 */     Node parent = this.nodeStack.peek();
/* 188 */     Text text = this.document.createTextNode(new String(ch, start, length));
/* 189 */     parent.appendChild(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) {}
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 198 */     Node parent = this.nodeStack.peek();
/* 199 */     Node node = this.document.createProcessingInstruction(target, data);
/* 200 */     parent.appendChild(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */ 
/*     */   
/*     */   public void skippedEntity(String name) {}
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) {
/* 210 */     this.unprocessedNamespaces.add(prefix);
/* 211 */     this.unprocessedNamespaces.add(uri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\marshaller\SAX2DOMEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */