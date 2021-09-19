/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.GridViewSkin;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.css.CssMetaData;
/*     */ import javafx.css.StyleConverter;
/*     */ import javafx.css.Styleable;
/*     */ import javafx.css.StyleableDoubleProperty;
/*     */ import javafx.css.StyleableProperty;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Skin;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridView<T>
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private DoubleProperty horizontalCellSpacing;
/*     */   private DoubleProperty verticalCellSpacing;
/*     */   private DoubleProperty cellWidth;
/*     */   private DoubleProperty cellHeight;
/*     */   private ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactory;
/*     */   private ObjectProperty<ObservableList<T>> items;
/*     */   private static final String DEFAULT_STYLE_CLASS = "grid-view";
/*     */   
/*     */   public GridView() {
/* 117 */     this(FXCollections.observableArrayList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridView(ObservableList<T> items) {
/* 126 */     getStyleClass().add("grid-view");
/* 127 */     setItems(items);
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
/*     */   protected Skin<?> createDefaultSkin() {
/* 142 */     return (Skin<?>)new GridViewSkin(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/* 147 */     return getUserAgentStylesheet(GridView.class, "gridview.css");
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
/*     */   public final DoubleProperty horizontalCellSpacingProperty() {
/* 162 */     if (this.horizontalCellSpacing == null) {
/* 163 */       this.horizontalCellSpacing = (DoubleProperty)new StyleableDoubleProperty(12.0D) {
/*     */           public CssMetaData<GridView<?>, Number> getCssMetaData() {
/* 165 */             return GridView.StyleableProperties.HORIZONTAL_CELL_SPACING;
/*     */           }
/*     */           
/*     */           public Object getBean() {
/* 169 */             return GridView.this;
/*     */           }
/*     */           
/*     */           public String getName() {
/* 173 */             return "horizontalCellSpacing";
/*     */           }
/*     */         };
/*     */     }
/* 177 */     return this.horizontalCellSpacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHorizontalCellSpacing(double value) {
/* 187 */     horizontalCellSpacingProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getHorizontalCellSpacing() {
/* 195 */     return (this.horizontalCellSpacing == null) ? 12.0D : this.horizontalCellSpacing.get();
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
/*     */   public final DoubleProperty verticalCellSpacingProperty() {
/* 207 */     if (this.verticalCellSpacing == null) {
/* 208 */       this.verticalCellSpacing = (DoubleProperty)new StyleableDoubleProperty(12.0D) {
/*     */           public CssMetaData<GridView<?>, Number> getCssMetaData() {
/* 210 */             return GridView.StyleableProperties.VERTICAL_CELL_SPACING;
/*     */           }
/*     */           
/*     */           public Object getBean() {
/* 214 */             return GridView.this;
/*     */           }
/*     */           
/*     */           public String getName() {
/* 218 */             return "verticalCellSpacing";
/*     */           }
/*     */         };
/*     */     }
/* 222 */     return this.verticalCellSpacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setVerticalCellSpacing(double value) {
/* 231 */     verticalCellSpacingProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getVerticalCellSpacing() {
/* 239 */     return (this.verticalCellSpacing == null) ? 12.0D : this.verticalCellSpacing.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty cellWidthProperty() {
/* 249 */     if (this.cellWidth == null) {
/* 250 */       this.cellWidth = (DoubleProperty)new StyleableDoubleProperty(64.0D) {
/*     */           public CssMetaData<GridView<?>, Number> getCssMetaData() {
/* 252 */             return GridView.StyleableProperties.CELL_WIDTH;
/*     */           }
/*     */           
/*     */           public Object getBean() {
/* 256 */             return GridView.this;
/*     */           }
/*     */           
/*     */           public String getName() {
/* 260 */             return "cellWidth";
/*     */           }
/*     */         };
/*     */     }
/* 264 */     return this.cellWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCellWidth(double value) {
/* 272 */     cellWidthProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getCellWidth() {
/* 279 */     return (this.cellWidth == null) ? 64.0D : this.cellWidth.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty cellHeightProperty() {
/* 288 */     if (this.cellHeight == null) {
/* 289 */       this.cellHeight = (DoubleProperty)new StyleableDoubleProperty(64.0D) {
/*     */           public CssMetaData<GridView<?>, Number> getCssMetaData() {
/* 291 */             return GridView.StyleableProperties.CELL_HEIGHT;
/*     */           }
/*     */           
/*     */           public Object getBean() {
/* 295 */             return GridView.this;
/*     */           }
/*     */           
/*     */           public String getName() {
/* 299 */             return "cellHeight";
/*     */           }
/*     */         };
/*     */     }
/* 303 */     return this.cellHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCellHeight(double value) {
/* 311 */     cellHeightProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getCellHeight() {
/* 318 */     return (this.cellHeight == null) ? 64.0D : this.cellHeight.get();
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
/*     */   public final ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactoryProperty() {
/* 367 */     if (this.cellFactory == null) {
/* 368 */       this.cellFactory = (ObjectProperty<Callback<GridView<T>, GridCell<T>>>)new SimpleObjectProperty(this, "cellFactory");
/*     */     }
/* 370 */     return this.cellFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCellFactory(Callback<GridView<T>, GridCell<T>> value) {
/* 379 */     cellFactoryProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Callback<GridView<T>, GridCell<T>> getCellFactory() {
/* 387 */     return (this.cellFactory == null) ? null : (Callback<GridView<T>, GridCell<T>>)this.cellFactory.get();
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
/*     */   public final ObjectProperty<ObservableList<T>> itemsProperty() {
/* 405 */     if (this.items == null) {
/* 406 */       this.items = (ObjectProperty<ObservableList<T>>)new SimpleObjectProperty(this, "items");
/*     */     }
/* 408 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setItems(ObservableList<T> value) {
/* 417 */     itemsProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObservableList<T> getItems() {
/* 425 */     return (this.items == null) ? null : (ObservableList<T>)this.items.get();
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
/*     */   private static class StyleableProperties
/*     */   {
/* 442 */     private static final CssMetaData<GridView<?>, Number> HORIZONTAL_CELL_SPACING = new CssMetaData<GridView<?>, Number>("-fx-horizontal-cell-spacing", 
/* 443 */         StyleConverter.getSizeConverter(), Double.valueOf(12.0D))
/*     */       {
/*     */         public Double getInitialValue(GridView<?> node) {
/* 446 */           return Double.valueOf(node.getHorizontalCellSpacing());
/*     */         }
/*     */         
/*     */         public boolean isSettable(GridView<?> n) {
/* 450 */           return (n.horizontalCellSpacing == null || !n.horizontalCellSpacing.isBound());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
/* 456 */           return (StyleableProperty<Number>)n.horizontalCellSpacingProperty();
/*     */         }
/*     */       };
/*     */     
/* 460 */     private static final CssMetaData<GridView<?>, Number> VERTICAL_CELL_SPACING = new CssMetaData<GridView<?>, Number>("-fx-vertical-cell-spacing", 
/* 461 */         StyleConverter.getSizeConverter(), Double.valueOf(12.0D))
/*     */       {
/*     */         public Double getInitialValue(GridView<?> node) {
/* 464 */           return Double.valueOf(node.getVerticalCellSpacing());
/*     */         }
/*     */         
/*     */         public boolean isSettable(GridView<?> n) {
/* 468 */           return (n.verticalCellSpacing == null || !n.verticalCellSpacing.isBound());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
/* 474 */           return (StyleableProperty<Number>)n.verticalCellSpacingProperty();
/*     */         }
/*     */       };
/*     */     
/* 478 */     private static final CssMetaData<GridView<?>, Number> CELL_WIDTH = new CssMetaData<GridView<?>, Number>("-fx-cell-width", 
/* 479 */         StyleConverter.getSizeConverter(), Double.valueOf(64.0D))
/*     */       {
/*     */         public Double getInitialValue(GridView<?> node) {
/* 482 */           return Double.valueOf(node.getCellWidth());
/*     */         }
/*     */         
/*     */         public boolean isSettable(GridView<?> n) {
/* 486 */           return (n.cellWidth == null || !n.cellWidth.isBound());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
/* 492 */           return (StyleableProperty<Number>)n.cellWidthProperty();
/*     */         }
/*     */       };
/*     */     
/* 496 */     private static final CssMetaData<GridView<?>, Number> CELL_HEIGHT = new CssMetaData<GridView<?>, Number>("-fx-cell-height", 
/* 497 */         StyleConverter.getSizeConverter(), Double.valueOf(64.0D))
/*     */       {
/*     */         public Double getInitialValue(GridView<?> node) {
/* 500 */           return Double.valueOf(node.getCellHeight());
/*     */         }
/*     */         
/*     */         public boolean isSettable(GridView<?> n) {
/* 504 */           return (n.cellHeight == null || !n.cellHeight.isBound());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
/* 510 */           return (StyleableProperty<Number>)n.cellHeightProperty();
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
/*     */     private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 535 */       List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
/* 536 */       styleables.add(HORIZONTAL_CELL_SPACING);
/* 537 */       styleables.add(VERTICAL_CELL_SPACING);
/* 538 */       styleables.add(CELL_WIDTH);
/* 539 */       styleables.add(CELL_HEIGHT);
/*     */       
/* 541 */       STYLEABLES = Collections.unmodifiableList(styleables);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
/* 550 */     return StyleableProperties.STYLEABLES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
/* 558 */     return getClassCssMetaData();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\GridView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */