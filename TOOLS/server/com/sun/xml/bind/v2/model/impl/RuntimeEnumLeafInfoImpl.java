/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.annotation.FieldLocatable;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeEnumLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
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
/*     */ final class RuntimeEnumLeafInfoImpl<T extends Enum<T>, B>
/*     */   extends EnumLeafInfoImpl<Type, Class, Field, Method>
/*     */   implements RuntimeEnumLeafInfo, Transducer<T>
/*     */ {
/*     */   private final Transducer<B> baseXducer;
/*     */   
/*     */   public Transducer<T> getTransducer() {
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final Map<B, T> parseMap = new HashMap<B, T>();
/*     */   private final Map<T, B> printMap;
/*     */   
/*     */   RuntimeEnumLeafInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class<T> enumType) {
/*  82 */     super(builder, upstream, enumType, enumType);
/*  83 */     this.printMap = new EnumMap<T, B>(enumType);
/*     */     
/*  85 */     this.baseXducer = ((RuntimeNonElement)this.baseType).getTransducer();
/*     */   }
/*     */ 
/*     */   
/*     */   public RuntimeEnumConstantImpl createEnumConstant(String name, String literal, Field constant, EnumConstantImpl<Type, Class<?>, Field, Method> last) {
/*     */     Enum enum_;
/*     */     try {
/*     */       try {
/*  93 */         constant.setAccessible(true);
/*  94 */       } catch (SecurityException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       enum_ = (Enum)constant.get(null);
/* 100 */     } catch (IllegalAccessException e) {
/*     */       
/* 102 */       throw new IllegalAccessError(e.getMessage());
/*     */     } 
/*     */     
/* 105 */     B b = null;
/*     */     try {
/* 107 */       b = (B)this.baseXducer.parse(literal);
/* 108 */     } catch (Exception e) {
/* 109 */       this.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ENUM_VALUE.format(new Object[] { literal, ((Type)this.baseType.getType()).toString() }, ), e, (Locatable)new FieldLocatable(this, constant, nav())));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 114 */     this.parseMap.put(b, (T)enum_);
/* 115 */     this.printMap.put((T)enum_, b);
/*     */     
/* 117 */     return new RuntimeEnumConstantImpl(this, name, literal, last);
/*     */   }
/*     */   
/*     */   public QName[] getTypeNames() {
/* 121 */     return new QName[] { getTypeName() };
/*     */   }
/*     */   
/*     */   public boolean isDefault() {
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   public Class getClazz() {
/* 129 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public boolean useNamespace() {
/* 133 */     return this.baseXducer.useNamespace();
/*     */   }
/*     */   
/*     */   public void declareNamespace(T t, XMLSerializer w) throws AccessorException {
/* 137 */     this.baseXducer.declareNamespace(this.printMap.get(t), w);
/*     */   }
/*     */   
/*     */   public CharSequence print(T t) throws AccessorException {
/* 141 */     return this.baseXducer.print(this.printMap.get(t));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T parse(CharSequence lexical) throws AccessorException, SAXException {
/* 147 */     B b = (B)this.baseXducer.parse(lexical);
/* 148 */     if (b == null)
/*     */     {
/* 150 */       return null;
/*     */     }
/*     */     
/* 153 */     return this.parseMap.get(b);
/*     */   }
/*     */   
/*     */   public void writeText(XMLSerializer w, T t, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 157 */     this.baseXducer.writeText(w, this.printMap.get(t), fieldName);
/*     */   }
/*     */   
/*     */   public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 161 */     this.baseXducer.writeLeafElement(w, tagName, this.printMap.get(o), fieldName);
/*     */   }
/*     */   
/*     */   public QName getTypeName(T instance) {
/* 165 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeEnumLeafInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */