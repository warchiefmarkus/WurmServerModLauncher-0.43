/*    */ package com.wurmonline.common;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class BuildProperties
/*    */ {
/*  9 */   private final Properties properties = new Properties();
/*    */ 
/*    */ 
/*    */   
/*    */   public static BuildProperties getPropertiesFor(String path) throws IOException {
/* 14 */     BuildProperties bp = new BuildProperties();
/* 15 */     try (InputStream inputStream = BuildProperties.class.getResourceAsStream(path)) {
/*    */       
/* 17 */       bp.properties.load(inputStream);
/*    */     } 
/* 19 */     return bp;
/*    */   }
/*    */   
/*    */   public String getGitSha1Short() {
/* 23 */     String sha = getGitSha1();
/* 24 */     if (sha.length() < 7)
/* 25 */       return sha; 
/* 26 */     return sha.substring(0, 7);
/*    */   }
/*    */   
/*    */   public String getGitBranch() {
/* 30 */     return this.properties.getProperty("git-branch");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getGitSha1() {
/* 35 */     return this.properties.getProperty("git-sha-1");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 40 */     return this.properties.getProperty("version");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getBuildTimeString() {
/* 45 */     return this.properties.getProperty("build-time");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\common\BuildProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */