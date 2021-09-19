/*      */ package org.controlsfx.control;
/*      */ 
/*      */ import com.sun.javafx.css.converters.BooleanConverter;
/*      */ import com.sun.javafx.css.converters.EnumConverter;
/*      */ import com.sun.javafx.css.converters.SizeConverter;
/*      */ import impl.org.controlsfx.skin.RangeSliderSkin;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import javafx.beans.property.BooleanProperty;
/*      */ import javafx.beans.property.DoubleProperty;
/*      */ import javafx.beans.property.DoublePropertyBase;
/*      */ import javafx.beans.property.IntegerProperty;
/*      */ import javafx.beans.property.ObjectProperty;
/*      */ import javafx.beans.property.SimpleBooleanProperty;
/*      */ import javafx.beans.property.SimpleDoubleProperty;
/*      */ import javafx.beans.property.SimpleObjectProperty;
/*      */ import javafx.css.CssMetaData;
/*      */ import javafx.css.PseudoClass;
/*      */ import javafx.css.StyleConverter;
/*      */ import javafx.css.StyleOrigin;
/*      */ import javafx.css.Styleable;
/*      */ import javafx.css.StyleableBooleanProperty;
/*      */ import javafx.css.StyleableDoubleProperty;
/*      */ import javafx.css.StyleableIntegerProperty;
/*      */ import javafx.css.StyleableObjectProperty;
/*      */ import javafx.css.StyleableProperty;
/*      */ import javafx.geometry.Orientation;
/*      */ import javafx.scene.control.Control;
/*      */ import javafx.scene.control.Skin;
/*      */ import javafx.util.StringConverter;
/*      */ import org.controlsfx.tools.Utils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RangeSlider
/*      */   extends ControlsFXControl
/*      */ {
/*      */   private DoubleProperty lowValue;
/*      */   private BooleanProperty lowValueChanging;
/*      */   private DoubleProperty highValue;
/*      */   private BooleanProperty highValueChanging;
/*      */   private final ObjectProperty<StringConverter<Number>> tickLabelFormatter;
/*      */   private DoubleProperty max;
/*      */   private DoubleProperty min;
/*      */   private BooleanProperty snapToTicks;
/*      */   private DoubleProperty majorTickUnit;
/*      */   private IntegerProperty minorTickCount;
/*      */   private DoubleProperty blockIncrement;
/*      */   private ObjectProperty<Orientation> orientation;
/*      */   private BooleanProperty showTickLabels;
/*      */   private BooleanProperty showTickMarks;
/*      */   private static final String DEFAULT_STYLE_CLASS = "range-slider";
/*      */   
/*      */   public RangeSlider() {
/*  163 */     this(0.0D, 1.0D, 0.25D, 0.75D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserAgentStylesheet() {
/*      */     return getUserAgentStylesheet(RangeSlider.class, "rangeslider.css");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Skin<?> createDefaultSkin() {
/*      */     return (Skin<?>)new RangeSliderSkin(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty lowValueProperty() {
/*      */     return this.lowValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLowValue(double d) {
/*      */     lowValueProperty().set(d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getLowValue() {
/*      */     return (this.lowValue != null) ? this.lowValue.get() : 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty lowValueChangingProperty() {
/*      */     if (this.lowValueChanging == null) {
/*      */       this.lowValueChanging = (BooleanProperty)new SimpleBooleanProperty(this, "lowValueChanging", false);
/*      */     }
/*      */     return this.lowValueChanging;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RangeSlider(double min, double max, double lowValue, double highValue) {
/*  215 */     this.lowValue = (DoubleProperty)new SimpleDoubleProperty(this, "lowValue", 0.0D) {
/*      */         protected void invalidated() {
/*  217 */           RangeSlider.this.adjustLowValues();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  281 */     this.highValue = (DoubleProperty)new SimpleDoubleProperty(this, "highValue", 100.0D) {
/*      */         protected void invalidated() {
/*  283 */           RangeSlider.this.adjustHighValues();
/*      */         }
/*      */         
/*      */         public Object getBean() {
/*  287 */           return RangeSlider.this;
/*      */         }
/*      */         
/*      */         public String getName() {
/*  291 */           return "highValue";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  343 */     this.tickLabelFormatter = (ObjectProperty<StringConverter<Number>>)new SimpleObjectProperty();
/*      */     getStyleClass().setAll((Object[])new String[] { "range-slider" });
/*      */     setMax(max);
/*      */     setMin(min);
/*      */     adjustValues();
/*      */     setLowValue(lowValue);
/*      */     setHighValue(highValue);
/*  350 */   } public final void setLowValueChanging(boolean value) { lowValueChangingProperty().set(value); } public final StringConverter<Number> getLabelFormatter() { return (StringConverter<Number>)this.tickLabelFormatter.get(); }
/*      */ 
/*      */   
/*      */   public final boolean isLowValueChanging() {
/*      */     return (this.lowValueChanging == null) ? false : this.lowValueChanging.get();
/*      */   }
/*      */   
/*      */   public final void setLabelFormatter(StringConverter<Number> value) {
/*  358 */     this.tickLabelFormatter.set(value);
/*      */   }
/*      */   public final DoubleProperty highValueProperty() { return this.highValue; }
/*      */   public final void setHighValue(double d) { if (!highValueProperty().isBound())
/*      */       highValueProperty().set(d);  }
/*      */   public final double getHighValue() { return (this.highValue != null) ? this.highValue.get() : 100.0D; } public final BooleanProperty highValueChangingProperty() { if (this.highValueChanging == null)
/*      */       this.highValueChanging = (BooleanProperty)new SimpleBooleanProperty(this, "highValueChanging", false); 
/*  365 */     return this.highValueChanging; } public final ObjectProperty<StringConverter<Number>> labelFormatterProperty() { return this.tickLabelFormatter; }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setHighValueChanging(boolean value) {
/*      */     highValueChangingProperty().set(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isHighValueChanging() {
/*      */     return (this.highValueChanging == null) ? false : this.highValueChanging.get();
/*      */   }
/*      */   
/*      */   public void incrementLowValue() {
/*  379 */     adjustLowValue(getLowValue() + getBlockIncrement());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void decrementLowValue() {
/*  387 */     adjustLowValue(getLowValue() - getBlockIncrement());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void incrementHighValue() {
/*  395 */     adjustHighValue(getHighValue() + getBlockIncrement());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void decrementHighValue() {
/*  403 */     adjustHighValue(getHighValue() - getBlockIncrement());
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
/*      */   public void adjustLowValue(double newValue) {
/*  416 */     double d1 = getMin();
/*  417 */     double d2 = getMax();
/*  418 */     if (d2 > d1) {
/*      */ 
/*      */       
/*  421 */       newValue = (newValue >= d1) ? newValue : d1;
/*  422 */       newValue = (newValue <= d2) ? newValue : d2;
/*  423 */       setLowValue(snapValueToTicks(newValue));
/*      */     } 
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
/*      */   public void adjustHighValue(double newValue) {
/*  437 */     double d1 = getMin();
/*  438 */     double d2 = getMax();
/*  439 */     if (d2 > d1) {
/*      */ 
/*      */       
/*  442 */       newValue = (newValue >= d1) ? newValue : d1;
/*  443 */       newValue = (newValue <= d2) ? newValue : d2;
/*  444 */       setHighValue(snapValueToTicks(newValue));
/*      */     } 
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
/*      */   public final void setMax(double value) {
/*  463 */     maxProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getMax() {
/*  471 */     return (this.max == null) ? 100.0D : this.max.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty maxProperty() {
/*  480 */     if (this.max == null) {
/*  481 */       this.max = (DoubleProperty)new DoublePropertyBase(100.0D) {
/*      */           protected void invalidated() {
/*  483 */             if (get() < RangeSlider.this.getMin()) {
/*  484 */               RangeSlider.this.setMin(get());
/*      */             }
/*  486 */             RangeSlider.this.adjustValues();
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  490 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  494 */             return "max";
/*      */           }
/*      */         };
/*      */     }
/*  498 */     return this.max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMin(double value) {
/*  508 */     minProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getMin() {
/*  517 */     return (this.min == null) ? 0.0D : this.min.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty minProperty() {
/*  526 */     if (this.min == null) {
/*  527 */       this.min = (DoubleProperty)new DoublePropertyBase(0.0D) {
/*      */           protected void invalidated() {
/*  529 */             if (get() > RangeSlider.this.getMax()) {
/*  530 */               RangeSlider.this.setMax(get());
/*      */             }
/*  532 */             RangeSlider.this.adjustValues();
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  536 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  540 */             return "min";
/*      */           }
/*      */         };
/*      */     }
/*  544 */     return this.min;
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
/*      */   public final void setSnapToTicks(boolean value) {
/*  558 */     snapToTicksProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSnapToTicks() {
/*  567 */     return (this.snapToTicks == null) ? false : this.snapToTicks.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty snapToTicksProperty() {
/*  578 */     if (this.snapToTicks == null) {
/*  579 */       this.snapToTicks = (BooleanProperty)new StyleableBooleanProperty(false) {
/*      */           public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
/*  581 */             return (CssMetaData)RangeSlider.StyleableProperties.SNAP_TO_TICKS;
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  585 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  589 */             return "snapToTicks";
/*      */           }
/*      */         };
/*      */     }
/*  593 */     return this.snapToTicks;
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
/*      */   public final void setMajorTickUnit(double value) {
/*  606 */     if (value <= 0.0D) {
/*  607 */       throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0.");
/*      */     }
/*  609 */     majorTickUnitProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getMajorTickUnit() {
/*  617 */     return (this.majorTickUnit == null) ? 25.0D : this.majorTickUnit.get();
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
/*      */   public final DoubleProperty majorTickUnitProperty() {
/*  634 */     if (this.majorTickUnit == null) {
/*  635 */       this.majorTickUnit = (DoubleProperty)new StyleableDoubleProperty(25.0D) {
/*      */           public void invalidated() {
/*  637 */             if (get() <= 0.0D) {
/*  638 */               throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0.");
/*      */             }
/*      */           }
/*      */           
/*      */           public CssMetaData<? extends Styleable, Number> getCssMetaData() {
/*  643 */             return (CssMetaData)RangeSlider.StyleableProperties.MAJOR_TICK_UNIT;
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  647 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  651 */             return "majorTickUnit";
/*      */           }
/*      */         };
/*      */     }
/*  655 */     return this.majorTickUnit;
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
/*      */   public final void setMinorTickCount(int value) {
/*  668 */     minorTickCountProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinorTickCount() {
/*  676 */     return (this.minorTickCount == null) ? 3 : this.minorTickCount.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IntegerProperty minorTickCountProperty() {
/*  686 */     if (this.minorTickCount == null) {
/*  687 */       this.minorTickCount = (IntegerProperty)new StyleableIntegerProperty(3) {
/*      */           public CssMetaData<? extends Styleable, Number> getCssMetaData() {
/*  689 */             return (CssMetaData)RangeSlider.StyleableProperties.MINOR_TICK_COUNT;
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  693 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  697 */             return "minorTickCount";
/*      */           }
/*      */         };
/*      */     }
/*  701 */     return this.minorTickCount;
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
/*      */   public final void setBlockIncrement(double value) {
/*  715 */     blockIncrementProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getBlockIncrement() {
/*  724 */     return (this.blockIncrement == null) ? 10.0D : this.blockIncrement.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty blockIncrementProperty() {
/*  735 */     if (this.blockIncrement == null) {
/*  736 */       this.blockIncrement = (DoubleProperty)new StyleableDoubleProperty(10.0D) {
/*      */           public CssMetaData<? extends Styleable, Number> getCssMetaData() {
/*  738 */             return (CssMetaData)RangeSlider.StyleableProperties.BLOCK_INCREMENT;
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  742 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  746 */             return "blockIncrement";
/*      */           }
/*      */         };
/*      */     }
/*  750 */     return this.blockIncrement;
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
/*      */   public final void setOrientation(Orientation value) {
/*  763 */     orientationProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Orientation getOrientation() {
/*  772 */     return (this.orientation == null) ? Orientation.HORIZONTAL : (Orientation)this.orientation.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Orientation> orientationProperty() {
/*  781 */     if (this.orientation == null) {
/*  782 */       this.orientation = (ObjectProperty<Orientation>)new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) {
/*      */           protected void invalidated() {
/*  784 */             boolean vertical = (get() == Orientation.VERTICAL);
/*  785 */             RangeSlider.this.pseudoClassStateChanged(RangeSlider.VERTICAL_PSEUDOCLASS_STATE, vertical);
/*  786 */             RangeSlider.this.pseudoClassStateChanged(RangeSlider.HORIZONTAL_PSEUDOCLASS_STATE, !vertical);
/*      */           }
/*      */           
/*      */           public CssMetaData<? extends Styleable, Orientation> getCssMetaData() {
/*  790 */             return (CssMetaData)RangeSlider.StyleableProperties.ORIENTATION;
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  794 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  798 */             return "orientation";
/*      */           }
/*      */         };
/*      */     }
/*  802 */     return this.orientation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setShowTickLabels(boolean value) {
/*  812 */     showTickLabelsProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isShowTickLabels() {
/*  819 */     return (this.showTickLabels == null) ? false : this.showTickLabels.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty showTickLabelsProperty() {
/*  829 */     if (this.showTickLabels == null) {
/*  830 */       this.showTickLabels = (BooleanProperty)new StyleableBooleanProperty(false) {
/*      */           public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
/*  832 */             return (CssMetaData)RangeSlider.StyleableProperties.SHOW_TICK_LABELS;
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  836 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  840 */             return "showTickLabels";
/*      */           }
/*      */         };
/*      */     }
/*  844 */     return this.showTickLabels;
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
/*      */   public final void setShowTickMarks(boolean value) {
/*  856 */     showTickMarksProperty().set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isShowTickMarks() {
/*  864 */     return (this.showTickMarks == null) ? false : this.showTickMarks.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty showTickMarksProperty() {
/*  872 */     if (this.showTickMarks == null) {
/*  873 */       this.showTickMarks = (BooleanProperty)new StyleableBooleanProperty(false) {
/*      */           public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
/*  875 */             return (CssMetaData)RangeSlider.StyleableProperties.SHOW_TICK_MARKS;
/*      */           }
/*      */           
/*      */           public Object getBean() {
/*  879 */             return RangeSlider.this;
/*      */           }
/*      */           
/*      */           public String getName() {
/*  883 */             return "showTickMarks";
/*      */           }
/*      */         };
/*      */     }
/*  887 */     return this.showTickMarks;
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
/*      */   private void adjustValues() {
/*  904 */     adjustLowValues();
/*  905 */     adjustHighValues();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void adjustLowValues() {
/*  912 */     if (getLowValue() < getMin() || getLowValue() > getMax()) {
/*  913 */       double value = Utils.clamp(getMin(), getLowValue(), getMax());
/*  914 */       setLowValue(value);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  922 */     else if (getLowValue() >= getHighValue() && getHighValue() >= getMin() && getHighValue() <= getMax()) {
/*  923 */       double value = Utils.clamp(getMin(), getLowValue(), getHighValue());
/*  924 */       setLowValue(value);
/*      */     } 
/*      */   }
/*      */   
/*      */   private double snapValueToTicks(double d) {
/*  929 */     double d1 = d;
/*  930 */     if (isSnapToTicks()) {
/*  931 */       double d2 = 0.0D;
/*  932 */       if (getMinorTickCount() != 0) {
/*  933 */         d2 = getMajorTickUnit() / (Math.max(getMinorTickCount(), 0) + 1);
/*      */       } else {
/*  935 */         d2 = getMajorTickUnit();
/*      */       } 
/*  937 */       int i = (int)((d1 - getMin()) / d2);
/*  938 */       double d3 = i * d2 + getMin();
/*  939 */       double d4 = (i + 1) * d2 + getMin();
/*  940 */       d1 = Utils.nearest(d3, d1, d4);
/*      */     } 
/*  942 */     return Utils.clamp(getMin(), d1, getMax());
/*      */   }
/*      */   
/*      */   private void adjustHighValues() {
/*  946 */     if (getHighValue() < getMin() || getHighValue() > getMax()) {
/*  947 */       setHighValue(Utils.clamp(getMin(), getHighValue(), getMax()));
/*  948 */     } else if (getHighValue() < getLowValue() && getLowValue() >= getMin() && getLowValue() <= getMax()) {
/*  949 */       setHighValue(Utils.clamp(getLowValue(), getHighValue(), getMax()));
/*      */     } 
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
/*      */   private static class StyleableProperties
/*      */   {
/*  964 */     private static final CssMetaData<RangeSlider, Number> BLOCK_INCREMENT = new CssMetaData<RangeSlider, Number>("-fx-block-increment", 
/*      */         
/*  966 */         SizeConverter.getInstance(), Double.valueOf(10.0D))
/*      */       {
/*      */         public boolean isSettable(RangeSlider n) {
/*  969 */           return (n.blockIncrement == null || !n.blockIncrement.isBound());
/*      */         }
/*      */ 
/*      */         
/*      */         public StyleableProperty<Number> getStyleableProperty(RangeSlider n) {
/*  974 */           return (StyleableProperty<Number>)n.blockIncrementProperty();
/*      */         }
/*      */       };
/*      */     
/*  978 */     private static final CssMetaData<RangeSlider, Boolean> SHOW_TICK_LABELS = new CssMetaData<RangeSlider, Boolean>("-fx-show-tick-labels", 
/*      */         
/*  980 */         BooleanConverter.getInstance(), Boolean.FALSE)
/*      */       {
/*      */         public boolean isSettable(RangeSlider n) {
/*  983 */           return (n.showTickLabels == null || !n.showTickLabels.isBound());
/*      */         }
/*      */ 
/*      */         
/*      */         public StyleableProperty<Boolean> getStyleableProperty(RangeSlider n) {
/*  988 */           return (StyleableProperty<Boolean>)n.showTickLabelsProperty();
/*      */         }
/*      */       };
/*      */     
/*  992 */     private static final CssMetaData<RangeSlider, Boolean> SHOW_TICK_MARKS = new CssMetaData<RangeSlider, Boolean>("-fx-show-tick-marks", 
/*      */         
/*  994 */         BooleanConverter.getInstance(), Boolean.FALSE)
/*      */       {
/*      */         public boolean isSettable(RangeSlider n) {
/*  997 */           return (n.showTickMarks == null || !n.showTickMarks.isBound());
/*      */         }
/*      */ 
/*      */         
/*      */         public StyleableProperty<Boolean> getStyleableProperty(RangeSlider n) {
/* 1002 */           return (StyleableProperty<Boolean>)n.showTickMarksProperty();
/*      */         }
/*      */       };
/*      */     
/* 1006 */     private static final CssMetaData<RangeSlider, Boolean> SNAP_TO_TICKS = new CssMetaData<RangeSlider, Boolean>("-fx-snap-to-ticks", 
/*      */         
/* 1008 */         BooleanConverter.getInstance(), Boolean.FALSE)
/*      */       {
/*      */         public boolean isSettable(RangeSlider n) {
/* 1011 */           return (n.snapToTicks == null || !n.snapToTicks.isBound());
/*      */         }
/*      */ 
/*      */         
/*      */         public StyleableProperty<Boolean> getStyleableProperty(RangeSlider n) {
/* 1016 */           return (StyleableProperty<Boolean>)n.snapToTicksProperty();
/*      */         }
/*      */       };
/*      */     
/* 1020 */     private static final CssMetaData<RangeSlider, Number> MAJOR_TICK_UNIT = new CssMetaData<RangeSlider, Number>("-fx-major-tick-unit", 
/*      */         
/* 1022 */         SizeConverter.getInstance(), Double.valueOf(25.0D))
/*      */       {
/*      */         public boolean isSettable(RangeSlider n) {
/* 1025 */           return (n.majorTickUnit == null || !n.majorTickUnit.isBound());
/*      */         }
/*      */ 
/*      */         
/*      */         public StyleableProperty<Number> getStyleableProperty(RangeSlider n) {
/* 1030 */           return (StyleableProperty<Number>)n.majorTickUnitProperty();
/*      */         }
/*      */       };
/*      */     
/* 1034 */     private static final CssMetaData<RangeSlider, Number> MINOR_TICK_COUNT = new CssMetaData<RangeSlider, Number>("-fx-minor-tick-count", 
/*      */         
/* 1036 */         SizeConverter.getInstance(), Double.valueOf(3.0D))
/*      */       {
/*      */         public void set(RangeSlider node, Number value, StyleOrigin origin)
/*      */         {
/* 1040 */           super.set((Styleable)node, Integer.valueOf(value.intValue()), origin);
/*      */         }
/*      */         
/*      */         public boolean isSettable(RangeSlider n) {
/* 1044 */           return (n.minorTickCount == null || !n.minorTickCount.isBound());
/*      */         }
/*      */ 
/*      */         
/*      */         public StyleableProperty<Number> getStyleableProperty(RangeSlider n) {
/* 1049 */           return (StyleableProperty<Number>)n.minorTickCountProperty();
/*      */         }
/*      */       };
/*      */     
/* 1053 */     private static final CssMetaData<RangeSlider, Orientation> ORIENTATION = new CssMetaData<RangeSlider, Orientation>("-fx-orientation", (StyleConverter)new EnumConverter(Orientation.class), Orientation.HORIZONTAL)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*      */         public Orientation getInitialValue(RangeSlider node)
/*      */         {
/* 1060 */           return node.getOrientation();
/*      */         }
/*      */         
/*      */         public boolean isSettable(RangeSlider n) {
/* 1064 */           return (n.orientation == null || !n.orientation.isBound());
/*      */         }
/*      */ 
/*      */         
/*      */         public StyleableProperty<Orientation> getStyleableProperty(RangeSlider n) {
/* 1069 */           return (StyleableProperty<Orientation>)n.orientationProperty();
/*      */         }
/*      */       };
/*      */     
/*      */     private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
/*      */     
/*      */     static {
/* 1076 */       List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
/* 1077 */       styleables.add(BLOCK_INCREMENT);
/* 1078 */       styleables.add(SHOW_TICK_LABELS);
/* 1079 */       styleables.add(SHOW_TICK_MARKS);
/* 1080 */       styleables.add(SNAP_TO_TICKS);
/* 1081 */       styleables.add(MAJOR_TICK_UNIT);
/* 1082 */       styleables.add(MINOR_TICK_COUNT);
/* 1083 */       styleables.add(ORIENTATION);
/*      */       
/* 1085 */       STYLEABLES = Collections.unmodifiableList(styleables);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
/* 1095 */     return StyleableProperties.STYLEABLES;
/*      */   }
/*      */ 
/*      */   
/* 1099 */   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
/*      */   
/* 1101 */   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\RangeSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */