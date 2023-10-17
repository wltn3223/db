import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static Connection makeConnection() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "hr";
        String password = "hr";
        Connection connection = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, id, password);

        } catch (ClassNotFoundException | SQLException e) {

        }

        return connection;
    }

    public static void main(String[] args) {
        boolean flag = false;

        while (!flag) {
            try {
                System.out.println("선택 1.create 2.select 3.insert 4.update 5.delete 6.dropTable  0. 종료 ");
                int num = Integer.parseInt(br.readLine());
                if (num > 6 || num < 0) {
                    System.out.println("올바른 숫자를 입력하세요");
                }
                switch (num) {
                    case 0:
                        flag = true;
                        break;
                    case 1:
                        Createtable();
                        break;
                    case 2:
                        selectMember();
                        break;
                    case 3:
                        insertMember();
                        break;
                    case 4:
                        updateMember();
                        break;
                    case 5:
                        DeleteMember();
                        break;
                    case 6:
                        Droptable();
                        break;
                }


            } catch (Exception e) {
                System.out.println("잘못입력하셨습니다");
            }

        }


    }

    public static void Droptable() {
        Connection con = makeConnection();
        try {
            Statement statement = con.createStatement();
            statement.executeQuery("drop table member");
            System.out.println("테이블이 삭제되었습니다.");
        } catch (SQLException e) {
            System.out.println("없는 table입니다.");
        }
    }


    public static void Createtable() {


        try {
            Connection con = makeConnection();
            Statement statement = con.createStatement();
            statement.execute(
                    "create table member(" +
                            "memberId number generated always as identity," +
                            "id varchar2(10) not null," +
                            "name varchar2(10) not null," +
                            "age number(3) not null," +
                            "phoneNum varchar2(16) not null," +
                            "constraint member_userId_pk primary key(memberId))"
            );
            System.out.println("테이블 생성완료");
        } catch (Exception e) {
            System.out.println("같은 테이블이 이미 존재합니다.");
        }


    }
    public static void selectMember() {
        Connection con = makeConnection();
        Statement statement = null;
        try {
            statement = con.createStatement();
            String sql = "select * from member order by memberId ";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int memberId = rs.getInt("memberId");
                String id = rs.getString("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String phoneNum = rs.getString("phoneNum");

                System.out.printf("%-5d %-10s %-5s %-5d %-20s\n", memberId, id, name, age, phoneNum);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
                statement.close();
            } catch (SQLException e) {

            }
        }


    }

    public static void insertMember() {

        try {
            Connection con = makeConnection();
            Statement statement = con.createStatement();
            System.out.printf("id 입력>>");
            String id = br.readLine();
            System.out.printf("이름 입력>>");
            String name = br.readLine();
            System.out.printf("나이 입력>>");
            int age = Integer.parseInt(br.readLine());
            System.out.printf("휴대폰번호 입력>>");
            String phoneNum = br.readLine();
            String data = String.format("insert into member(id,name,age,phoneNum) values('%s','%s','%d','%s') ",id,name,age,phoneNum);
            int count = statement.executeUpdate(data);
            if(!(count == 0)){
                System.out.println("삽입되었습니다.");
            } else {
                System.out.println("삽입실패");
            }
        } catch (Exception e) {
            System.out.println("잘못입력하셨습니다.");
        }
    }
    public static void updateMember() {
        selectMember();
        Member member = null;
        String id;
        String name;
        int memberId;
        int age = 0;
        try {
            Connection con = makeConnection();
            Statement statement = con.createStatement();
            System.out.printf("memberId 입력>>");
            memberId = Integer.parseInt(br.readLine());
            String data = String.format("select * from member where memberId = %d",memberId);
            ResultSet rs = statement.executeQuery(data);
            if(!(rs.next())){
                System.out.println("id를 잘못 입력하셨습니다.");
                return;
            }
                memberId = rs.getInt("memberId");
                id = rs.getString("id");
                name = rs.getString("name");
                 age = rs.getInt("age");
                String phoneNum = rs.getString("phoneNum");
                member = new Member(memberId,id,name,age,phoneNum);

            System.out.println(member);

            System.out.println("수정할 id 입력");
            id = br.readLine();
            if(id.isEmpty()) {
                id = member.getId();
            }
            System.out.println("수정할 이름 입력");
            name = br.readLine();
            if(name.isEmpty()) {
                name = member.getName();
            }
            System.out.println("수정할 나이 입력");
            data = br.readLine();
            if (data.isEmpty()){
                age = member.getAge();
            }else {
                age = Integer.parseInt(data);
            }
            System.out.println("수정할 휴대폰번호 입력");
            phoneNum = br.readLine();
            if(phoneNum.isEmpty()) {
                phoneNum = member.getPhoneNum();
            }
            data = String.format("update member set id = '%s',name = '%s',age = %d,phoneNum = '%s' where memberId = %d",id,name,age,phoneNum,member.getMemberId());
            int count = statement.executeUpdate(data);
            if(!(count == 0)){
                System.out.println("수정되었습니다.");
            } else {
                System.out.println("해당 id는 존재하지 않습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void DeleteMember(){
        try {
            selectMember();
            Connection con = makeConnection();
            Statement statement = con.createStatement();
            System.out.printf("삭제할 memberId 입력>>");
            int id = Integer.parseInt(br.readLine());
            String data = String.format("delete from member where memberId = %d",id);
            int count = statement.executeUpdate(data);
            if(!(count == 0)){
                System.out.println("삭제되었습니다.");
            } else {
                System.out.println("해당 id는 없습니다.");
            }
        } catch (IOException | SQLException e) {
            System.out.println("잘못입력하셨습니다.");
        }

    }

}