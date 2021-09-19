/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JDocComment;
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.util.JavadocEscapeWriter;
/*     */ import com.sun.msv.datatype.xsd.DatatypeFactory;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.tools.xjc.grammar.xducer.BuiltinDatatypeTransducerFactory;
/*     */ import com.sun.tools.xjc.grammar.xducer.EnumerationXducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.Messages;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIEnum;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIEnumMember;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.impl.util.SchemaWriter;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.relaxng.datatype.DatatypeException;
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
/*     */ final class ConversionFinder
/*     */ {
/*  83 */   private static final HashMap emptyHashMap = new HashMap();
/*     */ 
/*     */   
/*     */   private final BGMBuilder builder;
/*     */   
/*  88 */   private final Map builtinConversions = new Hashtable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final XSSimpleTypeFunction functor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Set builtinTypeSafeEnumCapableTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transducer find(XSSimpleType type) {
/* 127 */     return (Transducer)type.apply(this.functor);
/*     */   }
/*     */   
/*     */   ConversionFinder(BGMBuilder _builder) {
/* 131 */     this.functor = (XSSimpleTypeFunction)new Object(this);
/*     */     this.builder = _builder;
/*     */     String[] names = { 
/*     */         "anySimpleType", "ID", "IDREF", "boolean", "base64Binary", "hexBinary", "float", "decimal", "integer", "long", 
/*     */         "unsignedInt", "int", "unsignedShort", "short", "unsignedByte", "byte", "double", "QName", "token", "normalizedString", 
/*     */         "date", "dateTime", "time" };
/*     */     try {
/*     */       for (int i = 0; i < names.length; i++) {
/*     */         this.builtinConversions.put(names[i], BuiltinDatatypeTransducerFactory.getWithoutWhitespaceNormalization(this.builder.grammar, DatatypeFactory.getTypeByName(names[i])));
/*     */       }
/*     */     } catch (DatatypeException e) {
/*     */       e.printStackTrace();
/*     */       throw new JAXBAssertionError();
/*     */     } 
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
/*     */   private boolean shouldBeMappedToTypeSafeEnumByDefault(XSRestrictionSimpleType type) {
/*     */     XSSimpleType xSSimpleType;
/* 163 */     if (type.isLocal()) return false;
/*     */     
/* 165 */     if (!canBeMappedToTypeSafeEnum((XSSimpleType)type))
/*     */     {
/* 167 */       return false;
/*     */     }
/* 169 */     if (type.getDeclaredFacet("enumeration") == null)
/*     */     {
/*     */       
/* 172 */       return false;
/*     */     }
/*     */     
/* 175 */     XSRestrictionSimpleType xSRestrictionSimpleType = type;
/*     */     do {
/* 177 */       if (xSRestrictionSimpleType.isGlobal() && this.builder.getGlobalBinding().canBeMappedToTypeSafeEnum((XSDeclaration)xSRestrictionSimpleType)) {
/* 178 */         return true;
/*     */       }
/* 180 */       xSSimpleType = xSRestrictionSimpleType.getSimpleBaseType();
/* 181 */     } while (xSSimpleType != null);
/*     */     
/* 183 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 190 */     Set s = new HashSet();
/*     */ 
/*     */     
/* 193 */     String[] typeNames = { "string", "boolean", "float", "decimal", "double", "anyURI" };
/*     */ 
/*     */ 
/*     */     
/* 197 */     for (int i = 0; i < typeNames.length; i++) {
/* 198 */       s.add(typeNames[i]);
/*     */     }
/* 200 */     builtinTypeSafeEnumCapableTypes = Collections.unmodifiableSet(s);
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
/*     */   private boolean canBeMappedToTypeSafeEnum(XSSimpleType type) {
/*     */     do {
/* 215 */       if ("http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace())) {
/*     */         
/* 217 */         String localName = type.getName();
/* 218 */         if (localName != null) {
/* 219 */           if (localName.equals("anySimpleType"))
/* 220 */             return false; 
/* 221 */           if (localName.equals("ID") || localName.equals("IDREF")) {
/* 222 */             return false;
/*     */           }
/*     */           
/* 225 */           if (builtinTypeSafeEnumCapableTypes.contains(localName)) {
/* 226 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 230 */       type = type.getSimpleBaseType();
/* 231 */     } while (type != null);
/*     */     
/* 233 */     return false;
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
/*     */   private Transducer bindToTypeSafeEnum(XSRestrictionSimpleType type, String className, String javadoc, HashMap members, Locator loc) {
/* 260 */     if (loc == null) {
/* 261 */       loc = type.getLocator();
/*     */     }
/* 263 */     if (className == null) {
/*     */ 
/*     */       
/* 266 */       if (!type.isGlobal()) {
/* 267 */         this.builder.errorReporter.error(loc, "ConversionFinder.NoEnumNameAvailable");
/*     */         
/* 269 */         return (Transducer)new IdentityTransducer(this.builder.grammar.codeModel);
/*     */       } 
/* 271 */       className = type.getName();
/*     */     } 
/*     */     
/* 274 */     className = this.builder.getNameConverter().toClassName(className);
/*     */ 
/*     */     
/* 277 */     JDefinedClass clazz = this.builder.selector.codeModelClassFactory.createClass((JClassContainer)this.builder.selector.getPackage(type.getTargetNamespace()), className, type.getLocator());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     StringWriter out = new StringWriter();
/* 283 */     SchemaWriter sw = new SchemaWriter((Writer)new JavadocEscapeWriter(out));
/* 284 */     type.visit((XSVisitor)sw);
/*     */     
/* 286 */     JDocComment jdoc = clazz.javadoc();
/* 287 */     jdoc.appendComment((javadoc != null) ? (javadoc + "\n\n") : "");
/* 288 */     jdoc.appendComment(Messages.format("ClassSelector.JavadocHeading", type.getName()));
/*     */     
/* 290 */     jdoc.appendComment("\n<p>\n<pre>\n");
/* 291 */     jdoc.appendComment(out.getBuffer().toString());
/* 292 */     jdoc.appendComment("</pre>");
/*     */ 
/*     */ 
/*     */     
/* 296 */     boolean needsToGenerateMemberName = checkIfMemberNamesNeedToBeGenerated(type, members);
/*     */ 
/*     */     
/* 299 */     HashMap memberMap = new HashMap();
/* 300 */     int idx = 1;
/*     */     
/* 302 */     Expression exp = Expression.nullSet;
/*     */ 
/*     */     
/* 305 */     XSDatatype baseDt = this.builder.simpleTypeBuilder.datatypeBuilder.build(type.getSimpleBaseType());
/*     */ 
/*     */     
/* 308 */     Iterator itr = type.iterateDeclaredFacets();
/* 309 */     while (itr.hasNext()) {
/* 310 */       XSFacet facet = itr.next();
/* 311 */       if (!facet.getName().equals("enumeration")) {
/*     */         continue;
/*     */       }
/* 314 */       Expression vexp = this.builder.pool.createValue(baseDt, baseDt.createValue(facet.getValue(), facet.getContext()));
/*     */ 
/*     */       
/* 317 */       if (needsToGenerateMemberName) {
/*     */ 
/*     */         
/* 320 */         memberMap.put(vexp, new EnumerationXducer.MemberInfo("value" + idx++, null));
/*     */       } else {
/*     */         
/* 323 */         BIEnumMember mem = (BIEnumMember)members.get(facet.getValue());
/* 324 */         if (mem == null)
/*     */         {
/* 326 */           mem = (BIEnumMember)this.builder.getBindInfo((XSComponent)facet).get(BIEnumMember.NAME);
/*     */         }
/* 328 */         if (mem != null) {
/* 329 */           memberMap.put(vexp, mem.createMemberInfo());
/*     */         }
/*     */       } 
/* 332 */       exp = this.builder.pool.createChoice(exp, vexp);
/*     */     } 
/*     */ 
/*     */     
/* 336 */     if (memberMap.isEmpty()) {
/* 337 */       memberMap = emptyHashMap;
/*     */     }
/*     */     
/* 340 */     BIConversion conv = new BIConversion(type.getLocator(), (Transducer)new EnumerationXducer(NameConverter.standard, clazz, exp, memberMap, loc));
/*     */     
/* 342 */     conv.markAsAcknowledged();
/*     */ 
/*     */ 
/*     */     
/* 346 */     this.builder.getOrCreateBindInfo((XSComponent)type).addDecl((BIDeclaration)conv);
/*     */     
/* 348 */     return conv.getTransducer();
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
/*     */   private boolean checkIfMemberNamesNeedToBeGenerated(XSRestrictionSimpleType type, HashMap members) {
/* 360 */     Iterator itr = type.iterateDeclaredFacets();
/* 361 */     while (itr.hasNext()) {
/* 362 */       XSFacet facet = itr.next();
/* 363 */       if (!facet.getName().equals("enumeration")) {
/*     */         continue;
/*     */       }
/* 366 */       String value = facet.getValue();
/*     */       
/* 368 */       if (members.containsKey(value)) {
/*     */         continue;
/*     */       }
/* 371 */       if (!JJavaName.isJavaIdentifier(this.builder.getNameConverter().toConstantName(facet.getValue())))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 377 */         return this.builder.getGlobalBinding().needsToGenerateEnumMemberName();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 383 */     return false;
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
/*     */   private Transducer lookup(XSSimpleType type) {
/* 396 */     BindInfo info = this.builder.getBindInfo((XSComponent)type);
/* 397 */     BIConversion conv = (BIConversion)info.get(BIConversion.NAME);
/*     */     
/* 399 */     if (conv != null) {
/* 400 */       conv.markAsAcknowledged();
/* 401 */       return conv.getTransducer();
/*     */     } 
/*     */ 
/*     */     
/* 405 */     BIEnum en = (BIEnum)info.get(BIEnum.NAME);
/* 406 */     if (en != null) {
/* 407 */       en.markAsAcknowledged();
/*     */ 
/*     */ 
/*     */       
/* 411 */       if (!canBeMappedToTypeSafeEnum(type)) {
/* 412 */         this.builder.errorReporter.error(en.getLocation(), "ConversionFinder.CannotBeTypeSafeEnum");
/*     */         
/* 414 */         this.builder.errorReporter.error(type.getLocator(), "ConversionFinder.CannotBeTypeSafeEnum.Location");
/*     */ 
/*     */         
/* 417 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 421 */       return bindToTypeSafeEnum((XSRestrictionSimpleType)type, en.getClassName(), en.getJavadoc(), en.getMembers(), en.getLocation());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 427 */     if (type.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/* 428 */       String name = type.getName();
/* 429 */       if (name != null) {
/* 430 */         return lookupBuiltin(name);
/*     */       }
/*     */     } 
/* 433 */     return null;
/*     */   }
/*     */   
/*     */   private Transducer lookupBuiltin(String typeName) {
/* 437 */     return (Transducer)this.builtinConversions.get(typeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ConversionFinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */