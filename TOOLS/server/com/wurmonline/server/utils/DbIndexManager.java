/*     */ package com.wurmonline.server.utils;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.database.WurmDatabaseSchema;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DbIndexManager
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(DbIndexManager.class.getName());
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
/*     */   private static void createIndex(WurmDatabaseSchema aSchema, String aIndexCreationQuery) {
/*  72 */     if (aIndexCreationQuery != null && aIndexCreationQuery.startsWith("ALTER TABLE")) {
/*     */       
/*  74 */       long start = System.nanoTime();
/*  75 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/*  77 */         logger.fine("Going to create an index in schema: " + aSchema + " using: " + aIndexCreationQuery);
/*     */       }
/*  79 */       Connection lDbConnection = null;
/*  80 */       Statement lCreateIndexStatement = null;
/*     */       
/*     */       try {
/*  83 */         lDbConnection = DbConnector.getConnectionForSchema(aSchema);
/*     */ 
/*     */         
/*  86 */         lCreateIndexStatement = lDbConnection.createStatement();
/*  87 */         lCreateIndexStatement.execute(aIndexCreationQuery);
/*     */       }
/*  89 */       catch (SQLException sqx) {
/*     */         
/*  91 */         logger.log(Level.WARNING, "Problems creating an index in schema: " + aSchema + " using: " + aIndexCreationQuery + " due to " + sqx
/*  92 */             .getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/*  96 */         DbUtilities.closeDatabaseObjects(lCreateIndexStatement, null);
/*  97 */         DbConnector.returnConnection(lDbConnection);
/*  98 */         if (logger.isLoggable(Level.FINE))
/*     */         {
/* 100 */           float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 101 */           logger.fine("Creating an index in schema: " + aSchema + " using: " + aIndexCreationQuery + " took " + lElapsedTime + " millis.");
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 108 */       logger.warning("SQL query must start with ALTER TABLE. Schema: " + aSchema + ", SQL: " + aIndexCreationQuery);
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
/*     */   private static void removeIndex(WurmDatabaseSchema aSchema, String aIndexCreationQuery) {
/* 126 */     if (aIndexCreationQuery != null && aIndexCreationQuery.startsWith("ALTER TABLE")) {
/*     */       
/* 128 */       long start = System.nanoTime();
/* 129 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 131 */         logger.fine("Going to drop an index in schema: " + aSchema + " using: " + aIndexCreationQuery);
/*     */       }
/* 133 */       Connection lDbConnection = null;
/* 134 */       Statement lCreateIndexStatement = null;
/*     */       
/*     */       try {
/* 137 */         lDbConnection = DbConnector.getConnectionForSchema(aSchema);
/*     */ 
/*     */         
/* 140 */         lCreateIndexStatement = lDbConnection.createStatement();
/* 141 */         lCreateIndexStatement.execute(aIndexCreationQuery);
/*     */       }
/* 143 */       catch (SQLException sqx) {
/*     */         
/* 145 */         logger.log(Level.WARNING, "Problems dropping an index in schema: " + aSchema + " using: " + aIndexCreationQuery + " due to " + sqx
/* 146 */             .getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 150 */         DbUtilities.closeDatabaseObjects(lCreateIndexStatement, null);
/* 151 */         DbConnector.returnConnection(lDbConnection);
/* 152 */         if (logger.isLoggable(Level.FINE))
/*     */         {
/* 154 */           float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 155 */           logger.fine("Dropping an index in schema: " + aSchema + " using: " + aIndexCreationQuery + " took " + lElapsedTime + " millis.");
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 162 */       logger.warning("SQL query must start with ALTER TABLE. Schema: " + aSchema + ", SQL: " + aIndexCreationQuery);
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
/*     */   public static void createIndexes() {
/* 174 */     if (DbConnector.isUseSqlite())
/*     */       return; 
/* 176 */     logger.info("Starting to create database indices");
/* 177 */     long start = System.nanoTime();
/*     */     
/* 179 */     if (Constants.checkAllDbTables) {
/*     */       
/* 181 */       logger.info("The database tables have already been checked so no need to repair them before creating indices.");
/*     */     }
/*     */     else {
/*     */       
/* 185 */       repairDatabaseTables();
/*     */     } 
/*     */     
/* 188 */     createIndex(WurmDatabaseSchema.CREATURES, "ALTER TABLE SKILLS ADD INDEX OWNERID (OWNER)");
/*     */     
/* 190 */     createIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE BODYPARTS ADD INDEX BODYZONEID (ZONEID)");
/*     */     
/* 192 */     createIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE COINS ADD INDEX COINSZONEID (ZONEID)");
/*     */     
/* 194 */     createIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE EFFECTS ADD INDEX OWNERID (OWNER)");
/*     */ 
/*     */     
/* 197 */     createIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE FROZENITEMS ADD INDEX FROZENITEMS_TEMPLATEID (TEMPLATEID)");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     createIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE ITEMS ADD INDEX ITEMS_TEMPLATEID (TEMPLATEID)");
/*     */     
/* 205 */     createIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE ITEMS ADD INDEX ITEMZONEID (ZONEID)");
/*     */     
/* 207 */     createIndex(WurmDatabaseSchema.ZONES, "ALTER TABLE FENCES ADD INDEX FENCEZONEID (ZONEID)");
/*     */     
/* 209 */     createIndex(WurmDatabaseSchema.ZONES, "ALTER TABLE WALLS ADD INDEX WALLSSTRUCTUREID (STRUCTURE)");
/*     */     
/* 211 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 212 */     logger.info("Created database indices took " + lElapsedTime + " millis.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeIndexes() {
/* 222 */     if (DbConnector.isUseSqlite())
/*     */       return; 
/* 224 */     logger.info("Starting to remove database indices");
/* 225 */     long start = System.nanoTime();
/*     */     
/* 227 */     removeIndex(WurmDatabaseSchema.CREATURES, "ALTER TABLE SKILLS DROP INDEX OWNERID");
/*     */     
/* 229 */     removeIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE BODYPARTS DROP INDEX BODYZONEID");
/*     */     
/* 231 */     removeIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE COINS DROP INDEX COINSZONEID");
/*     */     
/* 233 */     removeIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE EFFECTS DROP INDEX OWNERID");
/*     */     
/* 235 */     removeIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE FROZENITEMS DROP INDEX FROZENITEMS_TEMPLATEID");
/*     */ 
/*     */ 
/*     */     
/* 239 */     removeIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE ITEMS DROP INDEX ITEMS_TEMPLATEID");
/*     */     
/* 241 */     removeIndex(WurmDatabaseSchema.ITEMS, "ALTER TABLE ITEMS DROP INDEX ITEMZONEID");
/*     */     
/* 243 */     removeIndex(WurmDatabaseSchema.ZONES, "ALTER TABLE FENCES DROP INDEX FENCEZONEID");
/*     */     
/* 245 */     removeIndex(WurmDatabaseSchema.ZONES, "ALTER TABLE WALLS DROP INDEX WALLSSTRUCTUREID");
/*     */     
/* 247 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 248 */     logger.info("Removed database indices took " + lElapsedTime + " millis.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void repairDatabaseTables() {
/* 258 */     if (DbConnector.isUseSqlite())
/*     */       return; 
/* 260 */     Connection dbcon = null;
/* 261 */     Statement stmt = null;
/*     */     
/*     */     try {
/* 264 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 266 */         logger.fine("Checking and, if necessary, repairing Items database table");
/*     */       }
/* 268 */       dbcon = DbConnector.getItemDbCon();
/* 269 */       stmt = dbcon.createStatement();
/* 270 */       stmt.execute("REPAIR TABLE ITEMS");
/*     */     }
/* 272 */     catch (SQLException sqx) {
/*     */       
/* 274 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 278 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 279 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 283 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 285 */         logger.fine("Checking and, if necessary, repairing Coins database table");
/*     */       }
/* 287 */       dbcon = DbConnector.getItemDbCon();
/* 288 */       stmt = dbcon.createStatement();
/* 289 */       stmt.execute("REPAIR TABLE COINS");
/*     */     }
/* 291 */     catch (SQLException sqx) {
/*     */       
/* 293 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 297 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 298 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 302 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 304 */         logger.fine("Checking and, if necessary, repairing Bodyparts database table");
/*     */       }
/* 306 */       dbcon = DbConnector.getItemDbCon();
/* 307 */       stmt = dbcon.createStatement();
/* 308 */       stmt.execute("REPAIR TABLE BODYPARTS");
/*     */     }
/* 310 */     catch (SQLException sqx) {
/*     */       
/* 312 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 316 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 317 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 321 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 323 */         logger.fine("Checking and, if necessary, repairing Walls database table");
/*     */       }
/* 325 */       dbcon = DbConnector.getZonesDbCon();
/* 326 */       stmt = dbcon.createStatement();
/* 327 */       stmt.execute("REPAIR TABLE WALLS");
/*     */     }
/* 329 */     catch (SQLException sqx) {
/*     */       
/* 331 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 335 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 336 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 340 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 342 */         logger.fine("Checking and, if necessary, repairing Fences database table");
/*     */       }
/* 344 */       dbcon = DbConnector.getZonesDbCon();
/* 345 */       stmt = dbcon.createStatement();
/* 346 */       stmt.execute("REPAIR TABLE FENCES");
/*     */     }
/* 348 */     catch (SQLException sqx) {
/*     */       
/* 350 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 354 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 355 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 359 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 361 */         logger.fine("Checking and, if necessary, repairing Players database table");
/*     */       }
/* 363 */       dbcon = DbConnector.getPlayerDbCon();
/* 364 */       stmt = dbcon.createStatement();
/* 365 */       stmt.execute("REPAIR TABLE PLAYERS");
/*     */     }
/* 367 */     catch (SQLException sqx) {
/*     */       
/* 369 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 373 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 374 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 378 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 380 */         logger.fine("Checking and, if necessary, repairing Skills database table");
/*     */       }
/* 382 */       dbcon = DbConnector.getPlayerDbCon();
/* 383 */       stmt = dbcon.createStatement();
/* 384 */       stmt.execute("REPAIR TABLE SKILLS");
/*     */     }
/* 386 */     catch (SQLException sqx) {
/*     */       
/* 388 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 392 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 393 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 397 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 399 */         logger.fine("Checking and, if necessary, repairing Creatures database table");
/*     */       }
/* 401 */       dbcon = DbConnector.getCreatureDbCon();
/* 402 */       stmt = dbcon.createStatement();
/* 403 */       stmt.execute("REPAIR TABLE CREATURES");
/*     */     }
/* 405 */     catch (SQLException sqx) {
/*     */       
/* 407 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 411 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 412 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 416 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 418 */         logger.fine("Checking and, if necessary, repairing Effects database table");
/*     */       }
/* 420 */       dbcon = DbConnector.getCreatureDbCon();
/* 421 */       stmt = dbcon.createStatement();
/* 422 */       stmt.execute("REPAIR TABLE EFFECTS");
/*     */     }
/* 424 */     catch (SQLException sqx) {
/*     */       
/* 426 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 430 */       DbUtilities.closeDatabaseObjects(stmt, null);
/* 431 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\DbIndexManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */