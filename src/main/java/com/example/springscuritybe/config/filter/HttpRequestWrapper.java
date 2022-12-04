package com.example.springscuritybe.config.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class HttpRequestWrapper extends HttpServletRequestWrapper {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private Long id;

    public HttpRequestWrapper(HttpServletRequest request, Long id) {
        super(request);
        this.id = id;
    }

    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            private HttpLogInputStream tee;

            {
                this.tee = new HttpLogInputStream(HttpRequestWrapper.super.getInputStream(),
                        HttpRequestWrapper.this.bos);
            }

            public int read() throws IOException {
                return this.tee.read();
            }
        };
    }

    public byte[] toByteArray() {
        return this.bos.toByteArray();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
