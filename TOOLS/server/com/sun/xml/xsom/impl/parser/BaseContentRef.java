/*    */ package com.sun.xml.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.xsom.XSContentType;
/*    */ import com.sun.xml.xsom.XSType;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class BaseContentRef implements Ref.ContentType, Patch {
/*    */   private final Ref.Type baseType;
/*    */   private final Locator loc;
/*    */   
/*    */   public BaseContentRef(final NGCCRuntimeEx $runtime, Ref.Type _baseType) {
/* 14 */     this.baseType = _baseType;
/* 15 */     $runtime.addPatcher(this);
/* 16 */     $runtime.addErrorChecker(new Patch() {
/*    */           public void run() throws SAXException {
/* 18 */             XSType t = BaseContentRef.this.baseType.getType();
/* 19 */             if (t.isComplexType() && t.asComplexType().getContentType().asParticle() != null) {
/* 20 */               $runtime.reportError(Messages.format("SimpleContentExpected", new Object[] { t.getTargetNamespace(), t.getName() }), BaseContentRef.this.loc);
/*    */             }
/*    */           }
/*    */         });
/*    */ 
/*    */     
/* 26 */     this.loc = $runtime.copyLocator();
/*    */   }
/*    */   
/*    */   public XSContentType getContentType() {
/* 30 */     XSType t = this.baseType.getType();
/* 31 */     if (t.asComplexType() != null) {
/* 32 */       return t.asComplexType().getContentType();
/*    */     }
/* 34 */     return (XSContentType)t.asSimpleType();
/*    */   }
/*    */   
/*    */   public void run() throws SAXException {
/* 38 */     if (this.baseType instanceof Patch)
/* 39 */       ((Patch)this.baseType).run(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\BaseContentRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */