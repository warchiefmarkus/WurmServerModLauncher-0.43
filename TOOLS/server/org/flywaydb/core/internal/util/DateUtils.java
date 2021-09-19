/*    */ package org.flywaydb.core.internal.util;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
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
/*    */ public class DateUtils
/*    */ {
/*    */   public static String formatDateAsIsoString(Date date) {
/* 39 */     if (date == null) {
/* 40 */       return "";
/*    */     }
/* 42 */     return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\DateUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */