# Data Structures Project

Lempel-Ziv-Welch

Mikko Nylén

## Implementation document

### Introduction

The subject of the project was to implement a Lempel-Ziv-Welch (LZW) data compression
and decompression algorithm with the Java programming language version 1.6.
The implementation supports the use of variable-width code lengths up to 32
bits for improved compression ratio with larger documents.

### Implementation

* Java SE 1.6
* Apache Ant for build management
* JavaDoc for inline documentation
* Git for version control

For readonly access to the version control, see the
[GitHub project page](https://github.com/mnylen/lzw)

### Compression

The pseudo code for the implemented compression algorithm is as follows:

    STRING = get input character
    WIDTH  = initial code width
    WHILE there are still input characters DO
        CHARACTER = get input character
        IF STRING+CHARACTER is in the string table then
            STRING = STRING+character
        ELSE
            output the code for STRING in code width WIDTH
            add STRING+CHARACTER to the string table
            STRING = CHARACTER
            
            IF next code in table == 2^WIDTH + 1 AND (WIDTH+1) <= MAX_WIDTH
              WIDTH = WIDTH + 1
            END
        END of IF
    END of WHILE
    output the code for STRING

String table management in the compression step is done by utilizing hash
tables. Each entry in the string table can be thought of as a 3-tuple
`(prefixCode, appendCharacter, codeValue)`, where:

* `prefixCode` is the code value for `STRING`
* `appendCharacter` is the `CHARACTER`
* `codeValue` is the code value assigned for string `STRING + CHARACTER`

For example, if the string 'Ca' was assigned code 300 and the next character
read in would be 't', the following would be added to the string table:
`(300, 't', 301)`

For storing these values, there are three arrays, all allocating `2^MAX_WIDTH`
indexes, where `MAX_WIDTH` is the user-set maximum value for code
width. Initially all indexes in the tables are initialized with a special value
`-1` (not used).

Because the string table does not store the actual strings, but only pointer
to prefix string and the appended character, it's operations are efficient both
memory and performance wise. Code widths up to 22 bits can be used without
requiring tuning the JVM options `-Xms` and `-Xmx`. Code widths larger than 22
bits, however, require more memory, because by default Java only allocates
128 MB for the heap.

The hash function itself does only bit manipulation based on the `prefixCode`
and `appendCharacter`, so calculating the hash function is a constant time
operation, not depending on the length of the string. This, however, makes it
nearly impossible to calculate unique hashes. As a result we need to resolve
hash collisions more frequently than with a hash function that would consider
the whole string. The implementation uses kind-of quadratic probing for
resolving hash collisions. Further, finding the index for a
`prefixCode` and `appendCharacter` pair has been optimized so that the
calculation of the hash function occurs only once. This saves a lot of CPU
cycles because when the load factor for the table gets around 0.8,
add operations tend to require as many as 20 probes for finding an empty
index.

The disk write operations are implemented using Java's built-in
`BufferedOutputStream`s that helps by greatly reducing disk I/O by 
buffering any writes and, when the buffer is full, writing all the data
in one large chunk. The savings are huge: for writing 8192 bytes (8 kB), only
one write is needed instead of 8192.

### Decompression

The LZW decompression algorithm implemented can be described in pseudo code as
follows:

    Read OLD_CODE
    output OLD_CODE
    CHARACTER = OLD_CODE
    WHILE there are still input characters DO
        IF next code to be inserted to translation table is 2^WIDTH+1
          increase code width for reader
        END of IF
        
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
    
In decompression, a similar structure is used for 