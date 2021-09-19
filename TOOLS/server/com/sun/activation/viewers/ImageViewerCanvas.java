/*    */ package com.sun.activation.viewers;
/*    */ 
/*    */ import java.awt.Canvas;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ImageViewerCanvas
/*    */   extends Canvas
/*    */ {
/* 47 */   private Image canvas_image = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setImage(Image new_image) {
/* 62 */     this.canvas_image = new_image;
/* 63 */     invalidate();
/* 64 */     repaint();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Dimension getPreferredSize() {
/* 72 */     Dimension d = null;
/*    */     
/* 74 */     if (this.canvas_image == null) {
/*    */       
/* 76 */       d = new Dimension(200, 200);
/*    */     } else {
/*    */       
/* 79 */       d = new Dimension(this.canvas_image.getWidth(this), this.canvas_image.getHeight(this));
/*    */     } 
/*    */     
/* 82 */     return d;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void paint(Graphics g) {
/* 90 */     if (this.canvas_image != null)
/* 91 */       g.drawImage(this.canvas_image, 0, 0, this); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\viewers\ImageViewerCanvas.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */