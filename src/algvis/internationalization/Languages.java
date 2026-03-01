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
package algvis.internationalization;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Languages {
    private static final Map<String, ResourceBundle> BUNDLES = new LinkedHashMap<>();
    private static String current_tag = "en";
    private static final List<LanguageListener> listeners = new LinkedList<>();

    private static ResourceBundle loadBundle(String path) {
        try (InputStream is = Languages.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new MissingResourceException(
                    "Resource not found: " + path, Languages.class.getName(), path);
            }
            return new PropertyResourceBundle(is);
        } catch (IOException e) {
            throw new MissingResourceException(
                "Failed to load: " + path, Languages.class.getName(), path);
        }
    }

    static {
        BUNDLES.put("en", loadBundle("/Messages_en.properties"));
        BUNDLES.put("sk", loadBundle("/Messages_sk.properties"));
    }

    public static void addListener(LanguageListener l) {
        listeners.add(l);
    }

    public static void selectLanguage(String tag) {
        if (!BUNDLES.containsKey(tag)) {
            tag = "en";
        }
        current_tag = tag;
        for (final LanguageListener l : listeners) {
            l.languageChanged();
        }
    }

    public static String getCurrentLanguageTag() {
        return current_tag;
    }

    public static String getString(String s) {
        try {
            return BUNDLES.get(current_tag).getString(s);
        } catch (final MissingResourceException e) {
            System.err.println(e.getMessage() + ": " + s);
            return "???";
        }
    }
}
