/*    */ package com.wurmonline.server.filesystems;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class MajorFileSystem
/*    */ {
/* 25 */   protected String rootDir = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MajorFileSystem(String aRootDir) {
/* 32 */     this.rootDir = aRootDir;
/* 33 */     if (this.rootDir.length() == 0) {
/* 34 */       this.rootDir = ".";
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDir(String fileName) {
/* 44 */     int hashCode = fileName.hashCode();
/* 45 */     int dir1 = hashCode >> 24 & 0xFF;
/* 46 */     int dir2 = hashCode >> 16 & 0xFF;
/* 47 */     int dir3 = hashCode >> 8 & 0xFF;
/* 48 */     String fileDir = this.rootDir + File.separator + dir1 + File.separator + dir2 + File.separator + dir3 + File.separator;
/* 49 */     File saveDir = new File(fileDir);
/* 50 */     if (!saveDir.exists())
/* 51 */       saveDir.mkdirs(); 
/* 52 */     return fileDir;
/*    */   }
/*    */ 
/*    */   
/*    */   public File[] getAllFiles() {
/* 57 */     List<File> files = new LinkedList<>();
/* 58 */     File[] dirFiles = (new File(this.rootDir)).listFiles();
/* 59 */     for (int x = 0; x < dirFiles.length; x++) {
/*    */       
/* 61 */       if (dirFiles[x].isDirectory()) {
/* 62 */         files.addAll(getAllFiles(dirFiles[x]));
/*    */       } else {
/* 64 */         files.add(dirFiles[x]);
/*    */       } 
/* 66 */     }  File[] toReturn = new File[files.size()];
/* 67 */     return files.<File>toArray(toReturn);
/*    */   }
/*    */ 
/*    */   
/*    */   private List<File> getAllFiles(File dir) {
/* 72 */     List<File> files = new LinkedList<>();
/* 73 */     File[] dirFiles = dir.listFiles();
/* 74 */     for (int x = 0; x < dirFiles.length; x++) {
/*    */       
/* 76 */       if (dirFiles[x].isDirectory()) {
/* 77 */         files.addAll(getAllFiles(dirFiles[x]));
/*    */       } else {
/* 79 */         files.add(dirFiles[x]);
/*    */       } 
/* 81 */     }  return files;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\filesystems\MajorFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */