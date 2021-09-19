/*    */ package com.sun.javaws;
/*    */ 
/*    */ import com.sun.deploy.config.Config;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WinNativeLibrary
/*    */   extends NativeLibrary
/*    */ {
/*    */   private static boolean isLoaded = false;
/*    */   
/*    */   public synchronized void load() {
/* 20 */     if (!isLoaded) {
/* 21 */       String str = Config.getJavaHome() + File.separator + "bin" + File.separator + "deploy.dll";
/*    */       
/* 23 */       System.load(str);
/* 24 */       isLoaded = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\WinNativeLibrary.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */