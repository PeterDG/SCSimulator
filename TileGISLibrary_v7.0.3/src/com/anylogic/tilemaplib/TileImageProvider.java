/*   1:    */ package com.anylogic.tilemaplib;
/*   2:    */ 
/*   3:    */ import com.xj.anylogic.engine.Utilities;
import com.xj.anylogic.engine.presentation.UtilitiesColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/*   4:    */
/*   5:    */
/*   6:    */
/*   7:    */
/*   8:    */
/*   9:    */
/*  10:    */
/*  11:    */
/*  12:    */
/*  13:    */
/*  14:    */
/*  15:    */
/*  16:    */
/*  17:    */
/*  18:    */
/*  19:    */
/*  20:    */
/*  21:    */
/*  22:    */
/*  23:    */
/*  24:    */
/*  25:    */ 
/*  26:    */ public class TileImageProvider
/*  27:    */   implements Serializable
/*  28:    */ {
/*  29: 60 */   public String courtesy = "Courtesy of MapQuest";
/*  30: 61 */   public String courtesyLnk = "http://www.mapquest.com/";
/*  31: 62 */   public String courtesyImgURL = "http://developer.mapquest.com/content/osm/mq_logo.png";
/*  32: 63 */   public String copyrightString = "© OpenStreetMap";
/*  33: 65 */   public Image courtesyImage = null;
/*  34: 66 */   private boolean loadingCourtesyImage = false;
/*  35: 68 */   private static String[] rand4L = { "a", "b", "c", "d" };
/*  36: 69 */   ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
/*  37: 70 */   Random random = new Random();
/*  38: 71 */   String sourceURL = "http://otile{rand4N}.mqcdn.com/tiles/1.0.0/osm/{zoom}/{x}/{y}.jpg";
/*  39: 72 */   private static String CACHE_DIR = "mapcache";
/*  40: 73 */   String sourceName = "mapquest";
/*  41: 75 */   Map<String, BufferedImage> mapTileCache = Collections.synchronizedMap(new LruCache(200));
/*  42: 76 */   Set<String> tileNamesInProgress = Collections.synchronizedSet(new HashSet());
/*  43: 77 */   Set<String> scaledTiles = new HashSet();
/*  44: 79 */   BufferedImage noTileImage = new BufferedImage(256, 256, 1);
/*  45: 80 */   BufferedImage loadingImage = new BufferedImage(256, 256, 1);
/*  46: 81 */   long lastRequest = 0L;
/*  47:    */   private static final long serialVersionUID = 1L;
/*  48:    */   
/*  49:    */   public TileImageProvider()
/*  50:    */   {
/*  51: 88 */     Graphics2D g2D = (Graphics2D)this.noTileImage.getGraphics();
/*  52: 89 */     g2D.setBackground(UtilitiesColor.white);
/*  53: 90 */     g2D.clearRect(0, 0, 256, 256);
/*  54: 91 */     g2D.setColor(UtilitiesColor.black);
/*  55: 92 */     g2D.drawOval(0, 0, 256, 256);
/*  56: 93 */     g2D.drawString("No tile", 128, 128);
/*  57: 94 */     g2D = (Graphics2D)this.loadingImage.getGraphics();
/*  58: 95 */     g2D.setBackground(UtilitiesColor.white);
/*  59: 96 */     g2D.setColor(UtilitiesColor.black);
/*  60: 97 */     g2D.clearRect(0, 0, 256, 256);
/*  61: 98 */     g2D.drawOval(0, 0, 256, 256);
/*  62: 99 */     g2D.drawString("Loading tile", 128, 128);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public TileImageProvider(String name, String url, String copyrightString, String courtesyString, String courtesyLink, String courtesyImgURL)
/*  66:    */   {
/*  67:113 */     this();
/*  68:114 */     this.sourceName = name;
/*  69:115 */     this.sourceURL = url;
/*  70:116 */     this.copyrightString = copyrightString;
/*  71:117 */     this.courtesy = courtesyString;
/*  72:118 */     this.courtesyImgURL = courtesyImgURL;
/*  73:    */   }
/*  74:    */   
/*  75:    */   private BufferedImage getLT()
/*  76:    */   {
/*  77:124 */     return this.loadingImage;
/*  78:    */   }
/*  79:    */   
/*  80:    */   private String getTileName(int x, int y, int z)
/*  81:    */   {
/*  82:147 */     return x + "_" + y + "_" + z;
/*  83:    */   }
/*  84:    */   
/*  85:    */   private String getTileURL(int x, int y, int z)
/*  86:    */   {
/*  87:151 */     String s = this.sourceURL.replace("{zoom}", Integer.toString(z));
/*  88:152 */     s = s.replace("{x}", Integer.toString(x));
/*  89:153 */     s = s.replace("{y}", Integer.toString(y));
/*  90:154 */     int r = Utilities.uniform_discr(1, 4, this.random);
/*  91:155 */     s = s.replace("{rand4N}", Integer.toString(r));
/*  92:156 */     s = s.replace("{rand4L}", rand4L[(r - 1)]);
/*  93:    */     
/*  94:    */ 
/*  95:159 */     return s;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected void cancelLoading()
/*  99:    */   {
/* 100:163 */     this.executor.getQueue().clear();
/* 101:164 */     this.executor.purge();
/* 102:165 */     this.tileNamesInProgress.clear();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Image getImage(int x, int y, int z)
/* 106:    */   {
/* 107:169 */     Image image = (Image)this.mapTileCache.get(z + "/" + x + "/" + y);
/* 108:170 */     if (image != null)
/* 109:    */     {
/* 110:171 */       if ((this.scaledTiles.contains(z + "/" + x + "/" + y)) && 
/* 111:172 */         (!this.tileNamesInProgress.contains(getTileName(x, y, z))))
/* 112:    */       {
/* 113:173 */         this.tileNamesInProgress.add(getTileName(x, y, z));
/* 114:174 */         this.executor.execute(new TileLoadProcessor(x, y, z));
/* 115:    */       }
/* 116:179 */       return image;
/* 117:    */     }
/* 118:182 */     if (!this.tileNamesInProgress.contains(getTileName(x, y, z)))
/* 119:    */     {
/* 120:183 */       this.tileNamesInProgress.add(getTileName(x, y, z));
/* 121:184 */       this.executor.execute(new TileLoadProcessor(x, y, z));
/* 122:    */     }
/* 123:189 */     BufferedImage imagePrev = (BufferedImage)this.mapTileCache.get(z - 1 + "/" + x / 2 + "/" + y / 2);
/* 124:190 */     if (imagePrev != null)
/* 125:    */     {
/* 126:191 */       int tileX = 128 * (x % 2);
/* 127:192 */       int tileY = 128 * (y % 2);
/* 128:    */       
/* 129:194 */       BufferedImage img = new BufferedImage(256, 256, 1);
/* 130:195 */       Graphics2D g2 = (Graphics2D)img.getGraphics();
/* 131:196 */       g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
/* 132:197 */         RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/* 133:198 */       g2.drawImage(imagePrev.getSubimage(tileX, tileY, 128, 128), 0, 0, 256, 256, null);
/* 134:    */       
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:203 */       this.mapTileCache.put(z + "/" + x + "/" + y, img);
/* 139:204 */       this.scaledTiles.add(z + "/" + x + "/" + y);
/* 140:    */       
/* 141:206 */       return img;
/* 142:    */     }
/* 143:208 */     return getLT();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Image getCourtesyImage()
/* 147:    */   {
/* 148:212 */     if (this.courtesyImage != null) {
/* 149:214 */       return this.courtesyImage;
/* 150:    */     }
/* 151:217 */     if (!this.loadingCourtesyImage)
/* 152:    */     {
/* 153:218 */       this.loadingCourtesyImage = true;
/* 154:219 */       this.executor.execute(new ImgLoadProcessor());
/* 155:    */     }
/* 156:221 */     return null;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public String toString()
/* 160:    */   {
/* 161:226 */     return super.toString();
/* 162:    */   }
/* 163:    */   
/* 164:    */   private class LruCache<A, B>
/* 165:    */     extends LinkedHashMap<A, B>
/* 166:    */   {
/* 167:    */     private final int maxEntries;
/* 168:    */     
/* 169:    */     public LruCache(int maxEntries)
/* 170:    */     {
/* 171:239 */       super(1,07, true);
/* 172:240 */       this.maxEntries = maxEntries;
/* 173:    */     }
/* 174:    */     
/* 175:    */     protected boolean removeEldestEntry(Map.Entry<A, B> eldest)
/* 176:    */     {
/* 177:245 */       return super.size() > this.maxEntries;
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   private class TileLoadProcessor
/* 182:    */     implements Runnable
/* 183:    */   {
/* 184:    */     int x;
/* 185:    */     int y;
/* 186:    */     int z;
/* 187:    */     
/* 188:    */     public TileLoadProcessor(int x, int y, int z)
/* 189:    */     {
/* 190:256 */       this.x = x;
/* 191:257 */       this.y = y;
/* 192:258 */       this.z = z;
/* 193:    */     }
/* 194:    */     
/* 195:    */     public void run()
/* 196:    */     {
/* 197:262 */       BufferedImage res = null;
/* 198:    */       
/* 199:264 */       File inputF = new File(TileImageProvider.CACHE_DIR + "/" + TileImageProvider.this.sourceName + "/" + this.x + "-" + this.y + "-" + this.z);
/* 200:    */       try
/* 201:    */       {
/* 202:266 */         if (inputF.exists())
/* 203:    */         {
/* 204:267 */           res = ImageIO.read(inputF);
/* 205:    */         }
/* 206:    */         else
/* 207:    */         {
/* 208:269 */           String urlS = TileImageProvider.this.getTileURL(this.x, this.y, this.z);
/* 209:270 */           URL url = new URL(urlS);
/* 210:271 */           URLConnection uConn = url.openConnection();
/* 211:272 */           uConn.addRequestProperty("Accept", "text/html, image/png, image/jpeg, image/gif");
/* 212:273 */           uConn.setReadTimeout(15000);
/* 213:    */           
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:    */ 
/* 219:280 */           String fileType = null;
/* 220:281 */           if ("image/jpeg".equals(uConn.getContentType()))
/* 221:    */           {
/* 222:282 */             fileType = "jpeg";
/* 223:    */           }
/* 224:283 */           else if ("image/png".equals(uConn.getContentType()))
/* 225:    */           {
/* 226:284 */             fileType = "png";
/* 227:    */           }
/* 228:285 */           else if ("image/gif".equals(uConn.getContentType()))
/* 229:    */           {
/* 230:286 */             fileType = "gif";
/* 231:    */           }
/* 232:    */           else
/* 233:    */           {
/* 234:287 */             if ("text/plain".equals(uConn.getContentType()))
/* 235:    */             {
/* 236:289 */               String resp = uConn.getHeaderField(0);
/* 237:290 */               if ((resp.startsWith("HTTP/1.1 404")) && 
/* 238:291 */                 (!TileImageProvider.this.mapTileCache.containsKey(this.z + "/" + this.x + "/" + this.y))) {
/* 239:292 */                 TileImageProvider.this.mapTileCache.put(this.z + "/" + this.x + "/" + this.y, TileImageProvider.this.noTileImage);
/* 240:    */               }
/* 241:294 */               TileImageProvider.this.tileNamesInProgress.remove(TileImageProvider.this.getTileName(this.x, this.y, this.z));
/* 242:295 */               return;
/* 243:    */             }
/* 244:299 */             if (!TileImageProvider.this.mapTileCache.containsKey(this.z + "/" + this.x + "/" + this.y)) {
/* 245:300 */               TileImageProvider.this.mapTileCache.put(this.z + "/" + this.x + "/" + this.y, TileImageProvider.this.noTileImage);
/* 246:    */             }
/* 247:302 */             TileImageProvider.this.tileNamesInProgress.remove(TileImageProvider.this.getTileName(this.x, this.y, this.z));
/* 248:303 */             return;
/* 249:    */           }
/* 250:306 */           InputStream is = uConn.getInputStream();
/* 251:307 */           res = ImageIO.read(is);
/* 252:308 */           TileImageProvider.this.executor.execute(new TileImageProvider.TileSaveProcessor(TileImageProvider.this, this.x, this.y, this.z, res, fileType));
/* 253:    */         }
/* 254:    */       }
/* 255:    */       catch (Exception e)
/* 256:    */       {
/* 257:311 */         e.printStackTrace();
/* 258:    */       }
/* 259:313 */       TileImageProvider.this.mapTileCache.put(this.z + "/" + this.x + "/" + this.y, res);
/* 260:314 */       TileImageProvider.this.tileNamesInProgress.remove(TileImageProvider.this.getTileName(this.x, this.y, this.z));
/* 261:315 */       TileImageProvider.this.scaledTiles.remove(this.z + "/" + this.x + "/" + this.y);
/* 262:    */     }
/* 263:    */   }
/* 264:    */   
/* 265:    */   private class TileSaveProcessor
/* 266:    */     implements Runnable
/* 267:    */   {
/* 268:    */     int x;
/* 269:    */     int y;
/* 270:    */     int z;
/* 271:    */     BufferedImage res;
/* 272:    */     String fileType;
/* 273:    */     
/* 274:    */     public TileSaveProcessor(TileImageProvider tileImageProvider, int x, int y, int z, BufferedImage res, String fileType)
/* 275:    */     {
/* 276:329 */       this.x = x;
/* 277:330 */       this.y = y;
/* 278:331 */       this.z = z;
/* 279:332 */       this.res = res;
/* 280:333 */       this.fileType = fileType;
/* 281:    */     }
/* 282:    */     
/* 283:    */     public void run()
/* 284:    */     {
/* 285:    */       try
/* 286:    */       {
/* 287:337 */         TileImageProvider.this.ensureDirExists();
/* 288:338 */         ImageIO.write(this.res, this.fileType, new File(TileImageProvider.CACHE_DIR + "/" + TileImageProvider.this.sourceName + "/" + this.x + "-" + this.y + "-" + this.z));
/* 289:    */       }
/* 290:    */       catch (Exception localException) {}
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   private class ImgLoadProcessor
/* 295:    */     implements Runnable
/* 296:    */   {
/* 297:    */     public ImgLoadProcessor() {}
/* 298:    */     
/* 299:    */     public void run()
/* 300:    */     {
/* 301:354 */       BufferedImage res = null;
/* 302:356 */       if (TileImageProvider.this.courtesyImgURL == null)
/* 303:    */       {
/* 304:357 */         TileImageProvider.this.courtesyImage = null;
/* 305:358 */         return;
/* 306:    */       }
/* 307:360 */       File inputF = new File(TileImageProvider.CACHE_DIR + "/" + TileImageProvider.this.sourceName + "/courtesy");
/* 308:    */       try
/* 309:    */       {
/* 310:362 */         if (inputF.exists())
/* 311:    */         {
/* 312:363 */           res = ImageIO.read(inputF);
/* 313:    */         }
/* 314:    */         else
/* 315:    */         {
/* 316:365 */           URL url = new URL(TileImageProvider.this.courtesyImgURL);
/* 317:366 */           URLConnection uConn = url.openConnection();
/* 318:367 */           uConn.addRequestProperty("Accept", "text/html, image/png, image/jpeg, image/gif");
/* 319:368 */           uConn.setReadTimeout(15000);
/* 320:    */           
/* 321:370 */           String fileType = null;
/* 322:371 */           if ("image/jpeg".equals(uConn.getContentType()))
/* 323:    */           {
/* 324:372 */             fileType = "jpeg";
/* 325:    */           }
/* 326:373 */           else if ("image/png".equals(uConn.getContentType()))
/* 327:    */           {
/* 328:374 */             fileType = "png";
/* 329:    */           }
/* 330:375 */           else if ("image/gif".equals(uConn.getContentType()))
/* 331:    */           {
/* 332:376 */             fileType = "gif";
/* 333:    */           }
/* 334:    */           else
/* 335:    */           {
/* 336:377 */             if ("text/plain".equals(uConn.getContentType()))
/* 337:    */             {
/* 338:379 */               String resp = uConn.getHeaderField(0);
/* 339:380 */               if (resp.startsWith("HTTP/1.1 404")) {
/* 340:381 */                 TileImageProvider.this.courtesyImage = new BufferedImage(10, 10, 1);
/* 341:    */               }
/* 342:382 */               TileImageProvider.this.loadingCourtesyImage = false;
/* 343:383 */               return;
/* 344:    */             }
/* 345:385 */             Utilities.traceln(uConn.getContentType());
/* 346:386 */             TileImageProvider.this.courtesyImage = new BufferedImage(10, 10, 1);
/* 347:387 */             TileImageProvider.this.loadingCourtesyImage = false;
/* 348:388 */             return;
/* 349:    */           }
/* 350:391 */           InputStream is = uConn.getInputStream();
/* 351:392 */           res = ImageIO.read(is);
/* 352:393 */           TileImageProvider.this.executor.execute(new TileImageProvider.ImgSaveProcessor(TileImageProvider.this, res, fileType));
/* 353:    */         }
/* 354:    */       }
/* 355:    */       catch (Exception e)
/* 356:    */       {
/* 357:396 */         e.printStackTrace();
/* 358:    */       }
/* 359:398 */       TileImageProvider.this.courtesyImage = res;
/* 360:399 */       TileImageProvider.this.loadingCourtesyImage = false;
/* 361:    */     }
/* 362:    */   }
/* 363:    */   
/* 364:    */   private class ImgSaveProcessor
/* 365:    */     implements Runnable
/* 366:    */   {
/* 367:    */     BufferedImage res;
/* 368:    */     String fileType;
/* 369:    */     
/* 370:    */     public ImgSaveProcessor(TileImageProvider tileImageProvider, BufferedImage res, String fileType)
/* 371:    */     {
/* 372:410 */       this.res = res;
/* 373:411 */       this.fileType = fileType;
/* 374:    */     }
/* 375:    */     
/* 376:    */     public void run()
/* 377:    */     {
/* 378:    */       try
/* 379:    */       {
/* 380:415 */         TileImageProvider.this.ensureDirExists();
/* 381:416 */         ImageIO.write(this.res, this.fileType, new File(TileImageProvider.CACHE_DIR + "/" + TileImageProvider.this.sourceName + "/" + "courtesy"));
/* 382:    */       }
/* 383:    */       catch (Exception localException) {}
/* 384:    */     }
/* 385:    */   }
/* 386:    */   
/* 387:    */   private File ensureDirExists()
/* 388:    */   {
/* 389:427 */     File f = new File(CACHE_DIR + "/" + this.sourceName);
/* 390:428 */     if (!f.exists()) {
/* 391:429 */       f.mkdirs();
/* 392:    */     }
/* 393:431 */     return f;
/* 394:    */   }
/* 395:    */ }



/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\

 * Qualified Name:     com.anylogic.tilemaplib.TileImageProvider

 * JD-Core Version:    0.7.0.1

 */