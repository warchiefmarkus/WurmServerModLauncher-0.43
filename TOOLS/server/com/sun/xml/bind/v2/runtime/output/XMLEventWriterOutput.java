/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
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
/*     */ public class XMLEventWriterOutput
/*     */   extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XMLEventWriter out;
/*     */   private final XMLEventFactory ef;
/*     */   private final Characters sp;
/*     */   
/*     */   public XMLEventWriterOutput(XMLEventWriter out) {
/*  64 */     this.out = out;
/*  65 */     this.ef = XMLEventFactory.newInstance();
/*  66 */     this.sp = this.ef.createCharacters(" ");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/*  72 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  73 */     if (!fragment)
/*  74 */       this.out.add(this.ef.createStartDocument()); 
/*     */   }
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/*  78 */     if (!fragment) {
/*  79 */       this.out.add(this.ef.createEndDocument());
/*  80 */       this.out.flush();
/*     */     } 
/*  82 */     super.endDocument(fragment);
/*     */   }
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/*  86 */     this.out.add(this.ef.createStartElement(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/*  93 */     if (nse.count() > 0)
/*  94 */       for (int i = nse.count() - 1; i >= 0; i--) {
/*  95 */         String uri = nse.getNsUri(i);
/*  96 */         if (uri.length() != 0 || nse.getBase() != 1)
/*     */         {
/*  98 */           this.out.add(this.ef.createNamespace(nse.getPrefix(i), uri));
/*     */         }
/*     */       }  
/*     */   }
/*     */   
/*     */   public void attribute(int prefix, String localName, String value) throws IOException, XMLStreamException {
/*     */     Attribute att;
/* 105 */     if (prefix == -1) {
/* 106 */       att = this.ef.createAttribute(localName, value);
/*     */     } else {
/* 108 */       att = this.ef.createAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName, value);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 113 */     this.out.add(att);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endStartTag() throws IOException, SAXException {}
/*     */ 
/*     */   
/*     */   public void endTag(int prefix, String localName) throws IOException, SAXException, XMLStreamException {
/* 121 */     this.out.add(this.ef.createEndElement(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 129 */     if (needsSeparatingWhitespace)
/* 130 */       this.out.add(this.sp); 
/* 131 */     this.out.add(this.ef.createCharacters(value));
/*     */   }
/*     */   
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 135 */     text(value.toString(), needsSeparatingWhitespace);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\XMLEventWriterOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */