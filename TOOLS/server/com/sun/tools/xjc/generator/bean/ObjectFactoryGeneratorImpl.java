/*     */ package com.sun.tools.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JCast;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlElementDeclWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlRegistryWriter;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.Constructor;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.xjc.outline.FieldOutline;
/*     */ import com.sun.xml.bind.v2.TODO;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
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
/*     */ abstract class ObjectFactoryGeneratorImpl
/*     */   extends ObjectFactoryGenerator
/*     */ {
/*     */   private final BeanGenerator outline;
/*     */   private final Model model;
/*     */   private final JCodeModel codeModel;
/*     */   private final JClass classRef;
/*     */   private final JDefinedClass objectFactory;
/*  97 */   private final HashMap<QName, JFieldVar> qnameMap = new HashMap<QName, JFieldVar>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   private final Map<String, CElementInfo> elementFactoryNames = new HashMap<String, CElementInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   private final Map<String, ClassOutlineImpl> valueFactoryNames = new HashMap<String, ClassOutlineImpl>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass getObjectFactory() {
/* 119 */     return this.objectFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectFactoryGeneratorImpl(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 126 */     this.outline = outline;
/* 127 */     this.model = model;
/* 128 */     this.codeModel = this.model.codeModel;
/* 129 */     this.classRef = this.codeModel.ref(Class.class);
/*     */ 
/*     */     
/* 132 */     this.objectFactory = this.outline.getClassFactory().createClass((JClassContainer)targetPackage, "ObjectFactory", null);
/*     */     
/* 134 */     this.objectFactory.annotate2(XmlRegistryWriter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     JMethod m1 = this.objectFactory.constructor(1);
/* 141 */     m1.javadoc().append("Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: " + targetPackage.name());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     this.objectFactory.javadoc().append("This object contains factory methods for each \nJava content interface and Java element interface \ngenerated in the " + targetPackage.name() + " package. \n" + "<p>An ObjectFactory allows you to programatically \n" + "construct new instances of the Java representation \n" + "for XML content. The Java representation of XML \n" + "content can consist of schema derived interfaces \n" + "and classes representing the binding of schema \n" + "type definitions, element declarations and model \n" + "groups.  Factory methods for each of these are \n" + "provided in this class.");
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
/*     */   protected final void populate(CElementInfo ei, Aspect impl, Aspect exposed) {
/*     */     JDefinedClass jDefinedClass;
/*     */     JExpression declaredType;
/* 165 */     JType exposedElementType = ei.toType(this.outline, exposed);
/* 166 */     JType exposedType = ei.getContentInMemoryType().toType(this.outline, exposed);
/* 167 */     JType implType = ei.getContentInMemoryType().toType(this.outline, impl);
/* 168 */     String namespaceURI = ei.getElementName().getNamespaceURI();
/* 169 */     String localPart = ei.getElementName().getLocalPart();
/*     */     
/* 171 */     JClass scope = null;
/* 172 */     if (ei.getScope() != null) {
/* 173 */       jDefinedClass = (this.outline.getClazz(ei.getScope())).implClass;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 178 */     if (ei.isAbstract())
/*     */     {
/*     */       
/* 181 */       TODO.checkSpec();
/*     */     }
/*     */ 
/*     */     
/* 185 */     CElementInfo existing = this.elementFactoryNames.put(ei.getSqueezedName(), ei);
/* 186 */     if (existing != null) {
/* 187 */       this.outline.getErrorReceiver().error(existing.getLocator(), Messages.OBJECT_FACTORY_CONFLICT.format(new Object[] { ei.getSqueezedName() }));
/*     */       
/* 189 */       this.outline.getErrorReceiver().error(ei.getLocator(), Messages.OBJECT_FACTORY_CONFLICT_RELATED.format(new Object[0]));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
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
/*     */ 
/*     */     
/* 217 */     JMethod m = this.objectFactory.method(1, exposedElementType, "create" + ei.getSqueezedName());
/* 218 */     JVar $value = m.param(exposedType, "value");
/*     */ 
/*     */     
/* 221 */     if (implType.boxify().isParameterized() || !exposedType.equals(implType)) {
/* 222 */       JCast jCast = JExpr.cast((JType)this.classRef, implType.boxify().dotclass());
/*     */     } else {
/* 224 */       declaredType = implType.boxify().dotclass();
/* 225 */     }  JExpression scopeClass = (jDefinedClass == null) ? JExpr._null() : jDefinedClass.dotclass();
/*     */ 
/*     */     
/* 228 */     JInvocation exp = JExpr._new(exposedElementType);
/* 229 */     if (!ei.hasClass()) {
/* 230 */       exp.arg(getQNameInvocation(ei));
/* 231 */       exp.arg(declaredType);
/* 232 */       exp.arg(scopeClass);
/*     */     } 
/* 234 */     if (implType == exposedType) {
/* 235 */       exp.arg((JExpression)$value);
/*     */     } else {
/* 237 */       exp.arg((JExpression)JExpr.cast(implType, (JExpression)$value));
/*     */     } 
/* 239 */     m.body()._return((JExpression)exp);
/*     */     
/* 241 */     m.javadoc().append("Create an instance of ").append(exposedElementType).append("}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     XmlElementDeclWriter xemw = (XmlElementDeclWriter)m.annotate2(XmlElementDeclWriter.class);
/* 247 */     xemw.namespace(namespaceURI).name(localPart);
/* 248 */     if (jDefinedClass != null) {
/* 249 */       xemw.scope((JType)jDefinedClass);
/*     */     }
/* 251 */     if (ei.getSubstitutionHead() != null) {
/* 252 */       QName n = ei.getSubstitutionHead().getElementName();
/* 253 */       xemw.substitutionHeadNamespace(n.getNamespaceURI());
/* 254 */       xemw.substitutionHeadName(n.getLocalPart());
/*     */     } 
/*     */     
/* 257 */     if (ei.getDefaultValue() != null) {
/* 258 */       xemw.defaultValue(ei.getDefaultValue());
/*     */     }
/* 260 */     if (ei.getProperty().inlineBinaryData()) {
/* 261 */       m.annotate(XmlInlineBinaryData.class);
/*     */     }
/*     */     
/* 264 */     this.outline.generateAdapterIfNecessary((CPropertyInfo)ei.getProperty(), (JAnnotatable)m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JExpression getQNameInvocation(CElementInfo ei) {
/* 273 */     QName name = ei.getElementName();
/* 274 */     if (this.qnameMap.containsKey(name)) {
/* 275 */       return (JExpression)this.qnameMap.get(name);
/*     */     }
/*     */     
/* 278 */     if (this.qnameMap.size() > 1024)
/*     */     {
/* 280 */       return (JExpression)createQName(name);
/*     */     }
/*     */ 
/*     */     
/* 284 */     JFieldVar qnameField = this.objectFactory.field(28, QName.class, '_' + ei.getSqueezedName() + "_QNAME", (JExpression)createQName(name));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 289 */     this.qnameMap.put(name, qnameField);
/*     */     
/* 291 */     return (JExpression)qnameField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JInvocation createQName(QName name) {
/* 298 */     return JExpr._new(this.codeModel.ref(QName.class)).arg(name.getNamespaceURI()).arg(name.getLocalPart());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void populate(ClassOutlineImpl cc, JClass sigType) {
/* 309 */     if (!cc.target.isAbstract()) {
/* 310 */       JMethod m = this.objectFactory.method(1, (JType)sigType, "create" + cc.target.getSqueezedName());
/*     */       
/* 312 */       m.body()._return((JExpression)JExpr._new(cc.implRef));
/*     */ 
/*     */       
/* 315 */       m.javadoc().append("Create an instance of ").append(cc.ref);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     Collection<? extends Constructor> consl = cc.target.getConstructors();
/* 323 */     if (consl.size() != 0)
/*     */     {
/*     */       
/* 326 */       cc.implClass.constructor(1);
/*     */     }
/*     */ 
/*     */     
/* 330 */     String name = cc.target.getSqueezedName();
/* 331 */     ClassOutlineImpl existing = this.valueFactoryNames.put(name, cc);
/* 332 */     if (existing != null) {
/* 333 */       this.outline.getErrorReceiver().error(existing.target.getLocator(), Messages.OBJECT_FACTORY_CONFLICT.format(new Object[] { name }));
/*     */       
/* 335 */       this.outline.getErrorReceiver().error(cc.target.getLocator(), Messages.OBJECT_FACTORY_CONFLICT_RELATED.format(new Object[0]));
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 341 */     for (Constructor cons : consl) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 347 */       JMethod m = this.objectFactory.method(1, (JType)cc.ref, "create" + cc.target.getSqueezedName());
/*     */       
/* 349 */       JInvocation inv = JExpr._new(cc.implRef);
/* 350 */       m.body()._return((JExpression)inv);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 356 */       m.javadoc().append("Create an instance of ").append(cc.ref).addThrows(JAXBException.class).append("if an error occurs");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 365 */       JMethod c = cc.implClass.constructor(1);
/*     */       
/* 367 */       for (String fieldName : cons.fields) {
/* 368 */         CPropertyInfo field = cc.target.getProperty(fieldName);
/* 369 */         if (field == null) {
/* 370 */           this.outline.getErrorReceiver().error(cc.target.getLocator(), Messages.ILLEGAL_CONSTRUCTOR_PARAM.format(new Object[] { fieldName }));
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 375 */           fieldName = camelize(fieldName);
/*     */           
/* 377 */           FieldOutline fo = this.outline.getField(field);
/* 378 */           FieldAccessor accessor = fo.create(JExpr._this());
/*     */ 
/*     */ 
/*     */           
/* 382 */           inv.arg((JExpression)m.param(fo.getRawType(), fieldName));
/*     */           
/* 384 */           JVar $var = c.param(fo.getRawType(), fieldName);
/* 385 */           accessor.fromRawValue(c.body(), '_' + fieldName, (JExpression)$var);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String camelize(String s) {
/* 393 */     return Character.toLowerCase(s.charAt(0)) + s.substring(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\ObjectFactoryGeneratorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */