/*     */ package org.controlsfx.control.decoration;
/*     */ 
/*     */ import impl.org.controlsfx.ImplUtils;
/*     */ import impl.org.controlsfx.skin.DecorationPane;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.Parent;
/*     */ import javafx.scene.Scene;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Decorator
/*     */ {
/*     */   private static final String DECORATIONS_PROPERTY_KEY = "$org.controlsfx.decorations$";
/*     */   
/*     */   public static final void addDecoration(Node target, Decoration decoration) {
/* 108 */     getDecorations(target, true).add(decoration);
/* 109 */     updateDecorationsOnNode(target, (List<Decoration>)FXCollections.observableArrayList((Object[])new Decoration[] { decoration }, ), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void removeDecoration(Node target, Decoration decoration) {
/* 118 */     getDecorations(target, true).remove(decoration);
/* 119 */     updateDecorationsOnNode(target, null, (List<Decoration>)FXCollections.observableArrayList((Object[])new Decoration[] { decoration }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void removeAllDecorations(Node target) {
/* 127 */     ObservableList<Decoration> observableList = getDecorations(target, true);
/* 128 */     ObservableList observableList1 = FXCollections.observableArrayList((Collection)observableList);
/*     */     
/* 130 */     target.getProperties().remove("$org.controlsfx.decorations$");
/*     */     
/* 132 */     updateDecorationsOnNode(target, null, (List<Decoration>)observableList1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ObservableList<Decoration> getDecorations(Node target) {
/* 141 */     return getDecorations(target, false);
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
/*     */   private static final ObservableList<Decoration> getDecorations(Node target, boolean createIfAbsent) {
/* 154 */     ObservableList<Decoration> decorations = (ObservableList<Decoration>)target.getProperties().get("$org.controlsfx.decorations$");
/* 155 */     if (decorations == null && createIfAbsent) {
/* 156 */       decorations = FXCollections.observableArrayList();
/* 157 */       target.getProperties().put("$org.controlsfx.decorations$", decorations);
/*     */     } 
/* 159 */     return decorations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void updateDecorationsOnNode(Node target, List<Decoration> added, List<Decoration> removed) {
/* 165 */     getDecorationPane(target, pane -> pane.updateDecorationsOnNode(target, added, removed));
/*     */   }
/*     */   
/* 168 */   private static List<Scene> currentlyInstallingScenes = new ArrayList<>();
/* 169 */   private static Map<Scene, List<Consumer<DecorationPane>>> pendingTasksByScene = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getDecorationPane(final Node target, Consumer<DecorationPane> task) {
/* 177 */     DecorationPane pane = getDecorationPaneInParentHierarchy(target);
/*     */     
/* 179 */     if (pane != null) {
/* 180 */       task.accept(pane);
/*     */     } else {
/*     */       
/* 183 */       final Consumer<Scene> sceneConsumer = scene -> {
/*     */           if (currentlyInstallingScenes.contains(scene)) {
/*     */             List<Consumer<DecorationPane>> list = pendingTasksByScene.get(scene);
/*     */             
/*     */             if (list == null) {
/*     */               list = new LinkedList<>();
/*     */               
/*     */               pendingTasksByScene.put(scene, list);
/*     */             } 
/*     */             
/*     */             list.add(task);
/*     */             return;
/*     */           } 
/*     */           DecorationPane _pane = getDecorationPaneInParentHierarchy(target);
/*     */           if (_pane == null) {
/*     */             currentlyInstallingScenes.add(scene);
/*     */             _pane = new DecorationPane();
/*     */             Parent parent = scene.getRoot();
/*     */             ImplUtils.injectAsRootPane(scene, (Parent)_pane, true);
/*     */             _pane.setRoot((Node)parent);
/*     */             currentlyInstallingScenes.remove(scene);
/*     */           } 
/*     */           task.accept(_pane);
/*     */           List<Consumer<DecorationPane>> pendingTasks = pendingTasksByScene.remove(scene);
/*     */           if (pendingTasks != null) {
/*     */             for (Consumer<DecorationPane> pendingTask : pendingTasks) {
/*     */               pendingTask.accept(_pane);
/*     */             }
/*     */           }
/*     */         };
/* 213 */       Scene scene = target.getScene();
/* 214 */       if (scene != null) {
/* 215 */         sceneConsumer.accept(scene);
/*     */       } else {
/*     */         
/* 218 */         InvalidationListener sceneListener = new InvalidationListener() {
/*     */             public void invalidated(Observable o) {
/* 220 */               if (target.getScene() != null) {
/* 221 */                 target.sceneProperty().removeListener(this);
/* 222 */                 sceneConsumer.accept(target.getScene());
/*     */               } 
/*     */             }
/*     */           };
/* 226 */         target.sceneProperty().addListener(sceneListener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DecorationPane getDecorationPaneInParentHierarchy(Node target) {
/* 232 */     Parent p = target.getParent();
/* 233 */     while (p != null) {
/* 234 */       if (p instanceof DecorationPane) {
/* 235 */         return (DecorationPane)p;
/*     */       }
/* 237 */       p = p.getParent();
/*     */     } 
/* 239 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\decoration\Decorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */