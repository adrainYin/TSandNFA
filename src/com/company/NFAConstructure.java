package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class NFAConstructure {
    ArrayList<String> state = new ArrayList();
    ArrayList<String> alphabet = new ArrayList();
    ArrayList<String> transitions = new ArrayList();
    ArrayList<String> init_state = new ArrayList();
    ArrayList<String> final_state = new ArrayList();
    private String filename;

    public NFAConstructure(File file) {
        filename = file.getName();

        try {
            Scanner scanner = new Scanner(file);

            //读取状态集合
            if (scanner.hasNextLine()){
                String state_line = scanner.nextLine();
                state_line = state_line.substring(state_line.indexOf("{")+1,state_line.indexOf("}"));
                String [] strings = state_line.split(",");
                for (int i = 0; i < strings.length; i++) {
                    state.add(strings[i]);
                }
            }
            //读取字母表
            if (scanner.hasNextLine()){
                String alphabet_line = scanner.nextLine();
                alphabet_line = alphabet_line.substring(alphabet_line.indexOf("{")+1,alphabet_line.indexOf("}"));
                String[] strings = alphabet_line.split(",");
                for (int i = 0; i < strings.length; i++) {
                    alphabet.add(strings[i]);
                }
            }
            //读取transitions
            if (scanner.hasNextLine()){
                String isTransition = scanner.nextLine();
                if (isTransition.equals("transition=")){
                    while (true){
                        String transitions_line = scanner.nextLine();
                        if (transitions_line.equals("end")){
                            break;
                        }
                        String string = transitions_line.substring(transitions_line.indexOf("(")+1,transitions_line.indexOf(")"));
                        transitions.add(string);
                    }
                }

            }
            //读取初始状态集合
            if (scanner.hasNextLine()){
                String init_state_line = scanner.nextLine();
                init_state_line = init_state_line.substring(init_state_line.indexOf("{")+1,init_state_line.indexOf("}"));
                String[] strings = init_state_line.split(",");
                for (int i = 0; i < strings.length; i++) {
                    init_state.add(strings[i]);
                }
            }
            //读取终止状态集合
            if (scanner.hasNextLine()){
                String final_state_line = scanner.nextLine();
                final_state_line = final_state_line.substring(final_state_line.indexOf("{")+1,final_state_line.indexOf("}"));
                String[] strings = final_state_line.split(",");
                for (int i = 0; i < strings.length; i++) {
                    final_state.add(strings[i]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 画出输入的nfa的png图
     */
    public void getTsGraph(){
        String name = filename.substring(0,filename.indexOf("."));
        String url = "/Users/yinchenhao/Documents/Test/"+name;

        File file = new File(url);
        FileWriter fw = null;
        BufferedWriter writer = null;

        if (file.exists()){
            file.delete();
        }
        Iterator<String> iterator;
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            //开始写文件
            writer.write("digraph G{");
            writer.newLine();
            //定义结点样式
            writer.write("node [shape = circle]");
            writer.newLine();
            writer.newLine();
            iterator = init_state.iterator();
            while (iterator.hasNext()){
                writer.write(" "+ iterator.next()+ " [style = filled];");
                writer.newLine();
            }
            iterator = final_state.iterator();
            while (iterator.hasNext()){
                writer.write(" "+iterator.next()+" [shape = doublecircle];");
                writer.newLine();
            }
            writer.newLine();
            iterator = transitions.iterator();
            while (iterator.hasNext()){
                String[] strings = iterator.next().split(",");
                String first = "{\""+strings[0]+"\"}";
                String symbol = " -> ";
                String next = "{\""+strings[2]+"\"}";
                String action = " [label = \"" + strings[1] + "\"]";
                writer.write(first+symbol+next+action);
                writer.newLine();
            }
            writer.write("}");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("dot "+name + " -T png -o "+name+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
