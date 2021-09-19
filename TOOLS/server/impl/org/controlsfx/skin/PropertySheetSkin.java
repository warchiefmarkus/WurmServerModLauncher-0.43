/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Accordion;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.ScrollPane;
/*     */ import javafx.scene.control.TextField;
/*     */ import javafx.scene.control.TitledPane;
/*     */ import javafx.scene.control.ToggleButton;
/*     */ import javafx.scene.control.ToolBar;
/*     */ import javafx.scene.control.Tooltip;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.layout.BorderPane;
/*     */ import javafx.scene.layout.GridPane;
/*     */ import javafx.scene.layout.HBox;
/*     */ import javafx.scene.layout.Priority;
/*     */ import javafx.scene.layout.Region;
/*     */ import org.controlsfx.control.PropertySheet;
/*     */ import org.controlsfx.control.SegmentedButton;
/*     */ import org.controlsfx.control.action.Action;
/*     */ import org.controlsfx.control.action.ActionUtils;
/*     */ import org.controlsfx.control.textfield.TextFields;
/*     */ import org.controlsfx.property.editor.AbstractPropertyEditor;
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
/*     */ public class PropertySheetSkin
/*     */   extends BehaviorSkinBase<PropertySheet, BehaviorBase<PropertySheet>>
/*     */ {
/*     */   private static final int MIN_COLUMN_WIDTH = 100;
/*     */   private final BorderPane content;
/*     */   private final ScrollPane scroller;
/*     */   private final ToolBar toolbar;
/*  90 */   private final SegmentedButton modeButton = ActionUtils.createSegmentedButton(new Action[] { new ActionChangeMode(PropertySheet.Mode.NAME), new ActionChangeMode(PropertySheet.Mode.CATEGORY) });
/*     */ 
/*     */ 
/*     */   
/*  94 */   private final TextField searchField = TextFields.createClearableTextField();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySheetSkin(PropertySheet control) {
/* 104 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */     
/* 106 */     this.scroller = new ScrollPane();
/* 107 */     this.scroller.setFitToWidth(true);
/*     */     
/* 109 */     this.toolbar = new ToolBar();
/* 110 */     this.toolbar.managedProperty().bind((ObservableValue)this.toolbar.visibleProperty());
/* 111 */     this.toolbar.setFocusTraversable(true);
/*     */ 
/*     */     
/* 114 */     this.modeButton.managedProperty().bind((ObservableValue)this.modeButton.visibleProperty());
/* 115 */     ((ToggleButton)this.modeButton.getButtons().get(((PropertySheet.Mode)((PropertySheet)getSkinnable()).modeProperty().get()).ordinal())).setSelected(true);
/* 116 */     this.toolbar.getItems().add(this.modeButton);
/*     */ 
/*     */     
/* 119 */     this.searchField.setPromptText(Localization.localize(Localization.asKey("property.sheet.search.field.prompt")));
/* 120 */     this.searchField.setMinWidth(0.0D);
/* 121 */     HBox.setHgrow((Node)this.searchField, Priority.SOMETIMES);
/* 122 */     this.searchField.managedProperty().bind((ObservableValue)this.searchField.visibleProperty());
/* 123 */     this.toolbar.getItems().add(this.searchField);
/*     */ 
/*     */     
/* 126 */     this.content = new BorderPane();
/* 127 */     this.content.setTop((Node)this.toolbar);
/* 128 */     this.content.setCenter((Node)this.scroller);
/* 129 */     getChildren().add(this.content);
/*     */ 
/*     */ 
/*     */     
/* 133 */     registerChangeListener((ObservableValue)control.modeProperty(), "MODE");
/* 134 */     registerChangeListener((ObservableValue)control.propertyEditorFactory(), "EDITOR-FACTORY");
/* 135 */     registerChangeListener((ObservableValue)control.titleFilter(), "FILTER");
/* 136 */     registerChangeListener((ObservableValue)this.searchField.textProperty(), "FILTER-UI");
/* 137 */     registerChangeListener((ObservableValue)control.modeSwitcherVisibleProperty(), "TOOLBAR-MODE");
/* 138 */     registerChangeListener((ObservableValue)control.searchBoxVisibleProperty(), "TOOLBAR-SEARCH");
/*     */     
/* 140 */     control.getItems().addListener(change -> refreshProperties());
/*     */ 
/*     */     
/* 143 */     refreshProperties();
/* 144 */     updateToolbar();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/* 155 */     super.handleControlPropertyChanged(p);
/*     */     
/* 157 */     if (p == "MODE" || p == "EDITOR-FACTORY" || p == "FILTER") {
/* 158 */       refreshProperties();
/* 159 */     } else if (p == "FILTER-UI") {
/* 160 */       ((PropertySheet)getSkinnable()).setTitleFilter(this.searchField.getText());
/* 161 */     } else if (p == "TOOLBAR-MODE") {
/* 162 */       updateToolbar();
/* 163 */     } else if (p == "TOOLBAR-SEARCH") {
/* 164 */       updateToolbar();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 169 */     this.content.resizeRelocate(x, y, w, h);
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
/*     */   private void updateToolbar() {
/* 181 */     this.modeButton.setVisible(((PropertySheet)getSkinnable()).isModeSwitcherVisible());
/* 182 */     this.searchField.setVisible(((PropertySheet)getSkinnable()).isSearchBoxVisible());
/*     */     
/* 184 */     this.toolbar.setVisible((this.modeButton.isVisible() || this.searchField.isVisible()));
/*     */   }
/*     */   
/*     */   private void refreshProperties() {
/* 188 */     this.scroller.setContent(buildPropertySheetContainer());
/*     */   } private Node buildPropertySheetContainer() {
/*     */     Map<String, List<PropertySheet.Item>> categoryMap;
/*     */     Accordion accordion;
/* 192 */     switch ((PropertySheet.Mode)((PropertySheet)getSkinnable()).modeProperty().get()) {
/*     */       
/*     */       case CATEGORY:
/* 195 */         categoryMap = new TreeMap<>();
/* 196 */         for (PropertySheet.Item p : ((PropertySheet)getSkinnable()).getItems()) {
/* 197 */           String category = p.getCategory();
/* 198 */           List<PropertySheet.Item> list = categoryMap.get(category);
/* 199 */           if (list == null) {
/* 200 */             list = new ArrayList<>();
/* 201 */             categoryMap.put(category, list);
/*     */           } 
/* 203 */           list.add(p);
/*     */         } 
/*     */ 
/*     */         
/* 207 */         accordion = new Accordion();
/* 208 */         for (String category : categoryMap.keySet()) {
/* 209 */           PropertyPane props = new PropertyPane(categoryMap.get(category));
/*     */           
/* 211 */           if (props.getChildrenUnmodifiable().size() > 0) {
/* 212 */             TitledPane pane = new TitledPane(category, (Node)props);
/* 213 */             pane.setExpanded(true);
/* 214 */             accordion.getPanes().add(pane);
/*     */           } 
/*     */         } 
/* 217 */         if (accordion.getPanes().size() > 0) {
/* 218 */           accordion.setExpandedPane((TitledPane)accordion.getPanes().get(0));
/*     */         }
/* 220 */         return (Node)accordion;
/*     */     } 
/*     */     
/* 223 */     return (Node)new PropertyPane((List<PropertySheet.Item>)((PropertySheet)getSkinnable()).getItems());
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
/*     */   private class ActionChangeMode
/*     */     extends Action
/*     */   {
/* 237 */     private final Image CATEGORY_IMAGE = new Image(PropertySheetSkin.class.getResource("/org/controlsfx/control/format-indent-more.png").toExternalForm());
/* 238 */     private final Image NAME_IMAGE = new Image(PropertySheetSkin.class.getResource("/org/controlsfx/control/format-line-spacing-triple.png").toExternalForm());
/*     */     
/*     */     public ActionChangeMode(PropertySheet.Mode mode) {
/* 241 */       super("");
/* 242 */       setEventHandler(ae -> ((PropertySheet)PropertySheetSkin.this.getSkinnable()).modeProperty().set(mode));
/*     */       
/* 244 */       if (mode == PropertySheet.Mode.CATEGORY) {
/* 245 */         setGraphic((Node)new ImageView(this.CATEGORY_IMAGE));
/* 246 */         setLongText(Localization.localize(Localization.asKey("property.sheet.group.mode.bycategory")));
/* 247 */       } else if (mode == PropertySheet.Mode.NAME) {
/* 248 */         setGraphic((Node)new ImageView(this.NAME_IMAGE));
/* 249 */         setLongText(Localization.localize(Localization.asKey("property.sheet.group.mode.byname")));
/*     */       } else {
/* 251 */         setText("???");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class PropertyPane
/*     */     extends GridPane
/*     */   {
/*     */     public PropertyPane(List<PropertySheet.Item> properties) {
/* 261 */       this(properties, 0);
/*     */     }
/*     */     
/*     */     public PropertyPane(List<PropertySheet.Item> properties, int nestingLevel) {
/* 265 */       setVgap(5.0D);
/* 266 */       setHgap(5.0D);
/* 267 */       setPadding(new Insets(5.0D, 15.0D, 5.0D, (15 + nestingLevel * 10)));
/* 268 */       getStyleClass().add("property-pane");
/* 269 */       setItems(properties);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setItems(List<PropertySheet.Item> properties) {
/* 274 */       getChildren().clear();
/*     */       
/* 276 */       String filter = ((PropertySheet)PropertySheetSkin.this.getSkinnable()).titleFilter().get();
/* 277 */       filter = (filter == null) ? "" : filter.trim().toLowerCase();
/*     */       
/* 279 */       int row = 0;
/*     */       
/* 281 */       for (PropertySheet.Item item : properties) {
/*     */ 
/*     */         
/* 284 */         String title = item.getName();
/*     */         
/* 286 */         if (!filter.isEmpty() && title.toLowerCase().indexOf(filter) < 0) {
/*     */           continue;
/*     */         }
/* 289 */         Label label = new Label(title);
/* 290 */         label.setMinWidth(100.0D);
/*     */ 
/*     */         
/* 293 */         String description = item.getDescription();
/* 294 */         if (description != null && !description.trim().isEmpty()) {
/* 295 */           label.setTooltip(new Tooltip(description));
/*     */         }
/*     */         
/* 298 */         add((Node)label, 0, row);
/*     */ 
/*     */         
/* 301 */         Node editor = getEditor(item);
/*     */         
/* 303 */         if (editor instanceof Region) {
/* 304 */           ((Region)editor).setMinWidth(100.0D);
/* 305 */           ((Region)editor).setMaxWidth(Double.MAX_VALUE);
/*     */         } 
/* 307 */         label.setLabelFor(editor);
/* 308 */         add(editor, 1, row);
/* 309 */         GridPane.setHgrow(editor, Priority.ALWAYS);
/*     */ 
/*     */ 
/*     */         
/* 313 */         row++;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Node getEditor(PropertySheet.Item item) {
/*     */       AbstractPropertyEditor<Object, TextField> abstractPropertyEditor;
/* 321 */       PropertyEditor editor = (PropertyEditor)((PropertySheet)PropertySheetSkin.this.getSkinnable()).getPropertyEditorFactory().call(item);
/* 322 */       if (editor == null) {
/* 323 */         abstractPropertyEditor = new AbstractPropertyEditor<Object, TextField>(item, new TextField(), true)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             protected ObservableValue<Object> getObservableValue()
/*     */             {
/* 333 */               return (ObservableValue<Object>)((TextField)getEditor()).textProperty();
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void setValue(Object value) {
/* 340 */               ((TextField)getEditor()).setText((value == null) ? "" : value.toString());
/*     */             }
/*     */           };
/* 343 */       } else if (!item.isEditable()) {
/* 344 */         abstractPropertyEditor.getEditor().setDisable(true);
/*     */       } 
/* 346 */       abstractPropertyEditor.setValue(item.getValue());
/* 347 */       return abstractPropertyEditor.getEditor();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\PropertySheetSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */