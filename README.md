# Структура проекта List and Details

```text
com.example.listanddetails/
│
├── App.kt                                # Класс Application, инициализирует Koin DI
├── MainActivity.kt                       # Главная Activity, содержит NavDisplay (Navigation 3) и анимации
│
├── data/                                 # Слой данных (Data Layer)
│   ├── dto/                              # DTO (Data Transfer Objects) для Retrofit/kotlinx.serialization
│   │   ├── LaunchDetailDto.kt            # Полная модель деталей запуска
│   │   ├── LaunchListItemDto.kt          # Сокращённая модель элемента списка
│   │   ├── LaunchListResponseDto.kt      # Ответ сервера: count, next, results
│   │   └── LaunchStatusDto.kt            # Статус миссии (abbrev, name)
│   ├── network/                          # Сетевой слой
│   │   └── LaunchApiService.kt           # Retrofit-интерфейс (LL2 API endpoints)
│   ├── mapper/                           # Преобразование DTO -> Domain
│   │   └── LaunchMapper.kt               # Extension-функции маппинга и форматирования дат
│   └── repository/                       # Реализация репозиториев
│       └── LaunchRepositoryImpl.kt       # Реализация LaunchRepository с выполнением в Dispatchers.IO
│
├── domain/                               # Доменная бизнес-логика (Domain Layer)
│   ├── model/                            # Чистые доменные модели
│   │   ├── LaunchItem.kt                 # Модель элемента карточки запуска
│   │   ├── LaunchDetail.kt               # Подробная модель детализированного запуска
│   │   └── LaunchListResult.kt           # Результат запроса списка (totalCount + items)
│   └── repository/                       # Интерфейсы репозиториев
│       └── LaunchRepository.kt           # Контракт репозитория (getListOfLaunches, getLaunchForId)
│
├── ui/                                   # Интерфейс пользователя (Presentation Layer / Jetpack Compose)
│   ├── list/                             # Экран списка запусков
│   │   ├── LaunchListScreen.kt           # Главный Composable экрана списка
│   │   ├── LaunchListViewModel.kt        # ViewModel: пагинация, фильтрация, таймер 429
│   │   ├── LaunchListUiState.kt          # UiState, LaunchFilter, DateSortOrder, LaunchFilterOptions
│   │   └── components/                   # Компоненты экрана списка
│   │       ├── ActiveFilterChipsRow.kt   # Строка активных чипсов фильтров
│   │       ├── EmptyLaunchesView.kt      # Компонент пустой выдачи списков
│   │       ├── FilterBottomSheet.kt      # Нижняя шторка фильтров с FlowRow
│   │       ├── LaunchCard.kt             # Карточка одного запуска со статусом
│   │       ├── LaunchesLazyColumn.kt     # Сквозной пагинируемый список с подгрузкой
│   │       ├── LaunchListErrorView.kt    # Экран ошибки с таймером обратного отсчета
│   │       ├── LaunchListPreviewData.kt # Моковые данные для Compose Previews
│   │       ├── LaunchListSkeleton.kt     # Скелетон загрузки с эффектом Shimmer
│   │       └── LaunchListTopBar.kt       # Верхняя панель со счетчиком фильтров
│   │
│   ├── details/                          # Экран детальной информации
│   │   ├── LaunchDetailScreen.kt         # Главный Composable экрана деталей
│   │   ├── LaunchDetailViewModel.kt      # ViewModel загрузки деталей
│   │   ├── LaunchDetailUiState.kt        # UiState деталей запуска
│   │   └── components/                   # Компоненты экрана деталей
│   │       ├── FullScreenImageViewer.kt  # Полноэкранный просмотрщик фото с Pinch-to-Zoom
│   │       ├── LaunchDetailHeader.kt     # Параллакс-хедер с фото, градиентом и заголовком
│   │       ├── LaunchFailureCard.kt      # Карточка с описанием причин сбоя
│   │       ├── LaunchMissionCard.kt      # Карточка миссии с информацией и переводом
│   │       ├── LaunchPadCard.kt          # Карточка стартовой площадки и локации
│   │       ├── LaunchRocketCard.kt       # Карточка ракеты с кликабельной фото
│   │       ├── LaunchStatusBadge.kt      # Чипс статуса запуска
│   │       └── TMinusCountdownBadge.kt   # Живой обратный отсчет T-Minus
│   │
│   ├── navigation/                       # Навигация (Navigation 3)
│   │   └── Route.kt                      # Иерархия маршрутов (@Serializable Route)
│   │
│   └── theme/                            # Темы и стили
│       ├── Color.kt                      # Цветовая палитра
│       ├── Theme.kt                      # Тема приложения ListAndDetailsTheme
│       └── Type.kt                       # Типографика
│
└── di/                                   # Внедрение зависимостей (Koin Dependency Injection)
    ├── NetworkModule.kt                  # Модуль OkHttpClient, Retrofit, Json, LaunchApiService
    ├── RepositoryModule.kt               # Модуль связывания LaunchRepositoryImpl
    └── PresentationModule.kt             # Модуль ViewModels (LaunchListViewModel, LaunchDetailViewModel)
```

## В файлах `graph.json` и `graph.html` показана структура в графовом представлении

## В `di/NetworkModule.kt` можно поменять в адресе `ll` на `lldev` для тестирования (запросы будут безграничными)
