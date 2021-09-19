/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.tools.xjc.outline.Outline;
/*    */ import com.sun.xml.xsom.XmlString;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CDefaultValue
/*    */ {
/*    */   public abstract JExpression compute(Outline paramOutline);
/*    */   
/*    */   public static CDefaultValue create(final TypeUse typeUse, final XmlString defaultValue) {
/* 60 */     return new CDefaultValue() {
/*    */         public JExpression compute(Outline outline) {
/* 62 */           return typeUse.createConstant(outline, defaultValue);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CDefaultValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */