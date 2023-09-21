package me.dasha.lab7.dataBase;

public class DataBaseRequests {
    public static final String SEQUENCE_ID = "CREATE SEQUENCE IF NOT EXISTS generated_id INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;";
    public static final String CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                salt TEXT NOT NULL
            );""";
    public static final String CREATE_ENUMS_TABLES = """
            CREATE TYPE  weapon AS ENUM (
                'BOLTGUN',
                'BOLT_RIFLE',
                'COMBI_FLAMER',
                'MISSILE_LAUNCHER'
            );
            CREATE TYPE  meleeWeapon AS ENUM (
                'CHAIN_SWORD',
                'CHAIN_AXE',
                'MANREAPER',
                'POWER_BLADE'
            );
            """;
    public static final String CREATE_MAIN_TABLES = """
            CREATE TABLE IF NOT EXISTS space_marine (
                id INT NOT NULL PRIMARY KEY DEFAULT nextval('generated_id'),
                name TEXT NOT NULL,
                x INT NOT NULL CHECK (x > -623),
                y DOUBLE PRECISION NOT NULL CHECK (x > -347),
                creation_date TIMESTAMP NOT NULL,
                health DOUBLE PRECISION NOT NULL CHECK (health > 0),
                achievements TEXT,
                weapon_type weapon,
                melee_weapon meleeWeapon,
                chapter_name TEXT NOT NULL,
                chapter_parent_legion TEXT, 
                chapter_world TEXT,
                owner_username TEXT NOT NULL
            );
            """;
    public static final String ADD_USER = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?);";
    public static final String CHECK_USER = "SELECT * FROM users WHERE username = ?;";
    public static final String GET_OBJECTS = "SELECT * FROM space_marine";
    public static final String ADD_OBJECT = """
            INSERT INTO space_marine(
            name, x, y, creation_date, health, achievements, weapon_type, melee_weapon, chapter_name, chapter_parent_legion, chapter_world, owner_username)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;""";
    public static final String ADD_OBJECT_IF_MIN = """
            INSERT INTO space_marine(
            name, x, y, creation_date, health, achievements, weapon_type, melee_weapon, chapter_name, chapter_parent_legion, chapter_world, owner_username)
            SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? WHERE ? < (SELECT MIN(health) FROM space_marine WHERE owner_username = ?)
            RETURNING id;""";

    public static final String DELETE_ALL_OBJECTS = "DELETE FROM space_marine WHERE owner_username = ?;";
    public static final String DELETE_OBJECT_BY_ID = "DELETE FROM space_marine WHERE owner_username = ? AND id = ?;";
    public static final String DELETE_OBJECT_BY_ACHIEVEMENTS = "DELETE FROM space_marine WHERE achievements = ? AND owner_username = ?;";
    public static final String UPDATE_OBJECT = """
            UPDATE space_marine SET (
            name, x, y, creation_date, health, achievements, weapon_type, melee_weapon, chapter_name, chapter_parent_legion, chapter_world)
            = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) WHERE id = ? AND owner_username = ?;""";
}
