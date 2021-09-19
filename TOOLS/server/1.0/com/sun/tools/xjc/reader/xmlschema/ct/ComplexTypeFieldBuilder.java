/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.CTBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ChoiceComplexTypeBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ExtendedComplexTypeBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.FreshComplexTypeBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.MixedComplexTypeBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.RestrictedComplexTypeBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.STDerivedComplexTypeBuilder;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComplexTypeFieldBuilder
/*    */ {
/*    */   protected final BGMBuilder builder;
/*    */   private final CTBuilder[] complexTypeBuilders;
/* 31 */   private final Map complexTypeBindingModes = new HashMap();
/*    */ 
/*    */ 
/*    */   
/*    */   public ComplexTypeFieldBuilder(BGMBuilder _builder) {
/* 36 */     this.builder = _builder;
/*    */ 
/*    */ 
/*    */     
/* 40 */     this.complexTypeBuilders = new CTBuilder[] { (CTBuilder)new ChoiceComplexTypeBuilder(this), (CTBuilder)new MixedComplexTypeBuilder(this), (CTBuilder)new FreshComplexTypeBuilder(this), (CTBuilder)new ExtendedComplexTypeBuilder(this), (CTBuilder)new RestrictedComplexTypeBuilder(this), (CTBuilder)new STDerivedComplexTypeBuilder(this) };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression build(XSComplexType type) {
/* 54 */     for (int i = 0; i < this.complexTypeBuilders.length; i++) {
/* 55 */       if (this.complexTypeBuilders[i].isApplicable(type))
/* 56 */         return this.complexTypeBuilders[i].build(type); 
/*    */     } 
/* 58 */     _assert(false);
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void recordBindingMode(XSComplexType type, ComplexTypeBindingMode flag) {
/* 77 */     Object o = this.complexTypeBindingModes.put(type, flag);
/* 78 */     _assert((o == null));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ComplexTypeBindingMode getBindingMode(XSComplexType type) {
/* 86 */     Object r = this.complexTypeBindingModes.get(type);
/* 87 */     _assert((r != null));
/* 88 */     return (ComplexTypeBindingMode)r;
/*    */   }
/*    */   
/*    */   protected static void _assert(boolean b) {
/* 92 */     if (!b)
/* 93 */       throw new JAXBAssertionError(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\ComplexTypeFieldBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */