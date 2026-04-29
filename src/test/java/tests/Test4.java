package tests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test4 {

    /**
     * Задача реализовать используя Stream
     * <p>
     *  Дано множество студентов, отсортируйте их по признакам (от наиболее важного к наименее важному):
     *  <p>
     * 1. Рейтинг (по убыванию)
     * 2. Первая буква фамилии (по возрастанию)
     * 3. Возраст (по возрастанию)
     * <p>
     * Верните отсортированный результат в виде строки с полными именами, разделенными запятыми.
     * Например, дан следующий список (имя, возраст, рейтинг):
     * <p>
     * • David Goodman, 23, 88 <p>
     * • Mark Rose, 25, 82 <p>
     * • Jane Doe, 22, 90 <p>
     * • Jane Dane, 25, 90 <p>
     * <p>
     *  sort(students) должен вернуть "Jane Doe,Jane Dane,David Goodman,Mark Rose"
     */
    public String sort(List<Student> students) {
        return "";
    }

    @Test
    public void basicTest() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(23, 88, "David Goodman"));
        students.add(new Student(25, 82, "Mark Rose"));
        students.add(new Student(22, 90, "Jane Doe"));
        students.add(new Student(25, 90, "Jane Dane"));
        System.out.println(sort(students));
        assertEquals("Jane Doe,Jane Dane,David Goodman,Mark Rose",
                sort(students));
    }

    @Getter
    @AllArgsConstructor
    public static class Student {
        int age;
        int rank;
        String fullName;
    }
}
