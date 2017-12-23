package com.company;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TSconstructure {
    ArrayList<String> state = new ArrayList();
    ArrayList<String> action = new ArrayList();
    ArrayList<String> init_state = new ArrayList();
    ArrayList<String> transition = new ArrayList();
    ArrayList<String> ap = new ArrayList();
    Map<String,String> label = new HashMap();

    private String filename;

    public TSconstructure(File file){
        filename = file.getName();
        try {
            Scanner scanner = new Scanner(file);


                //状态集合
                if (scanner.hasNextLine()) {
                    String state_line = scanner.nextLine();
                    Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
                    Matcher matcher = pattern.matcher(state_line);
                    if (matcher.find()) {
                        String string = matcher.group(0);
                        String[] str = string.split(",");
                        for (int i = 0; i < str.length; i++) {
                            state.add(str[i]);
                        }
                    }
                }
                //动作集合
                if (scanner.hasNextLine()) {
                    String act_line = scanner.nextLine();
                    Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
                    Matcher matcher = pattern.matcher(act_line);
                    if (matcher.find()) {
                        String string = matcher.group(0);
                        String[] str = string.split(",");
                        for (int i = 0; i < str.length; i++) {
                            action.add(str[i]);
                        }
                    }

                }
                //初始状态集合
                if (scanner.hasNextLine()) {
                    String init_state_line = scanner.nextLine();
                    Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
                    Matcher matcher = pattern.matcher(init_state_line);
                    if (matcher.find()) {
                        String string = matcher.group(0);
                        String[] str = string.split(",");
                        for (int i = 0; i < str.length; i++) {
                            init_state.add(str[i]);
                        }
                    }
                }
                //ap集合
                if (scanner.hasNextLine()) {
                    String ap_line = scanner.nextLine();
                    Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
                    Matcher matcher = pattern.matcher(ap_line);
                    if (matcher.find()) {
                        String string = matcher.group(0);
                        String[] str = string.split(",");
                        for (int i = 0; i < str.length; i++) {
                            ap.add(str[i]);
                        }
                    }
                }
                //transitons集合
                if (scanner.hasNextLine()) {
                    String isTransition = scanner.nextLine();
                    if (isTransition.equals("transition=")) {
                        while (true) {
                            //int i = 0;
                            String transition_line = scanner.nextLine();
                            if (transition_line.equals("label=")) {
                                break;
                            }
                            String req = transition_line.substring(transition_line.indexOf("(") + 1, transition_line.indexOf(")"));
                            transition.add(req);
                        }

                    }

                }
                //定义Label Function
                while (scanner.hasNextLine()) {
                    String label_line = scanner.nextLine();
                    String state = label_line.substring(label_line.indexOf("(") + 1, label_line.indexOf(")"));
                    String label_fun = label_line.substring(label_line.indexOf("{") + 1, label_line.indexOf("}"));
                    label.put(state, label_fun);
                }



        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 画出输入TS图的png图
     */
    public void getGraph(){
        String name = filename.substring(0,filename.indexOf("."));
        String url = "/Users/yinchenhao/Documents/Test/"+name;

        File file = new File(url);
        FileWriter fw = null;
        BufferedWriter writer = null;

        if (file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            //开始写文件
            writer.write("digraph G{");
            writer.newLine();
            Iterator<String> it = init_state.iterator();
            while (it.hasNext()){
                writer.write(" "+ "\""+it.next()+ "\""+" [style = filled];");
                writer.newLine();
            }

            Iterator<String> iterator = transition.iterator();
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
