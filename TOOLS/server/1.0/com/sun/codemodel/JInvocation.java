/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JAnonymousClass;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JExpressionImpl;
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class JInvocation
/*     */   extends JExpressionImpl
/*     */   implements JStatement
/*     */ {
/*     */   private JGenerable object;
/*     */   private String name;
/*     */   private boolean isConstructor = false;
/*  35 */   private List args = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private JType type = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JInvocation(JExpression object, String name) {
/*  54 */     this((JGenerable)object, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JInvocation(JClass type, String name) {
/*  61 */     this((JGenerable)type, name);
/*     */   }
/*     */   
/*     */   private JInvocation(JGenerable object, String name) {
/*  65 */     this.object = object;
/*  66 */     if (name.indexOf('.') >= 0) {
/*  67 */       throw new IllegalArgumentException("JClass name contains '.': " + name);
/*     */     }
/*  69 */     this.name = name;
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
/*     */   JInvocation(JType c) {
/*  83 */     this.object = null;
/*  84 */     this.name = c.fullName();
/*  85 */     this.isConstructor = true;
/*  86 */     this.type = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JInvocation arg(JExpression arg) {
/*  96 */     if (arg == null) throw new IllegalArgumentException(); 
/*  97 */     this.args.add(arg);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(JFormatter f) {
/* 103 */     if (this.isConstructor && this.type.isArray()) {
/*     */       
/* 105 */       f.p("new").p(this.name).p('{');
/*     */     }
/* 107 */     else if (this.isConstructor) {
/* 108 */       f.p("new").p(this.name).p('(');
/* 109 */     } else if (this.object != null) {
/* 110 */       f.g(this.object).p('.').p(this.name).p('(');
/*     */     } else {
/* 112 */       f.p(this.name).p('(');
/*     */     } 
/*     */     
/* 115 */     boolean first = true;
/* 116 */     for (Iterator i = this.args.iterator(); i.hasNext(); ) {
/* 117 */       if (!first) f.p(','); 
/* 118 */       f.g((JGenerable)i.next());
/* 119 */       first = false;
/*     */     } 
/*     */     
/* 122 */     if (this.isConstructor && this.type.isArray()) {
/* 123 */       f.p('}');
/*     */     } else {
/* 125 */       f.p(')');
/*     */     } 
/* 127 */     if (this.type instanceof JAnonymousClass) {
/* 128 */       ((JAnonymousClass)this.type).declareBody(f);
/*     */     }
/*     */   }
/*     */   
/*     */   public void state(JFormatter f) {
/* 133 */     f.g((JGenerable)this).p(';').nl();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JInvocation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */