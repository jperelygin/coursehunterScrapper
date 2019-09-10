package courseScraper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class Downloader {

    private File filePath;
    private long endSize;


    public Downloader(File file, long endSize) {
        filePath = file;
        this.endSize = endSize;
    }


    private String getCarriagesReturn(String percents) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < percents.length(); i++) {
            sb.append("\r");
            try {
            } catch (Exception e){
            }
        }
        return sb.toString();
    }



    public void download(URL link) {

        BufferedInputStream in = null;
        FileOutputStream out = null;

        NumberFormat nf = new DecimalFormat("#0.00");

        try {
            URLConnection conn = link.openConnection();
            in = new BufferedInputStream(link.openStream());
            out = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            int count;
            double sumCount = 0.0;

            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
                String percents = nf.format(sumCount / endSize * 100.0) + " %";
                sumCount += count;
                if (this.endSize > 0) {
                    System.out.print(percents);
                }
                Thread.sleep(50);
                System.out.print(getCarriagesReturn(percents));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

}
