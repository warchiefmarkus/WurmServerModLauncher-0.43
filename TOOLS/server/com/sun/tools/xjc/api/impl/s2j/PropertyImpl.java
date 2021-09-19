/*    */ package com.sun.tools.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.api.Mapping;
/*    */ import com.sun.tools.xjc.api.Property;
/*    */ import com.sun.tools.xjc.outline.FieldOutline;
/*    */ import javax.xml.namespace.QName;
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
/*    */ public final class PropertyImpl
/*    */   implements Property
/*    */ {
/*    */   protected final FieldOutline fr;
/*    */   protected final QName elementName;
/*    */   protected final Mapping parent;
/*    */   protected final JCodeModel codeModel;
/*    */   
/*    */   PropertyImpl(Mapping parent, FieldOutline fr, QName elementName) {
/* 57 */     this.parent = parent;
/* 58 */     this.fr = fr;
/* 59 */     this.elementName = elementName;
/* 60 */     this.codeModel = fr.getRawType().owner();
/*    */   }
/*    */   
/*    */   public final String name() {
/* 64 */     return this.fr.getPropertyInfo().getName(false);
/*    */   }
/*    */   
/*    */   public final QName elementName() {
/* 68 */     return this.elementName;
/*    */   }
/*    */   
/*    */   public final JType type() {
/* 72 */     return this.fr.getRawType();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\PropertyImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */