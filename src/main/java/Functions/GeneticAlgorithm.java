package Functions;

import Objects.*;

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.*;

public class GeneticAlgorithm {
    private final int P = 1000; //  Population size
    private final double M = 0.1;   //  Cull bottom M%
    private final double N = 0.1;   //  Mate top N%
    private final int R = 10;   //  Stop after R iterations without improvement

    public List<SolutionPermutation> generateInitialPopulation(List<Student> studentList, List<Project> projectList) {
        List<SolutionPermutation> populationList = new ArrayList<>();

        for(int i = 0; i < P; i++){
            List<Solution> solutionList = new ArrayList<Solution>();

            for (Student s : studentList) {
                solutionList.add(new Solution(s, projectList, solutionList));
            }

            SolutionPermutation sp = new SolutionPermutation(solutionList);
            populationList.add(sp);
        }

        return populationList;
    }

    private void cull(List<SolutionPermutation> population) {
        //  sort();
        int numberToCull = (int) (population.size() * M);

        for(int i=0; i<numberToCull; i++) {
            population.remove(population.size()-1);
        }
    }
}
