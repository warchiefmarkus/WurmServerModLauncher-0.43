/*      */ package org.controlsfx.control;
/*      */ 
/*      */ import impl.org.controlsfx.i18n.Localization;
/*      */ import impl.org.controlsfx.skin.PopOverSkin;
/*      */ import java.util.Objects;
/*      */ import javafx.animation.FadeTransition;
/*      */ import javafx.beans.InvalidationListener;
/*      */ import javafx.beans.Observable;
/*      */ import javafx.beans.WeakInvalidationListener;
/*      */ import javafx.beans.property.BooleanProperty;
/*      */ import javafx.beans.property.DoubleProperty;
/*      */ import javafx.beans.property.ObjectProperty;
/*      */ import javafx.beans.property.SimpleBooleanProperty;
/*      */ import javafx.beans.property.SimpleDoubleProperty;
/*      */ import javafx.beans.property.SimpleObjectProperty;
/*      */ import javafx.beans.property.SimpleStringProperty;
/*      */ import javafx.beans.property.StringProperty;
/*      */ import javafx.beans.value.ChangeListener;
/*      */ import javafx.beans.value.ObservableValue;
/*      */ import javafx.beans.value.WeakChangeListener;
/*      */ import javafx.event.ActionEvent;
/*      */ import javafx.event.Event;
/*      */ import javafx.event.EventHandler;
/*      */ import javafx.event.WeakEventHandler;
/*      */ import javafx.geometry.Bounds;
/*      */ import javafx.geometry.Insets;
/*      */ import javafx.scene.Node;
/*      */ import javafx.scene.control.Label;
/*      */ import javafx.scene.control.PopupControl;
/*      */ import javafx.scene.control.Skin;
/*      */ import javafx.scene.input.MouseEvent;
/*      */ import javafx.scene.layout.StackPane;
/*      */ import javafx.stage.PopupWindow;
/*      */ import javafx.stage.Window;
/*      */ import javafx.stage.WindowEvent;
/*      */ import javafx.util.Duration;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PopOver
/*      */   extends PopupControl
/*      */ {
/*      */   private static final String DEFAULT_STYLE_CLASS = "popover";
/*   89 */   private static final Duration DEFAULT_FADE_DURATION = Duration.seconds(0.2D);
/*      */   
/*      */   private double targetX;
/*      */   
/*      */   private double targetY;
/*      */   
/*   95 */   private final SimpleBooleanProperty animated = new SimpleBooleanProperty(true);
/*   96 */   private final ObjectProperty<Duration> fadeInDuration = (ObjectProperty<Duration>)new SimpleObjectProperty(DEFAULT_FADE_DURATION);
/*   97 */   private final ObjectProperty<Duration> fadeOutDuration = (ObjectProperty<Duration>)new SimpleObjectProperty(DEFAULT_FADE_DURATION);
/*      */ 
/*      */   
/*      */   private final StackPane root;
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Node> contentNode;
/*      */ 
/*      */   
/*      */   private InvalidationListener hideListener;
/*      */ 
/*      */   
/*      */   private WeakInvalidationListener weakHideListener;
/*      */ 
/*      */   
/*      */   private ChangeListener<Number> xListener;
/*      */ 
/*      */   
/*      */   private WeakChangeListener<Number> weakXListener;
/*      */ 
/*      */   
/*      */   private ChangeListener<Number> yListener;
/*      */ 
/*      */   
/*      */   private WeakChangeListener<Number> weakYListener;
/*      */ 
/*      */   
/*      */   private Window ownerWindow;
/*      */ 
/*      */   
/*      */   private final EventHandler<WindowEvent> closePopOverOnOwnerWindowCloseLambda;
/*      */ 
/*      */   
/*      */   private final WeakEventHandler<WindowEvent> closePopOverOnOwnerWindowClose;
/*      */ 
/*      */   
/*      */   private final BooleanProperty headerAlwaysVisible;
/*      */ 
/*      */   
/*      */   private final BooleanProperty closeButtonEnabled;
/*      */ 
/*      */   
/*      */   private final BooleanProperty detachable;
/*      */ 
/*      */   
/*      */   private final BooleanProperty detached;
/*      */ 
/*      */   
/*      */   private final DoubleProperty arrowSize;
/*      */ 
/*      */   
/*      */   private final DoubleProperty arrowIndent;
/*      */ 
/*      */   
/*      */   private final DoubleProperty cornerRadius;
/*      */ 
/*      */   
/*      */   private final StringProperty title;
/*      */   
/*      */   private final ObjectProperty<ArrowLocation> arrowLocation;
/*      */ 
/*      */   
/*      */   public PopOver(Node content) {
/*  160 */     this();
/*      */     
/*  162 */     setContentNode(content);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Skin<?> createDefaultSkin() {
/*  167 */     return (Skin<?>)new PopOverSkin(this); }
/*      */   public final StackPane getRoot() { return this.root; }
/*      */   public final ObjectProperty<Node> contentNodeProperty() { return this.contentNode; }
/*  170 */   public final Node getContentNode() { return (Node)contentNodeProperty().get(); } public final void setContentNode(Node content) { contentNodeProperty().set(content); } public final void show(Node owner) { show(owner, 4.0D); } public final void show(Node owner, double offset) { Objects.requireNonNull(owner); Bounds bounds = owner.localToScreen(owner.getBoundsInLocal()); switch (getArrowLocation()) { case BOTTOM_CENTER: case BOTTOM_LEFT: case BOTTOM_RIGHT: show(owner, bounds.getMinX() + bounds.getWidth() / 2.0D, bounds.getMinY() + offset); break;case LEFT_BOTTOM: case LEFT_CENTER: case LEFT_TOP: show(owner, bounds.getMaxX() - offset, bounds.getMinY() + bounds.getHeight() / 2.0D); break;case RIGHT_BOTTOM: case RIGHT_CENTER: case RIGHT_TOP: show(owner, bounds.getMinX() + offset, bounds.getMinY() + bounds.getHeight() / 2.0D); break;case TOP_CENTER: case TOP_LEFT: case TOP_RIGHT: show(owner, bounds.getMinX() + bounds.getWidth() / 2.0D, bounds.getMinY() + bounds.getHeight() - offset); break; }  } public final void show(Window owner) { super.show(owner); this.ownerWindow = owner; if (isAnimated()) showFadeInAnimation(getFadeInDuration());  this.ownerWindow.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (EventHandler)this.closePopOverOnOwnerWindowClose); this.ownerWindow.addEventFilter(WindowEvent.WINDOW_HIDING, (EventHandler)this.closePopOverOnOwnerWindowClose); } public final void show(Window ownerWindow, double anchorX, double anchorY) { super.show(ownerWindow, anchorX, anchorY); this.ownerWindow = ownerWindow; if (isAnimated()) showFadeInAnimation(getFadeInDuration());  ownerWindow.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (EventHandler)this.closePopOverOnOwnerWindowClose); ownerWindow.addEventFilter(WindowEvent.WINDOW_HIDING, (EventHandler)this.closePopOverOnOwnerWindowClose); } public final void show(Node owner, double x, double y) { show(owner, x, y, getFadeInDuration()); } public final void show(Node owner, double x, double y, Duration fadeInDuration) { if (this.ownerWindow != null && isShowing()) super.hide();  this.targetX = x; this.targetY = y; if (owner == null) throw new IllegalArgumentException("owner can not be null");  if (fadeInDuration == null) fadeInDuration = DEFAULT_FADE_DURATION;  if (this.ownerWindow != null) { this.ownerWindow.xProperty().removeListener((ChangeListener)this.weakXListener); this.ownerWindow.yProperty().removeListener((ChangeListener)this.weakYListener); this.ownerWindow.widthProperty().removeListener((InvalidationListener)this.weakHideListener); this.ownerWindow.heightProperty().removeListener((InvalidationListener)this.weakHideListener); }  this.ownerWindow = owner.getScene().getWindow(); this.ownerWindow.xProperty().addListener((ChangeListener)this.weakXListener); this.ownerWindow.yProperty().addListener((ChangeListener)this.weakYListener); this.ownerWindow.widthProperty().addListener((InvalidationListener)this.weakHideListener); this.ownerWindow.heightProperty().addListener((InvalidationListener)this.weakHideListener); setOnShown(evt -> { getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, ()); adjustWindowLocation(); }); super.show(owner, x, y); if (isAnimated()) showFadeInAnimation(fadeInDuration);  this.ownerWindow.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (EventHandler)this.closePopOverOnOwnerWindowClose); this.ownerWindow.addEventFilter(WindowEvent.WINDOW_HIDING, (EventHandler)this.closePopOverOnOwnerWindowClose); } private void showFadeInAnimation(Duration fadeInDuration) { Node skinNode = getSkin().getNode(); skinNode.setOpacity(0.0D); FadeTransition fadeIn = new FadeTransition(fadeInDuration, skinNode); fadeIn.setFromValue(0.0D); fadeIn.setToValue(1.0D); fadeIn.play(); } public PopOver() { this.root = new StackPane();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  191 */     this.contentNode = (ObjectProperty<Node>)new SimpleObjectProperty<Node>(this, "contentNode")
/*      */       {
/*      */         public void setValue(Node node)
/*      */         {
/*  195 */           if (node == null) {
/*  196 */             throw new IllegalArgumentException("content node can not be null");
/*      */           }
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  234 */     this.hideListener = new InvalidationListener()
/*      */       {
/*      */         public void invalidated(Observable observable) {
/*  237 */           if (!PopOver.this.isDetached()) {
/*  238 */             PopOver.this.hide(Duration.ZERO);
/*      */           }
/*      */         }
/*      */       };
/*      */     
/*  243 */     this.weakHideListener = new WeakInvalidationListener(this.hideListener);
/*      */ 
/*      */     
/*  246 */     this.xListener = new ChangeListener<Number>()
/*      */       {
/*      */         public void changed(ObservableValue<? extends Number> value, Number oldX, Number newX)
/*      */         {
/*  250 */           if (!PopOver.this.isDetached()) {
/*  251 */             PopOver.this.setAnchorX(PopOver.this.getAnchorX() + newX.doubleValue() - oldX.doubleValue());
/*      */           }
/*      */         }
/*      */       };
/*      */     
/*  256 */     this.weakXListener = new WeakChangeListener(this.xListener);
/*      */ 
/*      */     
/*  259 */     this.yListener = new ChangeListener<Number>()
/*      */       {
/*      */         public void changed(ObservableValue<? extends Number> value, Number oldY, Number newY)
/*      */         {
/*  263 */           if (!PopOver.this.isDetached()) {
/*  264 */             PopOver.this.setAnchorY(PopOver.this.getAnchorY() + newY.doubleValue() - oldY.doubleValue());
/*      */           }
/*      */         }
/*      */       };
/*      */     
/*  269 */     this.weakYListener = new WeakChangeListener(this.yListener);
/*      */ 
/*      */ 
/*      */     
/*  273 */     this.closePopOverOnOwnerWindowCloseLambda = (event -> ownerWindowClosing());
/*  274 */     this.closePopOverOnOwnerWindowClose = new WeakEventHandler(this.closePopOverOnOwnerWindowCloseLambda);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  619 */     this.headerAlwaysVisible = (BooleanProperty)new SimpleBooleanProperty(this, "headerAlwaysVisible");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  653 */     this.closeButtonEnabled = (BooleanProperty)new SimpleBooleanProperty(this, "closeButtonEnabled", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  687 */     this.detachable = (BooleanProperty)new SimpleBooleanProperty(this, "detachable", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  720 */     this.detached = (BooleanProperty)new SimpleBooleanProperty(this, "detached", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  762 */     this.arrowSize = (DoubleProperty)new SimpleDoubleProperty(this, "arrowSize", 12.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  801 */     this.arrowIndent = (DoubleProperty)new SimpleDoubleProperty(this, "arrowIndent", 12.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  841 */     this.cornerRadius = (DoubleProperty)new SimpleDoubleProperty(this, "cornerRadius", 6.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  878 */     this.title = (StringProperty)new SimpleStringProperty(this, "title", Localization.localize(Localization.asKey("popOver.default.title")));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  913 */     this.arrowLocation = (ObjectProperty<ArrowLocation>)new SimpleObjectProperty(this, "arrowLocation", ArrowLocation.LEFT_TOP); getStyleClass().add("popover"); getRoot().getStylesheets().add(PopOver.class.getResource("popover.css").toExternalForm()); setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_LEFT); setOnHiding(new EventHandler<WindowEvent>() {
/*      */           public void handle(WindowEvent evt) { PopOver.this.setDetached(false); }
/*      */         }); Label label = new Label(Localization.localize(Localization.asKey("popOver.default.content"))); label.setPrefSize(200.0D, 200.0D); label.setPadding(new Insets(4.0D)); setContentNode((Node)label); InvalidationListener repositionListener = observable -> { if (isShowing() && !isDetached()) { show(getOwnerNode(), this.targetX, this.targetY); adjustWindowLocation(); }  }; this.arrowSize.addListener(repositionListener); this.cornerRadius.addListener(repositionListener); this.arrowLocation.addListener(repositionListener); this.arrowIndent.addListener(repositionListener); this.headerAlwaysVisible.addListener(repositionListener); this.detached.addListener(it -> { if (isDetached()) { setAutoHide(false); } else { setAutoHide(true); }  }); setAutoHide(true); }
/*      */   private void ownerWindowClosing() { hide(Duration.ZERO); }
/*      */   public final void hide() { hide(getFadeOutDuration()); }
/*      */   public final void hide(Duration fadeOutDuration) { if (this.ownerWindow != null) { this.ownerWindow.removeEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (EventHandler)this.closePopOverOnOwnerWindowClose); this.ownerWindow.removeEventFilter(WindowEvent.WINDOW_HIDING, (EventHandler)this.closePopOverOnOwnerWindowClose); }  if (fadeOutDuration == null) fadeOutDuration = DEFAULT_FADE_DURATION;  if (isShowing()) if (isAnimated()) { Node skinNode = getSkin().getNode(); FadeTransition fadeOut = new FadeTransition(fadeOutDuration, skinNode); fadeOut.setFromValue(skinNode.getOpacity()); fadeOut.setToValue(0.0D); fadeOut.setOnFinished(evt -> super.hide()); fadeOut.play(); } else { super.hide(); }   }
/*      */   private void adjustWindowLocation() { Bounds bounds = getSkin().getNode().getBoundsInParent(); switch (getArrowLocation()) { case TOP_CENTER: case TOP_LEFT: case TOP_RIGHT: setAnchorX(getAnchorX() + bounds.getMinX() - computeXOffset()); setAnchorY(getAnchorY() + bounds.getMinY() + getArrowSize()); break;case LEFT_BOTTOM: case LEFT_CENTER: case LEFT_TOP: setAnchorX(getAnchorX() + bounds.getMinX() + getArrowSize()); setAnchorY(getAnchorY() + bounds.getMinY() - computeYOffset()); break;case BOTTOM_CENTER: case BOTTOM_LEFT: case BOTTOM_RIGHT: setAnchorX(getAnchorX() + bounds.getMinX() - computeXOffset()); setAnchorY(getAnchorY() - bounds.getMinY() - bounds.getMaxY() - 1.0D); break;case RIGHT_BOTTOM: case RIGHT_CENTER: case RIGHT_TOP: setAnchorX(getAnchorX() - bounds.getMinX() - bounds.getMaxX() - 1.0D); setAnchorY(getAnchorY() + bounds.getMinY() - computeYOffset()); break; }  }
/*      */   private double computeXOffset() { switch (getArrowLocation()) { case BOTTOM_LEFT: case TOP_LEFT: return getCornerRadius() + getArrowIndent() + getArrowSize();case BOTTOM_CENTER: case TOP_CENTER: return getContentNode().prefWidth(-1.0D) / 2.0D;case BOTTOM_RIGHT: case TOP_RIGHT: return getContentNode().prefWidth(-1.0D) - getArrowIndent() - getCornerRadius() - getArrowSize(); }  return 0.0D; }
/*      */   private double computeYOffset() { double prefContentHeight = getContentNode().prefHeight(-1.0D); switch (getArrowLocation()) { case LEFT_TOP: case RIGHT_TOP: return getCornerRadius() + getArrowIndent() + getArrowSize();case LEFT_CENTER: case RIGHT_CENTER: return Math.max(prefContentHeight, 2.0D * (getCornerRadius() + getArrowIndent() + getArrowSize())) / 2.0D;case LEFT_BOTTOM: case RIGHT_BOTTOM: return Math.max(prefContentHeight - getCornerRadius() - getArrowIndent() - getArrowSize(), getCornerRadius() + getArrowIndent() + getArrowSize()); }  return 0.0D; }
/*      */   public final void detach() { if (isDetachable())
/*      */       setDetached(true);  }
/*      */   public final BooleanProperty headerAlwaysVisibleProperty() { return this.headerAlwaysVisible; }
/*  925 */   public final void setHeaderAlwaysVisible(boolean visible) { this.headerAlwaysVisible.setValue(Boolean.valueOf(visible)); } public final boolean isHeaderAlwaysVisible() { return this.headerAlwaysVisible.getValue().booleanValue(); } public final ObjectProperty<ArrowLocation> arrowLocationProperty() { return this.arrowLocation; } public final BooleanProperty closeButtonEnabledProperty() { return this.closeButtonEnabled; } public final void setCloseButtonEnabled(boolean enabled) { this.closeButtonEnabled.setValue(Boolean.valueOf(enabled)); } public final boolean isCloseButtonEnabled() { return this.closeButtonEnabled.getValue().booleanValue(); } public final BooleanProperty detachableProperty() { return this.detachable; } public final void setDetachable(boolean detachable) { detachableProperty().set(detachable); } public final boolean isDetachable() { return detachableProperty().get(); } public final BooleanProperty detachedProperty() { return this.detached; } public final void setDetached(boolean detached) { detachedProperty().set(detached); } public final boolean isDetached() { return detachedProperty().get(); } public final DoubleProperty arrowSizeProperty() { return this.arrowSize; }
/*      */   public final double getArrowSize() { return arrowSizeProperty().get(); }
/*      */   public final void setArrowSize(double size) { arrowSizeProperty().set(size); }
/*      */   public final DoubleProperty arrowIndentProperty() { return this.arrowIndent; }
/*      */   public final double getArrowIndent() { return arrowIndentProperty().get(); }
/*      */   public final void setArrowIndent(double size) { arrowIndentProperty().set(size); }
/*      */   public final DoubleProperty cornerRadiusProperty() { return this.cornerRadius; }
/*      */   public final double getCornerRadius() { return cornerRadiusProperty().get(); }
/*      */   public final void setCornerRadius(double radius) { cornerRadiusProperty().set(radius); }
/*      */   public final StringProperty titleProperty() { return this.title; }
/*      */   public final String getTitle() { return (String)titleProperty().get(); }
/*      */   public final void setTitle(String title) { if (title == null) throw new IllegalArgumentException("title can not be null");  titleProperty().set(title); }
/*  937 */   public final void setArrowLocation(ArrowLocation location) { arrowLocationProperty().set(location); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ArrowLocation getArrowLocation() {
/*  948 */     return (ArrowLocation)arrowLocationProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ArrowLocation
/*      */   {
/*  955 */     LEFT_TOP, LEFT_CENTER, LEFT_BOTTOM, RIGHT_TOP, RIGHT_CENTER, RIGHT_BOTTOM, TOP_LEFT, TOP_CENTER, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Duration> fadeInDurationProperty() {
/*  964 */     return this.fadeInDuration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Duration> fadeOutDurationProperty() {
/*  973 */     return this.fadeOutDuration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Duration getFadeInDuration() {
/*  983 */     return (Duration)fadeInDurationProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFadeInDuration(Duration duration) {
/*  993 */     fadeInDurationProperty().setValue(duration);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Duration getFadeOutDuration() {
/* 1003 */     return (Duration)fadeOutDurationProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFadeOutDuration(Duration duration) {
/* 1013 */     fadeOutDurationProperty().setValue(duration);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty animatedProperty() {
/* 1022 */     return (BooleanProperty)this.animated;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAnimated() {
/* 1032 */     return animatedProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAnimated(boolean animated) {
/* 1042 */     animatedProperty().set(animated);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\PopOver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */