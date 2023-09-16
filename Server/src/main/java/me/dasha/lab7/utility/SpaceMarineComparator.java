package me.dasha.lab7.utility;

import me.dasha.lab7.collectionClasses.SpaceMarine;

import java.util.Comparator;

public class SpaceMarineComparator implements Comparator<SpaceMarine> {
    @Override
    public int compare(SpaceMarine a, SpaceMarine b) {
        if (a.getHealth() > b.getHealth())
            return 1;
        else
            return 0;
    }
}
