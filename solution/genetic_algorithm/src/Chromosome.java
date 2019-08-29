import java.math.BigDecimal;
import java.util.Random;

/**
 * COMP 6731 Assignment 2
 * Date: 2018-11-04
 * Author: Lei Xu
 * Define Chromosome Class
 */
public class Chromosome {
    private int[] genes = new int[Constants.CHROMOSOME_LENGTH];
    private String chromosome;

    /**
     * Constructor
     */
    public Chromosome() {
        for(int i = 0; i < Constants.CHROMOSOME_LENGTH; i++) {
            this.genes[i] = Math.random() >= 0.5 ? 1 : 0;
        }
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < Constants.CHROMOSOME_LENGTH; i++) {
            sb.append(this.genes[i]);
        }

        this.chromosome = sb.toString();

    }

    /**
     * Constructor with a parameter
     */
    public Chromosome(String chromosome) {
        char[] genesChar = chromosome.toCharArray();
        for(int i = 0; i < genesChar.length; i++) {
            if(Character.isDigit(genesChar[i])) {
                this.genes[i] = Integer.parseInt(String.valueOf(genesChar[i]));
            }
        }
        this.chromosome = chromosome;
    }

    /**
     * Fittness function
     */
    public double calculateFittness() {
        BigDecimal fittnessScoreBD = BigDecimal.valueOf(0.0);
        for(int i = 0; i < Constants.FITTNESS_FACTORS.length; i++) {
            fittnessScoreBD = fittnessScoreBD.add(BigDecimal.valueOf(this.genes[i]).multiply(BigDecimal.valueOf(Constants.FITTNESS_FACTORS[i])));
        }
        return fittnessScoreBD.doubleValue();
    }

    /**
     * Self Mutation
     */
    public void selfMutation(int position) {
        int mutatedGeneValue;
        char[] genes = this.chromosome.toCharArray();
        mutatedGeneValue = genes[position] == '0' ? 1 : 0;
        genes[position] = genes[position] == '0' ? '1' : '0';
        this.setGenes(mutatedGeneValue, position);
        this.chromosome = String.copyValueOf(genes);
    }

    /**
     * Return gene sequence
     */
    public int[] getGenes() {
        return this.genes;
    }

    /**
     * Genes Set Method
     */
    public void setGenes(int geneValue, int position) {
        this.genes[position] = geneValue;
    }

    /**
     * Return chromosome
     */
    public String getChromosome() {
        return this.chromosome;
    }

}
