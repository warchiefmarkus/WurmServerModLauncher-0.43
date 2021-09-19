/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.datatype.xsd.ByteType;
/*     */ import com.sun.msv.datatype.xsd.DoubleType;
/*     */ import com.sun.msv.datatype.xsd.FloatType;
/*     */ import com.sun.msv.datatype.xsd.IntType;
/*     */ import com.sun.msv.datatype.xsd.LongType;
/*     */ import com.sun.msv.datatype.xsd.ShortType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
/*     */ import com.sun.tools.xjc.grammar.xducer.BuiltinDatatypeTransducerFactory;
/*     */ import com.sun.tools.xjc.grammar.xducer.CastTranducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.DelayedTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.TypeAdaptedTransducer;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.Messages;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MagicTransducer
/*     */   extends DelayedTransducer
/*     */ {
/*     */   private final JType targetType;
/*     */   private BIConversion parent;
/*     */   protected static final String ERR_ATTRIBUTE_REQUIRED = "MagicTransducer.AttributeRequired";
/*     */   
/*     */   public MagicTransducer(JType _targetType) {
/*  44 */     this.targetType = _targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(BIConversion conv) {
/*  53 */     this.parent = conv;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Transducer create() {
/*  58 */     if (!this.targetType.isPrimitive()) {
/*  59 */       JPrimitiveType unboxed = ((JClass)this.targetType).getPrimitiveType();
/*  60 */       if (unboxed == null)
/*  61 */         return error(); 
/*  62 */       return TypeAdaptedTransducer.adapt((Transducer)new CastTranducer(unboxed, createCore()), this.targetType);
/*     */     } 
/*     */ 
/*     */     
/*  66 */     return (Transducer)new CastTranducer((JPrimitiveType)this.targetType, createCore());
/*     */   }
/*     */   
/*  69 */   public boolean isID() { return false; } public SymbolSpace getIDSymbolSpace() {
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Transducer createCore() {
/*  75 */     XSSimpleType owner = findOwner();
/*     */     
/*  77 */     AnnotatedGrammar grammar = (this.parent.getBuilder()).grammar;
/*     */ 
/*     */     
/*  80 */     for (XSSimpleType st = owner; st != null; st = st.getSimpleBaseType()) {
/*  81 */       if ("http://www.w3.org/2001/XMLSchema".equals(st.getTargetNamespace())) {
/*     */ 
/*     */         
/*  84 */         String name = st.getName().intern();
/*  85 */         if (name == "float")
/*  86 */           return BuiltinDatatypeTransducerFactory.get(grammar, (XSDatatype)FloatType.theInstance); 
/*  87 */         if (name == "double")
/*  88 */           return BuiltinDatatypeTransducerFactory.get(grammar, (XSDatatype)DoubleType.theInstance); 
/*  89 */         if (name == "byte")
/*  90 */           return BuiltinDatatypeTransducerFactory.get(grammar, (XSDatatype)ByteType.theInstance); 
/*  91 */         if (name == "short")
/*  92 */           return BuiltinDatatypeTransducerFactory.get(grammar, (XSDatatype)ShortType.theInstance); 
/*  93 */         if (name == "int")
/*  94 */           return BuiltinDatatypeTransducerFactory.get(grammar, (XSDatatype)IntType.theInstance); 
/*  95 */         if (name == "long") {
/*  96 */           return BuiltinDatatypeTransducerFactory.get(grammar, (XSDatatype)LongType.theInstance);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     return error();
/*     */   }
/*     */   
/*     */   private XSSimpleType findOwner() {
/* 105 */     XSComponent c = this.parent.getOwner();
/* 106 */     if (c instanceof XSSimpleType)
/* 107 */       return (XSSimpleType)c; 
/* 108 */     if (c instanceof XSComplexType)
/* 109 */       return ((XSComplexType)c).getContentType().asSimpleType(); 
/* 110 */     if (c instanceof XSElementDecl)
/* 111 */       return ((XSElementDecl)c).getType().asSimpleType(); 
/* 112 */     if (c instanceof XSAttributeDecl) {
/* 113 */       return ((XSAttributeDecl)c).getType();
/*     */     }
/*     */     
/* 116 */     return null;
/*     */   }
/*     */   
/*     */   private Transducer error() {
/* 120 */     (this.parent.getBuilder()).errorReceiver.error(this.parent.getLocation(), Messages.format("MagicTransducer.AttributeRequired"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return (Transducer)new IdentityTransducer((this.parent.getBuilder()).grammar.codeModel);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\MagicTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */