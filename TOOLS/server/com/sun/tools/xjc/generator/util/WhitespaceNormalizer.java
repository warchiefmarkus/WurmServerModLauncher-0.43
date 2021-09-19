/*     */ package com.sun.tools.xjc.generator.util;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JStringLiteral;
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
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
/*     */ public abstract class WhitespaceNormalizer
/*     */ {
/*     */   public abstract JExpression generate(JCodeModel paramJCodeModel, JExpression paramJExpression);
/*     */   
/*     */   public static WhitespaceNormalizer parse(String method) {
/*  70 */     if (method.equals("preserve")) {
/*  71 */       return PRESERVE;
/*     */     }
/*  73 */     if (method.equals("replace")) {
/*  74 */       return REPLACE;
/*     */     }
/*  76 */     if (method.equals("collapse")) {
/*  77 */       return COLLAPSE;
/*     */     }
/*  79 */     throw new IllegalArgumentException(method);
/*     */   }
/*     */   
/*  82 */   public static final WhitespaceNormalizer PRESERVE = new WhitespaceNormalizer() {
/*     */       public JExpression generate(JCodeModel codeModel, JExpression literal) {
/*  84 */         return literal;
/*     */       }
/*     */     };
/*     */   
/*  88 */   public static final WhitespaceNormalizer REPLACE = new WhitespaceNormalizer()
/*     */     {
/*     */       public JExpression generate(JCodeModel codeModel, JExpression literal) {
/*  91 */         if (literal instanceof JStringLiteral)
/*     */         {
/*  93 */           return JExpr.lit(WhiteSpaceProcessor.replace(((JStringLiteral)literal).str));
/*     */         }
/*  95 */         return (JExpression)codeModel.ref(WhiteSpaceProcessor.class).staticInvoke("replace").arg(literal);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 100 */   public static final WhitespaceNormalizer COLLAPSE = new WhitespaceNormalizer()
/*     */     {
/*     */       public JExpression generate(JCodeModel codeModel, JExpression literal) {
/* 103 */         if (literal instanceof JStringLiteral)
/*     */         {
/* 105 */           return JExpr.lit(WhiteSpaceProcessor.collapse(((JStringLiteral)literal).str));
/*     */         }
/* 107 */         return (JExpression)codeModel.ref(WhiteSpaceProcessor.class).staticInvoke("collapse").arg(literal);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generato\\util\WhitespaceNormalizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */