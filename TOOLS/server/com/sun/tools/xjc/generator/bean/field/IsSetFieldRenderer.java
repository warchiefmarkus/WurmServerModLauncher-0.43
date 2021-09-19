/*    */ package com.sun.tools.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IsSetFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private final FieldRenderer core;
/*    */   private final boolean generateUnSetMethod;
/*    */   private final boolean generateIsSetMethod;
/*    */   
/*    */   public IsSetFieldRenderer(FieldRenderer core, boolean generateUnSetMethod, boolean generateIsSetMethod) {
/* 63 */     this.core = core;
/* 64 */     this.generateUnSetMethod = generateUnSetMethod;
/* 65 */     this.generateIsSetMethod = generateIsSetMethod;
/*    */   }
/*    */   
/*    */   public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop) {
/* 69 */     return new IsSetField(context, prop, this.core.generate(context, prop), this.generateUnSetMethod, this.generateIsSetMethod);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\IsSetFieldRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */