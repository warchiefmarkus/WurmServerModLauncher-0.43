/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.cache.CacheUtilities;
/*     */ import com.sun.javaws.cache.DiskCacheEntry;
/*     */ import com.sun.javaws.cache.DownloadProtocol;
/*     */ import com.sun.javaws.exceptions.JNLPException;
/*     */ import com.sun.javaws.jnl.IconDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.PixelGrabber;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IcoEncoder
/*     */ {
/*     */   private static final int IMAGE_TYPE = 1;
/*     */   private static final int IMAGE_KIND = 0;
/*     */   private OutputStream outputS;
/*  31 */   private static byte ICON_SIZE = 32;
/*  32 */   private static byte NUM_COLORS = 0;
/*  33 */   private static int BYTE_SIZE = 8;
/*     */   
/*     */   private Image awtImage;
/*     */   
/*     */   private IcoEncoder(OutputStream paramOutputStream, Image paramImage) {
/*  38 */     this.outputS = paramOutputStream;
/*  39 */     this.awtImage = paramImage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getIconPath(LaunchDesc paramLaunchDesc) {
/*  48 */     IconDesc iconDesc = paramLaunchDesc.getInformation().getIconLocation(1, 0);
/*     */ 
/*     */     
/*  51 */     if (iconDesc != null) {
/*  52 */       return getIconPath(iconDesc.getLocation(), iconDesc.getVersion());
/*     */     }
/*  54 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getIconPath(URL paramURL, String paramString) {
/*  59 */     Trace.println("Getting icon path", TraceLevel.BASIC);
/*     */     
/*  61 */     File file = null;
/*     */ 
/*     */     
/*     */     try {
/*  65 */       DiskCacheEntry diskCacheEntry = DownloadProtocol.getResource(paramURL, paramString, 2, true, null);
/*     */ 
/*     */ 
/*     */       
/*  69 */       File file1 = diskCacheEntry.getMappedBitmap();
/*     */       
/*  71 */       if (file1 == null || !file1.exists()) {
/*  72 */         file1 = null;
/*     */         
/*  74 */         Image image = CacheUtilities.getSharedInstance().loadImage(diskCacheEntry.getFile().getPath());
/*     */ 
/*     */ 
/*     */         
/*  78 */         file = saveICOfile(image);
/*     */         
/*  80 */         Trace.println("updating ICO: " + file, TraceLevel.BASIC);
/*     */         
/*  82 */         if (file != null) {
/*  83 */           file1 = Cache.putMappedImage(paramURL, paramString, file);
/*  84 */           file = null;
/*     */         } 
/*     */       } 
/*  87 */       if (file1 != null) {
/*  88 */         return file1.getPath();
/*     */       }
/*  90 */     } catch (IOException iOException) {
/*  91 */       Trace.println("exception creating BMP: " + iOException, TraceLevel.BASIC);
/*  92 */     } catch (JNLPException jNLPException) {
/*  93 */       Trace.println("exception creating BMP: " + jNLPException, TraceLevel.BASIC);
/*     */     } 
/*     */     
/*  96 */     if (file != null) file.delete();
/*     */ 
/*     */ 
/*     */     
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File saveICOfile(Image paramImage) {
/* 109 */     FileOutputStream fileOutputStream = null;
/* 110 */     File file1 = null;
/* 111 */     File file2 = new File(Config.getJavawsCacheDir());
/*     */     try {
/* 113 */       file1 = File.createTempFile("javaws", ".ico", file2);
/* 114 */       fileOutputStream = new FileOutputStream(file1);
/*     */       
/* 116 */       IcoEncoder icoEncoder = new IcoEncoder(fileOutputStream, paramImage);
/*     */       
/* 118 */       icoEncoder.encode();
/* 119 */       fileOutputStream.close();
/* 120 */       return file1;
/* 121 */     } catch (Throwable throwable) {
/*     */       
/* 123 */       if (fileOutputStream != null) {
/*     */         try {
/* 125 */           fileOutputStream.close();
/* 126 */         } catch (IOException iOException) {}
/*     */       }
/* 128 */       if (file1 != null) file1.delete(); 
/* 129 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createBitmap() throws IOException {
/* 136 */     byte b1 = 32;
/* 137 */     byte b2 = 32;
/* 138 */     byte b3 = 0;
/*     */ 
/*     */     
/* 141 */     int[] arrayOfInt = new int[b1 * b2];
/*     */ 
/*     */     
/* 144 */     byte[] arrayOfByte1 = new byte[b1 * b2 * 3];
/* 145 */     byte[] arrayOfByte2 = new byte[b1 * b2 * 3];
/*     */ 
/*     */     
/* 148 */     byte[] arrayOfByte3 = new byte[b1 * 4];
/* 149 */     byte[] arrayOfByte4 = new byte[b1 * 4];
/*     */ 
/*     */     
/* 152 */     PixelGrabber pixelGrabber = new PixelGrabber(this.awtImage.getScaledInstance(32, 32, 1), 0, 0, b1, b2, arrayOfInt, 0, b1);
/*     */     
/*     */     try {
/* 155 */       if (pixelGrabber.grabPixels()) {
/*     */         
/* 157 */         Trace.println("pixels grabbed successfully", TraceLevel.BASIC);
/*     */       } else {
/*     */         
/* 160 */         Trace.println("cannot grab pixels!", TraceLevel.BASIC);
/*     */       } 
/* 162 */     } catch (InterruptedException interruptedException) {
/* 163 */       interruptedException.printStackTrace();
/*     */     } 
/*     */     
/* 166 */     byte b = 0;
/* 167 */     byte b4 = 0;
/* 168 */     byte b5 = 0;
/* 169 */     byte b6 = 0;
/*     */     
/*     */     byte b7;
/* 172 */     for (b7 = 0; b7 < b1 * b2; b7++) {
/*     */       
/* 174 */       int j = arrayOfInt[b7] >> 24 & 0xFF;
/* 175 */       int k = arrayOfInt[b7] >> 16 & 0xFF;
/* 176 */       int m = arrayOfInt[b7] >> 8 & 0xFF;
/* 177 */       int n = arrayOfInt[b7] & 0xFF;
/*     */ 
/*     */ 
/*     */       
/* 181 */       if (j != 0) {
/* 182 */         Trace.print(" 1", TraceLevel.BASIC);
/*     */       } else {
/* 184 */         Trace.print(" " + j, TraceLevel.BASIC);
/* 185 */       }  b6++;
/* 186 */       if (b6 == 32) {
/* 187 */         Trace.println(" ", TraceLevel.BASIC);
/* 188 */         b6 = 0;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 193 */       if (j == 0) {
/* 194 */         b = (byte)(b | 128 >> b5);
/*     */       }
/* 196 */       b5++;
/* 197 */       if (b5 == 8) {
/* 198 */         arrayOfByte3[b4++] = b;
/* 199 */         b = 0;
/* 200 */         b5 = 0;
/*     */       } 
/*     */ 
/*     */       
/* 204 */       arrayOfByte1[b3++] = (byte)n;
/* 205 */       arrayOfByte1[b3++] = (byte)m;
/* 206 */       arrayOfByte1[b3++] = (byte)k;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     b7 = 0;
/*     */     
/* 214 */     Trace.println("andPxiels bitmap", TraceLevel.BASIC); int i;
/* 215 */     for (i = 0; i < 128; i++) {
/*     */       byte b9;
/* 217 */       for (b9 = 0; b9 < 8; b9 = (byte)(b9 + 1)) {
/* 218 */         if ((arrayOfByte3[i] & 128 >> b9) != 0) {
/* 219 */           Trace.print(" 1", TraceLevel.BASIC);
/*     */         } else {
/* 221 */           Trace.print(" 0", TraceLevel.BASIC);
/*     */         } 
/*     */       } 
/* 224 */       b7++;
/* 225 */       if (b7 == 4) {
/* 226 */         Trace.println(" ", TraceLevel.BASIC);
/* 227 */         b7 = 0;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     for (byte b8 = 0; b8 < b2; b8++) {
/*     */ 
/*     */       
/* 239 */       int j = b8 * b1 * 3;
/* 240 */       i = (b2 - b8 - 1) * b1 * 3;
/*     */       
/*     */       byte b9;
/* 243 */       for (b9 = 0; b9 < b1 * 3; b9++) {
/* 244 */         arrayOfByte2[j + b9] = arrayOfByte1[i + b9];
/*     */       }
/*     */ 
/*     */       
/* 248 */       j = b8 * b1 / 8;
/* 249 */       i = (b2 - b8 - 1) * b1 / 8;
/*     */ 
/*     */       
/* 252 */       for (b9 = 0; b9 < b1 / 8; b9++) {
/* 253 */         arrayOfByte4[j + b9] = arrayOfByte3[i + b9];
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 259 */     this.outputS.write(arrayOfByte2);
/*     */ 
/*     */     
/* 262 */     this.outputS.write(arrayOfByte4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void encode() {
/* 270 */     writeIcoHeader();
/*     */ 
/*     */     
/* 273 */     writeIconDirEntry();
/*     */ 
/*     */     
/*     */     try {
/* 277 */       writeInfoHeader(40, 24);
/*     */ 
/*     */       
/* 280 */       createBitmap();
/*     */     }
/* 282 */     catch (Exception exception) {
/* 283 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeInfoHeader(int paramInt1, int paramInt2) throws IOException {
/* 293 */     writeDWord(paramInt1);
/*     */ 
/*     */     
/* 296 */     writeDWord(32);
/*     */ 
/*     */     
/* 299 */     writeDWord(64);
/*     */ 
/*     */     
/* 302 */     writeWord(1);
/*     */ 
/*     */     
/* 305 */     writeWord(paramInt2);
/*     */ 
/*     */     
/* 308 */     writeDWord(0);
/*     */ 
/*     */     
/* 311 */     writeDWord(0);
/*     */ 
/*     */     
/* 314 */     writeDWord(0);
/*     */ 
/*     */     
/* 317 */     writeDWord(0);
/*     */ 
/*     */     
/* 320 */     writeDWord(0);
/*     */ 
/*     */     
/* 323 */     writeDWord(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeIconDirEntry() {
/*     */     try {
/* 333 */       byte b = ICON_SIZE;
/* 334 */       this.outputS.write(b);
/*     */ 
/*     */       
/* 337 */       this.outputS.write(b);
/*     */ 
/*     */       
/* 340 */       b = NUM_COLORS;
/* 341 */       this.outputS.write(b);
/*     */ 
/*     */       
/* 344 */       b = 0;
/* 345 */       this.outputS.write(b);
/*     */ 
/*     */       
/* 348 */       b = 1;
/* 349 */       writeWord(b);
/*     */ 
/*     */       
/* 352 */       b = 24;
/* 353 */       writeWord(b);
/*     */ 
/*     */       
/* 356 */       char c = 'ನ';
/* 357 */       writeDWord(c);
/*     */ 
/*     */       
/* 360 */       byte b1 = 22;
/* 361 */       writeDWord(b1);
/*     */     }
/* 363 */     catch (IOException iOException) {
/* 364 */       iOException.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeIcoHeader() {
/*     */     try {
/* 373 */       boolean bool = false;
/* 374 */       writeWord(bool);
/*     */ 
/*     */       
/* 377 */       bool = true;
/* 378 */       writeWord(bool);
/*     */ 
/*     */       
/* 381 */       writeWord(bool);
/*     */     }
/* 383 */     catch (IOException iOException) {
/* 384 */       iOException.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeWord(int paramInt) throws IOException {
/* 390 */     this.outputS.write(paramInt & 0xFF);
/* 391 */     this.outputS.write((paramInt & 0xFF00) >> 8);
/*     */   }
/*     */   
/*     */   public void writeDWord(int paramInt) throws IOException {
/* 395 */     this.outputS.write(paramInt & 0xFF);
/* 396 */     this.outputS.write((paramInt & 0xFF00) >> 8);
/* 397 */     this.outputS.write((paramInt & 0xFF0000) >> 16);
/* 398 */     this.outputS.write((paramInt & 0xFF000000) >> 24);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\IcoEncoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */