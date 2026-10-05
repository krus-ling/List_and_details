# Graph Report - dz_4_list_and_details  (2026-10-05)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 517 nodes · 1407 edges · 20 communities (10 shown, 10 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 10 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `b1383ce5`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- LaunchDetailScreen.kt
- LaunchListViewModel.kt
- MainActivity.kt
- AgencyListScreen.kt
- FilterBottomSheet.kt
- LaunchListSkeleton.kt
- LaunchDetail
- AstronautItem
- LaunchApiService
- LaunchMapper.kt
- LaunchDetailDto
- NetworkModule.kt
- TranslationManager
- TMinusCountdownBadge.kt
- ExampleInstrumentedTest.kt
- ExampleUnitTest.kt

## God Nodes (most connected - your core abstractions)
1. `LaunchDetail` - 20 edges
2. `LaunchListViewModel` - 18 edges
3. `LaunchListContent()` - 14 edges
4. `LaunchDetailContent()` - 13 edges
5. `LaunchDetailDto` - 12 edges
6. `LaunchFilter` - 12 edges
7. `LaunchApiService` - 12 edges
8. `TranslationManager` - 11 edges
9. `LaunchItem` - 11 edges
10. `AstronautItem` - 11 edges

## Surprising Connections (you probably didn't know these)
- `AgencyListViewModel` --calls--> `AgencyListUiState`  [INFERRED]
  app/src/main/java/com/example/listanddetails/ui/agencies/AgencyListViewModel.kt → app/src/main/java/com/example/listanddetails/ui/agencies/AgencyListUiState.kt
- `AstronautListViewModel` --calls--> `AstronautListUiState`  [INFERRED]
  app/src/main/java/com/example/listanddetails/ui/astronauts/AstronautListViewModel.kt → app/src/main/java/com/example/listanddetails/ui/astronauts/AstronautListUiState.kt
- `LaunchDetailViewModel` --calls--> `LaunchDetailUiState`  [INFERRED]
  app/src/main/java/com/example/listanddetails/ui/details/LaunchDetailViewModel.kt → app/src/main/java/com/example/listanddetails/ui/details/LaunchDetailUiState.kt
- `LaunchDetailHeader()` --calls--> `TMinusCountdownBadge()`  [INFERRED]
  app/src/main/java/com/example/listanddetails/ui/details/components/LaunchDetailHeader.kt → app/src/main/java/com/example/listanddetails/ui/details/components/TMinusCountdownBadge.kt
- `AppNavHost()` --calls--> `MainTabScreen()`  [INFERRED]
  app/src/main/java/com/example/listanddetails/ui/navigation/AppNavHost.kt → app/src/main/java/com/example/listanddetails/ui/navigation/MainTabScreen.kt

## Import Cycles
- None detected.

## Communities (20 total, 10 thin omitted)

### Community 1 - "LaunchListViewModel.kt"
Cohesion: 0.05
Nodes (11): AgencyRepositoryImpl, AstronautRepositoryImpl, LaunchRepositoryImpl, AgencyListResult, LaunchListResult, AgencyRepository, AstronautRepository, LaunchRepository (+3 more)

### Community 2 - "MainActivity.kt"
Cohesion: 0.06
Nodes (9): MainActivity, AppNavHost(), rememberNavBackStack(), NavTransitions, LaunchDetails, LaunchList, MainTab, Route (+1 more)

### Community 3 - "AgencyListScreen.kt"
Cohesion: 0.07
Nodes (11): EmptyLaunchesView(), LaunchesLazyColumn(), LaunchListErrorView(), LaunchListContent(), LaunchListErrorPreview(), LaunchListScreen(), MainTabItem, AGENCIES (+3 more)

### Community 4 - "FilterBottomSheet.kt"
Cohesion: 0.07
Nodes (12): LaunchItem, ActiveFilterChipsRow(), FilterBottomSheet(), FilterCardSection(), LaunchCard(), DateSortOrder, NEWEST_FIRST, OLDEST_FIRST (+4 more)

### Community 5 - "LaunchListSkeleton.kt"
Cohesion: 0.13
Nodes (5): RocketLoadingIndicator(), LaunchCardSkeleton(), LaunchListSkeleton(), LaunchListSkeletonPreview(), rememberShimmerBrush()

### Community 6 - "LaunchDetail"
Cohesion: 0.11
Nodes (9): LaunchDetail, LaunchDetailHeader(), LaunchFailureCard(), LaunchMissionCard(), LaunchPadCard(), LaunchRocketCard(), LaunchDetailContent(), LaunchDetailScreen() (+1 more)

### Community 7 - "AstronautItem"
Cohesion: 0.10
Nodes (9): AstronautItem, AstronautListResult, AstronautListContent(), AstronautListScreen(), AstronautListUiState, AstronautCard(), AstronautDetailDialog(), SearchBarTopBar() (+1 more)

### Community 8 - "LaunchApiService"
Cohesion: 0.16
Nodes (8): AgencyDto, AgencyListResponseDto, AstronautAgencyDto, AstronautDto, AstronautListResponseDto, AstronautStatusDto, LaunchListResponseDto, LaunchApiService

### Community 9 - "LaunchMapper.kt"
Cohesion: 0.13
Nodes (8): formatLaunchDate(), toDomain(), AgencyItem, AgencyListContent(), AgencyListScreen(), AgencyListUiState, AgencyCard(), AgencyDetailDialog()

### Community 10 - "LaunchDetailDto"
Cohesion: 0.27
Nodes (11): LaunchDetailDto, LaunchProviderDto, LocationDto, MissionDto, OrbitDto, PadDto, RocketConfigDto, RocketDto (+3 more)

## Knowledge Gaps
- **5 isolated node(s):** `AGENCIES`, `ASTRONAUTS`, `LAUNCHES`, `NEWEST_FIRST`, `OLDEST_FIRST`
  These have ≤1 connection - possible missing edges. (Counts symbols only; 121 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **10 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `LaunchDetail` connect `LaunchDetail` to `LaunchDetailScreen.kt`, `LaunchMapper.kt`, `LaunchListViewModel.kt`?**
  _High betweenness centrality (0.088) - this node is a cross-community bridge._
- **Why does `TranslationManager` connect `TranslationManager` to `LaunchDetailScreen.kt`, `LaunchMapper.kt`, `LaunchListViewModel.kt`, `AstronautItem`?**
  _High betweenness centrality (0.061) - this node is a cross-community bridge._
- **Why does `AstronautItem` connect `AstronautItem` to `LaunchDetailScreen.kt`, `LaunchMapper.kt`, `AgencyListScreen.kt`?**
  _High betweenness centrality (0.053) - this node is a cross-community bridge._
- **What connects `AGENCIES`, `ASTRONAUTS`, `LAUNCHES` to the rest of the system?**
  _5 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `LaunchDetailScreen.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.09351692484222604 - nodes in this community are weakly interconnected._
- **Should `LaunchListViewModel.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.05029838022165388 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.05639097744360902 - nodes in this community are weakly interconnected._