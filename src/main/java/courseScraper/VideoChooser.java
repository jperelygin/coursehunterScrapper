package courseScraper;

import java.util.ArrayList;
import java.util.Scanner;

public class VideoChooser {

    public static ArrayList<String> chooseVideos(){
        System.out.println("Choose videos in format: 'first_video-last_video' or just type number of one video.");
        Scanner sc = new Scanner(System.in);
        String sentance = sc.nextLine();
        ArrayList<String> result = new ArrayList<>();
        String[] sept = sentance.split("-");
        if(sept.length == 2){
            try {
                for(int i = Integer.parseInt(sept[0]); i <= Integer.parseInt(sept[1]); i++) {
                    result.add(String.valueOf(i - 1));
                }
            } catch (NumberFormatException e){
                System.out.println("use only numbers!");
                chooseVideos();
            }
        }else if(sept.length == 1){
            try {
                result.add(String.valueOf(Integer.parseInt(sept[0]) - 1));
            } catch (NumberFormatException e){
                System.out.println("use only numbers!");
                chooseVideos();
            }
        } else {
            System.out.println("wrong syntax");
            chooseVideos();
        }
        return result;
    }

    public static ArrayList<String> makeArrayOfVideos(ArrayList<String> videos, ArrayList<String> numbers){
        ArrayList<String> neededVideos = new ArrayList<>();
        for(String number : numbers){
            try{
                neededVideos.add(videos.get(Integer.parseInt(number)));
            }catch (Exception e){
                // TODO: Optimise it
                System.out.println("video number out of bounds!");
                e.printStackTrace();
            }
        }
        return neededVideos;
    }
}
