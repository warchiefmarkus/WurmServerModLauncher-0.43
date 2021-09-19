/*    */ package com.wurmonline.server.filesystems;
/*    */ 
/*    */ import com.wurmonline.server.Constants;
/*    */ import com.wurmonline.server.ServerDirInfo;
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
/*    */ public final class FileSystems
/*    */ {
/* 24 */   public static final AlphabeticalFileSystem creatureTemplates = new AlphabeticalFileSystem(ServerDirInfo.getFileDBPath() + Constants.creatureTemplatesDBPath);
/*    */   
/* 26 */   public static final AlphabeticalFileSystem skillTemplates = new AlphabeticalFileSystem(ServerDirInfo.getFileDBPath() + Constants.skillTemplatesDBPath);
/*    */   
/* 28 */   public static final AlphabeticalFileSystem itemTemplates = new AlphabeticalFileSystem(ServerDirInfo.getFileDBPath() + Constants.itemTemplatesDBPath);
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final AlphaCreationalFileSystem playerStats = new AlphaCreationalFileSystem(ServerDirInfo.getFileDBPath() + Constants.playerStatsDBPath);
/*    */   
/* 34 */   public static final MajorFileSystem creatureStats = new MajorFileSystem(ServerDirInfo.getFileDBPath() + Constants.creatureStatsDBPath);
/*    */   
/* 36 */   public static final MajorFileSystem itemStats = new MajorFileSystem(ServerDirInfo.getFileDBPath() + Constants.itemStatsDBPath);
/* 37 */   public static final MajorFileSystem zoneStats = new MajorFileSystem(ServerDirInfo.getFileDBPath() + Constants.zonesDBPath);
/*    */ 
/*    */   
/* 40 */   public static final MajorFileSystem itemOldStats = new MajorFileSystem(ServerDirInfo.getFileDBPath() + Constants.itemOldStatsDBPath);
/*    */   
/* 42 */   public static final MajorFileSystem creatureOldStats = new MajorFileSystem(ServerDirInfo.getFileDBPath() + Constants.creatureOldStatsDBPath);
/*    */ 
/*    */   
/* 45 */   public static final MajorFileSystem tileStats = new MajorFileSystem(ServerDirInfo.getFileDBPath() + Constants.tileStatsDBPath);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getCreatureTemplateDirFor(String fileName) {
/* 56 */     return creatureTemplates.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getSkillTemplateDirFor(String fileName) {
/* 61 */     return skillTemplates.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getItemTemplateDirFor(String fileName) {
/* 66 */     return itemTemplates.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getCreatureStateDirFor(String fileName) {
/* 71 */     return creatureStats.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getPlayerStateDirFor(String fileName) {
/* 76 */     return playerStats.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getItemStateDirFor(String fileName) {
/* 81 */     return itemStats.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getItemOldStateDirFor(String fileName) {
/* 86 */     return itemOldStats.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getTileStateDirFor(String fileName) {
/* 91 */     return tileStats.getDir(fileName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getZoneStateDirFor(String fileName) {
/* 96 */     return zoneStats.getDir(fileName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\filesystems\FileSystems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */