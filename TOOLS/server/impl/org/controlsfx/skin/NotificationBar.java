/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javafx.animation.Animation;
/*     */ import javafx.animation.Interpolator;
/*     */ import javafx.animation.KeyFrame;
/*     */ import javafx.animation.KeyValue;
/*     */ import javafx.animation.Timeline;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WritableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.VPos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.ButtonBar;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.layout.GridPane;
/*     */ import javafx.scene.layout.Priority;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.NotificationPane;
/*     */ import org.controlsfx.control.action.Action;
/*     */ import org.controlsfx.control.action.ActionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NotificationBar
/*     */   extends Region
/*     */ {
/*     */   private static final double MIN_HEIGHT = 40.0D;
/*     */   final Label label;
/*     */   Label title;
/*     */   ButtonBar actionsBar;
/*     */   Button closeBtn;
/*     */   private final GridPane pane;
/*     */   
/*  71 */   public DoubleProperty transition = (DoubleProperty)new SimpleDoubleProperty() {
/*     */       protected void invalidated() {
/*  73 */         NotificationBar.this.requestContainerLayout();
/*     */       }
/*     */     };
/*     */   private final Duration TRANSITION_DURATION; private Timeline timeline; private double transitionStartValue;
/*     */   
/*     */   public void requestContainerLayout() {
/*  79 */     layoutChildren();
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  83 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isCloseButtonVisible() {
/*  87 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void updatePane() {
/* 165 */     this.actionsBar = ActionUtils.createButtonBar((Collection)getActions());
/* 166 */     this.actionsBar.opacityProperty().bind((ObservableValue)this.transition);
/* 167 */     GridPane.setHgrow((Node)this.actionsBar, Priority.SOMETIMES);
/* 168 */     this.pane.getChildren().clear();
/*     */     
/* 170 */     int row = 0;
/*     */     
/* 172 */     if (this.title != null) {
/* 173 */       this.pane.add((Node)this.title, 0, row++);
/*     */     }
/*     */     
/* 176 */     this.pane.add((Node)this.label, 0, row);
/* 177 */     this.pane.add((Node)this.actionsBar, 1, row);
/*     */     
/* 179 */     if (isCloseButtonVisible()) {
/* 180 */       this.pane.add((Node)this.closeBtn, 2, 0, 1, row + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void layoutChildren() {
/* 185 */     double w = getWidth();
/* 186 */     double h = computePrefHeight(-1.0D);
/*     */     
/* 188 */     double notificationBarHeight = prefHeight(w);
/* 189 */     double notificationMinHeight = minHeight(w);
/*     */     
/* 191 */     if (isShowFromTop()) {
/*     */       
/* 193 */       this.pane.resize(w, h);
/* 194 */       relocateInParent(0.0D, (this.transition.get() - 1.0D) * notificationMinHeight);
/*     */     } else {
/*     */       
/* 197 */       this.pane.resize(w, notificationBarHeight);
/* 198 */       relocateInParent(0.0D, getContainerHeight() - notificationBarHeight);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected double computeMinHeight(double width) {
/* 203 */     return Math.max(super.computePrefHeight(width), 40.0D);
/*     */   }
/*     */   
/*     */   protected double computePrefHeight(double width) {
/* 207 */     return Math.max(this.pane.prefHeight(width), minHeight(width)) * this.transition.get();
/*     */   }
/*     */   
/*     */   public void doShow() {
/* 211 */     this.transitionStartValue = 0.0D;
/* 212 */     doAnimationTransition();
/*     */   }
/*     */   
/*     */   public void doHide() {
/* 216 */     this.transitionStartValue = 1.0D;
/* 217 */     doAnimationTransition();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NotificationBar() {
/* 223 */     this.TRANSITION_DURATION = new Duration(350.0D); getStyleClass().add("notification-bar"); setVisible(isShowing()); this.pane = new GridPane(); this.pane.getStyleClass().add("pane"); this.pane.setAlignment(Pos.BASELINE_LEFT); getChildren().setAll((Object[])new Node[] { (Node)this.pane }); String titleStr = getTitle(); if (titleStr != null && !titleStr.isEmpty()) {
/*     */       this.title = new Label(); this.title.getStyleClass().add("title"); this.title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); GridPane.setHgrow((Node)this.title, Priority.ALWAYS); this.title.setText(titleStr); this.title.opacityProperty().bind((ObservableValue)this.transition);
/*     */     }  this.label = new Label(); this.label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); GridPane.setVgrow((Node)this.label, Priority.ALWAYS); GridPane.setHgrow((Node)this.label, Priority.ALWAYS); this.label.setText(getText()); this.label.setGraphic(getGraphic()); this.label.opacityProperty().bind((ObservableValue)this.transition); getActions().addListener(new InvalidationListener() { public void invalidated(Observable arg0) { NotificationBar.this.updatePane(); } }
/*     */       ); this.closeBtn = new Button(); this.closeBtn.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent arg0) { NotificationBar.this.hide(); } }); this.closeBtn.getStyleClass().setAll((Object[])new String[] { "close-button" }); StackPane graphic = new StackPane(); graphic.getStyleClass().setAll((Object[])new String[] { "graphic" }); this.closeBtn.setGraphic((Node)graphic); this.closeBtn.setMinSize(17.0D, 17.0D); this.closeBtn.setPrefSize(17.0D, 17.0D); this.closeBtn.setFocusTraversable(false); this.closeBtn.opacityProperty().bind((ObservableValue)this.transition); GridPane.setMargin((Node)this.closeBtn, new Insets(0.0D, 0.0D, 0.0D, 8.0D)); double minHeight = minHeight(-1.0D); GridPane.setValignment((Node)this.closeBtn, (minHeight == 40.0D) ? VPos.CENTER : VPos.TOP); updatePane();
/*     */   } private void doAnimationTransition() { Duration duration;
/*     */     KeyFrame k1, k2;
/* 229 */     if (this.timeline != null && this.timeline.getStatus() != Animation.Status.STOPPED) {
/* 230 */       duration = this.timeline.getCurrentTime();
/*     */ 
/*     */ 
/*     */       
/* 234 */       duration = (duration == Duration.ZERO) ? this.TRANSITION_DURATION : duration;
/* 235 */       this.transitionStartValue = this.transition.get();
/*     */ 
/*     */       
/* 238 */       this.timeline.stop();
/*     */     } else {
/* 240 */       duration = this.TRANSITION_DURATION;
/*     */     } 
/*     */     
/* 243 */     this.timeline = new Timeline();
/* 244 */     this.timeline.setCycleCount(1);
/*     */ 
/*     */ 
/*     */     
/* 248 */     if (isShowing()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 260 */       k1 = new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>() { public void handle(ActionEvent event) { NotificationBar.this.setCache(true); NotificationBar.this.setVisible(true); NotificationBar.this.pane.fireEvent(new Event(NotificationPane.ON_SHOWING)); } }, new KeyValue[] { new KeyValue((WritableValue)this.transition, Double.valueOf(this.transitionStartValue)) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 273 */       k2 = new KeyFrame(duration, new EventHandler<ActionEvent>() { public void handle(ActionEvent event) { NotificationBar.this.pane.setCache(false); NotificationBar.this.pane.fireEvent(new Event(NotificationPane.ON_SHOWN)); } }, new KeyValue[] { new KeyValue((WritableValue)this.transition, Integer.valueOf(1), Interpolator.EASE_OUT) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 287 */       k1 = new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>() { public void handle(ActionEvent event) { NotificationBar.this.pane.setCache(true); NotificationBar.this.pane.fireEvent(new Event(NotificationPane.ON_HIDING)); } }, new KeyValue[] { new KeyValue((WritableValue)this.transition, Double.valueOf(this.transitionStartValue)) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 301 */       k2 = new KeyFrame(duration, new EventHandler<ActionEvent>() { public void handle(ActionEvent event) { NotificationBar.this.setCache(false); NotificationBar.this.setVisible(false); NotificationBar.this.pane.fireEvent(new Event(NotificationPane.ON_HIDDEN)); } }, new KeyValue[] { new KeyValue((WritableValue)this.transition, Integer.valueOf(0), Interpolator.EASE_IN) });
/*     */     } 
/*     */ 
/*     */     
/* 305 */     this.timeline.getKeyFrames().setAll((Object[])new KeyFrame[] { k1, k2 });
/* 306 */     this.timeline.play(); }
/*     */ 
/*     */   
/*     */   public abstract String getText();
/*     */   
/*     */   public abstract Node getGraphic();
/*     */   
/*     */   public abstract ObservableList<Action> getActions();
/*     */   
/*     */   public abstract void hide();
/*     */   
/*     */   public abstract boolean isShowing();
/*     */   
/*     */   public abstract boolean isShowFromTop();
/*     */   
/*     */   public abstract double getContainerHeight();
/*     */   
/*     */   public abstract void relocateInParent(double paramDouble1, double paramDouble2);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\NotificationBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */