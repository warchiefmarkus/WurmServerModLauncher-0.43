/*     */ package org.controlsfx.control.table;
/*     */ 
/*     */ import impl.org.controlsfx.skin.ExpandableTableRowSkin;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.TableCell;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableRow;
/*     */ import javafx.scene.control.TableView;
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
/*     */ public final class TableRowExpanderColumn<S>
/*     */   extends TableColumn<S, Boolean>
/*     */ {
/*     */   private static final String STYLE_CLASS = "expander-column";
/*     */   private static final String EXPANDER_BUTTON_STYLE_CLASS = "expander-button";
/*  93 */   private final Map<S, Node> expandedNodeCache = new HashMap<>();
/*  94 */   private final Map<S, BooleanProperty> expansionState = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Callback<TableRowDataFeatures<S>, Node> expandedNodeCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanProperty getExpandedProperty(S item) {
/*     */     SimpleBooleanProperty simpleBooleanProperty;
/* 105 */     BooleanProperty value = this.expansionState.get(item);
/* 106 */     if (value == null) {
/* 107 */       simpleBooleanProperty = new SimpleBooleanProperty(item, "expanded", false)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           protected void invalidated()
/*     */           {
/* 114 */             TableRowExpanderColumn.this.getTableView().refresh();
/* 115 */             if (!getValue().booleanValue()) TableRowExpanderColumn.this.expandedNodeCache.remove(getBean()); 
/*     */           }
/*     */         };
/* 118 */       this.expansionState.put(item, simpleBooleanProperty);
/*     */     } 
/* 120 */     return (BooleanProperty)simpleBooleanProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getOrCreateExpandedNode(TableRow<S> tableRow) {
/* 130 */     int index = tableRow.getIndex();
/* 131 */     if (index > -1 && index < getTableView().getItems().size()) {
/* 132 */       S item = (S)getTableView().getItems().get(index);
/* 133 */       Node node = this.expandedNodeCache.get(item);
/* 134 */       if (node == null) {
/* 135 */         node = (Node)this.expandedNodeCallback.call(new TableRowDataFeatures<>(tableRow, this, item));
/* 136 */         this.expandedNodeCache.put(item, node);
/*     */       } 
/* 138 */       return node;
/*     */     } 
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getExpandedNode(S item) {
/* 150 */     return this.expandedNodeCache.get(item);
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
/*     */   public TableRowExpanderColumn(Callback<TableRowDataFeatures<S>, Node> expandedNodeCallback) {
/* 169 */     this.expandedNodeCallback = expandedNodeCallback;
/*     */     
/* 171 */     getStyleClass().add("expander-column");
/* 172 */     setCellValueFactory(param -> getExpandedProperty((S)param.getValue()));
/* 173 */     setCellFactory(param -> new ToggleCell());
/* 174 */     installRowFactoryOnTableViewAssignment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void installRowFactoryOnTableViewAssignment() {
/* 181 */     tableViewProperty().addListener((observable, oldValue, newValue) -> {
/*     */           if (newValue != null) {
/*     */             getTableView().setRowFactory(());
/*     */           }
/*     */         });
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
/*     */   private final class ToggleCell
/*     */     extends TableCell<S, Boolean>
/*     */   {
/* 200 */     private Button button = new Button();
/*     */     
/*     */     public ToggleCell() {
/* 203 */       this.button.setFocusTraversable(false);
/* 204 */       this.button.getStyleClass().add("expander-button");
/* 205 */       this.button.setPrefSize(16.0D, 16.0D);
/* 206 */       this.button.setPadding(new Insets(0.0D));
/* 207 */       this.button.setOnAction(event -> TableRowExpanderColumn.this.toggleExpanded(getIndex()));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateItem(Boolean expanded, boolean empty) {
/* 212 */       super.updateItem(expanded, empty);
/* 213 */       if (expanded == null || empty) {
/* 214 */         setGraphic(null);
/*     */       } else {
/* 216 */         this.button.setText(expanded.booleanValue() ? "-" : "+");
/* 217 */         setGraphic((Node)this.button);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toggleExpanded(int index) {
/* 228 */     BooleanProperty expanded = (BooleanProperty)getCellObservableValue(index);
/* 229 */     expanded.setValue(Boolean.valueOf(!expanded.getValue().booleanValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class TableRowDataFeatures<S>
/*     */   {
/*     */     private TableRow<S> tableRow;
/*     */ 
/*     */     
/*     */     private TableRowExpanderColumn<S> tableColumn;
/*     */ 
/*     */     
/*     */     private BooleanProperty expandedProperty;
/*     */ 
/*     */     
/*     */     private S value;
/*     */ 
/*     */ 
/*     */     
/*     */     public TableRowDataFeatures(TableRow<S> tableRow, TableRowExpanderColumn<S> tableColumn, S value) {
/* 250 */       this.tableRow = tableRow;
/* 251 */       this.tableColumn = tableColumn;
/* 252 */       this.expandedProperty = (BooleanProperty)tableColumn.getCellObservableValue(tableRow.getIndex());
/* 253 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TableRow<S> getTableRow() {
/* 264 */       return this.tableRow;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TableRowExpanderColumn<S> getTableColumn() {
/* 274 */       return this.tableColumn;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BooleanProperty expandedProperty() {
/* 284 */       return this.expandedProperty;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void toggleExpanded() {
/* 291 */       BooleanProperty expanded = expandedProperty();
/* 292 */       expanded.setValue(Boolean.valueOf(!expanded.getValue().booleanValue()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Boolean isExpanded() {
/* 301 */       return expandedProperty().getValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setExpanded(Boolean expanded) {
/* 310 */       expandedProperty().setValue(expanded);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public S getValue() {
/* 320 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\TableRowExpanderColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */