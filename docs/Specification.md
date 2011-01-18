# Data Structures Project

Lempel-Ziv-Welch

Mikko Nylén

## Specification Document

### Introduction

The subject of the project is to implement a Lempel-Ziv-Welch (LZW) data compression
and decompression algorithm with the Java programming language version 1.6.
Furthermore, the implementation will use variable-width code lengths for
improved compression ratio with larger documents.

As a secondary goal, the implementation will be made to behave like regular
`InputStream`s and `OutputStream`s in Java. This way the code can be used easily
in other projects as well.

### Tools

* Java SE 1.6
* Apache Ant for build management
* JavaDoc for inline documentation
* Git for version control

For readonly access to the version control, see the
[GitHub project page](https://github.com/mnylen/lzw)

### Compression

The LZW compression algorithm can be described in pseudo code as follows:

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

The LZW decompression algorithm can be described in pseudo code as follows:

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
included in reading and writing codes:

* when outputting a code, the code width must be increased by one if the next
  code to be added to the string table would be `2^p`, where `p` is the current code width

* when reading in a code, the code width must be increased by one at the exact
  same point the compression algorithm increased the code width

By implementing these two checks we can ensure the compression and decompression
algorithms both use the exact same code width at any point of processing the input
stream. 

### String table management

The only data structure to be implemented in the project is the string table.
It will be represented as a list of key-value pairs, where the key is the string
and the value is the code.

The string table will support at least the following operations:

* `addString(prefixCode, appendCharacter)` - adds a string to the table
* `codeValue(prefixCode, appendCharacter)` - looks up the code for `(prefixCode, appendCharacter)`
* `translate(code)` - translates the given code back as a string based on the entries on the string table

Because insertion and retrieval from string table is crucial to the performance
of the compression and decompression algorithm, the string table will be
implemented as a hash table with open addressing. Using open addressing,
in majority of the cases, the insertion and lookup operations will have O(1)
time complexity.

For smaller memory footprint, entries in the string table will be stored as
three-tuples `(prefixCode, appendCharacter, code)`, where `prefixCode` is the code
for the string _prefix_, `appendCharacter` is the character
appended to the prefix and `code` is the code for the string table entry.

For example, given input _"Quick"_ and there is already a string table
entry for string _"Quic"_ with code _1000_, we could store the entry
for _"Quick"_ as `(1000, 'k', 1001)`

The approach described here has one caveat: when decompressing an compressed
input stream, we can't just output the key for the string table entry, but
we must expand the whole prefix chain. However, because of the great benefits
in memory management, the possible performance hit of this will be ignored.