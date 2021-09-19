/*    */ package com.sun.xml.bind.v2.model.core;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*    */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Adapter<TypeT, ClassDeclT>
/*    */ {
/*    */   public final ClassDeclT adapterType;
/*    */   public final TypeT defaultType;
/*    */   public final TypeT customType;
/*    */   
/*    */   public Adapter(XmlJavaTypeAdapter spec, AnnotationReader<TypeT, ClassDeclT, ?, ?> reader, Navigator<TypeT, ClassDeclT, ?, ?> nav) {
/* 79 */     this((ClassDeclT)nav.asDecl(reader.getClassValue((Annotation)spec, "value")), nav);
/*    */   }
/*    */   
/*    */   public Adapter(ClassDeclT adapterType, Navigator<TypeT, ClassDeclT, ?, ?> nav) {
/* 83 */     this.adapterType = adapterType;
/* 84 */     TypeT baseClass = (TypeT)nav.getBaseClass(nav.use(adapterType), nav.asDecl(XmlAdapter.class));
/*    */ 
/*    */     
/* 87 */     assert baseClass != null;
/*    */     
/* 89 */     if (nav.isParameterizedType(baseClass)) {
/* 90 */       this.defaultType = (TypeT)nav.getTypeArgument(baseClass, 0);
/*    */     } else {
/* 92 */       this.defaultType = (TypeT)nav.ref(Object.class);
/*    */     } 
/* 94 */     if (nav.isParameterizedType(baseClass)) {
/* 95 */       this.customType = (TypeT)nav.getTypeArgument(baseClass, 1);
/*    */     } else {
/* 97 */       this.customType = (TypeT)nav.ref(Object.class);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\Adapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */