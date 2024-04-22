import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class InteractWithDatabase {
    private static final String CONFIG_FILE = "auth.cfg";

    public static void main(String[] args) {
        String connectionUrl = getConnectionUrl(CONFIG_FILE);

        Database database = new Database(connectionUrl);
        runConsole(database);
    }

    private static void runConsole(Database database)
    {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome! to Group 20 NBA Data Base.");
        System.out.println("Type \"t\" for team related commands.");
        System.out.println("Type \"p\" for player related commands.");
        System.out.println("Type \"o\" for other commands.");
        System.out.print("db > ");
        String line = console.nextLine();
        String[] parts;
        String arg = "";

        while (line != null && !line.equals("exit")) {
            parts = line.split("\\s+");

            if (line.indexOf(" ") > 0) {
                arg = line.substring(line.indexOf(" ")).trim();
            }

            else if (parts[0].equals("t")) {
                printTeam();
                System.out.println("Type \"t\" for team related commands.");
                System.out.println("Type \"p\" for player related commands.");
                System.out.println("Type \"o\" for other commands.");
            }

            else if (parts[0].equals("p")) {
                printPlayer();
                System.out.println("Type \"t\" for team related commands.");
                System.out.println("Type \"p\" for player related commands.");
                System.out.println("Type \"o\" for other commands.");
            }

            else if (parts[0].equals("o")) {
                printOthers();
                System.out.println("Type \"t\" for team related commands.");
                System.out.println("Type \"p\" for player related commands.");
                System.out.println("Type \"o\" for other commands.");
            }

            else if (parts[0].equals("loc")) {
                database.locationsAndArenas();
            }
            else if (parts[0].equals("MostPtsAtLocation")) {
                    if (parts.length >= 2)
                        database.playerWithHighestPointsAtThisLocation(arg);
                    else
                        System.out.println("Require an argument for this command.");
            }
            else if (parts[0].equals("MostPtsAtHome")) {
                    database.highestRecordePtsAtHome();
            }
            else if (parts[0].equals("MostPtsByAwayPlayer")) {
                    database.highestScoreAwayFromHome();
            }
            else if (parts[0].equals("PlayerPlayedAtAllLocations")) {
                database.playersPlayedAtAllLocations();
            }
            else if (parts[0].equals("Top10BestPlusMinusAvg")) {
                    database.bestPlusMinusAvg();
            }
            else if (parts[0].equals("GameDescription"))
            {
                database.gameDescriptions();
            }
            else if (parts[0].equals("WhoWon"))
            {
                database.winningTeam(arg);
            }
            else if (parts[0].equals("Top25MostEffPlayers"))
            {
                    database.mostEfficientPlayers();
            }
            else if (parts[0].equals("TeamEfficiencyRanking"))
            {
                    database.teamsWithMostPointsPerPossession();
            }
            else if (parts[0].equals("MostEffEachTeam"))
            {
                database.bestPlayerOnTeam();
            }
            else if (parts[0].equals("ImprovedCoach"))
            {
                database.mostImprovedCoach();
            }
            else if (parts[0].equals("BestUnder25"))
            {
                database.youngSuperStar();
            }
            else if (parts[0].equals("MissedAllGames"))
            {
                database.didNotPlay();
            }
            else if (parts[0].equals("MostGamesOfficiated"))
            {
                database.top5referee();
            }
            else if (parts[0].equals("HomeReferee"))
            {
                database.homeReferee();
            }
            else if (parts[0].equals("MultipleCoaches"))
            {
                database.moreThanOneCoach();
            }
            else if (parts[0].equals("PlayedAllGames"))
            {
                database.allGames();
            }
            else if (parts[0].equals("FreeThrow"))
            {
                database.topFiveByFreeThrow();
            }
            else if (parts[0].equals("BestInTheGame"))
            {
                database.topFifteenPlayers();
            }
            else if (parts[0].equals("MostWinsOverPeriod"))
            {
                try {
                    if (parts.length >= 2 && parts[1].matches("(\\d{4}-\\d{2}-\\d{2})")
                        && parts[2].matches("(\\d{4}-\\d{2}-\\d{2})")) {
                        database.maxWinsOverAPeriod(parts[1], parts[2]);
                    }
                    else
                        System.out.println("Please try again and enter a Valid Date format YYYY-MM-DD");
                } catch (Exception e) {
                    System.out.println("This query requires two Date arguments, Please try again.");
                }
            }
            else if (parts[0].equals("WhoReferredWhat")) {
                if (parts.length >= 2)
                    database.giveMeReferee(arg);
                else
                    System.out.println("Require an argument for this command.");
            }

            else {
                System.out.println("Type \"t\" for team related commands.");
                System.out.println("Type \"p\" for player related commands.");
                System.out.println("Type \"o\" for other commands.");
            }

            System.out.print("db > ");
            line = console.nextLine();
        }

        console.close();
    }



    private static String getConnectionUrl(String configFileName)
    {
        String connectionUrl;
        Properties prop = new Properties();

        try {
            FileInputStream configFile = new FileInputStream(configFileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                        + "database=cs3380;"
                        + "user=" + username + ";"
                        + "password="+ password +";"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        return connectionUrl;
    }

    private static void printTeam() {
        System.out.println("========================================================================================================================================================================");
        System.out.println(getPrintHelpCommands("loc", "- List all NBA Teams, their abbreviation, location and arenas they play at."));
        System.out.println(getPrintHelpCommands("MostPtsAtLocation <location>", "- Enter first few letters of location or \"City, State\" as you wish.\n" +
                getPrintHelpCommands("","  Most points by a player at a location over the season, and their team name. Use \"l\" command for all locations.")));
        System.out.println(getPrintHelpCommands("GameDescription", "- Get a list of all game Ids, game dates, home teams and away teams."));
        System.out.println(getPrintHelpCommands("WhoWon <game_id>", "- Enter game_id to see who won a particular game.\n" +
                getPrintHelpCommands("","  Use \"GameDescription\" command to look up game_id's or enter team abbreviation for all their home games.")));
        System.out.println(getPrintHelpCommands("TeamEfficiencyRanking", "- Rank teams by Points Per Possession, Used to find the best offensive teams, anything above \"1.1\" is considered good."));
        System.out.println(getPrintHelpCommands("MostWinsOverPeriod <st> <en>", "- Enter two dates between 2021-10-18 and 2022-04-11 in YYYY-MM-DD format. Ranks teams by wins in that period of time.\n") +
                getPrintHelpCommands("","  To measure teams performance in chunks, is used for a number of reasons; injuries, coaching change, etc."));
        System.out.println(getPrintHelpCommands("FreeThrow", "- Top 5 Free throw shooting teams."));
        System.out.println(getPrintHelpCommands("exit", "- Exit the program"));
        System.out.println("========================================================================================================================================================================");
    }

    private static void printPlayer() {
        System.out.println("========================================================================================================================================================================");
        System.out.println(getPrintHelpCommands("MostPtsAtHome", "- Who scored the highest points at a home game for each team.\n" +
                getPrintHelpCommands("","  Returns multiple players if they had the same high score at home.")));
        System.out.println(getPrintHelpCommands("MostPtsByAwayPlayer", "- Who scored the highest points at an away game for each team.\n" +
                getPrintHelpCommands("","  Returns multiple players if they had the same high score away from home.")));
        System.out.println(getPrintHelpCommands("PlayerPlayedAtAllLocations", "- Players who played at all NBA arenas over the season."));
        System.out.println(getPrintHelpCommands("Top10BestPlusMinusAvg", "- Top 10 players highest average plus-minus.\n" +
                getPrintHelpCommands("","  It measures how much a team scored when the player was on the floor.")));
        System.out.println(getPrintHelpCommands("Top25MostEffPlayers", "- Top 25 Most efficient players this season. Important stat, used to find the best offensive players.\n" +
                getPrintHelpCommands("","  abbreviated as \"PER\" Player efficiency rating, usually the player with highest PER is the MVP.\n") +
                getPrintHelpCommands("","  It is derived by a simple formula: (PTS + REB + AST + STL + BLK − Missed FG − Missed FT - TO) / GP.")));
        System.out.println(getPrintHelpCommands("MostEffEachTeam", "- This stat gives the \"best\" player on each team. \n" +
                getPrintHelpCommands("","  It is derived by a simple formula: (PTS + REB + AST + STL + BLK − Missed FG − Missed FT - TO) / GP.")));
        System.out.println(getPrintHelpCommands("BestUnder25", "- Top 10 Best young; up and coming players under 25 in the NBA."));
        System.out.println(getPrintHelpCommands("PlayedAllGames", "- Players who played all games in the NBA"));
        System.out.println(getPrintHelpCommands("BestInTheGame", "- Top 15 Best players in the game ranked by points per game, used for all star / all NBA selection."));
        System.out.println(getPrintHelpCommands("exit", "- Exit the program"));
        System.out.println("========================================================================================================================================================================");
    }

    private static void printOthers() {
        System.out.println("========================================================================================================================================================================");
        System.out.println(getPrintHelpCommands("ImprovedCoach", "- Coaches with better winning percentage this season compared to their career."));
        System.out.println(getPrintHelpCommands("MostGamesOfficiated", "- Referees who officiated the most number of games."));
        System.out.println(getPrintHelpCommands("HomeReferee", "- Referees who favoured home teams."));
        System.out.println(getPrintHelpCommands("MultipleCoaches", "- Teams that were coached by multiple coaches."));
        System.out.println(getPrintHelpCommands("WhoReferredWhat <st>", "- Provide game_id for a particular game or Team Abbrev to see who refereed all their home games.\n"+
                getPrintHelpCommands("","  Use \"GameDescription\" command to look up game_id's or enter team abbreviation.")));
        System.out.println(getPrintHelpCommands("exit", "- Exit the program"));
        System.out.println("========================================================================================================================================================================");
    }

    private static String getPrintHelpCommands(String command, String description)
    {
        String output = "";
        output = command;
        int spacing = 35;
        int locationLength = output.length();

        for (int i = locationLength; i < spacing; i++)
        {
            output += " ";
        }

        output += description;

        return output;
    }
}

class Database {
    private Connection connection;

    public Database(String connectionUrl) {
        try {
            connection = DriverManager.getConnection(connectionUrl);
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
    }


    public void locationsAndArenas()
    {

        try {
            String sql = "select team_name, team_abbrev, arenas.location, arenas.arena_name from teams\n" +
                    "join arenas on teams.arena_name = arenas.arena_name;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String output = "";
            int numColumns = 4;
            String[] columns = new String[numColumns];
            columns[0] = "Team Name";
            columns[1] = "Team Abbreviation";
            columns[2] = "Arena Name";
            columns[3] = "Team Location";
            output += "---------------------------------------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "---------------------------------------------------------------------------------------------------------------\n";
            while (resultSet.next())
            {
                columns = new String [numColumns];
                columns[0] = resultSet.getString("team_name");
                columns[1] = resultSet.getString("team_abbrev");
                columns[2] = resultSet.getString("location");
                columns[3] = resultSet.getString("arena_name");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "---------------------------------------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }


    // Player that has scored the highest points at a particular location
    public void playerWithHighestPointsAtThisLocation(String location)
    {
        try {
            String sql = "select TOP 1 arenas.location, playersStats.player_name, gamePlayerPerformance.pts, teams.team_name from arenas\n" +
                    "join teams on teams.arena_name = arenas.arena_name\n" +
                    "left join gameDetails on arenas.arena_name = gameDetails.arena_name\n" +
                    "join gamePlayerPerformance on gameDetails.game_id = gamePlayerPerformance.game_id\n" +
                    "join playersStats on gamePlayerPerformance.player_id = playersStats.player_id\n" +
                    "where arenas.location like (?) \n" +
                    "group by arenas.location, playersStats.player_name, gamePlayerPerformance.pts, teams.team_name\n" +
                    "order by gamePlayerPerformance.pts DESC;";


            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,  location + "%");
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 4;
            String[] columns = new String[numColumns];
            columns[0] = "Player Name";
            columns[1] = "Points";
            columns[2] = "Location";
            columns[3] = "Team Name";

            String output = "";
            output += "---------------------------------------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "---------------------------------------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("player_name");
                columns[1] = resultSet.getString("pts");
                columns[2] = resultSet.getString("location");
                columns[3] = resultSet.getString("team_name");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "---------------------------------------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    //Players with the highest score in home games
    public void highestRecordePtsAtHome()
    {
        try {
            String sql = "select outer_game_details.game_date, outer_stats.player_name, gamePlayerPerformance.pts, outer_game_details.home_team, outer_game_details.away_team from gameDetails as outer_game_details\n" +
                    "join gamePlayerPerformance on outer_game_details.game_id = gamePlayerPerformance.game_id \n" +
                    "join playerStatsPerTeam on gamePlayerPerformance.player_id = playerStatsPerTeam.player_id\n" +
                    "join playersStats as outer_stats on playerStatsPerTeam.player_id = outer_stats.player_id\n" +
                    "WHERE team_abbrev = outer_game_details.home_team and gamePlayerPerformance.pts in (\n" +
                    "    select top 1 max(gamePlayerPerformance.pts) from gameDetails as inner_game_details\n" +
                    "    join gamePlayerPerformance on inner_game_details.game_id = gamePlayerPerformance.game_id\n" +
                    "    join playerStatsPerTeam as inner_stats on gamePlayerPerformance.player_id = inner_stats.player_id\n" +
                    "    join playersStats on inner_stats.player_id = playersStats.player_id\n" +
                    "    WHERE inner_stats.team_abbrev = inner_game_details.home_team and outer_game_details.home_team = inner_game_details.home_team\n" +
                    "    group by inner_game_details.home_team\n" +
                    "    ORDER by max(gamePlayerPerformance.pts) desc\n" +
                    ") ORDER by gamePlayerPerformance.pts desc;\n";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 5;
            String[] columns = new String[numColumns];
            columns[0] = "Date";
            columns[1] = "Player Name";
            columns[2] = "Points";
            columns[3] = "Plays for / Home Team";
            columns[4] = "Against / Away Team";

            String output = "";
            output += "------------------------------------------------------------------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "------------------------------------------------------------------------------------------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("game_date");
                columns[1] = resultSet.getString("player_name");
                columns[2] = resultSet.getString("pts");
                columns[3] = resultSet.getString("home_team");
                columns[4] = resultSet.getString("away_team");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "------------------------------------------------------------------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    //Players with the highest score in away games
    public void highestScoreAwayFromHome()
    {
        try {
            String sql = "select outer_game_details.game_date, gamePlayerPerformance.pts, outer_stats.player_name, outer_game_details.away_team, gamePlayerPerformance.pts, outer_game_details.home_team from gameDetails as outer_game_details\n" +
                    "join gamePlayerPerformance on outer_game_details.game_id = gamePlayerPerformance.game_id \n" +
                    "join playerStatsPerTeam on gamePlayerPerformance.player_id = playerStatsPerTeam.player_id\n" +
                    "join playersStats as outer_stats on playerStatsPerTeam.player_id = outer_stats.player_id\n" +
                    "WHERE team_abbrev = outer_game_details.away_team and gamePlayerPerformance.pts in (\n" +
                    "    select top 1 max(gamePlayerPerformance.pts) from gameDetails as inner_game_details\n" +
                    "    join gamePlayerPerformance on inner_game_details.game_id = gamePlayerPerformance.game_id\n" +
                    "    join playerStatsPerTeam as inner_stats on gamePlayerPerformance.player_id = inner_stats.player_id\n" +
                    "    join playersStats on inner_stats.player_id = playersStats.player_id\n" +
                    "    WHERE inner_stats.team_abbrev = inner_game_details.away_team and outer_game_details.away_team = inner_game_details.away_team\n" +
                    "    group by inner_game_details.away_team\n" +
                    "    ORDER by max(gamePlayerPerformance.pts) desc\n" +
                    ") ORDER by gamePlayerPerformance.pts desc;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 5;
            String[] columns = new String[numColumns];
            columns[0] = "Date";
            columns[1] = "Player Name";
            columns[2] = "Points";
            columns[3] = "Plays for / Away Team";
            columns[4] = "Against / Home Team";

            String output = "";
            output += "-------------------------------------------------------------------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-------------------------------------------------------------------------------------------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("game_date");
                columns[1] = resultSet.getString("player_name");
                columns[2] = resultSet.getString("pts");
                columns[3] = resultSet.getString("away_team");
                columns[4] = resultSet.getString("home_team");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-------------------------------------------------------------------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    //Players who have played games at all the locations where games were played.
    public void playersPlayedAtAllLocations()
    {
        try {
            String sql = "select outerPlayersStats.player_id, outerPlayersStats.player_name from playersStats outerPlayersStats where not exists\n" +
                    "(\t\n" +
                    "\tselect distinct arenas.location from arenas EXCEPT\n" +
                    "\t(\t\n" +
                    "\t\tselect distinct arenas.location from arenas \n" +
                    "\t\tjoin gameDetails on arenas.arena_name = gameDetails.arena_name\n" +
                    "\t\tjoin gamePlayerPerformance on gameDetails.game_id = gamePlayerPerformance.game_id\n" +
                    "\t\twhere gamePlayerPerformance.player_id = outerPlayersStats.player_id\n" +
                    "\t)\n" +
                    ");";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 2;
            String[] columns = new String[numColumns];
            columns[0] = "Player Id";
            columns[1] = "Player Name";

            String output = "";
            output += "-------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("player_id");
                columns[1] = resultSet.getString("player_name");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void bestPlusMinusAvg()
    {
        try {
            String sql = "select top 10 playersStats.player_id, playersStats.player_name, avg(gamePlayerPerformance.plus_minus) as averagePlusMinus from playersStats\n" +
                    "left JOIN gamePlayerPerformance on playersStats.player_id = gamePlayerPerformance.player_id\n" +
                    "GROUP by playersStats.player_id, playersStats.player_name\n" +
                    "ORDER by averagePlusMinus desc;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Player Id";
            columns[1] = "Player Name";
            columns[2] = "Average plus_minus (+)";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("player_id");
                columns[1] = resultSet.getString("player_name");
                columns[2] = resultSet.getString("averagePlusMinus");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void gameDescriptions()
    {
        try {
            String sql = "select gameDetails.game_id, gameDetails.game_date, gameDetails.home_team, gameDetails.away_team \n" +
                    "from gameDetails;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Game Id";
            columns[1] = "Game Date";
            columns[2] = "Opponents";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("game_id");
                columns[1] = resultSet.getString("game_date");
                columns[2] = resultSet.getString("home_team") + " vs " + resultSet.getString("away_team");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // Get the team that won the game
    public void winningTeam(String gameId)
    {
        try {
            String sql = "SELECT gameDetails.game_date, team_abbrev as \"Winning Team\", team_score, home_team, away_team from gameDetails\n" +
                    "JOIN  gameTeamPerformance on gameDetails.game_id = gameTeamPerformance.game_id\n" +
                    "WHERE gameDetails.game_id like (?) and team_score in (\n" +
                    "    select max(team_score) FROM gameDetails as inner_game_details\n" +
                    "    join gameTeamPerformance on gameDetails.game_id = gameTeamPerformance.game_id\n" +
                    "    GROUP by inner_game_details.game_id\n" +
                    "    HAVING inner_game_details.game_id = gameDetails.game_id\n" +
                    ")";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + gameId + "%");
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Game Date";
            columns[1] = "Opponents";
            columns[2] = "Winner Team";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("game_date");
                columns[1] = resultSet.getString("home_team") + " vs " + resultSet.getString("away_team");
                columns[2] = resultSet.getString("Winning Team");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void mostEfficientPlayers()
    {
        try {
            String sql = "SET ARITHABORT OFF\n" +
                    "SET ANSI_WARNINGS OFF\n" +
                    "SELECT top 25 playersStats.player_name, (playersStats.total_pts + playersStats.tot_rb + playersStats.assists + playersStats.steals + playersStats.blocks - (playersStats.fg_attempts-playersStats.fg_made) - (playersStats.ft_attempts - playersStats.ft_made) - playersStats.turnovers)/playersStats.games_played as EFF FROM playersStats\n" +
                    "ORDER by EFF DESC;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            String output = "";
            int numColumns = 2;
            String[] columns = new String[numColumns];
            columns[0] = "Player Name";
            columns[1] = "PER";
            output += "----------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "----------------------------------------------------------------------------\n";
            while (resultSet.next())
            {
                columns = new String [numColumns];
                columns[0] = resultSet.getString("player_name");
                columns[1] = resultSet.getString("EFF");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "----------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void teamsWithMostPointsPerPossession() {
        try {
            String sql = "SELECT teams.team_name, avg(gameTeamPerformance.team_score/gameDetails.team_pace) as \"Points Per Possession\" from teams\n" +
                    "JOIN gameTeamPerformance on teams.team_abbrev = gameTeamPerformance.team_abbrev\n" +
                    "JOIN gameDetails on gameDetails.game_id = gameTeamPerformance.game_id\n" +
                    "GROUP by teams.team_name\n" +
                    "ORDER by \"Points Per Possession\" DESC;\n";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            String output = "";
            int numColumns = 2;
            String[] columns = new String[numColumns];
            columns[0] = "Team";
            columns[1] = "Points Per Possession";
            output += "-------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-------------------------------------------------------\n";
            while (resultSet.next())
            {
                columns = new String [numColumns];
                columns[0] = resultSet.getString("team_name");
                columns[1] = resultSet.getString("Points Per Possession");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void maxWinsOverAPeriod(String st, String en)
    {
        try {
            String sql = "SELECT team_abbrev as \"Team Name\", COUNT(team_abbrev) as Wins, gameDetails.game_date from gameDetails\n" +
                    "JOIN  gameTeamPerformance on gameDetails.game_id = gameTeamPerformance.game_id\n" +
                    "WHERE gameDetails.game_id BETWEEN (?) and (?)\n" +
                    "and team_score in (\n" +
                    "    select max(team_score) FROM gameDetails as inner_game_details\n" +
                    "    join gameTeamPerformance on gameDetails.game_id = gameTeamPerformance.game_id\n" +
                    "    WHERE inner_game_details.game_id = gameDetails.game_id \n" +
                    "    GROUP by inner_game_details.game_id\n" +
                    ") GROUP by team_abbrev, gameDetails.game_date\n" +
                    "ORDER by wins desc;";


            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,  st);
            statement.setString(2,  en);
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Team Name";
            columns[1] = "Wins";
            columns[2] = "Date";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("Team Name");
                columns[1] = resultSet.getString("Wins");
                columns[2] = resultSet.getString("game_date");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void bestPlayerOnTeam()
    {

        try {
            String sql = "select outerPlayerStatsPerTeam.team_abbrev, playersStats.player_name, \n" +
                    "(outerPlayerStatsPerTeam.total_pts + outerPlayerStatsPerTeam.tot_rb + outerPlayerStatsPerTeam.assists + outerPlayerStatsPerTeam.steals + outerPlayerStatsPerTeam.blocks - (outerPlayerStatsPerTeam.fg_attempts - outerPlayerStatsPerTeam.fg_made) - (outerPlayerStatsPerTeam.ft_attempts - outerPlayerStatsPerTeam.ft_made) - outerPlayerStatsPerTeam.turnovers)/outerPlayerStatsPerTeam.games_played as EFF from playerStatsPerTeam outerPlayerStatsPerTeam\n" +
                    "join playersStats on outerPlayerStatsPerTeam.player_id = playersStats.player_id\n" +
                    "where outerPlayerStatsPerTeam.player_id in (\n" +
                    "    select top 1 playersStats.player_id from playerStatsPerTeam innerPlayerStatsPerTeam\n" +
                    "    join playersStats on innerPlayerStatsPerTeam.player_id = playersStats.player_id\n" +
                    "    where innerPlayerStatsPerTeam.team_abbrev = outerPlayerStatsPerTeam.team_abbrev\n" +
                    "    order by (innerPlayerStatsPerTeam.total_pts + innerPlayerStatsPerTeam.tot_rb + innerPlayerStatsPerTeam.assists + innerPlayerStatsPerTeam.steals + innerPlayerStatsPerTeam.blocks - (innerPlayerStatsPerTeam.fg_attempts - innerPlayerStatsPerTeam.fg_made) - (innerPlayerStatsPerTeam.ft_attempts - innerPlayerStatsPerTeam.ft_made) - innerPlayerStatsPerTeam.turnovers)/innerPlayerStatsPerTeam.games_played DESC\n" +
                    ")\n" +
                    "group by outerPlayerStatsPerTeam.team_abbrev, playersStats.player_name, (outerPlayerStatsPerTeam.total_pts + outerPlayerStatsPerTeam.tot_rb + outerPlayerStatsPerTeam.assists + outerPlayerStatsPerTeam.steals + outerPlayerStatsPerTeam.blocks - (outerPlayerStatsPerTeam.fg_attempts - outerPlayerStatsPerTeam.fg_made) - (outerPlayerStatsPerTeam.ft_attempts - outerPlayerStatsPerTeam.ft_made) - outerPlayerStatsPerTeam.turnovers)/outerPlayerStatsPerTeam.games_played \n" +
                    "order by EFF DESC;";


            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Team Name";
            columns[1] = "Player Name";
            columns[2] = "PER";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("team_abbrev");
                columns[1] = resultSet.getString("player_name");
                columns[2] = resultSet.getString("EFF");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void mostImprovedCoach()
    {

        try {
            String sql = "select coaches.coach_id, coaches.coach_name, (CAST(((CAST(coaches.win_current_season AS float)/CAST(82 AS float)) - coaches.win_percentage) AS float)/CAST(coaches.win_percentage AS float))*100 as ImprovementPercentage from coaches\n" +
                    "where CAST(coaches.win_current_season AS float)/CAST(82 AS float) > coaches.win_percentage;";


            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Coach ID";
            columns[1] = "Coach Name";
            columns[2] = "Improved By";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("coach_id");
                columns[1] = resultSet.getString("coach_name");
                columns[2] = resultSet.getString("ImprovementPercentage");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void youngSuperStar()
    {
        try {
            String sql = "SET ARITHABORT OFF\n" +
                    "select top 10 playersStats.player_name, playersStats.age, " +
                    "CAST(CAST(playersStats.total_pts AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as points_per_game,  " +
                    "CAST(CAST(playersStats.tot_rb AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as rebounds_per_game, " +
                    "CAST(CAST(playersStats.assists AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as assists_per_game, " +
                    "CAST(CAST(playersStats.steals AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as steals_per_game, " +
                    "CAST(\"fg%\"*100 as decimal(18,2)) as \"fg%\", CAST(\"3p%\"*100 as decimal(18,2)) as \"3p%\" from playersStats\n" +
                    "where playersStats.games_played != 0 and playersStats.age < 25\n" +
                    "order by points_per_game DESC; ";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 8;
            String[] columns = new String[numColumns];
            columns[0] = "Name";
            columns[1] = "Age";
            columns[2] = "Points Per Game";
            columns[3] = "Rebounds Per Game";
            columns[4] = "Assists Per Game";
            columns[5] = "Steals Per Game";
            columns[6] = "Field Goal%";
            columns[7] = "Three Point%";
            String output = "";
            output += "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("player_name");
                columns[1] = resultSet.getString("age");
                columns[2] = resultSet.getString("points_per_game");
                columns[3] = resultSet.getString("rebounds_per_game");
                columns[4] = resultSet.getString("assists_per_game");
                columns[5] = resultSet.getString("steals_per_game");
                columns[6] = resultSet.getString("fg%");
                columns[7] = resultSet.getString("3p%");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void didNotPlay()
    {
        try {
            String sql = "select playersStats.player_name, count(game_id) as missedGames from playersStats \n" +
                    "left join inActivePlayersInGame on playersStats.player_id = inActivePlayersInGame.player_id\n" +
                    "group by playersStats.player_name\n" +
                    "having count(game_id) = 82;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            String output = "";
            int numColumns = 2;
            String[] columns = new String[numColumns];
            columns[0] = "Name";
            columns[1] = "Games Missed";
            output += "-------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-------------------------------------------------------\n";
            while (resultSet.next())
            {
                columns = new String [numColumns];
                columns[0] = resultSet.getString("player_name");
                columns[1] = resultSet.getString("missedGames");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    public void top5referee()
    {
        try {
            String sql = "select top 5 referees.referee_id, referees.referee_name, referees.games_officiated from referees\n" +
                    "order by referees.games_officiated DESC;";


            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Referee ID";
            columns[1] = "Name";
            columns[2] = "Number of Games";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("referee_id");
                columns[1] = resultSet.getString("referee_name");
                columns[2] = resultSet.getString("games_officiated");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    public void homeReferee()
    {
        try {
            String sql = "select top 5 referees.referee_id, referees.referee_name, referees.foul_differential from referees\n" +
                    "order by referees.foul_differential desc;";


            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Referee ID";
            columns[1] = "Name";
            columns[2] = "Foul Differential";

            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("referee_id");
                columns[1] = resultSet.getString("referee_name");
                columns[2] = resultSet.getString("foul_differential");

                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    public void moreThanOneCoach()
    {
        try {
            String sql = "select teamCoaches.team_abbrev from teamCoaches\n" +
                    "join coaches on teamCoaches.coach_id = coaches.coach_id\n" +
                    "group by teamCoaches.team_abbrev\n" +
                    "having count(team_abbrev) > 1;\n";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            String output = "";
            int numColumns = 1;
            String[] columns = new String[numColumns];
            columns[0] = "Team";
            output += "---------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "---------------------------\n";
            while (resultSet.next())
            {
                columns = new String [numColumns];
                columns[0] = resultSet.getString("team_abbrev");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "---------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }


    public void giveMeReferee(String game_id)
    {
        try {
            String sql = "select referees.referee_id, referees.referee_name, inGameReferees.game_id from inGameReferees join \n" +
                    "referees on inGameReferees.referee_id = referees.referee_id\n" +
                    "where inGameReferees.game_id like (?);";


            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + game_id );
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Referee ID";
            columns[1] = "Name";
            columns[2] = "Game ID";
            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("referee_id");
                columns[1] = resultSet.getString("referee_name");
                columns[2] = resultSet.getString("game_id");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void allGames()
    {
        try {
            String sql = "select playersStats.player_id, playersStats.player_name, outerPlayerStatsPerTeam.team_abbrev from playersStats \n" +
                    "join playerStatsPerTeam outerPlayerStatsPerTeam on playersStats.player_id = outerPlayerStatsPerTeam.player_id\n" +
                    "where playersStats.player_id in \n" +
                    "(\n" +
                    "    select playersStats.player_id from playersStats \n" +
                    "    join playerStatsPerTeam innerPlayerStatsPerTeam on playersStats.player_id = innerPlayerStatsPerTeam.player_id\n" +
                    "    where innerPlayerStatsPerTeam.team_abbrev = outerPlayerStatsPerTeam.team_abbrev \n" +
                    "    and playersStats.player_id in \n" +
                    "    (\n" +
                    "        select playersStats.player_id from playersStats\n" +
                    "        join playerStatsPerTeam on playersStats.player_id = playerStatsPerTeam.player_id\n" +
                    "        where not exists (\n" +
                    "            select gameTeamPerformance.game_id from gameTeamPerformance \n" +
                    "            where gameTeamPerformance.team_abbrev = outerPlayerStatsPerTeam.team_abbrev\n" +
                    "            except \n" +
                    "                select gameTeamPerformance.game_id from gameTeamPerformance join \n" +
                    "                gamePlayerPerformance on gameTeamPerformance.game_id = gamePlayerPerformance.game_id\n" +
                    "                where gameTeamPerformance.team_abbrev = outerPlayerStatsPerTeam.team_abbrev and gamePlayerPerformance.player_id = innerPlayerStatsPerTeam.player_id\n" +
                    "        )\n" +
                    "    )\n" +
                    ");";


            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 3;
            String[] columns = new String[numColumns];
            columns[0] = "Player ID";
            columns[1] = "Name";
            columns[2] = "Team";
            String output = "";
            output += "-----------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "-----------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("player_id");
                columns[1] = resultSet.getString("player_name");
                columns[2] = resultSet.getString("team_abbrev");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "-----------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void topFiveByFreeThrow()
    {
        try {
            String sql = "SELECT top 5 team_name, ft_made, ft_attempts, \"ft%\" from teams\n" +
                    "order by \"ft%\" DESC;";


            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int numColumns = 4;
            String[] columns = new String[numColumns];
            columns[0] = "Name";
            columns[1] = "Made Free Throws";
            columns[2] = "Attempted Free Throws";
            columns[3] = "FT%";
            String output = "";
            output += "---------------------------------------------------------------------------------------------------------------\n";
            output += getOutput(numColumns, columns) + "\n";
            output += "---------------------------------------------------------------------------------------------------------------\n";

            while (resultSet.next())
            {
                columns[0] = resultSet.getString("team_name");
                columns[1] = resultSet.getString("ft_made");
                columns[2] = resultSet.getString("ft_attempts");
                columns[3] = resultSet.getString("ft%");
                output += getOutput(numColumns, columns) + "\n";
            }
            output += "---------------------------------------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void topFifteenPlayers()
    {
        try {
            String sql = "select top 15 playersStats.player_name, playersStats.age, " +
                    "CAST(CAST(playersStats.total_pts AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as points_per_game,  " +
                    "CAST(CAST(playersStats.tot_rb AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as rebounds_per_game, " +
                    "CAST(CAST(playersStats.assists AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as assists_per_game, " +
                    "CAST(CAST(playersStats.steals AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as steals_per_game, " +
                    "CAST(CAST(playersStats.blocks AS float)/CAST(playersStats.games_played AS float) as decimal(18,2)) as blocks_per_game, " +
                    "CAST(\"fg%\"*100 as decimal(18,2)) as \"fg%\", CAST(\"3p%\"*100 as decimal(18,2)) as \"3p%\" from playersStats\n" +
                    "where playersStats.games_played != 0\n" +
                    "order by points_per_game DESC; ";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int numColumns = 9;
            String[] columns = new String[numColumns];
            columns[0] = "Name";
            columns[1] = "Age";
            columns[2] = "Pts Pr Gme";
            columns[3] = "Rb Pr Gme";
            columns[4] = "Ast Pr Gme";
            columns[5] = "Stl Pr Gme";
            columns[6] = "Blk Pr Gme";
            columns[7] = "FG%%";
            columns[8] = "3pt%";
            String output = "";
            output += "----------------------------------------------------------------------------------------------------------------------\n";
            output += getOutputTwo(numColumns, columns) + "\n";
            output += "----------------------------------------------------------------------------------------------------------------------\n";

            while (resultSet.next()) {
                columns[0] = resultSet.getString("player_name");
                columns[1] = resultSet.getString("age");
                columns[2] = resultSet.getString("points_per_game");
                columns[3] = resultSet.getString("rebounds_per_game");
                columns[4] = resultSet.getString("assists_per_game");
                columns[5] = resultSet.getString("steals_per_game");
                columns[6] = resultSet.getString("blocks_per_game");
                columns[7] = resultSet.getString("fg%");
                columns[8] = resultSet.getString("3p%");
                output += getOutputTwo(numColumns, columns) + "\n";
            }
            output += "----------------------------------------------------------------------------------------------------------------------\n";
            System.out.println(output);
        }
        catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }


    private String getOutput(int numColumns, String[] columns)
    {
        String output = "";

        for (int i = 0; i < numColumns; i++)
        {
            if (i == 0)
            {
                output = "";
            }
            output += columns[i];
            int spacing = 26;
            int length = columns[i].length();

            for (int j = length; j < spacing; j++)
            {
                output += " ";
            }
            output += "| ";
        }
        return output;
    }
    private String getOutputTwo(int numColumns, String[] columns)
    {
        String output = "";

        for (int i = 0; i < numColumns; i++)
        {
            int spacing = 0;
            if (i == 0)
            {
                output = "";
                spacing = 22;
            } else {
                spacing = 10;
            }
            output += columns[i];
            int length = columns[i].length();

            for (int j = length; j < spacing; j++)
            {
                output += " ";
            }
            output += "| ";
        }
        return output;
    }
}
