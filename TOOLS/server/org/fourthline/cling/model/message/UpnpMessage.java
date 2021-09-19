/*     */ package org.fourthline.cling.model.message;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.seamless.util.MimeType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UpnpMessage<O extends UpnpOperation>
/*     */ {
/*     */   public enum BodyType
/*     */   {
/*  42 */     STRING, BYTES;
/*     */   }
/*     */   
/*  45 */   private int udaMajorVersion = 1;
/*  46 */   private int udaMinorVersion = 0;
/*     */   
/*     */   private O operation;
/*  49 */   private UpnpHeaders headers = new UpnpHeaders();
/*     */   private Object body;
/*  51 */   private BodyType bodyType = BodyType.STRING;
/*     */   
/*     */   protected UpnpMessage(UpnpMessage<O> source) {
/*  54 */     this.operation = source.getOperation();
/*  55 */     this.headers = source.getHeaders();
/*  56 */     this.body = source.getBody();
/*  57 */     this.bodyType = source.getBodyType();
/*  58 */     this.udaMajorVersion = source.getUdaMajorVersion();
/*  59 */     this.udaMinorVersion = source.getUdaMinorVersion();
/*     */   }
/*     */   
/*     */   protected UpnpMessage(O operation) {
/*  63 */     this.operation = operation;
/*     */   }
/*     */   
/*     */   protected UpnpMessage(O operation, BodyType bodyType, Object body) {
/*  67 */     this.operation = operation;
/*  68 */     this.bodyType = bodyType;
/*  69 */     this.body = body;
/*     */   }
/*     */   
/*     */   public int getUdaMajorVersion() {
/*  73 */     return this.udaMajorVersion;
/*     */   }
/*     */   
/*     */   public void setUdaMajorVersion(int udaMajorVersion) {
/*  77 */     this.udaMajorVersion = udaMajorVersion;
/*     */   }
/*     */   
/*     */   public int getUdaMinorVersion() {
/*  81 */     return this.udaMinorVersion;
/*     */   }
/*     */   
/*     */   public void setUdaMinorVersion(int udaMinorVersion) {
/*  85 */     this.udaMinorVersion = udaMinorVersion;
/*     */   }
/*     */   
/*     */   public UpnpHeaders getHeaders() {
/*  89 */     return this.headers;
/*     */   }
/*     */   
/*     */   public void setHeaders(UpnpHeaders headers) {
/*  93 */     this.headers = headers;
/*     */   }
/*     */   
/*     */   public Object getBody() {
/*  97 */     return this.body;
/*     */   }
/*     */   
/*     */   public void setBody(String string) {
/* 101 */     this.bodyType = BodyType.STRING;
/* 102 */     this.body = string;
/*     */   }
/*     */   
/*     */   public void setBody(BodyType bodyType, Object body) {
/* 106 */     this.bodyType = bodyType;
/* 107 */     this.body = body;
/*     */   }
/*     */   
/*     */   public void setBodyCharacters(byte[] characterData) throws UnsupportedEncodingException {
/* 111 */     setBody(BodyType.STRING, new String(characterData, 
/*     */ 
/*     */ 
/*     */           
/* 115 */           (getContentTypeCharset() != null) ? 
/* 116 */           getContentTypeCharset() : "UTF-8"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBody() {
/* 123 */     return (getBody() != null);
/*     */   }
/*     */   
/*     */   public BodyType getBodyType() {
/* 127 */     return this.bodyType;
/*     */   }
/*     */   
/*     */   public void setBodyType(BodyType bodyType) {
/* 131 */     this.bodyType = bodyType;
/*     */   }
/*     */   
/*     */   public String getBodyString() {
/*     */     try {
/* 136 */       if (!hasBody()) {
/* 137 */         return null;
/*     */       }
/* 139 */       if (getBodyType().equals(BodyType.STRING)) {
/* 140 */         String body = (String)getBody();
/* 141 */         if (body.charAt(0) == '﻿') {
/* 142 */           body = body.substring(1);
/*     */         }
/* 144 */         return body;
/*     */       } 
/* 146 */       return new String((byte[])getBody(), "UTF-8");
/*     */     }
/* 148 */     catch (Exception ex) {
/* 149 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] getBodyBytes() {
/*     */     try {
/* 155 */       if (!hasBody()) {
/* 156 */         return null;
/*     */       }
/* 158 */       if (getBodyType().equals(BodyType.STRING)) {
/* 159 */         return getBodyString().getBytes();
/*     */       }
/* 161 */       return (byte[])getBody();
/*     */     }
/* 163 */     catch (Exception ex) {
/* 164 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public O getOperation() {
/* 169 */     return this.operation;
/*     */   }
/*     */   
/*     */   public boolean isContentTypeMissingOrText() {
/* 173 */     ContentTypeHeader contentTypeHeader = getContentTypeHeader();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     if (contentTypeHeader == null) return true; 
/* 179 */     if (contentTypeHeader.isText()) return true;
/*     */     
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   public ContentTypeHeader getContentTypeHeader() {
/* 185 */     return getHeaders().<ContentTypeHeader>getFirstHeader(UpnpHeader.Type.CONTENT_TYPE, ContentTypeHeader.class);
/*     */   }
/*     */   
/*     */   public boolean isContentTypeText() {
/* 189 */     ContentTypeHeader ct = getContentTypeHeader();
/* 190 */     return (ct != null && ct.isText());
/*     */   }
/*     */   
/*     */   public boolean isContentTypeTextUDA() {
/* 194 */     ContentTypeHeader ct = getContentTypeHeader();
/* 195 */     return (ct != null && ct.isUDACompliantXML());
/*     */   }
/*     */   
/*     */   public String getContentTypeCharset() {
/* 199 */     ContentTypeHeader ct = getContentTypeHeader();
/* 200 */     return (ct != null) ? (String)((MimeType)ct.getValue()).getParameters().get("charset") : null;
/*     */   }
/*     */   
/*     */   public boolean hasHostHeader() {
/* 204 */     return (getHeaders().getFirstHeader(UpnpHeader.Type.HOST) != null);
/*     */   }
/*     */   
/*     */   public boolean isBodyNonEmptyString() {
/* 208 */     return (hasBody() && 
/* 209 */       getBodyType().equals(BodyType.STRING) && 
/* 210 */       getBodyString().length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 215 */     return "(" + getClass().getSimpleName() + ") " + getOperation().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\UpnpMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */