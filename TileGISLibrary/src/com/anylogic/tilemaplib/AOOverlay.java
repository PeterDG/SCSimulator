package com.anylogic.tilemaplib;

import com.xj.anylogic.engine.ActiveObject;
import com.xj.anylogic.engine.presentation.Shape;
import com.xj.anylogic.engine.presentation.ShapeGroup;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.List;

public class AOOverlay
        implements IOverlay, Serializable
{
    ActiveObject ao;
    private static final long serialVersionUID = 1L;

    public AOOverlay(ActiveObject ao)
    {
        this.ao = ao;
    }

    public double getLon()
    {
        return 0.0D;
    }

    public double getLat()
    {
        return 0.0D;
    }

    public boolean isVisible()
    {
        return true;
    }

    public void draw(Graphics g, TileMapPanel tmp)
    {
        int x = MercatorProj.LonToX(getLon(), tmp.zoom);
        int y = MercatorProj.LatToY(getLat(), tmp.zoom);
        x = x - tmp.centerX + tmp.getWidth() / 2;
        y = y - tmp.centerY + tmp.getHeight() / 2;
        if ((x < 0) || (y < 0) || (x > tmp.getWidth()) || (y > tmp.getHeight())) {
            return;
        }
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform xform = g2d.getTransform();
        AffineTransform af = new AffineTransform();
        af.translate(x, y);

        g2d.transform(af);
        ShapeGroup s = (ShapeGroup)this.ao.getPersistentShape(0);
        List shapes = s.getShapes();
        for (Object o : shapes) {
            ((Shape)o).draw(this.ao.getPresentation().getPanel(), g2d, null, true);
        }
        g2d.setTransform(xform);
    }

    public String toString()
    {
        return super.toString();
    }
}
