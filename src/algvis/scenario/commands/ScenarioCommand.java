/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.scenario.commands;

public class ScenarioCommand extends
		MacroCommand<MacroCommand<MacroCommand<Command>>> {

	public ScenarioCommand(String name) {
		super(name);
	}

	@Override
	public void unexecuteOne() {
		if (current.hasPrevious()) {
			current.unexecuteOne();
			if (!current.hasPrevious() && iterator.previousIndex() == position) {
				iterator.previous();
			}
		} else {
			position = iterator.previousIndex();
			current = iterator.previous();
			current.unexecuteOne();
		}
		if (!current.hasPrevious() && iterator.hasPrevious()) {
			position = iterator.previousIndex();
			current = iterator.previous();
			iterator.next();
		}
	}

	@Override
	public void executeOne() {
		if (current.hasNext()) {
			current.executeOne();
			if (!current.hasNext() && iterator.nextIndex() == position) {
				iterator.next();
			}
		} else {
			position = iterator.nextIndex();
			current = iterator.next();
			current.executeOne();
		}
	}

	@Override
	public void execute() {
		if (current.hasNext()) {
			current.execute();
			if (!current.hasNext() && iterator.nextIndex() == position) {
				iterator.next();
			}
		} else {
			position = iterator.nextIndex();
			current = iterator.next();
			current.execute();
		}
	}

	@Override
	public void unexecute() {
		if (current.hasPrevious()) {
			current.unexecute();
			if (!current.hasPrevious() && iterator.previousIndex() == position) {
				iterator.previous();
			}
		} else {
			position = iterator.previousIndex();
			current = iterator.previous();
			current.unexecute();
		}
		if (!current.hasPrevious() && iterator.hasPrevious()) {
			position = iterator.previousIndex();
			current = iterator.previous();
			iterator.next();
		}
	}

}
