/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.DirectoryStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.Comparator;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Folder
/*    */ {
/* 15 */   private static final Logger logger = Logger.getLogger(Folder.class.getName());
/*    */   
/*    */   Path path;
/*    */   String name;
/*    */   
/*    */   public Folder(Path path) {
/* 21 */     this.path = path;
/* 22 */     this.name = this.path.getName(path.getNameCount() - 1).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public final Path getPath() {
/* 27 */     return this.path;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() throws IOException {
/* 37 */     try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(this.path)) {
/*    */       
/* 39 */       return !dirStream.iterator().hasNext();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean create() {
/* 45 */     if (exists()) {
/* 46 */       return true;
/*    */     }
/*    */     try {
/* 49 */       Files.createDirectory(this.path, (FileAttribute<?>[])new FileAttribute[0]);
/*    */     }
/* 51 */     catch (IOException ex) {
/*    */       
/* 53 */       logger.warning("Exception creating " + this.path.toString());
/* 54 */       ex.printStackTrace();
/* 55 */       return false;
/*    */     } 
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean exists() {
/* 62 */     return Files.exists(this.path, new java.nio.file.LinkOption[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean delete() {
/* 67 */     if (!exists()) {
/* 68 */       return true;
/*    */     }
/*    */     
/*    */     try {
/* 72 */       Files.walk(this.path, new java.nio.file.FileVisitOption[0])
/* 73 */         .sorted(Comparator.reverseOrder())
/* 74 */         .forEach(path -> {
/*    */ 
/*    */ 
/*    */             
/*    */             try {
/*    */               Files.delete(path);
/* 80 */             } catch (IOException ex) {
/*    */               logger.warning("Exception deleting " + this.path.toString());
/*    */ 
/*    */               
/*    */               ex.printStackTrace();
/*    */             } 
/*    */           });
/* 87 */     } catch (IOException ex) {
/*    */       
/* 89 */       logger.warning("Exception deleting " + this.path.toString());
/* 90 */       ex.printStackTrace();
/* 91 */       return false;
/*    */     } 
/* 93 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void move(Path path) throws IOException {
/* 98 */     Files.move(this.path, path, new java.nio.file.CopyOption[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\Folder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */