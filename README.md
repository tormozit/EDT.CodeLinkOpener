Данный плагин добавляет возможность переходить по тектовым ссылкам к строкам модулей.
#Минимальная версия EDT: 1.10.0

# Для разработки и запуска плагина необходимо, чтобы были установлены:
1. 1C:Enterprise Development Tools версии 1.10.0. (https://releases.1c.ru/version_files?nick=DevelopmentTools10&ver=1.10.0.1603)
2. Eclipse Oxygen (4.7.3) for RCP and RAP Developers (http://www.eclipse.org/downloads/packages/release/oxygen/3/eclipse-rcp-and-rap-developers)
3. Java SE Development Kit 8u181  (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

# Запуск плагина из Eclipse
Запускаем Eclipse на чистой рабочей области (workspace) 
## Импортируем проект
Импортировать проект можно любым удобным способом. Или, например:
 1. File -> Import -> Projects from Git
 2. В появившемся диалоге выбираем Clone URI, в поле URI вставляем адрес репозитория, далее следуем простым инструкциям помощника импорта проекта.

## Настраиваем Target Platform
После импорта проекта необходимо настроить целевую платформу. Для этого при открытии файла target/default.target нужно ввести в появившемся окне логин и пароль от "https://partners.v8.1c.ru/". После дожидаемся установки всех компонент и устанавливаем данную целевую платформу, ПКМ на <b>target/default.target</b> -> Open with -> Target Editor -> Set as target platform.

## Запуск 
ПКМ на проекте <b>ru.tormozit.dt.codelinkopener.plugin.ui</b> -> Run as -> Eclipse application.
После чего запустится 1C:Enterprise Development Tools с разработанным плагином.
Для проверки можно создать новую конфигурацию с документом и регистром накопления, далее в объектном модуле документа через контекстное меню вызвать "Создать движения регистров"

