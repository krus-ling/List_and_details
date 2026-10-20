package com.example.listanddetails.data.cache

import com.example.listanddetails.domain.model.AgencyItem
import com.example.listanddetails.domain.model.AstronautItem
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem
import java.util.concurrent.ConcurrentHashMap

class LaunchesMemoryCache {
    private val launchItemsMap = ConcurrentHashMap<String, LaunchItem>()
    private val launchDetailsMap = ConcurrentHashMap<String, LaunchDetail>()
    private val agencyItemsMap = ConcurrentHashMap<Int, AgencyItem>()
    @Volatile
    private var cachedAgencies: List<AgencyItem> = emptyList()
    private val astronautItemsMap = ConcurrentHashMap<Int, AstronautItem>()
    @Volatile
    private var cachedAstronauts: List<AstronautItem> = emptyList()

    // Launches
    fun saveLaunches(items: List<LaunchItem>) {
        items.forEach { item ->
            launchItemsMap[item.id] = item
        }
    }

    fun getLaunchItem(id: String): LaunchItem? = launchItemsMap[id]

    fun saveLaunchDetail(detail: LaunchDetail) {
        launchDetailsMap[detail.id] = detail
    }

    fun getLaunchDetail(id: String): LaunchDetail? = launchDetailsMap[id]

    // Agencies
    fun saveAgencies(items: List<AgencyItem>) {
        cachedAgencies = items
        items.forEach { item ->
            agencyItemsMap[item.id] = item
        }
    }

    fun getAgencies(): List<AgencyItem> = cachedAgencies

    fun getAgencyItem(id: Int): AgencyItem? = agencyItemsMap[id]

    // Astronauts
    fun saveAstronauts(items: List<AstronautItem>) {
        cachedAstronauts = items
        items.forEach { item ->
            astronautItemsMap[item.id] = item
        }
    }

    fun getAstronauts(): List<AstronautItem> = cachedAstronauts

    fun getAstronautItem(id: Int): AstronautItem? = astronautItemsMap[id]

    fun clear() {
        launchItemsMap.clear()
        launchDetailsMap.clear()
        agencyItemsMap.clear()
        cachedAgencies = emptyList()
        astronautItemsMap.clear()
        cachedAstronauts = emptyList()
    }
}
