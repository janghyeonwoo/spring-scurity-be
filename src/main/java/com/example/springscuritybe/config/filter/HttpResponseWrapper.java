package com.example.springscuritybe.config.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class HttpResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private long id;

    public HttpResponseWrapper(HttpServletResponse response, Long requestId) {
        super(response);
        this.id = requestId;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
            }

            private HttpLogOutputStream tee;

            {
                this.tee = new HttpLogOutputStream(HttpResponseWrapper.super.getOutputStream(),
                        HttpResponseWrapper.this.bos);
            }

            public void write(int b) throws IOException {
                this.tee.write(b);
            }
        };
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] toByteArray() {
        return this.bos.toByteArray();
    }
}

