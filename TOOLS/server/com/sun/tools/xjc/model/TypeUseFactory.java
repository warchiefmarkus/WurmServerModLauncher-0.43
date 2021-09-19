/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.xml.bind.v2.TODO;
/*    */ import com.sun.xml.bind.v2.model.core.ID;
/*    */ import javax.activation.MimeType;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TypeUseFactory
/*    */ {
/*    */   public static TypeUse makeID(TypeUse t, ID id) {
/* 55 */     if (t.idUse() != ID.NONE)
/*    */     {
/*    */       
/* 58 */       throw new IllegalStateException(); } 
/* 59 */     return new TypeUseImpl(t.getInfo(), t.isCollection(), id, t.getExpectedMimeType(), t.getAdapterUse());
/*    */   }
/*    */   
/*    */   public static TypeUse makeMimeTyped(TypeUse t, MimeType mt) {
/* 63 */     if (t.getExpectedMimeType() != null)
/*    */     {
/*    */       
/* 66 */       throw new IllegalStateException(); } 
/* 67 */     return new TypeUseImpl(t.getInfo(), t.isCollection(), t.idUse(), mt, t.getAdapterUse());
/*    */   }
/*    */   
/*    */   public static TypeUse makeCollection(TypeUse t) {
/* 71 */     if (t.isCollection()) return t; 
/* 72 */     CAdapter au = t.getAdapterUse();
/* 73 */     if (au != null && !au.isWhitespaceAdapter()) {
/*    */ 
/*    */       
/* 76 */       TODO.checkSpec();
/* 77 */       return CBuiltinLeafInfo.STRING_LIST;
/*    */     } 
/* 79 */     return new TypeUseImpl(t.getInfo(), true, t.idUse(), t.getExpectedMimeType(), null);
/*    */   }
/*    */   
/*    */   public static TypeUse adapt(TypeUse t, CAdapter adapter) {
/* 83 */     assert t.getAdapterUse() == null;
/* 84 */     return new TypeUseImpl(t.getInfo(), t.isCollection(), t.idUse(), t.getExpectedMimeType(), adapter);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TypeUse adapt(TypeUse t, Class<? extends XmlAdapter> adapter, boolean copy) {
/* 91 */     return adapt(t, new CAdapter(adapter, copy));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\TypeUseFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */