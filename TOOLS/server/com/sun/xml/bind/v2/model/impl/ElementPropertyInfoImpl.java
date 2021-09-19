/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElements;
/*     */ import javax.xml.bind.annotation.XmlList;
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
/*     */ class ElementPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   extends ERPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements ElementPropertyInfo<TypeT, ClassDeclT>
/*     */ {
/*     */   private List<TypeRefImpl<TypeT, ClassDeclT>> types;
/*     */   
/*  72 */   private final List<TypeInfo<TypeT, ClassDeclT>> ref = new AbstractList<TypeInfo<TypeT, ClassDeclT>>() {
/*     */       public TypeInfo<TypeT, ClassDeclT> get(int index) {
/*  74 */         return (TypeInfo<TypeT, ClassDeclT>)((TypeRefImpl)ElementPropertyInfoImpl.this.getTypes().get(index)).getTarget();
/*     */       }
/*     */       
/*     */       public int size() {
/*  78 */         return ElementPropertyInfoImpl.this.getTypes().size();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean isRequired;
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean isValueList;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ElementPropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> propertySeed) {
/*  96 */     super(parent, propertySeed);
/*     */     
/*  98 */     this.isValueList = this.seed.hasAnnotation(XmlList.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends TypeRefImpl<TypeT, ClassDeclT>> getTypes() {
/* 103 */     if (this.types == null) {
/* 104 */       this.types = (List<TypeRefImpl<TypeT, ClassDeclT>>)new FinalArrayList();
/* 105 */       XmlElement[] ann = null;
/*     */       
/* 107 */       XmlElement xe = (XmlElement)this.seed.readAnnotation(XmlElement.class);
/* 108 */       XmlElements xes = (XmlElements)this.seed.readAnnotation(XmlElements.class);
/*     */       
/* 110 */       if (xe != null && xes != null) {
/* 111 */         this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(new Object[] { nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), xe.annotationType().getName(), xes.annotationType().getName() }, ), (Annotation)xe, (Annotation)xes));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       this.isRequired = Boolean.valueOf(true);
/*     */       
/* 120 */       if (xe != null) {
/* 121 */         ann = new XmlElement[] { xe };
/*     */       }
/* 123 */       else if (xes != null) {
/* 124 */         ann = xes.value();
/*     */       } 
/* 126 */       if (ann == null) {
/*     */         
/* 128 */         TypeT t = getIndividualType();
/* 129 */         if (!nav().isPrimitive(t) || isCollection()) {
/* 130 */           this.isRequired = Boolean.valueOf(false);
/*     */         }
/* 132 */         this.types.add(createTypeRef(calcXmlName((XmlElement)null), t, isCollection(), (String)null));
/*     */       } else {
/* 134 */         for (XmlElement item : ann) {
/*     */           
/* 136 */           QName name = calcXmlName(item);
/* 137 */           TypeT type = (TypeT)reader().getClassValue((Annotation)item, "type");
/* 138 */           if (type.equals(nav().ref(XmlElement.DEFAULT.class))) type = getIndividualType(); 
/* 139 */           if ((!nav().isPrimitive(type) || isCollection()) && !item.required())
/* 140 */             this.isRequired = Boolean.valueOf(false); 
/* 141 */           this.types.add(createTypeRef(name, type, item.nillable(), getDefaultValue(item.defaultValue())));
/*     */         } 
/*     */       } 
/* 144 */       this.types = Collections.unmodifiableList(this.types);
/* 145 */       assert !this.types.contains(null);
/*     */     } 
/* 147 */     return this.types;
/*     */   }
/*     */   
/*     */   private String getDefaultValue(String value) {
/* 151 */     if (value.equals("\000")) {
/* 152 */       return null;
/*     */     }
/* 154 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRefImpl<TypeT, ClassDeclT> createTypeRef(QName name, TypeT type, boolean isNillable, String defaultValue) {
/* 161 */     return new TypeRefImpl<TypeT, ClassDeclT>(this, name, type, isNillable, defaultValue);
/*     */   }
/*     */   
/*     */   public boolean isValueList() {
/* 165 */     return this.isValueList;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 169 */     if (this.isRequired == null)
/* 170 */       getTypes(); 
/* 171 */     return this.isRequired.booleanValue();
/*     */   }
/*     */   
/*     */   public List<? extends TypeInfo<TypeT, ClassDeclT>> ref() {
/* 175 */     return this.ref;
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/* 179 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */   
/*     */   protected void link() {
/* 183 */     super.link();
/* 184 */     for (TypeRefImpl<TypeT, ClassDeclT> ref : getTypes()) {
/* 185 */       ref.link();
/*     */     }
/*     */     
/* 188 */     if (isValueList()) {
/*     */ 
/*     */       
/* 191 */       if (id() != ID.IDREF)
/*     */       {
/*     */ 
/*     */         
/* 195 */         for (TypeRefImpl<TypeT, ClassDeclT> ref : this.types) {
/* 196 */           if (!ref.getTarget().isSimpleType()) {
/* 197 */             this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_NEEDS_SIMPLETYPE.format(new Object[] { nav().getTypeName(ref.getTarget().getType()) }, ), this));
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 205 */       if (!isCollection())
/* 206 */         this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_ON_SINGLE_PROPERTY.format(new Object[0]), this)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ElementPropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */