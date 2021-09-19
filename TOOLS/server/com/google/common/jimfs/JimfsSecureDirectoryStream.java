/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.ClosedDirectoryStreamException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.SecureDirectoryStream;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.util.Iterator;
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
/*     */ final class JimfsSecureDirectoryStream
/*     */   implements SecureDirectoryStream<Path>
/*     */ {
/*     */   private final FileSystemView view;
/*     */   private final DirectoryStream.Filter<? super Path> filter;
/*     */   private final FileSystemState fileSystemState;
/*     */   private boolean open = true;
/*  55 */   private Iterator<Path> iterator = (Iterator<Path>)new DirectoryIterator();
/*     */ 
/*     */   
/*     */   public JimfsSecureDirectoryStream(FileSystemView view, DirectoryStream.Filter<? super Path> filter, FileSystemState fileSystemState) {
/*  59 */     this.view = (FileSystemView)Preconditions.checkNotNull(view);
/*  60 */     this.filter = (DirectoryStream.Filter<? super Path>)Preconditions.checkNotNull(filter);
/*  61 */     this.fileSystemState = fileSystemState;
/*  62 */     fileSystemState.register(this);
/*     */   }
/*     */   
/*     */   private JimfsPath path() {
/*  66 */     return this.view.getWorkingDirectoryPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Iterator<Path> iterator() {
/*  71 */     checkOpen();
/*  72 */     Iterator<Path> result = this.iterator;
/*  73 */     Preconditions.checkState((result != null), "iterator() has already been called once");
/*  74 */     this.iterator = null;
/*  75 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/*  80 */     this.open = false;
/*  81 */     this.fileSystemState.unregister(this);
/*     */   }
/*     */   
/*     */   protected synchronized void checkOpen() {
/*  85 */     if (!this.open)
/*  86 */       throw new ClosedDirectoryStreamException(); 
/*     */   }
/*     */   
/*     */   private final class DirectoryIterator extends AbstractIterator<Path> {
/*     */     @Nullable
/*     */     private Iterator<Name> fileNames;
/*     */     
/*     */     private DirectoryIterator() {}
/*     */     
/*     */     protected synchronized Path computeNext() {
/*  96 */       JimfsSecureDirectoryStream.this.checkOpen();
/*     */       
/*     */       try {
/*  99 */         if (this.fileNames == null) {
/* 100 */           this.fileNames = (Iterator<Name>)JimfsSecureDirectoryStream.this.view.snapshotWorkingDirectoryEntries().iterator();
/*     */         }
/*     */         
/* 103 */         while (this.fileNames.hasNext()) {
/* 104 */           Name name = this.fileNames.next();
/* 105 */           Path path = JimfsSecureDirectoryStream.this.view.getWorkingDirectoryPath().resolve(name);
/*     */           
/* 107 */           if (JimfsSecureDirectoryStream.this.filter.accept(path)) {
/* 108 */             return path;
/*     */           }
/*     */         } 
/*     */         
/* 112 */         return (Path)endOfData();
/* 113 */       } catch (IOException e) {
/* 114 */         throw new DirectoryIteratorException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public static final DirectoryStream.Filter<Object> ALWAYS_TRUE_FILTER = new DirectoryStream.Filter()
/*     */     {
/*     */       public boolean accept(Object entry) throws IOException
/*     */       {
/* 126 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public SecureDirectoryStream<Path> newDirectoryStream(Path path, LinkOption... options) throws IOException {
/* 133 */     checkOpen();
/* 134 */     JimfsPath checkedPath = checkPath(path);
/*     */ 
/*     */ 
/*     */     
/* 138 */     return (SecureDirectoryStream<Path>)this.view.newDirectoryStream(checkedPath, (DirectoryStream.Filter)ALWAYS_TRUE_FILTER, (Set<? super LinkOption>)Options.getLinkOptions(options), path().resolve(checkedPath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/* 149 */     checkOpen();
/* 150 */     JimfsPath checkedPath = checkPath(path);
/* 151 */     ImmutableSet<OpenOption> opts = Options.getOptionsForChannel(options);
/* 152 */     return new JimfsFileChannel(this.view.getOrCreateRegularFile(checkedPath, (Set<OpenOption>)opts, (FileAttribute<?>[])new FileAttribute[0]), (Set<OpenOption>)opts, this.fileSystemState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteFile(Path path) throws IOException {
/* 158 */     checkOpen();
/* 159 */     JimfsPath checkedPath = checkPath(path);
/* 160 */     this.view.deleteFile(checkedPath, FileSystemView.DeleteMode.NON_DIRECTORY_ONLY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteDirectory(Path path) throws IOException {
/* 165 */     checkOpen();
/* 166 */     JimfsPath checkedPath = checkPath(path);
/* 167 */     this.view.deleteFile(checkedPath, FileSystemView.DeleteMode.DIRECTORY_ONLY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(Path srcPath, SecureDirectoryStream<Path> targetDir, Path targetPath) throws IOException {
/* 173 */     checkOpen();
/* 174 */     JimfsPath checkedSrcPath = checkPath(srcPath);
/* 175 */     JimfsPath checkedTargetPath = checkPath(targetPath);
/*     */     
/* 177 */     if (!(targetDir instanceof JimfsSecureDirectoryStream)) {
/* 178 */       throw new ProviderMismatchException("targetDir isn't a secure directory stream associated with this file system");
/*     */     }
/*     */ 
/*     */     
/* 182 */     JimfsSecureDirectoryStream checkedTargetDir = (JimfsSecureDirectoryStream)targetDir;
/*     */     
/* 184 */     this.view.copy(checkedSrcPath, checkedTargetDir.view, checkedTargetPath, (Set<CopyOption>)ImmutableSet.of(), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <V extends FileAttributeView> V getFileAttributeView(Class<V> type) {
/* 194 */     return getFileAttributeView(path().getFileSystem().getPath(".", new String[0]), type, new LinkOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/* 200 */     checkOpen();
/* 201 */     final JimfsPath checkedPath = checkPath(path);
/* 202 */     final ImmutableSet<LinkOption> optionsSet = Options.getLinkOptions(options);
/* 203 */     return this.view.getFileAttributeView(new FileLookup()
/*     */         {
/*     */           public File lookup() throws IOException
/*     */           {
/* 207 */             JimfsSecureDirectoryStream.this.checkOpen();
/* 208 */             return JimfsSecureDirectoryStream.this.view.lookUpWithLock(checkedPath, (Set<? super LinkOption>)optionsSet).requireExists(checkedPath).file();
/*     */           }
/*     */         }type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JimfsPath checkPath(Path path) {
/* 218 */     if (path instanceof JimfsPath) {
/* 219 */       return (JimfsPath)path;
/*     */     }
/* 221 */     throw new ProviderMismatchException("path " + path + " is not associated with a Jimfs file system");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsSecureDirectoryStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */