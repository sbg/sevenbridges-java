/*
 * Copyright 2017 Seven Bridges Genomics, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sevenbridges.apiclient.impl.transfer.http;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class HttpAsyncClientPool {
  private static final Logger log = LoggerFactory.getLogger(HttpAsyncClientPool.class);

  /** The number of CPUs. */
  private static final int AVAIL_PROCS = Runtime.getRuntime().availableProcessors();
  // Make this configurable in later versions
  private static final int DEFAULT_IO_THREAD_COUNT = AVAIL_PROCS;
  private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
  private static final int DEFAULT_SO_TIMEOUT = 10000;
  private static final int DEFAULT_CONN_POOL_MAX_TOTAL = 200;
  private static final int DEFAULT_CONN_POOL_MAX_PER_ROUTE = 50;

  public static CloseableHttpAsyncClient getClient() {
    // The thread safe client is held by the singleton.
    return Singleton.INSTANCE.get();
  }

  public static void shutdown() throws InterruptedException {
    // Shutdown the monitor.
    Singleton.INSTANCE.monitor.shutdown();
  }

  // Single-element enum to implement Singleton.
  private enum Singleton {
    INSTANCE;
    // The thread-safe client.
    private final CloseableHttpAsyncClient threadSafeAsyncClient;
    // The pool monitor.
    private final IdleConnectionEvictorThread monitor;

    Singleton() throws ExceptionInInitializerError {
      // Create I/O reactor configuration
      ConnectingIOReactor ioReactor;
      try {
        ioReactor = new DefaultConnectingIOReactor(
            IOReactorConfig.custom()
                .setIoThreadCount(DEFAULT_IO_THREAD_COUNT)
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSoTimeout(DEFAULT_SO_TIMEOUT)
                .build());
      } catch (IOReactorException e) {
        log.error("Error while initializing HttpAsyncClientPool.");
        throw new ExceptionInInitializerError(e);
      }

      PoolingNHttpClientConnectionManager mngr = new PoolingNHttpClientConnectionManager(ioReactor);
      // Increase max total connection to 200
      mngr.setMaxTotal(DEFAULT_CONN_POOL_MAX_TOTAL);
      // Increase default max connection per route to 50
      mngr.setDefaultMaxPerRoute(DEFAULT_CONN_POOL_MAX_PER_ROUTE);

      // Build the client.
      threadSafeAsyncClient = HttpAsyncClientBuilder.create()
          .setConnectionManager(mngr)
          .build();

      // Start up an eviction
      monitor = new IdleConnectionEvictorThread(mngr);
      // Don't stop quitting.
      monitor.setDaemon(true);
      monitor.start();
    }

    public CloseableHttpAsyncClient get() {
      return threadSafeAsyncClient;
    }
  }

  // Watches for stale connections and evicts them.
  private static class IdleConnectionEvictorThread extends Thread {
    // The manager to watch.
    private final PoolingNHttpClientConnectionManager connMgr;
    // Use a BlockingQueue to stop everything.
    private final BlockingQueue<Stop> stopSignal = new ArrayBlockingQueue<>(1);
    //private volatile boolean shutdown;

    IdleConnectionEvictorThread(PoolingNHttpClientConnectionManager connMgr) {
      super("http-idle-connection-evictor");
      this.connMgr = connMgr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
      try {
        // Holds the stop request that stopped the process.
        Stop stopRequest;
        // Every 15 seconds.
        while ((stopRequest = stopSignal.poll(15, TimeUnit.SECONDS)) == null) {
          // Close expired connections
          connMgr.closeExpiredConnections();
          // Optionally, close connections that have been idle too long.
          connMgr.closeIdleConnections(60, TimeUnit.SECONDS);
          // Look at pool stats.
          log.trace("Stats: {}", connMgr.getTotalStats());
        }
        // Acknowledge the stop request.
        stopRequest.stopped();
      } catch (InterruptedException ex) {
        // terminate
      }
    }

    public void shutdown() throws InterruptedException {
      log.trace("Shutting down client pool");
      // Signal the stop to the thread.
      Stop stop = new Stop();
      stopSignal.add(stop);
      // Wait for the stop to complete.
      stop.waitForStopped();
      try {
        // Close the pool - Added
        Singleton.INSTANCE.threadSafeAsyncClient.close();
      } catch (IOException e) {
        log.error("Error closing client");
      }
      try {
        // Close the connection manager.
        connMgr.shutdown();
      } catch (IOException e) {
        log.error("Error shutting down connection manager");
      }
      log.trace("Client pool shut down");
    }

    // Pushed up the queue.
    private static class Stop {
      // The return queue.
      private final BlockingQueue<Stop> stop = new ArrayBlockingQueue<>(1);

      // Called by the process that is being told to stop.
      void stopped() {
        // Push me back up the queue to indicate we are now stopped.
        stop.add(this);
      }

      // Called by the process requesting the stop.
      void waitForStopped() throws InterruptedException {
        // Wait until the callee acknowledges that it has stopped.
        stop.take();
      }

    }

  }
}
