/*     */ package org.seamless.swing.logging;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogCategory
/*     */ {
/*     */   private String name;
/*  28 */   private List<Group> groups = new ArrayList<Group>();
/*     */   
/*     */   public LogCategory(String name) {
/*  31 */     this.name = name;
/*     */   }
/*     */   
/*     */   public LogCategory(String name, Group[] groups) {
/*  35 */     this.name = name;
/*  36 */     this.groups = Arrays.asList(groups);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  40 */     return this.name;
/*     */   }
/*     */   
/*     */   public List<Group> getGroups() {
/*  44 */     return this.groups;
/*     */   }
/*     */   
/*     */   public void addGroup(String name, LoggerLevel[] loggerLevels) {
/*  48 */     this.groups.add(new Group(name, loggerLevels));
/*     */   }
/*     */   
/*     */   public static class Group
/*     */   {
/*     */     private String name;
/*  54 */     private List<LogCategory.LoggerLevel> loggerLevels = new ArrayList<LogCategory.LoggerLevel>();
/*  55 */     private List<LogCategory.LoggerLevel> previousLevels = new ArrayList<LogCategory.LoggerLevel>();
/*     */     private boolean enabled;
/*     */     
/*     */     public Group(String name) {
/*  59 */       this.name = name;
/*     */     }
/*     */     
/*     */     public Group(String name, LogCategory.LoggerLevel[] loggerLevels) {
/*  63 */       this.name = name;
/*  64 */       this.loggerLevels = Arrays.asList(loggerLevels);
/*     */     }
/*     */     
/*     */     public String getName() {
/*  68 */       return this.name;
/*     */     }
/*     */     
/*     */     public List<LogCategory.LoggerLevel> getLoggerLevels() {
/*  72 */       return this.loggerLevels;
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/*  76 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/*  80 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public List<LogCategory.LoggerLevel> getPreviousLevels() {
/*  84 */       return this.previousLevels;
/*     */     }
/*     */     
/*     */     public void setPreviousLevels(List<LogCategory.LoggerLevel> previousLevels) {
/*  88 */       this.previousLevels = previousLevels;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LoggerLevel {
/*     */     private String logger;
/*     */     private Level level;
/*     */     
/*     */     public LoggerLevel(String logger, Level level) {
/*  97 */       this.logger = logger;
/*  98 */       this.level = level;
/*     */     }
/*     */     
/*     */     public String getLogger() {
/* 102 */       return this.logger;
/*     */     }
/*     */     
/*     */     public Level getLevel() {
/* 106 */       return this.level;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\logging\LogCategory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */