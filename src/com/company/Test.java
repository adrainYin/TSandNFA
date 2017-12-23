package com.company;

import java.io.File;
import java.util.*;

public class Test {

    public static void main(String[] args) {

        System.out.println(" import the TS");
        Scanner scanner = new Scanner(System.in);

        String local_TS= "/Users/yinchenhao/Documents/Test/"+scanner.nextLine();
        File file_TS = new File(local_TS);
        TSconstructure tSconstructure = new TSconstructure(file_TS);
        tSconstructure.getGraph();

        System.out.println("impoet the NFA");
        scanner = new Scanner(System.in);
        String local_NFA = "/Users/yinchenhao/Documents/Test/"+scanner.nextLine();
        File file_NFA = new File(local_NFA);
        NFAConstructure nfaConstructure = new NFAConstructure(file_NFA);
        nfaConstructure.getTsGraph();

        Iterator<String> iterator;

        //测试TS结构的读取
//        ArrayList<String> list = tSconstructure.state;
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//
//        }
//        ArrayList<String> list1 = tSconstructure.action;
//        iterator = list1.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//
//        }
//
//        ArrayList<String> list2 = tSconstructure.init_state;
//        iterator = list2.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//
//        ArrayList<String> list3 = tSconstructure.ap;
//        iterator = list3.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//
//        ArrayList<String> list4 = tSconstructure.transition;
//        iterator = list4.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//
//        Map<String,String> map = tSconstructure.label;
//        System.out.println(map);
//
//        //测试NFA的读取
//        ArrayList<String> state_NFA = nfaConstructure.state;
//        iterator = state_NFA.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//
//        ArrayList<String> alphabet = nfaConstructure.alphabet;
//        iterator = alphabet.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//
//        ArrayList<String> transitions_NFA = nfaConstructure.transitions;
//        iterator = transitions_NFA.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//
//        ArrayList<String> init_state = nfaConstructure.init_state;
//        iterator = init_state.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//
//        ArrayList<String> final_state = nfaConstructure.final_state;
//        iterator = final_state.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }

        //输出叉乘之后的TS结构
        Multiply multiply = new Multiply();
        multiply.mulitplyTSAndNFA(tSconstructure,nfaConstructure);
        System.out.println("export the new TS");
        scanner = new Scanner(System.in);
        Digraph digraph = new Digraph(scanner.nextLine());
        digraph.setMultiply(multiply);
        digraph.getDigraph();

        System.out.println("输出叉乘之后的TS结构");
        String[][] new_state =  multiply.getTs_state();
        ArrayList<String> new_ap =  multiply.getTs_ap();
        ArrayList<String[]> transition = multiply.getTransitions();
        ArrayList<String> new_init_state = multiply.getNew_init_state();
        ArrayList<String[]> new_transition = multiply.getNew_transition();
        Map<String,String> new_label = multiply.getTs_label();
        System.out.println("输出状态集合");
        for (int i = 0; i < new_state.length; i++) {
            System.out.println(new_state[i][0] +","+new_state[i][1]);
        }
        System.out.println("输出新的ap");
        iterator = new_ap.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("输出新的初始状态");
        for (int i = 0; i < new_init_state.size(); i++) {
            String string = new_init_state.get(i);
            System.out.println(string);
        }
        System.out.println("输出所有的transition集合");
        System.out.println(transition.size());
        for (int i = 0; i < transition.size(); i++) {
            String[] strings = transition.get(i);
            System.out.println(strings[0] + "," + strings[1]+ "," +strings[2]);
        }

        System.out.println("输出新的可达的transition");
        System.out.println(new_transition.size());
        for (int i = 0; i < new_transition.size(); i++) {
            String[] strings = new_transition.get(i);
            System.out.println(strings[0] + "," + strings[1]+ "," +strings[2]);
        }
        System.out.println("输出新的label function");
        System.out.println(new_label);
        System.out.println("输出终止的ap");
        iterator = multiply.getFinal_ap().iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }


        CheckInvariant test = new CheckInvariant();
        if(test.checkInvariant(multiply))
            System.out.println("Satisfy!");
        else{
            System.out.println("Not Satisfy!\nThe unsafe path is:");
            Collections.reverse(CheckInvariant.counterExample);
            for(String s:CheckInvariant.counterExample)
                System.out.println(s);
        }

    }
}
