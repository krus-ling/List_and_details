package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.LaunchFilter
import com.example.listanddetails.domain.model.LaunchListResult
import com.example.listanddetails.domain.repository.LaunchRepository
import java.time.Instant
import java.time.temporal.ChronoUnit

private const val INITIAL_PAGE_LIMIT = 15
private const val NEXT_PAGE_LIMIT = 20
private const val VIDEO_FETCH_LIMIT = 50

class GetLaunchesUseCase(
    private val repository: LaunchRepository,
) {
    suspend operator fun invoke(
        filter: LaunchFilter,
        searchQuery: String? = null,
        offset: Int = 0,
        isInitialPage: Boolean = true
    ): Result<LaunchListResult> {
        val (netGte, netLte) = buildDateRange(filter)
        val combinedSearchQuery = buildSearchQuery(filter, searchQuery)
        val limit = if (filter.hasVideoOnly) {
            VIDEO_FETCH_LIMIT
        } else if (isInitialPage) {
            INITIAL_PAGE_LIMIT
        } else {
            NEXT_PAGE_LIMIT
        }

        return repository.getListOfLaunches(
            search = combinedSearchQuery,
            ordering = filter.sortOrder.apiValue,
            netLte = netLte,
            netGte = netGte,
            statusId = filter.statusId,
            limit = limit,
            offset = offset
        ).map { result ->
            val finalItems = if (filter.hasVideoOnly) {
                result.items.filter { it.hasVideo }
            } else {
                result.items
            }
            result.copy(
                items = finalItems,
                fetchedCount = result.items.size
            )
        }
    }

    private fun buildDateRange(filter: LaunchFilter): Pair<String?, String?> {
        val nowIso = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString()
        return when {
            filter.year != null -> {
                Pair("${filter.year}-01-01T00:00:00Z", "${filter.year}-12-31T23:59:59Z")
            }
            filter.hasVideoOnly -> {
                Pair(null, nowIso)
            }
            else -> {
                Pair(null, null)
            }
        }
    }

    private fun buildSearchQuery(filter: LaunchFilter, freeTextSearchQuery: String?): String? {
        val freeText = freeTextSearchQuery?.takeIf { it.isNotBlank() }
        return listOfNotNull(filter.agency, filter.pad, freeText)
            .joinToString(" ")
            .takeIf { it.isNotBlank() }
    }
}
