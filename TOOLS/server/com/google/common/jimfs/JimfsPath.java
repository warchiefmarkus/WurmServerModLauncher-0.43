/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ final class JimfsPath
/*     */   implements Path
/*     */ {
/*     */   @Nullable
/*     */   private final Name root;
/*     */   private final ImmutableList<Name> names;
/*     */   private final PathService pathService;
/*     */   
/*     */   public JimfsPath(PathService pathService, @Nullable Name root, Iterable<Name> names) {
/*  61 */     this.pathService = (PathService)Preconditions.checkNotNull(pathService);
/*  62 */     this.root = root;
/*  63 */     this.names = ImmutableList.copyOf(names);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Name root() {
/*  71 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<Name> names() {
/*  78 */     return this.names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Name name() {
/*  87 */     if (!this.names.isEmpty()) {
/*  88 */       return (Name)Iterables.getLast((Iterable)this.names);
/*     */     }
/*  90 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmptyPath() {
/*  97 */     return (this.root == null && this.names.size() == 1 && ((Name)this.names.get(0)).toString().isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystem getFileSystem() {
/* 104 */     return this.pathService.getFileSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsFileSystem getJimfsFileSystem() {
/* 114 */     return (JimfsFileSystem)this.pathService.getFileSystem();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbsolute() {
/* 119 */     return (this.root != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath getRoot() {
/* 124 */     if (this.root == null) {
/* 125 */       return null;
/*     */     }
/* 127 */     return this.pathService.createRoot(this.root);
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath getFileName() {
/* 132 */     return this.names.isEmpty() ? null : getName(this.names.size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath getParent() {
/* 137 */     if (this.names.isEmpty() || (this.names.size() == 1 && this.root == null)) {
/* 138 */       return null;
/*     */     }
/*     */     
/* 141 */     return this.pathService.createPath(this.root, (Iterable<Name>)this.names.subList(0, this.names.size() - 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNameCount() {
/* 146 */     return this.names.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath getName(int index) {
/* 151 */     Preconditions.checkArgument((index >= 0 && index < this.names.size()), "index (%s) must be >= 0 and < name count (%s)", new Object[] { Integer.valueOf(index), Integer.valueOf(this.names.size()) });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     return this.pathService.createFileName((Name)this.names.get(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath subpath(int beginIndex, int endIndex) {
/* 161 */     Preconditions.checkArgument((beginIndex >= 0 && endIndex <= this.names.size() && endIndex > beginIndex), "beginIndex (%s) must be >= 0; endIndex (%s) must be <= name count (%s) and > beginIndex", new Object[] { Integer.valueOf(beginIndex), Integer.valueOf(endIndex), Integer.valueOf(this.names.size()) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     return this.pathService.createRelativePath((Iterable<Name>)this.names.subList(beginIndex, endIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean startsWith(List<?> list, List<?> other) {
/* 174 */     return (list.size() >= other.size() && list.subList(0, other.size()).equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean startsWith(Path other) {
/* 179 */     JimfsPath otherPath = checkPath(other);
/* 180 */     return (otherPath != null && getFileSystem().equals(otherPath.getFileSystem()) && Objects.equals(this.root, otherPath.root) && startsWith((List<?>)this.names, (List<?>)otherPath.names));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean startsWith(String other) {
/* 188 */     return startsWith(this.pathService.parsePath(other, new String[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean endsWith(Path other) {
/* 193 */     JimfsPath otherPath = checkPath(other);
/* 194 */     if (otherPath == null) {
/* 195 */       return false;
/*     */     }
/*     */     
/* 198 */     if (otherPath.isAbsolute()) {
/* 199 */       return (compareTo(otherPath) == 0);
/*     */     }
/* 201 */     return startsWith((List<?>)this.names.reverse(), (List<?>)otherPath.names.reverse());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean endsWith(String other) {
/* 206 */     return endsWith(this.pathService.parsePath(other, new String[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath normalize() {
/* 211 */     if (isNormal()) {
/* 212 */       return this;
/*     */     }
/*     */     
/* 215 */     Deque<Name> newNames = new ArrayDeque<>();
/* 216 */     for (Name name : this.names) {
/* 217 */       if (name.equals(Name.PARENT)) {
/* 218 */         Name lastName = newNames.peekLast();
/* 219 */         if (lastName != null && !lastName.equals(Name.PARENT)) {
/* 220 */           newNames.removeLast(); continue;
/* 221 */         }  if (!isAbsolute())
/*     */         {
/* 223 */           newNames.add(name); }  continue;
/*     */       } 
/* 225 */       if (!name.equals(Name.SELF)) {
/* 226 */         newNames.add(name);
/*     */       }
/*     */     } 
/*     */     
/* 230 */     return newNames.equals(this.names) ? this : this.pathService.createPath(this.root, newNames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNormal() {
/* 238 */     if (getNameCount() == 0 || (getNameCount() == 1 && !isAbsolute())) {
/* 239 */       return true;
/*     */     }
/*     */     
/* 242 */     boolean foundNonParentName = isAbsolute();
/* 243 */     boolean normal = true;
/* 244 */     for (Name name : this.names) {
/* 245 */       if (name.equals(Name.PARENT)) {
/* 246 */         if (foundNonParentName) {
/* 247 */           normal = false; break;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 251 */       if (name.equals(Name.SELF)) {
/* 252 */         normal = false;
/*     */         
/*     */         break;
/*     */       } 
/* 256 */       foundNonParentName = true;
/*     */     } 
/*     */     
/* 259 */     return normal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JimfsPath resolve(Name name) {
/* 266 */     if (name.toString().isEmpty()) {
/* 267 */       return this;
/*     */     }
/* 269 */     return this.pathService.createPathInternal(this.root, (Iterable<Name>)ImmutableList.builder().addAll((Iterable)this.names).add(name).build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath resolve(Path other) {
/* 279 */     JimfsPath otherPath = checkPath(other);
/* 280 */     if (otherPath == null) {
/* 281 */       throw new ProviderMismatchException(other.toString());
/*     */     }
/*     */     
/* 284 */     if (isEmptyPath() || otherPath.isAbsolute()) {
/* 285 */       return otherPath;
/*     */     }
/* 287 */     if (otherPath.isEmptyPath()) {
/* 288 */       return this;
/*     */     }
/* 290 */     return this.pathService.createPath(this.root, (Iterable<Name>)ImmutableList.builder().addAll((Iterable)this.names).addAll((Iterable)otherPath.names).build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath resolve(String other) {
/* 300 */     return resolve(this.pathService.parsePath(other, new String[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath resolveSibling(Path other) {
/* 305 */     JimfsPath otherPath = checkPath(other);
/* 306 */     if (otherPath == null) {
/* 307 */       throw new ProviderMismatchException(other.toString());
/*     */     }
/*     */     
/* 310 */     if (otherPath.isAbsolute()) {
/* 311 */       return otherPath;
/*     */     }
/* 313 */     JimfsPath parent = getParent();
/* 314 */     if (parent == null) {
/* 315 */       return otherPath;
/*     */     }
/* 317 */     return parent.resolve(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath resolveSibling(String other) {
/* 322 */     return resolveSibling(this.pathService.parsePath(other, new String[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath relativize(Path other) {
/* 327 */     JimfsPath otherPath = checkPath(other);
/* 328 */     if (otherPath == null) {
/* 329 */       throw new ProviderMismatchException(other.toString());
/*     */     }
/*     */     
/* 332 */     Preconditions.checkArgument(Objects.equals(this.root, otherPath.root), "Paths have different roots: %s, %s", new Object[] { this, other });
/*     */ 
/*     */     
/* 335 */     if (equals(other)) {
/* 336 */       return this.pathService.emptyPath();
/*     */     }
/*     */     
/* 339 */     if (isEmptyPath()) {
/* 340 */       return otherPath;
/*     */     }
/*     */     
/* 343 */     ImmutableList<Name> otherNames = otherPath.names;
/* 344 */     int sharedSubsequenceLength = 0;
/* 345 */     for (int i = 0; i < Math.min(getNameCount(), otherNames.size()) && (
/* 346 */       (Name)this.names.get(i)).equals(otherNames.get(i)); i++) {
/* 347 */       sharedSubsequenceLength++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     int extraNamesInThis = Math.max(0, getNameCount() - sharedSubsequenceLength);
/*     */     
/* 355 */     ImmutableList<Name> extraNamesInOther = (otherNames.size() <= sharedSubsequenceLength) ? ImmutableList.of() : otherNames.subList(sharedSubsequenceLength, otherNames.size());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     List<Name> parts = new ArrayList<>(extraNamesInThis + extraNamesInOther.size());
/*     */ 
/*     */     
/* 363 */     parts.addAll(Collections.nCopies(extraNamesInThis, Name.PARENT));
/*     */     
/* 365 */     parts.addAll((Collection<? extends Name>)extraNamesInOther);
/*     */     
/* 367 */     return this.pathService.createRelativePath(parts);
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath toAbsolutePath() {
/* 372 */     return isAbsolute() ? this : getJimfsFileSystem().getWorkingDirectory().resolve(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath toRealPath(LinkOption... options) throws IOException {
/* 377 */     return getJimfsFileSystem().getDefaultView().toRealPath(this, this.pathService, (Set<? super LinkOption>)Options.getLinkOptions(options));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
/* 386 */     Preconditions.checkNotNull(modifiers);
/* 387 */     return register(watcher, events);
/*     */   }
/*     */ 
/*     */   
/*     */   public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
/* 392 */     Preconditions.checkNotNull(watcher);
/* 393 */     Preconditions.checkNotNull(events);
/* 394 */     if (!(watcher instanceof AbstractWatchService)) {
/* 395 */       throw new IllegalArgumentException("watcher (" + watcher + ") is not associated with this file system");
/*     */     }
/*     */ 
/*     */     
/* 399 */     AbstractWatchService service = (AbstractWatchService)watcher;
/* 400 */     return service.register(this, Arrays.asList(events));
/*     */   }
/*     */ 
/*     */   
/*     */   public URI toUri() {
/* 405 */     return getJimfsFileSystem().toUri(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public File toFile() {
/* 411 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Path> iterator() {
/* 416 */     return asList().iterator();
/*     */   }
/*     */   
/*     */   private List<Path> asList() {
/* 420 */     return new AbstractList<Path>()
/*     */       {
/*     */         public Path get(int index) {
/* 423 */           return JimfsPath.this.getName(index);
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 428 */           return JimfsPath.this.getNameCount();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Path other) {
/* 436 */     JimfsPath otherPath = (JimfsPath)other;
/* 437 */     return ComparisonChain.start().compare(getJimfsFileSystem().getUri(), ((JimfsPath)other).getJimfsFileSystem().getUri()).compare(this, otherPath, this.pathService).result();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 445 */     return (obj instanceof JimfsPath && compareTo((JimfsPath)obj) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 450 */     return this.pathService.hash(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 455 */     return this.pathService.toString(this);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private JimfsPath checkPath(Path other) {
/* 460 */     if (Preconditions.checkNotNull(other) instanceof JimfsPath && other.getFileSystem().equals(getFileSystem())) {
/* 461 */       return (JimfsPath)other;
/*     */     }
/* 463 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsPath.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */