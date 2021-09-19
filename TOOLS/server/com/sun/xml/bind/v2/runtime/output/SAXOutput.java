/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.util.AttributesImpl;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SAXOutput
/*     */   extends XmlOutputAbstractImpl
/*     */ {
/*     */   protected final ContentHandler out;
/*     */   private String elementNsUri;
/*     */   private String elementLocalName;
/*     */   private String elementQName;
/*     */   private char[] buf;
/*     */   private final AttributesImpl atts;
/*     */   
/*     */   public SAXOutput(ContentHandler out) {
/*  65 */     this.buf = new char[256];
/*     */     
/*  67 */     this.atts = new AttributesImpl();
/*     */     this.out = out;
/*     */     out.setDocumentLocator(new LocatorImpl());
/*     */   }
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, IOException, XMLStreamException {
/*  73 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  74 */     if (!fragment)
/*  75 */       this.out.startDocument(); 
/*     */   }
/*     */   
/*     */   public void endDocument(boolean fragment) throws SAXException, IOException, XMLStreamException {
/*  79 */     if (!fragment)
/*  80 */       this.out.endDocument(); 
/*  81 */     super.endDocument(fragment);
/*     */   }
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) {
/*  85 */     this.elementNsUri = this.nsContext.getNamespaceURI(prefix);
/*  86 */     this.elementLocalName = localName;
/*  87 */     this.elementQName = getQName(prefix, localName);
/*  88 */     this.atts.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attribute(int prefix, String localName, String value) {
/*     */     String qname, nsUri;
/*  94 */     if (prefix == -1) {
/*  95 */       nsUri = "";
/*  96 */       qname = localName;
/*     */     } else {
/*  98 */       nsUri = this.nsContext.getNamespaceURI(prefix);
/*  99 */       String p = this.nsContext.getPrefix(prefix);
/* 100 */       if (p.length() == 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 105 */         qname = localName;
/*     */       } else {
/* 107 */         qname = p + ':' + localName;
/*     */       } 
/* 109 */     }  this.atts.addAttribute(nsUri, localName, qname, "CDATA", value);
/*     */   }
/*     */   
/*     */   public void endStartTag() throws SAXException {
/* 113 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/* 114 */     if (ns != null) {
/* 115 */       int sz = ns.count();
/* 116 */       for (int i = 0; i < sz; i++) {
/* 117 */         String p = ns.getPrefix(i);
/* 118 */         String uri = ns.getNsUri(i);
/* 119 */         if (uri.length() != 0 || ns.getBase() != 1)
/*     */         {
/* 121 */           this.out.startPrefixMapping(p, uri); } 
/*     */       } 
/*     */     } 
/* 124 */     this.out.startElement(this.elementNsUri, this.elementLocalName, this.elementQName, (Attributes)this.atts);
/*     */   }
/*     */   
/*     */   public void endTag(int prefix, String localName) throws SAXException {
/* 128 */     this.out.endElement(this.nsContext.getNamespaceURI(prefix), localName, getQName(prefix, localName));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/* 135 */     if (ns != null) {
/* 136 */       int sz = ns.count();
/* 137 */       for (int i = sz - 1; i >= 0; i--) {
/* 138 */         String p = ns.getPrefix(i);
/* 139 */         String uri = ns.getNsUri(i);
/* 140 */         if (uri.length() != 0 || ns.getBase() != 1)
/*     */         {
/* 142 */           this.out.endPrefixMapping(p);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getQName(int prefix, String localName) {
/* 149 */     String qname, p = this.nsContext.getPrefix(prefix);
/* 150 */     if (p.length() == 0) {
/* 151 */       qname = localName;
/*     */     } else {
/* 153 */       qname = p + ':' + localName;
/* 154 */     }  return qname;
/*     */   }
/*     */   
/*     */   public void text(String value, boolean needsSP) throws IOException, SAXException, XMLStreamException {
/* 158 */     int vlen = value.length();
/* 159 */     if (this.buf.length <= vlen) {
/* 160 */       this.buf = new char[Math.max(this.buf.length * 2, vlen + 1)];
/*     */     }
/* 162 */     if (needsSP) {
/* 163 */       value.getChars(0, vlen, this.buf, 1);
/* 164 */       this.buf[0] = ' ';
/*     */     } else {
/* 166 */       value.getChars(0, vlen, this.buf, 0);
/*     */     } 
/* 168 */     this.out.characters(this.buf, 0, vlen + (needsSP ? 1 : 0));
/*     */   }
/*     */   
/*     */   public void text(Pcdata value, boolean needsSP) throws IOException, SAXException, XMLStreamException {
/* 172 */     int vlen = value.length();
/* 173 */     if (this.buf.length <= vlen) {
/* 174 */       this.buf = new char[Math.max(this.buf.length * 2, vlen + 1)];
/*     */     }
/* 176 */     if (needsSP) {
/* 177 */       value.writeTo(this.buf, 1);
/* 178 */       this.buf[0] = ' ';
/*     */     } else {
/* 180 */       value.writeTo(this.buf, 0);
/*     */     } 
/* 182 */     this.out.characters(this.buf, 0, vlen + (needsSP ? 1 : 0));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\SAXOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */