package org.vb.resourceslibs;

import com.google.inject.Inject;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class APIHttpLib {

    /* ==========================================
     * =            Constructor                 =
     * ========================================== */
    @Inject
    APIHttpLib() {}

    /* ===============================
     * =             getPage         =
     * ===============================
     * method to get server json files
     * ===============================
     * Receives:
     *  - String strURL: http address
     *  - int intSocketTimeout: Socket Timeout
     *  - int intConnectTimeout: Connection Timeout
     *  - String strQueryPath: query path
     * Returns:
     *  - String strData:
     *    page as a string
     * =============================== */
    @Builder(builderMethodName = "getPageBuilder",
        buildMethodName = "buildgetPage")
    public String getPage(
        String strURL,
        int intSocketTimeout,
        int intConnectTimeout,
        String strQueryPath) {

        /* set async HTTP client */
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(intSocketTimeout)
            .setConnectionRequestTimeout(intConnectTimeout)
            .build();
        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build();

        final String[] strData = {""};

        try {
            String strHost = "";
            if (strQueryPath.isEmpty()) {
                strHost = strHost.concat(strURL);
            } else {
                strHost = strHost.concat(strURL + "/" + strQueryPath);
            }
            httpAsyncClient.start();
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final HttpGet httpGet = new HttpGet(strHost);
            httpAsyncClient.execute(httpGet,
                new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        log.info("{} -> {}",
                            httpGet.getRequestLine(),
                            httpResponse.getStatusLine());
                        try {
                            strData[0] = EntityUtils.toString(httpResponse.getEntity(),
                                Charsets.UTF_8.toString());
                        } catch (IOException ie) {
                            log.error("IOException getting response content: {}",
                                ie.toString());
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                    @Override
                    public void failed(Exception e) {
                        countDownLatch.countDown();
                        log.error("{} -> {}",
                            httpGet.getRequestLine(),
                            e.toString());
                        System.out.println("Exception retrieving server file: " +
                            httpGet.getRequestLine() +
                            "->" +
                            e.toString());
                    }
                    @Override
                    public void cancelled() {
                        countDownLatch.countDown();
                        System.out.println(httpGet.getRequestLine() +
                            " canceled");
                    }
                });
            countDownLatch.await();
            log.info("Shutting down http client.");
        } catch (InterruptedException ie) {
            log.error("Interrupted Exception: {}",
                ie.toString());
        } finally {
            try {
                httpAsyncClient.close();
            } catch (IOException ie) {
                log.error("IOException during closing the http client. Exception: {}",
                    ie.toString());
            }
        }
        return strData[0];
    } /* end of getPage method */

} /* end of class APIHttpLib */

