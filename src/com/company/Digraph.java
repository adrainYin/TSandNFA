package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 画出叉乘之后的TS图结构
 */

public class Digraph {

    private Multiply multiply;
    private String url = "/Users/yinchenhao/Documents/Test/";
    private String fileName;

    public void setMultiply(Multiply multiply) {
        this.multiply = multiply;
    }

    public Digraph(String string){
        url = url + string;
        fileName = string;

    }

    public void getDigraph(){
        File file = new File(url);
        FileWriter fw = null;
        BufferedWriter writer = null;

        ArrayList<String[]> transitions = multiply.getNew_transition();
        Iterator<String[]> iterator = transitions.iterator();
        Iterator<String> it = multiply.getNew_init_state().iterator();
        if(file.exists()){
            file.delete();
        }
            try {
                file.createNewFile();
                fw = new FileWriter(file);
                writer = new BufferedWriter(fw);
                //开始写文件
                writer.write("digraph G{");
                writer.newLine();

                while (it.hasNext()){
                    writer.write(" "+ "\""+it.next()+"\""+ " [style = filled];");
                    writer.newLine();
                }

                while(iterator.hasNext()){
                    String[] strings = iterator.next();
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
            runtime.exec("dot "+fileName + " -T png -o "+fileName+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

