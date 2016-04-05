import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class knn {
    private static List<knode> nodes = new ArrayList<knode>();
    private static int k = 3;
    private static List<Double> correct = new ArrayList<Double>();
    private static double avg_correct = 0;

    public static void readfiles() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("randomData"));
            String line = null;
            int sum = 0;
            while ((line = reader.readLine()) != null) {
                String[] splits = line.split(",");
                knode node = new knode();
                node.setType(splits[0]);
                node.setLifestyle(splits[1]);
                node.setVacation(Double.parseDouble(splits[2]));
                node.seteCredit(Double.parseDouble(splits[3]));
                node.setSalary(Double.parseDouble(splits[4]));
                node.setProperty(Double.parseDouble(splits[5]));
                node.setLabel(splits[6]);
                sum++;
                /*
                 * System.out.println(sum + " " + node.getType() + " " +
                 * node.getLifestyle() + " " + node.getVacation() + " " +
                 * node.geteCredit() + " " + node.getSalary() + " " +
                 * node.getProperty() + " " + node.getLabel());
                 */
                nodes.add(node);
            }
            System.out.println(sum);
            reader.close();
            setdata(sum);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new IllegalArgumentException("Can't read the file");
        }

    }

    public static void setdata(int sum) throws IOException {

        int set = sum / 10;
        for (int j = 0; j < 10; j++) {
            List<knode> trainset = new ArrayList<knode>();
            List<knode> testset = new ArrayList<knode>();
            for (int i = 0; i < set * j; i++) {
                knode node = nodes.get(i);
                trainset.add(node);
            }
            for (int i = set * j; i < set * (j + 1); i++) {
                knode node = nodes.get(i);
                testset.add(node);
            }

            for (int i = set * (j + 1); i < sum; i++) {
                knode node = nodes.get(i);
                trainset.add(node);
            }
            System.out.println("test case " + j);
            // System.out.println(testset.size());
            normalizeTrain(trainset);
            normalizeTest(testset);
            process(trainset, testset);
        }
        double correctness = 0;
        for (int s = 0; s < correct.size(); s++) {
            correctness = correctness + correct.get(s);
        }
        avg_correct  = correctness/10;

    }

    public static void normalizeTrain(List<knode> trainset) {
        knode node = trainset.get(0);
        double minvacation = node.getVacation();
        double maxvacation = node.getVacation();
        double mineCredit = node.geteCredit();
        double maxeCredit = node.geteCredit();
        double minsalary = node.getSalary();
        double maxsalary = node.getSalary();
        double minproperty = node.getProperty();
        double maxproperty = node.getProperty();
        for (int i = 1; i < trainset.size(); i++) {
            knode s = trainset.get(i);
            if (s.getVacation() < minvacation) {
                minvacation = s.getVacation();
            } else if (s.getVacation() > maxvacation) {
                maxvacation = s.getVacation();
            }
            if (s.geteCredit() < mineCredit) {
                mineCredit = s.geteCredit();
            } else if (s.geteCredit() > maxeCredit) {
                maxeCredit = s.geteCredit();
            }
            if (s.getSalary() < minsalary) {
                minsalary = s.getSalary();
            } else if (s.getSalary() > maxsalary) {
                maxsalary = s.getSalary();
            }
            if (s.getProperty() < minproperty) {
                minproperty = s.getProperty();
            } else if (s.getProperty() > maxproperty) {
                maxproperty = s.getProperty();
            }
        }
        for (int i = 0; i < trainset.size(); i++) {
            knode s = trainset.get(i);
            double vacation = s.getVacation();
            vacation = (vacation - minvacation) / (maxvacation - minvacation);
            s.setVacation(vacation);

            double eCredit = s.geteCredit();
            eCredit = (eCredit - mineCredit) / (maxeCredit - mineCredit);
            s.seteCredit(eCredit);

            double salary = s.getSalary();
            salary = (salary - minsalary) / (maxsalary - minsalary);
            s.setSalary(salary);

            double property = s.getProperty();
            property = (property - minproperty) / (maxproperty - minproperty);
            s.setProperty(property);
        }
        /*
         * for (int i = 0; i < trainset.size(); i++) { knode s =
         * trainset.get(i); System.out.println(i + " " + s.getType() + " " +
         * s.getLifestyle() + " " + s.getVacation() + " " + s.geteCredit() + " "
         * + s.getSalary() + " " + s.getProperty() + " " + s.getLabel()); }
         */
    }

    public static void normalizeTest(List<knode> testset) {
        knode node = testset.get(0);
        double minvacation = node.getVacation();
        double maxvacation = node.getVacation();
        double mineCredit = node.geteCredit();
        double maxeCredit = node.geteCredit();
        double minsalary = node.getSalary();
        double maxsalary = node.getSalary();
        double minproperty = node.getProperty();
        double maxproperty = node.getProperty();
        for (int i = 1; i < testset.size(); i++) {
            knode s = testset.get(i);
            if (s.getVacation() < minvacation) {
                minvacation = s.getVacation();
            } else if (s.getVacation() > maxvacation) {
                maxvacation = s.getVacation();
            }
            if (s.geteCredit() < mineCredit) {
                mineCredit = s.geteCredit();
            } else if (s.geteCredit() > maxeCredit) {
                maxeCredit = s.geteCredit();
            }
            if (s.getSalary() < minsalary) {
                minsalary = s.getSalary();
            } else if (s.getSalary() > maxsalary) {
                maxsalary = s.getSalary();
            }
            if (s.getProperty() < minproperty) {
                minproperty = s.getProperty();
            } else if (s.getProperty() > maxproperty) {
                maxproperty = s.getProperty();
            }
        }
        for (int i = 0; i < testset.size(); i++) {
            knode s = testset.get(i);
            double vacation = s.getVacation();
            vacation = (vacation - minvacation) / (maxvacation - minvacation);
            s.setVacation(vacation);

            double eCredit = s.geteCredit();
            eCredit = (eCredit - mineCredit) / (maxeCredit - mineCredit);
            s.seteCredit(eCredit);

            double salary = s.getSalary();
            salary = (salary - minsalary) / (maxsalary - minsalary);
            s.setSalary(salary);

            double property = s.getProperty();
            property = (property - minproperty) / (maxproperty - minproperty);
            s.setProperty(property);
        }
        /*
         * for (int i = 0; i < testset.size(); i++) { knode s = testset.get(i);
         * System.out.println(i + " " + s.getType() + " " + s.getLifestyle() +
         * " " + s.getVacation() + " " + s.geteCredit() + " " + s.getSalary() +
         * " " + s.getProperty() + " " + s.getLabel()); }
         */
    }

    public static void process(List<knode> trainset, List<knode> testset) throws IOException {

        List<String> result = new ArrayList<String>();
        for (int i = 0; i < testset.size(); i++) {
            List<kscore> scores = new ArrayList<kscore>();
            double[] parameters = setvalue();
            knode test = testset.get(i);

            for (int j = 0; j < trainset.size(); j++) {
                knode train = trainset.get(j);
                double type = 0;
                if (train.getType().equals(test.getType())) {
                    type = 1;
                }
                double score = parameters[0] * (1 - type);
                double lifestyle = 0;
                if (train.getLifestyle().equals(test.getLifestyle())) {
                    lifestyle = 1;
                }
                score = score + parameters[1] * (1 - lifestyle);
                double vacation = (train.getVacation() - test.getVacation())
                        * (train.getVacation() - test.getVacation());
                score = score + parameters[2] * vacation;
                double eCredit = (train.geteCredit() - test.geteCredit()) * (train.geteCredit() - test.geteCredit());
                score = score + parameters[3] * eCredit;
                double salary = (train.getSalary() - test.getSalary()) * (train.getSalary() - test.getSalary());
                score = score + parameters[4] * salary;
                double property = (train.getProperty() - test.getProperty())
                        * (train.getProperty() - test.getProperty());
                score = score + parameters[5] * property;
                score = 1 / score;

                kscore finalscore = new kscore();
                // System.out.println(j + " " + score);
                finalscore.setNum(j);
                finalscore.setScore(score);
                finalscore.setLabel(train.getLabel());
                scores.add(finalscore);
            }

            ValComparator compare = new ValComparator();
            Collections.sort(scores, compare);

            kscore first = scores.get(0);
            Map<String, Double> kscores = new HashMap<String, Double>();
            kscores.put(first.getLabel(), first.getScore());
            int num = 1;
            while (num < k) {
                kscore tmp = scores.get(num);
                int flag = 0;

                for (Map.Entry<String, Double> element : kscores.entrySet()) {
                    if (element.getKey().equals(tmp.getLabel())) {
                        double value = element.getValue() + tmp.getScore();
                        element.setValue(value);
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    kscores.put(tmp.getLabel(), tmp.getScore());
                }
                num++;
            }

            List<Map.Entry<String, Double>> resultlist = new ArrayList<Map.Entry<String, Double>>();
            resultlist.addAll(kscores.entrySet());
            kvote kclass = new kvote();
            Collections.sort(resultlist, kclass);

            /*
             * System.out.println("test case " + i); System.out
             * .println(scores.get(0).getNum() + " " + scores.get(0).getScore()
             * + " " + scores.get(0).getLabel()); System.out
             * .println(scores.get(1).getNum() + " " + scores.get(1).getScore()
             * + " " + scores.get(1).getLabel()); System.out
             * .println(scores.get(2).getNum() + " " + scores.get(2).getScore()
             * + " " + scores.get(2).getLabel());
             * System.out.println(resultlist.get(0).getKey() + " " +
             * resultlist.get(0).getValue()); System.out.println();
             */
            result.add(resultlist.get(0).getKey());

        }
        compute(result, testset);
    }

    private static class ValComparator implements Comparator<kscore> {
        public int compare(kscore o1, kscore o2) {
            if (o2.getScore() > o1.getScore())
                return 1;
            else if (o2.getScore() < o1.getScore())
                return -1;
            else if (o2.getNum() > o1.getNum()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private static class kvote implements Comparator<Map.Entry<String, Double>> {
        public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
            if (o2.getValue() > o1.getValue())
                return 1;
            else if (o2.getValue() < o1.getValue())
                return -1;
            else if (o2.getKey().compareTo(o1.getKey()) < 0) {
                return 1;
            } else {
                return -1;
            }

        }
    }

    public static double[] setvalue() {
        double[] result = new double[6];
        result[0] = 1;
        result[1] = 1;
        result[2] = 1;
        result[3] = 1;
        result[4] = 1;
        result[5] = 1;
        return result;
    }

    public static void compute(List<String> result, List<knode> testset) throws IOException {
        int num = 0;
        FileWriter fw = new FileWriter("output.txt", true);
        for (int i = 0; i < result.size(); i++) {
            String label = result.get(i);
            fw.write(label + "\n");
            String truelabel = testset.get(i).getLabel();
            if (label.equals(truelabel)) {
                num++;
            }
        }
        fw.close();
        double correctness = (double) num / result.size();
        System.out.println(num + " " + correctness);
        correct.add(correctness);

    }

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        readfiles();
        System.out.println("avgrage correctness: "+avg_correct);
    }
}
