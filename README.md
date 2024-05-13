# 🔍 lighthouse

This project consists of several Java classes designed to tokenize documents, calculate term frequencies, set up a Term-Document Matrix (TDM), and provide a user-facing interface for querying documents.

## Tokenizer

The `Tokenizer` class is responsible for tokenizing documents. It takes a corpus of documents located in the `./corpus/` directory, tokenizes each document, and counts the frequency of each term. These term frequencies are then saved inside files located in the `./tokens/` directory.

## QueryEngine

The `QueryEngine` class takes the tokenized files outputted by the `Tokenizer` class and sets up a Term-Document Matrix (TDM). The TDM is implemented as a linked list storing key-value pairs, where keys are terms and values are the head nodes in the TDM linked list. Each node in the linked list stores the document and weight for a particular term, calculated using BM25 weighting. Additionally, each node points to the next node until there are no more documents that contain that particular term to be examined.

## Lighthouse

The `Lighthouse` class is the main user-facing feature of the project. It starts up the `QueryEngine` and then continuously prompts the user for queries. The user can either pass a string to be queried or enter 'q' to terminate the program.

## Usage

### Building the Project

To build the project, run:

``` gradle build ```

### Running the Project

To use this project, follow these steps:

1. Place your corpus of documents in the `./corpus/` directory.
2. Run the `Lighthouse` class using the jar as follows:

    ```java -jar ./lighthouse-1.0.1.jar```

3. Wait for the tokenization process and the QueryEngine to finish loading
4. Begin passing search terms through the console
