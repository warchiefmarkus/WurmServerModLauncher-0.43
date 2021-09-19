/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.config.JREInfo;
/*     */ import com.sun.deploy.util.DialogFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class JPDA
/*     */ {
/*     */   private static final int NAPN = -1;
/*     */   public static final int JWS = 1;
/*     */   public static final int JWSJNL = 2;
/*     */   public static final int JNL = 3;
/*  33 */   private static String JWS_str = "1";
/*  34 */   private static String JWSJNL_str = "2";
/*  35 */   private static String JNL_str = "3";
/*     */ 
/*     */   
/*  38 */   private static String dbgNotificationTitle = "JPDA Notification";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   private static JPDA o_envCurrentJRE = null;
/*  45 */   private static JPDA o_envNextJRE = null;
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static String s_envCurrentJRE = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static int _debuggeeType = 0;
/*     */   private static boolean _jpdaConfigIsFromCmdLine = false;
/*  57 */   private static String _portsList = null;
/*  58 */   private static int[] _portsPool = null;
/*  59 */   private int _selectedPort = -1;
/*     */   private boolean _portIsAutoSelected = false;
/*  61 */   private String _excludedportsList = null;
/*  62 */   private int[] _excludedportsPool = null;
/*  63 */   private String _jreProductVersion = null;
/*  64 */   private int _jreNestingLevel = -1;
/*     */   private static boolean _jreUsesDashClassic = false;
/*  66 */   private String _javaMainArgsList = null;
/*     */   
/*     */   private static boolean _nextJreRunsInJpdaMode = false;
/*     */ 
/*     */   
/*     */   public static int getDebuggeeType() {
/*  72 */     return _debuggeeType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setup() {
/*  78 */     s_envCurrentJRE = getProperty("jnlpx.jpda.env");
/*  79 */     o_envCurrentJRE = decodeJpdaEnv(s_envCurrentJRE);
/*     */ 
/*     */ 
/*     */     
/*  83 */     if (getProperty("jpda.notification") != null) {
/*  84 */       showJpdaNotificationWindow(o_envCurrentJRE);
/*  85 */       Main.systemExit(0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JPDA decodeJpdaEnv(String paramString) {
/* 109 */     if (paramString == null || paramString.equals("")) {
/* 110 */       return null;
/*     */     }
/*     */     
/* 113 */     JPDA jPDA = new JPDA();
/*     */ 
/*     */     
/* 116 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, "&");
/* 117 */     int i = stringTokenizer.countTokens();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     boolean[] arrayOfBoolean = new boolean[i]; byte b;
/* 123 */     for (b = 0; b < i; ) { arrayOfBoolean[b] = true; b++; }
/*     */     
/*     */     try {
/* 126 */       while (stringTokenizer.hasMoreTokens())
/*     */       {
/* 128 */         String[] arrayOfString = tokenizeJpdaEnvEntry(stringTokenizer.nextToken(), "=");
/*     */         
/* 130 */         if (arrayOfBoolean[0] && arrayOfString[0].equals("debuggeeType")) {
/* 131 */           arrayOfBoolean[0] = false;
/* 132 */           if (arrayOfString[1].equals(JWS_str)) {
/* 133 */             _debuggeeType = 1;
/*     */             continue;
/*     */           } 
/* 136 */           if (arrayOfString[1].equals(JWSJNL_str)) {
/* 137 */             _debuggeeType = 2;
/*     */             continue;
/*     */           } 
/* 140 */           if (arrayOfString[1].equals(JNL_str)) {
/* 141 */             _debuggeeType = 3;
/*     */           }
/*     */           continue;
/*     */         } 
/* 145 */         if (arrayOfBoolean[1] && arrayOfString[0].equals("jpdaConfigIsFromCmdLine")) {
/* 146 */           arrayOfBoolean[1] = false;
/* 147 */           if (arrayOfString[1].equals("1")) {
/* 148 */             _jpdaConfigIsFromCmdLine = true;
/*     */           }
/*     */           continue;
/*     */         } 
/* 152 */         if (arrayOfBoolean[2] && arrayOfString[0].equals("portsList")) {
/* 153 */           arrayOfBoolean[2] = false;
/* 154 */           _portsList = arrayOfString[1];
/* 155 */           if (_portsList.equals("NONE")) {
/*     */             continue;
/*     */           }
/* 158 */           String[] arrayOfString1 = tokenizeJpdaEnvEntry(_portsList, ",");
/* 159 */           _portsPool = new int[arrayOfString1.length];
/* 160 */           for (b = 0; b < arrayOfString1.length; b++) {
/* 161 */             _portsPool[b] = string2Int(arrayOfString1[b]);
/*     */           }
/*     */           continue;
/*     */         } 
/* 165 */         if (arrayOfBoolean[3] && arrayOfString[0].equals("selectedPort")) {
/* 166 */           arrayOfBoolean[3] = false;
/* 167 */           jPDA._selectedPort = string2Int(arrayOfString[1]);
/*     */           continue;
/*     */         } 
/* 170 */         if (arrayOfBoolean[4] && arrayOfString[0].equals("portIsAutoSelected")) {
/* 171 */           arrayOfBoolean[4] = false;
/* 172 */           if (arrayOfString[1].equals("1")) {
/* 173 */             jPDA._portIsAutoSelected = true;
/*     */           }
/*     */           continue;
/*     */         } 
/* 177 */         if (arrayOfBoolean[5] && arrayOfString[0].equals("excludedportsList")) {
/* 178 */           arrayOfBoolean[5] = false;
/* 179 */           jPDA._excludedportsList = arrayOfString[1];
/* 180 */           if (jPDA._excludedportsList.equals("NONE")) {
/*     */             continue;
/*     */           }
/* 183 */           String[] arrayOfString1 = tokenizeJpdaEnvEntry(jPDA._excludedportsList, ",");
/* 184 */           jPDA._excludedportsPool = new int[arrayOfString1.length];
/*     */           
/* 186 */           for (b = 0; b < arrayOfString1.length; b++) {
/* 187 */             jPDA._excludedportsPool[b] = string2Int(arrayOfString1[b]);
/*     */           }
/*     */           continue;
/*     */         } 
/* 191 */         if (arrayOfBoolean[6] && arrayOfString[0].equals("jreProductVersion")) {
/* 192 */           arrayOfBoolean[6] = false;
/* 193 */           jPDA._jreProductVersion = arrayOfString[1];
/*     */           continue;
/*     */         } 
/* 196 */         if (arrayOfBoolean[7] && arrayOfString[0].equals("jreNestingLevel")) {
/* 197 */           arrayOfBoolean[7] = false;
/* 198 */           jPDA._jreNestingLevel = string2Int(arrayOfString[1]);
/*     */           
/*     */           continue;
/*     */         } 
/* 202 */         if (arrayOfBoolean[8] && arrayOfString[0].equals("jreUsesDashClassic")) {
/* 203 */           arrayOfBoolean[8] = false;
/* 204 */           if (arrayOfString[1].equals("1")) {
/* 205 */             _jreUsesDashClassic = true;
/*     */           }
/*     */           continue;
/*     */         } 
/* 209 */         if (arrayOfBoolean[9] && arrayOfString[0].equals("javaMainArgsList")) {
/* 210 */           arrayOfBoolean[9] = false;
/* 211 */           jPDA._javaMainArgsList = arrayOfString[1];
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 220 */     catch (NoSuchElementException noSuchElementException) {
/* 221 */       return null;
/*     */     } 
/* 223 */     return jPDA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeJpdaEnv(JPDA paramJPDA) {
/* 230 */     if (paramJPDA == null) return "-Djnlpx.jpda.env"; 
/* 231 */     return "-Djnlpx.jpda.env=debuggeeType=" + _debuggeeType + "&jpdaConfigIsFromCmdLine=" + (_jpdaConfigIsFromCmdLine ? "1" : "0") + "&portsList=" + _portsList + "&selectedPort=" + paramJPDA._selectedPort + "&portIsAutoSelected=" + (paramJPDA._portIsAutoSelected ? "1" : "0") + "&excludedportsList=" + paramJPDA._excludedportsList + "&jreProductVersion=" + paramJPDA._jreProductVersion + "&jreNestingLevel=" + paramJPDA._jreNestingLevel + "&jreUsesDashClassic=" + (_jreUsesDashClassic ? "1" : "0") + "&javaMainArgsList=" + paramJPDA._javaMainArgsList;
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
/*     */   private static void setJpdaEnvForNextJRE(boolean paramBoolean1, boolean paramBoolean2, String[] paramArrayOfString, JREInfo paramJREInfo) {
/* 293 */     if (_debuggeeType == 0 || _debuggeeType == 1) {
/*     */ 
/*     */       
/* 296 */       o_envNextJRE = o_envCurrentJRE;
/* 297 */       _nextJreRunsInJpdaMode = false;
/*     */       
/*     */       return;
/*     */     } 
/* 301 */     JPDA jPDA1 = o_envCurrentJRE;
/* 302 */     JPDA jPDA2 = new JPDA();
/*     */ 
/*     */ 
/*     */     
/* 306 */     jPDA2._jreProductVersion = paramJREInfo.getProduct();
/* 307 */     jPDA2._jreNestingLevel = 1 + jPDA1._jreNestingLevel;
/* 308 */     jPDA2._javaMainArgsList = jPDA1._javaMainArgsList;
/* 309 */     if (paramArrayOfString.length > 0)
/* 310 */       jPDA2._javaMainArgsList = paramArrayOfString[0]; 
/*     */     byte b;
/* 312 */     for (b = 1; b < paramArrayOfString.length; b++) {
/* 313 */       jPDA2._javaMainArgsList += "," + paramArrayOfString[b];
/*     */     }
/*     */     
/* 316 */     _nextJreRunsInJpdaMode = true;
/*     */ 
/*     */ 
/*     */     
/* 320 */     if (_debuggeeType == 3) {
/* 321 */       jPDA2._selectedPort = jPDA1._selectedPort;
/* 322 */       jPDA2._portIsAutoSelected = jPDA1._portIsAutoSelected;
/* 323 */       jPDA2._excludedportsList = jPDA1._excludedportsList;
/* 324 */       jPDA2._excludedportsPool = jPDA1._excludedportsPool;
/* 325 */       o_envNextJRE = jPDA2;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 331 */     if (paramBoolean1) {
/* 332 */       if (jPDA1._excludedportsPool == null) {
/* 333 */         jPDA2._excludedportsList = "" + jPDA1._selectedPort;
/* 334 */         jPDA2._excludedportsPool = new int[] { jPDA1._selectedPort };
/*     */       } else {
/*     */         
/* 337 */         jPDA1._excludedportsList += "," + jPDA1._selectedPort;
/*     */ 
/*     */         
/* 340 */         jPDA2._excludedportsPool = new int[jPDA1._excludedportsPool.length + 1];
/*     */         
/* 342 */         for (b = 0; b < jPDA1._excludedportsPool.length; b++) {
/* 343 */           jPDA2._excludedportsPool[b] = jPDA1._excludedportsPool[b];
/*     */         }
/* 345 */         jPDA2._excludedportsPool[b] = jPDA1._selectedPort;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 350 */     jPDA2._selectedPort = jPDA2.getAvailableServerPort(paramBoolean1, paramBoolean2);
/*     */ 
/*     */ 
/*     */     
/* 354 */     if (jPDA2._selectedPort < 0) {
/* 355 */       jPDA2 = null;
/* 356 */       _nextJreRunsInJpdaMode = false;
/*     */     } 
/*     */     
/* 359 */     o_envNextJRE = jPDA2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] tokenizeJpdaEnvEntry(String paramString1, String paramString2) {
/* 365 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString1, paramString2);
/* 366 */     String[] arrayOfString = new String[stringTokenizer.countTokens()];
/*     */ 
/*     */     
/*     */     try {
/* 370 */       for (byte b = 0; stringTokenizer.hasMoreTokens(); b++) {
/* 371 */         arrayOfString[b] = stringTokenizer.nextToken();
/*     */       }
/*     */     }
/* 374 */     catch (NoSuchElementException noSuchElementException) {
/* 375 */       noSuchElementException.printStackTrace();
/* 376 */       return null;
/*     */     } 
/* 378 */     return arrayOfString;
/*     */   }
/*     */   
/*     */   public static void showJpdaNotificationWindow(JPDA paramJPDA) {
/* 382 */     if (paramJPDA == null) {
/* 383 */       DialogFactory.showErrorDialog("ERROR: No JPDA environment.", dbgNotificationTitle);
/*     */     } else {
/*     */       
/* 386 */       DialogFactory.showInformationDialog("Starting JRE (version " + paramJPDA._jreProductVersion + ") in JPDA debugging mode, trying server socket port " + paramJPDA._selectedPort + " on this host (" + getLocalHostName() + ").\n\n        Main class  =  " + "com.sun.javaws.Main" + "\n        Arguments to main()  =  " + paramJPDA._javaMainArgsList + "\n\nTo start debugging, please connect a JPDA debugging client to this host at indicated port.\n\n\nDiagnostics:\n\n     Debugging directive was obtained from\n     " + (_jpdaConfigIsFromCmdLine ? "command line:" : "\"javaws-jpda.cfg\" configuration file:") + "\n        - JRE " + (_jreUsesDashClassic ? "uses" : "doesn't use") + "  -classic  option.\n        - Port " + (paramJPDA._portIsAutoSelected ? "automatically selected (by OS);\n          unable to find or use user-specified\n          ports list." : (" selected from user-specified list:\n          " + _portsList + ".")), dbgNotificationTitle + " (" + ((paramJPDA._jreNestingLevel < 1) ? "JWS" : "JNL") + ")");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getProperty(String paramString) {
/* 394 */     String str = null;
/*     */     try {
/* 396 */       str = System.getProperty(paramString);
/* 397 */     } catch (SecurityException securityException) {
/* 398 */       securityException.printStackTrace();
/* 399 */       return str;
/* 400 */     } catch (NullPointerException nullPointerException) {
/* 401 */       nullPointerException.printStackTrace();
/* 402 */       return str;
/* 403 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 404 */       illegalArgumentException.printStackTrace();
/* 405 */       return str;
/*     */     } 
/* 407 */     return str;
/*     */   }
/*     */   
/*     */   private static int string2Int(String paramString) {
/* 411 */     int i = -1;
/*     */     try {
/* 413 */       i = (new Integer(paramString)).intValue();
/* 414 */     } catch (NumberFormatException numberFormatException) {
/* 415 */       numberFormatException.printStackTrace();
/* 416 */       return i;
/*     */     } 
/* 418 */     return i;
/*     */   }
/*     */   
/*     */   private static String getLocalHostName() {
/*     */     try {
/* 423 */       return InetAddress.getLocalHost().getHostName();
/* 424 */     } catch (UnknownHostException unknownHostException) {
/* 425 */       return "localhost";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAvailableServerPort(boolean paramBoolean1, boolean paramBoolean2) {
/* 453 */     if (_portsPool == null) {
/* 454 */       return -1;
/*     */     }
/* 456 */     this._portIsAutoSelected = false; byte b;
/* 457 */     for (b = 0; b < _portsPool.length; b++) {
/* 458 */       int i; if ((i = _portsPool[b]) != 0 && (!paramBoolean1 || !isExcludedPort(i))) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 463 */           (new ServerSocket(i)).close();
/* 464 */           return i;
/* 465 */         } catch (IOException iOException) {}
/*     */       }
/*     */     } 
/* 468 */     if (paramBoolean2) {
/* 469 */       b = 0; try {
/*     */         int i;
/*     */         do {
/* 472 */           ServerSocket serverSocket = new ServerSocket(0);
/* 473 */           i = serverSocket.getLocalPort();
/* 474 */           serverSocket.close();
/* 475 */         } while (paramBoolean1 && isExcludedPort(i));
/* 476 */         this._portIsAutoSelected = true;
/* 477 */         return i;
/*     */       }
/* 479 */       catch (IOException iOException) {}
/*     */     } 
/* 481 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isExcludedPort(int paramInt) {
/* 486 */     if (this._excludedportsPool == null) return false; 
/* 487 */     for (byte b = 0; b < this._excludedportsPool.length; b++) {
/* 488 */       if (paramInt == this._excludedportsPool[b]) {
/* 489 */         return true;
/*     */       }
/*     */     } 
/* 492 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] JpdaSetup(String[] paramArrayOfString, JREInfo paramJREInfo) {
/* 519 */     setJpdaEnvForNextJRE(true, true, paramArrayOfString, paramJREInfo);
/*     */     
/* 521 */     if (_nextJreRunsInJpdaMode) {
/*     */ 
/*     */ 
/*     */       
/* 525 */       int i = paramArrayOfString.length + (_jreUsesDashClassic ? 5 : 2);
/* 526 */       String[] arrayOfString = new String[i];
/*     */       
/* 528 */       byte b1 = 0;
/* 529 */       arrayOfString[b1++] = paramArrayOfString[0];
/* 530 */       if (_jreUsesDashClassic) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 536 */         arrayOfString[b1++] = "-classic";
/* 537 */         arrayOfString[b1++] = "-Xnoagent";
/* 538 */         arrayOfString[b1++] = "-Djava.compiler=NONE";
/*     */       } 
/* 540 */       arrayOfString[b1++] = "-Xdebug";
/* 541 */       arrayOfString[b1++] = "-Xrunjdwp:transport=dt_socket,server=y,address=" + o_envNextJRE._selectedPort + ",suspend=y";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 547 */       for (byte b2 = 1; b2 < paramArrayOfString.length; arrayOfString[b1++] = paramArrayOfString[b2++]);
/*     */       
/* 549 */       showJpdaNotificationWindow(o_envNextJRE);
/* 550 */       return arrayOfString;
/*     */     } 
/* 552 */     return paramArrayOfString;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\JPDA.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */