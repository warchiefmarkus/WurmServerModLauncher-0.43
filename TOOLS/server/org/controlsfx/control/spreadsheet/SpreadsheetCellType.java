/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.time.LocalDate;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import javafx.util.StringConverter;
/*     */ import javafx.util.converter.DefaultStringConverter;
/*     */ import javafx.util.converter.DoubleStringConverter;
/*     */ import javafx.util.converter.IntegerStringConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SpreadsheetCellType<T>
/*     */ {
/*     */   protected StringConverter<T> converter;
/*     */   
/*     */   public SpreadsheetCellType() {}
/*     */   
/*     */   public SpreadsheetCellType(StringConverter<T> converter) {
/* 197 */     this.converter = converter;
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
/*     */   public abstract SpreadsheetCellEditor createEditor(SpreadsheetView paramSpreadsheetView);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(T object, String format) {
/* 219 */     return toString(object);
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
/*     */   public abstract String toString(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean match(Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isError(Object value) {
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptDrop() {
/* 263 */     return false;
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
/* 279 */   public static final SpreadsheetCellType<Object> OBJECT = new ObjectType();
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ObjectType
/*     */     extends SpreadsheetCellType<Object>
/*     */   {
/*     */     public ObjectType() {
/* 287 */       this(new StringConverterWithFormat()
/*     */           {
/*     */             public Object fromString(String arg0) {
/* 290 */               return arg0;
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString(Object arg0) {
/* 295 */               return (arg0 == null) ? "" : arg0.toString();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public ObjectType(StringConverterWithFormat<Object> converter) {
/* 301 */       super(converter);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 306 */       return "object";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(Object value) {
/* 311 */       return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SpreadsheetCell createCell(int row, int column, int rowSpan, int columnSpan, Object value) {
/* 332 */       SpreadsheetCell cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
/* 333 */       cell.setItem(value);
/* 334 */       return cell;
/*     */     }
/*     */ 
/*     */     
/*     */     public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
/* 339 */       return new SpreadsheetCellEditor.ObjectEditor(view);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object convertValue(Object value) {
/* 344 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString(Object item) {
/* 349 */       return this.converter.toString(item);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 357 */   public static final StringType STRING = new StringType();
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StringType
/*     */     extends SpreadsheetCellType<String>
/*     */   {
/*     */     public StringType() {
/* 365 */       this((StringConverter<String>)new DefaultStringConverter());
/*     */     }
/*     */     
/*     */     public StringType(StringConverter<String> converter) {
/* 369 */       super(converter);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 374 */       return "string";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(Object value) {
/* 379 */       return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SpreadsheetCell createCell(int row, int column, int rowSpan, int columnSpan, String value) {
/* 400 */       SpreadsheetCell cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
/* 401 */       cell.setItem(value);
/* 402 */       return cell;
/*     */     }
/*     */ 
/*     */     
/*     */     public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
/* 407 */       return new SpreadsheetCellEditor.StringEditor(view);
/*     */     }
/*     */ 
/*     */     
/*     */     public String convertValue(Object value) {
/* 412 */       String convertedValue = (String)this.converter.fromString((value == null) ? null : value.toString());
/* 413 */       if (convertedValue == null || convertedValue.equals("")) {
/* 414 */         return null;
/*     */       }
/* 416 */       return convertedValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString(String item) {
/* 421 */       return this.converter.toString(item);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 429 */   public static final DoubleType DOUBLE = new DoubleType();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DoubleType
/*     */     extends SpreadsheetCellType<Double>
/*     */   {
/*     */     public DoubleType() {
/* 438 */       this(new StringConverterWithFormat<Double>((StringConverter)new DoubleStringConverter())
/*     */           {
/*     */             public String toString(Double item) {
/* 441 */               return toStringFormat(item, "");
/*     */             }
/*     */ 
/*     */             
/*     */             public Double fromString(String str) {
/* 446 */               if (str == null || str.isEmpty() || "NaN".equals(str)) {
/* 447 */                 return Double.valueOf(Double.NaN);
/*     */               }
/* 449 */               return (Double)this.myConverter.fromString(str);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public String toStringFormat(Double item, String format) {
/*     */               try {
/* 456 */                 if (item == null || Double.isNaN(item.doubleValue())) {
/* 457 */                   return "";
/*     */                 }
/* 459 */                 return (new DecimalFormat(format)).format(item);
/*     */               }
/* 461 */               catch (Exception ex) {
/* 462 */                 return this.myConverter.toString(item);
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public DoubleType(StringConverter<Double> converter) {
/* 469 */       super(converter);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 474 */       return "double";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SpreadsheetCell createCell(int row, int column, int rowSpan, int columnSpan, Double value) {
/* 495 */       SpreadsheetCell cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
/* 496 */       cell.setItem(value);
/* 497 */       return cell;
/*     */     }
/*     */ 
/*     */     
/*     */     public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
/* 502 */       return new SpreadsheetCellEditor.DoubleEditor(view);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(Object value) {
/* 507 */       if (value instanceof Double) {
/* 508 */         return true;
/*     */       }
/*     */       try {
/* 511 */         this.converter.fromString((value == null) ? null : value.toString());
/* 512 */         return true;
/* 513 */       } catch (Exception e) {
/* 514 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Double convertValue(Object value) {
/* 521 */       if (value instanceof Double) {
/* 522 */         return (Double)value;
/*     */       }
/*     */       try {
/* 525 */         return (Double)this.converter.fromString((value == null) ? null : value.toString());
/* 526 */       } catch (Exception e) {
/* 527 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString(Double item) {
/* 534 */       return this.converter.toString(item);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString(Double item, String format) {
/* 539 */       return ((StringConverterWithFormat<Double>)this.converter).toStringFormat(item, format);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 546 */   public static final IntegerType INTEGER = new IntegerType();
/*     */   
/*     */   public abstract T convertValue(Object paramObject);
/*     */   
/*     */   public static class IntegerType
/*     */     extends SpreadsheetCellType<Integer>
/*     */   {
/*     */     public IntegerType() {
/* 554 */       this(new IntegerStringConverter()
/*     */           {
/*     */             public String toString(Integer item) {
/* 557 */               if (item == null || Double.isNaN(item.intValue())) {
/* 558 */                 return "";
/*     */               }
/* 560 */               return super.toString(item);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public Integer fromString(String str) {
/* 566 */               if (str == null || str.isEmpty() || "NaN".equals(str)) {
/* 567 */                 return null;
/*     */               }
/*     */ 
/*     */               
/*     */               try {
/* 572 */                 Double temp = Double.valueOf(Double.parseDouble(str));
/* 573 */                 return Integer.valueOf(temp.intValue());
/* 574 */               } catch (Exception e) {
/* 575 */                 return super.fromString(str);
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public IntegerType(IntegerStringConverter converter) {
/* 583 */       super((StringConverter<Integer>)converter);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 588 */       return "Integer";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SpreadsheetCell createCell(int row, int column, int rowSpan, int columnSpan, Integer value) {
/* 609 */       SpreadsheetCell cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
/* 610 */       cell.setItem(value);
/* 611 */       return cell;
/*     */     }
/*     */ 
/*     */     
/*     */     public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
/* 616 */       return new SpreadsheetCellEditor.IntegerEditor(view);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(Object value) {
/* 621 */       if (value instanceof Integer) {
/* 622 */         return true;
/*     */       }
/*     */       try {
/* 625 */         this.converter.fromString((value == null) ? null : value.toString());
/* 626 */         return true;
/* 627 */       } catch (Exception e) {
/* 628 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Integer convertValue(Object value) {
/* 635 */       if (value instanceof Integer) {
/* 636 */         return (Integer)value;
/*     */       }
/*     */       try {
/* 639 */         return (Integer)this.converter.fromString((value == null) ? null : value.toString());
/* 640 */       } catch (Exception e) {
/* 641 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString(Integer item) {
/* 648 */       return this.converter.toString(item);
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
/*     */   public static final ListType LIST(List<String> items) {
/* 660 */     return new ListType(items);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ListType
/*     */     extends SpreadsheetCellType<String>
/*     */   {
/*     */     protected final List<String> items;
/*     */     
/*     */     public ListType(List<String> items) {
/* 670 */       super((StringConverter<String>)new DefaultStringConverter(items)
/*     */           {
/*     */             public String fromString(String str) {
/* 673 */               if (str != null && items.contains(str)) {
/* 674 */                 return str;
/*     */               }
/* 676 */               return null;
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 681 */       this.items = items;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 686 */       return "list";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SpreadsheetCell createCell(int row, int column, int rowSpan, int columnSpan, String value) {
/* 707 */       SpreadsheetCell cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
/* 708 */       if (this.items != null && this.items.size() > 0) {
/* 709 */         if (value != null && this.items.contains(value)) {
/* 710 */           cell.setItem(value);
/*     */         } else {
/* 712 */           cell.setItem(this.items.get(0));
/*     */         } 
/*     */       }
/* 715 */       return cell;
/*     */     }
/*     */ 
/*     */     
/*     */     public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
/* 720 */       return new SpreadsheetCellEditor.ListEditor(view, this.items);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(Object value) {
/* 725 */       if (value instanceof String && this.items.contains(value.toString())) {
/* 726 */         return true;
/*     */       }
/* 728 */       return this.items.contains((value == null) ? null : value.toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public String convertValue(Object value) {
/* 733 */       return (String)this.converter.fromString((value == null) ? null : value.toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString(String item) {
/* 738 */       return this.converter.toString(item);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 745 */   public static final DateType DATE = new DateType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DateType
/*     */     extends SpreadsheetCellType<LocalDate>
/*     */   {
/*     */     public DateType() {
/* 756 */       this(new StringConverterWithFormat<LocalDate>()
/*     */           {
/*     */             public String toString(LocalDate item) {
/* 759 */               return toStringFormat(item, "");
/*     */             }
/*     */ 
/*     */             
/*     */             public LocalDate fromString(String str) {
/*     */               try {
/* 765 */                 return LocalDate.parse(str);
/* 766 */               } catch (Exception e) {
/* 767 */                 return null;
/*     */               } 
/*     */             }
/*     */ 
/*     */             
/*     */             public String toStringFormat(LocalDate item, String format) {
/* 773 */               if ("".equals(format) && item != null)
/* 774 */                 return item.toString(); 
/* 775 */               if (item != null) {
/* 776 */                 return item.format(DateTimeFormatter.ofPattern(format));
/*     */               }
/* 778 */               return "";
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public DateType(StringConverter<LocalDate> converter) {
/* 785 */       super(converter);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 790 */       return "date";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SpreadsheetCell createCell(int row, int column, int rowSpan, int columnSpan, LocalDate value) {
/* 811 */       SpreadsheetCell cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
/* 812 */       cell.setItem(value);
/* 813 */       return cell;
/*     */     }
/*     */ 
/*     */     
/*     */     public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
/* 818 */       return new SpreadsheetCellEditor.DateEditor(view, this.converter);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(Object value) {
/* 823 */       if (value instanceof LocalDate) {
/* 824 */         return true;
/*     */       }
/*     */       try {
/* 827 */         LocalDate temp = (LocalDate)this.converter.fromString((value == null) ? null : value.toString());
/* 828 */         return (temp != null);
/* 829 */       } catch (Exception e) {
/* 830 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public LocalDate convertValue(Object value) {
/* 837 */       if (value instanceof LocalDate) {
/* 838 */         return (LocalDate)value;
/*     */       }
/*     */       try {
/* 841 */         return (LocalDate)this.converter.fromString((value == null) ? null : value.toString());
/* 842 */       } catch (Exception e) {
/* 843 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString(LocalDate item) {
/* 850 */       return this.converter.toString(item);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString(LocalDate item, String format) {
/* 855 */       return ((StringConverterWithFormat<LocalDate>)this.converter).toStringFormat(item, format);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\SpreadsheetCellType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */