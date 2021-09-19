/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.Writer;
/*     */ import java.sql.Clob;
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
/*     */ public class Clob
/*     */   implements Clob, OutputStreamWatcher, WriterWatcher
/*     */ {
/*     */   private String charData;
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */   
/*     */   Clob(ExceptionInterceptor exceptionInterceptor) {
/*  46 */     this.charData = "";
/*  47 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */   
/*     */   Clob(String charDataInit, ExceptionInterceptor exceptionInterceptor) {
/*  51 */     this.charData = charDataInit;
/*  52 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getAsciiStream() throws SQLException {
/*  59 */     if (this.charData != null) {
/*  60 */       return new ByteArrayInputStream(this.charData.getBytes());
/*     */     }
/*     */     
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reader getCharacterStream() throws SQLException {
/*  70 */     if (this.charData != null) {
/*  71 */       return new StringReader(this.charData);
/*     */     }
/*     */     
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubString(long startPos, int length) throws SQLException {
/*  81 */     if (startPos < 1L) {
/*  82 */       throw SQLError.createSQLException(Messages.getString("Clob.6"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/*  86 */     int adjustedStartPos = (int)startPos - 1;
/*  87 */     int adjustedEndIndex = adjustedStartPos + length;
/*     */     
/*  89 */     if (this.charData != null) {
/*  90 */       if (adjustedEndIndex > this.charData.length()) {
/*  91 */         throw SQLError.createSQLException(Messages.getString("Clob.7"), "S1009", this.exceptionInterceptor);
/*     */       }
/*     */ 
/*     */       
/*  95 */       return this.charData.substring(adjustedStartPos, adjustedEndIndex);
/*     */     } 
/*     */ 
/*     */     
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() throws SQLException {
/* 106 */     if (this.charData != null) {
/* 107 */       return this.charData.length();
/*     */     }
/*     */     
/* 110 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long position(Clob arg0, long arg1) throws SQLException {
/* 117 */     return position(arg0.getSubString(0L, (int)arg0.length()), arg1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long position(String stringToFind, long startPos) throws SQLException {
/* 125 */     if (startPos < 1L) {
/* 126 */       throw SQLError.createSQLException(Messages.getString("Clob.8") + startPos + Messages.getString("Clob.9"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (this.charData != null) {
/* 132 */       if (startPos - 1L > this.charData.length()) {
/* 133 */         throw SQLError.createSQLException(Messages.getString("Clob.10"), "S1009", this.exceptionInterceptor);
/*     */       }
/*     */ 
/*     */       
/* 137 */       int pos = this.charData.indexOf(stringToFind, (int)(startPos - 1L));
/*     */       
/* 139 */       return (pos == -1) ? -1L : (pos + 1);
/*     */     } 
/*     */     
/* 142 */     return -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream setAsciiStream(long indexToWriteAt) throws SQLException {
/* 149 */     if (indexToWriteAt < 1L) {
/* 150 */       throw SQLError.createSQLException(Messages.getString("Clob.0"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 154 */     WatchableOutputStream bytesOut = new WatchableOutputStream();
/* 155 */     bytesOut.setWatcher(this);
/*     */     
/* 157 */     if (indexToWriteAt > 0L) {
/* 158 */       bytesOut.write(this.charData.getBytes(), 0, (int)(indexToWriteAt - 1L));
/*     */     }
/*     */ 
/*     */     
/* 162 */     return bytesOut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Writer setCharacterStream(long indexToWriteAt) throws SQLException {
/* 169 */     if (indexToWriteAt < 1L) {
/* 170 */       throw SQLError.createSQLException(Messages.getString("Clob.1"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 174 */     WatchableWriter writer = new WatchableWriter();
/* 175 */     writer.setWatcher(this);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     if (indexToWriteAt > 1L) {
/* 181 */       writer.write(this.charData, 0, (int)(indexToWriteAt - 1L));
/*     */     }
/*     */     
/* 184 */     return writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int setString(long pos, String str) throws SQLException {
/* 191 */     if (pos < 1L) {
/* 192 */       throw SQLError.createSQLException(Messages.getString("Clob.2"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 196 */     if (str == null) {
/* 197 */       throw SQLError.createSQLException(Messages.getString("Clob.3"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 201 */     StringBuffer charBuf = new StringBuffer(this.charData);
/*     */     
/* 203 */     pos--;
/*     */     
/* 205 */     int strLength = str.length();
/*     */     
/* 207 */     charBuf.replace((int)pos, (int)(pos + strLength), str);
/*     */     
/* 209 */     this.charData = charBuf.toString();
/*     */     
/* 211 */     return strLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int setString(long pos, String str, int offset, int len) throws SQLException {
/* 219 */     if (pos < 1L) {
/* 220 */       throw SQLError.createSQLException(Messages.getString("Clob.4"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 224 */     if (str == null) {
/* 225 */       throw SQLError.createSQLException(Messages.getString("Clob.5"), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 229 */     StringBuffer charBuf = new StringBuffer(this.charData);
/*     */     
/* 231 */     pos--;
/*     */     
/* 233 */     String replaceString = str.substring(offset, len);
/*     */     
/* 235 */     charBuf.replace((int)pos, (int)(pos + replaceString.length()), replaceString);
/*     */ 
/*     */     
/* 238 */     this.charData = charBuf.toString();
/*     */     
/* 240 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void streamClosed(WatchableOutputStream out) {
/* 247 */     int streamSize = out.size();
/*     */     
/* 249 */     if (streamSize < this.charData.length()) {
/*     */       try {
/* 251 */         out.write(StringUtils.getBytes(this.charData, (String)null, (String)null, false, (ConnectionImpl)null, this.exceptionInterceptor), streamSize, this.charData.length() - streamSize);
/*     */       
/*     */       }
/* 254 */       catch (SQLException ex) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 259 */     this.charData = StringUtils.toAsciiString(out.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void truncate(long length) throws SQLException {
/* 266 */     if (length > this.charData.length()) {
/* 267 */       throw SQLError.createSQLException(Messages.getString("Clob.11") + this.charData.length() + Messages.getString("Clob.12") + length + Messages.getString("Clob.13"), this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     this.charData = this.charData.substring(0, (int)length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writerClosed(char[] charDataBeingWritten) {
/* 280 */     this.charData = new String(charDataBeingWritten);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writerClosed(WatchableWriter out) {
/* 287 */     int dataLength = out.size();
/*     */     
/* 289 */     if (dataLength < this.charData.length()) {
/* 290 */       out.write(this.charData, dataLength, this.charData.length() - dataLength);
/*     */     }
/*     */ 
/*     */     
/* 294 */     this.charData = out.toString();
/*     */   }
/*     */   
/*     */   public void free() throws SQLException {
/* 298 */     this.charData = null;
/*     */   }
/*     */   
/*     */   public Reader getCharacterStream(long pos, long length) throws SQLException {
/* 302 */     return new StringReader(getSubString(pos, (int)length));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\Clob.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */