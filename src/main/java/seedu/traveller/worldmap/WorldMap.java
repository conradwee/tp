package seedu.traveller.worldmap;

import seedu.traveller.worldmap.exceptions.EmptyVertexException;
import seedu.traveller.worldmap.exceptions.NonZeroDistanceException;
import seedu.traveller.worldmap.exceptions.WorldMapException;
import seedu.traveller.worldmap.exceptions.NonStringDistanceException;


public class WorldMap {
    protected static GraphList graphList = new GraphList();
    protected static DataLoader loader = new DataLoader();
    protected static Logic logic = new Logic();

    public static void initWorldMap() {
        try {
            graphList = loader.readData();
        } catch (WorldMapException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void altWorldMap() {
        try {
            graphList = loader.readAltData();
        } catch (WorldMapException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printWorld() {
        for (String countryCode : graphList.getNameArray()) {
            System.out.print(countryCode + " ");
        }
        System.out.println("");
        int numberOfCountries = graphList.getNameArray().size();
        System.out.println(numberOfCountries);
        Country startCountry;
        Country endCountry;
        for (int i = 0; i < numberOfCountries; i++) {
            for (int j = 0; j < numberOfCountries; j++) {
                startCountry = graphList.getVertexArray().get(i);
                endCountry = graphList.getVertexArray().get(j);
                int startCountryKey = startCountry.getKey();
                int endCountryKey = endCountry.getKey();
                System.out.println("From : " + startCountry + " To : " + endCountry
                        + " is " + graphList.getEdgeMatrix()[startCountryKey][endCountryKey]);
            }
        }
    }

    public static MinCalcResult calcMinDistance(String sourceCountryName, String targetCountryName)
            throws EmptyVertexException {
        Country sourceCountry = getCountry(sourceCountryName);
        Country targetCountry = getCountry(targetCountryName);
        if (sourceCountry.getKey() == -1 || targetCountry.getKey() == -1) {
            MinCalcResult result = new MinCalcResult(sourceCountry, targetCountry, null, null);
            result.setError();
            return result;
        }
        logic.computeSource(sourceCountry, graphList);
        return logic.getToGoal(sourceCountry,targetCountry);
    }

    public static MinCalcResult calcMinCost(String sourceCountryName, String targetCountryName)
            throws EmptyVertexException {
        altWorldMap();

        Country sourceCountry = getCountry(sourceCountryName);
        Country targetCountry = getCountry(targetCountryName);
        if (sourceCountry.getKey() == -1 || targetCountry.getKey() == -1) {
            MinCalcResult result = new MinCalcResult(sourceCountry, targetCountry, null, null);
            result.setError();
            return result;
        }
        logic.computeSource(sourceCountry, graphList);
        MinCalcResult minResult = logic.getToGoal(sourceCountry,targetCountry);

        initWorldMap();

        return minResult;
    }

    public static Country getCountry(String countryName) throws EmptyVertexException {
        Country country;
        try {
            country = graphList.findVertex(countryName);
            return country;
        } catch (EmptyVertexException e) {
            System.out.println(e.getMessage());
        }
        throw new EmptyVertexException(countryName);
    }

    public static void distanceNonZero(double dist) throws NonZeroDistanceException {
        if (dist < 0.1) {
            throw new NonZeroDistanceException(dist);
        }
    }

    public static void distanceNonString(String rawDist)
            throws NonStringDistanceException {
        try {
            double dist = Double.parseDouble(rawDist);
        } catch (NumberFormatException e) {
            throw new NonStringDistanceException(rawDist);
        }
    }

    public static void editMap(Double dist, String sourceCountryName, String targetCountryName) {
        try {
            Country sourceCountry = getCountry(sourceCountryName);
            Country targetCountry = getCountry(targetCountryName);

            graphList.modifyEdge(dist, sourceCountry, targetCountry);
            distanceNonZero(dist);
        } catch (EmptyVertexException | NonZeroDistanceException e) {
            System.out.println(e.getMessage());
        }
    }
}
