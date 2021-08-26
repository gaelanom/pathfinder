public class NestingSpecies {
    private String species;
    private int generation;
    private String[] parks;
    private int reports;

    public NestingSpecies(String species, String park, int generation){
        this.species = species;
        this.generation = generation;
        this.parks = new String[10];
        parks[0] = park;
        this.reports = 0;
    }

    public String getSpecies() {
        return species;
    }

    public int getGeneration() {
        return generation;
    }

    public void addPark(String parkname) {
        this.parks[reports] = parkname;
    }

    public String[] getParks(){
        return this.parks;
    }

}
