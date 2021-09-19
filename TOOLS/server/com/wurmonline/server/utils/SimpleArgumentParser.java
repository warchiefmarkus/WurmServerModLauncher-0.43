/*    */ package com.wurmonline.server.utils;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Locale;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleArgumentParser
/*    */ {
/* 13 */   private final HashMap<String, String> assignedOptions = new HashMap<>();
/* 14 */   private final HashSet<String> flagOptions = new HashSet<>();
/* 15 */   private final HashSet<String> unknownOptions = new HashSet<>();
/*    */   
/*    */   public SimpleArgumentParser(String[] args, Set<String> allowedOptions) {
/* 18 */     for (String arg : args) {
/* 19 */       arg = arg.trim();
/* 20 */       if (!arg.isEmpty())
/*    */       {
/*    */         
/* 23 */         if (!arg.contains("WurmServerLauncher")) {
/*    */ 
/*    */           
/* 26 */           int assignmentIdx = arg.indexOf('=');
/* 27 */           if (assignmentIdx > 0) {
/* 28 */             String option = arg.substring(0, assignmentIdx).toLowerCase(Locale.ENGLISH);
/* 29 */             if (!allowedOptions.contains(option)) {
/* 30 */               System.err.println("Unknown parameter: " + option);
/* 31 */             } else if (assignmentIdx >= arg.length()) {
/* 32 */               this.assignedOptions.put(option, "");
/*    */             } else {
/* 34 */               this.assignedOptions.put(option, arg.substring(assignmentIdx + 1));
/*    */             } 
/* 36 */           } else if (allowedOptions.contains(arg)) {
/* 37 */             this.flagOptions.add(arg.toLowerCase(Locale.ENGLISH));
/*    */           } else {
/* 39 */             System.err.println("Unknown parameter: " + arg);
/* 40 */             this.unknownOptions.add(arg);
/*    */           } 
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasOption(String option) {
/* 51 */     return (this.flagOptions.contains(option) || this.assignedOptions.containsKey(option));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasFlag(String option) {
/* 60 */     return this.flagOptions.contains(option);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getOptionValue(String option) {
/* 70 */     return this.assignedOptions.get(option);
/*    */   }
/*    */   
/*    */   public boolean hasUnknownOptions() {
/* 74 */     return !this.unknownOptions.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\SimpleArgumentParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */