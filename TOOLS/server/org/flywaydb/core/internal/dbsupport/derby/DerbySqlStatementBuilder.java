/*    */ package org.flywaydb.core.internal.dbsupport.derby;
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
/*    */ public class DerbySqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/*    */   protected String extractAlternateOpenQuote(String token) {
/* 26 */     if (token.startsWith("$$")) {
/* 27 */       return "$$";
/*    */     }
/* 29 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String cleanToken(String token) {
/* 34 */     if (token.startsWith("X'")) {
/* 35 */       return token.substring(token.indexOf("'"));
/*    */     }
/* 37 */     return super.cleanToken(token);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\derby\DerbySqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */