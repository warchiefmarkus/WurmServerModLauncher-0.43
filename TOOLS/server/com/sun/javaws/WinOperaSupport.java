/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.deploy.util.WinRegistry;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
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
/*     */ public class WinOperaSupport
/*     */   extends OperaSupport
/*     */ {
/*     */   private static final String OPERA_SUBKEY = "Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\Opera.exe";
/*     */   private static final String OPERA_PATH = "Path";
/*     */   private static final String USER_HOME = "user.home";
/*     */   
/*     */   public boolean isInstalled() {
/*  45 */     return (getInstallPath().length() != 0);
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
/*     */   public void enableJnlp(File paramFile, boolean paramBoolean) {
/*  59 */     String str = getInstallPath();
/*     */     
/*  61 */     if (str.length() > 0) {
/*     */       
/*     */       try {
/*     */         
/*  65 */         File file1 = new File(str);
/*  66 */         File file2 = enableSystemJnlp(file1, paramFile);
/*     */         
/*  68 */         if (file2 == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  73 */           file2 = new File(file1, "Opera6.ini");
/*  74 */           if (!file2.exists()) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  79 */             file2 = new File(file1, "Opera.ini");
/*  80 */             if (!file2.exists())
/*     */             {
/*     */ 
/*     */ 
/*     */               
/*  85 */               file2 = new File(Config.getOSHome(), "Opera.ini");
/*     */             }
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  93 */         enableJnlp(null, file2, paramFile, paramBoolean);
/*     */       }
/*  95 */       catch (Exception exception) {
/*     */         
/*  97 */         Trace.ignoredException(exception);
/*     */       } 
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
/*     */   public WinOperaSupport(boolean paramBoolean) {
/* 113 */     super(paramBoolean);
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
/*     */ 
/*     */   
/*     */   private File enableSystemJnlp(File paramFile1, File paramFile2) throws IOException {
/* 134 */     OperaPreferences operaPreferences = null;
/* 135 */     File file1 = null;
/* 136 */     File file2 = null;
/*     */ 
/*     */     
/* 139 */     file1 = new File(paramFile1, "OperaDef6.ini");
/* 140 */     operaPreferences = getPreferences(file1);
/*     */     
/* 142 */     if (operaPreferences != null) {
/*     */       
/* 144 */       boolean bool = true;
/*     */ 
/*     */ 
/*     */       
/* 148 */       enableJnlp(operaPreferences, file1, paramFile2, true);
/*     */       
/* 150 */       if (operaPreferences.containsKey("System", "Multi User")) {
/*     */         
/* 152 */         String str = operaPreferences.get("System", "Multi User").trim();
/*     */ 
/*     */         
/* 155 */         str = str.substring(0, str.indexOf(' '));
/*     */ 
/*     */         
/*     */         try {
/* 159 */           int i = Integer.decode(str).intValue();
/* 160 */           if (i == 0)
/*     */           {
/* 162 */             bool = false;
/*     */             
/* 164 */             Trace.println("Multi-user support is turned off in the Opera system preference file (" + file1.getAbsolutePath() + ").", TraceLevel.BASIC);
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/* 171 */         catch (NumberFormatException numberFormatException) {
/*     */           
/* 173 */           bool = false;
/*     */           
/* 175 */           Trace.println("The Opera system preference file (" + file1.getAbsolutePath() + ") has '" + "Multi User" + "=" + str + "' in the " + "System" + " section, so multi-user " + "support is not enabled.", TraceLevel.BASIC);
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
/* 186 */       if (bool == true) {
/*     */         
/* 188 */         StringBuffer stringBuffer = new StringBuffer(512);
/*     */         
/* 190 */         stringBuffer.append(System.getProperty("user.home")).append(File.separator).append(USER_DATA_INFIX).append(File.separator).append(paramFile1.getName()).append(File.separator).append("Profile").append(File.separator).append("Opera6.ini");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 200 */         file2 = new File(stringBuffer.toString());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 206 */     return file2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getInstallPath() {
/* 217 */     String str = WinRegistry.getString(-2147483646, "Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\Opera.exe", "Path");
/*     */ 
/*     */     
/* 220 */     return (str != null) ? str : "";
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   private static final String USER_DATA_INFIX = "Application Data" + File.separator + "Opera";
/*     */   private static final String USER_DATA_POSTFIX = "Profile";
/*     */   private static final String SYSTEM_PREFERENCES = "OperaDef6.ini";
/*     */   private static final String MULTI_USER_SECTION = "System";
/*     */   private static final String MULTI_USER_KEY = "Multi User";
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\WinOperaSupport.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */