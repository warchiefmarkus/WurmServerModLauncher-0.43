/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
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
/*     */ public class Globals
/*     */ {
/*     */   private static final String JAVAWS_NAME = "javaws-1.5.0_04";
/*     */   public static final String JAVAWS_VERSION = "1.5.0_04";
/*     */   private static final String JNLP_VERSION = "1.5";
/*     */   private static final String WIN_ID = "Windows";
/*     */   private static boolean _isOffline = false;
/*     */   private static boolean _isImportMode = false;
/*     */   private static boolean _isSilentMode = false;
/*     */   private static boolean _isInstallMode = false;
/*     */   private static boolean _isSystemCache = false;
/*     */   private static boolean _isSecureMode = false;
/*  57 */   private static String _codebaseOverride = null;
/*  58 */   private static String[] _applicationArgs = null;
/*     */   private static boolean _createShortcut = false;
/*     */   private static boolean _createAssoc = false;
/*  61 */   private static URL _codebase = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_LOGHOST = "localhost:8205";
/*     */ 
/*     */   
/*  68 */   public static String BootClassPath = "NONE";
/*  69 */   public static String JCOV = "NONE";
/*     */   
/*     */   public static boolean TraceDefault = true;
/*     */   
/*     */   public static boolean TraceBasic = false;
/*     */   
/*     */   public static boolean TraceNetwork = false;
/*     */   
/*     */   public static boolean TraceSecurity = false;
/*     */   
/*     */   public static boolean TraceCache = false;
/*     */   
/*     */   public static boolean TraceExtensions = false;
/*     */   
/*     */   public static boolean TraceTemp = false;
/*  84 */   public static String LogToHost = null;
/*     */ 
/*     */   
/*     */   public static boolean SupportJREinstallation = true;
/*     */ 
/*     */   
/*     */   public static boolean OverrideSystemClassLoader = true;
/*     */ 
/*     */   
/*     */   public static boolean TCKHarnessRun = false;
/*     */   
/*     */   public static boolean TCKResponse = false;
/*     */   
/*     */   public static final String JAVA_STARTED = "Java Started";
/*     */   
/*     */   public static final String JNLP_LAUNCHING = "JNLP Launching";
/*     */   
/*     */   public static final String NEW_VM_STARTING = "JVM Starting";
/*     */   
/*     */   public static final String JAVA_SHUTDOWN = "JVM Shutdown";
/*     */   
/*     */   public static final String CACHE_CLEAR_OK = "Cache Clear Success";
/*     */   
/*     */   public static final String CACHE_CLEAR_FAILED = "Cache Clear Failed";
/*     */   
/* 109 */   private static final Locale defaultLocale = Locale.getDefault();
/* 110 */   private static final String defaultLocaleString = getDefaultLocale().toString();
/*     */   
/* 112 */   public static String getDefaultLocaleString() { return defaultLocaleString; } public static Locale getDefaultLocale() {
/* 113 */     return defaultLocale;
/*     */   }
/*     */   
/*     */   public static boolean isOffline() {
/* 117 */     return _isOffline;
/* 118 */   } public static boolean createShortcut() { return _createShortcut; }
/* 119 */   public static boolean createAssoc() { return _createAssoc; }
/* 120 */   public static boolean isImportMode() { return _isImportMode; } public static boolean isInstallMode() {
/* 121 */     return _isInstallMode;
/*     */   }
/*     */   public static boolean isSilentMode() {
/* 124 */     return (_isSilentMode && (_isImportMode || _isInstallMode));
/*     */   }
/* 126 */   public static boolean isSystemCache() { return _isSystemCache; } public static boolean isSecureMode() {
/* 127 */     return _isSecureMode;
/*     */   } public static String getCodebaseOverride() {
/* 129 */     return _codebaseOverride;
/*     */   }
/* 131 */   public static String[] getApplicationArgs() { return _applicationArgs; } public static URL getCodebase() {
/* 132 */     return _codebase;
/*     */   }
/* 134 */   public static void setCodebase(URL paramURL) { _codebase = paramURL; }
/* 135 */   public static void setCreateShortcut(boolean paramBoolean) { _createShortcut = paramBoolean; }
/* 136 */   public static void setCreateAssoc(boolean paramBoolean) { _createAssoc = paramBoolean; }
/* 137 */   public static void setOffline(boolean paramBoolean) { _isOffline = paramBoolean; }
/* 138 */   public static void setImportMode(boolean paramBoolean) { _isImportMode = paramBoolean; }
/* 139 */   public static void setSilentMode(boolean paramBoolean) { _isSilentMode = paramBoolean; }
/* 140 */   public static void setInstallMode(boolean paramBoolean) { _isInstallMode = paramBoolean; }
/* 141 */   public static void setSystemCache(boolean paramBoolean) { _isSystemCache = paramBoolean; } public static void setSecureMode(boolean paramBoolean) {
/* 142 */     _isSecureMode = paramBoolean;
/*     */   } public static void setCodebaseOverride(String paramString) {
/* 144 */     if (paramString != null && !paramString.endsWith(File.separator)) {
/* 145 */       paramString = paramString + File.separator;
/*     */     }
/* 147 */     _codebaseOverride = paramString;
/*     */   } public static void setApplicationArgs(String[] paramArrayOfString) {
/* 149 */     _applicationArgs = paramArrayOfString;
/*     */   }
/*     */   
/*     */   public static boolean isHeadless() {
/* 153 */     if (!isJavaVersionAtLeast14())
/*     */     {
/* 155 */       return false;
/*     */     }
/* 157 */     return GraphicsEnvironment.isHeadless();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean havePack200() {
/* 162 */     return isJavaVersionAtLeast15();
/*     */   }
/*     */   
/* 165 */   private static final String _javaVersionProperty = System.getProperty("java.version");
/*     */ 
/*     */   
/* 168 */   private static final boolean _atLeast14 = (!_javaVersionProperty.startsWith("1.2") && !_javaVersionProperty.startsWith("1.3"));
/*     */ 
/*     */ 
/*     */   
/* 172 */   private static final boolean _atLeast15 = (_atLeast14 && !_javaVersionProperty.startsWith("1.4"));
/*     */ 
/*     */   
/*     */   public static boolean isJavaVersionAtLeast15() {
/* 176 */     return _atLeast15;
/*     */   }
/*     */   
/*     */   public static boolean isJavaVersionAtLeast14() {
/* 180 */     return _atLeast14;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getBuildID() {
/* 187 */     String str = null;
/* 188 */     InputStream inputStream = Globals.class.getResourceAsStream("/build.id");
/* 189 */     if (inputStream != null) {
/* 190 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
/*     */       try {
/* 192 */         str = bufferedReader.readLine();
/* 193 */       } catch (IOException iOException) {}
/*     */     } 
/* 195 */     return (str == null || str.length() == 0) ? "<internal>" : str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaVersion() {
/* 202 */     return _javaVersionProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getComponentName() {
/* 208 */     return "javaws-1.5.0_04";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getUserAgent() {
/* 215 */     return "JNLP/1.5 javaws/1.5.0_04 (" + getBuildID() + ")" + " J2SE/" + System.getProperty("java.version");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] parseOptions(String[] paramArrayOfString) {
/* 223 */     readOptionFile();
/*     */     
/* 225 */     ArrayList arrayList = new ArrayList();
/*     */     
/* 227 */     byte b = 0;
/* 228 */     boolean bool = false;
/* 229 */     while (b < paramArrayOfString.length) {
/* 230 */       String str = paramArrayOfString[b++];
/* 231 */       if (str.startsWith("-XX:") && !bool) {
/*     */         
/* 233 */         parseOption(str.substring(4), false);
/*     */       } else {
/* 235 */         arrayList.add(str);
/*     */       } 
/*     */ 
/*     */       
/* 239 */       if (!str.startsWith("-")) bool = true;
/*     */     
/*     */     } 
/* 242 */     setTCKOptions();
/* 243 */     String[] arrayOfString = new String[arrayList.size()];
/* 244 */     return arrayList.<String>toArray(arrayOfString);
/*     */   }
/*     */   
/*     */   public static void getDebugOptionsFromProperties(Properties paramProperties) {
/* 248 */     byte b = 0;
/*     */     while (true) {
/* 250 */       String str = paramProperties.getProperty("javaws.debug." + b);
/* 251 */       if (str == null) {
/*     */         return;
/*     */       }
/* 254 */       parseOption(str, true);
/* 255 */       b++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setTCKOptions() {
/* 261 */     if (TCKHarnessRun == true && 
/* 262 */       LogToHost == null) {
/* 263 */       Trace.println("Warning: LogHost = null");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void parseOption(String paramString, boolean paramBoolean) {
/* 268 */     String str1 = null;
/* 269 */     String str2 = null;
/*     */     
/* 271 */     int i = paramString.indexOf('=');
/* 272 */     if (i == -1) {
/* 273 */       str1 = paramString;
/* 274 */       str2 = null;
/*     */     } else {
/* 276 */       str1 = paramString.substring(0, i);
/* 277 */       str2 = paramString.substring(i + 1);
/*     */     } 
/*     */ 
/*     */     
/* 281 */     if (str1.length() > 0 && (str1.startsWith("-") || str1.startsWith("+"))) {
/* 282 */       str1 = str1.substring(1);
/* 283 */       str2 = paramString.startsWith("+") ? "true" : "false";
/*     */     } 
/*     */ 
/*     */     
/* 287 */     if (paramBoolean && !str1.startsWith("x") && !str1.startsWith("Trace")) {
/* 288 */       str1 = null;
/*     */     }
/*     */     
/* 291 */     if (str1 != null && setOption(str1, str2)) {
/* 292 */       System.out.println("# Option: " + str1 + "=" + str2);
/*     */     } else {
/* 294 */       System.out.println("# Ignoring option: " + paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean setOption(String paramString1, String paramString2) {
/* 301 */     Class clazz = (new String()).getClass();
/* 302 */     boolean bool = true;
/*     */ 
/*     */     
/*     */     try {
/* 306 */       Field field = (new Globals()).getClass().getDeclaredField(paramString1);
/* 307 */       if ((field.getModifiers() & 0x8) == 0) return false;
/*     */       
/* 309 */       Class clazz1 = field.getType();
/* 310 */       if (clazz1 == clazz) {
/* 311 */         field.set(null, paramString2);
/* 312 */       } else if (clazz1 == boolean.class) {
/* 313 */         field.setBoolean(null, Boolean.valueOf(paramString2).booleanValue());
/* 314 */       } else if (clazz1 == int.class) {
/* 315 */         field.setInt(null, Integer.parseInt(paramString2));
/* 316 */       } else if (clazz1 == float.class) {
/* 317 */         field.setFloat(null, Float.parseFloat(paramString2));
/* 318 */       } else if (clazz1 == double.class) {
/* 319 */         field.setDouble(null, Double.parseDouble(paramString2));
/* 320 */       } else if (clazz1 == long.class) {
/* 321 */         field.setLong(null, Long.parseLong(paramString2));
/*     */       } else {
/*     */         
/* 324 */         return false;
/*     */       } 
/* 326 */     } catch (IllegalAccessException illegalAccessException) {
/* 327 */       return false;
/* 328 */     } catch (NoSuchFieldException noSuchFieldException) {
/* 329 */       return false;
/*     */     } 
/* 331 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void readOptionFile() {
/* 336 */     FileInputStream fileInputStream = null;
/*     */     try {
/* 338 */       fileInputStream = new FileInputStream(".javawsrc");
/* 339 */     } catch (FileNotFoundException fileNotFoundException) {
/*     */       try {
/* 341 */         fileInputStream = new FileInputStream(System.getProperty("user.home") + File.separator + ".javawsrc");
/*     */       }
/* 343 */       catch (FileNotFoundException fileNotFoundException1) {
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     try {
/* 348 */       Properties properties = new Properties();
/* 349 */       properties.load(fileInputStream);
/*     */ 
/*     */       
/* 352 */       Enumeration enumeration = properties.propertyNames();
/* 353 */       if (enumeration.hasMoreElements()) {
/* 354 */         System.out.println("\nSetting options from .javawsrc file:");
/*     */       }
/* 356 */       while (enumeration.hasMoreElements()) {
/* 357 */         String str1 = (String)enumeration.nextElement();
/* 358 */         String str2 = properties.getProperty(str1);
/* 359 */         parseOption(str1 + "=" + str2, false);
/*     */       } 
/* 361 */     } catch (IOException iOException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\Globals.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */