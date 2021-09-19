/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JArray;
/*     */ import com.sun.codemodel.JArrayCompRef;
/*     */ import com.sun.codemodel.JAssignment;
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JAtom;
/*     */ import com.sun.codemodel.JCast;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldRef;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JStringLiteral;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ 
/*     */ public abstract class JExpr {
/*     */   public static JExpression assign(JAssignmentTarget lhs, JExpression rhs) {
/*  20 */     return (JExpression)new JAssignment(lhs, rhs);
/*     */   }
/*     */   
/*     */   public static JExpression assignPlus(JAssignmentTarget lhs, JExpression rhs) {
/*  24 */     return (JExpression)new JAssignment(lhs, rhs, "+");
/*     */   }
/*     */   
/*     */   public static JInvocation _new(JClass c) {
/*  28 */     return new JInvocation((JType)c);
/*     */   }
/*     */   
/*     */   public static JInvocation _new(JType t) {
/*  32 */     return new JInvocation(t);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(String method) {
/*  36 */     return new JInvocation((JExpression)null, method);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JMethod method) {
/*  40 */     return new JInvocation((JExpression)null, method.name());
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JExpression lhs, JMethod method) {
/*  44 */     return new JInvocation(lhs, method.name());
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JExpression lhs, String method) {
/*  48 */     return new JInvocation(lhs, method);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(String field) {
/*  52 */     return new JFieldRef((JExpression)null, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(JExpression lhs, JVar field) {
/*  56 */     return new JFieldRef(lhs, field.name());
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(JExpression lhs, String field) {
/*  60 */     return new JFieldRef(lhs, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef refthis(String field) {
/*  64 */     return new JFieldRef(null, field, true);
/*     */   }
/*     */   
/*     */   public static JExpression dotclass(JClass cl) {
/*  68 */     return (JExpression)new Object(cl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression dotclass(JType t) {
/*  76 */     return (JExpression)new Object(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JArrayCompRef component(JExpression lhs, JExpression index) {
/*  84 */     return new JArrayCompRef(lhs, index);
/*     */   }
/*     */   
/*     */   public static JCast cast(JType type, JExpression expr) {
/*  88 */     return new JCast(type, expr);
/*     */   }
/*     */   
/*     */   public static JArray newArray(JType type) {
/*  92 */     return new JArray(type, null);
/*     */   }
/*     */   
/*     */   public static JArray newArray(JType type, JExpression size) {
/*  96 */     return new JArray(type, size);
/*     */   }
/*     */   
/*     */   public static JArray newArray(JType type, int size) {
/* 100 */     return newArray(type, lit(size));
/*     */   }
/*     */ 
/*     */   
/* 104 */   private static final JExpression __this = (JExpression)new JAtom("this");
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression _this() {
/* 109 */     return __this;
/*     */   }
/* 111 */   private static final JExpression __super = (JExpression)new JAtom("super");
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression _super() {
/* 116 */     return __super;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 121 */   private static final JExpression __null = (JExpression)new JAtom("null");
/*     */   public static JExpression _null() {
/* 123 */     return __null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public static final JExpression TRUE = (JExpression)new JAtom("true");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public static final JExpression FALSE = (JExpression)new JAtom("false"); static final String charEscape = "\bb\tt\nn\ff\rr\"\"''\\\\";
/*     */   
/*     */   public static JExpression lit(int n) {
/* 137 */     return (JExpression)new JAtom(Integer.toString(n));
/*     */   }
/*     */   
/*     */   public static JExpression lit(long n) {
/* 141 */     return (JExpression)new JAtom(Long.toString(n) + "L");
/*     */   }
/*     */   
/*     */   public static JExpression lit(float f) {
/* 145 */     return (JExpression)new JAtom(Float.toString(f) + "F");
/*     */   }
/*     */   
/*     */   public static JExpression lit(double d) {
/* 149 */     return (JExpression)new JAtom(Double.toString(d) + "D");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String quotify(char quote, String s) {
/* 159 */     int n = s.length();
/* 160 */     StringBuffer sb = new StringBuffer(n + 2);
/* 161 */     sb.append(quote);
/* 162 */     for (int i = 0; i < n; i++) {
/* 163 */       char c = s.charAt(i);
/*     */       int j;
/* 165 */       for (j = 0; j < 8; j++) {
/* 166 */         if (c == "\bb\tt\nn\ff\rr\"\"''\\\\".charAt(j * 2)) {
/* 167 */           sb.append('\\');
/* 168 */           sb.append("\bb\tt\nn\ff\rr\"\"''\\\\".charAt(j * 2 + 1)); break;
/*     */         } 
/*     */       } 
/* 171 */       if (j == 8)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 180 */         if (c < ' ' || '~' < c) {
/*     */           
/* 182 */           sb.append("\\u");
/* 183 */           String hex = Integer.toHexString(c & Character.MAX_VALUE);
/* 184 */           for (int k = hex.length(); k < 4; k++)
/* 185 */             sb.append('0'); 
/* 186 */           sb.append(hex);
/*     */         } else {
/* 188 */           sb.append(c);
/*     */         } 
/*     */       }
/*     */     } 
/* 192 */     sb.append(quote);
/* 193 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static JExpression lit(char c) {
/* 197 */     return (JExpression)new JAtom(quotify('\'', "" + c));
/*     */   }
/*     */   
/*     */   public static JExpression lit(String s) {
/* 201 */     return (JExpression)new JStringLiteral(s);
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
/*     */   public static JExpression direct(String source) {
/* 217 */     return (JExpression)new Object(source);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JExpr.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */