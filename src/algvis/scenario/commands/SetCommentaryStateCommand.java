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

import algvis.core.Commentary;
import algvis.core.Commentary.State;

public class SetCommentaryStateCommand implements Command {
	private final State fromState, toState;
	private final Commentary c;

	public SetCommentaryStateCommand(Commentary c, State fromState) {
		this.c = c;
		this.fromState = fromState;
		this.toState = c.getState();
	}

	@Override
	public Element getXML() {
		Element e = new Element("saveCommentary");
		return e;
	}

	@Override
	public void execute() {
		c.restoreState(toState);
	}

	@Override
	public void unexecute() {
		c.restoreState(fromState);
	}

}
