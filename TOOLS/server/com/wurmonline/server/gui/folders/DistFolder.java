/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.logging.Logger;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DistFolder
/*    */   extends Folder
/*    */ {
/* 13 */   private static final Logger logger = Logger.getLogger(PresetFolder.class.getName());
/*    */ 
/*    */   
/*    */   public DistFolder(Path path) {
/* 17 */     super(path);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static DistFolder fromPath(Path path) {
/* 23 */     if (path == null)
/* 24 */       return null; 
/* 25 */     if (!Files.isDirectory(path, new java.nio.file.LinkOption[0]))
/* 26 */       return null; 
/* 27 */     for (DistEntity entity : DistEntity.values()) {
/*    */       
/* 29 */       if (entity.isRequired() && !entity.existsIn(path)) {
/*    */         
/* 31 */         logger.warning("Dist folder missing " + entity.filename());
/* 32 */         return null;
/*    */       } 
/*    */     } 
/* 35 */     return new DistFolder(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public final Path getPathFor(DistEntity entity) {
/* 40 */     return getPath().resolve(entity.filename());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\DistFolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */