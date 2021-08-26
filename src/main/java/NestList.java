import java.util.ArrayList;

public class NestList {
    private int reportedNests;
    private ArrayList<NestingSpecies> nests;
    private String lastRotation;
    private String nextRotation;
    private static final String ELIGIBLE_SPECIES = "You can see a list of all possible nesting species by going to this link:";

    public NestList(String lastRotation, String nextRotation){
        this.reportedNests = 0;
        this.nests = new ArrayList<>();
    }

    public void newNest(String species, String park, int generation){
        NestingSpecies newNest = new NestingSpecies(species, park, generation);
        this.nests.add(newNest);
        this.reportedNests++;
        System.out.println("You reported " + species + " at " + park + ". ");
    }

    public void printNests(){
        for(NestingSpecies nest : nests){
            String[] parks = nest.getParks();
            System.out.print("`" + nest.getSpecies() + ":` ");
            for (String park : parks){
                System.out.println(park + ", ");
            }
        }
        System.out.println(ELIGIBLE_SPECIES);
        System.out.println("The next nest rotation will occur on " + nextRotation + ".");
    }

    public void addNest(){

    }
}

