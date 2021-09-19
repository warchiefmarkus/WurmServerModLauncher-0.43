/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.config.JREInfo;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.util.GeneralUtil;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
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
/*     */ public class JnlpxArgs
/*     */ {
/*     */   private static final String ARG_JVM = "jnlpx.jvm";
/*     */   private static final String ARG_SPLASHPORT = "jnlpx.splashport";
/*     */   private static final String ARG_REMOVE = "jnlpx.remove";
/*     */   private static final String ARG_OFFLINE = "jnlpx.offline";
/*     */   private static final String ARG_HEAPSIZE = "jnlpx.heapsize";
/*     */   private static final String ARG_VMARGS = "jnlpx.vmargs";
/*     */   private static final String ARG_HOME = "jnlpx.home";
/*  44 */   private static File _currentJVMCommand = null;
/*     */   
/*  46 */   private static final String JAVAWS_JAR = Config.isDebugMode() ? "javaws_g.jar" : "javaws.jar";
/*     */   
/*  48 */   private static final String DEPLOY_JAR = Config.isDebugMode() ? "deploy_g.jar" : "deploy.jar";
/*     */ 
/*     */   
/*     */   public static int getSplashPort() {
/*     */     try {
/*  53 */       return Integer.parseInt(System.getProperty("jnlpx.splashport", "-1"));
/*  54 */     } catch (NumberFormatException numberFormatException) {
/*  55 */       return -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getVMArgs() {
/*  60 */     return System.getProperty("jnlpx.vmargs");
/*     */   }
/*     */ 
/*     */   
/*     */   static File getJVMCommand() {
/*  65 */     if (_currentJVMCommand == null) {
/*  66 */       String str = System.getProperty("jnlpx.jvm", "").trim();
/*  67 */       if (str.startsWith("X")) {
/*  68 */         str = JREInfo.getDefaultJavaPath();
/*     */       }
/*  70 */       if (str.startsWith("\"")) str = str.substring(1); 
/*  71 */       if (str.endsWith("\"")) str = str.substring(0, str.length() - 1); 
/*  72 */       _currentJVMCommand = new File(str);
/*     */     } 
/*  74 */     return _currentJVMCommand;
/*     */   }
/*     */   
/*     */   public static boolean shouldRemoveArgumentFile() {
/*  78 */     return getBooleanProperty("jnlpx.remove");
/*     */   }
/*     */   public static void setShouldRemoveArgumentFile(String paramString) {
/*  81 */     System.setProperty("jnlpx.remove", paramString);
/*     */   }
/*     */   public static boolean isOffline() {
/*  84 */     return getBooleanProperty("jnlpx.offline");
/*     */   }
/*     */   public static void SetIsOffline() {
/*  87 */     System.setProperty("jnlpx.offline", "true");
/*     */   } public static String getHeapSize() {
/*  89 */     return System.getProperty("jnlpx.heapsize");
/*     */   }
/*     */   public static long getInitialHeapSize() {
/*  92 */     String str1 = getHeapSize();
/*  93 */     if (str1 == null) return -1L; 
/*  94 */     String str2 = str1.substring(str1.lastIndexOf('=') + 1);
/*  95 */     String str3 = str2.substring(0, str2.lastIndexOf(','));
/*  96 */     return GeneralUtil.heapValToLong(str3);
/*     */   }
/*     */   
/*     */   public static long getMaxHeapSize() {
/* 100 */     String str1 = getHeapSize();
/* 101 */     if (str1 == null) return -1L; 
/* 102 */     String str2 = str1.substring(str1.lastIndexOf('=') + 1);
/* 103 */     String str3 = str2.substring(str2.lastIndexOf(',') + 1, str2.length());
/* 104 */     return GeneralUtil.heapValToLong(str3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isCurrentRunningJREHeap(long paramLong1, long paramLong2) {
/* 109 */     long l1 = getInitialHeapSize();
/* 110 */     long l2 = getMaxHeapSize();
/*     */     
/* 112 */     Trace.println("isCurrentRunningJREHeap: passed args: " + paramLong1 + ", " + paramLong2, TraceLevel.BASIC);
/* 113 */     Trace.println("JnlpxArgs is " + l1 + ", " + l2, TraceLevel.BASIC);
/*     */     
/* 115 */     return (l1 == paramLong1 && l2 == paramLong2);
/*     */   }
/*     */   
/*     */   public static boolean isAuxArgsMatch(Properties paramProperties, String paramString) {
/* 119 */     String[] arrayOfString = Config.getSecureProperties();
/* 120 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 121 */       String str = arrayOfString[b];
/* 122 */       if (paramProperties.containsKey(str)) {
/* 123 */         Object object = paramProperties.get(str);
/* 124 */         if (object != null && !object.equals(System.getProperty(str))) {
/* 125 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 129 */     if (paramString != null && getVMArgs() == null) {
/* 130 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString);
/* 131 */       while (stringTokenizer.hasMoreTokens()) {
/* 132 */         String str = stringTokenizer.nextToken();
/* 133 */         if (Config.isSecureVmArg(str)) {
/* 134 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean heapSizesValid(long paramLong1, long paramLong2) {
/* 143 */     return (paramLong1 != -1L || paramLong2 != -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getArgumentList(String paramString1, long paramLong1, long paramLong2, Properties paramProperties, String paramString2) {
/* 152 */     String str1 = "-Djnlpx.heapsize=NULL,NULL";
/* 153 */     String str2 = "";
/* 154 */     String str3 = "";
/*     */     
/* 156 */     if (heapSizesValid(paramLong1, paramLong2)) {
/*     */       
/* 158 */       str1 = "-Djnlpx.heapsize=" + paramLong1 + "," + paramLong2;
/* 159 */       if (paramLong1 > 0L) str2 = "-Xms" + paramLong1; 
/* 160 */       if (paramLong2 > 0L) str3 = "-Xmx" + paramLong2;
/*     */     
/*     */     } 
/* 163 */     String str4 = getDesiredVMArgs(getVMArgs(), paramString2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     String[] arrayOfString1 = { "-Xbootclasspath/a:" + Config.getJavaHome() + File.separator + "lib" + File.separator + JAVAWS_JAR + File.pathSeparator + Config.getJavaHome() + File.separator + "lib" + File.separator + DEPLOY_JAR, "-classpath", File.pathSeparator + Config.getJavaHome() + File.separator + "lib" + File.separator + DEPLOY_JAR, str2, str3, (str4 != null) ? ("-Djnlpx.vmargs=" + str4) : "", "-Djnlpx.jvm=" + paramString1, "-Djnlpx.splashport=" + getSplashPort(), "-Djnlpx.home=" + Config.getJavaHome() + File.separator + "bin", "-Djnlpx.remove=" + (shouldRemoveArgumentFile() ? "true" : "false"), "-Djnlpx.offline=" + (isOffline() ? "true" : "false"), str1, "-Djava.security.policy=" + getPolicyURLString(), "-DtrustProxy=true", "-Xverify:remote", useJCOV(), useBootClassPath(), useJpiProfile(), useDebugMode(), useDebugVMMode(), "com.sun.javaws.Main", setTCKHarnessOption(), useLogToHost() };
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
/* 208 */     byte b1 = 0;
/* 209 */     for (byte b2 = 0; b2 < arrayOfString1.length; b2++) {
/* 210 */       if (!arrayOfString1[b2].equals("")) b1++;
/*     */     
/*     */     } 
/* 213 */     String[] arrayOfString2 = getVMArgList(paramProperties, paramString2);
/* 214 */     int i = arrayOfString2.length;
/*     */     
/* 216 */     String[] arrayOfString3 = new String[b1 + i];
/* 217 */     byte b3 = 0;
/* 218 */     for (b3 = 0; b3 < i; b3++) {
/* 219 */       arrayOfString3[b3] = arrayOfString2[b3];
/*     */     }
/*     */     
/* 222 */     for (byte b4 = 0; b4 < arrayOfString1.length; b4++) {
/* 223 */       if (!arrayOfString1[b4].equals("")) arrayOfString3[b3++] = arrayOfString1[b4];
/*     */     
/*     */     } 
/* 226 */     return arrayOfString3;
/*     */   }
/*     */   
/*     */   static String getPolicyURLString() {
/* 230 */     String str1 = Config.getJavaHome() + File.separator + "lib" + File.separator + "security" + File.separator + "javaws.policy";
/*     */     
/* 232 */     String str2 = str1;
/*     */     try {
/* 234 */       URL uRL = new URL("file", "", str1);
/* 235 */       str2 = uRL.toString();
/* 236 */     } catch (Exception exception) {}
/*     */     
/* 238 */     return str2;
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
/*     */   private static String getDesiredVMArgs(String paramString1, String paramString2) {
/* 252 */     if (paramString1 == null && 
/* 253 */       paramString2 != null) {
/* 254 */       String str = "";
/* 255 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString2, " \t\n\r\f\"");
/* 256 */       while (stringTokenizer.hasMoreTokens()) {
/* 257 */         String str1 = stringTokenizer.nextToken();
/* 258 */         if (Config.isSecureVmArg(str1)) {
/* 259 */           if (str.length() == 0) {
/* 260 */             str = str1; continue;
/*     */           } 
/* 262 */           str = str + " " + str1;
/*     */         } 
/*     */       } 
/*     */       
/* 266 */       if (str.length() > 0) {
/* 267 */         return str;
/*     */       }
/*     */     } 
/*     */     
/* 271 */     return paramString1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String[] getVMArgList(Properties paramProperties, String paramString) {
/* 276 */     Vector vector = new Vector();
/* 277 */     String str = null;
/*     */     
/* 279 */     if ((str = getVMArgs()) != null) {
/* 280 */       StringTokenizer stringTokenizer = new StringTokenizer(str, " \t\n\r\f\"");
/* 281 */       while (stringTokenizer.hasMoreTokens()) {
/* 282 */         vector.add(stringTokenizer.nextToken());
/*     */       }
/*     */     } 
/*     */     
/* 286 */     if (paramString != null) {
/* 287 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString, " \t\n\r\f\"");
/* 288 */       while (stringTokenizer.hasMoreTokens()) {
/* 289 */         String str1 = stringTokenizer.nextToken();
/* 290 */         if (Config.isSecureVmArg(str1) && 
/* 291 */           !vector.contains(str1)) {
/* 292 */           vector.add(str1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 298 */     String[] arrayOfString1 = Config.getSecureProperties();
/* 299 */     for (byte b1 = 0; b1 < arrayOfString1.length; b1++) {
/* 300 */       String str1 = arrayOfString1[b1];
/* 301 */       if (paramProperties.containsKey(str1)) {
/* 302 */         String str2 = "-D" + str1 + "=" + paramProperties.get(str1);
/* 303 */         if (!vector.contains(str2)) {
/* 304 */           vector.add(str2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 309 */     String[] arrayOfString2 = new String[vector.size()];
/* 310 */     for (byte b2 = 0; b2 < vector.size(); b2++) {
/* 311 */       arrayOfString2[b2] = new String(vector.elementAt(b2));
/*     */     }
/* 313 */     return arrayOfString2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String useLogToHost() {
/* 318 */     if (Globals.LogToHost != null) {
/* 319 */       return "-XX:LogToHost=" + Globals.LogToHost;
/*     */     }
/* 321 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String setTCKHarnessOption() {
/* 326 */     if (Globals.TCKHarnessRun == true) {
/* 327 */       return "-XX:TCKHarnessRun=true";
/*     */     }
/* 329 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String useBootClassPath() {
/* 334 */     if (Globals.BootClassPath.equals("NONE")) {
/* 335 */       return "";
/*     */     }
/* 337 */     return "-Xbootclasspath" + Globals.BootClassPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String useJpiProfile() {
/* 342 */     String str = System.getProperty("javaplugin.user.profile");
/* 343 */     if (str != null) {
/* 344 */       return "-Djavaplugin.user.profile=" + str;
/*     */     }
/* 346 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String useJCOV() {
/* 351 */     if (Globals.JCOV.equals("NONE")) {
/* 352 */       return "";
/*     */     }
/* 354 */     return "-Xrunjcov:file=" + Globals.JCOV;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String useDebugMode() {
/* 359 */     if (Config.isDebugMode()) {
/* 360 */       return "-Ddeploy.debugMode=true";
/*     */     }
/* 362 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String useDebugVMMode() {
/* 367 */     if (Config.isDebugVMMode()) {
/* 368 */       return "-Ddeploy.useDebugJavaVM=true";
/*     */     }
/* 370 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeArgumentFile(String[] paramArrayOfString) {
/* 378 */     if (shouldRemoveArgumentFile() && paramArrayOfString != null && paramArrayOfString.length > 0) {
/* 379 */       (new File(paramArrayOfString[0])).delete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void verify() {
/* 387 */     Trace.println("Java part started", TraceLevel.BASIC);
/* 388 */     Trace.println("jnlpx.jvm: " + getJVMCommand(), TraceLevel.BASIC);
/* 389 */     Trace.println("jnlpx.splashport: " + getSplashPort(), TraceLevel.BASIC);
/* 390 */     Trace.println("jnlpx.remove: " + shouldRemoveArgumentFile(), TraceLevel.BASIC);
/* 391 */     Trace.println("jnlpx.heapsize: " + getHeapSize(), TraceLevel.BASIC);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean getBooleanProperty(String paramString) {
/* 396 */     String str = System.getProperty(paramString, "false");
/* 397 */     return (str != null && str.equals("true"));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\JnlpxArgs.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */