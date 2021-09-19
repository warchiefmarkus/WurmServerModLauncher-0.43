/*     */ package 1.0.com.sun.codemodel;
/*     */ import com.sun.codemodel.JAssignment;
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JDoLoop;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JForLoop;
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JLabel;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JSwitch;
/*     */ import com.sun.codemodel.JTryBlock;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.codemodel.JWhileLoop;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class JBlock implements JGenerable, JStatement {
/*  22 */   private final List content = new ArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bracesRequired = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean indentRequired = true;
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static com.sun.codemodel.JBlock dummyInstance = new com.sun.codemodel.JBlock();
/*     */   
/*     */   JBlock() {
/*  37 */     this(true, true);
/*     */   }
/*     */   
/*     */   JBlock(boolean bracesRequired, boolean indentRequired) {
/*  41 */     this.bracesRequired = bracesRequired;
/*  42 */     this.indentRequired = indentRequired;
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
/*     */   public JVar decl(JType type, String name) {
/*  59 */     return decl(0, type, name, null);
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
/*  77 */     return decl(0, type, name, init);
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
/*  98 */     JVar v = new JVar(JMods.forVar(mods), type, name, init);
/*  99 */     this.content.add(v);
/* 100 */     this.bracesRequired = true;
/* 101 */     this.indentRequired = true;
/* 102 */     return v;
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
/*     */   public com.sun.codemodel.JBlock assign(JAssignmentTarget lhs, JExpression exp) {
/* 115 */     this.content.add(new JAssignment(lhs, exp));
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public com.sun.codemodel.JBlock assignPlus(JAssignmentTarget lhs, JExpression exp) {
/* 120 */     this.content.add(new JAssignment(lhs, exp, "+"));
/* 121 */     return this;
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
/* 137 */     JInvocation i = new JInvocation(expr, method);
/* 138 */     this.content.add(i);
/* 139 */     return i;
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
/* 155 */     return invoke(expr, method.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation staticInvoke(JClass type, String method) {
/* 162 */     JInvocation i = new JInvocation(type, method);
/* 163 */     this.content.add(i);
/* 164 */     return i;
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
/* 176 */     JInvocation i = new JInvocation((JExpression)null, method);
/* 177 */     this.content.add(i);
/* 178 */     return i;
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
/* 190 */     return invoke(method.name());
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
/*     */   public com.sun.codemodel.JBlock add(JStatement s) {
/* 202 */     this.content.add(s);
/* 203 */     return this;
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
/* 215 */     JConditional c = new JConditional(expr);
/* 216 */     this.content.add(c);
/* 217 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JForLoop _for() {
/* 226 */     JForLoop f = new JForLoop();
/* 227 */     this.content.add(f);
/* 228 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JWhileLoop _while(JExpression test) {
/* 237 */     JWhileLoop w = new JWhileLoop(test);
/* 238 */     this.content.add(w);
/* 239 */     return w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSwitch _switch(JExpression test) {
/* 246 */     JSwitch s = new JSwitch(test);
/* 247 */     this.content.add(s);
/* 248 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDoLoop _do(JExpression test) {
/* 257 */     JDoLoop d = new JDoLoop(test);
/* 258 */     this.content.add(d);
/* 259 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTryBlock _try() {
/* 268 */     JTryBlock t = new JTryBlock();
/* 269 */     this.content.add(t);
/* 270 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _return() {
/* 277 */     this.content.add(new JReturn(null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _return(JExpression exp) {
/* 284 */     this.content.add(new JReturn(exp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _throw(JExpression exp) {
/* 291 */     this.content.add(new JThrow(exp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _break() {
/* 298 */     _break(null);
/*     */   }
/*     */   
/*     */   public void _break(JLabel label) {
/* 302 */     this.content.add(new JBreak(label));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel label(String name) {
/* 310 */     JLabel l = new JLabel(name);
/* 311 */     this.content.add(l);
/* 312 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _continue(JLabel label) {
/* 319 */     this.content.add(new JContinue(label));
/*     */   }
/*     */   
/*     */   public void _continue() {
/* 323 */     _continue(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JBlock block() {
/* 330 */     com.sun.codemodel.JBlock b = new com.sun.codemodel.JBlock();
/* 331 */     b.bracesRequired = false;
/* 332 */     b.indentRequired = false;
/* 333 */     this.content.add(b);
/* 334 */     return b;
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
/*     */   public JStatement directStatement(String source) {
/* 349 */     Object object = new Object(this, source);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     add((JStatement)object);
/* 355 */     return (JStatement)object;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 359 */     if (this.bracesRequired)
/* 360 */       f.p('{').nl(); 
/* 361 */     if (this.indentRequired)
/* 362 */       f.i(); 
/* 363 */     for (Iterator i = this.content.iterator(); i.hasNext(); ) {
/* 364 */       Object o = i.next();
/* 365 */       if (o instanceof JDeclaration) {
/* 366 */         f.d((JDeclaration)o); continue;
/*     */       } 
/* 368 */       f.s((JStatement)o);
/*     */     } 
/* 370 */     if (this.indentRequired)
/* 371 */       f.o(); 
/* 372 */     if (this.bracesRequired)
/* 373 */       f.p('}'); 
/*     */   }
/*     */   
/*     */   public void state(JFormatter f) {
/* 377 */     f.g(this);
/* 378 */     if (this.bracesRequired)
/* 379 */       f.nl(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JBlock.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */