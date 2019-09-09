package courseScrapper;

public class Splitter {

    public static String videoName(String link){
        String[] splited = link.split("/");
        String last = splited[splited.length - 1];
        return last;
    }

    public static String folderName(String link){
        String[] splited = link.split("/");
        return splited[splited.length - 2] + "/";
    }
}
