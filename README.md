# Text-Classification---Reuters
Classifying articles based on body of text using Weka jar on Java


1) ExtractFiles.java iterates through all SGM files(each with more than one article) in a directory and divides it into files that contain only the heading and body of text (i.e. tags etc are removed) of ONE article alone


2) tfidf.java is actually used to create the arff dataset for weka from a set of text files


3) Classifiers.java performs the classification on the arff dataset using tfidf algorithm
