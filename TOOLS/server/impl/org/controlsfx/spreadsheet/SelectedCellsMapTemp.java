/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.transformation.SortedList;
/*     */ import javafx.scene.control.TablePositionBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectedCellsMapTemp<T extends TablePositionBase>
/*     */ {
/*     */   private final ObservableList<T> selectedCells;
/*     */   private final ObservableList<T> sortedSelectedCells;
/*     */   private final Map<Integer, BitSet> selectedCellBitSetMap;
/*     */   
/*     */   public SelectedCellsMapTemp(ListChangeListener<T> listener) {
/*  54 */     this.selectedCells = FXCollections.observableArrayList();
/*  55 */     this.sortedSelectedCells = (ObservableList<T>)new SortedList(this.selectedCells, (o1, o2) -> {
/*     */           int result = o1.getRow() - o2.getRow();
/*     */           return (result == 0) ? (o1.getColumn() - o2.getColumn()) : result;
/*     */         });
/*  59 */     this.sortedSelectedCells.addListener(listener);
/*     */     
/*  61 */     this.selectedCellBitSetMap = new TreeMap<>((o1, o2) -> o1.compareTo(o2));
/*     */   }
/*     */   
/*     */   public int size() {
/*  65 */     return this.selectedCells.size();
/*     */   }
/*     */   
/*     */   public T get(int i) {
/*  69 */     if (i < 0) {
/*  70 */       return null;
/*     */     }
/*  72 */     return (T)this.sortedSelectedCells.get(i);
/*     */   }
/*     */   public void add(T tp) {
/*     */     BitSet bitset;
/*  76 */     int row = tp.getRow();
/*  77 */     int columnIndex = tp.getColumn();
/*     */ 
/*     */ 
/*     */     
/*  81 */     if (!this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
/*  82 */       bitset = new BitSet();
/*  83 */       this.selectedCellBitSetMap.put(Integer.valueOf(row), bitset);
/*     */     } else {
/*  85 */       bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
/*     */     } 
/*     */     
/*  88 */     if (columnIndex >= 0) {
/*  89 */       boolean isAlreadySet = bitset.get(columnIndex);
/*  90 */       bitset.set(columnIndex);
/*     */       
/*  92 */       if (!isAlreadySet)
/*     */       {
/*  94 */         this.selectedCells.add(tp);
/*     */       
/*     */       }
/*     */     }
/*  98 */     else if (!this.selectedCells.contains(tp)) {
/*  99 */       this.selectedCells.add(tp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Collection<T> cells) {
/* 106 */     for (TablePositionBase tablePositionBase : cells) {
/* 107 */       BitSet bitset; int row = tablePositionBase.getRow();
/* 108 */       int columnIndex = tablePositionBase.getColumn();
/*     */ 
/*     */ 
/*     */       
/* 112 */       if (!this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
/* 113 */         bitset = new BitSet();
/* 114 */         this.selectedCellBitSetMap.put(Integer.valueOf(row), bitset);
/*     */       } else {
/* 116 */         bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
/*     */       } 
/*     */       
/* 119 */       if (columnIndex < 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 123 */       bitset.set(columnIndex);
/*     */     } 
/*     */ 
/*     */     
/* 127 */     this.selectedCells.addAll(cells);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Collection<T> cells) {
/* 132 */     this.selectedCellBitSetMap.clear();
/* 133 */     for (TablePositionBase tablePositionBase : cells) {
/* 134 */       BitSet bitset; int row = tablePositionBase.getRow();
/* 135 */       int columnIndex = tablePositionBase.getColumn();
/*     */ 
/*     */ 
/*     */       
/* 139 */       if (!this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
/* 140 */         bitset = new BitSet();
/* 141 */         this.selectedCellBitSetMap.put(Integer.valueOf(row), bitset);
/*     */       } else {
/* 143 */         bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
/*     */       } 
/*     */       
/* 146 */       if (columnIndex < 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 150 */       bitset.set(columnIndex);
/*     */     } 
/*     */ 
/*     */     
/* 154 */     this.selectedCells.setAll(cells);
/*     */   }
/*     */   
/*     */   public void remove(T tp) {
/* 158 */     int row = tp.getRow();
/* 159 */     int columnIndex = tp.getColumn();
/*     */ 
/*     */     
/* 162 */     if (this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
/* 163 */       BitSet bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
/*     */       
/* 165 */       if (columnIndex >= 0) {
/* 166 */         bitset.clear(columnIndex);
/*     */       }
/*     */       
/* 169 */       if (bitset.isEmpty()) {
/* 170 */         this.selectedCellBitSetMap.remove(Integer.valueOf(row));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 175 */     this.selectedCells.remove(tp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 180 */     this.selectedCellBitSetMap.clear();
/*     */ 
/*     */     
/* 183 */     this.selectedCells.clear();
/*     */   }
/*     */   
/*     */   public boolean isSelected(int row, int columnIndex) {
/* 187 */     if (columnIndex < 0) {
/* 188 */       return this.selectedCellBitSetMap.containsKey(Integer.valueOf(row));
/*     */     }
/* 190 */     return this.selectedCellBitSetMap.containsKey(Integer.valueOf(row)) ? ((BitSet)this.selectedCellBitSetMap.get(Integer.valueOf(row))).get(columnIndex) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(T tp) {
/* 195 */     return this.sortedSelectedCells.indexOf(tp);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 199 */     return this.selectedCells.isEmpty();
/*     */   }
/*     */   
/*     */   public ObservableList<T> getSelectedCells() {
/* 203 */     return this.selectedCells;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\SelectedCellsMapTemp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */