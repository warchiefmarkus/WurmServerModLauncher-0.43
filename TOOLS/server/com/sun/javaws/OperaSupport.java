/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.text.MessageFormat;
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
/*     */ public abstract class OperaSupport
/*     */ {
/*     */   protected static final String OPERA_PREFERENCES = "Opera.ini";
/*     */   protected static final String OPERA_6_PREFERENCES = "Opera6.ini";
/*     */   protected boolean useDefault;
/*     */   private static final String INSTALL_SECTION = "INSTALL";
/*     */   private static final String VERSION_KEY = "OVER";
/*     */   private static final float OPERA_2_PREFERENCE_VERSION = 5.0F;
/*     */   private static final float LAST_TESTED_OPERA_PREFERENCE_VERSION = 7.11F;
/*     */   private static final String FILE_TYPES_SECTION_INFO = "File Types Section Info";
/*     */   private static final String FILE_TYPES_VERSION_KEY = "Version";
/*     */   private static final String FILE_TYPES = "File Types";
/*     */   private static final String FILE_TYPES_KEY = "application/x-java-jnlp-file";
/*     */   private static final String FILE_TYPES_VALUE = "{0},{1},,,jnlp,|";
/*     */   private static final String EXPLICIT_PATH = "3";
/*     */   private static final String IMPLICIT_PATH = "4";
/*     */   private static final String FILE_TYPES_EXTENSION = "File Types Extension";
/*     */   private static final String FILE_TYPES_EXTENSION_KEY = "application/x-java-jnlp-file";
/*     */   private static final String FILE_TYPES_EXTENSION_VALUE = ",0";
/*     */   
/*     */   public abstract boolean isInstalled();
/*     */   
/*     */   public abstract void enableJnlp(File paramFile, boolean paramBoolean);
/*     */   
/*     */   protected void enableJnlp(OperaPreferences paramOperaPreferences, File paramFile1, File paramFile2, boolean paramBoolean) throws IOException {
/*  88 */     if (paramOperaPreferences == null)
/*     */     {
/*  90 */       paramOperaPreferences = getPreferences(paramFile1);
/*     */     }
/*     */ 
/*     */     
/*  94 */     if (paramOperaPreferences != null) {
/*     */       
/*  96 */       float f = 5.0F;
/*  97 */       String str = paramOperaPreferences.get("INSTALL", "OVER");
/*     */       
/*  99 */       if (str != null) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 104 */           f = Float.parseFloat(str.trim());
/*     */         }
/* 106 */         catch (NumberFormatException numberFormatException) {
/*     */           
/* 108 */           Trace.println("Unable to determine Opera version from the preference file; assuming 5.0 or higher.", TraceLevel.BASIC);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       if (f < 5.0F) {
/*     */ 
/*     */ 
/*     */         
/* 122 */         paramOperaPreferences.put("File Types Section Info", "Version", "1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 129 */       else if (!paramOperaPreferences.containsKey("File Types Section Info", "Version")) {
/*     */         
/* 131 */         if (f > 7.11F)
/*     */         {
/* 133 */           Trace.println("Setting '[File Types Section Info]Version=2' in the Opera preference file.", TraceLevel.BASIC);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 140 */         paramOperaPreferences.put("File Types Section Info", "Version", "2");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       if (paramBoolean == true || !paramOperaPreferences.containsKey("File Types", "application/x-java-jnlp-file")) {
/*     */ 
/*     */         
/* 150 */         Object[] arrayOfObject = { null, null };
/*     */         
/* 152 */         if (f < 5.0F || !this.useDefault) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 157 */           arrayOfObject[0] = "3";
/*     */           
/*     */           try {
/* 160 */             arrayOfObject[1] = paramFile2.getCanonicalPath();
/*     */           }
/* 162 */           catch (IOException iOException) {
/*     */             
/* 164 */             arrayOfObject[1] = paramFile2.getAbsolutePath();
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 171 */           arrayOfObject[0] = "4";
/* 172 */           arrayOfObject[1] = "";
/*     */         } 
/*     */         
/* 175 */         paramOperaPreferences.put("File Types", "application/x-java-jnlp-file", MessageFormat.format("{0},{1},,,jnlp,|", arrayOfObject));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 182 */       if (f >= 5.0F)
/*     */       {
/* 184 */         if (!paramOperaPreferences.containsKey("File Types Extension", "application/x-java-jnlp-file"))
/*     */         {
/* 186 */           paramOperaPreferences.put("File Types Extension", "application/x-java-jnlp-file", ",0");
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 197 */       paramOperaPreferences.store(new FileOutputStream(paramFile1));
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OperaPreferences getPreferences(File paramFile) throws IOException {
/* 217 */     OperaPreferences operaPreferences = null;
/*     */     
/* 219 */     if (paramFile.exists()) {
/*     */       
/* 221 */       if (paramFile.canRead())
/*     */       {
/* 223 */         if (paramFile.canWrite())
/*     */         {
/* 225 */           operaPreferences = new OperaPreferences();
/* 226 */           operaPreferences.load(new FileInputStream(paramFile));
/*     */         }
/*     */         else
/*     */         {
/* 230 */           Trace.println("No write access to the Opera preference file (" + paramFile.getAbsolutePath() + ").", TraceLevel.BASIC);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 237 */         Trace.println("No read access to the Opera preference file (" + paramFile.getAbsolutePath() + ").", TraceLevel.BASIC);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 244 */       Trace.println("The Opera preference file (" + paramFile.getAbsolutePath() + ") does not exist.", TraceLevel.BASIC);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 249 */     return operaPreferences;
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
/*     */   protected OperaSupport(boolean paramBoolean) {
/* 262 */     this.useDefault = paramBoolean;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\OperaSupport.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */