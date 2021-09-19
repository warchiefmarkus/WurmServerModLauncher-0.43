/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.util.JavadocEscapeWriter;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.CClassRef;
/*     */ import com.sun.tools.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.xjc.model.CNonElement;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIEnum;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIEnumMember;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.EnumMemberMode;
/*     */ import com.sun.tools.xjc.util.MimeTypeRange;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSVariety;
/*     */ import com.sun.xml.xsom.impl.util.SchemaWriter;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.math.BigInteger;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.activation.MimeTypeParseException;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpleTypeBuilder
/*     */   extends BindingComponent
/*     */ {
/* 121 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */   
/* 123 */   private final Model model = (Model)Ring.get(Model.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public final Stack<XSComponent> refererStack = new Stack<XSComponent>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   private final Set<XSComponent> acknowledgedXmimeContentTypes = new HashSet<XSComponent>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XSSimpleType initiatingType;
/*     */ 
/*     */ 
/*     */   
/* 150 */   public static final Map<String, TypeUse> builtinConversions = new HashMap<String, TypeUse>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeUse build(XSSimpleType type) {
/* 161 */     XSSimpleType oldi = this.initiatingType;
/* 162 */     this.initiatingType = type;
/*     */     
/* 164 */     TypeUse e = checkRefererCustomization(type);
/* 165 */     if (e == null) {
/* 166 */       e = compose(type);
/*     */     }
/* 168 */     this.initiatingType = oldi;
/*     */     
/* 170 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeUse buildDef(XSSimpleType type) {
/* 179 */     XSSimpleType oldi = this.initiatingType;
/* 180 */     this.initiatingType = type;
/*     */     
/* 182 */     TypeUse e = (TypeUse)type.apply(this.composer);
/*     */     
/* 184 */     this.initiatingType = oldi;
/*     */     
/* 186 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BIConversion getRefererCustomization() {
/* 195 */     BindInfo info = this.builder.getBindInfo(getReferer());
/* 196 */     BIProperty prop = (BIProperty)info.get(BIProperty.class);
/* 197 */     if (prop == null) return null; 
/* 198 */     return prop.getConv();
/*     */   }
/*     */   
/*     */   public XSComponent getReferer() {
/* 202 */     return this.refererStack.peek();
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
/*     */   private TypeUse checkRefererCustomization(XSSimpleType type) {
/* 218 */     XSComponent top = getReferer();
/*     */     
/* 220 */     if (top instanceof XSElementDecl) {
/*     */       
/* 222 */       XSElementDecl eref = (XSElementDecl)top;
/* 223 */       assert eref.getType() == type;
/*     */ 
/*     */ 
/*     */       
/* 227 */       BindInfo info = this.builder.getBindInfo(top);
/* 228 */       BIConversion bIConversion = (BIConversion)info.get(BIConversion.class);
/* 229 */       if (bIConversion != null) {
/* 230 */         bIConversion.markAsAcknowledged();
/*     */         
/* 232 */         return bIConversion.getTypeUse(type);
/*     */       } 
/* 234 */       detectJavaTypeCustomization();
/*     */     }
/* 236 */     else if (top instanceof XSAttributeDecl) {
/* 237 */       XSAttributeDecl aref = (XSAttributeDecl)top;
/* 238 */       assert aref.getType() == type;
/* 239 */       detectJavaTypeCustomization();
/*     */     }
/* 241 */     else if (top instanceof XSComplexType) {
/* 242 */       XSComplexType tref = (XSComplexType)top;
/* 243 */       assert tref.getBaseType() == type || tref.getContentType() == type;
/* 244 */       detectJavaTypeCustomization();
/*     */     }
/* 246 */     else if (top != type) {
/*     */       assert false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     BIConversion conv = getRefererCustomization();
/* 256 */     if (conv != null) {
/* 257 */       conv.markAsAcknowledged();
/*     */       
/* 259 */       return conv.getTypeUse(type);
/*     */     } 
/*     */     
/* 262 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void detectJavaTypeCustomization() {
/* 273 */     BindInfo info = this.builder.getBindInfo(getReferer());
/* 274 */     BIConversion conv = (BIConversion)info.get(BIConversion.class);
/*     */     
/* 276 */     if (conv != null) {
/*     */       
/* 278 */       conv.markAsAcknowledged();
/*     */ 
/*     */       
/* 281 */       getErrorReporter().error(conv.getLocation(), "SimpleTypeBuilder.UnnestedJavaTypeCustomization", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TypeUse compose(XSSimpleType t) {
/* 290 */     TypeUse e = find(t);
/* 291 */     if (e != null) return e; 
/* 292 */     return (TypeUse)t.apply(this.composer);
/*     */   }
/*     */   
/* 295 */   public final XSSimpleTypeFunction<TypeUse> composer = new XSSimpleTypeFunction<TypeUse>()
/*     */     {
/*     */ 
/*     */       
/*     */       public TypeUse listSimpleType(XSListSimpleType type)
/*     */       {
/* 301 */         XSSimpleType itemType = type.getItemType();
/* 302 */         SimpleTypeBuilder.this.refererStack.push(itemType);
/* 303 */         TypeUse tu = TypeUseFactory.makeCollection(SimpleTypeBuilder.this.build(type.getItemType()));
/* 304 */         SimpleTypeBuilder.this.refererStack.pop();
/* 305 */         return tu;
/*     */       }
/*     */       public TypeUse unionSimpleType(XSUnionSimpleType type) {
/*     */         TypeUse typeUse;
/* 309 */         boolean isCollection = false;
/* 310 */         for (int i = 0; i < type.getMemberSize(); i++) {
/* 311 */           if (type.getMember(i).getVariety() == XSVariety.LIST) {
/* 312 */             isCollection = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 316 */         CBuiltinLeafInfo cBuiltinLeafInfo = CBuiltinLeafInfo.STRING;
/* 317 */         if (isCollection)
/* 318 */           typeUse = TypeUseFactory.makeCollection((TypeUse)cBuiltinLeafInfo); 
/* 319 */         return typeUse;
/*     */       }
/*     */ 
/*     */       
/*     */       public TypeUse restrictionSimpleType(XSRestrictionSimpleType type) {
/* 324 */         return SimpleTypeBuilder.this.compose(type.getSimpleBaseType());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Set<String> builtinTypeSafeEnumCapableTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeUse find(XSSimpleType type) {
/* 338 */     boolean noAutoEnum = false;
/*     */ 
/*     */     
/* 341 */     BindInfo info = this.builder.getBindInfo((XSComponent)type);
/* 342 */     BIConversion conv = (BIConversion)info.get(BIConversion.class);
/*     */     
/* 344 */     if (conv != null) {
/*     */       
/* 346 */       conv.markAsAcknowledged();
/* 347 */       return conv.getTypeUse(type);
/*     */     } 
/*     */ 
/*     */     
/* 351 */     BIEnum en = (BIEnum)info.get(BIEnum.class);
/* 352 */     if (en != null) {
/* 353 */       en.markAsAcknowledged();
/*     */       
/* 355 */       if (!en.isMapped()) {
/* 356 */         noAutoEnum = true;
/*     */       }
/*     */       else {
/*     */         
/* 360 */         if (!canBeMappedToTypeSafeEnum(type)) {
/* 361 */           getErrorReporter().error(en.getLocation(), "ConversionFinder.CannotBeTypeSafeEnum", new Object[0]);
/*     */           
/* 363 */           getErrorReporter().error(type.getLocator(), "ConversionFinder.CannotBeTypeSafeEnum.Location", new Object[0]);
/*     */ 
/*     */           
/* 366 */           return null;
/*     */         } 
/*     */ 
/*     */         
/* 370 */         if (en.ref != null) {
/* 371 */           if (!JJavaName.isFullyQualifiedClassName(en.ref)) {
/* 372 */             ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(en.getLocation(), Messages.format("ClassSelector.IncorrectClassName", new Object[] { en.ref }));
/*     */ 
/*     */             
/* 375 */             return null;
/*     */           } 
/*     */           
/* 378 */           return (TypeUse)new CClassRef(this.model, (XSComponent)type, en, info.toCustomizationList());
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 383 */         return bindToTypeSafeEnum((XSRestrictionSimpleType)type, en.className, en.javadoc, en.members, getEnumMemberMode().getModeWithEnum(), en.getLocation());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 392 */     if (type.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/* 393 */       String name = type.getName();
/* 394 */       if (name != null) {
/* 395 */         TypeUse r = lookupBuiltin(name);
/* 396 */         if (r != null) {
/* 397 */           return r;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 402 */     if (type.getTargetNamespace().equals("http://ws-i.org/profiles/basic/1.1/xsd")) {
/* 403 */       String name = type.getName();
/* 404 */       if (name != null && name.equals("swaRef")) {
/* 405 */         return CBuiltinLeafInfo.STRING.makeAdapted(SwaRefAdapter.class, false);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 411 */     if (type.isRestriction() && !noAutoEnum) {
/* 412 */       XSRestrictionSimpleType rst = type.asRestriction();
/* 413 */       if (shouldBeMappedToTypeSafeEnumByDefault(rst)) {
/* 414 */         TypeUse r = bindToTypeSafeEnum(rst, (String)null, (String)null, Collections.emptyMap(), getEnumMemberMode(), (Locator)null);
/*     */         
/* 416 */         if (r != null) {
/* 417 */           return r;
/*     */         }
/*     */       } 
/*     */     } 
/* 421 */     return (TypeUse)getClassSelector()._bindToClass((XSComponent)type, null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldBeMappedToTypeSafeEnumByDefault(XSRestrictionSimpleType type) {
/* 431 */     if (type.isLocal()) return false;
/*     */ 
/*     */     
/* 434 */     if (type.getRedefinedBy() != null) return false;
/*     */     
/* 436 */     List<XSFacet> facets = type.getDeclaredFacets("enumeration");
/* 437 */     if (facets.isEmpty() || facets.size() > this.builder.getGlobalBinding().getDefaultEnumMemberSizeCap())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 442 */       return false;
/*     */     }
/* 444 */     if (!canBeMappedToTypeSafeEnum((XSSimpleType)type))
/*     */     {
/* 446 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 452 */     for (XSRestrictionSimpleType xSRestrictionSimpleType = type; xSRestrictionSimpleType != null; xSSimpleType = xSRestrictionSimpleType.getSimpleBaseType()) {
/* 453 */       XSSimpleType xSSimpleType; if (xSRestrictionSimpleType.isGlobal() && this.builder.getGlobalBinding().canBeMappedToTypeSafeEnum((XSDeclaration)xSRestrictionSimpleType))
/* 454 */         return true; 
/*     */     } 
/* 456 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 463 */     Set<String> s = new HashSet<String>();
/*     */ 
/*     */     
/* 466 */     String[] typeNames = { "string", "boolean", "float", "decimal", "double", "anyURI" };
/*     */ 
/*     */ 
/*     */     
/* 470 */     for (String type : typeNames) {
/* 471 */       s.add(type);
/*     */     }
/* 473 */     builtinTypeSafeEnumCapableTypes = Collections.unmodifiableSet(s);
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
/*     */   public static boolean canBeMappedToTypeSafeEnum(XSSimpleType type) {
/*     */     do {
/* 488 */       if ("http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace())) {
/*     */         
/* 490 */         String localName = type.getName();
/* 491 */         if (localName != null) {
/* 492 */           if (localName.equals("anySimpleType"))
/* 493 */             return false; 
/* 494 */           if (localName.equals("ID") || localName.equals("IDREF")) {
/* 495 */             return false;
/*     */           }
/*     */           
/* 498 */           if (builtinTypeSafeEnumCapableTypes.contains(localName)) {
/* 499 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 503 */       type = type.getSimpleBaseType();
/* 504 */     } while (type != null);
/*     */     
/* 506 */     return false;
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
/*     */   private TypeUse bindToTypeSafeEnum(XSRestrictionSimpleType type, String className, String javadoc, Map<String, BIEnumMember> members, EnumMemberMode mode, Locator loc) {
/*     */     CClassInfoParent scope;
/* 534 */     if (loc == null) {
/* 535 */       loc = type.getLocator();
/*     */     }
/* 537 */     if (className == null) {
/*     */ 
/*     */       
/* 540 */       if (!type.isGlobal()) {
/* 541 */         getErrorReporter().error(loc, "ConversionFinder.NoEnumNameAvailable", new Object[0]);
/*     */         
/* 543 */         return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */       } 
/* 545 */       className = type.getName();
/*     */     } 
/*     */ 
/*     */     
/* 549 */     className = this.builder.deriveName(className, (XSComponent)type);
/*     */ 
/*     */     
/* 552 */     StringWriter out = new StringWriter();
/* 553 */     SchemaWriter sw = new SchemaWriter((Writer)new JavadocEscapeWriter(out));
/* 554 */     type.visit((XSVisitor)sw);
/*     */     
/* 556 */     if (javadoc != null) { javadoc = javadoc + "\n\n"; }
/* 557 */     else { javadoc = ""; }
/*     */     
/* 559 */     javadoc = javadoc + Messages.format("ClassSelector.JavadocHeading", new Object[] { type.getName() }) + "\n<p>\n<pre>\n" + out.getBuffer() + "</pre>";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 565 */     this.refererStack.push(type.getSimpleBaseType());
/* 566 */     TypeUse use = build(type.getSimpleBaseType());
/* 567 */     this.refererStack.pop();
/*     */     
/* 569 */     if (use.isCollection()) {
/* 570 */       return null;
/*     */     }
/* 572 */     CNonElement baseDt = use.getInfo();
/*     */     
/* 574 */     if (baseDt instanceof com.sun.tools.xjc.model.CClassInfo) {
/* 575 */       return null;
/*     */     }
/*     */     
/* 578 */     XSFacet[] errorRef = new XSFacet[1];
/* 579 */     List<CEnumConstant> memberList = buildCEnumConstants(type, false, members, errorRef);
/* 580 */     if (memberList == null || checkMemberNameCollision(memberList) != null) {
/* 581 */       switch (mode) {
/*     */         
/*     */         case SKIP:
/* 584 */           return null;
/*     */         
/*     */         case ERROR:
/* 587 */           if (memberList == null) {
/* 588 */             getErrorReporter().error(errorRef[0].getLocator(), "ERR_CANNOT_GENERATE_ENUM_NAME", new Object[] { errorRef[0].getValue() });
/*     */           }
/*     */           else {
/*     */             
/* 592 */             CEnumConstant[] collision = checkMemberNameCollision(memberList);
/* 593 */             getErrorReporter().error(collision[0].getLocator(), "ERR_ENUM_MEMBER_NAME_COLLISION", new Object[] { collision[0].getName() });
/*     */ 
/*     */             
/* 596 */             getErrorReporter().error(collision[1].getLocator(), "ERR_ENUM_MEMBER_NAME_COLLISION_RELATED", new Object[0]);
/*     */           } 
/*     */           
/* 599 */           return null;
/*     */         
/*     */         case GENERATE:
/* 602 */           memberList = buildCEnumConstants(type, true, members, (XSFacet[])null);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 609 */     if (type.isGlobal()) {
/* 610 */       CClassInfoParent.Package package_ = new CClassInfoParent.Package(getClassSelector().getPackage(type.getTargetNamespace()));
/*     */     } else {
/* 612 */       scope = getClassSelector().getClassScope();
/* 613 */     }  CEnumLeafInfo xducer = new CEnumLeafInfo(this.model, BGMBuilder.getName((XSDeclaration)type), scope, className, baseDt, memberList, (XSComponent)type, this.builder.getBindInfo((XSComponent)type).toCustomizationList(), loc);
/*     */ 
/*     */     
/* 616 */     xducer.javadoc = javadoc;
/*     */     
/* 618 */     BIConversion.Static static_ = new BIConversion.Static(type.getLocator(), (TypeUse)xducer);
/* 619 */     static_.markAsAcknowledged();
/*     */ 
/*     */ 
/*     */     
/* 623 */     this.builder.getOrCreateBindInfo((XSComponent)type).addDecl((BIDeclaration)static_);
/*     */     
/* 625 */     return static_.getTypeUse((XSSimpleType)type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<CEnumConstant> buildCEnumConstants(XSRestrictionSimpleType type, boolean needsToGenerateMemberName, Map<String, BIEnumMember> members, XSFacet[] errorRef) {
/* 636 */     List<CEnumConstant> memberList = new ArrayList<CEnumConstant>();
/* 637 */     int idx = 1;
/* 638 */     Set<String> enums = new HashSet<String>();
/*     */     
/* 640 */     for (XSFacet facet : type.getDeclaredFacets("enumeration")) {
/* 641 */       String name = null;
/* 642 */       String mdoc = this.builder.getBindInfo((XSComponent)facet).getDocumentation();
/*     */       
/* 644 */       if (!enums.add((facet.getValue()).value)) {
/*     */         continue;
/*     */       }
/* 647 */       if (needsToGenerateMemberName) {
/*     */ 
/*     */         
/* 650 */         name = "VALUE_" + idx++;
/*     */       } else {
/* 652 */         String facetValue = (facet.getValue()).value;
/* 653 */         BIEnumMember mem = members.get(facetValue);
/* 654 */         if (mem == null)
/*     */         {
/* 656 */           mem = (BIEnumMember)this.builder.getBindInfo((XSComponent)facet).get(BIEnumMember.class);
/*     */         }
/* 658 */         if (mem != null) {
/* 659 */           name = mem.name;
/* 660 */           mdoc = mem.javadoc;
/*     */         } 
/*     */         
/* 663 */         if (name == null) {
/* 664 */           StringBuilder sb = new StringBuilder();
/* 665 */           for (int i = 0; i < facetValue.length(); i++) {
/* 666 */             char ch = facetValue.charAt(i);
/* 667 */             if (Character.isJavaIdentifierPart(ch)) {
/* 668 */               sb.append(ch);
/*     */             } else {
/* 670 */               sb.append('_');
/*     */             } 
/* 672 */           }  name = this.model.getNameConverter().toConstantName(sb.toString());
/*     */         } 
/*     */       } 
/*     */       
/* 676 */       if (!JJavaName.isJavaIdentifier(name)) {
/* 677 */         if (errorRef != null) errorRef[0] = facet; 
/* 678 */         return null;
/*     */       } 
/*     */       
/* 681 */       memberList.add(new CEnumConstant(name, mdoc, (facet.getValue()).value, facet.getLocator()));
/*     */     } 
/* 683 */     return memberList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CEnumConstant[] checkMemberNameCollision(List<CEnumConstant> memberList) {
/* 694 */     Map<String, CEnumConstant> names = new HashMap<String, CEnumConstant>();
/* 695 */     for (CEnumConstant c : memberList) {
/* 696 */       CEnumConstant old = names.put(c.getName(), c);
/* 697 */       if (old != null)
/*     */       {
/* 699 */         return new CEnumConstant[] { old, c }; } 
/*     */     } 
/* 701 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumMemberMode getEnumMemberMode() {
/* 707 */     return this.builder.getGlobalBinding().getEnumMemberMode();
/*     */   }
/*     */   
/*     */   private TypeUse lookupBuiltin(String typeLocalName) {
/* 711 */     if (typeLocalName.equals("integer") || typeLocalName.equals("long")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 725 */       BigInteger xe = readFacet("maxExclusive", -1);
/* 726 */       BigInteger xi = readFacet("maxInclusive", 0);
/* 727 */       BigInteger max = min(xe, xi);
/*     */       
/* 729 */       if (max != null) {
/* 730 */         BigInteger ne = readFacet("minExclusive", 1);
/* 731 */         BigInteger ni = readFacet("minInclusive", 0);
/* 732 */         BigInteger min = max(ne, ni);
/*     */         
/* 734 */         if (min != null)
/* 735 */           if (min.compareTo(INT_MIN) >= 0 && max.compareTo(INT_MAX) <= 0) {
/* 736 */             typeLocalName = "int";
/*     */           }
/* 738 */           else if (min.compareTo(LONG_MIN) >= 0 && max.compareTo(LONG_MAX) <= 0) {
/* 739 */             typeLocalName = "long";
/*     */           }  
/*     */       } 
/*     */     } else {
/* 743 */       if (typeLocalName.equals("boolean") && isRestrictedTo0And1())
/*     */       {
/* 745 */         return CBuiltinLeafInfo.BOOLEAN_ZERO_OR_ONE;
/*     */       }
/* 747 */       if (typeLocalName.equals("base64Binary")) {
/* 748 */         return lookupBinaryTypeBinding();
/*     */       }
/* 750 */       if (typeLocalName.equals("anySimpleType")) {
/* 751 */         if (getReferer() instanceof XSAttributeDecl || getReferer() instanceof XSSimpleType) {
/* 752 */           return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */         }
/* 754 */         return (TypeUse)CBuiltinLeafInfo.ANYTYPE;
/*     */       } 
/* 756 */     }  return builtinConversions.get(typeLocalName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeUse lookupBinaryTypeBinding() {
/* 765 */     XSComponent referer = getReferer();
/* 766 */     String emt = referer.getForeignAttribute("http://www.w3.org/2005/05/xmlmime", "expectedContentTypes");
/* 767 */     if (emt != null) {
/* 768 */       this.acknowledgedXmimeContentTypes.add(referer);
/*     */       
/*     */       try {
/* 771 */         List<MimeTypeRange> types = MimeTypeRange.parseRanges(emt);
/* 772 */         MimeTypeRange mt = MimeTypeRange.merge(types);
/*     */ 
/*     */         
/* 775 */         if (mt.majorType.equals("image")) {
/* 776 */           return CBuiltinLeafInfo.IMAGE.makeMimeTyped(mt.toMimeType());
/*     */         }
/* 778 */         if ((mt.majorType.equals("application") || mt.majorType.equals("text")) && isXml(mt.subType))
/*     */         {
/* 780 */           return CBuiltinLeafInfo.XML_SOURCE.makeMimeTyped(mt.toMimeType());
/*     */         }
/* 782 */         if (mt.majorType.equals("text") && mt.subType.equals("plain")) {
/* 783 */           return CBuiltinLeafInfo.STRING.makeMimeTyped(mt.toMimeType());
/*     */         }
/*     */         
/* 786 */         return CBuiltinLeafInfo.DATA_HANDLER.makeMimeTyped(mt.toMimeType());
/* 787 */       } catch (ParseException e) {
/* 788 */         getErrorReporter().error(referer.getLocator(), Messages.format("ERR_ILLEGAL_EXPECTED_MIME_TYPE", new Object[] { emt, e.getMessage() }), new Object[0]);
/*     */       
/*     */       }
/* 791 */       catch (MimeTypeParseException e) {
/* 792 */         getErrorReporter().error(referer.getLocator(), Messages.format("ERR_ILLEGAL_EXPECTED_MIME_TYPE", new Object[] { emt, e.getMessage() }), new Object[0]);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 797 */     return (TypeUse)CBuiltinLeafInfo.BASE64_BYTE_ARRAY;
/*     */   }
/*     */   
/*     */   public boolean isAcknowledgedXmimeContentTypes(XSComponent c) {
/* 801 */     return this.acknowledgedXmimeContentTypes.contains(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isXml(String subType) {
/* 808 */     return (subType.equals("xml") || subType.endsWith("+xml"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRestrictedTo0And1() {
/* 817 */     XSFacet pattern = this.initiatingType.getFacet("pattern");
/* 818 */     if (pattern != null) {
/* 819 */       String v = (pattern.getValue()).value;
/* 820 */       if (v.equals("0|1") || v.equals("1|0") || v.equals("\\d"))
/* 821 */         return true; 
/*     */     } 
/* 823 */     XSFacet enumf = this.initiatingType.getFacet("enumeration");
/* 824 */     if (enumf != null) {
/* 825 */       String v = (enumf.getValue()).value;
/* 826 */       if (v.equals("0") || v.equals("1"))
/* 827 */         return true; 
/*     */     } 
/* 829 */     return false;
/*     */   }
/*     */   
/*     */   private BigInteger readFacet(String facetName, int offset) {
/* 833 */     XSFacet me = this.initiatingType.getFacet(facetName);
/* 834 */     if (me == null)
/* 835 */       return null; 
/* 836 */     BigInteger bi = DatatypeConverterImpl._parseInteger((me.getValue()).value);
/* 837 */     if (offset != 0)
/* 838 */       bi = bi.add(BigInteger.valueOf(offset)); 
/* 839 */     return bi;
/*     */   }
/*     */   
/*     */   private BigInteger min(BigInteger a, BigInteger b) {
/* 843 */     if (a == null) return b; 
/* 844 */     if (b == null) return a; 
/* 845 */     return a.min(b);
/*     */   }
/*     */   
/*     */   private BigInteger max(BigInteger a, BigInteger b) {
/* 849 */     if (a == null) return b; 
/* 850 */     if (b == null) return a; 
/* 851 */     return a.max(b);
/*     */   }
/*     */   
/* 854 */   private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
/* 855 */   private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
/* 856 */   private static final BigInteger INT_MIN = BigInteger.valueOf(-2147483648L);
/* 857 */   private static final BigInteger INT_MAX = BigInteger.valueOf(2147483647L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 864 */     Map<String, TypeUse> m = builtinConversions;
/*     */ 
/*     */     
/* 867 */     m.put("string", CBuiltinLeafInfo.STRING);
/* 868 */     m.put("anyURI", CBuiltinLeafInfo.STRING);
/* 869 */     m.put("boolean", CBuiltinLeafInfo.BOOLEAN);
/*     */ 
/*     */     
/* 872 */     m.put("hexBinary", CBuiltinLeafInfo.HEXBIN_BYTE_ARRAY);
/* 873 */     m.put("float", CBuiltinLeafInfo.FLOAT);
/* 874 */     m.put("decimal", CBuiltinLeafInfo.BIG_DECIMAL);
/* 875 */     m.put("integer", CBuiltinLeafInfo.BIG_INTEGER);
/* 876 */     m.put("long", CBuiltinLeafInfo.LONG);
/* 877 */     m.put("unsignedInt", CBuiltinLeafInfo.LONG);
/* 878 */     m.put("int", CBuiltinLeafInfo.INT);
/* 879 */     m.put("unsignedShort", CBuiltinLeafInfo.INT);
/* 880 */     m.put("short", CBuiltinLeafInfo.SHORT);
/* 881 */     m.put("unsignedByte", CBuiltinLeafInfo.SHORT);
/* 882 */     m.put("byte", CBuiltinLeafInfo.BYTE);
/* 883 */     m.put("double", CBuiltinLeafInfo.DOUBLE);
/* 884 */     m.put("QName", CBuiltinLeafInfo.QNAME);
/* 885 */     m.put("NOTATION", CBuiltinLeafInfo.QNAME);
/* 886 */     m.put("dateTime", CBuiltinLeafInfo.CALENDAR);
/* 887 */     m.put("date", CBuiltinLeafInfo.CALENDAR);
/* 888 */     m.put("time", CBuiltinLeafInfo.CALENDAR);
/* 889 */     m.put("gYearMonth", CBuiltinLeafInfo.CALENDAR);
/* 890 */     m.put("gYear", CBuiltinLeafInfo.CALENDAR);
/* 891 */     m.put("gMonthDay", CBuiltinLeafInfo.CALENDAR);
/* 892 */     m.put("gDay", CBuiltinLeafInfo.CALENDAR);
/* 893 */     m.put("gMonth", CBuiltinLeafInfo.CALENDAR);
/* 894 */     m.put("duration", CBuiltinLeafInfo.DURATION);
/* 895 */     m.put("token", CBuiltinLeafInfo.TOKEN);
/* 896 */     m.put("normalizedString", CBuiltinLeafInfo.NORMALIZED_STRING);
/* 897 */     m.put("ID", CBuiltinLeafInfo.ID);
/* 898 */     m.put("IDREF", CBuiltinLeafInfo.IDREF);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\SimpleTypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */