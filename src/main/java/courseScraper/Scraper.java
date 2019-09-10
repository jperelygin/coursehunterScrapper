/**
 *  Scrapper for video courses on coursehunter.net
 */

package courseScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Scraper {

    // URL of the course page
    private final String courseURL = readProps("courseURL");

    // Path for the folder with videos
    private final String fileDestination = readProps("fileDestination");

    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    private String readProps(String propertyName){
        String result = "";
        try{
            InputStream inp = Scraper.class.getResourceAsStream("/config.properties");
            Properties props = new Properties();
            props.load(inp);
            return props.getProperty(propertyName);
        }catch (IOException e){
            System.out.println("-- error while reading config.properties file");
            e.printStackTrace();
        }
        return result;
    }

    private Document goToSite(String siteURL) throws Exception{
        return Jsoup.connect(siteURL).userAgent(USER_AGENT).referrer("http://google.com").get();
        }

    private ArrayList<String> getVideosList(Document page){
        ArrayList<String> videoList = new ArrayList<>();
        Elements lessons = page.getElementsByClass("lessons-item");
        if(lessons.isEmpty()){
            System.out.println("Something wrong. No lessons with css @class=\"lessons-item\"!");
        }else{
            for(Element lesson : lessons){
                Elements tag = lesson.select("[itemprop=contentUrl]");
                String url = tag.attr("href");
                videoList.add(url);
            }
        }
        return videoList;
    }

    private void saveFile(String link){
        URL fileLink;
        String fileName = Splitter.videoName(link);
        String folderName = Splitter.folderName(link);
        File file = new File(fileDestination + folderName + fileName);
        System.out.println("Download of " + file.toString() + " started.");
        try {
            fileLink = new URL(link);
            Downloader downloader = new Downloader(file, getOneFileSize(link));
            downloader.download(fileLink);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(file.toString() + " saved!");
    }

    private void printAllVideosScrapped(ArrayList<String> links){
        //for(String link : links){ System.out.println(link); }
        System.out.println(Splitter.folderName(links.get(0)));
        getFilesSize(links);
    }


    private long getOneFileSize(String link){
        long size = 0L;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.getInputStream();
            size = connection.getContentLength();
        }catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }

    private void getFilesSize(ArrayList<String> links){
        long totalSize = 0L;
        for(int i = 0; i < links.size(); i++){
            String link = links.get(i);
            long size = getOneFileSize(link);
            System.out.println(i+1 + ". " + Splitter.videoName(link) + " size is: " + ByteConverter.humanReadableByteCount(size, true));
            totalSize += size;
        }
        System.out.println("Course " + Splitter.folderName(links.get(1)) + " total size is: " + ByteConverter.humanReadableByteCount(totalSize, true));
    }

    private void download(ArrayList<String> videos){
        System.out.println("Start downloading of full course? [y/n]\nWrite [c] to choose exact videos from the course.");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        if(answer.equals("n")){
            System.out.println("Download aborted.");
            System.exit(1);
        } else if(answer.equals("y")){
            for (String video : videos){
                saveFile(video);
            }
        } else if(answer.equals("c")){
            ArrayList<String> neededVideos = VideoChooser.makeArrayOfVideos(videos, VideoChooser.chooseVideos());
            for(String video : neededVideos){
                saveFile(video);
            }
        } else {
            System.out.println("Use letters 'y', 'n' or 'c'");
            download(videos);
        }
    }

    public static void main(String[] args) {
        Scraper sc = new Scraper();
        try{
            ArrayList<String> videos = sc.getVideosList(sc.goToSite(sc.courseURL));
            sc.printAllVideosScrapped(videos);
            sc.download(videos);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("FINISHED!");
    }
}
