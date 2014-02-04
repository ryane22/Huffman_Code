compile this file using javac Huffman_Code.java
run using java Huffman_Code

The classes HuffmanTree HuffmanLeaf and HuffmanNode are all used in building the Huffman Tree.

The class Huffman_Code contains the main method and the methods used in building, encoding, and decoding the text.

buildTree builds the tree.

printCodes prints the tables to the command line.

encodeFind calls encode to builds an array of the symbols and their code to be decoded later.

encode is called from encodeFind the code for each symbol.

decode takes the encoded text and decodes each symbol and prints it to the decoded.txt file.

main is used to execute the methods.