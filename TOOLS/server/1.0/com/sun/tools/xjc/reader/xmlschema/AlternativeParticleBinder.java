/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.JClassFactory;
/*     */ import com.sun.tools.xjc.reader.xmlschema.NameGenerator;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ParticleBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.PrefixedJClassFactoryImpl;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import java.text.ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AlternativeParticleBinder
/*     */   extends ParticleBinder
/*     */   implements XSTermFunction, BGMBuilder.ParticleHandler
/*     */ {
/*     */   private XSParticle parent;
/*     */   
/*     */   AlternativeParticleBinder(BGMBuilder builder) {
/*  41 */     super(builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression build(XSParticle p, ClassItem superClass) {
/*  46 */     return (Expression)particle(p);
/*     */   }
/*     */   
/*     */   public boolean checkFallback(XSParticle p, ClassItem superClass) {
/*  50 */     return false;
/*     */   }
/*     */   
/*     */   public Object particle(XSParticle p) {
/*     */     Expression exp;
/*  55 */     this.builder.selector.bindToType((XSComponent)p);
/*     */     
/*  57 */     XSParticle oldParent = this.parent;
/*     */     
/*  59 */     this.parent = p;
/*     */ 
/*     */     
/*  62 */     XSTerm t = p.getTerm();
/*     */     
/*  64 */     if (needSkip(t)) {
/*  65 */       ElementPattern elementPattern1; XSElementDecl e = t.asElementDecl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  76 */       this.builder.particlesWithGlobalElementSkip.add(p);
/*     */       
/*  78 */       ElementPattern eexp = this.builder.typeBuilder.elementDeclFlat(e);
/*     */       
/*  80 */       if (needSkippableElement(e)) {
/*     */         
/*  82 */         exp = this.pool.createChoice((Expression)eexp, (Expression)this.builder.selector.bindToType(e));
/*     */       } else {
/*  84 */         elementPattern1 = eexp;
/*     */       } 
/*     */       
/*  87 */       Expression expression = this.pool.createChoice(this.builder.getSubstitionGroupList(e), (Expression)elementPattern1);
/*     */       
/*  89 */       FieldItem fieldItem = this.builder.fieldBuilder.createFieldItem(computeLabel(p), false, expression, (XSComponent)p);
/*     */     } else {
/*  91 */       exp = (Expression)t.apply(this);
/*     */     } 
/*  93 */     this.parent = oldParent;
/*  94 */     return this.builder.processMinMax(exp, p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 103 */     TypeItem typeItem = this.builder.selector.bindToType(decl);
/*     */     
/* 105 */     if (typeItem != null)
/*     */     {
/* 107 */       return this.builder.fieldBuilder.createFieldItem((Expression)typeItem, (XSDeclaration)decl, false);
/*     */     }
/* 109 */     return this.builder.fieldBuilder.elementDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object modelGroup(XSModelGroup group) {
/* 115 */     Expression typeExp = this.builder.selector.bindToType((XSComponent)group);
/*     */     
/* 117 */     if (typeExp == null) {
/* 118 */       if (group.getCompositor() == XSModelGroup.CHOICE || getLocalPropCustomization(this.parent) != null)
/*     */       {
/*     */         
/* 121 */         return this.builder.fieldBuilder.createFieldItem(this.builder.typeBuilder.build((XSComponent)group), group);
/*     */       }
/*     */ 
/*     */       
/* 125 */       if (this.parent.getMaxOccurs() != 1) {
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */           
/* 131 */           JDefinedClass cls = this.builder.selector.getClassFactory().create(this.builder.getNameConverter().toClassName(NameGenerator.getName(this.builder, group)), group.getLocator());
/*     */ 
/*     */ 
/*     */           
/* 135 */           ClassItem ci = this.builder.grammar.createClassItem(cls, Expression.epsilon, group.getLocator());
/*     */ 
/*     */           
/* 138 */           this.builder.selector.queueBuild((XSComponent)group, ci);
/* 139 */           ClassItem classItem1 = ci;
/* 140 */         } catch (ParseException e) {
/*     */           
/* 142 */           this.builder.errorReporter.error(group.getLocator(), "DefaultParticleBinder.UnableToGenerateNameFromModelGroup");
/*     */ 
/*     */ 
/*     */           
/* 146 */           typeExp = null;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 151 */     if (typeExp != null)
/*     */     {
/* 153 */       return this.builder.fieldBuilder.createFieldItem(typeExp, group);
/*     */     }
/*     */     
/* 156 */     return this.builder.applyRecursively(group, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 161 */     Expression typeExp = this.builder.selector.bindToType((XSComponent)decl);
/*     */     
/* 163 */     if (typeExp != null)
/*     */     {
/* 165 */       return this.builder.fieldBuilder.createFieldItem(typeExp, (XSDeclaration)decl, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     this.builder.selector.pushClassFactory((JClassFactory)new PrefixedJClassFactoryImpl(this.builder, decl));
/*     */ 
/*     */     
/* 174 */     Object r = modelGroup(decl.getModelGroup());
/*     */     
/* 176 */     this.builder.selector.popClassFactory();
/*     */     
/* 178 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 184 */     Expression typeExp = this.builder.selector.bindToType((XSComponent)wc);
/*     */     
/* 186 */     if (typeExp == null)
/*     */     {
/* 188 */       typeExp = this.builder.typeBuilder.build((XSComponent)wc);
/*     */     }
/*     */ 
/*     */     
/* 192 */     return this.builder.fieldBuilder.createFieldItem("any", false, typeExp, (XSComponent)wc);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\AlternativeParticleBinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */