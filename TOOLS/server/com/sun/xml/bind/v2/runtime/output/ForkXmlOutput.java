/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
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
/*     */ public final class ForkXmlOutput
/*     */   extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XmlOutput lhs;
/*     */   private final XmlOutput rhs;
/*     */   
/*     */   public ForkXmlOutput(XmlOutput lhs, XmlOutput rhs) {
/*  57 */     this.lhs = lhs;
/*  58 */     this.rhs = rhs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/*  63 */     this.lhs.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  64 */     this.rhs.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/*  69 */     this.lhs.endDocument(fragment);
/*  70 */     this.rhs.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginStartTag(Name name) throws IOException, XMLStreamException {
/*  75 */     this.lhs.beginStartTag(name);
/*  76 */     this.rhs.beginStartTag(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void attribute(Name name, String value) throws IOException, XMLStreamException {
/*  81 */     this.lhs.attribute(name, value);
/*  82 */     this.rhs.attribute(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTag(Name name) throws IOException, SAXException, XMLStreamException {
/*  87 */     this.lhs.endTag(name);
/*  88 */     this.rhs.endTag(name);
/*     */   }
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/*  92 */     this.lhs.beginStartTag(prefix, localName);
/*  93 */     this.rhs.beginStartTag(prefix, localName);
/*     */   }
/*     */   
/*     */   public void attribute(int prefix, String localName, String value) throws IOException, XMLStreamException {
/*  97 */     this.lhs.attribute(prefix, localName, value);
/*  98 */     this.rhs.attribute(prefix, localName, value);
/*     */   }
/*     */   
/*     */   public void endStartTag() throws IOException, SAXException {
/* 102 */     this.lhs.endStartTag();
/* 103 */     this.rhs.endStartTag();
/*     */   }
/*     */   
/*     */   public void endTag(int prefix, String localName) throws IOException, SAXException, XMLStreamException {
/* 107 */     this.lhs.endTag(prefix, localName);
/* 108 */     this.rhs.endTag(prefix, localName);
/*     */   }
/*     */   
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 112 */     this.lhs.text(value, needsSeparatingWhitespace);
/* 113 */     this.rhs.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */   
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 117 */     this.lhs.text(value, needsSeparatingWhitespace);
/* 118 */     this.rhs.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\ForkXmlOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */