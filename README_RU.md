[English](https://github.com/IsThisALis/ChatAIssistant/blob/main/README.md)

# ChatAIssistant

Twitch ИИ-ассистент, который может отвечать на самые разные вопросы в чате.
В следующих обновлениях планируется добавить полноценные средства модерации.

> ⚠️ **Важно:** В версиях до **v1.5** не гарантирована поддержка токенов с https://dev.twitch.tv. 
> Пожалуйста, используйте сервис [twitchtokengenerator.com](https://twitchtokengenerator.com) для получения Access Token.

## Установка

- Скачайте последний релиз в удобном формате (`.zip` или `.tar`).
- Распакуйте в любое удобное место.
- Настройте бота (см. раздел **Настройка**) и запустите `.bat` (Windows) или `executable` (Linux / Mac).

*Также вы можете собрать проект из исходников, чтобы получать самые свежие обновления.*

### Настройка

1. Получите **Access Token** одним из способов:
   - **До v1.5 (рекомендуется):** перейдите на [twitchtokengenerator.com](https://twitchtokengenerator.com), выберите **Chat Bot**, авторизуйтесь и скопируйте токен.
   - **Для v1.5+:** зарегистрируйте приложение на [dev.twitch.tv](https://dev.twitch.tv/) и сгенерируйте токен вручную.
   
   Сообщения будут отправляться от имени того аккаунта, через который вы авторизовались.

2. Заполните конфигурационные файлы:
   - Ваш никнейм → `configs/userName.txt` (например: `IsThisALis`)
   - Client ID → `configs/clientId.txt`
   - Access Token → `configs/accessToken.txt`

3. Получите ключ OpenRouter:
   - Перейдите на [openrouter.ai](https://openrouter.ai/) → **Get API Key** → войдите или зарегистрируйтесь.
   - Создайте новый API-ключ и сохраните его в `configs/apiKey.txt`.

4. Выберите модель:
   - Перейдите в раздел **Models** и выберите подходящую по бюджету (или любую с пометкой `(free)`).
   - Скопируйте название модели и сохраните его в `configs/model.txt`.

Вот так выглядит название модели в списке:
![a relative link](Images/model-name.png)

5. Настройте личность бота:
   - Выберите слово-триггер для вызова модели → `configs/askWord.txt`
   - Опишите себя → `configs/bio.txt`
   - Пропишите правила поведения → `configs/rules.txt` *(если не хотите, чтобы модель отвечала на определённые темы, добавьте инструкцию отвечать словом `"none"`)*.

Вот так должна выглядеть ваша папка `configs`:
![a relative link](Images/configs-folder.png)

### Компиляция из исходников

Вам понадобятся **Gradle** (последняя версия) и **JDK 21**.

```bash
git clone https://github.com/IsThisALis/ChatAIssistant.git 
cd ChatAIssistant
gradle build 
```

- Заберите собранный архив (`.zip` или `.tar`) из папки `build/distributions/`.
- Распакуйте его и переместите папку `configs` внутрь `ChatAIssistant/bin`.

# FAQ

Пока пусто. Если возникли проблемы — создайте [Issue](https://github.com/IsThisALis/ChatAIssistant/issues), разберёмся.
