package com.afTrolle.common;

import java.io.*;

public class Parser {

    BufferedReader br;

    public Parser(String filePath) throws FileNotFoundException {
        // if file is not specified use system-in.
        if (filePath == null || filePath.isEmpty()) {
            br = new BufferedReader(new InputStreamReader(System.in));
        } else {
            br = new BufferedReader(new FileReader(filePath));
        }
    }

    public Matrix<Double> parseMatrix() throws IOException {
        String line = br.readLine();
        String[] s = line.split(" ");
        if (s.length < 3) {
            return null;
        }

        int rows = Integer.parseInt(s[0]);
        int cols = Integer.parseInt(s[1]);

        Double[][] res = new Double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = 2 + i * cols + j;
                res[i][j] = Double.parseDouble(s[index]);
            }
        }
        return new Matrix<>(res);
    }

    public int[] parseArray() throws IOException {
        String line = br.readLine();
        String[] s = line.split(" ");
        int[] res = new int[s.length - 1];
        for (int i = 1; i < s.length; i++) {
            res[i - 1] = Integer.parseInt(s[i]);
        }

        return res;
    }


    public void Done() throws IOException {
        br.close();
    }

}
