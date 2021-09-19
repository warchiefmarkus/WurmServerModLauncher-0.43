/*     */ package org.controlsfx.property.editor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.time.LocalDate;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.scene.control.CheckBox;
/*     */ import javafx.scene.control.ColorPicker;
/*     */ import javafx.scene.control.ComboBox;
/*     */ import javafx.scene.control.DatePicker;
/*     */ import javafx.scene.control.TextField;
/*     */ import javafx.scene.control.TextInputControl;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.text.Font;
/*     */ import org.controlsfx.control.PropertySheet;
/*     */ import org.controlsfx.dialog.FontSelectorDialog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Editors
/*     */ {
/*     */   public static final PropertyEditor<?> createTextEditor(PropertySheet.Item property) {
/*  62 */     return new AbstractPropertyEditor<String, TextField>(property, new TextField())
/*     */       {
/*     */         
/*     */         protected StringProperty getObservableValue()
/*     */         {
/*  67 */           return getEditor().textProperty();
/*     */         }
/*     */         
/*     */         public void setValue(String value) {
/*  71 */           getEditor().setText(value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final PropertyEditor<?> createNumericEditor(final PropertySheet.Item property) {
/*  79 */     return new AbstractPropertyEditor<Number, NumericField>(property, new NumericField(property.getType()))
/*     */       {
/*     */         private Class<? extends Number> sourceClass;
/*     */ 
/*     */ 
/*     */         
/*     */         protected ObservableValue<Number> getObservableValue() {
/*  86 */           return getEditor().valueProperty();
/*     */         }
/*     */         
/*     */         public Number getValue() {
/*     */           try {
/*  91 */             return this.sourceClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { getEditor().getText() });
/*  92 */           } catch (InstantiationException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException e) {
/*     */             
/*  94 */             e.printStackTrace();
/*  95 */             return null;
/*     */           } 
/*     */         }
/*     */         
/*     */         public void setValue(Number value) {
/* 100 */           this.sourceClass = (Class)value.getClass();
/* 101 */           getEditor().setText(value.toString());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final PropertyEditor<?> createCheckEditor(PropertySheet.Item property) {
/* 109 */     return new AbstractPropertyEditor<Boolean, CheckBox>(property, new CheckBox())
/*     */       {
/*     */         protected BooleanProperty getObservableValue() {
/* 112 */           return getEditor().selectedProperty();
/*     */         }
/*     */         
/*     */         public void setValue(Boolean value) {
/* 116 */           getEditor().setSelected(value.booleanValue());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final <T> PropertyEditor<?> createChoiceEditor(PropertySheet.Item property, final Collection<T> choices) {
/* 124 */     return new AbstractPropertyEditor<T, ComboBox<T>>(property, new ComboBox())
/*     */       {
/*     */         
/*     */         protected ObservableValue<T> getObservableValue()
/*     */         {
/* 129 */           return (ObservableValue<T>)getEditor().getSelectionModel().selectedItemProperty();
/*     */         }
/*     */         
/*     */         public void setValue(T value) {
/* 133 */           getEditor().getSelectionModel().select(value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static final PropertyEditor<?> createColorEditor(PropertySheet.Item property) {
/* 139 */     return new AbstractPropertyEditor<Color, ColorPicker>(property, new ColorPicker())
/*     */       {
/*     */         protected ObservableValue<Color> getObservableValue() {
/* 142 */           return (ObservableValue<Color>)getEditor().valueProperty();
/*     */         }
/*     */         
/*     */         public void setValue(Color value) {
/* 146 */           getEditor().setValue(value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static final PropertyEditor<?> createDateEditor(PropertySheet.Item property) {
/* 153 */     return new AbstractPropertyEditor<LocalDate, DatePicker>(property, new DatePicker())
/*     */       {
/*     */         
/*     */         protected ObservableValue<LocalDate> getObservableValue()
/*     */         {
/* 158 */           return (ObservableValue<LocalDate>)getEditor().valueProperty();
/*     */         }
/*     */         
/*     */         public void setValue(LocalDate value) {
/* 162 */           getEditor().setValue(value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static final PropertyEditor<?> createFontEditor(PropertySheet.Item property) {
/* 169 */     return new AbstractPropertyEditor<Font, AbstractObjectField<Font>>(property, new AbstractObjectField<Font>() {
/*     */           protected Class<Font> getType() {
/* 171 */             return Font.class;
/*     */           }
/*     */           
/*     */           protected String objectToString(Font font) {
/* 175 */             return (font == null) ? "" : String.format("%s, %.1f", new Object[] { font.getName(), Double.valueOf(font.getSize()) });
/*     */           }
/*     */           
/*     */           protected Font edit(Font font) {
/* 179 */             FontSelectorDialog dlg = new FontSelectorDialog(font);
/* 180 */             Optional<Font> optionalFont = dlg.showAndWait();
/* 181 */             return optionalFont.orElse(null);
/*     */           }
/*     */         })
/*     */       {
/*     */         protected ObservableValue<Font> getObservableValue() {
/* 186 */           return (ObservableValue<Font>)getEditor().getObjectProperty();
/*     */         }
/*     */         
/*     */         public void setValue(Font value) {
/* 190 */           getEditor().getObjectProperty().set(value);
/*     */         }
/*     */       };
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
/*     */   public static final Optional<PropertyEditor<?>> createCustomEditor(PropertySheet.Item property) {
/* 209 */     return property.getPropertyEditorClass().map(cls -> {
/*     */           try {
/*     */             Constructor<?> cn = cls.getConstructor(new Class[] { PropertySheet.Item.class });
/*     */             if (cn != null) {
/*     */               return (PropertyEditor)cn.newInstance(new Object[] { property });
/*     */             }
/* 215 */           } catch (NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException ex) {
/*     */             ex.printStackTrace();
/*     */           } 
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   private static void enableAutoSelectAll(TextInputControl control) {
/* 223 */     control.focusedProperty().addListener((o, oldValue, newValue) -> {
/*     */           if (newValue.booleanValue())
/*     */             Platform.runLater(()); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\editor\Editors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */