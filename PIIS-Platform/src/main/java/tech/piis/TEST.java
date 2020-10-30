package tech.piis;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: PACKAGE_NAME
 * User: Tuzki
 * Date: 2020/9/24
 * Time: 8:34
 * Description:
 */
public class TEST {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        InspectionGroupMemberPO groupMemberPO1 = new InspectionGroupMemberPO();
//        InspectionGroupMemberPO groupMemberPO2 = new InspectionGroupMemberPO();
//
//        groupMemberPO1.setGroupMemberId(111L);
//        groupMemberPO2.setGroupMemberId(111L);
//        System.out.println(groupMemberPO1 == groupMemberPO2);
//        System.out.println(groupMemberPO1.hashCode() == groupMemberPO2.hashCode());
//        System.out.println(groupMemberPO1.equals(groupMemberPO2));
//        double d;
//        System.out.println(d);
//        String str = "0000000000|1800000000|1805000000";
//        str = str.replace("|", ",").replace("1805000000", "");
//        String[] strings = str.split(",");
//        Collections.reverse(Arrays.asList(strings));
//        String newStr = "";
//        for (String s : strings) {
//            newStr += s + ",";
//        }
//        System.out.println(newStr.substring(0,newStr.lastIndexOf(",")));
//        String fileUrl = "piis.file.com:81/upload/2020/10/20/授课2.txt";
//        BASE64Encoder encoder = new BASE64Encoder();
//        String text = "PIIS:Ceb12345";
//        byte[] bytes = text.getBytes("UTF-8");
//        System.out.println(encoder.encode(bytes));
        List<Student> students = new ArrayList<>();
        Student student1 = new Student();
        student1.setAge(11);
        student1.setName("11");

        Student student2 = new Student();
        student2.setAge(22);
        student2.setName("22");

        students.add(student1);
        students.add(student2);

//        List<Student> copyStudents = students;
        Student student3 = students.get(0);

        students.get(0).setAge(33);
        System.out.println("students" + students);
        System.out.println("student3" + student3);


    }


}


class Student {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}