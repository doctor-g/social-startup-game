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

import edu.bsu.cybersec.core.UsersPerHourState;
import org.junit.Test;
import tripleplay.entity.Entity;

import static org.junit.Assert.*;

public class UserGenerationSystemTest extends AbstractSystemTest {

    @Override
    public void setUp() {
        super.setUp();
        new UserGenerationSystem(world);
    }

    @Test
    public void testOnePerHour_GainOneUser() {
        createEntityGeneratingUsersPerHour(1);
        whenOneHourOfGameTimeElapses();
        assertIntegerNumberOfUsersIs(1);
    }

    private Entity createEntityGeneratingUsersPerHour(float usersPerHour) {
        Entity entity = world.create(true)
                .add(world.usersPerHour, world.usersPerHourState);
        world.usersPerHourState.set(entity.id, UsersPerHourState.ACTIVE.value);
        world.usersPerHour.set(entity.id, usersPerHour);
        return entity;
    }

    private void assertIntegerNumberOfUsersIs(int users) {
        assertEquals(users, world.users.get().intValue(), EPSILON);
    }

    @Test
    public void testFivePerHour_gainFiveUsers() {
        createEntityGeneratingUsersPerHour(5);
        whenOneHourOfGameTimeElapses();
        assertIntegerNumberOfUsersIs(5);
    }

    @Test
    public void testOnePerTwoHours_oneSecondsElapse_noUsers() {
        createEntityGeneratingUsersPerHour(0.5f);
        whenOneHourOfGameTimeElapses();
        assertIntegerNumberOfUsersIs(0);
    }

    @Test
    public void testOnePerTwoHours_twoHoursElapse_oneUser() {
        createEntityGeneratingUsersPerHour(0.5f);
        whenOneHourOfGameTimeElapses();
        whenOneHourOfGameTimeElapses();
        assertIntegerNumberOfUsersIs(1);
    }

    @Test
    public void testUpdate_usersPerHourInactive_noChange() {
        Entity e = createEntityGeneratingUsersPerHour(5);
        world.usersPerHourState.set(e.id, UsersPerHourState.INACTIVE.value);
        whenOneHourOfGameTimeElapses();
        assertIntegerNumberOfUsersIs(0);
    }

    @Test
    public void testOnServerDownSignal_someTimePasses_noUsersGained() {
        createEntityGeneratingUsersPerHour(10);
        whenOneHourOfGameTimeElapses();
        int usersBeforeServerDown = world.users.get().intValue();
        world.onServerDownEvent.emit();
        whenOneHourOfGameTimeElapses();
        int usersAfterServerDownAndTimeElapsed = world.users.get().intValue();
        assertEquals(usersBeforeServerDown, usersAfterServerDownAndTimeElapsed);
    }

    @Test
    public void testOnServerBackUp_oneHourElapses_gainsTenUsers() {
        createEntityGeneratingUsersPerHour(10);
        world.onServerDownEvent.emit();
        whenOneHourOfGameTimeElapses();
        world.onServerBackUpEvent.emit();
        whenOneHourOfGameTimeElapses();
        assertIntegerNumberOfUsersIs(10);
    }
}
