/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public final class JBlock
/*     */   implements JGenerable, JStatement
/*     */ {
/*  42 */   private final List<Object> content = new ArrayList();
/*     */ 
/*     */   
/*     */   private boolean bracesRequired = true;
/*     */ 
/*     */   
/*     */   private boolean indentRequired = true;
/*     */ 
/*     */   
/*     */   private int pos;
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock() {
/*  56 */     this(true, true);
/*     */   }
/*     */   
/*     */   public JBlock(boolean bracesRequired, boolean indentRequired) {
/*  60 */     this.bracesRequired = bracesRequired;
/*  61 */     this.indentRequired = indentRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Object> getContents() {
/*  69 */     return Collections.unmodifiableList(this.content);
/*     */   }
/*     */   
/*     */   private <T> T insert(T statementOrDeclaration) {
/*  73 */     this.content.add(this.pos, statementOrDeclaration);
/*  74 */     this.pos++;
/*  75 */     return statementOrDeclaration;
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
/*     */   public int pos() {
/*  87 */     return this.pos;
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
/*     */   public int pos(int newPos) {
/* 101 */     int r = this.pos;
/* 102 */     if (newPos > this.content.size() || newPos < 0)
/* 103 */       throw new IllegalArgumentException(); 
/* 104 */     this.pos = newPos;
/*     */     
/* 106 */     return r;
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
/*     */   public JVar decl(JType type, String name) {
/* 122 */     return decl(0, type, name, null);
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
/*     */ 
/*     */   
/*     */   public JVar decl(JType type, String name, JExpression init) {
/* 140 */     return decl(0, type, name, init);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar decl(int mods, JType type, String name, JExpression init) {
/* 161 */     JVar v = new JVar(JMods.forVar(mods), type, name, init);
/* 162 */     insert(v);
/* 163 */     this.bracesRequired = true;
/* 164 */     this.indentRequired = true;
/* 165 */     return v;
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
/*     */   public JBlock assign(JAssignmentTarget lhs, JExpression exp) {
/* 178 */     insert(new JAssignment(lhs, exp));
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   public JBlock assignPlus(JAssignmentTarget lhs, JExpression exp) {
/* 183 */     insert(new JAssignment(lhs, exp, "+"));
/* 184 */     return this;
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
/*     */   public JInvocation invoke(JExpression expr, String method) {
/* 200 */     JInvocation i = new JInvocation(expr, method);
/* 201 */     insert(i);
/* 202 */     return i;
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
/*     */   public JInvocation invoke(JExpression expr, JMethod method) {
/* 218 */     return insert(new JInvocation(expr, method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation staticInvoke(JClass type, String method) {
/* 225 */     return insert(new JInvocation(type, method));
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
/*     */   public JInvocation invoke(String method) {
/* 237 */     return insert(new JInvocation((JExpression)null, method));
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
/*     */   public JInvocation invoke(JMethod method) {
/* 249 */     return insert(new JInvocation((JExpression)null, method));
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
/*     */   public JBlock add(JStatement s) {
/* 261 */     insert(s);
/* 262 */     return this;
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
/*     */   public JConditional _if(JExpression expr) {
/* 274 */     return insert(new JConditional(expr));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JForLoop _for() {
/* 283 */     return insert(new JForLoop());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JWhileLoop _while(JExpression test) {
/* 292 */     return insert(new JWhileLoop(test));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSwitch _switch(JExpression test) {
/* 299 */     return insert(new JSwitch(test));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDoLoop _do(JExpression test) {
/* 308 */     return insert(new JDoLoop(test));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTryBlock _try() {
/* 317 */     return insert(new JTryBlock());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _return() {
/* 324 */     insert(new JReturn(null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _return(JExpression exp) {
/* 331 */     insert(new JReturn(exp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _throw(JExpression exp) {
/* 338 */     insert(new JThrow(exp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _break() {
/* 345 */     _break(null);
/*     */   }
/*     */   
/*     */   public void _break(JLabel label) {
/* 349 */     insert(new JBreak(label));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel label(String name) {
/* 357 */     JLabel l = new JLabel(name);
/* 358 */     insert(l);
/* 359 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _continue(JLabel label) {
/* 366 */     insert(new JContinue(label));
/*     */   }
/*     */   
/*     */   public void _continue() {
/* 370 */     _continue(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock block() {
/* 377 */     JBlock b = new JBlock();
/* 378 */     b.bracesRequired = false;
/* 379 */     b.indentRequired = false;
/* 380 */     return insert(b);
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
/*     */   public JStatement directStatement(final String source) {
/* 395 */     JStatement s = new JStatement() {
/*     */         public void state(JFormatter f) {
/* 397 */           f.p(source).nl();
/*     */         }
/*     */       };
/* 400 */     add(s);
/* 401 */     return s;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 405 */     if (this.bracesRequired)
/* 406 */       f.p('{').nl(); 
/* 407 */     if (this.indentRequired)
/* 408 */       f.i(); 
/* 409 */     generateBody(f);
/* 410 */     if (this.indentRequired)
/* 411 */       f.o(); 
/* 412 */     if (this.bracesRequired)
/* 413 */       f.p('}'); 
/*     */   }
/*     */   
/*     */   void generateBody(JFormatter f) {
/* 417 */     for (Object o : this.content) {
/* 418 */       if (o instanceof JDeclaration) {
/* 419 */         f.d((JDeclaration)o); continue;
/*     */       } 
/* 421 */       f.s((JStatement)o);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JForEach forEach(JType varType, String name, JExpression collection) {
/* 433 */     return insert(new JForEach(varType, name, collection));
/*     */   }
/*     */   
/*     */   public void state(JFormatter f) {
/* 437 */     f.g(this);
/* 438 */     if (this.bracesRequired)
/* 439 */       f.nl(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */