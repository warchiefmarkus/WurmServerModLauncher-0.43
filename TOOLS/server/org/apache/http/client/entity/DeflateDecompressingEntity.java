/*     */ package org.apache.http.client.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeflateDecompressingEntity
/*     */   extends DecompressingEntity
/*     */ {
/*     */   public DeflateDecompressingEntity(HttpEntity entity) {
/*  63 */     super(entity);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InputStream decorate(InputStream wrapped) throws IOException {
/* 101 */     byte[] peeked = new byte[6];
/*     */     
/* 103 */     PushbackInputStream pushback = new PushbackInputStream(wrapped, peeked.length);
/*     */     
/* 105 */     int headerLength = pushback.read(peeked);
/*     */     
/* 107 */     if (headerLength == -1) {
/* 108 */       throw new IOException("Unable to read the response");
/*     */     }
/*     */ 
/*     */     
/* 112 */     byte[] dummy = new byte[1];
/*     */     
/* 114 */     Inflater inf = new Inflater();
/*     */     
/*     */     try {
/*     */       int n;
/* 118 */       while ((n = inf.inflate(dummy)) == 0) {
/* 119 */         if (inf.finished())
/*     */         {
/*     */           
/* 122 */           throw new IOException("Unable to read the response");
/*     */         }
/*     */         
/* 125 */         if (inf.needsDictionary()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 131 */         if (inf.needsInput()) {
/* 132 */           inf.setInput(peeked);
/*     */         }
/*     */       } 
/*     */       
/* 136 */       if (n == -1) {
/* 137 */         throw new IOException("Unable to read the response");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       pushback.unread(peeked, 0, headerLength);
/* 145 */       return new DeflateStream(pushback, new Inflater());
/* 146 */     } catch (DataFormatException e) {
/*     */ 
/*     */ 
/*     */       
/* 150 */       pushback.unread(peeked, 0, headerLength);
/* 151 */       return new DeflateStream(pushback, new Inflater(true));
/*     */     } finally {
/* 153 */       inf.end();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header getContentEncoding() {
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 174 */     return -1L;
/*     */   }
/*     */   
/*     */   static class DeflateStream
/*     */     extends InflaterInputStream {
/*     */     private boolean closed = false;
/*     */     
/*     */     public DeflateStream(InputStream in, Inflater inflater) {
/* 182 */       super(in, inflater);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 187 */       if (this.closed) {
/*     */         return;
/*     */       }
/* 190 */       this.closed = true;
/* 191 */       this.inf.end();
/* 192 */       super.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\entity\DeflateDecompressingEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */