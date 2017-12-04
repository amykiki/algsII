import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {
    private int teamNum;
    private int[][] teams;
    private HashMap<String, Integer> teamMap;
    private String[] teamNames;
    private int[] remainGames;
    private int[][] rgMap;
    private final int WIN = 0;
    private final int LOSE = 1;
    private final int REMAIN = 2;
    private final int MAXWIN = 3;
    private final int SUB = 4;
    private Bag<FlowEdge>[] flowEdges;
    private int START;
    private int END;
    public BaseballElimination(String filename) {
        try {
            In in = new In(filename);
            teamNum = in.readInt();
            in.readLine();
            teams = new int[teamNum][5];
            teamMap = new HashMap<>();
            teamNames = new String[teamNum];
            remainGames = new int[teamNum * (teamNum - 1) / 2];
            rgMap = new int[remainGames.length][2];
            END = remainGames.length + teamNum + 1; // end index
            START = END - 1;
            int rgIndex = 0;
            for(int i = 0; i < teamNum; i++) {
                String str = in.readLine();
                str = str.replaceAll("\\s+", " ");
                String[] teaminfo = str.split(" ");
                teamMap.put(teaminfo[0], i);
                teamNames[i] = teaminfo[0];
                for(int j = 1; j <= 3; j++) {
                    teams[i][j - 1] = Integer.valueOf(teaminfo[j]);
                }
                teams[i][MAXWIN] = teams[i][WIN] + teams[i][REMAIN];
                teams[i][SUB] = i + 1;
                if (i > 0) {
                    teams[i][SUB] += teams[i - 1][SUB];
                }
                for(int j = 5 + i; j < teaminfo.length; j++) {
                    remainGames[rgIndex] = Integer.valueOf(teaminfo[j]);
                    rgMap[rgIndex][0] = i;
                    rgMap[rgIndex][1] = j - 4;
                    rgIndex++;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't Open the Team File " + filename);
        }
    }

    public Iterable<String> teams() {
        return new HashSet<>(teamMap.keySet());
    }
    public int numberOfTeams() {
        return teamNum;
    }

    public int wins(String team) {
        validateTeam(team);
        return teams[teamMap.get(team)][WIN];
    }

    public int losses(String team) {
        validateTeam(team);
        return teams[teamMap.get(team)][LOSE];
    }

    public int remaining(String team) {
        validateTeam(team);
        return teams[teamMap.get(team)][REMAIN];
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        int index1 = teamMap.get(team1);
        int index2 = teamMap.get(team2);

        if (index1 == index2) {
            return 0;
        } else if (index1 > index2) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }
        int i = index1 * teamNum + index2 - teams[index1][SUB];
        return remainGames[i];
    }

    public boolean isEliminated(String team) {
        validateTeam(team);
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        return null;
    }

    private int genFlowNetwork() {
        for(int i = 0; i < remainGames.length; i++) {

        }
        return 0;
    }

    private void validateTeam(String team) {
        if (!teamMap.containsKey(team)) {
            throw new IllegalArgumentException("Invalid Team " + team);
        }
        return;
    }
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(null);

    }
}
