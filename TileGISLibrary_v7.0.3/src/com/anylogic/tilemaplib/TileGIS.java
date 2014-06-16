/*   1:    */ package com.anylogic.tilemaplib;
/*   2:    */ 
/*   3:    */ import com.xj.anylogic.engine.Agent;
import com.xj.anylogic.engine.AgentArrayList;
import com.xj.anylogic.engine.Engine;
import com.xj.anylogic.engine.presentation.Panel;
import com.xj.anylogic.engine.presentation.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Map;

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
/*  29:    */ public class TileGIS
/*  30:    */   extends Agent
/*  31:    */ {
/*  32:    */   public ShapeRadioButtonGroup placeholder;
/*  33:    */   private TileMapPanel mapPanel;
/*  34:    */   
/*  35:    */   public ShapeRadioButtonGroup _placeholder_DefaultValue_xjal()
/*  36:    */   {
/*  37: 68 */     TileGIS self = this;
/*  38: 69 */     return null;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void set_placeholder(ShapeRadioButtonGroup placeholder)
/*  42:    */   {
/*  43: 73 */     if (placeholder == this.placeholder) {
/*  44: 74 */       return;
/*  45:    */     }
/*  46: 76 */     ShapeRadioButtonGroup _oldValue_xjal = this.placeholder;
/*  47: 77 */     this.placeholder = placeholder;
/*  48: 78 */     onChange_placeholder_xjal(_oldValue_xjal);
/*  49: 79 */     onChange();
/*  50:    */   }
/*  51:    */   
/*  52:    */   void onChange_placeholder()
/*  53:    */   {
/*  54: 89 */     onChange_placeholder_xjal(this.placeholder);
/*  55:    */   }
/*  56:    */   
/*  57:    */   void onChange_placeholder_xjal(ShapeRadioButtonGroup oldValue) {}
/*  58:    */   
/*  59:    */   public void onClick(double lat, double lon)
/*  60:    */   {
/*  61: 98 */     TileGIS self = this;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setParametersToDefaultValues()
/*  65:    */   {
/*  66:104 */     super.setParametersToDefaultValues();
/*  67:105 */     this.placeholder = _placeholder_DefaultValue_xjal();
/*  68:    */   }
/*  69:    */   
/*  70:    */   void setImageProvider(TileImageProvider tip)
/*  71:    */   {
/*  72:118 */     this.mapPanel.setTileImageProvider(tip);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void removeAllAgents()
/*  76:    */   {
/*  77:125 */     this.mapPanel.removeAllAgents();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void removeAgent(Agent a)
/*  81:    */   {
/*  82:132 */     this.mapPanel.removeAgent(a);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void addAgent(Agent a)
/*  86:    */   {
/*  87:139 */     this.mapPanel.addAgent(a);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void removeAllOverlayObj()
/*  91:    */   {
/*  92:146 */     this.mapPanel.removeAllOverlayObj();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void removeOverlayObj(IOverlay a)
/*  96:    */   {
/*  97:153 */     this.mapPanel.removeOverlayObj(a);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void addOverlayObj(IOverlay a)
/* 101:    */   {
/* 102:160 */     this.mapPanel.addOverlayObj(a);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setStaticOverlayVisible(boolean vis)
/* 106:    */   {
/* 107:167 */     this.mapPanel.showStaticOverlay = vis;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setDynamicOverlayVisible(boolean vis)
/* 111:    */   {
/* 112:174 */     this.mapPanel.showDynamicOverlay = vis;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void zoomToBounds(double ulLon, double ulLat, double lrLon, double lrLat)
/* 116:    */   {
/* 117:182 */     int x_min = 2147483647;
/* 118:183 */     int y_min = 2147483647;
/* 119:184 */     int x_max = -2147483648;
/* 120:185 */     int y_max = -2147483648;
/* 121:186 */     int mapZoomMax = 20;
/* 122:    */
/* 123:188 */     x_max = Math.max(x_max, MercatorProj.LonToX(lrLon, mapZoomMax));
/* 124:189 */     x_max = Math.max(x_max, MercatorProj.LonToX(ulLon, mapZoomMax));
/* 125:190 */     y_max = Math.max(y_max, MercatorProj.LatToY(ulLat, mapZoomMax));
/* 126:191 */     y_max = Math.max(y_max, MercatorProj.LatToY(lrLat, mapZoomMax));
/* 127:192 */     x_min = Math.min(x_min, MercatorProj.LonToX(ulLon, mapZoomMax));
/* 128:193 */     x_min = Math.min(x_min, MercatorProj.LonToX(lrLon, mapZoomMax));
/* 129:194 */     y_min = Math.min(y_min, MercatorProj.LatToY(ulLat, mapZoomMax));
/* 130:195 */     y_min = Math.min(y_min, MercatorProj.LatToY(lrLat, mapZoomMax));
/* 131:    */
/* 132:    */
/* 133:    */
/* 134:199 */     int height = Math.max(0, this.mapPanel.getHeight());
/* 135:200 */     int width = Math.max(0, this.mapPanel.getWidth());
/* 136:201 */     int newZoom = mapZoomMax;
/* 137:202 */     int x = x_max - x_min;
/* 138:203 */     int y = y_max - y_min;
/* 139:204 */     while ((x > width) || (y > height))
/* 140:    */     {
/* 141:205 */       newZoom--;
/* 142:206 */       x >>= 1;
/* 143:207 */       y >>= 1;
/* 144:    */     }
/* 145:209 */     x = x_min + (x_max - x_min) / 2;
/* 146:210 */     y = y_min + (y_max - y_min) / 2;
/* 147:211 */     int z = 1 << mapZoomMax - newZoom;
/* 148:212 */     x /= z;
/* 149:213 */     y /= z;
/* 150:214 */     this.mapPanel.setZoom(new Point(x, y), newZoom);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public double[] setMapBounds(double ulLon, double ulLat, double lrLon, double lrLat)
/* 154:    */   {
/* 117:182 */     int x_min = 2147483647;
/* 118:183 */     int y_min = 2147483647;
/* 119:184 */     int x_max = -2147483648;
/* 120:185 */     int y_max = -2147483648;
/* 121:186 */     int mapZoomMax = 20;
/* 122:    */
/* 123:188 */     x_max = Math.max(x_max, MercatorProj.LonToX(lrLon, mapZoomMax));
/* 124:189 */     x_max = Math.max(x_max, MercatorProj.LonToX(ulLon, mapZoomMax));
/* 125:190 */     y_max = Math.max(y_max, MercatorProj.LatToY(ulLat, mapZoomMax));
/* 126:191 */     y_max = Math.max(y_max, MercatorProj.LatToY(lrLat, mapZoomMax));
/* 127:192 */     x_min = Math.min(x_min, MercatorProj.LonToX(ulLon, mapZoomMax));
/* 128:193 */     x_min = Math.min(x_min, MercatorProj.LonToX(lrLon, mapZoomMax));
/* 129:194 */     y_min = Math.min(y_min, MercatorProj.LatToY(ulLat, mapZoomMax));
/* 130:195 */     y_min = Math.min(y_min, MercatorProj.LatToY(lrLat, mapZoomMax));
 /*                 x_max= (int)(Math.max(ulLon,lrLon)*1000000);
                  x_min= (int)(Math.min(ulLon,lrLon)*1000000);
                  y_max= (int)(Math.max(ulLat,lrLat)*1000000);
                  y_min= (int)(Math.min(ulLat,lrLat)*1000000);*/
/* 134:199 */     int height = Math.max(0, this.mapPanel.getHeight());
/* 135:200 */     int width = Math.max(0, this.mapPanel.getWidth());
/* 136:201 */     int newZoom = mapZoomMax;
/* 137:202 */     int x = x_max - x_min;
/* 138:203 */     int y = y_max - y_min;
/* 139:204 */     while ((x > width) || (y > height))
/* 140:    */     {
/* 141:205 */       newZoom--;
/* 142:206 */       x >>= 1;
/* 143:207 */       y >>= 1;
/* 144:    */     }
/* 145:209 */     x = x_min + (x_max - x_min)/2;
/* 146:210 */     y = y_min + (y_max - y_min)/2;
/* 147:211 */     int z = 1 << mapZoomMax - newZoom;
/* 148:212 */     x /= z;
/* 149:213 */     y /= z;
 /*                 int Cx=256;
                  int Cy=256;
                  //Cx>>=(newZoom);
                  //Cy>>=(newZoom);
                  double x1=((x*(width/2))/Cx);
                  double y1=((y*(height/2))/Cy);
                  x=(int) x1;
                  y=(int) y1;
                  x >>=(newZoom-this.mapPanel.zoom);
                  y >>=(newZoom-this.mapPanel.zoom);
                  //x = x+156;
                  //y = y-137;*/
    x=x/10000;
    y=y/10000;
                 /* 150:214 */     this.mapPanel.setZoom(new Point((int)x, (int)y), newZoom);
                 double[] res = { this.mapPanel.zoom, this.mapPanel.centerX, this.mapPanel.centerY,x,y, newZoom, z };
                traceln(Arrays.toString(res));
    traceln( x_max+","+x_min+","+y_max+","+y_min+",x:"+x+",y:"+y+",z:"+z+",nZomm:"+newZoom);
                 // this.mapPanel.getBounds().getX()setBounds( (int)ulLon,(int)ulLat, (int)width,(int)height);
                 return res;

/* 167:    */   }

                 public void setZoom(int centerX, int centerY, int newZoom)
                 {
                     this.mapPanel.setZoom(new Point((int)centerX, (int)centerY), newZoom);

                 }

                public void RestoreMap(int x,int y,int z)
                {
                    this.mapPanel.restoreMap(x,y,z);
                }

                public double[] getMapZoom()
                {
                    double[] res = {this.mapPanel.centerX, this.mapPanel.centerY, this.mapPanel.zoom};
                    traceln(Arrays.toString(res));
                    return res;
                }



    /* 153:    */   public double[] getMapBounds()
/* 154:    */   {
/* 155:222 */     int zoom = this.mapPanel.zoom;
/* 156:223 */     int ulX = this.mapPanel.centerX - this.mapPanel.getWidth() / 2;
/* 157:224 */     int ulY = this.mapPanel.centerY - this.mapPanel.getHeight() / 2;
/* 158:225 */     int lrX = this.mapPanel.centerX + this.mapPanel.getWidth() / 2;
/* 159:226 */     int lrY = this.mapPanel.centerY + this.mapPanel.getHeight() / 2;
/* 160:227 */     double ulLon = MercatorProj.XtoLon(ulX, zoom);
/* 161:228 */     double ulLat = MercatorProj.YtoLat(ulY, zoom);
/* 162:229 */     double lrLon = MercatorProj.XtoLon(lrX, zoom);
/* 163:230 */     double lrLat = MercatorProj.YtoLat(lrY, zoom);
/* 164:231 */     double[] res = { ulLon, ulLat, lrLon, lrLat};
/* 165:232 */     traceln(Arrays.toString(res));
/* 166:233 */     return res;
/* 167:    */   }


/* 168:    */   
/* 169:    */   public void addAgent(String layer, Agent a)
/* 170:    */   {
/* 171:240 */     this.mapPanel.addAgent(layer, a);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void removeAgent(String layer, Agent a)
/* 175:    */   {
/* 176:247 */     this.mapPanel.removeAgent(layer, a);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void removeAllAgents(String layer)
/* 180:    */   {
/* 181:254 */     this.mapPanel.removeAllAgents(layer);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void removeAllOverlayObj(String layer)
/* 185:    */   {
/* 186:261 */     this.mapPanel.removeAllOverlayObj(layer);
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void removeOverlayObj(String layer, IOverlay a)
/* 190:    */   {
/* 191:268 */     this.mapPanel.removeOverlayObj(layer, a);
/* 192:    */   }
/* 193:    */   
/* 194:    */   public void addOverlayObj(String layer, IOverlay a)
/* 195:    */   {
/* 196:275 */     this.mapPanel.addOverlayObj(layer, a);
/* 197:    */   }
/* 198:    */   
/* 199:278 */   public ViewArea _origin_VA = new ViewArea(this, "[Origin]", 0.0D, 0.0D, ViewArea.Alignment.CENTER, ViewArea.Scaling.NONE, 1.0D, 100.0D, 100.0D);
/* 200:    */   static final int _presentation = 0;
/* 201:    */   static final int _icon = -1;
/* 202:    */   ShapeTopLevelPresentationGroup presentation;
/* 203:    */   ShapeGroup icon;
/* 204:    */   private static final long serialVersionUID = 1386332165932L;
/* 205:    */   
/* 206:    */   public int getViewAreas(Map<String, ViewArea> _output)
/* 207:    */   {
/* 208:281 */     if (_output != null) {
/* 209:282 */       _output.put("_origin_VA", this._origin_VA);
/* 210:    */     }
/* 211:284 */     return 1 + super.getViewAreas(_output);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public Object getPersistentShape(int _shape)
/* 215:    */   {
/* 216:302 */     switch (_shape)
/* 217:    */     {
/* 218:    */     case 0: 
/* 219:303 */       return this.presentation;
/* 220:    */     case -1: 
/* 221:304 */       return this.icon;
/* 222:    */     }
/* 223:306 */     return null;
/* 224:    */   }
/* 225:    */   
/* 226:    */   private void drawModelElements_Parameters_xjal(Panel _panel, Graphics2D _g, boolean _publicOnly)
/* 227:    */   {
/* 228:311 */     if (!_publicOnly) {
/* 229:312 */       drawParameter(_panel, _g, 20, 30, 10, 0, "placeholder", this.placeholder, 0);
/* 230:    */     }
/* 231:314 */     if (!_publicOnly) {
/* 232:315 */       drawParameter(_panel, _g, 200, 90, 10, 0, "onClick", null, 4096);
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   private void drawModelElements_PlainVariables_xjal(Panel _panel, Graphics2D _g, boolean _publicOnly)
/* 237:    */   {
/* 238:320 */     if (!_publicOnly) {
/* 239:321 */       drawPlainVariable(_panel, _g, 20, 50, 10, 0, "mapPanel", this.mapPanel, false);
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   private void drawModelElements_Functions_xjal(Panel _panel, Graphics2D _g, boolean _publicOnly)
/* 244:    */   {
/* 245:326 */     if (!_publicOnly) {
/* 246:327 */       drawFunction(_panel, _g, 20, 70, 10, 0, "setImageProvider");
/* 247:    */     }
/* 248:329 */     if (!_publicOnly) {
/* 249:330 */       drawFunction(_panel, _g, 200, 70, 10, 0, "removeAllAgents");
/* 250:    */     }
/* 251:332 */     if (!_publicOnly) {
/* 252:333 */       drawFunction(_panel, _g, 200, 50, 10, 0, "removeAgent");
/* 253:    */     }
/* 254:335 */     if (!_publicOnly) {
/* 255:336 */       drawFunction(_panel, _g, 200, 30, 10, 0, "addAgent");
/* 256:    */     }
/* 257:338 */     if (!_publicOnly) {
/* 258:339 */       drawFunction(_panel, _g, 330, 70, 10, 0, "removeAllOverlayObj");
/* 259:    */     }
/* 260:341 */     if (!_publicOnly) {
/* 261:342 */       drawFunction(_panel, _g, 330, 50, 10, 0, "removeOverlayObj");
/* 262:    */     }
/* 263:344 */     if (!_publicOnly) {
/* 264:345 */       drawFunction(_panel, _g, 330, 30, 10, 0, "addOverlayObj");
/* 265:    */     }
/* 266:347 */     if (!_publicOnly) {
/* 267:348 */       drawFunction(_panel, _g, 20, 100, 10, 0, "setStaticOverlayVisible");
/* 268:    */     }
/* 269:350 */     if (!_publicOnly) {
/* 270:351 */       drawFunction(_panel, _g, 20, 120, 10, 0, "setDynamicOverlayVisible");
/* 271:    */     }
/* 272:353 */     if (!_publicOnly) {
/* 273:354 */       drawFunction(_panel, _g, 20, 180, 10, 0, "zoomToBounds");
/* 274:    */     }
/* 275:356 */     if (!_publicOnly) {
/* 276:357 */       drawFunction(_panel, _g, 20, 160, 10, 0, "getMapBounds");
/* 277:    */     }
/* 278:359 */     if (!_publicOnly) {
/* 279:360 */       drawFunction(_panel, _g, 200, 130, 10, 0, "addAgent");
/* 280:    */     }
/* 281:362 */     if (!_publicOnly) {
/* 282:363 */       drawFunction(_panel, _g, 200, 150, 10, 0, "removeAgent");
/* 283:    */     }
/* 284:365 */     if (!_publicOnly) {
/* 285:366 */       drawFunction(_panel, _g, 200, 170, 10, 0, "removeAllAgents");
/* 286:    */     }
/* 287:368 */     if (!_publicOnly) {
/* 288:369 */       drawFunction(_panel, _g, 330, 130, 10, 0, "removeAllOverlayObj");
/* 289:    */     }
/* 290:371 */     if (!_publicOnly) {
/* 291:372 */       drawFunction(_panel, _g, 330, 150, 10, 0, "removeOverlayObj");
/* 292:    */     }
/* 293:374 */     if (!_publicOnly) {
/* 294:375 */       drawFunction(_panel, _g, 330, 170, 10, 0, "addOverlayObj");
/* 295:    */     }
/* 296:    */   }
/* 297:    */   
/* 298:    */   public void drawModelElements(Panel _panel, Graphics2D _g, boolean _publicOnly)
/* 299:    */   {
/* 300:381 */     drawModelElements_Parameters_xjal(_panel, _g, _publicOnly);
/* 301:382 */     drawModelElements_PlainVariables_xjal(_panel, _g, _publicOnly);
/* 302:383 */     drawModelElements_Functions_xjal(_panel, _g, _publicOnly);
/* 303:    */   }
/* 304:    */   
/* 305:    */   private boolean onClickModelAt_Parameters_xjal(Panel _panel, double _x, double _y, int _clickCount, boolean _publicOnly)
/* 306:    */   {
/* 307:388 */     if ((!_publicOnly) && (modelElementContains(_x, _y, 20.0D, 30.0D)))
/* 308:    */     {
/* 309:389 */       _panel.addInspect(20.0D, 30.0D, this, "placeholder");
/* 310:390 */       return true;
/* 311:    */     }
/* 312:392 */     return false;
/* 313:    */   }
/* 314:    */   
/* 315:    */   private boolean onClickModelAt_PlainVariables_xjal(Panel _panel, double _x, double _y, int _clickCount, boolean _publicOnly)
/* 316:    */   {
/* 317:396 */     if ((!_publicOnly) && (modelElementContains(_x, _y, 20.0D, 50.0D)))
/* 318:    */     {
/* 319:397 */       _panel.addInspect(20.0D, 50.0D, this, "mapPanel");
/* 320:398 */       return true;
/* 321:    */     }
/* 322:400 */     return false;
/* 323:    */   }
/* 324:    */   
/* 325:    */   public boolean onClickModelAt(Panel _panel, double _x, double _y, int _clickCount, boolean _publicOnly)
/* 326:    */   {
/* 327:405 */     if (onClickModelAt_Parameters_xjal(_panel, _x, _y, _clickCount, _publicOnly)) {
/* 328:405 */       return true;
/* 329:    */     }
/* 330:406 */     if (onClickModelAt_PlainVariables_xjal(_panel, _x, _y, _clickCount, _publicOnly)) {
/* 331:406 */       return true;
/* 332:    */     }
/* 333:407 */     return false;
/* 334:    */   }
/* 335:    */   
/* 336:    */   public TileGIS(Engine engine, Agent owner, AgentArrayList<? extends TileGIS> collection)
/* 337:    */   {
/* 338:416 */     super(engine, owner, collection);
/* 339:    */   }
/* 340:    */   
/* 341:    */   public void create()
/* 342:    */   {
/* 343:422 */     setupPlainVariables_xjal();
/* 344:    */     
/* 345:424 */     this.presentation = new ShapeTopLevelPresentationGroup(this, true, 0.0D, 0.0D, 0.0D, 0.0D, new Object[0]);
/* 346:425 */     this.icon = new ShapeGroup(this, true, 0.0D, 0.0D, 0.0D, new Object[0]);
/* 347:    */     
/* 348:    */ 
/* 349:428 */     assignInitialConditions_xjal();
/* 350:429 */     onCreate();
/* 351:    */   }
/* 352:    */   
/* 353:    */   public void start()
/* 354:    */   {
/* 355:434 */     onStartup();
/* 356:    */   }
/* 357:    */   
/* 358:    */   public void onStartup()
/* 359:    */   {
/* 360:438 */     super.onStartup();
/* 361:    */     
/* 362:440 */     this.mapPanel.addMouseListener(new MouseAdapter()
/* 363:    */     {
/* 364:    */       public void mouseClicked(MouseEvent e)
/* 365:    */       {
/* 366:443 */         if (e.getButton() == 1)
/* 367:    */         {
/* 368:444 */           Point p = e.getPoint();
/* 369:445 */           if ((TileGIS.this.mapPanel.courtesyBounds != null) && (TileGIS.this.mapPanel.courtesyBounds.contains(p)))
/* 370:    */           {
/* 371:446 */             TileGIS.this.getPresentation().openWebSite(TileGIS.this.mapPanel.tip.courtesyLnk);
/* 372:    */           }
/* 373:447 */           else if ((TileGIS.this.mapPanel.courtesyImgBounds != null) && (TileGIS.this.mapPanel.courtesyImgBounds.contains(p)))
/* 374:    */           {
/* 375:448 */             TileGIS.this.getPresentation().openWebSite(TileGIS.this.mapPanel.tip.courtesyLnk);
/* 376:    */           }
/* 377:    */           else
/* 378:    */           {
/* 379:451 */             Point mP = TileGIS.this.mapPanel.panelToMap(p);
/* 380:452 */             double lon = MercatorProj.XtoLon(mP.x, TileGIS.this.mapPanel.zoom);
/* 381:453 */             double lat = MercatorProj.YtoLat(mP.y, TileGIS.this.mapPanel.zoom);
/* 382:454 */             TileGIS.this.onClick(lat, lon);
/* 383:    */           }
/* 384:    */         }
/* 385:    */       }
/* 386:458 */     });
/* 387:459 */     getPresentation().getPanel().addContainerListener(new ContainerListener()
/* 388:    */     {
/* 389:    */       public void componentAdded(ContainerEvent e)
/* 390:    */       {
/* 391:461 */         if (e.getChild() == TileGIS.this.placeholder.getJComponent()) {
/* 392:462 */           TileGIS.this.getPresentation().getPanel().add(TileGIS.this.mapPanel);
/* 393:    */         }
/* 394:    */       }
/* 395:    */       
/* 396:    */       public void componentRemoved(ContainerEvent e)
/* 397:    */       {
/* 398:467 */         if (e.getChild() == TileGIS.this.placeholder.getJComponent()) {
/* 399:468 */           TileGIS.this.getPresentation().getPanel().remove(TileGIS.this.mapPanel);
/* 400:    */         }
/* 401:    */       }
/* 402:472 */     });
/* 403:473 */     this.placeholder.getJComponent().addComponentListener(new ComponentListener()
/* 404:    */     {
/* 405:    */       public void componentShown(ComponentEvent e) {}
/* 406:    */       
/* 407:    */       public void componentHidden(ComponentEvent e) {}
/* 408:    */       
/* 409:    */       public void componentResized(ComponentEvent e)
/* 410:    */       {
/* 411:478 */         TileGIS.this.mapPanel.setSize(TileGIS.this.placeholder.getJComponent().getSize());
/* 412:    */       }
/* 413:    */       
/* 414:    */       public void componentMoved(ComponentEvent e)
/* 415:    */       {
/* 416:482 */         TileGIS.this.mapPanel.setLocation(TileGIS.this.placeholder.getJComponent().getLocation());
/* 417:    */       }
/* 418:    */     });
/* 419:    */   }
/* 420:    */   
/* 421:    */   public void setupPlainVariables_xjal()
/* 422:    */   {
/* 423:492 */     this.mapPanel = 
/* 424:493 */       new TileMapPanel(new TileImageProvider());
/* 425:    */   }
/* 426:    */   
/* 427:    */   private void writeObject(ObjectOutputStream _stream)
/* 428:    */     throws IOException
/* 429:    */   {
/* 430:503 */     _stream.defaultWriteObject();
/* 431:504 */     writeCustomData(_stream);
/* 432:    */   }
/* 433:    */   
/* 434:    */   private void readObject(ObjectInputStream _stream)
/* 435:    */     throws IOException, ClassNotFoundException
/* 436:    */   {
/* 437:508 */     _stream.defaultReadObject();
/* 438:509 */     this._origin_VA.restoreOwner(this);
/* 439:510 */     //finishReadObject_xjal(_stream, TileGIS.class);
/* 440:    */   }
/* 441:    */ }



/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\

 * Qualified Name:     com.anylogic.tilemaplib.TileGIS

 * JD-Core Version:    0.7.0.1

 */