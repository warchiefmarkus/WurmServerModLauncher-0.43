/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import com.sun.xml.txw2.TxwException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Stack;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Dom2SaxAdapter
/*     */   implements ContentHandler, LexicalHandler
/*     */ {
/*     */   private final Node _node;
/* 133 */   private final Stack _nodeStk = new Stack(); private boolean inCDATA; private final Document _document;
/*     */   private ArrayList unprocessedNamespaces;
/*     */   
/*     */   public final Element getCurrentElement() {
/* 137 */     return this._nodeStk.peek();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getDOM() {
/* 174 */     return this._node;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void endDocument() {}
/*     */ 
/*     */   
/*     */   public void startElement(String namespace, String localName, String qName, Attributes attrs) {
/* 187 */     Element element = this._document.createElementNS(namespace, qName);
/*     */     
/* 189 */     if (element == null)
/*     */     {
/*     */       
/* 192 */       throw new TxwException("Your DOM provider doesn't support the createElementNS method properly");
/*     */     }
/*     */ 
/*     */     
/* 196 */     for (int i = 0; i < this.unprocessedNamespaces.size(); i += 2) {
/* 197 */       String qname, prefix = this.unprocessedNamespaces.get(i + 0);
/* 198 */       String uri = this.unprocessedNamespaces.get(i + 1);
/*     */ 
/*     */       
/* 201 */       if ("".equals(prefix) || prefix == null) {
/* 202 */         qname = "xmlns";
/*     */       } else {
/* 204 */         qname = "xmlns:" + prefix;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 209 */       if (element.hasAttributeNS("http://www.w3.org/2000/xmlns/", qname))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 217 */         element.removeAttributeNS("http://www.w3.org/2000/xmlns/", qname);
/*     */       }
/*     */ 
/*     */       
/* 221 */       element.setAttributeNS("http://www.w3.org/2000/xmlns/", qname, uri);
/*     */     } 
/* 223 */     this.unprocessedNamespaces.clear();
/*     */ 
/*     */     
/* 226 */     int length = attrs.getLength();
/* 227 */     for (int j = 0; j < length; j++) {
/* 228 */       String namespaceuri = attrs.getURI(j);
/* 229 */       String value = attrs.getValue(j);
/* 230 */       String qname = attrs.getQName(j);
/* 231 */       element.setAttributeNS(namespaceuri, qname, value);
/*     */     } 
/*     */     
/* 234 */     getParent().appendChild(element);
/*     */     
/* 236 */     this._nodeStk.push(element);
/*     */   }
/*     */   
/*     */   private final Node getParent() {
/* 240 */     return this._nodeStk.peek();
/*     */   }
/*     */   
/*     */   public void endElement(String namespace, String localName, String qName) {
/* 244 */     this._nodeStk.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void characters(char[] ch, int start, int length) {
/*     */     Node text;
/* 250 */     if (this.inCDATA) {
/* 251 */       text = this._document.createCDATASection(new String(ch, start, length));
/*     */     } else {
/* 253 */       text = this._document.createTextNode(new String(ch, start, length));
/* 254 */     }  getParent().appendChild(text);
/*     */   }
/*     */   
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/* 258 */     getParent().appendChild(this._document.createComment(new String(ch, start, length)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) {}
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 267 */     Node node = this._document.createProcessingInstruction(target, data);
/* 268 */     getParent().appendChild(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */   
/*     */   public void skippedEntity(String name) {}
/*     */   
/*     */   public Dom2SaxAdapter(Node node) {
/* 277 */     this.unprocessedNamespaces = new ArrayList(); this._node = node; this._nodeStk.push(this._node); if (node instanceof Document) { this._document = (Document)node; } else { this._document = node.getOwnerDocument(); }  } public Dom2SaxAdapter() throws ParserConfigurationException { this.unprocessedNamespaces = new ArrayList(); DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); factory.setNamespaceAware(true); factory.setValidating(false);
/*     */     this._document = factory.newDocumentBuilder().newDocument();
/*     */     this._node = this._document;
/* 280 */     this._nodeStk.push(this._document); } public void startPrefixMapping(String prefix, String uri) { this.unprocessedNamespaces.add(prefix);
/* 281 */     this.unprocessedNamespaces.add(uri); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endPrefixMapping(String prefix) {}
/*     */ 
/*     */   
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException {}
/*     */ 
/*     */   
/*     */   public void endDTD() throws SAXException {}
/*     */ 
/*     */   
/*     */   public void startEntity(String name) throws SAXException {}
/*     */ 
/*     */   
/*     */   public void endEntity(String name) throws SAXException {}
/*     */   
/*     */   public void startCDATA() throws SAXException {
/* 300 */     this.inCDATA = true;
/*     */   }
/*     */   
/*     */   public void endCDATA() throws SAXException {
/* 304 */     this.inCDATA = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\Dom2SaxAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */