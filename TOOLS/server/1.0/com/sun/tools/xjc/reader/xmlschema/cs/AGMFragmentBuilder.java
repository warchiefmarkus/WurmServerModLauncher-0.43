/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*     */ 
/*     */ import com.sun.msv.datatype.xsd.BooleanType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.ext.WildcardItem;
/*     */ import com.sun.tools.xjc.reader.xmlschema.AbstractXSFunctionImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.xsom.XSAttContainer;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.util.Iterator;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AGMFragmentBuilder
/*     */   extends AbstractXSFunctionImpl
/*     */ {
/*     */   private final BGMBuilder builder;
/*     */   private final ExpressionPool pool;
/*     */   private XSComponent root;
/*     */   private ClassItem superClass;
/*     */   
/*     */   AGMFragmentBuilder(BGMBuilder builder) {
/*  58 */     this.builder = builder;
/*  59 */     this.pool = builder.pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression build(XSComponent sc, ClassItem owner) {
/*  69 */     this.superClass = findSuperClass(owner);
/*  70 */     this.root = sc;
/*  71 */     return (Expression)sc.apply((XSFunction)this);
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
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/*  83 */     return attributeContainer((XSAttContainer)decl);
/*     */   }
/*     */   
/*     */   private Expression attributeContainer(XSAttContainer cont) {
/*  87 */     Expression exp = Expression.epsilon;
/*     */     
/*  89 */     for (Iterator itr = cont.iterateAttributeUses(); itr.hasNext();) {
/*  90 */       exp = this.pool.createSequence(exp, recurse((XSComponent)itr.next()));
/*     */     }
/*     */     
/*  93 */     return exp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/*  99 */     return attribute(decl, decl.getFixedValue(), decl.getContext());
/*     */   }
/*     */   
/*     */   private Expression attribute(XSAttributeDecl decl, String fixedValue, ValidationContext context) {
/* 103 */     SimpleNameClass name = new SimpleNameClass(decl.getTargetNamespace(), decl.getName());
/* 104 */     XSDatatype xSDatatype = this.builder.simpleTypeBuilder.datatypeBuilder.build(decl.getType());
/*     */     
/* 106 */     if (fixedValue != null) {
/* 107 */       Object value = xSDatatype.createValue(fixedValue, context);
/* 108 */       return this.pool.createAttribute((NameClass)name, this.pool.createValue((Datatype)xSDatatype, null, value));
/*     */     } 
/* 110 */     return this.pool.createAttribute((NameClass)name, this.pool.createData((Datatype)xSDatatype, null));
/*     */   }
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/* 114 */     Expression e = attribute(use.getDecl(), use.getFixedValue(), use.getContext());
/* 115 */     if (use.isRequired()) return e; 
/* 116 */     return this.pool.createOptional(e);
/*     */   }
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/* 120 */     XSContentType content = type.getContentType();
/* 121 */     Expression body = recurse((XSComponent)content);
/*     */     
/* 123 */     if (type.isMixed())
/* 124 */       body = this.pool.createMixed(body); 
/* 125 */     body = this.pool.createSequence(body, attributeContainer((XSAttContainer)type));
/* 126 */     return body;
/*     */   }
/*     */   
/*     */   public Object empty(XSContentType empty) {
/* 130 */     return Expression.epsilon;
/*     */   }
/*     */   public Object particle(XSParticle particle) {
/*     */     Expression exp;
/* 134 */     XSTerm t = particle.getTerm();
/*     */ 
/*     */     
/* 137 */     if (this.builder.particlesWithGlobalElementSkip.contains(particle))
/* 138 */     { XSElementDecl e = t.asElementDecl();
/*     */       
/* 140 */       if (e.isAbstract()) {
/* 141 */         exp = Expression.nullSet;
/*     */       } else {
/* 143 */         ElementPattern ep = _elementDecl(e);
/* 144 */         if (e.getType().isComplexType()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 150 */           exp = this.pool.createChoice((Expression)this.builder.selector.bindToType(e), (Expression)ep);
/*     */         } else {
/* 152 */           ElementPattern elementPattern = ep;
/*     */         } 
/*     */       }  }
/* 155 */     else { exp = recurse((XSComponent)t); }
/*     */ 
/*     */     
/* 158 */     if (t.isElementDecl()) {
/* 159 */       exp = this.pool.createChoice(exp, this.builder.getSubstitionGroupList(t.asElementDecl()));
/*     */     }
/* 161 */     return this.builder.processMinMax(exp, particle);
/*     */   }
/*     */   
/*     */   public Object simpleType(XSSimpleType simpleType) {
/* 165 */     return this.pool.createData(this.builder.simpleTypeBuilder.datatypeBuilder.build(simpleType));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 170 */     if (decl.isAbstract()) {
/* 171 */       return Expression.nullSet;
/*     */     }
/* 173 */     return _elementDecl(decl);
/*     */   }
/*     */   private ElementPattern _elementDecl(XSElementDecl decl) {
/* 176 */     Expression body = recurse((XSComponent)decl.getType(), (this.root == decl));
/*     */     
/* 178 */     if (decl.getType() instanceof XSComplexType && this.builder.getGlobalBinding().isTypeSubstitutionSupportEnabled()) {
/*     */       
/* 180 */       if (decl.getType().asComplexType().isAbstract())
/* 181 */         body = Expression.nullSet; 
/* 182 */       body = this.pool.createChoice(body, this.builder.getTypeSubstitutionList((XSComplexType)decl.getType(), true));
/*     */     } else {
/*     */       
/* 185 */       body = this.pool.createSequence(body, this.builder.createXsiTypeExp(decl));
/*     */     } 
/* 187 */     if (decl.isNillable()) {
/* 188 */       body = this.pool.createChoice(this.pool.createAttribute((NameClass)new SimpleNameClass("http://www.w3.org/2001/XMLSchema-instance", "nil"), this.pool.createValue((XSDatatype)BooleanType.theInstance, Boolean.TRUE)), body);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 193 */     return new ElementPattern((NameClass)new SimpleNameClass(decl.getTargetNamespace(), decl.getName()), body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object modelGroup(XSModelGroup group) {
/*     */     Expression exp;
/* 200 */     XSModelGroup.Compositor comp = group.getCompositor();
/*     */     
/* 202 */     if (comp == XSModelGroup.CHOICE) { exp = Expression.nullSet; }
/* 203 */     else { exp = Expression.epsilon; }
/*     */     
/* 205 */     for (int i = 0; i < group.getSize(); i++) {
/* 206 */       Expression item = recurse((XSComponent)group.getChild(i));
/*     */       
/* 208 */       if (comp == XSModelGroup.CHOICE)
/* 209 */         exp = this.pool.createChoice(exp, item); 
/* 210 */       if (comp == XSModelGroup.SEQUENCE)
/* 211 */         exp = this.pool.createSequence(exp, item); 
/* 212 */       if (comp == XSModelGroup.ALL) {
/* 213 */         exp = this.pool.createInterleave(exp, item);
/*     */       }
/*     */     } 
/* 216 */     return exp;
/*     */   }
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 220 */     return recurse((XSComponent)decl.getModelGroup());
/*     */   }
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 224 */     return new WildcardItem(this.builder.grammar.codeModel, wc);
/*     */   }
/*     */   
/*     */   private Expression recurse(XSComponent sc) {
/* 228 */     return recurse(sc, true);
/*     */   }
/*     */   
/*     */   private Expression recurse(XSComponent sc, boolean superClassCheck) {
/* 232 */     Expression e = this.builder.selector.bindToType(sc);
/* 233 */     if (e != null) {
/* 234 */       if (this.superClass == e && superClassCheck)
/*     */       {
/* 236 */         return (Expression)this.superClass.agm;
/*     */       }
/* 238 */       return e;
/*     */     } 
/* 240 */     return (Expression)sc.apply((XSFunction)this);
/*     */   }
/*     */ 
/*     */   
/*     */   private ClassItem findSuperClass(ClassItem parent) {
/* 245 */     if (parent == null) return null;
/*     */     
/* 247 */     ClassItem[] result = new ClassItem[1];
/*     */     
/* 249 */     parent.exp.visit((ExpressionVisitorVoid)new Object(this, result));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     return result[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\AGMFragmentBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */