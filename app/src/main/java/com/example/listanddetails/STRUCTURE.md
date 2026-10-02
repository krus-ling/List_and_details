```
com.example.listanddetails/
│
├── data/                                 # Работа с данными, сетью и мапперами
│   ├── dto/                              # Сырые данные с бэка (json)
│   │   ├──LaunchDetailDto.kt             # Детали одного запуска по id + вспомогательные dto RocketDto, MissionDto, PadDto и т.д.
│   │   ├──LaunchListItemDto.kt           # Один элемент списка (один запуск) в сокращенном формате
│   │   ├──LaunchListResponseDto.kt       # Ответ бэка: count, next + results - наш список
│   │   └── LaunchStatusDto.kt            # Статус миссии
│   ├── network/                          # Работа с сетью
│   │   └── LaunchApiService.kt           # Retrofit интерфейс с эндпоинтами
│   ├── mapper/                           # Преобразование "грязных" DTO в чистые модели
│   │   └── LaunchMapper.kt               # extension-функции: LaunchListItemDto.toDomain() и LaunchDetailDto.toDomain()
│   └── repository/                       # Реализация репозиториев
│       └── LaunchRepositoryImpl.kt       # Реализует domain/repository/LaunchRepository, маппит и отдает в domain
│
├── domain/                               # Бизнес-логика
│   ├── model/                            # Модели (сущности) приложения (только те поля, что нужны для UI)
│   │   ├── LaunchItem.kt                 # Модель карточки запуска
│   │   └── LaunchDetail.kt               # Модель детального экрана запуска
│   └── repository/                       # Интерфейсы (контракты)
│       └── LaunchRepository.kt           # fun getListOfLaunches: Result<List<LaunchItem>> и getLaunchForId: Result<LaunchDetail>
│
│           ДАЛЕЕ ПОКА НЕ РЕДАКТИРОВАЛ
│
│
├── ui/                                   # Всё, что связано с UI (Jetpack Compose)
│   ├── list/                             # Экран списка
│   │   ├── LaunchListScreen.kt
│   │   ├── LaunchListViewModel.kt
│   │   ├── LaunchListUiState.kt          # Loading, Success(launches), Error
│   │   └── components/                   # Карточка запуска, плашка статуса, поле поиска
│   ├── details/                          # Экран детальной информации
│   │   ├── LaunchDetailScreen.kt
│   │   ├── LaunchDetailViewModel.kt
│   │   ├── LaunchDetailUiState.kt
│   │   └── components/                   # Таймер T-minus, шапка с ракетой, характеристики
│   └── navigation/                       # Маршруты и навигационный граф
│       └── Screen.kt                     # List / Details(id)
│
├── di/                                   # Внедрение зависимостей Koin
│   └── NetworkModule.kt / AppModule.kt   # Сборка OkHttpClient, Retrofit, Repository, ViewModel
│
└── ui.theme/                             # Цвета, шрифты, космическая темная тема
```