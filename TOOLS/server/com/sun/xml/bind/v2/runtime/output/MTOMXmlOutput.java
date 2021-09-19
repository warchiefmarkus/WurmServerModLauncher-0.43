/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
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
/*     */ public final class MTOMXmlOutput
/*     */   extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XmlOutput next;
/*     */   private String nsUri;
/*     */   private String localName;
/*     */   
/*     */   public MTOMXmlOutput(XmlOutput next) {
/*  67 */     this.next = next;
/*     */   }
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/*  71 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  72 */     this.next.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */   }
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/*  76 */     this.next.endDocument(fragment);
/*  77 */     super.endDocument(fragment);
/*     */   }
/*     */   
/*     */   public void beginStartTag(Name name) throws IOException, XMLStreamException {
/*  81 */     this.next.beginStartTag(name);
/*  82 */     this.nsUri = name.nsUri;
/*  83 */     this.localName = name.localName;
/*     */   }
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/*  87 */     this.next.beginStartTag(prefix, localName);
/*  88 */     this.nsUri = this.nsContext.getNamespaceURI(prefix);
/*  89 */     this.localName = localName;
/*     */   }
/*     */   
/*     */   public void attribute(Name name, String value) throws IOException, XMLStreamException {
/*  93 */     this.next.attribute(name, value);
/*     */   }
/*     */   
/*     */   public void attribute(int prefix, String localName, String value) throws IOException, XMLStreamException {
/*  97 */     this.next.attribute(prefix, localName, value);
/*     */   }
/*     */   
/*     */   public void endStartTag() throws IOException, SAXException {
/* 101 */     this.next.endStartTag();
/*     */   }
/*     */   
/*     */   public void endTag(Name name) throws IOException, SAXException, XMLStreamException {
/* 105 */     this.next.endTag(name);
/*     */   }
/*     */   
/*     */   public void endTag(int prefix, String localName) throws IOException, SAXException, XMLStreamException {
/* 109 */     this.next.endTag(prefix, localName);
/*     */   }
/*     */   
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 113 */     this.next.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */   
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 117 */     if (value instanceof Base64Data && !this.serializer.getInlineBinaryFlag()) {
/* 118 */       String cid; Base64Data b64d = (Base64Data)value;
/*     */       
/* 120 */       if (b64d.hasData()) {
/* 121 */         cid = this.serializer.attachmentMarshaller.addMtomAttachment(b64d.get(), 0, b64d.getDataLen(), b64d.getMimeType(), this.nsUri, this.localName);
/*     */       } else {
/*     */         
/* 124 */         cid = this.serializer.attachmentMarshaller.addMtomAttachment(b64d.getDataHandler(), this.nsUri, this.localName);
/*     */       } 
/*     */       
/* 127 */       if (cid != null) {
/* 128 */         this.nsContext.getCurrent().push();
/* 129 */         int prefix = this.nsContext.declareNsUri("http://www.w3.org/2004/08/xop/include", "xop", false);
/* 130 */         beginStartTag(prefix, "Include");
/* 131 */         attribute(-1, "href", cid);
/* 132 */         endStartTag();
/* 133 */         endTag(prefix, "Include");
/* 134 */         this.nsContext.getCurrent().pop();
/*     */         return;
/*     */       } 
/*     */     } 
/* 138 */     this.next.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\MTOMXmlOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */