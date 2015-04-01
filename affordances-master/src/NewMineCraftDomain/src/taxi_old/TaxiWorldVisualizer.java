package taxi;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.visualizer.ObjectPainter;
import burlap.oomdp.visualizer.StaticPainter;
import burlap.oomdp.visualizer.Visualizer;

public class TaxiWorldVisualizer {
    public static Visualizer getVisualizer() {
        TaxiWorldDomain txd = new TaxiWorldDomain();
        Domain d = txd.generateDomain();
        Visualizer v = new Visualizer();

        v.addStaticPainter(new RoomsMapPainter(d));

        v.addObjectClassPainter(TaxiNameSpace.CLASSPASS, new CellPainter(d, Color.red));
        v.addObjectClassPainter(TaxiNameSpace.CLASSAGENT, new CellPainter(d, Color.yellow));

        return v;
    }

    public static class RoomsMapPainter implements StaticPainter {
        public RoomsMapPainter(Domain domain) { }

        @Override
        public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {
            //draw the walls; make them black
            g2.setColor(Color.black);

            float domainXScale = (TaxiWorldDomain.MAXX + 1);
            float domainYScale = (TaxiWorldDomain.MAXY + 1);

            //determine then normalized width
            float width = (1.0f / domainXScale) * cWidth;
            float height = (1.0f / domainYScale) * cHeight;

            //pass through each cell of the map and if it is a wall, draw it
            for(int i = 0; i <= TaxiWorldDomain.MAXX; i++) {
                for(int j = 0; j <= TaxiWorldDomain.MAXY; j++) {

                    if(TaxiWorldDomain.MAP[i][j] == 1) {

                        float rx = i*width;
                        float ry = cHeight - height - j*height;

                        g2.fill(new Rectangle2D.Float(rx, ry, width, height));
                    }		
                }
            }		
        }
    }

    public static class CellPainter implements ObjectPainter {
        Color col;

        public CellPainter(Domain domain, Color col) {
            this.col = col;
        }

        @Override
        public void paintObject(Graphics2D g2, State s, ObjectInstance ob,
                                float cWidth, float cHeight) {
            //draw the walls; make them black
            g2.setColor(this.col);

            float domainXScale = (TaxiWorldDomain.MAXX + 1);
            float domainYScale = (TaxiWorldDomain.MAXY + 1);

            //determine then normalized width
            float width = (1.0f / domainXScale) * cWidth;
            float height = (1.0f / domainYScale) * cHeight;
            float rx = ob.getDiscValForAttribute(TaxiNameSpace.ATTX) * width;
            float ry = cHeight - height - ob.getDiscValForAttribute(TaxiNameSpace.ATTY) * height;

            g2.fill(new Rectangle2D.Float(rx, ry, width, height));

        }		
    }
}