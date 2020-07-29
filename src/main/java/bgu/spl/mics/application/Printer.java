package bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class Printer {
    private static Gson gson= new GsonBuilder().setPrettyPrinting().create();
    public static void print(String filename, Object page){

        try(FileWriter fileWriter=new FileWriter(filename)){
            gson.toJson(page,fileWriter);
        }
        catch (IOException ioe){
            System.out.println("couldn't print "+page.getClass().getSimpleName()+" to "+filename);
        }

    }
}
