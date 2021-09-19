/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.msv.grammar.ExpressionPool;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.CTBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
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
/*    */ 
/*    */ abstract class AbstractCTBuilder
/*    */   implements CTBuilder
/*    */ {
/*    */   protected final ComplexTypeFieldBuilder builder;
/*    */   protected final BGMBuilder bgmBuilder;
/*    */   protected final ExpressionPool pool;
/*    */   
/*    */   protected AbstractCTBuilder(ComplexTypeFieldBuilder _builder) {
/* 32 */     this.builder = _builder;
/* 33 */     this.bgmBuilder = this.builder.builder;
/* 34 */     this.pool = this.bgmBuilder.grammar.getPool();
/*    */   }
/*    */   
/*    */   protected static void _assert(boolean b) {
/* 38 */     if (!b)
/* 39 */       throw new JAXBAssertionError(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\AbstractCTBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */