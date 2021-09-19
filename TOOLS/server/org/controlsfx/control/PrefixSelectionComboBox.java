/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.tools.PrefixSelectionCustomizer;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.IntegerProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleIntegerProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.control.ComboBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrefixSelectionComboBox<T>
/*     */   extends ComboBox<T>
/*     */ {
/*     */   private final ChangeListener<Boolean> focusedListener;
/*     */   private final BooleanProperty displayOnFocusedEnabled;
/*     */   private final BooleanProperty backSpaceAllowed;
/*     */   private final IntegerProperty typingDelay;
/*     */   private final ObjectProperty<BiFunction<ComboBox, String, Optional>> lookup;
/*     */   
/*     */   public final boolean isDisplayOnFocusedEnabled() {
/*     */     return this.displayOnFocusedEnabled.get();
/*     */   }
/*     */   
/*     */   public final void setDisplayOnFocusedEnabled(boolean value) {
/*     */     this.displayOnFocusedEnabled.set(value);
/*     */   }
/*     */   
/*     */   public final BooleanProperty displayOnFocusedEnabledProperty() {
/*     */     return this.displayOnFocusedEnabled;
/*     */   }
/*     */   
/*     */   public final boolean isBackSpaceAllowed() {
/*     */     return this.backSpaceAllowed.get();
/*     */   }
/*     */   
/*     */   public final void setBackSpaceAllowed(boolean value) {
/*     */     this.backSpaceAllowed.set(value);
/*     */   }
/*     */   
/*     */   public final BooleanProperty backSpaceAllowedProperty() {
/*     */     return this.backSpaceAllowed;
/*     */   }
/*     */   
/*     */   public final int getTypingDelay() {
/*     */     return this.typingDelay.get();
/*     */   }
/*     */   
/*     */   public final void setTypingDelay(int value) {
/*     */     this.typingDelay.set(value);
/*     */   }
/*     */   
/*     */   public final IntegerProperty typingDelayProperty() {
/*     */     return this.typingDelay;
/*     */   }
/*     */   
/*     */   public PrefixSelectionComboBox() {
/*  83 */     this.focusedListener = ((obs, ov, nv) -> {
/*     */         if (nv.booleanValue()) {
/*     */           show();
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     this.displayOnFocusedEnabled = (BooleanProperty)new SimpleBooleanProperty(this, "displayOnFocusedEnabled", false)
/*     */       {
/*     */         protected void invalidated() {
/* 112 */           if (get()) {
/* 113 */             PrefixSelectionComboBox.this.focusedProperty().addListener(PrefixSelectionComboBox.this.focusedListener);
/*     */           } else {
/* 115 */             PrefixSelectionComboBox.this.focusedProperty().removeListener(PrefixSelectionComboBox.this.focusedListener);
/*     */           } 
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
/* 129 */     this.backSpaceAllowed = (BooleanProperty)new SimpleBooleanProperty(this, "backSpaceAllowed", false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     this.typingDelay = (IntegerProperty)new SimpleIntegerProperty(this, "typingDelay", 500);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     this.lookup = (ObjectProperty<BiFunction<ComboBox, String, Optional>>)new SimpleObjectProperty(this, "lookup", PrefixSelectionCustomizer.DEFAULT_LOOKUP_COMBOBOX); setEditable(false);
/* 152 */     PrefixSelectionCustomizer.customize(this); } public final BiFunction<ComboBox, String, Optional> getLookup() { return (BiFunction<ComboBox, String, Optional>)this.lookup.get(); }
/* 153 */   public final void setLookup(BiFunction<ComboBox, String, Optional> value) { this.lookup.set(value); } public final ObjectProperty<BiFunction<ComboBox, String, Optional>> lookupProperty() {
/* 154 */     return this.lookup;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\PrefixSelectionComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */