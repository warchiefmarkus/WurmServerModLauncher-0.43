/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.sql.Blob;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Blob
/*     */   implements Blob, OutputStreamWatcher
/*     */ {
/*  59 */   private byte[] binaryData = null;
/*     */   
/*     */   private boolean isClosed = false;
/*     */   
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */ 
/*     */   
/*     */   Blob(ExceptionInterceptor exceptionInterceptor) {
/*  67 */     setBinaryData(Constants.EMPTY_BYTE_ARRAY);
/*  68 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Blob(byte[] data, ExceptionInterceptor exceptionInterceptor) {
/*  78 */     setBinaryData(data);
/*  79 */     this.exceptionInterceptor = exceptionInterceptor;
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
/*     */   Blob(byte[] data, ResultSetInternalMethods creatorResultSetToSet, int columnIndexToSet) {
/*  93 */     setBinaryData(data);
/*     */   }
/*     */   
/*     */   private synchronized byte[] getBinaryData() {
/*  97 */     return this.binaryData;
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
/*     */   public synchronized InputStream getBinaryStream() throws SQLException {
/* 109 */     checkClosed();
/*     */     
/* 111 */     return new ByteArrayInputStream(getBinaryData());
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
/*     */   public synchronized byte[] getBytes(long pos, int length) throws SQLException {
/* 130 */     checkClosed();
/*     */     
/* 132 */     if (pos < 1L) {
/* 133 */       throw SQLError.createSQLException(Messages.getString("Blob.2"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 137 */     pos--;
/*     */     
/* 139 */     if (pos > this.binaryData.length) {
/* 140 */       throw SQLError.createSQLException("\"pos\" argument can not be larger than the BLOB's length.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 144 */     if (pos + length > this.binaryData.length) {
/* 145 */       throw SQLError.createSQLException("\"pos\" + \"length\" arguments can not be larger than the BLOB's length.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 149 */     byte[] newData = new byte[length];
/* 150 */     System.arraycopy(getBinaryData(), (int)pos, newData, 0, length);
/*     */     
/* 152 */     return newData;
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
/*     */   public synchronized long length() throws SQLException {
/* 165 */     checkClosed();
/*     */     
/* 167 */     return (getBinaryData()).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long position(byte[] pattern, long start) throws SQLException {
/* 174 */     throw SQLError.createSQLException("Not implemented", this.exceptionInterceptor);
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
/*     */   public synchronized long position(Blob pattern, long start) throws SQLException {
/* 192 */     checkClosed();
/*     */     
/* 194 */     return position(pattern.getBytes(0L, (int)pattern.length()), start);
/*     */   }
/*     */   
/*     */   private synchronized void setBinaryData(byte[] newBinaryData) {
/* 198 */     this.binaryData = newBinaryData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized OutputStream setBinaryStream(long indexToWriteAt) throws SQLException {
/* 206 */     checkClosed();
/*     */     
/* 208 */     if (indexToWriteAt < 1L) {
/* 209 */       throw SQLError.createSQLException(Messages.getString("Blob.0"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 213 */     WatchableOutputStream bytesOut = new WatchableOutputStream();
/* 214 */     bytesOut.setWatcher(this);
/*     */     
/* 216 */     if (indexToWriteAt > 0L) {
/* 217 */       bytesOut.write(this.binaryData, 0, (int)(indexToWriteAt - 1L));
/*     */     }
/*     */     
/* 220 */     return bytesOut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int setBytes(long writeAt, byte[] bytes) throws SQLException {
/* 227 */     checkClosed();
/*     */     
/* 229 */     return setBytes(writeAt, bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int setBytes(long writeAt, byte[] bytes, int offset, int length) throws SQLException {
/* 237 */     checkClosed();
/*     */     
/* 239 */     OutputStream bytesOut = setBinaryStream(writeAt);
/*     */     
/*     */     try {
/* 242 */       bytesOut.write(bytes, offset, length);
/* 243 */     } catch (IOException ioEx) {
/* 244 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("Blob.1"), "S1000", this.exceptionInterceptor);
/*     */       
/* 246 */       sqlEx.initCause(ioEx);
/*     */       
/* 248 */       throw sqlEx;
/*     */     } finally {
/*     */       try {
/* 251 */         bytesOut.close();
/* 252 */       } catch (IOException doNothing) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 257 */     return length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void streamClosed(byte[] byteData) {
/* 264 */     this.binaryData = byteData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void streamClosed(WatchableOutputStream out) {
/* 271 */     int streamSize = out.size();
/*     */     
/* 273 */     if (streamSize < this.binaryData.length) {
/* 274 */       out.write(this.binaryData, streamSize, this.binaryData.length - streamSize);
/*     */     }
/*     */ 
/*     */     
/* 278 */     this.binaryData = out.toByteArray();
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
/*     */   public synchronized void truncate(long len) throws SQLException {
/* 300 */     checkClosed();
/*     */     
/* 302 */     if (len < 0L) {
/* 303 */       throw SQLError.createSQLException("\"len\" argument can not be < 1.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 307 */     if (len > this.binaryData.length) {
/* 308 */       throw SQLError.createSQLException("\"len\" argument can not be larger than the BLOB's length.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     byte[] newData = new byte[(int)len];
/* 316 */     System.arraycopy(getBinaryData(), 0, newData, 0, (int)len);
/* 317 */     this.binaryData = newData;
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
/*     */   public synchronized void free() throws SQLException {
/* 339 */     this.binaryData = null;
/* 340 */     this.isClosed = true;
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
/*     */   public synchronized InputStream getBinaryStream(long pos, long length) throws SQLException {
/* 360 */     checkClosed();
/*     */     
/* 362 */     if (pos < 1L) {
/* 363 */       throw SQLError.createSQLException("\"pos\" argument can not be < 1.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 367 */     pos--;
/*     */     
/* 369 */     if (pos > this.binaryData.length) {
/* 370 */       throw SQLError.createSQLException("\"pos\" argument can not be larger than the BLOB's length.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 374 */     if (pos + length > this.binaryData.length) {
/* 375 */       throw SQLError.createSQLException("\"pos\" + \"length\" arguments can not be larger than the BLOB's length.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 379 */     return new ByteArrayInputStream(getBinaryData(), (int)pos, (int)length);
/*     */   }
/*     */   
/*     */   private synchronized void checkClosed() throws SQLException {
/* 383 */     if (this.isClosed)
/* 384 */       throw SQLError.createSQLException("Invalid operation on closed BLOB", "S1009", this.exceptionInterceptor); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\Blob.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */