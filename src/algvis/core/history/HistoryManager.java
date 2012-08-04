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
package algvis.core.history;

import algvis.gui.VisPanel;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.util.Map;
import java.util.WeakHashMap;

public class HistoryManager extends UndoManager {
	private final Map<UndoableEdit, Boolean> algorithmEnds = new WeakHashMap<UndoableEdit, Boolean>();
	private long id = -1; // ak sa v paneli vykona viac ako 2^63 krokov, tak mame problem 
	private final VisPanel panel;

	public HistoryManager(VisPanel panel) {
		super();
		this.panel = panel;
		setLimit(500);
	}

	@Override
	public synchronized boolean addEdit(UndoableEdit anEdit) {
		return anEdit instanceof UpdatableStateEdit && super.addEdit(anEdit);
	}

	public long getNextId() {
		return ++id;
	}
	
	public long getLastEditId() {
		return id;
	}
	
	public synchronized void putAlgorithmEnd() {
		UndoableEdit edit = editToBeUndone();
		if (edit != null) {
			algorithmEnds.put(edit, true);
		}
	}

	public synchronized void undoAlgorithm() {
		undo();
		UndoableEdit edit = editToBeUndone();
		while(!algorithmEnds.containsKey(edit) && canUndo()) {
			undo();
			edit = editToBeUndone();
		}
	}
	
	public synchronized void redoAlgorithm() {
		UndoableEdit edit = editToBeRedone();
		while(!algorithmEnds.containsKey(edit)) {
			redo();
			edit = editToBeRedone();
		}
		redo();
	}
	
	public synchronized void goTo(long id) {
		if (id <= editToBeUndone().getId()) {
			while(canUndo() && editToBeUndone().getId() >= id) undo();
			panel.scene.endAnimation();
			redo();
		} else {
			while(editToBeRedone().getId() < id) redo();
			redo();
		}
	}
	
	public synchronized boolean isBetweenAlgorithms() {
		UndoableEdit e = editToBeUndone();
		return e == null || algorithmEnds.containsKey(e);
	}

	@Override
	protected UpdatableStateEdit editToBeUndone() {
		return (UpdatableStateEdit) super.editToBeUndone();
	}

	@Override
	protected UpdatableStateEdit editToBeRedone() {
		return (UpdatableStateEdit) super.editToBeRedone();
	}
}
