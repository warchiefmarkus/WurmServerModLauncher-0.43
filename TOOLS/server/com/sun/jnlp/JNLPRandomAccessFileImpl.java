/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import javax.jnlp.FileContents;
/*     */ import javax.jnlp.JNLPRandomAccessFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JNLPRandomAccessFileImpl
/*     */   implements JNLPRandomAccessFile
/*     */ {
/*  19 */   private RandomAccessFile _raf = null;
/*  20 */   private FileContents _contents = null;
/*  21 */   private long _length = 0L;
/*  22 */   private String _message = null;
/*     */   
/*     */   JNLPRandomAccessFileImpl(File paramFile, String paramString, FileContents paramFileContents) throws IOException {
/*  25 */     this._raf = new RandomAccessFile(paramFile, paramString);
/*  26 */     this._length = this._raf.length();
/*  27 */     this._contents = paramFileContents;
/*  28 */     if (this._contents == null) {
/*  29 */       throw new IllegalArgumentException("FileContents can not be null");
/*     */     }
/*     */ 
/*     */     
/*  33 */     if (this._message == null) {
/*  34 */       this._message = ResourceManager.getString("APIImpl.persistence.filesizemessage");
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  39 */     this._raf.close();
/*     */   }
/*     */   
/*     */   public long length() throws IOException {
/*  43 */     return this._raf.length();
/*     */   }
/*     */   
/*     */   public long getFilePointer() throws IOException {
/*  47 */     return this._raf.getFilePointer();
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/*  51 */     return this._raf.read();
/*     */   }
/*     */   
/*     */   public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/*  55 */     return this._raf.read(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public int read(byte[] paramArrayOfbyte) throws IOException {
/*  59 */     return this._raf.read(paramArrayOfbyte);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] paramArrayOfbyte) throws IOException {
/*  63 */     this._raf.readFully(paramArrayOfbyte);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/*  67 */     this._raf.readFully(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public int skipBytes(int paramInt) throws IOException {
/*  71 */     return this._raf.skipBytes(paramInt);
/*     */   }
/*     */   
/*     */   public boolean readBoolean() throws IOException {
/*  75 */     return this._raf.readBoolean();
/*     */   }
/*     */   
/*     */   public byte readByte() throws IOException {
/*  79 */     return this._raf.readByte();
/*     */   }
/*     */   
/*     */   public int readUnsignedByte() throws IOException {
/*  83 */     return this._raf.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public short readShort() throws IOException {
/*  87 */     return this._raf.readShort();
/*     */   }
/*     */   
/*     */   public int readUnsignedShort() throws IOException {
/*  91 */     return this._raf.readUnsignedShort();
/*     */   }
/*     */   
/*     */   public char readChar() throws IOException {
/*  95 */     return this._raf.readChar();
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException {
/*  99 */     return this._raf.readInt();
/*     */   }
/*     */   
/*     */   public long readLong() throws IOException {
/* 103 */     return this._raf.readLong();
/*     */   }
/*     */   
/*     */   public float readFloat() throws IOException {
/* 107 */     return this._raf.readFloat();
/*     */   }
/*     */   
/*     */   public double readDouble() throws IOException {
/* 111 */     return this._raf.readDouble();
/*     */   }
/*     */   
/*     */   public String readLine() throws IOException {
/* 115 */     return this._raf.readLine();
/*     */   }
/*     */   
/*     */   public String readUTF() throws IOException {
/* 119 */     return this._raf.readUTF();
/*     */   }
/*     */   
/*     */   public void seek(long paramLong) throws IOException {
/* 123 */     this._raf.seek(paramLong);
/*     */   }
/*     */   
/*     */   public void setLength(long paramLong) throws IOException {
/* 127 */     if (paramLong > this._contents.getMaxLength()) {
/* 128 */       throw new IOException(this._message);
/*     */     }
/* 130 */     this._raf.setLength(paramLong);
/*     */   }
/*     */   
/*     */   public void write(int paramInt) throws IOException {
/* 134 */     checkWrite(1);
/* 135 */     this._raf.write(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] paramArrayOfbyte) throws IOException {
/* 140 */     if (paramArrayOfbyte != null) checkWrite(paramArrayOfbyte.length); 
/* 141 */     this._raf.write(paramArrayOfbyte);
/*     */   }
/*     */   
/*     */   public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/* 145 */     checkWrite(paramInt2);
/* 146 */     this._raf.write(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public void writeBoolean(boolean paramBoolean) throws IOException {
/* 150 */     checkWrite(1);
/* 151 */     this._raf.writeBoolean(paramBoolean);
/*     */   }
/*     */   
/*     */   public void writeByte(int paramInt) throws IOException {
/* 155 */     checkWrite(1);
/* 156 */     this._raf.writeByte(paramInt);
/*     */   }
/*     */   
/*     */   public void writeShort(int paramInt) throws IOException {
/* 160 */     checkWrite(2);
/* 161 */     this._raf.writeShort(paramInt);
/*     */   }
/*     */   
/*     */   public void writeChar(int paramInt) throws IOException {
/* 165 */     checkWrite(2);
/* 166 */     this._raf.writeChar(paramInt);
/*     */   }
/*     */   
/*     */   public void writeInt(int paramInt) throws IOException {
/* 170 */     checkWrite(4);
/* 171 */     this._raf.writeInt(paramInt);
/*     */   }
/*     */   
/*     */   public void writeLong(long paramLong) throws IOException {
/* 175 */     checkWrite(8);
/* 176 */     this._raf.writeLong(paramLong);
/*     */   }
/*     */   
/*     */   public void writeFloat(float paramFloat) throws IOException {
/* 180 */     checkWrite(4);
/* 181 */     this._raf.writeFloat(paramFloat);
/*     */   }
/*     */   
/*     */   public void writeDouble(double paramDouble) throws IOException {
/* 185 */     checkWrite(8);
/* 186 */     this._raf.writeDouble(paramDouble);
/*     */   }
/*     */   
/*     */   public void writeBytes(String paramString) throws IOException {
/* 190 */     if (paramString != null) checkWrite(paramString.length()); 
/* 191 */     this._raf.writeBytes(paramString);
/*     */   }
/*     */   
/*     */   public void writeChars(String paramString) throws IOException {
/* 195 */     if (paramString != null) checkWrite(paramString.length() * 2); 
/* 196 */     this._raf.writeChars(paramString);
/*     */   }
/*     */   
/*     */   public void writeUTF(String paramString) throws IOException {
/* 200 */     if (paramString != null) checkWrite(getUTFLen(paramString)); 
/* 201 */     this._raf.writeUTF(paramString);
/*     */   }
/*     */   
/*     */   private int getUTFLen(String paramString) {
/* 205 */     int i = paramString.length();
/* 206 */     char[] arrayOfChar = new char[i];
/*     */ 
/*     */     
/* 209 */     paramString.getChars(0, i, arrayOfChar, 0);
/*     */     
/* 211 */     byte b1 = 2;
/* 212 */     for (byte b2 = 0; b2 < i; b2++) {
/* 213 */       char c = arrayOfChar[b2];
/* 214 */       if (c >= '\001' && c <= '') {
/* 215 */         b1++;
/* 216 */       } else if (c > '߿') {
/* 217 */         b1 += 3;
/*     */       } else {
/* 219 */         b1 += 2;
/*     */       } 
/*     */     } 
/*     */     
/* 223 */     return b1;
/*     */   }
/*     */   
/*     */   private void checkWrite(int paramInt) throws IOException {
/* 227 */     if (this._raf.getFilePointer() + paramInt > this._contents.getMaxLength())
/* 228 */       throw new IOException(this._message); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\JNLPRandomAccessFileImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */