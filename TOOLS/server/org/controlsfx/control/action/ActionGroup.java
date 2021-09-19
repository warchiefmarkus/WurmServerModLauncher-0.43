/*     */ package org.controlsfx.control.action;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
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
/*     */ public class ActionGroup
/*     */   extends Action
/*     */ {
/*     */   private final ObservableList<Action> actions;
/*     */   
/*     */   public ActionGroup(String text, Action... actions) {
/* 109 */     this(text, Arrays.asList(actions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionGroup(String text, Collection<Action> actions)
/*     */   {
/* 120 */     super(text);
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
/* 153 */     this.actions = FXCollections.observableArrayList(); getActions().addAll(actions); } public ActionGroup(String text, Node icon, Collection<Action> actions) { super(text); this.actions = FXCollections.observableArrayList();
/*     */     setGraphic(icon);
/*     */     getActions().addAll(actions); }
/*     */    public ActionGroup(String text, Node icon, Action... actions) {
/*     */     this(text, icon, Arrays.asList(actions));
/*     */   }
/*     */   public final ObservableList<Action> getActions() {
/* 160 */     return this.actions;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 164 */     return getText();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\ActionGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */