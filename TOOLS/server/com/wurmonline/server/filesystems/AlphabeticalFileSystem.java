/*    */ package com.wurmonline.server.filesystems;
/*    */ 
/*    */ import java.io.File;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AlphabeticalFileSystem
/*    */   extends MajorFileSystem
/*    */ {
/*    */   public AlphabeticalFileSystem(String aRootDir) {
/* 29 */     super(aRootDir);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDir(String fileName) {
/* 40 */     String firstLetter = fileName.substring(0, 1);
/* 41 */     String secondLetters = fileName.substring(0, 2);
/*    */     
/* 43 */     String dir1 = firstLetter.toLowerCase();
/* 44 */     String dir2 = secondLetters.toLowerCase();
/* 45 */     String fileDir = this.rootDir + File.separator + dir1 + File.separator + dir2 + File.separator;
/* 46 */     File saveDir = new File(fileDir);
/* 47 */     if (!saveDir.exists())
/* 48 */       saveDir.mkdirs(); 
/* 49 */     return fileDir;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\filesystems\AlphabeticalFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */