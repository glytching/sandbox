package org.glytching.sandbox.simulator;

import rx.Observable;

public interface Simulator<T> {

    Observable<T> simulate(int simulationCount);
}