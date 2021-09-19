/*     */ package com.wurmonline.server.utils;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.WillClose;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DbUtilities
/*     */ {
/*  39 */   private static Logger logger = Logger.getLogger(DbUtilities.class.getName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum DbAdminAction
/*     */   {
/*  68 */     ANALYZE, CHECK_QUICK, CHECK_FAST, CHECK_CHANGED, CHECK_MEDIUM, CHECK_EXTENDED, OPTIMIZE;
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
/*     */   public static void closeDatabaseObjects(@Nullable @WillClose Statement aStatementToClose, @Nullable @WillClose ResultSet aResultSetToClose) {
/*  90 */     if (aResultSetToClose != null) {
/*     */ 
/*     */       
/*     */       try {
/*  94 */         aResultSetToClose.close();
/*     */       }
/*  96 */       catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/* 100 */       aResultSetToClose = null;
/*     */     } 
/* 102 */     if (aStatementToClose != null) {
/*     */ 
/*     */       
/*     */       try {
/* 106 */         aStatementToClose.close();
/*     */       }
/* 108 */       catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/* 112 */       aStatementToClose = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void performAdminOnAllTables(Connection aConnection, DbAdminAction aAction) {
/* 130 */     logger.info("Performing " + aAction + " on all Wurm database tables");
/*     */     
/* 132 */     long start = System.nanoTime();
/*     */     
/* 134 */     ResultSet rsCatalogs = null;
/* 135 */     ResultSet rsTables = null;
/* 136 */     ResultSet rsOperationStatus = null;
/* 137 */     Statement lStmt = null;
/*     */     
/* 139 */     boolean problemsEncountered = false;
/*     */ 
/*     */     
/*     */     try {
/* 143 */       DatabaseMetaData dbmd = aConnection.getMetaData();
/* 144 */       rsCatalogs = dbmd.getCatalogs();
/* 145 */       while (rsCatalogs.next()) {
/*     */         
/* 147 */         String lCatalogName = rsCatalogs.getString("TABLE_CAT");
/* 148 */         boolean proceed = true;
/* 149 */         if (aAction == DbAdminAction.CHECK_MEDIUM)
/*     */         {
/* 151 */           if (lCatalogName.toUpperCase().startsWith("WURMLOGS")) {
/* 152 */             proceed = Constants.checkWurmLogs;
/*     */           }
/*     */         }
/*     */         
/* 156 */         if (lCatalogName.toUpperCase().startsWith("WURM") && proceed) {
/*     */           
/* 158 */           logger.info("Performing " + aAction + " on CatalogName: " + lCatalogName);
/* 159 */           lStmt = aConnection.createStatement();
/* 160 */           rsTables = dbmd.getTables(lCatalogName, null, null, null);
/* 161 */           while (rsTables.next()) {
/*     */             
/* 163 */             String lTableName = rsTables.getString("TABLE_NAME");
/* 164 */             String lAdminQuery = null;
/* 165 */             switch (aAction) {
/*     */               
/*     */               case ANALYZE:
/* 168 */                 lAdminQuery = "ANALYZE LOCAL TABLE " + lCatalogName + '.' + lTableName;
/*     */                 break;
/*     */               case CHECK_CHANGED:
/* 171 */                 lAdminQuery = "CHECK TABLE " + lCatalogName + '.' + lTableName + " CHANGED";
/*     */                 break;
/*     */               case CHECK_EXTENDED:
/* 174 */                 lAdminQuery = "CHECK TABLE " + lCatalogName + '.' + lTableName + " EXTENDED";
/*     */                 break;
/*     */               case CHECK_FAST:
/* 177 */                 lAdminQuery = "CHECK TABLE " + lCatalogName + '.' + lTableName + " FAST";
/*     */                 break;
/*     */               case CHECK_MEDIUM:
/* 180 */                 lAdminQuery = "CHECK TABLE " + lCatalogName + '.' + lTableName + " MEDIUM";
/*     */                 break;
/*     */               case CHECK_QUICK:
/* 183 */                 lAdminQuery = "CHECK TABLE " + lCatalogName + '.' + lTableName + " QUICK";
/*     */                 break;
/*     */               case OPTIMIZE:
/* 186 */                 lAdminQuery = "OPTIMIZE LOCAL TABLE " + lCatalogName + '.' + lTableName;
/*     */                 break;
/*     */             } 
/* 189 */             lStmt = aConnection.createStatement();
/* 190 */             if (lStmt.execute(lAdminQuery)) {
/*     */ 
/*     */               
/* 193 */               rsOperationStatus = lStmt.getResultSet();
/* 194 */               if (rsOperationStatus.next()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 200 */                 String lMsgType = rsOperationStatus.getString("Msg_type");
/* 201 */                 String lMsgText = rsOperationStatus.getString("Msg_text");
/* 202 */                 if ("OK".equals(lMsgText) && "status".equals(lMsgType)) {
/*     */ 
/*     */                   
/* 205 */                   if (logger.isLoggable(Level.FINE))
/*     */                   {
/* 207 */                     logger.fine("TableName: " + lAdminQuery + " - OK");
/*     */                   }
/*     */                 }
/*     */                 else {
/*     */                   
/* 212 */                   logger.warning("TableName: " + lAdminQuery + " - " + lMsgType + ": " + lMsgText);
/* 213 */                   if (!"status".equals(lMsgType) || lMsgText == null || !lMsgText.contains("is not BASE TABLE"));
/*     */                 } 
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 219 */               rsOperationStatus.close();
/*     */ 
/*     */ 
/*     */             
/*     */             }
/* 224 */             else if (logger.isLoggable(Level.FINE)) {
/*     */               
/* 226 */               logger.fine("TableName: " + lAdminQuery);
/*     */             } 
/*     */             
/* 229 */             lStmt.close();
/*     */           } 
/* 231 */           rsTables.close();
/*     */           
/*     */           continue;
/*     */         } 
/* 235 */         if (logger.isLoggable(Level.FINE))
/*     */         {
/* 237 */           logger.fine("Not performing " + aAction + " on non-Wurm CatalogName: " + lCatalogName);
/*     */         }
/*     */       } 
/*     */       
/* 241 */       rsCatalogs.close();
/*     */     }
/* 243 */     catch (SQLException e) {
/*     */       
/* 245 */       logger.log(Level.WARNING, e.getMessage(), e);
/*     */     }
/*     */     finally {
/*     */       
/* 249 */       closeDatabaseObjects(lStmt, rsCatalogs);
/* 250 */       closeDatabaseObjects(null, rsTables);
/* 251 */       closeDatabaseObjects(null, rsOperationStatus);
/*     */       
/* 253 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 254 */       logger.info("Finished performing " + aAction + " on all database tables, which took " + lElapsedTime + " millis.");
/*     */ 
/*     */       
/* 257 */       if (problemsEncountered)
/*     */       {
/* 259 */         logger.severe("\n\n**** At least one problem was encountered while performing admin actions ***********\n\n");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timestamp getTimestampOrNull(String timestampString) {
/* 269 */     if (timestampString.contains(":")) {
/*     */       
/*     */       try {
/*     */         
/* 273 */         return new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(timestampString).getTime());
/*     */       }
/* 275 */       catch (ParseException e) {
/*     */         
/* 277 */         logger.warning("Unable to convert '" + timestampString + "' into a timestamp, expected format: yyyy-MM-dd HH:mm:ss");
/* 278 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 285 */       return new Timestamp(Long.parseLong(timestampString));
/*     */     }
/* 287 */     catch (NumberFormatException e) {
/*     */       
/* 289 */       logger.warning("Unable to convert '" + timestampString + "' into a timestamp, value is not valid for type 'long'");
/* 290 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\DbUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */