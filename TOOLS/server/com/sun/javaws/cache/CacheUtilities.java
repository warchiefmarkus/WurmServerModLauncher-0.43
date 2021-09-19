/*    */ package com.sun.javaws.cache;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Image;
/*    */ import java.awt.MediaTracker;
/*    */ import java.awt.Toolkit;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
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
/*    */ public class CacheUtilities
/*    */ {
/* 23 */   private static CacheUtilities _instance = null;
/*    */ 
/*    */   
/*    */   private Component _component;
/*    */ 
/*    */ 
/*    */   
/*    */   public static CacheUtilities getSharedInstance() {
/* 31 */     if (_instance == null) {
/* 32 */       synchronized (CacheUtilities.class) {
/* 33 */         if (_instance == null) {
/* 34 */           _instance = new CacheUtilities();
/*    */         }
/*    */       } 
/*    */     }
/* 38 */     return _instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Image loadImage(String paramString) throws IOException {
/* 48 */     Image image = Toolkit.getDefaultToolkit().createImage(paramString);
/* 49 */     if (image != null) {
/* 50 */       Component component = getComponent();
/* 51 */       MediaTracker mediaTracker = new MediaTracker(component);
/* 52 */       mediaTracker.addImage(image, 0);
/*    */       
/*    */       try {
/* 55 */         mediaTracker.waitForID(0, 5000L);
/* 56 */       } catch (InterruptedException interruptedException) {
/* 57 */         throw new IOException("Failed to load");
/*    */       } 
/* 59 */       return image;
/*    */     } 
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Image loadImage(URL paramURL) throws IOException {
/* 68 */     Image image = Toolkit.getDefaultToolkit().createImage(paramURL);
/* 69 */     if (image != null) {
/* 70 */       Component component = getComponent();
/* 71 */       MediaTracker mediaTracker = new MediaTracker(component);
/* 72 */       mediaTracker.addImage(image, 0);
/*    */       
/*    */       try {
/* 75 */         mediaTracker.waitForID(0, 5000L);
/* 76 */       } catch (InterruptedException interruptedException) {
/* 77 */         throw new IOException("Failed to load");
/*    */       } 
/* 79 */       return image;
/*    */     } 
/* 81 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Component getComponent() {
/* 88 */     if (this._component == null)
/* 89 */       synchronized (this) {
/* 90 */         if (this._component == null)
/* 91 */           this._component = new Component(this) {
/*    */               private final CacheUtilities this$0;
/*    */             }; 
/*    */       }  
/* 95 */     return this._component;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\CacheUtilities.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */