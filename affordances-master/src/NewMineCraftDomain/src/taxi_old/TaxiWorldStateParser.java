package taxi;

import burlap.oomdp.auxiliary.StateParser;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;

public class TaxiWorldStateParser implements StateParser {
    @Override
    public String stateToString(State s) {

        StringBuffer sbuf = new StringBuffer(256);

        ObjectInstance a = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSAGENT).get(0);

        String xa = TaxiNameSpace.ATTX;
        String ya = TaxiNameSpace.ATTY;

        sbuf.append(a.getDiscValForAttribute(xa)).append(" ").append(a.getDiscValForAttribute(ya)).append(", ");

        for (int i = 1; i <= TaxiNameSpace.MAXPASS; i++) {
            ObjectInstance p = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSPASS).get(i - 1);
            sbuf.append(p.getDiscValForAttribute(xa)).append(" ").append(p.getDiscValForAttribute(ya));
            if (i != TaxiNameSpace.MAXPASS) {
                sbuf.append(", ");
            }
        }

        return sbuf.toString();
    }

    @Override
    public State stringToState(String str) {
        String [] obcomps = str.split(", ");

        String [] acomps = obcomps[0].split(" ");

        int ax = Integer.parseInt(acomps[0]);
        int ay = Integer.parseInt(acomps[1]);
        int gx = TaxiWorldDomain.GOALX;
        int gy = TaxiWorldDomain.GOALY;

        State s = TaxiWorldDomain.getCleanState();
        TaxiWorldDomain.setAgent(s, ax, ay);
        TaxiWorldDomain.setGoal(gx, gy);

        for (int i = 1; i <= TaxiNameSpace.MAXPASS; i++) {
            String[] pcomps = obcomps[i].split(" ");
            int px = Integer.parseInt(pcomps[0]);
            int py = Integer.parseInt(pcomps[1]);
            TaxiWorldDomain.setPassenger(s, i, px, py);
        }

        return s;
    }

}