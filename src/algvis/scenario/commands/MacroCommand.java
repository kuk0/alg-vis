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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jdom.Element;

public class MacroCommand<T extends Command> implements Command {
	private final String name;
	protected final List<T> commands;
	protected final ListIterator<T> iterator;
	protected T current = null;
	protected int position = -1;

	public MacroCommand(String name) {
		commands = new ArrayList<T>();
		iterator = commands.listIterator();
		this.name = name;
	}

	public void add(T c) {
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		iterator.add(c);
		current = c;
		position = iterator.previousIndex();
	}

	public void executeOne() {
		if (current instanceof MacroCommand
				&& ((MacroCommand<?>) current).hasNext()) {
			current.execute();
			if (iterator.nextIndex() == position) {
				iterator.next();
			}
		} else {
			position = iterator.nextIndex();
			current = iterator.next();
			current.execute();
		}
	}

	public void unexecuteOne() {
		if (current instanceof MacroCommand
				&& ((MacroCommand<?>) current).hasPrevious()) {
			current.unexecute();
			if (iterator.previousIndex() == position) {
				iterator.previous();
			}
		} else {
			position = iterator.previousIndex();
			current = iterator.previous();
			current.unexecute();
		}
	}

	/**
	 * returns true, if macro or submacro has next element
	 */
	public boolean hasNext() {
		if (current instanceof MacroCommand) {
			return ((MacroCommand<?>) current).hasNext() || iterator.hasNext();
		} else {
			return iterator.hasNext();
		}
	}

	/**
	 * returns true, if macro or submacro has previous element
	 */
	public boolean hasPrevious() {
		if (current instanceof MacroCommand) {
			return ((MacroCommand<?>) current).hasPrevious()
					|| iterator.hasPrevious();
		} else {
			return iterator.hasPrevious();
		}
	}

	public void goBefore(int p) throws IndexOutOfBoundsException {
		if (p < 0 || p >= commands.size()) {
			IndexOutOfBoundsException e = new IndexOutOfBoundsException();
			throw e;
		}
		if (p <= position) {
			while (p < position) {
				unexecuteOne();
			}
			if (p == position && ((MacroCommand<?>) current).hasPrevious()) {
				unexecuteOne();
			}
		} else {
			while (p - 1 > position) {
				executeOne();
			}
			if (p - 1 == position && ((MacroCommand<?>) current).hasNext()) {
				executeOne();
			}
		}
	}

	public int getPosition() {
		return position;
	}

	public T getCurrent() {
		return current;
	}

	public boolean isEmpty() {
		return current == null;
	}

	@Override
	public void execute() {
		if (current instanceof MacroCommand) {
			current.execute();
			if (iterator.nextIndex() == position) {
				iterator.next();
			}
		}
		while (iterator.hasNext()) {
			position = iterator.nextIndex();
			current = iterator.next();
			current.execute();
		}
	}

	@Override
	public void unexecute() {
		if (current instanceof MacroCommand) {
			current.unexecute();
			if (iterator.previousIndex() == position) {
				iterator.previous();
			}
		}
		while (iterator.hasPrevious()) {
			position = iterator.previousIndex();
			current = iterator.previous();
			current.unexecute();
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element(name);
		int k = 1;
		for (Command c : commands) {
			Element ch = c.getXML();
			ch.setAttribute("order", Integer.toString(k++));
			e.addContent(ch);
		}
		return e;
	}
}
