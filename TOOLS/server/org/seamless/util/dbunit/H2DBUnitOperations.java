/*    */ package org.seamless.util.dbunit;
/*    */ 
/*    */ import org.dbunit.database.DatabaseConfig;
/*    */ import org.dbunit.database.IDatabaseConnection;
/*    */ import org.dbunit.dataset.datatype.DataType;
/*    */ import org.dbunit.dataset.datatype.DataTypeException;
/*    */ import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
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
/*    */ public abstract class H2DBUnitOperations
/*    */   extends DBUnitOperations
/*    */ {
/*    */   protected void disableReferentialIntegrity(IDatabaseConnection con) {
/*    */     try {
/* 33 */       con.getConnection().prepareStatement("set referential_integrity FALSE").execute();
/* 34 */     } catch (Exception ex) {
/* 35 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void enableReferentialIntegrity(IDatabaseConnection con) {
/*    */     try {
/* 42 */       con.getConnection().prepareStatement("set referential_integrity TRUE").execute();
/* 43 */     } catch (Exception ex) {
/* 44 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void editConfig(DatabaseConfig config) {
/* 50 */     super.editConfig(config);
/*    */ 
/*    */ 
/*    */     
/* 54 */     config.setProperty("http://www.dbunit.org/properties/datatypeFactory", new DefaultDataTypeFactory()
/*    */         {
/*    */           public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException
/*    */           {
/* 58 */             if (sqlType == 16) {
/* 59 */               return DataType.BOOLEAN;
/*    */             }
/* 61 */             return super.createDataType(sqlType, sqlTypeName);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\dbunit\H2DBUnitOperations.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */