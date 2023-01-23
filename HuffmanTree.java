/**
 * CSCI 2110
 *
 * @author - Rahul Kumar
 * @description: This program uses to the huffman's algorithm to encode and decode text
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class HuffmanTree {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the name of the file with letters and probability: ");
        String filename = in.nextLine();

        File file = new File(filename);
        Scanner fileReader = new Scanner(file);
        ArrayList<BinaryTree<Pair>> treeList = new ArrayList<>();
        ArrayList<BinaryTree<Pair>> resultList = new ArrayList<>();

        char value = ' ';
        double prob = 0.0;

        /*
        1. Reading character and their probabilities from the entered file to create Pair objects.
        2. Then using those pair to create binary trees and add them to arraylist (treeList)
         */
        while (fileReader.hasNext()) {
            StringTokenizer token = new StringTokenizer(fileReader.nextLine(), "\t");
            while (token.hasMoreTokens()) {
                value = token.nextToken().charAt(0);
                prob = Double.parseDouble(token.nextToken());
            }
            Pair newPair = new Pair(value, prob);
            BinaryTree<Pair> newTree = new BinaryTree<>();
            newTree.makeRoot(newPair);
            treeList.add(newTree);
        }

        while (!treeList.isEmpty()) {
            // when resultList is empty, use the first two elements of treeList
            if (resultList.isEmpty()) {
                resultList.add(createTree(treeList.get(0), treeList.get(1)));
                treeList.remove(0);
                treeList.remove(0);
            } else {
                /* when resultList is not empty and the probability of first two elements of treeList is smaller than
                the probability of resultList element*/
                if (treeList.size() > 1 && treeList.get(0).getData().getProb() < resultList.get(0).getData().getProb() &&
                        treeList.get(1).getData().getProb() < resultList.get(0).getData().getProb()) {
                    resultList.add(createTree(treeList.get(0), treeList.get(1)));
                    treeList.remove(0);
                    treeList.remove(0);

                }
                //when probability of second element of treeList is greater than the probability of resultList element
                else if (treeList.size() > 1 && treeList.get(0).getData().getProb() < resultList.get(0).getData().getProb() &&
                        treeList.get(1).getData().getProb() > resultList.get(0).getData().getProb()) {
                    resultList.set(0, createTree(treeList.get(0), resultList.get(0)));
                    treeList.remove(0);

                }
                //when probability of first element of treeList is greater than the probability of resultList element
                else if (!treeList.isEmpty() && treeList.get(0).getData().getProb() > resultList.get(0).getData().getProb()) {
                    resultList.set(0, createTree(resultList.get(0), treeList.get(0)));
                    treeList.remove(0);

                }
                // when treeList has at least one element left and resultList has more than one element with varying probability
                else if (!treeList.isEmpty() && treeList.get(0).getData().getProb() < resultList.get(0).getData().getProb()) {
                    resultList.set(0, createTree(resultList.get(1), treeList.get(0)));
                    treeList.remove(0);
                }
            }
            //when resultList element has more than one element.
            if (resultList.size() > 1) {
                resultList.set(0, createTree(resultList.get(0), resultList.get(1)));
                resultList.remove(1);
            }
        }

        //Encoding the text
        String[] encodedValues = findEncoding(resultList.get(0));
        String encodedString = "";
        System.out.print("Enter a line of text (uppercase letters only): ");
        String text = in.nextLine();
        // Storing encoded text in a string
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                encodedString += " ";
            } else {
                encodedString += encodedValues[text.charAt(i) - 65];
            }
        }
        System.out.println(encodedString);
        System.out.println("Decoded text is as follow:");
        String decodeString = "";

        BinaryTree<Pair> current = resultList.get(0);

        // Decoding the encoded string
       for (int i = 0; i < encodedString.length(); i++) {
           // when the node has no child, add its data to string
               if (current.getLeft() == null && current.getRight()==null) {
                   decodeString += current.getData();
                   current = resultList.get(0);
               }
               // if string says 0, go to left child
               if (encodedString.charAt(i)=='0') {
                   current = current.getLeft();
               }
               // if string says 1, go to right child
               else if (encodedString.charAt(i)=='1') {
                   current = current.getRight();
               } else  {
                   decodeString += " ";
               }
        }
       decodeString += current.getData();
        System.out.println(decodeString);

    }

    // this piece of code is provided by Dr. Srini Sampalli
    private static String[] findEncoding(BinaryTree<Pair> bt) {
        String[] result = new String[26];
        findEncoding(bt, result, "");
        return result;
    }

    // this piece of code is provided by Dr. Srini Sampalli
    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix) {
        // test is node/tree is a leaf
        if (bt.getLeft() == null && bt.getRight() == null) {
            if (bt.getData().getValue() != '0') {
                a[bt.getData().getValue() - 65] = prefix;
            }
        }

        // recursive calls
        else {
            findEncoding(bt.getLeft(), a, prefix + "0");
            findEncoding(bt.getRight(), a, prefix + "1");
        }
    }

    /**
     * This method helps in creating the huffman tree
     * @param first binary tree with smallest probability
     * @param second another binary tree with probability equal or greater than first but
     *               smaller than others
     * @return binary tree that contains the sum of first and second node
     */
    private static BinaryTree<Pair> createTree(BinaryTree<Pair> first, BinaryTree<Pair> second) {
        char letter = '0';
        Pair sumOfNodes = new Pair(letter, first.getData().getProb() + second.getData().getProb());
        BinaryTree<Pair> result = new BinaryTree<>();
        result.makeRoot(sumOfNodes);
        first.setParent(result);
        second.setParent(result);
        result.setLeft(first);
        result.setRight(second);
        return result;

    }

}
