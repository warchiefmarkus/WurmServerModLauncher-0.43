/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.WhitespaceTransducer;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ConversionFinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.DatatypeBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleTypeBuilder
/*     */ {
/*     */   protected final BGMBuilder builder;
/*     */   public final DatatypeBuilder datatypeBuilder;
/*     */   protected final ConversionFinder conversionFinder;
/*     */   private final ExpressionPool pool;
/*     */   public final Stack refererStack;
/*     */   
/*     */   SimpleTypeBuilder(BGMBuilder builder) {
/*  76 */     this.refererStack = new Stack();
/*     */     this.builder = builder;
/*     */     this.datatypeBuilder = new DatatypeBuilder(builder, builder.schemas);
/*     */     this.conversionFinder = new ConversionFinder(builder);
/*     */     this.pool = builder.pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression build(XSSimpleType type) {
/*  87 */     Expression e = checkRefererCustomization(type);
/*  88 */     if (e == null) {
/*  89 */       e = (Expression)type.apply((XSSimpleTypeFunction)new Functor(this, type, null));
/*     */     }
/*  91 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BIConversion getRefererCustomization() {
/*  99 */     BindInfo info = this.builder.getBindInfo(this.refererStack.peek());
/* 100 */     BIProperty prop = (BIProperty)info.get(BIProperty.NAME);
/* 101 */     if (prop == null) return null; 
/* 102 */     return prop.conv;
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
/*     */   private Expression checkRefererCustomization(XSSimpleType type) {
/* 118 */     XSComponent top = this.refererStack.peek();
/*     */     
/* 120 */     if (top instanceof XSElementDecl) {
/*     */       
/* 122 */       XSElementDecl eref = (XSElementDecl)top;
/* 123 */       _assert((eref.getType() == type));
/* 124 */       detectJavaTypeCustomization();
/*     */     }
/* 126 */     else if (top instanceof XSAttributeDecl) {
/* 127 */       XSAttributeDecl aref = (XSAttributeDecl)top;
/* 128 */       _assert((aref.getType() == type));
/* 129 */       detectJavaTypeCustomization();
/*     */     }
/* 131 */     else if (top instanceof XSComplexType) {
/* 132 */       XSComplexType tref = (XSComplexType)top;
/* 133 */       _assert((tref.getBaseType() == type));
/* 134 */       detectJavaTypeCustomization();
/*     */     }
/* 136 */     else if (top != type) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       _assert(false);
/*     */     } 
/*     */ 
/*     */     
/* 146 */     BIConversion conv = getRefererCustomization();
/* 147 */     if (conv != null) {
/* 148 */       conv.markAsAcknowledged();
/*     */       
/* 150 */       return (Expression)buildPrimitiveType(type, conv.getTransducer());
/*     */     } 
/*     */     
/* 153 */     return null;
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
/* 164 */     BindInfo info = this.builder.getBindInfo(this.refererStack.peek());
/* 165 */     BIConversion conv = (BIConversion)info.get(BIConversion.NAME);
/*     */     
/* 167 */     if (conv != null) {
/*     */       
/* 169 */       conv.markAsAcknowledged();
/*     */ 
/*     */       
/* 172 */       this.builder.errorReporter.error(conv.getLocation(), "SimpleTypeBuilder.UnnestedJavaTypeCustomization");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PrimitiveItem buildPrimitiveType(XSSimpleType type, Transducer xducer) {
/* 181 */     XSDatatype dt = this.datatypeBuilder.build(type);
/* 182 */     return this.builder.grammar.createPrimitiveItem(WhitespaceTransducer.create(xducer, this.builder.grammar.codeModel, type), (DatabindableDatatype)dt, this.pool.createData(dt), type.getLocator());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void _assert(boolean b) {
/* 307 */     if (!b)
/* 308 */       throw new JAXBAssertionError(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\SimpleTypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */