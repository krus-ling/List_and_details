# Graph Report - dz_4_list_and_details  (2026-10-03)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 341 nodes · 650 edges · 22 communities (6 shown, 16 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 4 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `43299edd`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- LaunchListViewModel.kt
- MainActivity.kt
- LaunchListViewModel
- LaunchDetailDto
- NetworkModule.kt
- LaunchListSkeleton.kt
- FilterBottomSheet.kt
- TranslationManager.kt
- LaunchListErrorView.kt
- LaunchDetailContent
- ExampleInstrumentedTest.kt
- ExampleUnitTest.kt

## God Nodes (most connected - your core abstractions)
1. `LaunchListViewModel` - 17 edges
2. `LaunchListContent()` - 13 edges
3. `LaunchFilter` - 12 edges
4. `LaunchDetailDto` - 12 edges
5. `LaunchDetail` - 10 edges
6. `LaunchRepository` - 10 edges
7. `LaunchDetailViewModel` - 10 edges
8. `LaunchListItemDto` - 10 edges
9. `LaunchItem` - 9 edges
10. `LaunchDetailContent()` - 8 edges

## Surprising Connections (you probably didn't know these)
- `LaunchRepositoryImpl` --references--> `LaunchApiService`  [EXTRACTED]
  app/src/main/java/com/example/listanddetails/data/repository/LaunchRepositoryImpl.kt → app/src/main/java/com/example/listanddetails/data/network/LaunchApiService.kt
- `LaunchListResult` --references--> `LaunchItem`  [EXTRACTED]
  app/src/main/java/com/example/listanddetails/domain/model/LaunchListResult.kt → app/src/main/java/com/example/listanddetails/domain/model/LaunchItem.kt
- `LaunchListViewModel` --references--> `LaunchRepository`  [EXTRACTED]
  app/src/main/java/com/example/listanddetails/ui/list/LaunchListViewModel.kt → app/src/main/java/com/example/listanddetails/domain/repository/LaunchRepository.kt
- `LaunchDetailContent()` --references--> `LaunchDetailUiState`  [EXTRACTED]
  app/src/main/java/com/example/listanddetails/ui/details/LaunchDetailScreen.kt → app/src/main/java/com/example/listanddetails/ui/details/LaunchDetailUiState.kt
- `LaunchDetailViewModel` --calls--> `LaunchDetailUiState`  [INFERRED]
  app/src/main/java/com/example/listanddetails/ui/details/LaunchDetailViewModel.kt → app/src/main/java/com/example/listanddetails/ui/details/LaunchDetailUiState.kt

## Import Cycles
- None detected.

## Communities (22 total, 16 thin omitted)

### Community 0 - "LaunchListViewModel.kt"
Cohesion: 0.07
Nodes (8): formatLaunchDate(), toDomain(), LaunchRepositoryImpl, LaunchDetail, LaunchListResult, LaunchRepository, LaunchDetailUiState, LaunchDetailViewModel

### Community 1 - "MainActivity.kt"
Cohesion: 0.06
Nodes (5): MainActivity, LaunchDetails, LaunchList, Route, ListAndDetailsTheme()

### Community 2 - "LaunchListViewModel"
Cohesion: 0.08
Nodes (14): LaunchItem, ActiveFilterChipsRow(), LaunchCard(), LaunchListTopBar(), LaunchListContent(), LaunchListErrorPreview(), LaunchListScreen(), DateSortOrder (+6 more)

### Community 3 - "LaunchDetailDto"
Cohesion: 0.15
Nodes (13): LaunchDetailDto, LaunchProviderDto, LocationDto, MissionDto, OrbitDto, PadDto, RocketConfigDto, RocketDto (+5 more)

### Community 6 - "LaunchListSkeleton.kt"
Cohesion: 0.14
Nodes (4): LaunchCardSkeleton(), LaunchListSkeleton(), LaunchListSkeletonPreview(), rememberShimmerBrush()

### Community 14 - "LaunchDetailContent"
Cohesion: 0.39
Nodes (5): FullScreenImageViewer(), LaunchDetailContent(), LaunchDetailScreen(), LaunchStatusBadge(), TMinusCountdownBadge()

## Knowledge Gaps
- **2 isolated node(s):** `NEWEST_FIRST`, `OLDEST_FIRST`
  These have ≤1 connection - possible missing edges. (Counts symbols only; 119 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **16 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `LaunchDetailViewModel` connect `LaunchListViewModel.kt` to `TranslationManager.kt`, `NetworkModule.kt`, `LaunchDetailContent`?**
  _High betweenness centrality (0.121) - this node is a cross-community bridge._
- **Why does `LaunchDetail` connect `LaunchListViewModel.kt` to `LaunchDetailScreen.kt`?**
  _High betweenness centrality (0.107) - this node is a cross-community bridge._
- **Why does `LaunchDetailScreen()` connect `LaunchDetailContent` to `LaunchListViewModel.kt`, `MainActivity.kt`, `LaunchDetailScreen.kt`?**
  _High betweenness centrality (0.102) - this node is a cross-community bridge._
- **What connects `NEWEST_FIRST`, `OLDEST_FIRST` to the rest of the system?**
  _2 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `LaunchListViewModel.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06871035940803383 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.0620782726045884 - nodes in this community are weakly interconnected._
- **Should `LaunchListViewModel` be split into smaller, more focused modules?**
  _Cohesion score 0.08392603129445235 - nodes in this community are weakly interconnected._