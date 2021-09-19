/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.namespace.NamespaceContext;
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
/*     */ final class MTOMDecorator
/*     */   implements XmlVisitor
/*     */ {
/*     */   private final XmlVisitor next;
/*     */   private final AttachmentUnmarshaller au;
/*     */   private UnmarshallerImpl parent;
/*  61 */   private final Base64Data base64data = new Base64Data();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inXopInclude;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean followXop;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MTOMDecorator(UnmarshallerImpl parent, XmlVisitor next, AttachmentUnmarshaller au) {
/*  79 */     this.parent = parent;
/*  80 */     this.next = next;
/*  81 */     this.au = au;
/*     */   }
/*     */   
/*     */   public void startDocument(LocatorEx loc, NamespaceContext nsContext) throws SAXException {
/*  85 */     this.next.startDocument(loc, nsContext);
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/*  89 */     this.next.endDocument();
/*     */   }
/*     */   
/*     */   public void startElement(TagName tagName) throws SAXException {
/*  93 */     if (tagName.local == "Include" && tagName.uri == "http://www.w3.org/2004/08/xop/include") {
/*     */       
/*  95 */       String href = tagName.atts.getValue("href");
/*  96 */       DataHandler attachment = this.au.getAttachmentAsDataHandler(href);
/*  97 */       if (attachment == null)
/*     */       {
/*  99 */         this.parent.getEventHandler().handleEvent(null);
/*     */       }
/*     */       
/* 102 */       this.base64data.set(attachment);
/* 103 */       this.next.text((CharSequence)this.base64data);
/* 104 */       this.inXopInclude = true;
/* 105 */       this.followXop = true;
/*     */     } else {
/* 107 */       this.next.startElement(tagName);
/*     */     } 
/*     */   }
/*     */   public void endElement(TagName tagName) throws SAXException {
/* 111 */     if (this.inXopInclude) {
/*     */       
/* 113 */       this.inXopInclude = false;
/* 114 */       this.followXop = true;
/*     */       return;
/*     */     } 
/* 117 */     this.next.endElement(tagName);
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
/* 121 */     this.next.startPrefixMapping(prefix, nsUri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 125 */     this.next.endPrefixMapping(prefix);
/*     */   }
/*     */   
/*     */   public void text(CharSequence pcdata) throws SAXException {
/* 129 */     if (!this.followXop) {
/* 130 */       this.next.text(pcdata);
/*     */     } else {
/* 132 */       this.followXop = false;
/*     */     } 
/*     */   }
/*     */   public UnmarshallingContext getContext() {
/* 136 */     return this.next.getContext();
/*     */   }
/*     */   
/*     */   public XmlVisitor.TextPredictor getPredictor() {
/* 140 */     return this.next.getPredictor();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\MTOMDecorator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */