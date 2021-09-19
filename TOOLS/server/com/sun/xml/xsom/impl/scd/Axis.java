/*     */ package com.sun.xml.xsom.impl.scd;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttContainer;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Axis<T extends XSComponent>
/*     */ {
/*  54 */   public static final Axis<XSSchema> ROOT = new Axis<XSSchema>() {
/*     */       public Iterator<XSSchema> iterator(XSComponent contextNode) {
/*  56 */         return contextNode.getRoot().iterateSchema();
/*     */       }
/*     */       
/*     */       public Iterator<XSSchema> iterator(Iterator<? extends XSComponent> contextNodes) {
/*  60 */         if (!contextNodes.hasNext()) {
/*  61 */           return Iterators.empty();
/*     */         }
/*     */         
/*  64 */         return iterator(contextNodes.next());
/*     */       }
/*     */       
/*     */       public boolean isModelGroup() {
/*  68 */         return false;
/*     */       }
/*     */       
/*     */       public String toString() {
/*  72 */         return "root::";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final Axis<XSComponent> INTERMEDIATE_SKIP = new AbstractAxisImpl<XSComponent>() {
/*     */       public Iterator<XSComponent> elementDecl(XSElementDecl decl) {
/*  87 */         XSComplexType ct = decl.getType().asComplexType();
/*  88 */         if (ct == null) {
/*  89 */           return empty();
/*     */         }
/*     */         
/*  92 */         return new Iterators.Union<XSComponent>(singleton((XSComponent)ct), complexType(ct));
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<XSComponent> modelGroupDecl(XSModelGroupDecl decl) {
/*  97 */         return descendants(decl.getModelGroup());
/*     */       }
/*     */       
/*     */       public Iterator<XSComponent> particle(XSParticle particle) {
/* 101 */         return descendants(particle.getTerm().asModelGroup());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private Iterator<XSComponent> descendants(XSModelGroup mg) {
/* 110 */         List<XSComponent> r = new ArrayList<XSComponent>();
/* 111 */         visit(mg, r);
/* 112 */         return r.iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       private void visit(XSModelGroup mg, List<XSComponent> r) {
/* 117 */         r.add(mg);
/* 118 */         for (XSParticle p : mg) {
/* 119 */           XSModelGroup child = p.getTerm().asModelGroup();
/* 120 */           if (child != null)
/* 121 */             visit(child, r); 
/*     */         } 
/*     */       }
/*     */       
/*     */       public String toString() {
/* 126 */         return "(intermediateSkip)";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public static final Axis<XSComponent> DESCENDANTS = new Axis<XSComponent>() {
/*     */       public Iterator<XSComponent> iterator(XSComponent contextNode) {
/* 138 */         return (new Visitor()).iterator(contextNode);
/*     */       }
/*     */       public Iterator<XSComponent> iterator(Iterator<? extends XSComponent> contextNodes) {
/* 141 */         return (new Visitor()).iterator(contextNodes);
/*     */       }
/*     */       
/*     */       public boolean isModelGroup() {
/* 145 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       final class Visitor
/*     */         extends AbstractAxisImpl<XSComponent>
/*     */       {
/* 152 */         private final Set<XSComponent> visited = new HashSet<XSComponent>();
/*     */ 
/*     */         
/*     */         final class Recursion
/*     */           extends Iterators.Map<XSComponent, XSComponent>
/*     */         {
/*     */           public Recursion(Iterator<? extends XSComponent> core) {
/* 159 */             super(core);
/*     */           }
/*     */           
/*     */           protected Iterator<XSComponent> apply(XSComponent u) {
/* 163 */             return Axis.DESCENDANTS.iterator(u);
/*     */           } }
/*     */         
/*     */         public Iterator<XSComponent> schema(XSSchema schema) {
/* 167 */           if (this.visited.add(schema)) {
/* 168 */             return ret((XSComponent)schema, new Recursion(schema.iterateElementDecls()));
/*     */           }
/* 170 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> elementDecl(XSElementDecl decl) {
/* 174 */           if (this.visited.add(decl)) {
/* 175 */             return ret((XSComponent)decl, iterator((XSComponent)decl.getType()));
/*     */           }
/* 177 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> simpleType(XSSimpleType type) {
/* 181 */           if (this.visited.add(type)) {
/* 182 */             return ret((XSComponent)type, (Iterator)FACET.iterator((XSComponent)type));
/*     */           }
/* 184 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> complexType(XSComplexType type) {
/* 188 */           if (this.visited.add(type)) {
/* 189 */             return ret((XSComponent)type, iterator((XSComponent)type.getContentType()));
/*     */           }
/* 191 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> particle(XSParticle particle) {
/* 195 */           if (this.visited.add(particle)) {
/* 196 */             return ret((XSComponent)particle, iterator((XSComponent)particle.getTerm()));
/*     */           }
/* 198 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> modelGroupDecl(XSModelGroupDecl decl) {
/* 202 */           if (this.visited.add(decl)) {
/* 203 */             return ret((XSComponent)decl, iterator((XSComponent)decl.getModelGroup()));
/*     */           }
/* 205 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> modelGroup(XSModelGroup group) {
/* 209 */           if (this.visited.add(group)) {
/* 210 */             return ret((XSComponent)group, new Recursion(group.iterator()));
/*     */           }
/* 212 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> attGroupDecl(XSAttGroupDecl decl) {
/* 216 */           if (this.visited.add(decl)) {
/* 217 */             return ret((XSComponent)decl, new Recursion(decl.iterateAttributeUses()));
/*     */           }
/* 219 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> attributeUse(XSAttributeUse use) {
/* 223 */           if (this.visited.add(use)) {
/* 224 */             return ret((XSComponent)use, iterator((XSComponent)use.getDecl()));
/*     */           }
/* 226 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> attributeDecl(XSAttributeDecl decl) {
/* 230 */           if (this.visited.add(decl)) {
/* 231 */             return ret((XSComponent)decl, iterator((XSComponent)decl.getType()));
/*     */           }
/* 233 */           return empty();
/*     */         }
/*     */         
/*     */         private Iterator<XSComponent> ret(XSComponent one, Iterator<? extends XSComponent> rest) {
/* 237 */           return union(singleton(one), rest);
/*     */         }
/*     */       }
/*     */       
/*     */       public String toString() {
/* 242 */         return "/";
/*     */       }
/*     */     };
/*     */   
/* 246 */   public static final Axis<XSSchema> X_SCHEMA = new Axis<XSSchema>() {
/*     */       public Iterator<XSSchema> iterator(XSComponent contextNode) {
/* 248 */         return Iterators.singleton(contextNode.getOwnerSchema());
/*     */       }
/*     */       
/*     */       public Iterator<XSSchema> iterator(Iterator<? extends XSComponent> contextNodes) {
/* 252 */         return new Iterators.Adapter<XSSchema, XSComponent>(contextNodes) {
/*     */             protected XSSchema filter(XSComponent u) {
/* 254 */               return u.getOwnerSchema();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       public boolean isModelGroup() {
/* 260 */         return false;
/*     */       }
/*     */       
/*     */       public String toString() {
/* 264 */         return "x-schema::";
/*     */       }
/*     */     };
/*     */   
/* 268 */   public static final Axis<XSElementDecl> SUBSTITUTION_GROUP = new AbstractAxisImpl<XSElementDecl>() {
/*     */       public Iterator<XSElementDecl> elementDecl(XSElementDecl decl) {
/* 270 */         return singleton(decl.getSubstAffiliation());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 274 */         return "substitutionGroup::";
/*     */       }
/*     */     };
/*     */   
/* 278 */   public static final Axis<XSAttributeDecl> ATTRIBUTE = new AbstractAxisImpl<XSAttributeDecl>() {
/*     */       public Iterator<XSAttributeDecl> complexType(XSComplexType type) {
/* 280 */         return attributeHolder((XSAttContainer)type);
/*     */       }
/*     */       
/*     */       public Iterator<XSAttributeDecl> attGroupDecl(XSAttGroupDecl decl) {
/* 284 */         return attributeHolder((XSAttContainer)decl);
/*     */       }
/*     */ 
/*     */       
/*     */       private Iterator<XSAttributeDecl> attributeHolder(XSAttContainer atts) {
/* 289 */         return new Iterators.Adapter<XSAttributeDecl, XSAttributeUse>(atts.iterateAttributeUses()) {
/*     */             protected XSAttributeDecl filter(XSAttributeUse u) {
/* 291 */               return u.getDecl();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       public Iterator<XSAttributeDecl> schema(XSSchema schema) {
/* 297 */         return schema.iterateAttributeDecls();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 301 */         return "@";
/*     */       }
/*     */     };
/*     */   
/* 305 */   public static final Axis<XSElementDecl> ELEMENT = new AbstractAxisImpl<XSElementDecl>() {
/*     */       public Iterator<XSElementDecl> particle(XSParticle particle) {
/* 307 */         return singleton(particle.getTerm().asElementDecl());
/*     */       }
/*     */       
/*     */       public Iterator<XSElementDecl> schema(XSSchema schema) {
/* 311 */         return schema.iterateElementDecls();
/*     */       }
/*     */       
/*     */       public Iterator<XSElementDecl> modelGroupDecl(XSModelGroupDecl decl) {
/* 315 */         return modelGroup(decl.getModelGroup());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String getName() {
/* 328 */         return "";
/*     */       }
/*     */       
/*     */       public String toString() {
/* 332 */         return "element::";
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 337 */   public static final Axis<XSType> TYPE_DEFINITION = new AbstractAxisImpl<XSType>() {
/*     */       public Iterator<XSType> schema(XSSchema schema) {
/* 339 */         return schema.iterateTypes();
/*     */       }
/*     */       
/*     */       public Iterator<XSType> attributeDecl(XSAttributeDecl decl) {
/* 343 */         return singleton((XSType)decl.getType());
/*     */       }
/*     */       
/*     */       public Iterator<XSType> elementDecl(XSElementDecl decl) {
/* 347 */         return singleton(decl.getType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 351 */         return "~";
/*     */       }
/*     */     };
/*     */   
/* 355 */   public static final Axis<XSType> BASETYPE = new AbstractAxisImpl<XSType>() {
/*     */       public Iterator<XSType> simpleType(XSSimpleType type) {
/* 357 */         return singleton(type.getBaseType());
/*     */       }
/*     */       
/*     */       public Iterator<XSType> complexType(XSComplexType type) {
/* 361 */         return singleton(type.getBaseType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 365 */         return "baseType::";
/*     */       }
/*     */     };
/*     */   
/* 369 */   public static final Axis<XSSimpleType> PRIMITIVE_TYPE = new AbstractAxisImpl<XSSimpleType>() {
/*     */       public Iterator<XSSimpleType> simpleType(XSSimpleType type) {
/* 371 */         return singleton(type.getPrimitiveType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 375 */         return "primitiveType::";
/*     */       }
/*     */     };
/*     */   
/* 379 */   public static final Axis<XSSimpleType> ITEM_TYPE = new AbstractAxisImpl<XSSimpleType>() {
/*     */       public Iterator<XSSimpleType> simpleType(XSSimpleType type) {
/* 381 */         XSListSimpleType baseList = type.getBaseListType();
/* 382 */         if (baseList == null) return empty(); 
/* 383 */         return singleton(baseList.getItemType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 387 */         return "itemType::";
/*     */       }
/*     */     };
/*     */   
/* 391 */   public static final Axis<XSSimpleType> MEMBER_TYPE = new AbstractAxisImpl<XSSimpleType>() {
/*     */       public Iterator<XSSimpleType> simpleType(XSSimpleType type) {
/* 393 */         XSUnionSimpleType baseUnion = type.getBaseUnionType();
/* 394 */         if (baseUnion == null) return empty(); 
/* 395 */         return baseUnion.iterator();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 399 */         return "memberType::";
/*     */       }
/*     */     };
/*     */   
/* 403 */   public static final Axis<XSComponent> SCOPE = new AbstractAxisImpl<XSComponent>() {
/*     */       public Iterator<XSComponent> complexType(XSComplexType type) {
/* 405 */         return singleton((XSComponent)type.getScope());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public String toString() {
/* 411 */         return "scope::";
/*     */       }
/*     */     };
/*     */   
/* 415 */   public static final Axis<XSAttGroupDecl> ATTRIBUTE_GROUP = new AbstractAxisImpl<XSAttGroupDecl>() {
/*     */       public Iterator<XSAttGroupDecl> schema(XSSchema schema) {
/* 417 */         return schema.iterateAttGroupDecls();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 421 */         return "attributeGroup::";
/*     */       }
/*     */     };
/*     */   
/* 425 */   public static final Axis<XSModelGroupDecl> MODEL_GROUP_DECL = new AbstractAxisImpl<XSModelGroupDecl>() {
/*     */       public Iterator<XSModelGroupDecl> schema(XSSchema schema) {
/* 427 */         return schema.iterateModelGroupDecls();
/*     */       }
/*     */       
/*     */       public Iterator<XSModelGroupDecl> particle(XSParticle particle) {
/* 431 */         return singleton(particle.getTerm().asModelGroupDecl());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 435 */         return "group::";
/*     */       }
/*     */     };
/*     */   
/* 439 */   public static final Axis<XSIdentityConstraint> IDENTITY_CONSTRAINT = new AbstractAxisImpl<XSIdentityConstraint>() {
/*     */       public Iterator<XSIdentityConstraint> elementDecl(XSElementDecl decl) {
/* 441 */         return decl.getIdentityConstraints().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<XSIdentityConstraint> schema(XSSchema schema) {
/* 446 */         return super.schema(schema);
/*     */       }
/*     */       
/*     */       public String toString() {
/* 450 */         return "identityConstraint::";
/*     */       }
/*     */     };
/*     */   
/* 454 */   public static final Axis<XSIdentityConstraint> REFERENCED_KEY = new AbstractAxisImpl<XSIdentityConstraint>() {
/*     */       public Iterator<XSIdentityConstraint> identityConstraint(XSIdentityConstraint decl) {
/* 456 */         return singleton(decl.getReferencedKey());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 460 */         return "key::";
/*     */       }
/*     */     };
/*     */   
/* 464 */   public static final Axis<XSNotation> NOTATION = new AbstractAxisImpl<XSNotation>() {
/*     */       public Iterator<XSNotation> schema(XSSchema schema) {
/* 466 */         return schema.iterateNotations();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 470 */         return "notation::";
/*     */       }
/*     */     };
/*     */   
/* 474 */   public static final Axis<XSWildcard> WILDCARD = new AbstractAxisImpl<XSWildcard>() {
/*     */       public Iterator<XSWildcard> particle(XSParticle particle) {
/* 476 */         return singleton(particle.getTerm().asWildcard());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 480 */         return "any::";
/*     */       }
/*     */     };
/*     */   
/* 484 */   public static final Axis<XSWildcard> ATTRIBUTE_WILDCARD = new AbstractAxisImpl<XSWildcard>() {
/*     */       public Iterator<XSWildcard> complexType(XSComplexType type) {
/* 486 */         return singleton(type.getAttributeWildcard());
/*     */       }
/*     */       
/*     */       public Iterator<XSWildcard> attGroupDecl(XSAttGroupDecl decl) {
/* 490 */         return singleton(decl.getAttributeWildcard());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 494 */         return "anyAttribute::";
/*     */       }
/*     */     };
/*     */   
/* 498 */   public static final Axis<XSFacet> FACET = new AbstractAxisImpl<XSFacet>()
/*     */     {
/*     */       public Iterator<XSFacet> simpleType(XSSimpleType type) {
/* 501 */         XSRestrictionSimpleType r = type.asRestriction();
/* 502 */         if (r != null) {
/* 503 */           return r.iterateDeclaredFacets();
/*     */         }
/* 505 */         return empty();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 509 */         return "facet::";
/*     */       }
/*     */     };
/*     */   
/* 513 */   public static final Axis<XSModelGroup> MODELGROUP_ALL = new ModelGroupAxis(XSModelGroup.Compositor.ALL);
/* 514 */   public static final Axis<XSModelGroup> MODELGROUP_CHOICE = new ModelGroupAxis(XSModelGroup.Compositor.CHOICE);
/* 515 */   public static final Axis<XSModelGroup> MODELGROUP_SEQUENCE = new ModelGroupAxis(XSModelGroup.Compositor.SEQUENCE); Iterator<T> iterator(XSComponent paramXSComponent); Iterator<T> iterator(Iterator<? extends XSComponent> paramIterator);
/* 516 */   public static final Axis<XSModelGroup> MODELGROUP_ANY = new ModelGroupAxis(null);
/*     */   
/*     */   boolean isModelGroup();
/*     */   
/*     */   public static final class ModelGroupAxis extends AbstractAxisImpl<XSModelGroup> {
/*     */     ModelGroupAxis(XSModelGroup.Compositor compositor) {
/* 522 */       this.compositor = compositor;
/*     */     }
/*     */     private final XSModelGroup.Compositor compositor;
/*     */     
/*     */     public boolean isModelGroup() {
/* 527 */       return true;
/*     */     }
/*     */     
/*     */     public Iterator<XSModelGroup> particle(XSParticle particle) {
/* 531 */       return filter(particle.getTerm().asModelGroup());
/*     */     }
/*     */     
/*     */     public Iterator<XSModelGroup> modelGroupDecl(XSModelGroupDecl decl) {
/* 535 */       return filter(decl.getModelGroup());
/*     */     }
/*     */     
/*     */     private Iterator<XSModelGroup> filter(XSModelGroup mg) {
/* 539 */       if (mg == null)
/* 540 */         return empty(); 
/* 541 */       if (mg.getCompositor() == this.compositor || this.compositor == null) {
/* 542 */         return singleton(mg);
/*     */       }
/* 544 */       return empty();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 548 */       if (this.compositor == null) {
/* 549 */         return "model::*";
/*     */       }
/* 551 */       return "model::" + this.compositor;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\scd\Axis.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */