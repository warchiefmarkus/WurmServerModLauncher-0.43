/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.time.LocalDate;
/*     */ import java.util.List;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.control.ComboBox;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.DatePicker;
/*     */ import javafx.scene.control.IndexRange;
/*     */ import javafx.scene.control.TextArea;
/*     */ import javafx.scene.control.TextField;
/*     */ import javafx.scene.input.KeyCode;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import javafx.util.StringConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SpreadsheetCellEditor
/*     */ {
/*     */   private static final double MAX_EDITOR_HEIGHT = 50.0D;
/* 149 */   private static final DecimalFormat decimalFormat = new DecimalFormat("#.##########");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SpreadsheetView view;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpreadsheetCellEditor(SpreadsheetView view) {
/* 162 */     this.view = view;
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
/*     */   public final void endEdit(boolean b) {
/* 181 */     this.view.getCellsViewSkin().getSpreadsheetCellEditorImpl().endEdit(b);
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
/*     */   public void startEdit(Object item) {
/* 194 */     startEdit(item, null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaxHeight() {
/* 240 */     return 50.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void startEdit(Object paramObject, String paramString);
/*     */ 
/*     */   
/*     */   public abstract Control getEditor();
/*     */ 
/*     */   
/*     */   public abstract String getControlValue();
/*     */ 
/*     */   
/*     */   public abstract void end();
/*     */ 
/*     */   
/*     */   public static class ObjectEditor
/*     */     extends SpreadsheetCellEditor
/*     */   {
/*     */     private final TextField tf;
/*     */ 
/*     */     
/*     */     public ObjectEditor(SpreadsheetView view) {
/* 263 */       super(view);
/* 264 */       this.tf = new TextField();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startEdit(Object value, String format) {
/* 272 */       if (value instanceof String) {
/* 273 */         this.tf.setText(value.toString());
/*     */       }
/* 275 */       attachEnterEscapeEventHandler();
/*     */       
/* 277 */       this.tf.requestFocus();
/* 278 */       this.tf.end();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getControlValue() {
/* 283 */       return this.tf.getText();
/*     */     }
/*     */ 
/*     */     
/*     */     public void end() {
/* 288 */       this.tf.setOnKeyPressed(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public TextField getEditor() {
/* 293 */       return this.tf;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void attachEnterEscapeEventHandler() {
/* 301 */       this.tf.setOnKeyPressed(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent t) {
/* 304 */               if (t.getCode() == KeyCode.ENTER) {
/* 305 */                 SpreadsheetCellEditor.ObjectEditor.this.endEdit(true);
/* 306 */               } else if (t.getCode() == KeyCode.ESCAPE) {
/* 307 */                 SpreadsheetCellEditor.ObjectEditor.this.endEdit(false);
/*     */               } 
/*     */             }
/*     */           });
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
/*     */   public static class StringEditor
/*     */     extends SpreadsheetCellEditor
/*     */   {
/*     */     private final TextField tf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringEditor(SpreadsheetView view) {
/* 333 */       super(view);
/* 334 */       this.tf = new TextField();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startEdit(Object value, String format) {
/* 343 */       if (value instanceof String || value == null) {
/* 344 */         this.tf.setText((String)value);
/*     */       }
/* 346 */       attachEnterEscapeEventHandler();
/*     */       
/* 348 */       this.tf.requestFocus();
/* 349 */       this.tf.selectAll();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getControlValue() {
/* 354 */       return this.tf.getText();
/*     */     }
/*     */ 
/*     */     
/*     */     public void end() {
/* 359 */       this.tf.setOnKeyPressed(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public TextField getEditor() {
/* 364 */       return this.tf;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void attachEnterEscapeEventHandler() {
/* 372 */       this.tf.setOnKeyPressed(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent t) {
/* 375 */               if (t.getCode() == KeyCode.ENTER) {
/* 376 */                 SpreadsheetCellEditor.StringEditor.this.endEdit(true);
/* 377 */               } else if (t.getCode() == KeyCode.ESCAPE) {
/* 378 */                 SpreadsheetCellEditor.StringEditor.this.endEdit(false);
/*     */               } 
/*     */             }
/*     */           });
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
/*     */   public static class TextAreaEditor
/*     */     extends SpreadsheetCellEditor
/*     */   {
/*     */     private final TextArea textArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TextAreaEditor(SpreadsheetView view) {
/* 411 */       super(view);
/* 412 */       this.textArea = new TextArea();
/* 413 */       this.textArea.setWrapText(true);
/*     */       
/* 415 */       this.textArea.minHeightProperty().bind((ObservableValue)this.textArea.maxHeightProperty());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startEdit(Object value, String format) {
/* 425 */       if (value instanceof String || value == null) {
/* 426 */         this.textArea.setText((String)value);
/*     */       }
/* 428 */       attachEnterEscapeEventHandler();
/*     */       
/* 430 */       this.textArea.requestFocus();
/* 431 */       this.textArea.selectAll();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getControlValue() {
/* 436 */       return this.textArea.getText();
/*     */     }
/*     */ 
/*     */     
/*     */     public void end() {
/* 441 */       this.textArea.setOnKeyPressed(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public TextArea getEditor() {
/* 446 */       return this.textArea;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxHeight() {
/* 451 */       return Double.MAX_VALUE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void attachEnterEscapeEventHandler() {
/* 461 */       this.textArea.setOnKeyPressed(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent keyEvent) {
/* 464 */               if (keyEvent.getCode() == KeyCode.ENTER) {
/* 465 */                 if (keyEvent.isShiftDown()) {
/*     */                   
/* 467 */                   SpreadsheetCellEditor.TextAreaEditor.this.textArea.replaceSelection("\n");
/*     */                 } else {
/* 469 */                   SpreadsheetCellEditor.TextAreaEditor.this.endEdit(true);
/*     */                 } 
/* 471 */               } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
/* 472 */                 SpreadsheetCellEditor.TextAreaEditor.this.endEdit(false);
/* 473 */               } else if (keyEvent.getCode() == KeyCode.TAB) {
/* 474 */                 if (keyEvent.isShiftDown()) {
/*     */                   
/* 476 */                   SpreadsheetCellEditor.TextAreaEditor.this.textArea.replaceSelection("\t");
/* 477 */                   keyEvent.consume();
/*     */                 } else {
/* 479 */                   SpreadsheetCellEditor.TextAreaEditor.this.endEdit(true);
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           });
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
/*     */   public static class DoubleEditor
/*     */     extends SpreadsheetCellEditor
/*     */   {
/*     */     private final TextField tf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DoubleEditor(SpreadsheetView view) {
/* 510 */       super(view);
/* 511 */       this.tf = new TextField()
/*     */         {
/*     */           public void insertText(int index, String text)
/*     */           {
/* 515 */             String fixedText = fixText(text);
/* 516 */             super.insertText(index, fixedText);
/*     */           }
/*     */ 
/*     */           
/*     */           public void replaceText(int start, int end, String text) {
/* 521 */             String fixedText = fixText(text);
/* 522 */             super.replaceText(start, end, fixedText);
/*     */           }
/*     */ 
/*     */           
/*     */           public void replaceText(IndexRange range, String text) {
/* 527 */             replaceText(range.getStart(), range.getEnd(), text);
/*     */           }
/*     */           
/*     */           private String fixText(String text) {
/* 531 */             DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Localization.getLocale());
/* 532 */             text = text.replace(' ', ' ');
/* 533 */             return text.replaceAll("\\.", Character.toString(symbols.getDecimalSeparator()));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startEdit(Object value, String format) {
/* 544 */       if (value instanceof Double) {
/*     */         
/* 546 */         SpreadsheetCellEditor.decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Localization.getLocale()));
/* 547 */         this.tf.setText(((Double)value).isNaN() ? "" : SpreadsheetCellEditor.decimalFormat.format(value));
/*     */       } else {
/* 549 */         this.tf.setText(null);
/*     */       } 
/*     */       
/* 552 */       this.tf.getStyleClass().removeAll((Object[])new String[] { "error" });
/* 553 */       attachEnterEscapeEventHandler();
/*     */       
/* 555 */       this.tf.requestFocus();
/* 556 */       this.tf.selectAll();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void end() {
/* 562 */       this.tf.setOnKeyPressed(null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TextField getEditor() {
/* 568 */       return this.tf;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getControlValue() {
/* 574 */       NumberFormat format = NumberFormat.getInstance(Localization.getLocale());
/* 575 */       ParsePosition parsePosition = new ParsePosition(0);
/* 576 */       if (this.tf.getText() != null) {
/* 577 */         Number number = format.parse(this.tf.getText(), parsePosition);
/* 578 */         if (number != null && parsePosition.getIndex() == this.tf.getText().length()) {
/* 579 */           return String.valueOf(number.doubleValue());
/*     */         }
/*     */       } 
/* 582 */       return this.tf.getText();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void attachEnterEscapeEventHandler() {
/* 590 */       this.tf.setOnKeyPressed(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent t) {
/* 593 */               if (t.getCode() == KeyCode.ENTER) {
/*     */                 try {
/* 595 */                   if (SpreadsheetCellEditor.DoubleEditor.this.tf.getText().equals("")) {
/* 596 */                     SpreadsheetCellEditor.DoubleEditor.this.endEdit(true);
/*     */                   } else {
/* 598 */                     SpreadsheetCellEditor.DoubleEditor.this.tryParsing();
/* 599 */                     SpreadsheetCellEditor.DoubleEditor.this.endEdit(true);
/*     */                   } 
/* 601 */                 } catch (Exception exception) {}
/*     */               
/*     */               }
/* 604 */               else if (t.getCode() == KeyCode.ESCAPE) {
/* 605 */                 SpreadsheetCellEditor.DoubleEditor.this.endEdit(false);
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 610 */       this.tf.setOnKeyReleased(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent t) {
/*     */               try {
/* 614 */                 if (SpreadsheetCellEditor.DoubleEditor.this.tf.getText().equals("")) {
/* 615 */                   SpreadsheetCellEditor.DoubleEditor.this.tf.getStyleClass().removeAll((Object[])new String[] { "error" });
/*     */                 } else {
/* 617 */                   SpreadsheetCellEditor.DoubleEditor.this.tryParsing();
/* 618 */                   SpreadsheetCellEditor.DoubleEditor.this.tf.getStyleClass().removeAll((Object[])new String[] { "error" });
/*     */                 } 
/* 620 */               } catch (Exception e) {
/* 621 */                 SpreadsheetCellEditor.DoubleEditor.this.tf.getStyleClass().add("error");
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     private void tryParsing() throws ParseException {
/* 628 */       NumberFormat format = NumberFormat.getNumberInstance(Localization.getLocale());
/* 629 */       ParsePosition parsePosition = new ParsePosition(0);
/* 630 */       format.parse(this.tf.getText(), parsePosition);
/* 631 */       if (parsePosition.getIndex() != this.tf.getText().length()) {
/* 632 */         throw new ParseException("Invalid input", parsePosition.getIndex());
/*     */       }
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
/*     */   public static class IntegerEditor
/*     */     extends SpreadsheetCellEditor
/*     */   {
/*     */     private final TextField tf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public IntegerEditor(SpreadsheetView view) {
/* 660 */       super(view);
/* 661 */       this.tf = new TextField();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startEdit(Object value, String format) {
/* 670 */       if (value instanceof Integer) {
/* 671 */         this.tf.setText(Integer.toString(((Integer)value).intValue()));
/*     */       } else {
/* 673 */         this.tf.setText(null);
/*     */       } 
/*     */       
/* 676 */       this.tf.getStyleClass().removeAll((Object[])new String[] { "error" });
/* 677 */       attachEnterEscapeEventHandler();
/*     */       
/* 679 */       this.tf.requestFocus();
/* 680 */       this.tf.selectAll();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void end() {
/* 686 */       this.tf.setOnKeyPressed(null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TextField getEditor() {
/* 692 */       return this.tf;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getControlValue() {
/* 698 */       return this.tf.getText();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void attachEnterEscapeEventHandler() {
/* 706 */       this.tf.setOnKeyPressed(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent t) {
/* 709 */               if (t.getCode() == KeyCode.ENTER) {
/*     */                 try {
/* 711 */                   if (SpreadsheetCellEditor.IntegerEditor.this.tf.getText().equals("")) {
/* 712 */                     SpreadsheetCellEditor.IntegerEditor.this.endEdit(true);
/*     */                   } else {
/* 714 */                     Integer.parseInt(SpreadsheetCellEditor.IntegerEditor.this.tf.getText());
/* 715 */                     SpreadsheetCellEditor.IntegerEditor.this.endEdit(true);
/*     */                   } 
/* 717 */                 } catch (Exception exception) {}
/*     */               
/*     */               }
/* 720 */               else if (t.getCode() == KeyCode.ESCAPE) {
/* 721 */                 SpreadsheetCellEditor.IntegerEditor.this.endEdit(false);
/*     */               } 
/*     */             }
/*     */           });
/* 725 */       this.tf.setOnKeyReleased(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent t) {
/*     */               try {
/* 729 */                 if (SpreadsheetCellEditor.IntegerEditor.this.tf.getText().equals("")) {
/* 730 */                   SpreadsheetCellEditor.IntegerEditor.this.tf.getStyleClass().removeAll((Object[])new String[] { "error" });
/*     */                 } else {
/* 732 */                   Integer.parseInt(SpreadsheetCellEditor.IntegerEditor.this.tf.getText());
/* 733 */                   SpreadsheetCellEditor.IntegerEditor.this.tf.getStyleClass().removeAll((Object[])new String[] { "error" });
/*     */                 } 
/* 735 */               } catch (Exception e) {
/* 736 */                 SpreadsheetCellEditor.IntegerEditor.this.tf.getStyleClass().add("error");
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ListEditor<R>
/*     */     extends SpreadsheetCellEditor
/*     */   {
/*     */     private final List<String> itemList;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final ComboBox<String> cb;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String originalValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ListEditor(SpreadsheetView view, List<String> itemList) {
/* 767 */       super(view);
/* 768 */       this.itemList = itemList;
/* 769 */       this.cb = new ComboBox();
/* 770 */       this.cb.setVisibleRowCount(5);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startEdit(Object value, String format) {
/* 780 */       if (value instanceof String) {
/* 781 */         this.originalValue = value.toString();
/*     */       } else {
/* 783 */         this.originalValue = null;
/*     */       } 
/* 785 */       ObservableList<String> items = FXCollections.observableList(this.itemList);
/* 786 */       this.cb.setItems(items);
/* 787 */       this.cb.setValue(this.originalValue);
/*     */       
/* 789 */       attachEnterEscapeEventHandler();
/* 790 */       this.cb.show();
/* 791 */       this.cb.requestFocus();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void end() {
/* 797 */       this.cb.setOnKeyPressed(null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ComboBox<String> getEditor() {
/* 803 */       return this.cb;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getControlValue() {
/* 809 */       return (String)this.cb.getSelectionModel().getSelectedItem();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void attachEnterEscapeEventHandler() {
/* 818 */       this.cb.setOnKeyPressed(new EventHandler<KeyEvent>()
/*     */           {
/*     */             public void handle(KeyEvent t) {
/* 821 */               if (t.getCode() == KeyCode.ESCAPE) {
/* 822 */                 SpreadsheetCellEditor.ListEditor.this.cb.setValue(SpreadsheetCellEditor.ListEditor.this.originalValue);
/* 823 */                 SpreadsheetCellEditor.ListEditor.this.endEdit(false);
/* 824 */               } else if (t.getCode() == KeyCode.ENTER) {
/* 825 */                 SpreadsheetCellEditor.ListEditor.this.endEdit(true);
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DateEditor
/*     */     extends SpreadsheetCellEditor
/*     */   {
/*     */     private final DatePicker datePicker;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private EventHandler<KeyEvent> eh;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ChangeListener<LocalDate> cl;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean ending = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DateEditor(SpreadsheetView view, StringConverter<LocalDate> converter) {
/* 861 */       super(view);
/* 862 */       this.datePicker = new DatePicker();
/* 863 */       this.datePicker.setConverter(converter);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startEdit(Object value, String format) {
/* 872 */       if (value instanceof LocalDate) {
/* 873 */         this.datePicker.setValue(value);
/*     */       }
/* 875 */       attachEnterEscapeEventHandler();
/* 876 */       this.datePicker.show();
/* 877 */       this.datePicker.getEditor().requestFocus();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void end() {
/* 883 */       if (this.datePicker.isShowing()) {
/* 884 */         this.datePicker.hide();
/*     */       }
/* 886 */       this.datePicker.removeEventFilter(KeyEvent.KEY_PRESSED, this.eh);
/* 887 */       this.datePicker.valueProperty().removeListener(this.cl);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public DatePicker getEditor() {
/* 893 */       return this.datePicker;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getControlValue() {
/* 899 */       return this.datePicker.getEditor().getText();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void attachEnterEscapeEventHandler() {
/* 913 */       this.eh = new EventHandler<KeyEvent>()
/*     */         {
/*     */           public void handle(KeyEvent t) {
/* 916 */             if (t.getCode() == KeyCode.ENTER) {
/* 917 */               SpreadsheetCellEditor.DateEditor.this.ending = true;
/* 918 */               SpreadsheetCellEditor.DateEditor.this.endEdit(true);
/* 919 */               SpreadsheetCellEditor.DateEditor.this.ending = false;
/* 920 */             } else if (t.getCode() == KeyCode.ESCAPE) {
/* 921 */               SpreadsheetCellEditor.DateEditor.this.endEdit(false);
/*     */             } 
/*     */           }
/*     */         };
/*     */       
/* 926 */       this.datePicker.addEventFilter(KeyEvent.KEY_PRESSED, this.eh);
/*     */       
/* 928 */       this.cl = new ChangeListener<LocalDate>()
/*     */         {
/*     */           public void changed(ObservableValue<? extends LocalDate> arg0, LocalDate arg1, LocalDate arg2) {
/* 931 */             if (!SpreadsheetCellEditor.DateEditor.this.ending)
/* 932 */               SpreadsheetCellEditor.DateEditor.this.endEdit(true); 
/*     */           }
/*     */         };
/* 935 */       this.datePicker.valueProperty().addListener(this.cl);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\SpreadsheetCellEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */