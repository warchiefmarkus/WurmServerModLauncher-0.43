/*    */ package com.wurmonline.server.database;
/*    */ 
/*    */ import javax.annotation.concurrent.Immutable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public enum WurmDatabaseSchema
/*    */ {
/* 14 */   CREATURES("WURMCREATURES", "creatures"),
/* 15 */   DEITIES("WURMDEITIES", "deities"),
/* 16 */   ECONOMY("WURMECONOMY", "economy"),
/* 17 */   ITEMS("WURMITEMS", "items"),
/* 18 */   LOGIN("WURMLOGIN", "login"),
/* 19 */   LOGS("WURMLOGS", "logs"),
/* 20 */   PLAYERS("WURMPLAYERS", "players"),
/* 21 */   TEMPLATES("WURMTEMPLATES", "templates"),
/* 22 */   ZONES("WURMZONES", "zones");
/*    */   
/*    */   private final String database;
/*    */   
/*    */   private final String migration;
/*    */   
/*    */   WurmDatabaseSchema(String database, String migration) {
/* 29 */     this.database = database;
/* 30 */     this.migration = migration;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDatabase() {
/* 42 */     return this.database;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMigration() {
/* 51 */     return this.migration;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\WurmDatabaseSchema.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */