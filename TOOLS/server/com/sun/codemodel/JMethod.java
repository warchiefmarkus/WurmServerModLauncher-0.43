/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.util.ClassNameComparator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMethod
/*     */   extends JGenerifiableImpl
/*     */   implements JDeclaration, JAnnotatable
/*     */ {
/*     */   private JMods mods;
/*  44 */   private JType type = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private String name = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private final List<JVar> params = new ArrayList<JVar>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<JClass> _throws;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private JBlock body = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private JDefinedClass outer;
/*     */ 
/*     */   
/*  72 */   private JDocComment jdoc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private JVar varParam = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */   
/*     */   private boolean isConstructor() {
/*  87 */     return (this.type == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private JExpression defaultValue = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JMethod(JDefinedClass outer, int mods, JType type, String name) {
/* 109 */     this.mods = JMods.forMethod(mods);
/* 110 */     this.type = type;
/* 111 */     this.name = name;
/* 112 */     this.outer = outer;
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
/*     */   JMethod(int mods, JDefinedClass _class) {
/* 125 */     this.mods = JMods.forMethod(mods);
/* 126 */     this.type = null;
/* 127 */     this.name = _class.name();
/* 128 */     this.outer = _class;
/*     */   }
/*     */   
/*     */   private Set<JClass> getThrows() {
/* 132 */     if (this._throws == null)
/* 133 */       this._throws = new TreeSet<JClass>(ClassNameComparator.theInstance); 
/* 134 */     return this._throws;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod _throws(JClass exception) {
/* 145 */     getThrows().add(exception);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public JMethod _throws(Class exception) {
/* 150 */     return _throws(this.outer.owner().ref(exception));
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
/*     */   public JVar param(int mods, JType type, String name) {
/* 166 */     JVar v = new JVar(JMods.forVar(mods), type, name, null);
/* 167 */     this.params.add(v);
/* 168 */     return v;
/*     */   }
/*     */   
/*     */   public JVar param(JType type, String name) {
/* 172 */     return param(0, type, name);
/*     */   }
/*     */   
/*     */   public JVar param(int mods, Class type, String name) {
/* 176 */     return param(mods, this.outer.owner()._ref(type), name);
/*     */   }
/*     */   
/*     */   public JVar param(Class type, String name) {
/* 180 */     return param(this.outer.owner()._ref(type), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar varParam(Class type, String name) {
/* 187 */     return varParam(this.outer.owner()._ref(type), name);
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
/*     */   public JVar varParam(JType type, String name) {
/* 208 */     if (!hasVarArgs()) {
/*     */       
/* 210 */       this.varParam = new JVar(JMods.forVar(0), type.array(), name, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 216 */       return this.varParam;
/*     */     } 
/* 218 */     throw new IllegalStateException("Cannot have two varargs in a method,\nCheck if varParam method of JMethod is invoked more than once");
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
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 233 */     if (this.annotations == null)
/* 234 */       this.annotations = new ArrayList<JAnnotationUse>(); 
/* 235 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 236 */     this.annotations.add(a);
/* 237 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 247 */     return annotate(owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 251 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasVarArgs() {
/* 259 */     return (this.varParam != null);
/*     */   }
/*     */   
/*     */   public String name() {
/* 263 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void name(String n) {
/* 270 */     this.name = n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType type() {
/* 277 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void type(JType t) {
/* 284 */     this.type = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType[] listParamTypes() {
/* 293 */     JType[] r = new JType[this.params.size()];
/* 294 */     for (int i = 0; i < r.length; i++)
/* 295 */       r[i] = ((JVar)this.params.get(i)).type(); 
/* 296 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType listVarParamType() {
/* 305 */     if (this.varParam != null) {
/* 306 */       return this.varParam.type();
/*     */     }
/* 308 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar[] listParams() {
/* 317 */     return this.params.<JVar>toArray(new JVar[this.params.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar listVarParam() {
/* 326 */     return this.varParam;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSignature(JType[] argTypes) {
/* 333 */     JVar[] p = listParams();
/* 334 */     if (p.length != argTypes.length) {
/* 335 */       return false;
/*     */     }
/* 337 */     for (int i = 0; i < p.length; i++) {
/* 338 */       if (!p[i].type().equals(argTypes[i]))
/* 339 */         return false; 
/*     */     } 
/* 341 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock body() {
/* 350 */     if (this.body == null)
/* 351 */       this.body = new JBlock(); 
/* 352 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void declareDefaultValue(JExpression value) {
/* 362 */     this.defaultValue = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 372 */     if (this.jdoc == null)
/* 373 */       this.jdoc = new JDocComment(owner()); 
/* 374 */     return this.jdoc;
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 378 */     if (this.jdoc != null) {
/* 379 */       f.g(this.jdoc);
/*     */     }
/* 381 */     if (this.annotations != null) {
/* 382 */       for (JAnnotationUse a : this.annotations) {
/* 383 */         f.g(a).nl();
/*     */       }
/*     */     }
/*     */     
/* 387 */     super.declare(f);
/*     */     
/* 389 */     f.g(this.mods);
/* 390 */     if (!isConstructor())
/* 391 */       f.g(this.type); 
/* 392 */     f.id(this.name).p('(').i();
/*     */ 
/*     */     
/* 395 */     boolean first = true;
/* 396 */     for (JVar var : this.params) {
/* 397 */       if (!first)
/* 398 */         f.p(','); 
/* 399 */       if (var.isAnnotated())
/* 400 */         f.nl(); 
/* 401 */       f.b(var);
/* 402 */       first = false;
/*     */     } 
/* 404 */     if (hasVarArgs()) {
/* 405 */       if (!first)
/* 406 */         f.p(','); 
/* 407 */       f.g(this.varParam.type().elementType());
/* 408 */       f.p("... ");
/* 409 */       f.id(this.varParam.name());
/*     */     } 
/*     */     
/* 412 */     f.o().p(')');
/* 413 */     if (this._throws != null && !this._throws.isEmpty()) {
/* 414 */       f.nl().i().p("throws").g((Collection)this._throws).nl().o();
/*     */     }
/*     */     
/* 417 */     if (this.defaultValue != null) {
/* 418 */       f.p("default ");
/* 419 */       f.g(this.defaultValue);
/*     */     } 
/* 421 */     if (this.body != null) {
/* 422 */       f.s(this.body);
/* 423 */     } else if (!this.outer.isInterface() && !this.outer.isAnnotationTypeDeclaration() && !this.mods.isAbstract() && !this.mods.isNative()) {
/*     */ 
/*     */       
/* 426 */       f.s(new JBlock());
/*     */     } else {
/* 428 */       f.p(';').nl();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods mods() {
/* 438 */     return this.mods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods getMods() {
/* 445 */     return this.mods;
/*     */   }
/*     */   
/*     */   protected JCodeModel owner() {
/* 449 */     return this.outer.owner();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JMethod.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */