/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum GameEntity
/*    */ {
/* 11 */   CurrentDir("currentdir", false),
/* 12 */   GameDir("gamedir", false),
/* 13 */   WurmINI("wurm.ini", true),
/* 14 */   Sqlite("sqlite", true),
/* 15 */   TopLayer("top_layer.map", true),
/* 16 */   RockLayer("rock_layer.map", true),
/* 17 */   Flags("flags.map", false),
/* 18 */   Cave("map_cave.map", false),
/* 19 */   Resources("resources.map", false),
/* 20 */   Recipes("recipes", false),
/* 21 */   ProtectedTiles("protectedTiles.bmap", false);
/*    */   
/*    */   private FolderEntity entity;
/*    */ 
/*    */   
/*    */   GameEntity(String name, boolean required) {
/* 27 */     this.entity = new FolderEntity(name, required);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return this.entity.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String filename() {
/* 37 */     return this.entity.getFilename();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRequired() {
/* 42 */     return this.entity.isRequired();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean existsIn(GameFolder gameFolder) {
/* 47 */     return this.entity.existsIn(gameFolder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean existsIn(Path path) {
/* 52 */     return this.entity.existsIn(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public void createIn(GameFolder gameFolder) throws IOException {
/* 57 */     this.entity.createIn(gameFolder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public void createIn(Path path) throws IOException {
/* 62 */     this.entity.createIn(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public void deleteFrom(GameFolder gameFolder) throws IOException {
/* 67 */     this.entity.deleteFrom(gameFolder.getPath());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\GameEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */