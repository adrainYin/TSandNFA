package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class CheckInvariant {

    public static ArrayList<String> counterExample = new ArrayList<>();

    private static String post = null;
    private List<String> R = new ArrayList<>();
    private Stack<String> U = new Stack<>();

    public boolean checkInvariant(Multiply ts){

        boolean b = true;

        for(String s:ts.getNew_init_state()){
           if (!R.contains(s)){
               b = visit(s, ts);
           }
        }

        return b;
    }

    private boolean visit(String s, Multiply ts){

        U.push(s);
        R.add(s);

       do {
            String s1 = U.peek();
            if(!getUnvisitedPost(ts, s1)){  //后继结点已经访问
                if (isQF(ts, s1)){
                    while(!U.empty()){
                        counterExample.add(U.pop());
                    }
                    return false;
                }
                U.pop();

            }
            else{
                U.push(post);
                R.add(post);
            }
        }while(!U.isEmpty());
        return true;
    }

    private boolean getUnvisitedPost(Multiply ts, String current){
         for (String[] triple:ts.getNew_transition()){
            if ((triple[0].equals(current)) && (!R.contains(triple[2]))) { //后继没有被访问放回true
                post = triple[2];
                return true;
            }
        }
        return false;    //访问放回false
    }

    private boolean isQF(Multiply ts, String current){
        Map<String,String> ts_label = ts.getTs_label();
        String label = ts_label.get(current);

        if (ts.getFinal_ap().contains(label)) {
            return true;
        }
        else
            return false;
    }

}
