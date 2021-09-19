/*     */ package com.sun.javaws.jnl;
/*     */ 
/*     */ import com.sun.deploy.config.JREInfo;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.URLUtil;
/*     */ import com.sun.deploy.xml.XMLNode;
/*     */ import com.sun.deploy.xml.XMLNodeBuilder;
/*     */ import com.sun.javaws.cache.DiskCacheEntry;
/*     */ import com.sun.javaws.cache.DownloadProtocol;
/*     */ import com.sun.javaws.util.VersionString;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourcesDesc
/*     */   implements ResourceType
/*     */ {
/*  29 */   private ArrayList _list = null;
/*  30 */   private LaunchDesc _parent = null;
/*     */ 
/*     */   
/*     */   public ResourcesDesc() {
/*  34 */     this._list = new ArrayList();
/*     */   }
/*     */   public LaunchDesc getParent() {
/*  37 */     return this._parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setParent(LaunchDesc paramLaunchDesc) {
/*  44 */     this._parent = paramLaunchDesc;
/*  45 */     for (byte b = 0; b < this._list.size(); b++) {
/*  46 */       JREDesc jREDesc = (JREDesc)this._list.get(b);
/*  47 */       if (jREDesc instanceof JREDesc) {
/*  48 */         JREDesc jREDesc1 = jREDesc;
/*  49 */         if (jREDesc1.getNestedResources() != null) {
/*  50 */           jREDesc1.getNestedResources().setParent(paramLaunchDesc);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void addResource(ResourceType paramResourceType) {
/*  57 */     if (paramResourceType != null)
/*  58 */       this._list.add(paramResourceType); 
/*     */   }
/*     */   
/*     */   boolean isEmpty() {
/*  62 */     return this._list.isEmpty();
/*     */   }
/*     */   public JREDesc getSelectedJRE() {
/*  65 */     for (byte b = 0; b < this._list.size(); b++) {
/*  66 */       JREDesc jREDesc = (JREDesc)this._list.get(b);
/*  67 */       if (jREDesc instanceof JREDesc && ((JREDesc)jREDesc).isSelected()) {
/*  68 */         return jREDesc;
/*     */       }
/*     */     } 
/*  71 */     return null;
/*     */   }
/*     */   
/*     */   public JARDesc[] getLocalJarDescs() {
/*  75 */     ArrayList arrayList = new ArrayList(this._list.size());
/*  76 */     for (byte b = 0; b < this._list.size(); b++) {
/*  77 */       Object object = this._list.get(b);
/*  78 */       if (object instanceof JARDesc) arrayList.add(object); 
/*     */     } 
/*  80 */     return toJARDescArray(arrayList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtensionDesc[] getExtensionDescs() {
/*  86 */     ArrayList arrayList = new ArrayList();
/*  87 */     ExtensionDesc[] arrayOfExtensionDesc = new ExtensionDesc[0];
/*  88 */     visit(new ResourceVisitor(this, arrayList) { private final ArrayList val$l; private final ResourcesDesc this$0; public void visitJARDesc(JARDesc param1JARDesc) {}
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/*  91 */             this.val$l.add(param1ExtensionDesc);
/*     */           } public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/*  95 */     return (ExtensionDesc[])arrayList.toArray((Object[])arrayOfExtensionDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JARDesc[] getEagerOrAllJarDescs(boolean paramBoolean) {
/* 104 */     HashSet hashSet = new HashSet();
/*     */ 
/*     */ 
/*     */     
/* 108 */     if (!paramBoolean)
/* 109 */       visit(new ResourceVisitor(this, hashSet) { private final HashSet val$eagerParts; private final ResourcesDesc this$0;
/*     */             public void visitJARDesc(JARDesc param1JARDesc) {
/* 111 */               if (!param1JARDesc.isLazyDownload() && param1JARDesc.getPartName() != null)
/* 112 */                 this.val$eagerParts.add(param1JARDesc.getPartName()); 
/*     */             }
/*     */             
/*     */             public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */             
/*     */             public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {}
/*     */             
/*     */             public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */             
/*     */             public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */         ); 
/* 123 */     ArrayList arrayList = new ArrayList();
/* 124 */     addJarsToList(arrayList, hashSet, paramBoolean, true);
/* 125 */     return toJARDescArray(arrayList);
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
/*     */   private void addJarsToList(ArrayList paramArrayList, HashSet paramHashSet, boolean paramBoolean1, boolean paramBoolean2) {
/* 139 */     visit(new ResourceVisitor(this, paramBoolean1, paramBoolean2, paramHashSet, paramArrayList) { private final boolean val$includeAll; private final boolean val$includeEager; private final HashSet val$includeParts; private final ArrayList val$list; private final ResourcesDesc this$0;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {
/* 141 */             if (this.val$includeAll || (this.val$includeEager && !param1JARDesc.isLazyDownload()) || this.val$includeParts.contains(param1JARDesc.getPartName()))
/*     */             {
/* 143 */               this.val$list.add(param1JARDesc);
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/* 151 */             HashSet hashSet = param1ExtensionDesc.getExtensionPackages(this.val$includeParts, this.val$includeEager);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 162 */             if (param1ExtensionDesc.getExtensionDesc() == null) {
/* 163 */               String str = JREInfo.getKnownPlatforms();
/* 164 */               DiskCacheEntry diskCacheEntry = null;
/*     */               try {
/* 166 */                 diskCacheEntry = DownloadProtocol.getCachedExtension(param1ExtensionDesc.getLocation(), param1ExtensionDesc.getVersion(), str);
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 171 */                 if (diskCacheEntry != null && diskCacheEntry.getFile() != null) {
/* 172 */                   LaunchDesc launchDesc = LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/* 173 */                   param1ExtensionDesc.setExtensionDesc(launchDesc);
/*     */                 } 
/* 175 */               } catch (Exception exception) {
/* 176 */                 Trace.ignoredException(exception);
/*     */               } 
/*     */             } 
/*     */             
/* 180 */             if (param1ExtensionDesc.getExtensionDesc() != null) {
/* 181 */               ResourcesDesc resourcesDesc = param1ExtensionDesc.getExtensionDesc().getResources();
/* 182 */               if (resourcesDesc != null) {
/* 183 */                 resourcesDesc.addJarsToList(this.val$list, hashSet, this.val$includeAll, this.val$includeEager);
/*     */               }
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {
/* 193 */             if (param1JREDesc.isSelected()) {
/*     */               
/* 195 */               ResourcesDesc resourcesDesc = param1JREDesc.getNestedResources();
/* 196 */               if (resourcesDesc != null) {
/* 197 */                 resourcesDesc.addJarsToList(this.val$list, this.val$includeParts, this.val$includeAll, this.val$includeEager);
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 204 */               if (param1JREDesc.getExtensionDesc() != null) {
/* 205 */                 ResourcesDesc resourcesDesc1 = param1JREDesc.getExtensionDesc().getResources();
/* 206 */                 if (resourcesDesc1 != null) {
/* 207 */                   resourcesDesc1.addJarsToList(this.val$list, new HashSet(), this.val$includeAll, this.val$includeEager);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/*     */   }
/*     */ 
/*     */   
/*     */   public JARDesc[] getPartJars(String[] paramArrayOfString) {
/* 221 */     HashSet hashSet = new HashSet();
/* 222 */     for (byte b = 0; b < paramArrayOfString.length; ) { hashSet.add(paramArrayOfString[b]); b++; }
/* 223 */      ArrayList arrayList = new ArrayList();
/* 224 */     addJarsToList(arrayList, hashSet, false, false);
/* 225 */     return toJARDescArray(arrayList);
/*     */   }
/*     */ 
/*     */   
/*     */   public JARDesc[] getPartJars(String paramString) {
/* 230 */     return getPartJars(new String[] { paramString });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JARDesc[] getResource(URL paramURL, String paramString) {
/* 237 */     VersionString versionString = (paramString != null) ? new VersionString(paramString) : null;
/* 238 */     JARDesc[] arrayOfJARDesc = new JARDesc[1];
/*     */     
/* 240 */     visit(new ResourceVisitor(this, paramURL, versionString, arrayOfJARDesc) { private final URL val$location; private final VersionString val$vs; private final JARDesc[] val$resources; private final ResourcesDesc this$0;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {
/* 242 */             if (URLUtil.equals(param1JARDesc.getLocation(), this.val$location))
/* 243 */               if (this.val$vs == null) {
/* 244 */                 this.val$resources[0] = param1JARDesc;
/* 245 */               } else if (this.val$vs.contains(param1JARDesc.getVersion())) {
/* 246 */                 this.val$resources[0] = param1JARDesc;
/*     */               }  
/*     */           }
/*     */           
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {}
/*     */           
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/* 258 */     if (arrayOfJARDesc[0] == null) return null;
/*     */     
/* 260 */     if (arrayOfJARDesc[0].getPartName() != null) {
/* 261 */       return getPartJars(arrayOfJARDesc[0].getPartName());
/*     */     }
/*     */     
/* 264 */     return arrayOfJARDesc;
/*     */   }
/*     */ 
/*     */   
/*     */   public JARDesc[] getExtensionPart(URL paramURL, String paramString, String[] paramArrayOfString) {
/* 269 */     ExtensionDesc extensionDesc = findExtension(paramURL, paramString);
/* 270 */     if (extensionDesc == null) return null; 
/* 271 */     ResourcesDesc resourcesDesc = extensionDesc.getExtensionResources();
/* 272 */     if (resourcesDesc == null) return null; 
/* 273 */     return resourcesDesc.getPartJars(paramArrayOfString);
/*     */   }
/*     */   
/*     */   private ExtensionDesc findExtension(URL paramURL, String paramString) {
/* 277 */     ExtensionDesc[] arrayOfExtensionDesc = new ExtensionDesc[1];
/*     */     
/* 279 */     visit(new ResourceVisitor(this, arrayOfExtensionDesc, paramURL, paramString) { private final ExtensionDesc[] val$ea; private final URL val$location; private final String val$version; private final ResourcesDesc this$0;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {}
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/* 284 */             if (this.val$ea[0] == null)
/* 285 */               if (URLUtil.equals(param1ExtensionDesc.getLocation(), this.val$location) && (this.val$version == null || (new VersionString(this.val$version)).contains(param1ExtensionDesc.getVersion()))) {
/*     */                 
/* 287 */                 this.val$ea[0] = param1ExtensionDesc;
/*     */               } else {
/*     */                 
/* 290 */                 LaunchDesc launchDesc = param1ExtensionDesc.getExtensionDesc();
/* 291 */                 if (launchDesc != null && launchDesc.getResources() != null)
/* 292 */                   this.val$ea[0] = launchDesc.getResources().findExtension(this.val$location, this.val$version); 
/*     */               }  
/*     */           }
/*     */           
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/* 300 */     return arrayOfExtensionDesc[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JARDesc getMainJar(boolean paramBoolean) {
/* 307 */     JARDesc[] arrayOfJARDesc = new JARDesc[2];
/* 308 */     visit(new ResourceVisitor(this, arrayOfJARDesc) { private final JARDesc[] val$results; private final ResourcesDesc this$0;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {
/* 310 */             if (param1JARDesc.isJavaFile()) {
/*     */               
/* 312 */               if (this.val$results[0] == null) this.val$results[0] = param1JARDesc;
/*     */               
/* 314 */               if (param1JARDesc.isMainJarFile()) this.val$results[1] = param1JARDesc; 
/*     */             } 
/*     */           }
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {}
/*     */           
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/* 324 */     JARDesc jARDesc1 = arrayOfJARDesc[0];
/* 325 */     JARDesc jARDesc2 = arrayOfJARDesc[1];
/* 326 */     return (jARDesc2 != null && paramBoolean) ? jARDesc2 : jARDesc1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JARDesc[] getPart(String paramString) {
/* 333 */     ArrayList arrayList = new ArrayList();
/* 334 */     visit(new ResourceVisitor(this, paramString, arrayList) { private final String val$name; private final ArrayList val$l; private final ResourcesDesc this$0;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {
/* 336 */             if (this.val$name.equals(param1JARDesc.getPartName()))
/* 337 */               this.val$l.add(param1JARDesc); 
/*     */           }
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {}
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/* 345 */     return toJARDescArray(arrayList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JARDesc[] getExtensionPart(URL paramURL, String paramString1, String paramString2) {
/* 352 */     JARDesc[][] arrayOfJARDesc = new JARDesc[1][];
/* 353 */     visit(new ResourceVisitor(this, paramURL, paramString1, arrayOfJARDesc, paramString2) { private final URL val$url; private final String val$version;
/*     */           private final JARDesc[][] val$jdss;
/*     */           
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/* 357 */             if (URLUtil.equals(param1ExtensionDesc.getLocation(), this.val$url))
/* 358 */               if (this.val$version == null) {
/* 359 */                 if (param1ExtensionDesc.getVersion() == null && param1ExtensionDesc.getExtensionResources() != null) {
/* 360 */                   this.val$jdss[0] = param1ExtensionDesc.getExtensionResources().getPart(this.val$part);
/*     */                 }
/* 362 */               } else if (this.val$version.equals(param1ExtensionDesc.getVersion()) && 
/* 363 */                 param1ExtensionDesc.getExtensionResources() != null) {
/* 364 */                 this.val$jdss[0] = param1ExtensionDesc.getExtensionResources().getPart(this.val$part);
/*     */               }  
/*     */           } private final String val$part; private final ResourcesDesc this$0;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {}
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/* 372 */     return arrayOfJARDesc[0];
/*     */   }
/*     */ 
/*     */   
/*     */   private JARDesc[] toJARDescArray(ArrayList paramArrayList) {
/* 377 */     JARDesc[] arrayOfJARDesc = new JARDesc[paramArrayList.size()];
/* 378 */     return (JARDesc[])paramArrayList.toArray((Object[])arrayOfJARDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getResourceProperties() {
/* 386 */     Properties properties = new Properties();
/* 387 */     visit(new ResourceVisitor(this, properties) { private final Properties val$props; private final ResourcesDesc this$0;
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {
/* 389 */             this.val$props.setProperty(param1PropertyDesc.getKey(), param1PropertyDesc.getValue());
/*     */           }
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {}
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {}
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */       );
/* 397 */     return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class PackageInformation
/*     */   {
/*     */     private LaunchDesc _launchDesc;
/*     */     
/*     */     private String _part;
/*     */     
/*     */     PackageInformation(LaunchDesc param1LaunchDesc, String param1String) {
/* 408 */       this._launchDesc = param1LaunchDesc;
/* 409 */       this._part = param1String;
/*     */     }
/*     */     
/* 412 */     public LaunchDesc getLaunchDesc() { return this._launchDesc; } public String getPart() {
/* 413 */       return this._part;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageInformation getPackageInformation(String paramString) {
/* 421 */     paramString = paramString.replace('/', '.');
/* 422 */     if (paramString.endsWith(".class")) paramString = paramString.substring(0, paramString.length() - 6);
/*     */     
/* 424 */     return visitPackageElements(getParent(), paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPackagePart(String paramString) {
/* 432 */     boolean[] arrayOfBoolean = { false };
/*     */     
/* 434 */     visit(new ResourceVisitor(this, arrayOfBoolean, paramString) { private final boolean[] val$result; private final String val$part; private final ResourcesDesc this$0;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {}
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/* 441 */             if (!param1ExtensionDesc.isInstaller()) {
/* 442 */               LaunchDesc launchDesc = param1ExtensionDesc.getExtensionDesc();
/* 443 */               if (!this.val$result[0] && launchDesc.isLibrary() && launchDesc.getResources() != null)
/*     */               {
/* 445 */                 this.val$result[0] = launchDesc.getResources().isPackagePart(this.val$part);
/*     */               }
/*     */             } 
/*     */           }
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {
/* 451 */             if (param1PackageDesc.getPart().equals(this.val$part)) {
/* 452 */               this.val$result[0] = true;
/*     */             }
/*     */           } }
/*     */       );
/* 456 */     return arrayOfBoolean[0];
/*     */   }
/*     */   
/*     */   private static PackageInformation visitPackageElements(LaunchDesc paramLaunchDesc, String paramString) {
/* 460 */     PackageInformation[] arrayOfPackageInformation = new PackageInformation[1];
/*     */     
/* 462 */     paramLaunchDesc.getResources().visit(new ResourceVisitor(arrayOfPackageInformation, paramString, paramLaunchDesc) { private final ResourcesDesc.PackageInformation[] val$result; private final String val$name; private final LaunchDesc val$ld;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {}
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */           
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/* 469 */             if (!param1ExtensionDesc.isInstaller()) {
/* 470 */               LaunchDesc launchDesc = param1ExtensionDesc.getExtensionDesc();
/* 471 */               if (this.val$result[0] == null && launchDesc.isLibrary() && launchDesc.getResources() != null) {
/* 472 */                 this.val$result[0] = ResourcesDesc.visitPackageElements(launchDesc, this.val$name);
/*     */               }
/*     */             } 
/*     */           }
/*     */           
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {
/* 478 */             if (this.val$result[0] == null && param1PackageDesc.match(this.val$name)) {
/* 479 */               this.val$result[0] = new ResourcesDesc.PackageInformation(this.val$ld, param1PackageDesc.getPart());
/*     */             }
/*     */           } }
/*     */       );
/* 483 */     return arrayOfPackageInformation[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(ResourceVisitor paramResourceVisitor) {
/* 488 */     for (byte b = 0; b < this._list.size(); b++) {
/* 489 */       ResourceType resourceType = this._list.get(b);
/* 490 */       resourceType.visit(paramResourceVisitor);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLNode asXML() {
/* 496 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("resources", null);
/* 497 */     for (byte b = 0; b < this._list.size(); b++) {
/* 498 */       ResourceType resourceType = this._list.get(b);
/* 499 */       xMLNodeBuilder.add(resourceType);
/*     */     } 
/* 501 */     return xMLNodeBuilder.getNode();
/*     */   }
/*     */   
/*     */   public void addNested(ResourcesDesc paramResourcesDesc) {
/* 505 */     if (paramResourcesDesc != null) paramResourcesDesc.visit(new ResourceVisitor(this) { private final ResourcesDesc this$0;
/* 506 */             public void visitJARDesc(JARDesc param1JARDesc) { this.this$0._list.add(param1JARDesc); }
/* 507 */             public void visitPropertyDesc(PropertyDesc param1PropertyDesc) { this.this$0._list.add(param1PropertyDesc); } public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/* 508 */               this.this$0._list.add(param1ExtensionDesc);
/*     */             }
/*     */             
/*     */             public void visitJREDesc(JREDesc param1JREDesc) {}
/*     */             
/*     */             public void visitPackageDesc(PackageDesc param1PackageDesc) {} }
/*     */         ); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\ResourcesDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */