/*     */ package com.sun.tools.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JDocComment;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlAccessorTypeWriter;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlEnum;
/*     */ import javax.xml.bind.annotation.XmlEnumValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlEnum(Boolean.class)
/*     */ public enum ImplStructureStrategy
/*     */ {
/*  73 */   BEAN_ONLY
/*     */   {
/*     */     protected Result createClasses(Outline outline, CClassInfo bean) {
/*  76 */       JClassContainer parent = outline.getContainer(bean.parent(), Aspect.EXPOSED);
/*     */       
/*  78 */       JDefinedClass impl = outline.getClassFactory().createClass(parent, 0x1 | (parent.isPackage() ? 0 : 16) | (bean.isAbstract() ? 32 : 0), bean.shortName, bean.getLocator());
/*     */ 
/*     */ 
/*     */       
/*  82 */       ((XmlAccessorTypeWriter)impl.annotate2(XmlAccessorTypeWriter.class)).value(XmlAccessType.FIELD);
/*     */       
/*  84 */       return new Result(impl, impl);
/*     */     }
/*     */     
/*     */     protected JPackage getPackage(JPackage pkg, Aspect a) {
/*  88 */       return pkg;
/*     */     }
/*     */     
/*     */     protected MethodWriter createMethodWriter(final ClassOutlineImpl target) {
/*  92 */       assert target.ref == target.implClass;
/*     */       
/*  94 */       return new MethodWriter(target) {
/*  95 */           private final JDefinedClass impl = target.implClass;
/*     */           
/*     */           private JMethod implMethod;
/*     */           
/*     */           public JVar addParameter(JType type, String name) {
/* 100 */             return this.implMethod.param(type, name);
/*     */           }
/*     */           
/*     */           public JMethod declareMethod(JType returnType, String methodName) {
/* 104 */             this.implMethod = this.impl.method(1, returnType, methodName);
/* 105 */             return this.implMethod;
/*     */           }
/*     */           
/*     */           public JDocComment javadoc() {
/* 109 */             return this.implMethod.javadoc();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     protected void _extends(ClassOutlineImpl derived, ClassOutlineImpl base) {
/* 115 */       derived.implClass._extends(base.implRef);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   INTF_AND_IMPL
/*     */   {
/*     */     protected Result createClasses(Outline outline, CClassInfo bean) {
/* 128 */       JClassContainer parent = outline.getContainer(bean.parent(), Aspect.EXPOSED);
/*     */       
/* 130 */       JDefinedClass intf = outline.getClassFactory().createInterface(parent, bean.shortName, bean.getLocator());
/*     */ 
/*     */       
/* 133 */       parent = outline.getContainer(bean.parent(), Aspect.IMPLEMENTATION);
/* 134 */       JDefinedClass impl = outline.getClassFactory().createClass(parent, 0x1 | (parent.isPackage() ? 0 : 16) | (bean.isAbstract() ? 32 : 0), bean.shortName + "Impl", bean.getLocator());
/*     */ 
/*     */ 
/*     */       
/* 138 */       ((XmlAccessorTypeWriter)impl.annotate2(XmlAccessorTypeWriter.class)).value(XmlAccessType.FIELD);
/*     */       
/* 140 */       impl._implements((JClass)intf);
/*     */       
/* 142 */       return new Result(intf, impl);
/*     */     }
/*     */     
/*     */     protected JPackage getPackage(JPackage pkg, Aspect a) {
/* 146 */       switch (a) {
/*     */         case EXPOSED:
/* 148 */           return pkg;
/*     */         case IMPLEMENTATION:
/* 150 */           return pkg.subPackage("impl");
/*     */       } 
/*     */       assert false;
/* 153 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     protected MethodWriter createMethodWriter(final ClassOutlineImpl target) {
/* 158 */       return new MethodWriter(target) {
/* 159 */           private final JDefinedClass intf = target.ref;
/* 160 */           private final JDefinedClass impl = target.implClass;
/*     */           
/*     */           private JMethod intfMethod;
/*     */           
/*     */           private JMethod implMethod;
/*     */           
/*     */           public JVar addParameter(JType type, String name) {
/* 167 */             if (this.intf != null)
/* 168 */               this.intfMethod.param(type, name); 
/* 169 */             return this.implMethod.param(type, name);
/*     */           }
/*     */           
/*     */           public JMethod declareMethod(JType returnType, String methodName) {
/* 173 */             if (this.intf != null)
/* 174 */               this.intfMethod = this.intf.method(0, returnType, methodName); 
/* 175 */             this.implMethod = this.impl.method(1, returnType, methodName);
/* 176 */             return this.implMethod;
/*     */           }
/*     */           
/*     */           public JDocComment javadoc() {
/* 180 */             if (this.intf != null) {
/* 181 */               return this.intfMethod.javadoc();
/*     */             }
/* 183 */             return this.implMethod.javadoc();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     protected void _extends(ClassOutlineImpl derived, ClassOutlineImpl base) {
/* 189 */       derived.implClass._extends(base.implRef);
/* 190 */       derived.ref._implements((JClass)base.ref);
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Result createClasses(Outline paramOutline, CClassInfo paramCClassInfo);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract JPackage getPackage(JPackage paramJPackage, Aspect paramAspect);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract MethodWriter createMethodWriter(ClassOutlineImpl paramClassOutlineImpl);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void _extends(ClassOutlineImpl paramClassOutlineImpl1, ClassOutlineImpl paramClassOutlineImpl2);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Result
/*     */   {
/*     */     public final JDefinedClass exposed;
/*     */ 
/*     */     
/*     */     public final JDefinedClass implementation;
/*     */ 
/*     */ 
/*     */     
/*     */     public Result(JDefinedClass exposed, JDefinedClass implementation) {
/* 223 */       this.exposed = exposed;
/* 224 */       this.implementation = implementation;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\ImplStructureStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */