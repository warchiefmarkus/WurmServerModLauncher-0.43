/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JGenerable;
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
/*     */ public class JMods
/*     */   implements JGenerable
/*     */ {
/*  20 */   private static int VAR = 8;
/*     */ 
/*     */   
/*  23 */   private static int FIELD = 799;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   private static int METHOD = 255;
/*     */ 
/*     */ 
/*     */   
/*  32 */   private static int CLASS = 63;
/*     */ 
/*     */ 
/*     */   
/*  36 */   private static int INTERFACE = 1;
/*     */   
/*     */   private int mods;
/*     */ 
/*     */   
/*     */   private JMods(int mods) {
/*  42 */     this.mods = mods;
/*     */   }
/*     */   
/*     */   private static void check(int mods, int legal, String what) {
/*  46 */     if ((mods & (legal ^ 0xFFFFFFFF)) != 0) {
/*  47 */       throw new IllegalArgumentException("Illegal modifiers for " + what + ": " + (new com.sun.codemodel.JMods(mods)).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static com.sun.codemodel.JMods forVar(int mods) {
/*  54 */     check(mods, VAR, "variable");
/*  55 */     return new com.sun.codemodel.JMods(mods);
/*     */   }
/*     */   
/*     */   static com.sun.codemodel.JMods forField(int mods) {
/*  59 */     check(mods, FIELD, "field");
/*  60 */     return new com.sun.codemodel.JMods(mods);
/*     */   }
/*     */   
/*     */   static com.sun.codemodel.JMods forMethod(int mods) {
/*  64 */     check(mods, METHOD, "method");
/*  65 */     return new com.sun.codemodel.JMods(mods);
/*     */   }
/*     */   
/*     */   static com.sun.codemodel.JMods forClass(int mods) {
/*  69 */     check(mods, CLASS, "class");
/*  70 */     return new com.sun.codemodel.JMods(mods);
/*     */   }
/*     */   
/*     */   static com.sun.codemodel.JMods forInterface(int mods) {
/*  74 */     check(mods, INTERFACE, "class");
/*  75 */     return new com.sun.codemodel.JMods(mods);
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  79 */     return ((this.mods & 0x20) != 0);
/*     */   }
/*     */   
/*     */   public boolean isNative() {
/*  83 */     return ((this.mods & 0x40) != 0);
/*     */   }
/*     */   
/*     */   public boolean isSynchronized() {
/*  87 */     return ((this.mods & 0x80) != 0);
/*     */   }
/*     */   
/*     */   public void setSynchronized(boolean newValue) {
/*  91 */     setFlag(128, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setFlag(int bit, boolean newValue) {
/*  97 */     this.mods = this.mods & (bit ^ 0xFFFFFFFF) | (newValue ? bit : 0);
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 101 */     if ((this.mods & 0x1) != 0) f.p("public"); 
/* 102 */     if ((this.mods & 0x2) != 0) f.p("protected"); 
/* 103 */     if ((this.mods & 0x4) != 0) f.p("private"); 
/* 104 */     if ((this.mods & 0x8) != 0) f.p("final"); 
/* 105 */     if ((this.mods & 0x10) != 0) f.p("static"); 
/* 106 */     if ((this.mods & 0x20) != 0) f.p("abstract"); 
/* 107 */     if ((this.mods & 0x40) != 0) f.p("native"); 
/* 108 */     if ((this.mods & 0x80) != 0) f.p("synchronized"); 
/* 109 */     if ((this.mods & 0x100) != 0) f.p("transient"); 
/* 110 */     if ((this.mods & 0x200) != 0) f.p("volatile"); 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 114 */     StringWriter s = new StringWriter();
/* 115 */     JFormatter f = new JFormatter(new PrintWriter(s));
/* 116 */     generate(f);
/* 117 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JMods.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */