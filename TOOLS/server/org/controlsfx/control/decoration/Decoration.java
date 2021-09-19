/*    */ package org.controlsfx.control.decoration;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javafx.scene.Node;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Decoration
/*    */ {
/*    */   private volatile Map<String, Object> properties;
/*    */   
/*    */   public abstract Node applyDecoration(Node paramNode);
/*    */   
/*    */   public abstract void removeDecoration(Node paramNode);
/*    */   
/*    */   public final synchronized Map<String, Object> getProperties() {
/* 87 */     if (this.properties == null) {
/* 88 */       this.properties = new HashMap<>();
/*    */     }
/* 90 */     return this.properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\decoration\Decoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */