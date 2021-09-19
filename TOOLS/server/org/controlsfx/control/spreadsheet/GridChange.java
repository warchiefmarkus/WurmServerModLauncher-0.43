/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridChange
/*     */   extends Event
/*     */   implements Serializable
/*     */ {
/*  45 */   public static final EventType<GridChange> GRID_CHANGE_EVENT = new EventType(Event.ANY, "GridChange");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 210644901287223524L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int modelRow;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int column;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object oldValue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object newValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridChange(int modelRow, int column, Object oldValue, Object newValue) {
/*  79 */     super(GRID_CHANGE_EVENT);
/*  80 */     this.modelRow = modelRow;
/*  81 */     this.column = column;
/*  82 */     this.oldValue = oldValue;
/*  83 */     this.newValue = newValue;
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
/*     */   public int getRow() {
/*  97 */     return this.modelRow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 106 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOldValue() {
/* 115 */     return this.oldValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNewValue() {
/* 124 */     return this.newValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\GridChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */