/*    */ package com.wurmonline.server.gui.folders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import java.util.logging.Logger;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PresetFolder
/*    */   extends Folder
/*    */ {
/* 17 */   private static final Logger logger = Logger.getLogger(PresetFolder.class.getName());
/*    */   
/*    */   private boolean original;
/*    */   
/*    */   public PresetFolder(Path path, boolean original) {
/* 22 */     this(path);
/* 23 */     this.original = original;
/*    */   }
/*    */ 
/*    */   
/*    */   public PresetFolder(Path path) {
/* 28 */     super(path);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static PresetFolder fromPath(Path path) {
/* 34 */     if (path == null)
/* 35 */       return null; 
/* 36 */     if (!Files.isDirectory(path, new java.nio.file.LinkOption[0]))
/* 37 */       return null; 
/* 38 */     for (PresetEntity entity : PresetEntity.values()) {
/* 39 */       if (entity.isRequired() && !entity.existsIn(path))
/* 40 */         return null; 
/* 41 */     }  return new PresetFolder(path, PresetEntity.OriginalDir.existsIn(path));
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getError() {
/* 46 */     for (PresetEntity entity : PresetEntity.values()) {
/*    */       
/* 48 */       if (entity.isRequired() && !entity.existsIn(this))
/* 49 */         return "Preset folder missing: " + entity.filename(); 
/*    */     } 
/* 51 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean delete() {
/* 56 */     return (!this.original && super.delete());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOriginal() {
/* 61 */     return this.original;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean copyTo(Path path) {
/* 66 */     if (!Files.exists(path, new java.nio.file.LinkOption[0]) || !exists())
/* 67 */       return false; 
/* 68 */     for (PresetEntity entity : PresetEntity.values()) {
/* 69 */       if (entity != PresetEntity.OriginalDir && entity.existsIn(this.path))
/*    */         
/*    */         try {
/* 72 */           if (Files.isDirectory(this.path.resolve(entity.filename()), new java.nio.file.LinkOption[0])) {
/* 73 */             Files.walkFileTree(this.path.resolve(entity.filename()), new CopyDirVisitor(this.path.resolve(entity.filename()), path.resolve(entity.filename()), StandardCopyOption.REPLACE_EXISTING));
/*    */           } else {
/* 75 */             Files.copy(this.path.resolve(entity.filename()), path.resolve(entity.filename()), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*    */           } 
/* 77 */         } catch (IOException e) {
/*    */           
/* 79 */           logger.warning("Unable to copy " + entity.filename() + " from " + this.path.toString() + " to " + path.toString());
/* 80 */           e.printStackTrace();
/* 81 */           return false;
/*    */         }  
/* 83 */     }  return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\PresetFolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */