/*     */ package org.fourthline.cling.model;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelUtil
/*     */ {
/*     */   public static final boolean ANDROID_RUNTIME;
/*     */   public static final boolean ANDROID_EMULATOR;
/*     */   
/*     */   static {
/*  37 */     boolean foundAndroid = false;
/*     */     try {
/*  39 */       Class<?> androidBuild = Thread.currentThread().getContextClassLoader().loadClass("android.os.Build");
/*  40 */       foundAndroid = (androidBuild.getField("ID").get(null) != null);
/*  41 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  44 */     ANDROID_RUNTIME = foundAndroid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     boolean foundEmulator = false;
/*     */     try {
/*  54 */       Class<?> androidBuild = Thread.currentThread().getContextClassLoader().loadClass("android.os.Build");
/*  55 */       String product = (String)androidBuild.getField("PRODUCT").get(null);
/*  56 */       if ("google_sdk".equals(product) || "sdk".equals(product))
/*  57 */         foundEmulator = true; 
/*  58 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  61 */     ANDROID_EMULATOR = foundEmulator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStringConvertibleType(Set<Class<?>> stringConvertibleTypes, Class<?> clazz) {
/*  70 */     if (clazz.isEnum()) return true; 
/*  71 */     for (Class<?> toStringOutputType : stringConvertibleTypes) {
/*  72 */       if (toStringOutputType.isAssignableFrom(clazz)) {
/*  73 */         return true;
/*     */       }
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidUDAName(String name) {
/*  85 */     if (ANDROID_RUNTIME) {
/*  86 */       return (name != null && name.length() != 0);
/*     */     }
/*  88 */     return (name != null && name.length() != 0 && !name.toLowerCase(Locale.ROOT).startsWith("xml") && name.matches("[a-zA-Z0-9^-_\\p{L}\\p{N}]{1}[a-zA-Z0-9^-_\\.\\\\p{L}\\\\p{N}\\p{Mc}\\p{Sk}]*"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InetAddress getInetAddressByName(String name) {
/*     */     try {
/*  96 */       return InetAddress.getByName(name);
/*  97 */     } catch (Exception ex) {
/*  98 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toCommaSeparatedList(Object[] o) {
/* 107 */     return toCommaSeparatedList(o, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toCommaSeparatedList(Object[] o, boolean escapeCommas, boolean escapeDoubleQuotes) {
/* 115 */     if (o == null) {
/* 116 */       return "";
/*     */     }
/* 118 */     StringBuilder sb = new StringBuilder();
/* 119 */     for (Object obj : o) {
/* 120 */       String objString = obj.toString();
/* 121 */       objString = objString.replaceAll("\\\\", "\\\\\\\\");
/* 122 */       if (escapeCommas) {
/* 123 */         objString = objString.replaceAll(",", "\\\\,");
/*     */       }
/* 125 */       if (escapeDoubleQuotes) {
/* 126 */         objString = objString.replaceAll("\"", "\\\"");
/*     */       }
/* 128 */       sb.append(objString).append(",");
/*     */     } 
/* 130 */     if (sb.length() > 1) {
/* 131 */       sb.deleteCharAt(sb.length() - 1);
/*     */     }
/* 133 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] fromCommaSeparatedList(String s) {
/* 142 */     return fromCommaSeparatedList(s, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] fromCommaSeparatedList(String s, boolean unescapeCommas) {
/* 150 */     if (s == null || s.length() == 0) {
/* 151 */       return null;
/*     */     }
/*     */     
/* 154 */     String QUOTED_COMMA_PLACEHOLDER = "XXX1122334455XXX";
/* 155 */     if (unescapeCommas) {
/* 156 */       s = s.replaceAll("\\\\,", "XXX1122334455XXX");
/*     */     }
/*     */     
/* 159 */     String[] split = s.split(",");
/* 160 */     for (int i = 0; i < split.length; i++) {
/* 161 */       split[i] = split[i].replaceAll("XXX1122334455XXX", ",");
/* 162 */       split[i] = split[i].replaceAll("\\\\\\\\", "\\\\");
/*     */     } 
/* 164 */     return split;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toTimeString(long seconds) {
/* 172 */     long hours = seconds / 3600L;
/* 173 */     long remainder = seconds % 3600L;
/* 174 */     long minutes = remainder / 60L;
/* 175 */     long secs = remainder % 60L;
/*     */     
/* 177 */     return ((hours < 10L) ? "0" : "") + hours + ":" + ((minutes < 10L) ? "0" : "") + minutes + ":" + ((secs < 10L) ? "0" : "") + secs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long fromTimeString(String s) {
/* 188 */     if (s.lastIndexOf(".") != -1)
/* 189 */       s = s.substring(0, s.lastIndexOf(".")); 
/* 190 */     String[] split = s.split(":");
/* 191 */     if (split.length != 3) {
/* 192 */       throw new IllegalArgumentException("Can't parse time string: " + s);
/*     */     }
/*     */     
/* 195 */     return Long.parseLong(split[0]) * 3600L + Long.parseLong(split[1]) * 60L + Long.parseLong(split[2]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String commaToNewline(String s) {
/* 203 */     StringBuilder sb = new StringBuilder();
/* 204 */     String[] split = s.split(",");
/* 205 */     for (String splitString : split) {
/* 206 */       sb.append(splitString).append(",").append("\n");
/*     */     }
/* 208 */     if (sb.length() > 2) {
/* 209 */       sb.deleteCharAt(sb.length() - 2);
/*     */     }
/* 211 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getLocalHostName(boolean includeDomain) {
/*     */     try {
/* 222 */       String hostname = InetAddress.getLocalHost().getHostName();
/* 223 */       return includeDomain ? hostname : (
/*     */         
/* 225 */         (hostname.indexOf(".") != -1) ? hostname.substring(0, hostname.indexOf(".")) : hostname);
/*     */     }
/* 227 */     catch (Exception ex) {
/*     */       
/* 229 */       return "UNKNOWN HOST";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getFirstNetworkInterfaceHardwareAddress() {
/*     */     try {
/* 238 */       Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
/* 239 */       for (NetworkInterface iface : Collections.<NetworkInterface>list(interfaceEnumeration)) {
/* 240 */         if (!iface.isLoopback() && iface.isUp() && iface.getHardwareAddress() != null) {
/* 241 */           return iface.getHardwareAddress();
/*     */         }
/*     */       } 
/* 244 */     } catch (Exception ex) {
/* 245 */       throw new RuntimeException("Could not discover first network interface hardware address");
/*     */     } 
/* 247 */     throw new RuntimeException("Could not discover first network interface hardware address");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\ModelUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */