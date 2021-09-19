/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class SerializableEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private byte[] objSer;
/*     */   private Serializable objRef;
/*     */   
/*     */   public SerializableEntity(Serializable ser, boolean bufferize) throws IOException {
/*  65 */     if (ser == null) {
/*  66 */       throw new IllegalArgumentException("Source object may not be null");
/*     */     }
/*     */     
/*  69 */     if (bufferize) {
/*  70 */       createBytes(ser);
/*     */     } else {
/*  72 */       this.objRef = ser;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createBytes(Serializable ser) throws IOException {
/*  77 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  78 */     ObjectOutputStream out = new ObjectOutputStream(baos);
/*  79 */     out.writeObject(ser);
/*  80 */     out.flush();
/*  81 */     this.objSer = baos.toByteArray();
/*     */   }
/*     */   
/*     */   public InputStream getContent() throws IOException, IllegalStateException {
/*  85 */     if (this.objSer == null) {
/*  86 */       createBytes(this.objRef);
/*     */     }
/*  88 */     return new ByteArrayInputStream(this.objSer);
/*     */   }
/*     */   
/*     */   public long getContentLength() {
/*  92 */     if (this.objSer == null) {
/*  93 */       return -1L;
/*     */     }
/*  95 */     return this.objSer.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 100 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isStreaming() {
/* 104 */     return (this.objSer == null);
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 108 */     if (outstream == null) {
/* 109 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/*     */     
/* 112 */     if (this.objSer == null) {
/* 113 */       ObjectOutputStream out = new ObjectOutputStream(outstream);
/* 114 */       out.writeObject(this.objRef);
/* 115 */       out.flush();
/*     */     } else {
/* 117 */       outstream.write(this.objSer);
/* 118 */       outstream.flush();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\SerializableEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */