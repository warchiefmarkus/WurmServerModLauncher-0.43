/*     */ package com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LSInputSAXWrapper
/*     */   implements LSInput
/*     */ {
/*     */   private InputSource core;
/*     */   
/*     */   public LSInputSAXWrapper(InputSource inputSource) {
/*  54 */     assert inputSource != null;
/*  55 */     this.core = inputSource;
/*     */   }
/*     */   
/*     */   public Reader getCharacterStream() {
/*  59 */     return this.core.getCharacterStream();
/*     */   }
/*     */   
/*     */   public void setCharacterStream(Reader characterStream) {
/*  63 */     this.core.setCharacterStream(characterStream);
/*     */   }
/*     */   
/*     */   public InputStream getByteStream() {
/*  67 */     return this.core.getByteStream();
/*     */   }
/*     */   
/*     */   public void setByteStream(InputStream byteStream) {
/*  71 */     this.core.setByteStream(byteStream);
/*     */   }
/*     */   
/*     */   public String getStringData() {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStringData(String stringData) {}
/*     */ 
/*     */   
/*     */   public String getSystemId() {
/*  83 */     return this.core.getSystemId();
/*     */   }
/*     */   
/*     */   public void setSystemId(String systemId) {
/*  87 */     this.core.setSystemId(systemId);
/*     */   }
/*     */   
/*     */   public String getPublicId() {
/*  91 */     return this.core.getPublicId();
/*     */   }
/*     */   
/*     */   public void setPublicId(String publicId) {
/*  95 */     this.core.setPublicId(publicId);
/*     */   }
/*     */   
/*     */   public String getBaseURI() {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaseURI(String baseURI) {}
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 107 */     return this.core.getEncoding();
/*     */   }
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 111 */     this.core.setEncoding(encoding);
/*     */   }
/*     */   
/*     */   public boolean getCertifiedText() {
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   public void setCertifiedText(boolean certifiedText) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\parser\LSInputSAXWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */