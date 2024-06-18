import org.hibernate.internal.build.AllowSysOut;

class Student{
	private int id;
	private String name;
	public Student(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}

public class Test {
	public static void main(String args[]) {
//		m1();
		String s1 = "a";
		String s2 = "a";
		System.out.println(s1.hashCode() + " " + s2.hashCode());
		System.out.println(s1 == s2);
		
	}

	private static void m1() {
		Student s = new Student(1, "Naveen");
		Integer x = 100;
		System.out.println("hc "+ x.hashCode());
		System.out.println(x);
		m3(x);
		System.out.println(x);
		System.out.println(s.hashCode());
		System.out.println("from m1 : "+s);
		m2(s);
		System.out.println("from m1 : "+s);
		
	}

	private static void m3(Integer x) {
		System.out.println("hc "+x.hashCode());
		x = 10;
	}

	private static void m2(Student s) {
		System.out.println(s.hashCode());
		s.setName("Alok");
		System.out.println("from m2 : "+s);
	}
}
