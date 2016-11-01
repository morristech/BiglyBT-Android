/**
 * Copyright (C) Azureus Software, Inc, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

package com.vuze.android.remote;

import android.support.annotation.NonNull;

import java.util.*;

import com.vuze.android.remote.adapter.TorrentListAdapter;
import com.vuze.util.MapUtils;

@SuppressWarnings({
	"rawtypes",
	"unchecked"
})
public class RemoteProfile
{
	private static final String ID_UPDATE_INTERVAL_ENABLED = "updateIntervalEnabled";

	private static final String ID_UPDATE_INTERVAL_MOBILE_SEPARATE = "updateIntervalMobileSeparate";

	private static final String ID_UPDATE_INTERVAL_MOBILE_ENABLED = "updateIntervalMobileEnabled";

	private static final String ID_FILTER_BY = "filterBy";

	private static final String ID_SORT_BY = "sortBy";

	private static final String ID_SORT_ORDER = "sortOrder";

	private static final String ID_SORT = "sort";

	private static final String ID_NICK = "nick";

	private static final String ID_PORT = "port";

	private static final String ID_HOST = "host";

	private static final String ID_PROTOCOL = "protocol";

	private static final String ID_LAST_USED = "lastUsed";

	private static final String ID_ID = "id";

	private static final String ID_AC = "ac";

	private static final String ID_USER = "user";

	private static final String ID_UPDATEINTERVAL = "updateInterval";

	private static final String ID_UPDATEINTERVAL_MOBILE = "updateIntervalMobile";

	private static final String ID_SAVE_PATH_HISTORY = "savePathHistory";

	/** Map of Key = Hash; Value = AddedOn **/
	private static final String ID_OPEN_OPTION_HASHES = "openOptionHashes";

	private static final String ID_ADD_TORRENT_SILENTLY = "showTorrentOpenOptions";

	private static final String ID_ADD_POSITION_LAST = "addPositionLast";

	private static final String ID_ADD_STATE_QUEUED = "addStateQueued";

	private static final String ID_DELETE_REMOVES_DATA = "deleteRemovesData";

	private static final String ID_SMALL_LISTS = "useSmallLists";

	private static final String ID_LAST_BINDING_INFO = "lastBindingInfo";

	private static final String ID_FILTER_TIMERANGE = "FilterTimeRange";

	private static final String ID_FILTER_SIZERANGE = "FilterSizeRange";

	private static final String ID_FILTER_NUMBER = "FilterNumber";

	private static final boolean DEFAULT_ADD_POSITION_LAST = true;

	private static final boolean DEFAULT_ADD_STATE_QUEUED = true;

	private static final boolean DEFAULT_ADD_TORRENTS_SILENTLY = false;

	private static final boolean DEFAULT_DELETE_REMOVES_DATA = true;

	private static final boolean DEFAULT_SMALL_LISTS = false;

	private static final long DEFAULT_FILTER_BY = TorrentListAdapter.FILTERBY_ALL;

	public static final int TYPE_LOOKUP = 1;

	public static final int TYPE_NORMAL = 2;

	public static final int TYPE_CORE = 3;

	private Map<String, Object> mapRemote;

	private int remoteType;

	public RemoteProfile(int remoteType) {
		mapRemote = new HashMap<>();
		this.remoteType = remoteType;
		mapRemote.put(ID_ID,
				Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE)));
	}

	public RemoteProfile(Map mapRemote) {
		if (mapRemote == null) {
			mapRemote = new HashMap<>();
		}
		this.mapRemote = mapRemote;
		remoteType = getHost().length() > 0 ? TYPE_NORMAL : TYPE_LOOKUP;
		if (remoteType == TYPE_NORMAL && isLocalHost()
				&& VuzeRemoteApp.isCoreAllowed() && getPort() == 9092) {
			remoteType = TYPE_CORE;
		}
	}

	public RemoteProfile(String user, String ac) {
		mapRemote = new HashMap<>();
		mapRemote.put(ID_USER, user);
		mapRemote.put(ID_AC, ac);
		mapRemote.put(ID_ID, ac);
		remoteType = TYPE_LOOKUP;
	}

	public String getID() {
		return MapUtils.getMapString(mapRemote, ID_ID, getAC());
	}

	public String getAC() {
		return MapUtils.getMapString(mapRemote, ID_AC, "");
	}

	public void setAC(String ac) {
		mapRemote.put(ID_AC, ac);
	}

	public String getUser() {
		String user = (String) mapRemote.get(ID_USER);
		return user;
	}

	public @NonNull	String getNick() {
		String nick = MapUtils.getMapString(mapRemote, ID_NICK, null);
		String ac = getAC();
		if (nick == null || nick.equals(ac)) {
			if (getRemoteType() == TYPE_LOOKUP) {
				if (ac.length() > 1) {
					nick = "Remote " + getAC().substring(0, 2);
				} else {
					nick = "Remote";
				}
			} else {
				nick = MapUtils.getMapString(mapRemote, ID_HOST, "Remote");
			}
		}
		return nick;
	}

	public long getLastUsedOn() {
		return MapUtils.getMapLong(mapRemote, ID_LAST_USED, 0);
	}

	public void setLastUsedOn(long t) {
		mapRemote.put(ID_LAST_USED, t);
	}

	public Map<String, Object> getAsMap(boolean forSaving) {
		if (forSaving && remoteType == TYPE_LOOKUP) {
			Map<String, Object> map = new HashMap<>(mapRemote);
			map.remove(ID_HOST);
			map.remove(ID_PORT);
			return map;
		}
		return mapRemote;
	}

	public String getHost() {
		return MapUtils.getMapString(mapRemote, ID_HOST, "").trim();
	}

	public int getPort() {
		return MapUtils.getMapInt(mapRemote, ID_PORT, 9091);
	}

	public void setNick(String nick) {
		mapRemote.put(ID_NICK, nick);
	}

	public void setPort(int port) {
		mapRemote.put(ID_PORT, port);
	}

	public void setHost(String host) {
		mapRemote.put(ID_HOST, host);
	}

	public void setProtocol(String protocol) {
		mapRemote.put(ID_PROTOCOL, protocol);
	}

	public String getProtocol() {
		return MapUtils.getMapString(mapRemote, ID_PROTOCOL, "http").trim();
	}

	public boolean isLocalHost() {
		return "localhost".equals(getHost());
	}

	public int getRemoteType() {
		return remoteType;
	}

	public String[] getSortBy(String id, String def) {
		Map mapSort = MapUtils.getMapMap(mapRemote, ID_SORT + id, null);
		if (mapSort != null) {
			List mapList = MapUtils.getMapList(mapSort, ID_SORT_BY + id, null);
			if (mapList != null) {
				return (String[]) mapList.toArray(new String[mapList.size()]);
			}
		}
		return new String[] {
			def
		};
	}

	public Boolean[] getSortOrderAsc(String id, boolean defaultAscending) {
		Map mapSort = MapUtils.getMapMap(mapRemote, ID_SORT + id, null);
		if (mapSort != null) {
			List mapList = MapUtils.getMapList(mapSort, ID_SORT_ORDER + id, null);
			if (mapList != null) {
				return (Boolean[]) mapList.toArray(new Boolean[mapList.size()]);
			}
		}
		return new Boolean[] {
			defaultAscending
		};
	}

	public void setSortBy(String id, String[] sortBy, Boolean[] sortOrderAsc) {
		Map mapSort = MapUtils.getMapMap(mapRemote, ID_SORT + id, null);
		if (mapSort == null) {
			mapSort = new HashMap();
			mapRemote.put(ID_SORT + id, mapSort);
		}
		mapSort.put(ID_SORT_BY + id, sortBy);
		mapSort.put(ID_SORT_ORDER + id, sortOrderAsc);
	}

	public long getFilterBy() {
		return MapUtils.getMapLong(mapRemote, ID_FILTER_BY, DEFAULT_FILTER_BY);
	}

	public void setFilterBy(long filterBy) {
		mapRemote.put(ID_FILTER_BY, filterBy);
	}

	public boolean isUpdateIntervalEnabled() {
		return MapUtils.getMapBoolean(mapRemote, ID_UPDATE_INTERVAL_ENABLED, true);
	}

	public void setUpdateIntervalEnabled(boolean enabled) {
		mapRemote.put(ID_UPDATE_INTERVAL_ENABLED, enabled);
	}

	public boolean isUpdateIntervalMobileSeparate() {
		return MapUtils.getMapBoolean(mapRemote, ID_UPDATE_INTERVAL_MOBILE_SEPARATE,
				false);
	}

	public void setUpdateIntervalEnabledSeparate(boolean separate) {
		mapRemote.put(ID_UPDATE_INTERVAL_MOBILE_SEPARATE, separate);
	}

	public boolean isUpdateIntervalMobileEnabled() {
		return MapUtils.getMapBoolean(mapRemote, ID_UPDATE_INTERVAL_MOBILE_ENABLED,
				true);
	}

	public void setUpdateIntervalMobileEnabled(boolean enabled) {
		mapRemote.put(ID_UPDATE_INTERVAL_MOBILE_ENABLED, enabled);
	}

	/**
	 * @return current update interval based on network connection. 
	 * 0 for manual refresh.
	 * < 0 for refresh impossible (not online)
	 */
	public long calcUpdateInterval() {
		if (isLocalHost()) {
			if (isUpdateIntervalEnabled()) {
				return getUpdateInterval();
			}
			return 0;
		}
		NetworkState networkState = VuzeRemoteApp.getNetworkState();
		if (isUpdateIntervalMobileSeparate() && networkState.isOnlineMobile()) {
			if (isUpdateIntervalMobileEnabled()) {
				return getUpdateIntervalMobile();
			}
			return 0;
		} else if (networkState.isOnline()) {
			if (isUpdateIntervalEnabled()) {
				return getUpdateInterval();
			}
			return 0;
		}
		return -1;
	}

	public long getUpdateInterval() {
		return MapUtils.getMapInt(mapRemote, ID_UPDATEINTERVAL, 30);
	}

	public void setUpdateInterval(long interval_secs) {
		mapRemote.put(ID_UPDATEINTERVAL, interval_secs);
	}

	public long getUpdateIntervalMobile() {
		return MapUtils.getMapInt(mapRemote, ID_UPDATEINTERVAL_MOBILE, 30);
	}

	public void setUpdateIntervalMobile(long interval) {
		mapRemote.put(ID_UPDATEINTERVAL_MOBILE, interval);
	}

	public List<String> getSavePathHistory() {
		return MapUtils.getMapList(mapRemote, ID_SAVE_PATH_HISTORY,
				new ArrayList<String>());
	}

	public void setSavePathHistory(List<String> history) {
		mapRemote.put(ID_SAVE_PATH_HISTORY, history);
	}

	public void setUser(String user) {
		mapRemote.put(ID_USER, user);
	}

	public void addOpenOptionsWaiter(String hashString) {
		Map mapOpenOptionHashes = MapUtils.getMapMap(mapRemote,
				ID_OPEN_OPTION_HASHES, null);
		if (mapOpenOptionHashes == null) {
			mapOpenOptionHashes = new HashMap<>();
			mapRemote.put(ID_OPEN_OPTION_HASHES, mapOpenOptionHashes);
		}
		mapOpenOptionHashes.put(hashString, System.currentTimeMillis());
	}

	public void removeOpenOptionsWaiter(String hashString) {
		Map mapOpenOptionHashes = MapUtils.getMapMap(mapRemote,
				ID_OPEN_OPTION_HASHES, null);
		if (mapOpenOptionHashes == null) {
			return;
		}
		mapOpenOptionHashes.remove(hashString);
	}

	public List<String> getOpenOptionsWaiterList() {
		Map mapOpenOptionHashes = MapUtils.getMapMap(mapRemote,
				ID_OPEN_OPTION_HASHES, null);
		if (mapOpenOptionHashes == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(mapOpenOptionHashes.keySet());
	}

	public void cleanupOpenOptionsWaiterList() {
		Map<String, Long> mapOpenOptionHashes = MapUtils.getMapMap(mapRemote,
				ID_OPEN_OPTION_HASHES, null);
		if (mapOpenOptionHashes == null) {
			return;
		}
		long tooOld = System.currentTimeMillis() - (1000L * 3600 * 2); // 2 hours

		for (Iterator<Long> it = mapOpenOptionHashes.values().iterator(); it.hasNext();) {
			Long since = it.next();
			if (since < tooOld) {
				it.remove();
			}
		}
	}

	public boolean isAddTorrentSilently() {
		return MapUtils.getMapBoolean(mapRemote, ID_ADD_TORRENT_SILENTLY,
				DEFAULT_ADD_TORRENTS_SILENTLY);
	}

	public void setAddTorrentSilently(boolean silent) {
		if (silent == DEFAULT_ADD_TORRENTS_SILENTLY) {
			mapRemote.remove(ID_ADD_TORRENT_SILENTLY);
		} else {
			mapRemote.put(ID_ADD_TORRENT_SILENTLY, silent);
		}
	}

	public boolean isAddPositionLast() {
		return MapUtils.getMapBoolean(mapRemote, ID_ADD_POSITION_LAST,
				DEFAULT_ADD_POSITION_LAST);
	}

	public void setAddPositionLast(boolean last) {
		if (last == DEFAULT_ADD_POSITION_LAST) {
			mapRemote.remove(ID_ADD_POSITION_LAST);
		} else {
			mapRemote.put(ID_ADD_POSITION_LAST, last);
		}
	}

	public boolean isAddStateQueued() {
		return MapUtils.getMapBoolean(mapRemote, ID_ADD_STATE_QUEUED,
				DEFAULT_ADD_STATE_QUEUED);
	}

	public void setAddStateQueued(boolean queued) {
		if (queued == DEFAULT_ADD_STATE_QUEUED) {
			mapRemote.remove(ID_ADD_STATE_QUEUED);
		} else {
			mapRemote.put(ID_ADD_STATE_QUEUED, queued);
		}
	}

	public boolean isDeleteRemovesData() {
		return MapUtils.getMapBoolean(mapRemote, ID_DELETE_REMOVES_DATA,
				DEFAULT_DELETE_REMOVES_DATA);
	}

	public void setDeleteRemovesData(boolean removesData) {
		if (removesData == DEFAULT_DELETE_REMOVES_DATA) {
			mapRemote.remove(ID_DELETE_REMOVES_DATA);
		} else {
			mapRemote.put(ID_DELETE_REMOVES_DATA, removesData);
		}
	}

	public boolean useSmallLists() {
		return MapUtils.getMapBoolean(mapRemote, ID_SMALL_LISTS,
				DEFAULT_SMALL_LISTS);
	}

	public void setUseSmallLists(boolean smallLists) {
		if (smallLists == DEFAULT_SMALL_LISTS) {
			mapRemote.remove(ID_SMALL_LISTS);
		} else {
			mapRemote.put(ID_SMALL_LISTS, smallLists);
		}
	}

	public void setLastBindingInfo(Map bindingInfo) {
		if (bindingInfo == null) {
			mapRemote.remove(ID_LAST_BINDING_INFO);
		} else {
			mapRemote.put(ID_LAST_BINDING_INFO, bindingInfo);
		}
	}

	public Map getLastBindingInfo() {
		return MapUtils.getMapMap(mapRemote, ID_LAST_BINDING_INFO, null);
	}

	public String getRemoteTypeName() {
		switch (remoteType) {
			case TYPE_LOOKUP:
				return "Vuze";
			case TYPE_CORE:
				return "Core";
			case TYPE_NORMAL:
				if (isLocalHost()) {
					return "Local";
				}
				return "Transmission";
		}
		return "Unknown";
	}

//	public long[] getFilter_TimeRange(String id) {
//		Object o = mapRemote.get(ID_FILTER_TIMERANGE + id);
//		if (o instanceof long[]) {
//			return (long[]) o;
//		}
//		return null;
//	}
//
//	public void setFilter_TimeRange(String id, long[] range) {
//		if (range == null || (range[0] <= 0 && range[1] <= 0)) {
//			mapRemote.remove(ID_FILTER_TIMERANGE + id);
//		} else {
//			mapRemote.put(ID_FILTER_TIMERANGE + id, range);
//		}
//	}
//
//	public long[] getFilter_SizeRange(String id) {
//		Object o = mapRemote.get(ID_FILTER_SIZERANGE + id);
//		if (o instanceof long[]) {
//			return (long[]) o;
//		}
//		return null;
//	}
//
//	public void setFilter_SizeRange(String id, long[] range) {
//		if (range == null || (range[0] <= 0 && range[1] <= 0)) {
//			mapRemote.remove(ID_FILTER_SIZERANGE + id);
//		} else {
//			mapRemote.put(ID_FILTER_SIZERANGE + id, range);
//		}
//	}
//
//	public int getFilter_Number(String id, int def) {
//		Object o = mapRemote.get(ID_FILTER_NUMBER + id);
//		if (o instanceof Number) {
//			return ((Number) o).intValue();
//		}
//		return def;
//	}
//
//	public void setFilter_Number(String id, Integer val) {
//		if (val == null) {
//			mapRemote.remove(ID_FILTER_NUMBER + id);
//		} else {
//			mapRemote.put(ID_FILTER_NUMBER + id, val);
//		}
//	}
}
