/*    */ package org.flywaydb.core.internal.dbsupport.sybase.ase;
/*    */ 
/*    */ import org.flywaydb.core.internal.dbsupport.Delimiter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SybaseASESqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/*    */   protected Delimiter getDefaultDelimiter() {
/* 34 */     return new Delimiter("GO", true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String computeAlternateCloseQuote(String openQuote) {
/* 39 */     char specialChar = openQuote.charAt(2);
/* 40 */     switch (specialChar) {
/*    */       case '(':
/* 42 */         return ")'";
/*    */     } 
/* 44 */     return specialChar + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sybase\ase\SybaseASESqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */