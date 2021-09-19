/*    */ package com.sun.tools.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.tools.xjc.api.Property;
/*    */ import com.sun.tools.xjc.api.TypeAndAnnotation;
/*    */ import com.sun.tools.xjc.model.CClassInfo;
/*    */ import com.sun.tools.xjc.model.TypeUse;
/*    */ import java.util.List;
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
/*    */ final class BeanMappingImpl
/*    */   extends AbstractMappingImpl<CClassInfo>
/*    */ {
/* 54 */   private final TypeAndAnnotationImpl taa = new TypeAndAnnotationImpl(this.parent.outline, (TypeUse)this.clazz);
/*    */   
/*    */   BeanMappingImpl(JAXBModelImpl parent, CClassInfo classInfo) {
/* 57 */     super(parent, classInfo);
/* 58 */     assert classInfo.isElement();
/*    */   }
/*    */   
/*    */   public TypeAndAnnotation getType() {
/* 62 */     return this.taa;
/*    */   }
/*    */   
/*    */   public final String getTypeClass() {
/* 66 */     return getClazz();
/*    */   }
/*    */   
/*    */   public List<Property> calcDrilldown() {
/* 70 */     if (!this.clazz.isOrdered())
/* 71 */       return null; 
/* 72 */     return buildDrilldown(this.clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\BeanMappingImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */