/*    */ package impl.org.controlsfx.skin;
/*    */ 
/*    */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*    */ import com.sun.javafx.scene.control.skin.CellSkinBase;
/*    */ import java.util.Collections;
/*    */ import javafx.scene.control.Cell;
/*    */ import javafx.scene.control.Control;
/*    */ import org.controlsfx.control.GridCell;
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
/*    */ public class GridCellSkin<T>
/*    */   extends CellSkinBase<GridCell<T>, BehaviorBase<GridCell<T>>>
/*    */ {
/*    */   public GridCellSkin(GridCell<T> control) {
/* 40 */     super((Cell)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\GridCellSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */