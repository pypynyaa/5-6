
package managers;

import com.jcraft.jsch.Session;
import mainClasses.Coordinates;
import mainClasses.Car;
import mainClasses.WeaponType;
import mainClasses.HumanBeing;
import network.SSHConnection;
import network.Security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shit.User;


import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static Connection connection;
    private static SSHConnection sshTunnel;

    public static void initialize(String sshUser, String sshPassword,
                                  String sshHost, int sshPort,
                                  int localPort, String dbHost, int dbPort,
                                  String dbName, String dbUser, String dbPassword) throws SQLException {

        sshTunnel = new SSHConnection(sshUser, sshPassword, sshHost, sshPort,
                localPort, dbHost, dbPort);

        Thread tunnelThread = new Thread(sshTunnel, "SSH-Tunnel-Thread");
        tunnelThread.setDaemon(true);
        tunnelThread.start();

        try {

            if (!sshTunnel.waitUntilConnected(10000)) {
                throw new SQLException("Не удалось установить SSH-соединение в течение 10 секунд.");
            }


            String jdbcUrl = String.format("jdbc:postgresql://localhost:%d/%s",
                    localPort, dbName);

            Properties props = new Properties();
            props.setProperty("user", dbUser);
            props.setProperty("password", dbPassword);
            props.setProperty("ssl", "false");
            props.setProperty("connectTimeout", "10");
            props.setProperty("socketTimeout", "10");
            props.setProperty("tcpKeepAlive", "true");


            connection = DriverManager.getConnection(jdbcUrl, props);
            logger.info("Успешное подключение к БД через SSH-туннель");

            connection.setAutoCommit(true);

        } catch (InterruptedException e) {
            logger.error("Ожидание SSH-туннеля было прервано", e);
            Thread.currentThread().interrupt();
            throw new SQLException("Ожидание подключения прервано", e);
        } catch (SQLException e) {
            logger.fatal("Не удалось подключиться к базе данных: {}", e.getMessage(), e);
            throw e;
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("DatabaseManager не инициализирован. Сначала вызовите initialize().");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Соединение с БД закрыто.");
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии соединения с БД: {}", e.getMessage(), e);
            }
        }
        if (sshTunnel != null) {
            sshTunnel.close();
        }
    }


    public static int addHumanBeing(HumanBeing humanBeing) throws SQLException {
        PreparedStatement coordsStmt = null;
        PreparedStatement carStmt = null;
        PreparedStatement humanBeingStmt = null;
        ResultSet generatedKeys = null;
        int humanBeingId = -1;

        try {
            connection.setAutoCommit(false);


            String coordsSql = "INSERT INTO coordinates (x, y) VALUES (?, ?)";
            coordsStmt = connection.prepareStatement(coordsSql, Statement.RETURN_GENERATED_KEYS);
            coordsStmt.setDouble(1, humanBeing.getCoordinates().getX());
            coordsStmt.setLong(2, humanBeing.getCoordinates().getY());
            coordsStmt.executeUpdate();

            int coordsId;
            generatedKeys = coordsStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                coordsId = generatedKeys.getInt(1);
                logger.debug("Сгенерирован ID для координат: {}", coordsId);
            } else {
                throw new SQLException("Не удалось получить ID координат!");
            }
            generatedKeys.close();
            generatedKeys = null;


            Integer carId = null;
            if (humanBeing.getCar() != null) {
                String carSql = "INSERT INTO car (name) VALUES (?)";
                carStmt = connection.prepareStatement(carSql, Statement.RETURN_GENERATED_KEYS);
                carStmt.setString(1, humanBeing.getCar().getName());
                carStmt.executeUpdate();

                generatedKeys = carStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    carId = generatedKeys.getInt(1);
                    logger.debug("Сгенерирован ID для машины: {}", carId);
                } else {
                    throw new SQLException("Не удалось получить ID машины!");
                }
                generatedKeys.close();
                generatedKeys = null;
            }






            String humanBeingSql = "INSERT INTO humanbeing (" +
                    "name, coordinates_id, car_id, user_id, creationDate, realHero, hasToothpick, " +
                    "impactSpeed, soundtrackName, minutesOfWaiting, weaponType) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            humanBeingStmt = connection.prepareStatement(humanBeingSql, Statement.RETURN_GENERATED_KEYS);

            humanBeingStmt.setString(1, humanBeing.getName());
            humanBeingStmt.setInt(2, coordsId);
            if (carId != null) {
                humanBeingStmt.setInt(3, carId);
            } else {
                humanBeingStmt.setNull(3, Types.INTEGER);
            }



            int userId = getUserIdByUsername(humanBeing.getUser());
            humanBeingStmt.setInt(4, userId);


            humanBeingStmt.setDate(5, Date.valueOf(humanBeing.getCreationDate()));
            humanBeingStmt.setBoolean(6, humanBeing.getRealHero());
            humanBeingStmt.setBoolean(7, humanBeing.getHasToothpick());
            humanBeingStmt.setFloat(8, humanBeing.getImpactSpeed());
            humanBeingStmt.setString(9, humanBeing.getSoundtrackName());
            if (humanBeing.getMinutesOfWaiting() != null) {
                humanBeingStmt.setFloat(10, humanBeing.getMinutesOfWaiting());
            } else {
                humanBeingStmt.setNull(10, Types.REAL);
            }
            if (humanBeing.getWeaponType() != null) {
                humanBeingStmt.setString(11, humanBeing.getWeaponType().name());
            } else {
                humanBeingStmt.setNull(11, Types.VARCHAR);
            }

            int affectedRows = humanBeingStmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = humanBeingStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    humanBeingId = generatedKeys.getInt(1);
                    logger.debug("Сгенерирован ID для HumanBeing: {}", humanBeingId);
                } else {
                    throw new SQLException("Создание HumanBeing не удалось, не получен ID.");
                }
            } else {
                throw new SQLException("Создание HumanBeing не удалось, ни одна строка не затронута.");
            }

            connection.commit();
            logger.info("HumanBeing с ID {} успешно добавлен в БД.", humanBeingId);
            return humanBeingId;

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Ошибка при добавлении HumanBeing: {}", e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
                if (generatedKeys != null) generatedKeys.close();
                if (humanBeingStmt != null) humanBeingStmt.close();
                if (carStmt != null) carStmt.close();
                if (coordsStmt != null) coordsStmt.close();
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии ресурсов после addHumanBeing: {}", e.getMessage(), e);
            }
        }
    }

    public static int getUserIdByUsername(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {


                throw new SQLException("Пользователь с именем '" + username + "' не найден в БД. Невозможно добавить HumanBeing.");
            }
        }
    }


    public static boolean updateHumanBeing(int humanBeingId, HumanBeing updatedHumanBeing) throws SQLException {
        PreparedStatement selectStmt = null;
        PreparedStatement coordsStmt = null;
        PreparedStatement carStmt = null;
        PreparedStatement humanBeingStmt = null;
        ResultSet rs = null;

        try {
            connection.setAutoCommit(false);


            String selectSql = "SELECT coordinates_id, car_id FROM humanbeing WHERE id = ?";
            selectStmt = connection.prepareStatement(selectSql);
            selectStmt.setInt(1, humanBeingId);
            rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("HumanBeing с ID " + humanBeingId + " не найден");
            }

            int coordinatesId = rs.getInt("coordinates_id");
            Integer carId = rs.getObject("car_id", Integer.class);


            String coordsSql = "UPDATE coordinates SET x = ?, y = ? WHERE id = ?";
            coordsStmt = connection.prepareStatement(coordsSql);
            coordsStmt.setDouble(1, updatedHumanBeing.getCoordinates().getX());
            coordsStmt.setLong(2, updatedHumanBeing.getCoordinates().getY());
            coordsStmt.setInt(3, coordinatesId);
            int affectedCoordsRows = coordsStmt.executeUpdate();
            if (affectedCoordsRows == 0) {
                logger.warn("Не удалось обновить координаты для HumanBeing ID {}, coords_id {}", humanBeingId, coordinatesId);
            }


            if (updatedHumanBeing.getCar() != null) {
                if (carId != null) {
                    String carUpdateSql = "UPDATE car SET name = ? WHERE id = ?";
                    carStmt = connection.prepareStatement(carUpdateSql);
                    carStmt.setString(1, updatedHumanBeing.getCar().getName());
                    carStmt.setInt(2, carId);
                    carStmt.executeUpdate();
                } else {

                    String carInsertSql = "INSERT INTO car (name) VALUES (?)";
                    carStmt = connection.prepareStatement(carInsertSql, Statement.RETURN_GENERATED_KEYS);
                    carStmt.setString(1, updatedHumanBeing.getCar().getName());
                    carStmt.executeUpdate();
                    ResultSet generatedCarKeys = carStmt.getGeneratedKeys();
                    if (generatedCarKeys.next()) {
                        carId = generatedCarKeys.getInt(1);
                        logger.debug("Сгенерирован новый ID для машины при обновлении: {}", carId);
                    } else {
                        throw new SQLException("Не удалось получить ID новой машины при обновлении!");
                    }
                    generatedCarKeys.close();
                }
            } else {
                if (carId != null) {

                    String deleteCarSql = "DELETE FROM car WHERE id = ?";
                    try (PreparedStatement deleteCarStmt = connection.prepareStatement(deleteCarSql)) {
                        deleteCarStmt.setInt(1, carId);
                        deleteCarStmt.executeUpdate();
                        logger.debug("Машина с ID {} удалена при обновлении HumanBeing ID {}", carId, humanBeingId);
                    }
                }
                carId = null;
            }



            String humanBeingSql = "UPDATE humanbeing SET name = ?, realHero = ?, hasToothpick = ?, " +
                    "impactSpeed = ?, soundtrackName = ?, minutesOfWaiting = ?, weaponType = ?, car_id = ? " +
                    "WHERE id = ?";
            humanBeingStmt = connection.prepareStatement(humanBeingSql);

            humanBeingStmt.setString(1, updatedHumanBeing.getName());
            humanBeingStmt.setBoolean(2, updatedHumanBeing.getRealHero());
            humanBeingStmt.setBoolean(3, updatedHumanBeing.getHasToothpick());
            humanBeingStmt.setFloat(4, updatedHumanBeing.getImpactSpeed());
            humanBeingStmt.setString(5, updatedHumanBeing.getSoundtrackName());
            if (updatedHumanBeing.getMinutesOfWaiting() != null) {
                humanBeingStmt.setFloat(6, updatedHumanBeing.getMinutesOfWaiting());
            } else {
                humanBeingStmt.setNull(6, Types.REAL);
            }
            if (updatedHumanBeing.getWeaponType() != null) {
                humanBeingStmt.setString(7, updatedHumanBeing.getWeaponType().name());
            } else {
                humanBeingStmt.setNull(7, Types.VARCHAR);
            }
            if (carId != null) {
                humanBeingStmt.setInt(8, carId);
            } else {
                humanBeingStmt.setNull(8, Types.INTEGER);
            }
            humanBeingStmt.setInt(9, humanBeingId);

            int affectedRows = humanBeingStmt.executeUpdate();
            connection.commit();
            logger.info("HumanBeing с ID {} успешно обновлен. Затронуто строк: {}", humanBeingId, affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Ошибка при обновлении HumanBeing (ID: {}): {}", humanBeingId, e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
                if (rs != null) rs.close();
                if (selectStmt != null) selectStmt.close();
                if (humanBeingStmt != null) humanBeingStmt.close();
                if (carStmt != null) carStmt.close();
                if (coordsStmt != null) coordsStmt.close();
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии ресурсов после updateHumanBeing: {}", e.getMessage(), e);
            }
        }
    }



    public static boolean deleteHumanBeing(HumanBeing humanBeing) throws SQLException {
        if (humanBeing == null) {
            throw new IllegalArgumentException("Объект HumanBeing не может быть null для удаления.");
        }

        PreparedStatement deleteHumanBeingStmt = null;
        PreparedStatement selectIdsStmt = null;
        int carIdToDelete = -1;
        int coordsIdToDelete = -1;

        try {
            connection.setAutoCommit(false);


            String selectIdsSql = "SELECT car_id, coordinates_id FROM humanbeing WHERE id = ?";
            selectIdsStmt = connection.prepareStatement(selectIdsSql);
            selectIdsStmt.setInt(1, humanBeing.getId());
            ResultSet rs = selectIdsStmt.executeQuery();
            if (rs.next()) {
                Integer carId = rs.getObject("car_id", Integer.class);
                if (carId != null) {
                    carIdToDelete = carId;
                }
                coordsIdToDelete = rs.getInt("coordinates_id");
            } else {

                connection.rollback();
                logger.warn("Попытка удалить несуществующий HumanBeing с ID: {}", humanBeing.getId());
                return false;
            }
            rs.close();


            String deleteHumanBeingSql = "DELETE FROM humanbeing WHERE id = ?";
            deleteHumanBeingStmt = connection.prepareStatement(deleteHumanBeingSql);
            deleteHumanBeingStmt.setInt(1, humanBeing.getId());
            int affectedRows = deleteHumanBeingStmt.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return false;
            }


            if (carIdToDelete != -1) {
                String deleteCarSql = "DELETE FROM car WHERE id = ?";
                try (PreparedStatement deleteCarFromCarTableStmt = connection.prepareStatement(deleteCarSql)) {
                    deleteCarFromCarTableStmt.setInt(1, carIdToDelete);
                    deleteCarFromCarTableStmt.executeUpdate();
                    logger.debug("Машина с ID {} удалена.", carIdToDelete);
                }
            }



            String deleteCoordsSql = "DELETE FROM coordinates WHERE id = ?";
            try (PreparedStatement deleteCoordsStmt = connection.prepareStatement(deleteCoordsSql)) {
                deleteCoordsStmt.setInt(1, coordsIdToDelete);
                deleteCoordsStmt.executeUpdate();
                logger.debug("Координаты с ID {} удалены.", coordsIdToDelete);
            }

            connection.commit();
            logger.info("HumanBeing с ID {} успешно удален вместе с связанными данными.", humanBeing.getId());
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Ошибка при удалении HumanBeing (ID: {}): {}", humanBeing.getId(), e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
                if (deleteHumanBeingStmt != null) deleteHumanBeingStmt.close();
                if (selectIdsStmt != null) selectIdsStmt.close();
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии ресурсов после deleteHumanBeing: {}", e.getMessage(), e);
            }
        }
    }



    public static boolean saveUser(User user) {
        String sql = "INSERT INTO users (username, pass_hash, salt) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            String salt = Security.generateSalt();
            String hashedPassword = Security.hashPassword(user.getPassword(), salt);

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, salt);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 1) {
                logger.info("Пользователь '{}' успешно сохранен.", user.getUserName());
                return true;
            }
            return false;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                logger.warn("Ошибка: пользователь с именем '{}' уже существует.", user.getUserName());
            } else {
                logger.error("Ошибка при сохранении пользователя '{}': {}", user.getUserName(), e.getMessage(), e);
            }
            return false;
        }
    }


    public static HashMap<String, String> loadAllUsers() {
        HashMap<String, String> usersMap = new HashMap<>();
        String sql = "SELECT username, pass_hash FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String passwordHash = rs.getString("pass_hash");
                usersMap.put(username, passwordHash);
            }
            logger.debug("Загружено {} пользователей из БД.", usersMap.size());

        } catch (SQLException e) {
            logger.error("Ошибка при загрузке пользователей: {}", e.getMessage(), e);
        }
        return usersMap;
    }


    public static boolean verifyUser(String username, String inputPassword) throws SQLException {
        String sql = "SELECT pass_hash, salt FROM users WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("pass_hash");
                String salt = rs.getString("salt");
                String inputHash = Security.hashPassword(inputPassword, salt);
                boolean matches = storedHash.equals(inputHash);
                logger.debug("Верификация пользователя '{}': {}", username, matches ? "успешна" : "неудачна");
                return matches;
            }
            logger.debug("Верификация пользователя '{}': пользователь не найден.", username);
            return false;

        } catch (SQLException e) {
            logger.error("Ошибка при верификации пользователя '{}': {}", username, e.getMessage(), e);
            throw e;
        }
    }


    public static LinkedHashSet<HumanBeing> loadAllHumanBeings() {
        LinkedHashSet<HumanBeing> humanBeings = new LinkedHashSet<>();

        try (Statement stmt = connection.createStatement()) {
            String sql = "SELECT hb.id, hb.name, hb.creationDate, hb.realHero, hb.hasToothpick, " +
                    "hb.impactSpeed, hb.soundtrackName, hb.minutesOfWaiting, hb.weaponType, " +
                    "c.x, c.y, car.name as car_name, u.username as created_by_username " +
                    "FROM humanbeing hb " +
                    "JOIN coordinates c ON hb.coordinates_id = c.id " +
                    "LEFT JOIN car ON hb.car_id = car.id " +
                    "JOIN users u ON hb.user_id = u.id " +
                    "ORDER BY hb.id";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                HumanBeing humanBeing = new HumanBeing();

                humanBeing.setId(rs.getInt("id"));
                humanBeing.setName(rs.getString("name"));
                humanBeing.setCreationDate(rs.getDate("creationDate").toLocalDate());
                humanBeing.setRealHero(rs.getBoolean("realHero"));
                humanBeing.setHasToothpick(rs.getBoolean("hasToothpick"));
                humanBeing.setImpactSpeed(rs.getFloat("impactSpeed"));
                humanBeing.setSoundtrackName(rs.getString("soundtrackName"));




                float minutesOfWaitingPrimitive = rs.getFloat("minutesOfWaiting");
                if (rs.wasNull()) {
                    humanBeing.setMinutesOfWaiting(null);
                } else {
                    humanBeing.setMinutesOfWaiting(minutesOfWaitingPrimitive);
                }

                String weaponTypeStr = rs.getString("weaponType");
                if (weaponTypeStr != null) {
                    humanBeing.setWeaponType(WeaponType.valueOf(weaponTypeStr));
                } else {
                    humanBeing.setWeaponType(null);
                }

                humanBeing.setUser(rs.getString("created_by_username"));


                String carName = rs.getString("car_name");
                if (carName != null) {
                    Car car = new Car();
                    car.setName(carName);
                    humanBeing.setCar(car);
                } else {
                    humanBeing.setCar(null);
                }


                Coordinates coordinates = new Coordinates();
                coordinates.setX((float) rs.getDouble("x"));
                coordinates.setY(rs.getLong("y"));
                humanBeing.setCoordinates(coordinates);

                humanBeings.add(humanBeing);
                logger.debug("Загружен HumanBeing с ID: {}", humanBeing.getId());
            }
            logger.info("Загружено {} HumanBeing'ов из БД.", humanBeings.size());
        } catch (SQLException e) {
            logger.error("Ошибка при загрузке HumanBeing'ов: {}", e.getMessage(), e);
        }
        return humanBeings;
    }

}