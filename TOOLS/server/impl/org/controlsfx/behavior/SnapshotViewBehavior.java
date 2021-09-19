/*     */ package impl.org.controlsfx.behavior;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import impl.org.controlsfx.tools.rectangle.CoordinatePosition;
/*     */ import impl.org.controlsfx.tools.rectangle.CoordinatePositions;
/*     */ import impl.org.controlsfx.tools.rectangle.Rectangles2D;
/*     */ import impl.org.controlsfx.tools.rectangle.change.MoveChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.NewChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.Rectangle2DChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToEastChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToNorthChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToNortheastChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToNorthwestChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToSouthChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToSoutheastChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToSouthwestChangeStrategy;
/*     */ import impl.org.controlsfx.tools.rectangle.change.ToWestChangeStrategy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import javafx.event.EventType;
/*     */ import javafx.geometry.Bounds;
/*     */ import javafx.geometry.Point2D;
/*     */ import javafx.geometry.Rectangle2D;
/*     */ import javafx.scene.Cursor;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.input.MouseButton;
/*     */ import javafx.scene.input.MouseEvent;
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
/*     */ public class SnapshotViewBehavior
/*     */   extends BehaviorBase<SnapshotView>
/*     */ {
/*     */   private static final double RELATIVE_EDGE_TOLERANCE = 0.015D;
/*     */   private SelectionChange selectionChange;
/*     */   private final Consumer<Boolean> setSelectionChanging;
/*     */   
/*     */   public SnapshotViewBehavior(SnapshotView snapshotView) {
/* 105 */     super((Control)snapshotView, new ArrayList());
/* 106 */     this.setSelectionChanging = createSetSelectionChanging();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Consumer<Boolean> createSetSelectionChanging() {
/* 115 */     return changing -> ((SnapshotView)getControl()).getProperties().put(SnapshotView.SELECTION_CHANGING_PROPERTY_KEY, changing);
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
/*     */   public Cursor handleMouseEvent(MouseEvent mouseEvent) {
/* 133 */     Objects.requireNonNull(mouseEvent, "The argument 'mouseEvent' must not be null.");
/*     */     
/* 135 */     EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
/* 136 */     SelectionEvent selectionEvent = createSelectionEvent(mouseEvent);
/*     */     
/* 138 */     if (eventType == MouseEvent.MOUSE_MOVED) {
/* 139 */       return getCursor(selectionEvent);
/*     */     }
/* 141 */     if (eventType == MouseEvent.MOUSE_PRESSED) {
/* 142 */       return handleMousePressedEvent(selectionEvent);
/*     */     }
/* 144 */     if (eventType == MouseEvent.MOUSE_DRAGGED) {
/* 145 */       return handleMouseDraggedEvent(selectionEvent);
/*     */     }
/* 147 */     if (eventType == MouseEvent.MOUSE_RELEASED) {
/* 148 */       return handleMouseReleasedEvent(selectionEvent);
/*     */     }
/*     */     
/* 151 */     return Cursor.DEFAULT;
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
/*     */   private SelectionEvent createSelectionEvent(MouseEvent mouseEvent) {
/* 164 */     Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
/* 165 */     Rectangle2D selectionBounds = createBoundsForCurrentBoundary();
/* 166 */     CoordinatePosition position = computePosition(point);
/* 167 */     return new SelectionEvent(mouseEvent, point, selectionBounds, position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Rectangle2D createBoundsForCurrentBoundary() {
/*     */     boolean nodeExists;
/* 177 */     SnapshotView.Boundary boundary = ((SnapshotView)getControl()).getSelectionAreaBoundary();
/* 178 */     switch (boundary) {
/*     */       case IN_RECTANGLE:
/* 180 */         return new Rectangle2D(0.0D, 0.0D, getControlWidth(), getControlHeight());
/*     */       case OUT_OF_RECTANGLE:
/* 182 */         nodeExists = (getNode() != null);
/* 183 */         if (nodeExists) {
/* 184 */           Bounds nodeBounds = getNode().getBoundsInParent();
/* 185 */           return Rectangles2D.fromBounds(nodeBounds);
/*     */         } 
/* 187 */         return Rectangle2D.EMPTY;
/*     */     } 
/*     */     
/* 190 */     throw new IllegalArgumentException("The boundary " + boundary + " is not fully implemented.");
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
/*     */   private CoordinatePosition computePosition(Point2D point) {
/* 204 */     boolean noSelection = (!((SnapshotView)getControl()).hasSelection() || !((SnapshotView)getControl()).isSelectionActive());
/* 205 */     boolean controlHasNoSpace = (getControlWidth() == 0.0D || getControlHeight() == 0.0D);
/* 206 */     if (noSelection || controlHasNoSpace) {
/* 207 */       return CoordinatePosition.OUT_OF_RECTANGLE;
/*     */     }
/*     */     
/* 210 */     double tolerance = computeTolerance();
/* 211 */     return computePosition(getSelection(), point, tolerance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double computeTolerance() {
/* 220 */     double controlMeanLength = Math.sqrt(getControlWidth() * getControlHeight());
/* 221 */     return 0.015D * controlMeanLength;
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
/*     */   private static CoordinatePosition computePosition(Rectangle2D selection, Point2D point, double tolerance) {
/* 238 */     CoordinatePosition onEdge = CoordinatePositions.onEdges(selection, point, tolerance);
/* 239 */     if (onEdge != null) {
/* 240 */       return onEdge;
/*     */     }
/* 242 */     return CoordinatePositions.inRectangle(selection, point);
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
/*     */   private Cursor handleMousePressedEvent(SelectionEvent selectionEvent) {
/* 257 */     if (selectionEvent.isPointInSelectionBounds()) {
/*     */       
/* 259 */       Cursor cursor = getCursor(selectionEvent);
/* 260 */       Rectangle2DChangeStrategy selectionChangeStrategy = getChangeStrategy(selectionEvent);
/* 261 */       boolean deactivateSelectionIfClick = willDeactivateSelectionIfClick(selectionEvent);
/*     */ 
/*     */       
/* 264 */       this
/* 265 */         .selectionChange = new SelectionChangeByStrategy((SnapshotView)getControl(), this.setSelectionChanging, selectionChangeStrategy, cursor, deactivateSelectionIfClick);
/* 266 */       this.selectionChange.beginSelectionChange(selectionEvent.getPoint());
/*     */     } else {
/*     */       
/* 269 */       this.selectionChange = NoSelectionChange.INSTANCE;
/*     */     } 
/*     */     
/* 272 */     return this.selectionChange.getCursor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Cursor handleMouseDraggedEvent(SelectionEvent selectionEvent) {
/* 282 */     this.selectionChange.continueSelectionChange(selectionEvent.getPoint());
/* 283 */     return this.selectionChange.getCursor();
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
/*     */   private Cursor handleMouseReleasedEvent(SelectionEvent selectionEvent) {
/* 296 */     this.selectionChange.endSelectionChange(selectionEvent.getPoint());
/* 297 */     this.selectionChange = null;
/*     */     
/* 299 */     return getCursor(selectionEvent);
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
/*     */   private Cursor getCursor(SelectionEvent selectionEvent) {
/* 313 */     if (!selectionEvent.isPointInSelectionBounds()) {
/* 314 */       return getRegularCursor();
/*     */     }
/*     */ 
/*     */     
/* 318 */     switch (selectionEvent.getPosition()) {
/*     */       case IN_RECTANGLE:
/* 320 */         return Cursor.MOVE;
/*     */       case OUT_OF_RECTANGLE:
/* 322 */         return getRegularCursor();
/*     */       case NORTH_EDGE:
/* 324 */         return Cursor.N_RESIZE;
/*     */       case NORTHEAST_EDGE:
/* 326 */         return Cursor.NE_RESIZE;
/*     */       case EAST_EDGE:
/* 328 */         return Cursor.E_RESIZE;
/*     */       case SOUTHEAST_EDGE:
/* 330 */         return Cursor.SE_RESIZE;
/*     */       case SOUTH_EDGE:
/* 332 */         return Cursor.S_RESIZE;
/*     */       case SOUTHWEST_EDGE:
/* 334 */         return Cursor.SW_RESIZE;
/*     */       case WEST_EDGE:
/* 336 */         return Cursor.W_RESIZE;
/*     */       case NORTHWEST_EDGE:
/* 338 */         return Cursor.NW_RESIZE;
/*     */     } 
/* 340 */     throw new IllegalArgumentException("The position " + selectionEvent.getPosition() + " is not fully implemented.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Cursor getRegularCursor() {
/* 349 */     return ((SnapshotView)getControl()).getCursor();
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
/*     */   private Rectangle2DChangeStrategy getChangeStrategy(SelectionEvent selectionEvent) {
/* 363 */     boolean mousePressed = (selectionEvent.getMouseEvent().getEventType() == MouseEvent.MOUSE_PRESSED);
/* 364 */     if (!mousePressed) {
/* 365 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 368 */     Rectangle2D selectionBounds = selectionEvent.getSelectionBounds();
/*     */     
/* 370 */     switch (selectionEvent.getPosition()) {
/*     */       case IN_RECTANGLE:
/* 372 */         return (Rectangle2DChangeStrategy)new MoveChangeStrategy(getSelection(), selectionBounds);
/*     */       case OUT_OF_RECTANGLE:
/* 374 */         return (Rectangle2DChangeStrategy)new NewChangeStrategy(
/* 375 */             isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case NORTH_EDGE:
/* 377 */         return (Rectangle2DChangeStrategy)new ToNorthChangeStrategy(
/* 378 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case NORTHEAST_EDGE:
/* 380 */         return (Rectangle2DChangeStrategy)new ToNortheastChangeStrategy(
/* 381 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case EAST_EDGE:
/* 383 */         return (Rectangle2DChangeStrategy)new ToEastChangeStrategy(
/* 384 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case SOUTHEAST_EDGE:
/* 386 */         return (Rectangle2DChangeStrategy)new ToSoutheastChangeStrategy(
/* 387 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case SOUTH_EDGE:
/* 389 */         return (Rectangle2DChangeStrategy)new ToSouthChangeStrategy(
/* 390 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case SOUTHWEST_EDGE:
/* 392 */         return (Rectangle2DChangeStrategy)new ToSouthwestChangeStrategy(
/* 393 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case WEST_EDGE:
/* 395 */         return (Rectangle2DChangeStrategy)new ToWestChangeStrategy(
/* 396 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */       case NORTHWEST_EDGE:
/* 398 */         return (Rectangle2DChangeStrategy)new ToNorthwestChangeStrategy(
/* 399 */             getSelection(), isSelectionRatioFixed(), getSelectionRatio(), selectionBounds);
/*     */     } 
/* 401 */     throw new IllegalArgumentException("The position " + selectionEvent.getPosition() + " is not fully implemented.");
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
/*     */   private static boolean willDeactivateSelectionIfClick(SelectionEvent selectionEvent) {
/* 415 */     boolean rightClick = (selectionEvent.getMouseEvent().getButton() == MouseButton.SECONDARY);
/* 416 */     boolean outOfAreaClick = (selectionEvent.getPosition() == CoordinatePosition.OUT_OF_RECTANGLE);
/*     */     
/* 418 */     return (rightClick || outOfAreaClick);
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
/*     */   private double getControlWidth() {
/* 433 */     return ((SnapshotView)getControl()).getWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getControlHeight() {
/* 442 */     return ((SnapshotView)getControl()).getHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Node getNode() {
/* 451 */     return ((SnapshotView)getControl()).getNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Rectangle2D getSelection() {
/* 460 */     return ((SnapshotView)getControl()).getSelection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSelectionRatioFixed() {
/* 468 */     return ((SnapshotView)getControl()).isSelectionRatioFixed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getSelectionRatio() {
/* 477 */     return ((SnapshotView)getControl()).getFixedSelectionRatio();
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
/*     */   private static class SelectionEvent
/*     */   {
/*     */     private final MouseEvent mouseEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Point2D point;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Rectangle2D selectionBounds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final CoordinatePosition position;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SelectionEvent(MouseEvent mouseEvent, Point2D point, Rectangle2D selectionBounds, CoordinatePosition position) {
/* 527 */       this.mouseEvent = mouseEvent;
/* 528 */       this.point = point;
/* 529 */       this.selectionBounds = selectionBounds;
/* 530 */       this.position = position;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MouseEvent getMouseEvent() {
/* 537 */       return this.mouseEvent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Point2D getPoint() {
/* 544 */       return this.point;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Rectangle2D getSelectionBounds() {
/* 551 */       return this.selectionBounds;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isPointInSelectionBounds() {
/* 559 */       return this.selectionBounds.contains(this.point);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CoordinatePosition getPosition() {
/* 566 */       return this.position;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NoSelectionChange
/*     */     implements SelectionChange
/*     */   {
/* 617 */     public static final NoSelectionChange INSTANCE = new NoSelectionChange();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void beginSelectionChange(Point2D point) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void continueSelectionChange(Point2D point) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void endSelectionChange(Point2D point) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cursor getCursor() {
/* 643 */       return Cursor.DEFAULT;
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
/*     */   private static class SelectionChangeByStrategy
/*     */     implements SelectionChange
/*     */   {
/*     */     private final SnapshotView snapshotView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Consumer<Boolean> setSelectionChanging;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Rectangle2DChangeStrategy selectionChangeStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Cursor cursor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final boolean deactivateSelectionIfClick;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Point2D startingPoint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean mouseMoved;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SelectionChangeByStrategy(SnapshotView snapshotView, Consumer<Boolean> setSelectionChanging, Rectangle2DChangeStrategy selectionChangeStrategy, Cursor cursor, boolean deactivateSelectionIfClick) {
/* 716 */       this.snapshotView = snapshotView;
/* 717 */       this.setSelectionChanging = setSelectionChanging;
/* 718 */       this.selectionChangeStrategy = selectionChangeStrategy;
/* 719 */       this.cursor = cursor;
/* 720 */       this.deactivateSelectionIfClick = deactivateSelectionIfClick;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void beginSelectionChange(Point2D point) {
/* 727 */       this.startingPoint = point;
/* 728 */       this.setSelectionChanging.accept(Boolean.valueOf(true));
/*     */       
/* 730 */       Rectangle2D newSelection = this.selectionChangeStrategy.beginChange(point);
/* 731 */       this.snapshotView.setSelection(newSelection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void continueSelectionChange(Point2D point) {
/* 736 */       updateMouseMoved(point);
/*     */       
/* 738 */       Rectangle2D newSelection = this.selectionChangeStrategy.continueChange(point);
/* 739 */       this.snapshotView.setSelection(newSelection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endSelectionChange(Point2D point) {
/* 744 */       updateMouseMoved(point);
/*     */       
/* 746 */       Rectangle2D newSelection = this.selectionChangeStrategy.endChange(point);
/* 747 */       this.snapshotView.setSelection(newSelection);
/*     */       
/* 749 */       boolean deactivateSelection = (this.deactivateSelectionIfClick && !this.mouseMoved);
/* 750 */       if (deactivateSelection) {
/* 751 */         this.snapshotView.setSelection(null);
/*     */       }
/* 753 */       this.setSelectionChanging.accept(Boolean.valueOf(false));
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
/*     */     private void updateMouseMoved(Point2D point) {
/* 765 */       if (this.mouseMoved) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 770 */       boolean mouseMovedNow = !this.startingPoint.equals(point);
/* 771 */       this.mouseMoved = mouseMovedNow;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cursor getCursor() {
/* 778 */       return this.cursor;
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface SelectionChange {
/*     */     void beginSelectionChange(Point2D param1Point2D);
/*     */     
/*     */     void continueSelectionChange(Point2D param1Point2D);
/*     */     
/*     */     void endSelectionChange(Point2D param1Point2D);
/*     */     
/*     */     Cursor getCursor();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\behavior\SnapshotViewBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */