/*    */ package impl.org.controlsfx.spreadsheet;
/*    */ 
/*    */ import javafx.collections.ObservableList;
/*    */ import javafx.scene.control.Skin;
/*    */ import javafx.scene.control.TableView;
/*    */ import org.controlsfx.control.spreadsheet.SpreadsheetCell;
/*    */ import org.controlsfx.control.spreadsheet.SpreadsheetView;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpreadsheetGridView
/*    */   extends TableView<ObservableList<SpreadsheetCell>>
/*    */ {
/*    */   private final SpreadsheetHandle handle;
/*    */   private String stylesheet;
/*    */   
/*    */   public SpreadsheetGridView(SpreadsheetHandle handle) {
/* 48 */     this.handle = handle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserAgentStylesheet() {
/* 56 */     if (this.stylesheet == null) {
/* 57 */       this
/* 58 */         .stylesheet = SpreadsheetView.class.getResource("spreadsheet.css").toExternalForm();
/*    */     }
/*    */     
/* 61 */     return this.stylesheet;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Skin<?> createDefaultSkin() {
/* 66 */     return (Skin<?>)new GridViewSkin(this.handle);
/*    */   }
/*    */   
/*    */   public GridViewSkin getGridViewSkin() {
/* 70 */     return this.handle.getCellsViewSkin();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\SpreadsheetGridView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */