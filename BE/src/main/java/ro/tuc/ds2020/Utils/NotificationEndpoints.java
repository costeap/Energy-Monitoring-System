package ro.tuc.ds2020.Utils;

public class NotificationEndpoints {

    public static final String MAXIMUM_HOURLY_CONSUMPTION_EXCEEDED = "/topic/socket/client";
    public static final String MESSAGE_FROM_ADMIN = "/topic/socket/message/admin";
    public static final String MESSAGE_FROM_CLIENT = "/topic/socket/message/client";
    public static final String ADMIN_IS_TYPING = "/topic/socket/typing/admin";
    public static final String CLIENT_IS_TYPING = "/topic/socket/typing/client";
    public static final String ADMIN_HAS_READ = "/topic/socket/read/admin";
    public static final String CLIENT_HAS_READ = "/topic/socket/read/client";
}
