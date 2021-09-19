/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JDeclaration;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JVar;
/*     */ import java.io.PrintWriter;
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
/*     */ public class JFormatter
/*     */ {
/*     */   private int indentLevel;
/*     */   private String indentSpace;
/*     */   private PrintWriter pw;
/*     */   private char lastChar;
/*     */   private boolean atBeginningOfLine;
/*     */   
/*     */   public JFormatter(PrintWriter s, String space) {
/* 119 */     this.lastChar = Character.MIN_VALUE;
/* 120 */     this.atBeginningOfLine = true; this.pw = s;
/*     */     this.indentSpace = space;
/*     */   } public JFormatter(PrintWriter s) { this(s, "    "); } public void close() { this.pw.close(); }
/* 123 */   private void spaceIfNeeded(char c) { if (this.atBeginningOfLine) {
/* 124 */       for (int i = 0; i < this.indentLevel; i++)
/* 125 */         this.pw.print(this.indentSpace); 
/* 126 */       this.atBeginningOfLine = false;
/* 127 */     } else if (this.lastChar != '\000' && needSpace(this.lastChar, c)) {
/* 128 */       this.pw.print(' ');
/*     */     }  }
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JFormatter o() {
/*     */     this.indentLevel--;
/*     */     return this;
/*     */   }
/*     */   
/* 137 */   public com.sun.codemodel.JFormatter p(char c) { spaceIfNeeded(c);
/* 138 */     this.pw.print(c);
/* 139 */     this.lastChar = c;
/* 140 */     return this; }
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JFormatter i() {
/*     */     this.indentLevel++;
/*     */     return this;
/*     */   }
/*     */   
/*     */   public com.sun.codemodel.JFormatter p(String s) {
/* 149 */     spaceIfNeeded(s.charAt(0));
/* 150 */     this.pw.print(s);
/* 151 */     this.lastChar = s.charAt(s.length() - 1);
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JFormatter nl() {
/* 159 */     this.pw.println();
/* 160 */     this.lastChar = Character.MIN_VALUE;
/* 161 */     this.atBeginningOfLine = true;
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JFormatter g(JGenerable g) {
/* 171 */     g.generate(this);
/* 172 */     return this;
/*     */   } private boolean needSpace(char c1, char c2) { if (c1 == ']' && c2 == '{')
/*     */       return true;  if (c1 == ';')
/*     */       return true;  if (c1 == ')' && c2 == '{')
/*     */       return true;  if (c1 == ',' || c1 == '=')
/*     */       return true;  if (c2 == '=')
/*     */       return true;  if (Character.isDigit(c1)) { if (c2 == '(' || c2 == ')' || c2 == ';' || c2 == ',')
/*     */         return false;  return true; }  if (Character.isJavaIdentifierPart(c1)) { switch (c2) { case '+': case '>': case '{': case '}': return true; }  return Character.isJavaIdentifierStart(c2); }  if (Character.isJavaIdentifierStart(c2)) { switch (c1) { case ')': case '+': case ']': case '}':
/*     */           return true; }  return false; }  if (Character.isDigit(c2)) { if (c1 == '(')
/* 181 */         return false;  return true; }  return false; } public com.sun.codemodel.JFormatter d(JDeclaration d) { d.declare(this);
/* 182 */     return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JFormatter s(JStatement s) {
/* 191 */     s.state(this);
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JFormatter b(JVar v) {
/* 201 */     v.bind(this);
/* 202 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JFormatter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */