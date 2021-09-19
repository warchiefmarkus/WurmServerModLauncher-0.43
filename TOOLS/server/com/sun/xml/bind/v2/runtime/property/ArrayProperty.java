/*    */ package com.sun.xml.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*    */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*    */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*    */ import com.sun.xml.bind.v2.runtime.reflect.Lister;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class ArrayProperty<BeanT, ListT, ItemT>
/*    */   extends PropertyImpl<BeanT>
/*    */ {
/*    */   protected final Accessor<BeanT, ListT> acc;
/*    */   protected final Lister<BeanT, ListT, ItemT, Object> lister;
/*    */   
/*    */   protected ArrayProperty(JAXBContextImpl context, RuntimePropertyInfo prop) {
/* 59 */     super(context, prop);
/*    */     
/* 61 */     assert prop.isCollection();
/* 62 */     this.lister = Lister.create(Navigator.REFLECTION.erasure(prop.getRawType()), prop.id(), prop.getAdapter());
/*    */     
/* 64 */     assert this.lister != null;
/* 65 */     this.acc = prop.getAccessor().optimize(context);
/* 66 */     assert this.acc != null;
/*    */   }
/*    */   
/*    */   public void reset(BeanT o) throws AccessorException {
/* 70 */     this.lister.reset(o, this.acc);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getIdValue(BeanT bean) {
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\ArrayProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */