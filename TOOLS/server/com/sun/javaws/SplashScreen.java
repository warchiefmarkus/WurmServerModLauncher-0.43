/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import java.awt.Frame;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
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
/*     */ public class SplashScreen
/*     */ {
/*     */   private static boolean _alreadyHidden = false;
/*     */   private static final int HIDE_SPASH_SCREEN_TOKEN = 90;
/*     */   
/*     */   public static void hide() {
/*  50 */     hide(JnlpxArgs.getSplashPort());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void hide(int paramInt) {
/*  58 */     if (paramInt <= 0 || _alreadyHidden)
/*     */       return; 
/*  60 */     _alreadyHidden = true;
/*     */     
/*  62 */     Socket socket = null;
/*     */     try {
/*  64 */       socket = new Socket("127.0.0.1", paramInt);
/*  65 */       if (socket != null) {
/*  66 */         OutputStream outputStream = socket.getOutputStream();
/*     */         try {
/*  68 */           outputStream.write(90);
/*  69 */           outputStream.flush();
/*  70 */         } catch (IOException iOException) {}
/*     */ 
/*     */         
/*  73 */         outputStream.close();
/*     */       }
/*     */     
/*     */     }
/*  77 */     catch (IOException iOException) {
/*     */ 
/*     */     
/*  80 */     } catch (Exception exception) {
/*     */       
/*  82 */       Trace.ignoredException(exception);
/*     */     } 
/*  84 */     if (socket != null) {
/*     */       try {
/*  86 */         socket.close();
/*  87 */       } catch (IOException iOException) {
/*     */         
/*  89 */         Trace.println("exception closing socket: " + iOException, TraceLevel.BASIC);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generateCustomSplash(Frame paramFrame, LaunchDesc paramLaunchDesc, boolean paramBoolean) {
/*  99 */     SplashGenerator splashGenerator = new SplashGenerator(paramFrame, paramLaunchDesc);
/* 100 */     if (paramBoolean || splashGenerator.needsCustomSplash()) {
/* 101 */       splashGenerator.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void removeCustomSplash(LaunchDesc paramLaunchDesc) {
/* 106 */     SplashGenerator splashGenerator = new SplashGenerator(null, paramLaunchDesc);
/* 107 */     splashGenerator.remove();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\SplashScreen.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */