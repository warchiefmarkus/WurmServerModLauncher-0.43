/*     */ package com.sun.tools.xjc.util;
/*     */ 
/*     */ import com.sun.codemodel.ClassType;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CodeModelClassFactory
/*     */ {
/*     */   private ErrorReceiver errorReceiver;
/*  66 */   private int ticketMaster = 0;
/*     */ 
/*     */   
/*     */   public CodeModelClassFactory(ErrorReceiver _errorReceiver) {
/*  70 */     this.errorReceiver = _errorReceiver;
/*     */   }
/*     */   
/*     */   public JDefinedClass createClass(JClassContainer parent, String name, Locator source) {
/*  74 */     return createClass(parent, 1, name, source);
/*     */   }
/*     */   public JDefinedClass createClass(JClassContainer parent, int mod, String name, Locator source) {
/*  77 */     return createClass(parent, mod, name, source, ClassType.CLASS);
/*     */   }
/*     */   
/*     */   public JDefinedClass createInterface(JClassContainer parent, String name, Locator source) {
/*  81 */     return createInterface(parent, 1, name, source);
/*     */   }
/*     */   public JDefinedClass createInterface(JClassContainer parent, int mod, String name, Locator source) {
/*  84 */     return createClass(parent, mod, name, source, ClassType.INTERFACE);
/*     */   }
/*     */   
/*     */   public JDefinedClass createClass(JClassContainer parent, String name, Locator source, ClassType kind) {
/*  88 */     return createClass(parent, 1, name, source, kind);
/*     */   }
/*     */ 
/*     */   
/*     */   public JDefinedClass createClass(JClassContainer parent, int mod, String name, Locator source, ClassType kind) {
/*  93 */     if (!JJavaName.isJavaIdentifier(name)) {
/*     */       
/*  95 */       this.errorReceiver.error(new SAXParseException(Messages.format("ERR_INVALID_CLASSNAME", new Object[] { name }), source));
/*     */       
/*  97 */       return createDummyClass(parent);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 102 */       if (parent.isClass() && kind == ClassType.CLASS) {
/* 103 */         mod |= 0x10;
/*     */       }
/* 105 */       JDefinedClass r = parent._class(mod, name, kind);
/*     */ 
/*     */       
/* 108 */       r.metadata = source;
/*     */       
/* 110 */       return r;
/* 111 */     } catch (JClassAlreadyExistsException e) {
/*     */       
/* 113 */       JDefinedClass cls = e.getExistingClass();
/*     */ 
/*     */       
/* 116 */       this.errorReceiver.error(new SAXParseException(Messages.format("CodeModelClassFactory.ClassNameCollision", new Object[] { cls.fullName() }), (Locator)cls.metadata));
/*     */ 
/*     */       
/* 119 */       this.errorReceiver.error(new SAXParseException(Messages.format("CodeModelClassFactory.ClassNameCollision.Source", new Object[] { name }), source));
/*     */ 
/*     */ 
/*     */       
/* 123 */       if (!name.equals(cls.name()))
/*     */       {
/* 125 */         this.errorReceiver.error(new SAXParseException(Messages.format("CodeModelClassFactory.CaseSensitivityCollision", new Object[] { name, cls.name() }), null));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 130 */       if (Util.equals((Locator)cls.metadata, source)) {
/* 131 */         this.errorReceiver.error(new SAXParseException(Messages.format("ERR_CHAMELEON_SCHEMA_GONE_WILD", new Object[0]), source));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 136 */       return createDummyClass(parent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JDefinedClass createDummyClass(JClassContainer parent) {
/*     */     try {
/* 148 */       return parent._class("$$$garbage$$$" + this.ticketMaster++);
/* 149 */     } catch (JClassAlreadyExistsException ee) {
/* 150 */       return ee.getExistingClass();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\CodeModelClassFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */