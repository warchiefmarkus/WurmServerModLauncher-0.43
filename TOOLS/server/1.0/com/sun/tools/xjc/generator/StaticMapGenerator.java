/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JMethod;
/*    */ import com.sun.codemodel.JVar;
/*    */ import com.sun.tools.xjc.generator.ObjectFactoryGenerator;
/*    */ import com.sun.tools.xjc.util.Util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class StaticMapGenerator
/*    */ {
/*    */   public final JVar $map;
/*    */   private JBlock block;
/*    */   private int cnt;
/* 46 */   private int ticketMaster = 1;
/*    */ 
/*    */   
/*    */   private final int THRESHOLD;
/*    */ 
/*    */ 
/*    */   
/*    */   protected StaticMapGenerator(JVar $map, JBlock block) {
/* 54 */     this.$map = $map;
/* 55 */     this.block = block;
/*    */     
/* 57 */     String debug = Util.getSystemProperty(ObjectFactoryGenerator.class, "staticThreshold");
/* 58 */     if (debug == null) { this.THRESHOLD = 500; }
/* 59 */     else { this.THRESHOLD = Integer.parseInt(debug); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void add(JExpression key, JExpression value) {
/* 66 */     this.block.invoke((JExpression)this.$map, "put").arg(key).arg(value);
/*    */ 
/*    */ 
/*    */     
/* 70 */     if (++this.cnt >= this.THRESHOLD) {
/*    */       
/* 72 */       JMethod m = createNewMethod(this.ticketMaster++);
/* 73 */       this.block.invoke(m);
/* 74 */       this.block = m.body();
/* 75 */       this.cnt = 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract JMethod createNewMethod(int paramInt);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\StaticMapGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */