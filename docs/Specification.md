# Data Structures Project

Lempel-Ziv-Welch

Mikko Nylén

## Specification Document

### Introduction

The subject of the project is to implement a Lempel-Ziv-Welch (LZW) compression
and decompression algorithm with the Java programming language version 1.6.
Furthermore, the implementation will use variable-width code lengths for
improved compression ratio with larger documents.

As a secondary goal, the implementation will be made to behave like regular
InputStreams and OutputStreams in Java. This way the code can be used easily
in other projects as well.

### Tools

* Java SE 1.6
* Apache Ant for build management
* JavaDoc for inline documentation
* Git for version control

For read-only access to the version control, see the [GitHub project page](https://github.com/mnylen/lzw)

### Compression

The LZW compression algorithm can be described as pseudo code as follows:

    STRING = get input character
    WHILE there are still input characters DO
        CHARACTER = get input character
        IF STRING+CHARACTER is in the string table then
            STRING = STRING+character
        ELSE
            output the code for STRING
            add STRING+CHARACTER to the string table
            STRING = CHARACTER
        END of IF
    END of WHILE
    output the code for STRING

### Decompression

The LZW decompression algorithm can be described as pseudo code as follows:

    Read OLD_CODE
    output OLD_CODE
    CHARACTER = OLD_CODE
    WHILE there are still input characters DO
        Read NEW_CODE
        IF NEW_CODE is not in the translation table THEN
            STRING = get translation of OLD_CODE
            STRING = STRING+CHARACTER
        ELSE
            STRING = get translation of NEW_CODE
        END of IF
        output STRING
        CHARACTER = first character in STRING
        add OLD_CODE + CHARACTER to the translation table
        OLD_CODE = NEW_CODE
    END of WHILE
    
### Variable width codes

Because of the variable-width codes there will be some additional complexity
while reading and outputting the codes:

When outputting a code, the code width must be increased by one if the next
code to be added to the string table would be 2^currentCodeWidth

When reading in a code, the code width must be increased by one at the exact
same point the compression algorithm increased the code width.

### String table management

The only data structure to be implemented in the project is the string table.
It will be represented as a list of key-value pairs, where the key is the string
and the value is the code.

Because insertion and retrieval from string table is crucial to the performance
of the compression and decompression algorithm, the string table will be
implemented as a hash table with open addressing. This way all insertion and
lookup operations will have O(1) time complexity in most cases.

For smaller memory footprint, entries in the string table will be stored as
three-tuples `(prefixCode, append, code)`. This way the entire string doesn't
need to be stored in the string table. 