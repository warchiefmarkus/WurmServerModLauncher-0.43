/*    */ package org.flywaydb.core.internal.util.logging;
/*    */ 
/*    */ import org.flywaydb.core.internal.util.FeatureDetector;
/*    */ import org.flywaydb.core.internal.util.logging.android.AndroidLogCreator;
/*    */ import org.flywaydb.core.internal.util.logging.apachecommons.ApacheCommonsLogCreator;
/*    */ import org.flywaydb.core.internal.util.logging.javautil.JavaUtilLogCreator;
/*    */ import org.flywaydb.core.internal.util.logging.slf4j.Slf4jLogCreator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogFactory
/*    */ {
/*    */   private static LogCreator logCreator;
/*    */   private static LogCreator fallbackLogCreator;
/*    */   
/*    */   public static void setLogCreator(LogCreator logCreator) {
/* 49 */     LogFactory.logCreator = logCreator;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setFallbackLogCreator(LogCreator fallbackLogCreator) {
/* 57 */     LogFactory.fallbackLogCreator = fallbackLogCreator;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Log getLog(Class<?> clazz) {
/* 67 */     if (logCreator == null) {
/* 68 */       FeatureDetector featureDetector = new FeatureDetector(Thread.currentThread().getContextClassLoader());
/* 69 */       if (featureDetector.isAndroidAvailable()) {
/* 70 */         logCreator = (LogCreator)new AndroidLogCreator();
/* 71 */       } else if (featureDetector.isSlf4jAvailable()) {
/* 72 */         logCreator = (LogCreator)new Slf4jLogCreator();
/* 73 */       } else if (featureDetector.isApacheCommonsLoggingAvailable()) {
/* 74 */         logCreator = (LogCreator)new ApacheCommonsLogCreator();
/* 75 */       } else if (fallbackLogCreator == null) {
/* 76 */         logCreator = (LogCreator)new JavaUtilLogCreator();
/*    */       } else {
/* 78 */         logCreator = fallbackLogCreator;
/*    */       } 
/*    */     } 
/*    */     
/* 82 */     return logCreator.createLogger(clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\LogFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */