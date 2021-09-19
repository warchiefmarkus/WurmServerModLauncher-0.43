/*    */ package 1.0.com.sun.codemodel.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
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
/*    */ public class EncoderFactory
/*    */ {
/*    */   public static CharsetEncoder createEncoder(String encodin) {
/* 25 */     Charset cs = Charset.forName(System.getProperty("file.encoding"));
/* 26 */     CharsetEncoder encoder = cs.newEncoder();
/*    */     
/* 28 */     if (cs.getClass().getName().equals("sun.nio.cs.MS1252")) {
/*    */       
/*    */       try {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 36 */         Class ms1252encoder = Class.forName("com.sun.codemodel.util.MS1252Encoder");
/* 37 */         Constructor c = ms1252encoder.getConstructor(new Class[] { Charset.class });
/*    */ 
/*    */         
/* 40 */         return (CharsetEncoder)c.newInstance(new Object[] { cs });
/* 41 */       } catch (Throwable t) {
/*    */ 
/*    */ 
/*    */         
/* 45 */         return encoder;
/*    */       } 
/*    */     }
/*    */     
/* 49 */     return encoder;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemode\\util\EncoderFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */