/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSortedSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Directory
/*     */   extends File
/*     */   implements Iterable<DirectoryEntry>
/*     */ {
/*     */   private DirectoryEntry entryInParent;
/*     */   private static final int INITIAL_CAPACITY = 16;
/*     */   private static final int INITIAL_RESIZE_THRESHOLD = 12;
/*     */   private DirectoryEntry[] table;
/*     */   private int resizeThreshold;
/*     */   private int entryCount;
/*     */   
/*     */   public static Directory create(int id) {
/*  41 */     return new Directory(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Directory createRoot(int id, Name name) {
/*  48 */     return new Directory(id, name);
/*     */   }
/*     */   private Directory(int id, Name rootName) { this(id); linked(new DirectoryEntry(this, rootName, this)); }
/*     */   Directory copyWithoutContent(int id) { return create(id); }
/*  52 */   public DirectoryEntry entryInParent() { return this.entryInParent; } public Directory parent() { return this.entryInParent.directory(); } private Directory(int id) { super(id);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     this.table = new DirectoryEntry[16];
/* 198 */     this.resizeThreshold = 12;
/*     */     put(new DirectoryEntry(this, Name.SELF, this)); }
/*     */   void linked(DirectoryEntry entry) { File parent = entry.directory();
/*     */     this.entryInParent = entry;
/*     */     forcePut(new DirectoryEntry(this, Name.PARENT, parent)); }
/*     */   void unlinked() { parent().decrementLinkCount(); }
/*     */   @VisibleForTesting
/*     */   int entryCount() { return this.entryCount; } private static int bucketIndex(Name name, int tableLength) {
/* 206 */     return name.hashCode() & tableLength - 1; } public boolean isEmpty() { return (entryCount() == 2); }
/*     */   @Nullable public DirectoryEntry get(Name name) { int index = bucketIndex(name, this.table.length); DirectoryEntry entry = this.table[index];
/*     */     while (entry != null) {
/*     */       if (name.equals(entry.name()))
/*     */         return entry; 
/*     */       entry = entry.next;
/*     */     } 
/*     */     return null; }
/*     */   public void link(Name name, File file) { DirectoryEntry entry = new DirectoryEntry(this, checkNotReserved(name, "link"), file);
/*     */     put(entry);
/*     */     file.linked(entry); }
/* 217 */   @VisibleForTesting void put(DirectoryEntry entry) { put(entry, false); } public void unlink(Name name) { DirectoryEntry entry = remove(checkNotReserved(name, "unlink")); entry.file().unlinked(); }
/*     */   public ImmutableSortedSet<Name> snapshot() { ImmutableSortedSet.Builder<Name> builder = new ImmutableSortedSet.Builder((Comparator)Name.displayOrdering()); for (DirectoryEntry entry : this) {
/*     */       if (!isReserved(entry.name()))
/*     */         builder.add(entry.name()); 
/*     */     }  return builder.build(); }
/*     */   private static Name checkNotReserved(Name name, String action) { if (isReserved(name))
/*     */       throw new IllegalArgumentException("cannot " + action + ": " + name);  return name; }
/*     */   private static boolean isReserved(Name name) { return (name == Name.SELF || name == Name.PARENT); }
/* 225 */   private void forcePut(DirectoryEntry entry) { put(entry, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void put(DirectoryEntry entry, boolean overwriteExisting) {
/* 233 */     int index = bucketIndex(entry.name(), this.table.length);
/*     */ 
/*     */ 
/*     */     
/* 237 */     DirectoryEntry prev = null;
/* 238 */     DirectoryEntry curr = this.table[index];
/* 239 */     while (curr != null) {
/* 240 */       if (curr.name().equals(entry.name())) {
/* 241 */         if (overwriteExisting) {
/*     */           
/* 243 */           if (prev != null) {
/* 244 */             prev.next = entry;
/*     */           } else {
/* 246 */             this.table[index] = entry;
/*     */           } 
/* 248 */           entry.next = curr.next;
/* 249 */           curr.next = null;
/* 250 */           entry.file().incrementLinkCount();
/*     */           return;
/*     */         } 
/* 253 */         throw new IllegalArgumentException("entry '" + entry.name() + "' already exists");
/*     */       } 
/*     */ 
/*     */       
/* 257 */       prev = curr;
/* 258 */       curr = curr.next;
/*     */     } 
/*     */     
/* 261 */     this.entryCount++;
/* 262 */     if (expandIfNeeded()) {
/*     */ 
/*     */       
/* 265 */       index = bucketIndex(entry.name(), this.table.length);
/* 266 */       addToBucket(index, this.table, entry);
/*     */     
/*     */     }
/* 269 */     else if (prev != null) {
/* 270 */       prev.next = entry;
/*     */     } else {
/* 272 */       this.table[index] = entry;
/*     */     } 
/*     */ 
/*     */     
/* 276 */     entry.file().incrementLinkCount();
/*     */   }
/*     */   
/*     */   private boolean expandIfNeeded() {
/* 280 */     if (this.entryCount <= this.resizeThreshold) {
/* 281 */       return false;
/*     */     }
/*     */     
/* 284 */     DirectoryEntry[] newTable = new DirectoryEntry[this.table.length << 1];
/*     */ 
/*     */     
/* 287 */     for (DirectoryEntry entry : this.table) {
/* 288 */       while (entry != null) {
/* 289 */         int index = bucketIndex(entry.name(), newTable.length);
/* 290 */         addToBucket(index, newTable, entry);
/* 291 */         DirectoryEntry next = entry.next;
/*     */         
/* 293 */         entry.next = null;
/* 294 */         entry = next;
/*     */       } 
/*     */     } 
/*     */     
/* 298 */     this.table = newTable;
/* 299 */     this.resizeThreshold <<= 1;
/* 300 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addToBucket(int bucketIndex, DirectoryEntry[] table, DirectoryEntry entryToAdd) {
/* 305 */     DirectoryEntry prev = null;
/* 306 */     DirectoryEntry existing = table[bucketIndex];
/* 307 */     while (existing != null) {
/* 308 */       prev = existing;
/* 309 */       existing = existing.next;
/*     */     } 
/*     */     
/* 312 */     if (prev != null) {
/* 313 */       prev.next = entryToAdd;
/*     */     } else {
/* 315 */       table[bucketIndex] = entryToAdd;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   DirectoryEntry remove(Name name) {
/* 326 */     int index = bucketIndex(name, this.table.length);
/*     */     
/* 328 */     DirectoryEntry prev = null;
/* 329 */     DirectoryEntry entry = this.table[index];
/* 330 */     while (entry != null) {
/* 331 */       if (name.equals(entry.name())) {
/* 332 */         if (prev != null) {
/* 333 */           prev.next = entry.next;
/*     */         } else {
/* 335 */           this.table[index] = entry.next;
/*     */         } 
/*     */         
/* 338 */         entry.next = null;
/* 339 */         this.entryCount--;
/* 340 */         entry.file().decrementLinkCount();
/* 341 */         return entry;
/*     */       } 
/*     */       
/* 344 */       prev = entry;
/* 345 */       entry = entry.next;
/*     */     } 
/*     */     
/* 348 */     throw new IllegalArgumentException("no entry matching '" + name + "' in this directory");
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<DirectoryEntry> iterator() {
/* 353 */     return (Iterator<DirectoryEntry>)new AbstractIterator<DirectoryEntry>() {
/*     */         int index;
/*     */         @Nullable
/*     */         DirectoryEntry entry;
/*     */         
/*     */         protected DirectoryEntry computeNext() {
/* 359 */           if (this.entry != null) {
/* 360 */             this.entry = this.entry.next;
/*     */           }
/*     */           
/* 363 */           while (this.entry == null && this.index < Directory.this.table.length) {
/* 364 */             this.entry = Directory.this.table[this.index++];
/*     */           }
/*     */           
/* 367 */           return (this.entry != null) ? this.entry : (DirectoryEntry)endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\Directory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */