/*     */ package 1.0.com.sun.tools.xjc.reader.dtd;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.datatype.xsd.DatatypeFactory;
/*     */ import com.sun.msv.datatype.xsd.IDREFType;
/*     */ import com.sun.msv.datatype.xsd.IDType;
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.msv.grammar.SequenceExp;
/*     */ import com.sun.msv.reader.AbortException;
/*     */ import com.sun.msv.reader.GrammarReaderController;
/*     */ import com.sun.msv.reader.dtd.DTDReader;
/*     */ import com.sun.msv.scanner.dtd.DTDEventListener;
/*     */ import com.sun.msv.scanner.dtd.DTDParser;
/*     */ import com.sun.msv.scanner.dtd.InputEntity;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassCandidateItem;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.id.IDREFTransducer;
/*     */ import com.sun.tools.xjc.grammar.id.IDTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.reader.GrammarReaderControllerAdaptor;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.tools.xjc.reader.PackageTracker;
/*     */ import com.sun.tools.xjc.reader.annotator.Annotator;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.reader.annotator.FieldCollisionChecker;
/*     */ import com.sun.tools.xjc.reader.dtd.Messages;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIAttribute;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIContent;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIInterface;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.relaxng.datatype.DatatypeException;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TDTDReader
/*     */   extends DTDReader
/*     */   implements AnnotatorController, PackageTracker
/*     */ {
/*     */   private AnnotatedGrammar annGrammar;
/*     */   private final Options opts;
/*     */   private final BindInfo bindInfo;
/*     */   private final JCodeModel codeModel;
/*     */   private final CodeModelClassFactory classFactory;
/*     */   private final ErrorReceiver errorReceiver;
/*     */   
/*     */   public static AnnotatedGrammar parse(InputSource dtd, InputSource bindingInfo, ErrorReceiver errorReceiver, Options opts, ExpressionPool pool) {
/*     */     try {
/*  85 */       com.sun.tools.xjc.reader.dtd.TDTDReader reader = new com.sun.tools.xjc.reader.dtd.TDTDReader(new GrammarReaderControllerAdaptor(errorReceiver, opts.entityResolver), opts, pool, bindingInfo);
/*     */ 
/*     */ 
/*     */       
/*  89 */       DTDParser parser = new DTDParser();
/*  90 */       parser.setDtdHandler((DTDEventListener)reader);
/*  91 */       if (opts.entityResolver != null) {
/*  92 */         parser.setEntityResolver(opts.entityResolver);
/*     */       }
/*     */       try {
/*  95 */         parser.parse(dtd);
/*  96 */       } catch (SAXParseException e) {
/*  97 */         return null;
/*     */       } 
/*     */       
/* 100 */       return reader.getAnnotatedResult();
/* 101 */     } catch (IOException e) {
/* 102 */       errorReceiver.error(new SAXParseException(e.getMessage(), null, e));
/* 103 */       return null;
/* 104 */     } catch (SAXException e) {
/* 105 */       errorReceiver.error(new SAXParseException(e.getMessage(), null, e));
/* 106 */       return null;
/* 107 */     } catch (AbortException e) {
/*     */       
/* 109 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected TDTDReader(GrammarReaderControllerAdaptor _controller, Options opts, ExpressionPool pool, InputSource _bindInfo) throws AbortException {
/* 115 */     super((GrammarReaderController)_controller, pool);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     this.codeModel = new JCodeModel();
/*     */     this.opts = opts;
/*     */     this.bindInfo = new BindInfo(_bindInfo, (ErrorReceiver)_controller, this.codeModel, opts);
/*     */     this.errorReceiver = (ErrorReceiver)_controller;
/*     */     this.classFactory = new CodeModelClassFactory(this.errorReceiver);
/*     */   }
/*     */   
/*     */   public AnnotatedGrammar getAnnotatedResult() {
/* 147 */     if (this.controller.hadError()) {
/* 148 */       return null;
/*     */     }
/* 150 */     return this.annGrammar;
/*     */   }
/*     */   
/*     */   public void startDTD(InputEntity entity) throws SAXException {
/* 154 */     super.startDTD(entity);
/*     */     
/* 156 */     this.annGrammar = new AnnotatedGrammar((Grammar)this.grammar, this.codeModel);
/*     */   }
/*     */   
/*     */   public void endDTD() throws SAXException {
/* 160 */     super.endDTD();
/*     */ 
/*     */     
/* 163 */     if (this.controller.hadError()) {
/*     */       return;
/*     */     }
/* 166 */     resetStartPattern();
/*     */     
/* 168 */     processInterfaceDeclarations();
/*     */ 
/*     */ 
/*     */     
/* 172 */     this.annGrammar.exp = this.grammar.getTopLevel();
/*     */ 
/*     */     
/* 175 */     this.annGrammar.serialVersionUID = this.bindInfo.getSerialVersionUID();
/* 176 */     this.annGrammar.rootClass = this.bindInfo.getSuperClass();
/*     */ 
/*     */     
/* 179 */     Annotator.annotate(this.annGrammar, this);
/* 180 */     FieldCollisionChecker.check(this.annGrammar, this);
/*     */ 
/*     */     
/* 183 */     processConstructorDeclarations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetStartPattern() {
/* 191 */     Expression exp = Expression.nullSet;
/*     */     
/* 193 */     Iterator itr = this.bindInfo.elements();
/* 194 */     while (itr.hasNext()) {
/* 195 */       BIElement e = itr.next();
/*     */       
/* 197 */       if (!e.isRoot())
/*     */         continue; 
/* 199 */       ReferenceExp rexp = this.grammar.namedPatterns.getOrCreate(e.name());
/* 200 */       if (!rexp.isDefined()) {
/*     */         
/* 202 */         error(e.getSourceLocation(), "TDTDReader.UndefinedElementInBindInfo", e.name());
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 207 */       exp = this.grammar.pool.createChoice(exp, (Expression)rexp);
/*     */     } 
/*     */     
/* 210 */     if (exp != Expression.nullSet)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       this.grammar.exp = exp;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInterfaceDeclarations() {
/* 226 */     Map decls = new HashMap();
/* 227 */     Iterator itr = this.bindInfo.interfaces();
/* 228 */     while (itr.hasNext()) {
/* 229 */       BIInterface decl = itr.next();
/*     */       
/* 231 */       decls.put(decl, this.annGrammar.createInterfaceItem((JClass)this.classFactory.createInterface((JClassContainer)getTargetPackage(), decl.name(), copyLocator()), Expression.nullSet, copyLocator()));
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
/* 243 */     Map fromName = new HashMap();
/*     */     
/* 245 */     itr = this.annGrammar.iterateClasses();
/* 246 */     while (itr.hasNext()) {
/* 247 */       ClassItem ci = (ClassItem)itr.next();
/* 248 */       fromName.put(ci.getTypeAsDefined().name(), ci);
/*     */     } 
/* 250 */     itr = this.annGrammar.iterateInterfaces();
/* 251 */     while (itr.hasNext()) {
/* 252 */       InterfaceItem itf = (InterfaceItem)itr.next();
/* 253 */       fromName.put(itf.getTypeAsClass().name(), itf);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 258 */     itr = decls.entrySet().iterator();
/* 259 */     while (itr.hasNext()) {
/* 260 */       Map.Entry e = (Map.Entry)itr.next();
/* 261 */       BIInterface decl = (BIInterface)e.getKey();
/* 262 */       InterfaceItem item = (InterfaceItem)e.getValue();
/*     */       
/* 264 */       String[] members = decl.members();
/* 265 */       for (int i = 0; i < members.length; i++) {
/* 266 */         Expression exp = (Expression)fromName.get(members[i]);
/* 267 */         if (exp == null) {
/*     */ 
/*     */           
/* 270 */           error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentInterfaceMember", members[i]);
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 277 */           item.exp = this.annGrammar.getPool().createChoice(item.exp, exp);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPackage getTargetPackage() {
/* 287 */     if (this.opts.defaultPackage != null) {
/* 288 */       return this.codeModel._package(this.opts.defaultPackage);
/*     */     }
/* 290 */     return this.bindInfo.getTargetPackage();
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
/*     */   private void processConstructorDeclarations() {
/* 303 */     Iterator itr = this.bindInfo.elements();
/* 304 */     while (itr.hasNext()) {
/* 305 */       BIElement decl = itr.next();
/* 306 */       ReferenceExp rexp = this.grammar.namedPatterns._get(decl.name());
/* 307 */       if (rexp == null) {
/* 308 */         error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentElementDeclaration", decl.name());
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 313 */       if (!decl.isClass()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 319 */       _assert(rexp.exp instanceof ClassItem);
/*     */       
/* 321 */       ClassItem ci = (ClassItem)rexp.exp;
/*     */       
/* 323 */       decl.declareConstructors(ci, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Expression createAttributeBody(String elementName, String attributeName, String attributeType, String[] enums, short attributeUse, String defaultValue) throws SAXException {
/*     */     FieldItem fieldItem;
/* 333 */     Expression exp = super.createAttributeBody(elementName, attributeName, attributeType, enums, attributeUse, defaultValue);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 338 */     BIElement edecl = this.bindInfo.element(elementName);
/* 339 */     BIAttribute decl = null;
/* 340 */     if (edecl != null) decl = edecl.attribute(attributeName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     if (decl != null) {
/*     */       PrimitiveItem primitiveItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 365 */       BIConversion conv = decl.getConversion();
/* 366 */       if (conv != null) {
/* 367 */         primitiveItem = this.annGrammar.createPrimitiveItem(conv.getTransducer(), (DatabindableDatatype)StringType.theInstance, exp, copyLocator());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 372 */       FieldItem fi = new FieldItem(decl.getPropertyName(), (Expression)primitiveItem, copyLocator());
/* 373 */       fi.realization = decl.getRealization();
/* 374 */       fieldItem = fi;
/*     */     } else {
/*     */       PrimitiveItem primitiveItem;
/*     */       
/*     */       Expression expression;
/* 379 */       if (attributeType.equals("ID")) {
/* 380 */         primitiveItem = this.annGrammar.createPrimitiveItem((Transducer)new IDTransducer(this.codeModel, this.annGrammar.defaultSymbolSpace), (DatabindableDatatype)IDType.theInstance, (Expression)fieldItem, copyLocator());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 385 */       if (attributeType.equals("IDREF")) {
/* 386 */         primitiveItem = this.annGrammar.createPrimitiveItem((Transducer)new IDREFTransducer(this.codeModel, this.annGrammar.defaultSymbolSpace, true), (DatabindableDatatype)IDREFType.theInstance, (Expression)primitiveItem, copyLocator());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 391 */       if (attributeType.equals("IDREFS")) {
/*     */         try {
/* 393 */           expression = this.grammar.pool.createList(this.grammar.pool.createOneOrMore((Expression)this.annGrammar.createPrimitiveItem((Transducer)new IDREFTransducer(this.codeModel, this.annGrammar.defaultSymbolSpace, false), (DatabindableDatatype)DatatypeFactory.getTypeByName("IDREFS"), this.grammar.pool.createData((XSDatatype)IDREFType.theInstance), copyLocator())));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 399 */         catch (DatatypeException e) {
/*     */           
/* 401 */           e.printStackTrace();
/* 402 */           throw new JAXBAssertionError();
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 407 */       fieldItem = new FieldItem(NameConverter.standard.toPropertyName(attributeName), expression, copyLocator());
/*     */     } 
/*     */     
/* 410 */     return (Expression)fieldItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ReferenceExp createElementDeclaration(String elementName) {
/* 417 */     BIElement decl = this.bindInfo.element(elementName);
/*     */ 
/*     */     
/* 420 */     Locator loc = getDeclaredLocationOf(this.grammar.namedPatterns.getOrCreate(elementName));
/*     */     
/* 422 */     if (decl == null || decl.isClass())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 429 */       this.elementDecls.put(elementName, performContentAnnotation(elementName, decl, (Expression)this.elementDecls.get(elementName), loc));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 436 */     ReferenceExp exp = super.createElementDeclaration(elementName);
/*     */     
/* 438 */     ElementExp eexp = (ElementExp)exp.exp;
/*     */     
/* 440 */     if (decl == null) {
/*     */ 
/*     */ 
/*     */       
/* 444 */       exp.exp = (Expression)new ClassCandidateItem(this.classFactory, this.annGrammar, getTargetPackage(), getNameConverter().toClassName(elementName), loc, (Expression)eexp);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 452 */     else if (decl.isClass()) {
/*     */       
/* 454 */       ClassItem t = this.annGrammar.createClassItem(decl.getClassObject(), (Expression)eexp, loc);
/* 455 */       setDeclaredLocationOf(t);
/* 456 */       exp.exp = (Expression)t;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 461 */       if (eexp.contentModel != Expression.anyString) {
/* 462 */         error((Expression)eexp, "TDTDReader.ConversionForNonValueElement", elementName);
/*     */       }
/*     */       
/* 465 */       BIConversion cnv = decl.getConversion();
/* 466 */       if (cnv != null) {
/*     */         
/* 468 */         PrimitiveItem pi = this.annGrammar.createPrimitiveItem(cnv.getTransducer(), (DatabindableDatatype)StringType.theInstance, (Expression)eexp, loc);
/*     */ 
/*     */ 
/*     */         
/* 472 */         exp.exp = (Expression)pi;
/*     */       } else {
/*     */         
/* 475 */         PrimitiveItem pi = this.annGrammar.createPrimitiveItem(this.codeModel, (DatabindableDatatype)StringType.theInstance, (Expression)eexp, loc);
/*     */ 
/*     */ 
/*     */         
/* 479 */         exp.exp = (Expression)pi;
/*     */       } 
/*     */     } 
/*     */     
/* 483 */     return exp;
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
/*     */   private Expression performContentAnnotation(String elementName, BIElement decl, Expression exp, Locator loc) {
/*     */     Expression[] children;
/* 500 */     if (exp == Expression.anyString && decl == null)
/*     */     {
/*     */       
/* 503 */       return exp;
/*     */     }
/* 505 */     if (exp == Expression.epsilon) {
/* 506 */       return exp;
/*     */     }
/* 508 */     if (exp instanceof SequenceExp) {
/*     */       
/* 510 */       children = ((SequenceExp)exp).getChildren();
/*     */     } else {
/* 512 */       children = new Expression[] { exp };
/*     */     } 
/* 514 */     int idx = 0;
/*     */     
/* 516 */     Expression expression = Expression.epsilon;
/*     */     
/* 518 */     if (decl != null) {
/* 519 */       Iterator itr = decl.iterateContents();
/*     */       
/* 521 */       while (itr.hasNext()) {
/*     */         try {
/* 523 */           BIContent bic = itr.next();
/*     */           
/* 525 */           if (idx == children.length)
/*     */           {
/*     */             
/* 528 */             throw new BIContent.MismatchException();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 533 */           expression = this.grammar.pool.createSequence(expression, bic.wrap(children[idx]));
/*     */           
/* 535 */           idx++;
/* 536 */         } catch (com.sun.tools.xjc.reader.dtd.bindinfo.BIContent.MismatchException mme) {
/* 537 */           error(exp, "TDTDReader.ContentProperty.ParticleMismatch", elementName);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 543 */     BIContent restDecl = (decl != null) ? decl.getRest() : null;
/* 544 */     if (restDecl != null) {
/*     */ 
/*     */       
/* 547 */       Expression rest = Expression.epsilon;
/* 548 */       while (idx < children.length) {
/* 549 */         rest = this.grammar.pool.createSequence(rest, children[idx++]);
/*     */       }
/*     */       
/* 552 */       FieldItem fi = new FieldItem(restDecl.getPropertyName(), rest, (JType)restDecl.getType(), loc);
/*     */       
/* 554 */       fi.realization = restDecl.getRealization();
/* 555 */       FieldItem fieldItem1 = fi;
/*     */       
/* 557 */       expression = this.grammar.pool.createSequence(expression, (Expression)fieldItem1);
/*     */     } else {
/*     */       FieldItem fieldItem;
/*     */ 
/*     */ 
/*     */       
/*     */       int i;
/*     */ 
/*     */ 
/*     */       
/* 567 */       for (i = idx; i < children.length; i++) {
/* 568 */         Expression item = children[i].peelOccurence();
/* 569 */         if (!(item instanceof ReferenceExp) || item == getAnyExp()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 574 */       if (i != children.length) {
/*     */ 
/*     */ 
/*     */         
/* 578 */         if (idx == 0) {
/* 579 */           fieldItem = new FieldItem("Content", exp, loc);
/*     */         } else {
/*     */           
/* 582 */           error(exp, "TDTDReader.ContentProperty.DeclarationTooShort", elementName);
/*     */           
/* 584 */           return Expression.nullSet;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 589 */         for (i = idx; i < children.length; i++) {
/* 590 */           expression = this.grammar.pool.createSequence((Expression)fieldItem, (Expression)new FieldItem(NameConverter.standard.toPropertyName(((ReferenceExp)children[i].peelOccurence()).name), children[i], loc));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 599 */     return expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator copyLocator() {
/* 606 */     return new LocatorImpl(this.locator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage get(ReferenceExp exp) {
/* 616 */     return getTargetPackage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameConverter getNameConverter() {
/* 625 */     return NameConverter.standard;
/*     */   }
/*     */   
/*     */   public PackageTracker getPackageTracker() {
/* 629 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reportError(Expression[] srcs, String msg) {
/* 634 */     Vector vec = new Vector();
/* 635 */     for (int i = 0; i < srcs.length; i++) {
/* 636 */       Locator loc = getDeclaredLocationOf(srcs[i]);
/* 637 */       if (loc != null) vec.add(loc); 
/*     */     } 
/* 639 */     reportError(vec.<Locator>toArray(new Locator[0]), msg);
/*     */   }
/*     */   public void reportError(Locator[] locs, String msg) {
/* 642 */     this.controller.error(locs, msg, null);
/*     */   }
/*     */   public ErrorReceiver getErrorReceiver() {
/* 645 */     return this.errorReceiver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void error(Expression loc, String prop) {
/* 655 */     error(loc, prop, null);
/*     */   }
/*     */   protected final void error(Expression loc, String prop, Object arg1) {
/* 658 */     error(loc, prop, new Object[] { arg1 });
/*     */   }
/*     */   protected final void error(Expression loc, String prop, Object[] args) {
/* 661 */     reportError(new Expression[] { loc }, Messages.format(prop, args));
/*     */   }
/*     */   
/*     */   protected final void error(Locator loc, String prop, Object arg1) {
/* 665 */     error(loc, prop, new Object[] { arg1 });
/*     */   }
/*     */   protected final void error(Locator loc, String prop, Object[] args) {
/* 668 */     reportError(new Locator[] { loc }, Messages.format(prop, args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void _assert(boolean b) {
/* 675 */     if (!b) throw new JAXBAssertionError(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\TDTDReader.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */