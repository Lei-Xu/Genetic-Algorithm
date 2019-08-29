import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * COMP 6731 Assignment 2
 * Date: 2018-11-04
 * Author: Lei Xu
 * Class used to implement the genetic algorithm
 */
public class GeneticAlgorithm {

    /**
     * Initiate population
     */
    public ArrayList<Chromosome> initPopulation(int populationSize) {

        ArrayList<Chromosome> population = new ArrayList<Chromosome>();
        for(int i = 0; i < populationSize; i++) {
            Chromosome chromosome;
            do {
                chromosome = new Chromosome();
            } while(!isValid(chromosome));
            population.add(chromosome);
        }

        System.out.println("\nInitial Population: ");
        displayPhaseInfo(population);

        return population;
    }

    /**
     * Roulette Wheel Selection method.
     */
    public ArrayList<Chromosome> selector(ArrayList<Chromosome> parentPopulation, int childPopulationSize) {

        ArrayList<Chromosome> childPopulation = new ArrayList<Chromosome>(childPopulationSize);
        double totalFitness = 0.0;
        double[] fitness = new double[parentPopulation.size()];
        for(Chromosome chromosome : parentPopulation) {
            totalFitness += chromosome.calculateFittness();
        }

        for(int index = 0; index < parentPopulation.size(); index++) {
            fitness[index] = parentPopulation.get(index).calculateFittness() / totalFitness;
        }

        for(int i = 1; i < fitness.length; i++) {
            fitness[i] = fitness[i - 1] + fitness[i];
        }

        for(int i = 0; i < childPopulationSize; i++) {
            Random random = new Random();
            double probability = random.nextDouble();
            int choose;

            for(choose = 1; choose < fitness.length; choose++) {
                if(probability < fitness[choose]) {
                    break;
                }
            }

            childPopulation.add(parentPopulation.get(choose));
        }


        System.out.println("Selection: ");
        displayPhaseInfo(childPopulation);

        return childPopulation;
    }

    /**
     * Crossover function
     */
    public ArrayList<Chromosome> crossover(ArrayList<Chromosome> parentPopulation, double crossRate) {

        ArrayList<Chromosome> childPopulation = new ArrayList<Chromosome>();
        ArrayList<Chromosome> individualsSelected = new ArrayList<Chromosome>();
        childPopulation.addAll(parentPopulation);
        Random random = new Random();
        for(int i = 0; i < parentPopulation.size() / 2; i++) {
            if(crossRate > random.nextDouble()) {
                int m = 0;
                int n = 0;
                do {
                    m = random.nextInt(parentPopulation.size());
                    n = random.nextInt(parentPopulation.size());
                } while (m == n);
                int position = random.nextInt(Constants.CHROMOSOME_LENGTH-1);
                Chromosome parent1 = parentPopulation.get(m);
                Chromosome parent2 = parentPopulation.get(n);
                individualsSelected.add(parent1);
                individualsSelected.add(parent2);
                String child1;
                String child2;
                child1 = parent1.getChromosome().substring(0, position+1) + parent2.getChromosome().substring(position+1, Constants.CHROMOSOME_LENGTH);
                child2 = parent2.getChromosome().substring(0, position+1) + parent1.getChromosome().substring(position+1, Constants.CHROMOSOME_LENGTH);
                Chromosome childChromosome1 = new Chromosome(child1);
                Chromosome childChromosome2 = new Chromosome(child2);

                if(isValid(childChromosome1)) {
                    childPopulation.add(childChromosome1);
                }

                if(isValid(childChromosome2)) {
                    childPopulation.add(childChromosome2);
                }
            }

        }

        System.out.println("\nCrossover: ");
        System.out.println("Individuals selected for crossover: " + chromosomesInPopulation(individualsSelected));
        displayPhaseInfo(childPopulation);

        return childPopulation;
    }

    /**
     * Mutation function
     */
    public ArrayList<Chromosome> mutation(ArrayList<Chromosome> parentPopulation, double mutationRate) {

        String mutationProbability;
        int mutationCount = 0;
        ArrayList<Chromosome> individualsSelected = new ArrayList<Chromosome>();

        Random random = new Random();

        ArrayList<Chromosome> bestChromosomes = selectBestChromosomes(parentPopulation);

        do {
            for(Chromosome chromosome : parentPopulation) {
                if(mutationRate > random.nextDouble()) {
                    int position = random.nextInt(Constants.CHROMOSOME_LENGTH);
                    chromosome.selfMutation(position);
                    individualsSelected.add(chromosome);
                    mutationCount++;
                }
            }
        } while(mutationCount <= 0);

        DecimalFormat df = new DecimalFormat("0.00");
        mutationProbability = df.format((double)mutationCount / parentPopulation.size());

        System.out.println("\nMutation: ");
        System.out.println("Mutation Probability: " + mutationProbability);
        System.out.println("Individuals selected for mutation: " + chromosomesInPopulation(individualsSelected));
        System.out.println("After mutation, the population size: " + parentPopulation.size());

        deleteInvalidChromosome(parentPopulation);

        parentPopulation.addAll(bestChromosomes);

        System.out.println("After deleting invalid chromosomes and adding the best chromosome, the final population size: " + parentPopulation.size());

        displayPhaseInfo(parentPopulation);

        return parentPopulation;
    }

    public ArrayList<Chromosome> selectBestChromosomes(ArrayList<Chromosome> population) {
        ArrayList<Chromosome> bestChromosomes = new ArrayList<Chromosome>();
        Chromosome bestChromosome = new Chromosome(population.get(0).getChromosome());
        bestChromosomes.add(bestChromosome);
        for(Chromosome chromosome : population) {
            double chromosomeFitnessScore = chromosome.calculateFittness();
            double bestChromosomeFitnessScore = bestChromosome.calculateFittness();
            if(chromosomeFitnessScore > bestChromosomeFitnessScore) {
                bestChromosomes.clear();
                bestChromosome = new Chromosome(chromosome.getChromosome());
                bestChromosomes.add(bestChromosome);
            } else if(chromosomeFitnessScore == bestChromosomeFitnessScore) {
                boolean flag = true;
                for(Chromosome chromosomeInBest : bestChromosomes) {
                    if(chromosomeInBest.getChromosome().equals(chromosome.getChromosome())) {
                        flag = false;
                        break;
                    }
                }
                while(flag) {
                    bestChromosomes.add(chromosome);
                    flag =false;
                }
            }
        }

        return bestChromosomes;
    }

    /**
     * Check whether the chromosome is valid
     */
    public boolean isValid(Chromosome chromosome) {
        int a = chromosome.getGenes()[0];
        int b = chromosome.getGenes()[1];
        int c = chromosome.getGenes()[2];
        int d = chromosome.getGenes()[3];

        boolean[] requirenments = new boolean[Constants.CONSTRAINT_FACTORS.length];

        for(int i = 0; i < Constants.CONSTRAINT_FACTORS.length; i++) {
            requirenments[i] = Constants.CONSTRAINT_FACTORS[i][0] * a
                    + Constants.CONSTRAINT_FACTORS[i][1] * b
                    + Constants.CONSTRAINT_FACTORS[i][2] * c
                    + Constants.CONSTRAINT_FACTORS[i][3] * d
                    <= Constants.CONSTRAINT_FACTORS[i][4];
        }

        boolean constraint = requirenments[0];

        for(int i = 1; i < requirenments.length; i++) {
            constraint = constraint && requirenments[i];
        }

        return constraint;
    }

    /**
     * Delete the invalid chromosome
     */
    private void deleteInvalidChromosome(ArrayList<Chromosome> population) {
        ArrayList<Chromosome> invalidChromosomes = new ArrayList<Chromosome>();
        for(Chromosome chromosome : population) {
            if(!isValid(chromosome)) {
                invalidChromosomes.add(chromosome);
            }
        }
        for(Chromosome invalidChromosome : invalidChromosomes) {
            population.remove(invalidChromosome);
        }
    }

    /**
     * Only for displaying the info of each phase
     */
    private void displayPhaseInfo(ArrayList<Chromosome> population) {

        System.out.println("Population size: " + population.size());

        System.out.println("Population members (Format: [chromosome : fitness score]): " + chromosomesInPopulation(population));
    }

    /**
     * Return chromosomes in the population
     */
    private String chromosomesInPopulation(ArrayList<Chromosome> population) {
        ArrayList<String> popualtionString = new ArrayList<String>();
        for(Chromosome chromosome : population) {
            popualtionString.add(chromosome.getChromosome() + " : " + chromosome.calculateFittness());
        }
        return popualtionString.toString();
    }

    //Just for testing
    public void displayPopulation(ArrayList<Chromosome> population) {
        for(Chromosome chromosome : population) {
            System.out.println(chromosome.getChromosome());
        }
    }
}
