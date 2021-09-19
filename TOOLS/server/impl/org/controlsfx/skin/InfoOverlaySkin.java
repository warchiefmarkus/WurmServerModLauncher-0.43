/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import java.util.Collections;
/*     */ import javafx.animation.Animation;
/*     */ import javafx.animation.Interpolator;
/*     */ import javafx.animation.KeyFrame;
/*     */ import javafx.animation.KeyValue;
/*     */ import javafx.animation.Timeline;
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableBooleanValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WritableValue;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.HPos;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.VPos;
/*     */ import javafx.scene.Cursor;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.ToggleButton;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.HBox;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.InfoOverlay;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InfoOverlaySkin
/*     */   extends BehaviorSkinBase<InfoOverlay, BehaviorBase<InfoOverlay>>
/*     */ {
/*  65 */   private final ImageView EXPAND_IMAGE = new ImageView(new Image(InfoOverlay.class.getResource("expand.png").toExternalForm()));
/*  66 */   private final ImageView COLLAPSE_IMAGE = new ImageView(new Image(InfoOverlay.class.getResource("collapse.png").toExternalForm()));
/*     */   
/*  68 */   private static final Duration TRANSITION_DURATION = new Duration(350.0D);
/*     */   
/*     */   private Node content;
/*     */   
/*     */   private Label infoLabel;
/*     */   private HBox infoPanel;
/*     */   private ToggleButton expandCollapseButton;
/*     */   private Timeline timeline;
/*     */   
/*  77 */   private DoubleProperty transition = (DoubleProperty)new SimpleDoubleProperty(this, "transition", 0.0D) {
/*     */       protected void invalidated() {
/*  79 */         ((InfoOverlay)InfoOverlaySkin.this.getSkinnable()).requestLayout();
/*     */       }
/*     */     };
/*     */   
/*     */   public InfoOverlaySkin(final InfoOverlay control) {
/*  84 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */ 
/*     */     
/*  87 */     this.content = control.getContent();
/*  88 */     control.hoverProperty().addListener(new ChangeListener<Boolean>() {
/*     */           public void changed(ObservableValue<? extends Boolean> o, Boolean wasHover, Boolean isHover) {
/*  90 */             if (control.isShowOnHover() && ((
/*  91 */               isHover.booleanValue() && !InfoOverlaySkin.this.isExpanded()) || (!isHover.booleanValue() && InfoOverlaySkin.this.isExpanded()))) {
/*  92 */               InfoOverlaySkin.this.doToggle();
/*     */             }
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/*  99 */     this.infoLabel = new Label();
/* 100 */     this.infoLabel.setWrapText(true);
/* 101 */     this.infoLabel.setAlignment(Pos.TOP_LEFT);
/* 102 */     this.infoLabel.getStyleClass().add("info");
/* 103 */     this.infoLabel.textProperty().bind((ObservableValue)control.textProperty());
/*     */ 
/*     */     
/* 106 */     this.expandCollapseButton = new ToggleButton();
/* 107 */     this.expandCollapseButton.setMouseTransparent(true);
/* 108 */     this.expandCollapseButton.visibleProperty().bind((ObservableValue)Bindings.not((ObservableBooleanValue)control.showOnHoverProperty()));
/* 109 */     this.expandCollapseButton.managedProperty().bind((ObservableValue)Bindings.not((ObservableBooleanValue)control.showOnHoverProperty()));
/* 110 */     updateToggleButton();
/*     */ 
/*     */     
/* 113 */     this.infoPanel = new HBox(new Node[] { (Node)this.infoLabel, (Node)this.expandCollapseButton });
/* 114 */     this.infoPanel.setAlignment(Pos.TOP_LEFT);
/* 115 */     this.infoPanel.setFillHeight(true);
/* 116 */     this.infoPanel.getStyleClass().add("info-panel");
/* 117 */     this.infoPanel.setCursor(Cursor.HAND);
/* 118 */     this.infoPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent e) {
/* 120 */             if (!control.isShowOnHover()) {
/* 121 */               InfoOverlaySkin.this.doToggle();
/*     */             }
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 127 */     getChildren().addAll((Object[])new Node[] { this.content, (Node)this.infoPanel });
/*     */     
/* 129 */     registerChangeListener((ObservableValue)control.contentProperty(), "CONTENT");
/*     */   }
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/* 133 */     super.handleControlPropertyChanged(p);
/*     */     
/* 135 */     if ("CONTENT".equals(p)) {
/* 136 */       getChildren().remove(0);
/* 137 */       getChildren().add(0, ((InfoOverlay)getSkinnable()).getContent());
/* 138 */       ((InfoOverlay)getSkinnable()).requestLayout();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doToggle() {
/* 144 */     this.expandCollapseButton.setSelected(!this.expandCollapseButton.isSelected());
/* 145 */     toggleInfoPanel();
/* 146 */     updateToggleButton();
/*     */   }
/*     */   
/*     */   private boolean isExpanded() {
/* 150 */     return this.expandCollapseButton.isSelected();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
/* 155 */     double contentPrefHeight = this.content.prefHeight(contentWidth);
/*     */ 
/*     */ 
/*     */     
/* 159 */     double toggleButtonPrefWidth = this.expandCollapseButton.prefWidth(-1.0D);
/* 160 */     this.expandCollapseButton.setMinWidth(toggleButtonPrefWidth);
/*     */ 
/*     */     
/* 163 */     Insets infoPanelPadding = this.infoPanel.getPadding();
/* 164 */     double infoLabelWidth = snapSize(contentWidth - toggleButtonPrefWidth - infoPanelPadding
/* 165 */         .getLeft() - infoPanelPadding.getRight());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     double prefInfoPanelHeight = (snapSize(this.infoLabel.prefHeight(infoLabelWidth)) + snapSpace(this.infoPanel.getPadding().getTop()) + snapSpace(this.infoPanel.getPadding().getBottom())) * this.transition.get();
/*     */     
/* 174 */     this.infoLabel.setMaxWidth(infoLabelWidth);
/* 175 */     this.infoLabel.setMaxHeight(prefInfoPanelHeight);
/*     */ 
/*     */     
/* 178 */     layoutInArea(this.content, contentX, contentY, contentWidth, contentHeight, -1.0D, HPos.CENTER, VPos.TOP);
/*     */ 
/*     */ 
/*     */     
/* 182 */     layoutInArea((Node)this.infoPanel, contentX, snapPosition(contentPrefHeight - prefInfoPanelHeight), contentWidth, prefInfoPanelHeight, 0.0D, HPos.CENTER, VPos.BOTTOM);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateToggleButton() {
/* 187 */     if (this.expandCollapseButton.isSelected()) {
/* 188 */       this.expandCollapseButton.getStyleClass().setAll((Object[])new String[] { "collapse-button" });
/* 189 */       this.expandCollapseButton.setGraphic((Node)this.COLLAPSE_IMAGE);
/*     */     } else {
/* 191 */       this.expandCollapseButton.getStyleClass().setAll((Object[])new String[] { "expand-button" });
/* 192 */       this.expandCollapseButton.setGraphic((Node)this.EXPAND_IMAGE);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 197 */     double insets = topInset + bottomInset;
/* 198 */     return insets + ((this.content == null) ? 0.0D : this.content.prefHeight(width));
/*     */   }
/*     */   
/*     */   protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 202 */     double insets = leftInset + rightInset;
/* 203 */     return insets + ((this.content == null) ? 0.0D : this.content.prefWidth(height));
/*     */   }
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 207 */     return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
/*     */   }
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 211 */     return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void toggleInfoPanel() {
/*     */     Duration duration;
/*     */     KeyFrame k1, k2;
/* 220 */     if (this.content == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 225 */     if (this.timeline != null && this.timeline.getStatus() != Animation.Status.STOPPED) {
/* 226 */       duration = this.timeline.getCurrentTime();
/* 227 */       this.timeline.stop();
/*     */     } else {
/* 229 */       duration = TRANSITION_DURATION;
/*     */     } 
/*     */     
/* 232 */     this.timeline = new Timeline();
/* 233 */     this.timeline.setCycleCount(1);
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (isExpanded()) {
/* 238 */       k1 = new KeyFrame(Duration.ZERO, new KeyValue[] { new KeyValue((WritableValue)this.transition, Integer.valueOf(0)) });
/* 239 */       k2 = new KeyFrame(duration, new KeyValue[] { new KeyValue((WritableValue)this.transition, Integer.valueOf(1), Interpolator.LINEAR) });
/*     */     } else {
/* 241 */       k1 = new KeyFrame(Duration.ZERO, new KeyValue[] { new KeyValue((WritableValue)this.transition, Integer.valueOf(1)) });
/* 242 */       k2 = new KeyFrame(duration, new KeyValue[] { new KeyValue((WritableValue)this.transition, Integer.valueOf(0), Interpolator.LINEAR) });
/*     */     } 
/*     */     
/* 245 */     this.timeline.getKeyFrames().setAll((Object[])new KeyFrame[] { k1, k2 });
/* 246 */     this.timeline.play();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\InfoOverlaySkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */