/*    */ package org.flywaydb.core.internal.util.logging.android;
/*    */ 
/*    */ import android.util.Log;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
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
/*    */ public class AndroidLog
/*    */   implements Log
/*    */ {
/*    */   private static final String TAG = "Flyway";
/*    */   
/*    */   public void debug(String message) {
/* 31 */     Log.d("Flyway", message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void info(String message) {
/* 36 */     Log.i("Flyway", message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void warn(String message) {
/* 41 */     Log.w("Flyway", message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void error(String message) {
/* 46 */     Log.e("Flyway", message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void error(String message, Exception e) {
/* 51 */     Log.e("Flyway", message, e);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\android\AndroidLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */