package classifierss;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import weka.*;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.*;
import weka.filters.unsupervised.attribute.*;

public class Classifiers 
{
	public static void main(String[] args) 
	{
		try {
			
			BufferedReader in= new BufferedReader(new FileReader("/home/vishakh/Downloads/ReutersCorn-Train.arff"));
			Instances dataRaw=new Instances(in);
			//System.out.println("Attributes : "+dataRaw.numAttributes()+"\n Instances : "+dataRaw.numInstances() );
			StringToWordVector filter = new StringToWordVector();
		    filter.setInputFormat(dataRaw);
		    Instances dataFiltered = Filter.useFilter(dataRaw, filter);
		    //System.out.println("Attributes : "+dataFiltered.numAttributes()+"\n Instances : "+dataFiltered.numInstances() +"\nSample Instance:\n"+dataFiltered.firstInstance().toString());
		    dataFiltered.setClassIndex(0);
		    System.out.println("Training Data Set");
		    
		    BufferedReader in2= new BufferedReader(new FileReader("/home/vishakh/Downloads/ReutersCorn-Test.arff"));
			Instances dataRaw2=new Instances(in2);
			//System.out.println("Attributes : "+dataRaw.numAttributes()+"\n Instances : "+dataRaw.numInstances() );
			StringToWordVector filter2 = new StringToWordVector();
		    filter2.setInputFormat(dataRaw2);
		    Instances dataFiltered2 = Filter.useFilter(dataRaw2, filter2);
		    dataFiltered2.setClassIndex(0);
		    System.out.println("Test Data Set");
		    
		    System.out.println("Model Select:\n 1 - J48\n 2 - Naive Bayes\n 3 - Logistic\n 4 - LibSVM");
		    Scanner s=new Scanner(System.in);
		    int ch=s.nextInt();
		    if(ch==1)
		    {
			    J48 classifier = new J48();
			    classifier.buildClassifier(dataFiltered);
			    System.out.println("\n\nClassifier model:\n\n" + classifier);
			    System.out.println("Testing on ReutersCorn-Test.arff:");
			    Evaluation eTest = new Evaluation(dataFiltered);
		        /*eTest.crossValidateModel(classifier, dataFiltered, 10, new Random(1));
		         String strSummary = eTest.toSummaryString();
		        System.out.println(strSummary);*/
			    eTest.evaluateModel(classifier, dataFiltered2);
			    System.out.println(eTest.toSummaryString());
		    }
		    else if(ch==2)
		    {
		    	NaiveBayes classifier=new NaiveBayes();
		    	classifier.buildClassifier(dataFiltered);
			    System.out.println("\n\nClassifier model:\n\n" + classifier);
			    System.out.println("Testing on ReutersCorn-Test.arff:");
			    Evaluation eTest = new Evaluation(dataFiltered);
		        eTest.crossValidateModel(classifier, dataFiltered, 10, new Random(1));
		        String strSummary = eTest.toSummaryString();
		        System.out.println(strSummary);
			    //eTest.evaluateModel(classifier, dataFiltered2);
			    //System.out.println(eTest.toSummaryString());
		    }
		    else if(ch==3)
		    {
		    	Logistic classifier = new Logistic();
		    	classifier.buildClassifier(dataFiltered);
			    System.out.println("\n\nClassifier model:\n\n" + classifier);
			    System.out.println("Testing on ReutersCorn-Test.arff:");
			    Evaluation eTest = new Evaluation(dataFiltered);
		        //eTest.crossValidateModel(classifier, dataFiltered, 10, new Random(1));
		        //String strSummary = eTest.toSummaryString();
		        //System.out.println(strSummary);
			    eTest.evaluateModel(classifier, dataFiltered2);
			    System.out.println(eTest.toSummaryString());
		    }
		    else if(ch==4)
		    {
		    	//LibSVM classifier= new LibSVM();
		    	AbstractClassifier classifier = ( AbstractClassifier ) Class.forName(
		                "weka.classifiers.functions.LibSVM" ).newInstance();
		    	classifier.buildClassifier(dataFiltered);
			    System.out.println("\n\nClassifier model:\n\n" + classifier);
			    System.out.println("Testing on ReutersCorn-Test.arff:");
			    Evaluation eTest = new Evaluation(dataFiltered);
		        eTest.crossValidateModel(classifier, dataFiltered, 10, new Random(1));
		        String strSummary = eTest.toSummaryString();
		        System.out.println(strSummary);
			    //eTest.evaluateModel(classifier, dataFiltered2);
			    //System.out.println(eTest.toSummaryString());
		    
		    }
		    s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
