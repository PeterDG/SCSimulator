/*   1:    */ package com.anylogic.tilemaplib;
/*   2:    */ 
/*   3:    */ import com.xj.anylogic.engine.AgentContinuousGIS;
import com.xj.anylogic.engine.presentation.Shape;
import com.xj.anylogic.engine.presentation.ShapeGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.*;
import java.util.List;

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
/*  26:    */
/*  27:    */
/*  28:    */
/*  29:    */ 
/*  30:    */ class TileMapPanel
/*  31:    */   extends JPanel
/*  32:    */   implements Serializable
/*  33:    */ {
/*  34:    */   private static final String DEFAULT_LAYER = "DEFAULT_LAYER";
/*  35: 61 */   int centerX = 256;
/*  36: 62 */   int centerY = 256;
/*  37: 63 */   int zoom = 1;
/*  38:    */   TileImageProvider tip;
/*  39: 65 */   boolean scrollWrapEnabled = true;
/*  40: 66 */   public static final Font font = new Font("Arial", 0, 10);
/*  41:    */   Rectangle courtesyBounds;
/*  42:    */   Rectangle courtesyImgBounds;
/*  43:    */   Rectangle copyrightBounds;
/*  44: 71 */   private boolean needRedraw = true;
/*  45:    */   BufferedImage cachedMapBuffer;
/*  46: 74 */   Map<String, MapLayer> layers = new HashMap();
/*  47: 75 */   List<MapLayer> staticLayers = new ArrayList();
/*  48: 76 */   List<MapLayer> dynamicLayers = new ArrayList();
/*  49: 78 */   public boolean showStaticOverlay = true;
/*  50: 79 */   public boolean showDynamicOverlay = true;
/*  51:    */   
/*  52:    */   public void addAgent(String layerName, AgentContinuousGIS a)
/*  53:    */   {
/*  54: 82 */     MapLayer oml = (MapLayer)this.layers.get(layerName);
/*  55: 83 */     if (oml == null) {
/*  56: 84 */       return;
/*  57:    */     }
/*  58: 85 */     oml.addAgent(a);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void removeAgent(String layerName, AgentContinuousGIS a)
/*  62:    */   {
/*  63: 89 */     MapLayer oml = (MapLayer)this.layers.get(layerName);
/*  64: 90 */     if (oml == null) {
/*  65: 91 */       return;
/*  66:    */     }
/*  67: 92 */     oml.removeAgent(a);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void removeAllAgents(String layerName)
/*  71:    */   {
/*  72: 96 */     MapLayer oml = (MapLayer)this.layers.get(layerName);
/*  73: 97 */     if (oml == null) {
/*  74: 98 */       return;
/*  75:    */     }
/*  76: 99 */     oml.removeAllAgents();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void addAgent(AgentContinuousGIS a)
/*  80:    */   {
/*  81:103 */     addAgent(getDefaultLayer().name, a);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void removeAgent(AgentContinuousGIS a)
/*  85:    */   {
/*  86:107 */     removeAgent(getDefaultLayer().name, a);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void removeAllAgents()
/*  90:    */   {
/*  91:111 */     removeAllAgents(getDefaultLayer().name);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void addOverlayObj(String layerName, IOverlay a)
/*  95:    */   {
/*  96:115 */     MapLayer oml = (MapLayer)this.layers.get(layerName);
/*  97:116 */     if (oml == null) {
/*  98:117 */       return;
/*  99:    */     }
/* 100:118 */     oml.addOverlayObj(a);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void removeOverlayObj(String layerName, IOverlay a)
/* 104:    */   {
/* 105:122 */     MapLayer oml = (MapLayer)this.layers.get(layerName);
/* 106:123 */     if (oml == null) {
/* 107:124 */       return;
/* 108:    */     }
/* 109:125 */     oml.removeOverlayObj(a);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void removeAllOverlayObj(String layerName)
/* 113:    */   {
/* 114:129 */     MapLayer oml = (MapLayer)this.layers.get(layerName);
/* 115:130 */     if (oml == null) {
/* 116:131 */       return;
/* 117:    */     }
/* 118:132 */     oml.removeAllOverlayObj();
/* 119:    */   }
/* 120:    */   
/* 121:    */   private MapLayer getDefaultLayer()
/* 122:    */   {
/* 123:137 */     MapLayer oml = (MapLayer)this.layers.get("DEFAULT_LAYER");
/* 124:138 */     if (oml == null)
/* 125:    */     {
/* 126:139 */       oml = new MapLayer("DEFAULT_LAYER", true, this);
/* 127:140 */       this.layers.put("DEFAULT_LAYER", oml);
/* 128:141 */       this.dynamicLayers.add(oml);
/* 129:    */     }
/* 130:143 */     return oml;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void addLayer(MapLayer ml)
/* 134:    */   {
/* 135:147 */     this.layers.put(ml.name, ml);
/* 136:148 */     if (ml.dynamic) {
/* 137:149 */       synchronized (this.dynamicLayers)
/* 138:    */       {
/* 139:150 */         this.dynamicLayers.add(ml);
/* 140:    */       }
/* 141:    */     }
/* 142:153 */     synchronized (this.staticLayers)
/* 143:    */     {
/* 144:154 */       this.staticLayers.add(ml);
/* 145:    */     }
/* 146:157 */     ml.hasChanged();
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void addOverlayObj(IOverlay a)
/* 150:    */   {
/* 151:161 */     addOverlayObj(getDefaultLayer().name, a);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void removeOverlayObj(IOverlay a)
/* 155:    */   {
/* 156:165 */     removeOverlayObj(getDefaultLayer().name, a);
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void removeAllOverlayObj()
/* 160:    */   {
/* 161:169 */     removeAllOverlayObj(getDefaultLayer().name);
/* 162:    */   }
/* 163:    */   
/* 164:    */   protected void setTileImageProvider(TileImageProvider tip)
/* 165:    */   {
/* 166:172 */     if (tip == null) {
/* 167:173 */       return;
/* 168:    */     }
/* 169:174 */     if (this.tip != null)
/* 170:    */     {
/* 171:175 */       this.tip.cancelLoading();
/* 172:176 */       this.tip = tip;
/* 173:177 */       repaint();
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:181 */   MouseAdapter mwl = new MouseAdapter()
/* 178:    */   {
/* 179:    */     private Point lastDragPoint;
/* 180:    */     
/* 181:    */     public void mouseClicked(MouseEvent e) {}
/* 182:    */     
/* 183:    */     public void mouseReleased(MouseEvent e)
/* 184:    */     {
/* 185:188 */       this.lastDragPoint = null;
/* 186:    */     }
/* 187:    */     
/* 188:    */     public void mouseDragged(MouseEvent e)
/* 189:    */     {
/* 190:193 */       Point p = e.getPoint();
/* 191:194 */       if (this.lastDragPoint != null)
/* 192:    */       {
/* 193:195 */         int diffx = this.lastDragPoint.x - p.x;
/* 194:196 */         int diffy = this.lastDragPoint.y - p.y;
/* 195:197 */         TileMapPanel.this.moveMap(diffx, diffy);
/* 196:198 */         TileMapPanel.this.needRedraw = true;
/* 197:    */       }
/* 198:200 */       this.lastDragPoint = p;
/* 199:    */     }
/* 200:    */     
/* 201:    */     public void mouseWheelMoved(MouseWheelEvent e)
/* 202:    */     {
/* 203:204 */       Point p = e.getPoint();
/* 204:205 */       TileMapPanel.this.setZoom(p, TileMapPanel.this.zoom - e.getWheelRotation());
/* 205:206 */       TileMapPanel.this.needRedraw = true;
/* 206:    */     }
/* 207:    */   };
/* 208:    */   private static final long serialVersionUID = 1L;
/* 209:    */   
/* 210:    */   public TileMapPanel(TileImageProvider tip)
/* 211:    */   {
/* 212:216 */     this.tip = tip;
/* 213:217 */     addMouseWheelListener(this.mwl);
/* 214:218 */     addMouseMotionListener(this.mwl);
/* 215:219 */     addMouseListener(this.mwl);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public Point panelToMap(Point p)
/* 219:    */   {
/* 220:225 */     return new Point(p.x + this.centerX - getWidth() / 2, p.y + this.centerY - getHeight() / 2);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public void moveMap(int x, int y)
/* 224:    */   {
/* 225:229 */     this.tip.cancelLoading();
/* 226:230 */     this.centerX += x;
/* 227:231 */     this.centerY += y;
/* 228:232 */     repaint();
/* 229:    */   }

  public void restoreMap(int x, int y,int z)
  {
    this.tip.cancelLoading();
    this.centerX = x;
    this.centerY = y;
    this.zoom=z;
    repaint();
  }

/* 230:    */   
/* 231:    */   public void setZoom(int zoom)
/* 232:    */   {
/* 233:236 */     Point p = new Point(this.centerX, this.centerY);
/* 234:237 */     setZoom(p, zoom);
/* 235:    */   }
/* 236:    */   
/* 237:    */   public void setZoom(Point p, int zoom)
/* 238:    */   {
/* 239:241 */     if ((zoom > 25) || (zoom < 0) || (zoom == this.zoom)) {
/* 240:242 */       return;
/* 241:    */     }
/* 242:243 */     this.tip.cancelLoading();
/* 243:244 */     int x = this.centerX - getWidth() / 2 + p.x;
/* 244:245 */     int y = this.centerY - getHeight() / 2 + p.y;
/* 245:    */     
/* 246:247 */     double lon = MercatorProj.XtoLon(x, this.zoom);
/* 247:248 */     double lat = MercatorProj.YtoLat(y, this.zoom);
/* 248:249 */     this.centerX = (MercatorProj.LonToX(lon, zoom) - p.x + getWidth() / 2);
/* 249:250 */     this.centerY = (MercatorProj.LatToY(lat, zoom) - p.y + getHeight() / 2);
/* 250:251 */     this.zoom = zoom;
/* 251:    */   }
/* 252:    */   
/* 253:    */   protected void paintComponent(Graphics g2)
/* 254:    */   {
/* 255:257 */     super.paintComponent(g2);
/* 256:258 */     if ((this.needRedraw) || (this.cachedMapBuffer == null) || (this.tip.tileNamesInProgress.size() > 0))
/* 257:    */     {
/* 258:260 */       this.cachedMapBuffer = new BufferedImage(getWidth(), getHeight(), 1);
/* 259:261 */       Graphics g = this.cachedMapBuffer.getGraphics();
/* 260:262 */       int w2 = getWidth() / 2;
/* 261:263 */       int h2 = getHeight() / 2;
/* 262:    */       
/* 263:265 */       int tlx = this.centerX - w2;
/* 264:266 */       int tly = this.centerY - h2;
/* 265:267 */       int lrx = this.centerX + w2;
/* 266:268 */       int lry = this.centerY + h2;
/* 267:    */       
/* 268:    */ 
/* 269:271 */       int leftVisTile = Math.max(0, tlx / 256);
/* 270:272 */       int rightVisTile = Math.min(lrx / 256 + 1, 1 << this.zoom);
/* 271:273 */       int topVisTile = Math.max(0, tly / 256);
/* 272:274 */       int bottomVisTile = Math.min(lry / 256 + 1, 1 << this.zoom);
/* 273:276 */       for (int x = leftVisTile; x < rightVisTile; x++) {
/* 274:277 */         for (int y = topVisTile; y < bottomVisTile; y++)
/* 275:    */         {
/* 276:278 */           Image tile = this.tip.getImage(x, y, this.zoom);
/* 277:279 */           if (tile != null) {
/* 278:280 */             g.drawImage(tile, x * 256 - tlx, y * 256 - tly, null);
/* 279:    */           }
/* 280:    */         }
/* 281:    */       }
/* 282:284 */       g.drawRect(leftVisTile * 256 - tlx, topVisTile * 256 - tly, (rightVisTile - leftVisTile) * 256, (bottomVisTile - topVisTile) * 256);
/* 283:    */       
/* 284:    */ 
/* 285:287 */       String coutesyStr = this.tip.courtesy;
/* 286:288 */       int termsTextHeight = 0;
/* 287:289 */       int termsTextY = getWidth();
/* 288:291 */       if (coutesyStr != null)
/* 289:    */       {
/* 290:292 */         Rectangle2D termsStringBounds = g.getFontMetrics().getStringBounds(coutesyStr, g);
/* 291:293 */         int textRealHeight = (int)termsStringBounds.getHeight();
/* 292:294 */         termsTextHeight = textRealHeight - 5;
/* 293:295 */         int termsTextWidth = (int)termsStringBounds.getWidth();
/* 294:296 */         termsTextY = getHeight() - termsTextHeight;
/* 295:297 */         int x = 2;
/* 296:298 */         int y = getHeight() - termsTextHeight;
/* 297:299 */         this.courtesyBounds = new Rectangle(x, y - termsTextHeight, termsTextWidth, textRealHeight);
/* 298:300 */         g.setColor(Color.black);
/* 299:301 */         g.drawString(coutesyStr, x + 1, y + 1);
/* 300:    */       }
/* 301:    */       else
/* 302:    */       {
/* 303:303 */         this.courtesyBounds = null;
/* 304:    */       }
/* 305:306 */       Image courtesyImage = this.tip.getCourtesyImage();
/* 306:307 */       if (courtesyImage != null)
/* 307:    */       {
/* 308:308 */         int x = 2;
/* 309:309 */         int imgWidth = courtesyImage.getWidth(this);
/* 310:310 */         int imgHeight = courtesyImage.getHeight(this);
/* 311:311 */         int y = termsTextY - imgHeight - termsTextHeight - 5;
/* 312:312 */         this.courtesyImgBounds = new Rectangle(x, y, imgWidth, imgHeight);
/* 313:313 */         g.drawImage(courtesyImage, x, y, null);
/* 314:    */       }
/* 315:    */       else
/* 316:    */       {
/* 317:315 */         this.courtesyImgBounds = null;
/* 318:    */       }
/* 319:318 */       g.setFont(font);
/* 320:    */       int textHeight;
/* 321:319 */       if (this.tip.copyrightString != null)
/* 322:    */       {
/* 323:320 */         Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(this.tip.copyrightString, g);
/* 324:321 */         textHeight = (int)stringBounds.getHeight() - 5;
/* 325:322 */         int x = getWidth() - (int)stringBounds.getWidth();
/* 326:323 */         int y = getHeight() - textHeight;
/* 327:324 */         g.setColor(Color.black);
/* 328:325 */         g.drawString(this.tip.copyrightString, x + 1, y + 1);
/* 329:326 */         this.copyrightBounds = new Rectangle(x, y - textHeight, (int)stringBounds.getWidth(), (int)stringBounds.getHeight());
/* 330:    */       }
/* 331:    */       else
/* 332:    */       {
/* 333:328 */         this.copyrightBounds = null;
/* 334:    */       }
/* 335:332 */       for (MapLayer oml : this.staticLayers) {
/* 336:333 */         if (oml.isVisible()) {
/* 337:334 */           oml.draw(g);
/* 338:    */         }
/* 339:    */       }
/* 340:    */     }
/* 341:341 */     g2.drawImage(this.cachedMapBuffer, 0, 0, null);
/* 342:342 */     paintOverlay(g2);
/* 343:    */   }
/* 344:    */   
/* 345:    */   private void paintOverlay(Graphics g)
/* 346:    */   {
/* 347:351 */     int w2 = getWidth() / 2;
/* 348:352 */     int h2 = getHeight() / 2;
/* 349:    */     
/* 350:354 */     int tlx = this.centerX - w2;
/* 351:355 */     int tly = this.centerY - h2;
/* 352:356 */     int lrx = this.centerX + w2;
/* 353:357 */     int lry = this.centerY + h2;
/* 354:370 */     if (this.showDynamicOverlay) {
/* 355:371 */       synchronized (this.dynamicLayers)
/* 356:    */       {
/* 357:372 */         for (MapLayer oml : this.dynamicLayers) {
/* 358:373 */           if (oml.isVisible()) {
/* 359:374 */             oml.draw(g);
/* 360:    */           }
/* 361:    */         }
/* 362:    */       }
/* 363:    */     }
/* 364:    */   }
/* 365:    */
/* 366:    */   private class MapLayer
/* 367:    */   {
/* 368:    */     TileMapPanel tmp;
/* 369:    */     String name;
/* 370:    */     boolean dynamic;
/* 371:393 */     boolean visible = true;
/* 372:395 */     public List<IOverlay> overlayObjs = new ArrayList();
/* 373:396 */     public List<AgentContinuousGIS> overlayAgents = new ArrayList();
/* 374:397 */     List<AgentContinuousGIS> toRemoveAgents = new ArrayList();
/* 375:    */     
/* 376:    */     public MapLayer(String name, boolean dynamic, TileMapPanel tmp)
/* 377:    */     {
/* 378:400 */       this.tmp = tmp;
/* 379:401 */       this.name = name;
/* 380:402 */       this.dynamic = dynamic;
/* 381:    */     }
/* 382:    */     
/* 383:    */     public boolean isVisible()
/* 384:    */     {
/* 385:406 */       return this.visible;
/* 386:    */     }
/* 387:    */     
/* 388:    */     public void setVisible(boolean visible)
/* 389:    */     {
/* 390:410 */       this.visible = visible;
/* 391:411 */       hasChanged();
/* 392:    */     }
/* 393:    */     
/* 394:    */     public void hasChanged()
/* 395:    */     {
/* 396:414 */       this.tmp.repaint();
/* 397:    */     }
/* 398:    */     
/* 399:    */     public void addOverlayObj(IOverlay a)
/* 400:    */     {
/* 401:417 */       synchronized (this.overlayObjs)
/* 402:    */       {
/* 403:417 */         this.overlayObjs.add(a);
/* 404:    */       }
/* 405:418 */       hasChanged();
/* 406:    */     }
/* 407:    */     
/* 408:    */     public void removeOverlayObj(IOverlay a)
/* 409:    */     {
/* 410:422 */       synchronized (this.overlayObjs)
/* 411:    */       {
/* 412:422 */         this.overlayObjs.remove(a);
/* 413:    */       }
/* 414:423 */       hasChanged();
/* 415:    */     }
/* 416:    */     
/* 417:    */     public void removeAllOverlayObj()
/* 418:    */     {
/* 419:427 */       synchronized (this.overlayObjs)
/* 420:    */       {
/* 421:427 */         this.overlayObjs.clear();
/* 422:    */       }
/* 423:428 */       hasChanged();
/* 424:    */     }
/* 425:    */     
/* 426:    */     public void addAgent(AgentContinuousGIS a)
/* 427:    */     {
/* 428:432 */       synchronized (this.overlayAgents)
/* 429:    */       {
/* 430:433 */         this.overlayAgents.add(a);
/* 431:    */       }
/* 432:    */     }
/* 433:    */     
/* 434:    */     public void removeAgent(AgentContinuousGIS a)
/* 435:    */     {
/* 436:438 */       synchronized (this.overlayAgents)
/* 437:    */       {
/* 438:440 */         if (!this.overlayAgents.remove(a)) {
/* 439:441 */           this.toRemoveAgents.add(a);
/* 440:    */         }
/* 441:    */       }
/* 442:445 */       synchronized (this.overlayAgents)
/* 443:    */       {
/* 444:447 */         Iterator<AgentContinuousGIS> it = this.toRemoveAgents.iterator();
/* 445:448 */         while (it.hasNext())
/* 446:    */         {
/* 447:449 */           AgentContinuousGIS aR = (AgentContinuousGIS)it.next();
/* 448:450 */           if (this.overlayAgents.remove(aR)) {
/* 449:451 */             it.remove();
/* 450:    */           }
/* 451:    */         }
/* 452:    */       }
/* 453:    */     }
/* 454:    */     
/* 455:    */     public void removeAllAgents()
/* 456:    */     {
/* 457:459 */       synchronized (this.overlayAgents)
/* 458:    */       {
/* 459:460 */         this.overlayAgents.clear();
/* 460:    */       }
/* 461:    */     }
/* 462:    */     
/* 463:    */     public void draw(Graphics g)
/* 464:    */     {
/* 465:464 */       int w2 = this.tmp.getWidth() / 2;
/* 466:465 */       int h2 = this.tmp.getHeight() / 2;
/* 467:    */       
/* 468:467 */       int tlx = this.tmp.centerX - w2;
/* 469:468 */       int tly = this.tmp.centerY - h2;
/* 470:469 */       int lrx = this.tmp.centerX + w2;
/* 471:470 */       int lry = this.tmp.centerY + h2;
/* 472:473 */       synchronized (this.overlayObjs)
/* 473:    */       {
/* 474:474 */         for (IOverlay ovr : this.overlayObjs) {
/* 475:475 */           if (ovr.isVisible()) {
/* 476:476 */             ovr.draw(g, this.tmp);
/* 477:    */           }
/* 478:    */         }
/* 479:    */       }
/* 480:479 */       synchronized (this.overlayAgents)
/* 481:    */       {
/* 482:480 */         for (AgentContinuousGIS a : this.overlayAgents)
/* 483:    */         {
/* 484:482 */           int xMerc = MercatorProj.LonToX(a.getX(), TileMapPanel.this.zoom);
/* 485:483 */           int yMerc = MercatorProj.LatToY(a.getY(), TileMapPanel.this.zoom);
/* 486:484 */           int x = xMerc - tlx;
/* 487:485 */           int y = yMerc - tly;
/* 488:486 */           xMerc = MercatorProj.LonToX(a.getX(), 100);
/* 489:487 */           yMerc = MercatorProj.LatToY(a.getY(), 100);
/* 490:488 */           if ((x >= 0) && (y >= 0) && (x <= TileMapPanel.this.getWidth()) && (y <= TileMapPanel.this.getHeight()))
/* 491:    */           {
/* 492:491 */             Graphics2D g2d = (Graphics2D)g;
/* 493:492 */             AffineTransform xform = g2d.getTransform();
/* 494:493 */             AffineTransform af = new AffineTransform();
/* 495:494 */             af.translate(x, y);
/* 496:495 */             int xMerc1 = MercatorProj.LonToX(a.getTargetX(), 100);
/* 497:496 */             int yMerc1 = MercatorProj.LatToY(a.getTargetY(), 100);
/* 498:    */             
/* 499:    */ 
/* 500:    */ 
/* 501:500 */             af.rotate(a.getRotation());
/* 502:501 */             g2d.transform(af);
/* 503:502 */             ShapeGroup s = (ShapeGroup)a.getPersistentShape(0);
/* 504:503 */             List shapes = s.getShapes();
/* 505:504 */             for (Object o : shapes) {
/* 506:505 */               ((Shape)o).draw(a.getPresentation().getPanel(), g2d, null, true);
/* 507:    */             }
/* 508:507 */             g2d.setTransform(xform);
/* 509:    */           }
/* 510:    */         }
/* 511:    */       }
/* 512:    */     }
/* 513:    */   }
/* 514:    */ }



/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\

 * Qualified Name:     com.anylogic.tilemaplib.TileMapPanel

 * JD-Core Version:    0.7.0.1

 */