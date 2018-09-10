package trolle.af.alexander;


public class Parser {


   public static double[][] parseLine(String line){
       String[] s = line.split(" ");
       if (s.length < 3){
            return null;
       }

       int rows = Integer.parseInt(s[0]);
       int cols = Integer.parseInt(s[1]);

       double[][] ret = new double[rows][cols];

       for (int i = 0; i < rows; i++) {
           for (int j = 0; j < cols; j++) {
               int index = 2+i*cols+j;
               ret[i][j] = Double.parseDouble(s[index]);
           }
       }

       return ret;
   }

}
