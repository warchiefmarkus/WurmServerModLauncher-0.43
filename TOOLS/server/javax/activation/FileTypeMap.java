/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.File;
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
/*     */ 
/*     */ public abstract class FileTypeMap
/*     */ {
/*  63 */   private static FileTypeMap defaultMap = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultFileTypeMap(FileTypeMap map) {
/*  99 */     SecurityManager security = System.getSecurityManager();
/* 100 */     if (security != null)
/*     */       
/*     */       try {
/* 103 */         security.checkSetFactory();
/* 104 */       } catch (SecurityException ex) {
/*     */ 
/*     */ 
/*     */         
/* 108 */         if (FileTypeMap.class.getClassLoader() != map.getClass().getClassLoader())
/*     */         {
/* 110 */           throw ex;
/*     */         }
/*     */       }  
/* 113 */     defaultMap = map;
/*     */   }
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
/*     */   public static FileTypeMap getDefaultFileTypeMap() {
/* 127 */     if (defaultMap == null)
/* 128 */       defaultMap = new MimetypesFileTypeMap(); 
/* 129 */     return defaultMap;
/*     */   }
/*     */   
/*     */   public abstract String getContentType(File paramFile);
/*     */   
/*     */   public abstract String getContentType(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\FileTypeMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */