/*     */ package impl.org.controlsfx.i18n;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Translations
/*     */ {
/*  46 */   private static List<Translation> translations = new ArrayList<>();
/*     */ 
/*     */   
/*     */   static {
/*  50 */     File file = new File(Translations.class.getProtectionDomain().getCodeSource().getLocation().getPath());
/*  51 */     if (file.getName().endsWith(".jar")) {
/*  52 */       Path jarFile = file.toPath();
/*  53 */       try (FileSystem fs = FileSystems.newFileSystem(jarFile, (ClassLoader)null)) {
/*  54 */         fs.getRootDirectories().forEach(path -> loadFrom(path));
/*  55 */       } catch (IOException e) {
/*  56 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  61 */     if (translations.isEmpty()) {
/*     */ 
/*     */       
/*  64 */       Path srcDir = (new File("src/main/resources")).toPath();
/*  65 */       loadFrom(srcDir);
/*     */     } 
/*     */ 
/*     */     
/*  69 */     if (translations.isEmpty()) {
/*  70 */       Path binDir = (new File("bin")).toPath();
/*  71 */       loadFrom(binDir);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  76 */     if (translations.isEmpty() && 
/*  77 */       file.getAbsolutePath().endsWith("controlsfx" + File.separator + "bin")) {
/*  78 */       loadFrom(file.toPath());
/*     */     }
/*     */ 
/*     */     
/*  82 */     Collections.sort(translations);
/*     */   }
/*     */   
/*     */   private static void loadFrom(Path rootPath) {
/*  86 */     try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootPath)) {
/*  87 */       for (Path path : stream) {
/*  88 */         String filename = path.getFileName().toString();
/*     */         
/*  90 */         if (!filename.startsWith("controlsfx") && !filename.endsWith(".properties")) {
/*     */           continue;
/*     */         }
/*     */         
/*  94 */         if ("controlsfx.properties".equals(filename)) {
/*  95 */           translations.add(new Translation("en", path)); continue;
/*  96 */         }  if (filename.contains("_")) {
/*  97 */           String locale = filename.substring(11, filename.indexOf(".properties"));
/*  98 */           translations.add(new Translation(locale, path)); continue;
/*     */         } 
/* 100 */         throw new IllegalStateException("Unknown translation file '" + path + "'.");
/*     */       }
/*     */     
/*     */     }
/* 104 */     catch (IOException|java.nio.file.DirectoryIteratorException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<Translation> getTranslation(String localeString) {
/* 114 */     for (Translation t : translations) {
/* 115 */       if (localeString.equals(t.getLocaleString())) {
/* 116 */         return Optional.of(t);
/*     */       }
/*     */     } 
/* 119 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static List<Translation> getAllTranslations() {
/* 123 */     return translations;
/*     */   }
/*     */   
/*     */   public static List<Locale> getAllTranslationLocales() {
/* 127 */     return (List<Locale>)translations.stream().map(t -> t.getLocale()).collect(Collectors.toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\i18n\Translations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */