/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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

import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import algvis.core.visual.Scene;

public class HistoryManager extends UndoManager {
    private static final long serialVersionUID = -842354204488084104L;
    private final Map<UndoableEdit, Boolean> algorithmEnds = new WeakHashMap<>();
    private long id = -1;
    private long savedEditId;
    private UpdatableStateEdit lastState;

    public HistoryManager(Scene scene) {
        super();
        setLimit(500);
    }

    @Override
    public synchronized boolean addEdit(UndoableEdit anEdit) {
        return anEdit instanceof UpdatableStateEdit && super.addEdit(anEdit);
    }

    private long getEditId() {
        if (canUndo()) {
            return editToBeUndone().getId();
        } else {
            return -1;
        }
    }

    public synchronized void putAlgorithmEnd() {
        final UndoableEdit edit = editToBeUndone();
        if (edit != null) {
            algorithmEnds.put(edit, true);
        }
    }

    public synchronized void undoAlgorithm() {
        do {
            undo();
        } while (canUndo() && !algorithmEnds.containsKey(editToBeUndone()));
    }

    public synchronized void redoAlgorithm() {
        do {
            redo();
        } while (!algorithmEnds.containsKey(editToBeUndone()) && canRedo());
    }

    public void saveEditId() {
        savedEditId = getEditId();
    }

    public void rewind() {
        while (canUndo() && editToBeUndone().getId() != savedEditId) {
            undo();
        }
        redo();
    }

    public synchronized boolean isBetweenAlgorithms() {
        final UndoableEdit e = editToBeUndone();
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

    public void trimToEnd() {
        final AbstractUndoableEdit fakeEdit = new AbstractUndoableEdit();
        if (super.addEdit(fakeEdit)) {
            final int lastEditIndex = edits.size() - 1;
            trimEdits(lastEditIndex, lastEditIndex);
        }
    }

    public void firstEdit(StateEditable panel) {
        lastState = new UpdatableStateEdit(panel, ++id);
        addEdit(lastState);
    }

    public void nextEdit(StateEditable panel) {
        lastState.end();
        lastState = new UpdatableStateEdit(panel, ++id);
        addEdit(lastState);
    }

    public void finishEdits() {
        lastState.end();
        putAlgorithmEnd();
    }

    public void addToPreState(StateEditable element) {
        lastState.addToPreState(element);
    }
}
