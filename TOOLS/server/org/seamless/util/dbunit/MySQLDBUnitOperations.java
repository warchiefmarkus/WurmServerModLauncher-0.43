/*    */ package org.seamless.util.dbunit;
/*    */ 
/*    */ import org.dbunit.database.IDatabaseConnection;
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
/*    */ public abstract class MySQLDBUnitOperations
/*    */   extends DBUnitOperations
/*    */ {
/*    */   protected void disableReferentialIntegrity(IDatabaseConnection con) {
/*    */     try {
/* 27 */       con.getConnection().prepareStatement("set foreign_key_checks=0").execute();
/* 28 */     } catch (Exception ex) {
/* 29 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void enableReferentialIntegrity(IDatabaseConnection con) {
/*    */     try {
/* 36 */       con.getConnection().prepareStatement("set foreign_key_checks=1").execute();
/* 37 */     } catch (Exception ex) {
/* 38 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\dbunit\MySQLDBUnitOperations.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */