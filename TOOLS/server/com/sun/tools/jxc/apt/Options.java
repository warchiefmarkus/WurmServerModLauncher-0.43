/*     */ package com.sun.tools.jxc.apt;
/*     */ 
/*     */ import com.sun.tools.xjc.BadCommandLineException;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Options
/*     */ {
/*  54 */   public String classpath = System.getenv("CLASSPATH");
/*     */   
/*  56 */   public File targetDir = null;
/*     */   
/*  58 */   public File episodeFile = null;
/*     */   
/*  60 */   public final List<String> arguments = new ArrayList<String>();
/*     */   
/*     */   public void parseArguments(String[] args) throws BadCommandLineException {
/*  63 */     for (int i = 0; i < args.length; i++) {
/*  64 */       if (args[i].charAt(0) == '-') {
/*  65 */         int j = parseArgument(args, i);
/*  66 */         if (j == 0) {
/*  67 */           throw new BadCommandLineException(Messages.UNRECOGNIZED_PARAMETER.format(new Object[] { args[i] }));
/*     */         }
/*  69 */         i += j;
/*     */       } else {
/*  71 */         this.arguments.add(args[i]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int parseArgument(String[] args, int i) throws BadCommandLineException {
/*  77 */     if (args[i].equals("-d")) {
/*  78 */       if (i == args.length - 1) {
/*  79 */         throw new BadCommandLineException(Messages.OPERAND_MISSING.format(new Object[] { args[i] }));
/*     */       }
/*  81 */       this.targetDir = new File(args[++i]);
/*  82 */       if (!this.targetDir.exists()) {
/*  83 */         throw new BadCommandLineException(Messages.NON_EXISTENT_FILE.format(new Object[] { this.targetDir }));
/*     */       }
/*  85 */       return 1;
/*     */     } 
/*     */     
/*  88 */     if (args[i].equals("-episode")) {
/*  89 */       if (i == args.length - 1) {
/*  90 */         throw new BadCommandLineException(Messages.OPERAND_MISSING.format(new Object[] { args[i] }));
/*     */       }
/*  92 */       this.episodeFile = new File(args[++i]);
/*  93 */       return 1;
/*     */     } 
/*     */     
/*  96 */     if (args[i].equals("-cp") || args[i].equals("-classpath")) {
/*  97 */       if (i == args.length - 1) {
/*  98 */         throw new BadCommandLineException(Messages.OPERAND_MISSING.format(new Object[] { args[i] }));
/*     */       }
/* 100 */       this.classpath = args[++i];
/*     */       
/* 102 */       return 1;
/*     */     } 
/*     */     
/* 105 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\apt\Options.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */