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
package algvis.ds.suffixtree;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.Stack;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.trie.TrieNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class SuffixTreeNode extends TrieNode {
    private SuffixTreeNode suffixLink = null; // also called suffixNode
    private boolean packed;
    static boolean implicitNodes = false;

    public SuffixTreeNode(DataStructure D, int key, int x, int y, boolean packed) {
        super(D, key, x, y);
        setPacked(packed);
    }

    public SuffixTreeNode(DataStructure D, int key, char ch, boolean packed) {
        super(D, key, ch);
        setPacked(packed);
    }

    public SuffixTreeNode(DataStructure D, int key, boolean packed) {
        super(D, key);
        setPacked(packed);
    }

    public SuffixTreeNode(DataStructure D, char ch, int x, int y, boolean packed) {
        super(D, ch, x, y);
        setPacked(packed);
    }

    private SuffixTreeNode(DataStructure D, char ch, boolean packed) {
        super(D, ch);
        setPacked(packed);
    }

    public SuffixTreeNode(DataStructure D, boolean packed) {
        super(D);
        setPacked(packed);
    }

    public SuffixTreeNode(DataStructure D) {
        super(D);
        setPacked(false);
    }

    @Override
    public SuffixTreeNode getParent() {
        return (SuffixTreeNode) super.getParent();
    }

    @Override
    public SuffixTreeNode getChild() {
        return (SuffixTreeNode) super.getChild();
    }

    @Override
    public SuffixTreeNode getRight() {
        return (SuffixTreeNode) super.getRight();
    }

    @Override
    public SuffixTreeNode getChildWithCH(char ch) {
        return (SuffixTreeNode) super.getChildWithCH(ch);
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    @Override
    protected void drawBg(View v) {
        if (implicitNodes) {
            super.drawBg(v);
            if (isPacked()) {
                v.setColor(Color.WHITE);
                v.fillCircle(x, y, radius - 1);
            }
        } else {
            if (!isPacked()) {
                super.drawBg(v);
            }
        }
    }

    SuffixTreeNode addRight(char ch, int x, int y, boolean packed) {
        if (getLabel() > ch) {
            final SuffixTreeNode u = new SuffixTreeNode(D, ch, packed);
            u.setParent(getParent());
            u.setRight(this);
            getParent().setChild(u);
            return u;
        } else if (getLabel() == ch) {
            return this;
        } else {
            final SuffixTreeNode v = getRight();
            if ((v == null)) {
                final SuffixTreeNode u = new SuffixTreeNode(D, ch, packed);
                u.setParent(getParent());
                setRight(u);
                return u;
            } else {
                if (v.getLabel() > ch) {
                    final SuffixTreeNode u = new SuffixTreeNode(D, ch, packed);
                    u.setRight(getRight());
                    u.setParent(getParent());
                    setRight(u);
                    return u;
                } else if (v.getLabel() < ch) {
                    return v.addRight(ch, x, y, packed);
                } else {
                    // if (v.getLabel() == ch)
                    return v;
                }
            }
        }
    }

    public TrieNode addChild(char ch, int x, int y, boolean packed) {
        final SuffixTreeNode v = getChild();
        if (v == null) {
            final SuffixTreeNode u = new SuffixTreeNode(D, ch, x, y, packed);
            setChild(u);
            u.setParent(this);
            return u;
        } else {
            return v.addRight(ch, x, y, packed);
        }
    }

    public SuffixTreeNode getSuffixLink() {
        return suffixLink;
    }

    public void setSuffixLink(SuffixTreeNode suffixLink) {
        this.suffixLink = suffixLink;
    }

    @Override
    public void draw(View v) {
        if (state == Node.INVISIBLE || getKey() == NULL) {
            return;
        }
        drawBg(v);
        drawLabel(v);
        drawArrow(v);
        drawArc(v);
        if (isLeaf() && !isRoot()) {
            v.setColor(Color.WHITE);
            v.fillCircle(x, y + 11, 7);
            v.setColor(Color.BLACK);
            if (marked) {
                v.drawCircle(x, y + 11, 7);
            }
            v.drawString(String.valueOf(getKey()), x, y + 10, Fonts.TYPEWRITER);
        }
    }

    void drawSuffixLinks(View v) {
        SuffixTreeNode child = getChild();
        while (child != null) {
            child.drawSuffixLinks(v);
            child = child.getRight();
        }

        if (getSuffixLink() != null) {
            if (isPacked()) {
                v.setColor(new Color(0xffaaaa));
            } else {
                v.setColor(new Color(0xcccccc));
            }
            final SuffixTreeNode w = getSuffixLink();
            final Point2D p = v.cut(x, y, w.x, w.y + 1, 10);
            v.drawArrow(x, y, (int) p.getX(), (int) p.getY());
            v.setColor(Color.BLACK);
        }
    }

    @Override
    public void drawTree(View v) {
        drawSuffixLinks(v);
        super.drawTree(v);
    }

    @Override
    public void drawLabel(View v) {
        if (implicitNodes) {
            super.drawLabel(v);
        } else {
            if (getParent() == null) {
                return;
            }
            TrieNode u = this;
            final StringBuilder s = new StringBuilder("");
            final Stack<Color> col = new Stack<Color>();
            if (getChild() == null || getChild().getRight() != null) {
                while (u != null && u.getParent() != null
                    && u.getParent().getChild() == u && u.getRight() == null) {
                    s.append(u.ch);
                    col.add(u.getBgColor());
                    u = u.getParent();
                }
                if (u != null && u.getParent() != null) {
                    s.append(u.ch);
                    col.add(u.getBgColor());
                }
                if (u == null) {
                    System.out.println("Something went wrong at [" + x + ","
                        + y + "]");
                    return;
                }
                int py = u.y;
                if (u.getParent() == null) {
                    py += 30;
                }
                s.reverse();
                final int fonth = Fonts.TYPEWRITER.fm.getHeight(), len = s
                    .length();
                final int midy = (py + y) / 2, w = 6, h = len * fonth / 2;
                final int xx = x;
                int yy = midy - fonth * len / 2 - 4;
                Color cc = col.pop();
                v.setColor(cc);
                if (col.empty()) {
                    v.fillRoundRectangle(xx, yy, 6, fonth / 2.0, 6, 10);
                } else {
                    v.fillRoundRectangle(xx, yy - 3, 6, fonth / 2.0 - 3, 6, 10);
                    v.fillRect(xx, yy + 3, 6, fonth / 2.0 - 3);
                    yy += fonth;
                    while (col.size() > 1) {
                        cc = col.pop();
                        v.setColor(cc);
                        v.fillRect(xx, yy, 6, fonth / 2.0);
                        //v.setColor(Color.BLACK);
                        //v.drawRoundRectangle(xx, yy, 6, fonth / 2.0, 0, 0);
                        yy += fonth;
                    }
                    cc = col.pop();
                    v.setColor(cc);
                    v.fillRect(xx, yy - 3, 6, fonth / 2.0 - 3);
                    v.fillRoundRectangle(xx, yy + 3, 6, fonth / 2.0 - 3, 6, 10);
                }

                v.setColor(Color.BLACK);
                v.drawRoundRectangle(x, midy - 12, w, h, 6, 10);

                v.setColor(getFgColor());
                v.drawVerticalString(s.toString(), x - 3, midy - 1,
                    Fonts.TYPEWRITER);
                // System.out.println(u.y + " " + py + " " + midy + " " + y);
            }
        }
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "suffixLink", suffixLink);
        HashtableStoreSupport.store(state, hash + "packed", packed);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object suffixLink = state.get(hash + "suffixLink");
        if (suffixLink != null) {
            this.suffixLink = (SuffixTreeNode) HashtableStoreSupport
                .restore(suffixLink);
        }
        final Object packed = state.get(hash + "packed");
        if (packed != null) {
            this.packed = (Boolean) HashtableStoreSupport.restore(packed);
        }
    }
}
