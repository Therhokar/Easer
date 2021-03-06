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

package ryey.easer.plugins;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.CommonHelper;
import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.plugins.event.battery.BatteryEventPlugin;
import ryey.easer.plugins.event.bluetooth_device.BTDeviceEventPlugin;
import ryey.easer.plugins.event.broadcast.BroadcastEventPlugin;
import ryey.easer.plugins.event.calendar.CalendarEventPlugin;
import ryey.easer.plugins.event.celllocation.CellLocationEventPlugin;
import ryey.easer.plugins.event.connectivity.ConnectivityEventPlugin;
import ryey.easer.plugins.event.date.DateEventPlugin;
import ryey.easer.plugins.event.dayofweek.DayOfWeekEventPlugin;
import ryey.easer.plugins.event.headset.HeadsetEventPlugin;
import ryey.easer.plugins.event.nfc_tag.NfcTagEventPlugin;
import ryey.easer.plugins.event.notification.NotificationEventPlugin;
import ryey.easer.plugins.event.sms.SmsEventPlugin;
import ryey.easer.plugins.event.tcp_trip.TcpTripEventPlugin;
import ryey.easer.plugins.event.time.TimeEventPlugin;
import ryey.easer.plugins.event.timer.TimerEventPlugin;
import ryey.easer.plugins.event.wifi.WifiEventPlugin;
import ryey.easer.plugins.operation.airplane_mode.AirplaneModeOperationPlugin;
import ryey.easer.plugins.operation.alarm.AlarmOperationPlugin;
import ryey.easer.plugins.operation.bluetooth.BluetoothOperationPlugin;
import ryey.easer.plugins.operation.brightness.BrightnessOperationPlugin;
import ryey.easer.plugins.operation.broadcast.BroadcastOperationPlugin;
import ryey.easer.plugins.operation.cellular.CellularOperationPlugin;
import ryey.easer.plugins.operation.command.CommandOperationPlugin;
import ryey.easer.plugins.operation.event_control.EventControlOperationPlugin;
import ryey.easer.plugins.operation.hotspot.HotspotOperationPlugin;
import ryey.easer.plugins.operation.launch_app.LaunchAppOperationPlugin;
import ryey.easer.plugins.operation.media_control.MediaControlOperationPlugin;
import ryey.easer.plugins.operation.network_transmission.NetworkTransmissionOperationPlugin;
import ryey.easer.plugins.operation.ringer_mode.RingerModeOperationPlugin;
import ryey.easer.plugins.operation.rotation.RotationOperationPlugin;
import ryey.easer.plugins.operation.send_notification.SendNotificationOperationPlugin;
import ryey.easer.plugins.operation.send_sms.SendSmsOperationPlugin;
import ryey.easer.plugins.operation.synchronization.SynchronizationOperationPlugin;
import ryey.easer.plugins.operation.volume.VolumeOperationPlugin;
import ryey.easer.plugins.operation.wifi.WifiOperationPlugin;

/**
 * Used to tell the app what plugins can be used.
 *
 * To register a new plugin, simply write a new line in the constructor of this class.
 */
final public class PluginRegistry {

    private static final int TYPE_OPERATION = 0;
    private static final int TYPE_EVENT = 1;

    private final Registry<EventPlugin, EventData> eventPluginRegistry = new Registry<>(TYPE_EVENT);
    private final Registry<OperationPlugin, OperationData> operationPluginRegistry = new Registry<>(TYPE_OPERATION);
    private final OverallRegistry overallRegistry = new OverallRegistry(new PluginLookuper[] {
            eventPluginRegistry, operationPluginRegistry,
    });

    {
        event().registerPlugin(TimeEventPlugin.class);
        event().registerPlugin(DateEventPlugin.class);
        event().registerPlugin(WifiEventPlugin.class);
        event().registerPlugin(CellLocationEventPlugin.class);
        event().registerPlugin(BatteryEventPlugin.class);
        event().registerPlugin(DayOfWeekEventPlugin.class);
        event().registerPlugin(BTDeviceEventPlugin.class);
        event().registerPlugin(ConnectivityEventPlugin.class);
        event().registerPlugin(CalendarEventPlugin.class);
        event().registerPlugin(BroadcastEventPlugin.class);
        event().registerPlugin(SmsEventPlugin.class);
        event().registerPlugin(NotificationEventPlugin.class);
        event().registerPlugin(TimerEventPlugin.class);
        event().registerPlugin(NfcTagEventPlugin.class);
        event().registerPlugin(HeadsetEventPlugin.class);
        event().registerPlugin(TcpTripEventPlugin.class);

        operation().registerPlugin(WifiOperationPlugin.class);
        operation().registerPlugin(CellularOperationPlugin.class);
        operation().registerPlugin(BluetoothOperationPlugin.class);
        operation().registerPlugin(RotationOperationPlugin.class);
        operation().registerPlugin(BroadcastOperationPlugin.class);
        operation().registerPlugin(BrightnessOperationPlugin.class);
        operation().registerPlugin(RingerModeOperationPlugin.class);
        operation().registerPlugin(CommandOperationPlugin.class);
        operation().registerPlugin(HotspotOperationPlugin.class);
        operation().registerPlugin(SynchronizationOperationPlugin.class);
        operation().registerPlugin(NetworkTransmissionOperationPlugin.class);
        operation().registerPlugin(MediaControlOperationPlugin.class);
        operation().registerPlugin(AirplaneModeOperationPlugin.class);
        operation().registerPlugin(SendSmsOperationPlugin.class);
        operation().registerPlugin(SendNotificationOperationPlugin.class);
        operation().registerPlugin(AlarmOperationPlugin.class);
        operation().registerPlugin(EventControlOperationPlugin.class);
        operation().registerPlugin(VolumeOperationPlugin.class);
        operation().registerPlugin(LaunchAppOperationPlugin.class);
        //TODO: write more plugins
    }

    private static final PluginRegistry instance = new PluginRegistry();

    public static PluginRegistry getInstance() {
        return instance;
    }

    private PluginRegistry() {}

    public Registry<EventPlugin, EventData> event() {
        return eventPluginRegistry;
    }

    public Registry<OperationPlugin, OperationData> operation() {
        return operationPluginRegistry;
    }

    public PluginLookuper<PluginDef, StorageData> all() {
        return overallRegistry;
    }

    public interface PluginLookuper<T extends PluginDef, T_data extends StorageData> {
        List<T> getEnabledPlugins(@NonNull Context context);
        List<T> getAllPlugins();
        T findPlugin(T_data data);
        T findPlugin(String name);
        T findPlugin(PluginViewFragment view);
    }

    public static class Registry<T extends PluginDef, T_data extends StorageData> implements PluginLookuper<T, T_data> {
        final int type;
        final List<Class<? extends T>> pluginClassList = new ArrayList<>();
        final List<T> pluginList = new ArrayList<>();

        private Registry(int type) {
            this.type = type;
        }

        synchronized void registerPlugin(Class<? extends T> pluginClass) {
            for (Class<? extends T> klass : pluginClassList) {
                if (klass == pluginClass)
                    return;
            }
            pluginClassList.add(pluginClass);
            try {
                T plugin = pluginClass.newInstance();
                pluginList.add(plugin);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public List<Class<? extends T>> getPluginClasses() {
            return pluginClassList;
        }

        public List<T> getEnabledPlugins(@NonNull Context context) {
            List<T> enabledPlugins = new ArrayList<>(pluginList.size());
            SharedPreferences settingsPreference =
                    PreferenceManager.getDefaultSharedPreferences(context);
            for (T plugin : pluginList) {
                if (settingsPreference.getBoolean(CommonHelper.pluginEnabledKey(plugin), true)
                        && plugin.isCompatible(context)) {
                    enabledPlugins.add(plugin);
                }
            }
            return enabledPlugins;
        }

        @Override
        public List<T> getAllPlugins() {
            return pluginList;
        }

        public T findPlugin(T_data data) {
            for (T plugin : getAllPlugins()) {
                if (data.getClass() == plugin.dataFactory().dataClass()) {
                    return plugin;
                }
            }
            throw new IllegalAccessError();
        }

        public T findPlugin(String name) {
            for (T plugin : getAllPlugins()) {
                if (name.equals(plugin.id())) {
                    return plugin;
                }
            }
            throw new IllegalAccessError();
        }

        @Override
        public T findPlugin(PluginViewFragment view) {
            for (T plugin : getAllPlugins()) {
                if (view.getClass().equals(plugin.view().getClass()))
                    return plugin;
            }
            throw new IllegalAccessError();
        }

    }

    public static class OverallRegistry implements PluginLookuper<PluginDef, StorageData> {

        final PluginLookuper<? extends PluginDef, ? extends StorageData>[] lookupers;

        OverallRegistry(PluginLookuper<? extends PluginDef, ? extends StorageData>[] lookupers) {
            this.lookupers = lookupers;
        }

        public List<PluginDef> getEnabledPlugins(@NonNull Context context) {
            List<PluginDef> list = new ArrayList<>();
            for (PluginLookuper<? extends PluginDef, ? extends StorageData> lookuper : lookupers) {
                list.addAll(lookuper.getEnabledPlugins(context));
            }
            return list;
        }

        @Override
        public List<PluginDef> getAllPlugins() {
            List<PluginDef> list = new ArrayList<>();
            for (PluginLookuper<? extends PluginDef, ? extends StorageData> lookuper : lookupers) {
                list.addAll(lookuper.getAllPlugins());
            }
            return list;
        }

        @Override
        public PluginDef findPlugin(StorageData storageData) {
            for (PluginDef plugin : getAllPlugins()) {
                if (storageData.getClass().equals(plugin.dataFactory().dataClass()))
                    return plugin;
            }
            throw new IllegalAccessError();
        }

        @Override
        public PluginDef findPlugin(String name) {
            for (PluginDef plugin : getAllPlugins()) {
                if (name.equals(plugin.id()))
                    return plugin;
            }
            throw new IllegalAccessError();
        }

        @Override
        public PluginDef findPlugin(PluginViewFragment view) {
            for (PluginDef plugin : getAllPlugins()) {
                if (view.getClass().equals(plugin.view().getClass()))
                    return plugin;
            }
            throw new IllegalAccessError();
        }
    }
}
