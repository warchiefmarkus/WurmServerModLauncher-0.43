/*    */ package org.flywaydb.core.internal.dbsupport;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.internal.util.StringUtils;
/*    */ import org.flywaydb.core.internal.util.scanner.Resource;
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
/*    */ public class FlywaySqlScriptException
/*    */   extends FlywayException
/*    */ {
/*    */   private final Resource resource;
/*    */   private final SqlStatement statement;
/*    */   
/*    */   public FlywaySqlScriptException(Resource resource, SqlStatement statement, SQLException sqlException) {
/* 40 */     super(sqlException);
/* 41 */     this.resource = resource;
/* 42 */     this.statement = statement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLineNumber() {
/* 51 */     return this.statement.getLineNumber();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getStatement() {
/* 60 */     return this.statement.getSql();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 65 */     String title = (this.resource == null) ? "Script failed" : ("Migration " + this.resource.getFilename() + " failed");
/* 66 */     String underline = StringUtils.trimOrPad("", title.length(), '-');
/*    */     
/* 68 */     SQLException cause = (SQLException)getCause();
/* 69 */     while (cause.getNextException() != null) {
/* 70 */       cause = cause.getNextException();
/*    */     }
/*    */     
/* 73 */     String message = "\n" + title + "\n" + underline + "\n";
/* 74 */     message = message + "SQL State  : " + cause.getSQLState() + "\n";
/* 75 */     message = message + "Error Code : " + cause.getErrorCode() + "\n";
/* 76 */     if (cause.getMessage() != null) {
/* 77 */       message = message + "Message    : " + cause.getMessage().trim() + "\n";
/*    */     }
/* 79 */     if (this.resource != null) {
/* 80 */       message = message + "Location   : " + this.resource.getLocation() + " (" + this.resource.getLocationOnDisk() + ")\n";
/*    */     }
/* 82 */     message = message + "Line       : " + getLineNumber() + "\n";
/* 83 */     message = message + "Statement  : " + getStatement() + "\n";
/*    */     
/* 85 */     return message;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\FlywaySqlScriptException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */