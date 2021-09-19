/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSortedMap;
/*     */ import com.google.common.collect.ImmutableSortedSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FileTree
/*     */ {
/*     */   private static final int MAX_SYMBOLIC_LINK_DEPTH = 40;
/*  49 */   private static final ImmutableList<Name> EMPTY_PATH_NAMES = ImmutableList.of(Name.SELF);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableSortedMap<Name, Directory> roots;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FileTree(Map<Name, Directory> roots) {
/*  60 */     this.roots = ImmutableSortedMap.copyOf(roots, (Comparator)Name.canonicalOrdering());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<Name> getRootDirectoryNames() {
/*  67 */     return this.roots.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DirectoryEntry getRoot(Name name) {
/*  76 */     Directory dir = (Directory)this.roots.get(name);
/*  77 */     return (dir == null) ? null : dir.entryInParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryEntry lookUp(File workingDirectory, JimfsPath path, Set<? super LinkOption> options) throws IOException {
/*  85 */     Preconditions.checkNotNull(path);
/*  86 */     Preconditions.checkNotNull(options);
/*     */     
/*  88 */     DirectoryEntry result = lookUp(workingDirectory, path, options, 0);
/*  89 */     if (result == null)
/*     */     {
/*  91 */       throw new NoSuchFileException(path.toString());
/*     */     }
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private DirectoryEntry lookUp(File dir, JimfsPath path, Set<? super LinkOption> options, int linkDepth) throws IOException {
/*  99 */     ImmutableList<Name> names = path.names();
/*     */     
/* 101 */     if (path.isAbsolute()) {
/*     */       
/* 103 */       DirectoryEntry entry = getRoot(path.root());
/* 104 */       if (entry == null)
/*     */       {
/*     */         
/* 107 */         return null; } 
/* 108 */       if (names.isEmpty())
/*     */       {
/* 110 */         return entry;
/*     */       }
/*     */       
/* 113 */       dir = entry.file();
/*     */     }
/* 115 */     else if (isEmpty(names)) {
/*     */       
/* 117 */       names = EMPTY_PATH_NAMES;
/*     */     } 
/*     */     
/* 120 */     return lookUp(dir, (Iterable<Name>)names, options, linkDepth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private DirectoryEntry lookUp(File dir, Iterable<Name> names, Set<? super LinkOption> options, int linkDepth) throws IOException {
/* 131 */     Iterator<Name> nameIterator = names.iterator();
/* 132 */     Name name = nameIterator.next();
/* 133 */     while (nameIterator.hasNext()) {
/* 134 */       Directory directory = toDirectory(dir);
/* 135 */       if (directory == null) {
/* 136 */         return null;
/*     */       }
/*     */       
/* 139 */       DirectoryEntry entry = directory.get(name);
/* 140 */       if (entry == null) {
/* 141 */         return null;
/*     */       }
/*     */       
/* 144 */       File file = entry.file();
/* 145 */       if (file.isSymbolicLink()) {
/* 146 */         DirectoryEntry linkResult = followSymbolicLink(dir, (SymbolicLink)file, linkDepth);
/*     */         
/* 148 */         if (linkResult == null) {
/* 149 */           return null;
/*     */         }
/*     */         
/* 152 */         dir = linkResult.fileOrNull();
/*     */       } else {
/* 154 */         dir = file;
/*     */       } 
/*     */       
/* 157 */       name = nameIterator.next();
/*     */     } 
/*     */     
/* 160 */     return lookUpLast(dir, name, options, linkDepth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private DirectoryEntry lookUpLast(@Nullable File dir, Name name, Set<? super LinkOption> options, int linkDepth) throws IOException {
/* 170 */     Directory directory = toDirectory(dir);
/* 171 */     if (directory == null) {
/* 172 */       return null;
/*     */     }
/*     */     
/* 175 */     DirectoryEntry entry = directory.get(name);
/* 176 */     if (entry == null) {
/* 177 */       return new DirectoryEntry(directory, name, null);
/*     */     }
/*     */     
/* 180 */     File file = entry.file();
/* 181 */     if (!options.contains(LinkOption.NOFOLLOW_LINKS) && file.isSymbolicLink()) {
/* 182 */       return followSymbolicLink(dir, (SymbolicLink)file, linkDepth);
/*     */     }
/*     */     
/* 185 */     return getRealEntry(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private DirectoryEntry followSymbolicLink(File dir, SymbolicLink link, int linkDepth) throws IOException {
/* 195 */     if (linkDepth >= 40) {
/* 196 */       throw new IOException("too many levels of symbolic links");
/*     */     }
/*     */     
/* 199 */     return lookUp(dir, link.target(), (Set<? super LinkOption>)Options.FOLLOW_LINKS, linkDepth + 1);
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
/*     */   @Nullable
/*     */   private DirectoryEntry getRealEntry(DirectoryEntry entry) {
/* 213 */     Name name = entry.name();
/*     */     
/* 215 */     if (name.equals(Name.SELF) || name.equals(Name.PARENT)) {
/* 216 */       Directory dir = toDirectory(entry.file());
/* 217 */       assert dir != null;
/* 218 */       return dir.entryInParent();
/*     */     } 
/* 220 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Directory toDirectory(@Nullable File file) {
/* 226 */     return (file == null || !file.isDirectory()) ? null : (Directory)file;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isEmpty(ImmutableList<Name> names) {
/* 231 */     return (names.isEmpty() || (names.size() == 1 && ((Name)names.get(0)).toString().isEmpty()));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\FileTree.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */