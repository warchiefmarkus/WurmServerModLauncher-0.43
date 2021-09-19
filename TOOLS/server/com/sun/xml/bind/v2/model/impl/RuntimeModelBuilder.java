/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.model.nav.ReflectionNavigator;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfoSet;
/*     */ import com.sun.xml.bind.v2.runtime.FilterTransducer;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.InlineBinaryTransducer;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.MimeTypedTransducer;
/*     */ import com.sun.xml.bind.v2.runtime.SchemaTypeTransducer;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import javax.activation.MimeType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RuntimeModelBuilder
/*     */   extends ModelBuilder<Type, Class, Field, Method>
/*     */ {
/*     */   @Nullable
/*     */   public final JAXBContextImpl context;
/*     */   
/*     */   public RuntimeModelBuilder(JAXBContextImpl context, RuntimeAnnotationReader annotationReader, Map<Class<?>, Class<?>> subclassReplacements, String defaultNamespaceRemap) {
/*  87 */     super((AnnotationReader<Type, Class, Field, Method>)annotationReader, (Navigator<Type, Class, Field, Method>)Navigator.REFLECTION, subclassReplacements, defaultNamespaceRemap);
/*  88 */     this.context = context;
/*     */   }
/*     */ 
/*     */   
/*     */   public RuntimeNonElement getClassInfo(Class clazz, Locatable upstream) {
/*  93 */     return (RuntimeNonElement)super.getClassInfo(clazz, upstream);
/*     */   }
/*     */ 
/*     */   
/*     */   public RuntimeNonElement getClassInfo(Class clazz, boolean searchForSuperClass, Locatable upstream) {
/*  98 */     return (RuntimeNonElement)super.getClassInfo(clazz, searchForSuperClass, upstream);
/*     */   }
/*     */ 
/*     */   
/*     */   protected RuntimeEnumLeafInfoImpl createEnumLeafInfo(Class<Enum> clazz, Locatable upstream) {
/* 103 */     return new RuntimeEnumLeafInfoImpl<Enum, Object>(this, upstream, clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected RuntimeClassInfoImpl createClassInfo(Class clazz, Locatable upstream) {
/* 108 */     return new RuntimeClassInfoImpl(this, upstream, clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   public RuntimeElementInfoImpl createElementInfo(RegistryInfoImpl<Type, Class<?>, Field, Method> registryInfo, Method method) throws IllegalAnnotationException {
/* 113 */     return new RuntimeElementInfoImpl(this, registryInfo, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public RuntimeArrayInfoImpl createArrayInfo(Locatable upstream, Type arrayType) {
/* 118 */     return new RuntimeArrayInfoImpl(this, upstream, (Class)arrayType);
/*     */   }
/*     */   
/*     */   public ReflectionNavigator getNavigator() {
/* 122 */     return (ReflectionNavigator)this.nav;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RuntimeTypeInfoSetImpl createTypeInfoSet() {
/* 127 */     return new RuntimeTypeInfoSetImpl(this.reader);
/*     */   }
/*     */ 
/*     */   
/*     */   public RuntimeTypeInfoSet link() {
/* 132 */     return (RuntimeTypeInfoSet)super.link();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Transducer createTransducer(RuntimeNonElementRef ref) {
/*     */     IDTransducerImpl iDTransducerImpl;
/*     */     MimeTypedTransducer mimeTypedTransducer;
/*     */     InlineBinaryTransducer inlineBinaryTransducer;
/*     */     SchemaTypeTransducer schemaTypeTransducer;
/* 144 */     Transducer<?> t = ref.getTarget().getTransducer();
/* 145 */     RuntimePropertyInfo src = ref.getSource();
/* 146 */     ID id = src.id();
/*     */     
/* 148 */     if (id == ID.IDREF) {
/* 149 */       return RuntimeBuiltinLeafInfoImpl.STRING;
/*     */     }
/* 151 */     if (id == ID.ID) {
/* 152 */       iDTransducerImpl = new IDTransducerImpl(t);
/*     */     }
/* 154 */     MimeType emt = src.getExpectedMimeType();
/* 155 */     if (emt != null) {
/* 156 */       mimeTypedTransducer = new MimeTypedTransducer((Transducer)iDTransducerImpl, emt);
/*     */     }
/* 158 */     if (src.inlineBinaryData()) {
/* 159 */       inlineBinaryTransducer = new InlineBinaryTransducer((Transducer)mimeTypedTransducer);
/*     */     }
/* 161 */     if (src.getSchemaType() != null) {
/* 162 */       schemaTypeTransducer = new SchemaTypeTransducer((Transducer)inlineBinaryTransducer, src.getSchemaType());
/*     */     }
/* 164 */     return (Transducer)schemaTypeTransducer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class IDTransducerImpl<ValueT>
/*     */     extends FilterTransducer<ValueT>
/*     */   {
/*     */     public IDTransducerImpl(Transducer<ValueT> core) {
/* 177 */       super(core);
/*     */     }
/*     */ 
/*     */     
/*     */     public ValueT parse(CharSequence lexical) throws AccessorException, SAXException {
/* 182 */       String value = WhiteSpaceProcessor.trim(lexical).toString();
/* 183 */       UnmarshallingContext.getInstance().addToIdTable(value);
/* 184 */       return (ValueT)this.core.parse(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeModelBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */