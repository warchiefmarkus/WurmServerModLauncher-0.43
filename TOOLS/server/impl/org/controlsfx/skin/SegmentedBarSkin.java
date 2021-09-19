/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalDouble;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.WeakInvalidationListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.PopOver;
/*     */ import org.controlsfx.control.SegmentedBar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SegmentedBarSkin<T extends SegmentedBar.Segment>
/*     */   extends SkinBase<SegmentedBar<T>>
/*     */ {
/*  42 */   private Map<T, Node> segmentNodes = new HashMap<>();
/*     */   
/*     */   private InvalidationListener buildListener = it -> buildSegments();
/*     */   
/*  46 */   private WeakInvalidationListener weakBuildListener = new WeakInvalidationListener(this.buildListener);
/*     */   
/*     */   private InvalidationListener layoutListener = it -> ((SegmentedBar)getSkinnable()).requestLayout();
/*     */   
/*  50 */   private WeakInvalidationListener weakLayoutListener = new WeakInvalidationListener(this.layoutListener);
/*     */   
/*     */   private PopOver popOver;
/*     */   
/*     */   public SegmentedBarSkin(SegmentedBar<T> bar) {
/*  55 */     super((Control)bar);
/*     */     
/*  57 */     bar.segmentViewFactoryProperty().addListener((InvalidationListener)this.weakBuildListener);
/*  58 */     bar.getSegments().addListener((InvalidationListener)this.weakBuildListener);
/*  59 */     bar.orientationProperty().addListener((InvalidationListener)this.weakLayoutListener);
/*  60 */     bar.totalProperty().addListener((InvalidationListener)this.weakBuildListener);
/*     */     
/*  62 */     bar.orientationProperty().addListener(it -> {
/*     */           if (this.popOver == null) {
/*     */             return;
/*     */           }
/*     */           switch (bar.getOrientation()) {
/*     */             case HORIZONTAL:
/*     */               this.popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
/*     */               break;
/*     */             
/*     */             case VERTICAL:
/*     */               this.popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
/*     */               break;
/*     */           } 
/*     */         });
/*  76 */     buildSegments();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/*  81 */     if (((SegmentedBar)getSkinnable()).getOrientation().equals(Orientation.HORIZONTAL)) {
/*  82 */       OptionalDouble maxHeight = getChildren().stream().mapToDouble(node -> node.prefHeight(-1.0D)).max();
/*  83 */       if (maxHeight.isPresent()) {
/*  84 */         return maxHeight.getAsDouble();
/*     */       }
/*     */     } 
/*     */     
/*  88 */     return ((SegmentedBar)getSkinnable()).getPrefHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/*  93 */     if (((SegmentedBar)getSkinnable()).getOrientation().equals(Orientation.VERTICAL)) {
/*  94 */       OptionalDouble maxWidth = getChildren().stream().mapToDouble(node -> node.prefWidth(height)).max();
/*  95 */       if (maxWidth.isPresent()) {
/*  96 */         return maxWidth.getAsDouble();
/*     */       }
/*     */     } 
/*     */     
/* 100 */     return ((SegmentedBar)getSkinnable()).getPrefWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 105 */     if (((SegmentedBar)getSkinnable()).getOrientation().equals(Orientation.HORIZONTAL)) {
/* 106 */       return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
/*     */     }
/*     */     
/* 109 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 114 */     if (((SegmentedBar)getSkinnable()).getOrientation().equals(Orientation.VERTICAL)) {
/* 115 */       return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
/*     */     }
/*     */     
/* 118 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 123 */     if (((SegmentedBar)getSkinnable()).getOrientation().equals(Orientation.HORIZONTAL)) {
/* 124 */       return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
/*     */     }
/*     */     
/* 127 */     return Double.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 132 */     if (((SegmentedBar)getSkinnable()).getOrientation().equals(Orientation.VERTICAL)) {
/* 133 */       return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
/*     */     }
/*     */     
/* 136 */     return Double.MAX_VALUE;
/*     */   }
/*     */   
/*     */   private void buildSegments() {
/* 140 */     this.segmentNodes.clear();
/* 141 */     getChildren().clear();
/*     */     
/* 143 */     ObservableList<SegmentedBar.Segment> observableList = ((SegmentedBar)getSkinnable()).getSegments();
/* 144 */     int size = observableList.size();
/*     */     
/* 146 */     Callback<T, Node> cellFactory = ((SegmentedBar)getSkinnable()).getSegmentViewFactory();
/*     */     
/* 148 */     for (int i = 0; i < size; i++) {
/* 149 */       SegmentedBar.Segment segment = observableList.get(i);
/* 150 */       Node segmentNode = (Node)cellFactory.call(segment);
/* 151 */       this.segmentNodes.put((T)segment, segmentNode);
/* 152 */       getChildren().add(segmentNode);
/*     */       
/* 154 */       segmentNode.getStyleClass().add("segment");
/*     */       
/* 156 */       if (i == 0) {
/* 157 */         if (size == 1) {
/* 158 */           segmentNode.getStyleClass().add("only-segment");
/*     */         } else {
/* 160 */           segmentNode.getStyleClass().add("first-segment");
/*     */         } 
/* 162 */       } else if (i == size - 1) {
/* 163 */         segmentNode.getStyleClass().add("last-segment");
/*     */       } else {
/* 165 */         segmentNode.getStyleClass().add("middle-segment");
/*     */       } 
/*     */       
/* 168 */       segmentNode.setOnMouseEntered(evt -> showPopOver(segmentNode, (T)segment));
/* 169 */       segmentNode.setOnMouseExited(evt -> hidePopOver());
/*     */     } 
/*     */     
/* 172 */     ((SegmentedBar)getSkinnable()).requestLayout();
/*     */   }
/*     */   
/*     */   private void showPopOver(Node owner, T segment) {
/* 176 */     Callback<T, Node> infoNodeFactory = ((SegmentedBar)getSkinnable()).getInfoNodeFactory();
/*     */     
/* 178 */     Node infoNode = null;
/* 179 */     if (infoNodeFactory != null) {
/* 180 */       infoNode = (Node)infoNodeFactory.call(segment);
/*     */     }
/*     */     
/* 183 */     if (infoNode != null) {
/*     */       
/* 185 */       if (this.popOver == null) {
/* 186 */         this.popOver = new PopOver();
/* 187 */         this.popOver.setAnimated(false);
/* 188 */         this.popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
/* 189 */         this.popOver.setDetachable(false);
/* 190 */         this.popOver.setArrowSize(6.0D);
/* 191 */         this.popOver.setCornerRadius(3.0D);
/* 192 */         this.popOver.setAutoFix(false);
/* 193 */         this.popOver.setAutoHide(true);
/*     */       } 
/*     */       
/* 196 */       this.popOver.setContentNode(infoNode);
/* 197 */       this.popOver.show(owner, -2.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void hidePopOver() {
/* 202 */     if (this.popOver != null && this.popOver.isShowing()) {
/* 203 */       this.popOver.hide();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
/* 209 */     double total = ((SegmentedBar)getSkinnable()).getTotal();
/*     */     
/* 211 */     ObservableList<SegmentedBar.Segment> observableList = ((SegmentedBar)getSkinnable()).getSegments();
/* 212 */     int size = observableList.size();
/*     */     
/* 214 */     double x = contentX;
/* 215 */     double y = contentY + contentHeight;
/*     */     
/* 217 */     for (int i = 0; i < size; i++) {
/* 218 */       SegmentedBar.Segment segment = observableList.get(i);
/* 219 */       Node segmentNode = this.segmentNodes.get(segment);
/* 220 */       double segmentValue = segment.getValue();
/*     */       
/* 222 */       if (((SegmentedBar)getSkinnable()).getOrientation().equals(Orientation.HORIZONTAL)) {
/* 223 */         double segmentWidth = segmentValue / total * contentWidth;
/* 224 */         segmentNode.resizeRelocate(x, contentY, segmentWidth, contentHeight);
/* 225 */         x += segmentWidth;
/*     */       } else {
/* 227 */         double segmentHeight = segmentValue / total * contentHeight;
/* 228 */         segmentNode.resizeRelocate(contentX, y - segmentHeight, contentWidth, segmentHeight);
/* 229 */         y -= segmentHeight;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\SegmentedBarSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */