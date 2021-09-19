/*    */ package com.sun.javaws;
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
/*    */ public abstract class NativeLibrary
/*    */ {
/*    */   private static NativeLibrary nativeLibrary;
/*    */   
/*    */   public static synchronized NativeLibrary getInstance() {
/* 26 */     if (nativeLibrary == null) {
/* 27 */       nativeLibrary = NativeLibraryFactory.newInstance();
/*    */     }
/* 29 */     return nativeLibrary;
/*    */   }
/*    */   
/*    */   public void load() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\NativeLibrary.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */