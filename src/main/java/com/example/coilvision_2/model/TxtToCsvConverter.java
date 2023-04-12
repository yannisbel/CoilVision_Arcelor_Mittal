package com.example.coilvision_2.model;

import java.io.*;

public class TxtToCsvConverter {

    public static void Txt_to_csv2(String url) {
        String inputFilePath  = url+".txt";
        String outputFilePath  = url+".csv";

        String stringToInsert = "csv_in/";

        StringBuilder stringBuilder = new StringBuilder(outputFilePath);
        int insertIndex = outputFilePath.indexOf("Krakov/") + "Krakov/".length();
        stringBuilder.insert(insertIndex, stringToInsert);

        outputFilePath = stringBuilder.toString();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            reader.skip(0);

            String line = reader.readLine();

            while (line != null) {
                String[] values = line.split(";");

                for (int i = 0; i < values.length; i++) {
                    String formattedValue = values[i].replace(",", ".");
                    if (i != values.length - 1) {
                        writer.write(formattedValue + ";");
                    } else {
                        writer.write(formattedValue + "\n");
                    }
                }

                line = reader.readLine();
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Txt_to_csv_out(String url) {
        try {
            String inputFile  = url;
            String outputFile  = url+".csv";

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] values = line.split("\\s+");
                for (int i = 0; i < values.length; i++) {
                    writer.write(values[i]);
                    if (i != values.length - 1) {
                        writer.write(";");
                    }
                }
                writer.newLine();
            }

            reader.close();
            writer.close();

        } catch (Exception e) {
            System.out.println("Error during conversion: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        String inputFilePath2  = "src/main/java/com/example/Fichiers/Krakov/1939351_F2";
        String inputFilePathout  = "src/main/java/com/example/Fichiers/Model/output2";

        try {
            Txt_to_csv2(inputFilePath2);
            Txt_to_csv_out(inputFilePathout);
            } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
