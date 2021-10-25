@Entity
public class PersonInfor {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String perNo;//人员编号

    private String name;//人员姓名

    private String sex;//人员姓名

    @Generated(hash = 1311768890)
    public PersonInfor(Long id, String perNo, String name, String sex) {
        this.id = id;
        this.perNo = perNo;
        this.name = name;
        this.sex = sex;
    }

    @Generated(hash = 1362534400)
    public PersonInfor() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerNo() {
        return this.perNo;
    }

    public void setPerNo(String perNo) {
        this.perNo = perNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
