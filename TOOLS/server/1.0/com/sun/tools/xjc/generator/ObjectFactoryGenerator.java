/*     */ package 1.0.com.sun.tools.xjc.generator;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JForLoop;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.codemodel.fmt.JPropertyFile;
/*     */ import com.sun.codemodel.fmt.JSerializedObject;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.msv.writer.relaxng.RELAXNGWriter;
/*     */ import com.sun.org.apache.xml.internal.serialize.OutputFormat;
/*     */ import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.generator.AGMBuilder;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.Constructor;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.runtime.DefaultJAXBContextImpl;
/*     */ import com.sun.tools.xjc.runtime.GrammarInfo;
/*     */ import com.sun.tools.xjc.runtime.GrammarInfoImpl;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import com.sun.xml.bind.ContextFactory_1_0_1;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.PropertyException;
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
/*     */ final class ObjectFactoryGenerator
/*     */ {
/*  62 */   private static final PrintStream debug = (Util.getSystemProperty(com.sun.tools.xjc.generator.ObjectFactoryGenerator.class, "debug") != null) ? System.out : null;
/*     */ 
/*     */   
/*     */   private final GeneratorContext context;
/*     */ 
/*     */   
/*     */   private final AnnotatedGrammar grammar;
/*     */ 
/*     */   
/*     */   private final JCodeModel codeModel;
/*     */ 
/*     */   
/*     */   private final Options opt;
/*     */ 
/*     */   
/*     */   private final JPackage targetPackage;
/*     */   
/*     */   private final JVar $grammarInfo;
/*     */   
/*     */   private final JVar $rootTagMap;
/*     */   
/*     */   private final DefaultImplementationMapGenerator defImplMapGenerator;
/*     */   
/*     */   private final JDefinedClass objectFactory;
/*     */ 
/*     */   
/*     */   public JVar getGrammarInfo() {
/*  89 */     return this.$grammarInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass getObjectFactory() {
/*  96 */     return this.objectFactory;
/*     */   }
/*     */   
/*     */   public JVar getRootTagMap() {
/* 100 */     return this.$rootTagMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ObjectFactoryGenerator(GeneratorContext _context, AnnotatedGrammar _grammar, Options _opt, JPackage _pkg) {
/* 110 */     this.context = _context;
/* 111 */     this.grammar = _grammar;
/* 112 */     this.opt = _opt;
/* 113 */     this.codeModel = this.grammar.codeModel;
/* 114 */     this.targetPackage = _pkg;
/*     */ 
/*     */     
/* 117 */     this.objectFactory = this.context.getClassFactory().createClass((JClassContainer)this.targetPackage, "ObjectFactory", null);
/*     */ 
/*     */     
/* 120 */     this.defImplMapGenerator = new DefaultImplementationMapGenerator(this, Util.calculateInitialHashMapCapacity(countClassItems(), 0.75F));
/*     */ 
/*     */ 
/*     */     
/* 124 */     this.$rootTagMap = (JVar)this.objectFactory.field(20, HashMap.class, "rootTagMap", (JExpression)JExpr._new(this.objectFactory.owner().ref(HashMap.class)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     this.objectFactory._extends(this.context.getRuntime(DefaultJAXBContextImpl.class));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     JPropertyFile jaxbProperties = new JPropertyFile("jaxb.properties");
/* 137 */     this.targetPackage.addResourceFile((JResourceFile)jaxbProperties);
/* 138 */     jaxbProperties.add("javax.xml.bind.context.factory", ContextFactory_1_0_1.class.getName());
/*     */ 
/*     */     
/* 141 */     jaxbProperties.add("com.sun.xml.bind.jaxbContextImpl", this.context.getRuntime(DefaultJAXBContextImpl.class).fullName());
/*     */ 
/*     */ 
/*     */     
/* 145 */     if (this.opt.debugMode)
/*     */     {
/*     */       
/* 148 */       if (!this.targetPackage.isUnnamed()) {
/*     */         try {
/* 150 */           this.codeModel._package("")._class("ObjectFactory")._extends((JClass)this.objectFactory);
/*     */         }
/* 152 */         catch (JClassAlreadyExistsException e) {}
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.$grammarInfo = (JVar)this.objectFactory.field(25, (JType)this.context.getRuntime(GrammarInfo.class), "grammarInfo", (JExpression)JExpr._new(this.context.getRuntime(GrammarInfoImpl.class)).arg((JExpression)this.$rootTagMap).arg((JExpression)this.defImplMapGenerator.$map).arg(this.objectFactory.dotclass()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     JMethod m1 = this.objectFactory.constructor(1);
/* 175 */     m1.body().invoke("super").arg((JExpression)this.$grammarInfo);
/* 176 */     m1.javadoc().setComment("Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: " + this.targetPackage.name());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     JMethod m2 = this.objectFactory.method(1, (JType)this.codeModel.ref(Object.class), "newInstance")._throws(JAXBException.class);
/*     */ 
/*     */ 
/*     */     
/* 195 */     m2.param(Class.class, "javaContentInterface");
/* 196 */     m2.body()._return((JExpression)JExpr.invoke(JExpr._super(), "newInstance").arg((JExpression)JExpr.ref("javaContentInterface")));
/*     */ 
/*     */     
/* 199 */     m2.javadoc().setComment("Create an instance of the specified Java content interface.").addParam("javaContentInterface", "the Class object of the javacontent interface to instantiate").addReturn("a new instance").addThrows("JAXBException", "if an error occurs");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     JMethod m3 = this.objectFactory.method(1, (JType)this.codeModel.ref(Object.class), "getProperty")._throws(PropertyException.class);
/*     */ 
/*     */ 
/*     */     
/* 229 */     JVar $name = m3.param(String.class, "name");
/* 230 */     m3.body()._return((JExpression)JExpr._super().invoke("getProperty").arg((JExpression)$name));
/* 231 */     m3.javadoc().setComment("Get the specified property. This method can only be\nused to get provider specific properties.\nAttempting to get an undefined property will result\nin a PropertyException being thrown.").addParam("name", "the name of the property to retrieve").addReturn("the value of the requested property").addThrows("PropertyException", "when there is an error retrieving the given property or value");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     JMethod m4 = this.objectFactory.method(1, (JType)this.codeModel.VOID, "setProperty")._throws(PropertyException.class);
/*     */ 
/*     */ 
/*     */     
/* 247 */     $name = m4.param(String.class, "name");
/* 248 */     JVar $value = m4.param(Object.class, "value");
/* 249 */     m4.body().invoke(JExpr._super(), "setProperty").arg((JExpression)$name).arg((JExpression)$value);
/*     */     
/* 251 */     m4.javadoc().setComment("Set the specified property. This method can only be\nused to set provider specific properties.\nAttempting to set an undefined property will result\nin a PropertyException being thrown.").addParam("name", "the name of the property to retrieve").addParam("value", "the value of the property to be set").addThrows("PropertyException", "when there is an error processing the given property or value");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 263 */     Grammar purifiedGrammar = AGMBuilder.remove(this.grammar);
/*     */     try {
/* 265 */       this.targetPackage.addResourceFile((JResourceFile)new JSerializedObject("bgm.ser", purifiedGrammar));
/*     */     }
/* 267 */     catch (IOException e) {
/*     */       
/* 269 */       throw new JAXBAssertionError(e);
/*     */     } 
/*     */     
/* 272 */     if (debug != null) {
/* 273 */       debug.println("---- schema ----");
/*     */       try {
/* 275 */         RELAXNGWriter w = new RELAXNGWriter();
/* 276 */         OutputFormat format = new OutputFormat("xml", null, true);
/*     */         
/* 278 */         format.setIndent(1);
/* 279 */         w.setDocumentHandler(new XMLSerializer(debug, format));
/* 280 */         w.write(purifiedGrammar);
/* 281 */       } catch (SAXException e) {
/* 282 */         throw new JAXBAssertionError(e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 288 */     this.objectFactory.javadoc().appendComment("This object contains factory methods for each \nJava content interface and Java element interface \ngenerated in the " + this.targetPackage.name() + " package. \n" + "<p>An ObjectFactory allows you to programatically \n" + "construct new instances of the Java representation \n" + "for XML content. The Java representation of XML \n" + "content can consist of schema derived interfaces \n" + "and classes representing the binding of schema \n" + "type definitions, element declarations and model \n" + "groups.  Factory methods for each of these are \n" + "provided in this class.");
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
/*     */   void populate(ClassContext cc) {
/* 320 */     JMethod m = this.objectFactory.method(1, (JType)cc.ref, "create" + getPartlyQualifiedName(cc.ref))._throws(JAXBException.class);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     m.body()._return((JExpression)JExpr._new(cc.implRef));
/*     */ 
/*     */     
/* 328 */     m.javadoc().appendComment("Create an instance of " + getPartlyQualifiedName(cc.ref)).addThrows("JAXBException", "if an error occurs");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 336 */     Iterator itr = cc.target.iterateConstructors();
/* 337 */     if (itr.hasNext())
/*     */     {
/*     */       
/* 340 */       cc.implClass.constructor(1);
/*     */     }
/*     */     
/* 343 */     while (itr.hasNext()) {
/* 344 */       Constructor cons = itr.next();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 351 */       JMethod jMethod1 = this.objectFactory.method(1, (JType)cc.ref, "create" + getPartlyQualifiedName(cc.ref));
/*     */       
/* 353 */       JInvocation inv = JExpr._new(cc.implRef);
/* 354 */       jMethod1.body()._return((JExpression)inv);
/*     */       
/* 356 */       jMethod1._throws(this.codeModel.ref(JAXBException.class));
/*     */ 
/*     */       
/* 359 */       jMethod1.javadoc().appendComment("Create an instance of " + getPartlyQualifiedName(cc.ref)).addThrows("JAXBException", "if an error occurs");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 368 */       JMethod c = cc.implClass.constructor(1);
/*     */       
/* 370 */       for (int i = 0; i < cons.fields.length; i++) {
/* 371 */         JVar $fvar; String fieldName = cons.fields[i];
/* 372 */         FieldUse field = cc.target.getField(fieldName);
/* 373 */         if (field == null)
/*     */         {
/* 375 */           throw new UnsupportedOperationException("illegal constructor param name: " + fieldName);
/*     */         }
/*     */         
/* 378 */         fieldName = camelize(fieldName);
/*     */         
/* 380 */         FieldRenderer renderer = this.context.getField(field);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 387 */         if (field.multiplicity.isAtMostOnce()) {
/*     */           
/* 389 */           $fvar = jMethod1.param(field.type, fieldName);
/* 390 */           JVar $var = c.param(field.type, fieldName);
/*     */           
/* 392 */           renderer.setter(c.body(), (JExpression)$var);
/*     */         } else {
/*     */           
/* 395 */           $fvar = jMethod1.param((JType)field.type.array(), fieldName);
/* 396 */           JVar $var = c.param((JType)field.type.array(), fieldName);
/*     */           
/* 398 */           JForLoop forLoop = c.body()._for();
/* 399 */           JVar $i = forLoop.init((JType)this.codeModel.INT, "___i", JExpr.lit(0));
/* 400 */           forLoop.test($i.lt((JExpression)$var.ref("length")));
/* 401 */           forLoop.update($i.incr());
/*     */           
/* 403 */           renderer.setter(forLoop.body(), (JExpression)$var.component((JExpression)$i));
/*     */         } 
/*     */         
/* 406 */         inv.arg((JExpression)$fvar);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 411 */     this.defImplMapGenerator.add(cc.ref, cc.implRef);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getPartlyQualifiedName(JDefinedClass cls) {
/* 417 */     if (cls.parentContainer() instanceof JPackage) {
/* 418 */       return cls.name();
/*     */     }
/* 420 */     return getPartlyQualifiedName((JDefinedClass)cls.parentContainer()) + cls.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String camelize(String s) {
/* 427 */     return Character.toLowerCase(s.charAt(0)) + s.substring(1);
/*     */   }
/*     */ 
/*     */   
/*     */   private int countClassItems() {
/* 432 */     ClassItem[] classItems = this.grammar.getClasses();
/* 433 */     int count = 0;
/*     */ 
/*     */ 
/*     */     
/* 437 */     for (int i = 0; i < classItems.length; i++) {
/* 438 */       if (classItems[i].getTypeAsDefined()._package() == this.targetPackage) {
/* 439 */         count++;
/*     */       }
/*     */     } 
/* 442 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\ObjectFactoryGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */