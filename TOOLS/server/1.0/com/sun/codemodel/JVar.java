/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JDeclaration;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JExpressionImpl;
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JMods;
/*     */ import com.sun.codemodel.JType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JVar
/*     */   extends JExpressionImpl
/*     */   implements JDeclaration, JAssignmentTarget
/*     */ {
/*     */   private JMods mods;
/*     */   JType type;
/*     */   String name;
/*     */   JExpression init;
/*     */   
/*     */   JVar(JMods mods, JType type, String name, JExpression init) {
/*  48 */     this.mods = mods;
/*  49 */     this.type = type;
/*  50 */     this.name = name;
/*  51 */     this.init = init;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JVar init(JExpression init) {
/*  61 */     this.init = init;
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  71 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType type() {
/*  78 */     return this.type;
/*     */   }
/*     */   
/*     */   public void bind(JFormatter f) {
/*  82 */     f.g((JGenerable)this.mods).g((JGenerable)this.type).p(this.name);
/*  83 */     if (this.init != null)
/*  84 */       f.p('=').g((JGenerable)this.init); 
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/*  88 */     f.b(this).p(';').nl();
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/*  92 */     f.p(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression assign(JExpression rhs) {
/* 101 */     return JExpr.assign(this, rhs);
/*     */   }
/*     */   public JExpression assignPlus(JExpression rhs) {
/* 104 */     return JExpr.assignPlus(this, rhs);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JVar.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */