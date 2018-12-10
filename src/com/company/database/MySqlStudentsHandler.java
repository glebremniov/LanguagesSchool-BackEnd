package com.company.database;

import com.company.database.configuration.DbTables;
import com.company.model.Student;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MySqlStudentsHandler extends MySqlHandler {

    public static int getCountByTeacherUsername(String username) {
        ResultSet resultSet = null;
        int count = 0;
        try (Connection connection = getDBConnection()) {
            final String query = "select count(distinct students.username) from orders_courses\n" +
                    "inner join courses on courses.id = orders_courses.id_course\n" +
                    "inner join teachers on teachers.id = courses.id_teacher\n" +
                    "inner join names_of_courses on courses.id_name_of_course = names_of_courses.id\n" +
                    "inner join students on orders_courses.username_student = students.username\n" +
                    "where teachers.username = ?;";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                count = resultSet.getInt("count(distinct students.username)");
            }

            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closePreparedStatement();
            }
        }
    }

    public static List<Student> selectAllStudents() {
        ResultSet resultSet = null;
        try (Connection connection = getDBConnection()) {

            final String query = "SELECT * FROM LanguagesSchool.students";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            List<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setFirstName(resultSet.getString("first_name"));
                student.setMiddleName(resultSet.getString("middle_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setUsername(resultSet.getString("username"));
                student.setPassword(resultSet.getString("password"));
                student.setBirthdate(resultSet.getString("birthdate"));
                studentList.add(student);
            }
            return studentList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closePreparedStatement();
            }
        }
    }

    public static List<Student> selectAllStudentsByTeacherUsername(String username) {
        ResultSet resultSet = null;
        try (Connection connection = getDBConnection()) {

            final String query = "select " +
                    "LanguagesSchool.students.first_name, " +
                    "LanguagesSchool.students.middle_name, " +
                    "LanguagesSchool.students.last_name, " +
                    "LanguagesSchool.students.birthdate, " +
                    "LanguagesSchool.names_of_courses.name " +
                    "from LanguagesSchool.orders_courses\n" +
                    "inner join LanguagesSchool.students " +
                    "on LanguagesSchool.orders_courses.username_student = students.username\n" +
                    "inner join LanguagesSchool.courses " +
                    "on orders_courses.id_course = courses.id\n" +
                    "inner join LanguagesSchool.teachers " +
                    "on courses.id_teacher = teachers.id\n" +
                    "inner join LanguagesSchool.names_of_courses " +
                    "on courses.id_name_of_course = names_of_courses.id\n" +
                    "\t\twhere teachers.username = ?;";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            List<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setFirstName(resultSet.getString("first_name"));
                student.setMiddleName(resultSet.getString("middle_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setBirthdate(resultSet.getDate("birthdate"));
                student.setAdditionInfo(resultSet.getString("name"));
                studentList.add(student);
            }
            return studentList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closePreparedStatement();
            }
        }
    }

    public static Student selectStudentByUsernameAndPassword(Student student) {
        ResultSet resultSet = null;
        try (Connection connection = getDBConnection()) {

            final String query = "SELECT " +
                    "LanguagesSchool.students.first_name, " +
                    "LanguagesSchool.students.middle_name, " +
                    "LanguagesSchool.students.last_name, " +
                    "LanguagesSchool.students.username, " +
                    "LanguagesSchool.students.password, " +
                    "LanguagesSchool.students.birthdate, " +
                    "LanguagesSchool.students.gender " +
                    "FROM LanguagesSchool.students " +
                    "WHERE LanguagesSchool.students.username = ? AND LanguagesSchool.students.password = ?;";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,student.getUsername());
            preparedStatement.setString(2,student.getPassword());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                student.setFirstName(resultSet.getString("first_name"));
                student.setMiddleName(resultSet.getString("middle_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setUsername(resultSet.getString("username"));
                student.setPassword(resultSet.getString("password"));
                student.setBirthdate(resultSet.getDate("birthdate"));
                student.setMale((resultSet.getString("gender").equalsIgnoreCase("male")));
            }
            return student;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closePreparedStatement();
            }
        }
    }

    public static boolean updateStudent(Student student) {
        try (Connection connection = getDBConnection()) {
            final String query = "UPDATE " + DbTables.TABLE_STUDENTS + " SET " +
                    "LanguagesSchool.students.first_name = ?, " +
                    "LanguagesSchool.students.middle_name = ?, " +
                    "LanguagesSchool.students.last_name = ?, " +
                    "LanguagesSchool.students.password = ?, " +
                    "LanguagesSchool.students.birthdate = ?, " +
                    "LanguagesSchool.students.gender = ? " +
                    "WHERE (LanguagesSchool.students.username=?);";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getMiddleName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.setString(4, student.getPassword());
            Calendar cal = Calendar.getInstance();
            cal.setTime(student.getBirthdate());
            String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
            preparedStatement.setString(5, formatedDate);
            preparedStatement.setString(6, (student.isMale() ? "Male" : "Female"));
            preparedStatement.setString(7, student.getUsername());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closePreparedStatement();
        }
    }

    public static boolean deleteStudent(Student student) {
        try (Connection connection = getDBConnection()) {

            final String query = "DELETE FROM LanguagesSchool.students WHERE (LanguagesSchool.students.username=?);";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,student.getUsername());
            int rows = preparedStatement.executeUpdate();

            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closePreparedStatement();
        }
    }

    public static Student insertStudent(Student student) {
        ResultSet resultSet = null;
        try (Connection connection = getDBConnection()) {
            final String query = "INSERT INTO LanguagesSchool.students (" +
                    "first_name, " +
                    "middle_name, " +
                    "last_name, " +
                    "username, " +
                    "password, " +
                    "birthdate, " +
                    "gender) VALUES (?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,student.getFirstName());
            preparedStatement.setString(2,student.getMiddleName());
            preparedStatement.setString(3,student.getLastName());
            preparedStatement.setString(4,student.getUsername());
            preparedStatement.setString(5,student.getPassword());
            Calendar cal = Calendar.getInstance();
            cal.setTime(student.getBirthdate());
            String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
            preparedStatement.setString(6, formatedDate);
            preparedStatement.setString(7,(student.isMale()?"Male":"Female"));

            preparedStatement.execute();

            return selectStudentByUsernameAndPassword(student);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closePreparedStatement();
            }
        }
    }

}
