/*     */ package org.kohsuke.rngom.digested;
/*     */ 
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DOMPrinter
/*     */ {
/*     */   protected XMLStreamWriter out;
/*     */   
/*     */   public DOMPrinter(XMLStreamWriter out) {
/*  28 */     this.out = out;
/*     */   }
/*     */   
/*     */   public void print(Node node) throws XMLStreamException {
/*  32 */     switch (node.getNodeType()) {
/*     */       case 9:
/*  34 */         visitDocument((Document)node);
/*     */       
/*     */       case 11:
/*  37 */         visitDocumentFragment((DocumentFragment)node);
/*     */       
/*     */       case 1:
/*  40 */         visitElement((Element)node);
/*     */       
/*     */       case 3:
/*  43 */         visitText((Text)node);
/*     */       
/*     */       case 4:
/*  46 */         visitCDATASection((CDATASection)node);
/*     */       
/*     */       case 7:
/*  49 */         visitProcessingInstruction((ProcessingInstruction)node);
/*     */       
/*     */       case 5:
/*  52 */         visitReference((EntityReference)node);
/*     */       
/*     */       case 8:
/*  55 */         visitComment((Comment)node);
/*     */ 
/*     */       
/*     */       case 10:
/*     */         return;
/*     */     } 
/*     */     
/*  62 */     throw new XMLStreamException("Unexpected DOM Node Type " + node.getNodeType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitChildren(Node node) throws XMLStreamException {
/*  70 */     NodeList nodeList = node.getChildNodes();
/*  71 */     if (nodeList != null) {
/*  72 */       for (int i = 0; i < nodeList.getLength(); i++) {
/*  73 */         print(nodeList.item(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitDocument(Document document) throws XMLStreamException {
/*  80 */     this.out.writeStartDocument();
/*  81 */     print(document.getDocumentElement());
/*  82 */     this.out.writeEndDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitDocumentFragment(DocumentFragment documentFragment) throws XMLStreamException {
/*  87 */     visitChildren(documentFragment);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitElement(Element node) throws XMLStreamException {
/*  92 */     this.out.writeStartElement(node.getPrefix(), node.getLocalName(), node.getNamespaceURI());
/*     */ 
/*     */ 
/*     */     
/*  96 */     NamedNodeMap attrs = node.getAttributes();
/*  97 */     for (int i = 0; i < attrs.getLength(); i++) {
/*  98 */       visitAttr((Attr)attrs.item(i));
/*     */     }
/* 100 */     visitChildren(node);
/* 101 */     this.out.writeEndElement();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitAttr(Attr node) throws XMLStreamException {
/* 106 */     String name = node.getLocalName();
/* 107 */     if (name.equals("xmlns")) {
/* 108 */       this.out.writeDefaultNamespace(node.getNamespaceURI());
/*     */     } else {
/* 110 */       String prefix = node.getPrefix();
/* 111 */       if (prefix != null && prefix.equals("xmlns")) {
/* 112 */         this.out.writeNamespace(prefix, node.getNamespaceURI());
/*     */       } else {
/* 114 */         this.out.writeAttribute(prefix, node.getNamespaceURI(), name, node.getNodeValue());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitComment(Comment comment) throws XMLStreamException {
/* 124 */     this.out.writeComment(comment.getData());
/*     */   }
/*     */   
/*     */   protected void visitText(Text node) throws XMLStreamException {
/* 128 */     this.out.writeCharacters(node.getNodeValue());
/*     */   }
/*     */   
/*     */   protected void visitCDATASection(CDATASection cdata) throws XMLStreamException {
/* 132 */     this.out.writeCData(cdata.getNodeValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitProcessingInstruction(ProcessingInstruction processingInstruction) throws XMLStreamException {
/* 137 */     this.out.writeProcessingInstruction(processingInstruction.getNodeName(), processingInstruction.getData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitReference(EntityReference entityReference) throws XMLStreamException {
/* 144 */     visitChildren(entityReference);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DOMPrinter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */