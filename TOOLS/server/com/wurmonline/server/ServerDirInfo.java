/*    */ package com.wurmonline.server;
/*    */ 
/*    */ import com.wurmonline.server.gui.folders.GameEntity;
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
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
/*    */ public class ServerDirInfo
/*    */ {
/* 32 */   private static String constantsFileName = "wurm.ini";
/* 33 */   private static String fileDBPath = "wurmDB" + File.separator;
/* 34 */   private static Path path = Paths.get("wurmDB", new String[0]);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getConstantsFileName() {
/* 43 */     return constantsFileName;
/*    */   }
/*    */   public static String getFileDBPath() {
/* 46 */     return fileDBPath;
/*    */   }
/*    */   public static void setFileDBPath(String fileDBPath) {
/* 49 */     ServerDirInfo.fileDBPath = fileDBPath;
/*    */   }
/*    */   
/*    */   public static void setPath(Path path) {
/* 53 */     ServerDirInfo.path = path;
/* 54 */     fileDBPath = path.toString() + File.separator;
/* 55 */     constantsFileName = path.resolve(GameEntity.WurmINI.filename()).toString();
/*    */   }
/*    */   
/*    */   public static Path getPath() {
/* 59 */     return path;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\ServerDirInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */