/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.core.data.storage;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.event.JsonEventDataStorageBackend;

public class EventDataStorage extends AbstractDataStorage<EventStructure, EventDataStorageBackendInterface> {

    private static EventDataStorage instance = null;

    private final Context context;

    public static EventDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new EventDataStorage(context);
            instance.storage_backend_list = new EventDataStorageBackendInterface[] {
                    JsonEventDataStorageBackend.getInstance(context),
            };
        }
        return instance;
    }

    private EventDataStorage(Context context) {
        this.context = context;
    }

    @Override
    boolean isSafeToDelete(String name) {
        return StorageHelper.isSafeToDeleteEvent(context, name);
    }

    /**
     * Edit an existing {@link EventStructure} and handles name changing if any.
     * {@inheritDoc}
     */
    public boolean edit(String oldName, EventStructure event) throws IOException {
        boolean success;
        success = super.edit(oldName, event);
        if (success) {
            if (!oldName.equals(event.getName())) {
                handleEventRename(oldName, event.getName());
            }
        }
        return success;
    }

    public List<EventTree> getEventTrees() {
        return StorageHelper.eventListToTrees(allEvents());
    }

    List<EventStructure> allEvents() {
        List<EventStructure> list = null;
        for (EventDataStorageBackendInterface backend : storage_backend_list) {
            if (list == null)
                list = backend.all();
            else
                list.addAll(backend.all());
        }
        return list;
    }

    void handleProfileRename(String oldName, String newName) throws IOException {
        for (EventDataStorageBackendInterface backend : storage_backend_list) {
            for (EventStructure eventStructure : backend.all()) {
                if (oldName.equals(eventStructure.getProfileName())) {
                    eventStructure.setProfileName(newName);
                    backend.update(eventStructure);
                }
            }
        }
    }

    /**
     *
     * @param oldName
     * @param newName
     * @throws IOException
     */
    void handleEventRename(String oldName, String newName) throws IOException {
        // alter subnodes to point to the new name
        List<EventStructure> subs = StorageHelper.eventParentMap(allEvents()).get(oldName);
        if (subs != null) {
            for (EventStructure sub : subs) {
                sub.setParentName(newName);
                update(sub);
            }
        }
    }
}
