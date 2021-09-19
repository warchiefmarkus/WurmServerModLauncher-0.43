/*    */ package com.sun.tools.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.xjc.outline.FieldOutline;
/*    */ import com.sun.tools.xjc.outline.Outline;
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
/*    */ final class ConstFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private final FieldRenderer fallback;
/*    */   
/*    */   protected ConstFieldRenderer(FieldRenderer fallback) {
/* 58 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public FieldOutline generate(ClassOutlineImpl outline, CPropertyInfo prop) {
/* 62 */     if (prop.defaultValue.compute((Outline)outline.parent()) == null) {
/* 63 */       return this.fallback.generate(outline, prop);
/*    */     }
/* 65 */     return new ConstField(outline, prop);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\ConstFieldRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */