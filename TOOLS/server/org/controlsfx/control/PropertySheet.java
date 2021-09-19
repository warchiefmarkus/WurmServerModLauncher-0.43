/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.PropertySheetSkin;
/*     */ import java.util.Optional;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
/*     */ import org.controlsfx.property.editor.PropertyEditor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySheet
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private final ObservableList<Item> items;
/*     */   private final SimpleObjectProperty<Mode> modeProperty;
/*     */   private final SimpleObjectProperty<Callback<Item, PropertyEditor<?>>> propertyEditorFactory;
/*     */   private final SimpleBooleanProperty modeSwitcherVisible;
/*     */   private final SimpleBooleanProperty searchBoxVisible;
/*     */   private final SimpleStringProperty titleFilterProperty;
/*     */   private static final String DEFAULT_STYLE_CLASS = "property-sheet";
/*     */   
/*     */   public enum Mode
/*     */   {
/* 109 */     NAME,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     CATEGORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Item
/*     */   {
/*     */     Class<?> getType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String getCategory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String getDescription();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setValue(Object param1Object);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Optional<ObservableValue<? extends Object>> getObservableValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
/* 184 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default boolean isEditable() {
/* 193 */       return true;
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
/*     */   public PropertySheet() {
/* 218 */     this((ObservableList<Item>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<Item> getItems() {
/*     */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new PropertySheetSkin(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(PropertySheet.class, "propertysheet.css");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SimpleObjectProperty<Mode> modeProperty() {
/*     */     return this.modeProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySheet(ObservableList<Item> items) {
/* 270 */     this.modeProperty = new SimpleObjectProperty(this, "mode", Mode.NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 303 */     this.propertyEditorFactory = new SimpleObjectProperty(this, "propertyEditor", new DefaultPropertyEditorFactory());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 339 */     this.modeSwitcherVisible = new SimpleBooleanProperty(this, "modeSwitcherVisible", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 372 */     this.searchBoxVisible = new SimpleBooleanProperty(this, "searchBoxVisible", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 411 */     this.titleFilterProperty = new SimpleStringProperty(this, "titleFilter", "");
/*     */     getStyleClass().add("property-sheet");
/*     */     this.items = (items == null) ? FXCollections.observableArrayList() : items;
/*     */   }
/*     */   public final Mode getMode() { return (Mode)this.modeProperty.get(); } public final void setMode(Mode mode) { this.modeProperty.set(mode); } public final SimpleObjectProperty<Callback<Item, PropertyEditor<?>>> propertyEditorFactory() {
/*     */     return this.propertyEditorFactory;
/*     */   } public final Callback<Item, PropertyEditor<?>> getPropertyEditorFactory() {
/*     */     return (Callback<Item, PropertyEditor<?>>)this.propertyEditorFactory.get();
/*     */   } public final void setPropertyEditorFactory(Callback<Item, PropertyEditor<?>> factory) {
/*     */     this.propertyEditorFactory.set((factory == null) ? new DefaultPropertyEditorFactory() : factory);
/*     */   } public final SimpleStringProperty titleFilter() {
/* 422 */     return this.titleFilterProperty;
/*     */   } public final SimpleBooleanProperty modeSwitcherVisibleProperty() {
/*     */     return this.modeSwitcherVisible;
/*     */   }
/*     */   public final boolean isModeSwitcherVisible() {
/*     */     return this.modeSwitcherVisible.get();
/*     */   }
/*     */   public final String getTitleFilter() {
/* 430 */     return this.titleFilterProperty.get(); } public final void setModeSwitcherVisible(boolean visible) {
/*     */     this.modeSwitcherVisible.set(visible);
/*     */   } public final SimpleBooleanProperty searchBoxVisibleProperty() {
/*     */     return this.searchBoxVisible;
/*     */   } public final boolean isSearchBoxVisible() {
/*     */     return this.searchBoxVisible.get();
/*     */   } public final void setSearchBoxVisible(boolean visible) {
/*     */     this.searchBoxVisible.set(visible);
/*     */   } public final void setTitleFilter(String filter) {
/* 439 */     this.titleFilterProperty.set(filter);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\PropertySheet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */