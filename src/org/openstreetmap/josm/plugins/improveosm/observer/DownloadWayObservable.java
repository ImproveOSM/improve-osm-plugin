package org.openstreetmap.josm.plugins.improveosm.observer;


public interface DownloadWayObservable {
    void addObserver(final DownloadWayObserver observer);
    void notifyObserver();
}
