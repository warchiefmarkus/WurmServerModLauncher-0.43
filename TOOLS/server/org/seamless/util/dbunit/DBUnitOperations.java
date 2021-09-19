/*     */ package org.seamless.util.dbunit;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.sql.Connection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ import org.dbunit.database.DatabaseConfig;
/*     */ import org.dbunit.database.DatabaseConnection;
/*     */ import org.dbunit.database.IDatabaseConnection;
/*     */ import org.dbunit.dataset.IDataSet;
/*     */ import org.dbunit.dataset.ReplacementDataSet;
/*     */ import org.dbunit.dataset.xml.FlatXmlDataSet;
/*     */ import org.dbunit.operation.DatabaseOperation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DBUnitOperations
/*     */   extends ArrayList<DBUnitOperations.Op>
/*     */ {
/*  39 */   private static final Logger log = Logger.getLogger(DBUnitOperations.class.getName());
/*     */ 
/*     */   
/*     */   public abstract DataSource getDataSource();
/*     */   
/*     */   public static abstract class Op
/*     */   {
/*     */     ReplacementDataSet dataSet;
/*     */     DatabaseOperation operation;
/*     */     
/*     */     public Op(String dataLocation) {
/*  50 */       this(dataLocation, null, DatabaseOperation.CLEAN_INSERT);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Op(String dataLocation, String dtdLocation) {
/*  57 */       this(dataLocation, dtdLocation, DatabaseOperation.CLEAN_INSERT);
/*     */     }
/*     */ 
/*     */     
/*     */     public Op(String dataLocation, String dtdLocation, DatabaseOperation operation) {
/*     */       try {
/*  63 */         this.dataSet = (dtdLocation != null) ? new ReplacementDataSet((IDataSet)new FlatXmlDataSet(openStream(dataLocation), openStream(dtdLocation))) : new ReplacementDataSet((IDataSet)new FlatXmlDataSet(openStream(dataLocation)));
/*     */       
/*     */       }
/*  66 */       catch (Exception ex) {
/*  67 */         throw new RuntimeException(ex);
/*     */       } 
/*  69 */       this.dataSet.addReplacementObject("[NULL]", null);
/*  70 */       this.operation = operation;
/*     */     }
/*     */     
/*     */     public IDataSet getDataSet() {
/*  74 */       return (IDataSet)this.dataSet;
/*     */     }
/*     */     
/*     */     public DatabaseOperation getOperation() {
/*  78 */       return this.operation;
/*     */     }
/*     */     
/*     */     public void execute(IDatabaseConnection connection) {
/*     */       try {
/*  83 */         this.operation.execute(connection, (IDataSet)this.dataSet);
/*  84 */       } catch (Exception ex) {
/*  85 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected abstract InputStream openStream(String param1String);
/*     */   }
/*     */   
/*     */   public static class ClasspathOp
/*     */     extends Op {
/*     */     public ClasspathOp(String dataLocation) {
/*  95 */       super(dataLocation);
/*     */     }
/*     */     
/*     */     public ClasspathOp(String dataLocation, String dtdLocation) {
/*  99 */       super(dataLocation, dtdLocation);
/*     */     }
/*     */     
/*     */     public ClasspathOp(String dataLocation, String dtdLocation, DatabaseOperation operation) {
/* 103 */       super(dataLocation, dtdLocation, operation);
/*     */     }
/*     */ 
/*     */     
/*     */     protected InputStream openStream(String location) {
/* 108 */       return Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
/*     */     }
/*     */   }
/*     */   
/*     */   public class FileOp
/*     */     extends Op {
/*     */     public FileOp(String dataLocation) {
/* 115 */       super(dataLocation);
/*     */     }
/*     */     
/*     */     public FileOp(String dataLocation, String dtdLocation) {
/* 119 */       super(dataLocation, dtdLocation);
/*     */     }
/*     */     
/*     */     public FileOp(String dataLocation, String dtdLocation, DatabaseOperation operation) {
/* 123 */       super(dataLocation, dtdLocation, operation);
/*     */     }
/*     */ 
/*     */     
/*     */     protected InputStream openStream(String location) {
/*     */       try {
/* 129 */         return new FileInputStream(location);
/* 130 */       } catch (FileNotFoundException ex) {
/* 131 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 142 */     log.info("Executing DBUnit operations: " + size());
/* 143 */     IDatabaseConnection con = null;
/*     */     try {
/* 145 */       con = getConnection();
/* 146 */       disableReferentialIntegrity(con);
/* 147 */       for (Op op : this) {
/* 148 */         op.execute(con);
/*     */       }
/* 150 */       enableReferentialIntegrity(con);
/*     */     } finally {
/* 152 */       if (con != null) {
/*     */         try {
/* 154 */           con.close();
/* 155 */         } catch (Exception ex) {
/* 156 */           log.log(Level.WARNING, "Failed to close connection after DBUnit operation: " + ex, ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IDatabaseConnection getConnection() {
/*     */     try {
/* 172 */       DataSource datasource = getDataSource();
/* 173 */       Connection con = datasource.getConnection();
/* 174 */       DatabaseConnection databaseConnection = new DatabaseConnection(con);
/* 175 */       editConfig(databaseConnection.getConfig());
/* 176 */       return (IDatabaseConnection)databaseConnection;
/* 177 */     } catch (Exception ex) {
/* 178 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void disableReferentialIntegrity(IDatabaseConnection paramIDatabaseConnection);
/*     */   
/*     */   protected abstract void enableReferentialIntegrity(IDatabaseConnection paramIDatabaseConnection);
/*     */   
/*     */   protected void editConfig(DatabaseConfig config) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\dbunit\DBUnitOperations.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */