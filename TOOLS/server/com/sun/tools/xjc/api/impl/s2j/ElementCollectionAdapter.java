/*     */ package com.sun.tools.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JForEach;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.xjc.outline.FieldOutline;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ElementCollectionAdapter
/*     */   extends ElementAdapter
/*     */ {
/*     */   public ElementCollectionAdapter(FieldOutline core, CElementInfo ei) {
/*  66 */     super(core, ei);
/*     */   }
/*     */   
/*     */   public JType getRawType() {
/*  70 */     return (JType)codeModel().ref(List.class).narrow(itemType().boxify());
/*     */   }
/*     */   
/*     */   private JType itemType() {
/*  74 */     return this.ei.getContentInMemoryType().toType(outline(), Aspect.EXPOSED);
/*     */   }
/*     */   
/*     */   public FieldAccessor create(JExpression targetObject) {
/*  78 */     return new FieldAccessorImpl(targetObject);
/*     */   }
/*     */   
/*     */   final class FieldAccessorImpl extends ElementAdapter.FieldAccessorImpl {
/*     */     public FieldAccessorImpl(JExpression target) {
/*  83 */       super(target);
/*     */     }
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/*  87 */       JCodeModel cm = ElementCollectionAdapter.this.outline().getCodeModel();
/*  88 */       JClass elementType = ElementCollectionAdapter.this.ei.toType(ElementCollectionAdapter.this.outline(), Aspect.EXPOSED).boxify();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       block.assign((JAssignmentTarget)$var, (JExpression)JExpr._new(cm.ref(ArrayList.class).narrow(ElementCollectionAdapter.this.itemType().boxify())));
/* 100 */       JVar $col = block.decl(ElementCollectionAdapter.this.core.getRawType(), "col" + hashCode());
/* 101 */       this.acc.toRawValue(block, $col);
/* 102 */       JForEach loop = block.forEach((JType)elementType, "v" + hashCode(), (JExpression)$col);
/*     */       
/* 104 */       JConditional cond = loop.body()._if(loop.var().eq(JExpr._null()));
/* 105 */       cond._then().invoke((JExpression)$var, "add").arg(JExpr._null());
/* 106 */       cond._else().invoke((JExpression)$var, "add").arg((JExpression)loop.var().invoke("getValue"));
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 110 */       JCodeModel cm = ElementCollectionAdapter.this.outline().getCodeModel();
/* 111 */       JClass elementType = ElementCollectionAdapter.this.ei.toType(ElementCollectionAdapter.this.outline(), Aspect.EXPOSED).boxify();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       JClass col = cm.ref(ArrayList.class).narrow(elementType);
/* 121 */       JVar $t = block.decl((JType)col, uniqueName + "_col", (JExpression)JExpr._new(col));
/*     */       
/* 123 */       JForEach loop = block.forEach(ElementCollectionAdapter.this.itemType(), uniqueName + "_i", (JExpression)$t);
/* 124 */       loop.body().invoke($var, "add").arg((JExpression)createJAXBElement((JExpression)loop.var()));
/*     */       
/* 126 */       this.acc.fromRawValue(block, uniqueName, (JExpression)$t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\ElementCollectionAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */