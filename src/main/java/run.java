import Functions.*;
import IO.Input.CSV.ReadProjects;
import IO.Input.CSV.ReadStudents;
import IO.Output.CSV.WriteToCSVFile;
import Objects.Project;
import Objects.SolutionPermutation;
import Objects.Student;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

import static javax.swing.JOptionPane.showMessageDialog;

public class run {
    private List<Student> studentList;
    private List<Project> projectList;
    private Visualization visual;
    private SolutionPermutation output;

    public static void main(String[] args) {
        run gui = new run();
        gui.createGUI();
    }

    private void createGUI(){
        this.visual = new  Visualization();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Genetic Algorithm", gaPanel(visual));
        tabbedPane.addTab("Simulated Annealing", saPanel(visual));
        tabbedPane.addTab("Hill Climbing", hcPanel(visual));

        final JFrame mainWindow = new JFrame("Chorazos");
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setSize(1000, 720);
        mainWindow.setLayout(new GridLayout(1, 2));
        mainWindow.setResizable(false);
        mainWindow.add(tabbedPane);
        mainWindow.add(visual);
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    private JPanel gaPanel(Visualization visual) {
        JPanel container = new JPanel();
        GeneticAlgorithm ga = new GeneticAlgorithm(visual);
        JCheckBox useDefault = new JCheckBox("Use Default Values", true);
        List<JSlider> JSliderList = new ArrayList<>();

        JLabel pLabel = new JLabel();
        pLabel.setText("Population Size");
        JLabel mLabel = new JLabel();
        mLabel.setText("Percentage to be culled");
        JLabel nLabel = new JLabel();
        nLabel.setText("Percentage to be mated");
        JLabel rLabel = new JLabel();
        rLabel.setText("Number of Iterations without improvement before terminating");
        JLabel eLabel = new JLabel();
        eLabel.setText("Chance of Mutation");
        JLabel gpaLabel = new JLabel();
        gpaLabel.setText("Influence of GPA");

        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;
        c.weightx = 0.5;
        c.ipady = 40;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(loadFileButton(), c);
        JSlider p = new JSlider(JSlider.HORIZONTAL, 1000, 40000, 1000);

        p.setMajorTickSpacing(2000);
        p.setPaintTicks(true);

        Hashtable<Integer, JLabel> pLabelTable = new Hashtable<Integer, JLabel>();
        pLabelTable.put( new Integer( 1000 ), new JLabel("1,000") );
        pLabelTable.put( new Integer( 20000 ), new JLabel("20,000") );
        pLabelTable.put( new Integer( 40000 ), new JLabel("40,000") );
        p.setLabelTable( pLabelTable );

        p.setPaintLabels(true);
        c.gridy = 1;
        container.add(useDefault, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 0;
        c.ipady = 0;
        c.gridy = 2;
        container.add(pLabel, c);
        c.gridy = 3;
        container.add(p, c);
        JSlider m = new JSlider();

        m.setMajorTickSpacing(10);
        m.setPaintTicks(true);

        Hashtable<Integer, JLabel> mLabelTable = new Hashtable<Integer, JLabel>();
        mLabelTable.put(new Integer(0), new JLabel("0%"));
        mLabelTable.put(new Integer(50), new JLabel("50%"));
        mLabelTable.put(new Integer(100), new JLabel("100%"));
        m.setLabelTable(mLabelTable);

        m.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 4;
        container.add(mLabel, c);
        c.gridy = 5;
        container.add(m, c);
        JSlider n = new JSlider();

        n.setMajorTickSpacing(10);
        n.setPaintTicks(true);

        Hashtable<Integer, JLabel> nLabelTable = new Hashtable<Integer, JLabel>();
        nLabelTable.put(new Integer(0), new JLabel("0%"));
        nLabelTable.put(new Integer(50), new JLabel("50%"));
        nLabelTable.put(new Integer(100), new JLabel("100%"));
        n.setLabelTable(nLabelTable);

        n.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 6;
        container.add(nLabel, c);
        c.gridy = 7;
        container.add(n, c);
        JSlider r = new JSlider(JSlider.HORIZONTAL, 0, 50, 15);

        r.setMajorTickSpacing(5);
        r.setPaintTicks(true);

        Hashtable<Integer, JLabel> rLabelTable = new Hashtable<Integer, JLabel>();
        rLabelTable.put(new Integer(0), new JLabel("0"));
        rLabelTable.put(new Integer(25), new JLabel("25"));
        rLabelTable.put(new Integer(50), new JLabel("50"));
        r.setLabelTable(rLabelTable);

        r.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 8;
        container.add(rLabel, c);
        c.gridy = 9;
        container.add(r, c);
        JSlider e = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);

        e.setMajorTickSpacing(10);
        e.setPaintTicks(true);

        Hashtable<Integer, JLabel> eLabelTable = new Hashtable<Integer, JLabel>();
        eLabelTable.put(new Integer(0), new JLabel("0%"));
        eLabelTable.put(new Integer(50), new JLabel("50%"));
        eLabelTable.put(new Integer(100), new JLabel("100%"));
        e.setLabelTable(mLabelTable);

        e.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 10;
        container.add(eLabel, c);
        c.gridy = 11;
        container.add(e, c);
        JSlider gpa = new JSlider();

        gpa.setMajorTickSpacing(10);
        gpa.setPaintTicks(true);

        Hashtable<Integer, JLabel> gpaLabelTable = new Hashtable<Integer, JLabel>();
        gpaLabelTable.put(new Integer(0), new JLabel("0"));
        gpaLabelTable.put(new Integer(50), new JLabel("0.5"));
        gpaLabelTable.put(new Integer(100), new JLabel("1"));
        gpa.setLabelTable(gpaLabelTable);

        gpa.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 12;
        container.add(gpaLabel, c);
        c.gridy = 13;
        container.add(gpa, c);


        if(useDefault.isSelected() == true) {
            p.setEnabled(false);
            m.setEnabled(false);
            n.setEnabled(false);
            r.setEnabled(false);
            e.setEnabled(false);
            gpa.setEnabled(false);
        }


        JSliderList.add(p);
        JSliderList.add(m);
        JSliderList.add(n);
        JSliderList.add(r);
        JSliderList.add(e);
        JSliderList.add(gpa);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridy = 14;
        c.anchor = GridBagConstraints.PAGE_END;

        container.add(startSearchButton(ga, useDefault, JSliderList), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        container.add(exportButton(), c);

        p.setEnabled(!useDefault.isSelected());
        m.setEnabled(!useDefault.isSelected());
        n.setEnabled(!useDefault.isSelected());
        r.setEnabled(!useDefault.isSelected());
        e.setEnabled(!useDefault.isSelected());
        gpa.setEnabled(!useDefault.isSelected());

        useDefault.addActionListener(action -> {
            p.setEnabled(!useDefault.isSelected());
            m.setEnabled(!useDefault.isSelected());
            n.setEnabled(!useDefault.isSelected());
            r.setEnabled(!useDefault.isSelected());
            e.setEnabled(!useDefault.isSelected());
            gpa.setEnabled(!useDefault.isSelected());

            container.revalidate();
        });

        return container;
    }

    private JPanel saPanel(Visualization visual) {
        JPanel container = new JPanel();
        SimulatedAnnealing sa = new SimulatedAnnealing(visual);

        List<JSlider> JSliderList = new ArrayList<>();
        JCheckBox useDefault = new JCheckBox("Use Default Values", true);


        JLabel changesLabel = new JLabel();
        changesLabel.setText("Number of Changes per iteration");
        JLabel tempLabel = new JLabel();
        tempLabel.setText("Initial Temperature");
        JLabel changeRateLabel = new JLabel();
        changeRateLabel.setText("Change in Temperature");
        JLabel gpaLabel = new JLabel();
        gpaLabel.setText("Influence of GPA");

        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;
        c.weightx = 0.5;
        c.ipady = 40;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(loadFileButton(), c);
        JSlider changes = new JSlider();

        changes.setMajorTickSpacing(10);
        changes.setPaintTicks(true);

        Hashtable<Integer, JLabel> changesLabelTable = new Hashtable<Integer, JLabel>();
        changesLabelTable.put( new Integer( 0 ), new JLabel("0") );
        changesLabelTable.put( new Integer( 50 ), new JLabel("50") );
        changesLabelTable.put( new Integer( 100 ), new JLabel("100") );
        changes.setLabelTable( changesLabelTable );

        changes.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 0;
        c.ipady = 0;
        c.gridy = 1;
        container.add(useDefault, c);
        c.gridy = 2;
        container.add(changesLabel, c);
        c.gridy = 3;
        container.add(changes, c);
        JSlider temp = new JSlider(JSlider.HORIZONTAL, 0, 1000000, 500000);

        temp.setMajorTickSpacing(100000);
        temp.setPaintTicks(true);

        Hashtable<Integer, JLabel> tempLabelTable = new Hashtable<Integer, JLabel>();
        tempLabelTable.put( new Integer( 0 ), new JLabel("0") );
        tempLabelTable.put( new Integer( 500000 ), new JLabel("500,000") );
        tempLabelTable.put( new Integer( 1000000 ), new JLabel("1,000,000") );
        temp.setLabelTable( tempLabelTable );

        temp.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 4;
        container.add(tempLabel, c);
        c.gridy = 5;
        container.add(temp, c);
        JSlider changeRate = new JSlider(JSlider.HORIZONTAL, 0, 10000, 9999);

        changeRate.setMajorTickSpacing(1000);
        changeRate.setPaintTicks(true);

        Hashtable<Integer, JLabel> changeRateLabelTable = new Hashtable<Integer, JLabel>();
        changeRateLabelTable.put( new Integer( 0 ), new JLabel("0") );
        changeRateLabelTable.put( new Integer( 5000 ), new JLabel("0.5") );
        changeRateLabelTable.put( new Integer( 10000 ), new JLabel("1") );
        changeRate.setLabelTable( changeRateLabelTable );

        changeRate.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 6;
        container.add(changeRateLabel, c);
        c.gridy = 7;
        container.add(changeRate, c);
        JSlider gpa = new JSlider();

        gpa.setMajorTickSpacing(10);
        gpa.setPaintTicks(true);

        Hashtable<Integer, JLabel> gpaLabelTable = new Hashtable<Integer, JLabel>();
        gpaLabelTable.put( new Integer( 0 ), new JLabel("0") );
        gpaLabelTable.put( new Integer( 50 ), new JLabel("0.5") );
        gpaLabelTable.put( new Integer( 100 ), new JLabel("1") );
        gpa.setLabelTable( gpaLabelTable );

        gpa.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 8;
        container.add(gpaLabel, c);
        c.gridy = 9;
        container.add(gpa, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridy = 10;
        c.anchor = GridBagConstraints.PAGE_END;

        JSliderList.add(changes);
        JSliderList.add(temp);
        JSliderList.add(changeRate);
        JSliderList.add(gpa);

        container.add(startSearchButton(sa, useDefault, JSliderList), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        container.add(exportButton(), c);

        temp.setEnabled(!useDefault.isSelected());
        changeRate.setEnabled(!useDefault.isSelected());
        changes.setEnabled(!useDefault.isSelected());
        gpa.setEnabled(!useDefault.isSelected());

        useDefault.addActionListener(action -> {
            temp.setEnabled(!useDefault.isSelected());
            changeRate.setEnabled(!useDefault.isSelected());
            changes.setEnabled(!useDefault.isSelected());
            gpa.setEnabled(!useDefault.isSelected());

            container.revalidate();
        });

        return container;
    }

    private JPanel hcPanel(Visualization visual) {
        JPanel container = new JPanel();
        HillClimbing hc = new HillClimbing(visual);

        List<JSlider> JSliderList = new ArrayList<>();

        JCheckBox useDefault = new JCheckBox("Use Default Values", true);


        JLabel changesLabel = new JLabel();
        changesLabel.setText("Number of Changes per iteration");
        JLabel gpaLabel = new JLabel();
        gpaLabel.setText("Influence of GPA");
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;
        c.weightx = 0.5;
        c.ipady = 40;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(loadFileButton(), c);
        JSlider changes = new JSlider();

        changes.setMajorTickSpacing(10);
        changes.setPaintTicks(true);

        Hashtable<Integer, JLabel> changesLabelTable = new Hashtable<Integer, JLabel>();
        changesLabelTable.put( new Integer( 0 ), new JLabel("0") );
        changesLabelTable.put( new Integer( 50 ), new JLabel("50") );
        changesLabelTable.put( new Integer( 100 ), new JLabel("100") );
        changes.setLabelTable( changesLabelTable );

        changes.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 0;
        c.ipady = 0;
        c.gridy = 1;
        container.add(useDefault, c);
        c.gridy = 2;
        container.add(changesLabel, c);
        c.gridy = 3;
        container.add(changes, c);
        JSlider gpa = new JSlider();

        gpa.setMajorTickSpacing(10);
        gpa.setPaintTicks(true);

        Hashtable<Integer, JLabel> gpaLabelTable = new Hashtable<Integer, JLabel>();
        gpaLabelTable.put( new Integer( 0 ), new JLabel("0") );
        gpaLabelTable.put( new Integer( 50 ), new JLabel("0.5") );
        gpaLabelTable.put( new Integer( 100 ), new JLabel("1") );
        gpa.setLabelTable( gpaLabelTable );

        gpa.setPaintLabels(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 4;
        container.add(gpaLabel, c);
        c.gridy = 5;
        container.add(gpa, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridy = 6;
        c.anchor = GridBagConstraints.PAGE_END;


        JSliderList.add(changes);
        JSliderList.add(gpa);

        container.add(startSearchButton(hc, useDefault, JSliderList), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        container.add(exportButton(), c);

        changes.setEnabled(!useDefault.isSelected());
        gpa.setEnabled(!useDefault.isSelected());

        useDefault.addActionListener(action -> {
            changes.setEnabled(!useDefault.isSelected());
            gpa.setEnabled(!useDefault.isSelected());

            container.revalidate();
        });

        return container;
    }

    private JButton loadFileButton() {
        JButton loadFile = new JButton("Load Inputs");
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        loadFile.addActionListener(e -> {
            this.projectList = new ArrayList<>();
            this.studentList = new ArrayList<>();

            try {
                fc.setDialogTitle("Select Project List");
                int r = fc.showOpenDialog(null);

                if (r == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    ReadProjects.Read(file, this.projectList);
                }

                fc.setDialogTitle("Select Student List");
                r = fc.showOpenDialog(null);

                if (r == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    ReadStudents.Read(file, this.studentList, this.projectList);
                }

                this.visual.loadValues(this.studentList, this.projectList);
            } catch (FileNotFoundException fileNotFoundException) {
                showMessageDialog(null, "Invalid File\nPlease reselect a valid file");
            }
        });

        return loadFile;
    }

    private JButton startSearchButton(Search algorithm, JCheckBox defaultValues, List<JSlider> sliders) {
        JButton runButton = new JButton("Run");

        runButton.addActionListener(e -> {
            if(projectList == null || studentList == null) {
                showMessageDialog(null, "No inputs to run\nPlease input values to create a solution");
            } else {
                algorithm.resetParameters();
                if(!defaultValues.isSelected())
                    algorithm.setParameters(sliders);

                this.output = algorithm.solve(this.studentList, this.projectList);
                int[] preferenceArray = output.getPreferenceInfo();
                showMessageDialog(null, "Run Complete\n" + preferenceArray[1] + " students got an average of their " + preferenceArray[0] + " choice, while " + preferenceArray[2] + " students did not get any of their choices");
            }
        });

        return runButton;
    }

    private JButton exportButton() {
        JButton runButton = new JButton("Export");

        runButton.addActionListener(e -> {
            if(this.output == null) {
                showMessageDialog(null, "No output to export.\nPlease run to create a solution");
            } else {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                try {
                    int r = fc.showSaveDialog(null);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        WriteToCSVFile.Write(this.output, file);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        return runButton;
    }
}
