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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class XmlOutputAbstractImpl
/*     */   implements XmlOutput
/*     */ {
/*     */   protected int[] nsUriIndex2prefixIndex;
/*     */   protected NamespaceContextImpl nsContext;
/*     */   protected XMLSerializer serializer;
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/*  72 */     this.nsUriIndex2prefixIndex = nsUriIndex2prefixIndex;
/*  73 */     this.nsContext = nsContext;
/*  74 */     this.serializer = serializer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/*  84 */     this.serializer = null;
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
/*     */   public void beginStartTag(Name name) throws IOException, XMLStreamException {
/*  98 */     beginStartTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
/*     */   }
/*     */   
/*     */   public abstract void beginStartTag(int paramInt, String paramString) throws IOException, XMLStreamException;
/*     */   
/*     */   public void attribute(Name name, String value) throws IOException, XMLStreamException {
/* 104 */     short idx = name.nsUriIndex;
/* 105 */     if (idx == -1) {
/* 106 */       attribute(-1, name.localName, value);
/*     */     } else {
/* 108 */       attribute(this.nsUriIndex2prefixIndex[idx], name.localName, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void attribute(int paramInt, String paramString1, String paramString2) throws IOException, XMLStreamException;
/*     */ 
/*     */   
/*     */   public abstract void endStartTag() throws IOException, SAXException;
/*     */ 
/*     */   
/*     */   public void endTag(Name name) throws IOException, SAXException, XMLStreamException {
/* 120 */     endTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
/*     */   }
/*     */   
/*     */   public abstract void endTag(int paramInt, String paramString) throws IOException, SAXException, XMLStreamException;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\XmlOutputAbstractImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */