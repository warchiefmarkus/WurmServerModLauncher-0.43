/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeValuePropertyInfo;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertyFactory
/*     */ {
/*     */   private static final Constructor<? extends Property>[] propImpls;
/*     */   
/*     */   static {
/*  68 */     Class[] arrayOfClass = { SingleElementLeafProperty.class, null, null, ArrayElementLeafProperty.class, null, null, SingleElementNodeProperty.class, SingleReferenceNodeProperty.class, SingleMapNodeProperty.class, ArrayElementNodeProperty.class, ArrayReferenceNodeProperty.class, null };
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
/*  86 */     propImpls = (Constructor<? extends Property>[])new Constructor[arrayOfClass.length];
/*  87 */     for (int i = 0; i < propImpls.length; i++) {
/*  88 */       if (arrayOfClass[i] != null)
/*     */       {
/*  90 */         propImpls[i] = (Constructor)arrayOfClass[i].getConstructors()[0];
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Property create(JAXBContextImpl grammar, RuntimePropertyInfo info) {
/* 100 */     PropertyKind kind = info.kind();
/*     */     
/* 102 */     switch (kind) {
/*     */       case ATTRIBUTE:
/* 104 */         return new AttributeProperty(grammar, (RuntimeAttributePropertyInfo)info);
/*     */       case VALUE:
/* 106 */         return new ValueProperty(grammar, (RuntimeValuePropertyInfo)info);
/*     */       case ELEMENT:
/* 108 */         if (((RuntimeElementPropertyInfo)info).isValueList()) {
/* 109 */           return new ListElementProperty<Object, Object, Object>(grammar, (RuntimeElementPropertyInfo)info);
/*     */         }
/*     */         break;
/*     */       case REFERENCE:
/*     */       case MAP:
/*     */         break;
/*     */       default:
/*     */         assert false;
/*     */         break;
/*     */     } 
/* 119 */     boolean isCollection = info.isCollection();
/* 120 */     boolean isLeaf = isLeaf(info);
/*     */     
/* 122 */     Constructor<? extends Property> c = propImpls[(isLeaf ? 0 : 6) + (isCollection ? 3 : 0) + kind.propertyIndex];
/*     */     try {
/* 124 */       return c.newInstance(new Object[] { grammar, info });
/* 125 */     } catch (InstantiationException e) {
/* 126 */       throw new InstantiationError(e.getMessage());
/* 127 */     } catch (IllegalAccessException e) {
/* 128 */       throw new IllegalAccessError(e.getMessage());
/* 129 */     } catch (InvocationTargetException e) {
/* 130 */       Throwable t = e.getCause();
/* 131 */       if (t instanceof Error)
/* 132 */         throw (Error)t; 
/* 133 */       if (t instanceof RuntimeException) {
/* 134 */         throw (RuntimeException)t;
/*     */       }
/* 136 */       throw new AssertionError(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isLeaf(RuntimePropertyInfo info) {
/* 145 */     Collection<? extends RuntimeTypeInfo> types = info.ref();
/* 146 */     if (types.size() != 1) return false;
/*     */     
/* 148 */     RuntimeTypeInfo rti = types.iterator().next();
/* 149 */     if (!(rti instanceof RuntimeNonElement)) return false;
/*     */     
/* 151 */     if (info.id() == ID.IDREF)
/*     */     {
/* 153 */       return true;
/*     */     }
/* 155 */     if (((RuntimeNonElement)rti).getTransducer() == null)
/*     */     {
/*     */ 
/*     */       
/* 159 */       return false;
/*     */     }
/* 161 */     if (!info.getIndividualType().equals(rti.getType())) {
/* 162 */       return false;
/*     */     }
/* 164 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\PropertyFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */