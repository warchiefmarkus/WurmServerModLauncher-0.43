/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.v2.runtime.output.Pcdata;
/*     */ import com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput;
/*     */ import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Base64Data
/*     */   extends Pcdata
/*     */ {
/*     */   private DataHandler dataHandler;
/*     */   private byte[] data;
/*     */   private int dataLen;
/*     */   @Nullable
/*     */   private String mimeType;
/*     */   
/*     */   public void set(byte[] data, int len, @Nullable String mimeType) {
/*  98 */     this.data = data;
/*  99 */     this.dataLen = len;
/* 100 */     this.dataHandler = null;
/* 101 */     this.mimeType = mimeType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(byte[] data, @Nullable String mimeType) {
/* 111 */     set(data, data.length, mimeType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(DataHandler data) {
/* 118 */     assert data != null;
/* 119 */     this.dataHandler = data;
/* 120 */     this.data = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataHandler getDataHandler() {
/* 127 */     if (this.dataHandler == null) {
/* 128 */       this.dataHandler = new DataHandler(new DataSource() {
/*     */             public String getContentType() {
/* 130 */               return Base64Data.this.getMimeType();
/*     */             }
/*     */             
/*     */             public InputStream getInputStream() {
/* 134 */               return new ByteArrayInputStream(Base64Data.this.data, 0, Base64Data.this.dataLen);
/*     */             }
/*     */             
/*     */             public String getName() {
/* 138 */               return null;
/*     */             }
/*     */             
/*     */             public OutputStream getOutputStream() {
/* 142 */               throw new UnsupportedOperationException();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 147 */     return this.dataHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getExact() {
/* 154 */     get();
/* 155 */     if (this.dataLen != this.data.length) {
/* 156 */       byte[] buf = new byte[this.dataLen];
/* 157 */       System.arraycopy(this.data, 0, buf, 0, this.dataLen);
/* 158 */       this.data = buf;
/*     */     } 
/* 160 */     return this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 167 */     if (this.dataHandler != null) {
/* 168 */       return this.dataHandler.getInputStream();
/*     */     }
/* 170 */     return new ByteArrayInputStream(this.data, 0, this.dataLen);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasData() {
/* 178 */     return (this.data != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] get() {
/* 185 */     if (this.data == null) {
/*     */       try {
/* 187 */         ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx(1024);
/* 188 */         InputStream is = this.dataHandler.getDataSource().getInputStream();
/* 189 */         baos.readFrom(is);
/* 190 */         is.close();
/* 191 */         this.data = baos.getBuffer();
/* 192 */         this.dataLen = baos.size();
/* 193 */       } catch (IOException e) {
/*     */         
/* 195 */         this.dataLen = 0;
/*     */       } 
/*     */     }
/* 198 */     return this.data;
/*     */   }
/*     */   
/*     */   public int getDataLen() {
/* 202 */     return this.dataLen;
/*     */   }
/*     */   
/*     */   public String getMimeType() {
/* 206 */     if (this.mimeType == null)
/* 207 */       return "application/octet-stream"; 
/* 208 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 218 */     get();
/* 219 */     return (this.dataLen + 2) / 3 * 4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/*     */     byte b1;
/* 231 */     int offset = index % 4;
/* 232 */     int base = index / 4 * 3;
/*     */ 
/*     */ 
/*     */     
/* 236 */     switch (offset) {
/*     */       case 0:
/* 238 */         return DatatypeConverterImpl.encode(this.data[base] >> 2);
/*     */       case 1:
/* 240 */         if (base + 1 < this.dataLen) {
/* 241 */           b1 = this.data[base + 1];
/*     */         } else {
/* 243 */           b1 = 0;
/* 244 */         }  return DatatypeConverterImpl.encode((this.data[base] & 0x3) << 4 | b1 >> 4 & 0xF);
/*     */ 
/*     */       
/*     */       case 2:
/* 248 */         if (base + 1 < this.dataLen) {
/* 249 */           byte b2; b1 = this.data[base + 1];
/* 250 */           if (base + 2 < this.dataLen) {
/* 251 */             b2 = this.data[base + 2];
/*     */           } else {
/* 253 */             b2 = 0;
/*     */           } 
/* 255 */           return DatatypeConverterImpl.encode((b1 & 0xF) << 2 | b2 >> 6 & 0x3);
/*     */         } 
/*     */ 
/*     */         
/* 259 */         return '=';
/*     */       case 3:
/* 261 */         if (base + 2 < this.dataLen) {
/* 262 */           return DatatypeConverterImpl.encode(this.data[base + 2] & 0x3F);
/*     */         }
/* 264 */         return '=';
/*     */     } 
/*     */     
/* 267 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 276 */     StringBuilder buf = new StringBuilder();
/* 277 */     get();
/* 278 */     for (int i = start; i < end; i++)
/* 279 */       buf.append(charAt(i)); 
/* 280 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 287 */     get();
/* 288 */     return DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen);
/*     */   }
/*     */   
/*     */   public void writeTo(char[] buf, int start) {
/* 292 */     get();
/* 293 */     DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen, buf, start);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(UTF8XmlOutput output) throws IOException {
/* 298 */     get();
/* 299 */     output.text(this.data, this.dataLen);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\Base64Data.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */