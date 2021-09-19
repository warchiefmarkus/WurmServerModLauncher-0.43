/*    */ package com.sun.tools.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldRendererFactory
/*    */ {
/*    */   public FieldRenderer getDefault() {
/* 65 */     return this.DEFAULT;
/*    */   }
/*    */   public FieldRenderer getArray() {
/* 68 */     return ARRAY;
/*    */   }
/*    */   public FieldRenderer getRequiredUnboxed() {
/* 71 */     return REQUIRED_UNBOXED;
/*    */   }
/*    */   public FieldRenderer getSingle() {
/* 74 */     return SINGLE;
/*    */   }
/*    */   public FieldRenderer getSinglePrimitiveAccess() {
/* 77 */     return SINGLE_PRIMITIVE_ACCESS;
/*    */   }
/*    */   public FieldRenderer getList(JClass coreList) {
/* 80 */     return new UntypedListFieldRenderer(coreList);
/*    */   }
/*    */   public FieldRenderer getConst(FieldRenderer fallback) {
/* 83 */     return new ConstFieldRenderer(fallback);
/*    */   }
/*    */   
/* 86 */   private final FieldRenderer DEFAULT = new DefaultFieldRenderer(this);
/*    */ 
/*    */   
/* 89 */   private static final FieldRenderer ARRAY = new GenericFieldRenderer(ArrayField.class);
/*    */ 
/*    */   
/* 92 */   private static final FieldRenderer REQUIRED_UNBOXED = new GenericFieldRenderer(UnboxedField.class);
/*    */ 
/*    */   
/* 95 */   private static final FieldRenderer SINGLE = new GenericFieldRenderer(SingleField.class);
/*    */ 
/*    */   
/* 98 */   private static final FieldRenderer SINGLE_PRIMITIVE_ACCESS = new GenericFieldRenderer(SinglePrimitiveAccessField.class);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\FieldRendererFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */