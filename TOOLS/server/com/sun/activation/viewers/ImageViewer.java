/*     */ package com.sun.activation.viewers;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.Panel;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.activation.CommandObject;
/*     */ import javax.activation.DataHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImageViewer
/*     */   extends Panel
/*     */   implements CommandObject
/*     */ {
/*  50 */   private ImageViewerCanvas canvas = null;
/*     */ 
/*     */ 
/*     */   
/*  54 */   private Image image = null;
/*  55 */   private DataHandler _dh = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean DEBUG = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public ImageViewer() {
/*  64 */     this.canvas = new ImageViewerCanvas();
/*  65 */     add(this.canvas);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommandContext(String verb, DataHandler dh) throws IOException {
/*  72 */     this._dh = dh;
/*  73 */     setInputStream(this._dh.getInputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setInputStream(InputStream ins) throws IOException {
/*  82 */     MediaTracker mt = new MediaTracker(this);
/*  83 */     int bytes_read = 0;
/*  84 */     byte[] data = new byte[1024];
/*  85 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     
/*  87 */     while ((bytes_read = ins.read(data)) > 0)
/*  88 */       baos.write(data, 0, bytes_read); 
/*  89 */     ins.close();
/*     */ 
/*     */     
/*  92 */     this.image = getToolkit().createImage(baos.toByteArray());
/*     */     
/*  94 */     mt.addImage(this.image, 0);
/*     */     
/*     */     try {
/*  97 */       mt.waitForID(0);
/*  98 */       mt.waitForAll();
/*  99 */       if (mt.statusID(0, true) != 8) {
/* 100 */         System.out.println("Error occured in image loading = " + mt.getErrorsID(0));
/*     */ 
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 106 */     catch (InterruptedException e) {
/* 107 */       throw new IOException("Error reading image data");
/*     */     } 
/*     */     
/* 110 */     this.canvas.setImage(this.image);
/* 111 */     if (this.DEBUG) {
/* 112 */       System.out.println("calling invalidate");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addNotify() {
/* 117 */     super.addNotify();
/* 118 */     invalidate();
/* 119 */     validate();
/* 120 */     doLayout();
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 124 */     return this.canvas.getPreferredSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\viewers\ImageViewer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */