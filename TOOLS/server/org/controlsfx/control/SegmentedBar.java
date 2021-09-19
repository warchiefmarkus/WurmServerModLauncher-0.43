/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import com.sun.javafx.css.converters.EnumConverter;
/*     */ import impl.org.controlsfx.skin.SegmentedBarSkin;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.WeakInvalidationListener;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ListProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ReadOnlyDoubleProperty;
/*     */ import javafx.beans.property.ReadOnlyDoubleWrapper;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleListProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.css.CssMetaData;
/*     */ import javafx.css.PseudoClass;
/*     */ import javafx.css.StyleConverter;
/*     */ import javafx.css.Styleable;
/*     */ import javafx.css.StyleableObjectProperty;
/*     */ import javafx.css.StyleableProperty;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.OverrunStyle;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.util.Callback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SegmentedBar<T extends SegmentedBar.Segment>
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private static final String DEFAULT_STYLE = "segmented-bar";
/* 102 */   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
/*     */   
/* 104 */   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal"); private final ObjectProperty<Callback<T, Node>> infoNodeFactory;
/*     */   private ObjectProperty<Orientation> orientation;
/*     */   private final ObjectProperty<Callback<T, Node>> segmentViewFactory;
/*     */   private final ListProperty<T> segments;
/*     */   private final ReadOnlyDoubleWrapper total;
/*     */   private final InvalidationListener sumListener;
/*     */   private final WeakInvalidationListener weakSumListener;
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new SegmentedBarSkin(this);
/*     */   }
/*     */   
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(SegmentedBar.class, "segmentedbar.css");
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Callback<T, Node>> infoNodeFactoryProperty() {
/*     */     return this.infoNodeFactory;
/*     */   }
/*     */   
/*     */   public final Callback<T, Node> getInfoNodeFactory() {
/*     */     return (Callback<T, Node>)this.infoNodeFactory.get();
/*     */   }
/*     */   
/*     */   public void setInfoNodeFactory(Callback<T, Node> factory) {
/*     */     this.infoNodeFactory.set(factory);
/*     */   }
/*     */   
/*     */   public final void setOrientation(Orientation value) {
/*     */     orientationProperty().set(value);
/*     */   }
/*     */   
/* 136 */   public SegmentedBar() { this.infoNodeFactory = (ObjectProperty<Callback<T, Node>>)new SimpleObjectProperty(this, "infoNodeFactory");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     this.orientation = (ObjectProperty<Orientation>)new StyleableObjectProperty<Orientation>(null)
/*     */       {
/*     */         protected void invalidated() {
/* 171 */           boolean vertical = (get() == Orientation.VERTICAL);
/* 172 */           SegmentedBar.this.pseudoClassStateChanged(SegmentedBar.VERTICAL_PSEUDOCLASS_STATE, vertical);
/*     */           
/* 174 */           SegmentedBar.this.pseudoClassStateChanged(SegmentedBar.HORIZONTAL_PSEUDOCLASS_STATE, !vertical);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public CssMetaData<SegmentedBar, Orientation> getCssMetaData() {
/* 180 */           return SegmentedBar.StyleableProperties.ORIENTATION;
/*     */         }
/*     */ 
/*     */         
/*     */         public Object getBean() {
/* 185 */           return SegmentedBar.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public String getName() {
/* 190 */           return "orientation";
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     this.segmentViewFactory = (ObjectProperty<Callback<T, Node>>)new SimpleObjectProperty(this, "segmentViewFactory");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     this.segments = (ListProperty<T>)new SimpleListProperty(this, "segments", FXCollections.observableArrayList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     this.total = new ReadOnlyDoubleWrapper(this, "total");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 305 */     this.sumListener = (it -> this.total.set(((Double)this.segments.stream().collect(Collectors.summingDouble(()))).doubleValue()));
/*     */ 
/*     */     
/* 308 */     this.weakSumListener = new WeakInvalidationListener(this.sumListener); this.segments.addListener(it -> listenToValues()); listenToValues(); getStyleClass().add("segmented-bar"); setSegmentViewFactory(segment -> new SegmentView((T)segment)); setInfoNodeFactory(segment -> { Label label = new Label("Value: " + segment.getValue()); label.setPadding(new Insets(4.0D)); return (Node)label; }); }
/*     */   public final Orientation getOrientation() { return (this.orientation == null) ? Orientation.HORIZONTAL : (Orientation)this.orientation.get(); }
/*     */   public final ObjectProperty<Orientation> orientationProperty() { return this.orientation; }
/* 311 */   public final ObjectProperty<Callback<T, Node>> segmentViewFactoryProperty() { return this.segmentViewFactory; } public final Callback<T, Node> getSegmentViewFactory() { return (Callback<T, Node>)this.segmentViewFactory.get(); } public final void setSegmentViewFactory(Callback<T, Node> factory) { this.segmentViewFactory.set(factory); } public final ListProperty<T> segmentsProperty() { return this.segments; } private void listenToValues() { ((ObservableList)this.segments.get()).addListener((InvalidationListener)this.weakSumListener);
/*     */     
/* 313 */     getSegments().forEach(segment -> {
/*     */           segment.valueProperty().removeListener((InvalidationListener)this.weakSumListener);
/*     */           segment.valueProperty().addListener((InvalidationListener)this.weakSumListener);
/*     */         }); }
/*     */   
/*     */   public final ObservableList<T> getSegments() {
/*     */     return (ObservableList<T>)this.segments.get();
/*     */   }
/*     */   public void setSegments(ObservableList<T> segments) {
/*     */     this.segments.set(segments);
/*     */   }
/*     */   
/*     */   public final ReadOnlyDoubleProperty totalProperty() {
/*     */     return this.total.getReadOnlyProperty();
/*     */   }
/*     */   
/*     */   public final double getTotal() {
/*     */     return this.total.get();
/*     */   }
/*     */   
/*     */   public static class Segment { public Segment(double value) {
/* 334 */       if (value < 0.0D) {
/* 335 */         throw new IllegalArgumentException("value must be larger or equal to 0 but was " + value);
/*     */       }
/* 337 */       setValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Segment(double value, String text) {
/* 347 */       this(value);
/* 348 */       setText(text);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 353 */     private final StringProperty text = (StringProperty)new SimpleStringProperty(this, "text");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final StringProperty textProperty() {
/* 361 */       return this.text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setText(String text) {
/* 370 */       this.text.set(text);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getText() {
/* 379 */       return (String)this.text.get();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 384 */     private final DoubleProperty value = (DoubleProperty)new SimpleDoubleProperty(this, "value")
/*     */       {
/*     */         public void set(double newValue) {
/* 387 */           if (newValue < 0.0D) {
/* 388 */             throw new IllegalArgumentException("segment value must be >= 0 but was " + newValue);
/*     */           }
/* 390 */           super.set(newValue);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final DoubleProperty valueProperty() {
/* 401 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setValue(double value) {
/* 410 */       this.value.set(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final double getValue() {
/* 419 */       return this.value.get();
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class StyleableProperties
/*     */   {
/* 425 */     private static final CssMetaData<SegmentedBar, Orientation> ORIENTATION = new CssMetaData<SegmentedBar, Orientation>("-fx-orientation", (StyleConverter)new EnumConverter(Orientation.class), Orientation.VERTICAL)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public Orientation getInitialValue(SegmentedBar node)
/*     */         {
/* 432 */           return node.getOrientation();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isSettable(SegmentedBar n) {
/* 437 */           return (n.orientation == null || !n.orientation.isBound());
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public StyleableProperty<Orientation> getStyleableProperty(SegmentedBar n) {
/* 444 */           return (StyleableProperty<Orientation>)n.orientationProperty();
/*     */         }
/*     */       };
/*     */     
/*     */     private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
/*     */     
/*     */     static {
/* 451 */       List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
/* 452 */       styleables.add(ORIENTATION);
/*     */       
/* 454 */       STYLEABLES = Collections.unmodifiableList(styleables);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class SegmentView
/*     */     extends StackPane
/*     */   {
/*     */     private Label label;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SegmentView(T segment) {
/* 474 */       getStyleClass().add("segment-view");
/*     */       
/* 476 */       this.label = new Label();
/* 477 */       this.label.textProperty().bind((ObservableValue)segment.textProperty());
/* 478 */       this.label.setTextOverrun(OverrunStyle.CLIP);
/* 479 */       StackPane.setAlignment((Node)this.label, Pos.CENTER_LEFT);
/*     */       
/* 481 */       getChildren().add(this.label);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void layoutChildren() {
/* 491 */       super.layoutChildren();
/* 492 */       this.label.setVisible((this.label.prefWidth(-1.0D) < getWidth() - getPadding().getLeft() - getPadding().getRight()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\SegmentedBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */