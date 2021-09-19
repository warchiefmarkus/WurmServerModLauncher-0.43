/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FolderEntity
/*    */ {
/*    */   boolean required;
/*    */   String name;
/*    */   
/*    */   FolderEntity(String name, boolean required) {
/* 17 */     this.required = required;
/* 18 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 24 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFilename() {
/* 29 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRequired() {
/* 34 */     return this.required;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean existsIn(Path path) {
/* 39 */     return Files.exists(path.resolve(getFilename()), new java.nio.file.LinkOption[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void createIn(Path path) throws IOException {
/* 44 */     Files.createFile(path.resolve(getFilename()), (FileAttribute<?>[])new FileAttribute[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void deleteFrom(Path path) throws IOException {
/* 49 */     Files.delete(path.resolve(getFilename()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\FolderEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */