/*     */ package org.controlsfx.validation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Consumer;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ReadOnlyBooleanProperty;
/*     */ import javafx.beans.property.ReadOnlyObjectProperty;
/*     */ import javafx.beans.property.ReadOnlyObjectWrapper;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.MapChangeListener;
/*     */ import javafx.collections.ObservableMap;
/*     */ import javafx.collections.ObservableSet;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.tools.ValueExtractor;
/*     */ import org.controlsfx.validation.decoration.GraphicValidationDecoration;
/*     */ import org.controlsfx.validation.decoration.ValidationDecoration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValidationSupport
/*     */ {
/*     */   private static final String CTRL_REQUIRED_FLAG = "$org.controlsfx.validation.required$";
/*     */   
/*     */   public static void setRequired(Control c, boolean required) {
/*  97 */     c.getProperties().put("$org.controlsfx.validation.required$", Boolean.valueOf(required));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isRequired(Control c) {
/* 106 */     Object value = c.getProperties().get("$org.controlsfx.validation.required$");
/* 107 */     return (value instanceof Boolean) ? ((Boolean)value).booleanValue() : false;
/*     */   }
/*     */   
/* 110 */   private ObservableSet<Control> controls = FXCollections.observableSet((Object[])new Control[0]);
/*     */   
/* 112 */   private ObservableMap<Control, ValidationResult> validationResults = FXCollections.observableMap(new WeakHashMap<>());
/*     */ 
/*     */   
/* 115 */   private AtomicBoolean dataChanged = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BooleanProperty errorDecorationEnabledProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ReadOnlyObjectWrapper<ValidationResult> validationResultProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BooleanProperty invalidProperty;
/*     */ 
/*     */ 
/*     */   
/*     */   private ObjectProperty<ValidationDecoration> validationDecoratorProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initInitialDecoration() {
/* 140 */     this.dataChanged.set(true);
/* 141 */     redecorate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redecorate() {
/* 150 */     Optional<ValidationDecoration> odecorator = Optional.ofNullable(getValidationDecorator());
/* 151 */     for (Iterator<Control> iterator = getRegisteredControls().iterator(); iterator.hasNext(); ) { Control target = iterator.next();
/* 152 */       odecorator.ifPresent(decorator -> {
/*     */             decorator.removeDecorations(target);
/*     */             decorator.applyRequiredDecoration(target);
/*     */             if (this.dataChanged.get() && isErrorDecorationEnabled())
/*     */               getHighestMessage(target).ifPresent(()); 
/*     */           }); }
/*     */   
/*     */   }
/*     */   
/*     */   public ValidationSupport() {
/* 162 */     this.errorDecorationEnabledProperty = (BooleanProperty)new SimpleBooleanProperty(true) {
/*     */         protected void invalidated() {
/* 164 */           ValidationSupport.this.redecorate();
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
/* 182 */     this.validationResultProperty = new ReadOnlyObjectWrapper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     this.invalidProperty = (BooleanProperty)new SimpleBooleanProperty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     this.validationDecoratorProperty = (ObjectProperty<ValidationDecoration>)new SimpleObjectProperty<ValidationDecoration>(this, "validationDecorator", (ValidationDecoration)new GraphicValidationDecoration())
/*     */       {
/*     */         protected void invalidated()
/*     */         {
/* 225 */           ValidationSupport.this.redecorate();
/*     */         }
/*     */       };
/*     */     validationResultProperty().addListener((o, oldValue, validationResult) -> {
/*     */           this.invalidProperty.set(!validationResult.getErrors().isEmpty());
/*     */           redecorate();
/*     */         });
/*     */     this.validationResults.addListener(change -> this.validationResultProperty.set(ValidationResult.fromResults(this.validationResults.values())));
/* 233 */   } public BooleanProperty errorDecorationEnabledProperty() { return this.errorDecorationEnabledProperty; } public ObjectProperty<ValidationDecoration> validationDecoratorProperty() { return this.validationDecoratorProperty; }
/*     */   public void setErrorDecorationEnabled(boolean enabled) { this.errorDecorationEnabledProperty.set(enabled); }
/*     */   private boolean isErrorDecorationEnabled() { return this.errorDecorationEnabledProperty.get(); }
/*     */   public ValidationResult getValidationResult() { return (ValidationResult)this.validationResultProperty.get(); }
/*     */   public ReadOnlyObjectProperty<ValidationResult> validationResultProperty() { return this.validationResultProperty.getReadOnlyProperty(); }
/*     */   public Boolean isInvalid() { return Boolean.valueOf(this.invalidProperty.get()); } public ReadOnlyBooleanProperty invalidProperty() {
/*     */     return (ReadOnlyBooleanProperty)this.invalidProperty;
/*     */   } public ValidationDecoration getValidationDecorator() {
/* 241 */     return (ValidationDecoration)this.validationDecoratorProperty.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationDecorator(ValidationDecoration decorator) {
/* 249 */     this.validationDecoratorProperty.set(decorator);
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
/*     */   public <T> boolean registerValidator(Control c, boolean required, Validator<T> validator) {
/* 263 */     Optional.<Control>ofNullable(c).ifPresent(ctrl -> ctrl.getProperties().addListener(new MapChangeListener<Object, Object>()
/*     */           {
/*     */ 
/*     */ 
/*     */             
/*     */             public void onChanged(MapChangeListener.Change change)
/*     */             {
/* 270 */               if ("$org.controlsfx.validation.required$".equals(change.getKey())) {
/* 271 */                 ValidationSupport.this.redecorate();
/*     */               }
/*     */             }
/*     */           }));
/*     */ 
/*     */ 
/*     */     
/* 278 */     setRequired(c, required);
/*     */     
/* 280 */     return ValueExtractor.getObservableValueExtractor(c).map(e -> {
/*     */           ObservableValue<T> observable = (ObservableValue<T>)e.call(c);
/*     */ 
/*     */ 
/*     */           
/*     */           Consumer<T> updateResults = ();
/*     */ 
/*     */           
/*     */           this.controls.add(c);
/*     */ 
/*     */           
/*     */           observable.addListener(());
/*     */ 
/*     */           
/*     */           updateResults.accept((T)observable.getValue());
/*     */ 
/*     */           
/*     */           return e;
/* 298 */         }).isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> boolean registerValidator(Control c, Validator<T> validator) {
/* 308 */     return registerValidator(c, true, validator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Control> getRegisteredControls() {
/* 316 */     return Collections.unmodifiableSet((Set<? extends Control>)this.controls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<ValidationMessage> getHighestMessage(Control target) {
/* 325 */     return Optional.<Object>ofNullable(this.validationResults.get(target)).flatMap(result -> result.getMessages().stream().max(ValidationMessage.COMPARATOR));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\ValidationSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */