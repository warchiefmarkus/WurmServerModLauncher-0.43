/*    */ package com.sun.tools.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.xjc.outline.FieldOutline;
/*    */ import java.util.ArrayList;
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
/*    */ final class DefaultFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private final FieldRendererFactory frf;
/*    */   private FieldRenderer defaultCollectionFieldRenderer;
/*    */   
/*    */   DefaultFieldRenderer(FieldRendererFactory frf) {
/* 63 */     this.frf = frf;
/*    */   }
/*    */   
/*    */   public DefaultFieldRenderer(FieldRendererFactory frf, FieldRenderer defaultCollectionFieldRenderer) {
/* 67 */     this.frf = frf;
/* 68 */     this.defaultCollectionFieldRenderer = defaultCollectionFieldRenderer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FieldOutline generate(ClassOutlineImpl outline, CPropertyInfo prop) {
/* 75 */     return decideRenderer(outline, prop).generate(outline, prop);
/*    */   }
/*    */   
/*    */   private FieldRenderer decideRenderer(ClassOutlineImpl outline, CPropertyInfo prop) {
/* 79 */     if (!prop.isCollection()) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 84 */       if (prop.isUnboxable())
/*    */       {
/* 86 */         return this.frf.getRequiredUnboxed();
/*    */       }
/*    */       
/* 89 */       return this.frf.getSingle();
/*    */     } 
/*    */     
/* 92 */     if (this.defaultCollectionFieldRenderer == null) {
/* 93 */       return this.frf.getList(outline.parent().getCodeModel().ref(ArrayList.class));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 99 */     return this.defaultCollectionFieldRenderer;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\DefaultFieldRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */