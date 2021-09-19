/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DistEntity
/*    */ {
/* 11 */   Recipes("recipes", true),
/* 12 */   Adventure("Adventure", true),
/* 13 */   Creative("Creative", true),
/* 14 */   Migrations("migrations", true);
/*    */   
/*    */   private FolderEntity entity;
/*    */ 
/*    */   
/*    */   DistEntity(String name, boolean required) {
/* 20 */     this.entity = new FolderEntity(name, required);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return this.entity.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String filename() {
/* 30 */     return this.entity.getFilename();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRequired() {
/* 35 */     return this.entity.isRequired();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean existsIn(DistFolder folder) {
/* 40 */     return this.entity.existsIn(folder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean existsIn(Path path) {
/* 45 */     return this.entity.existsIn(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public void createIn(DistFolder folder) throws IOException {
/* 50 */     this.entity.createIn(folder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public void createIn(Path path) throws IOException {
/* 55 */     this.entity.createIn(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public void deleteFrom(DistFolder folder) throws IOException {
/* 60 */     this.entity.deleteFrom(folder.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public void deleteFrom(Path path) throws IOException {
/* 65 */     this.entity.deleteFrom(path);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\DistEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */