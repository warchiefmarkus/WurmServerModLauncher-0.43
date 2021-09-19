/*    */ package com.sun.xml.bind.v2.model.core;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*    */ import com.sun.xml.bind.v2.model.impl.ModelBuilder;
/*    */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*    */ import javax.xml.bind.annotation.XmlList;
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
/*    */ public final class Ref<T, C>
/*    */ {
/*    */   public final T type;
/*    */   public final Adapter<T, C> adapter;
/*    */   public final boolean valueList;
/*    */   
/*    */   public Ref(T type) {
/* 71 */     this(type, null, false);
/*    */   }
/*    */   public Ref(T type, Adapter<T, C> adapter, boolean valueList) {
/*    */     TypeT typeT;
/* 75 */     this.adapter = adapter;
/* 76 */     if (adapter != null)
/* 77 */       typeT = adapter.defaultType; 
/* 78 */     this.type = (T)typeT;
/* 79 */     this.valueList = valueList;
/*    */   }
/*    */   
/*    */   public Ref(ModelBuilder<T, C, ?, ?> builder, T type, XmlJavaTypeAdapter xjta, XmlList xl) {
/* 83 */     this(builder.reader, builder.nav, type, xjta, xl);
/*    */   }
/*    */ 
/*    */   
/*    */   public Ref(AnnotationReader<T, C, ?, ?> reader, Navigator<T, C, ?, ?> nav, T type, XmlJavaTypeAdapter xjta, XmlList xl) {
/*    */     TypeT typeT;
/* 89 */     Adapter<T, C> adapter = null;
/* 90 */     if (xjta != null) {
/* 91 */       adapter = new Adapter<T, C>(xjta, reader, nav);
/* 92 */       typeT = adapter.defaultType;
/*    */     } 
/*    */     
/* 95 */     this.type = (T)typeT;
/* 96 */     this.adapter = adapter;
/* 97 */     this.valueList = (xl != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\Ref.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */