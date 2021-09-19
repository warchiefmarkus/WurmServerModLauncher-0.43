/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.ForeignAttributes;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.AttributesImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ForeignAttributesImpl
/*    */   extends AttributesImpl
/*    */   implements ForeignAttributes
/*    */ {
/*    */   private final ValidationContext context;
/*    */   private final Locator locator;
/*    */   final ForeignAttributesImpl next;
/*    */   
/*    */   public ForeignAttributesImpl(ValidationContext context, Locator locator, ForeignAttributesImpl next) {
/* 22 */     this.context = context;
/* 23 */     this.locator = locator;
/* 24 */     this.next = next;
/*    */   }
/*    */   
/*    */   public ValidationContext getContext() {
/* 28 */     return this.context;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 32 */     return this.locator;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ForeignAttributesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */