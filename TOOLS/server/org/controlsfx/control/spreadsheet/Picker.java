/*    */ package org.controlsfx.control.spreadsheet;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import javafx.collections.FXCollections;
/*    */ import javafx.collections.ObservableList;
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
/*    */ public abstract class Picker
/*    */ {
/* 54 */   private final ObservableList<String> styleClass = FXCollections.observableArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Picker() {
/* 60 */     this(new String[] { "picker-label" });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Picker(String... styleClass) {
/* 68 */     this.styleClass.addAll((Object[])styleClass);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Picker(Collection<String> styleClass) {
/* 76 */     this.styleClass.addAll(styleClass);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final ObservableList<String> getStyleClass() {
/* 84 */     return this.styleClass;
/*    */   }
/*    */   
/*    */   public abstract void onClick();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\Picker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */