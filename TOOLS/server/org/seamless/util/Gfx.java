/*     */ package org.seamless.util;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.ImageIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Gfx
/*     */ {
/*     */   public static byte[] resizeProportionally(ImageIcon icon, String contentType, int newWidth, int newHeight) throws IOException {
/*  36 */     double widthRatio = (newWidth != icon.getIconWidth()) ? (newWidth / icon.getIconWidth()) : 1.0D;
/*     */ 
/*     */ 
/*     */     
/*  40 */     double heightRatio = (newHeight != icon.getIconHeight()) ? (newHeight / icon.getIconHeight()) : 1.0D;
/*     */ 
/*     */ 
/*     */     
/*  44 */     if (widthRatio < heightRatio) {
/*  45 */       newHeight = (int)(icon.getIconHeight() * widthRatio);
/*     */     } else {
/*  47 */       newWidth = (int)(icon.getIconWidth() * heightRatio);
/*     */     } 
/*     */     
/*  50 */     int imageType = "image/png".equals(contentType) ? 2 : 1;
/*     */ 
/*     */ 
/*     */     
/*  54 */     BufferedImage bImg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), imageType);
/*  55 */     Graphics2D g2d = bImg.createGraphics();
/*  56 */     g2d.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(), null);
/*  57 */     g2d.dispose();
/*     */     
/*  59 */     BufferedImage scaledImg = getScaledInstance(bImg, newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR, false);
/*     */ 
/*     */     
/*  62 */     String formatName = "";
/*  63 */     if ("image/png".equals(contentType)) {
/*  64 */       formatName = "png";
/*  65 */     } else if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType)) {
/*  66 */       formatName = "jpeg";
/*     */     } 
/*  68 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/*  69 */     ImageIO.write(scaledImg, formatName, baos);
/*  70 */     return baos.toByteArray();
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
/*     */   public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
/* 100 */     int w, h, type = (img.getTransparency() == 1) ? 1 : 2;
/*     */     
/* 102 */     BufferedImage ret = img;
/*     */     
/* 104 */     if (higherQuality) {
/*     */ 
/*     */ 
/*     */       
/* 108 */       w = img.getWidth();
/* 109 */       h = img.getHeight();
/*     */     }
/*     */     else {
/*     */       
/* 113 */       w = targetWidth;
/* 114 */       h = targetHeight;
/*     */     } 
/*     */     
/*     */     do {
/* 118 */       if (higherQuality && w > targetWidth) {
/* 119 */         w /= 2;
/* 120 */         if (w < targetWidth) {
/* 121 */           w = targetWidth;
/*     */         }
/*     */       } 
/*     */       
/* 125 */       if (higherQuality && h > targetHeight) {
/* 126 */         h /= 2;
/* 127 */         if (h < targetHeight) {
/* 128 */           h = targetHeight;
/*     */         }
/*     */       } 
/*     */       
/* 132 */       BufferedImage tmp = new BufferedImage(w, h, type);
/* 133 */       Graphics2D g2 = tmp.createGraphics();
/* 134 */       g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
/* 135 */       g2.drawImage(ret, 0, 0, w, h, null);
/* 136 */       g2.dispose();
/*     */       
/* 138 */       ret = tmp;
/* 139 */     } while (w != targetWidth || h != targetHeight);
/*     */     
/* 141 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\Gfx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */