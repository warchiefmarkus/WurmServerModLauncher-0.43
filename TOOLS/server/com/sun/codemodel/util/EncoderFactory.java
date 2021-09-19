/*    */ package com.sun.codemodel.util;
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
/* 40 */     Charset cs = Charset.forName(System.getProperty("file.encoding"));
/* 41 */     CharsetEncoder encoder = cs.newEncoder();
/*    */     
/* 43 */     if (cs.getClass().getName().equals("sun.nio.cs.MS1252")) {
/*    */       
/*    */       try {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 51 */         Class<?> ms1252encoder = Class.forName("com.sun.codemodel.util.MS1252Encoder");
/* 52 */         Constructor<?> c = ms1252encoder.getConstructor(new Class[] { Charset.class });
/*    */ 
/*    */         
/* 55 */         return (CharsetEncoder)c.newInstance(new Object[] { cs });
/* 56 */       } catch (Throwable t) {
/*    */ 
/*    */ 
/*    */         
/* 60 */         return encoder;
/*    */       } 
/*    */     }
/*    */     
/* 64 */     return encoder;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemode\\util\EncoderFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */