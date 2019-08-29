import java.util.ArrayList;
import java.util.Random;

/**
 * COMP 6731 Assignment 2
 * Date: 2018-11-04
 * Author: Lei Xu
 * Main class to run the whole project
 */
public class Assign2Solution {

    public static void main(String[] args) {

        ArrayList<Chromosome> bestChromosomes = new ArrayList<Chromosome>();
        int iterationCount = 1;
        int countOfSameResult = 0;
        double worestResult = 1.0;
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ArrayList<Chromosome> population = ga.initPopulation(Constants.POPULATION_SIZE);

        Random random = new Random();

        ArrayList<Chromosome> populationAfterMutation = new ArrayList<Chromosome>();

        while(countOfSameResult != Constants.MAX_COUNT_OF_SAME_RESULT) {

            ArrayList<Chromosome> populationAfterSelection = new ArrayList<Chromosome>();
            ArrayList<Chromosome> populationAfterCrossover = new ArrayList<Chromosome>();

            System.out.println("\n----------No." + iterationCount + " Iteration----------\n");

            if(Constants.selection_number == 0) {
                int randomFactor = random.nextInt(population.size());
                Constants.selection_number = (randomFactor == 0 || randomFactor == 1) ? randomFactor + 2 : randomFactor;

                populationAfterSelection.addAll(ga.selector(population, Constants.selection_number));
            } else {
                if(populationAfterMutation.size() < 2 || worestResult == 0.0) {
                    ArrayList<Chromosome> newPopulations = ga.initPopulation(Constants.POPULATION_SIZE);
                    populationAfterMutation.addAll(newPopulations);
                }
                int randomFactor = random.nextInt(populationAfterMutation.size());
                Constants.selection_number = (randomFactor == 0 || randomFactor == 1) ? randomFactor + 2 : randomFactor;
                populationAfterSelection.addAll(ga.selector(populationAfterMutation, Constants.selection_number));
                populationAfterMutation.clear();
            }

            populationAfterCrossover.addAll(ga.crossover(populationAfterSelection, Constants.CROSSOVER_RATE));

            populationAfterMutation.addAll(ga.mutation(populationAfterCrossover, Constants.MUTATION_RATE));

            if(bestChromosomes.isEmpty()) {
                bestChromosomes.addAll(ga.selectBestChromosomes(populationAfterMutation));
            } else {
                bestChromosomes.clear();
                bestChromosomes.addAll(ga.selectBestChromosomes(populationAfterMutation));
            }

            if(!bestChromosomes.isEmpty()) {

                String optimalSolution = bestChromosomes.get(0).getChromosome();
                double optimalReturnOfIteration = bestChromosomes.get(0).calculateFittness();
                worestResult = optimalReturnOfIteration == 0.0 ? 0.0 : worestResult;

                if(Constants.optimal_total_return == optimalReturnOfIteration) {
                    countOfSameResult++;
                } else if(Constants.optimal_total_return < optimalReturnOfIteration) {
                    Constants.optimal_total_return = optimalReturnOfIteration;
                    countOfSameResult = 0;
                } else {
                    countOfSameResult = 0;
                }

//                displayPopulation(bestChromosomes);

                System.out.println("\nThe optimal solution is --> " + optimalSolution + " <--");
                System.out.println("The optimal toatl return is --> " + optimalReturnOfIteration + " <--");
            }

            iterationCount++;
        }

        System.out.println("\n----------The final result----------");
        if(!bestChromosomes.isEmpty()) {
            System.out.println("\nThe optimal solution is --> " + bestChromosomes.get(0).getChromosome() + " <--");
            System.out.println("The optimal toatl return is --> " + bestChromosomes.get(0).calculateFittness() + " <--");
        }

    }

    //Just for testing
    public static void displayPopulation(ArrayList<Chromosome> population) {
        for(Chromosome chromosome : population) {
            System.out.println(chromosome.getChromosome());
        }
    }
}
