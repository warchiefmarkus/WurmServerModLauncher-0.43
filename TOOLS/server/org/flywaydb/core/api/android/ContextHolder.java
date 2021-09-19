/*    */ package org.flywaydb.core.api.android;
/*    */ 
/*    */ import android.content.Context;
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
/*    */ public class ContextHolder
/*    */ {
/*    */   private static Context context;
/*    */   
/*    */   public static Context getContext() {
/* 39 */     return context;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setContext(Context context) {
/* 46 */     ContextHolder.context = context;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\android\ContextHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */