/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ContentHandlerNamespacePrefixAdapter
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   private boolean namespacePrefixes = false;
/*  61 */   private String[] nsBinding = new String[8];
/*     */   
/*     */   private int len;
/*     */   
/*     */   private final AttributesImpl atts;
/*     */   
/*     */   private static final String PREFIX_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
/*     */   
/*     */   private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  72 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/*  73 */       return this.namespacePrefixes; 
/*  74 */     return super.getFeature(name);
/*     */   }
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  78 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
/*  79 */       this.namespacePrefixes = value;
/*     */       return;
/*     */     } 
/*  82 */     if (name.equals("http://xml.org/sax/features/namespaces") && value)
/*     */       return; 
/*  84 */     super.setFeature(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/*  89 */     if (this.len == this.nsBinding.length) {
/*     */       
/*  91 */       String[] buf = new String[this.nsBinding.length * 2];
/*  92 */       System.arraycopy(this.nsBinding, 0, buf, 0, this.nsBinding.length);
/*  93 */       this.nsBinding = buf;
/*     */     } 
/*  95 */     this.nsBinding[this.len++] = prefix;
/*  96 */     this.nsBinding[this.len++] = uri;
/*  97 */     super.startPrefixMapping(prefix, uri);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 101 */     if (this.namespacePrefixes) {
/* 102 */       this.atts.setAttributes(atts);
/*     */       
/* 104 */       for (int i = 0; i < this.len; i += 2) {
/* 105 */         String prefix = this.nsBinding[i];
/* 106 */         if (prefix.length() == 0) {
/* 107 */           this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", "xmlns", "xmlns", "CDATA", this.nsBinding[i + 1]);
/*     */         } else {
/* 109 */           this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", prefix, "xmlns:" + prefix, "CDATA", this.nsBinding[i + 1]);
/*     */         } 
/* 111 */       }  atts = this.atts;
/*     */     } 
/* 113 */     this.len = 0;
/* 114 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */   
/* 117 */   public ContentHandlerNamespacePrefixAdapter() { this.atts = new AttributesImpl(); } public ContentHandlerNamespacePrefixAdapter(XMLReader parent) { this.atts = new AttributesImpl();
/*     */     setParent(parent); }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\ContentHandlerNamespacePrefixAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */