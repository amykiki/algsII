import edu.princeton.cs.algs4.StdOut;

public class BaseBallEliminationTest {

    public static String genFile() {
        String filePath = "D:\\codeProject\\git_repo\\algsII\\test-data\\datas\\baseball-testing\\baseball\\";
        String fileName = "teams5.txt";
        return filePath + fileName;
    }

    public static void testElimination() {
        BaseballElimination division = new BaseballElimination(genFile());
        for (String team : division.teams()) {
            StdOut.println(team + " WIN " + division.wins(team) + " LOSE " + division.losses(team) + " REMAIN " + division.remaining(team));
            for (String otherTeam : division.teams()) {
                if (otherTeam.equals(team)) {
                    continue;
                }
                StdOut.print(otherTeam + "-" + division.against(team, otherTeam) + " " );
            }
            StdOut.println();
        }
    }
    public static void main(String[] args) {
        testElimination();
    }
}
