/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import org.controlsfx.control.decoration.Decoration;
/*     */ import org.controlsfx.control.decoration.Decorator;
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
/*     */ public class DecorationPane
/*     */   extends StackPane
/*     */ {
/*  47 */   private final Map<Node, List<Node>> nodeDecorationMap = new WeakHashMap<>();
/*     */   
/*  49 */   ChangeListener<Boolean> visibilityListener = new ChangeListener<Boolean>() {
/*     */       public void changed(ObservableValue<? extends Boolean> o, Boolean wasVisible, Boolean isVisible) {
/*  51 */         BooleanProperty p = (BooleanProperty)o;
/*  52 */         Node n = (Node)p.getBean();
/*     */         
/*  54 */         DecorationPane.this.removeAllDecorationsOnNode(n, (List<Decoration>)Decorator.getDecorations(n));
/*  55 */         Decorator.removeAllDecorations(n);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public DecorationPane() {
/*  61 */     setBackground(null);
/*     */   }
/*     */   
/*     */   public void setRoot(Node root) {
/*  65 */     getChildren().setAll((Object[])new Node[] { root });
/*     */   }
/*     */   
/*     */   public void updateDecorationsOnNode(Node targetNode, List<Decoration> added, List<Decoration> removed) {
/*  69 */     removeAllDecorationsOnNode(targetNode, removed);
/*  70 */     addAllDecorationsOnNode(targetNode, added);
/*     */   }
/*     */   
/*     */   private void showDecoration(Node targetNode, Decoration decoration) {
/*  74 */     Node decorationNode = decoration.applyDecoration(targetNode);
/*  75 */     if (decorationNode != null) {
/*  76 */       List<Node> decorationNodes = this.nodeDecorationMap.get(targetNode);
/*  77 */       if (decorationNodes == null) {
/*  78 */         decorationNodes = new ArrayList<>();
/*  79 */         this.nodeDecorationMap.put(targetNode, decorationNodes);
/*     */       } 
/*  81 */       decorationNodes.add(decorationNode);
/*     */       
/*  83 */       if (!getChildren().contains(decorationNode)) {
/*  84 */         getChildren().add(decorationNode);
/*  85 */         StackPane.setAlignment(decorationNode, Pos.TOP_LEFT);
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     targetNode.visibleProperty().addListener(this.visibilityListener);
/*     */   }
/*     */   
/*     */   private void removeAllDecorationsOnNode(Node targetNode, List<Decoration> decorations) {
/*  93 */     if (decorations == null || targetNode == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  98 */     List<Node> decorationNodes = this.nodeDecorationMap.remove(targetNode);
/*  99 */     if (decorationNodes != null) {
/* 100 */       for (Node decorationNode : decorationNodes) {
/* 101 */         boolean success = getChildren().remove(decorationNode);
/* 102 */         if (!success) {
/* 103 */           throw new IllegalStateException("Could not remove decoration " + decorationNode + " from decoration pane children list: " + 
/*     */               
/* 105 */               getChildren());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 111 */     for (Decoration decoration : decorations) {
/* 112 */       decoration.removeDecoration(targetNode);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addAllDecorationsOnNode(Node targetNode, List<Decoration> decorations) {
/* 117 */     if (decorations == null)
/* 118 */       return;  for (Decoration decoration : decorations)
/* 119 */       showDecoration(targetNode, decoration); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\DecorationPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */