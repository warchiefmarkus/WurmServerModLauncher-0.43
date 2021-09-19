/*      */ package org.controlsfx.control;
/*      */ 
/*      */ import impl.org.controlsfx.skin.SnapshotViewSkin;
/*      */ import impl.org.controlsfx.tools.rectangle.Rectangles2D;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import javafx.beans.binding.Bindings;
/*      */ import javafx.beans.property.BooleanProperty;
/*      */ import javafx.beans.property.DoubleProperty;
/*      */ import javafx.beans.property.ObjectProperty;
/*      */ import javafx.beans.property.Property;
/*      */ import javafx.beans.property.ReadOnlyBooleanProperty;
/*      */ import javafx.beans.property.SimpleBooleanProperty;
/*      */ import javafx.beans.property.SimpleDoubleProperty;
/*      */ import javafx.beans.property.SimpleObjectProperty;
/*      */ import javafx.beans.value.ChangeListener;
/*      */ import javafx.beans.value.ObservableBooleanValue;
/*      */ import javafx.beans.value.ObservableObjectValue;
/*      */ import javafx.beans.value.ObservableValue;
/*      */ import javafx.collections.MapChangeListener;
/*      */ import javafx.collections.ObservableMap;
/*      */ import javafx.css.CssMetaData;
/*      */ import javafx.css.StyleConverter;
/*      */ import javafx.css.Styleable;
/*      */ import javafx.css.StyleableDoubleProperty;
/*      */ import javafx.css.StyleableObjectProperty;
/*      */ import javafx.css.StyleableProperty;
/*      */ import javafx.geometry.Bounds;
/*      */ import javafx.geometry.Point2D;
/*      */ import javafx.geometry.Rectangle2D;
/*      */ import javafx.scene.Node;
/*      */ import javafx.scene.SnapshotParameters;
/*      */ import javafx.scene.control.Control;
/*      */ import javafx.scene.control.Skin;
/*      */ import javafx.scene.image.WritableImage;
/*      */ import javafx.scene.paint.Color;
/*      */ import javafx.scene.paint.Paint;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SnapshotView
/*      */   extends ControlsFXControl
/*      */ {
/*      */   public static final double MAX_SELECTION_RATIO_DIVERGENCE = 1.0E-6D;
/*  156 */   public static final String SELECTION_CHANGING_PROPERTY_KEY = SnapshotView.class
/*  157 */     .getCanonicalName() + ".selection_changing";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Node> node;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Rectangle2D> selection;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BooleanProperty hasSelection;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BooleanProperty selectionActive;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BooleanProperty selectionChanging;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BooleanProperty selectionRatioFixed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final DoubleProperty fixedSelectionRatio;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Boundary> selectionAreaBoundary;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BooleanProperty selectionActivityManaged;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BooleanProperty selectionMouseTransparent;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Boundary> unselectedAreaBoundary;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Paint> selectionBorderPaint;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final DoubleProperty selectionBorderWidth;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Paint> selectionAreaFill;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ObjectProperty<Paint> unselectedAreaFill;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DEFAULT_STYLE_CLASS = "snapshot-view";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SnapshotView() {
/*  258 */     getStyleClass().setAll((Object[])new String[] { "snapshot-view" });
/*      */ 
/*      */     
/*  261 */     this.node = (ObjectProperty<Node>)new SimpleObjectProperty(this, "node");
/*      */ 
/*      */     
/*  264 */     this.selection = (ObjectProperty<Rectangle2D>)new SimpleObjectProperty<Rectangle2D>(this, "selection")
/*      */       {
/*      */         public void set(Rectangle2D selection) {
/*  267 */           if (!SnapshotView.this.isSelectionValid(selection)) {
/*  268 */             throw new IllegalArgumentException("The selection \"" + selection + "\" is invalid. Check the comment on 'SnapshotView.selectionProperty()' for all criteria a selection must fulfill.");
/*      */           }
/*      */ 
/*      */           
/*  272 */           super.set(selection);
/*      */         }
/*      */       };
/*  275 */     this.hasSelection = (BooleanProperty)new SimpleBooleanProperty(this, "hasSelection", false);
/*  276 */     this.hasSelection.bind((ObservableValue)Bindings.and((ObservableBooleanValue)Bindings.isNotNull((ObservableObjectValue)this.selection), (ObservableBooleanValue)Bindings.notEqual(Rectangle2D.EMPTY, (ObservableObjectValue)this.selection)));
/*  277 */     this.selectionActive = (BooleanProperty)new SimpleBooleanProperty(this, "selectionActive", false);
/*  278 */     this.selectionChanging = (BooleanProperty)new SimpleBooleanProperty(this, "selectionChanging", false);
/*      */     
/*  280 */     this.selectionRatioFixed = (BooleanProperty)new SimpleBooleanProperty(this, "selectionRatioFixed", false);
/*  281 */     this.fixedSelectionRatio = (DoubleProperty)new SimpleDoubleProperty(this, "fixedSelectionRatio", 1.0D)
/*      */       {
/*      */         public void set(double newValue) {
/*  284 */           if (newValue <= 0.0D) {
/*  285 */             throw new IllegalArgumentException("The fixed selection ratio must be positive.");
/*      */           }
/*  287 */           super.set(newValue);
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*  292 */     this.selectionAreaBoundary = createStylableObjectProperty(this, "selectionAreaBoundary", Boundary.CONTROL, (CssMetaData)Css.SELECTION_AREA_BOUNDARY);
/*      */     
/*  294 */     this.selectionActivityManaged = (BooleanProperty)new SimpleBooleanProperty(this, "selectionActivityManaged", true);
/*  295 */     this.selectionMouseTransparent = (BooleanProperty)new SimpleBooleanProperty(this, "selectionMouseTransparent", false);
/*      */ 
/*      */     
/*  298 */     this.unselectedAreaBoundary = createStylableObjectProperty(this, "unselectedAreaBoundary", Boundary.CONTROL, (CssMetaData)Css.UNSELECTED_AREA_BOUNDARY);
/*      */     
/*  300 */     this.selectionBorderPaint = createStylableObjectProperty(this, "selectionBorderPaint", Color.WHITESMOKE, (CssMetaData)Css.SELECTION_BORDER_PAINT);
/*      */     
/*  302 */     this.selectionBorderWidth = (DoubleProperty)createStylableDoubleProperty(this, "selectionBorderWidth", 2.5D, (CssMetaData)Css.SELECTION_BORDER_WIDTH);
/*      */     
/*  304 */     this.selectionAreaFill = createStylableObjectProperty(this, "selectionAreaFill", Color.TRANSPARENT, (CssMetaData)Css.SELECTION_AREA_FILL);
/*      */     
/*  306 */     this.unselectedAreaFill = createStylableObjectProperty(this, "unselectedAreaFill", new Color(0.0D, 0.0D, 0.0D, 0.5D), (CssMetaData)Css.UNSELECTED_AREA_FILL);
/*      */ 
/*      */     
/*  309 */     addStateUpdatingListeners();
/*      */     
/*  311 */     (new SelectionSizeUpdater()).enableResizing();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addStateUpdatingListeners() {
/*  319 */     this.selection.addListener((o, oldValue, newValue) -> updateSelectionActivityState());
/*      */ 
/*      */     
/*  322 */     this.selectionRatioFixed.addListener((o, oldValue, newValue) -> {
/*  323 */           boolean valueChangedToTrue = (!oldValue.booleanValue() && newValue.booleanValue());
/*      */           if (valueChangedToTrue) {
/*      */             fixSelectionRatio();
/*      */           }
/*      */         });
/*  328 */     this.fixedSelectionRatio.addListener((o, oldValue, newValue) -> {
/*      */           if (isSelectionRatioFixed()) {
/*      */             fixSelectionRatio();
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*  335 */     listenToProperty(
/*  336 */         getProperties(), SELECTION_CHANGING_PROPERTY_KEY, value -> this.selectionChanging.set(value.booleanValue()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> void listenToProperty(ObservableMap<Object, Object> properties, Object key, Consumer<T> processValue) {
/*  354 */     Objects.requireNonNull(properties, "The argument 'properties' must not be null.");
/*  355 */     Objects.requireNonNull(key, "The argument 'key' must not be null.");
/*  356 */     Objects.requireNonNull(processValue, "The argument 'processValue' must not be null.");
/*      */ 
/*      */     
/*  359 */     MapChangeListener<Object, Object> listener = change -> {
/*  360 */         boolean addedForKey = (change.wasAdded() && Objects.equals(key, change.getKey()));
/*      */ 
/*      */ 
/*      */         
/*      */         if (addedForKey) {
/*      */           try {
/*      */             T newValue = (T)change.getValueAdded();
/*      */ 
/*      */             
/*      */             processValue.accept(newValue);
/*  370 */           } catch (ClassCastException classCastException) {}
/*      */ 
/*      */ 
/*      */           
/*      */           properties.remove(key);
/*      */         } 
/*      */       };
/*      */ 
/*      */     
/*  379 */     properties.addListener(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SnapshotView(Node node) {
/*  389 */     this();
/*  390 */     setNode(node);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle2D transformSelectionToNodeCoordinates() {
/*  410 */     if (!hasSelection()) {
/*  411 */       throw new IllegalStateException("The selection can not be transformed if it does not exist (check 'hasSelection()').");
/*      */     }
/*      */ 
/*      */     
/*  415 */     return transformToNodeCoordinates(getSelection());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle2D transformToNodeCoordinates(Rectangle2D area) throws IllegalStateException {
/*  431 */     Objects.requireNonNull(area, "The argument 'area' must not be null.");
/*  432 */     if (getNode() == null) {
/*  433 */       throw new IllegalStateException("The selection can not be transformed if the node is null (check 'getNode()').");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  438 */     Bounds nodeBounds = getNode().getBoundsInParent();
/*  439 */     double xOffset = nodeBounds.getMinX();
/*  440 */     double yOffset = nodeBounds.getMinY();
/*      */ 
/*      */     
/*  443 */     double minX = area.getMinX() - xOffset;
/*  444 */     double minY = area.getMinY() - yOffset;
/*      */     
/*  446 */     return new Rectangle2D(minX, minY, area.getWidth(), area.getHeight());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WritableImage createSnapshot() throws IllegalStateException {
/*  460 */     if (getNode() == null) {
/*  461 */       throw new IllegalStateException("No snapshot can be created if the node is null (check 'getNode()').");
/*      */     }
/*  463 */     if (!hasSelection()) {
/*  464 */       throw new IllegalStateException("No snapshot can be created if there is no selection (check 'hasSelection()').");
/*      */     }
/*      */ 
/*      */     
/*  468 */     SnapshotParameters parameters = new SnapshotParameters();
/*  469 */     parameters.setViewport(getSelection());
/*  470 */     return createSnapshot(parameters);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WritableImage createSnapshot(SnapshotParameters parameters) throws IllegalStateException {
/*  486 */     Objects.requireNonNull(parameters, "The argument 'parameters' must not be null.");
/*  487 */     if (getNode() == null) {
/*  488 */       throw new IllegalStateException("No snapshot can be created if the node is null (check 'getNode()').");
/*      */     }
/*      */ 
/*      */     
/*  492 */     return getNode().snapshot(parameters, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateSelectionActivityState() {
/*  507 */     boolean userManaged = !isSelectionActivityManaged();
/*  508 */     if (userManaged) {
/*      */       return;
/*      */     }
/*      */     
/*  512 */     boolean selectionActive = (getSelection() != null && getSelection() != Rectangle2D.EMPTY);
/*  513 */     setSelectionActive(selectionActive);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fixSelectionRatio() {
/*  520 */     boolean noSelectionToFix = (getNode() == null || !hasSelection());
/*  521 */     if (noSelectionToFix) {
/*      */       return;
/*      */     }
/*      */     
/*  525 */     Rectangle2D selectionBounds = getSelectionBounds();
/*  526 */     Rectangle2D resizedSelection = Rectangles2D.fixRatioWithinBounds(
/*  527 */         getSelection(), getFixedSelectionRatio(), selectionBounds);
/*      */     
/*  529 */     this.selection.set(resizedSelection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Rectangle2D getSelectionBounds() {
/*  538 */     Boundary boundary = getSelectionAreaBoundary();
/*  539 */     switch (boundary) {
/*      */       case CONTROL:
/*  541 */         return new Rectangle2D(0.0D, 0.0D, getWidth(), getHeight());
/*      */       case NODE:
/*  543 */         return Rectangles2D.fromBounds(getNode().getBoundsInParent());
/*      */     } 
/*  545 */     throw new IllegalArgumentException("The boundary '" + boundary + "' is not fully implemented yet.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isSelectionValid(Rectangle2D selection) {
/*  559 */     boolean emptySelection = (selection == null || selection == Rectangle2D.EMPTY);
/*  560 */     if (emptySelection) {
/*  561 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  565 */     if (!valuesFinite(selection)) {
/*  566 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  570 */     if (!inBounds(selection)) {
/*  571 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  575 */     if (!hasCorrectRatio(selection)) {
/*  576 */       return false;
/*      */     }
/*      */     
/*  579 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean valuesFinite(Rectangle2D selection) {
/*  590 */     return (Double.isFinite(selection.getMinX()) && Double.isFinite(selection.getMinY()) && 
/*  591 */       Double.isFinite(selection.getWidth()) && Double.isFinite(selection.getHeight()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean inBounds(Rectangle2D selection) {
/*  603 */     Boundary boundary = getSelectionAreaBoundary();
/*  604 */     switch (boundary) {
/*      */       case CONTROL:
/*  606 */         return inBounds(selection, getBoundsInLocal());
/*      */       case NODE:
/*  608 */         if (getNode() == null) {
/*  609 */           return false;
/*      */         }
/*  611 */         return inBounds(selection, getNode().getBoundsInParent());
/*      */     } 
/*      */     
/*  614 */     throw new IllegalArgumentException("The boundary '" + boundary + "' is not fully implemented yet.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean inBounds(Rectangle2D selection, Bounds bounds) {
/*  628 */     return (bounds.getMinX() <= selection.getMinX() && bounds.getMinY() <= selection.getMinY() && selection
/*  629 */       .getMaxX() <= bounds.getMaxX() && selection.getMaxY() <= bounds.getMaxY());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasCorrectRatio(Rectangle2D selection) {
/*  641 */     if (!isSelectionRatioFixed()) {
/*  642 */       return true;
/*      */     }
/*      */     
/*  645 */     double ratio = selection.getWidth() / selection.getHeight();
/*      */     
/*  647 */     double ratioDivergence = Math.abs(1.0D - ratio / getFixedSelectionRatio());
/*  648 */     return (ratioDivergence <= 1.0E-6D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserAgentStylesheet() {
/*  665 */     return getUserAgentStylesheet(SnapshotView.class, "snapshot-view.css");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StyleableDoubleProperty createStylableDoubleProperty(final Object bean, final String name, double initialValue, final CssMetaData<? extends Styleable, Number> cssMetaData) {
/*  684 */     return new StyleableDoubleProperty(initialValue)
/*      */       {
/*      */         public Object getBean()
/*      */         {
/*  688 */           return bean;
/*      */         }
/*      */ 
/*      */         
/*      */         public String getName() {
/*  693 */           return name;
/*      */         }
/*      */ 
/*      */         
/*      */         public CssMetaData<? extends Styleable, Number> getCssMetaData() {
/*  698 */           return cssMetaData;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> StyleableObjectProperty<T> createStylableObjectProperty(final Object bean, final String name, T initialValue, final CssMetaData<? extends Styleable, T> cssMetaData) {
/*  720 */     return new StyleableObjectProperty<T>(initialValue)
/*      */       {
/*      */         public Object getBean()
/*      */         {
/*  724 */           return bean;
/*      */         }
/*      */ 
/*      */         
/*      */         public String getName() {
/*  729 */           return name;
/*      */         }
/*      */ 
/*      */         
/*      */         public CssMetaData<? extends Styleable, T> getCssMetaData() {
/*  734 */           return cssMetaData;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <S extends Styleable, T> CssMetaData<S, T> createCssMetaData(final Function<S, Property<T>> getProperty, String cssPropertyName, StyleConverter<?, T> styleConverter) {
/*  755 */     return new CssMetaData<S, T>(cssPropertyName, styleConverter)
/*      */       {
/*      */         public boolean isSettable(S styleable)
/*      */         {
/*  759 */           Property<T> property = getProperty.apply(styleable);
/*  760 */           return (property != null && !property.isBound());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public StyleableProperty<T> getStyleableProperty(S styleable) {
/*  766 */           return getProperty.apply(styleable);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Css
/*      */   {
/*      */     public static final CssMetaData<SnapshotView, SnapshotView.Boundary> SELECTION_AREA_BOUNDARY;
/*      */     public static final CssMetaData<SnapshotView, SnapshotView.Boundary> UNSELECTED_AREA_BOUNDARY;
/*      */     public static final CssMetaData<SnapshotView, Paint> SELECTION_BORDER_PAINT;
/*      */     
/*      */     static {
/*  779 */       SELECTION_AREA_BOUNDARY = (CssMetaData)SnapshotView.createCssMetaData(snapshotView -> snapshotView.selectionAreaBoundary, "-fx-selection-area-boundary", 
/*      */           
/*  781 */           StyleConverter.getEnumConverter(SnapshotView.Boundary.class));
/*      */ 
/*      */       
/*  784 */       UNSELECTED_AREA_BOUNDARY = (CssMetaData)SnapshotView.createCssMetaData(snapshotView -> snapshotView.unselectedAreaBoundary, "-fx-unselected-area-boundary", 
/*      */           
/*  786 */           StyleConverter.getEnumConverter(SnapshotView.Boundary.class));
/*      */ 
/*      */       
/*  789 */       SELECTION_BORDER_PAINT = (CssMetaData)SnapshotView.createCssMetaData(snapshotView -> snapshotView.selectionBorderPaint, "-fx-selection-border-paint", 
/*      */           
/*  791 */           StyleConverter.getPaintConverter());
/*      */ 
/*      */       
/*  794 */       SELECTION_BORDER_WIDTH = (CssMetaData)SnapshotView.createCssMetaData(snapshotView -> snapshotView.selectionBorderWidth, "-fx-selection-border-width", 
/*      */           
/*  796 */           StyleConverter.getSizeConverter());
/*      */ 
/*      */       
/*  799 */       SELECTION_AREA_FILL = (CssMetaData)SnapshotView.createCssMetaData(snapshotView -> snapshotView.selectionAreaFill, "-fx-selection-area-fill", 
/*      */           
/*  801 */           StyleConverter.getPaintConverter());
/*      */ 
/*      */       
/*  804 */       UNSELECTED_AREA_FILL = (CssMetaData)SnapshotView.createCssMetaData(snapshotView -> snapshotView.unselectedAreaFill, "-fx-unselected-area-fill", 
/*      */           
/*  806 */           StyleConverter.getPaintConverter());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  815 */       List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
/*  816 */       styleables.add(SELECTION_AREA_BOUNDARY);
/*  817 */       styleables.add(UNSELECTED_AREA_BOUNDARY);
/*  818 */       styleables.add(SELECTION_BORDER_PAINT);
/*  819 */       styleables.add(SELECTION_BORDER_WIDTH);
/*  820 */       styleables.add(SELECTION_AREA_FILL);
/*  821 */       styleables.add(UNSELECTED_AREA_FILL);
/*  822 */       CSS_META_DATA = Collections.unmodifiableList(styleables);
/*      */     }
/*      */     
/*      */     public static final CssMetaData<SnapshotView, Number> SELECTION_BORDER_WIDTH;
/*      */     public static final CssMetaData<SnapshotView, Paint> SELECTION_AREA_FILL;
/*      */     public static final CssMetaData<SnapshotView, Paint> UNSELECTED_AREA_FILL;
/*      */     public static final List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA; }
/*      */   
/*      */   public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
/*  831 */     return Css.CSS_META_DATA;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
/*  836 */     return getClassCssMetaData();
/*      */   }
/*      */ 
/*      */   
/*      */   protected Skin<?> createDefaultSkin() {
/*  841 */     return (Skin<?>)new SnapshotViewSkin(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Node> nodeProperty() {
/*  865 */     return this.node;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Node getNode() {
/*  873 */     return (Node)nodeProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNode(Node node) {
/*  882 */     nodeProperty().set(node);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Rectangle2D> selectionProperty() {
/*  914 */     return this.selection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Rectangle2D getSelection() {
/*  922 */     return (Rectangle2D)selectionProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelection(Rectangle2D selection) {
/*  935 */     selectionProperty().set(selection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelection(double upperLeftX, double upperLeftY, double width, double height) {
/*  959 */     selectionProperty().set(new Rectangle2D(upperLeftX, upperLeftY, width, height));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ReadOnlyBooleanProperty hasSelectionProperty() {
/*  969 */     return (ReadOnlyBooleanProperty)this.hasSelection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean hasSelection() {
/*  977 */     return hasSelectionProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty selectionActiveProperty() {
/*  989 */     return this.selectionActive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSelectionActive() {
/*  997 */     return selectionActiveProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionActive(boolean selectionActive) {
/* 1006 */     selectionActiveProperty().set(selectionActive);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ReadOnlyBooleanProperty selectionChangingProperty() {
/* 1020 */     return (ReadOnlyBooleanProperty)this.selectionChanging;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSelectionChanging() {
/* 1028 */     return selectionChangingProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty selectionRatioFixedProperty() {
/* 1046 */     return this.selectionRatioFixed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSelectionRatioFixed() {
/* 1054 */     return selectionRatioFixedProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionRatioFixed(boolean selectionRatioFixed) {
/* 1063 */     selectionRatioFixedProperty().set(selectionRatioFixed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty fixedSelectionRatioProperty() {
/* 1083 */     return this.fixedSelectionRatio;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getFixedSelectionRatio() {
/* 1091 */     return fixedSelectionRatioProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFixedSelectionRatio(double fixedSelectionRatio) {
/* 1102 */     fixedSelectionRatioProperty().set(fixedSelectionRatio);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Boundary> selectionAreaBoundaryProperty() {
/* 1125 */     return this.selectionAreaBoundary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Boundary getSelectionAreaBoundary() {
/* 1132 */     return (Boundary)selectionAreaBoundaryProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionAreaBoundary(Boundary selectionAreaBoundary) {
/* 1140 */     selectionAreaBoundaryProperty().set(selectionAreaBoundary);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty selectionActivityManagedProperty() {
/* 1160 */     return this.selectionActivityManaged;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSelectionActivityManaged() {
/* 1168 */     return selectionActivityManagedProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionActivityManaged(boolean selectionActivityManaged) {
/* 1177 */     selectionActivityManagedProperty().set(selectionActivityManaged);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty selectionMouseTransparentProperty() {
/* 1191 */     return this.selectionMouseTransparent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSelectionMouseTransparent() {
/* 1199 */     return selectionMouseTransparentProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionMouseTransparent(boolean selectionMouseTransparent) {
/* 1208 */     selectionMouseTransparentProperty().set(selectionMouseTransparent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Boundary> unselectedAreaBoundaryProperty() {
/* 1228 */     return this.unselectedAreaBoundary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Boundary getUnselectedAreaBoundary() {
/* 1236 */     return (Boundary)unselectedAreaBoundaryProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setUnselectedAreaBoundary(Boundary unselectedAreaBoundary) {
/* 1245 */     unselectedAreaBoundaryProperty().set(unselectedAreaBoundary);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Paint> selectionBorderPaintProperty() {
/* 1256 */     return this.selectionBorderPaint;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Paint getSelectionBorderPaint() {
/* 1264 */     return (Paint)selectionBorderPaintProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionBorderPaint(Paint selectionBorderPaint) {
/* 1273 */     selectionBorderPaintProperty().set(selectionBorderPaint);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty selectionBorderWidthProperty() {
/* 1286 */     return this.selectionBorderWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getSelectionBorderWidth() {
/* 1294 */     return selectionBorderWidthProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionBorderWidth(double selectionBorderWidth) {
/* 1303 */     selectionBorderWidthProperty().set(selectionBorderWidth);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Paint> selectionAreaFillProperty() {
/* 1313 */     return this.selectionAreaFill;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Paint getSelectionAreaFill() {
/* 1321 */     return (Paint)selectionAreaFillProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSelectionAreaFill(Paint selectionAreaFill) {
/* 1330 */     selectionAreaFillProperty().set(selectionAreaFill);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Paint> unselectedAreaFillProperty() {
/* 1340 */     return this.unselectedAreaFill;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Paint getUnselectedAreaFill() {
/* 1348 */     return (Paint)unselectedAreaFillProperty().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setUnselectedAreaFill(Paint unselectedAreaFill) {
/* 1357 */     unselectedAreaFillProperty().set(unselectedAreaFill);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Boundary
/*      */   {
/* 1377 */     CONTROL,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1382 */     NODE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class SelectionSizeUpdater
/*      */   {
/* 1438 */     private final ChangeListener<Number> resizeSelectionToNewControlWidthListener = this::resizeSelectionToNewControlWidth;
/* 1439 */     private final ChangeListener<Number> resizeSelectionToNewControlHeightListener = this::resizeSelectionToNewControlHeight;
/* 1440 */     private final ChangeListener<Node> updateSelectionToNodeListener = this::updateSelectionToNewNode;
/* 1441 */     private final ChangeListener<Bounds> resizeSelectionToNewNodeBoundsListener = this::resizeSelectionToNewNodeBounds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void enableResizing() {
/* 1451 */       enableResizingForBoundary(SnapshotView.this.getSelectionAreaBoundary());
/* 1452 */       SnapshotView.this.selectionAreaBoundary.addListener((o, oldBoundary, newBoundary) -> enableResizingForBoundary(newBoundary));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void enableResizingForBoundary(SnapshotView.Boundary boundary) {
/* 1462 */       switch (boundary) {
/*      */         case CONTROL:
/* 1464 */           enableResizingForControl();
/*      */           return;
/*      */         case NODE:
/* 1467 */           enableResizingForNode();
/*      */           return;
/*      */       } 
/* 1470 */       throw new IllegalArgumentException("The boundary '" + boundary + "' is not fully implemented yet.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void enableResizingForControl() {
/* 1480 */       SnapshotView.this.node.removeListener(this.updateSelectionToNodeListener);
/* 1481 */       if (SnapshotView.this.getNode() != null) {
/* 1482 */         SnapshotView.this.getNode().boundsInParentProperty().removeListener(this.resizeSelectionToNewNodeBoundsListener);
/*      */       }
/*      */ 
/*      */       
/* 1486 */       SnapshotView.this.widthProperty().addListener(this.resizeSelectionToNewControlWidthListener);
/* 1487 */       SnapshotView.this.heightProperty().addListener(this.resizeSelectionToNewControlHeightListener);
/*      */       
/* 1489 */       resizeSelectionFromNodeToControl();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void enableResizingForNode() {
/* 1498 */       SnapshotView.this.widthProperty().removeListener(this.resizeSelectionToNewControlWidthListener);
/* 1499 */       SnapshotView.this.heightProperty().removeListener(this.resizeSelectionToNewControlHeightListener);
/*      */ 
/*      */       
/* 1502 */       if (SnapshotView.this.getNode() != null) {
/* 1503 */         SnapshotView.this.getNode().boundsInParentProperty().addListener(this.resizeSelectionToNewNodeBoundsListener);
/*      */       }
/* 1505 */       SnapshotView.this.node.addListener(this.updateSelectionToNodeListener);
/*      */       
/* 1507 */       resizeSelectionFromControlToNode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void resizeSelectionFromNodeToControl() {
/* 1517 */       if (SnapshotView.this.getNode() == null) {
/* 1518 */         SnapshotView.this.setSelection((Rectangle2D)null);
/*      */       } else {
/*      */         
/* 1521 */         Rectangle2D controlBounds = new Rectangle2D(0.0D, 0.0D, SnapshotView.this.getWidth(), SnapshotView.this.getHeight());
/* 1522 */         Rectangle2D nodeBounds = Rectangles2D.fromBounds(SnapshotView.this.getNode().getBoundsInParent());
/* 1523 */         resizeSelectionToNewBounds(nodeBounds, controlBounds);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void resizeSelectionToNewControlWidth(ObservableValue<? extends Number> o, Number oldWidth, Number newWidth) {
/* 1543 */       Rectangle2D oldBounds = new Rectangle2D(0.0D, 0.0D, oldWidth.doubleValue(), SnapshotView.this.getHeight());
/* 1544 */       Rectangle2D newBounds = new Rectangle2D(0.0D, 0.0D, newWidth.doubleValue(), SnapshotView.this.getHeight());
/* 1545 */       resizeSelectionToNewBounds(oldBounds, newBounds);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void resizeSelectionToNewControlHeight(ObservableValue<? extends Number> o, Number oldHeight, Number newHeight) {
/* 1564 */       Rectangle2D oldBounds = new Rectangle2D(0.0D, 0.0D, SnapshotView.this.getWidth(), oldHeight.doubleValue());
/* 1565 */       Rectangle2D newBounds = new Rectangle2D(0.0D, 0.0D, SnapshotView.this.getWidth(), newHeight.doubleValue());
/* 1566 */       resizeSelectionToNewBounds(oldBounds, newBounds);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void resizeSelectionFromControlToNode() {
/* 1576 */       if (SnapshotView.this.getNode() == null) {
/* 1577 */         SnapshotView.this.setSelection((Rectangle2D)null);
/*      */       } else {
/*      */         
/* 1580 */         Rectangle2D controlBounds = new Rectangle2D(0.0D, 0.0D, SnapshotView.this.getWidth(), SnapshotView.this.getHeight());
/* 1581 */         Rectangle2D nodeBounds = Rectangles2D.fromBounds(SnapshotView.this.getNode().getBoundsInParent());
/* 1582 */         resizeSelectionToNewBounds(controlBounds, nodeBounds);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void updateSelectionToNewNode(ObservableValue<? extends Node> o, Node oldNode, Node newNode) {
/* 1604 */       if (oldNode != null) {
/* 1605 */         oldNode.boundsInParentProperty().removeListener(this.resizeSelectionToNewNodeBoundsListener);
/*      */       }
/* 1607 */       if (newNode != null) {
/* 1608 */         newNode.boundsInParentProperty().addListener(this.resizeSelectionToNewNodeBoundsListener);
/*      */       }
/*      */ 
/*      */       
/* 1612 */       if (oldNode == null || newNode == null) {
/*      */         
/* 1614 */         SnapshotView.this.setSelection((Rectangle2D)null);
/*      */       } else {
/*      */         
/* 1617 */         resizeSelectionToNewNodeBounds(null, oldNode.getBoundsInParent(), newNode.getBoundsInParent());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void resizeSelectionToNewNodeBounds(ObservableValue<? extends Bounds> o, Bounds oldBounds, Bounds newBounds) {
/* 1635 */       resizeSelectionToNewBounds(Rectangles2D.fromBounds(oldBounds), Rectangles2D.fromBounds(newBounds));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void resizeSelectionToNewBounds(Rectangle2D oldBounds, Rectangle2D newBounds) {
/* 1650 */       if (!SnapshotView.this.hasSelection()) {
/*      */         return;
/*      */       }
/*      */       
/* 1654 */       Rectangle2D newSelection = transformSelectionToNewBounds(SnapshotView.this.getSelection(), oldBounds, newBounds);
/* 1655 */       if (SnapshotView.this.isSelectionValid(newSelection)) {
/* 1656 */         SnapshotView.this.setSelection(newSelection);
/*      */       } else {
/* 1658 */         SnapshotView.this.setSelection((Rectangle2D)null);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Rectangle2D transformSelectionToNewBounds(Rectangle2D oldSelection, Rectangle2D oldBounds, Rectangle2D newBounds) {
/* 1682 */       Point2D newSelectionCenter = computeNewSelectionCenter(oldSelection, oldBounds, newBounds);
/*      */       
/* 1684 */       double widthRatio = newBounds.getWidth() / oldBounds.getWidth();
/* 1685 */       double heightRatio = newBounds.getHeight() / oldBounds.getHeight();
/*      */       
/* 1687 */       if (SnapshotView.this.isSelectionRatioFixed()) {
/* 1688 */         double newArea = oldSelection.getWidth() * widthRatio * oldSelection.getHeight() * heightRatio;
/* 1689 */         double ratio = SnapshotView.this.getFixedSelectionRatio();
/* 1690 */         return Rectangles2D.forCenterAndAreaAndRatioWithinBounds(newSelectionCenter, newArea, ratio, newBounds);
/*      */       } 
/* 1692 */       double newWidth = oldSelection.getWidth() * widthRatio;
/* 1693 */       double newHeight = oldSelection.getHeight() * heightRatio;
/* 1694 */       return Rectangles2D.forCenterAndSize(newSelectionCenter, newWidth, newHeight);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Point2D computeNewSelectionCenter(Rectangle2D oldSelection, Rectangle2D oldBounds, Rectangle2D newBounds) {
/* 1716 */       Point2D oldSelectionCenter = Rectangles2D.getCenterPoint(oldSelection);
/* 1717 */       Point2D oldBoundsCenter = Rectangles2D.getCenterPoint(oldBounds);
/* 1718 */       Point2D oldSelectionCenterOffset = oldSelectionCenter.subtract(oldBoundsCenter);
/*      */       
/* 1720 */       double widthRatio = newBounds.getWidth() / oldBounds.getWidth();
/* 1721 */       double heightRatio = newBounds.getHeight() / oldBounds.getHeight();
/*      */ 
/*      */       
/* 1724 */       Point2D newSelectionCenterOffset = new Point2D(oldSelectionCenterOffset.getX() * widthRatio, oldSelectionCenterOffset.getY() * heightRatio);
/* 1725 */       Point2D newBoundsCenter = Rectangles2D.getCenterPoint(newBounds);
/* 1726 */       Point2D newSelectionCenter = newBoundsCenter.add(newSelectionCenterOffset);
/*      */       
/* 1728 */       return newSelectionCenter;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\SnapshotView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */