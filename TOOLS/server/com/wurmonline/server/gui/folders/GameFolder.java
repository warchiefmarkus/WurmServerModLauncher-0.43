/*     */ package com.wurmonline.server.gui.folders;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameFolder
/*     */   extends Folder
/*     */ {
/*  19 */   private static final Logger logger = Logger.getLogger(GameFolder.class.getName());
/*     */   
/*     */   boolean current;
/*     */   
/*     */   public GameFolder(Path path, boolean current) {
/*  24 */     this(path);
/*  25 */     this.current = current;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameFolder(Path path) {
/*  30 */     super(path);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static GameFolder fromPath(Path path) {
/*  36 */     if (path == null)
/*  37 */       return null; 
/*  38 */     if (!Files.isDirectory(path, new java.nio.file.LinkOption[0]))
/*  39 */       return null; 
/*  40 */     if (!GameEntity.GameDir.existsIn(path))
/*  41 */       return null; 
/*  42 */     return new GameFolder(path, GameEntity.CurrentDir.existsIn(path));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean create() {
/*  47 */     if (!super.create())
/*  48 */       return false; 
/*  49 */     if (GameEntity.GameDir.existsIn(this.path)) {
/*  50 */       return true;
/*     */     }
/*     */     try {
/*  53 */       GameEntity.GameDir.createIn(this.path);
/*     */     }
/*  55 */     catch (IOException ex) {
/*     */       
/*  57 */       logger.warning("Could not create gamedir in " + this.path.toString());
/*  58 */       ex.printStackTrace();
/*  59 */       return false;
/*     */     } 
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getError() {
/*  66 */     for (GameEntity entity : GameEntity.values()) {
/*     */       
/*  68 */       if (entity.isRequired() && !entity.existsIn(this))
/*  69 */         return "Game folder missing: " + entity.filename(); 
/*     */     } 
/*  71 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCurrent() {
/*  76 */     return this.current;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean setCurrent(boolean isCurrent) {
/*     */     try {
/*  83 */       this.current = isCurrent;
/*  84 */       if (isCurrent && !GameEntity.CurrentDir.existsIn(this)) {
/*  85 */         GameEntity.CurrentDir.createIn(this);
/*  86 */       } else if (!isCurrent && GameEntity.CurrentDir.existsIn(this)) {
/*  87 */         GameEntity.CurrentDir.deleteFrom(this);
/*     */       } 
/*  89 */     } catch (IOException ex) {
/*     */       
/*  91 */       logger.warning("Unable to set current game folder");
/*  92 */       ex.printStackTrace();
/*  93 */       return false;
/*     */     } 
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean copyTo(Path path) {
/* 100 */     if (!Files.exists(path, new java.nio.file.LinkOption[0]) || !exists())
/* 101 */       return false; 
/* 102 */     for (GameEntity entity : GameEntity.values()) {
/* 103 */       if (entity != GameEntity.CurrentDir && entity != GameEntity.GameDir && entity.existsIn(this.path))
/*     */         
/*     */         try {
/* 106 */           if (Files.isDirectory(this.path.resolve(entity.filename()), new java.nio.file.LinkOption[0])) {
/* 107 */             Files.walkFileTree(this.path.resolve(entity.filename()), new CopyDirVisitor(this.path.resolve(entity.filename()), path.resolve(entity.filename()), StandardCopyOption.REPLACE_EXISTING));
/*     */           } else {
/* 109 */             Files.copy(this.path.resolve(entity.filename()), path.resolve(entity.filename()), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */           } 
/* 111 */         } catch (IOException e) {
/*     */           
/* 113 */           logger.warning("Unable to copy " + entity.filename() + " from " + this.path.toString() + " to " + path.toString());
/* 114 */           e.printStackTrace();
/* 115 */           return false;
/*     */         }  
/* 117 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getPathFor(GameEntity entity) {
/* 122 */     return getPath().resolve(entity.filename());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\GameFolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */