/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.bean.field.FieldRenderer;
/*    */ import com.sun.tools.xjc.generator.bean.field.FieldRendererFactory;
/*    */ import com.sun.tools.xjc.model.Model;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ import javax.xml.bind.annotation.XmlValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class CollectionTypeAttribute
/*    */ {
/*    */   @XmlValue
/* 53 */   String collectionType = null;
/*    */ 
/*    */   
/*    */   @XmlTransient
/*    */   private FieldRenderer fr;
/*    */ 
/*    */ 
/*    */   
/*    */   FieldRenderer get(Model m) {
/* 62 */     if (this.fr == null)
/* 63 */       this.fr = calcFr(m); 
/* 64 */     return this.fr;
/*    */   }
/*    */   
/*    */   private FieldRenderer calcFr(Model m) {
/* 68 */     FieldRendererFactory frf = m.options.getFieldRendererFactory();
/* 69 */     if (this.collectionType == null) {
/* 70 */       return frf.getDefault();
/*    */     }
/* 72 */     if (this.collectionType.equals("indexed")) {
/* 73 */       return frf.getArray();
/*    */     }
/* 75 */     return frf.getList(m.codeModel.ref(this.collectionType));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\CollectionTypeAttribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */