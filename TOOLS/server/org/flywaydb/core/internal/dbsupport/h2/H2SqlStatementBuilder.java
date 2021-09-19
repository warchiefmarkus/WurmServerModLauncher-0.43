/*    */ package org.flywaydb.core.internal.dbsupport.h2;
/*    */ 
/*    */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
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
/*    */ public class H2SqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/*    */   protected String extractAlternateOpenQuote(String token) {
/* 26 */     if (token.startsWith("$$")) {
/* 27 */       return "$$";
/*    */     }
/* 29 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\h2\H2SqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */