# Структура проекта List and Details

```text
com.example.listanddetails/
│
├── App.kt                                # Класс Application, инициализирует Koin DI
├── MainActivity.kt                       # Главная Activity, содержит NavDisplay (Navigation 3) и анимации
│
├── data/                                 # Слой данных (Data Layer)
│   ├── dto/                              # DTO (Data Transfer Objects) для Retrofit/kotlinx.serialization
│   │   ├── AgencyDto.kt                  # Модель космического агентства (count, results)
│   │   ├── AstronautDto.kt               # Модель космонавта (count, results)
│   │   ├── LaunchDetailDto.kt            # Полная модель деталей запуска
│   │   ├── LaunchListItemDto.kt          # Сокращённая модель элемента списка
│   │   ├── LaunchListResponseDto.kt      # Ответ сервера: count, next, results
│   │   └── LaunchStatusDto.kt            # Статус миссии (abbrev, name)
│   ├── network/                          # Сетевой слой
│   │   └── LaunchApiService.kt           # Retrofit-интерфейс (LL2 API endpoints: launch, agencies, astronaut)
│   ├── mapper/                           # Преобразование DTO -> Domain
│   │   └── LaunchMapper.kt               # Extension-функции маппинга для запусков, агентств и космонавтов
│   └── repository/                       # Реализация репозиториев
│       ├── AgencyRepositoryImpl.kt       # Реализация AgencyRepository с выполнением в Dispatchers.IO
│       ├── AstronautRepositoryImpl.kt    # Реализация AstronautRepository с выполнением в Dispatchers.IO
│       └── LaunchRepositoryImpl.kt       # Реализация LaunchRepository с выполнением в Dispatchers.IO
│
├── domain/                               # Доменная бизнес-логика (Domain Layer)
│   ├── model/                            # Чистые доменные модели
│   │   ├── AgencyItem.kt                 # Модель космического агентства и AgencyListResult
│   │   ├── AstronautItem.kt              # Модель космонавта и AstronautListResult
│   │   ├── LaunchItem.kt                 # Модель элемента карточки запуска
│   │   ├── LaunchDetail.kt               # Подробная модель детализированного запуска
│   │   └── LaunchListResult.kt           # Результат запроса списка (totalCount + items)
│   └── repository/                       # Интерфейсы репозиториев
│       ├── AgencyRepository.kt           # Контракт репозитория агентств (getAgencies)
│       ├── AstronautRepository.kt        # Контракт репозитория космонавтов (getAstronauts)
│       └── LaunchRepository.kt           # Контракт репозитория запусков (getListOfLaunches, getLaunchForId)
│
├── ui/                                   # Интерфейс пользователя (Presentation Layer / Jetpack Compose)
│   ├── list/                             # Раздел списка запусков
│   │   ├── LaunchListScreen.kt           # Composable экрана списка запусков
│   │   ├── LaunchListViewModel.kt        # ViewModel: пагинация, фильтрация, таймер 429
│   │   ├── LaunchListUiState.kt          # UiState, LaunchFilter, DateSortOrder, LaunchFilterOptions
│   │   └── components/                   # Компоненты экрана списка
│   │
│   ├── agencies/                         # Раздел космических агентств
│   │   ├── AgencyListScreen.kt           # Composable экрана списка агентств
│   │   ├── AgencyListViewModel.kt        # ViewModel подгрузки и пагинации агентств
│   │   ├── AgencyListUiState.kt          # UiState списка агентств
│   │   └── components/                   # Компоненты экрана агентств
│   │       └── AgencyCard.kt             # Карточка космического агентства
│   │
│   ├── astronauts/                       # Раздел космонавтов и астронавтов
│   │   ├── AstronautListScreen.kt        # Composable экрана списка космонавтов
│   │   ├── AstronautListViewModel.kt     # ViewModel подгрузки и пагинации космонавтов
│   │   ├── AstronautListUiState.kt       # UiState списка космонавтов
│   │   └── components/                   # Компоненты экрана космонавтов
│   │       └── AstronautCard.kt          # Карточка космонавта
│   │
│   ├── details/                          # Экран детальной информации
│   │   ├── LaunchDetailScreen.kt         # Главный Composable экрана деталей
│   │   ├── LaunchDetailViewModel.kt      # ViewModel загрузки деталей
│   │   ├── LaunchDetailUiState.kt        # UiState деталей запуска
│   │   └── components/                   # Выделенные компоненты экрана деталей (2026 стандарты)
│   │
│   ├── navigation/                       # Навигация (Navigation 3)
│   │   ├── MainTabScreen.kt              # Нижняя панель навигации (Запуски, Агентства, Космонавты)
│   │   ├── AppNavHost.kt                 # Навигационный граф приложения
│   │   └── Route.kt                      # Иерархия маршрутов (@Serializable Route)
│   │
│   └── theme/                            # Темы и стили
│       ├── Color.kt                      # Цветовая палитра
│       ├── Theme.kt                      # Тема приложения ListAndDetailsTheme
│       └── Type.kt                       # Типографика
│
└── di/                                   # Внедрение зависимостей (Koin Dependency Injection)
    ├── NetworkModule.kt                  # Модуль OkHttpClient, Retrofit, Json, LaunchApiService
    ├── RepositoryModule.kt               # Модуль связывания LaunchRepositoryImpl, AgencyRepositoryImpl, AstronautRepositoryImpl
    └── PresentationModule.kt             # Модуль ViewModels (LaunchListViewModel, AgencyListViewModel, AstronautListViewModel, LaunchDetailViewModel)
```

## В файлах `graph.json` и `graph.html` показана структура в графовом представлении

## В `di/NetworkModule.kt` можно поменять в адресе `ll` на `lldev` для тестирования (запросы будут безграничными)
