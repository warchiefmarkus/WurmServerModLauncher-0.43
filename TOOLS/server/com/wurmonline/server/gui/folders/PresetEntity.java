/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum PresetEntity
/*    */ {
/* 11 */   WurmINI("wurm.ini", true),
/* 12 */   Sqlite("sqlite", true),
/* 13 */   OriginalDir("originaldir", false),
/* 14 */   TopLayer("top_layer.map", true),
/* 15 */   RockLayer("rock_layer.map", true),
/* 16 */   Flags("flags.map", false),
/* 17 */   Cave("map_cave.map", false),
/* 18 */   Resources("resources.map", false),
/* 19 */   ProtectedTiles("protectedTiles.bmap", false);
/*    */   
/*    */   private FolderEntity entity;
/*    */ 
/*    */   
/*    */   PresetEntity(String name, boolean required) {
/* 25 */     this.entity = new FolderEntity(name, required);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     return this.entity.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String filename() {
/* 35 */     return this.entity.getFilename();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRequired() {
/* 40 */     return this.entity.isRequired();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean existsIn(PresetFolder folder) {
/* 45 */     return this.entity.existsIn(folder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean existsIn(Path path) {
/* 50 */     return this.entity.existsIn(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public void createIn(PresetFolder folder) throws IOException {
/* 55 */     this.entity.createIn(folder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public void createIn(Path path) throws IOException {
/* 60 */     this.entity.createIn(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public void deleteFrom(PresetFolder folder) throws IOException {
/* 65 */     this.entity.deleteFrom(folder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public void deleteFrom(Path path) throws IOException {
/* 70 */     this.entity.deleteFrom(path);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\PresetEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */