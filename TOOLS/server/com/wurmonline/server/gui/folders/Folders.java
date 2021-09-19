/*     */ package com.wurmonline.server.gui.folders;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public enum Folders {
/*     */   private Path presetsPath;
/*     */   private Path gamesPath;
/*     */   private Path distPath;
/*     */   private DistFolder dist;
/*     */   private GameFolder current;
/*     */   private HashMap<String, PresetFolder> presets;
/*  18 */   INSTANCE; private HashMap<String, GameFolder> gameFolders; private static final Logger logger;
/*     */   
/*     */   Folders() {
/*  21 */     this.gameFolders = new HashMap<>();
/*  22 */     this.presets = new HashMap<>();
/*     */ 
/*     */     
/*  25 */     this.distPath = Paths.get(System.getProperty("wurm.distRoot", "./dist"), new String[0]);
/*  26 */     this.gamesPath = Paths.get(System.getProperty("wurm.gameFolderRoot", "."), new String[0]);
/*  27 */     this.presetsPath = Paths.get(System.getProperty("wurm.presetsRoot", "./presets"), new String[0]);
/*     */   } static {
/*     */     logger = Logger.getLogger(Folders.class.getName());
/*     */   } public static Folders getInstance() {
/*  31 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ArrayList<GameFolder> getGameFolders() {
/*  36 */     ArrayList<GameFolder> gameFolders = new ArrayList<>();
/*  37 */     gameFolders.addAll((getInstance()).gameFolders.values());
/*  38 */     return gameFolders;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static GameFolder getGameFolder(String folderName) {
/*  44 */     return (getInstance()).gameFolders.get(folderName);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean setCurrent(GameFolder gameFolder) {
/*  49 */     if ((getInstance()).current != null)
/*     */     {
/*  51 */       if (!(getInstance()).current.setCurrent(false))
/*  52 */         return false; 
/*     */     }
/*  54 */     (getInstance()).current = gameFolder;
/*  55 */     if (!gameFolder.setCurrent(true))
/*  56 */       return false; 
/*  57 */     logger.info("Current game folder: " + gameFolder.getName());
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clear() {
/*  63 */     (getInstance()).gameFolders.clear();
/*  64 */     (getInstance()).current = null;
/*  65 */     logger.info("Game folders cleared.");
/*     */   }
/*     */ 
/*     */   
/*     */   public static GameFolder getCurrent() {
/*  70 */     return (getInstance()).current;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean loadGames() {
/*  75 */     return loadGamesFrom((getInstance()).gamesPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean loadGamesFrom(Path parent) {
/*  80 */     if (!(getInstance()).gameFolders.isEmpty()) {
/*  81 */       (getInstance()).gameFolders = new HashMap<>();
/*     */     }
/*  83 */     try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(parent)) {
/*     */       
/*  85 */       for (Path path : directoryStream) {
/*     */         
/*  87 */         GameFolder gameFolder = GameFolder.fromPath(path);
/*  88 */         if (gameFolder == null)
/*     */           continue; 
/*  90 */         (getInstance()).gameFolders.put(gameFolder.getName(), gameFolder);
/*  91 */         if (gameFolder.isCurrent()) {
/*     */ 
/*     */           
/*  94 */           if ((getInstance()).current == null) {
/*  95 */             (getInstance()).current = gameFolder; continue;
/*  96 */           }  if (!gameFolder.setCurrent(false)) {
/*  97 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/* 101 */     } catch (IOException ex) {
/*     */       
/* 103 */       logger.warning("IOException while reading game folders");
/* 104 */       ex.printStackTrace();
/* 105 */       return false;
/*     */     } 
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean loadDist() {
/* 112 */     (getInstance()).dist = DistFolder.fromPath((getInstance()).distPath);
/* 113 */     return ((getInstance()).dist != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DistFolder getDist() {
/* 118 */     if ((getInstance()).dist == null)
/*     */     {
/* 120 */       if (!loadDist()) {
/*     */         
/* 122 */         logger.warning("Unable to load 'dist' folder, please run Steam validation");
/*     */         
/* 124 */         return new DistFolder((getInstance()).distPath);
/*     */       } 
/*     */     }
/* 127 */     return (getInstance()).dist;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean loadPresets() {
/* 132 */     if (!(getInstance()).presets.isEmpty()) {
/* 133 */       (getInstance()).presets = new HashMap<>();
/*     */     }
/* 135 */     if ((getInstance()).dist == null)
/*     */     {
/* 137 */       if (!loadDist()) {
/*     */         
/* 139 */         logger.warning("Unable to load 'dist' folder, please run Steam validation");
/* 140 */         return false;
/*     */       } 
/*     */     }
/* 143 */     if (!loadPresetsFrom((getInstance()).dist.getPath())) {
/*     */       
/* 145 */       logger.warning("Unable to load presets from 'dist', please run Steam validation");
/* 146 */       return false;
/*     */     } 
/* 148 */     if (!Files.exists((getInstance()).presetsPath, new java.nio.file.LinkOption[0])) {
/*     */       
/*     */       try {
/*     */         
/* 152 */         Files.createDirectory((getInstance()).presetsPath, (FileAttribute<?>[])new FileAttribute[0]);
/*     */       }
/* 154 */       catch (IOException ex) {
/*     */         
/* 156 */         logger.warning("Could not create presets folder");
/* 157 */         return false;
/*     */       } 
/*     */     }
/* 160 */     return loadPresetsFrom((getInstance()).presetsPath);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean loadPresetsFrom(Path parent) {
/* 165 */     try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(parent)) {
/*     */       
/* 167 */       for (Path path : directoryStream)
/*     */       {
/* 169 */         PresetFolder folder = PresetFolder.fromPath(path);
/* 170 */         if (folder == null)
/*     */           continue; 
/* 172 */         (getInstance()).presets.put(folder.getName(), folder);
/*     */       }
/*     */     
/* 175 */     } catch (IOException ex) {
/*     */       
/* 177 */       logger.warning("IOException while reading game folders");
/* 178 */       ex.printStackTrace();
/* 179 */       return false;
/*     */     } 
/* 181 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Path getGamesPath() {
/* 186 */     return (getInstance()).gamesPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addGame(GameFolder folder) {
/* 191 */     (getInstance()).gameFolders.put(folder.getName(), folder);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeGame(GameFolder folder) {
/* 196 */     (getInstance()).gameFolders.remove(folder.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\folders\Folders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */