/*     */ package 1.0.com.sun.tools.xjc.generator;
/*     */ 
/*     */ import com.sun.codemodel.fmt.JStaticJavaFile;
/*     */ import java.text.ParseException;
/*     */ import java.util.Stack;
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
/*     */ public abstract class PreProcessingLineFilter
/*     */   implements JStaticJavaFile.LineFilter
/*     */ {
/*  50 */   private final Stack conditions = new Stack();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String META_TOKEN = "// META-";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isOn() {
/*  61 */     for (int i = this.conditions.size() - 1; i >= 0; i--) {
/*  62 */       if (!((Boolean)this.conditions.get(i)).booleanValue())
/*  63 */         return false; 
/*  64 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String process(String line) throws ParseException {
/*  69 */     int idx = line.indexOf("// META-");
/*  70 */     if (idx < 0) {
/*     */       
/*  72 */       if (isOn()) return line; 
/*  73 */       return null;
/*     */     } 
/*     */     
/*  76 */     String cond = line.substring(idx + "// META-".length()).trim();
/*  77 */     if (cond.startsWith("IF(")) {
/*     */ 
/*     */       
/*  80 */       idx = cond.indexOf(')');
/*  81 */       if (idx < 0) throw new ParseException("Unable to parse " + cond, -1); 
/*  82 */       String exp = cond.substring(3, idx);
/*     */       
/*  84 */       this.conditions.push(eval(exp) ? Boolean.TRUE : Boolean.FALSE);
/*  85 */       return null;
/*     */     } 
/*  87 */     if (cond.equals("ELSE")) {
/*     */       
/*  89 */       Boolean b = this.conditions.pop();
/*  90 */       this.conditions.push(!b.booleanValue() ? Boolean.TRUE : Boolean.FALSE);
/*  91 */       return null;
/*     */     } 
/*  93 */     if (cond.equals("ENDIF")) {
/*     */       
/*  95 */       this.conditions.pop();
/*  96 */       return null;
/*     */     } 
/*     */     
/*  99 */     throw new ParseException("unrecognized meta statement " + line, -1);
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
/*     */   private boolean eval(String exp) throws ParseException {
/* 112 */     boolean r = getVar(exp.charAt(0));
/* 113 */     int i = 1;
/* 114 */     if (i < exp.length())
/* 115 */     { char op = exp.charAt(i++);
/* 116 */       if (i == exp.length())
/* 117 */         throw new ParseException("Unable to parse " + exp, -1); 
/* 118 */       boolean rhs = getVar(exp.charAt(i++));
/* 119 */       switch (op) { case '|':
/* 120 */           r |= rhs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 126 */           return r;case '&': r &= rhs; return r; }  throw new ParseException("Unable to parse" + exp, -1); }  return r;
/*     */   }
/*     */   
/*     */   protected abstract boolean getVar(char paramChar) throws ParseException;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\PreProcessingLineFilter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */