package com.company;

import java.util.*;

public class Multiply {

    //定义叉乘之后的状态集合，用二维数组的表示
    private String[][] ts_state;
    //定义新的状态数组
    private ArrayList<String> ts_ap = new ArrayList();
    //定义新的label function
    private Map<String,String> ts_label = new HashMap();
    //定义叉乘之后的transition
    //定义为数组集合，每一条list存储一个String数组，每个数组存放一个三元组
    private ArrayList<String[]> transitions = new ArrayList();
    //定义叉乘之后的初始状态集合，每一条list存储一个状态字符串
    private ArrayList<String> new_init_state = new ArrayList();
    /*定义NFA中的终止状态final,作为验证不变性的条件
    如果在DFS搜索中，验证某一个状态的label function等于终止状态，那么该TS是不安全的
    */
    private ArrayList<String> final_ap = new ArrayList();

    private static String next = null;
    private List<Integer> index = new ArrayList();
    private ArrayList<String[]> new_transition = new ArrayList();

    //定义get函数，对private成员访问
    public String[][] getTs_state() {
        return ts_state;
    }
    public ArrayList<String> getTs_ap() {
        return ts_ap;
    }
    public ArrayList<String> getNew_init_state() {
        return new_init_state;
    }
    public ArrayList<String[]> getNew_transition() {
        return new_transition;
    }
    public Map<String, String> getTs_label() {
        return ts_label;
    }
    public ArrayList<String> getFinal_ap() {
        return final_ap;
    }
    public ArrayList<String[]> getTransitions() {
        return transitions;
    }

    /*叉乘函数，返回叉乘后的TS结构

         */
    public void mulitplyTSAndNFA(TSconstructure tSconstructure, NFAConstructure nfaConstructure){

       //将两个list集合做笛卡尔积
        ArrayList<String> ts_state_list = tSconstructure.state;
        ArrayList<String> nfa_state_list = nfaConstructure.state;
        ArrayList<String> multiply_state = new ArrayList();
        for (int i = 0; i < ts_state_list.size(); i++) {
            for (int j = 0; j < nfa_state_list.size(); j++) {
                multiply_state.add(ts_state_list.get(i) +"," +nfa_state_list.get(j));
            }

        }
        //将叉乘后的集合进行拆分，拆分为一个二维数组
        //第一列表示TS图中的状态，第二列表示NFA中的状态
        int mun_state = multiply_state.size();
        ts_state = new String[mun_state][2];
        for (int i = 0; i < mun_state; i++) {
            String[] strings = multiply_state.get(i).split(",");
            for (int j = 0; j < strings.length; j++) {
                ts_state[i][j] = strings[j];
            }
        }

        //ap集合合并
        //读取NFA的状态集合，直接转换为新的ap
        ArrayList<String> ap = nfaConstructure.state;
        Iterator<String> iterator = ap.iterator();
        while (iterator.hasNext()){
            ts_ap.add(iterator.next());
        }

        //transition的处理
        // 将TS的transition的List集合转为二维数组
        int ts_length = tSconstructure.transition.size();
        String[][] ts_transition = new String[ts_length][3];
        for (int i = 0; i < ts_length; i++) {
            String[] strings = tSconstructure.transition.get(i).split(",");
            for (int j = 0; j < strings.length; j++) {
                ts_transition[i][j] = strings[j];
            }
        }

//        transition的处理
//        将NFA的transition的List集合转为二维数组
        int nfa_length = nfaConstructure.transitions.size();
        String[][] nfa_transition = new String[nfa_length][3];
        for (int i = 0; i < nfa_length; i++) {
            String[] strings = nfaConstructure.transitions.get(i).split(",");
            for (int j = 0; j < strings.length; j++) {
                nfa_transition[i][j] = strings[j];
            }
        }
        //label function的处理，直接读取状态集合的第二个元素存入map
        //map中TS和NFA的状态用逗号隔开
        for (int i = 0; i < ts_state.length; i++) {
            ts_label.put(ts_state[i][0]+ "," +ts_state[i][1],ts_state[i][1]);
        }

        //初始状态叉乘后的结果
        ArrayList<String> init_ts = tSconstructure.init_state;
        ArrayList<String> init_nfa = nfaConstructure.init_state;
        Map<String,String> label = tSconstructure.label;
        for (int i = 0; i < init_ts.size(); i++) {
            String ts_label = label.get(init_ts.get(i));
            for (int j = 0; j < init_nfa.size(); j++) {
                for (int k = 0; k < nfa_length; k++) {
                    if (init_nfa.get(j).equals(nfa_transition[k][0])){
                        String tra = nfa_transition[k][1];
                        if (tra.equals(ts_label)){
                            String init_q = nfa_transition[k][2];
                            String[] strings = new String[2];
                            strings[0] = init_ts.get(i);
                            strings[1] = init_q;
                            String string = strings[0] + "," +strings[1];
                            new_init_state.add(string);
                        }
                    }
                }
            }

        }

        //定义状态叉乘后的状态转移集合
        //对两个transition的二维数组进行遍历
        Map<String,String> ts_map = tSconstructure.label;
        for (int i = 0; i < ts_length; i++) {
            String first_ts = ts_transition[i][0];
            String action = ts_transition[i][1];
            String next_ts = ts_transition[i][2];
            String nextTsLbbel = ts_map.get(next_ts);
            for (int j = 0; j < nfa_length; j++) {
                String words = nfa_transition[j][1];
                if (words.equals(nextTsLbbel)){
                    String first_nfa = nfa_transition[j][0];
                    String next_nfa = nfa_transition[j][2];
                    String new_first_ts = first_ts + "," +first_nfa;
                    String new_next_ts = next_ts  + "," + next_nfa;
                    String[] strings = new String[3];
                    strings[0] = new_first_ts;
                    strings[1] = action;
                    strings[2] = new_next_ts;
                    transitions.add(strings);
                }
            }
        }

        //删除TS中的不可达的transition
        for (String string : new_init_state){
            isExist(string);
        }
        Iterator<Integer> integerIterator = index.iterator();
        while (integerIterator.hasNext()){
            new_transition.add(transitions.get(integerIterator.next()));
        }
        //存储终止状态
        ArrayList<String> final_state = nfaConstructure.final_state;
        iterator = final_state.iterator();
        while (iterator.hasNext()){
            final_ap.add(iterator.next());
        }
    }


    /**
     * 删除不可达结点
     * @param string
     * @return
     */
    private String isExist(String string){
       if (string.equals(null)){
           return "complete!";
       }
       else {
           for (int i = 0; i < transitions.size(); i++) {
               if (string.equals(transitions.get(i)[0]) && !index.contains(i)) {
                   next = transitions.get(i)[2];
                   index.add(i);
                   isExist(next);
               }
           }
           return null;
       }
    }
}
