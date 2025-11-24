package com.griep.postmortem.service.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Accumulator {
    private int incidents = 0;
    private List<Integer> mtta = new ArrayList<>();
    private List<Integer> mttr = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();
    private int done = 0;
    private int doneOnTime = 0;

    public void incrementIncidents() {
        this.incidents++;
    }

    public void incrementDone() {
        this.done++;
    }

    public void incrementDoneOnTime() {
        this.doneOnTime++;
    }
}
