package com.springboot.project.citycab.strategies.manager;

import com.springboot.project.citycab.exceptions.DistanceRestClientServiceException;
import com.springboot.project.citycab.services.impl.DistanceServiceHereRoutingImpl;
import com.springboot.project.citycab.services.impl.DistanceServiceOSRMImpl;
import com.springboot.project.citycab.services.impl.DistanceServiceOtherRoutingImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistanceServiceManager {

    private final DistanceServiceOSRMImpl distanceServiceOSRMImpl;
    private final DistanceServiceHereRoutingImpl distanceServiceHereRoutingImpl;
    private final DistanceServiceOtherRoutingImpl distanceServiceOtherRoutingImpl;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Attempts to calculate distance using OSRM with a timeout.
     * If OSRM fails or takes more than 1 second, falls back to Here Routing.
     * If Here Routing fails or takes more than 1 second, falls back to Other Routing Service.
     *
     * @param src  Source location.
     * @param dest Destination location.
     * @return Distance in kilometers.
     */
    public double calculateDistance(Point src, Point dest) {
        Future<Double> osrmFuture = executor.submit(() -> distanceServiceOSRMImpl.calculateDistance(src, dest));

        try {
            // Attempt to get the OSRM result within 1 second
            return osrmFuture.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException te) {
            System.out.println("OSRM API call timed out after 2 second. Falling back to Here Routing.");
            osrmFuture.cancel(true); // Cancel the OSRM task
            return calculateWithHereRouting(src, dest);
        } catch (ExecutionException ee) {
            System.out.println("OSRM API execution failed: " + ee.getCause().getMessage() + ". Falling back to Here Routing.");
            return calculateWithHereRouting(src, dest);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted during OSRM API call. Falling back to Here Routing.");
            return calculateWithHereRouting(src, dest);
        }
    }

    /**
     * Attempts to calculate distance using Here Routing with a timeout.
     * If Here Routing fails or takes more than 1 second, falls back to Other Routing Service.
     *
     * @param src  Source location.
     * @param dest Destination location.
     * @return Distance in kilometers.
     */
    private double calculateWithHereRouting(Point src, Point dest) {
        Future<Double> hereRoutingFuture = executor.submit(() -> distanceServiceHereRoutingImpl.calculateDistance(src, dest));

        try {
            // Attempt to get the Here Routing result within 1 second
            return hereRoutingFuture.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException te) {
            System.out.println("Here Routing API call timed out after 2 second. Falling back to Other Routing Service.");
            hereRoutingFuture.cancel(true); // Cancel the Here Routing task
            return calculateWithOtherRouting(src, dest);
        } catch (ExecutionException ee) {
            System.out.println("Here Routing API execution failed: " + ee.getCause().getMessage() + ". Falling back to Other Routing Service.");
            return calculateWithOtherRouting(src, dest);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted during Here Routing API call. Falling back to Other Routing Service.");
            return calculateWithOtherRouting(src, dest);
        }
    }

    /**
     * Attempts to calculate distance using Other Routing Service with a timeout.
     * If Other Routing Service fails or takes more than 1 second, throws an exception.
     *
     * @param src  Source location.
     * @param dest Destination location.
     * @return Distance in kilometers.
     */
    private double calculateWithOtherRouting(Point src, Point dest) {
        Future<Double> otherRoutingFuture = executor.submit(() -> distanceServiceOtherRoutingImpl.calculateDistance(src, dest));

        try {
            // Attempt to get the Other Routing result within 1 second
            return otherRoutingFuture.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException te) {
            System.out.println("Other Routing API call timed out after 2 second.");
            otherRoutingFuture.cancel(true); // Cancel the Other Routing task
            throw new DistanceRestClientServiceException("All distance calculation services failed due to timeouts.");
        } catch (ExecutionException ee) {
            System.out.println("Other Routing API execution failed: " + ee.getCause().getMessage());
            throw new DistanceRestClientServiceException("All distance calculation services failed.", ee.getCause());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted during Other Routing API call.");
            throw new DistanceRestClientServiceException("Thread was interrupted during distance calculation.", ie);
        }
    }
}

