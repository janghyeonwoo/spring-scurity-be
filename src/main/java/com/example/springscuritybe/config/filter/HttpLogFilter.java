package com.example.springscuritybe.config.filter;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
@NoArgsConstructor
public class HttpLogFilter extends OncePerRequestFilter {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AtomicLong id = new AtomicLong(1L);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) {
        try {
            long requestId = this.id.incrementAndGet();
            Date startTime = new Date();

            request = new HttpRequestWrapper(request, requestId);
            response = new HttpResponseWrapper(response, requestId);
            filterChain.doFilter(request, response);

            if (!logger.isInfoEnabled()) {
                return;
            }
            httpRequestLog(request, new Date().getTime() - startTime.getTime());
            httpResponseLog(response);


        } catch (Exception e) {
            logger.error("", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpRequestLog(request, -1);
        }
    }

    private void httpRequestLog(HttpServletRequest request, long elapsed) {
        HttpRequestWrapper wrequest = (HttpRequestWrapper) request;
        Enumeration<String> enumeration = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "\r\n============= HTTP %s REQUEST MESSAGE START ID[%d] elapsed[%dms] ============\r\n",
                request.getMethod(), wrequest.getId(), elapsed));
        sb.append("Request URI : " + request.getRequestURI() + "\r\n");
        sb.append("Request URL : " + request.getRequestURL() + "\r\n");

        if (request.getParameterMap().size() > 0) {
            sb.append("Parameters : ");
            Enumeration<String> e = request.getParameterNames();
            String key;
            while (e.hasMoreElements()) {
                key = e.nextElement();
                sb.append(key + "=" + request.getParameter(key));
                if (e.hasMoreElements()) {
                    sb.append(",");
                }
            }
            sb.append(System.lineSeparator());
        }

        while (enumeration != null && enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            sb.append(String.format("%s : %s\r\n", StringUtils.rightPad(key, 24), value));
        }
        String contentType = request.getContentType();

        try {

            if (!StringUtils.isEmpty(contentType) && contentType.startsWith("multipart")) {

                String boundary = getAcceptParamValue("boundary",
                        getAcceptParam("boundary", contentType));
                logger.info("boundary = {}", boundary);
                List<byte[]> bytes = getMultiPartAsByteList(wrequest.toByteArray(), boundary);
                for (byte[] b : bytes) {
                    HttpLogMultiPart part = getMultiParts(b);
                    sb.append("--------------------------------------------------"
                            + System.lineSeparator());
                    if (part != null) {
                        Set<String> keys = part.getHeaders().keySet();
                        for (String key : keys) {
                            sb.append(
                                    StringUtils.rightPad(key, 24) + " : " + part.getHeaderValue(key)
                                            + System.lineSeparator());
                        }

                        sb.append(part.getBody() + System.lineSeparator());
                    } else {
                        sb.append("part null");
                    }
                }
            } else if (request.getContentLength() > 0) {

                if (contentType.startsWith("application")) {
                    String body = new String(wrequest.toByteArray(),
                            request.getCharacterEncoding() != null ? request.getCharacterEncoding()
                                    : "UTF-8");
                    sb.append(System.lineSeparator() + body + System.lineSeparator());
                }

            }
        } catch (Exception e) {
            logger.error("httpRequestLog : ", e);
        }
        sb.append("============= HTTP REQUEST MESSAGE END ===============\r\n");
        logger.info(sb.toString());
    }

    private void httpResponseLog(HttpServletResponse response) {
        HttpResponseWrapper wresponse = (HttpResponseWrapper) response;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "\r\n============= HTTP %d RESPONSE MESSAGE START ID [%d] ============\r\n",
                response.getStatus(), wresponse.getId()));

        String contentType = null;
        if (((HttpResponseWrapper) response).getResponse() != null) {
            ServletResponse sresponse = ((HttpResponseWrapper) response).getResponse();
            if (sresponse != null && (sresponse.getContentType() != null
                    && !sresponse.getContentType().isEmpty())) {
                contentType = sresponse.getContentType();
                sb.append(String.format("%s : %s\r\n", StringUtils.rightPad("Content-Type", 24),
                        contentType));
            }
        }

        try {
            if ((contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE))) {
                sb.append(System.lineSeparator() + new String(wresponse.toByteArray(), "UTF-8")
                        + System.lineSeparator());
            }
        } catch (Exception e) {
            logger.error("httpResponseLog : ", e);
        }

        sb.append("============= HTTP RESPONSE MESSAGE END ========================\r\n");
        logger.info(sb.toString());
    }

    private HttpHeader getHeaderValue(String hdr) {

        String[] str = hdr.split(":", 2);
        return new HttpHeader(str[0], str[1]);
    }

    private String getAcceptParam(String name, String param) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(param)) {
            return null;
        }
//        try {
        String[] split = StringUtils.split(param, ';');
        if (split != null) {
            for (String s : split) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                if (StringUtils.startsWithIgnoreCase(StringUtils.trim(s), name)) {
                    return s;
                }
            }
        }
        return null;

//        } catch (Exception e) {
//            return null;
//        }
    }

    private String getAcceptParamValue(String name, String param) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(param)) {
            return null;
        }
        try {
            String[] split = StringUtils.split(param, '=');
            if (split != null && split.length > 0) {
                if (StringUtils.startsWithIgnoreCase(StringUtils.trim(split[0]), name)) {
                    return split[1];
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private HttpLogMultiPart getMultiParts(byte[] in) {
        HttpLogMultiPart ret = new HttpLogMultiPart();
        try {
            int i;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //get Headers
            for (i = 0; i < in.length; i++) {
                if (in[i] == '\r' && in[i + 1] == '\n') {
                    if (bos.size() == 0) {
                        i += 2;
                        break; // Header Part end
                    } else {
                        HttpHeader hdr = getHeaderValue(new String(bos.toByteArray()));
                        ret.setHeaders(hdr.getHdr(), hdr.getValue());
                        bos.reset();
                        i = i + 1;
                    }
                } else {
                    bos.write(in[i]);
                }
            }

            // get Body
            if (StringUtils.startsWithIgnoreCase(ret.getContent_Type(),
                    MediaType.APPLICATION_JSON_VALUE)) {
                String charset = getAcceptParamValue("charset",
                        getAcceptParam("charset", ret.getContent_Type()));
                ret.setBody(new String(Arrays.copyOfRange(in, i, in.length),
                        charset != null ? charset : "UTF-8"));
            } else {
                ret.setBody("(non application/json data)");
            }
        } catch (Exception e) {
            logger.error("PARSER ERROR ", e);
        }
        return ret;
    }

    private List<byte[]> getMultiPartAsByteList(byte[] in, String boundary) {
        List<byte[]> bytes = new ArrayList<byte[]>();

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int i;
            byte[] byte_boundary = ("--" + boundary).getBytes();
            for (i = 0; i < in.length; i++) {
                int j;
                for (j = 0; j < byte_boundary.length; j++) {
                    if (j + i >= in.length) {
                        break;
                    }
                    if (in[i + j] != byte_boundary[j]) {
                        break;
                    }
                }
                if (j == byte_boundary.length) {
                    if (in[i + j] == '\r' && in[i + j + 1] == '\n') {
                        j = j + 1;
                    } else if (in[i + j] == '-' && in[i + j + 1] == '-') {
                        bytes.add(Arrays.copyOfRange(bos.toByteArray(), 0, bos.size() - 2));
                        bos.reset();
                        break;
                    }
                    if (bos.size() > 0) {
                        bytes.add(Arrays.copyOfRange(bos.toByteArray(), 0, bos.size() - 2));
                    }
                    bos.reset();
                    i = i + j;
                } else {
                    bos.write(in[i]);
                }
            }
        } catch (Exception e) {
            logger.error("getMultiPart ERROR ", e);
        }
        return bytes;
    }

    class HttpHeader {

        String hdr;
        String value;

        public HttpHeader(String hdr, String value) {
            this.hdr = StringUtils.lowerCase(hdr);
            this.value = value.trim();
        }

        public String getHdr() {
            return hdr;
        }

        public void setHdr(String hdr) {
            this.hdr = hdr;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    class HttpLogMultiPart {

        private static final String CONTENT_TYPE = "content-type";

        Map<String, String> headers = new HashMap<String, String>();
        String body;

        public String getContent_Type() {
            return headers.get(CONTENT_TYPE);
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getHeaderValue(String key) {
            return headers.get(key);
        }

        public void setHeaders(String hdr, String value) {
            headers.put(hdr, value);
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}

