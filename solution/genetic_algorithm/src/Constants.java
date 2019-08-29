import java.util.ArrayList;

/**
 * COMP 6731 Assignment 2
 * Date: 2018-11-04
 * Author: Lei Xu
 * Class used to store constants in the project
 */
public final class Constants {
    /**The optimal solution*/
    public static ArrayList<Chromosome> optimal_solution = new ArrayList<Chromosome>();
    /**The optional total return*/
    public static double optimal_total_return = 0.0;
    /**The selection number*/
    public static int selection_number = 0;
    /**The length of Gene*/
    public static final int CHROMOSOME_LENGTH = 4;
    /**The size of Population*/
    public static final int POPULATION_SIZE = 100;
    /**The maximum count of same result*/
    public static final int MAX_COUNT_OF_SAME_RESULT = 50;
    /**The rate of crossover (0.8 - 0.95)*/
    public static final double CROSSOVER_RATE = 0.9;
    /**The rate of mutation (0.001 - 0.1)*/
    public static final double MUTATION_RATE = 0.1;
    /**Fittness function factors*/
    public static final double[] FITTNESS_FACTORS = {0.2, 0.3, 0.5, 0.1};
    /**Constraints factors*/
    public static final double[][] CONSTRAINT_FACTORS = {{0.5, 1.0, 1.5, 0.1, 3.1}, {0.3, 0.8, 1.5, 0.4, 2.5}, {0.2, 0.2, 0.3, 0.1, 0.4}};
}
