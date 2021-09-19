/*    */ package org.controlsfx.control.decoration;
/*    */ 
/*    */ import javafx.collections.ObservableList;
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
/*    */ public class StyleClassDecoration
/*    */   extends Decoration
/*    */ {
/*    */   private final String[] styleClasses;
/*    */   
/*    */   public StyleClassDecoration(String... styleClass) {
/* 55 */     if (styleClass == null || styleClass.length == 0) {
/* 56 */       throw new IllegalArgumentException("var-arg style class array must not be null or empty");
/*    */     }
/* 58 */     this.styleClasses = styleClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public Node applyDecoration(Node targetNode) {
/* 63 */     ObservableList<String> observableList = targetNode.getStyleClass();
/*    */     
/* 65 */     for (String styleClass : this.styleClasses) {
/* 66 */       if (!observableList.contains(styleClass))
/*    */       {
/*    */ 
/*    */         
/* 70 */         observableList.add(styleClass);
/*    */       }
/*    */     } 
/*    */     
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeDecoration(Node targetNode) {
/* 79 */     targetNode.getStyleClass().removeAll((Object[])this.styleClasses);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\decoration\StyleClassDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */