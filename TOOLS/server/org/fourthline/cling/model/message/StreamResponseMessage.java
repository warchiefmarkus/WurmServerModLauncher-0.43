/*    */ package org.fourthline.cling.model.message;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.seamless.util.MimeType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StreamResponseMessage
/*    */   extends UpnpMessage<UpnpResponse>
/*    */ {
/*    */   public StreamResponseMessage(StreamResponseMessage source) {
/* 30 */     super(source);
/*    */   }
/*    */   
/*    */   public StreamResponseMessage(UpnpResponse.Status status) {
/* 34 */     super(new UpnpResponse(status));
/*    */   }
/*    */   
/*    */   public StreamResponseMessage(UpnpResponse operation) {
/* 38 */     super(operation);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamResponseMessage(UpnpResponse operation, String body) {
/* 43 */     super(operation, UpnpMessage.BodyType.STRING, body);
/*    */   }
/*    */   
/*    */   public StreamResponseMessage(String body) {
/* 47 */     super(new UpnpResponse(UpnpResponse.Status.OK), UpnpMessage.BodyType.STRING, body);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamResponseMessage(UpnpResponse operation, byte[] body) {
/* 52 */     super(operation, UpnpMessage.BodyType.BYTES, body);
/*    */   }
/*    */   
/*    */   public StreamResponseMessage(byte[] body) {
/* 56 */     super(new UpnpResponse(UpnpResponse.Status.OK), UpnpMessage.BodyType.BYTES, body);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamResponseMessage(String body, ContentTypeHeader contentType) {
/* 61 */     this(body);
/* 62 */     getHeaders().add(UpnpHeader.Type.CONTENT_TYPE, (UpnpHeader)contentType);
/*    */   }
/*    */   
/*    */   public StreamResponseMessage(String body, MimeType mimeType) {
/* 66 */     this(body, new ContentTypeHeader(mimeType));
/*    */   }
/*    */   
/*    */   public StreamResponseMessage(byte[] body, ContentTypeHeader contentType) {
/* 70 */     this(body);
/* 71 */     getHeaders().add(UpnpHeader.Type.CONTENT_TYPE, (UpnpHeader)contentType);
/*    */   }
/*    */   
/*    */   public StreamResponseMessage(byte[] body, MimeType mimeType) {
/* 75 */     this(body, new ContentTypeHeader(mimeType));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\StreamResponseMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */