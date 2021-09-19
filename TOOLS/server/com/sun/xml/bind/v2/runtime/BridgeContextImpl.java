/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.bind.api.BridgeContext;
/*    */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.ValidationEventHandler;
/*    */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*    */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BridgeContextImpl
/*    */   extends BridgeContext
/*    */ {
/*    */   public final UnmarshallerImpl unmarshaller;
/*    */   public final MarshallerImpl marshaller;
/*    */   
/*    */   BridgeContextImpl(JAXBContextImpl context) {
/* 58 */     this.unmarshaller = context.createUnmarshaller();
/* 59 */     this.marshaller = context.createMarshaller();
/*    */   }
/*    */   
/*    */   public void setErrorHandler(ValidationEventHandler handler) {
/*    */     try {
/* 64 */       this.unmarshaller.setEventHandler(handler);
/* 65 */       this.marshaller.setEventHandler(handler);
/* 66 */     } catch (JAXBException e) {
/*    */       
/* 68 */       throw new Error(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setAttachmentMarshaller(AttachmentMarshaller m) {
/* 73 */     this.marshaller.setAttachmentMarshaller(m);
/*    */   }
/*    */   
/*    */   public void setAttachmentUnmarshaller(AttachmentUnmarshaller u) {
/* 77 */     this.unmarshaller.setAttachmentUnmarshaller(u);
/*    */   }
/*    */   
/*    */   public AttachmentMarshaller getAttachmentMarshaller() {
/* 81 */     return this.marshaller.getAttachmentMarshaller();
/*    */   }
/*    */   
/*    */   public AttachmentUnmarshaller getAttachmentUnmarshaller() {
/* 85 */     return this.unmarshaller.getAttachmentUnmarshaller();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\BridgeContextImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */