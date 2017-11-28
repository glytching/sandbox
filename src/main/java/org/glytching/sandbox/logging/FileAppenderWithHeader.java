package org.glytching.sandbox.logging;

import ch.qos.logback.core.FileAppender;

import java.io.File;
import java.io.IOException;

public class FileAppenderWithHeader extends FileAppender {

    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public void openFile(String fileName) throws IOException {
        super.openFile(fileName);
        File activeFile = new File(getFile());
        if (activeFile.exists() && activeFile.isFile() && activeFile.length() == 0) {
            write((header + "\n").getBytes());
        }
    }

    private void write(byte[] byteArray) throws IOException {
        if (byteArray == null || byteArray.length == 0)
            return;

        lock.lock();
        try {
            super.getOutputStream().write(byteArray);
            if (super.isImmediateFlush()) {
                super.getOutputStream().flush();
            }
        } finally {
            lock.unlock();
        }
    }
}
