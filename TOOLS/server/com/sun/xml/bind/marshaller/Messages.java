/*    */ package com.sun.xml.bind.marshaller;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
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
/*    */ public class Messages
/*    */ {
/*    */   public static final String NOT_MARSHALLABLE = "MarshallerImpl.NotMarshallable";
/*    */   public static final String UNSUPPORTED_RESULT = "MarshallerImpl.UnsupportedResult";
/*    */   public static final String UNSUPPORTED_ENCODING = "MarshallerImpl.UnsupportedEncoding";
/*    */   public static final String NULL_WRITER = "MarshallerImpl.NullWriterParam";
/*    */   public static final String ASSERT_FAILED = "SAXMarshaller.AssertFailed";
/*    */   public static final String ERR_MISSING_OBJECT = "SAXMarshaller.MissingObject";
/*    */   public static final String ERR_MISSING_OBJECT2 = "SAXMarshaller.MissingObject2";
/*    */   public static final String ERR_DANGLING_IDREF = "SAXMarshaller.DanglingIDREF";
/*    */   public static final String ERR_NOT_IDENTIFIABLE = "SAXMarshaller.NotIdentifiable";
/*    */   public static final String DOM_IMPL_DOESNT_SUPPORT_CREATELEMENTNS = "SAX2DOMEx.DomImplDoesntSupportCreateElementNs";
/*    */   
/*    */   public static String format(String property) {
/* 50 */     return format(property, (Object[])null);
/*    */   }
/*    */   
/*    */   public static String format(String property, Object arg1) {
/* 54 */     return format(property, new Object[] { arg1 });
/*    */   }
/*    */   
/*    */   public static String format(String property, Object arg1, Object arg2) {
/* 58 */     return format(property, new Object[] { arg1, arg2 });
/*    */   }
/*    */   
/*    */   public static String format(String property, Object arg1, Object arg2, Object arg3) {
/* 62 */     return format(property, new Object[] { arg1, arg2, arg3 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static String format(String property, Object[] args) {
/* 69 */     String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
/* 70 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\marshaller\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */