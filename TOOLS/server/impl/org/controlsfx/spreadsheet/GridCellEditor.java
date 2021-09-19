/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.binding.BooleanExpression;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.Parent;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.input.KeyCode;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCell;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridCellEditor
/*     */ {
/*     */   private final SpreadsheetHandle handle;
/*     */   private SpreadsheetCell modelCell;
/*     */   private CellView viewCell;
/*     */   private BooleanExpression focusProperty;
/*     */   private boolean editing = false;
/*     */   private SpreadsheetCellEditor spreadsheetCellEditor;
/*     */   private KeyCode lastKeyPressed;
/*     */   private final EventHandler<KeyEvent> enterKeyPressed;
/*     */   private final ChangeListener<Boolean> focusListener;
/*     */   private final InvalidationListener endEditionListener;
/*     */   
/*     */   public void updateDataCell(SpreadsheetCell cell) {
/*  88 */     this.modelCell = cell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSpreadsheetCell(CellView cell) {
/*  97 */     this.viewCell = cell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSpreadsheetCellEditor(SpreadsheetCellEditor spreadsheetCellEditor) {
/* 106 */     this.spreadsheetCellEditor = spreadsheetCellEditor;
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
/*     */   public void endEdit(boolean commitValue) {
/* 122 */     if (commitValue && this.editing) {
/* 123 */       SpreadsheetView view = this.handle.getView();
/* 124 */       boolean match = this.modelCell.getCellType().match(this.spreadsheetCellEditor.getControlValue());
/*     */       
/* 126 */       if (match && this.viewCell != null) {
/* 127 */         Object value = this.modelCell.getCellType().convertValue(this.spreadsheetCellEditor.getControlValue());
/*     */ 
/*     */         
/* 130 */         view.getGrid().setCellValue(this.modelCell.getRow(), this.modelCell.getColumn(), value);
/* 131 */         this.editing = false;
/* 132 */         this.viewCell.commitEdit(this.modelCell);
/* 133 */         end();
/* 134 */         this.spreadsheetCellEditor.end();
/*     */ 
/*     */         
/* 137 */         if (KeyCode.ENTER.equals(this.lastKeyPressed)) {
/* 138 */           ((GridViewBehavior)this.handle.getCellsViewSkin().getBehavior()).selectCell(1, 0);
/* 139 */         } else if (KeyCode.TAB.equals(this.lastKeyPressed)) {
/* 140 */           this.handle.getView().getSelectionModel().clearAndSelectRightCell();
/* 141 */           this.handle.getCellsViewSkin().scrollHorizontally();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (this.editing) {
/* 147 */       this.editing = false;
/* 148 */       if (this.viewCell != null) {
/* 149 */         this.viewCell.cancelEdit();
/*     */       }
/* 151 */       end();
/* 152 */       if (this.spreadsheetCellEditor != null) {
/* 153 */         this.spreadsheetCellEditor.end();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEditing() {
/* 164 */     return this.editing;
/*     */   }
/*     */   
/*     */   public SpreadsheetCell getModelCell() {
/* 168 */     return this.modelCell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void startEdit() {
/* 176 */     this.lastKeyPressed = null;
/* 177 */     this.editing = true;
/*     */     
/* 179 */     this.handle.getGridView().addEventFilter(KeyEvent.KEY_PRESSED, this.enterKeyPressed);
/*     */     
/* 181 */     this.handle.getCellsViewSkin().getVBar().valueProperty().addListener(this.endEditionListener);
/* 182 */     this.handle.getCellsViewSkin().getHBar().valueProperty().addListener(this.endEditionListener);
/*     */     
/* 184 */     Control editor = this.spreadsheetCellEditor.getEditor();
/*     */ 
/*     */     
/* 187 */     Object value = this.modelCell.getItem();
/*     */     
/* 189 */     Double maxHeight = Double.valueOf(Math.min(this.viewCell.getHeight(), this.spreadsheetCellEditor.getMaxHeight()));
/*     */     
/* 191 */     if (editor != null) {
/* 192 */       this.viewCell.setGraphic((Node)editor);
/* 193 */       editor.setMaxHeight(maxHeight.doubleValue());
/* 194 */       editor.setPrefWidth(this.viewCell.getWidth());
/*     */     } 
/*     */     
/* 197 */     this.spreadsheetCellEditor.startEdit(value, this.modelCell.getFormat());
/*     */     
/* 199 */     if (editor != null) {
/* 200 */       this.focusProperty = getFocusProperty(editor);
/* 201 */       this.focusProperty.addListener(this.focusListener);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void end() {
/* 206 */     if (this.focusProperty != null) {
/* 207 */       this.focusProperty.removeListener(this.focusListener);
/* 208 */       this.focusProperty = null;
/*     */     } 
/* 210 */     this.handle.getCellsViewSkin().getVBar().valueProperty().removeListener(this.endEditionListener);
/* 211 */     this.handle.getCellsViewSkin().getHBar().valueProperty().removeListener(this.endEditionListener);
/*     */     
/* 213 */     this.handle.getGridView().removeEventFilter(KeyEvent.KEY_PRESSED, this.enterKeyPressed);
/*     */     
/* 215 */     this.modelCell = null;
/* 216 */     this.viewCell = null;
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
/*     */   private BooleanExpression getFocusProperty(Control control) {
/* 228 */     if (control instanceof javafx.scene.control.TextArea) {
/* 229 */       return (BooleanExpression)Bindings.createBooleanBinding(() -> { if (this.handle.getView().getScene() == null) return Boolean.valueOf(false);  Node n = this.handle.getView().getScene().getFocusOwner(); while (n != null) { if (n == control) return Boolean.valueOf(true);  Parent parent = n.getParent(); }  return Boolean.valueOf(false); }new Observable[] { (Observable)this.handle
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 239 */             .getView().getScene().focusOwnerProperty() });
/*     */     }
/* 241 */     return (BooleanExpression)control.focusedProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridCellEditor(SpreadsheetHandle handle) {
/* 248 */     this.enterKeyPressed = new EventHandler<KeyEvent>()
/*     */       {
/*     */         public void handle(KeyEvent t) {
/* 251 */           GridCellEditor.this.lastKeyPressed = t.getCode();
/*     */         }
/*     */       };
/*     */     
/* 255 */     this.focusListener = new ChangeListener<Boolean>()
/*     */       {
/*     */         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isFocus) {
/* 258 */           if (!isFocus.booleanValue()) {
/* 259 */             GridCellEditor.this.endEdit(true);
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 264 */     this.endEditionListener = new InvalidationListener()
/*     */       {
/*     */         public void invalidated(Observable observable) {
/* 267 */           GridCellEditor.this.endEdit(true);
/*     */         }
/*     */       };
/*     */     this.handle = handle;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\GridCellEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */