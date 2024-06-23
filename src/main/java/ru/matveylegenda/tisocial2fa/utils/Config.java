package ru.matveylegenda.tisocial2fa.utils;

import net.elytrium.serializer.annotations.Comment;
import net.elytrium.serializer.annotations.CommentValue;
import net.elytrium.serializer.annotations.NewLine;
import net.elytrium.serializer.language.object.YamlSerializable;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Config extends YamlSerializable {

    @Comment({
            @CommentValue(" Доступные варианты:"),
            @CommentValue(" LEGACY - \"&fПример &#650dbdтекста\""),
            @CommentValue(" MINIMESSAGE - \"<white>Пример</white> <color:#650dbd>текста</color>\" (https://webui.advntr.dev/)"),
    })
    public String serializer = "LEGACY";

    public Discord discord = new Discord();

    @NewLine(amount = 1)
    public static class Discord {
        public boolean enabled = false;

        @NewLine(amount = 1)
        @Comment({
                @CommentValue(" Токен дискорд бота")
        })
        public String token = "TOKEN";

        @NewLine(amount = 1)
        @Comment({
                @CommentValue(" Добавление юзеров"),
                @CommentValue(" NICK: \"DISCORD_ID\"")
        })
        public Map<String, String> users = new LinkedHashMap<>();
        {
            users.put("exampleUser", "12345");
        }
    }

    public Telegram telegram = new Telegram();

    public static class Telegram {
        public boolean enabled = false;

        @NewLine(amount = 1)
        @Comment({
                @CommentValue(" Токен телеграм бота")
        })
        public String token = "TOKEN";

        @NewLine(amount = 1)
        @Comment({
                @CommentValue(" Добавление юзеров"),
                @CommentValue(" NICK: \"CHAT_ID(чтобы узнать надо отправить бота /start)\"")
        })
        public Map<String, String> users = new LinkedHashMap<>();
        {
            users.put("exampleUser", "12345");
        }
    }

    public Settings settings = new Settings();

    @NewLine(amount = 1)
    public static class Settings {
        @Comment({
                @CommentValue(" Время в секундах через которое игрока кикнет если он не подтвердит вход")
        })
        public int time = 30;

        @NewLine(amount = 1)
        @Comment({
                @CommentValue(" Разрешенные команды во время подтверждения")
        })
        public List<String> allowedCommands = Arrays.asList("/register", "/reg", "/login", "/l");

        public Bossbar bossbar = new Bossbar();

        @NewLine(amount = 1)
        public static class Bossbar {
            public boolean enabled = true;

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Текст боссбара")
            })
            public String title = "Осталось &c{time} &fсекунд";

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Цвет боссбара")
            })
            public String color = "RED";

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Стиль боссбара")
            })
            public String style = "SEGMENTED_12";
        }

        @NewLine(amount = 1)
        @Comment({
                @CommentValue(" Команда которая будет выполняться если игрок не успел подтвердить вход")
        })
        public String timeoutCommand = "kick {player} &cВы не успели подтвердить вход!";

        @NewLine(amount = 1)
        @Comment({
                @CommentValue(" Команда которая будет выполняться если игрока кикнули через кнопку")
        })
        public String punishCommand = "ban {player} 5m &cВас выгнали!";
    }

    public Messages messages = new Messages();

    @NewLine(amount = 1)
    public static class Messages {
        public Social social = new Social();

        public static class Social {
            public Buttons buttons = new Buttons();

            public static class Buttons {
                public Allow allow = new Allow();

                public static class Allow {
                    @Comment({
                            @CommentValue(" Эмодзи кнопки")
                    })
                    public String emoji = "✅";

                    @Comment({
                            @CommentValue(" Текст кнопки")
                    })
                    public String text = "Подтвердить";
                }

                public Deny deny = new Deny();

                public static class Deny {
                    @Comment({
                            @CommentValue(" Эмодзи кнопки")
                    })
                    public String emoji = "❌";

                    @Comment({
                            @CommentValue(" Текст кнопки")
                    })
                    public String text = "Отклонить";
                }
            }

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Сообщение отправляемое при входе на сервер")
            })
            public String join = "Подтвердите вход на сервер!";

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Сообщение отправляемое при подтверждении входа")
            })
            public String allowJoin = "Вход подтвержден!";

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Сообщение отправляемое при отклонении входа")
            })
            public String denyJoin = "Вход отклонен!";

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Сообщение отправляемое если игрок уже подтвердил вход")
            })
            public String verifyNotRequired = "Подтверждение не требуется";

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Сообщение отправляемое если игрок не найден на сервере")
            })
            public String playerNotFound = "Аккаунт не найден!";
        }

        public Minecraft minecraft = new Minecraft();

        public static class Minecraft {
            @Comment({
                    @CommentValue(" Сообщение отправляемое при входе на сервер")
            })
            public List<String> join = Arrays.asList("", " &fПодтвердите вход через &bсоц. сеть&f!", "");

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Сообщение отправляемое при подтверждении входа")
            })
            public List<String> allowJoin = Arrays.asList("", " &fВход успешно выполнен!", "");

            @NewLine(amount = 1)
            @Comment({
                    @CommentValue(" Сообщение отправляемое при вводе команды без аргументов")
            })
            public List<String> usage = Arrays.asList(
                    "",
                    "Команды:",
                    "/social2fa reload - перезагрузить конфиг",
                    "/social2fa add <discord/telegram> <ник> <айди> - добавить игрока в список",
                    "/social2fa remove <discord/telegram> <ник> <айди> - удалить игрока из списка",
                    ""
            );

            @Comment({
                    @CommentValue(" Сообщение отправляемое при перезагрузке конфига")
            })
            public String reload = "&aКонфиг перезагружен!";

            @Comment({
                    @CommentValue(" Сообщение отправляемое при добавлении игрока")
            })
            public String addPlayer = "&aИгрок {player} успешно добавлен";

            @Comment({
                    @CommentValue(" Сообщение отправляемое при удалении игрока")
            })
            public String removePlayer = "&aИгрок {player} успешно удален";

            @Comment({
                    @CommentValue(" Сообщение отправляемое при удалении игрока")
            })
            public String removePlayerNotFound = "&cИгрок {player} не найден в списке";

            @Comment({
                    @CommentValue(" Сообщение отправляемое при отсутствии прав на команду")
            })
            public String noPermission = "&cНет прав!";
        }
    }

    public void saveConfig() {
        save(TiSocial2FA.getInstance().configFile.toPath());
    }

    public void reloadConfig() {
        reload(TiSocial2FA.getInstance().configFile.toPath());
    }
}
