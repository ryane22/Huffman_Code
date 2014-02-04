//Ryan Evenstad
import java.util.*;
import java.io.*;
 
abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; 
    public HuffmanTree(int freq) { frequency = freq; }
 
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}
 
class HuffmanLeaf extends HuffmanTree {
    public final char value;
 
    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}
 
class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right;
 
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}
/**
 *
 * @author Evenstad
 */
public class Huffman_Code {
    /**
     * @param args the command line arguments
     */
    private class global{
        String code = "";
    }
    
    public static HuffmanTree buildTree(int[] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        for (int i = 0; i < charFreqs.length; i++)
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char)i));
 
        assert trees.size() > 0;

        while (trees.size() > 1) {

            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
 
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
 
    public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
        
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
 
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
 
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length()-1);
 
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }
    
    public static String[] encodeFind(HuffmanTree tree, StringBuffer prefix, String test){
        String returned = "";
        int i = 0;
        int lastLength = 0;
        String[] codeArray = new String[10000];
        for(char c : test.toCharArray()){
            codeArray[i] = String.valueOf(c);
            //System.out.print(codeArray[i]+"\t");
            i++;
            returned = encode(tree, prefix, c);
            codeArray[i] = returned.substring(lastLength);
            lastLength = lastLength + returned.substring(lastLength).length();
            //System.out.println(codeArray[i] + "\t"+ lastLength + "\t" + i);
            i++;
        }
        codeArray[i] = returned;
        return codeArray;
    }
    
    public static String encode(HuffmanTree tree, StringBuffer prefix, char c){
        assert tree != null;
        String temp = "";
           if (tree instanceof HuffmanLeaf) {
                HuffmanLeaf leaf = (HuffmanLeaf)tree;
                if (leaf.value == c){
                    //String temp;
                    temp = prefix.toString();
                    //System.out.println(c+"="+leaf.value+"\tprefix is "+prefix+"\ttemp is "+temp);
                    return temp;
                }
                
            } else if (tree instanceof HuffmanNode) {
                HuffmanNode node = (HuffmanNode)tree;
 
                prefix.append('0');
                temp = encode(node.left, prefix, c);
                if(!temp.equals(""))
                    return temp;
                prefix.deleteCharAt(prefix.length()-1);
 
                prefix.append('1');
                temp = encode(node.right, prefix, c);
                if(!temp.equals(""))
                    return temp;
                prefix.deleteCharAt(prefix.length()-1);
            }
           return "";
        }
    
    public static void decode(HuffmanTree tree, String encoded, String[] encodedArray){
        PrintStream filewriter = null;
        try 
        {
            filewriter = new PrintStream("decoded.txt");
        }
        catch (IOException e)
        {
            System.out.println
                ("File cannot be opened for writing");
        }
        String temp = "";
        int start = 0;
        for(int j=0; j<encoded.length(); j++){
            temp = encoded.substring(start, j);
            //System.out.println("temp is "+ temp);
            for(int k=1; k<encodedArray.length; k=k+2){
                //System.out.println("comparing "+temp+"with "+encodedArray[k-1]);
                if(temp.equals(encodedArray[k])){
                    if(!temp.equals("10110"))
                        filewriter.print(encodedArray[k-1]);
                    else
                        filewriter.println("");
                    start = j;
                    break;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner filereader = null;
        File infile = new File("Desiderata.txt");
        try
        {
            filereader = new Scanner(infile);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("file not found");
        }
        String test = "";
        while (filereader.hasNextLine())
        {
            test = test + filereader.nextLine() + "\n";
        }
 
        int[] charFreqs = new int[256];
        for (char c : test.toCharArray())
            charFreqs[c]++;
 
        HuffmanTree tree = buildTree(charFreqs);
 
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        printCodes(tree, new StringBuffer());
        
        String[] encodedArray = new String[2000];
        encodedArray = encodeFind(tree, new StringBuffer(), test);
        String encodedD = encodedArray[test.toCharArray().length*2];
        
        PrintStream filewriter = null;
        try 
        {
            filewriter = new PrintStream("encoded.txt");
        }
        catch (IOException e)
        {
            System.out.println
                ("File cannot be opened for writing");
        }
        if(filewriter != null)
            filewriter.print(encodedArray[test.toCharArray().length*2]);
        
        System.out.println("\nnumber of bits in input file (ASCII encoded) is "+ test.length()*7);
        System.out.println("number of bits in decoded is " +encodedArray[test.toCharArray().length * 2].length());
        float percent = (1 - ((float)(encodedArray[test.toCharArray().length * 2].length())/(float)(test.length()*7)))*100;
        System.out.println("the decoded file is "+percent+"% smaller than the input file");
        
        decode(tree, encodedD, encodedArray);
        
    }
}
