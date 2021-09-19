/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import com.sun.javafx.scene.traversal.Algorithm;
/*     */ import com.sun.javafx.scene.traversal.Direction;
/*     */ import com.sun.javafx.scene.traversal.ParentTraversalEngine;
/*     */ import com.sun.javafx.scene.traversal.TraversalContext;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.Parent;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.TreeItem;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.Paint;
/*     */ import javafx.scene.shape.ArcTo;
/*     */ import javafx.scene.shape.ClosePath;
/*     */ import javafx.scene.shape.HLineTo;
/*     */ import javafx.scene.shape.LineTo;
/*     */ import javafx.scene.shape.MoveTo;
/*     */ import javafx.scene.shape.Path;
/*     */ import javafx.scene.shape.Shape;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.BreadCrumbBar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BreadCrumbBarSkin<T>
/*     */   extends BehaviorSkinBase<BreadCrumbBar<T>, BehaviorBase<BreadCrumbBar<T>>>
/*     */ {
/*     */   private static final String STYLE_CLASS_FIRST = "first";
/*     */   private final ChangeListener<TreeItem<T>> selectedPathChangeListener;
/*     */   private final EventHandler<TreeItem.TreeModificationEvent<Object>> treeChildrenModifiedHandler;
/*     */   
/*     */   public BreadCrumbBarSkin(BreadCrumbBar<T> control) {
/*  74 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     this.selectedPathChangeListener = ((obs, oldItem, newItem) -> updateSelectedPath(newItem, oldItem));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     this.treeChildrenModifiedHandler = (args -> updateBreadCrumbs()); control.selectedCrumbProperty().addListener(this.selectedPathChangeListener); updateSelectedPath((TreeItem<T>)((BreadCrumbBar)getSkinnable()).selectedCrumbProperty().get(), (TreeItem<T>)null); fixFocusTraversal();
/*     */   }
/*     */   private void fixFocusTraversal() { ParentTraversalEngine engine = new ParentTraversalEngine((Parent)getSkinnable(), new Algorithm() {
/*     */           public Node select(Node owner, Direction dir, TraversalContext context) { Node node = null; int idx = BreadCrumbBarSkin.this.getChildren().indexOf(owner); switch (dir) { case NEXT: case NEXT_IN_LINE: case RIGHT: if (idx < BreadCrumbBarSkin.this.getChildren().size() - 1) node = (Node)BreadCrumbBarSkin.this.getChildren().get(idx + 1);  break;case PREVIOUS: case LEFT: if (idx > 0) node = (Node)BreadCrumbBarSkin.this.getChildren().get(idx - 1);  break; }  return node; } public Node selectFirst(TraversalContext context) { Node first = null; if (!BreadCrumbBarSkin.this.getChildren().isEmpty()) first = (Node)BreadCrumbBarSkin.this.getChildren().get(0);  return first; } public Node selectLast(TraversalContext context) { Node last = null; if (!BreadCrumbBarSkin.this.getChildren().isEmpty())
/*     */               last = (Node)BreadCrumbBarSkin.this.getChildren().get(BreadCrumbBarSkin.this.getChildren().size() - 1);  return last; }
/* 153 */         }); engine.setOverriddenFocusTraversability(Boolean.valueOf(false)); ((BreadCrumbBar)getSkinnable()).setImpl_traversalEngine(engine); } private void updateBreadCrumbs() { BreadCrumbBar<T> buttonBar = (BreadCrumbBar<T>)getSkinnable();
/* 154 */     TreeItem<T> pathTarget = buttonBar.getSelectedCrumb();
/* 155 */     Callback<TreeItem<T>, Button> factory = buttonBar.getCrumbFactory();
/*     */     
/* 157 */     getChildren().clear();
/*     */     
/* 159 */     if (pathTarget != null) {
/* 160 */       List<TreeItem<T>> crumbs = constructFlatPath(pathTarget);
/*     */       
/* 162 */       for (int i = 0; i < crumbs.size(); i++) {
/* 163 */         Button crumb = createCrumb(factory, crumbs.get(i));
/* 164 */         crumb.setMnemonicParsing(false);
/* 165 */         if (i == 0) {
/* 166 */           if (!crumb.getStyleClass().contains("first")) {
/* 167 */             crumb.getStyleClass().add("first");
/*     */           }
/*     */         } else {
/* 170 */           crumb.getStyleClass().remove("first");
/*     */         } 
/*     */         
/* 173 */         getChildren().add(crumb);
/*     */       } 
/*     */     }  }
/*     */   private void updateSelectedPath(TreeItem<T> newTarget, TreeItem<T> oldTarget) { if (oldTarget != null)
/*     */       oldTarget.removeEventHandler(TreeItem.childrenModificationEvent(), this.treeChildrenModifiedHandler);  if (newTarget != null)
/*     */       newTarget.addEventHandler(TreeItem.childrenModificationEvent(), this.treeChildrenModifiedHandler); 
/* 179 */     updateBreadCrumbs(); } protected void layoutChildren(double x, double y, double w, double h) { for (int i = 0; i < getChildren().size(); i++) {
/* 180 */       Node n = (Node)getChildren().get(i);
/*     */       
/* 182 */       double nw = snapSize(n.prefWidth(h));
/* 183 */       double nh = snapSize(n.prefHeight(-1.0D));
/*     */       
/* 185 */       if (i > 0) {
/*     */         
/* 187 */         double ins = (n instanceof BreadCrumbButton) ? ((BreadCrumbButton)n).getArrowWidth() : 0.0D;
/* 188 */         x = snapPosition(x - ins);
/*     */       } 
/*     */       
/* 191 */       n.resize(nw, nh);
/* 192 */       n.relocate(x, y);
/* 193 */       x += nw;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<TreeItem<T>> constructFlatPath(TreeItem<T> bottomMost) {
/* 203 */     List<TreeItem<T>> path = new ArrayList<>();
/*     */     
/* 205 */     TreeItem<T> current = bottomMost;
/*     */     do {
/* 207 */       path.add(current);
/* 208 */       current = current.getParent();
/* 209 */     } while (current != null);
/*     */     
/* 211 */     Collections.reverse(path);
/* 212 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Button createCrumb(Callback<TreeItem<T>, Button> factory, TreeItem<T> selectedCrumb) {
/* 219 */     Button crumb = (Button)factory.call(selectedCrumb);
/*     */     
/* 221 */     crumb.getStyleClass().add("crumb");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     crumb.setOnAction(ae -> onBreadCrumbAction(selectedCrumb));
/*     */     
/* 230 */     return crumb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onBreadCrumbAction(TreeItem<T> crumbModel) {
/* 239 */     BreadCrumbBar<T> breadCrumbBar = (BreadCrumbBar<T>)getSkinnable();
/*     */ 
/*     */     
/* 242 */     Event.fireEvent((EventTarget)breadCrumbBar, (Event)new BreadCrumbBar.BreadCrumbActionEvent(crumbModel));
/*     */ 
/*     */     
/* 245 */     if (breadCrumbBar.isAutoNavigationEnabled()) {
/* 246 */       breadCrumbBar.setSelectedCrumb(crumbModel);
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
/*     */   
/*     */   public static class BreadCrumbButton
/*     */     extends Button
/*     */   {
/* 267 */     private final ObjectProperty<Boolean> first = (ObjectProperty<Boolean>)new SimpleObjectProperty(this, "first");
/*     */     
/* 269 */     private final double arrowWidth = 5.0D;
/* 270 */     private final double arrowHeight = 20.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BreadCrumbButton(String text) {
/* 278 */       this(text, (Node)null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BreadCrumbButton(String text, Node gfx) {
/* 287 */       super(text, gfx);
/* 288 */       this.first.set(Boolean.valueOf(false));
/*     */       
/* 290 */       getStyleClass().addListener(new InvalidationListener() {
/*     */             public void invalidated(Observable arg0) {
/* 292 */               BreadCrumbBarSkin.BreadCrumbButton.this.updateShape();
/*     */             }
/*     */           });
/*     */       
/* 296 */       updateShape();
/*     */     }
/*     */     
/*     */     private void updateShape() {
/* 300 */       setShape((Shape)createButtonShape());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getArrowWidth() {
/* 309 */       return 5.0D;
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
/*     */     private Path createButtonShape() {
/* 326 */       Path path = new Path();
/*     */ 
/*     */       
/* 329 */       MoveTo e1 = new MoveTo(0.0D, 0.0D);
/* 330 */       path.getElements().add(e1);
/*     */ 
/*     */       
/* 333 */       HLineTo e2 = new HLineTo();
/*     */       
/* 335 */       e2.xProperty().bind((ObservableValue)widthProperty().subtract(5.0D));
/* 336 */       path.getElements().add(e2);
/*     */ 
/*     */       
/* 339 */       LineTo e3 = new LineTo();
/*     */       
/* 341 */       e3.xProperty().bind((ObservableValue)e2.xProperty().add(5.0D));
/* 342 */       e3.setY(10.0D);
/* 343 */       path.getElements().add(e3);
/*     */ 
/*     */       
/* 346 */       LineTo e4 = new LineTo();
/*     */       
/* 348 */       e4.xProperty().bind((ObservableValue)e2.xProperty());
/* 349 */       e4.setY(20.0D);
/* 350 */       path.getElements().add(e4);
/*     */ 
/*     */       
/* 353 */       HLineTo e5 = new HLineTo(0.0D);
/* 354 */       path.getElements().add(e5);
/*     */       
/* 356 */       if (!getStyleClass().contains("first")) {
/*     */ 
/*     */         
/* 359 */         LineTo e6 = new LineTo(5.0D, 10.0D);
/* 360 */         path.getElements().add(e6);
/*     */       } else {
/*     */         
/* 363 */         ArcTo arcTo = new ArcTo();
/* 364 */         arcTo.setSweepFlag(true);
/* 365 */         arcTo.setX(0.0D);
/* 366 */         arcTo.setY(0.0D);
/* 367 */         arcTo.setRadiusX(15.0D);
/* 368 */         arcTo.setRadiusY(15.0D);
/* 369 */         path.getElements().add(arcTo);
/*     */       } 
/*     */ 
/*     */       
/* 373 */       ClosePath e7 = new ClosePath();
/* 374 */       path.getElements().add(e7);
/*     */       
/* 376 */       path.setFill((Paint)Color.BLACK);
/*     */       
/* 378 */       return path;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\BreadCrumbBarSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */