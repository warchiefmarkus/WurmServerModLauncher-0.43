/*     */ package com.sun.javaws.ui;
/*     */ 
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.cache.DiskCacheEntry;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import javax.swing.table.TableModel;
/*     */ 
/*     */ 
/*     */ class CacheTable
/*     */   extends JTable
/*     */ {
/*  30 */   private static final TableCellRenderer _defaultRenderer = new DefaultTableCellRenderer();
/*     */ 
/*     */   
/*     */   private static final int MIN_ROW_HEIGHT = 36;
/*     */   
/*     */   private boolean _system;
/*     */   
/*  37 */   private int _filter = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheTable(CacheViewer paramCacheViewer, boolean paramBoolean) {
/*  42 */     this._system = paramBoolean;
/*  43 */     setShowGrid(false);
/*  44 */     setIntercellSpacing(new Dimension(0, 0));
/*  45 */     setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
/*  46 */     int i = getRowHeight();
/*  47 */     if (i < 36) {
/*  48 */       setRowHeight(36);
/*     */     }
/*     */     
/*  51 */     setPreferredScrollableViewportSize(new Dimension(640, 280));
/*  52 */     addMouseListener(new MouseAdapter(this, paramCacheViewer) { private final CacheViewer val$cv; private final CacheTable this$0;
/*     */           public void mousePressed(MouseEvent param1MouseEvent) {
/*  54 */             if (param1MouseEvent.isPopupTrigger()) {
/*  55 */               int i = param1MouseEvent.getY();
/*  56 */               int j = i / this.this$0.getRowHeight();
/*     */               
/*  58 */               this.this$0.getSelectionModel().clearSelection();
/*  59 */               this.this$0.getSelectionModel().addSelectionInterval(j, j);
/*     */ 
/*     */               
/*  62 */               this.val$cv.popupApplicationMenu(this.this$0, param1MouseEvent.getX(), i);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void mouseReleased(MouseEvent param1MouseEvent) {
/*  67 */             if (param1MouseEvent.isPopupTrigger()) {
/*  68 */               int i = param1MouseEvent.getY();
/*  69 */               int j = i / this.this$0.getRowHeight();
/*     */               
/*  71 */               this.this$0.getSelectionModel().clearSelection();
/*  72 */               this.this$0.getSelectionModel().addSelectionInterval(j, j);
/*     */ 
/*     */               
/*  75 */               this.val$cv.popupApplicationMenu(this.this$0, param1MouseEvent.getX(), i);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void mouseClicked(MouseEvent param1MouseEvent) {
/*  80 */             Point point = param1MouseEvent.getPoint();
/*  81 */             if (param1MouseEvent.getClickCount() == 2 && 
/*  82 */               param1MouseEvent.getButton() == 1) {
/*     */               
/*  84 */               int i = this.this$0.getColumnModel().getColumnIndexAtX(point.x);
/*     */               
/*  86 */               if (i < 3) {
/*  87 */                 this.val$cv.launchApplication();
/*     */               }
/*     */             } 
/*     */           } }
/*     */       );
/*     */ 
/*     */     
/*  94 */     reset();
/*     */   }
/*     */   
/*     */   public void setFilter(int paramInt) {
/*  98 */     if (paramInt != this._filter) {
/*  99 */       this._filter = paramInt;
/* 100 */       reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reset() {
/* 105 */     TableModel tableModel = getModel();
/* 106 */     if (tableModel instanceof CacheTableModel) {
/* 107 */       ((CacheTableModel)tableModel).removeMouseListenerFromHeaderInTable(this);
/*     */     }
/* 109 */     CacheTableModel cacheTableModel = new CacheTableModel(this, this._system, this._filter);
/* 110 */     setModel(cacheTableModel);
/* 111 */     for (byte b = 0; b < getModel().getColumnCount(); b++) {
/* 112 */       TableColumn tableColumn = getColumnModel().getColumn(b);
/* 113 */       tableColumn.setHeaderRenderer(new CacheTableHeaderRenderer());
/* 114 */       int i = cacheTableModel.getPreferredWidth(b);
/* 115 */       tableColumn.setPreferredWidth(i);
/* 116 */       tableColumn.setMinWidth(i);
/*     */     } 
/*     */     
/* 119 */     setDefaultRenderer(JLabel.class, cacheTableModel);
/* 120 */     cacheTableModel.addMouseListenerToHeaderInTable(this);
/*     */   }
/*     */   
/*     */   public CacheObject getCacheObject(int paramInt) {
/* 124 */     return ((CacheTableModel)getModel()).getCacheObject(paramInt);
/*     */   }
/*     */   
/*     */   public String[] getAllHrefs() {
/* 128 */     ArrayList arrayList = new ArrayList();
/* 129 */     TableModel tableModel = getModel();
/* 130 */     if (tableModel instanceof CacheTableModel) {
/* 131 */       for (byte b = 0; b < tableModel.getRowCount(); b++) {
/* 132 */         String str = ((CacheTableModel)tableModel).getRowHref(b);
/* 133 */         if (str != null) {
/* 134 */           arrayList.add(str);
/*     */         }
/*     */       } 
/*     */     }
/* 138 */     return arrayList.<String>toArray(new String[0]);
/*     */   }
/*     */   private class CacheTableHeaderRenderer extends DefaultTableCellRenderer { private CacheTableHeaderRenderer(CacheTable this$0) {
/* 141 */       CacheTable.this = CacheTable.this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final CacheTable this$0;
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable param1JTable, Object param1Object, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2) {
/* 150 */       if (param1JTable != null) {
/* 151 */         JTableHeader jTableHeader = param1JTable.getTableHeader();
/* 152 */         if (jTableHeader != null) {
/* 153 */           setForeground(jTableHeader.getForeground());
/* 154 */           setBackground(jTableHeader.getBackground());
/* 155 */           setFont(jTableHeader.getFont());
/*     */         } 
/*     */       } 
/*     */       
/* 159 */       setText((param1Object == null) ? "" : param1Object.toString());
/* 160 */       setBorder(UIManager.getBorder("TableHeader.cellBorder"));
/* 161 */       setHorizontalAlignment(0);
/* 162 */       String str = CacheObject.getHeaderToolTipText(param1Int2);
/* 163 */       if (str != null && str.length() > 0) {
/* 164 */         setToolTipText(str);
/*     */       }
/* 166 */       return this;
/*     */     } }
/*     */   
/*     */   private class CacheTableModel extends AbstractTableModel implements TableCellRenderer {
/*     */     private boolean _system;
/*     */     private CacheObject[] _rows;
/*     */     private int _filter;
/*     */     private int _sortColumn;
/*     */     private boolean _sortAscending;
/*     */     private MouseListener _mouseListener;
/*     */     private final CacheTable this$0;
/*     */     
/*     */     public CacheTableModel(CacheTable this$0, boolean param1Boolean, int param1Int) {
/* 179 */       this.this$0 = this$0; this._mouseListener = null;
/* 180 */       this._system = param1Boolean;
/* 181 */       this._filter = param1Int;
/* 182 */       this._rows = new CacheObject[0];
/* 183 */       this._sortColumn = -1;
/* 184 */       this._sortAscending = true;
/* 185 */       refresh();
/* 186 */       fireTableDataChanged();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable param1JTable, Object param1Object, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2) {
/* 193 */       if (param1Object instanceof Component) {
/* 194 */         Component component = (Component)param1Object;
/* 195 */         if (param1Boolean1) {
/* 196 */           component.setForeground(param1JTable.getSelectionForeground());
/* 197 */           component.setBackground(param1JTable.getSelectionBackground());
/*     */         } else {
/* 199 */           component.setForeground(param1JTable.getForeground());
/* 200 */           component.setBackground(param1JTable.getBackground());
/*     */         } 
/* 202 */         CacheObject.hasFocus(component, param1Boolean2);
/* 203 */         return component;
/*     */       } 
/* 205 */       return CacheTable._defaultRenderer.getTableCellRendererComponent(param1JTable, param1Object, param1Boolean1, param1Boolean2, param1Int1, param1Int2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void refresh() {
/* 210 */       ArrayList arrayList = new ArrayList();
/* 211 */       Iterator iterator = Cache.getJnlpCacheEntries(this._system);
/* 212 */       while (iterator.hasNext()) {
/* 213 */         CacheObject cacheObject = new CacheObject(iterator.next(), this);
/*     */         
/* 215 */         if (cacheObject.inFilter(this._filter) && cacheObject.getLaunchDesc() != null) {
/* 216 */           arrayList.add(cacheObject);
/*     */         }
/*     */       } 
/* 219 */       this._rows = arrayList.<CacheObject>toArray(new CacheObject[0]);
/* 220 */       if (this._sortColumn != -1) {
/* 221 */         sort();
/*     */       }
/*     */     }
/*     */     
/*     */     CacheObject getCacheObject(int param1Int) {
/* 226 */       return this._rows[param1Int];
/*     */     }
/*     */     
/*     */     public Object getValueAt(int param1Int1, int param1Int2) {
/* 230 */       return this._rows[param1Int1].getObject(param1Int2);
/*     */     }
/*     */     
/*     */     public int getRowCount() {
/* 234 */       return this._rows.length;
/*     */     }
/*     */     
/*     */     public String getRowHref(int param1Int) {
/* 238 */       return this._rows[param1Int].getHref();
/*     */     }
/*     */     
/*     */     public int getColumnCount() {
/* 242 */       return CacheObject.getColumnCount();
/*     */     }
/*     */     
/*     */     public boolean isCellEditable(int param1Int1, int param1Int2) {
/* 246 */       return this._rows[param1Int1].isEditable(param1Int2);
/*     */     }
/*     */     
/*     */     public Class getColumnClass(int param1Int) {
/* 250 */       return CacheObject.getClass(param1Int);
/*     */     }
/*     */     
/*     */     public String getColumnName(int param1Int) {
/* 254 */       return CacheObject.getColumnName(param1Int);
/*     */     }
/*     */     
/*     */     public void setValueAt(Object param1Object, int param1Int1, int param1Int2) {
/* 258 */       this._rows[param1Int1].setValue(param1Int2, param1Object);
/*     */     }
/*     */     
/*     */     public int getPreferredWidth(int param1Int) {
/* 262 */       return CacheObject.getPreferredWidth(param1Int);
/*     */     }
/*     */     
/*     */     public void removeMouseListenerFromHeaderInTable(JTable param1JTable) {
/* 266 */       if (this._mouseListener != null) {
/* 267 */         param1JTable.getTableHeader().removeMouseListener(this._mouseListener);
/*     */       }
/*     */     }
/*     */     
/*     */     public void addMouseListenerToHeaderInTable(JTable param1JTable) {
/* 272 */       JTable jTable = param1JTable;
/* 273 */       jTable.setColumnSelectionAllowed(false);
/* 274 */       ListSelectionModel listSelectionModel = jTable.getSelectionModel();
/* 275 */       this._mouseListener = new MouseAdapter(this, jTable, listSelectionModel) { private final JTable val$tableView;
/*     */           public void mouseClicked(MouseEvent param2MouseEvent) {
/* 277 */             TableColumnModel tableColumnModel = this.val$tableView.getColumnModel();
/* 278 */             int i = tableColumnModel.getColumnIndexAtX(param2MouseEvent.getX());
/* 279 */             int j = this.val$lsm.getMinSelectionIndex();
/* 280 */             this.val$lsm.clearSelection();
/* 281 */             int k = this.val$tableView.convertColumnIndexToModel(i);
/* 282 */             if (param2MouseEvent.getClickCount() == 1 && k >= 0) {
/* 283 */               int m = param2MouseEvent.getModifiers() & 0x1;
/*     */               
/* 285 */               this.this$1._sortAscending = (m == 0);
/* 286 */               this.this$1._sortColumn = k;
/* 287 */               this.this$1.runSort(this.val$lsm, j);
/*     */             } 
/*     */           } private final ListSelectionModel val$lsm; private final CacheTable.CacheTableModel this$1; }
/*     */         ;
/* 291 */       jTable.getTableHeader().addMouseListener(this._mouseListener);
/*     */     }
/*     */ 
/*     */     
/*     */     public void sort() {
/* 296 */       boolean bool = false;
/*     */       
/* 298 */       if (this._sortAscending) {
/* 299 */         for (byte b = 0; b < getRowCount(); b++) {
/* 300 */           for (int i = b + 1; i < getRowCount(); i++) {
/* 301 */             if (this._rows[b].compareColumns(this._rows[i], this._sortColumn) > 0) {
/*     */               
/* 303 */               bool = true;
/* 304 */               CacheObject cacheObject = this._rows[b];
/* 305 */               this._rows[b] = this._rows[i];
/* 306 */               this._rows[i] = cacheObject;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 311 */         for (byte b = 0; b < getRowCount(); b++) {
/* 312 */           for (int i = b + 1; i < getRowCount(); i++) {
/* 313 */             if (this._rows[i].compareColumns(this._rows[b], this._sortColumn) > 0) {
/*     */               
/* 315 */               bool = true;
/* 316 */               CacheObject cacheObject = this._rows[b];
/* 317 */               this._rows[b] = this._rows[i];
/* 318 */               this._rows[i] = cacheObject;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 323 */       if (bool) {
/* 324 */         fireTableDataChanged();
/*     */       }
/*     */     }
/*     */     
/*     */     private void runSort(ListSelectionModel param1ListSelectionModel, int param1Int) {
/* 329 */       if (CacheViewer.getStatus() != 4)
/* 330 */         (new Thread(new Runnable(this, param1Int, param1ListSelectionModel) { private final int val$selected; private final ListSelectionModel val$lsm; private final CacheTable.CacheTableModel this$1;
/*     */               public void run() {
/* 332 */                 CacheViewer.setStatus(4);
/*     */                 try {
/* 334 */                   CacheObject cacheObject = null;
/* 335 */                   if (this.val$selected >= 0) {
/* 336 */                     cacheObject = this.this$1._rows[this.val$selected];
/*     */                   }
/* 338 */                   this.this$1.sort();
/* 339 */                   if (cacheObject != null) {
/* 340 */                     for (byte b = 0; b < this.this$1._rows.length; b++) {
/* 341 */                       if (this.this$1._rows[b] == cacheObject) {
/* 342 */                         this.val$lsm.addSelectionInterval(b, b);
/*     */                         break;
/*     */                       } 
/*     */                     } 
/*     */                   }
/*     */                 } finally {
/* 348 */                   CacheViewer.setStatus(0);
/*     */                 } 
/*     */               } }
/*     */           )).start(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\ui\CacheTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */