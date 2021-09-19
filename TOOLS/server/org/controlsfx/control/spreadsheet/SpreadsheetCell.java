/*    */ package org.controlsfx.control.spreadsheet;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import javafx.beans.property.ObjectProperty;
/*    */ import javafx.beans.property.ReadOnlyStringProperty;
/*    */ import javafx.beans.property.StringProperty;
/*    */ import javafx.collections.ObservableSet;
/*    */ import javafx.event.Event;
/*    */ import javafx.event.EventHandler;
/*    */ import javafx.event.EventType;
/*    */ import javafx.scene.Node;
/*    */ import javafx.scene.control.MenuItem;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SpreadsheetCell
/*    */ {
/* 54 */   public static final EventType EDITABLE_EVENT_TYPE = new EventType("EditableEventType"); boolean match(SpreadsheetCell paramSpreadsheetCell); void setItem(Object paramObject); Object getItem(); ObjectProperty<Object> itemProperty();
/*    */   boolean isEditable();
/*    */   void setEditable(boolean paramBoolean);
/*    */   boolean isWrapText();
/*    */   void setWrapText(boolean paramBoolean);
/*    */   boolean hasPopup();
/* 60 */   public static final EventType WRAP_EVENT_TYPE = new EventType("WrapTextEventType"); void setHasPopup(boolean paramBoolean); List<MenuItem> getPopupItems(); void setStyle(String paramString); String getStyle(); StringProperty styleProperty(); void activateCorner(CornerPosition paramCornerPosition); void deactivateCorner(CornerPosition paramCornerPosition); boolean isCornerActivated(CornerPosition paramCornerPosition); StringProperty formatProperty(); String getFormat(); void setFormat(String paramString); ReadOnlyStringProperty textProperty(); String getText();
/*    */   SpreadsheetCellType getCellType();
/*    */   int getRow();
/*    */   int getColumn();
/*    */   int getRowSpan();
/*    */   void setRowSpan(int paramInt);
/* 66 */   public static final EventType CORNER_EVENT_TYPE = new EventType("CornerEventType"); int getColumnSpan(); void setColumnSpan(int paramInt);
/*    */   ObservableSet<String> getStyleClass();
/*    */   ObjectProperty<Node> graphicProperty();
/*    */   void setGraphic(Node paramNode);
/*    */   Node getGraphic();
/*    */   Optional<String> getTooltip();
/*    */   void addEventHandler(EventType<Event> paramEventType, EventHandler<Event> paramEventHandler);
/*    */   void removeEventHandler(EventType<Event> paramEventType, EventHandler<Event> paramEventHandler);
/* 74 */   public enum CornerPosition { TOP_LEFT,
/* 75 */     TOP_RIGHT,
/* 76 */     BOTTOM_RIGHT,
/* 77 */     BOTTOM_LEFT; }
/*    */ 
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\SpreadsheetCell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */