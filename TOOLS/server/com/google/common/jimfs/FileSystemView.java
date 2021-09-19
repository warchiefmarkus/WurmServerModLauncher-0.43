/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSortedSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryNotEmptyException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.FileSystemException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FileSystemView
/*     */ {
/*     */   private final JimfsFileStore store;
/*     */   private final Directory workingDirectory;
/*     */   private final JimfsPath workingDirectoryPath;
/*     */   
/*     */   public FileSystemView(JimfsFileStore store, Directory workingDirectory, JimfsPath workingDirectoryPath) {
/*  78 */     this.store = (JimfsFileStore)Preconditions.checkNotNull(store);
/*  79 */     this.workingDirectory = (Directory)Preconditions.checkNotNull(workingDirectory);
/*  80 */     this.workingDirectoryPath = (JimfsPath)Preconditions.checkNotNull(workingDirectoryPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSameFileSystem(FileSystemView other) {
/*  87 */     return (this.store == other.store);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemState state() {
/*  94 */     return this.store.state();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath getWorkingDirectoryPath() {
/* 102 */     return this.workingDirectoryPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DirectoryEntry lookUpWithLock(JimfsPath path, Set<? super LinkOption> options) throws IOException {
/* 110 */     this.store.readLock().lock();
/*     */     try {
/* 112 */       return lookUp(path, options);
/*     */     } finally {
/* 114 */       this.store.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DirectoryEntry lookUp(JimfsPath path, Set<? super LinkOption> options) throws IOException {
/* 123 */     return this.store.lookUp(this.workingDirectory, path, options);
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
/*     */   public DirectoryStream<Path> newDirectoryStream(JimfsPath dir, DirectoryStream.Filter<? super Path> filter, Set<? super LinkOption> options, JimfsPath basePathForStream) throws IOException {
/* 137 */     Directory file = (Directory)lookUpWithLock(dir, options).requireDirectory(dir).file();
/*     */ 
/*     */     
/* 140 */     FileSystemView view = new FileSystemView(this.store, file, basePathForStream);
/* 141 */     JimfsSecureDirectoryStream stream = new JimfsSecureDirectoryStream(view, filter, state());
/* 142 */     return this.store.supportsFeature(Feature.SECURE_DIRECTORY_STREAM) ? stream : new DowngradedDirectoryStream(stream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<Name> snapshotWorkingDirectoryEntries() {
/* 151 */     this.store.readLock().lock();
/*     */     try {
/* 153 */       ImmutableSortedSet<Name> names = this.workingDirectory.snapshot();
/* 154 */       this.workingDirectory.updateAccessTime();
/* 155 */       return names;
/*     */     } finally {
/* 157 */       this.store.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<Name, Long> snapshotModifiedTimes(JimfsPath path) throws IOException {
/* 166 */     ImmutableMap.Builder<Name, Long> modifiedTimes = ImmutableMap.builder();
/*     */     
/* 168 */     this.store.readLock().lock();
/*     */     try {
/* 170 */       Directory dir = (Directory)lookUp(path, (Set<? super LinkOption>)Options.FOLLOW_LINKS).requireDirectory(path).file();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       for (DirectoryEntry entry : dir) {
/* 178 */         if (!entry.name().equals(Name.SELF) && !entry.name().equals(Name.PARENT)) {
/* 179 */           modifiedTimes.put(entry.name(), Long.valueOf(entry.file().getLastModifiedTime()));
/*     */         }
/*     */       } 
/*     */       
/* 183 */       return modifiedTimes.build();
/*     */     } finally {
/* 185 */       this.store.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSameFile(JimfsPath path, FileSystemView view2, JimfsPath path2) throws IOException {
/* 195 */     if (!isSameFileSystem(view2)) {
/* 196 */       return false;
/*     */     }
/*     */     
/* 199 */     this.store.readLock().lock();
/*     */     try {
/* 201 */       File file = lookUp(path, (Set<? super LinkOption>)Options.FOLLOW_LINKS).fileOrNull();
/* 202 */       File file2 = view2.lookUp(path2, (Set<? super LinkOption>)Options.FOLLOW_LINKS).fileOrNull();
/* 203 */       return (file != null && Objects.equals(file, file2));
/*     */     } finally {
/* 205 */       this.store.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath toRealPath(JimfsPath path, PathService pathService, Set<? super LinkOption> options) throws IOException {
/* 215 */     Preconditions.checkNotNull(path);
/* 216 */     Preconditions.checkNotNull(options);
/*     */     
/* 218 */     this.store.readLock().lock();
/*     */     try {
/* 220 */       DirectoryEntry entry = lookUp(path, options).requireExists(path);
/*     */       
/* 222 */       List<Name> names = new ArrayList<>();
/* 223 */       names.add(entry.name());
/* 224 */       while (!entry.file().isRootDirectory()) {
/* 225 */         entry = entry.directory().entryInParent();
/* 226 */         names.add(entry.name());
/*     */       } 
/*     */ 
/*     */       
/* 230 */       List<Name> reversed = Lists.reverse(names);
/* 231 */       Name root = reversed.remove(0);
/* 232 */       return pathService.createPath(root, reversed);
/*     */     } finally {
/* 234 */       this.store.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Directory createDirectory(JimfsPath path, FileAttribute<?>... attrs) throws IOException {
/* 243 */     return (Directory)createFile(path, (Supplier)this.store.directoryCreator(), true, attrs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SymbolicLink createSymbolicLink(JimfsPath path, JimfsPath target, FileAttribute<?>... attrs) throws IOException {
/* 252 */     if (!this.store.supportsFeature(Feature.SYMBOLIC_LINKS)) {
/* 253 */       throw new UnsupportedOperationException();
/*     */     }
/* 255 */     return (SymbolicLink)createFile(path, (Supplier)this.store.symbolicLinkCreator(target), true, attrs);
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
/*     */   private File createFile(JimfsPath path, Supplier<? extends File> fileCreator, boolean failIfExists, FileAttribute<?>... attrs) throws IOException {
/* 269 */     Preconditions.checkNotNull(path);
/* 270 */     Preconditions.checkNotNull(fileCreator);
/*     */     
/* 272 */     this.store.writeLock().lock();
/*     */     try {
/* 274 */       DirectoryEntry entry = lookUp(path, (Set<? super LinkOption>)Options.NOFOLLOW_LINKS);
/*     */       
/* 276 */       if (entry.exists()) {
/* 277 */         if (failIfExists) {
/* 278 */           throw new FileAlreadyExistsException(path.toString());
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 284 */         return entry.file();
/*     */       } 
/*     */       
/* 287 */       Directory parent = entry.directory();
/*     */       
/* 289 */       File newFile = (File)fileCreator.get();
/* 290 */       this.store.setInitialAttributes(newFile, attrs);
/* 291 */       parent.link(path.name(), newFile);
/* 292 */       parent.updateModifiedTime();
/* 293 */       return newFile;
/*     */     } finally {
/* 295 */       this.store.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegularFile getOrCreateRegularFile(JimfsPath path, Set<OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/* 305 */     Preconditions.checkNotNull(path);
/*     */     
/* 307 */     if (!options.contains(StandardOpenOption.CREATE_NEW)) {
/*     */       
/* 309 */       RegularFile file = lookUpRegularFile(path, options);
/* 310 */       if (file != null) {
/* 311 */         return file;
/*     */       }
/*     */     } 
/*     */     
/* 315 */     if (options.contains(StandardOpenOption.CREATE) || options.contains(StandardOpenOption.CREATE_NEW)) {
/* 316 */       return getOrCreateRegularFileWithWriteLock(path, options, attrs);
/*     */     }
/* 318 */     throw new NoSuchFileException(path.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private RegularFile lookUpRegularFile(JimfsPath path, Set<OpenOption> options) throws IOException {
/* 329 */     this.store.readLock().lock();
/*     */     try {
/* 331 */       DirectoryEntry entry = lookUp(path, (Set)options);
/* 332 */       if (entry.exists()) {
/* 333 */         File file = entry.file();
/* 334 */         if (!file.isRegularFile()) {
/* 335 */           throw new FileSystemException(path.toString(), null, "not a regular file");
/*     */         }
/* 337 */         return open((RegularFile)file, options);
/*     */       } 
/* 339 */       return null;
/*     */     } finally {
/*     */       
/* 342 */       this.store.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RegularFile getOrCreateRegularFileWithWriteLock(JimfsPath path, Set<OpenOption> options, FileAttribute<?>[] attrs) throws IOException {
/* 351 */     this.store.writeLock().lock();
/*     */     try {
/* 353 */       File file = createFile(path, (Supplier)this.store.regularFileCreator(), options.contains(StandardOpenOption.CREATE_NEW), attrs);
/*     */       
/* 355 */       if (!file.isRegularFile()) {
/* 356 */         throw new FileSystemException(path.toString(), null, "not a regular file");
/*     */       }
/* 358 */       return open((RegularFile)file, options);
/*     */     } finally {
/* 360 */       this.store.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RegularFile open(RegularFile file, Set<OpenOption> options) {
/* 369 */     if (options.contains(StandardOpenOption.TRUNCATE_EXISTING) && options.contains(StandardOpenOption.WRITE)) {
/* 370 */       file.writeLock().lock();
/*     */       try {
/* 372 */         file.truncate(0L);
/*     */       } finally {
/* 374 */         file.writeLock().unlock();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 380 */     file.opened();
/*     */     
/* 382 */     return file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath readSymbolicLink(JimfsPath path) throws IOException {
/* 389 */     if (!this.store.supportsFeature(Feature.SYMBOLIC_LINKS)) {
/* 390 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/* 393 */     SymbolicLink symbolicLink = (SymbolicLink)lookUpWithLock(path, (Set<? super LinkOption>)Options.NOFOLLOW_LINKS).requireSymbolicLink(path).file();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 398 */     return symbolicLink.target();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkAccess(JimfsPath path) throws IOException {
/* 407 */     lookUpWithLock(path, (Set<? super LinkOption>)Options.FOLLOW_LINKS).requireExists(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void link(JimfsPath link, FileSystemView existingView, JimfsPath existing) throws IOException {
/* 417 */     Preconditions.checkNotNull(link);
/* 418 */     Preconditions.checkNotNull(existingView);
/* 419 */     Preconditions.checkNotNull(existing);
/*     */     
/* 421 */     if (!this.store.supportsFeature(Feature.LINKS)) {
/* 422 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/* 425 */     if (!isSameFileSystem(existingView)) {
/* 426 */       throw new FileSystemException(link.toString(), existing.toString(), "can't link: source and target are in different file system instances");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 432 */     Name linkName = link.name();
/*     */ 
/*     */     
/* 435 */     this.store.writeLock().lock();
/*     */     
/*     */     try {
/* 438 */       File existingFile = existingView.lookUp(existing, (Set<? super LinkOption>)Options.FOLLOW_LINKS).requireExists(existing).file();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 443 */       if (!existingFile.isRegularFile()) {
/* 444 */         throw new FileSystemException(link.toString(), existing.toString(), "can't link: not a regular file");
/*     */       }
/*     */ 
/*     */       
/* 448 */       Directory linkParent = lookUp(link, (Set<? super LinkOption>)Options.NOFOLLOW_LINKS).requireDoesNotExist(link).directory();
/*     */ 
/*     */       
/* 451 */       linkParent.link(linkName, existingFile);
/* 452 */       linkParent.updateModifiedTime();
/*     */     } finally {
/* 454 */       this.store.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteFile(JimfsPath path, DeleteMode deleteMode) throws IOException {
/* 462 */     this.store.writeLock().lock();
/*     */     try {
/* 464 */       DirectoryEntry entry = lookUp(path, (Set<? super LinkOption>)Options.NOFOLLOW_LINKS).requireExists(path);
/* 465 */       delete(entry, deleteMode, path);
/*     */     } finally {
/* 467 */       this.store.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delete(DirectoryEntry entry, DeleteMode deleteMode, JimfsPath pathForException) throws IOException {
/* 476 */     Directory parent = entry.directory();
/* 477 */     File file = entry.file();
/*     */     
/* 479 */     checkDeletable(file, deleteMode, pathForException);
/* 480 */     parent.unlink(entry.name());
/* 481 */     parent.updateModifiedTime();
/*     */     
/* 483 */     file.deleted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum DeleteMode
/*     */   {
/* 493 */     ANY,
/*     */ 
/*     */ 
/*     */     
/* 497 */     NON_DIRECTORY_ONLY,
/*     */ 
/*     */ 
/*     */     
/* 501 */     DIRECTORY_ONLY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkDeletable(File file, DeleteMode mode, Path path) throws IOException {
/* 508 */     if (file.isRootDirectory()) {
/* 509 */       throw new FileSystemException(path.toString(), null, "can't delete root directory");
/*     */     }
/*     */     
/* 512 */     if (file.isDirectory()) {
/* 513 */       if (mode == DeleteMode.NON_DIRECTORY_ONLY) {
/* 514 */         throw new FileSystemException(path.toString(), null, "can't delete: is a directory");
/*     */       }
/*     */       
/* 517 */       checkEmpty((Directory)file, path);
/* 518 */     } else if (mode == DeleteMode.DIRECTORY_ONLY) {
/* 519 */       throw new FileSystemException(path.toString(), null, "can't delete: is not a directory");
/*     */     } 
/*     */     
/* 522 */     if (file == this.workingDirectory && !path.isAbsolute())
/*     */     {
/*     */ 
/*     */       
/* 526 */       throw new FileSystemException(path.toString(), null, "invalid argument");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkEmpty(Directory dir, Path pathForException) throws FileSystemException {
/* 534 */     if (!dir.isEmpty()) {
/* 535 */       throw new DirectoryNotEmptyException(pathForException.toString());
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
/*     */   public void copy(JimfsPath source, FileSystemView destView, JimfsPath dest, Set<CopyOption> options, boolean move) throws IOException {
/*     */     File sourceFile;
/* 549 */     Preconditions.checkNotNull(source);
/* 550 */     Preconditions.checkNotNull(destView);
/* 551 */     Preconditions.checkNotNull(dest);
/* 552 */     Preconditions.checkNotNull(options);
/*     */     
/* 554 */     boolean sameFileSystem = isSameFileSystem(destView);
/*     */ 
/*     */     
/* 557 */     File copyFile = null;
/* 558 */     lockBoth(this.store.writeLock(), destView.store.writeLock());
/*     */     try {
/* 560 */       DirectoryEntry sourceEntry = lookUp(source, (Set)options).requireExists(source);
/* 561 */       DirectoryEntry destEntry = destView.lookUp(dest, (Set<? super LinkOption>)Options.NOFOLLOW_LINKS);
/*     */       
/* 563 */       Directory sourceParent = sourceEntry.directory();
/* 564 */       sourceFile = sourceEntry.file();
/*     */       
/* 566 */       Directory destParent = destEntry.directory();
/*     */       
/* 568 */       if (move && sourceFile.isDirectory()) {
/* 569 */         if (sameFileSystem) {
/* 570 */           checkMovable(sourceFile, source);
/* 571 */           checkNotAncestor(sourceFile, destParent, destView);
/*     */         }
/*     */         else {
/*     */           
/* 575 */           checkDeletable(sourceFile, DeleteMode.ANY, source);
/*     */         } 
/*     */       }
/*     */       
/* 579 */       if (destEntry.exists()) {
/* 580 */         if (destEntry.file().equals(sourceFile))
/*     */           return; 
/* 582 */         if (options.contains(StandardCopyOption.REPLACE_EXISTING)) {
/* 583 */           destView.delete(destEntry, DeleteMode.ANY, dest);
/*     */         } else {
/* 585 */           throw new FileAlreadyExistsException(dest.toString());
/*     */         } 
/*     */       } 
/*     */       
/* 589 */       if (move && sameFileSystem) {
/*     */         
/* 591 */         sourceParent.unlink(source.name());
/* 592 */         sourceParent.updateModifiedTime();
/*     */         
/* 594 */         destParent.link(dest.name(), sourceFile);
/* 595 */         destParent.updateModifiedTime();
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 601 */         AttributeCopyOption attributeCopyOption = AttributeCopyOption.NONE;
/* 602 */         if (move) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 607 */           attributeCopyOption = AttributeCopyOption.BASIC;
/* 608 */         } else if (options.contains(StandardCopyOption.COPY_ATTRIBUTES)) {
/*     */ 
/*     */           
/* 611 */           attributeCopyOption = sameFileSystem ? AttributeCopyOption.ALL : AttributeCopyOption.BASIC;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 616 */         copyFile = destView.store.copyWithoutContent(sourceFile, attributeCopyOption);
/* 617 */         destParent.link(dest.name(), copyFile);
/* 618 */         destParent.updateModifiedTime();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 625 */         lockSourceAndCopy(sourceFile, copyFile);
/*     */         
/* 627 */         if (move)
/*     */         {
/*     */           
/* 630 */           delete(sourceEntry, DeleteMode.ANY, source);
/*     */         }
/*     */       } 
/*     */     } finally {
/* 634 */       destView.store.writeLock().unlock();
/* 635 */       this.store.writeLock().unlock();
/*     */     } 
/*     */     
/* 638 */     if (copyFile != null) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 645 */         sourceFile.copyContentTo(copyFile);
/*     */       }
/*     */       finally {
/*     */         
/* 649 */         unlockSourceAndCopy(sourceFile, copyFile);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkMovable(File file, JimfsPath path) throws FileSystemException {
/* 655 */     if (file.isRootDirectory()) {
/* 656 */       throw new FileSystemException(path.toString(), null, "can't move root directory");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void lockBoth(Lock sourceWriteLock, Lock destWriteLock) {
/*     */     while (true) {
/* 667 */       sourceWriteLock.lock();
/* 668 */       if (destWriteLock.tryLock()) {
/*     */         return;
/*     */       }
/* 671 */       sourceWriteLock.unlock();
/*     */ 
/*     */       
/* 674 */       destWriteLock.lock();
/* 675 */       if (sourceWriteLock.tryLock()) {
/*     */         return;
/*     */       }
/* 678 */       destWriteLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkNotAncestor(File source, Directory destParent, FileSystemView destView) throws IOException {
/* 689 */     if (!isSameFileSystem(destView)) {
/*     */       return;
/*     */     }
/*     */     
/* 693 */     Directory current = destParent;
/*     */     while (true) {
/* 695 */       if (current.equals(source)) {
/* 696 */         throw new IOException("invalid argument: can't move directory into a subdirectory of itself");
/*     */       }
/*     */ 
/*     */       
/* 700 */       if (current.isRootDirectory()) {
/*     */         return;
/*     */       }
/* 703 */       current = current.parent();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void lockSourceAndCopy(File sourceFile, File copyFile) {
/* 713 */     sourceFile.opened();
/* 714 */     ReadWriteLock sourceLock = sourceFile.contentLock();
/* 715 */     if (sourceLock != null) {
/* 716 */       sourceLock.readLock().lock();
/*     */     }
/* 718 */     ReadWriteLock copyLock = copyFile.contentLock();
/* 719 */     if (copyLock != null) {
/* 720 */       copyLock.writeLock().lock();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void unlockSourceAndCopy(File sourceFile, File copyFile) {
/* 729 */     ReadWriteLock sourceLock = sourceFile.contentLock();
/* 730 */     if (sourceLock != null) {
/* 731 */       sourceLock.readLock().unlock();
/*     */     }
/* 733 */     ReadWriteLock copyLock = copyFile.contentLock();
/* 734 */     if (copyLock != null) {
/* 735 */       copyLock.writeLock().unlock();
/*     */     }
/* 737 */     sourceFile.closed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(FileLookup lookup, Class<V> type) {
/* 745 */     return this.store.getFileAttributeView(lookup, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(final JimfsPath path, Class<V> type, final Set<? super LinkOption> options) {
/* 754 */     return this.store.getFileAttributeView(new FileLookup()
/*     */         {
/*     */           public File lookup() throws IOException
/*     */           {
/* 758 */             return FileSystemView.this.lookUpWithLock(path, options).requireExists(path).file();
/*     */           }
/*     */         }type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(JimfsPath path, Class<A> type, Set<? super LinkOption> options) throws IOException {
/* 771 */     File file = lookUpWithLock(path, options).requireExists(path).file();
/*     */ 
/*     */     
/* 774 */     return this.store.readAttributes(file, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<String, Object> readAttributes(JimfsPath path, String attributes, Set<? super LinkOption> options) throws IOException {
/* 782 */     File file = lookUpWithLock(path, options).requireExists(path).file();
/*     */ 
/*     */     
/* 785 */     return this.store.readAttributes(file, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(JimfsPath path, String attribute, Object value, Set<? super LinkOption> options) throws IOException {
/* 795 */     File file = lookUpWithLock(path, options).requireExists(path).file();
/*     */ 
/*     */     
/* 798 */     this.store.setAttribute(file, attribute, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\FileSystemView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */