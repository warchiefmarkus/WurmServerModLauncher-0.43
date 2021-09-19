/*    */ package org.flywaydb.core.internal.util.logging.javautil;
/*    */ 
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ public class JavaUtilLogCreator
/*    */   implements LogCreator
/*    */ {
/*    */   public Log createLogger(Class<?> clazz) {
/* 28 */     return new JavaUtilLog(Logger.getLogger(clazz.getName()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\javautil\JavaUtilLogCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */