package com.example.springscuritybe.config.filter;

import java.io.IOException;
import java.io.OutputStream;


public class HttpLogOutputStream extends HttpLogProxyOutputStream {

    protected OutputStream branch;

    public HttpLogOutputStream(OutputStream out, OutputStream branch) {
        super(out);
        this.branch = branch;
    }

    public synchronized void write(byte[] b) throws IOException {
        super.write(b);
        this.branch.write(b);
    }

    public synchronized void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        this.branch.write(b, off, len);
    }

    public synchronized void write(int b) throws IOException {
        super.write(b);
        this.branch.write(b);
    }

    public void flush() throws IOException {
        super.flush();
        this.branch.flush();
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.branch.close();
        }

    }
}
