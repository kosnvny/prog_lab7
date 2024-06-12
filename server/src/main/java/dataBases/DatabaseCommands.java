package dataBases;

public class DatabaseCommands {
    public static final String creation = """
            CREATE TYPE FORM_OF_EDUCATION AS ENUM(
                'DISTANCE_EDUCATION',
                'FULL_TIME_EDUCATION',
                'EVENING_CLASSES'
            );
            CREATE TYPE COLOR AS ENUM (
                'GREEN',
                'RED',
                'YELLOW',
                'WHITE'
            );
            CREATE TYPE COUNTRY AS ENUM(
                'RUSSIA',
                'UNITED_KINGDOM',
                'INDIA',
                'ITALY'
            );
            CREATE TYPE SEMESTER AS ENUM (
                'FIRST',
                'SECOND',
                'THIRD',
                'FOURTH',
                'SIXTH'
            );
            CREATE TABLE IF NOT EXISTS studyGroup (
                id SERIAL PRIMARY KEY,
                group_name TEXT NOT NULL ,
                cord_x NUMERIC NOT NULL,
                cord_y NUMERIC NOT NULL ,
                creation_date TIMESTAMP NOT NULL ,
                students_count BIGINT NOT NULL ,
                expelled_students INT NOT NULL ,
                form_of_education FORM_OF_EDUCATION,
                semester SEMESTER,
                person_name TEXT NOT NULL ,
                person_weight INT,
                passport text,
                person_hair_color COLOR,
                person_nationality COUNTRY,
                owner_login TEXT NOT NULL
            );
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                login TEXT,
                password TEXT,
                salt TEXT
            );
            """;
    public static final String addUser = """
            INSERT INTO users(login, password, salt) VALUES (?, ?, ?);""";
    public static final String getUser = """
            SELECT * FROM users WHERE (login = ?);""";
    // passport генерится внутри person'а
    public static final String addObject = """
            INSERT INTO studygroup(group_name, cord_x, cord_y, creation_date, students_count, expelled_students, form_of_education, semester, person_name, person_weight, 
            person_hair_color, person_nationality, owner_login)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;
    public static final String getAllObjects = """
            SELECT * FROM studygroup;
            """;
    public static final String deleteUserOwnedObjects = """
            DELETE FROM studygroup WHERE (owner_login = ?) AND (id = ?) RETURNING id;
            """;
    public static final String updateUserObject = """
            UPDATE studygroup
            SET (group_name, cord_x, cord_y, creation_date, students_count, expelled_students, form_of_education, semester, person_name, person_weight, person_hair_color, person_nationality)
             = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            WHERE (id = ?) AND (owner_login = ?)
            RETURNING id;
            """;
}
