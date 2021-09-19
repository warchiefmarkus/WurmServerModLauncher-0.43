/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.container.Album;
/*     */ import org.fourthline.cling.support.model.container.Container;
/*     */ import org.fourthline.cling.support.model.container.GenreContainer;
/*     */ import org.fourthline.cling.support.model.container.MovieGenre;
/*     */ import org.fourthline.cling.support.model.container.MusicAlbum;
/*     */ import org.fourthline.cling.support.model.container.MusicArtist;
/*     */ import org.fourthline.cling.support.model.container.MusicGenre;
/*     */ import org.fourthline.cling.support.model.container.PersonContainer;
/*     */ import org.fourthline.cling.support.model.container.PhotoAlbum;
/*     */ import org.fourthline.cling.support.model.container.PlaylistContainer;
/*     */ import org.fourthline.cling.support.model.container.StorageFolder;
/*     */ import org.fourthline.cling.support.model.container.StorageSystem;
/*     */ import org.fourthline.cling.support.model.container.StorageVolume;
/*     */ import org.fourthline.cling.support.model.item.AudioBook;
/*     */ import org.fourthline.cling.support.model.item.AudioBroadcast;
/*     */ import org.fourthline.cling.support.model.item.AudioItem;
/*     */ import org.fourthline.cling.support.model.item.ImageItem;
/*     */ import org.fourthline.cling.support.model.item.Item;
/*     */ import org.fourthline.cling.support.model.item.Movie;
/*     */ import org.fourthline.cling.support.model.item.MusicTrack;
/*     */ import org.fourthline.cling.support.model.item.MusicVideoClip;
/*     */ import org.fourthline.cling.support.model.item.Photo;
/*     */ import org.fourthline.cling.support.model.item.PlaylistItem;
/*     */ import org.fourthline.cling.support.model.item.TextItem;
/*     */ import org.fourthline.cling.support.model.item.VideoBroadcast;
/*     */ import org.fourthline.cling.support.model.item.VideoItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DIDLContent
/*     */ {
/*     */   public static final String NAMESPACE_URI = "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/";
/*     */   public static final String DESC_WRAPPER_NAMESPACE_URI = "urn:fourthline-org:cling:support:content-directory-desc-1-0";
/*  56 */   protected List<Container> containers = new ArrayList<>();
/*  57 */   protected List<Item> items = new ArrayList<>();
/*  58 */   protected List<DescMeta> descMetadata = new ArrayList<>();
/*     */   
/*     */   public Container getFirstContainer() {
/*  61 */     return getContainers().get(0);
/*     */   }
/*     */   
/*     */   public DIDLContent addContainer(Container container) {
/*  65 */     getContainers().add(container);
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public List<Container> getContainers() {
/*  70 */     return this.containers;
/*     */   }
/*     */   
/*     */   public void setContainers(List<Container> containers) {
/*  74 */     this.containers = containers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DIDLContent addObject(Object object) {
/*  81 */     if (object instanceof Item) {
/*  82 */       addItem((Item)object);
/*  83 */     } else if (object instanceof Container) {
/*  84 */       addContainer((Container)object);
/*     */     } 
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public DIDLContent addItem(Item item) {
/*  90 */     getItems().add(item);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public List<Item> getItems() {
/*  95 */     return this.items;
/*     */   }
/*     */   
/*     */   public void setItems(List<Item> items) {
/*  99 */     this.items = items;
/*     */   }
/*     */   
/*     */   public DIDLContent addDescMetadata(DescMeta descMetadata) {
/* 103 */     getDescMetadata().add(descMetadata);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public List<DescMeta> getDescMetadata() {
/* 108 */     return this.descMetadata;
/*     */   }
/*     */   
/*     */   public void setDescMetadata(List<DescMeta> descMetadata) {
/* 112 */     this.descMetadata = descMetadata;
/*     */   }
/*     */   
/*     */   public void replaceGenericContainerAndItems() {
/* 116 */     setItems(replaceGenericItems(getItems()));
/* 117 */     setContainers(replaceGenericContainers(getContainers()));
/*     */   }
/*     */   
/*     */   protected List<Item> replaceGenericItems(List<Item> genericItems) {
/* 121 */     List<Item> specificItems = new ArrayList<>();
/*     */     
/* 123 */     for (Item genericItem : genericItems) {
/* 124 */       String genericType = genericItem.getClazz().getValue();
/*     */       
/* 126 */       if (AudioItem.CLASS.getValue().equals(genericType)) {
/* 127 */         specificItems.add(new AudioItem(genericItem)); continue;
/* 128 */       }  if (MusicTrack.CLASS.getValue().equals(genericType)) {
/* 129 */         specificItems.add(new MusicTrack(genericItem)); continue;
/* 130 */       }  if (AudioBook.CLASS.getValue().equals(genericType)) {
/* 131 */         specificItems.add(new AudioBook(genericItem)); continue;
/* 132 */       }  if (AudioBroadcast.CLASS.getValue().equals(genericType)) {
/* 133 */         specificItems.add(new AudioBroadcast(genericItem)); continue;
/*     */       } 
/* 135 */       if (VideoItem.CLASS.getValue().equals(genericType)) {
/* 136 */         specificItems.add(new VideoItem(genericItem)); continue;
/* 137 */       }  if (Movie.CLASS.getValue().equals(genericType)) {
/* 138 */         specificItems.add(new Movie(genericItem)); continue;
/* 139 */       }  if (VideoBroadcast.CLASS.getValue().equals(genericType)) {
/* 140 */         specificItems.add(new VideoBroadcast(genericItem)); continue;
/* 141 */       }  if (MusicVideoClip.CLASS.getValue().equals(genericType)) {
/* 142 */         specificItems.add(new MusicVideoClip(genericItem)); continue;
/*     */       } 
/* 144 */       if (ImageItem.CLASS.getValue().equals(genericType)) {
/* 145 */         specificItems.add(new ImageItem(genericItem)); continue;
/* 146 */       }  if (Photo.CLASS.getValue().equals(genericType)) {
/* 147 */         specificItems.add(new Photo(genericItem)); continue;
/*     */       } 
/* 149 */       if (PlaylistItem.CLASS.getValue().equals(genericType)) {
/* 150 */         specificItems.add(new PlaylistItem(genericItem)); continue;
/*     */       } 
/* 152 */       if (TextItem.CLASS.getValue().equals(genericType)) {
/* 153 */         specificItems.add(new TextItem(genericItem));
/*     */         continue;
/*     */       } 
/* 156 */       specificItems.add(genericItem);
/*     */     } 
/*     */ 
/*     */     
/* 160 */     return specificItems;
/*     */   }
/*     */   
/*     */   protected List<Container> replaceGenericContainers(List<Container> genericContainers) {
/* 164 */     List<Container> specificContainers = new ArrayList<>();
/*     */     
/* 166 */     for (Container genericContainer : genericContainers) {
/* 167 */       Container specific; String genericType = genericContainer.getClazz().getValue();
/*     */ 
/*     */ 
/*     */       
/* 171 */       if (Album.CLASS.getValue().equals(genericType)) {
/* 172 */         Album album = new Album(genericContainer);
/*     */       }
/* 174 */       else if (MusicAlbum.CLASS.getValue().equals(genericType)) {
/* 175 */         MusicAlbum musicAlbum = new MusicAlbum(genericContainer);
/*     */       }
/* 177 */       else if (PhotoAlbum.CLASS.getValue().equals(genericType)) {
/* 178 */         PhotoAlbum photoAlbum = new PhotoAlbum(genericContainer);
/*     */       }
/* 180 */       else if (GenreContainer.CLASS.getValue().equals(genericType)) {
/* 181 */         GenreContainer genreContainer = new GenreContainer(genericContainer);
/*     */       }
/* 183 */       else if (MusicGenre.CLASS.getValue().equals(genericType)) {
/* 184 */         MusicGenre musicGenre = new MusicGenre(genericContainer);
/*     */       }
/* 186 */       else if (MovieGenre.CLASS.getValue().equals(genericType)) {
/* 187 */         MovieGenre movieGenre = new MovieGenre(genericContainer);
/*     */       }
/* 189 */       else if (PlaylistContainer.CLASS.getValue().equals(genericType)) {
/* 190 */         PlaylistContainer playlistContainer = new PlaylistContainer(genericContainer);
/*     */       }
/* 192 */       else if (PersonContainer.CLASS.getValue().equals(genericType)) {
/* 193 */         PersonContainer personContainer = new PersonContainer(genericContainer);
/*     */       }
/* 195 */       else if (MusicArtist.CLASS.getValue().equals(genericType)) {
/* 196 */         MusicArtist musicArtist = new MusicArtist(genericContainer);
/*     */       }
/* 198 */       else if (StorageSystem.CLASS.getValue().equals(genericType)) {
/* 199 */         StorageSystem storageSystem = new StorageSystem(genericContainer);
/*     */       }
/* 201 */       else if (StorageVolume.CLASS.getValue().equals(genericType)) {
/* 202 */         StorageVolume storageVolume = new StorageVolume(genericContainer);
/*     */       }
/* 204 */       else if (StorageFolder.CLASS.getValue().equals(genericType)) {
/* 205 */         StorageFolder storageFolder = new StorageFolder(genericContainer);
/*     */       } else {
/*     */         
/* 208 */         specific = genericContainer;
/*     */       } 
/*     */       
/* 211 */       specific.setItems(replaceGenericItems(genericContainer.getItems()));
/* 212 */       specificContainers.add(specific);
/*     */     } 
/*     */     
/* 215 */     return specificContainers;
/*     */   }
/*     */   
/*     */   public long getCount() {
/* 219 */     return (this.items.size() + this.containers.size());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\DIDLContent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */