/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JExpressionImpl;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JArray
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private final JType type;
/*    */   private final JExpression size;
/* 20 */   private List exprs = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.sun.codemodel.JArray add(JExpression e) {
/* 26 */     if (this.exprs == null)
/* 27 */       this.exprs = new ArrayList(); 
/* 28 */     this.exprs.add(e);
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   JArray(JType type, JExpression size) {
/* 33 */     this.type = type;
/* 34 */     this.size = size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(JFormatter f) {
/* 41 */     int arrayCount = 0;
/* 42 */     JType t = this.type;
/*    */     
/* 44 */     while (t.isArray()) {
/* 45 */       t = t.elementType();
/* 46 */       arrayCount++;
/*    */     } 
/*    */     
/* 49 */     f.p("new").g((JGenerable)t).p('[');
/* 50 */     if (this.size != null)
/* 51 */       f.g((JGenerable)this.size); 
/* 52 */     f.p(']');
/*    */     
/* 54 */     for (int i = 0; i < arrayCount; i++) {
/* 55 */       f.p("[]");
/*    */     }
/* 57 */     if (this.size == null || this.exprs != null)
/* 58 */       f.p('{'); 
/* 59 */     if (this.exprs != null) {
/* 60 */       boolean first = true;
/* 61 */       if (this.exprs.size() > 0) {
/* 62 */         for (Iterator iterator = this.exprs.iterator(); iterator.hasNext(); ) {
/* 63 */           if (!first)
/* 64 */             f.p(','); 
/* 65 */           f.g((JGenerable)iterator.next());
/* 66 */           first = false;
/*    */         } 
/*    */       }
/*    */     } else {
/* 70 */       f.p(' ');
/*    */     } 
/* 72 */     if (this.size == null || this.exprs != null)
/* 73 */       f.p('}'); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JArray.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */