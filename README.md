# List and Details App

Современное Android-приложение для отслеживания космических запусков, агентств и космонавтов на базе **The Space Devs LL2 API**. Построено на принципах **Clean Architecture**, **Unidirectional Data Flow (UDF)** и стандартах **MAD 2026**.

---

## 🛠 Технологический стек

* **Язык:** Kotlin 2.x
* **UI:** Jetpack Compose (Material 3) + Navigation 3 (`androidx.navigation3`)
* **Архитектура:** Clean Architecture (Data, Domain, Presentation) + MVVM / UDF
* **Асинхронность:** Kotlin Coroutines + StateFlow / Flow + Structured Concurrency (`runCatchingCancellable`)
* **Внедрение зависимостей:** Koin DI (`koin-android`, `koin-androidx-compose`)
* **Сеть & Кэш:** Retrofit 2 + OkHttpClient 5 (Offline Cache Interceptor) + Kotlinx Serialization
* **Перевод:** ML Kit On-Device Translation (Английский -> Русский)
* **Загрузка изображений:** Coil 3

---

## 📁 Структура проекта

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
│   │   ├── ConnectivityInterceptor.kt    # Перехватчик отсутствия сети и офлайн-кэширования OkHttp
│   │   └── LaunchApiService.kt           # Retrofit-интерфейс (LL2 API endpoints: launch, agencies, astronaut)
│   │
│   ├── mapper/                           # Преобразование DTO -> Domain
│   │   ├── ErrorMapper.kt                # Преобразование сетевых и HTTP-исключений в AppError
│   │   └── LaunchMapper.kt               # Extension-функции маппинга запусков, агентств и космонавтов
│   │
│   ├── mlkit/                            # Офлайн-перевод текста
│   │   └── MlKitTextTranslator.kt        # Реализация TextTranslator через ML Kit Translate (thread/cancellation safe)
│   │
│   ├── repository/                       # Реализации репозиториев (Dispatchers.IO)
│   │   ├── AgencyRepositoryImpl.kt       # Реализация AgencyRepository
│   │   ├── AstronautRepositoryImpl.kt    # Реализация AstronautRepository
│   │   └── LaunchRepositoryImpl.kt       # Реализация LaunchRepository
│   │
│   └── util/                             # Сетевые и корутинные утилиты
│       └── RunCatchingCancellable.kt     # Безопасный runCatching, пробрасывающий CancellationException
│
├── domain/                               # Доменная бизнес-логика (Domain Layer)
│   ├── model/                            # Чистые доменные модели
│   │   ├── AgencyItem.kt                 # Модель космического агентства
│   │   ├── AppError.kt                   # Sealed-интерфейс доменных ошибок (RateLimit, NoInternet, Timeout и т.д.)
│   │   ├── AstronautItem.kt              # Модель космонавта
│   │   ├── LaunchDetail.kt               # Детализированная модель запуска
│   │   ├── LaunchFilter.kt               # Фильтры списка запусков (сортировка, статус, видео)
│   │   ├── LaunchItem.kt                 # Карточка запуска
│   │   └── LaunchListResult.kt           # Результат запроса списка
│   │
│   ├── repository/                       # Контракты репозиториев
│   │   ├── AgencyRepository.kt           # Интерфейс репозитория агентств
│   │   ├── AstronautRepository.kt        # Интерфейс репозитория космонавтов
│   │   ├── LaunchRepository.kt           # Интерфейс репозитория запусков
│   │   └── TextTranslator.kt             # Контракт машинного перевода
│   │
│   └── usecase/                          # Сценарии использования (Use Cases)
│       ├── GetAgenciesUseCase.kt         # Получение списка космических агентств
│       ├── GetAstronautsUseCase.kt       # Получение списка космонавтов
│       ├── GetLaunchDetailUseCase.kt     # Получение деталей запуска
│       └── GetLaunchesUseCase.kt         # Получение списка запусков с фильтрацией
│
├── ui/                                   # Интерфейс пользователя (Presentation Layer / Jetpack Compose)
│   ├── list/                             # Экран списка запусков
│   │   ├── LaunchListScreen.kt           # Composable экрана запусков
│   │   ├── LaunchListViewModel.kt        # ViewModel: пагинация, дебаунс поиска, таймер 429
│   │   ├── LaunchListUiState.kt          # UI State экрана запусков
│   │   └── components/                   # Компоненты экрана запусков
│   │
│   ├── agencies/                         # Экран космических агентств
│   │   ├── AgencyListScreen.kt           # Composable экрана агентств
│   │   ├── AgencyListViewModel.kt        # ViewModel списка агентств
│   │   ├── AgencyListUiState.kt          # UI State списка агентств
│   │   └── components/                   # Компоненты экрана агентств
│   │
│   ├── astronauts/                       # Экран космонавтов
│   │   ├── AstronautListScreen.kt        # Composable экрана космонавтов
│   │   ├── AstronautListViewModel.kt     # ViewModel списка космонавтов
│   │   ├── AstronautListUiState.kt       # UI State списка космонавтов
│   │   └── components/                   # Компоненты экрана космонавтов
│   │
│   ├── details/                          # Экран деталей запуска
│   │   ├── LaunchDetailScreen.kt         # Главный Composable экрана деталей
│   │   ├── LaunchDetailViewModel.kt      # ViewModel деталей запуска и перевода
│   │   ├── LaunchDetailUiState.kt        # UI State деталей запуска
│   │   └── components/                   # Выделенные карточки деталей (миссия, ракета, площадка)
│   │
│   ├── components/                       # Общие переиспользуемые UI-компоненты
│   │   ├── AppErrorBanner.kt             # Баннер ошибки
│   │   ├── AppErrorHandler.kt            # Обработчик ошибок для ViewModel
│   │   ├── AppErrorView.kt               # Полноэкранный view ошибки
│   │   ├── RocketLoadingIndicator.kt     # Анимированный индикатор загрузки с орбитой
│   │   └── SearchBarTopBar.kt            # Поисковая панель с анимированным переключением
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
    ├── RepositoryModule.kt               # Модуль связывания репозиториев
    ├── UseCaseModule.kt                  # Модуль регистрации Use Cases
    ├── PresentationModule.kt             # Модуль ViewModels
    └── TranslationModule.kt              # Модуль TextTranslator
```

---

## В `di/NetworkModule.kt` можно поменять в адресе `ll` на `lldev` для тестирования (запросы будут безграничны)
