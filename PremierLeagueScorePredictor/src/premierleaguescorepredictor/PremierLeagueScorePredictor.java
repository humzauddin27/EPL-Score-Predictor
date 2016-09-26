//PREMIER LEAGUE SCORE PREDICTOR\\
//A PROGRAM BY: HUMZA FAHEEMUDDIN\\
//    LAST MODIFIED: 1/22/15     \\
//           ICS 4UI             \\
package premierleaguescorepredictor;

import java.awt.*;
import java.io.*;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFrame;

public class PremierLeagueScorePredictor extends JFrame {

    public static final int numTeams = 20;
    public static Team[] teams = new Team[numTeams];
    public static int numHomeGoals;
    public static int numAwayGoals;
    public static int numPlayers;
    public static Team home;
    public static Team away;
    public static String homeTeam;
    public static String awayTeam;
    static String[] teamList = new String[numTeams];
    static int[] stats = new int[220];
    static boolean seasons;
    static Color[] colors = new Color[numTeams];

    public static void calcHomeGoals(Team home, Team away) {
        //Using the stats from the 2014-2015 season, such as average goals at home, average goals the away team concedes, and wins/losses. 
        //These stats are multiplied by a random double between 0 and 1, and a value is created. This took a while to balance out because
        //teams were getting unrealisitically good/bad results at home, so I had to find the perfect balance. 


        //numHomeGoals = (int) (1.2 * avgHomeGoals + (home.winsHome / home.gamesHome) + avgAwayConceded*1.5 + (away.lossesAway / (away.gamesPlayed - away.gamesHome))
        /// (avgAwayConceded + (away.winsAway / (away.gamesPlayed - away.gamesHome))));
        //numHomeGoals = (int) (((1.5* avgHomeGoals) + (1.2*avgAwayConceded) + (away.lossesAway / (away.gamesPlayed - away.gamesHome)) + (home.winsHome/home.gamesHome))
        //- ((away.winsAway/(away.gamesPlayed - away.gamesHome)) + (home.concededHome/home.gamesHome)));
        //numHomeGoals = (int) (1.1 * (avgHomeGoals + avgAwayConceded + (home.winsHome/home.gamesHome) - away.winsAway/(away.gamesPlayed - away.gamesHome) - home.lossesHome/3 ));
        double avgHomeGoals = home.goalsHome / home.gamesHome;
        double avgAwayConceded = away.concededAway / (away.gamesPlayed - away.gamesHome);

        Random rand = new Random();
        numHomeGoals = (int) ((rand.nextDouble() * avgHomeGoals) + (rand.nextDouble() * avgAwayConceded) + ((home.winsHome / home.gamesHome) * rand.nextDouble())
                - (away.winsAway / (away.gamesPlayed - away.gamesHome)) - (away.winsAway / (away.gamesPlayed - away.gamesHome)) - (home.lossesHome / home.gamesHome));

        if (numHomeGoals < 0) {
            numAwayGoals += 1;
            numHomeGoals = 0;

        }


    }

    public static void calcAwayGoals(Team away, Team home) {
        //Same as above, but this time calculates the goals for the away team.

        double avgAwayGoals = away.goalsAway / (away.gamesPlayed - away.gamesHome);
        double avgHomeConceded = home.concededHome / home.gamesHome;

        Random rand = new Random();

        //numAwayGoals = (int) (avgAwayGoals  + (away.winsAway / (away.gamesPlayed - away.gamesHome)) + (avgHomeConceded * 1.5) + (home.lossesHome / home.gamesHome) / 
        //(avgHomeConceded + (home.winsHome / home.gamesHome)));
        //numAwayGoals = (int) (avgAwayGoals + avgHomeConceded + 1.3*(home.lossesHome/home.gamesHome) + (away.winsAway/(away.gamesPlayed - away.gamesHome)) / 
        //(1 + home.winsHome/home.gamesHome) );
        //numAwayGoals = (int) (avgAwayGoals + avgHomeConceded + (away.winsAway / (away.gamesPlayed - away.gamesHome)) - home.winsHome/home.gamesHome - away.lossesAway/3  );
        numAwayGoals = (int) ((rand.nextDouble() * avgAwayGoals) + (rand.nextDouble() * avgHomeConceded) + (away.winsAway) / (away.gamesPlayed - away.gamesHome) * rand.nextDouble()
                - (home.winsHome / home.gamesHome) - 0.4 * (away.lossesAway / (away.gamesPlayed - away.gamesHome)));

        if (numAwayGoals < 0) {
            numHomeGoals += 1;
            numAwayGoals = 0;

        }

    }

    public static void drawPotential(Team home, Team away) {
        //This checks to see, if first, the game is tied. Then, if the
        //home team deserves to win (based on wins and losses), it adds a goal to their total
        //If the away team deserves to win, since it is harder to score when you are away from home,
        //it adds 1 x a random number between 0-1. This way, there's a 50% chance that they get a goal added

        Random rand = new Random();
        if (numHomeGoals == numAwayGoals) {
            if (home.lossesHome < away.lossesAway) {
                if (home.winsHome > away.winsAway) {
                    numHomeGoals += 1;
                }
            } else {
                numAwayGoals += 1 * rand.nextDouble();
            }
        }

    }

    public static void luckyGoals() {
        //Sometimes, teams get lucky, and this usually happens when you're away from home
        //Things like incorrect penalty calls, red cards, always appear during games.

        Random rand = new Random();
        double x = rand.nextDouble();
        double y = rand.nextDouble();

        if (x > 0.85) {
            numHomeGoals += 1;

        }
        if (y < 0.5) {
            numAwayGoals += 1;

        }

    }

    private static void printStats(Team home, Team away) {
        //Used this to help troubleshoot.

        System.out.println(home.gamesPlayed);
        System.out.println(home.gamesHome);
        System.out.println(home.winsHome);
        System.out.println(home.winsAway);
        System.out.println(home.draws);
        System.out.println(home.lossesHome);
        System.out.println(home.lossesAway);
        System.out.println(home.goalsHome);
        System.out.println(home.goalsAway);
        System.out.println(home.concededHome);
        System.out.println(home.concededAway);
    }

    public static void realisticResult(Team home, Team away) {
        //Honestly, the goal of this method was that if a team was
        //way lower than another team in the league standings, then
        //it would add goals based on that. There was a weird glitch 
        //though and it didn't work the way I wanted to.
//        int x = 0;
//        int y = 0;
//
//        for (int i = 0; i < teams.length; i++) {
//            if (teamList[i].equalsIgnoreCase(homeTeam)) {
//                x = i;
//            }
//        }
//
//        for (int j = 0; j < teams.length; j++) {
//            if (teamList[j].equalsIgnoreCase(awayTeam)) {
//                y = j;
//            }
//        }
//
//        Random rand = new Random();
//        
//        if (x < y) {
//            if ((y - x) > 10) {
//                numHomeGoals += 1.5 * rand.nextDouble();
//
//            } else if ((y - x) > 5) {
//                numHomeGoals += 1;
//            }
//
//        } else {
//            if ((x - y) > 10) {
//                numAwayGoals += 1;
//
//            }
//        }
    }

    public static Team getHomeTeam() {

        //Just getting the home team from the user, as the title implied.

        Scanner s = new Scanner(System.in);
        if (seasons == true) {
            System.out.println("Enter the team who's season you wish to simulate: ");
            homeTeam = s.nextLine();
        } else {
            System.out.println("Enter the home team: ");
            homeTeam = s.nextLine();
        }

        for (int i = 0; i < teams.length; i++) {
            if (homeTeam.equalsIgnoreCase(teams[i].teamName)) {
                home = teams[i];
            }
        }
        return home;
    }

    public static Team getAwayTeam() {

        //Same as above, just getting the away team.

        Scanner s = new Scanner(System.in);
        System.out.println("Enter the away team: ");
        awayTeam = s.nextLine();

        for (int j = 0; j < teams.length; j++) {
            if (awayTeam.equalsIgnoreCase(teams[j].teamName)) {
                away = teams[j];
            }
        }

        return away;
    }

    public static void getColors() {

        //I had to assign colors to each team, and since the teams are organized
        //already, this was an easy task. Had to make a few custom colors, and those
        //turned out quite nice.

        colors[0] = Color.BLUE;
        Color lightBlue = new Color(50, 255, 255);
        colors[1] = lightBlue;
        colors[2] = Color.RED;
        colors[3] = Color.red;
        colors[4] = Color.red;
        colors[5] = Color.white;
        Color maroon = new Color(176, 48, 96);
        colors[6] = maroon;
        colors[7] = Color.red;
        colors[8] = Color.white;
        colors[9] = Color.BLACK;
        colors[10] = Color.red;
        colors[11] = Color.blue;
        Color fireBrick = new Color(178, 34, 34);
        colors[12] = fireBrick;
        colors[13] = Color.white;
        colors[14] = Color.blue;
        colors[15] = Color.red;
        colors[16] = fireBrick;
        colors[17] = Color.yellow;
        colors[18] = Color.white;
        colors[19] = Color.blue;

    }

    public void paint(Graphics g) {
        //Drawing the field
        g.setColor(Color.white);
        g.drawRect(100, 100, 1500, 600);
        g.drawLine(800, 100, 800, 700);
        g.drawOval(600, 200, 400, 400);
        g.drawRect(100, 250, 200, 300);
        g.drawRect(1400, 250, 200, 300);
        g.drawRect(100, 300, 75, 200);
        g.drawRect(1525, 300, 75, 200);
        g.drawOval(225, 390, 15, 15);
        g.drawOval(1460, 390, 15, 15);

        //All the text, the for loops dealing with getting the team's colors
        int a = 0;
        for (int i = 0; i < teams.length; i++) {
            if (teamList[i].equalsIgnoreCase(homeTeam)) {
                a = i;
            }
        }

        g.setFont(new Font("default", Font.BOLD, 30));
        g.setColor(colors[a]);
        g.drawString(homeTeam.toUpperCase(), 450, 75);
        String x = Integer.toString(numHomeGoals);
        g.drawString(x, 730, 75);

        for (int i = 0; i < teams.length; i++) {
            if (teamList[i].equalsIgnoreCase(awayTeam)) {
                a = i;
            }
        }

        g.setColor(colors[a]);
        g.drawString(awayTeam.toUpperCase(), 975, 75);
        String y = Integer.toString(numAwayGoals);
        g.drawString(y, 860, 75);
        g.setColor(Color.white);


        g.drawString(" - ", 790, 75);
        //
//        int xVal = 400;
//        int yVal = 125;
//        for (int i = 0; i < teams.length; i++) {
//            g.setColor(colors[i]);
//            g.drawString(teams[i].teamName, xVal, yVal);
//            yVal += 30;
//        }
        




    }

    public static String[] readInTeams() throws IOException {
        //Title says it all, if you want to enable a list that shows you 
        //all the team names, uncomment the stuff below!

        FileReader f = new FileReader("teams.txt");
        Scanner s = new Scanner(f);

        for (int i = 0; i < 20; i++) {
            teamList[i] = s.nextLine();
        }
//        System.out.println("Here is a list of the teams you can choose from: ");
//        for (int i = 0; i < teams.length; i++) {
//            System.out.println(teamList[i]);
//
//        }
        return teamList;
    }

    public static int[] readInStats() throws IOException {
        //Again, title tells it all

        FileReader f = new FileReader("estats.txt");
        Scanner s = new Scanner(f);

        for (int i = 0; i < stats.length + 1; i++) {
            if (!s.hasNext()) {
                break;
            } else {
                stats[i] = s.nextInt();
            }
        }
        return stats;

    }

    public static Team[] createTeamsArray(String[] names, int[] stats) {
        //This is to create an array of teams, all read in from a file. This method
        //takes as arguments the names and statistics of each team, but all jumbled up.
        //Each team has a name and 11 stats, so by constantly adding j+1, j+2, etc..
        //and then at the end, j+= 11, we get to the end of the arrays comfortably, 
        //getting all the teams with all of their stats.
        int j = 0;
        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Team();
            teams[i].teamName = names[i];       //getting the team names as part of the team array   
            teams[i].gamesPlayed = stats[j];
            teams[i].gamesHome = stats[j + 1];
            teams[i].winsHome = stats[j + 2];
            teams[i].winsAway = stats[j + 3];
            teams[i].draws = stats[j + 4];
            teams[i].lossesHome = stats[j + 5];
            teams[i].lossesAway = stats[j + 6];
            teams[i].goalsHome = stats[j + 7];
            teams[i].goalsAway = stats[j + 8];
            teams[i].concededHome = stats[j + 9];
            teams[i].concededAway = stats[j + 10];
            j += 11;
        }
        return teams;
    }

    public static void generateSingleTeamSeason(Team[] z) {
        //Asks the user which team they want to see season's simulated
        //Prints all 38 game score lines
        //Also had to make sure to get rid of the index of the team, so that it 
        //doesn't play itself during the regular season.
        //NOTE: If you prefer a different style for printing out the scores, change up the stuff starred below
        Scanner s = new Scanner(System.in);
        System.out.println("Do you want an entire season simulated for a single team, or just a game between two teams?");
        System.out.println("Type 'season' or 'game'");
        String ans = s.nextLine();
        int indexOfTeam = 0;
        int numGames = 1;
        int numHomePoints = 0;
        int numAwayPoints = 0;


        if (ans.equalsIgnoreCase("g")) {
            seasons = false;

        } else if (ans.equalsIgnoreCase("S") || ans.startsWith("S") || ans.startsWith("s")) {
            seasons = true;
            Team season = getHomeTeam();



            for (int i = 0; i < teamList.length; i++) {
                if (teamList[i].equalsIgnoreCase(season.teamName)) {
                    indexOfTeam = i;
                }
            }

            for (int j = 0; j < teams.length; j++) {
                if (j != indexOfTeam) {
                    home = season;
                    away = z[j];

                    calcHomeGoals(season, z[j]);
                    calcAwayGoals(z[j], season);
                    drawPotential(season, z[j]);
//                    realisticResult(season, z[j]);
                    luckyGoals();

                    System.out.println("-------");
                    System.out.println("Game #" + numGames);
                    System.out.println("-------");

                    //**************************************************************************
//                    System.out.println(home.teamName + " scored " + numHomeGoals + " goals!");
//                    System.out.println(away.teamName + " scored " + numAwayGoals + " goals!");
                    //**************************************************************************

                    System.out.println(home.teamName + " " + numHomeGoals + " - " + numAwayGoals + " " + away.teamName);

                    numGames++;

                    if (numHomeGoals > numAwayGoals) {
                        numHomePoints += 3;
                    } else if (numHomeGoals == numAwayGoals) {
                        numHomePoints += 1;
                    }

                }
            }

            for (int i = 0; i < z.length; i++) {
                if (i != indexOfTeam) {
                    home = z[i];
                    away = season;
                    calcHomeGoals(z[i], season);
                    calcAwayGoals(season, z[i]);
                    drawPotential(z[i], season);
//                    realisticResult(z[i], season);
                    luckyGoals();

                    System.out.println("-------");
                    System.out.println("Game #" + numGames);
                    System.out.println("-------");

                    System.out.println(home.teamName + " " + numHomeGoals + " - " + numAwayGoals + " " + away.teamName);

                    //**************************************************************************
//                    System.out.println(home.teamName + " scored " + numHomeGoals + " goals!");
//                    System.out.println(away.teamName + " scored " + numAwayGoals + " goals!");
                    //**************************************************************************


                    numGames++;

                    if (numHomeGoals < numAwayGoals) {
                        numAwayPoints += 3;
                    } else if (numHomeGoals == numAwayGoals) {
                        numAwayPoints += 1;
                    }

                }

            }

            System.out.println("-------");
            System.out.println(season.teamName + " collected " + numHomePoints + " out of a possible 57 points at home during this season!");
            System.out.println(season.teamName + " collected " + numAwayPoints + " out of a possible 57 points away from home during this season!");
            System.out.println(season.teamName + " collected " + (numHomePoints + numAwayPoints) + " out of a possible 114 points this season!");

        }

    }

    public static void chelseaOP() {
        if (homeTeam.equalsIgnoreCase("chelsea")) {
            numHomeGoals += 2;
        } else if (awayTeam.equalsIgnoreCase("chelsea")) {
            numAwayGoals += 2;
        }
    }

    public static void main(String[] args) throws IOException {

        PremierLeagueScorePredictor epl = new PremierLeagueScorePredictor();

        epl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color beautifulGreen = new Color(0, 175, 0);
        epl.setBackground(beautifulGreen);
        epl.setSize(3000, 800);

        int[] x = readInStats();
        String[] y = readInTeams();
        Team[] z;
        z = createTeamsArray(y, x);
        generateSingleTeamSeason(z);

        //Is initially set to false, but before this happens, in generateSingleTeamSeason, it asks
        //if you want a season or just a game. If you say season, the variable seasons is set to true
        //and none of the below happens, instead all the stuff that happens is in generateSingleTeamSeason
        if (seasons == false) {
            Team a = getHomeTeam();
            Team b = getAwayTeam();
            calcHomeGoals(a, b);
            calcAwayGoals(b, a);
            drawPotential(a, b);
            luckyGoals();
            getColors();
            epl.setVisible(true);
        }
    }
}
