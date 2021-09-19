/*     */ package 1.0.com.sun.tools.xjc.generator.validator;
/*     */ 
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionCloner;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.NameClassAndExpression;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.ExternalItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.IgnoreItem;
/*     */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*     */ import com.sun.tools.xjc.grammar.JavaItem;
/*     */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SchemaFragmentBuilder
/*     */   extends ExpressionCloner
/*     */   implements JavaItemVisitor
/*     */ {
/*     */   private boolean inAttribute = false;
/*     */   private boolean inSuperClass = false;
/*     */   private Expression anyAttributes;
/*     */   
/*     */   public SchemaFragmentBuilder(ExpressionPool pool) {
/*  50 */     super(pool);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     this.anyAttributes = this.pool.createZeroOrMore(this.pool.createAttribute(NameClass.ALL));
/*     */   }
/*     */   
/*     */   public Object onClass(ClassItem ii) {
/* 123 */     if (this.inSuperClass) {
/* 124 */       this.inSuperClass = false;
/*     */       try {
/* 126 */         return ii.exp.visit((ExpressionVisitorExpression)this);
/*     */       } finally {
/* 128 */         this.inSuperClass = true;
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     if (this.inAttribute)
/*     */     {
/* 134 */       return this.pool.createValue((XSDatatype)StringType.theInstance, ("\000" + ii.getType().fullName()).intern());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     return new ElementPattern((NameClass)new SimpleNameClass("http://java.sun.com/jaxb/xjc/dummy-elements", ii.getType().fullName().intern()), this.anyAttributes);
/*     */   }
/*     */   
/*     */   public Expression onRef(ReferenceExp exp) {
/*     */     return exp.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Expression onOther(OtherExp exp) {
/*     */     if (exp instanceof JavaItem)
/*     */       return (Expression)((JavaItem)exp).visitJI(this); 
/*     */     return exp.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Expression onAttribute(AttributeExp exp) {
/*     */     if (this.inAttribute)
/*     */       throw new JAXBAssertionError(); 
/*     */     this.inAttribute = true;
/*     */     try {
/*     */       return (Expression)new AttributeExp(exp.nameClass, exp.exp.visit((ExpressionVisitorExpression)this));
/*     */     } finally {
/*     */       this.inAttribute = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Expression onElement(ElementExp exp) {
/*     */     return (Expression)createElement((NameClassAndExpression)exp);
/*     */   }
/*     */   
/*     */   public ElementPattern createElement(NameClassAndExpression exp) {
/*     */     return new ElementPattern(exp.getNameClass(), exp.getContentModel().visit((ExpressionVisitorExpression)this));
/*     */   }
/*     */   
/*     */   public Object onPrimitive(PrimitiveItem pi) {
/*     */     return pi.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Object onField(FieldItem fi) {
/*     */     return fi.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Object onIgnore(IgnoreItem ii) {
/*     */     return ii.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Object onInterface(InterfaceItem ii) {
/*     */     return ii.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Object onSuper(SuperClassItem si) {
/*     */     this.inSuperClass = true;
/*     */     try {
/*     */       return si.exp.visit((ExpressionVisitorExpression)this);
/*     */     } finally {
/*     */       this.inSuperClass = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object onExternal(ExternalItem ei) {
/*     */     return ei.createValidationFragment();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\validator\SchemaFragmentBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */