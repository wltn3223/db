import java.util.Objects;

public class Member {
    private int memberId;

    private String id;
    private String name;
    private int age;
    private String phoneNum;

    public Member(int memberId, String id, String name, int age, String phoneNum) {
        this.memberId = memberId;
        this.id = id;
        this.name = name;
        this.age = age;
        this.phoneNum = phoneNum;

    }

    public int getMemberId() {
        return memberId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Member)){
            return false;
        }
        Member user = (Member) obj;
        return memberId == user.memberId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }

    public String toString(){
        return String.format("%-5d %-10s %-5s %-5d %-20s",memberId,id,name,age,phoneNum);
    }
}
