/*     */ package org.seamless.swing;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanTableModel<T>
/*     */   extends AbstractTableModel
/*     */ {
/*     */   private Class<T> beanClass;
/*  33 */   private List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
/*     */   private List<T> rows;
/*     */   
/*     */   public BeanTableModel(Class<T> beanClass, Collection<T> rows) {
/*  37 */     this.beanClass = beanClass;
/*  38 */     this.rows = new ArrayList<T>(rows);
/*     */   }
/*     */   
/*     */   public String getColumnName(int column) {
/*  42 */     return ((PropertyDescriptor)this.properties.get(column)).getDisplayName();
/*     */   }
/*     */   
/*     */   public int getColumnCount() {
/*  46 */     return this.properties.size();
/*     */   }
/*     */   
/*     */   public int getRowCount() {
/*  50 */     return this.rows.size();
/*     */   }
/*     */   
/*     */   public Object getValueAt(int row, int column) {
/*  54 */     Object value = null;
/*  55 */     T entityInstance = this.rows.get(row);
/*  56 */     if (entityInstance != null) {
/*  57 */       PropertyDescriptor property = this.properties.get(column);
/*  58 */       Method readMethod = property.getReadMethod();
/*     */       try {
/*  60 */         value = readMethod.invoke(entityInstance, new Object[0]);
/*  61 */       } catch (Exception ex) {
/*  62 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/*  65 */     return value;
/*     */   }
/*     */   
/*     */   public void addColumn(String displayName, String propertyName) {
/*     */     try {
/*  70 */       PropertyDescriptor property = new PropertyDescriptor(propertyName, this.beanClass, propertyName, null);
/*     */       
/*  72 */       property.setDisplayName(displayName);
/*  73 */       this.properties.add(property);
/*  74 */     } catch (Exception ex) {
/*  75 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetColumns() {
/*  80 */     this.properties = new ArrayList<PropertyDescriptor>();
/*     */   }
/*     */   
/*     */   public List<T> getRows() {
/*  84 */     return this.rows;
/*     */   }
/*     */   
/*     */   public void setRows(Collection<T> rows) {
/*  88 */     this.rows = new ArrayList<T>(rows);
/*  89 */     fireTableDataChanged();
/*     */   }
/*     */   
/*     */   public void addRow(T value) {
/*  93 */     this.rows.add(value);
/*  94 */     fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
/*     */   }
/*     */   
/*     */   public void removeRow(int row) {
/*  98 */     if (this.rows.size() > row && row != -1) {
/*  99 */       this.rows.remove(row);
/* 100 */       fireTableRowsDeleted(row, row);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRow(int row, T entityInstance) {
/* 105 */     this.rows.remove(row);
/* 106 */     this.rows.add(row, entityInstance);
/* 107 */     fireTableDataChanged();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\BeanTableModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */