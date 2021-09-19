/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.bind.util.Which;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.core.ErrorHandler;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.bind.v2.model.core.RegistryInfo;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlRegistry;
/*     */ import javax.xml.bind.annotation.XmlSchema;
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
/*     */ public class ModelBuilder<T, C, F, M>
/*     */ {
/*     */   final TypeInfoSetImpl<T, C, F, M> typeInfoSet;
/*     */   public final AnnotationReader<T, C, F, M> reader;
/*     */   public final Navigator<T, C, F, M> nav;
/*  94 */   private final Map<QName, TypeInfo> typeNames = new HashMap<QName, TypeInfo>();
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
/*     */   public final String defaultNsUri;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   final Map<String, RegistryInfoImpl<T, C, F, M>> registries = new HashMap<String, RegistryInfoImpl<T, C, F, M>>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<C, C> subclassReplacements;
/*     */ 
/*     */   
/*     */   private ErrorHandler errorHandler;
/*     */ 
/*     */   
/*     */   private boolean hadError;
/*     */ 
/*     */   
/*     */   public boolean hasSwaRef;
/*     */ 
/*     */ 
/*     */   
/* 132 */   private final ErrorHandler proxyErrorHandler = new ErrorHandler() {
/*     */       public void error(IllegalAnnotationException e) {
/* 134 */         ModelBuilder.this.reportError(e);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean linked;
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBuilder(AnnotationReader<T, C, F, M> reader, Navigator<T, C, F, M> navigator, Map<C, C> subclassReplacements, String defaultNamespaceRemap) {
/* 145 */     this.reader = reader;
/* 146 */     this.nav = navigator;
/* 147 */     this.subclassReplacements = subclassReplacements;
/* 148 */     if (defaultNamespaceRemap == null)
/* 149 */       defaultNamespaceRemap = ""; 
/* 150 */     this.defaultNsUri = defaultNamespaceRemap;
/* 151 */     reader.setErrorHandler(this.proxyErrorHandler);
/* 152 */     this.typeInfoSet = createTypeInfoSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 161 */       XmlSchema s = null;
/* 162 */       s.location();
/* 163 */     } catch (NullPointerException e) {
/*     */     
/* 165 */     } catch (NoSuchMethodError e) {
/*     */       Messages res;
/*     */       
/* 168 */       if (XmlSchema.class.getClassLoader() == null) {
/* 169 */         res = Messages.INCOMPATIBLE_API_VERSION_MUSTANG;
/*     */       } else {
/* 171 */         res = Messages.INCOMPATIBLE_API_VERSION;
/*     */       } 
/* 173 */       throw new LinkageError(res.format(new Object[] { Which.which(XmlSchema.class), Which.which(ModelBuilder.class) }));
/*     */     } 
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
/*     */     try {
/* 186 */       WhiteSpaceProcessor.isWhiteSpace("xyz");
/* 187 */     } catch (NoSuchMethodError e) {
/*     */       
/* 189 */       throw new LinkageError(Messages.RUNNING_WITH_1_0_RUNTIME.format(new Object[] { Which.which(WhiteSpaceProcessor.class), Which.which(ModelBuilder.class) }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeInfoSetImpl<T, C, F, M> createTypeInfoSet() {
/* 197 */     return new TypeInfoSetImpl<T, C, F, M>(this.nav, this.reader, BuiltinLeafInfoImpl.createLeaves(this.nav));
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
/*     */   public NonElement<T, C> getClassInfo(C clazz, Locatable upstream) {
/* 209 */     return getClassInfo(clazz, false, upstream);
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
/*     */   public NonElement<T, C> getClassInfo(C clazz, boolean searchForSuperClass, Locatable upstream) {
/*     */     // Byte code:
/*     */     //   0: getstatic com/sun/xml/bind/v2/model/impl/ModelBuilder.$assertionsDisabled : Z
/*     */     //   3: ifne -> 18
/*     */     //   6: aload_1
/*     */     //   7: ifnonnull -> 18
/*     */     //   10: new java/lang/AssertionError
/*     */     //   13: dup
/*     */     //   14: invokespecial <init> : ()V
/*     */     //   17: athrow
/*     */     //   18: aload_0
/*     */     //   19: getfield typeInfoSet : Lcom/sun/xml/bind/v2/model/impl/TypeInfoSetImpl;
/*     */     //   22: aload_1
/*     */     //   23: invokevirtual getClassInfo : (Ljava/lang/Object;)Lcom/sun/xml/bind/v2/model/core/NonElement;
/*     */     //   26: astore #4
/*     */     //   28: aload #4
/*     */     //   30: ifnull -> 36
/*     */     //   33: aload #4
/*     */     //   35: areturn
/*     */     //   36: aload_0
/*     */     //   37: getfield nav : Lcom/sun/xml/bind/v2/model/nav/Navigator;
/*     */     //   40: aload_1
/*     */     //   41: invokeinterface isEnum : (Ljava/lang/Object;)Z
/*     */     //   46: ifeq -> 79
/*     */     //   49: aload_0
/*     */     //   50: aload_1
/*     */     //   51: aload_3
/*     */     //   52: invokevirtual createEnumLeafInfo : (Ljava/lang/Object;Lcom/sun/xml/bind/v2/model/annotation/Locatable;)Lcom/sun/xml/bind/v2/model/impl/EnumLeafInfoImpl;
/*     */     //   55: astore #5
/*     */     //   57: aload_0
/*     */     //   58: getfield typeInfoSet : Lcom/sun/xml/bind/v2/model/impl/TypeInfoSetImpl;
/*     */     //   61: aload #5
/*     */     //   63: invokevirtual add : (Lcom/sun/xml/bind/v2/model/impl/EnumLeafInfoImpl;)V
/*     */     //   66: aload #5
/*     */     //   68: astore #4
/*     */     //   70: aload_0
/*     */     //   71: aload #4
/*     */     //   73: invokespecial addTypeName : (Lcom/sun/xml/bind/v2/model/core/NonElement;)V
/*     */     //   76: goto -> 379
/*     */     //   79: aload_0
/*     */     //   80: getfield subclassReplacements : Ljava/util/Map;
/*     */     //   83: aload_1
/*     */     //   84: invokeinterface containsKey : (Ljava/lang/Object;)Z
/*     */     //   89: istore #5
/*     */     //   91: iload #5
/*     */     //   93: ifeq -> 120
/*     */     //   96: iload_2
/*     */     //   97: ifne -> 120
/*     */     //   100: aload_0
/*     */     //   101: aload_0
/*     */     //   102: getfield subclassReplacements : Ljava/util/Map;
/*     */     //   105: aload_1
/*     */     //   106: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   111: aload_3
/*     */     //   112: invokevirtual getClassInfo : (Ljava/lang/Object;Lcom/sun/xml/bind/v2/model/annotation/Locatable;)Lcom/sun/xml/bind/v2/model/core/NonElement;
/*     */     //   115: astore #4
/*     */     //   117: goto -> 379
/*     */     //   120: aload_0
/*     */     //   121: getfield reader : Lcom/sun/xml/bind/v2/model/annotation/AnnotationReader;
/*     */     //   124: aload_1
/*     */     //   125: ldc_w javax/xml/bind/annotation/XmlTransient
/*     */     //   128: invokeinterface hasClassAnnotation : (Ljava/lang/Object;Ljava/lang/Class;)Z
/*     */     //   133: ifne -> 141
/*     */     //   136: iload #5
/*     */     //   138: ifeq -> 174
/*     */     //   141: aload_0
/*     */     //   142: aload_0
/*     */     //   143: getfield nav : Lcom/sun/xml/bind/v2/model/nav/Navigator;
/*     */     //   146: aload_1
/*     */     //   147: invokeinterface getSuperClass : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   152: iload_2
/*     */     //   153: new com/sun/xml/bind/v2/model/annotation/ClassLocatable
/*     */     //   156: dup
/*     */     //   157: aload_3
/*     */     //   158: aload_1
/*     */     //   159: aload_0
/*     */     //   160: getfield nav : Lcom/sun/xml/bind/v2/model/nav/Navigator;
/*     */     //   163: invokespecial <init> : (Lcom/sun/xml/bind/v2/model/annotation/Locatable;Ljava/lang/Object;Lcom/sun/xml/bind/v2/model/nav/Navigator;)V
/*     */     //   166: invokevirtual getClassInfo : (Ljava/lang/Object;ZLcom/sun/xml/bind/v2/model/annotation/Locatable;)Lcom/sun/xml/bind/v2/model/core/NonElement;
/*     */     //   169: astore #4
/*     */     //   171: goto -> 379
/*     */     //   174: aload_0
/*     */     //   175: aload_1
/*     */     //   176: aload_3
/*     */     //   177: invokevirtual createClassInfo : (Ljava/lang/Object;Lcom/sun/xml/bind/v2/model/annotation/Locatable;)Lcom/sun/xml/bind/v2/model/impl/ClassInfoImpl;
/*     */     //   180: astore #6
/*     */     //   182: aload_0
/*     */     //   183: getfield typeInfoSet : Lcom/sun/xml/bind/v2/model/impl/TypeInfoSetImpl;
/*     */     //   186: aload #6
/*     */     //   188: invokevirtual add : (Lcom/sun/xml/bind/v2/model/impl/ClassInfoImpl;)V
/*     */     //   191: aload #6
/*     */     //   193: invokevirtual getProperties : ()Ljava/util/List;
/*     */     //   196: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   201: astore #7
/*     */     //   203: aload #7
/*     */     //   205: invokeinterface hasNext : ()Z
/*     */     //   210: ifeq -> 363
/*     */     //   213: aload #7
/*     */     //   215: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   220: checkcast com/sun/xml/bind/v2/model/core/PropertyInfo
/*     */     //   223: astore #8
/*     */     //   225: aload #8
/*     */     //   227: invokeinterface kind : ()Lcom/sun/xml/bind/v2/model/core/PropertyKind;
/*     */     //   232: getstatic com/sun/xml/bind/v2/model/core/PropertyKind.REFERENCE : Lcom/sun/xml/bind/v2/model/core/PropertyKind;
/*     */     //   235: if_acmpne -> 321
/*     */     //   238: aload_0
/*     */     //   239: getfield nav : Lcom/sun/xml/bind/v2/model/nav/Navigator;
/*     */     //   242: aload #6
/*     */     //   244: invokevirtual getClazz : ()Ljava/lang/Object;
/*     */     //   247: invokeinterface getPackageName : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   252: astore #9
/*     */     //   254: aload_0
/*     */     //   255: getfield registries : Ljava/util/Map;
/*     */     //   258: aload #9
/*     */     //   260: invokeinterface containsKey : (Ljava/lang/Object;)Z
/*     */     //   265: ifne -> 321
/*     */     //   268: aload_0
/*     */     //   269: getfield nav : Lcom/sun/xml/bind/v2/model/nav/Navigator;
/*     */     //   272: new java/lang/StringBuilder
/*     */     //   275: dup
/*     */     //   276: invokespecial <init> : ()V
/*     */     //   279: aload #9
/*     */     //   281: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   284: ldc '.ObjectFactory'
/*     */     //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   289: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   292: aload #6
/*     */     //   294: invokevirtual getClazz : ()Ljava/lang/Object;
/*     */     //   297: invokeinterface findClass : (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   302: astore #10
/*     */     //   304: aload #10
/*     */     //   306: ifnull -> 321
/*     */     //   309: aload_0
/*     */     //   310: aload #10
/*     */     //   312: aload #8
/*     */     //   314: checkcast com/sun/xml/bind/v2/model/annotation/Locatable
/*     */     //   317: invokevirtual addRegistry : (Ljava/lang/Object;Lcom/sun/xml/bind/v2/model/annotation/Locatable;)Lcom/sun/xml/bind/v2/model/core/RegistryInfo;
/*     */     //   320: pop
/*     */     //   321: aload #8
/*     */     //   323: invokeinterface ref : ()Ljava/util/Collection;
/*     */     //   328: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   333: astore #9
/*     */     //   335: aload #9
/*     */     //   337: invokeinterface hasNext : ()Z
/*     */     //   342: ifeq -> 360
/*     */     //   345: aload #9
/*     */     //   347: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   352: checkcast com/sun/xml/bind/v2/model/core/TypeInfo
/*     */     //   355: astore #10
/*     */     //   357: goto -> 335
/*     */     //   360: goto -> 203
/*     */     //   363: aload #6
/*     */     //   365: invokevirtual getBaseClass : ()Lcom/sun/xml/bind/v2/model/impl/ClassInfoImpl;
/*     */     //   368: pop
/*     */     //   369: aload #6
/*     */     //   371: astore #4
/*     */     //   373: aload_0
/*     */     //   374: aload #4
/*     */     //   376: invokespecial addTypeName : (Lcom/sun/xml/bind/v2/model/core/NonElement;)V
/*     */     //   379: aload_0
/*     */     //   380: getfield reader : Lcom/sun/xml/bind/v2/model/annotation/AnnotationReader;
/*     */     //   383: ldc_w javax/xml/bind/annotation/XmlSeeAlso
/*     */     //   386: aload_1
/*     */     //   387: aload_3
/*     */     //   388: invokeinterface getClassAnnotation : (Ljava/lang/Class;Ljava/lang/Object;Lcom/sun/xml/bind/v2/model/annotation/Locatable;)Ljava/lang/annotation/Annotation;
/*     */     //   393: checkcast javax/xml/bind/annotation/XmlSeeAlso
/*     */     //   396: astore #5
/*     */     //   398: aload #5
/*     */     //   400: ifnull -> 458
/*     */     //   403: aload_0
/*     */     //   404: getfield reader : Lcom/sun/xml/bind/v2/model/annotation/AnnotationReader;
/*     */     //   407: aload #5
/*     */     //   409: ldc 'value'
/*     */     //   411: invokeinterface getClassArrayValue : (Ljava/lang/annotation/Annotation;Ljava/lang/String;)[Ljava/lang/Object;
/*     */     //   416: astore #6
/*     */     //   418: aload #6
/*     */     //   420: arraylength
/*     */     //   421: istore #7
/*     */     //   423: iconst_0
/*     */     //   424: istore #8
/*     */     //   426: iload #8
/*     */     //   428: iload #7
/*     */     //   430: if_icmpge -> 458
/*     */     //   433: aload #6
/*     */     //   435: iload #8
/*     */     //   437: aaload
/*     */     //   438: astore #9
/*     */     //   440: aload_0
/*     */     //   441: aload #9
/*     */     //   443: aload #5
/*     */     //   445: checkcast com/sun/xml/bind/v2/model/annotation/Locatable
/*     */     //   448: invokevirtual getTypeInfo : (Ljava/lang/Object;Lcom/sun/xml/bind/v2/model/annotation/Locatable;)Lcom/sun/xml/bind/v2/model/core/NonElement;
/*     */     //   451: pop
/*     */     //   452: iinc #8, 1
/*     */     //   455: goto -> 426
/*     */     //   458: aload #4
/*     */     //   460: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #218	-> 0
/*     */     //   #219	-> 18
/*     */     //   #220	-> 28
/*     */     //   #221	-> 33
/*     */     //   #223	-> 36
/*     */     //   #224	-> 49
/*     */     //   #225	-> 57
/*     */     //   #226	-> 66
/*     */     //   #227	-> 70
/*     */     //   #228	-> 76
/*     */     //   #229	-> 79
/*     */     //   #230	-> 91
/*     */     //   #232	-> 100
/*     */     //   #233	-> 117
/*     */     //   #234	-> 120
/*     */     //   #236	-> 141
/*     */     //   #238	-> 171
/*     */     //   #239	-> 174
/*     */     //   #240	-> 182
/*     */     //   #243	-> 191
/*     */     //   #244	-> 225
/*     */     //   #246	-> 238
/*     */     //   #247	-> 254
/*     */     //   #249	-> 268
/*     */     //   #250	-> 304
/*     */     //   #251	-> 309
/*     */     //   #255	-> 321
/*     */     //   #256	-> 357
/*     */     //   #257	-> 360
/*     */     //   #258	-> 363
/*     */     //   #260	-> 369
/*     */     //   #261	-> 373
/*     */     //   #267	-> 379
/*     */     //   #268	-> 398
/*     */     //   #269	-> 403
/*     */     //   #270	-> 440
/*     */     //   #269	-> 452
/*     */     //   #275	-> 458
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   57	19	5	li	Lcom/sun/xml/bind/v2/model/impl/EnumLeafInfoImpl;
/*     */     //   304	17	10	c	Ljava/lang/Object;
/*     */     //   254	67	9	pkg	Ljava/lang/String;
/*     */     //   357	0	10	t	Lcom/sun/xml/bind/v2/model/core/TypeInfo;
/*     */     //   335	25	9	i$	Ljava/util/Iterator;
/*     */     //   225	135	8	p	Lcom/sun/xml/bind/v2/model/core/PropertyInfo;
/*     */     //   203	160	7	i$	Ljava/util/Iterator;
/*     */     //   182	197	6	ci	Lcom/sun/xml/bind/v2/model/impl/ClassInfoImpl;
/*     */     //   91	288	5	isReplaced	Z
/*     */     //   440	12	9	t	Ljava/lang/Object;
/*     */     //   418	40	6	arr$	[Ljava/lang/Object;
/*     */     //   423	35	7	len$	I
/*     */     //   426	32	8	i$	I
/*     */     //   0	461	0	this	Lcom/sun/xml/bind/v2/model/impl/ModelBuilder;
/*     */     //   0	461	1	clazz	Ljava/lang/Object;
/*     */     //   0	461	2	searchForSuperClass	Z
/*     */     //   0	461	3	upstream	Lcom/sun/xml/bind/v2/model/annotation/Locatable;
/*     */     //   28	433	4	r	Lcom/sun/xml/bind/v2/model/core/NonElement;
/*     */     //   398	63	5	sa	Ljavax/xml/bind/annotation/XmlSeeAlso;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   57	19	5	li	Lcom/sun/xml/bind/v2/model/impl/EnumLeafInfoImpl<TT;TC;TF;TM;>;
/*     */     //   304	17	10	c	TC;
/*     */     //   357	0	10	t	Lcom/sun/xml/bind/v2/model/core/TypeInfo<TT;TC;>;
/*     */     //   225	135	8	p	Lcom/sun/xml/bind/v2/model/core/PropertyInfo<TT;TC;>;
/*     */     //   182	197	6	ci	Lcom/sun/xml/bind/v2/model/impl/ClassInfoImpl<TT;TC;TF;TM;>;
/*     */     //   440	12	9	t	TT;
/*     */     //   418	40	6	arr$	[TT;
/*     */     //   0	461	0	this	Lcom/sun/xml/bind/v2/model/impl/ModelBuilder<TT;TC;TF;TM;>;
/*     */     //   0	461	1	clazz	TC;
/*     */     //   28	433	4	r	Lcom/sun/xml/bind/v2/model/core/NonElement<TT;TC;>;
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
/*     */   private void addTypeName(NonElement<T, C> r) {
/* 282 */     QName t = r.getTypeName();
/* 283 */     if (t == null)
/*     */       return; 
/* 285 */     TypeInfo old = (TypeInfo)this.typeNames.put(t, r);
/* 286 */     if (old != null)
/*     */     {
/* 288 */       reportError(new IllegalAnnotationException(Messages.CONFLICTING_XML_TYPE_MAPPING.format(new Object[] { r.getTypeName() }, ), (Locatable)old, (Locatable)r));
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
/*     */   public NonElement<T, C> getTypeInfo(T t, Locatable upstream) {
/* 302 */     NonElement<T, C> r = this.typeInfoSet.getTypeInfo(t);
/* 303 */     if (r != null) return r;
/*     */     
/* 305 */     if (this.nav.isArray(t)) {
/* 306 */       ArrayInfoImpl<T, C, F, M> ai = createArrayInfo(upstream, t);
/*     */       
/* 308 */       addTypeName((NonElement)ai);
/* 309 */       this.typeInfoSet.add(ai);
/* 310 */       return (NonElement)ai;
/*     */     } 
/*     */     
/* 313 */     C c = (C)this.nav.asDecl(t);
/* 314 */     assert c != null : t.toString() + " must be a leaf, but we failed to recognize it.";
/* 315 */     return getClassInfo(c, upstream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NonElement<T, C> getTypeInfo(Ref<T, C> ref) {
/* 323 */     assert !ref.valueList;
/* 324 */     C c = (C)this.nav.asDecl(ref.type);
/* 325 */     if (c != null && this.reader.getClassAnnotation(XmlRegistry.class, c, null) != null) {
/* 326 */       if (!this.registries.containsKey(this.nav.getPackageName(c)))
/* 327 */         addRegistry(c, null); 
/* 328 */       return null;
/*     */     } 
/* 330 */     return getTypeInfo((T)ref.type, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected EnumLeafInfoImpl<T, C, F, M> createEnumLeafInfo(C clazz, Locatable upstream) {
/* 335 */     return new EnumLeafInfoImpl<T, C, F, M>(this, upstream, clazz, (T)this.nav.use(clazz));
/*     */   }
/*     */   
/*     */   protected ClassInfoImpl<T, C, F, M> createClassInfo(C clazz, Locatable upstream) {
/* 339 */     return new ClassInfoImpl<T, C, F, M>(this, upstream, clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ElementInfoImpl<T, C, F, M> createElementInfo(RegistryInfoImpl<T, C, F, M> registryInfo, M m) throws IllegalAnnotationException {
/* 344 */     return new ElementInfoImpl<T, C, F, M>(this, registryInfo, m);
/*     */   }
/*     */   
/*     */   protected ArrayInfoImpl<T, C, F, M> createArrayInfo(Locatable upstream, T arrayType) {
/* 348 */     return new ArrayInfoImpl<T, C, F, M>(this, upstream, arrayType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegistryInfo<T, C> addRegistry(C registryClass, Locatable upstream) {
/* 357 */     return new RegistryInfoImpl<T, C, F, M>(this, upstream, registryClass);
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
/*     */   public RegistryInfo<T, C> getRegistry(String packageName) {
/* 369 */     return this.registries.get(packageName);
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
/*     */   
/*     */   public TypeInfoSet<T, C, F, M> link() {
/* 387 */     assert !this.linked;
/* 388 */     this.linked = true;
/*     */     
/* 390 */     for (ElementInfoImpl<T, C, F, M> ei : this.typeInfoSet.getAllElements()) {
/* 391 */       ei.link();
/*     */     }
/* 393 */     for (ClassInfoImpl ci : this.typeInfoSet.beans().values()) {
/* 394 */       ci.link();
/*     */     }
/* 396 */     for (EnumLeafInfoImpl li : this.typeInfoSet.enums().values()) {
/* 397 */       li.link();
/*     */     }
/* 399 */     if (this.hadError) {
/* 400 */       return null;
/*     */     }
/* 402 */     return this.typeInfoSet;
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
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 418 */     this.errorHandler = errorHandler;
/*     */   }
/*     */   
/*     */   public final void reportError(IllegalAnnotationException e) {
/* 422 */     this.hadError = true;
/* 423 */     if (this.errorHandler != null)
/* 424 */       this.errorHandler.error(e); 
/*     */   }
/*     */   
/*     */   public boolean isReplaced(C sc) {
/* 428 */     return this.subclassReplacements.containsKey(sc);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ModelBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */