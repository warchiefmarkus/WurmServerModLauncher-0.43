/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JAssignmentTarget;
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JMods;
/*    */ import com.sun.codemodel.JStatement;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.codemodel.JVar;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class JForLoop
/*    */   implements JStatement {
/* 19 */   private List inits = new ArrayList();
/* 20 */   private JExpression test = null;
/* 21 */   private List updates = new ArrayList();
/* 22 */   private JBlock body = null;
/*    */   
/*    */   public JVar init(int mods, JType type, String var, JExpression e) {
/* 25 */     JVar v = new JVar(JMods.forVar(mods), type, var, e);
/* 26 */     this.inits.add(v);
/* 27 */     return v;
/*    */   }
/*    */   
/*    */   public JVar init(JType type, String var, JExpression e) {
/* 31 */     return init(0, type, var, e);
/*    */   }
/*    */   
/*    */   public void init(JVar v, JExpression e) {
/* 35 */     this.inits.add(JExpr.assign((JAssignmentTarget)v, e));
/*    */   }
/*    */   
/*    */   public void test(JExpression e) {
/* 39 */     this.test = e;
/*    */   }
/*    */   
/*    */   public void update(JExpression e) {
/* 43 */     this.updates.add(e);
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 47 */     if (this.body == null) this.body = new JBlock(); 
/* 48 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 52 */     f.p("for (");
/* 53 */     boolean first = true; Iterator i;
/* 54 */     for (i = this.inits.iterator(); i.hasNext(); ) {
/* 55 */       if (!first) f.p(','); 
/* 56 */       Object o = i.next();
/* 57 */       if (o instanceof JVar) {
/* 58 */         f.b((JVar)o);
/*    */       } else {
/* 60 */         f.g((JGenerable)o);
/* 61 */       }  first = false;
/*    */     } 
/* 63 */     f.p(';').g((JGenerable)this.test).p(';');
/* 64 */     first = true;
/* 65 */     for (i = this.updates.iterator(); i.hasNext(); ) {
/* 66 */       if (!first) f.p(','); 
/* 67 */       f.g((JGenerable)i.next());
/* 68 */       first = false;
/*    */     } 
/* 70 */     f.p(')');
/* 71 */     if (this.body != null) {
/* 72 */       f.g((JGenerable)this.body).nl();
/*    */     } else {
/* 74 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JForLoop.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */