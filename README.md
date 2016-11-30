# G3Computer
Simulator of a CSIC machine

## How to Use

### 1. General Introductions  

You can click each radio button to set the value of those registers and click the Set buttons to confirm making changes. The Run button would run all the instructions stored in the memory sequentially (until a halt instruction), and Single Step is used to execute the current instruction. Upon start, nothing is in the memory.

New instructions, or programs, can be loaded into memory in one of the three format: hexadecimal, octal and binary.

This computer should always have one and only one boot program, which can be loaded either by the "Default IPL" button, or by loading a program file that contains the boot program.

How cache behaves as the system runs is recorded in "trace.txt".

### 2. Running Programs  

All the files are located in the "programs" directory.

If different boot programs are needed, you have to restart the whole simulator in order to load a different boot program. (Because currently there is no mechanism for clearing boot program that has been loaded before.)

Please note that "Default IPL" and "Load" buttons only change the main memory. As a consequence, the content of registers (such as PC) will not be affected by such operations, which means if you want to jump among multiple programs (meaning not to execute programs sequentially), you have to remember their initial address (which would be provided when loading succeeds) and set the value of PC manually.

Once encountered an error (unexpected instruction or accessing wrong memory address), our computer would reboot automatically with boot program automatically executed and PC pointing to the initial address of the first user program.

#### 2.1 Program 2.txt:  

1) Click "Load Bin" first to load "Boot Program.txt" as a boot program.  
2) Click "Load Bin" again to load "Program 2.txt".  
3) Click "Card Reader" button to read an external text file, "test sample.txt", into the card reader.  
4) Click "Run" to run boot program first. The computer will halt after the boot program is done.  
5) Click "Run" again to execute Program 2. It reads the file into memory, prints out the contents of the text file, and then asks the user for a word. Finally, it searches the paragraph to see if it contains the word. If so, it prints out the word, the sentence number, and the word number in the sentence.

#### 2.2 program_1.txt  

1) Click "Default IPL" before doing anything to load built-in boot program.  
2) Click "Load Bin" to load "program_1.txt".   
3) Click "Run" to execute the program. It reads 20 numbers (integers) from the keyboard, prints the numbers to the console printer, requests a number from the user, and searches the 20 numbers read in for the number closest to the number entered by the user. Finally it prints the number entered by the user and the number closest to that number.

#### 2.3 program_2.txt  

This program is almost the same with "Program 2.txt" except that it uses a built-in boot program. To run this program:  
1) Click "Default IPL" before doing anything to load built-in boot program.  
2) Click "Load Bin" to load "program_2.txt".  
3) Click "Card Reader" button to read an external text file, "test sample.txt", into the card reader.  
4) Click "Run" to execute the program. It reads the file into memory, prints out the contents of the text file, and then asks the user for a word. Finally, it searches the paragraph to see if it contains the word. If so, it prints out the word number in the sentence, the sentence number, and the word.

This version of program 2 is flawed in that the output form is not perfect and that it can't tolerate the space after a comma (under this circumstance, it will count one more to the word number in the sentence). Besides, it would match suffix.
