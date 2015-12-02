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

package edu.bsu.cybersec.core.narrative;

import edu.bsu.cybersec.core.ClockUtils;
import edu.bsu.cybersec.core.GameConfig;
import edu.bsu.cybersec.core.GameWorld;
import edu.bsu.cybersec.core.NarrativeEvent;
import tripleplay.entity.Entity;

import static com.google.common.base.Preconditions.checkNotNull;

public final class DefaultNarrativeScript {

    private GameWorld world;

    public void createIn(GameWorld world, GameConfig config) {
        this.world = checkNotNull(world);
        if (!config.skipWelcome()) {
            now().runEvent(new WelcomeEvent(world));
        }
        hour(2).runEvent(new ScriptKiddieAttackEvent(world));
        hour(4).runEvent(new SecurityConferenceEvent(world));
        hour(6).runEvent(new DataStolenNotifyChoiceEvent(world));
        hour(8).runEvent(new InputSanitizationEvent(world));
        hour(10).runEvent(new DDOSEvent(world));
    }

    private TimedEventBuilder now() {
        return new TimedEventBuilder(world.gameTime.get().now);
    }

    private TimedEventBuilder hour(int hour) {
        return new TimedEventBuilder(world.gameTime.get().now).addHours(hour);
    }

    private final class TimedEventBuilder {
        private int trigger;

        private TimedEventBuilder(int trigger) {
            this.trigger = trigger;
        }

        public void runEvent(NarrativeEvent event) {
            Entity e = world.create(true).add(world.event, world.timeTrigger);
            world.event.set(e.id, event);
            world.timeTrigger.set(e.id, trigger);
        }

        public TimedEventBuilder addHours(int hour) {
            trigger += hour * ClockUtils.SECONDS_PER_HOUR;
            return this;
        }
    }

}
