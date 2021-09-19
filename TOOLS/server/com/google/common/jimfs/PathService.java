/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Functions;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Ordering;
/*     */ import java.net.URI;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ final class PathService
/*     */   implements Comparator<JimfsPath>
/*     */ {
/*  52 */   private static final Ordering<Name> DISPLAY_ROOT_ORDERING = Name.displayOrdering().nullsLast();
/*  53 */   private static final Ordering<Iterable<Name>> DISPLAY_NAMES_ORDERING = Name.displayOrdering().lexicographical();
/*     */ 
/*     */   
/*  56 */   private static final Ordering<Name> CANONICAL_ROOT_ORDERING = Name.canonicalOrdering().nullsLast();
/*     */   
/*  58 */   private static final Ordering<Iterable<Name>> CANONICAL_NAMES_ORDERING = Name.canonicalOrdering().lexicographical();
/*     */   
/*     */   private final PathType type;
/*     */   
/*     */   private final ImmutableSet<PathNormalization> displayNormalizations;
/*     */   
/*     */   private final ImmutableSet<PathNormalization> canonicalNormalizations;
/*     */   
/*     */   private final boolean equalityUsesCanonicalForm;
/*     */   
/*     */   private final Ordering<Name> rootOrdering;
/*     */   private final Ordering<Iterable<Name>> namesOrdering;
/*     */   private volatile FileSystem fileSystem;
/*     */   private volatile JimfsPath emptyPath;
/*     */   
/*     */   PathService(Configuration config) {
/*  74 */     this(config.pathType, (Iterable<PathNormalization>)config.nameDisplayNormalization, (Iterable<PathNormalization>)config.nameCanonicalNormalization, config.pathEqualityUsesCanonicalForm);
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
/*     */   PathService(PathType type, Iterable<PathNormalization> displayNormalizations, Iterable<PathNormalization> canonicalNormalizations, boolean equalityUsesCanonicalForm) {
/*  86 */     this.type = (PathType)Preconditions.checkNotNull(type);
/*  87 */     this.displayNormalizations = ImmutableSet.copyOf(displayNormalizations);
/*  88 */     this.canonicalNormalizations = ImmutableSet.copyOf(canonicalNormalizations);
/*  89 */     this.equalityUsesCanonicalForm = equalityUsesCanonicalForm;
/*     */     
/*  91 */     this.rootOrdering = equalityUsesCanonicalForm ? CANONICAL_ROOT_ORDERING : DISPLAY_ROOT_ORDERING;
/*  92 */     this.namesOrdering = equalityUsesCanonicalForm ? CANONICAL_NAMES_ORDERING : DISPLAY_NAMES_ORDERING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileSystem(FileSystem fileSystem) {
/* 101 */     Preconditions.checkState((this.fileSystem == null), "may not set fileSystem twice");
/* 102 */     this.fileSystem = (FileSystem)Preconditions.checkNotNull(fileSystem);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystem getFileSystem() {
/* 109 */     return this.fileSystem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSeparator() {
/* 116 */     return this.type.getSeparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath emptyPath() {
/* 123 */     JimfsPath result = this.emptyPath;
/* 124 */     if (result == null) {
/*     */       
/* 126 */       result = createPathInternal(null, (Iterable<Name>)ImmutableList.of(Name.EMPTY));
/* 127 */       this.emptyPath = result;
/* 128 */       return result;
/*     */     } 
/* 130 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name name(String name) {
/* 137 */     switch (name) {
/*     */       case "":
/* 139 */         return Name.EMPTY;
/*     */       case ".":
/* 141 */         return Name.SELF;
/*     */       case "..":
/* 143 */         return Name.PARENT;
/*     */     } 
/* 145 */     String display = PathNormalization.normalize(name, (Iterable<PathNormalization>)this.displayNormalizations);
/* 146 */     String canonical = PathNormalization.normalize(name, (Iterable<PathNormalization>)this.canonicalNormalizations);
/* 147 */     return Name.create(display, canonical);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   List<Name> names(Iterable<String> names) {
/* 156 */     List<Name> result = new ArrayList<>();
/* 157 */     for (String name : names) {
/* 158 */       result.add(name(name));
/*     */     }
/* 160 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath createRoot(Name root) {
/* 167 */     return createPath((Name)Preconditions.checkNotNull(root), (Iterable<Name>)ImmutableList.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath createFileName(Name name) {
/* 174 */     return createPath(null, (Iterable<Name>)ImmutableList.of(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath createRelativePath(Iterable<Name> names) {
/* 181 */     return createPath(null, (Iterable<Name>)ImmutableList.copyOf(names));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath createPath(@Nullable Name root, Iterable<Name> names) {
/* 188 */     ImmutableList<Name> nameList = ImmutableList.copyOf(Iterables.filter(names, NOT_EMPTY));
/* 189 */     if (root == null && nameList.isEmpty())
/*     */     {
/*     */       
/* 192 */       return emptyPath();
/*     */     }
/* 194 */     return createPathInternal(root, (Iterable<Name>)nameList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JimfsPath createPathInternal(@Nullable Name root, Iterable<Name> names) {
/* 201 */     return new JimfsPath(this, root, names);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath parsePath(String first, String... more) {
/* 208 */     String joined = this.type.joiner().join(Iterables.filter(Lists.asList(first, (Object[])more), NOT_EMPTY));
/* 209 */     return toPath(this.type.parsePath(joined));
/*     */   }
/*     */   
/*     */   private JimfsPath toPath(PathType.ParseResult parsed) {
/* 213 */     Name root = (parsed.root() == null) ? null : name(parsed.root());
/* 214 */     Iterable<Name> names = names(parsed.names());
/* 215 */     return createPath(root, names);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(JimfsPath path) {
/* 222 */     Name root = path.root();
/* 223 */     String rootString = (root == null) ? null : root.toString();
/* 224 */     Iterable<String> names = Iterables.transform((Iterable)path.names(), Functions.toStringFunction());
/* 225 */     return this.type.toString(rootString, names);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hash(JimfsPath path) {
/* 232 */     int hash = 31;
/* 233 */     hash = 31 * hash + getFileSystem().hashCode();
/*     */     
/* 235 */     Name root = path.root();
/* 236 */     ImmutableList<Name> names = path.names();
/*     */     
/* 238 */     if (this.equalityUsesCanonicalForm) {
/*     */       
/* 240 */       hash = 31 * hash + ((root == null) ? 0 : root.hashCode());
/* 241 */       for (Name name : names) {
/* 242 */         hash = 31 * hash + name.hashCode();
/*     */       }
/*     */     } else {
/*     */       
/* 246 */       hash = 31 * hash + ((root == null) ? 0 : root.toString().hashCode());
/* 247 */       for (Name name : names) {
/* 248 */         hash = 31 * hash + name.toString().hashCode();
/*     */       }
/*     */     } 
/* 251 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compare(JimfsPath a, JimfsPath b) {
/* 256 */     return ComparisonChain.start().compare(a.root(), b.root(), (Comparator)this.rootOrdering).compare(a.names(), b.names(), (Comparator)this.namesOrdering).result();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI toUri(URI fileSystemUri, JimfsPath path) {
/* 267 */     Preconditions.checkArgument(path.isAbsolute(), "path (%s) must be absolute", new Object[] { path });
/* 268 */     String root = String.valueOf(path.root());
/* 269 */     Iterable<String> names = Iterables.transform((Iterable)path.names(), Functions.toStringFunction());
/* 270 */     return this.type.toUri(fileSystemUri, root, names, Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath fromUri(URI uri) {
/* 277 */     return toPath(this.type.fromUri(uri));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatcher createPathMatcher(String syntaxAndPattern) {
/* 285 */     return PathMatchers.getPathMatcher(syntaxAndPattern, this.type.getSeparator() + this.type.getOtherSeparators(), this.displayNormalizations);
/*     */   }
/*     */ 
/*     */   
/* 289 */   private static final Predicate<Object> NOT_EMPTY = new Predicate<Object>()
/*     */     {
/*     */       public boolean apply(Object input)
/*     */       {
/* 293 */         return !input.toString().isEmpty();
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\PathService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */