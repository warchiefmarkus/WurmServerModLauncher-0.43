/*    */ package org.flywaydb.core.internal.util.logging.android;
/*    */ 
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogCreator;
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
/*    */ public class AndroidLogCreator
/*    */   implements LogCreator
/*    */ {
/*    */   public Log createLogger(Class<?> clazz) {
/* 26 */     return new AndroidLog();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\android\AndroidLogCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */