/*     */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*     */ 
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.DataExp;
/*     */ import com.sun.msv.grammar.DataOrValueExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionCloner;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.NameClassAndExpression;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassCandidateItem;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.xducer.BuiltinDatatypeTransducerFactory;
/*     */ import com.sun.tools.xjc.grammar.xducer.EnumerationXducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PrimitiveTypeAnnotator
/*     */   extends ExpressionCloner
/*     */ {
/*     */   private final AnnotatedGrammar grammar;
/*     */   private final AnnotatorController controller;
/*     */   private final CodeModelClassFactory classFactory;
/*     */   private final Set visitedExps;
/*     */   private final Map primitiveItems;
/*     */   private JPackage currentPackage;
/*     */   
/*     */   PrimitiveTypeAnnotator(AnnotatedGrammar _grammar, AnnotatorController _controller) {
/*  65 */     super(_grammar.getPool());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     this.visitedExps = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     this.primitiveItems = new HashMap();
/*     */     this.grammar = _grammar;
/*     */     this.controller = _controller;
/*     */     this.classFactory = new CodeModelClassFactory(this.controller.getErrorReceiver());
/*     */     this.currentPackage = _grammar.codeModel._package("");
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression onRef(ReferenceExp exp) {
/* 100 */     JPackage oldPackage = this.currentPackage;
/* 101 */     if (this.controller.getPackageTracker().get(exp) != null) {
/* 102 */       this.currentPackage = this.controller.getPackageTracker().get(exp);
/*     */     }
/* 104 */     if (this.visitedExps.add(exp)) {
/* 105 */       Expression e = processEnumeration(exp.name, exp.exp);
/* 106 */       if (e == null) e = exp.exp.visit((ExpressionVisitorExpression)this); 
/* 107 */       exp.exp = e;
/*     */     } 
/*     */     
/* 110 */     this.currentPackage = oldPackage;
/* 111 */     return (Expression)exp;
/*     */   }
/*     */   
/*     */   public Expression onOther(OtherExp exp) {
/* 115 */     if (exp instanceof PrimitiveItem)
/* 116 */       return (Expression)exp; 
/* 117 */     if (exp instanceof com.sun.tools.xjc.grammar.IgnoreItem) {
/* 118 */       return (Expression)exp;
/*     */     }
/* 120 */     if (this.visitedExps.add(exp)) {
/* 121 */       String name = null;
/* 122 */       if (exp instanceof ClassItem)
/* 123 */         name = ((ClassItem)exp).name; 
/* 124 */       if (exp instanceof ClassCandidateItem) {
/* 125 */         name = ((ClassCandidateItem)exp).name;
/*     */       }
/* 127 */       Expression e = null;
/* 128 */       if (name != null)
/* 129 */         e = processEnumeration(name, exp.exp); 
/* 130 */       if (e == null) {
/* 131 */         e = exp.exp.visit((ExpressionVisitorExpression)this);
/*     */       }
/* 133 */       exp.exp = e;
/*     */     } 
/* 135 */     return (Expression)exp;
/*     */   }
/*     */   
/*     */   public Expression onElement(ElementExp exp) {
/* 139 */     if (this.visitedExps.add(exp)) {
/* 140 */       Expression e = processEnumeration((NameClassAndExpression)exp);
/* 141 */       if (e == null)
/* 142 */         e = exp.contentModel.visit((ExpressionVisitorExpression)this); 
/* 143 */       exp.contentModel = e;
/*     */     } 
/* 145 */     return (Expression)exp;
/*     */   }
/*     */   
/*     */   public Expression onAttribute(AttributeExp exp) {
/* 149 */     if (this.visitedExps.contains(exp)) {
/* 150 */       return (Expression)exp;
/*     */     }
/* 152 */     Expression e = processEnumeration((NameClassAndExpression)exp);
/* 153 */     if (e == null) {
/* 154 */       e = exp.exp.visit((ExpressionVisitorExpression)this);
/*     */     }
/* 156 */     e = this.pool.createAttribute(exp.nameClass, e);
/* 157 */     this.visitedExps.add(e);
/* 158 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression processEnumeration(NameClassAndExpression exp) {
/* 166 */     NameClass nc = exp.getNameClass();
/* 167 */     if (!(nc instanceof SimpleNameClass)) {
/* 168 */       return null;
/*     */     }
/* 170 */     return processEnumeration(((SimpleNameClass)nc).localName + "Type", exp.getContentModel());
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
/*     */   public Expression processEnumeration(String className, Expression exp) {
/*     */     String decoratedClassName;
/* 189 */     if (className == null)
/*     */     {
/* 191 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     Expression e = exp.visit((ExpressionVisitorExpression)new Object(this, this.pool));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     if (!(e instanceof ChoiceExp))
/*     */     {
/* 215 */       return null;
/*     */     }
/* 217 */     ChoiceExp cexp = (ChoiceExp)e;
/* 218 */     Expression[] children = cexp.getChildren();
/* 219 */     for (int i = 0; i < children.length; i++) {
/* 220 */       if (!(children[i] instanceof ValueExp))
/*     */       {
/* 222 */         return null;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 228 */     int cnt = 1;
/*     */     
/*     */     do {
/* 231 */       decoratedClassName = this.controller.getNameConverter().toClassName(className) + ((cnt++ == 1) ? "" : String.valueOf(cnt));
/*     */     }
/* 233 */     while (this.currentPackage._getClass(decoratedClassName) != null);
/*     */ 
/*     */     
/* 236 */     PrimitiveItem p = this.grammar.createPrimitiveItem((Transducer)new EnumerationXducer(this.controller.getNameConverter(), this.classFactory.createClass((JClassContainer)this.currentPackage, decoratedClassName, null), (Expression)cexp, new HashMap(), null), (DatabindableDatatype)StringType.theInstance, (Expression)cexp, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     this.primitiveItems.put(exp, p);
/* 245 */     return (Expression)p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression onData(DataExp exp) {
/* 253 */     return onDataOrValue((DataOrValueExp)exp); } public Expression onValue(ValueExp exp) {
/* 254 */     return onDataOrValue((DataOrValueExp)exp);
/*     */   }
/*     */   private Expression onDataOrValue(DataOrValueExp exp) {
/*     */     StringType stringType;
/*     */     IdentityTransducer identityTransducer;
/* 259 */     if (this.primitiveItems.containsKey(exp))
/*     */     {
/*     */       
/* 262 */       return (Expression)this.primitiveItems.get(exp);
/*     */     }
/*     */     
/* 265 */     Datatype dt = exp.getType();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     if (dt instanceof XSDatatype) {
/* 271 */       Transducer xducer = BuiltinDatatypeTransducerFactory.get(this.grammar, (XSDatatype)dt);
/*     */       
/* 273 */       XSDatatype guard = (XSDatatype)dt;
/*     */     } else {
/*     */       
/* 276 */       identityTransducer = new IdentityTransducer(this.grammar.codeModel);
/* 277 */       stringType = StringType.theInstance;
/*     */     } 
/*     */     
/* 280 */     PrimitiveItem p = this.grammar.createPrimitiveItem((Transducer)identityTransducer, (DatabindableDatatype)stringType, (Expression)exp, null);
/* 281 */     this.primitiveItems.put(exp, p);
/* 282 */     return (Expression)p;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\PrimitiveTypeAnnotator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */