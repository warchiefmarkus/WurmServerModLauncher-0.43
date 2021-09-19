/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import impl.org.controlsfx.behavior.SnapshotViewBehavior;
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.binding.BooleanBinding;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ReadOnlyBooleanProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableBooleanValue;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.geometry.Bounds;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.Rectangle2D;
/*     */ import javafx.scene.Cursor;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.GridPane;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.Paint;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import javafx.scene.shape.StrokeType;
/*     */ import org.controlsfx.control.SnapshotView;
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
/*     */ public class SnapshotViewSkin
/*     */   extends BehaviorSkinBase<SnapshotView, SnapshotViewBehavior>
/*     */ {
/*     */   private Node node;
/*     */   private final GridPane gridPane;
/*     */   private final Rectangle selectedArea;
/*     */   private final Rectangle unselectedArea;
/*     */   private final Node mouseNode;
/*     */   
/*     */   public SnapshotViewSkin(SnapshotView snapshotView) {
/* 104 */     super((Control)snapshotView, (BehaviorBase)new SnapshotViewBehavior(snapshotView));
/*     */     
/* 106 */     this.gridPane = createGridPane();
/* 107 */     this.selectedArea = new Rectangle();
/* 108 */     this.unselectedArea = new Rectangle();
/* 109 */     this.mouseNode = createMouseNode();
/*     */     
/* 111 */     buildSceneGraph();
/* 112 */     initializeAreas();
/*     */     
/* 114 */     registerChangeListener((ObservableValue)snapshotView.nodeProperty(), "NODE");
/* 115 */     registerChangeListener((ObservableValue)snapshotView.selectionProperty(), "SELECTION");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/* 120 */     super.handleControlPropertyChanged(p);
/*     */     
/* 122 */     if ("NODE".equals(p)) {
/* 123 */       updateNode();
/* 124 */     } else if ("SELECTION".equals(p)) {
/* 125 */       updateSelection();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static GridPane createGridPane() {
/* 135 */     GridPane pane = new GridPane();
/* 136 */     pane.setAlignment(Pos.CENTER);
/* 137 */     return pane;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Node createMouseNode() {
/* 147 */     Rectangle mouseNode = new Rectangle();
/*     */ 
/*     */     
/* 150 */     mouseNode.setFill((Paint)Color.TRANSPARENT);
/* 151 */     mouseNode.setManaged(false);
/*     */ 
/*     */     
/* 154 */     mouseNode.widthProperty().bind((ObservableValue)((SnapshotView)getSkinnable()).widthProperty());
/* 155 */     mouseNode.heightProperty().bind((ObservableValue)((SnapshotView)getSkinnable()).heightProperty());
/*     */ 
/*     */     
/* 158 */     mouseNode.addEventHandler(MouseEvent.ANY, this::handleMouseEvent);
/* 159 */     mouseNode.mouseTransparentProperty().bind((ObservableValue)((SnapshotView)getSkinnable()).selectionMouseTransparentProperty());
/*     */     
/* 161 */     return (Node)mouseNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildSceneGraph() {
/* 168 */     getChildren().addAll((Object[])new Node[] { (Node)this.gridPane, (Node)this.unselectedArea, (Node)this.selectedArea, this.mouseNode });
/* 169 */     updateNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeAreas() {
/* 177 */     styleAreas();
/* 178 */     bindAreaCoordinatesTogether();
/* 179 */     bindAreaVisibilityToSelection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void styleAreas() {
/* 186 */     this.selectedArea.fillProperty().bind((ObservableValue)((SnapshotView)getSkinnable()).selectionAreaFillProperty());
/* 187 */     this.selectedArea.strokeProperty().bind((ObservableValue)((SnapshotView)getSkinnable()).selectionBorderPaintProperty());
/* 188 */     this.selectedArea.strokeWidthProperty().bind((ObservableValue)((SnapshotView)getSkinnable()).selectionBorderWidthProperty());
/* 189 */     this.selectedArea.setStrokeType(StrokeType.OUTSIDE);
/*     */ 
/*     */     
/* 192 */     this.selectedArea.setManaged(false);
/* 193 */     this.selectedArea.setMouseTransparent(true);
/*     */     
/* 195 */     this.unselectedArea.setFill((Paint)Color.TRANSPARENT);
/* 196 */     this.unselectedArea.strokeProperty().bind((ObservableValue)((SnapshotView)getSkinnable()).unselectedAreaFillProperty());
/* 197 */     this.unselectedArea.strokeWidthProperty().bind(
/* 198 */         (ObservableValue)Bindings.max((ObservableNumberValue)((SnapshotView)getSkinnable()).widthProperty(), (ObservableNumberValue)((SnapshotView)getSkinnable()).heightProperty()));
/* 199 */     this.unselectedArea.setStrokeType(StrokeType.OUTSIDE);
/*     */     
/* 201 */     this.unselectedArea.setManaged(false);
/* 202 */     this.unselectedArea.setMouseTransparent(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindAreaCoordinatesTogether() {
/* 209 */     this.unselectedArea.xProperty().bind((ObservableValue)this.selectedArea.xProperty());
/* 210 */     this.unselectedArea.yProperty().bind((ObservableValue)this.selectedArea.yProperty());
/* 211 */     this.unselectedArea.widthProperty().bind((ObservableValue)this.selectedArea.widthProperty());
/* 212 */     this.unselectedArea.heightProperty().bind((ObservableValue)this.selectedArea.heightProperty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindAreaVisibilityToSelection() {
/* 222 */     ReadOnlyBooleanProperty selectionExists = ((SnapshotView)getSkinnable()).hasSelectionProperty();
/* 223 */     BooleanProperty booleanProperty = ((SnapshotView)getSkinnable()).selectionActiveProperty();
/* 224 */     BooleanBinding existsAndActive = Bindings.and((ObservableBooleanValue)selectionExists, (ObservableBooleanValue)booleanProperty);
/*     */     
/* 226 */     this.selectedArea.visibleProperty().bind((ObservableValue)existsAndActive);
/* 227 */     this.unselectedArea.visibleProperty().bind((ObservableValue)existsAndActive);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 234 */     new Clipper((SnapshotView)getSkinnable(), (Node)this.unselectedArea, () -> this.unselectedArea.visibleProperty().bind((ObservableValue)existsAndActive));
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
/*     */   private void updateNode() {
/* 247 */     if (this.node != null) {
/* 248 */       this.gridPane.getChildren().remove(this.node);
/*     */     }
/*     */     
/* 251 */     this.node = ((SnapshotView)getSkinnable()).getNode();
/* 252 */     if (this.node != null) {
/* 253 */       this.gridPane.getChildren().add(0, this.node);
/*     */     }
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
/*     */   
/*     */   private void updateSelection() {
/* 268 */     boolean showSelection = (((SnapshotView)getSkinnable()).hasSelection() && ((SnapshotView)getSkinnable()).isSelectionActive());
/*     */     
/* 270 */     if (showSelection) {
/*     */       
/* 272 */       Rectangle2D selection = ((SnapshotView)getSkinnable()).getSelection();
/* 273 */       setSelection(selection.getMinX(), selection.getMinY(), selection.getWidth(), selection.getHeight());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 278 */       setSelection(0.0D, 0.0D, 0.0D, 0.0D);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setSelection(double x, double y, double width, double height) {
/* 296 */     this.selectedArea.setX(x);
/* 297 */     this.selectedArea.setY(y);
/* 298 */     this.selectedArea.setWidth(width);
/* 299 */     this.selectedArea.setHeight(height);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleMouseEvent(MouseEvent event) {
/* 315 */     Cursor newCursor = ((SnapshotViewBehavior)getBehavior()).handleMouseEvent(event);
/* 316 */     this.mouseNode.setCursor(newCursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Clipper
/*     */   {
/*     */     private final SnapshotView snapshotView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Node clippedNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Runnable rebindClippedNodeVisibility;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Rectangle controlClip;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Rectangle nodeClip;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final ChangeListener<Bounds> updateControlClipToNewBoundsListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final ChangeListener<Bounds> updateNodeClipToNewBoundsListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Clipper(SnapshotView snapshotView, Node clippedNode, Runnable rebindClippedNodeVisibility) {
/* 381 */       this.snapshotView = snapshotView;
/* 382 */       this.clippedNode = clippedNode;
/* 383 */       this.rebindClippedNodeVisibility = rebindClippedNodeVisibility;
/*     */ 
/*     */       
/* 386 */       this.controlClip = new Rectangle();
/* 387 */       this.updateControlClipToNewBoundsListener = ((o, oldBounds, newBounds) -> resizeRectangleToBounds(this.controlClip, newBounds));
/*     */ 
/*     */ 
/*     */       
/* 391 */       this.nodeClip = new Rectangle();
/*     */       
/* 393 */       this.updateNodeClipToNewBoundsListener = ((o, oldBounds, newBounds) -> resizeRectangleToBounds(this.nodeClip, newBounds));
/*     */ 
/*     */ 
/*     */       
/* 397 */       setClipping();
/* 398 */       snapshotView.unselectedAreaBoundaryProperty().addListener((o, oldBoundary, newBoundary) -> setClipping());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void setClipping() {
/* 405 */       SnapshotView.Boundary boundary = this.snapshotView.getUnselectedAreaBoundary();
/* 406 */       switch (boundary) {
/*     */         case CONTROL:
/* 408 */           clipToControl();
/*     */           return;
/*     */         case NODE:
/* 411 */           clipToNode();
/*     */           return;
/*     */       } 
/* 414 */       throw new IllegalArgumentException("The boundary " + boundary + " is not fully implemented.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void clipToControl() {
/* 424 */       updateNodeClipToChangingNode((ObservableValue<? extends Node>)this.snapshotView.nodeProperty(), this.snapshotView.getNode(), null);
/*     */ 
/*     */       
/* 427 */       resizeRectangleToBounds(this.controlClip, this.snapshotView.getBoundsInLocal());
/* 428 */       this.snapshotView.boundsInLocalProperty().addListener(this.updateControlClipToNewBoundsListener);
/*     */ 
/*     */       
/* 431 */       setClip((Node)this.controlClip);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void clipToNode() {
/* 440 */       updateNodeClipToChangingNode((ObservableValue<? extends Node>)this.snapshotView.nodeProperty(), null, this.snapshotView.getNode());
/*     */       
/* 442 */       this.snapshotView.nodeProperty().addListener(this::updateNodeClipToChangingNode);
/*     */ 
/*     */       
/* 445 */       setClip((Node)this.nodeClip);
/*     */     }
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
/*     */     private void updateNodeClipToChangingNode(ObservableValue<? extends Node> o, Node oldNode, Node newNode) {
/* 466 */       resizeRectangleToNodeBounds(this.nodeClip, newNode);
/*     */ 
/*     */       
/* 469 */       if (oldNode != null) {
/* 470 */         oldNode.boundsInParentProperty().removeListener(this.updateNodeClipToNewBoundsListener);
/*     */       }
/* 472 */       if (newNode != null) {
/* 473 */         newNode.boundsInParentProperty().addListener(this.updateNodeClipToNewBoundsListener);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static void resizeRectangleToNodeBounds(Rectangle rectangle, Node node) {
/* 486 */       if (node == null) {
/* 487 */         resizeRectangleToZero(rectangle);
/*     */       } else {
/* 489 */         resizeRectangleToBounds(rectangle, node.getBoundsInParent());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static void resizeRectangleToZero(Rectangle rectangle) {
/* 501 */       rectangle.setX(0.0D);
/* 502 */       rectangle.setY(0.0D);
/* 503 */       rectangle.setWidth(0.0D);
/* 504 */       rectangle.setHeight(0.0D);
/*     */     }
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
/*     */     private static void resizeRectangleToBounds(Rectangle rectangle, Bounds bounds) {
/* 517 */       rectangle.setX(bounds.getMinX());
/* 518 */       rectangle.setY(bounds.getMinY());
/* 519 */       rectangle.setWidth(bounds.getWidth());
/* 520 */       rectangle.setHeight(bounds.getHeight());
/*     */     }
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
/*     */     private void setClip(Node clip) {
/* 551 */       boolean workAroundVisibilityProblem = !this.clippedNode.isVisible();
/* 552 */       if (workAroundVisibilityProblem) {
/* 553 */         this.clippedNode.visibleProperty().unbind();
/* 554 */         this.clippedNode.setVisible(true);
/*     */       } 
/*     */       
/* 557 */       this.clippedNode.setClip(clip);
/*     */       
/* 559 */       if (workAroundVisibilityProblem)
/* 560 */         this.rebindClippedNodeVisibility.run(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\SnapshotViewSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */