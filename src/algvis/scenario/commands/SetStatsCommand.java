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

import org.jdom.Element;

import algvis.core.Buttons;

public class SetStatsCommand implements Command {
	private final Buttons B;
	private final String oldStats, newStats;

	public SetStatsCommand(Buttons B, String oldStats, String newStats) {
		this.B = B;
		this.oldStats = oldStats;
		this.newStats = newStats;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setStats");
		e.setAttribute("oldStats", oldStats);
		e.setAttribute("newStats", newStats);
		return e;
	}

	@Override
	public void execute() {
		B.setStats(newStats);
	}

	@Override
	public void unexecute() {
		B.setStats(oldStats);
	}

}
