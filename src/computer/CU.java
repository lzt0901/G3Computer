/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

import computer.ComputerExceptions.MemoryAddressException;
import computer.ComputerExceptions.InterruptException;
import computer.ComputerExceptions.UnexpectedInstructionException;
import computer.ComputerExceptions.HaltException;
import computer.ComputerExceptions.DeviceFailureException;
import computer.OperationInterface.ControlFlowOperations;
import computer.OperationInterface.DataHandlingOperations;
import gui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CU implements DataHandlingOperations, ControlFlowOperations {

    private UI ui;

    private final MemorySystem memory;
    private final ProcessorRegisters registers;
    private final ALU alu;

    private boolean interrupted;

    // All for pipeline
    private int stageNumber;
    private ISA currentInstruction;
    private ISA nextInstruction;

    public CU(MemorySystem memory, ProcessorRegisters registers, ALU alu) {
        this.memory = memory;
        this.registers = registers;
        this.alu = alu;
        this.interrupted = false;

        // Not the best place to initialize it.
        this.stageNumber = 0;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    public void instuctionCycle()
            throws InterruptException, HaltException, UnexpectedInstructionException, MemoryAddressException, DeviceFailureException {
        this.fetchInstruction();
        ISA instruction = this.decode();
        this.fetchOperand(instruction);
        Register target = this.execute(instruction);
        this.store(target);
    }

    // Interrupt should not be considered here cuz it's the process after that is done. Nor should other exceptions.
    public void recover(ISA instruction) throws Exception {
        Register target = this.execute(instruction);
        this.store(target);
    }

    /**
     * In this pipeline model, instruction cycle are divided into
     * FetchInstruction -> Decode -> FetchOperand -> Execute. The stageNumber
     * variable would match to the four phases respectively. However, what it
     * means is more than that: It will tell us how to inversely execute the
     * instruction cycle.
     *
     * This pipeline has conquered hazards. However, it can't allow branches
     * yet.
     *
     * One way to test this method is to replace in the singleStep method of the
     * CPU class, the statement "this.cu.instuctionCycle();" with "this.cu.pipeline();".
     *
     * @throws InterruptException
     * @throws HaltException
     * @throws UnexpectedInstructionException
     * @throws MemoryAddressException
     * @throws DeviceFailureException
     */
    public void pipeline()
            throws InterruptException, HaltException, UnexpectedInstructionException, MemoryAddressException, DeviceFailureException {
        switch (this.stageNumber) {
            case 0:
                this.fetchInstruction();
                break;
            case 1:
                this.currentInstruction = this.decode();
                this.fetchInstruction();
                break;
            case 2:
                this.fetchOperand(this.currentInstruction);
                this.nextInstruction = this.decode();
                this.fetchInstruction();
                break;
            case 3:
                Register target = this.execute(this.currentInstruction);
                this.store(target);
                if (target == this.registers.pc) {
                    this.stageNumber = 0;
                    // If branch should be taken into consideration, the implementation of at least JSR and TRAP should be changed
                    // Because in this pipepline version, PC would be 2 more ahead than normal version.
                    return;
                }
                this.currentInstruction = this.nextInstruction;
                this.fetchOperand(this.currentInstruction);
                this.nextInstruction = this.decode();
                this.fetchInstruction();
                break;
        }
        if (this.stageNumber < 3) {
            ++this.stageNumber;
        }
    }

    public void fetchInstruction() throws MemoryAddressException {
        // IR = Memory[PC]
        this.registers.ir.setContent(this.memory.read(this.registers.pc.getContent()));
        // Increment PC by 1
        this.registers.pc.setContent(this.registers.pc.getContent() + 1);
    }

    public ISA decode() {
        return new ISA(this.registers.ir.getContent());
    }

    public void fetchOperand(ISA instruction) throws MemoryAddressException {
        if (instruction.canSkipFetchOperand()) {
            return;
        }

        // Direct addressing
        if (instruction.getI() == 0) {
            // IAR = Instruction.address
            this.registers.iar.setContent(instruction.getAddress());
            // Indexing
            if (instruction.getIX() != 0) {
                // IAR += X[ix]
                this.registers.iar.setContent(this.registers.iar.getContent() + this.registers.x[instruction.getIX()].getContent());
            }
        } // Indirect addressing
        else {
            if (instruction.getIX() == 0) {
                // IAR = Memory[Instruction.address]
                this.registers.iar.setContent(memory.read(instruction.getAddress()));
            } // Indexing
            else {
                // IAR = Memory[Instruction.address + X[ix]]
                this.registers.iar.setContent(memory.read(this.registers.x[instruction.getIX()].getContent() + instruction.getAddress()));
            }
        }
        // MAR = IAR
        this.registers.mar.setContent(this.registers.iar.getContent());
        // MBR = Memory[MAR]
        this.registers.mbr.setContent(this.memory.read(this.registers.mar.getContent()));
    }

    public Register execute(ISA instruction)
            throws InterruptException, HaltException, UnexpectedInstructionException, DeviceFailureException, MemoryAddressException {
        return instruction.operate(this, this.alu, this);
    }

    public void store(Register target) throws MemoryAddressException {
        // Store into memory
        if (target == null) {
            // MBR = IRR
            this.registers.mbr.setContent(this.registers.irr.getContent());
            // Memory[MAR] = MBR
            this.memory.write(this.registers.mar.getContent(), this.registers.mbr.getContent());
        } else {
            // Target register = IRR
            target.setContent(this.registers.irr.getContent());
        }
    }

    @Override
    public Register LDR(ISA instruction) {
        // IRR = MBR
        this.registers.irr.setContent(this.registers.mbr.getContent());
        // GPR[r]
        return this.registers.gpr[instruction.getR()];
    }

    @Override
    public Register STR(ISA instruction) {
        // IRR = R[r]
        this.registers.irr.setContent(this.registers.gpr[instruction.getR()].getContent());
        // Means the result will be stored into memory
        return null;
    }

    @Override
    public Register LDA(ISA instruction) {
        // IRR = MAR
        this.registers.irr.setContent(this.registers.mar.getContent());
        // GPR[r]
        return this.registers.gpr[instruction.getR()];
    }

    @Override
    public Register LDX(ISA instruction) {
        // IRR = MBR
        this.registers.irr.setContent(this.registers.mbr.getContent());
        // X[ix]
        // ix can't be 0 here. Confirmation is needed (later).
        return this.registers.x[instruction.getIX()];
    }

    @Override
    public Register STX(ISA instruction) {
        // IRR = X[ix]
        this.registers.irr.setContent(this.registers.x[instruction.getIX()].getContent());
        // Means the result will be stored into memory
        return null;
    }

    @Override
    public Register JZ(ISA instruction) {
        // IRR = MAR if GPR[r] == 0 else PC
        if (this.registers.gpr[instruction.getR()].getContent() == 0) {
            this.registers.irr.setContent(this.registers.mar.getContent());
        } else {
            this.registers.irr.setContent(this.registers.pc.getContent());
        }
        // PC
        return this.registers.pc;
    }

    @Override
    public Register JNE(ISA instruction) {
        // IRR = MAR if GPR[r] != 0 else PC
        if (this.registers.gpr[instruction.getR()].getContent() != 0) {
            this.registers.irr.setContent(this.registers.mar.getContent());
        } else {
            this.registers.irr.setContent(this.registers.pc.getContent());
        }
        // PC
        return this.registers.pc;
    }

    @Override
    public Register JCC(ISA instruction) {
        int cc = instruction.getR();
        // IRR = MAR if If cc bit == 1 else PC
        if ((this.registers.cc.getContent() >> cc & 1) == 1) {
            this.registers.irr.setContent(this.registers.mar.getContent());
        } else {
            this.registers.irr.setContent(this.registers.pc.getContent());
        }
        // PC
        return this.registers.pc;
    }

    @Override
    public Register JMA(ISA instruction) {
        // IRR = MAR
        this.registers.irr.setContent(this.registers.mar.getContent());
        // PC
        return this.registers.pc;
    }

    @Override
    public Register JSR(ISA instruction) {
        // GPR[3] = PC (already incremented)
        this.registers.gpr[3].setContent(this.registers.pc.getContent());
        // IRR = MAR
        this.registers.irr.setContent(this.registers.mar.getContent());
        // PC
        return this.registers.pc;
    }

    @Override
    public Register RFS(ISA instruction) {
        // GPR[0] = Immed
        this.registers.gpr[0].setContent(instruction.getAddress());
        // IRR = GPR[3]
        this.registers.irr.setContent(this.registers.gpr[3].getContent());
        // PC
        return this.registers.pc;
    }

    @Override
    public Register SOB(ISA instruction) {
        // GPR[r] = GPR[r] - 1
        this.registers.gpr[instruction.getR()].setContent(this.registers.gpr[instruction.getR()].getContent() - 1);
        // IRR = MAR if If GPR[r] > 0 else PC
        if (this.registers.gpr[instruction.getR()].getContent() > 0) {
            this.registers.irr.setContent(this.registers.mar.getContent());
        } else {
            this.registers.irr.setContent(this.registers.pc.getContent());
        }
        // PC
        return this.registers.pc;
    }

    @Override
    public Register JGE(ISA instruction) {
        // IRR = MAR if If GPR[r] >= 0 else PC
        if (this.registers.gpr[instruction.getR()].getContent() >= 0) {
            this.registers.irr.setContent(this.registers.mar.getContent());
        } else {
            this.registers.irr.setContent(this.registers.pc.getContent());
        }
        // PC
        return this.registers.pc;
    }

    @Override
    public Register LIX(ISA instruction) {
        // IRR = Immed
        this.registers.irr.setContent(instruction.getAddress());
        // X[ix]
        // ix can't be 0 here. Confirmation is needed (later).
        return this.registers.x[instruction.getIX()];
    }

    @Override
    public Register IN(ISA instruction) throws InterruptException, DeviceFailureException {
        int devid = instruction.getAddress();
        // Console Keyboard
        if (devid == 0) {
            if (!this.interrupted) {
                this.interrupted = true;
                this.ui.operatorConsole.showMessage("Please input your data.");
                this.ui.operatorConsole.ioPanel.focusOnInputAndSelectAll();
                throw new InterruptException();
            } else {
                this.interrupted = false;
                // At this point the input would be absolutely right (cuz it's been examined).
                try {
                    // Read in number format.
                    if (this.memory.directRead(7) == 0) {
                        this.registers.irr.setContent(this.ui.operatorConsole.ioPanel.getNumberInput());
                    } // Read in string format.
                    else {
                        String str = this.ui.operatorConsole.ioPanel.getStringInput();
                        List<Integer> strAscii = new ArrayList();
                        for (int i = 0; i < str.length(); ++i) {
                            strAscii.add((int) str.charAt(i));
                        }
                        this.registers.irr.setContent(this.memory.allocate(strAscii));
                    }
                } catch (MemoryAddressException ex) {
                }
            }
        } // Card Reader
        else {
            this.registers.irr.setContent(this.memory.cardReader.getASCII());
        }

        return this.registers.gpr[instruction.getR()];
    }

    @Override
    public Register OUT(ISA instruction) {
        // Temporary solution.
        int type = 0;
        try {
            type = this.memory.directRead(7);
        } catch (MemoryAddressException ex) {
        }
        if (type == 0) {
            this.ui.operatorConsole.ioPanel.setOutput(this.registers.gpr[instruction.getR()].getContent());
        } else {
            this.ui.operatorConsole.ioPanel.setOutput((char) this.registers.gpr[instruction.getR()].getContent());
        }

        this.registers.irr.setContent(this.registers.gpr[instruction.getR()].getContent());
        return this.registers.gpr[instruction.getR()];
    }

    @Override
    public Register CHK(ISA instruction) {
        int devid = instruction.getAddress();
        // Card Reader
        if (devid == 2) {
            this.registers.irr.setContent(this.memory.cardReader.getStatus());
        } // Others
        else {
            this.registers.irr.setContent(1);
        }
        return this.registers.gpr[instruction.getR()];
    }

    @Override
    public Register HLT(ISA instruction) throws HaltException, UnexpectedInstructionException {
        int checkDigits = instruction.getAddress() | (instruction.getI() << 5);
        if (checkDigits != 0) {
            throw new UnexpectedInstructionException();
        } else {
            throw new HaltException();
        }
    }

    @Override
    public Register TRAP(ISA instruction) throws MemoryAddressException {
        int trapCode = instruction.getAddress() & 0x0000000f;
        // Stores the PC (already incremented) in memory location 2.
        this.memory.write(2, this.registers.pc.getContent());
        // Goes to the routine whose address is in Memory[0] + trap code.
        this.registers.irr.setContent(this.memory.read(this.memory.read(0) + trapCode));
        return this.registers.pc;
    }

    @Override
    public Register GETS(ISA instruction) throws DeviceFailureException {
        int ascii;
        List<Integer> strAscii = new ArrayList();
        while ((ascii = this.memory.cardReader.getASCII()) != 0) {
            strAscii.add(ascii);
        }
        this.registers.irr.setContent(this.memory.allocate(strAscii));
        return this.registers.gpr[instruction.getR()];
    }

    @Override
    public Register PUTS(ISA instruction) throws MemoryAddressException {
        int ascii;
        StringBuilder sb = new StringBuilder();
        for (int i = this.registers.gpr[instruction.getR()].getContent(); (ascii = this.memory.directRead(i)) != 0; ++i) {
            sb.append((char) ascii);
        }
        this.ui.operatorConsole.ioPanel.setOutput(sb.toString());
        this.registers.irr.setContent(this.registers.gpr[instruction.getR()].getContent());
        return this.registers.gpr[instruction.getR()];
    }

    @Override
    public Register LDFR(ISA instruction) throws MemoryAddressException, UnexpectedInstructionException {
        int fr = instruction.getR();
        if (fr > 1) {
            throw new UnexpectedInstructionException();
        }

        int exponent = this.registers.mbr.getContent();
        int signedMantissa = this.memory.read(this.registers.mar.getContent() + 1);
        this.registers.irr.setContent(new FloatNumber(exponent, FloatNumber.complementToDecimal(signedMantissa)).getBits());
        // FR[fr]
        return this.registers.fpr[fr];
    }

    @Override
    public Register STFR(ISA instruction) throws MemoryAddressException, UnexpectedInstructionException {
        int fr = instruction.getR();
        if (fr > 1) {
            throw new UnexpectedInstructionException();
        }

        FloatNumber fn = new FloatNumber(this.registers.fpr[fr].getContent());
        this.memory.write(this.registers.mar.getContent() + 1, fn.getMantissaBits());
        this.registers.irr.setContent(fn.getExponentBits());
        // Means the result will be stored into memory
        return null;
    }

    @Override
    public Register VADD(ISA instruction) throws MemoryAddressException, UnexpectedInstructionException {
        int fr = instruction.getR();
        if (fr > 1) {
            throw new UnexpectedInstructionException();
        }

        int v1 = this.registers.mbr.getContent();
        int v2 = this.memory.read(this.registers.mar.getContent() + 1);
        int n = this.registers.fpr[fr].getContent();
        for (int i = 0; i < n; ++i) {
            this.memory.write(v1 + i, this.memory.read(v1 + i) + this.memory.read(v2 + i));
        }

        // Meaningless
        this.registers.irr.setContent(n);
        return this.registers.fpr[fr];
    }

    @Override
    public Register VSUB(ISA instruction) throws MemoryAddressException, UnexpectedInstructionException {
        int fr = instruction.getR();
        if (fr > 1) {
            throw new UnexpectedInstructionException();
        }

        int v1 = this.registers.mbr.getContent();
        int v2 = this.memory.read(this.registers.mar.getContent() + 1);
        int n = this.registers.fpr[fr].getContent();
        for (int i = 0; i < n; ++i) {
            this.memory.write(v1 + i, this.memory.read(v1 + i) - this.memory.read(v2 + i));
        }

        // Meaningless
        this.registers.irr.setContent(n);
        return this.registers.fpr[fr];
    }
}
