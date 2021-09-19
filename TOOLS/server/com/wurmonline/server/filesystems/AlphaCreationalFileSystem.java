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
/*    */ 
/*    */ 
/*    */ public class AlphaCreationalFileSystem
/*    */   extends AlphabeticalFileSystem
/*    */ {
/*    */   public AlphaCreationalFileSystem(String aRootDir) {
/* 31 */     super(aRootDir);
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
/* 42 */     String dirName = fileName;
/* 43 */     if (fileName.indexOf(".") > 0)
/* 44 */       dirName = fileName.substring(0, fileName.indexOf(".")); 
/* 45 */     String firstLetter = fileName.substring(0, 1);
/* 46 */     String secondLetters = fileName.substring(0, 2);
/*    */     
/* 48 */     String dir1 = firstLetter.toLowerCase();
/* 49 */     String dir2 = secondLetters.toLowerCase();
/* 50 */     String fileDir = this.rootDir + File.separator + dir1 + File.separator + dir2 + File.separator + dirName + File.separator;
/* 51 */     File saveDir = new File(fileDir);
/* 52 */     if (!saveDir.exists())
/* 53 */       saveDir.mkdirs(); 
/* 54 */     return fileDir;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\filesystems\AlphaCreationalFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */