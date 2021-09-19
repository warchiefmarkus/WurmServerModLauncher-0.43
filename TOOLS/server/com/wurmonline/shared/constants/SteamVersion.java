/*    */ package com.wurmonline.shared.constants;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SteamVersion
/*    */ {
/*    */   public static final String UNKNOWN_VERSION = "UNKNOWN";
/*    */   static final int MAJOR = 1;
/*    */   static final int COMPATIBILITY = 9;
/*    */   static final int CLIENT = 1;
/*    */   static final int SERVER = 5;
/* 20 */   private static final SteamVersion current = new SteamVersion(1, 9, 1, 5);
/* 21 */   private static final Pattern versionPattern = Pattern.compile("^(?:version=)?(?<major>\\d+)\\.(?<compatibility>\\d+)\\.(?<client>\\d+)\\.(?<server>\\d+);?$");
/*    */   private static final String patternString = "^(?:version=)?(?<major>\\d+)\\.(?<compatibility>\\d+)\\.(?<client>\\d+)\\.(?<server>\\d+);?$";
/*    */   
/*    */   public static SteamVersion getCurrentVersion() {
/* 25 */     return current;
/*    */   }
/*    */   
/* 28 */   private int major = 0;
/* 29 */   private int compatibility = 0;
/* 30 */   private int client = 0;
/* 31 */   private int server = 0;
/*    */   
/*    */   public SteamVersion(String version) {
/* 34 */     if (version == null)
/*    */       return; 
/* 36 */     Matcher matcher = versionPattern.matcher(version);
/* 37 */     if (matcher.matches()) {
/*    */       
/* 39 */       this.major = Integer.valueOf(matcher.group("major")).intValue();
/* 40 */       this.compatibility = Integer.valueOf(matcher.group("compatibility")).intValue();
/* 41 */       this.client = Integer.valueOf(matcher.group("client")).intValue();
/* 42 */       this.server = Integer.valueOf(matcher.group("server")).intValue();
/*    */     } 
/*    */   }
/*    */   
/*    */   SteamVersion(int major, int compatibility, int client, int server) {
/* 47 */     this.major = major;
/* 48 */     this.compatibility = compatibility;
/* 49 */     this.client = client;
/* 50 */     this.server = server;
/*    */   }
/*    */   
/*    */   public boolean isCompatibleWith(SteamVersion version) {
/* 54 */     return (this.major == version.major && this.compatibility == version.compatibility);
/*    */   }
/*    */   
/*    */   public String getTag() {
/* 58 */     return "version=" + toString() + ";";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 64 */     if (this == o) return true; 
/* 65 */     if (o == null || getClass() != o.getClass()) return false; 
/* 66 */     SteamVersion version = (SteamVersion)o;
/* 67 */     return (this.major == version.major && this.compatibility == version.compatibility && this.client == version.client && this.server == version.server);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 76 */     return Objects.hash(new Object[] { Integer.valueOf(this.major), Integer.valueOf(this.compatibility), Integer.valueOf(this.client), Integer.valueOf(this.server) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(this.major), Integer.valueOf(this.compatibility), Integer.valueOf(this.client), Integer.valueOf(this.server) });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompatibleWith(String version) {
/* 86 */     return isCompatibleWith(new SteamVersion(version));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\SteamVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */