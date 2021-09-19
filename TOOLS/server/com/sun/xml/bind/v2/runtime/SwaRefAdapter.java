/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*    */ import javax.activation.DataHandler;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SwaRefAdapter
/*    */   extends XmlAdapter<String, DataHandler>
/*    */ {
/*    */   public DataHandler unmarshal(String cid) {
/* 71 */     AttachmentUnmarshaller au = (UnmarshallingContext.getInstance()).parent.getAttachmentUnmarshaller();
/*    */     
/* 73 */     return au.getAttachmentAsDataHandler(cid);
/*    */   }
/*    */   
/*    */   public String marshal(DataHandler data) {
/* 77 */     if (data == null) return null; 
/* 78 */     AttachmentMarshaller am = (XMLSerializer.getInstance()).attachmentMarshaller;
/*    */     
/* 80 */     return am.addSwaRefAttachment(data);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\SwaRefAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */