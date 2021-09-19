/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
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
/*    */ public class ContentTypeHeader
/*    */   extends UpnpHeader<MimeType>
/*    */ {
/* 25 */   public static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/xml");
/* 26 */   public static final MimeType DEFAULT_CONTENT_TYPE_UTF8 = MimeType.valueOf("text/xml;charset=\"utf-8\"");
/*    */   
/*    */   public ContentTypeHeader() {
/* 29 */     setValue(DEFAULT_CONTENT_TYPE);
/*    */   }
/*    */   
/*    */   public ContentTypeHeader(MimeType contentType) {
/* 33 */     setValue(contentType);
/*    */   }
/*    */   
/*    */   public ContentTypeHeader(String s) throws InvalidHeaderException {
/* 37 */     setString(s);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 41 */     setValue(MimeType.valueOf(s));
/*    */   }
/*    */   
/*    */   public String getString() {
/* 45 */     return getValue().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUDACompliantXML() {
/* 50 */     return (isText() && getValue().getSubtype().equals(DEFAULT_CONTENT_TYPE.getSubtype()));
/*    */   }
/*    */   
/*    */   public boolean isText() {
/* 54 */     return (getValue() != null && getValue().getType().equals(DEFAULT_CONTENT_TYPE.getType()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\ContentTypeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */