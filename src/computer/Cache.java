/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

import gui.UI;
import computer.ComputerExceptions.MemoryAddressException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Administrator
 */
public class Cache {

    private UI ui;

    private final int lineSize;
    private final int cacheSize;
    private final Queue<Integer> tagQueue;
    private final Map<Integer, CacheLine> lineMap;

    private final String traceFile;
    private BufferedWriter writer;
    private StringBuilder buffer;
    private int bufferSize;

    public Cache(int lineSize, int cacheSize) {
        this.lineSize = lineSize;
        this.cacheSize = cacheSize;
        this.tagQueue = new ArrayDeque<>();
        this.lineMap = new HashMap<>();
        this.traceFile = "trace.txt";

        try (BufferedWriter bwriter = new BufferedWriter(new FileWriter(this.traceFile))) {
            bwriter.append("Cache Log");
        } catch (IOException ex) {
        }
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    private CacheLine find(int address) {
        int tag = address / this.lineSize * this.lineSize;
        return this.lineMap.containsKey(tag) ? this.lineMap.get(tag) : null;
    }

    private CacheLine addLine(int address, MemorySystem memory) throws MemoryAddressException {
        if (this.tagQueue.size() >= this.cacheSize) {
            int tag = this.tagQueue.remove();
            CacheLine removed = this.lineMap.remove(tag);
            this.traceRemove(removed);
        }
        CacheLine newLine = new CacheLine(this.lineSize, address, memory);
        this.tagQueue.add(newLine.getTag());
        this.lineMap.put(newLine.getTag(), newLine);
        this.traceAdd(newLine);
        return newLine;
    }

    private void writeThrough(CacheLine line, int address, int datum, MemorySystem memory) throws MemoryAddressException {
        // Update cache
        line.setData(address, datum);
        // Update memory
        memory.directWrite(address, datum);
    }

    public int read(int address, MemorySystem memory) throws MemoryAddressException {
        memory.check(address);
        CacheLine line = this.find(address);
        if (line == null) {
            this.traceMiss(address);
            line = this.addLine(address, memory);
        } else {
            this.traceHit(address);
        }
        return line.getData(address);
    }

    public void write(int address, int datum, MemorySystem memory) throws MemoryAddressException {
        memory.check(address);
        CacheLine line = this.find(address);
        if (line == null) {
            this.traceMiss(address);
            // Write no-allocate
            memory.directWrite(address, datum);
        } else {
            this.traceHit(address);
            this.writeThrough(line, address, datum, memory);
        }
    }

    public void clear() {
        this.tagQueue.clear();
        this.lineMap.clear();
    }

    private void traceMiss(int address) {
        String msg = String.format("Cache miss for memory address %d.", address);
        this.bufferedOutput(msg);
        this.traceToFile(msg);
    }

    private void traceHit(int address) {
        String msg = String.format("Cache hit for memory address %d.", address);
        this.bufferedOutput(msg);
        this.traceToFile(msg);
    }

    private void traceAdd(CacheLine line) {
        String msg = String.format("Added a new cache line with tag %d. Current number of lines: %d.", line.getTag(), this.tagQueue.size());
        this.bufferedOutput(msg);
        this.traceToFile(msg);
    }

    private void traceRemove(CacheLine line) {
        String msg = String.format("Removed a cache line with tag %d. Current number of lines: %d.", line.getTag(), this.tagQueue.size());
        this.bufferedOutput(msg);
        this.traceToFile(msg);
    }

    private void traceToFile(String msg) {
        try {
            this.writer.newLine();
            this.writer.append(msg);
        } catch (IOException ex) {
        }
    }

    private void resetBuffer() {
        this.buffer = new StringBuilder();
        this.bufferSize = 1000;
    }

    private void flushBuffer() {
        if (this.buffer.length() != 0) {
            this.ui.fieldEngineerConsole.outputPanel.setOutput(this.buffer.toString());
        }
        this.resetBuffer();
    }

    private void bufferedOutput(String msg) {
        if (this.bufferSize > 0) {
            if (this.buffer.length() == 0) {
                this.buffer.append(msg);
            } else {
                this.buffer.append(System.lineSeparator()).append(msg);
            }
            --this.bufferSize;
        } else {
            this.flushBuffer();
        }
    }

    public void openTraceFile() {
        try {
            this.writer = new BufferedWriter(new FileWriter(this.traceFile, true));
            this.resetBuffer();
        } catch (IOException ex) {
        }
    }

    public void closeTraceFile() {
        try {
            this.writer.close();
            this.flushBuffer();
        } catch (IOException ex) {
        }
    }
}

class CacheLine {

    private final int tag;
    private final int[] data;

    public CacheLine(int size, int address, MemorySystem memory) throws MemoryAddressException {
        // Cache line can't overlap.
        this.tag = address / size * size;
        this.data = new int[size];
        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] = memory.directRead(tag + i);
        }
    }

    public int getTag() {
        return this.tag;
    }

    public void setData(int address, int datum) {
        int pos = address - tag;
        this.data[pos] = datum;
    }

    public int getData(int address) {
        int pos = address - tag;
        return this.data[pos];
    }
}
