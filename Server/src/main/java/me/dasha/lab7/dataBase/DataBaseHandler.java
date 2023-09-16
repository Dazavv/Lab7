package me.dasha.lab7.dataBase;

import me.dasha.lab7.collectionClasses.*;
import me.dasha.lab7.utility.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Logger;

public class DataBaseHandler {

    private static String URL;
    private static String username;
    private static String password;
    private static Connection connection;
    private static boolean isConnect;
    private final static String pepper = "^34::]ddnk";
    private static final String characters = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890-=[];',./!@#$%TY&*()";
    private static Logger logger = Logger.getLogger(DataBaseHandler.class.getName());

    public DataBaseHandler(){
        connectToDb();
        createSequence();
        createEnums();
        createUsers();
        createSpaceMarine();
    }

    public static void connectToDb() {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("database.properties"))){
            props.load(in);
            URL = props.getProperty("url");
            username = props.getProperty("username");
            password = props.getProperty("password");
            connection = DriverManager.getConnection(URL, username, password);
            isConnect = true;
            logger.info("Успешное подключение к бд");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            logger.severe("Ошибка при подключении к бд");
            isConnect = false;
            System.exit(1);
        }
    }

    public boolean registerUser(User user) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (userExists(user.getUsername())) return false;
        String userSalt = getRandomString();
        PreparedStatement regStatement = connection.prepareStatement(DataBaseRequests.ADD_USER);
        regStatement.setString(1, user.getUsername());
        regStatement.setString(2, hashPassword(user.getPassword(), userSalt));
        regStatement.setString(3, userSalt);
        regStatement.executeUpdate();
        regStatement.close();
        logger.info("добавлен новый пользователь username: " + user.getUsername());
        return true;
    }

    private boolean userExists(String username) throws SQLException {
        PreparedStatement checkStatement = connection.prepareStatement(DataBaseRequests.CHECK_USER);
        checkStatement.setString(1, username);
        ResultSet resultSet = checkStatement.executeQuery();
        return resultSet.next();
    }
    public boolean loginUser(User inputUser) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            String username1 = inputUser.getUsername();
            PreparedStatement getUser = connection.prepareStatement(DataBaseRequests.CHECK_USER);
            getUser.setString(1, username1);
            ResultSet resultSet = getUser.executeQuery();
            if(resultSet.next()) {
                String inputUserPassword = resultSet.getString("password");
                String inputUserSalt = resultSet.getString("salt");
                String checkPassword = hashPassword(inputUser.getPassword(), inputUserSalt);
                return checkPassword.equals(inputUserPassword);
            }
            else {
                return false;
            }
//        } catch (PSQLException e) {
//            logger.severe("Ошибка ввода/вывода при отправке бэкенду");
//            return false;
        } catch (SQLException e) {
            logger.severe("Неверная команда sql");
            e.printStackTrace();
            return false;
        }
    }

    public static Stack<SpaceMarine> loadCollection(User user) {
        try {
            String usernameForLoadCollection = user.getUsername();
            PreparedStatement statement = connection.prepareStatement(DataBaseRequests.GET_OBJECTS);
            statement.setString(1, usernameForLoadCollection);
            ResultSet resultSet = statement.executeQuery();
            Stack<SpaceMarine> stack = new Stack<>();
            while (resultSet.next()){

                Coordinates coordinates = new Coordinates(
                        resultSet.getInt("x"),
                        resultSet.getDouble("y"));

                String chapterParentLegion = resultSet.getString("chapter_parent_legion");
                if (chapterParentLegion.isEmpty()) {
                    chapterParentLegion = null;
                }
                String chapterWorld = resultSet.getString("chapter_world");
                if (chapterWorld.isEmpty()) {
                    chapterWorld = null;
                }
                Chapter chapter = new Chapter(
                        resultSet.getString("chapter_name"),
                        chapterParentLegion,
                        chapterWorld);

                Weapon checkWeaponType = null;
                String weaponType = resultSet.getString("weapon_type");
                if (weaponType != null) {
                    checkWeaponType = Weapon.valueOf(weaponType);
                }

                MeleeWeapon checkMeleeWeapon = null;
                String meleeWeapon = resultSet.getString("melee_weapon");
                if (meleeWeapon != null) {
                    checkMeleeWeapon = MeleeWeapon.valueOf(meleeWeapon);
                }

                String achievements = resultSet.getString("achievements");
                if (achievements.isEmpty()) {
                    achievements = null;
                }

                SpaceMarine spaceMarine = new SpaceMarine(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        coordinates,
                        (resultSet.getTimestamp("creation_date")).toLocalDateTime(),
                        resultSet.getDouble("health"),
                        achievements,
                        checkWeaponType,
                        checkMeleeWeapon,
                        chapter,
                        resultSet.getString("owner_username"));
                stack.push(spaceMarine);
            }
            logger.info("Коллекция юзера " + usernameForLoadCollection + " загружена. Количество элементов - " + stack.size());
            return stack;
        } catch (SQLException e) {
            logger.severe("SQL ошибка");
            throw new RuntimeException(e);
        }
    }
    public static Integer addObject(SpaceMarine spaceMarine, User user){
        try {
            PreparedStatement statement = connection.prepareStatement(DataBaseRequests.ADD_OBJECT);
            statement.setString(1, spaceMarine.getName());
            statement.setInt(2, spaceMarine.getCoordinates().getX());
            statement.setDouble(3, spaceMarine.getCoordinates().getY());
            statement.setTimestamp(4, Timestamp.valueOf(spaceMarine.getCreationDate()));
            statement.setDouble(5, spaceMarine.getHealth());
            statement.setString(6, spaceMarine.getAchievements());
            statement.setObject(7, spaceMarine.getWeaponType(), Types.OTHER);
            statement.setObject(8, spaceMarine.getMeleeWeapon(), Types.OTHER);
            statement.setString(9, spaceMarine.getChapter().getName());
            statement.setString(10, spaceMarine.getChapter().getParentLegion());
            statement.setString(11, spaceMarine.getChapter().getWorld());
            statement.setString(12, user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.severe("Объект не добавлен в коллекцию");
                return -1;
            }
            logger.info("Объект добавлен в коллекцию");
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static Integer addObjectIfMin(SpaceMarine spaceMarine, User user){
        try {
            PreparedStatement statement = connection.prepareStatement(DataBaseRequests.ADD_OBJECT_IF_MIN);

            statement.setString(1, spaceMarine.getName());
            statement.setInt(2, spaceMarine.getCoordinates().getX());
            statement.setDouble(3, spaceMarine.getCoordinates().getY());
            statement.setTimestamp(4, Timestamp.valueOf(spaceMarine.getCreationDate()));
            statement.setDouble(5, spaceMarine.getHealth());
            statement.setString(6, spaceMarine.getAchievements());
            statement.setObject(7, spaceMarine.getWeaponType(), Types.OTHER);
            statement.setObject(8, spaceMarine.getMeleeWeapon(), Types.OTHER);
            statement.setString(9, spaceMarine.getChapter().getName());
            statement.setString(10, spaceMarine.getChapter().getParentLegion());
            statement.setString(11, spaceMarine.getChapter().getWorld());
            statement.setString(12, user.getUsername());

            statement.setDouble(13, spaceMarine.getHealth());
            statement.setString(14, user.getUsername());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.severe("Объект не добавлен в коллекцию");
                return -1;
            }
            logger.info("Объект добавлен в коллекцию");
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static boolean removeById(Integer id, User user){
        try{
            PreparedStatement statement = connection.prepareStatement(DataBaseRequests.DELETE_OBJECT_BY_ID);
            statement.setString(1, user.getUsername());
            statement.setInt(2, id);
            int stat = statement.executeUpdate();
            logger.info("объект удален из коллекцию");
            return stat != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("объект не удален из коллекцию");
            return false;
        }
    }
    public static boolean removeByAchievements(String achievements, User user){
        try{
            PreparedStatement statement = connection.prepareStatement(DataBaseRequests.DELETE_OBJECT_BY_ACHIEVEMENTS);
            statement.setString(1, achievements);
            statement.setString(2, user.getUsername());
            int stat = statement.executeUpdate();
            logger.info("объект удален из коллекцию");
            return stat != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("объект не удален из коллекцию");
            return false;
        }
    }
    public static boolean update(Integer id, SpaceMarine spaceMarine, User user){
        try {
            LocalDateTime creationTime = LocalDateTime.now();
            PreparedStatement statement = connection.prepareStatement(DataBaseRequests.UPDATE_OBJECT);
            statement.setString(1, spaceMarine.getName());
            statement.setInt(2, spaceMarine.getCoordinates().getX());
            statement.setDouble(3, spaceMarine.getCoordinates().getY());
            statement.setTimestamp(4, Timestamp.valueOf(creationTime));
            statement.setDouble(5, spaceMarine.getHealth());
            statement.setString(6, spaceMarine.getAchievements());
            statement.setObject(7, spaceMarine.getWeaponType(), Types.OTHER);
            statement.setObject(8, spaceMarine.getMeleeWeapon(), Types.OTHER);
            statement.setString(9, spaceMarine.getChapter().getName());
            statement.setString(10, spaceMarine.getChapter().getParentLegion());
            statement.setString(11, spaceMarine.getChapter().getWorld());
            statement.setInt(12, id);
            statement.setString(12, user.getUsername());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            logger.severe("Обновление объекта не произошло");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean clear (User user){
        try {
            PreparedStatement statement = connection.prepareStatement(DataBaseRequests.DELETE_ALL_OBJECTS);
            statement.setString(1, user.getUsername());
            int stat = statement.executeUpdate();
            logger.info("коллекция юзера " + user.getUsername() + " очищена");
            return stat != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void createSequence() {
        try {
            connection.prepareStatement(DataBaseRequests.SEQUENCE_ID).execute();
            logger.info("Sequence создан");
        } catch (SQLException e) {
            logger.info("Какая-та ошибка в SQL запросе sequence");
            e.printStackTrace();
        }

    }
    public static void createUsers() {
        try {
            connection.prepareStatement(DataBaseRequests.CREATE_USERS_TABLE).execute();
            logger.info("Таблица users создана");
        } catch (SQLException e) {
            logger.info("Какая-та ошибка в SQL запросе таблицы users");
        }

    }
    public static void createEnums() {
        try {
            connection.prepareStatement(DataBaseRequests.CREATE_ENUMS_TABLES).execute();
            logger.info("Enums созданы");
        } catch (SQLException e) {
            logger.info("Enums уже созданы");
        }
    }
    public static void createSpaceMarine() {
        try {
            connection.prepareStatement(DataBaseRequests.CREATE_MAIN_TABLES).execute();
            logger.info("Таблица space_marine создана");
        } catch (SQLException e) {
            logger.info("Какая-та ошибка в SQL запросе таблицы space_marine");
            e.printStackTrace();
        }
    }
    private static String getRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
    public static String hashPassword(String passwd, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] hash = md.digest((pepper + passwd + salt).getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }



}
