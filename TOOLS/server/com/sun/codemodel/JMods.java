/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMods
/*     */   implements JGenerable
/*     */ {
/*  35 */   private static int VAR = 8;
/*     */ 
/*     */   
/*  38 */   private static int FIELD = 799;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   private static int METHOD = 255;
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static int CLASS = 63;
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static int INTERFACE = 1;
/*     */   
/*     */   private int mods;
/*     */ 
/*     */   
/*     */   private JMods(int mods) {
/*  57 */     this.mods = mods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*  64 */     return this.mods;
/*     */   }
/*     */   
/*     */   private static void check(int mods, int legal, String what) {
/*  68 */     if ((mods & (legal ^ 0xFFFFFFFF)) != 0) {
/*  69 */       throw new IllegalArgumentException("Illegal modifiers for " + what + ": " + (new JMods(mods)).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static JMods forVar(int mods) {
/*  76 */     check(mods, VAR, "variable");
/*  77 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forField(int mods) {
/*  81 */     check(mods, FIELD, "field");
/*  82 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forMethod(int mods) {
/*  86 */     check(mods, METHOD, "method");
/*  87 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forClass(int mods) {
/*  91 */     check(mods, CLASS, "class");
/*  92 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forInterface(int mods) {
/*  96 */     check(mods, INTERFACE, "class");
/*  97 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 101 */     return ((this.mods & 0x20) != 0);
/*     */   }
/*     */   
/*     */   public boolean isNative() {
/* 105 */     return ((this.mods & 0x40) != 0);
/*     */   }
/*     */   
/*     */   public boolean isSynchronized() {
/* 109 */     return ((this.mods & 0x80) != 0);
/*     */   }
/*     */   
/*     */   public void setSynchronized(boolean newValue) {
/* 113 */     setFlag(128, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setFlag(int bit, boolean newValue) {
/* 119 */     this.mods = this.mods & (bit ^ 0xFFFFFFFF) | (newValue ? bit : 0);
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 123 */     if ((this.mods & 0x1) != 0) f.p("public"); 
/* 124 */     if ((this.mods & 0x2) != 0) f.p("protected"); 
/* 125 */     if ((this.mods & 0x4) != 0) f.p("private"); 
/* 126 */     if ((this.mods & 0x8) != 0) f.p("final"); 
/* 127 */     if ((this.mods & 0x10) != 0) f.p("static"); 
/* 128 */     if ((this.mods & 0x20) != 0) f.p("abstract"); 
/* 129 */     if ((this.mods & 0x40) != 0) f.p("native"); 
/* 130 */     if ((this.mods & 0x80) != 0) f.p("synchronized"); 
/* 131 */     if ((this.mods & 0x100) != 0) f.p("transient"); 
/* 132 */     if ((this.mods & 0x200) != 0) f.p("volatile"); 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 136 */     StringWriter s = new StringWriter();
/* 137 */     JFormatter f = new JFormatter(new PrintWriter(s));
/* 138 */     generate(f);
/* 139 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JMods.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */