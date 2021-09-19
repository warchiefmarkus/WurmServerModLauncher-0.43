/*    */ package com.sun.tools.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.codemodel.JAssignmentTarget;
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JConditional;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.codemodel.JVar;
/*    */ import com.sun.tools.xjc.model.CElementInfo;
/*    */ import com.sun.tools.xjc.outline.Aspect;
/*    */ import com.sun.tools.xjc.outline.FieldAccessor;
/*    */ import com.sun.tools.xjc.outline.FieldOutline;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ElementSingleAdapter
/*    */   extends ElementAdapter
/*    */ {
/*    */   public ElementSingleAdapter(FieldOutline core, CElementInfo ei) {
/* 62 */     super(core, ei);
/*    */   }
/*    */   
/*    */   public JType getRawType() {
/* 66 */     return this.ei.getContentInMemoryType().toType(outline(), Aspect.EXPOSED);
/*    */   }
/*    */   
/*    */   public FieldAccessor create(JExpression targetObject) {
/* 70 */     return new FieldAccessorImpl(targetObject);
/*    */   }
/*    */   
/*    */   final class FieldAccessorImpl extends ElementAdapter.FieldAccessorImpl {
/*    */     public FieldAccessorImpl(JExpression target) {
/* 75 */       super(target);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void toRawValue(JBlock block, JVar $var) {
/* 85 */       JConditional cond = block._if(this.acc.hasSetValue());
/* 86 */       JVar $v = cond._then().decl(ElementSingleAdapter.this.core.getRawType(), "v" + hashCode());
/* 87 */       this.acc.toRawValue(cond._then(), $v);
/* 88 */       cond._then().assign((JAssignmentTarget)$var, (JExpression)$v.invoke("getValue"));
/* 89 */       cond._else().assign((JAssignmentTarget)$var, JExpr._null());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 96 */       this.acc.fromRawValue(block, uniqueName, (JExpression)createJAXBElement($var));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\ElementSingleAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */