/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import com.sun.xml.txw2.TxwException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StaxSerializer
/*     */   implements XmlSerializer
/*     */ {
/*     */   private final XMLStreamWriter out;
/*     */   
/*     */   public StaxSerializer(XMLStreamWriter writer) {
/*  40 */     this(writer, true);
/*     */   }
/*     */   
/*     */   public StaxSerializer(XMLStreamWriter writer, boolean indenting) {
/*  44 */     if (indenting)
/*  45 */       writer = new IndentingXMLStreamWriter(writer); 
/*  46 */     this.out = writer;
/*     */   }
/*     */   
/*     */   public void startDocument() {
/*     */     try {
/*  51 */       this.out.writeStartDocument();
/*  52 */     } catch (XMLStreamException e) {
/*  53 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beginStartTag(String uri, String localName, String prefix) {
/*     */     try {
/*  59 */       this.out.writeStartElement(prefix, localName, uri);
/*  60 */     } catch (XMLStreamException e) {
/*  61 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/*     */     try {
/*  67 */       this.out.writeAttribute(prefix, uri, localName, value.toString());
/*  68 */     } catch (XMLStreamException e) {
/*  69 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeXmlns(String prefix, String uri) {
/*     */     try {
/*  75 */       if (prefix.length() == 0) {
/*  76 */         this.out.setDefaultNamespace(uri);
/*     */       } else {
/*  78 */         this.out.setPrefix(prefix, uri);
/*     */       } 
/*     */ 
/*     */       
/*  82 */       this.out.writeNamespace(prefix, uri);
/*  83 */     } catch (XMLStreamException e) {
/*  84 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void endStartTag(String uri, String localName, String prefix) {}
/*     */ 
/*     */   
/*     */   public void endTag() {
/*     */     try {
/*  94 */       this.out.writeEndElement();
/*  95 */     } catch (XMLStreamException e) {
/*  96 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void text(StringBuilder text) {
/*     */     try {
/* 102 */       this.out.writeCharacters(text.toString());
/* 103 */     } catch (XMLStreamException e) {
/* 104 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cdata(StringBuilder text) {
/*     */     try {
/* 110 */       this.out.writeCData(text.toString());
/* 111 */     } catch (XMLStreamException e) {
/* 112 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void comment(StringBuilder comment) {
/*     */     try {
/* 118 */       this.out.writeComment(comment.toString());
/* 119 */     } catch (XMLStreamException e) {
/* 120 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endDocument() {
/*     */     try {
/* 126 */       this.out.writeEndDocument();
/* 127 */       this.out.flush();
/* 128 */     } catch (XMLStreamException e) {
/* 129 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flush() {
/*     */     try {
/* 135 */       this.out.flush();
/* 136 */     } catch (XMLStreamException e) {
/* 137 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\StaxSerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */