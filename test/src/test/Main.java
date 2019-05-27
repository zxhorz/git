package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Main {
    public static void main(String args[]) throws FileNotFoundException, IOException {
        List<String> PT = readFileByLines("relationPT.txt");
        List<String> TP = readFileByLines("relationTP.txt");
        Map<String, Double> MPT = new HashMap<String, Double>();
        Map<String, Double> MTP = new HashMap<String, Double>();
        for (String string : PT) {
            string = string.replaceAll("\n", "");
            String[] strings = string.split(",");
            Double result = new Double(strings[2].split("/")[0]) / new Double(strings[2].split("/")[1]);
            MPT.put(strings[0] + "," + strings[1], result);
        }

        for (String string : TP) {
            string = string.replaceAll("\n", "");
            String[] strings = string.split(",");
            Double result = new Double(strings[2].split("/")[0]) / new Double(strings[2].split("/")[1]);
            MTP.put(strings[0] + "," + strings[1], result);
        }

        Iterator<Entry<String, Double>> it = MPT.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Double> entry = it.next();
            String key = entry.getKey();
            if (entry.getValue().compareTo(0.1) <= 0) {
                String program = key.split(",")[0];
                String table = key.split(",")[1];
                if (MTP.get(table + "," + program).compareTo(0.2) < 0) {
                    it.remove();
                    MTP.remove(table + "," + program);
                }
            }
        }

        it = MTP.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Double> entry = it.next();
            String key = entry.getKey();
            if (entry.getValue().compareTo(0.1) <= 0) {
                String table = key.split(",")[0];
                String program = key.split(",")[1];
                if (MPT.get(program + "," + table).compareTo(0.2) < 0) {
                    it.remove();
                    MPT.remove(program + "," + table);
                }
            }
        }


        FileWriter writer = new FileWriter("relationPT_m.csv", false);
        for (String key : MPT.keySet()) {
            String temp = key + "\n";
            temp = temp.replaceAll(",", " -> ");
            writer.write(temp);
        }
        writer.flush();
        writer.close();

        writer = new FileWriter("relationTP_m.csv", false);
        for (String key : MTP.keySet()) {
            String temp = key + "\n";
            temp = temp.replaceAll(",", " -> ");
            writer.write(temp);
        }
        writer.flush();
        writer.close();
    }

    public void readData() throws IOException {
        // File file = new File("C:/Users/xihaozhou/Desktop/useTable.gv");
        Set<String> programs = new HashSet<>();
        Set<String> tables = new HashSet<>();
        Set<String> edges = new HashSet<>();
        List<String> lines = readFileByLines("C:/Users/xihaozhou/Desktop/useTable.gv");
        int count = 0;
        String type = "";
        List<String> relation = new ArrayList<String>();
        for (String line : lines) {
            line = line.replace("\n", "");
            if (line.equals(""))
                continue;
            if (line.contains("node")) {
                count = count + 1;
                if (count == 1)
                    type = "program";
                else if (count == 2)
                    type = "table";
            } else if (line.contains("edge"))
                type = "edge";
            else {
                if (type == "program")
                    programs.add(line);
                else if (type == "table")
                    tables.add(line);
                else if (type == "edge")
                    edges.add(line);
            }
        }
        FileWriter writer = new FileWriter("relationPT.txt", false);
        for (String program : programs) {
            for (String table : tables) {
                int pt = 0;
                int pat = 0;
                for (String edge : edges) {
                    if (edge.startsWith(program + " -> "))
                        pat = pat + 1;
                    if (edge.equals(program + " -> " + table))
                        pt = pt + 1;
                }
                if (pt != 0) {
                    String temp = program + " " + table + " 1/" + (pat + pt) + "\n";
                    relation.add(temp);
                    writer.write(temp);
                    System.out.print(temp);
                }
            }
        }
        writer.flush();
        writer.close();
        writer = new FileWriter("relationTP.txt", false);

        for (String table : tables) {
            for (String program : programs) {
                int pt = 0;
                int pat = 0;
                for (String edge : edges) {
                    if (edge.endsWith(" -> " + table))
                        pat = pat + 1;
                    if (edge.equals(program + " -> " + table))
                        pt = pt + 1;
                }
                if (pt != 0) {
                    String temp = table + " " + program + " 1/" + (pat + pt) + "\n";
                    relation.add(temp);
                    writer.write(temp);
                    System.out.print(temp);
                }
            }
        }
        writer.flush();
        writer.close();

    }

    public static List<String> readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            List<String> strings = new ArrayList<>();
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                strings.add(tempString);
            }
            reader.close();
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }
}
