/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ 
/*     */ public class IndentingXMLStreamWriter
/*     */   extends DelegatingXMLStreamWriter
/*     */ {
/*  11 */   private static final Object SEEN_NOTHING = new Object();
/*  12 */   private static final Object SEEN_ELEMENT = new Object();
/*  13 */   private static final Object SEEN_DATA = new Object();
/*     */   
/*  15 */   private Object state = SEEN_NOTHING;
/*  16 */   private Stack<Object> stateStack = new Stack();
/*     */   
/*  18 */   private String indentStep = "  ";
/*  19 */   private int depth = 0;
/*     */   
/*     */   public IndentingXMLStreamWriter(XMLStreamWriter writer) {
/*  22 */     super(writer);
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
/*     */   public int getIndentStep() {
/*  40 */     return this.indentStep.length();
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
/*     */   public void setIndentStep(int indentStep) {
/*  55 */     StringBuilder s = new StringBuilder();
/*  56 */     while (indentStep > 0) { s.append(' '); indentStep--; }
/*  57 */      setIndentStep(s.toString());
/*     */   }
/*     */   
/*     */   public void setIndentStep(String s) {
/*  61 */     this.indentStep = s;
/*     */   }
/*     */   
/*     */   private void onStartElement() throws XMLStreamException {
/*  65 */     this.stateStack.push(SEEN_ELEMENT);
/*  66 */     this.state = SEEN_NOTHING;
/*  67 */     if (this.depth > 0) {
/*  68 */       super.writeCharacters("\n");
/*     */     }
/*  70 */     doIndent();
/*  71 */     this.depth++;
/*     */   }
/*     */   
/*     */   private void onEndElement() throws XMLStreamException {
/*  75 */     this.depth--;
/*  76 */     if (this.state == SEEN_ELEMENT) {
/*  77 */       super.writeCharacters("\n");
/*  78 */       doIndent();
/*     */     } 
/*  80 */     this.state = this.stateStack.pop();
/*     */   }
/*     */   
/*     */   private void onEmptyElement() throws XMLStreamException {
/*  84 */     this.state = SEEN_ELEMENT;
/*  85 */     if (this.depth > 0) {
/*  86 */       super.writeCharacters("\n");
/*     */     }
/*  88 */     doIndent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doIndent() throws XMLStreamException {
/*  99 */     if (this.depth > 0) {
/* 100 */       for (int i = 0; i < this.depth; i++) {
/* 101 */         super.writeCharacters(this.indentStep);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeStartDocument() throws XMLStreamException {
/* 107 */     super.writeStartDocument();
/* 108 */     super.writeCharacters("\n");
/*     */   }
/*     */   
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/* 112 */     super.writeStartDocument(version);
/* 113 */     super.writeCharacters("\n");
/*     */   }
/*     */   
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 117 */     super.writeStartDocument(encoding, version);
/* 118 */     super.writeCharacters("\n");
/*     */   }
/*     */   
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/* 122 */     onStartElement();
/* 123 */     super.writeStartElement(localName);
/*     */   }
/*     */   
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/* 127 */     onStartElement();
/* 128 */     super.writeStartElement(namespaceURI, localName);
/*     */   }
/*     */   
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 132 */     onStartElement();
/* 133 */     super.writeStartElement(prefix, localName, namespaceURI);
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/* 137 */     onEmptyElement();
/* 138 */     super.writeEmptyElement(namespaceURI, localName);
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 142 */     onEmptyElement();
/* 143 */     super.writeEmptyElement(prefix, localName, namespaceURI);
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/* 147 */     onEmptyElement();
/* 148 */     super.writeEmptyElement(localName);
/*     */   }
/*     */   
/*     */   public void writeEndElement() throws XMLStreamException {
/* 152 */     onEndElement();
/* 153 */     super.writeEndElement();
/*     */   }
/*     */   
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/* 157 */     this.state = SEEN_DATA;
/* 158 */     super.writeCharacters(text);
/*     */   }
/*     */   
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
/* 162 */     this.state = SEEN_DATA;
/* 163 */     super.writeCharacters(text, start, len);
/*     */   }
/*     */   
/*     */   public void writeCData(String data) throws XMLStreamException {
/* 167 */     this.state = SEEN_DATA;
/* 168 */     super.writeCData(data);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\IndentingXMLStreamWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */