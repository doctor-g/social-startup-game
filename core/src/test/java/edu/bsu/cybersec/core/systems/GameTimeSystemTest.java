/*
 * Copyright 2015 Paul Gestwicki
 *
 * This file is part of The Social Startup Game
 *
 * The Social Startup Game is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Social Startup Game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The Social Startup Game.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.cybersec.core.systems;

import org.junit.Test;

import static org.junit.Assert.*;

public final class GameTimeSystemTest extends AbstractSystemTest {

    private GameTimeSystem system;

    @Override
    public void setUp() {
        super.setUp();
        system = new GameTimeSystem(world);
        new UpdatingSystem(world);
    }

    @Test
    public void testScale_measuresGameTimeUnitsPerClockUnits_times10() {
        system.scale().update(10f);
        advancePlayNClockOneSecond();
        assertEquals(10, now());
    }

    private int now() {
        return world.gameTime.get().now;
    }

    @Test
    public void testPreviousTimeIsAdvanced() {
        whenSomeTimeElapses();
        whenSomeTimeElapses();
        assertTrue(previous() > 0);
    }

    private int previous() {
        return world.gameTime.get().previous;
    }

    @Test
    public void testSystemDisable_timeStopsMoving() {
        system.setEnabled(false);
        whenSomeTimeElapses();
        assertEquals(0, now());
    }

    @Test
    public void testSystemDisable_noElapsedTime() {
        system.setEnabled(false);
        whenSomeTimeElapses();
        assertTrue(now() == previous());
    }

    @Test
    public void testSystemDisableAfterHavingRun_noElapsedTime() {
        whenSomeTimeElapses();
        system.setEnabled(false);
        whenSomeTimeElapses();
        assertTrue(now() == previous());
    }

    @Test
    public void testScale_doublingScaleDoublesDeltas() {
        whenOneSecondOfGameTimeElapses();
        final float delta1 = world.gameTime.get().delta();
        system.scale().update(system.scale().get() * 2);
        whenOneSecondOfGameTimeElapses();
        final float delta2 = world.gameTime.get().delta();
        assertEquals(delta1 * 2, delta2, EPSILON);
    }

    @Test
    public void testIsEnabled_startsTrue() {
        assertTrue(system.isEnabled());
    }

    @Test
    public void testIsEnabled_falseAfterSetEnabled() {
        system.setEnabled(false);
        assertFalse(system.isEnabled());
    }

}
