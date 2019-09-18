package courseScraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/*
*   Just class for keeping all in one place. Using ffmpeg program. Speeds up a video to x2 speed.
*   to use this class you have to have ffmpeg installed on computer.
*   Speed of the video has to be between 0.5 and 2
*/
public class SpeedUpFfmpeg {


    private static String format(float speed){
        DecimalFormatSymbols dotSeparator = new DecimalFormatSymbols();
        dotSeparator.setDecimalSeparator('.');
        NumberFormat formatter = new DecimalFormat("#0.0", dotSeparator);
        return formatter.format(speed);
    }


    public static String outputFileName(String filename, float speed){
        String result = "";
        int dot = filename.lastIndexOf(".");
        StringBuilder sb = new StringBuilder();

        try{
            sb.append(filename.substring(0,dot));
            sb.append("_" + format(speed) + "x_speed");
            sb.append(filename.substring(dot));
            result = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void speedUp(String file, float speed) {
        String newFileName = outputFileName(file, speed);

        String formattedSpeed = format(speed);
        String reversedSpeed = format(1 / speed); // because ffmpeg video converter using inverted arguments. F.e.: 0,5 means 2x video speed

        String command = "ffmpeg -i " + file + " -filter_complex [0:v]setpts=" + reversedSpeed +
                "*PTS[v];[0:a]atempo=" + formattedSpeed + "[a] -map [v] -map [a] " + newFileName;
        try {
            Process p1 = Runtime.getRuntime().exec(command);
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
            BufferedReader errors = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            while ((line = errors.readLine()) != null) {
                System.out.println(line);
            }
            errors.close();
            p1.waitFor();
        }catch (IOException e){
            System.out.println("/n---Something went wrong:");
            e.printStackTrace();
        }catch (InterruptedException e){
            System.out.println("/n/n---Process was interrupted!");
        }
        System.out.println("Done!\n" + newFileName);
    }

}
