/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.bind.IDResolver;
/*    */ import java.util.HashMap;
/*    */ import java.util.concurrent.Callable;
/*    */ import javax.xml.bind.ValidationEventHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DefaultIDResolver
/*    */   extends IDResolver
/*    */ {
/* 55 */   private HashMap<String, Object> idmap = null;
/*    */ 
/*    */   
/*    */   public void startDocument(ValidationEventHandler eventHandler) throws SAXException {
/* 59 */     if (this.idmap != null) {
/* 60 */       this.idmap.clear();
/*    */     }
/*    */   }
/*    */   
/*    */   public void bind(String id, Object obj) {
/* 65 */     if (this.idmap == null) this.idmap = new HashMap<String, Object>(); 
/* 66 */     this.idmap.put(id, obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public Callable resolve(final String id, Class targetType) {
/* 71 */     return new Callable() {
/*    */         public Object call() throws Exception {
/* 73 */           if (DefaultIDResolver.this.idmap == null) return null; 
/* 74 */           return DefaultIDResolver.this.idmap.get(id);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\DefaultIDResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */