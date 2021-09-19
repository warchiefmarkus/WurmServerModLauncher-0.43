/*    */ package org.flywaydb.core.internal.util;
/*    */ 
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*    */ import org.flywaydb.core.internal.util.scanner.classpath.ClassPathResource;
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
/*    */ public class VersionPrinter
/*    */ {
/* 26 */   private static final Log LOG = LogFactory.getLog(VersionPrinter.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean printed;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void printVersion() {
/* 40 */     if (printed) {
/*    */       return;
/*    */     }
/* 43 */     printed = true;
/* 44 */     String version = (new ClassPathResource("org/flywaydb/core/internal/version.txt", VersionPrinter.class.getClassLoader())).loadAsString("UTF-8");
/* 45 */     LOG.info("Flyway " + version + " by Boxfuse");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\VersionPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */