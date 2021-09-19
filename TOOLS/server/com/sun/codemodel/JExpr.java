/*     */ package com.sun.codemodel;
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
/*     */ public abstract class JExpr
/*     */ {
/*     */   public static JExpression assign(JAssignmentTarget lhs, JExpression rhs) {
/*  35 */     return new JAssignment(lhs, rhs);
/*     */   }
/*     */   
/*     */   public static JExpression assignPlus(JAssignmentTarget lhs, JExpression rhs) {
/*  39 */     return new JAssignment(lhs, rhs, "+");
/*     */   }
/*     */   
/*     */   public static JInvocation _new(JClass c) {
/*  43 */     return new JInvocation(c);
/*     */   }
/*     */   
/*     */   public static JInvocation _new(JType t) {
/*  47 */     return new JInvocation(t);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(String method) {
/*  51 */     return new JInvocation((JExpression)null, method);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JMethod method) {
/*  55 */     return new JInvocation((JExpression)null, method);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JExpression lhs, JMethod method) {
/*  59 */     return new JInvocation(lhs, method);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JExpression lhs, String method) {
/*  63 */     return new JInvocation(lhs, method);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(String field) {
/*  67 */     return new JFieldRef((JExpression)null, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(JExpression lhs, JVar field) {
/*  71 */     return new JFieldRef(lhs, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(JExpression lhs, String field) {
/*  75 */     return new JFieldRef(lhs, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef refthis(String field) {
/*  79 */     return new JFieldRef(null, field, true);
/*     */   }
/*     */   
/*     */   public static JExpression dotclass(final JClass cl) {
/*  83 */     return new JExpressionImpl() {
/*     */         public void generate(JFormatter f) {
/*     */           JClass c;
/*  86 */           if (cl instanceof JNarrowedClass) {
/*  87 */             c = ((JNarrowedClass)cl).basis;
/*     */           } else {
/*  89 */             c = cl;
/*  90 */           }  f.g(c).p(".class");
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static JArrayCompRef component(JExpression lhs, JExpression index) {
/*  96 */     return new JArrayCompRef(lhs, index);
/*     */   }
/*     */   
/*     */   public static JCast cast(JType type, JExpression expr) {
/* 100 */     return new JCast(type, expr);
/*     */   }
/*     */   
/*     */   public static JArray newArray(JType type) {
/* 104 */     return newArray(type, (JExpression)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JArray newArray(JType type, JExpression size) {
/* 115 */     return new JArray(type.erasure(), size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JArray newArray(JType type, int size) {
/* 125 */     return newArray(type, lit(size));
/*     */   }
/*     */ 
/*     */   
/* 129 */   private static final JExpression __this = new JAtom("this");
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression _this() {
/* 134 */     return __this;
/*     */   }
/* 136 */   private static final JExpression __super = new JAtom("super");
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression _super() {
/* 141 */     return __super;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   private static final JExpression __null = new JAtom("null");
/*     */   public static JExpression _null() {
/* 148 */     return __null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final JExpression TRUE = new JAtom("true");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static final JExpression FALSE = new JAtom("false"); static final String charEscape = "\b\t\n\f\r\"'\\";
/*     */   
/*     */   public static JExpression lit(boolean b) {
/* 162 */     return b ? TRUE : FALSE;
/*     */   }
/*     */   static final String charMacro = "btnfr\"'\\";
/*     */   public static JExpression lit(int n) {
/* 166 */     return new JAtom(Integer.toString(n));
/*     */   }
/*     */   
/*     */   public static JExpression lit(long n) {
/* 170 */     return new JAtom(Long.toString(n) + "L");
/*     */   }
/*     */   
/*     */   public static JExpression lit(float f) {
/* 174 */     return new JAtom(Float.toString(f) + "F");
/*     */   }
/*     */   
/*     */   public static JExpression lit(double d) {
/* 178 */     return new JAtom(Double.toString(d) + "D");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String quotify(char quote, String s) {
/* 189 */     int n = s.length();
/* 190 */     StringBuilder sb = new StringBuilder(n + 2);
/* 191 */     sb.append(quote);
/* 192 */     for (int i = 0; i < n; i++) {
/* 193 */       char c = s.charAt(i);
/* 194 */       int j = "\b\t\n\f\r\"'\\".indexOf(c);
/* 195 */       if (j >= 0) {
/* 196 */         sb.append('\\');
/* 197 */         sb.append("btnfr\"'\\".charAt(j));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 207 */       else if (c < ' ' || '~' < c) {
/*     */         
/* 209 */         sb.append("\\u");
/* 210 */         String hex = Integer.toHexString(c & Character.MAX_VALUE);
/* 211 */         for (int k = hex.length(); k < 4; k++)
/* 212 */           sb.append('0'); 
/* 213 */         sb.append(hex);
/*     */       } else {
/* 215 */         sb.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 219 */     sb.append(quote);
/* 220 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static JExpression lit(char c) {
/* 224 */     return new JAtom(quotify('\'', "" + c));
/*     */   }
/*     */   
/*     */   public static JExpression lit(String s) {
/* 228 */     return new JStringLiteral(s);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression direct(final String source) {
/* 244 */     return new JExpressionImpl() {
/*     */         public void generate(JFormatter f) {
/* 246 */           f.p('(').p(source).p(')');
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */