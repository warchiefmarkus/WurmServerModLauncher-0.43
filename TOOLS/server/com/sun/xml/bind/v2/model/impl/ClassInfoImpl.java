/*      */ package com.sun.xml.bind.v2.model.impl;
/*      */ 
/*      */ import com.sun.istack.FinalArrayList;
/*      */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*      */ import com.sun.xml.bind.v2.model.annotation.MethodLocatable;
/*      */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*      */ import com.sun.xml.bind.v2.model.core.Element;
/*      */ import com.sun.xml.bind.v2.model.core.ID;
/*      */ import com.sun.xml.bind.v2.model.core.NonElement;
/*      */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*      */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*      */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*      */ import com.sun.xml.bind.v2.runtime.Location;
/*      */ import com.sun.xml.bind.v2.util.EditDistance;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.AbstractList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import javax.xml.bind.annotation.XmlAccessOrder;
/*      */ import javax.xml.bind.annotation.XmlAccessType;
/*      */ import javax.xml.bind.annotation.XmlAccessorOrder;
/*      */ import javax.xml.bind.annotation.XmlAccessorType;
/*      */ import javax.xml.bind.annotation.XmlAnyAttribute;
/*      */ import javax.xml.bind.annotation.XmlAnyElement;
/*      */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*      */ import javax.xml.bind.annotation.XmlAttribute;
/*      */ import javax.xml.bind.annotation.XmlElement;
/*      */ import javax.xml.bind.annotation.XmlElementRef;
/*      */ import javax.xml.bind.annotation.XmlElementRefs;
/*      */ import javax.xml.bind.annotation.XmlElementWrapper;
/*      */ import javax.xml.bind.annotation.XmlElements;
/*      */ import javax.xml.bind.annotation.XmlID;
/*      */ import javax.xml.bind.annotation.XmlIDREF;
/*      */ import javax.xml.bind.annotation.XmlInlineBinaryData;
/*      */ import javax.xml.bind.annotation.XmlList;
/*      */ import javax.xml.bind.annotation.XmlMimeType;
/*      */ import javax.xml.bind.annotation.XmlMixed;
/*      */ import javax.xml.bind.annotation.XmlSchemaType;
/*      */ import javax.xml.bind.annotation.XmlTransient;
/*      */ import javax.xml.bind.annotation.XmlType;
/*      */ import javax.xml.bind.annotation.XmlValue;
/*      */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*      */ import javax.xml.namespace.QName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class ClassInfoImpl<T, C, F, M>
/*      */   extends TypeInfoImpl<T, C, F, M>
/*      */   implements ClassInfo<T, C>, Element<T, C>
/*      */ {
/*      */   protected final C clazz;
/*      */   private final QName elementName;
/*      */   private final QName typeName;
/*      */   private FinalArrayList<PropertyInfoImpl<T, C, F, M>> properties;
/*      */   private final String[] propOrder;
/*      */   private ClassInfoImpl<T, C, F, M> baseClass;
/*      */   private boolean baseClassComputed = false;
/*      */   private boolean hasSubClasses = false;
/*      */   protected PropertySeed<T, C, F, M> attributeWildcard;
/*  158 */   private M factoryMethod = null;
/*      */   
/*      */   ClassInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C clazz) {
/*  161 */     super(builder, upstream);
/*  162 */     this.clazz = clazz;
/*  163 */     assert clazz != null;
/*      */ 
/*      */     
/*  166 */     this.elementName = parseElementName(clazz);
/*      */ 
/*      */     
/*  169 */     XmlType t = (XmlType)reader().getClassAnnotation(XmlType.class, clazz, this);
/*  170 */     this.typeName = parseTypeName(clazz, t);
/*      */     
/*  172 */     if (t != null) {
/*  173 */       String[] propOrder = t.propOrder();
/*  174 */       if (propOrder.length == 0) {
/*  175 */         this.propOrder = null;
/*      */       }
/*  177 */       else if (propOrder[0].length() == 0) {
/*  178 */         this.propOrder = DEFAULT_ORDER;
/*      */       } else {
/*  180 */         this.propOrder = propOrder;
/*      */       } 
/*      */     } else {
/*  183 */       this.propOrder = DEFAULT_ORDER;
/*      */     } 
/*      */     
/*  186 */     if (nav().isInterface(clazz)) {
/*  187 */       builder.reportError(new IllegalAnnotationException(Messages.CANT_HANDLE_INTERFACE.format(new Object[] { nav().getClassName(clazz) }, ), this));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  192 */     if (!hasFactoryConstructor(t) && 
/*  193 */       !nav().hasDefaultConstructor(clazz)) {
/*      */       Messages msg;
/*  195 */       if (nav().isInnerClass(clazz)) {
/*  196 */         msg = Messages.CANT_HANDLE_INNER_CLASS;
/*      */       } else {
/*  198 */         msg = Messages.NO_DEFAULT_CONSTRUCTOR;
/*      */       } 
/*  200 */       builder.reportError(new IllegalAnnotationException(msg.format(new Object[] { nav().getClassName(clazz) }, ), this));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfoImpl<T, C, F, M> getBaseClass() {
/*  207 */     if (!this.baseClassComputed) {
/*  208 */       this.baseClassComputed = true;
/*      */       
/*  210 */       C s = (C)nav().getSuperClass(this.clazz);
/*  211 */       if (s == null || s == nav().asDecl(Object.class)) {
/*  212 */         this.baseClass = null;
/*      */       } else {
/*  214 */         NonElement<T, C> b = this.builder.getClassInfo(s, true, this);
/*  215 */         if (b instanceof ClassInfoImpl) {
/*  216 */           this.baseClass = (ClassInfoImpl)b;
/*  217 */           this.baseClass.hasSubClasses = true;
/*      */         } else {
/*  219 */           this.baseClass = null;
/*      */         } 
/*      */       } 
/*      */     } 
/*  223 */     return this.baseClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Element<T, C> getSubstitutionHead() {
/*  232 */     ClassInfoImpl<T, C, F, M> c = getBaseClass();
/*  233 */     while (c != null && !c.isElement())
/*  234 */       c = c.getBaseClass(); 
/*  235 */     return c;
/*      */   }
/*      */   
/*      */   public final C getClazz() {
/*  239 */     return this.clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfoImpl<T, C, F, M> getScope() {
/*  250 */     return null;
/*      */   }
/*      */   
/*      */   public final T getType() {
/*  254 */     return (T)nav().use(this.clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeReferencedByIDREF() {
/*  262 */     for (PropertyInfo<T, C> p : getProperties()) {
/*  263 */       if (p.id() == ID.ID)
/*  264 */         return true; 
/*      */     } 
/*  266 */     ClassInfoImpl<T, C, F, M> base = getBaseClass();
/*  267 */     if (base != null) {
/*  268 */       return base.canBeReferencedByIDREF();
/*      */     }
/*  270 */     return false;
/*      */   }
/*      */   
/*      */   public final String getName() {
/*  274 */     return nav().getClassName(this.clazz);
/*      */   }
/*      */   
/*      */   public <A extends Annotation> A readAnnotation(Class<A> a) {
/*  278 */     return (A)reader().getClassAnnotation(a, this.clazz, this);
/*      */   }
/*      */   
/*      */   public Element<T, C> asElement() {
/*  282 */     if (isElement()) {
/*  283 */       return this;
/*      */     }
/*  285 */     return null;
/*      */   }
/*      */   
/*      */   public List<? extends PropertyInfo<T, C>> getProperties() {
/*  289 */     if (this.properties != null) return (List)this.properties;
/*      */ 
/*      */     
/*  292 */     XmlAccessType at = getAccessType();
/*      */     
/*  294 */     this.properties = new FinalArrayList();
/*      */     
/*  296 */     findFieldProperties(this.clazz, at);
/*      */     
/*  298 */     findGetterSetterProperties(at);
/*      */     
/*  300 */     if (this.propOrder == DEFAULT_ORDER || this.propOrder == null) {
/*  301 */       XmlAccessOrder ao = getAccessorOrder();
/*  302 */       if (ao == XmlAccessOrder.ALPHABETICAL) {
/*  303 */         Collections.sort((List<PropertyInfoImpl<T, C, F, M>>)this.properties);
/*      */       }
/*      */     } else {
/*  306 */       PropertySorter sorter = new PropertySorter();
/*  307 */       for (PropertyInfoImpl<T, C, F, M> p : this.properties)
/*  308 */         sorter.checkedGet(p); 
/*  309 */       Collections.sort((List<PropertyInfoImpl<T, C, F, M>>)this.properties, sorter);
/*  310 */       sorter.checkUnusedProperties();
/*      */     } 
/*      */ 
/*      */     
/*  314 */     PropertyInfoImpl<T, C, F, M> vp = null;
/*  315 */     PropertyInfoImpl<T, C, F, M> ep = null;
/*      */     
/*  317 */     for (PropertyInfoImpl<T, C, F, M> p : this.properties) {
/*  318 */       switch (p.kind()) {
/*      */         case TRANSIENT:
/*      */         case ANY_ATTRIBUTE:
/*      */         case ATTRIBUTE:
/*  322 */           ep = p;
/*      */           continue;
/*      */         case VALUE:
/*  325 */           if (vp != null)
/*      */           {
/*  327 */             this.builder.reportError(new IllegalAnnotationException(Messages.MULTIPLE_VALUE_PROPERTY.format(new Object[0]), vp, p));
/*      */           }
/*      */ 
/*      */           
/*  331 */           if (getBaseClass() != null) {
/*  332 */             this.builder.reportError(new IllegalAnnotationException(Messages.XMLVALUE_IN_DERIVED_TYPE.format(new Object[0]), p));
/*      */           }
/*      */           
/*  335 */           vp = p;
/*      */           continue;
/*      */         
/*      */         case ELEMENT:
/*      */           continue;
/*      */       } 
/*      */       
/*      */       assert false;
/*      */     } 
/*  344 */     if (ep != null && vp != null)
/*      */     {
/*  346 */       this.builder.reportError(new IllegalAnnotationException(Messages.ELEMENT_AND_VALUE_PROPERTY.format(new Object[0]), vp, ep));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  353 */     return (List)this.properties;
/*      */   }
/*      */ 
/*      */   
/*      */   private void findFieldProperties(C c, XmlAccessType at) {
/*  358 */     C sc = (C)nav().getSuperClass(c);
/*  359 */     if (shouldRecurseSuperClass(sc)) {
/*  360 */       findFieldProperties(sc, at);
/*      */     }
/*  362 */     for (F f : nav().getDeclaredFields(c)) {
/*  363 */       Annotation[] annotations = reader().getAllFieldAnnotations(f, this);
/*  364 */       if (nav().isTransient(f)) {
/*      */         
/*  366 */         if (hasJAXBAnnotation(annotations)) {
/*  367 */           this.builder.reportError(new IllegalAnnotationException(Messages.TRANSIENT_FIELD_NOT_BINDABLE.format(new Object[] { nav().getFieldName(f) }, ), getSomeJAXBAnnotation(annotations)));
/*      */         }
/*      */         continue;
/*      */       } 
/*  371 */       if (nav().isStaticField(f)) {
/*      */         
/*  373 */         if (hasJAXBAnnotation(annotations))
/*  374 */           addProperty(createFieldSeed(f), annotations);  continue;
/*      */       } 
/*  376 */       if (at == XmlAccessType.FIELD || (at == XmlAccessType.PUBLIC_MEMBER && nav().isPublicField(f)) || hasJAXBAnnotation(annotations))
/*      */       {
/*      */         
/*  379 */         addProperty(createFieldSeed(f), annotations); } 
/*  380 */       checkFieldXmlLocation(f);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasValueProperty() {
/*  386 */     ClassInfoImpl<T, C, F, M> bc = getBaseClass();
/*  387 */     if (bc != null && bc.hasValueProperty()) {
/*  388 */       return true;
/*      */     }
/*  390 */     for (PropertyInfo<T, C> p : getProperties()) {
/*  391 */       if (p instanceof com.sun.xml.bind.v2.model.core.ValuePropertyInfo) return true;
/*      */     
/*      */     } 
/*  394 */     return false;
/*      */   }
/*      */   
/*      */   public PropertyInfo<T, C> getProperty(String name) {
/*  398 */     for (PropertyInfo<T, C> p : getProperties()) {
/*  399 */       if (p.getName().equals(name))
/*  400 */         return p; 
/*      */     } 
/*  402 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkFieldXmlLocation(F f) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends Annotation> T getClassOrPackageAnnotation(Class<T> type) {
/*  415 */     Annotation annotation = reader().getClassAnnotation(type, this.clazz, this);
/*  416 */     if (annotation != null) {
/*  417 */       return (T)annotation;
/*      */     }
/*  419 */     return (T)reader().getPackageAnnotation(type, this.clazz, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private XmlAccessType getAccessType() {
/*  427 */     XmlAccessorType xat = getClassOrPackageAnnotation(XmlAccessorType.class);
/*  428 */     if (xat != null) {
/*  429 */       return xat.value();
/*      */     }
/*  431 */     return XmlAccessType.PUBLIC_MEMBER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private XmlAccessOrder getAccessorOrder() {
/*  438 */     XmlAccessorOrder xao = getClassOrPackageAnnotation(XmlAccessorOrder.class);
/*  439 */     if (xao != null) {
/*  440 */       return xao.value();
/*      */     }
/*  442 */     return XmlAccessOrder.UNDEFINED;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class PropertySorter
/*      */     extends HashMap<String, Integer>
/*      */     implements Comparator<PropertyInfoImpl>
/*      */   {
/*  455 */     PropertyInfoImpl[] used = new PropertyInfoImpl[ClassInfoImpl.this.propOrder.length];
/*      */ 
/*      */ 
/*      */     
/*      */     private Set<String> collidedNames;
/*      */ 
/*      */ 
/*      */     
/*      */     PropertySorter() {
/*  464 */       super(ClassInfoImpl.this.propOrder.length);
/*  465 */       for (String name : ClassInfoImpl.this.propOrder) {
/*  466 */         if (put(name, Integer.valueOf(size())) != null)
/*      */         {
/*  468 */           ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_ENTRY_IN_PROP_ORDER.format(new Object[] { name }, ), ClassInfoImpl.this));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public int compare(PropertyInfoImpl o1, PropertyInfoImpl o2) {
/*  474 */       int lhs = checkedGet(o1);
/*  475 */       int rhs = checkedGet(o2);
/*      */       
/*  477 */       return lhs - rhs;
/*      */     }
/*      */     
/*      */     private int checkedGet(PropertyInfoImpl p) {
/*  481 */       Integer i = get(p.getName());
/*  482 */       if (i == null) {
/*      */         
/*  484 */         if ((p.kind()).isOrdered) {
/*  485 */           ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_MISSING_FROM_ORDER.format(new Object[] { p.getName() }, ), p));
/*      */         }
/*      */ 
/*      */         
/*  489 */         i = Integer.valueOf(size());
/*  490 */         put(p.getName(), i);
/*      */       } 
/*      */ 
/*      */       
/*  494 */       int ii = i.intValue();
/*  495 */       if (ii < this.used.length) {
/*  496 */         if (this.used[ii] != null && this.used[ii] != p) {
/*  497 */           if (this.collidedNames == null) this.collidedNames = new HashSet<String>();
/*      */           
/*  499 */           if (this.collidedNames.add(p.getName()))
/*      */           {
/*  501 */             ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_PROPERTIES.format(new Object[] { p.getName() }, ), p, this.used[ii]));
/*      */           }
/*      */         } 
/*  504 */         this.used[ii] = p;
/*      */       } 
/*      */       
/*  507 */       return i.intValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void checkUnusedProperties() {
/*  514 */       for (int i = 0; i < this.used.length; i++) {
/*  515 */         if (this.used[i] == null) {
/*  516 */           String unusedName = ClassInfoImpl.this.propOrder[i];
/*  517 */           String nearest = EditDistance.findNearest(unusedName, new AbstractList<String>() {
/*      */                 public String get(int index) {
/*  519 */                   return ((PropertyInfoImpl)ClassInfoImpl.this.properties.get(index)).getName();
/*      */                 }
/*      */                 
/*      */                 public int size() {
/*  523 */                   return ClassInfoImpl.this.properties.size();
/*      */                 }
/*      */               });
/*  526 */           ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_ORDER_CONTAINS_UNUSED_ENTRY.format(new Object[] { unusedName, nearest }, ), ClassInfoImpl.this));
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean hasProperties() {
/*  533 */     return !this.properties.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> T pickOne(T... args) {
/*  541 */     for (T arg : args) {
/*  542 */       if (arg != null)
/*  543 */         return arg; 
/*  544 */     }  return null;
/*      */   }
/*      */   
/*      */   private static <T> List<T> makeSet(T... args) {
/*  548 */     FinalArrayList<T> finalArrayList = new FinalArrayList();
/*  549 */     for (T arg : args) {
/*  550 */       if (arg != null) finalArrayList.add(arg); 
/*  551 */     }  return (List<T>)finalArrayList;
/*      */   }
/*      */   
/*      */   private static final class ConflictException extends Exception {
/*      */     final List<Annotation> annotations;
/*      */     
/*      */     public ConflictException(List<Annotation> one) {
/*  558 */       this.annotations = one;
/*      */     } }
/*      */   
/*      */   private static final class DupliateException extends Exception {
/*      */     final Annotation a1;
/*      */     
/*      */     public DupliateException(Annotation a1, Annotation a2) {
/*  565 */       this.a1 = a1;
/*  566 */       this.a2 = a2;
/*      */     }
/*      */     
/*      */     final Annotation a2;
/*      */   }
/*      */   
/*      */   private enum SecondaryAnnotation
/*      */   {
/*  574 */     JAVA_TYPE(1, new Class[] { XmlJavaTypeAdapter.class }),
/*  575 */     ID_IDREF(2, new Class[] { XmlID.class, XmlIDREF.class }),
/*  576 */     BINARY(4, new Class[] { XmlInlineBinaryData.class, XmlMimeType.class, XmlAttachmentRef.class }),
/*  577 */     ELEMENT_WRAPPER(8, new Class[] { XmlElementWrapper.class }),
/*  578 */     LIST(16, new Class[] { XmlList.class }),
/*  579 */     SCHEMA_TYPE(32, new Class[] { XmlSchemaType.class });
/*      */ 
/*      */ 
/*      */     
/*      */     final int bitMask;
/*      */ 
/*      */ 
/*      */     
/*      */     final Class<? extends Annotation>[] members;
/*      */ 
/*      */ 
/*      */     
/*      */     SecondaryAnnotation(int bitMask, Class<? extends Annotation>... members) {
/*  592 */       this.bitMask = bitMask;
/*  593 */       this.members = members;
/*      */     }
/*      */   }
/*      */   
/*  597 */   private static final SecondaryAnnotation[] SECONDARY_ANNOTATIONS = SecondaryAnnotation.values();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum PropertyGroup
/*      */   {
/*  606 */     TRANSIENT((String)new boolean[] { false, false, false, false, false, false }),
/*  607 */     ANY_ATTRIBUTE((String)new boolean[] { true, false, false, false, false, false }),
/*  608 */     ATTRIBUTE((String)new boolean[] { true, true, true, false, true, true }),
/*  609 */     VALUE((String)new boolean[] { true, true, true, false, true, true }),
/*  610 */     ELEMENT((String)new boolean[] { true, true, true, true, true, true }),
/*  611 */     ELEMENT_REF((String)new boolean[] { true, false, false, true, false, false }),
/*  612 */     MAP((String)new boolean[] { false, false, false, true, false, false });
/*      */ 
/*      */ 
/*      */     
/*      */     final int allowedsecondaryAnnotations;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     PropertyGroup(boolean... bits) {
/*  622 */       int mask = 0;
/*  623 */       assert bits.length == ClassInfoImpl.SECONDARY_ANNOTATIONS.length;
/*  624 */       for (int i = 0; i < bits.length; i++) {
/*  625 */         if (bits[i])
/*  626 */           mask |= (ClassInfoImpl.SECONDARY_ANNOTATIONS[i]).bitMask; 
/*      */       } 
/*  628 */       this.allowedsecondaryAnnotations = mask ^ 0xFFFFFFFF;
/*      */     }
/*      */     
/*      */     boolean allows(ClassInfoImpl.SecondaryAnnotation a) {
/*  632 */       return ((this.allowedsecondaryAnnotations & a.bitMask) == 0);
/*      */     }
/*      */   }
/*      */   
/*  636 */   private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  641 */   private static final HashMap<Class, Integer> ANNOTATION_NUMBER_MAP = new HashMap<Class<?>, Integer>();
/*      */   static {
/*  643 */     Class[] annotations = { XmlTransient.class, XmlAnyAttribute.class, XmlAttribute.class, XmlValue.class, XmlElement.class, XmlElements.class, XmlElementRef.class, XmlElementRefs.class, XmlAnyElement.class, XmlMixed.class };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  656 */     HashMap<Class<?>, Integer> m = ANNOTATION_NUMBER_MAP;
/*      */ 
/*      */     
/*  659 */     for (Class<?> c : annotations) {
/*  660 */       m.put(c, Integer.valueOf(m.size()));
/*      */     }
/*      */     
/*  663 */     int index = 20;
/*  664 */     for (SecondaryAnnotation sa : SECONDARY_ANNOTATIONS) {
/*  665 */       for (Class<? extends Annotation> member : sa.members)
/*  666 */         m.put(member, Integer.valueOf(index)); 
/*  667 */       index++;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkConflict(Annotation a, Annotation b) throws DupliateException {
/*  672 */     assert b != null;
/*  673 */     if (a != null) {
/*  674 */       throw new DupliateException(a, b);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addProperty(PropertySeed<T, C, F, M> seed, Annotation[] annotations) {
/*  697 */     XmlTransient t = null;
/*  698 */     XmlAnyAttribute aa = null;
/*  699 */     XmlAttribute a = null;
/*  700 */     XmlValue v = null;
/*  701 */     XmlElement e1 = null;
/*  702 */     XmlElements e2 = null;
/*  703 */     XmlElementRef r1 = null;
/*  704 */     XmlElementRefs r2 = null;
/*  705 */     XmlAnyElement xae = null;
/*  706 */     XmlMixed mx = null;
/*      */ 
/*      */     
/*  709 */     int secondaryAnnotations = 0;
/*      */     
/*      */     try {
/*  712 */       for (Annotation ann : annotations) {
/*  713 */         Integer index = ANNOTATION_NUMBER_MAP.get(ann.annotationType());
/*  714 */         if (index != null) {
/*  715 */           switch (index.intValue()) { case 0:
/*  716 */               checkConflict((Annotation)t, ann); t = (XmlTransient)ann; break;
/*  717 */             case 1: checkConflict((Annotation)aa, ann); aa = (XmlAnyAttribute)ann; break;
/*  718 */             case 2: checkConflict((Annotation)a, ann); a = (XmlAttribute)ann; break;
/*  719 */             case 3: checkConflict((Annotation)v, ann); v = (XmlValue)ann; break;
/*  720 */             case 4: checkConflict((Annotation)e1, ann); e1 = (XmlElement)ann; break;
/*  721 */             case 5: checkConflict((Annotation)e2, ann); e2 = (XmlElements)ann; break;
/*  722 */             case 6: checkConflict((Annotation)r1, ann); r1 = (XmlElementRef)ann; break;
/*  723 */             case 7: checkConflict((Annotation)r2, ann); r2 = (XmlElementRefs)ann; break;
/*  724 */             case 8: checkConflict((Annotation)xae, ann); xae = (XmlAnyElement)ann; break;
/*  725 */             case 9: checkConflict((Annotation)mx, ann); mx = (XmlMixed)ann;
/*      */               break;
/*      */             default:
/*  728 */               secondaryAnnotations |= 1 << index.intValue() - 20;
/*      */               break; }
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */       } 
/*  735 */       PropertyGroup group = null;
/*  736 */       int groupCount = 0;
/*      */       
/*  738 */       if (t != null) {
/*  739 */         group = PropertyGroup.TRANSIENT;
/*  740 */         groupCount++;
/*      */       } 
/*  742 */       if (aa != null) {
/*  743 */         group = PropertyGroup.ANY_ATTRIBUTE;
/*  744 */         groupCount++;
/*      */       } 
/*  746 */       if (a != null) {
/*  747 */         group = PropertyGroup.ATTRIBUTE;
/*  748 */         groupCount++;
/*      */       } 
/*  750 */       if (v != null) {
/*  751 */         group = PropertyGroup.VALUE;
/*  752 */         groupCount++;
/*      */       } 
/*  754 */       if (e1 != null || e2 != null) {
/*  755 */         group = PropertyGroup.ELEMENT;
/*  756 */         groupCount++;
/*      */       } 
/*  758 */       if (r1 != null || r2 != null || xae != null || mx != null) {
/*  759 */         group = PropertyGroup.ELEMENT_REF;
/*  760 */         groupCount++;
/*      */       } 
/*      */       
/*  763 */       if (groupCount > 1) {
/*      */         
/*  765 */         List<Annotation> err = makeSet(new Annotation[] { (Annotation)t, (Annotation)aa, (Annotation)a, (Annotation)v, pickOne(new Annotation[] { (Annotation)e1, (Annotation)e2 }), pickOne(new Annotation[] { (Annotation)r1, (Annotation)r2, (Annotation)xae }) });
/*  766 */         throw new ConflictException(err);
/*      */       } 
/*      */       
/*  769 */       if (group == null) {
/*      */ 
/*      */         
/*  772 */         assert groupCount == 0;
/*      */ 
/*      */         
/*  775 */         if (nav().isSubClassOf(seed.getRawType(), nav().ref(Map.class)) && !seed.hasAnnotation(XmlJavaTypeAdapter.class)) {
/*      */           
/*  777 */           group = PropertyGroup.MAP;
/*      */         } else {
/*  779 */           group = PropertyGroup.ELEMENT;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  784 */       if ((secondaryAnnotations & group.allowedsecondaryAnnotations) != 0) {
/*      */         
/*  786 */         for (SecondaryAnnotation sa : SECONDARY_ANNOTATIONS) {
/*  787 */           if (!group.allows(sa))
/*      */           {
/*  789 */             for (Class<? extends Annotation> m : sa.members) {
/*  790 */               Annotation offender = seed.readAnnotation(m);
/*  791 */               if (offender != null) {
/*      */                 
/*  793 */                 this.builder.reportError(new IllegalAnnotationException(Messages.ANNOTATION_NOT_ALLOWED.format(new Object[] { m.getSimpleName() }, ), offender));
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/*      */         assert false;
/*      */       } 
/*      */       
/*  804 */       switch (group) {
/*      */         case TRANSIENT:
/*      */           return;
/*      */         
/*      */         case ANY_ATTRIBUTE:
/*  809 */           if (this.attributeWildcard != null) {
/*  810 */             this.builder.reportError(new IllegalAnnotationException(Messages.TWO_ATTRIBUTE_WILDCARDS.format(new Object[] { nav().getClassName(getClazz()) }, ), (Annotation)aa, this.attributeWildcard));
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  815 */           this.attributeWildcard = seed;
/*      */           
/*  817 */           if (inheritsAttributeWildcard()) {
/*  818 */             this.builder.reportError(new IllegalAnnotationException(Messages.SUPER_CLASS_HAS_WILDCARD.format(new Object[0]), (Annotation)aa, getInheritedAttributeWildcard()));
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */ 
/*      */           
/*  825 */           if (!nav().isSubClassOf(seed.getRawType(), nav().ref(Map.class))) {
/*  826 */             this.builder.reportError(new IllegalAnnotationException(Messages.INVALID_ATTRIBUTE_WILDCARD_TYPE.format(new Object[] { nav().getTypeName(seed.getRawType()) }, ), (Annotation)aa, getInheritedAttributeWildcard()));
/*      */             return;
/*      */           } 
/*      */           return;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case ATTRIBUTE:
/*  835 */           this.properties.add(createAttributeProperty(seed));
/*      */           return;
/*      */         case VALUE:
/*  838 */           this.properties.add(createValueProperty(seed));
/*      */           return;
/*      */         case ELEMENT:
/*  841 */           this.properties.add(createElementProperty(seed));
/*      */           return;
/*      */         case ELEMENT_REF:
/*  844 */           this.properties.add(createReferenceProperty(seed));
/*      */           return;
/*      */         case MAP:
/*  847 */           this.properties.add(createMapProperty(seed));
/*      */           return;
/*      */       } 
/*      */       
/*      */       assert false;
/*  852 */     } catch (ConflictException x) {
/*      */       
/*  854 */       List<Annotation> err = x.annotations;
/*      */       
/*  856 */       this.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(new Object[] { nav().getClassName(getClazz()) + '#' + seed.getName(), ((Annotation)err.get(0)).annotationType().getName(), ((Annotation)err.get(1)).annotationType().getName() }, ), err.get(0), err.get(1)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  863 */     catch (DupliateException e) {
/*      */       
/*  865 */       this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_ANNOTATIONS.format(new Object[] { e.a1.annotationType().getName() }, ), e.a1, e.a2));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ReferencePropertyInfoImpl<T, C, F, M> createReferenceProperty(PropertySeed<T, C, F, M> seed) {
/*  874 */     return new ReferencePropertyInfoImpl<T, C, F, M>(this, seed);
/*      */   }
/*      */   
/*      */   protected AttributePropertyInfoImpl<T, C, F, M> createAttributeProperty(PropertySeed<T, C, F, M> seed) {
/*  878 */     return new AttributePropertyInfoImpl<T, C, F, M>(this, seed);
/*      */   }
/*      */   
/*      */   protected ValuePropertyInfoImpl<T, C, F, M> createValueProperty(PropertySeed<T, C, F, M> seed) {
/*  882 */     return new ValuePropertyInfoImpl<T, C, F, M>(this, seed);
/*      */   }
/*      */   
/*      */   protected ElementPropertyInfoImpl<T, C, F, M> createElementProperty(PropertySeed<T, C, F, M> seed) {
/*  886 */     return new ElementPropertyInfoImpl<T, C, F, M>(this, seed);
/*      */   }
/*      */   
/*      */   protected MapPropertyInfoImpl<T, C, F, M> createMapProperty(PropertySeed<T, C, F, M> seed) {
/*  890 */     return new MapPropertyInfoImpl<T, C, F, M>(this, seed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void findGetterSetterProperties(XmlAccessType at) {
/*  900 */     Map<String, M> getters = new LinkedHashMap<String, M>();
/*  901 */     Map<String, M> setters = new LinkedHashMap<String, M>();
/*      */     
/*  903 */     C c = this.clazz;
/*      */     do {
/*  905 */       collectGetterSetters(this.clazz, getters, setters);
/*      */ 
/*      */       
/*  908 */       c = (C)nav().getSuperClass(c);
/*  909 */     } while (shouldRecurseSuperClass(c));
/*      */ 
/*      */ 
/*      */     
/*  913 */     Set<String> complete = new TreeSet<String>(getters.keySet());
/*  914 */     complete.retainAll(setters.keySet());
/*      */     
/*  916 */     resurrect(getters, complete);
/*  917 */     resurrect(setters, complete);
/*      */ 
/*      */     
/*  920 */     for (String name : complete) {
/*  921 */       M getter = getters.get(name);
/*  922 */       M setter = setters.get(name);
/*      */       
/*  924 */       Annotation[] ga = (getter != null) ? reader().getAllMethodAnnotations(getter, (Locatable)new MethodLocatable(this, getter, nav())) : EMPTY_ANNOTATIONS;
/*  925 */       Annotation[] sa = (setter != null) ? reader().getAllMethodAnnotations(setter, (Locatable)new MethodLocatable(this, setter, nav())) : EMPTY_ANNOTATIONS;
/*      */       
/*  927 */       boolean hasAnnotation = (hasJAXBAnnotation(ga) || hasJAXBAnnotation(sa));
/*  928 */       boolean isOverriding = false;
/*  929 */       if (!hasAnnotation)
/*      */       {
/*      */         
/*  932 */         isOverriding = ((getter != null && nav().isOverriding(getter, c)) || (setter != null && nav().isOverriding(setter, c)));
/*      */       }
/*      */ 
/*      */       
/*  936 */       if ((at == XmlAccessType.PROPERTY && !isOverriding) || (at == XmlAccessType.PUBLIC_MEMBER && isConsideredPublic(getter) && isConsideredPublic(setter) && !isOverriding) || hasAnnotation) {
/*      */         Annotation[] r;
/*      */ 
/*      */         
/*  940 */         if (getter != null && setter != null && !nav().getReturnType(getter).equals(nav().getMethodParameters(setter)[0])) {
/*      */ 
/*      */           
/*  943 */           this.builder.reportError(new IllegalAnnotationException(Messages.GETTER_SETTER_INCOMPATIBLE_TYPE.format(new Object[] { nav().getTypeName(nav().getReturnType(getter)), nav().getTypeName(nav().getMethodParameters(setter)[0]) }, ), (Locatable)new MethodLocatable(this, getter, nav()), (Locatable)new MethodLocatable(this, setter, nav())));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  955 */         if (ga.length == 0) {
/*  956 */           r = sa;
/*      */         }
/*  958 */         else if (sa.length == 0) {
/*  959 */           r = ga;
/*      */         } else {
/*  961 */           r = new Annotation[ga.length + sa.length];
/*  962 */           System.arraycopy(ga, 0, r, 0, ga.length);
/*  963 */           System.arraycopy(sa, 0, r, ga.length, sa.length);
/*      */         } 
/*      */         
/*  966 */         addProperty(createAccessorSeed(getter, setter), r);
/*      */       } 
/*      */     } 
/*      */     
/*  970 */     getters.keySet().removeAll(complete);
/*  971 */     setters.keySet().removeAll(complete);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void collectGetterSetters(C c, Map<String, M> getters, Map<String, M> setters) {
/*  988 */     C sc = (C)nav().getSuperClass(c);
/*  989 */     if (shouldRecurseSuperClass(sc)) {
/*  990 */       collectGetterSetters(sc, getters, setters);
/*      */     }
/*      */     
/*  993 */     Collection<? extends M> methods = nav().getDeclaredMethods(c);
/*  994 */     Map<String, List<M>> allSetters = new LinkedHashMap<String, List<M>>();
/*  995 */     for (M method : methods) {
/*  996 */       boolean used = false;
/*      */       
/*  998 */       if (nav().isBridgeMethod(method)) {
/*      */         continue;
/*      */       }
/* 1001 */       String name = nav().getMethodName(method);
/* 1002 */       int arity = (nav().getMethodParameters(method)).length;
/*      */       
/* 1004 */       if (nav().isStaticMethod(method)) {
/* 1005 */         ensureNoAnnotation(method);
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */       
/* 1012 */       String propName = getPropertyNameFromGetMethod(name);
/* 1013 */       if (propName != null && arity == 0) {
/* 1014 */         getters.put(propName, method);
/* 1015 */         used = true;
/*      */       } 
/*      */ 
/*      */       
/* 1019 */       propName = getPropertyNameFromSetMethod(name);
/* 1020 */       if (propName != null && arity == 1) {
/* 1021 */         List<M> propSetters = allSetters.get(propName);
/* 1022 */         if (null == propSetters) {
/* 1023 */           propSetters = new ArrayList<M>();
/* 1024 */           allSetters.put(propName, propSetters);
/*      */         } 
/* 1026 */         propSetters.add(method);
/* 1027 */         used = true;
/*      */       } 
/*      */       
/* 1030 */       if (!used) {
/* 1031 */         ensureNoAnnotation(method);
/*      */       }
/*      */     } 
/*      */     
/* 1035 */     for (Map.Entry<String, M> entry : getters.entrySet()) {
/* 1036 */       String propName = entry.getKey();
/* 1037 */       M getter = entry.getValue();
/* 1038 */       List<M> propSetters = allSetters.remove(propName);
/* 1039 */       if (null == propSetters) {
/*      */         continue;
/*      */       }
/*      */       
/* 1043 */       T getterType = (T)nav().getReturnType(getter);
/* 1044 */       for (M setter : propSetters) {
/* 1045 */         T setterType = (T)nav().getMethodParameters(setter)[0];
/* 1046 */         if (setterType.equals(getterType)) {
/* 1047 */           setters.put(propName, setter);
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1054 */     for (Map.Entry<String, List<M>> e : allSetters.entrySet()) {
/* 1055 */       setters.put(e.getKey(), ((List<M>)e.getValue()).get(0));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean shouldRecurseSuperClass(C sc) {
/* 1063 */     return (sc != null && (this.builder.isReplaced(sc) || reader().hasClassAnnotation(sc, XmlTransient.class)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isConsideredPublic(M m) {
/* 1071 */     return (m == null || nav().isPublicMethod(m));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void resurrect(Map<String, M> methods, Set<String> complete) {
/* 1079 */     for (Map.Entry<String, M> e : methods.entrySet()) {
/* 1080 */       if (complete.contains(e.getKey()))
/*      */         continue; 
/* 1082 */       if (hasJAXBAnnotation(reader().getAllMethodAnnotations(e.getValue(), this))) {
/* 1083 */         complete.add(e.getKey());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void ensureNoAnnotation(M method) {
/* 1092 */     Annotation[] annotations = reader().getAllMethodAnnotations(method, this);
/* 1093 */     for (Annotation a : annotations) {
/* 1094 */       if (isJAXBAnnotation(a)) {
/* 1095 */         this.builder.reportError(new IllegalAnnotationException(Messages.ANNOTATION_ON_WRONG_METHOD.format(new Object[0]), a));
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isJAXBAnnotation(Annotation a) {
/* 1107 */     return ANNOTATION_NUMBER_MAP.containsKey(a.annotationType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean hasJAXBAnnotation(Annotation[] annotations) {
/* 1114 */     return (getSomeJAXBAnnotation(annotations) != null);
/*      */   }
/*      */   
/*      */   private static Annotation getSomeJAXBAnnotation(Annotation[] annotations) {
/* 1118 */     for (Annotation a : annotations) {
/* 1119 */       if (isJAXBAnnotation(a))
/* 1120 */         return a; 
/* 1121 */     }  return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getPropertyNameFromGetMethod(String name) {
/* 1132 */     if (name.startsWith("get") && name.length() > 3)
/* 1133 */       return name.substring(3); 
/* 1134 */     if (name.startsWith("is") && name.length() > 2)
/* 1135 */       return name.substring(2); 
/* 1136 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getPropertyNameFromSetMethod(String name) {
/* 1146 */     if (name.startsWith("set") && name.length() > 3)
/* 1147 */       return name.substring(3); 
/* 1148 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertySeed<T, C, F, M> createFieldSeed(F f) {
/* 1158 */     return new FieldPropertySeed<T, C, F, M>(this, f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertySeed<T, C, F, M> createAccessorSeed(M getter, M setter) {
/* 1165 */     return new GetterSetterPropertySeed<T, C, F, M>(this, getter, setter);
/*      */   }
/*      */   
/*      */   public final boolean isElement() {
/* 1169 */     return (this.elementName != null);
/*      */   }
/*      */   
/*      */   public boolean isAbstract() {
/* 1173 */     return nav().isAbstract(this.clazz);
/*      */   }
/*      */   
/*      */   public boolean isOrdered() {
/* 1177 */     return (this.propOrder != null);
/*      */   }
/*      */   
/*      */   public final boolean isFinal() {
/* 1181 */     return nav().isFinal(this.clazz);
/*      */   }
/*      */   
/*      */   public final boolean hasSubClasses() {
/* 1185 */     return this.hasSubClasses;
/*      */   }
/*      */   
/*      */   public final boolean hasAttributeWildcard() {
/* 1189 */     return (declaresAttributeWildcard() || inheritsAttributeWildcard());
/*      */   }
/*      */   
/*      */   public final boolean inheritsAttributeWildcard() {
/* 1193 */     return (getInheritedAttributeWildcard() != null);
/*      */   }
/*      */   
/*      */   public final boolean declaresAttributeWildcard() {
/* 1197 */     return (this.attributeWildcard != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PropertySeed<T, C, F, M> getInheritedAttributeWildcard() {
/* 1204 */     for (ClassInfoImpl<T, C, F, M> c = getBaseClass(); c != null; c = c.getBaseClass()) {
/* 1205 */       if (c.attributeWildcard != null)
/* 1206 */         return c.attributeWildcard; 
/* 1207 */     }  return null;
/*      */   }
/*      */   
/*      */   public final QName getElementName() {
/* 1211 */     return this.elementName;
/*      */   }
/*      */   
/*      */   public final QName getTypeName() {
/* 1215 */     return this.typeName;
/*      */   }
/*      */   
/*      */   public final boolean isSimpleType() {
/* 1219 */     List<? extends PropertyInfo> props = (List)getProperties();
/* 1220 */     if (props.size() != 1) return false; 
/* 1221 */     return (((PropertyInfo)props.get(0)).kind() == PropertyKind.VALUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void link() {
/* 1229 */     getProperties();
/*      */ 
/*      */     
/* 1232 */     Map<String, PropertyInfoImpl> names = new HashMap<String, PropertyInfoImpl>();
/* 1233 */     for (PropertyInfoImpl<T, C, F, M> p : this.properties) {
/* 1234 */       p.link();
/* 1235 */       PropertyInfoImpl old = names.put(p.getName(), p);
/* 1236 */       if (old != null) {
/* 1237 */         this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_COLLISION.format(new Object[] { p.getName() }, ), p, old));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1242 */     super.link();
/*      */   }
/*      */   
/*      */   public Location getLocation() {
/* 1246 */     return nav().getClassLocation(this.clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasFactoryConstructor(XmlType t) {
/* 1258 */     if (t == null) return false;
/*      */     
/* 1260 */     String method = t.factoryMethod();
/* 1261 */     T fClass = (T)reader().getClassValue((Annotation)t, "factoryClass");
/* 1262 */     if (method.length() > 0) {
/* 1263 */       if (fClass.equals(nav().ref(XmlType.DEFAULT.class))) {
/* 1264 */         fClass = (T)nav().use(this.clazz);
/*      */       }
/* 1266 */       for (M m : nav().getDeclaredMethods(nav().asDecl(fClass))) {
/*      */         
/* 1268 */         if (nav().getMethodName(m).equals(method) && nav().getReturnType(m).equals(nav().use(this.clazz)) && (nav().getMethodParameters(m)).length == 0 && nav().isStaticMethod(m)) {
/*      */ 
/*      */ 
/*      */           
/* 1272 */           this.factoryMethod = m;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1276 */       if (this.factoryMethod == null) {
/* 1277 */         this.builder.reportError(new IllegalAnnotationException(Messages.NO_FACTORY_METHOD.format(new Object[] { nav().getClassName(nav().asDecl(fClass)), method }, ), this));
/*      */       }
/*      */     }
/* 1280 */     else if (!fClass.equals(nav().ref(XmlType.DEFAULT.class))) {
/* 1281 */       this.builder.reportError(new IllegalAnnotationException(Messages.FACTORY_CLASS_NEEDS_FACTORY_METHOD.format(new Object[] { nav().getClassName(nav().asDecl(fClass)) }, ), this));
/*      */     } 
/*      */     
/* 1284 */     return (this.factoryMethod != null);
/*      */   }
/*      */   
/*      */   public Method getFactoryMethod() {
/* 1288 */     return (Method)this.factoryMethod;
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1292 */     return "ClassInfo(" + this.clazz + ')';
/*      */   }
/*      */   
/* 1295 */   private static final String[] DEFAULT_ORDER = new String[0];
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ClassInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */