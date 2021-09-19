/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.image.codec.jpeg.JPEGCodec;
/*     */ import com.sun.image.codec.jpeg.JPEGImageEncoder;
/*     */ import com.sun.javaws.cache.CacheImageLoader;
/*     */ import com.sun.javaws.cache.CacheImageLoaderCallback;
/*     */ import com.sun.javaws.jnl.IconDesc;
/*     */ import com.sun.javaws.jnl.InformationDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SplashGenerator
/*     */   extends Thread
/*     */   implements CacheImageLoaderCallback
/*     */ {
/*     */   private File _index;
/*     */   private File _dir;
/*     */   private final String _key;
/*     */   private final LaunchDesc _ld;
/*     */   private final Frame _owner;
/* 124 */   private Properties _props = new Properties();
/*     */   private boolean _useAppSplash = false;
/*     */   
/*     */   public SplashGenerator(Frame paramFrame, LaunchDesc paramLaunchDesc) {
/* 128 */     this._owner = paramFrame;
/* 129 */     this._ld = paramLaunchDesc;
/* 130 */     this._dir = new File(Config.getSplashDir());
/* 131 */     this._key = this._ld.getCanonicalHome().toString();
/*     */     
/* 133 */     String str = Config.getSplashIndex();
/* 134 */     this._index = new File(str);
/*     */ 
/*     */ 
/*     */     
/* 138 */     Config.setSplashCache();
/* 139 */     Config.storeIfDirty();
/*     */     
/* 141 */     if (this._index.exists()) {
/*     */       try {
/* 143 */         FileInputStream fileInputStream = new FileInputStream(this._index);
/* 144 */         if (fileInputStream != null) {
/* 145 */           this._props.load(fileInputStream);
/* 146 */           fileInputStream.close();
/*     */         } 
/* 148 */       } catch (IOException iOException) {
/* 149 */         Trace.ignoredException(iOException);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean needsCustomSplash() {
/* 155 */     return !this._props.containsKey(this._key);
/*     */   }
/*     */   
/*     */   public void remove() {
/* 159 */     addSplashToCacheIndex(this._key, null);
/*     */   }
/*     */   
/*     */   public void run() {
/* 163 */     InformationDesc informationDesc = this._ld.getInformation();
/* 164 */     IconDesc[] arrayOfIconDesc = informationDesc.getIcons();
/*     */ 
/*     */     
/*     */     try {
/* 168 */       this._dir.mkdirs();
/* 169 */     } catch (Throwable throwable) {
/* 170 */       splashError(throwable);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 175 */       this._index.createNewFile();
/* 176 */     } catch (Throwable throwable) {
/* 177 */       splashError(throwable);
/*     */     } 
/*     */ 
/*     */     
/* 181 */     IconDesc iconDesc = informationDesc.getIconLocation(2, 4);
/*     */ 
/*     */     
/* 184 */     this._useAppSplash = (iconDesc != null);
/*     */     
/* 186 */     if (!this._useAppSplash) {
/* 187 */       iconDesc = informationDesc.getIconLocation(2, 0);
/*     */     }
/*     */ 
/*     */     
/* 191 */     if (iconDesc == null) {
/*     */       try {
/* 193 */         create(null, null);
/* 194 */       } catch (Throwable throwable) {
/* 195 */         splashError(throwable);
/*     */       } 
/*     */     } else {
/* 198 */       CacheImageLoader.getInstance().loadImage(iconDesc, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void imageAvailable(IconDesc paramIconDesc, Image paramImage, File paramFile) {}
/*     */ 
/*     */   
/*     */   public void finalImageAvailable(IconDesc paramIconDesc, Image paramImage, File paramFile) {
/* 207 */     if (!Globals.isHeadless())
/*     */       try {
/* 209 */         create(paramImage, paramFile);
/* 210 */       } catch (Throwable throwable) {
/* 211 */         splashError(throwable);
/*     */       }  
/*     */   }
/*     */   
/*     */   public void create(Image paramImage, File paramFile) {
/*     */     byte b1, b2;
/*     */     Rectangle rectangle;
/* 218 */     InformationDesc informationDesc = this._ld.getInformation();
/* 219 */     String str1 = informationDesc.getTitle();
/* 220 */     String str2 = informationDesc.getVendor();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     byte b3 = 5;
/* 226 */     JPanel jPanel = new JPanel();
/* 227 */     Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
/*     */     
/* 229 */     CompoundBorder compoundBorder = new CompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createBevelBorder(0));
/*     */ 
/*     */     
/* 232 */     Insets insets = compoundBorder.getBorderInsets(jPanel);
/*     */     
/* 234 */     int i = 320;
/* 235 */     int j = 64 + 2 * b3 + insets.top + insets.bottom;
/*     */     
/* 237 */     if (paramImage == null) {
/* 238 */       b1 = b2 = 0;
/*     */     }
/* 240 */     else if (this._useAppSplash) {
/* 241 */       b2 = paramImage.getHeight(this._owner);
/* 242 */       b1 = paramImage.getWidth(this._owner);
/* 243 */       if (paramFile != null)
/* 244 */         try { String str = paramFile.getCanonicalPath();
/*     */           
/* 246 */           if (str.endsWith(".jpg")) {
/* 247 */             addSplashToCacheIndex(this._key, str);
/*     */             return;
/*     */           }  }
/* 250 */         catch (IOException iOException) {} 
/* 251 */       b3 = 0;
/* 252 */       i = Math.min(b1, dimension.width);
/* 253 */       j = Math.min(b2, dimension.height);
/*     */     } else {
/* 255 */       b1 = b2 = 64;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 260 */     BufferedImage bufferedImage = new BufferedImage(i, j, 5);
/* 261 */     Graphics2D graphics2D = bufferedImage.createGraphics();
/*     */ 
/*     */     
/* 264 */     if (this._useAppSplash) {
/* 265 */       rectangle = new Rectangle(0, 0, i, j);
/*     */     } else {
/* 267 */       graphics2D.setColor(new Color(238, 238, 238));
/* 268 */       graphics2D.fillRect(0, 0, i, j);
/* 269 */       graphics2D.setColor(Color.black);
/* 270 */       compoundBorder.paintBorder(jPanel, graphics2D, 0, 0, i, j);
/*     */       
/* 272 */       Rectangle rectangle1 = new Rectangle(insets.left, insets.top, i - insets.left - insets.right, j - insets.top - insets.bottom);
/*     */ 
/*     */ 
/*     */       
/* 276 */       Border border = BorderFactory.createLineBorder(Color.black);
/* 277 */       Insets insets1 = border.getBorderInsets(jPanel);
/* 278 */       rectangle = new Rectangle(insets.left + b3, insets.top + b3, b1, b2);
/*     */ 
/*     */       
/* 281 */       if (paramImage != null) {
/* 282 */         border.paintBorder(jPanel, graphics2D, rectangle.x - insets1.left, rectangle.y - insets1.top, rectangle.width + insets1.left + insets1.right, rectangle.height + insets1.top + insets1.bottom);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 287 */         rectangle1.x += b1 + 2 * b3;
/* 288 */         rectangle1.width -= b1 + 2 * b3;
/*     */       } 
/* 290 */       Font font1 = new Font("SansSerif", 1, 20);
/* 291 */       Font font2 = new Font("SansSerif", 1, 16);
/* 292 */       graphics2D.setColor(Color.black);
/* 293 */       graphics2D.setFont(font1);
/* 294 */       Rectangle rectangle2 = new Rectangle(rectangle1.x, rectangle1.y + 6, rectangle1.width, rectangle1.height - 12);
/*     */       
/* 296 */       rectangle2.height /= 2;
/* 297 */       drawStringInRect(graphics2D, str1, rectangle2, 1);
/* 298 */       graphics2D.setFont(font2);
/* 299 */       rectangle2.y += rectangle2.height;
/* 300 */       drawStringInRect(graphics2D, str2, rectangle2, 1);
/*     */     } 
/*     */     
/* 303 */     if (paramImage != null) {
/* 304 */       byte b = 0;
/*     */       
/* 306 */       while (!graphics2D.drawImage(paramImage, rectangle.x, rectangle.y, rectangle.width, rectangle.height, this._owner)) {
/*     */         
/*     */         try {
/* 309 */           Thread.sleep(2000L);
/* 310 */         } catch (Exception exception) {}
/* 311 */         if (++b > 5) {
/*     */ 
/*     */           
/* 314 */           Trace.println("couldnt draw splash image : " + paramImage, TraceLevel.BASIC);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 322 */       File file = File.createTempFile("splash", ".jpg", this._dir);
/* 323 */       writeImage(file, bufferedImage);
/* 324 */       addSplashToCacheIndex(this._key, file.getCanonicalPath());
/* 325 */     } catch (IOException iOException) {
/* 326 */       splashError(iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawStringInRect(Graphics2D paramGraphics2D, String paramString, Rectangle paramRectangle, int paramInt) {
/*     */     int j;
/* 332 */     FontMetrics fontMetrics = paramGraphics2D.getFontMetrics();
/* 333 */     Rectangle2D rectangle2D = fontMetrics.getStringBounds(paramString, paramGraphics2D);
/* 334 */     int i = fontMetrics.getMaxAscent();
/*     */     
/* 336 */     int m = (int)rectangle2D.getWidth();
/* 337 */     int n = (int)rectangle2D.getHeight();
/*     */     
/* 339 */     if (m > paramRectangle.width)
/* 340 */     { j = paramRectangle.x;
/* 341 */       String str = paramString.substring(0, paramString.length() - 3);
/* 342 */       int i1 = str.length();
/* 343 */       while (i1 > 3 && fontMetrics.stringWidth(str + "...") > paramRectangle.width) {
/* 344 */         i1--;
/* 345 */         str = str.substring(0, i1);
/*     */       } 
/* 347 */       paramString = str + "..."; }
/* 348 */     else { switch (paramInt) {
/*     */         
/*     */         default:
/* 351 */           j = paramRectangle.x;
/*     */           break;
/*     */         case 1:
/* 354 */           j = paramRectangle.x + (paramRectangle.width - m) / 2;
/*     */           break;
/*     */         case 2:
/* 357 */           j = paramRectangle.x + paramRectangle.width - m - 1; break;
/*     */       }  }
/*     */     
/* 360 */     if (j < paramRectangle.x) j = paramRectangle.x;
/*     */     
/* 362 */     int k = paramRectangle.y + i + (paramRectangle.height - n) / 2;
/* 363 */     paramGraphics2D.drawString(paramString, j, k);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addSplashToCacheIndex(String paramString1, String paramString2) {
/* 369 */     if (paramString2 != null) {
/* 370 */       this._props.setProperty(paramString1, paramString2);
/* 371 */     } else if (this._props.containsKey(paramString1)) {
/* 372 */       this._props.remove(paramString1);
/*     */     } 
/*     */ 
/*     */     
/* 376 */     File[] arrayOfFile = this._dir.listFiles();
/*     */ 
/*     */ 
/*     */     
/* 380 */     if (arrayOfFile == null)
/*     */       return; 
/* 382 */     for (byte b = 0; b < arrayOfFile.length; b++) {
/*     */       
/* 384 */       if (!arrayOfFile[b].equals(this._index)) {
/* 385 */         try { String str = arrayOfFile[b].getCanonicalPath();
/* 386 */           if (!this._props.containsValue(str)) {
/* 387 */             arrayOfFile[b].delete();
/*     */           } }
/* 389 */         catch (IOException iOException)
/* 390 */         { splashError(iOException); }
/*     */       
/*     */       }
/*     */     } 
/*     */     try {
/* 395 */       FileOutputStream fileOutputStream = new FileOutputStream(this._index);
/* 396 */       this._props.store(fileOutputStream, "");
/* 397 */       fileOutputStream.flush();
/* 398 */       fileOutputStream.close();
/* 399 */     } catch (IOException iOException) {
/* 400 */       splashError(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeImage(File paramFile, BufferedImage paramBufferedImage) {
/*     */     try {
/* 408 */       FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
/* 409 */       JPEGImageEncoder jPEGImageEncoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
/* 410 */       jPEGImageEncoder.encode(paramBufferedImage);
/* 411 */     } catch (Throwable throwable) {
/* 412 */       splashError(throwable);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void splashError(Throwable paramThrowable) {
/* 417 */     LaunchErrorDialog.show(this._owner, paramThrowable, false);
/* 418 */     throw new Error(paramThrowable.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\SplashGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */