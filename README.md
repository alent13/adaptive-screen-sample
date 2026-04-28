# Adaptive Android Compose Sample

Учебный проект, демонстрирующий реализацию современного адаптивного интерфейса на Jetpack Compose для телефонов, планшетов и складных устройств (Foldables).

## Особенности
- **Adaptive Scaffolding**: Использование `ListDetailPaneScaffold` из библиотеки `material3-adaptive` для автоматического переключения между однопанельным и двухпанельным режимами.
- **Fold Awareness**: Интеграция с `currentWindowAdaptiveInfo()` для корректной работы на складных устройствах (например, Samsung Galaxy Z Fold).
- **Navigation State Preservation**: Сохранение стека навигации и состояния выбранных элементов при раскрытии/закрытии устройства.
- **Navigation Suite**: Адаптивное боковое меню (`ModalNavigationDrawer`) с кнопкой-бургером.
- **Type-safe Navigation**: Использование Kotlin Serialization для безопасной навигации между экранами.

## Технологический стек
- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM / State-driven UI
- **Adaptive**: `androidx.compose.material3.adaptive`
- **Navigation**: `androidx.navigation:navigation-compose`
- **Build System**: Gradle Kotlin DSL + Version Catalogs

## Как это работает
Приложение анализирует `WindowSizeClass` и состояние петель устройства. 
- На **Compact** устройствах (телефоны) используется стандартная навигация: список -> детали.
- На **Medium/Expanded** устройствах (планшеты, раскрытые фолды) приложение отображает список и детали одновременно.
- Обработка кнопки "Назад" адаптирована: в двухпанельном режиме первое нажатие снимает выделение с элемента, а не закрывает экран.

## Скриншоты (планируется)
*(Здесь можно добавить скриншоты работы в разных режимах)*

## Установка
1. Клонируйте репозиторий.
2. Откройте в Android Studio (Ladybug или новее).
3. Используйте JDK 17+ для сборки.
