# G3Computer
Simulator of a CSIC machine

## How to Use

### 1. General Introductions  

You can click each radio button to set the value of those registers and click the Set buttons to confirm making changes. The Run button would run all the instructions stored in the memory sequentially (until a halt instruction), and Single Step is used to execute the current instruction. Upon start, nothing is in the memory.

New instructions, or programs, can be loaded into memory in one of the three format: hexadecimal, octal and binary.

This computer should always have one and only one boot program, which can be loaded either by the "Default IPL" button, or by loading a program file that contains the boot program.

How cache behaves as the system runs is recorded in "trace.txt".

### 2. Running Programs  

**All the files are located in the _"programs"_ directory.**

If different boot programs are needed, you have to restart the whole simulator in order to load a different boot program. (Because currently there is no mechanism for clearing boot program that has been loaded before.)

When there are multiple programs in the simulator, you can randomly execute any of them by remembering their initial address (which would be provided upon loaded) and setting the value of PC manually.

Once encountered an error (unexpected instruction or accessing wrong memory address), our computer would reboot with boot program automatically executed and PC pointing to the initial address of the first user program.

#### 2.1 Program 1.txt  

1) Click "Load Bin" first to load "Boot Program.txt" as a boot program (If it is already set, ignore this step and do step 2, 4 instead).  
2) Click "Load Bin" again to load "Program 2.txt".  
3) Click "Run" to run boot program first. The computer will halt after the boot program is done.  
4) Click "Run" again to execute the program. It reads 20 numbers (integers) from the keyboard, prints the numbers to the console printer, requests a number from the user, and searches the 20 numbers read in for the number closest to the number entered by the user. Finally, it prints the number entered by the user and the number closest to that number.

#### 2.2 Program 2.txt:  

1) Click "Load Bin" first to load "Boot Program.txt" as a boot program (If it is already set, ignore this step and do step 2, 3, 5 instead).  
2) Click "Load Bin" again to load "Program 2.txt".  
3) Click "Card Reader" button to read an external text file, "Paragraph Sample.txt", into the card reader.  
4) Click "Run" to run boot program first. The computer will halt after the boot program is done.  
5) Click "Run" again to execute Program 2. It reads the file into memory, prints out the contents of the text file, and then asks the user for a word. Finally, it searches the paragraph to see if it contains the word. If so, it prints out the word, the sentence number, and the word number in the sentence.