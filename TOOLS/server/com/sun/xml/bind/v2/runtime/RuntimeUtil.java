/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.util.ValidationEventLocatorExImpl;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.helpers.PrintConversionEventImpl;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RuntimeUtil
/*     */ {
/*     */   public static final Map<Class, Class> boxToPrimitive;
/*     */   public static final Map<Class, Class> primitiveToBox;
/*     */   
/*     */   public static final class ToStringAdapter
/*     */     extends XmlAdapter<String, Object>
/*     */   {
/*     */     public Object unmarshal(String s) {
/*  63 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public String marshal(Object o) {
/*  67 */       if (o == null) return null; 
/*  68 */       return o.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  86 */     Map<Class<?>, Class<?>> b = new HashMap<Class<?>, Class<?>>();
/*  87 */     b.put(byte.class, Byte.class);
/*  88 */     b.put(short.class, Short.class);
/*  89 */     b.put(int.class, Integer.class);
/*  90 */     b.put(long.class, Long.class);
/*  91 */     b.put(char.class, Character.class);
/*  92 */     b.put(boolean.class, Boolean.class);
/*  93 */     b.put(float.class, Float.class);
/*  94 */     b.put(double.class, Double.class);
/*  95 */     b.put(void.class, Void.class);
/*     */     
/*  97 */     primitiveToBox = Collections.unmodifiableMap(b);
/*     */     
/*  99 */     Map<Class<?>, Class<?>> p = new HashMap<Class<?>, Class<?>>();
/* 100 */     for (Map.Entry<Class<?>, Class<?>> e : b.entrySet()) {
/* 101 */       p.put(e.getValue(), e.getKey());
/*     */     }
/* 103 */     boxToPrimitive = Collections.unmodifiableMap(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void handlePrintConversionException(Object caller, Exception e, XMLSerializer serializer) throws SAXException {
/* 112 */     if (e instanceof SAXException)
/*     */     {
/*     */ 
/*     */       
/* 116 */       throw (SAXException)e;
/*     */     }
/* 118 */     PrintConversionEventImpl printConversionEventImpl = new PrintConversionEventImpl(1, e.getMessage(), (ValidationEventLocator)new ValidationEventLocatorImpl(caller), e);
/*     */ 
/*     */     
/* 121 */     serializer.reportError((ValidationEvent)printConversionEventImpl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void handleTypeMismatchError(XMLSerializer serializer, Object parentObject, String fieldName, Object childObject) throws SAXException {
/* 130 */     ValidationEventImpl validationEventImpl = new ValidationEventImpl(1, Messages.TYPE_MISMATCH.format(new Object[] { getTypeName(parentObject), fieldName, getTypeName(childObject) }, ), (ValidationEventLocator)new ValidationEventLocatorExImpl(parentObject, fieldName));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     serializer.reportError((ValidationEvent)validationEventImpl);
/*     */   }
/*     */   
/*     */   private static String getTypeName(Object o) {
/* 142 */     return o.getClass().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\RuntimeUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */