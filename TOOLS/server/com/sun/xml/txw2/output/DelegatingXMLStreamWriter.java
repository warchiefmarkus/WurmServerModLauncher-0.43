/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class DelegatingXMLStreamWriter
/*     */   implements XMLStreamWriter
/*     */ {
/*     */   private final XMLStreamWriter writer;
/*     */   
/*     */   public DelegatingXMLStreamWriter(XMLStreamWriter writer) {
/*  16 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/*  20 */     this.writer.writeStartElement(localName);
/*     */   }
/*     */   
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/*  24 */     this.writer.writeStartElement(namespaceURI, localName);
/*     */   }
/*     */   
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/*  28 */     this.writer.writeStartElement(prefix, localName, namespaceURI);
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/*  32 */     this.writer.writeEmptyElement(namespaceURI, localName);
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/*  36 */     this.writer.writeEmptyElement(prefix, localName, namespaceURI);
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/*  40 */     this.writer.writeEmptyElement(localName);
/*     */   }
/*     */   
/*     */   public void writeEndElement() throws XMLStreamException {
/*  44 */     this.writer.writeEndElement();
/*     */   }
/*     */   
/*     */   public void writeEndDocument() throws XMLStreamException {
/*  48 */     this.writer.writeEndDocument();
/*     */   }
/*     */   
/*     */   public void close() throws XMLStreamException {
/*  52 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public void flush() throws XMLStreamException {
/*  56 */     this.writer.flush();
/*     */   }
/*     */   
/*     */   public void writeAttribute(String localName, String value) throws XMLStreamException {
/*  60 */     this.writer.writeAttribute(localName, value);
/*     */   }
/*     */   
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
/*  64 */     this.writer.writeAttribute(prefix, namespaceURI, localName, value);
/*     */   }
/*     */   
/*     */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
/*  68 */     this.writer.writeAttribute(namespaceURI, localName, value);
/*     */   }
/*     */   
/*     */   public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
/*  72 */     this.writer.writeNamespace(prefix, namespaceURI);
/*     */   }
/*     */   
/*     */   public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
/*  76 */     this.writer.writeDefaultNamespace(namespaceURI);
/*     */   }
/*     */   
/*     */   public void writeComment(String data) throws XMLStreamException {
/*  80 */     this.writer.writeComment(data);
/*     */   }
/*     */   
/*     */   public void writeProcessingInstruction(String target) throws XMLStreamException {
/*  84 */     this.writer.writeProcessingInstruction(target);
/*     */   }
/*     */   
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
/*  88 */     this.writer.writeProcessingInstruction(target, data);
/*     */   }
/*     */   
/*     */   public void writeCData(String data) throws XMLStreamException {
/*  92 */     this.writer.writeCData(data);
/*     */   }
/*     */   
/*     */   public void writeDTD(String dtd) throws XMLStreamException {
/*  96 */     this.writer.writeDTD(dtd);
/*     */   }
/*     */   
/*     */   public void writeEntityRef(String name) throws XMLStreamException {
/* 100 */     this.writer.writeEntityRef(name);
/*     */   }
/*     */   
/*     */   public void writeStartDocument() throws XMLStreamException {
/* 104 */     this.writer.writeStartDocument();
/*     */   }
/*     */   
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/* 108 */     this.writer.writeStartDocument(version);
/*     */   }
/*     */   
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 112 */     this.writer.writeStartDocument(encoding, version);
/*     */   }
/*     */   
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/* 116 */     this.writer.writeCharacters(text);
/*     */   }
/*     */   
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
/* 120 */     this.writer.writeCharacters(text, start, len);
/*     */   }
/*     */   
/*     */   public String getPrefix(String uri) throws XMLStreamException {
/* 124 */     return this.writer.getPrefix(uri);
/*     */   }
/*     */   
/*     */   public void setPrefix(String prefix, String uri) throws XMLStreamException {
/* 128 */     this.writer.setPrefix(prefix, uri);
/*     */   }
/*     */   
/*     */   public void setDefaultNamespace(String uri) throws XMLStreamException {
/* 132 */     this.writer.setDefaultNamespace(uri);
/*     */   }
/*     */   
/*     */   public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
/* 136 */     this.writer.setNamespaceContext(context);
/*     */   }
/*     */   
/*     */   public NamespaceContext getNamespaceContext() {
/* 140 */     return this.writer.getNamespaceContext();
/*     */   }
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/* 144 */     return this.writer.getProperty(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\DelegatingXMLStreamWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */