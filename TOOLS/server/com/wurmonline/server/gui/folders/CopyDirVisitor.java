/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ 
/*    */ public class CopyDirVisitor extends SimpleFileVisitor<Path> {
/*    */   private final Path fromPath;
/*    */   private final Path toPath;
/*    */   private final CopyOption copyOption;
/*    */   
/*    */   public CopyDirVisitor(Path fromPath, Path toPath, CopyOption copyOption) {
/* 18 */     this.fromPath = fromPath;
/* 19 */     this.toPath = toPath;
/* 20 */     this.copyOption = copyOption;
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/* 25 */     Path target = this.toPath.resolve(this.fromPath.relativize(dir));
/* 26 */     if (!Files.exists(target, new java.nio.file.LinkOption[0]))
/*    */     {
/* 28 */       Files.createDirectory(target, (FileAttribute<?>[])new FileAttribute[0]);
/*    */     }
/* 30 */     return FileVisitResult.CONTINUE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 36 */     Files.copy(file, this.toPath.resolve(this.fromPath.relativize(file)), new CopyOption[] { this.copyOption });
/* 37 */     return FileVisitResult.CONTINUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\CopyDirVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */