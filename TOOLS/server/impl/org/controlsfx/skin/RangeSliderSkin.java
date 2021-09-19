/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import com.sun.javafx.scene.traversal.Algorithm;
/*     */ import com.sun.javafx.scene.traversal.Direction;
/*     */ import com.sun.javafx.scene.traversal.ParentTraversalEngine;
/*     */ import com.sun.javafx.scene.traversal.TraversalContext;
/*     */ import impl.org.controlsfx.behavior.RangeSliderBehavior;
/*     */ import java.util.List;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.ObjectBinding;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.geometry.Point2D;
/*     */ import javafx.geometry.Side;
/*     */ import javafx.scene.Cursor;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.chart.NumberAxis;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.input.KeyCode;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.BackgroundFill;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.RangeSlider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RangeSliderSkin
/*     */   extends BehaviorSkinBase<RangeSlider, RangeSliderBehavior>
/*     */ {
/*  65 */   private NumberAxis tickLine = null;
/*  66 */   private double trackToTickGap = 2.0D;
/*     */   
/*     */   private boolean showTickMarks;
/*     */   
/*     */   private double thumbWidth;
/*     */   
/*     */   private double thumbHeight;
/*     */   
/*     */   private Orientation orientation;
/*     */   
/*     */   private StackPane track;
/*     */   
/*     */   private double trackStart;
/*     */   private double trackLength;
/*     */   private double lowThumbPos;
/*     */   private double rangeEnd;
/*     */   private double rangeStart;
/*     */   private ThumbPane lowThumb;
/*     */   private ThumbPane highThumb;
/*     */   private StackPane rangeBar;
/*     */   private double preDragPos;
/*     */   private Point2D preDragThumbPoint;
/*  88 */   private RangeSliderBehavior.FocusedChild currentFocus = RangeSliderBehavior.FocusedChild.LOW_THUMB;
/*     */   
/*     */   public RangeSliderSkin(final RangeSlider rangeSlider) {
/*  91 */     super((Control)rangeSlider, (BehaviorBase)new RangeSliderBehavior(rangeSlider));
/*  92 */     this.orientation = ((RangeSlider)getSkinnable()).getOrientation();
/*  93 */     initFirstThumb();
/*  94 */     initSecondThumb();
/*  95 */     initRangeBar();
/*  96 */     registerChangeListener((ObservableValue)rangeSlider.lowValueProperty(), "LOW_VALUE");
/*  97 */     registerChangeListener((ObservableValue)rangeSlider.highValueProperty(), "HIGH_VALUE");
/*  98 */     registerChangeListener((ObservableValue)rangeSlider.minProperty(), "MIN");
/*  99 */     registerChangeListener((ObservableValue)rangeSlider.maxProperty(), "MAX");
/* 100 */     registerChangeListener((ObservableValue)rangeSlider.orientationProperty(), "ORIENTATION");
/* 101 */     registerChangeListener((ObservableValue)rangeSlider.showTickMarksProperty(), "SHOW_TICK_MARKS");
/* 102 */     registerChangeListener((ObservableValue)rangeSlider.showTickLabelsProperty(), "SHOW_TICK_LABELS");
/* 103 */     registerChangeListener((ObservableValue)rangeSlider.majorTickUnitProperty(), "MAJOR_TICK_UNIT");
/* 104 */     registerChangeListener((ObservableValue)rangeSlider.minorTickCountProperty(), "MINOR_TICK_COUNT");
/* 105 */     this.lowThumb.focusedProperty().addListener(new ChangeListener<Boolean>() {
/*     */           public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
/* 107 */             if (hasFocus.booleanValue()) {
/* 108 */               RangeSliderSkin.this.currentFocus = RangeSliderBehavior.FocusedChild.LOW_THUMB;
/*     */             }
/*     */           }
/*     */         });
/* 112 */     this.highThumb.focusedProperty().addListener(new ChangeListener<Boolean>() {
/*     */           public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
/* 114 */             if (hasFocus.booleanValue()) {
/* 115 */               RangeSliderSkin.this.currentFocus = RangeSliderBehavior.FocusedChild.HIGH_THUMB;
/*     */             }
/*     */           }
/*     */         });
/* 119 */     this.rangeBar.focusedProperty().addListener(new ChangeListener<Boolean>() {
/*     */           public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
/* 121 */             if (hasFocus.booleanValue()) {
/* 122 */               RangeSliderSkin.this.currentFocus = RangeSliderBehavior.FocusedChild.RANGE_BAR;
/*     */             }
/*     */           }
/*     */         });
/* 126 */     rangeSlider.focusedProperty().addListener(new ChangeListener<Boolean>() {
/*     */           public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
/* 128 */             if (hasFocus.booleanValue()) {
/* 129 */               RangeSliderSkin.this.lowThumb.setFocus(true);
/*     */             } else {
/* 131 */               RangeSliderSkin.this.lowThumb.setFocus(false);
/* 132 */               RangeSliderSkin.this.highThumb.setFocus(false);
/* 133 */               RangeSliderSkin.this.currentFocus = RangeSliderBehavior.FocusedChild.NONE;
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 138 */     EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>()
/*     */       {
/*     */         
/* 141 */         private final Algorithm algorithm = new Algorithm()
/*     */           {
/*     */             
/*     */             public Node selectLast(TraversalContext context)
/*     */             {
/* 146 */               List<Node> focusTraversableNodes = context.getAllTargetNodes();
/*     */               
/* 148 */               return focusTraversableNodes.get(focusTraversableNodes
/* 149 */                   .size() - 1);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public Node selectFirst(TraversalContext context) {
/* 161 */               return context.getAllTargetNodes().get(0);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public Node select(Node owner, Direction dir, TraversalContext context) {
/*     */               int direction;
/* 183 */               switch (dir) {
/*     */                 
/*     */                 case DOWN:
/*     */                 case RIGHT:
/*     */                 case NEXT:
/*     */                 case NEXT_IN_LINE:
/* 189 */                   direction = 1;
/*     */                   break;
/*     */ 
/*     */ 
/*     */                 
/*     */                 case LEFT:
/*     */                 case PREVIOUS:
/*     */                 case UP:
/* 197 */                   direction = -2;
/*     */                   break;
/*     */ 
/*     */ 
/*     */                 
/*     */                 default:
/* 203 */                   throw new EnumConstantNotPresentException(dir
/* 204 */                       .getClass(), dir.name());
/*     */               } 
/*     */ 
/*     */               
/* 208 */               List<Node> focusTraversableNodes = context.getAllTargetNodes();
/*     */               
/* 210 */               int focusReceiverIndex = focusTraversableNodes.indexOf(owner) + direction;
/*     */               
/* 212 */               if (focusReceiverIndex < 0) {
/* 213 */                 return focusTraversableNodes.get(focusTraversableNodes
/* 214 */                     .size() - 1);
/*     */               }
/* 216 */               if (focusReceiverIndex == focusTraversableNodes.size()) {
/* 217 */                 return focusTraversableNodes.get(0);
/*     */               }
/*     */               
/* 220 */               return focusTraversableNodes.get(focusReceiverIndex);
/*     */             }
/*     */           };
/*     */         
/*     */         public void handle(KeyEvent event) {
/* 225 */           if (KeyCode.TAB.equals(event.getCode())) {
/* 226 */             if (RangeSliderSkin.this.lowThumb.isFocused()) {
/* 227 */               if (event.isShiftDown()) {
/* 228 */                 RangeSliderSkin.this.lowThumb.setFocus(false);
/* 229 */                 (new ParentTraversalEngine(rangeSlider
/* 230 */                     .getScene().getRoot(), this.algorithm))
/* 231 */                   .select((Node)RangeSliderSkin.this.lowThumb, Direction.PREVIOUS)
/* 232 */                   .requestFocus();
/*     */               } else {
/* 234 */                 RangeSliderSkin.this.lowThumb.setFocus(false);
/* 235 */                 RangeSliderSkin.this.highThumb.setFocus(true);
/*     */               } 
/* 237 */               event.consume();
/* 238 */             } else if (RangeSliderSkin.this.highThumb.isFocused()) {
/* 239 */               if (event.isShiftDown()) {
/* 240 */                 RangeSliderSkin.this.highThumb.setFocus(false);
/* 241 */                 RangeSliderSkin.this.lowThumb.setFocus(true);
/*     */               } else {
/* 243 */                 RangeSliderSkin.this.highThumb.setFocus(false);
/* 244 */                 (new ParentTraversalEngine(rangeSlider
/* 245 */                     .getScene().getRoot(), this.algorithm))
/* 246 */                   .select((Node)RangeSliderSkin.this.highThumb, Direction.NEXT)
/* 247 */                   .requestFocus();
/*     */               } 
/* 249 */               event.consume();
/*     */             } 
/*     */           }
/*     */         }
/*     */       };
/* 254 */     ((RangeSlider)getSkinnable()).addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
/*     */ 
/*     */     
/* 257 */     ((RangeSliderBehavior)getBehavior()).setSelectedValue(new Callback<Void, RangeSliderBehavior.FocusedChild>() {
/*     */           public RangeSliderBehavior.FocusedChild call(Void v) {
/* 259 */             return RangeSliderSkin.this.currentFocus;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void initFirstThumb() {
/* 265 */     this.lowThumb = new ThumbPane();
/* 266 */     this.lowThumb.getStyleClass().setAll((Object[])new String[] { "low-thumb" });
/* 267 */     this.lowThumb.setFocusTraversable(true);
/* 268 */     this.track = new StackPane();
/* 269 */     this.track.getStyleClass().setAll((Object[])new String[] { "track" });
/*     */     
/* 271 */     getChildren().clear();
/* 272 */     getChildren().addAll((Object[])new Node[] { (Node)this.track, (Node)this.lowThumb });
/* 273 */     setShowTickMarks(((RangeSlider)getSkinnable()).isShowTickMarks(), ((RangeSlider)getSkinnable()).isShowTickLabels());
/* 274 */     this.track.setOnMousePressed(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent me) {
/* 276 */             if (!RangeSliderSkin.this.lowThumb.isPressed() && !RangeSliderSkin.this.highThumb.isPressed()) {
/* 277 */               if (RangeSliderSkin.this.isHorizontal()) {
/* 278 */                 ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).trackPress(me, me.getX() / RangeSliderSkin.this.trackLength);
/*     */               } else {
/* 280 */                 ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).trackPress(me, me.getY() / RangeSliderSkin.this.trackLength);
/*     */               } 
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 286 */     this.track.setOnMouseReleased(new EventHandler<MouseEvent>()
/*     */         {
/*     */           public void handle(MouseEvent me)
/*     */           {
/* 290 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).trackRelease(me, 0.0D);
/*     */           }
/*     */         });
/*     */     
/* 294 */     this.lowThumb.setOnMousePressed(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent me) {
/* 296 */             RangeSliderSkin.this.highThumb.setFocus(false);
/* 297 */             RangeSliderSkin.this.lowThumb.setFocus(true);
/* 298 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).lowThumbPressed(me, 0.0D);
/* 299 */             RangeSliderSkin.this.preDragThumbPoint = RangeSliderSkin.this.lowThumb.localToParent(me.getX(), me.getY());
/* 300 */             RangeSliderSkin.this.preDragPos = (((RangeSlider)RangeSliderSkin.this.getSkinnable()).getLowValue() - ((RangeSlider)RangeSliderSkin.this.getSkinnable()).getMin()) / RangeSliderSkin.this
/* 301 */               .getMaxMinusMinNoZero();
/*     */           }
/*     */         });
/*     */     
/* 305 */     this.lowThumb.setOnMouseReleased(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent me) {
/* 307 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).lowThumbReleased(me);
/*     */           }
/*     */         });
/*     */     
/* 311 */     this.lowThumb.setOnMouseDragged(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent me) {
/* 313 */             Point2D cur = RangeSliderSkin.this.lowThumb.localToParent(me.getX(), me.getY());
/*     */             
/* 315 */             double dragPos = RangeSliderSkin.this.isHorizontal() ? (cur.getX() - RangeSliderSkin.this.preDragThumbPoint.getX()) : -(cur.getY() - RangeSliderSkin.this.preDragThumbPoint.getY());
/* 316 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).lowThumbDragged(me, RangeSliderSkin.this.preDragPos + dragPos / RangeSliderSkin.this.trackLength);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void initSecondThumb() {
/* 322 */     this.highThumb = new ThumbPane();
/* 323 */     this.highThumb.getStyleClass().setAll((Object[])new String[] { "high-thumb" });
/* 324 */     this.highThumb.setFocusTraversable(true);
/* 325 */     if (!getChildren().contains(this.highThumb)) {
/* 326 */       getChildren().add(this.highThumb);
/*     */     }
/*     */     
/* 329 */     this.highThumb.setOnMousePressed(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent e) {
/* 331 */             RangeSliderSkin.this.lowThumb.setFocus(false);
/* 332 */             RangeSliderSkin.this.highThumb.setFocus(true);
/* 333 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).highThumbPressed(e, 0.0D);
/* 334 */             RangeSliderSkin.this.preDragThumbPoint = RangeSliderSkin.this.highThumb.localToParent(e.getX(), e.getY());
/* 335 */             RangeSliderSkin.this.preDragPos = (((RangeSlider)RangeSliderSkin.this.getSkinnable()).getHighValue() - ((RangeSlider)RangeSliderSkin.this.getSkinnable()).getMin()) / RangeSliderSkin.this
/* 336 */               .getMaxMinusMinNoZero();
/*     */           }
/*     */         });
/*     */     
/* 340 */     this.highThumb.setOnMouseReleased(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent e) {
/* 342 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).highThumbReleased(e);
/*     */           }
/*     */         });
/*     */     
/* 346 */     this.highThumb.setOnMouseDragged(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent e) {
/* 348 */             boolean orientation = (((RangeSlider)RangeSliderSkin.this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL);
/* 349 */             double trackLength = orientation ? RangeSliderSkin.this.track.getWidth() : RangeSliderSkin.this.track.getHeight();
/*     */             
/* 351 */             Point2D point2d = RangeSliderSkin.this.highThumb.localToParent(e.getX(), e.getY());
/* 352 */             double d = (((RangeSlider)RangeSliderSkin.this.getSkinnable()).getOrientation() != Orientation.HORIZONTAL) ? -(point2d.getY() - RangeSliderSkin.this.preDragThumbPoint.getY()) : (point2d.getX() - RangeSliderSkin.this.preDragThumbPoint.getX());
/* 353 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).highThumbDragged(e, RangeSliderSkin.this.preDragPos + d / trackLength);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void initRangeBar() {
/* 359 */     this.rangeBar = new StackPane();
/* 360 */     this.rangeBar.cursorProperty().bind((ObservableValue)new ObjectBinding<Cursor>()
/*     */         {
/*     */           protected Cursor computeValue()
/*     */           {
/* 364 */             return RangeSliderSkin.this.rangeBar.isHover() ? Cursor.HAND : Cursor.DEFAULT;
/*     */           }
/*     */         });
/* 367 */     this.rangeBar.getStyleClass().setAll((Object[])new String[] { "range-bar" });
/*     */     
/* 369 */     this.rangeBar.setOnMousePressed(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent e) {
/* 371 */             RangeSliderSkin.this.rangeBar.requestFocus();
/* 372 */             RangeSliderSkin.this.preDragPos = RangeSliderSkin.this.isHorizontal() ? e.getX() : -e.getY();
/*     */           }
/*     */         });
/*     */     
/* 376 */     this.rangeBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent e) {
/* 378 */             double delta = (RangeSliderSkin.this.isHorizontal() ? e.getX() : -e.getY()) - RangeSliderSkin.this.preDragPos;
/* 379 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).moveRange(delta);
/*     */           }
/*     */         });
/*     */     
/* 383 */     this.rangeBar.setOnMouseReleased(new EventHandler<MouseEvent>() {
/*     */           public void handle(MouseEvent e) {
/* 385 */             ((RangeSliderBehavior)RangeSliderSkin.this.getBehavior()).confirmRange();
/*     */           }
/*     */         });
/*     */     
/* 389 */     getChildren().add(this.rangeBar);
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
/*     */   private void setShowTickMarks(boolean ticksVisible, boolean labelsVisible) {
/* 401 */     this.showTickMarks = (ticksVisible || labelsVisible);
/* 402 */     RangeSlider rangeSlider = (RangeSlider)getSkinnable();
/* 403 */     if (this.showTickMarks) {
/* 404 */       if (this.tickLine == null) {
/* 405 */         this.tickLine = new NumberAxis();
/* 406 */         this.tickLine.tickLabelFormatterProperty().bind((ObservableValue)((RangeSlider)getSkinnable()).labelFormatterProperty());
/* 407 */         this.tickLine.setAnimated(false);
/* 408 */         this.tickLine.setAutoRanging(false);
/* 409 */         this.tickLine.setSide(isHorizontal() ? Side.BOTTOM : Side.RIGHT);
/* 410 */         this.tickLine.setUpperBound(rangeSlider.getMax());
/* 411 */         this.tickLine.setLowerBound(rangeSlider.getMin());
/* 412 */         this.tickLine.setTickUnit(rangeSlider.getMajorTickUnit());
/* 413 */         this.tickLine.setTickMarkVisible(ticksVisible);
/* 414 */         this.tickLine.setTickLabelsVisible(labelsVisible);
/* 415 */         this.tickLine.setMinorTickVisible(ticksVisible);
/*     */ 
/*     */         
/* 418 */         this.tickLine.setMinorTickCount(Math.max(rangeSlider.getMinorTickCount(), 0) + 1);
/* 419 */         getChildren().clear();
/* 420 */         getChildren().addAll((Object[])new Node[] { (Node)this.tickLine, (Node)this.track, (Node)this.lowThumb });
/*     */       } else {
/* 422 */         this.tickLine.setTickLabelsVisible(labelsVisible);
/* 423 */         this.tickLine.setTickMarkVisible(ticksVisible);
/* 424 */         this.tickLine.setMinorTickVisible(ticksVisible);
/*     */       } 
/*     */     } else {
/*     */       
/* 428 */       getChildren().clear();
/* 429 */       getChildren().addAll((Object[])new Node[] { (Node)this.track, (Node)this.lowThumb });
/*     */     } 
/*     */ 
/*     */     
/* 433 */     ((RangeSlider)getSkinnable()).requestLayout();
/*     */   }
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/* 437 */     super.handleControlPropertyChanged(p);
/* 438 */     if ("ORIENTATION".equals(p)) {
/* 439 */       this.orientation = ((RangeSlider)getSkinnable()).getOrientation();
/* 440 */       if (this.showTickMarks && this.tickLine != null) {
/* 441 */         this.tickLine.setSide(isHorizontal() ? Side.BOTTOM : Side.RIGHT);
/*     */       }
/* 443 */       ((RangeSlider)getSkinnable()).requestLayout();
/* 444 */     } else if ("MIN".equals(p)) {
/* 445 */       if (this.showTickMarks && this.tickLine != null) {
/* 446 */         this.tickLine.setLowerBound(((RangeSlider)getSkinnable()).getMin());
/*     */       }
/* 448 */       ((RangeSlider)getSkinnable()).requestLayout();
/* 449 */     } else if ("MAX".equals(p)) {
/* 450 */       if (this.showTickMarks && this.tickLine != null) {
/* 451 */         this.tickLine.setUpperBound(((RangeSlider)getSkinnable()).getMax());
/*     */       }
/* 453 */       ((RangeSlider)getSkinnable()).requestLayout();
/* 454 */     } else if ("SHOW_TICK_MARKS".equals(p) || "SHOW_TICK_LABELS".equals(p)) {
/* 455 */       setShowTickMarks(((RangeSlider)getSkinnable()).isShowTickMarks(), ((RangeSlider)getSkinnable()).isShowTickLabels());
/* 456 */       if (!getChildren().contains(this.highThumb))
/* 457 */         getChildren().add(this.highThumb); 
/* 458 */       if (!getChildren().contains(this.rangeBar))
/* 459 */         getChildren().add(this.rangeBar); 
/* 460 */     } else if ("MAJOR_TICK_UNIT".equals(p)) {
/* 461 */       if (this.tickLine != null) {
/* 462 */         this.tickLine.setTickUnit(((RangeSlider)getSkinnable()).getMajorTickUnit());
/* 463 */         ((RangeSlider)getSkinnable()).requestLayout();
/*     */       } 
/* 465 */     } else if ("MINOR_TICK_COUNT".equals(p)) {
/* 466 */       if (this.tickLine != null) {
/* 467 */         this.tickLine.setMinorTickCount(Math.max(((RangeSlider)getSkinnable()).getMinorTickCount(), 0) + 1);
/* 468 */         ((RangeSlider)getSkinnable()).requestLayout();
/*     */       } 
/* 470 */     } else if ("LOW_VALUE".equals(p)) {
/* 471 */       positionLowThumb();
/* 472 */       this.rangeBar.resizeRelocate(this.rangeStart, this.rangeBar.getLayoutY(), this.rangeEnd - this.rangeStart, this.rangeBar
/* 473 */           .getHeight());
/* 474 */     } else if ("HIGH_VALUE".equals(p)) {
/* 475 */       positionHighThumb();
/* 476 */       this.rangeBar.resize(this.rangeEnd - this.rangeStart, this.rangeBar.getHeight());
/*     */     } 
/* 478 */     super.handleControlPropertyChanged(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getMaxMinusMinNoZero() {
/* 488 */     RangeSlider s = (RangeSlider)getSkinnable();
/* 489 */     return (s.getMax() - s.getMin() == 0.0D) ? 1.0D : (s.getMax() - s.getMin());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void positionLowThumb() {
/* 496 */     RangeSlider s = (RangeSlider)getSkinnable();
/* 497 */     boolean horizontal = isHorizontal();
/*     */     
/* 499 */     double lx = horizontal ? (this.trackStart + this.trackLength * (s.getLowValue() - s.getMin()) / getMaxMinusMinNoZero() - this.thumbWidth / 2.0D) : this.lowThumbPos;
/*     */ 
/*     */     
/* 502 */     double ly = horizontal ? this.lowThumbPos : (((RangeSlider)getSkinnable()).getInsets().getTop() + this.trackLength - this.trackLength * (s.getLowValue() - s.getMin()) / getMaxMinusMinNoZero());
/* 503 */     this.lowThumb.setLayoutX(lx);
/* 504 */     this.lowThumb.setLayoutY(ly);
/* 505 */     if (horizontal) { this.rangeStart = lx + this.thumbWidth; } else { this.rangeEnd = ly; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void positionHighThumb() {
/* 512 */     RangeSlider slider = (RangeSlider)getSkinnable();
/* 513 */     boolean orientation = (((RangeSlider)getSkinnable()).getOrientation() == Orientation.HORIZONTAL);
/*     */     
/* 515 */     double thumbWidth = this.lowThumb.getWidth();
/* 516 */     double thumbHeight = this.lowThumb.getHeight();
/* 517 */     this.highThumb.resize(thumbWidth, thumbHeight);
/*     */     
/* 519 */     double pad = 0.0D;
/* 520 */     double trackStart = orientation ? this.track.getLayoutX() : this.track.getLayoutY();
/* 521 */     trackStart += pad;
/* 522 */     double trackLength = orientation ? this.track.getWidth() : this.track.getHeight();
/* 523 */     trackLength -= 2.0D * pad;
/*     */     
/* 525 */     double x = orientation ? (trackStart + trackLength * (slider.getHighValue() - slider.getMin()) / getMaxMinusMinNoZero() - thumbWidth / 2.0D) : this.lowThumb.getLayoutX();
/* 526 */     double y = orientation ? this.lowThumb.getLayoutY() : (((RangeSlider)getSkinnable()).getInsets().getTop() + trackLength - trackLength * (slider.getHighValue() - slider.getMin()) / getMaxMinusMinNoZero());
/* 527 */     this.highThumb.setLayoutX(x);
/* 528 */     this.highThumb.setLayoutY(y);
/* 529 */     if (orientation) { this.rangeEnd = x; } else { this.rangeStart = y + thumbWidth; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 535 */     this.thumbWidth = this.lowThumb.prefWidth(-1.0D);
/* 536 */     this.thumbHeight = this.lowThumb.prefHeight(-1.0D);
/* 537 */     this.lowThumb.resize(this.thumbWidth, this.thumbHeight);
/*     */ 
/*     */     
/* 540 */     double trackRadius = (this.track.getBackground() == null) ? 0.0D : ((this.track.getBackground().getFills().size() > 0) ? ((BackgroundFill)this.track.getBackground().getFills().get(0)).getRadii().getTopLeftHorizontalRadius() : 0.0D);
/*     */     
/* 542 */     if (isHorizontal()) {
/* 543 */       double tickLineHeight = this.showTickMarks ? this.tickLine.prefHeight(-1.0D) : 0.0D;
/* 544 */       double trackHeight = this.track.prefHeight(-1.0D);
/* 545 */       double trackAreaHeight = Math.max(trackHeight, this.thumbHeight);
/* 546 */       double totalHeightNeeded = trackAreaHeight + (this.showTickMarks ? (this.trackToTickGap + tickLineHeight) : 0.0D);
/* 547 */       double startY = y + (h - totalHeightNeeded) / 2.0D;
/* 548 */       this.trackLength = w - this.thumbWidth;
/* 549 */       this.trackStart = x + this.thumbWidth / 2.0D;
/* 550 */       double trackTop = (int)(startY + (trackAreaHeight - trackHeight) / 2.0D);
/* 551 */       this.lowThumbPos = (int)(startY + (trackAreaHeight - this.thumbHeight) / 2.0D);
/*     */       
/* 553 */       positionLowThumb();
/*     */       
/* 555 */       this.track.resizeRelocate(this.trackStart - trackRadius, trackTop, this.trackLength + trackRadius + trackRadius, trackHeight);
/* 556 */       positionHighThumb();
/*     */       
/* 558 */       this.rangeBar.resizeRelocate(this.rangeStart, trackTop, this.rangeEnd - this.rangeStart, trackHeight);
/*     */       
/* 560 */       if (this.showTickMarks) {
/* 561 */         this.tickLine.setLayoutX(this.trackStart);
/* 562 */         this.tickLine.setLayoutY(trackTop + trackHeight + this.trackToTickGap);
/* 563 */         this.tickLine.resize(this.trackLength, tickLineHeight);
/* 564 */         this.tickLine.requestAxisLayout();
/*     */       } else {
/* 566 */         if (this.tickLine != null) {
/* 567 */           this.tickLine.resize(0.0D, 0.0D);
/* 568 */           this.tickLine.requestAxisLayout();
/*     */         } 
/* 570 */         this.tickLine = null;
/*     */       } 
/*     */     } else {
/* 573 */       double tickLineWidth = this.showTickMarks ? this.tickLine.prefWidth(-1.0D) : 0.0D;
/* 574 */       double trackWidth = this.track.prefWidth(-1.0D);
/* 575 */       double trackAreaWidth = Math.max(trackWidth, this.thumbWidth);
/* 576 */       double totalWidthNeeded = trackAreaWidth + (this.showTickMarks ? (this.trackToTickGap + tickLineWidth) : 0.0D);
/* 577 */       double startX = x + (w - totalWidthNeeded) / 2.0D;
/* 578 */       this.trackLength = h - this.thumbHeight;
/* 579 */       this.trackStart = y + this.thumbHeight / 2.0D;
/* 580 */       double trackLeft = (int)(startX + (trackAreaWidth - trackWidth) / 2.0D);
/* 581 */       this.lowThumbPos = (int)(startX + (trackAreaWidth - this.thumbWidth) / 2.0D);
/*     */       
/* 583 */       positionLowThumb();
/*     */       
/* 585 */       this.track.resizeRelocate(trackLeft, this.trackStart - trackRadius, trackWidth, this.trackLength + trackRadius + trackRadius);
/* 586 */       positionHighThumb();
/*     */       
/* 588 */       this.rangeBar.resizeRelocate(trackLeft, this.rangeStart, trackWidth, this.rangeEnd - this.rangeStart);
/*     */       
/* 590 */       if (this.showTickMarks) {
/* 591 */         this.tickLine.setLayoutX(trackLeft + trackWidth + this.trackToTickGap);
/* 592 */         this.tickLine.setLayoutY(this.trackStart);
/* 593 */         this.tickLine.resize(tickLineWidth, this.trackLength);
/* 594 */         this.tickLine.requestAxisLayout();
/*     */       } else {
/* 596 */         if (this.tickLine != null) {
/* 597 */           this.tickLine.resize(0.0D, 0.0D);
/* 598 */           this.tickLine.requestAxisLayout();
/*     */         } 
/* 600 */         this.tickLine = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private double minTrackLength() {
/* 606 */     return 2.0D * this.lowThumb.prefWidth(-1.0D);
/*     */   }
/*     */   
/*     */   protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 610 */     if (isHorizontal()) {
/* 611 */       return leftInset + minTrackLength() + this.lowThumb.minWidth(-1.0D) + rightInset;
/*     */     }
/* 613 */     return leftInset + this.lowThumb.prefWidth(-1.0D) + rightInset;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 618 */     if (isHorizontal()) {
/* 619 */       return topInset + this.lowThumb.prefHeight(-1.0D) + bottomInset;
/*     */     }
/* 621 */     return topInset + minTrackLength() + this.lowThumb.prefHeight(-1.0D) + bottomInset;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 626 */     if (isHorizontal()) {
/* 627 */       if (this.showTickMarks) {
/* 628 */         return Math.max(140.0D, this.tickLine.prefWidth(-1.0D));
/*     */       }
/* 630 */       return 140.0D;
/*     */     } 
/*     */ 
/*     */     
/* 634 */     return leftInset + Math.max(this.lowThumb.prefWidth(-1.0D), this.track.prefWidth(-1.0D)) + (this.showTickMarks ? (this.trackToTickGap + this.tickLine
/* 635 */       .prefWidth(-1.0D)) : 0.0D) + rightInset;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 640 */     if (isHorizontal()) {
/* 641 */       return ((RangeSlider)getSkinnable()).getInsets().getTop() + Math.max(this.lowThumb.prefHeight(-1.0D), this.track.prefHeight(-1.0D)) + (this.showTickMarks ? (this.trackToTickGap + this.tickLine
/* 642 */         .prefHeight(-1.0D)) : 0.0D) + bottomInset;
/*     */     }
/* 644 */     if (this.showTickMarks) {
/* 645 */       return Math.max(140.0D, this.tickLine.prefHeight(-1.0D));
/*     */     }
/* 647 */     return 140.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 653 */     if (isHorizontal()) {
/* 654 */       return Double.MAX_VALUE;
/*     */     }
/* 656 */     return ((RangeSlider)getSkinnable()).prefWidth(-1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 661 */     if (isHorizontal()) {
/* 662 */       return ((RangeSlider)getSkinnable()).prefHeight(width);
/*     */     }
/* 664 */     return Double.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isHorizontal() {
/* 669 */     return (this.orientation == null || this.orientation == Orientation.HORIZONTAL);
/*     */   }
/*     */   private static class ThumbPane extends StackPane { private ThumbPane() {}
/*     */     
/*     */     public void setFocus(boolean value) {
/* 674 */       setFocused(value);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\RangeSliderSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */