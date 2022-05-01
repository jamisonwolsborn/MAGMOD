package net.jamisonwolsborn.magmod.util;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.util.math.Vec3d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ReadCSV {
    public static void main(String[] args, String filename) throws Exception {
        String line = "";
        String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader("net.jamisonwolsborn.magmod.util.B_fields" + filename));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] B_field = line.split(splitBy);
                int pos_x = Integer.parseInt(B_field[0]);
                int pos_y = Integer.parseInt(B_field[1]);
                int pos_z = Integer.parseInt(B_field[2]);
                int field_x = Integer.parseInt(B_field[3]);
                int field_y = Integer.parseInt(B_field[4]);
                int field_z = Integer.parseInt(B_field[5]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
