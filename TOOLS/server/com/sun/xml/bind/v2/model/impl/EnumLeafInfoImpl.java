/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.bind.v2.model.core.Element;
/*     */ import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.bind.annotation.XmlEnum;
/*     */ import javax.xml.bind.annotation.XmlEnumValue;
/*     */ import javax.xml.namespace.QName;
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
/*     */ class EnumLeafInfoImpl<T, C, F, M>
/*     */   extends TypeInfoImpl<T, C, F, M>
/*     */   implements EnumLeafInfo<T, C>, Element<T, C>, Iterable<EnumConstantImpl<T, C, F, M>>
/*     */ {
/*     */   final C clazz;
/*     */   NonElement<T, C> baseType;
/*     */   private final T type;
/*     */   private final QName typeName;
/*     */   private EnumConstantImpl<T, C, F, M> firstConstant;
/*     */   private QName elementName;
/*     */   
/*     */   public EnumLeafInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C clazz, T type) {
/*  96 */     super(builder, upstream);
/*  97 */     this.clazz = clazz;
/*  98 */     this.type = type;
/*     */     
/* 100 */     this.elementName = parseElementName(clazz);
/*     */ 
/*     */ 
/*     */     
/* 104 */     this.typeName = parseTypeName(clazz);
/*     */ 
/*     */ 
/*     */     
/* 108 */     XmlEnum xe = (XmlEnum)builder.reader.getClassAnnotation(XmlEnum.class, clazz, this);
/* 109 */     if (xe != null) {
/* 110 */       T base = (T)builder.reader.getClassValue((Annotation)xe, "value");
/* 111 */       this.baseType = builder.getTypeInfo(base, this);
/*     */     } else {
/* 113 */       this.baseType = builder.getTypeInfo((T)builder.nav.ref(String.class), this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void calcConstants() {
/* 121 */     EnumConstantImpl<T, C, F, M> last = null;
/* 122 */     F[] constants = (F[])nav().getEnumConstants(this.clazz);
/* 123 */     for (int i = constants.length - 1; i >= 0; i--) {
/* 124 */       String literal; F constant = constants[i];
/* 125 */       String name = nav().getFieldName(constant);
/* 126 */       XmlEnumValue xev = (XmlEnumValue)this.builder.reader.getFieldAnnotation(XmlEnumValue.class, constant, this);
/*     */ 
/*     */       
/* 129 */       if (xev == null) { literal = name; }
/* 130 */       else { literal = xev.value(); }
/*     */       
/* 132 */       last = createEnumConstant(name, literal, constant, last);
/*     */     } 
/* 134 */     this.firstConstant = last;
/*     */   }
/*     */   
/*     */   protected EnumConstantImpl<T, C, F, M> createEnumConstant(String name, String literal, F constant, EnumConstantImpl<T, C, F, M> last) {
/* 138 */     return new EnumConstantImpl<T, C, F, M>(this, name, literal, last);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getType() {
/* 143 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean canBeReferencedByIDREF() {
/* 153 */     return false;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/* 157 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public C getClazz() {
/* 161 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public NonElement<T, C> getBaseType() {
/* 165 */     return this.baseType;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/* 169 */     return true;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 173 */     return nav().getClassLocation(this.clazz);
/*     */   }
/*     */   
/*     */   public Iterable<? extends EnumConstantImpl<T, C, F, M>> getConstants() {
/* 177 */     if (this.firstConstant == null)
/* 178 */       calcConstants(); 
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void link() {
/* 184 */     getConstants();
/* 185 */     super.link();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Element<T, C> getSubstitutionHead() {
/* 194 */     return null;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/* 198 */     return this.elementName;
/*     */   }
/*     */   
/*     */   public boolean isElement() {
/* 202 */     return (this.elementName != null);
/*     */   }
/*     */   
/*     */   public Element<T, C> asElement() {
/* 206 */     if (isElement()) {
/* 207 */       return this;
/*     */     }
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassInfo<T, C> getScope() {
/* 220 */     return null;
/*     */   }
/*     */   
/*     */   public Iterator<EnumConstantImpl<T, C, F, M>> iterator() {
/* 224 */     return new Iterator<EnumConstantImpl<T, C, F, M>>() {
/* 225 */         private EnumConstantImpl<T, C, F, M> next = EnumLeafInfoImpl.this.firstConstant;
/*     */         public boolean hasNext() {
/* 227 */           return (this.next != null);
/*     */         }
/*     */         
/*     */         public EnumConstantImpl<T, C, F, M> next() {
/* 231 */           EnumConstantImpl<T, C, F, M> r = this.next;
/* 232 */           this.next = this.next.next;
/* 233 */           return r;
/*     */         }
/*     */         
/*     */         public void remove() {
/* 237 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\EnumLeafInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */