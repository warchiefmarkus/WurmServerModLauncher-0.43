/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import javafx.animation.Animation;
/*     */ import javafx.animation.KeyFrame;
/*     */ import javafx.animation.KeyValue;
/*     */ import javafx.animation.Timeline;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WritableValue;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.Side;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.HiddenSidesPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HiddenSidesPaneSkin
/*     */   extends SkinBase<HiddenSidesPane>
/*     */ {
/*     */   private final StackPane stackPane;
/*     */   private final EventHandler<MouseEvent> exitedHandler;
/*     */   private boolean mousePressed;
/*     */   private DoubleProperty[] visibility;
/*     */   private Timeline showTimeline;
/*     */   private Timeline hideTimeline;
/*     */   
/*     */   private boolean isMouseMovedOutsideSides(MouseEvent event) {
/*     */     if (((HiddenSidesPane)getSkinnable()).getLeft() != null && ((HiddenSidesPane)getSkinnable()).getLeft().getBoundsInParent().contains(event.getX(), event.getY()))
/*     */       return false; 
/*     */     if (((HiddenSidesPane)getSkinnable()).getTop() != null && ((HiddenSidesPane)getSkinnable()).getTop().getBoundsInParent().contains(event.getX(), event.getY()))
/*     */       return false; 
/*     */     if (((HiddenSidesPane)getSkinnable()).getRight() != null && ((HiddenSidesPane)getSkinnable()).getRight().getBoundsInParent().contains(event.getX(), event.getY()))
/*     */       return false; 
/*     */     if (((HiddenSidesPane)getSkinnable()).getBottom() != null && ((HiddenSidesPane)getSkinnable()).getBottom().getBoundsInParent().contains(event.getX(), event.getY()))
/*     */       return false; 
/*     */     return true;
/*     */   }
/*     */   
/*     */   public HiddenSidesPaneSkin(HiddenSidesPane pane) {
/*  54 */     super((Control)pane);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     this
/* 176 */       .visibility = (DoubleProperty[])new SimpleDoubleProperty[(Side.values()).length]; this.exitedHandler = (event -> { if (isMouseEnabled() && ((HiddenSidesPane)getSkinnable()).getPinnedSide() == null && !this.mousePressed)
/*     */           hide();  }); this.stackPane = new StackPane(); getChildren().add(this.stackPane); updateStackPane(); InvalidationListener rebuildListener = observable -> updateStackPane(); pane.contentProperty().addListener(rebuildListener); pane.topProperty().addListener(rebuildListener); pane.rightProperty().addListener(rebuildListener); pane.bottomProperty().addListener(rebuildListener); pane.leftProperty().addListener(rebuildListener); pane.addEventFilter(MouseEvent.MOUSE_MOVED, event -> { if (isMouseEnabled() && ((HiddenSidesPane)getSkinnable()).getPinnedSide() == null) { Side side = getSide(event); if (side != null) { show(side); } else if (isMouseMovedOutsideSides(event)) { hide(); }  }  }); pane.addEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler); pane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> this.mousePressed = true); pane.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> { this.mousePressed = false; if (isMouseEnabled() && ((HiddenSidesPane)getSkinnable()).getPinnedSide() == null) { Side side = getSide(event); if (side != null) { show(side); } else { hide(); }  }
/*     */         
/*     */         }); for (Side side : Side.values()) { this.visibility[side.ordinal()] = (DoubleProperty)new SimpleDoubleProperty(0.0D); this.visibility[side.ordinal()].addListener(observable -> ((HiddenSidesPane)getSkinnable()).requestLayout()); }
/*     */      Side pinnedSide = ((HiddenSidesPane)getSkinnable()).getPinnedSide(); if (pinnedSide != null)
/* 181 */       show(pinnedSide);  pane.pinnedSideProperty().addListener(observable -> show(((HiddenSidesPane)getSkinnable()).getPinnedSide())); Rectangle clip = new Rectangle(); clip.setX(0.0D); clip.setY(0.0D); clip.widthProperty().bind((ObservableValue)((HiddenSidesPane)getSkinnable()).widthProperty()); clip.heightProperty().bind((ObservableValue)((HiddenSidesPane)getSkinnable()).heightProperty()); ((HiddenSidesPane)getSkinnable()).setClip((Node)clip); } private void show(Side side) { if (this.hideTimeline != null) {
/* 182 */       this.hideTimeline.stop();
/*     */     }
/*     */     
/* 185 */     if (this.showTimeline != null && this.showTimeline.getStatus() == Animation.Status.RUNNING) {
/*     */       return;
/*     */     }
/*     */     
/* 189 */     KeyValue[] keyValues = new KeyValue[(Side.values()).length];
/* 190 */     for (Side s : Side.values()) {
/* 191 */       keyValues[s.ordinal()] = new KeyValue((WritableValue)this.visibility[s.ordinal()], 
/* 192 */           Integer.valueOf(s.equals(side) ? 1 : 0));
/*     */     }
/*     */ 
/*     */     
/* 196 */     Duration delay = (((HiddenSidesPane)getSkinnable()).getAnimationDelay() != null) ? ((HiddenSidesPane)getSkinnable()).getAnimationDelay() : Duration.millis(300.0D);
/*     */     
/* 198 */     Duration duration = (((HiddenSidesPane)getSkinnable()).getAnimationDuration() != null) ? ((HiddenSidesPane)getSkinnable()).getAnimationDuration() : Duration.millis(200.0D);
/*     */     
/* 200 */     KeyFrame keyFrame = new KeyFrame(duration, keyValues);
/* 201 */     this.showTimeline = new Timeline(new KeyFrame[] { keyFrame });
/* 202 */     this.showTimeline.setDelay(delay);
/* 203 */     this.showTimeline.play(); } private boolean isMouseEnabled() { return (((HiddenSidesPane)getSkinnable()).getTriggerDistance() > 0.0D); }
/*     */   private Side getSide(MouseEvent evt) { if (this.stackPane.getBoundsInLocal().contains(evt.getX(), evt.getY())) { double trigger = ((HiddenSidesPane)getSkinnable()).getTriggerDistance(); if (evt.getX() <= trigger)
/*     */         return Side.LEFT;  if (evt.getX() > ((HiddenSidesPane)getSkinnable()).getWidth() - trigger)
/*     */         return Side.RIGHT;  if (evt.getY() <= trigger)
/*     */         return Side.TOP;  if (evt.getY() > ((HiddenSidesPane)getSkinnable()).getHeight() - trigger)
/*     */         return Side.BOTTOM;  }  return null; }
/* 209 */   private void hide() { if (this.showTimeline != null) {
/* 210 */       this.showTimeline.stop();
/*     */     }
/*     */     
/* 213 */     if (this.hideTimeline != null && this.hideTimeline.getStatus() == Animation.Status.RUNNING) {
/*     */       return;
/*     */     }
/*     */     
/* 217 */     boolean sideVisible = false;
/* 218 */     for (Side side : Side.values()) {
/* 219 */       if (this.visibility[side.ordinal()].get() > 0.0D) {
/* 220 */         sideVisible = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     if (!sideVisible) {
/*     */       return;
/*     */     }
/*     */     
/* 230 */     KeyValue[] keyValues = new KeyValue[(Side.values()).length];
/* 231 */     for (Side side : Side.values()) {
/* 232 */       keyValues[side.ordinal()] = new KeyValue(
/* 233 */           (WritableValue)this.visibility[side.ordinal()], Integer.valueOf(0));
/*     */     }
/*     */ 
/*     */     
/* 237 */     Duration delay = (((HiddenSidesPane)getSkinnable()).getAnimationDelay() != null) ? ((HiddenSidesPane)getSkinnable()).getAnimationDelay() : Duration.millis(300.0D);
/*     */     
/* 239 */     Duration duration = (((HiddenSidesPane)getSkinnable()).getAnimationDuration() != null) ? ((HiddenSidesPane)getSkinnable()).getAnimationDuration() : Duration.millis(200.0D);
/*     */     
/* 241 */     KeyFrame keyFrame = new KeyFrame(duration, keyValues);
/* 242 */     this.hideTimeline = new Timeline(new KeyFrame[] { keyFrame });
/* 243 */     this.hideTimeline.setDelay(delay);
/* 244 */     this.hideTimeline.play(); }
/*     */ 
/*     */   
/*     */   private void updateStackPane() {
/* 248 */     this.stackPane.getChildren().clear();
/*     */     
/* 250 */     if (((HiddenSidesPane)getSkinnable()).getContent() != null) {
/* 251 */       this.stackPane.getChildren().add(((HiddenSidesPane)getSkinnable()).getContent());
/*     */     }
/* 253 */     if (((HiddenSidesPane)getSkinnable()).getTop() != null) {
/* 254 */       this.stackPane.getChildren().add(((HiddenSidesPane)getSkinnable()).getTop());
/* 255 */       ((HiddenSidesPane)getSkinnable()).getTop().setManaged(false);
/* 256 */       ((HiddenSidesPane)getSkinnable()).getTop().removeEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
/*     */       
/* 258 */       ((HiddenSidesPane)getSkinnable()).getTop().addEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
/*     */     } 
/*     */     
/* 261 */     if (((HiddenSidesPane)getSkinnable()).getRight() != null) {
/* 262 */       this.stackPane.getChildren().add(((HiddenSidesPane)getSkinnable()).getRight());
/* 263 */       ((HiddenSidesPane)getSkinnable()).getRight().setManaged(false);
/* 264 */       ((HiddenSidesPane)getSkinnable()).getRight().removeEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
/*     */       
/* 266 */       ((HiddenSidesPane)getSkinnable()).getRight().addEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
/*     */     } 
/*     */     
/* 269 */     if (((HiddenSidesPane)getSkinnable()).getBottom() != null) {
/* 270 */       this.stackPane.getChildren().add(((HiddenSidesPane)getSkinnable()).getBottom());
/* 271 */       ((HiddenSidesPane)getSkinnable()).getBottom().setManaged(false);
/* 272 */       ((HiddenSidesPane)getSkinnable()).getBottom().removeEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
/*     */       
/* 274 */       ((HiddenSidesPane)getSkinnable()).getBottom().addEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
/*     */     } 
/*     */     
/* 277 */     if (((HiddenSidesPane)getSkinnable()).getLeft() != null) {
/* 278 */       this.stackPane.getChildren().add(((HiddenSidesPane)getSkinnable()).getLeft());
/* 279 */       ((HiddenSidesPane)getSkinnable()).getLeft().setManaged(false);
/* 280 */       ((HiddenSidesPane)getSkinnable()).getLeft().removeEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
/*     */       
/* 282 */       ((HiddenSidesPane)getSkinnable()).getLeft().addEventFilter(MouseEvent.MOUSE_EXITED, this.exitedHandler);
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
/*     */   protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
/* 295 */     super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
/*     */ 
/*     */ 
/*     */     
/* 299 */     Node bottom = ((HiddenSidesPane)getSkinnable()).getBottom();
/* 300 */     if (bottom != null) {
/* 301 */       double prefHeight = bottom.prefHeight(-1.0D);
/*     */       
/* 303 */       double offset = prefHeight * this.visibility[Side.BOTTOM.ordinal()].get();
/* 304 */       bottom.resizeRelocate(contentX, contentY + contentHeight - offset, contentWidth, prefHeight);
/*     */       
/* 306 */       bottom.setVisible((this.visibility[Side.BOTTOM.ordinal()].get() > 0.0D));
/*     */     } 
/*     */     
/* 309 */     Node left = ((HiddenSidesPane)getSkinnable()).getLeft();
/* 310 */     if (left != null) {
/* 311 */       double prefWidth = left.prefWidth(-1.0D);
/* 312 */       double offset = prefWidth * this.visibility[Side.LEFT.ordinal()].get();
/* 313 */       left.resizeRelocate(contentX - prefWidth - offset, contentY, prefWidth, contentHeight);
/*     */       
/* 315 */       left.setVisible((this.visibility[Side.LEFT.ordinal()].get() > 0.0D));
/*     */     } 
/*     */     
/* 318 */     Node right = ((HiddenSidesPane)getSkinnable()).getRight();
/* 319 */     if (right != null) {
/* 320 */       double prefWidth = right.prefWidth(-1.0D);
/* 321 */       double offset = prefWidth * this.visibility[Side.RIGHT.ordinal()].get();
/* 322 */       right.resizeRelocate(contentX + contentWidth - offset, contentY, prefWidth, contentHeight);
/*     */       
/* 324 */       right.setVisible((this.visibility[Side.RIGHT.ordinal()].get() > 0.0D));
/*     */     } 
/*     */     
/* 327 */     Node top = ((HiddenSidesPane)getSkinnable()).getTop();
/* 328 */     if (top != null) {
/* 329 */       double prefHeight = top.prefHeight(-1.0D);
/* 330 */       double offset = prefHeight * this.visibility[Side.TOP.ordinal()].get();
/* 331 */       top.resizeRelocate(contentX, contentY - prefHeight - offset, contentWidth, prefHeight);
/*     */       
/* 333 */       top.setVisible((this.visibility[Side.TOP.ordinal()].get() > 0.0D));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\HiddenSidesPaneSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */