/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.event.EventHandlerManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ReadOnlyStringProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableSet;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventDispatchChain;
/*     */ import javafx.event.EventDispatcher;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.event.EventType;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.MenuItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpreadsheetCellBase
/*     */   implements SpreadsheetCell, EventTarget
/*     */ {
/*     */   private static final int EDITABLE_BIT_POSITION = 4;
/*     */   private static final int WRAP_BIT_POSITION = 5;
/*     */   private static final int POPUP_BIT_POSITION = 6;
/*     */   private final SpreadsheetCellType type;
/*     */   private final int row;
/*     */   private final int column;
/*     */   private int rowSpan;
/*     */   private int columnSpan;
/*     */   private final StringProperty format;
/*     */   private final StringProperty text;
/*     */   private final StringProperty styleProperty;
/*     */   private final ObjectProperty<Node> graphic;
/*     */   private String tooltip;
/* 237 */   private int propertyContainer = 0;
/* 238 */   private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObservableSet<String> styleClass;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<MenuItem> actionsList;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ObjectProperty<Object> item;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpreadsheetCellBase(int row, int column, int rowSpan, int columnSpan) {
/* 258 */     this(row, column, rowSpan, columnSpan, SpreadsheetCellType.OBJECT);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(SpreadsheetCell cell) {
/* 301 */     return this.type.match(cell);
/*     */   }
/*     */   
/*     */   public SpreadsheetCellBase(int row, int column, int rowSpan, int columnSpan, SpreadsheetCellType<?> type) {
/* 305 */     this.item = (ObjectProperty<Object>)new SimpleObjectProperty<Object>(this, "item")
/*     */       {
/*     */         protected void invalidated() {
/* 308 */           SpreadsheetCellBase.this.updateText(); }
/*     */       }; this.row = row; this.column = column; this.rowSpan = rowSpan; this.columnSpan = columnSpan; this.type = type; this.text = (StringProperty)new SimpleStringProperty("");
/*     */     this.format = (StringProperty)new SimpleStringProperty("");
/*     */     this.graphic = (ObjectProperty<Node>)new SimpleObjectProperty();
/*     */     this.format.addListener(new ChangeListener<String>() { public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) { SpreadsheetCellBase.this.updateText(); } });
/*     */     setEditable(true);
/*     */     getStyleClass().add("spreadsheet-cell");
/* 315 */     this.styleProperty = (StringProperty)new SimpleStringProperty(); } public final void setItem(Object value) { if (isEditable()) {
/* 316 */       this.item.set(value);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getItem() {
/* 322 */     return this.item.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObjectProperty<Object> itemProperty() {
/* 328 */     return this.item;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEditable() {
/* 334 */     return isSet(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setEditable(boolean editable) {
/* 340 */     if (setMask(editable, 4)) {
/* 341 */       Event.fireEvent(this, new Event(EDITABLE_EVENT_TYPE));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapText() {
/* 348 */     return isSet(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWrapText(boolean wrapText) {
/* 354 */     if (setMask(wrapText, 5)) {
/* 355 */       Event.fireEvent(this, new Event(WRAP_EVENT_TYPE));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPopup() {
/* 362 */     return isSet(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasPopup(boolean value) {
/* 368 */     setMask(value, 6);
/*     */     
/* 370 */     Event.fireEvent(this, new Event(CORNER_EVENT_TYPE));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MenuItem> getPopupItems() {
/* 376 */     if (this.actionsList == null) {
/* 377 */       this.actionsList = new ArrayList<>();
/*     */     }
/* 379 */     return this.actionsList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringProperty formatProperty() {
/* 385 */     return this.format;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getFormat() {
/* 391 */     return (String)this.format.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setFormat(String format) {
/* 397 */     formatProperty().set(format);
/* 398 */     updateText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ReadOnlyStringProperty textProperty() {
/* 404 */     return (ReadOnlyStringProperty)this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getText() {
/* 410 */     return (String)this.text.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final SpreadsheetCellType getCellType() {
/* 416 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getRow() {
/* 422 */     return this.row;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getColumn() {
/* 428 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getRowSpan() {
/* 434 */     return this.rowSpan;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setRowSpan(int rowSpan) {
/* 440 */     this.rowSpan = rowSpan;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getColumnSpan() {
/* 446 */     return this.columnSpan;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setColumnSpan(int columnSpan) {
/* 452 */     this.columnSpan = columnSpan;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObservableSet<String> getStyleClass() {
/* 458 */     if (this.styleClass == null) {
/* 459 */       this.styleClass = FXCollections.observableSet((Object[])new String[0]);
/*     */     }
/* 461 */     return this.styleClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyle(String style) {
/* 467 */     this.styleProperty.set(style);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStyle() {
/* 473 */     return (String)this.styleProperty.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StringProperty styleProperty() {
/* 479 */     return this.styleProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectProperty<Node> graphicProperty() {
/* 485 */     return this.graphic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGraphic(Node graphic) {
/* 491 */     this.graphic.set(graphic);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getGraphic() {
/* 497 */     return (Node)this.graphic.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<String> getTooltip() {
/* 503 */     return Optional.ofNullable(this.tooltip);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTooltip(String tooltip) {
/* 511 */     this.tooltip = tooltip;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateCorner(SpreadsheetCell.CornerPosition position) {
/* 517 */     if (setMask(true, getCornerBitNumber(position))) {
/* 518 */       Event.fireEvent(this, new Event(CORNER_EVENT_TYPE));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deactivateCorner(SpreadsheetCell.CornerPosition position) {
/* 525 */     if (setMask(false, getCornerBitNumber(position))) {
/* 526 */       Event.fireEvent(this, new Event(CORNER_EVENT_TYPE));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCornerActivated(SpreadsheetCell.CornerPosition position) {
/* 533 */     return isSet(getCornerBitNumber(position));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
/* 539 */     return tail.append((EventDispatcher)this.eventHandlerManager);
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
/*     */   public String toString() {
/* 551 */     return "cell[" + this.row + "][" + this.column + "]" + this.rowSpan + "-" + this.columnSpan;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 557 */     if (this == obj)
/* 558 */       return true; 
/* 559 */     if (!(obj instanceof SpreadsheetCell)) {
/* 560 */       return false;
/*     */     }
/* 562 */     SpreadsheetCell otherCell = (SpreadsheetCell)obj;
/* 563 */     return (otherCell.getRow() == this.row && otherCell.getColumn() == this.column && 
/* 564 */       Objects.equals(otherCell.getText(), getText()) && this.rowSpan == otherCell
/* 565 */       .getRowSpan() && this.columnSpan == otherCell
/* 566 */       .getColumnSpan() && 
/* 567 */       Objects.equals(getStyleClass(), otherCell.getStyleClass()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 573 */     int prime = 31;
/* 574 */     int result = 1;
/* 575 */     result = 31 * result + this.column;
/* 576 */     result = 31 * result + this.row;
/* 577 */     result = 31 * result + this.rowSpan;
/* 578 */     result = 31 * result + this.columnSpan;
/* 579 */     result = 31 * result + Objects.hashCode(getText());
/* 580 */     result = 31 * result + Objects.hashCode(getStyleClass());
/* 581 */     return result;
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
/*     */   public void addEventHandler(EventType<Event> eventType, EventHandler<Event> eventHandler) {
/* 595 */     this.eventHandlerManager.addEventHandler(eventType, eventHandler);
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
/*     */   public void removeEventHandler(EventType<Event> eventType, EventHandler<Event> eventHandler) {
/* 610 */     this.eventHandlerManager.removeEventHandler(eventType, eventHandler);
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
/*     */   private void updateText() {
/* 624 */     if (getItem() == null) {
/* 625 */       this.text.setValue("");
/* 626 */     } else if (!"".equals(getFormat())) {
/* 627 */       this.text.setValue(this.type.toString(getItem(), getFormat()));
/*     */     } else {
/* 629 */       this.text.setValue(this.type.toString(getItem()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getCornerBitNumber(SpreadsheetCell.CornerPosition position) {
/* 639 */     switch (position) {
/*     */       case TOP_LEFT:
/* 641 */         return 0;
/*     */       
/*     */       case TOP_RIGHT:
/* 644 */         return 1;
/*     */       
/*     */       case BOTTOM_RIGHT:
/* 647 */         return 2;
/*     */     } 
/*     */ 
/*     */     
/* 651 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean setMask(boolean flag, int position) {
/* 662 */     int oldCorner = this.propertyContainer;
/* 663 */     if (flag) {
/* 664 */       this.propertyContainer |= 1 << position;
/*     */     } else {
/* 666 */       this.propertyContainer &= 1 << position ^ 0xFFFFFFFF;
/*     */     } 
/* 668 */     return (this.propertyContainer != oldCorner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSet(int position) {
/* 677 */     return ((this.propertyContainer & 1 << position) != 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\SpreadsheetCellBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */