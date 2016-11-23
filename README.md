# G3Computer
Simulator of a CSIC machine

How to Use

I. General Introductions

You can click each radio button to set the value of those registers and click the Set buttons to confirm making changes. And before clicking the Run or Single Step button, you need to click the IPL button first to read initial program. The Run button would run all the instructions stored in the Memory sequentially, and Single Step is used to execute the current instruction.

New instructions, or programs, can be loaded into memory in one of the three format: Hexadecimal, octal and binary.

Please note that at the very beginning, before we load anything into memory, the system is all empty. In order to run programs, we need to have a boot program, which can be loaded either by the "Default IPL" button, or by loading a program file that contains the boot program. Under the latter circumstance, you should not click the "Default IPL" button.

II. Running Programs

All the files are located in the "programs" directory.

II.1 Program 2.txt:

Click "Load Bin" first to load "Boot Program.txt" as a boot program, and then click again to load "Program 2.txt" using binary format. After that, getting an external text file, "test sample.txt", to make the card reader ready. Now the preparation is done. Click "Run" to run boot program first. The computer will halt after the boot program is done. Click "Run" again to execute Program 2. It reads the file into memory, print out the contents of the text file, and then asks the user for a word. Finally, it searches the paragraph to see if it contains the word. If so, it prints out the word, the sentence number, and the word number in the sentence.

II.2 program_2.txt

This program is almost the same with "Program 2.txt" except that it uses a built-in boot program. To run this program, click "Default IPL" before doing anything to load built-in boot program, then click "Load Bin" to load "program_2.txt". Now you can click "Run" to execute the program. It reads the file into memory, print out the contents of the text file, and then asks the user for a word. Finally, it searches the paragraph to see if it contains the word. If so, it prints out the word number in the sentence, the sentence number, and the word. This version of program 2 is flawed in that the output form is not perfect and it can't tolerate the space after a comma (under this circumstance, it will count one more to the word number in the sentence).

III. Others

Once encountered an error (unexpected instruction or accessing wrong memory address), our computer would reboot automatically with boot program automatically executed and PC pointing to the initial address of the first user program.
