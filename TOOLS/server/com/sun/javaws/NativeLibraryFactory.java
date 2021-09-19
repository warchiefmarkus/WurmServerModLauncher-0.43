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
/*    */ public class NativeLibraryFactory
/*    */ {
/*    */   public static NativeLibrary newInstance() {
/* 17 */     return new WinNativeLibrary();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\NativeLibraryFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */