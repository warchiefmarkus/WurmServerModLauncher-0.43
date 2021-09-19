/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class Wire
/*     */ {
/*     */   private final Log log;
/*     */   
/*     */   public Wire(Log log) {
/*  49 */     this.log = log;
/*     */   }
/*     */ 
/*     */   
/*     */   private void wire(String header, InputStream instream) throws IOException {
/*  54 */     StringBuilder buffer = new StringBuilder();
/*     */     int ch;
/*  56 */     while ((ch = instream.read()) != -1) {
/*  57 */       if (ch == 13) {
/*  58 */         buffer.append("[\\r]"); continue;
/*  59 */       }  if (ch == 10) {
/*  60 */         buffer.append("[\\n]\"");
/*  61 */         buffer.insert(0, "\"");
/*  62 */         buffer.insert(0, header);
/*  63 */         this.log.debug(buffer.toString());
/*  64 */         buffer.setLength(0); continue;
/*  65 */       }  if (ch < 32 || ch > 127) {
/*  66 */         buffer.append("[0x");
/*  67 */         buffer.append(Integer.toHexString(ch));
/*  68 */         buffer.append("]"); continue;
/*     */       } 
/*  70 */       buffer.append((char)ch);
/*     */     } 
/*     */     
/*  73 */     if (buffer.length() > 0) {
/*  74 */       buffer.append('"');
/*  75 */       buffer.insert(0, '"');
/*  76 */       buffer.insert(0, header);
/*  77 */       this.log.debug(buffer.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enabled() {
/*  83 */     return this.log.isDebugEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(InputStream outstream) throws IOException {
/*  88 */     if (outstream == null) {
/*  89 */       throw new IllegalArgumentException("Output may not be null");
/*     */     }
/*  91 */     wire(">> ", outstream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(InputStream instream) throws IOException {
/*  96 */     if (instream == null) {
/*  97 */       throw new IllegalArgumentException("Input may not be null");
/*     */     }
/*  99 */     wire("<< ", instream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(byte[] b, int off, int len) throws IOException {
/* 104 */     if (b == null) {
/* 105 */       throw new IllegalArgumentException("Output may not be null");
/*     */     }
/* 107 */     wire(">> ", new ByteArrayInputStream(b, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(byte[] b, int off, int len) throws IOException {
/* 112 */     if (b == null) {
/* 113 */       throw new IllegalArgumentException("Input may not be null");
/*     */     }
/* 115 */     wire("<< ", new ByteArrayInputStream(b, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(byte[] b) throws IOException {
/* 120 */     if (b == null) {
/* 121 */       throw new IllegalArgumentException("Output may not be null");
/*     */     }
/* 123 */     wire(">> ", new ByteArrayInputStream(b));
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(byte[] b) throws IOException {
/* 128 */     if (b == null) {
/* 129 */       throw new IllegalArgumentException("Input may not be null");
/*     */     }
/* 131 */     wire("<< ", new ByteArrayInputStream(b));
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(int b) throws IOException {
/* 136 */     output(new byte[] { (byte)b });
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(int b) throws IOException {
/* 141 */     input(new byte[] { (byte)b });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void output(String s) throws IOException {
/* 150 */     if (s == null) {
/* 151 */       throw new IllegalArgumentException("Output may not be null");
/*     */     }
/* 153 */     output(s.getBytes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void input(String s) throws IOException {
/* 162 */     if (s == null) {
/* 163 */       throw new IllegalArgumentException("Input may not be null");
/*     */     }
/* 165 */     input(s.getBytes());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\Wire.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */