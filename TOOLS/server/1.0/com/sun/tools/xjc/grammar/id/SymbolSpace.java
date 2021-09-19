/*    */ package 1.0.com.sun.tools.xjc.grammar.id;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SymbolSpace
/*    */ {
/*    */   private JType type;
/*    */   private final JCodeModel codeModel;
/*    */   
/*    */   public SymbolSpace(JCodeModel _codeModel) {
/* 34 */     this.codeModel = _codeModel;
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
/*    */   public JType getType() {
/* 46 */     if (this.type == null) return (JType)this.codeModel.ref(Object.class); 
/* 47 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(JType _type) {
/* 51 */     if (this.type == null)
/* 52 */       this.type = _type; 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 56 */     if (this.type == null) return "undetermined"; 
/* 57 */     return this.type.name();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\id\SymbolSpace.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */