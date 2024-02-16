/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.randomdatagen2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Yevhen K, 2024
 */
public class RandomDataGen2 {

   private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";

    private static BufferedReader inputF;
    private static BufferedWriter outputF;
    
    private static String[] nationalities;
    private static String[] genres;
    private static String[] names;
    private static String[] surnames;
    private static String[] imbd;
    private static String[] actors;
    private static String[] users;
    
    private static String[] ready_movies;
    private static String[] ready_series;
    
    
    private static HashSet<String> usedSurnames;
    private static HashSet<String> usedMovies;
    private static HashSet<String> usedSeries;
    
    private static List<String> Movies;
    private static List<String> Series;
    
    public static void main(String[] args) {
        
        //load_genres();
        load_nationalities();
        load_names();
        load_surnames();
        
        usedSurnames  = new HashSet<>();
        
        System.out.print("Generating actors... ");
        make_random_people("actors.csv");
        System.out.print("Generating directors... ");
        make_random_people("directors.csv");
        System.out.print("Generating users... ");
        make_random_users("users.csv");
        
        // Free up the memory
        nationalities = null;
        surnames = null; 
        names = null;
        usedSurnames  = new HashSet<>();
        
        Movies = new ArrayList<>();
        Series = new ArrayList<>();
        
        load_movies_series();
        load_actors();
        load_users();

        System.out.print("Parsing movies & series ... ");
        parce_movies_series();
        
        usedMovies  = new HashSet<>();
        usedSeries  = new HashSet<>();
        
        System.out.print("Generating movies ... ");
        make_random_movies("movies.csv");
        System.out.print("Generating series ... ");
        make_random_series("series.csv");
        
        load_movies();
        System.out.print("Generating user's movie activity ... ");
        make_random_movie_activity("movies_activity.csv");
        
        load_series();
        System.out.print("Generating user's series activity ... ");
        make_random_series_activity("series_activity.csv");
        
    }
    
    public static void load_genres(){
        System.out.print("Loading genres... ");
        genres = readStringsFromFile("genres.txt");
        System.out.println( genres.length + " done");
    }

    public static void load_nationalities(){
        System.out.print("Loading nationalities... ");
        nationalities = readStringsFromFile("nationalities.txt");
        System.out.println( nationalities.length + " done");
    }

    public static void load_surnames(){
        System.out.print("Loading surnames... ");
        surnames = readStringsFromFile("surnames.txt");
        System.out.println( surnames.length + " done");
    }

    public static void load_names(){
        System.out.print("Loading names... ");
        names = readStringsFromFile("names.txt");
        System.out.println( names.length + " done");
    }    

    public static void load_movies_series(){
        System.out.print("Loading IMDB... ");
        imbd = readStringsFromFile("imdb.csv");
        System.out.println( imbd.length + " done");
    }
    
    public static void load_actors(){
        System.out.print("Loading actors... ");
        actors = readStringsFromFile("actors.csv");
        System.out.println( actors.length + " done");
    }    

    public static void load_users(){
        System.out.print("Loading users... ");
        users = readStringsFromFile("users.csv");
        System.out.println( users.length + " done");
    }
    
    public static void load_movies(){
        System.out.print("Loading movies... ");
        ready_movies = readStringsFromFile("movies.csv");
        System.out.println( ready_movies.length + " done");
    }    

    public static void load_series(){
        System.out.print("Loading series... ");
        ready_series = readStringsFromFile("series.csv");
        System.out.println( ready_series.length + " done");
    }      
    
    public static String[] parseCSV(String csvLine) {
        List<String> result = new ArrayList<>();
        boolean withinQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : csvLine.toCharArray()) {
            if (c == '"') {
                withinQuotes = !withinQuotes;
            } else if (c == ',' && !withinQuotes) {
                result.add(sb.toString());
                sb.setLength(0); // Clear the StringBuilder
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }

    public static void make_random_series_activity(String fileName){
                 
        boolean in_progress = true;
        int how_many_generate = 500;
        int cnt = 0;
        String delimiter = ";";
        boolean first_line = true;
        
        try {
            outputF  = new BufferedWriter(new FileWriter(fileName, false ));
        } catch (Exception e) {
            System.out.println("Open file error: " + e );
        } 
        
        while (in_progress) {
            
            String random_serie = (ready_series[randomRangeRandom(1, ready_series.length-1)]);
            String random_user = (users[randomRangeRandom(1, users.length-1)]);
            String[] serie_parts =  random_serie.split(";");
            String[] user_parts =  random_user.split(";");
            
            try {
                if (first_line){
                    outputF.write("serie_id" + delimiter + "username" + delimiter + "genre\n" );
                    first_line = false;
                }
                outputF.write(serie_parts[0] + delimiter + user_parts[0] + delimiter + serie_parts[4] );
            } catch (Exception e) {
                System.out.println("Write to file error: " + e );
            }                    
            cnt++;
                if (cnt>how_many_generate-1) {
                    in_progress = false;
                } else {
                    try {
                        outputF.newLine();
                    } catch (Exception e) {
                        System.out.println("Write to file error: " + e );
                    }                    
                
            }

        }
        
        try {
            outputF.close();
        } catch (Exception e) {
              System.out.println("Closing error: " + e );
        }
        System.out.println(cnt + " Done");
        
    }    
    
    public static void make_random_movie_activity(String fileName){
                 
        boolean in_progress = true;
        int how_many_generate = 500;
        int cnt = 0;
        String delimiter = ";";
        boolean first_line = true;
        
        try {
            outputF  = new BufferedWriter(new FileWriter(fileName, false ));
        } catch (Exception e) {
            System.out.println("Open file error: " + e );
        } 
        
        while (in_progress) {
            
            String random_movie = (ready_movies[randomRangeRandom(1, ready_movies.length-1)]);
            String random_user = (users[randomRangeRandom(1, users.length-1)]);
            String[] movie_parts =  random_movie.split(";");
            String[] user_parts =  random_user.split(";");
            
            try {
                if (first_line){
                    outputF.write("movie_id" + delimiter + "username" + delimiter + "genre\n" );
                    first_line = false;
                }
                outputF.write(movie_parts[0] + delimiter + user_parts[0] + delimiter + movie_parts[4] );
            } catch (Exception e) {
                System.out.println("Write to file error: " + e );
            }                    
            cnt++;
                if (cnt>how_many_generate-1) {
                    in_progress = false;
                } else {
                    try {
                        outputF.newLine();
                    } catch (Exception e) {
                        System.out.println("Write to file error: " + e );
                    }                    
                
            }

        }
        
        try {
            outputF.close();
        } catch (Exception e) {
              System.out.println("Closing error: " + e );
        }
        System.out.println(cnt + " Done");
        
    } 
    
    
    public static void make_random_series(String fileName){
                 
        boolean in_progress = true;
        int how_many_generate = 500;
        int cnt = 0;
        String delimiter = ";";
        boolean first_line = true;
        boolean check_year = false;
        
        Calendar cal = Calendar.getInstance();
        int CurrYear = cal.get(Calendar.YEAR);
        
        try {
            outputF  = new BufferedWriter(new FileWriter(fileName, false ));
        } catch (Exception e) {
            System.out.println("Open file error: " + e );
        } 
        
        while (in_progress) {
            
            String random_series = (Series.get(randomRangeRandom(0, Series.size()-1)) );
            String random_actor = (actors[randomRangeRandom(1, actors.length-1)]);
            String[] actor_cre =  random_actor.split(";");
            String[] series_details =  random_series.split(";");
            
            boolean can_process = true;
            int movie_year = 0;
            if (check_year) {
                movie_year = Integer.parseInt(series_details[1]);
                if (movie_year<=(CurrYear-10)) {
                    can_process = false;
                }
            }
                                    
            int prev_count = usedSeries.size();
            if (can_process) usedSeries.add(random_series);
            if (usedSeries.size()>prev_count) {

                try {
                    if (first_line){
                        outputF.write("id" + delimiter + "name" + delimiter + "year" + delimiter + "qualification" + delimiter  + "genre" + delimiter + "duration" + delimiter + "number_of_seasons" + delimiter + "actor_name" + delimiter + "actor_surname\n" );
                        first_line = false;
                    }
                    outputF.write(cnt+1 + delimiter + random_series + delimiter + actor_cre[0] + delimiter + actor_cre[1] );
                } catch (Exception e) {
                    System.out.println("Write to file error: " + e );
                }                    
                cnt++;
                if (cnt>how_many_generate-1) {
                    in_progress = false;
                } else {
                    try {
                        outputF.newLine();
                    } catch (Exception e) {
                        System.out.println("Write to file error: " + e );
                    }                    
                }
            } else {
               // System.out.println("The same - skip [" + cnt + "]");
            }

        }
        
        try {
            outputF.close();
        } catch (Exception e) {
              System.out.println("Closing error: " + e );
        }
        System.out.println(cnt + " Done");
        
    }     
    
    public static void make_random_movies(String fileName){
                 
        boolean in_progress = true;
        int how_many_generate = 500;
        int cnt = 0;
        String delimiter = ";";
        boolean first_line = true;
        boolean check_year = false;
        
        Calendar cal = Calendar.getInstance();
        int CurrYear = cal.get(Calendar.YEAR);
        
        try {
            outputF  = new BufferedWriter(new FileWriter(fileName, false ));
        } catch (Exception e) {
            System.out.println("Open file error: " + e );
        } 
        
        while (in_progress) {
            
            String random_movie = (Movies.get(randomRangeRandom(0, Movies.size()-1)) );
            String random_actor = (actors[randomRangeRandom(1, actors.length-1)]);
            String[] actor_cre =  random_actor.split(";");
            String[] movie_details =  random_movie.split(";");
            
            boolean can_process = true;
            int movie_year = 0;
            if (check_year) {
                movie_year = Integer.parseInt(movie_details[1]);
                if (movie_year<=(CurrYear-10)) {
                    can_process = false;
                }
            }
                                    
            int prev_count = usedMovies.size();
            if (can_process) usedMovies.add(random_movie);
            if (usedMovies.size()>prev_count) {

                try {
                    if (first_line){
                        outputF.write("id" + delimiter + "name" + delimiter + "year" + delimiter + "qualification" + delimiter + "genre" + delimiter + "duration" + delimiter + "actor_name" + delimiter + "actor_surname\n" );
                        first_line = false;
                    }
                    outputF.write(cnt+1 + delimiter + random_movie + delimiter + actor_cre[0] + delimiter + actor_cre[1] );
                } catch (Exception e) {
                    System.out.println("Write to file error: " + e );
                }                    
                cnt++;
                if (cnt>how_many_generate-1) {
                    in_progress = false;
                } else {
                    try {
                        outputF.newLine();
                    } catch (Exception e) {
                        System.out.println("Write to file error: " + e );
                    }                    
                }
            } else {
               // System.out.println("The same - skip [" + cnt + "]");
            }

        }
        
        try {
            outputF.close();
        } catch (Exception e) {
              System.out.println("Closing error: " + e );
        }
        System.out.println(cnt + " Done");
        
    } 
    
    
    public static void parce_movies_series(){

        int cnt = 0;
        String delimiter = ";";
        boolean first_line = true;
        int max_field_length = 35;
        
        int idx_title = 0;
        int idx_date = 1;
        int idx_rate = 2;
        int idx_genre = 4;
        int idx_duration = 5;
        int idx_type = 6;
        int idx_episodes = 8;
        
        for (String random_string : imbd) {

            String[] parts = parseCSV(random_string.trim());
            String genre = parts[idx_genre].replaceAll("[^a-zA-Z]", "");
            String title = parts[idx_title].trim(); 
            if (title.length()>max_field_length) title = parts[idx_title].substring(0, max_field_length);
            
            
            String tmp_str = "";
            
            if (parts[idx_type].equalsIgnoreCase("film")) {
                
                tmp_str = title + delimiter + parts[idx_date].trim() + delimiter + parts[idx_rate].trim() + delimiter + genre + delimiter + parts[idx_duration].trim();
                Movies.add(tmp_str);
            } else if (parts[idx_type].equalsIgnoreCase("series")) { 
                tmp_str = title + delimiter + parts[idx_date].trim() + delimiter + parts[idx_rate].trim() + delimiter + genre + delimiter + parts[idx_duration].trim()+ delimiter + parts[idx_episodes].trim();
                Series.add(tmp_str);
            }
            
            //System.out.println(tmp_str );
            
            cnt++;
        }
        System.out.println(" movies: " + Movies.size() + ", series: " + Series.size() + " Done");
        
    }
    
    public static void make_series_log(){
        
    }
    
    public static void make_random_users(String fileName){
                 
        boolean in_progress = true;
        int how_many_generate = 500;
        int cnt = 0;
        String delimiter = ";";
        boolean first_line = true;
        
        try {
            outputF  = new BufferedWriter(new FileWriter(fileName, false ));
        } catch (Exception e) {
            System.out.println("Open file error: " + e );
        } 
        
        while (in_progress) {
            
            String random_name = (names[randomRangeRandom(0, names.length-1)]);
            String random_surname = (surnames[randomRangeRandom(0, surnames.length-1)]);
            
            int prev_count = usedSurnames.size();
            usedSurnames.add(random_surname);
            if (usedSurnames.size()>prev_count) {

                try {
                    if (first_line){
                        outputF.write("username" + delimiter + "name" + delimiter + "surname" + delimiter + "password\n" );
                        first_line = false;
                    }
                    outputF.write(random_name + delimiter + random_name + delimiter + random_surname + delimiter + generatePassword(8) );
                } catch (Exception e) {
                    System.out.println("Write to file error: " + e );
                }                    
                cnt++;
                if (cnt>how_many_generate-1) {
                    in_progress = false;
                } else {
                    try {
                        outputF.newLine();
                    } catch (Exception e) {
                        System.out.println("Write to file error: " + e );
                    }                    
                }
            } else {
               // System.out.println("The same - skip [" + cnt + "]");
            }

        }
        
        try {
            outputF.close();
        } catch (Exception e) {
              System.out.println("Closing error: " + e );
        }
        System.out.println(cnt + " Done");
        
    }    
    

    public static void make_random_people(String fileName){
                 
        boolean in_progress = true;
        int how_many_generate = 500;
        int cnt = 0;
        String delimiter = ";";
        boolean first_line = true;
        
        try {
            outputF  = new BufferedWriter(new FileWriter(fileName, false ));
        } catch (Exception e) {
            System.out.println("Open file error: " + e );
        } 
        
        while (in_progress) {
            
            String random_name = (names[randomRangeRandom(0, names.length-1)]);
            String random_surname = (surnames[randomRangeRandom(0, surnames.length-1)]);
            
            int prev_count = usedSurnames.size();
            usedSurnames.add(random_surname);
            if (usedSurnames.size()>prev_count) {
                String nationality = nationalities[randomRangeRandom(0, nationalities.length-1)];
                try {
                    if (first_line){
                        outputF.write("name" + delimiter + "surname" + delimiter + "nationality\n" );
                        first_line = false;
                    }                    
                    
                    outputF.write(random_name + delimiter + random_surname + delimiter + nationality );
                } catch (Exception e) {
                    System.out.println("Write to file error: " + e );
                }                    
                cnt++;
                if (cnt>how_many_generate-1) {
                    in_progress = false;
                } else {
                    try {
                        outputF.newLine();
                    } catch (Exception e) {
                        System.out.println("Write to file error: " + e );
                    }                    
                }
            } else {
               // System.out.println("The same - skip [" + cnt + "]");
            }

        }
        
        try {
            outputF.close();
        } catch (Exception e) {
              System.out.println("Closing error: " + e );
        }
        System.out.println(cnt + " Done");
        
    }
    
    public static int randomRangeRandom(int start, int end) {
        Random random = new Random();
        int number = random.nextInt((end - start) + 1) + start;
        return number;
    }
    
    private static String[] readStringsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.lines().toArray(String[]::new);
        } catch (Exception e) {
            return new String[0];
        }
    }
    
   

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    } 
    
}
